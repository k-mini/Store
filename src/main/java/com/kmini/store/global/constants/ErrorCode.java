package com.kmini.store.global.constants;

public enum ErrorCode {

    BAD_REQUEST(400, "잘못된 요청입니다."),
    INVALID_PASSWORD(400, "비밀번호가 맞지 않습니다."),
    INVALID_ID_OR_PASSWORD(401, "등록되지 않은 아이디이거나 잘못된 비밀번호 입니다."),
    FORBIDDEN(403, "해당 자원을 접근할 수 없습니다."),
    NOT_FOUND_RESOURCE(404, "해당하신 정보를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(500,"예상치 못한 문제가 발생했습니다. 잠시 후에 다시 시도해 주시기 바랍니다."),;

    /**
     *  클라이언트에게 보낼 상태코드
     */
    private int status;
    /**
     *  클라이언트에게 보여줄 메시지
     */
    private String clientErrorMsg;

    ErrorCode(int status, String clientErrorMsg) {
        this.status = status;
        this.clientErrorMsg = clientErrorMsg;
    }

    public int getStatus() {
        return status;
    }

    public String getClientErrorMsg() {
        return clientErrorMsg;
    }
}
