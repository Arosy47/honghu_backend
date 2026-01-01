package com.fmx.xiaomeng.modules.application.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fmx.xiaomeng.common.enums.AccountStatusEnum;
import com.fmx.xiaomeng.common.enums.AuthStatusEnum;
import com.fmx.xiaomeng.common.error.ErrorCodeEnum;
import com.fmx.xiaomeng.common.exception.BusinessException;
import com.fmx.xiaomeng.common.exception.ExceptionResolver;
import com.fmx.xiaomeng.common.utils.*;
import com.fmx.xiaomeng.common.utils.http.HttpClientUtil;
import com.fmx.xiaomeng.common.utils.redis.RedisUtil;
import com.fmx.xiaomeng.common.utils.redis.RedissonLockUtil;
import com.fmx.xiaomeng.common.validator.ValidationResult;
import com.fmx.xiaomeng.common.validator.ValidatorImpl;
import com.fmx.xiaomeng.modules.admin.request.UserPageQueryParam;
import com.fmx.xiaomeng.modules.application.model.Audience;
import com.fmx.xiaomeng.modules.application.model.WXProperties;
import com.fmx.xiaomeng.modules.application.model.WXSessionModel;
import com.fmx.xiaomeng.modules.application.repository.dao.*;
import com.fmx.xiaomeng.modules.application.repository.model.*;
import com.fmx.xiaomeng.modules.application.service.UserService;
import com.fmx.xiaomeng.modules.application.service.convert.Converter;
import com.fmx.xiaomeng.modules.application.service.model.OssFileModel;
import com.fmx.xiaomeng.modules.application.service.model.UserModel;
import com.fmx.xiaomeng.modules.application.utils.JwtUtils;
import lombok.CustomLog;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionUtils;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.fmx.xiaomeng.common.constant.GlobalConstants.initAnonymousAvatarList;
import static com.fmx.xiaomeng.common.constant.GlobalConstants.initAvatarList;
import static com.fmx.xiaomeng.common.error.ErrorCodeEnum.*;

/**
 * @Description
 * @Date 2025-12-20
 * @Author honghu
 */
