package com.kmini.store.global.exception;

import com.kmini.store.domain.common.dto.CommonRespDto;
import com.kmini.store.global.constants.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.kmini.store.global.constants.ErrorCode.BAD_REQUEST;
import static com.kmini.store.global.constants.ErrorCode.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    public String getTransactionId() {
        return MDC.get("transactionId");
    }

    private void loggingErrorMessage(Exception ex) {
        String logMsg = String.format("[%s] %s", getTransactionId(), ex.getMessage());
        log.error(logMsg);
    }

    private void loggingErrorMessageWithStackTrace(Exception ex) {
        String logMsg = String.format("[%s] %s", getTransactionId(), ex.getMessage());
        log.error(logMsg, ex);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException ex) {
        loggingErrorMessageWithStackTrace(ex);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new CommonRespDto<>(-1, BAD_REQUEST.getClientErrorMsg(), null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex) {
        loggingErrorMessageWithStackTrace(ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new CommonRespDto<>(-1, INTERNAL_SERVER_ERROR.getClientErrorMsg(),null));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<?> handleIllegalStateException(Exception ex) {
        loggingErrorMessageWithStackTrace(ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new CommonRespDto<>(-1, INTERNAL_SERVER_ERROR.getClientErrorMsg(),null));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<?> handleBusinessException(BusinessException ex) {
        loggingErrorMessage(ex);
        return ResponseEntity
                .status(ex.getErrorCode().getStatus())
                .body(new CommonRespDto<>(-1, ex.getErrorCode().getClientErrorMsg(), null));
    }
}
