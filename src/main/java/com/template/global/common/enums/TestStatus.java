package com.template.global.common.enums;


import com.template.global.common.BaseEnum;

public enum TestStatus implements BaseEnum<Integer> {
    ACCEPTED(0), // 승인
    DECLINED(1), // 거절
    ERROR(2) // 에러
    ;

    private final int value;

    TestStatus(int value) {
        this.value = value;
    }

    public static TestStatus fromInteger(Integer value) {
        return switch (value) {
            case 0 -> ACCEPTED;
            case 1 -> DECLINED;
            case 2 -> ERROR;
            default -> null;
        };
    }


    @Override
    public Integer getValue() {
        return this.value;
    }

    public String getName() {
        switch (this) {
            case ACCEPTED:
                return "승인";
            case DECLINED:
                return "취소";
            case ERROR:
                return "에러";
        }
        return null;
    }
}