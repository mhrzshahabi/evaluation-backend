package com.nicico.evaluation.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class EvaluationHandleException {

    public EvaluationHandleException() {
        new ApiError(HttpStatus.BAD_REQUEST); //UnKnown
    }

    public EvaluationHandleException(HttpStatus httpStatus) {
        new ApiError(httpStatus);
    }

//    public EvaluationHandleException(Exception innerException) {
//        this(innerException, ErrorType.Unknown, "", innerException.getMessage());
//    }

    public EvaluationHandleException(HttpStatus httpStatus, String message) {
        new ApiError(httpStatus, message);
    }

    public EvaluationHandleException(HttpStatus httpStatus, String message, Exception innerException) {
        new ApiError(httpStatus, message, innerException);
    }
}