@CustomLog
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    WXProperties wxProperties;
    @Autowired
    Audience audience;
    @Autowired
    private UserDOMapper userDAO;
    @Autowired
    private UserBlackDOMapper userBlackDOMapper;

    @Autowired
    private Converter converter;
    @Autowired
    private ValidatorImpl validator;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private EnvUtil envUtil;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private RedissonLockUtil redissonLockUtil;

    @Autowired
    private WxMaService wxService;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;

    private UserDOMapper userDOMapper;


    /**
     * 随机生成包含大小写字母及数字的字符串
     *
     * @param length
     * @return
     */

    public String getStringRandom(int length) {
        String val = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            if ("char".equalsIgnoreCase(charOrNum)) {
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (random.nextInt(26) + temp);
            } else if ("num".equalsIgnoreCase(charOrNum)) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WXSessionModel login(String code, UserModel userModel) throws BusinessException {

        String url = "https://api.weixin.qq.com/sns/jscode2session";
        Map<String, String> param = new HashMap<String, String>();
        param.put("appid", wxProperties.getWxId());
        param.put("secret", wxProperties.getWxSecret());
        param.put("js_code", code);
        param.put("grant_type", "authorization_code");
        String wxResult;
        wxResult = HttpClientUtil.doGet(url, param);

        JSONObject json = JSON.parseObject(wxResult);
        if (Objects.nonNull(json.get("errcode"))) {
            throw new BusinessException(HTTP_REQUEST_ERROR,
                    String.format("获取openId失败,errorCode:%s,errorMsg:%s", json.get("errcode"), json.get("errmsg")));
        }

        String openId = (String) json.get("openid");
        String unionId = (String) json.get("unionid");

        WXSessionModel wxSessionModel = new WXSessionModel();

        UserModel existModel = null;
        if (StringUtils.isNotBlank(unionId)) {
            existModel = this.queryByUnionId(unionId);
        }
        if (Objects.isNull(existModel)) {
            existModel = this.queryByOpenId(openId);
        }

        log.info("用户登录，openId:{}, unionId:{}, userModel:{}", openId, unionId, existModel);

        if (Objects.isNull(existModel)) {
            Long userId = this.generateUserId();
            while (true) {
                UserDO userDO = userDAO.selectByUserId(userId);
                if (Objects.isNull(userDO)) {
                    break;
                } else {
                    userId = this.generateUserId();
                }
            }

            UserModel newUser = new UserModel();
            newUser.setUserId(userId);
            newUser.setMotto("这个人很懒,什么也没有留下");
            newUser.setOpenId(openId);
            newUser.setUnionId(unionId);
            newUser.setCreateTime(new Date());
            newUser.setModifiedTime(new Date());
            newUser.setAuthStatus(AuthStatusEnum.UNAUTH);

            // newUser.setScore(0);

            newUser.setNickName("萌新_" + getStringRandom(6));

            newUser.setAnonymousName(RandomNameUtil.generateNickname());
            Random random = new Random();
            int randomIndex = random.nextInt(initAvatarList.length);
            newUser.setAvatar(new OssFileModel(null, initAvatarList[randomIndex]));

            Random random2 = new Random();
            int randomIndex2 = random2.nextInt(initAnonymousAvatarList.length);
            newUser.setAnonymousAvatar(new OssFileModel(null, initAnonymousAvatarList[randomIndex2]));

            this.createUser(newUser);

            wxSessionModel.setUserId(userId);
        } else {
            if (StringUtils.isNotBlank(unionId) && !unionId.equals(existModel.getUnionId())) {
                UserModel updateUser = new UserModel();
                updateUser.setUserId(existModel.getUserId());
                updateUser.setUnionId(unionId);
                userDAO.updateByUserId(converter.convert(updateUser));
            }

            if (StringUtils.isBlank(existModel.getOpenId())) {
                UserModel updateUser = new UserModel();
                updateUser.setUserId(existModel.getUserId());
                updateUser.setOpenId(openId);
                userDAO.updateByUserId(converter.convert(updateUser));
            }
            wxSessionModel.setUserId(existModel.getUserId());
        }

        Long expireTimeStamp = jwtUtils.getExpireTimeStamp();
        wxSessionModel.setExpire(expireTimeStamp);
        String token = jwtUtils.generateToken(wxSessionModel.getUserId());
        wxSessionModel.setToken(token);
        return wxSessionModel;
    }

    private Long generateUserId() {
        long tampstamp = System.currentTimeMillis() % 10000000;
        Random random = new Random();
        int randomValue = random.nextInt(10000000);
        tampstamp ^= randomValue;

        if (tampstamp > 10000000) {
            tampstamp %= 10000000;
        }

        if (tampstamp < 1000000) {
            tampstamp += (random.nextInt(9) + 1) * 1000000;
        }
        return tampstamp;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String certificatePhone(String code, UserModel userModel) throws BusinessException, WxErrorException {
        redisUtil.delete("userId:" + userModel.getUserId());

        WxMaPhoneNumberInfo phoneNoInfo = wxService.getUserService().getPhoneNoInfo(code);
        String phoneNumber = phoneNoInfo.getPhoneNumber();

        UserModel update = new UserModel();
        update.setUserId(userModel.getUserId());
        update.setPhone(phoneNumber);
        userDAO.updateByUserId(converter.convert(update));

        return phoneNumber;

    }


    @Override
    public UserModel getUserInfo(Long userId) {
        UserDO userInfo = redisUtil.get("userId:" + userId, UserDO.class);
        if (userInfo != null) {
            return converter.convert(userInfo);
        }
        UserDO userDO = userDAO.selectByUserId(userId);
        redisUtil.set("userId:" + userId, userDO, 7200);
        return converter.convert(userDO);
    }

    @Override
    public UserModel getAllUserInfo(Long userId) {
        UserModel userModel = this.getUserInfo(userId);
        return userModel;
    }

    @Override
    public UserModel queryByOpenId(String openId) {
        UserDO userDO = userDAO.selectByOpenId(openId);
        return converter.convert(userDO);
    }

    @Override
    public UserModel queryByUnionId(String unionId) {
        UserDO userDO = userDAO.selectByUnionId(unionId);
        return converter.convert(userDO);
    }

    /**
     * 分页查询用户
     *
     * @param params
     * @return
     */
    @Override
    public PageList<UserModel> pageQuery(UserPageQueryParam params) {
        long total = userDAO.count(params);
        @NotNull PageParam pageParam = params.getPageParam();
        List<UserModel> userModelList = null;
        if (total > pageParam.getOffset()) {
            List<UserDO> userDOList = userDAO.pageQuery(params);
            userModelList = converter.convertToUserList(userDOList);
        }
        return new PageList<>(userModelList, new Paginator(params.getPageParam(), total));
    }

        private void createUser(UserModel userModel) throws BusinessException {
        ValidationResult result = validator.validate(userModel);
        if (result.isHasErrors()) {
            throw new BusinessException(ErrorCodeEnum.PARAMETER_VALIDATION_ERROR, result.getErrMsg());
        }
        UserDO userDO = converter.convert(userModel);
        userDAO.insertSelective(userDO);

    }

    @Override
    public void changeUserInfo(UserModel userModel) {
        redisUtil.delete("userId:" + userModel.getUserId());
        userModel.setModifiedTime(new Date());

        userDAO.updateByUserId(converter.convert(userModel));

        if (Objects.nonNull(userModel.getAvatar()) || StringUtils.isNotBlank(userModel.getNickName())) {
            EventBus.emit(EventBus.Topic.CHANGE_USER_INFO, userModel);
        }

    }


    @Override
    public void addViolationTimes(Long userId) {
        userDAO.addViolationTimes(userId);
    }

    @Override
    public void authenticate(UserModel userModel) {

        redisUtil.delete("userId:" + userModel.getUserId());

        userDAO.updateByUserId(converter.convert(userModel));

    }



    @Override
    public void updateStudentAccount(UserModel userModel) {
        UserDO userDO = converter.convert(userModel);
        userDAO.updateByUserId(userDO);
        redisUtil.delete("userId:" + userModel.getUserId());

    }




    @Override
    public void banUser(Long userId) {
        UserModel userModel = new UserModel();
        redisUtil.delete("userId:" + userId);
        userModel.setUserId(userId);
        userModel.setModifiedTime(new Date());
        userModel.setAccountStatus(AccountStatusEnum.NO_EDIT);
        userDAO.updateByUserId(converter.convert(userModel));
    }

    @Override
    public void unbanUser(Long userId) {
        UserModel userModel = new UserModel();
        redisUtil.delete("userId:" + userId);
        userModel.setUserId(userId);
        userModel.setModifiedTime(new Date());
        userModel.setAccountStatus(AccountStatusEnum.NORMAL);
        userDAO.updateByUserId(converter.convert(userModel));
    }

    @Override
    public void blackUser(Long userId, Long blackedUserId) {
        if (userId.equals(blackedUserId)) {
            throw new BusinessException(ErrorCodeEnum.PARAMETER_VALIDATION_ERROR, "不能拉黑自己");
        }

        UserBlackDO exist = userBlackDOMapper.queryByUserIdAndBlackedUserId(userId, blackedUserId);
        if (exist != null && Boolean.TRUE.equals(exist.getValid())) {
            return;
        }

        if (Objects.isNull(exist)) {
            UserBlackDO blacklistDO = new UserBlackDO();
            blacklistDO.setUserId(userId);
            blacklistDO.setBlackedUserId(blackedUserId);
            blacklistDO.setCreateTime(new Date());
            blacklistDO.setModifiedTime(new Date());
            blacklistDO.setValid(true);
            userBlackDOMapper.insertSelective(blacklistDO);
        } else {
            UserBlackDO blacklistDO = new UserBlackDO();
            blacklistDO.setId(exist.getId());
            blacklistDO.setModifiedTime(new Date());
            blacklistDO.setValid(true);
            userBlackDOMapper.updateByPrimaryKeySelective(  blacklistDO);
        }


    }


    @Override
    public void unblackUser(Long userId, Long blackedUserId) {
        UserBlackDO exist = userBlackDOMapper.queryByUserIdAndBlackedUserId(userId, blackedUserId);
        if (Objects.isNull(exist) || Boolean.FALSE.equals(exist.getValid())) {
            return;
        }
        UserBlackDO blacklistDO = new UserBlackDO();
        blacklistDO.setId(exist.getId());
        blacklistDO.setModifiedTime(new Date());
        blacklistDO.setValid(false);
        userBlackDOMapper.updateByPrimaryKeySelective(blacklistDO);
    }


    @Override
    public Boolean isBlacked(Long userId, Long blackedUserId) {
        UserBlackDO blacklistDO = userBlackDOMapper.queryByUserIdAndBlackedUserId(userId, blackedUserId);
        return blacklistDO != null && Boolean.TRUE.equals(blacklistDO.getValid());
    }

    @Override
    public void setBlueV(Long userId, Boolean blueV) {
        userDOMapper.setBlueV(userId, blueV);
    }



    @Override public Integer indexDate(Integer days) { return 0; }

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UserServiceImpl.class);

    @Override public void initHistoryUserAnonymousAvatar() {}
}
