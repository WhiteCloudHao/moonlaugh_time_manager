package com.falcom.manager.meeting.api.dto.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyCodeRequest {
    private String email;
    private String verifyCode;
}
