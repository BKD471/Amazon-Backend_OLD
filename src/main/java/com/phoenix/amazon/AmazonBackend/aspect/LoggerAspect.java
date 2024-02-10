package com.phoenix.amazon.AmazonBackend.aspect;

import com.phoenix.amazon.AmazonBackend.dto.PageableResponse;
import com.phoenix.amazon.AmazonBackend.dto.UserDto;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;

@Aspect
@Component
public class LoggerAspect {
    Logger logger = LoggerFactory.getLogger(LoggerAspect.class);

    @Around(value = "execution(com.phoenix.amazon.AmazonBackend.dto.UserDto   com.phoenix.amazon.AmazonBackend.services.*.*(..))")
    public UserDto logUserDtoType(final ProceedingJoinPoint joinPoint) throws Throwable {

        LocalTime localTime = LocalTime.now(Clock.system(ZoneId.of("Asia/Kolkata")));
        logger.info(String.format("<################# %s   ", localTime) + joinPoint.
                getSignature().
                toString() + " method executions starts ################################################################" +
                "#################################################>");
        Instant start = Instant.now();
        Object result = joinPoint.proceed();
        Instant end = Instant.now();
        long timeElapsedInMs = Duration.between(start, end).toMillis();
        logger.info(String.format("<################# Time elapsed to execute %s is %s Ms ##############################" +
                        "######################################################################################" +
                        "#########>",
                joinPoint.getSignature().toString(), timeElapsedInMs));

        localTime = LocalTime.now(Clock.system(ZoneId.of("Asia/Kolkata")));
        logger.info(String.format("<################# %s    ", localTime) + joinPoint.getSignature().toString() +
                " method execution " +
                "completed #############################################################################################" +
                "#################>");
        return (UserDto) result;
    }

    @Around(value = "execution(String   com.phoenix.amazon.AmazonBackend.services.*.*(..))")
    public String logStringType(final ProceedingJoinPoint joinPoint) throws Throwable {

        LocalTime localTime = LocalTime.now(Clock.system(ZoneId.of("Asia/Kolkata")));
        logger.info(String.format("<################# %s   ", localTime) + joinPoint.
                getSignature().
                toString() + " method executions starts ################################################################" +
                "#################################################>");
        Instant start = Instant.now();
        Object result = joinPoint.proceed();
        Instant end = Instant.now();
        long timeElapsedInMs = Duration.between(start, end).toMillis();
        logger.info(String.format("<################# Time elapsed to execute %s is %s Ms ##############################" +
                        "######################################################################################" +
                        "#########>",
                joinPoint.getSignature().toString(), timeElapsedInMs));

        localTime = LocalTime.now(Clock.system(ZoneId.of("Asia/Kolkata")));
        logger.info(String.format("<################# %s    ", localTime) + joinPoint.getSignature().toString() +
                " method execution " +
                "completed #############################################################################################" +
                "#################>");
        return (String) result;
    }

    @Around(value = "execution(com.phoenix.amazon.AmazonBackend.dto.PageableResponse   com.phoenix.amazon.AmazonBackend.services.*.*(..))")
    public PageableResponse<UserDto> logPageableResponseType(final ProceedingJoinPoint joinPoint) throws Throwable {

        LocalTime localTime = LocalTime.now(Clock.system(ZoneId.of("Asia/Kolkata")));
        logger.info(String.format("<################# %s   ", localTime) + joinPoint.
                getSignature().
                toString() + " method executions starts ################################################################" +
                "#################################################>");
        Instant start = Instant.now();
        Object result = joinPoint.proceed();
        Instant end = Instant.now();
        long timeElapsedInMs = Duration.between(start, end).toMillis();
        logger.info(String.format("<################# Time elapsed to execute %s is %s Ms ##############################" +
                        "######################################################################################" +
                        "#########>",
                joinPoint.getSignature().toString(), timeElapsedInMs));

        localTime = LocalTime.now(Clock.system(ZoneId.of("Asia/Kolkata")));
        logger.info(String.format("<################# %s    ", localTime) + joinPoint.getSignature().toString() +
                " method execution " +
                "completed #############################################################################################" +
                "#################>");
        return (PageableResponse<UserDto>) result;
    }

    @Around(value = "execution(*   com.phoenix.amazon.AmazonBackend.services.*.*(..))")
    public Object logObjectType(final ProceedingJoinPoint joinPoint) throws Throwable {

        LocalTime localTime = LocalTime.now(Clock.system(ZoneId.of("Asia/Kolkata")));
        logger.info(String.format("<################# %s   ", localTime) + joinPoint.
                getSignature().
                toString() + " method executions starts ################################################################" +
                "#################################################>");
        Instant start = Instant.now();
        Object result = joinPoint.proceed();
        Instant end = Instant.now();
        long timeElapsedInMs = Duration.between(start, end).toMillis();
        logger.info(String.format("<################# Time elapsed to execute %s is %s Ms ##############################" +
                        "######################################################################################" +
                        "#########>",
                joinPoint.getSignature().toString(), timeElapsedInMs));

        localTime = LocalTime.now(Clock.system(ZoneId.of("Asia/Kolkata")));
        logger.info(String.format("<################# %s    ", localTime) + joinPoint.getSignature().toString() +
                " method execution " +
                "completed #############################################################################################" +
                "#################>");
        return result;
    }

    @Around(value = "execution(java.io.InputStream  com.phoenix.amazon.AmazonBackend.services.*.*(..))")
    public InputStream logInputStreamType(final ProceedingJoinPoint joinPoint) throws Throwable {

        LocalTime localTime = LocalTime.now(Clock.system(ZoneId.of("Asia/Kolkata")));
        logger.info(String.format("<################# %s   ", localTime) + joinPoint.
                getSignature().
                toString() + " method executions starts ################################################################" +
                "#################################################>");
        Instant start = Instant.now();
        Object result = joinPoint.proceed();
        Instant end = Instant.now();
        long timeElapsedInMs = Duration.between(start, end).toMillis();
        logger.info(String.format("<################# Time elapsed to execute %s is %s Ms ##############################" +
                        "######################################################################################" +
                        "#########>",
                joinPoint.getSignature().toString(), timeElapsedInMs));

        localTime = LocalTime.now(Clock.system(ZoneId.of("Asia/Kolkata")));
        logger.info(String.format("<################# %s    ", localTime) + joinPoint.getSignature().toString() +
                " method execution " +
                "completed #############################################################################################" +
                "#################>");
        return (InputStream) result;
    }
}
