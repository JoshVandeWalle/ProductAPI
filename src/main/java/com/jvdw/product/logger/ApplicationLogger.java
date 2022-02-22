package com.jvdw.product.logger;

import com.jvdw.product.business.ProductBusinessResponse;
import com.jvdw.product.model.ProductModel;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

/**
 * The ApplicationLogger is an interceptor logger implemented with AOP
 * Will intercept all API Service (REST Controller), Business Service, and Repository method calls
 * covering happy paths (2xx), warning paths (4xx), and exception paths (5xx)
 * WARNING do not call Spring Bean method in other methods of the same bean doing so
 * only log calls to the first method because of th way AOP proxies are implemented
 */
@Configuration
@Aspect
@Slf4j
public class ApplicationLogger
{
    /**
     * This advice method execute before entering an API service, Business service, or Repository method
     * @param joinPoint the execution instance of the advice for the pointcut
     */
    @Before("execution(* com.jvdw.product.api.*.*(..)) || execution(* com.jvdw.product.business.*.*(..)) || execution(* com.jvdw.product.data.repository.*.*(..))")
    public void before(JoinPoint joinPoint)
    {
        // read method signature for inclusion in logs
        String signature = joinPoint.getSignature().toString();
        // log method entry
        log.info("Entering " + signature.substring(signature.indexOf(" ") + 1));
    }

    /**
     * This advice method execute after a return statement in an API service, Business service, or Repository method
     * @param joinPoint the execution instance of the advice for the pointcut
     * @param result the return value
     */
    @AfterReturning(value = "execution(* com.jvdw.product.api.*.*(..)) || execution(* com.jvdw.product.business.*.*(..)) || execution(* com.jvdw.product.data.repository.*.*(..))", returning = "result")
    public void afterReturning(JoinPoint joinPoint, Object result)
    {
        // read method signature for inclusion in logs
        String signature = joinPoint.getSignature().toString();

        // if the method return value is on a happy path
        if ((result instanceof ResponseEntity && ((ResponseEntity<?>) result).getStatusCode().is2xxSuccessful()) || result instanceof ProductModel || result instanceof List || (result instanceof ProductBusinessResponse && result != ProductBusinessResponse.NOT_FOUND) || (result instanceof Optional && ((Optional<?>) result).isPresent()))
        {
            // log the happy path execution
            log.info("Exiting " + signature.substring(signature.indexOf(" ") + 1) + " after successful execution");
        }

        else
        {
            // calculate outcome
            String outcome = result == null ? "null" : result.getClass().toString();
            // log the warning path return value
            log.warn("Exiting " + signature.substring(signature.indexOf(" ") + 1) + " with " + outcome);
        }
    }

    /**
     * This advice method execute after an exception is thrown in an API service, Business service, or Repository method
     * @param joinPoint joinPoint the execution instance of the advice for the pointcut
     * @param e the exception thrown
     */
    @AfterThrowing(value = "execution(* com.jvdw.product.api.*.*(..)) || execution(* com.jvdw.product.business.*.*(..)) || execution(* com.jvdw.product.data.repository.*.*(..))", throwing = "e")
    public void afterThrowing(JoinPoint joinPoint, Exception e)
    {
        // read method signature for inclusion in logs
        String signature = joinPoint.getSignature().toString();
        //log the exception
        log.error("Exiting " + signature.substring(signature.indexOf(" ") + 1) + " with exception of type: " + e.getClass() + " and message: " + e.getMessage());
    }
}
