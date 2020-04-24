package com.centime.util.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthenticationRequestInterceptor extends HandlerInterceptorAdapter {

    @Value("${authorization.token:abcdefgh}")
    String staticToken;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws NumberFormatException {
        String token = request.getHeader("access-token");
        if (token == null || token.equals("")) {
            logger.info("Authentication failed as no access token passed");
            return false;
        }
        boolean isAuth = false;
        if (token.equals(staticToken))
            isAuth = true;
        if (isAuth) {
            return true;
        } else {
            logger.info("Authentication failed");
            throw new RuntimeException("Error " + HttpStatus.FORBIDDEN);
        }
    }
}
