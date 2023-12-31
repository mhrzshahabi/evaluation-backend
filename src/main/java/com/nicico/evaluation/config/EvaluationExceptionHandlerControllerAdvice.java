package com.nicico.evaluation.config;

import com.nicico.copper.common.AbstractExceptionHandlerControllerAdvice;
import com.nicico.copper.common.IErrorCode;
import com.nicico.copper.common.dto.ErrorResponseDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import oracle.jdbc.OracleDatabaseException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLTransactionRollbackException;
import java.util.*;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class EvaluationExceptionHandlerControllerAdvice extends AbstractExceptionHandlerControllerAdvice {

    private final ResourceBundleMessageSource messageSource;

    @Override
    protected Map<String, ErrorResponseDTO.ErrorFieldDTO> getUniqueConstraintErrors() {

        return new HashMap<>();
    }

    @Override
    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException exception) {

        final Locale locale = LocaleContextHolder.getLocale();
        log.error("DataIntegrityViolationException", exception);
        OracleDatabaseException oracleDatabaseException = getOracleException(exception);
        if (oracleDatabaseException != null) {

            if (oracleDatabaseException.getOracleErrorNumber() == 2292 || oracleDatabaseException.getOracleErrorNumber() == 1407) {

                String consoleMessage = oracleDatabaseException.getSql() + System.getProperty("line.separator") + oracleDatabaseException.getMessage();
                return error(EvaluationHandleException.ErrorType.Forbidden, null, messageSource.getMessage("exception.foreign-key.constraint", null, locale), consoleMessage);
            }
            if (oracleDatabaseException.getOracleErrorNumber() == 1) {

                String consoleMessage = oracleDatabaseException.getSql() + System.getProperty("line.separator") + oracleDatabaseException.getMessage();
                return error(EvaluationHandleException.ErrorType.DuplicateRecord, null, messageSource.getMessage("exception.duplicate.information", null, locale), consoleMessage);
            }
        }

        return error(EvaluationHandleException.ErrorType.Forbidden, null, messageSource.getMessage("exception.unknown.constraint", null, locale), exception.toString());
    }

    @ExceptionHandler({EvaluationHandleException.class})
    public ResponseEntity<Object> handleEvaluationException(EvaluationHandleException exception) {

        log.error("EvaluationException", exception);
        return error(exception.getErrorCode(), exception.getField(), exception.getMessage(), "managed error occur in rest api.");
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleException(Exception exception) {

        log.error("Error is from : EvaluationExceptionHandlerController class, handleException", exception);
        final Locale locale = LocaleContextHolder.getLocale();
        OracleDatabaseException oracleDatabaseException = getOracleException(exception);
        if (oracleDatabaseException != null) {

            String consoleMessage = oracleDatabaseException.getSql() + System.getProperty("line.separator") + oracleDatabaseException.getMessage();

            if (oracleDatabaseException.getOracleErrorNumber() == 4063) {

                return error(EvaluationHandleException.ErrorType.Forbidden, null, messageSource.getMessage("exception.entity.not-found", null, locale), consoleMessage);
            } else if (oracleDatabaseException.getOracleErrorNumber() == 6575) {

                return error(EvaluationHandleException.ErrorType.Forbidden, null, messageSource.getMessage("exception.function.not-found", null, locale), consoleMessage);
            }
        }
        SQLTransactionRollbackException transactionRollbackException = getTransactionException(exception);
        if (transactionRollbackException != null) {

            if (transactionRollbackException.getErrorCode() == 2091)
                return error(EvaluationHandleException.ErrorType.Forbidden, null, messageSource.getMessage("exception.unknown.constraint", null, locale), transactionRollbackException.getMessage());
        }
        NumberFormatException numberFormatException = getNumberFormatException(exception);
        if (numberFormatException != null) {
            return error(EvaluationHandleException.ErrorType.Forbidden, null, messageSource.getMessage("exception.wrong.data.format", null, locale), numberFormatException.getMessage());
        }

        return error(EvaluationHandleException.ErrorType.Forbidden, null, messageSource.getMessage("exception.un-managed", null, locale), exception.getMessage());
    }

    private NumberFormatException getNumberFormatException(Throwable exception) {

        List<Throwable> innerExceptions = getInnerExceptions(exception, NumberFormatException.class);
        if (innerExceptions.size() > 0)
            try {
                return (NumberFormatException) innerExceptions.get(innerExceptions.size() - 1);
            } catch (Exception ex) {
                return null;
            }
        return null;
    }

    private OracleDatabaseException getOracleException(Throwable exception) {

        List<Throwable> innerExceptions = getInnerExceptions(exception, OracleDatabaseException.class);
        if (innerExceptions.size() > 0)
            try {
                return (OracleDatabaseException) innerExceptions.get(innerExceptions.size() - 1);
            } catch (Exception ex) {
                return null;
            }

        return null;
    }

    private SQLTransactionRollbackException getTransactionException(Throwable exception) {

        List<Throwable> innerExceptions = getInnerExceptions(exception, SQLTransactionRollbackException.class);
        if (innerExceptions.size() > 0)
            try {
                return (SQLTransactionRollbackException) innerExceptions.get(innerExceptions.size() - 1);
            } catch (Exception ex) {
                return null;
            }

        return null;
    }

    private List<Throwable> getInnerExceptions(Throwable ex, Class targetExceptionClass) {

        Throwable throwable = ex.getCause();
        List<Throwable> result = new ArrayList<>();
        while (throwable != null) {

            result.add(throwable);
            if (targetExceptionClass != null && throwable.getClass() == targetExceptionClass)
                break;
            throwable = throwable.getCause();
        }

        return result;
    }

    private ResponseEntity<Object> error(IErrorCode code, String field, String message, String exceptionMessage) {

        final String fieldVal = field;
        final IErrorCode codeVal = code;
        final String messageVal = message;
        Object error = new Object() {
            public final String field = fieldVal;
            public final IErrorCode code = codeVal;
            public final String message = messageVal;
        };
        Object response = new Object() {
            public final Object[] errors = {error};
        };

        return ResponseEntity.
                status(HttpStatus.INTERNAL_SERVER_ERROR).
                body(response);
    }
}
