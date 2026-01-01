package com.fmx.xiaomeng.modules.application.resolver;

import com.fmx.xiaomeng.common.utils.redis.RedisUtil;
import com.fmx.xiaomeng.modules.application.annotation.UserInfo;
import com.fmx.xiaomeng.modules.application.interceptor.AuthorizationInterceptor;
import com.fmx.xiaomeng.modules.application.repository.model.UserDO;
import com.fmx.xiaomeng.modules.application.service.UserService;
import com.fmx.xiaomeng.modules.application.service.convert.Converter;
import com.fmx.xiaomeng.modules.application.service.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 参数解析器：为标注了登录用户注入点的参数提供用户模型。
 * 原理说明：读取请求作用域中的用户标识，优先命中缓存，
 * 未命中则回源查询并回写缓存。
 */
@Component
public class LoginUserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {


    @Autowired
    private UserService userService;

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private Converter converter;


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(UserModel.class) && parameter.hasParameterAnnotation(UserInfo.class);
    }

    /**
     * 返回值应该就会存到@LoginUser 注解的入参
     *
     * @param parameter
     * @param container
     * @param request
     * @param factory
     * @return
     * @throws Exception
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer container,
                                  NativeWebRequest request, WebDataBinderFactory factory) {
        Long uid = (Long) request.getAttribute(AuthorizationInterceptor.USER_KEY, RequestAttributes.SCOPE_REQUEST);
        String cacheKey = "userId:" + uid;
        UserDO cached = redisUtil.get(cacheKey, UserDO.class);
        if (cached != null) {
            return converter.convert(cached);
        }
        UserModel model = userService.getUserInfo(uid);
        redisUtil.set(cacheKey, converter.convert(model), 7200);
        return model;
    }
}
