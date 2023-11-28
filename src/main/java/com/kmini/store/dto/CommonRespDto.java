package com.kmini.store.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

// 공통 반환 DTO
@AllArgsConstructor
@Data
public class CommonRespDto<T> {

    private int code;
    private String message;
    private T data;
}
