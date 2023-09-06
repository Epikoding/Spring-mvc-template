package com.test.template.domain.test.dto;

import com.test.template.global.common.BaseDTO;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestDTO extends BaseDTO {

    private String test;

}
