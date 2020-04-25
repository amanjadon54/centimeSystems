package com.centime.util.interceptor;

import com.centime.util.exception.CustomRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.centime.util.constants.StringConstants.ACCESS_TOKEN;
import static com.centime.util.constants.StringConstants.X_REQUEST_ID;

public class AuthenticationRequestInterceptor extends HandlerInterceptorAdapter {

    @Value("${authorization.token:abcdefgh}")
    String staticToken;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws NumberFormatException {
        String token = request.getHeader(ACCESS_TOKEN);
        if (token == null || token.equals("")) {
            logger.info("Authentication failed as no access token passed");
            throw new CustomRuntimeException("Access Denied", 401, request.getHeader(X_REQUEST_ID));
        }
        if (token.equals(staticToken)) {
            return true;
        } else {
            logger.info("Authentication failed");
            throw new CustomRuntimeException("Not Authorized", 403, request.getHeader(X_REQUEST_ID));
        }
    }
}
