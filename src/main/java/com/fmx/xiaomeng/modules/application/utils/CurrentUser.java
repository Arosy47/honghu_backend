package com.fmx.xiaomeng.modules.application.utils;

import com.fmx.xiaomeng.common.exception.BusinessException;
import com.fmx.xiaomeng.common.error.ErrorCodeEnum;
import com.fmx.xiaomeng.common.utils.redis.RedisUtil;
import com.fmx.xiaomeng.modules.application.repository.model.UserDO;
import com.fmx.xiaomeng.modules.application.service.UserService;
import com.fmx.xiaomeng.modules.application.service.convert.Converter;
import com.fmx.xiaomeng.modules.application.service.model.UserModel;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 提供当前请求上下文中的登录用户获取能力。
 * 原理说明：从请求头读取令牌，解析后得到用户标识，
 * 优先从缓存命中用户模型，未命中则回源至服务层并刷新缓存。
 */
@Component
public class CurrentUser {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private RedisUtil redisUtils;
    @Autowired
    private UserService userService;
    @Autowired
    private Converter converter;


    public UserModel getUser(){
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(attrs).getRequest();
        String token = request.getHeader(jwtUtils.getHeader());
        if (StringUtils.isBlank(token)) {
            token = request.getParameter(jwtUtils.getHeader());
        }
        if (StringUtils.isBlank(token)) {
            return null;
        }
        Claims claims = jwtUtils.getClaimByToken(token);
        if (claims == null) {
            throw new BusinessException(ErrorCodeEnum.NOT_LOGIN, "请登录");
        }
        long userId = Long.parseLong(claims.getSubject());
        String cacheKey = "userId:" + userId;
        UserDO cached = redisUtils.get(cacheKey, UserDO.class);
        if (cached != null) {
            return converter.convert(cached);
        }
        UserModel fetched = userService.getUserInfo(userId);
        redisUtils.set(cacheKey, converter.convert(fetched), 7200);
        return fetched;
    }
}
