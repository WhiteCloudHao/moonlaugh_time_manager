package com.moonlaugh.manager.meeting.exception.dto;
import lombok.*;


@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@ToString
public class ErrorMessage {
    private final int statusCode;
    private final String statusName;
    private final int errorCode;
    private final String errorMessage;
}
