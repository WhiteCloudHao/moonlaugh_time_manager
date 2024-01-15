package com.falcom.manager.meeting.api.dto;

import com.falcom.manager.meeting.persistence.user.Role;
import lombok.*;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegisterRequest {
    private String fullName;
    private String email;
    private String password;
//    private String birthDay;
    private String role;
}