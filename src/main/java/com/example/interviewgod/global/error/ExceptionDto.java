package com.example.interviewgod.global.error;

import lombok.Getter;

@Getter
public class ExceptionDto {
    private Integer code;
    private String message;

    public ExceptionDto(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }
}
