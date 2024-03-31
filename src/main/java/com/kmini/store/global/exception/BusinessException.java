package com.kmini.store.global.exception;


import com.kmini.store.global.constants.ErrorCode;

public class BusinessException extends RuntimeException{

    private ErrorCode errorCode;

    /**
     *  logMessage: 로그에 남길 메시지
     *  errorCode: 클라이언트에 응답할 코드와 메세지가 들어 있다.
     */
    public BusinessException(String logMessage, ErrorCode errorCode) {
        super(logMessage);
        this.errorCode = errorCode;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getClientErrorMsg());
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
