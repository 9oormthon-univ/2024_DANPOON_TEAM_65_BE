package com.example.interviewgod.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

//Runtime Exception 상속 try catch 안해도됨
@RequiredArgsConstructor
@Getter
public class CommonException extends RuntimeException{

    private final ErrorCode errorCode;


}
