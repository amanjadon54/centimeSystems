package com.centime.util.aspect;

import com.centime.util.LogUtils;
import com.centime.util.LoggingLevel;
import com.centime.util.exception.CustomRuntimeException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.StringEscapeUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

@Aspect
public class LoggerAspect {

    @Value("${log.api.response:true}")
    private boolean logApiResponse;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final int MAX_EXCEPTION_STACK_LINE = 6;

    @Around("execution(* com.centime.hello.controller.*.*(..)))")
    public Object logHelloController(ProceedingJoinPoint joinPoint) throws Throwable {
        return handleControllerInvocation(joinPoint);
    }

    @Around("execution(* com.centime.concatenaton.controller.*.*(..))")
    public Object logConcatenationController(ProceedingJoinPoint joinPoint) throws Throwable {
        return handleControllerInvocation(joinPoint);
    }

    //This will log all the methods which are throwing any exception
    @AfterThrowing(value = "execution(* com.centime.hello.*.*.*(..))", throwing = "e")
    public void allMethodException(JoinPoint joinPoint, Exception e) {
        handleAllException(joinPoint, e);
    }

    private void handleAllException(JoinPoint joinPoint, Exception e) {
        List<String> paramNames =
                new ArrayList<>(Arrays.asList(((CodeSignature) joinPoint.getSignature()).getParameterNames()));
        List<Object> args = new ArrayList<>(Arrays.asList(joinPoint.getArgs()));

        paramNames.add(LogUtils.exceptionMap);
        args.add(getExceptionMap(e));

        paramNames.add(LogUtils.handler);
        args.add("allMethodException");

        logParams(paramNames, args, LoggingLevel.ERROR);
    }

    private Object handleControllerInvocation(ProceedingJoinPoint joinPoint) throws Throwable {

        long startTime = new Date().getTime();
        //get params and their values
        List<String> paramsNamesList =
                new ArrayList<>(Arrays.asList(((CodeSignature) joinPoint.getSignature()).getParameterNames()));
        Object[] argsArray = joinPoint.getArgs();

        //assign logId to the last param name
        String logId = LogUtils.getLogId();
        if (!CollectionUtils.isEmpty(paramsNamesList) && paramsNamesList.get(paramsNamesList.size() - 1)
                .equalsIgnoreCase(LogUtils.logId)) {
            argsArray[argsArray.length - 1] = logId;
        }

        List<Object> paramsValuesList = new ArrayList<>(Arrays.asList(argsArray));

        //assign method Name
        paramsNamesList.add(LogUtils.methodName);
        paramsValuesList.add(joinPoint.getSignature().getName());

        //printing Request logs
        logParams(paramsNamesList, paramsValuesList, LoggingLevel.ERROR);
        try {
            //removing header logs
            paramsValuesList.remove(0);
            paramsNamesList.remove(0);

            //method proceeds
            Object response = joinPoint.proceed(argsArray);
            long endTime = new Date().getTime();
            if (logApiResponse) {
                //assuming request header is always the first object

                paramsNamesList.add(LogUtils.response);
                paramsValuesList.add(response);
            }

            paramsNamesList.add(LogUtils.runtimeMillis);
            paramsValuesList.add(endTime - startTime);

            logParams(paramsNamesList, paramsValuesList, LoggingLevel.INFO);

            return response;
        } catch (Exception e) {

            long endTime = new Date().getTime();
            Map<String, String> exceptionMap = getExceptionMap(e);

            paramsNamesList.add(LogUtils.runtimeMillis);
            paramsValuesList.add(endTime - startTime);

            paramsNamesList.add(LogUtils.exceptionMap);
            paramsValuesList.add(exceptionMap);

            paramsNamesList.add(LogUtils.handler);
            paramsValuesList.add("handleControllerInvocation");

            logParams(paramsNamesList, paramsValuesList, LoggingLevel.ERROR);
            throw e;
        }
    }

    private Map<String, String> getExceptionMap(Exception e) {
        Map<String, String> exceptionMap = new HashedMap<>();
        String stackLog = getSomeStack(e);
        Throwable t = e.getCause();
        exceptionMap.put("class", e.getClass().toString());
        exceptionMap.put("message", e.getMessage());
        exceptionMap.put("stacktrace", StringEscapeUtils.escapeJava(stackLog));
        exceptionMap.put("cause", t == null ? null : t.getMessage());
        if (e instanceof CustomRuntimeException) {
            String developerMessage = ((CustomRuntimeException) e).getDeveloperMsg();
            developerMessage = developerMessage == null ? "" : developerMessage;
            developerMessage = developerMessage.substring(0, Math.min(developerMessage.length(), 512));
            exceptionMap.put("developerMsg", developerMessage);
        }
        return exceptionMap;
    }

    private String getSomeStack(Exception e) {
        StackTraceElement[] stack = e.getStackTrace();
        StringBuilder sbStack = new StringBuilder();
        for (int i = 0; i < MAX_EXCEPTION_STACK_LINE; i++) {
            if (stack[i] == null)
                break;
            sbStack.append(stack[i]);
            sbStack.append("    ");
        }
        return sbStack.toString();
    }

    public void logParams(List<String> paramNames, List<Object> paramValues, LoggingLevel level) {
        switch (level) {
            case INFO:
                logger.info(LogUtils.getFormattedLog(paramNames, paramValues));
                break;
            case DEBUG:
                logger.debug(LogUtils.getFormattedLog(paramNames, paramValues));
                break;
            case ERROR:
                logger.error(LogUtils.getFormattedLog(paramNames, paramValues));
                break;
            case WARNING:
                logger.warn(LogUtils.getFormattedLog(paramNames, paramValues));
                break;
        }
    }
}
