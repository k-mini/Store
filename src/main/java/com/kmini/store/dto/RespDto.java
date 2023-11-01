package com.kmini.store.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RespDto<T> {

    private int code;
    private String message;
    private T data;
}
