package com.example.interviewgod.global.dto;

import com.example.interviewgod.global.error.CommonException;
import com.example.interviewgod.global.error.ErrorCode;
import com.example.interviewgod.global.error.ExceptionDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

public record ResponseDto<T>(@JsonIgnore HttpStatus httpStatus,
                             @NotNull int statusCode,
                             @NotNull Boolean success,
                             @Nullable T data,
                             @Nullable ExceptionDto error) {

    public static <T> ResponseDto<T> ok(@Nullable final T data) {
        return new ResponseDto<>(HttpStatus.OK, HttpStatus.OK.value(), true, data, null);
    }

    public static <T> ResponseDto<T> created(@Nullable final T data) {
        return new ResponseDto<>(HttpStatus.CREATED, HttpStatus.CREATED.value(),true, data, null);
    }

    public static ResponseDto<Object> fail(final MethodArgumentNotValidException e) {
        return new ResponseDto<>(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), false, null,
                new ExceptionDto(ErrorCode.INVALID_ARGUMENT));
    }

    public static ResponseDto<Object> fail(final MissingServletRequestParameterException e) {
        return new ResponseDto<>(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), false, null,
                new ExceptionDto(ErrorCode.MISSING_REQUEST_PARAMETER));
    }

    public static ResponseDto<Object> fail(final MethodArgumentTypeMismatchException e) {
        return new ResponseDto<>(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value(), false, null,
                new ExceptionDto(ErrorCode.INVALID_PARAMETER_FORMAT));
    }

    public static ResponseDto<Object> fail(final CommonException e) {
        return new ResponseDto<>(e.getErrorCode().getHttpStatus(), e.getErrorCode().getHttpStatus().value(), false, null,
                new ExceptionDto(e.getErrorCode()));
    }
}
