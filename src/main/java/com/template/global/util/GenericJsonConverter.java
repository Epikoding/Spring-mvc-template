package com.template.global.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

@Slf4j
public class GenericJsonConverter<T> implements AttributeConverter<T, String> {
    @Override
    public String convertToDatabaseColumn(T additionalData) {
        // AdditionalData -> Json 문자열로 변환
        try {
            if(additionalData == null){
                return null;
            }
            return new ObjectMapper().writeValueAsString(additionalData);
        } catch (JsonProcessingException e) {
            log.error("fail to serialize as object into Json : {}", additionalData, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public T convertToEntityAttribute(String jsonStr) {
        Type type = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        ParameterizedType parameterizedType = new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[]{type};
            }

            @Override
            public Type getRawType() {
                return List.class;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        };

        // Json 문자열 -> AdditionalData로 변환
        try {
            if (jsonStr == null || "null".equals(jsonStr)) {
                return null;
            }
            return new ObjectMapper().readValue(jsonStr, new TypeReference<T>() {
                @Override
                public Type getType() {
                    return parameterizedType;
                }
            });
        } catch (IOException e) {
            log.error("fail to deserialize as Json into Object : {}", jsonStr, e);
            throw new RuntimeException(e);
        }
    }
}
