package com.moonlaugh.manager.meeting.exception;


import com.moonlaugh.manager.meeting.exception.dto.ErrorCode;
import com.moonlaugh.manager.meeting.exception.dto.ErrorMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.ws.rs.BadRequestException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorMessage> handleResponseStatusException(BadRequestException ex) {
        log.info("bad request");
        return this.toResponseEntity(ErrorCode.INVALID_PARAMETER, ex.getMessage());
    }

    private ResponseEntity<ErrorMessage> toResponseEntity(ErrorCode errorCode, String errorMessage) {
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ErrorMessage.builder()
                        .statusCode(errorCode.getHttpStatus().value())
                        .statusName(errorCode.getHttpStatus().name())
                        .errorCode(errorCode.getErrorCode())
                        .errorMessage(errorMessage)
                        .build()
                );
    }
}
