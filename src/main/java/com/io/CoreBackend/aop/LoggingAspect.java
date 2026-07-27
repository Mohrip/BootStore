package com.io.CoreBackend.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;



    @Aspect
    @Component
    public class LoggingAspect {

        private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

        // here I just exclude the AuthService to avoid security vulnerability in real world APPS
        @Pointcut("@within(org.springframework.stereotype.Service) "
                + "&& !within(com.io.CoreBackend.customer.service.AuthService)")
        public void serviceMethods() {
        }

        @AfterReturning(pointcut = "serviceMethods()", returning = "result")
        public void afterReturning(JoinPoint joinPoint, Object result) {
            String method = joinPoint.getSignature().toShortString();
            log.info("{} completed successfully. Returned payload: {}", method, result);
        }


        @AfterThrowing(pointcut = "serviceMethods()", throwing = "ex")
        public void afterThrowing(JoinPoint joinPoint, Exception ex) {
            String method = joinPoint.getSignature().toShortString();
            log.info("{} completed with exception: {}", method, ex.getMessage());
        }

        @After("serviceMethods()")
        public void logAfter(JoinPoint joinPoint) {
            String method = joinPoint.getSignature().toShortString();
            log.info("{} completed successfully", method);
        }


        @Around("serviceMethods()")
        public Object logExecution(ProceedingJoinPoint joinPoint) throws Throwable {
            long start = System.currentTimeMillis();
            String method = joinPoint.getSignature().toShortString();
            try {
                return joinPoint.proceed();
            } catch (Throwable ex) {
                log.warn("{} failed after {} ms: {}", method,
                        System.currentTimeMillis() - start, ex.getClass().getSimpleName());
                throw ex;
            } finally {
                log.debug("{} took {} ms", method, System.currentTimeMillis() - start);
            }
        }
    }
