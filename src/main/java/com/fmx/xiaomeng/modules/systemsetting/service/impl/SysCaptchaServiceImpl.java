
package com.fmx.xiaomeng.modules.systemsetting.service.impl;


import com.fmx.xiaomeng.common.exception.BusinessException;
import com.fmx.xiaomeng.common.error.ErrorCodeEnum;
import com.fmx.xiaomeng.common.utils.DateUtil;
import com.fmx.xiaomeng.modules.admin.repository.dao.AdminCaptchaDOMapper;
import com.fmx.xiaomeng.modules.admin.repository.model.AdminCaptchaDO;
import com.fmx.xiaomeng.modules.systemsetting.entity.SysCaptchaEntity;
import com.fmx.xiaomeng.modules.systemsetting.service.SysCaptchaService;
import com.google.code.kaptcha.Producer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.Objects;

/**
 * 验证码
 *
 * @author honghu
 * @date 2025-12-20
 */
@Service("sysCaptchaService")
public class SysCaptchaServiceImpl implements SysCaptchaService {
    @Autowired
    private Producer producer;

    @Autowired
    private AdminCaptchaDOMapper captchaDOMapper;
    @Override
    public BufferedImage getCaptcha(String uuid) {
        if(StringUtils.isBlank(uuid)){
            throw new BusinessException(ErrorCodeEnum.PARAMETER_VALIDATION_ERROR, "uuid不能为空");
        }
        String code = producer.createText();

        AdminCaptchaDO exist = captchaDOMapper.selectByPrimaryKey(uuid);
        if(Objects.isNull(exist)){
            SysCaptchaEntity captchaEntity = new SysCaptchaEntity();
            captchaEntity.setUuid(uuid);
            captchaEntity.setCode(code);
            captchaEntity.setExpireTime(DateUtil.addDateMinutes(new Date(), 5));
            AdminCaptchaDO adminCaptchaDO = new AdminCaptchaDO();
            BeanUtils.copyProperties(captchaEntity, adminCaptchaDO);
            captchaDOMapper.insertSelective(adminCaptchaDO);
        }else {
            SysCaptchaEntity captchaEntity = new SysCaptchaEntity();
            captchaEntity.setUuid(uuid);
            captchaEntity.setCode(code);
            captchaEntity.setExpireTime(DateUtil.addDateMinutes(new Date(), 5));
            AdminCaptchaDO adminCaptchaDO = new AdminCaptchaDO();
            BeanUtils.copyProperties(captchaEntity, adminCaptchaDO);
            captchaDOMapper.updateByPrimaryKeySelective(adminCaptchaDO);
        }

        return producer.createImage(code);
    }

    @Override
    public boolean validate(String uuid, String code) {
        AdminCaptchaDO adminCaptchaDO = captchaDOMapper.selectByPrimaryKey(uuid);
        if(adminCaptchaDO == null){
            return false;
        }
        SysCaptchaEntity captchaEntity = new SysCaptchaEntity();
        BeanUtils.copyProperties(adminCaptchaDO, captchaEntity);


        captchaDOMapper.deleteByPrimaryKey(uuid);

        return captchaEntity.getCode().equalsIgnoreCase(code) && captchaEntity.getExpireTime().getTime() >= System.currentTimeMillis();
    }
}
