package com.vipin.product.aspect;

import com.vipin.product.annotation.RequiresRole;
import com.vipin.product.exception.AuthorizationException;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class RoleAuthorizationAspect {
    @Before("@annotation(requiresRole)")
    public void authorize(RequiresRole requiresRole) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String role = request.getHeader("role");
        for (String allowedRole : requiresRole.value()) {
            if (allowedRole.equals(role)) {
                return;
            }
        }
        throw new AuthorizationException("Access denied. Required role: " + requiresRole.value());
    }

}
