package com.nicico.evaluation.exception;


import com.nicico.evaluation.enums.ErrorType;
import lombok.Getter;

@Getter
public class EvaluationHandleException extends BaseException {

    public EvaluationHandleException() {
        this(ErrorType.Unknown);
    }

    public EvaluationHandleException(ErrorType errorType) {
        this(errorType, null);
    }

    public EvaluationHandleException(Exception innerException) {
        this(innerException, ErrorType.Unknown, "", innerException.getMessage());
    }

    public EvaluationHandleException(ErrorType errorType, String field) {
        this(errorType, field, null);
    }

    public EvaluationHandleException(ErrorType errorType, String field, String message) {
        this(null, errorType, field, message);
    }

    public EvaluationHandleException(Exception innerException, ErrorType errorType, String field, String message) {

        super(innerException);
        this.response = new ErrorResponse(errorType, field, message);
    }

}