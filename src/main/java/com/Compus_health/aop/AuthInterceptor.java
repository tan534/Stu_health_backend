package com.Compus_health.aop;

import com.Compus_health.annotation.AuthCheck;
import com.Compus_health.common.ErrorCode;
import com.Compus_health.constant.UserConstant;
import com.Compus_health.exception.BusinessException;
import com.Compus_health.model.entity.User;
import com.Compus_health.service.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 鉴权拦截器
 *
 */
@Aspect
@Component
public class AuthInterceptor {

    @Resource
    private UserService userService;

    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        int mustRole = authCheck.mustRole();
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        User loginUser = userService.getLoginUser(request);

        // 检查是否被封禁
        if (loginUser.getStatus() != null && loginUser.getStatus() == UserConstant.STATUS_BAN) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        // 需要管理员权限
        if (mustRole == UserConstant.ADMIN_ROLE) {
            if (loginUser.getRole() == null || loginUser.getRole() != UserConstant.ADMIN_ROLE) {
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
            }
        }

        return joinPoint.proceed();
    }
}
