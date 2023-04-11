package com.nicico.evaluation.exception;

import com.nicico.copper.common.IErrorCode;
import com.nicico.copper.common.NICICOException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.servlet.HandlerInterceptor;

@Getter
public class EvaluationHandleException extends NICICOException implements HandlerInterceptor {

    @Getter
    @Setter(AccessLevel.PRIVATE)
    private String msg;

    public EvaluationHandleException(IErrorCode errorCode) {
        super(errorCode);
    }

    // ------------------------------

    public EvaluationHandleException(ErrorType errorCode) {
        this(errorCode, null);
    }

    public EvaluationHandleException(ErrorType errorCode, String field) {
        super(errorCode, field);
    }

    public EvaluationHandleException(ErrorType errorCode, String field, String msg) {

        super(errorCode, field);
        setMsg(msg);
    }

    @Getter
    @RequiredArgsConstructor
    public enum ErrorType implements IErrorCode {

        NotFound(404),
        NotEditable(404),
        NotSave(404),
        DuplicateRecord(404),
        NotDeletable(404),
        Unknown(500),
        Unauthorized(401),
        Forbidden(403),
        RecordAlreadyExists(405),
        conflict(409),
        conflictForManager(405),
        NeedsAssessmentNotFound(404),
        NeedsAssessmentDomainNotFound(404),
        NeedsAssessmentPriorityNotFound(404),
        UpdatingInvalidOldVersion(400),
        EvaluationNotFound(404),
        EvaluationAnswerNotFound(404),
        EvaluationDeadline(406),
        ProvinceNotFound(404),
        InvalidData(405),
        IntegrityConstraint(403),
        GradeIsInAnotherGroup(403);

        private final Integer httpStatusCode;

        @Override
        public String getName() {
            return name();
        }
    }
}
