package com.rgmb.generator.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AspectLogger {

    private static final Logger logger = LogManager.getLogger(AspectLogger.class);

   /* @Pointcut("execution (public * com.rgmb.generator.service.BookService.getRandomBook(..))")
    public void logGetRandomBook(){}

    @Pointcut("execution (public * com.rgmb.generator.service.BookService.getRandomBooks(..))")
    public void logGetRandomBooks(){}

    @Before("logGetRandomBook()")
    public void logBeforeGetRandomBook(){
        logger.info("Start method getRandomBook from BookService");
    }

    @AfterReturning("logGetRandomBook()")
    public void logAfterReturningGetRandomBook(){
        logger.info("Successfully worked out the method getRandomBook from BookService");
    }

    @AfterThrowing("logGetRandomBook()")
    public void logThrowingReturningGetRandomBook(){
        logger.error("Method getRandomBook from BookService error received");
    }

    @Before("logGetRandomBooks()")
    public void logBeforeGetRandomBooks(){
        logger.info("Start method getRandomBooks from BookService");
    }

    @AfterReturning("logGetRandomBooks()")
    public void logAfterReturningGetRandomBooks(){
        logger.info("Successfully worked out the method getRandomBooks from BookService");
    }

    @AfterThrowing("logGetRandomBooks()")
    public void logThrowingReturningGetRandomBooks() {
        logger.error("Method getRandomBooks from BookService error received");
    }*/


}
