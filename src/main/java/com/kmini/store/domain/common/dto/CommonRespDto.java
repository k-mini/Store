package com.kmini.store.domain.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

// 공통 반환 DTO
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
public class CommonRespDto<T> {

    private int code;
    private String message;
    private T data;
}
