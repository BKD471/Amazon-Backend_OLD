package com.phoenix.amazon.AmazonBackend.aspect;

import com.phoenix.amazon.AmazonBackend.dto.UserDto;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

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
    public UserDto log(final ProceedingJoinPoint joinPoint) throws Throwable {

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
                        "##########################################################################################>",
                joinPoint.getSignature().toString(), timeElapsedInMs));

        localTime = LocalTime.now(Clock.system(ZoneId.of("Asia/Kolkata")));
        logger.info(String.format("<################# %s    ", localTime) + joinPoint.getSignature().toString() +
                " method execution " +
                "completed ############################################################################################>");
        return (UserDto) result;
    }
}
