package com.template.global.common.enums;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // COMMON
    ERROR_WRONG_REQUEST("잘못된 요청입니다."),
    ERROR_USER_NOT_FOUND("해당 사용자를 찾을 수 없습니다."),

    // Date
    ERROR_DATE_EXCEED_7_DAYS("7일 이상의 날짜를 선택할 수 없습니다."),

    // AUTHORITY
    ERROR_USER_BLOCKED("해당 유저는 차단되었습니다."),
    ERROR_USER_AUTHORITY_NOT_NORMAL("요청한 권한이 일반이 아닙니다."),
    ERROR_USER_AUTHORITY_TEMPORARY("유저의 권한은 대기자입니다. 관리자에게 문의하세요."),

        // to select
    ERROR_USER_NOT_AUTHORIZED_TO_SELECT_USER_PAGE("해당 유저는 유저 정보 페이지 조회를 할 수 없습니다."),

        // to change
    ERROR_USER_NOT_AUTHORIZED_TO_CHANGE_NAME("해당 유저는 이름을 수정할 수 없습니다."),
    ERROR_USER_NOT_AUTHORIZED_TO_CHANGE_ROLE("해당 유저는 권한 변경을 할 수 없습니다."),
    ERROR_USER_NOT_AUTHORIZED_TO_REQUEST_TO_CHANGE_ROLE("해당 유저는 권한 변경 요청을 할 수 없습니다."),
    ERROR_USER_NOT_AUTHORIZED_TO_CHANGE_ROLE_FROM_ADMIN_TO_SUPER_ADMIN("해당 유저는 최고 관리자 권한 요청을 할 수 없습니다."),

    ERROR_AUTHORITY_SIZE_NOT_OVER_2("권한을 두개를 초과하여 가질 수 없습니다."),


    // TOKEN
    ERROR_TOKEN_VALIDATE("Token validation failed."),
    ERROR_TOKEN_NULL("Token is null."),
    ERROR_TOKEN_INVALID("Unauthorized: Invalid token"),
    ;

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
