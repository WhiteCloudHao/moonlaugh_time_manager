package com.moonlaugh.manager.meeting.exception.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /* 400 BAD_REQUEST */
    INVALID_PARAMETER(20001, HttpStatus.BAD_REQUEST, "Invalid Parameter");

    private final int errorCode;
    private final HttpStatus httpStatus;
    private final String errorMessage;
}
