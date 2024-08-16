package com.template.global.common.enums;

import lombok.Getter;

@Getter
public enum Role {
    ADMIN(1),
    NORMAL(2),
    TEMPORARY(3),
    BLOCKED(4);

    private final int priority;

    Role(int priority) {
        this.priority = priority;
    }

}
