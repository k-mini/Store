package com.kmini.store.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 공통 반환 DTO
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CommonRespDto<T> {

    private int code;
    private String message;
    private T data;
}
