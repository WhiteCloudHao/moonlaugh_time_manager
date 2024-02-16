package com.moonlaugh.manager.meeting.api.dto;

import lombok.*;

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