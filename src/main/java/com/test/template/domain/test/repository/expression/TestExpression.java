package com.test.template.domain.test.repository.expression;

import com.querydsl.core.types.dsl.BooleanExpression;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.test.template.domain.test.entity.QTestEntity.testEntity;

public class TestExpression {
    public static BooleanExpression between(LocalDateTime from, LocalDateTime to) {
        return Objects.isNull(from) || Objects.isNull(to) ? null : testEntity.createdAt.between(from, to);
    }
}
