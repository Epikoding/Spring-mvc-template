package com.template.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.Objects;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BasePageDto {

    private Long totalElementCount;
    private Integer totalPageCount;
    private Integer pageSize;
    private Integer pageNo;
    private Integer numberOfElement;
    private Integer from;
    private Integer to;

    public BasePageDto(final Page<?> page) {
        this.totalElementCount = page.getTotalElements();
        this.totalPageCount = page.getTotalPages();
        this.pageSize = page.getSize();
        this.pageNo = page.getNumber();
        this.numberOfElement = page.getNumberOfElements();
        this.from = (int) (totalElementCount - (pageSize * pageNo));
        this.to = from - numberOfElement + 1;
    }

    public BasePageDto(final Long totalElementCount, Integer totalPageCount, Integer pageSize, Integer pageNo, Integer numberOfElement) {
        this.totalElementCount = totalElementCount;
        this.totalPageCount = totalPageCount;
        this.pageSize = pageSize;
        this.pageNo = pageNo;
        this.numberOfElement = numberOfElement;

        this.from = 0;
        this.to = 0;
        if (Objects.nonNull(totalElementCount) && !Objects.equals(totalElementCount.intValue(), 0)) {
            this.from = (int) (totalElementCount - (pageSize * pageNo));
            this.to = from - numberOfElement + 1;
        }
    }
}
