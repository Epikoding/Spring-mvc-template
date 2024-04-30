package com.template.domain.user.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.template.domain.user.dto.UserDto;
import com.template.domain.user.entity.UserEntity;
import com.template.domain.user.repository.expression.UserExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.template.domain.user.entity.QAuthorityEntity.authorityEntity;
import static com.template.domain.user.entity.QUserAuthorityEntity.userAuthorityEntity;
import static com.template.domain.user.entity.QUserEntity.userEntity;

@Service
@RequiredArgsConstructor
public class UserDao {
    private final JPAQueryFactory queryFactory;

    private <K, U, T> Map<K, U> toMap(List<T> tuples, Function<T, K> keyMapper, Function<T, U> valueMapper, BinaryOperator<U> mergeFunction) {
        return tuples.stream()
                .collect(Collectors.toMap(keyMapper, valueMapper, mergeFunction));
    }


    public Optional<UserDto.Response> findUserByEmailAddressOptional(String emailAddress) {
        List<Tuple> fetch = queryFactory
                .select(userEntity.id,
                        userEntity.email,
                        userEntity.phone,
                        userEntity.name,
                        authorityEntity.role)
                .from(userEntity)
                .innerJoin(userEntity.userAuthorityList, userAuthorityEntity)
                .innerJoin(userAuthorityEntity.authorityEntity, authorityEntity)
                .where(UserExpression.eqEmailAddress(emailAddress))
                .fetch();

        Map<Long, UserDto.Response> userResponseMap = toMap(
                fetch,
                tuple -> tuple.get(userEntity.id), // Key mapper
                tuple -> new UserDto.Response( // Value mapper
                        tuple.get(userEntity.email),
                        tuple.get(userEntity.phone),
                        tuple.get(userEntity.name),
                        new ArrayList<>(Collections.singletonList(tuple.get(authorityEntity.role)))),
                (existing, replacement) -> { // Merge function
                    existing.getRoleList().add(replacement.getRoleList().get(0));
                    return existing;
                });

        return userResponseMap.values().stream().findFirst();
    }

    public Optional<UserEntity> findUserById(Long id) {
        return Optional.ofNullable(queryFactory
                .selectFrom(userEntity)
                .where(UserExpression.eqId(id))
                .fetchFirst()
        );
    }

    public List<UserEntity> findAll() {
        return queryFactory
                .selectFrom(userEntity)
                .fetch();
    }
}