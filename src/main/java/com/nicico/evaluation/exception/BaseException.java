package com.nicico.evaluation.exception;

import com.nicico.evaluation.utility.SpringContext;
import lombok.Getter;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.HandlerInterceptor;

@Getter
public abstract class BaseException extends RuntimeException implements HandlerInterceptor {

    protected ErrorResponse response;
    protected ResourceBundleMessageSource messageSource;

    BaseException() {
        this(null);
    }

    BaseException(Exception innerException) {
        super(innerException);
        this.messageSource = SpringContext.getBean(ResourceBundleMessageSource.class);
    }
}
