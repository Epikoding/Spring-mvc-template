package com.template.global.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BaseDto {

    @Schema(description = "PK String UUID", accessMode = Schema.AccessMode.READ_ONLY)
    private String id;

    @Schema(description = "데이터 삭제 상태", example = "true/false", defaultValue = "false")
    private boolean deleted;

    @Schema(description = "데이터 생성 날짜", example = "1990-01-01 12:24:56", format = "datetime")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @Schema(description = "데이터 수정 날짜", example = "1990-01-01 12:24:56", format = "datetime")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime modifiedAt;

}