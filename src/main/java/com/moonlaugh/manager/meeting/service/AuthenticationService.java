package com.moonlaugh.manager.meeting.service;
import com.moonlaugh.manager.meeting.api.dto.RegisterRequest;
import com.moonlaugh.manager.meeting.api.dto.request.VerifyCodeRequest;
import com.moonlaugh.manager.meeting.persistence.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


public interface AuthenticationService {


    User register(RegisterRequest request);

    User authenticate(String email, String password);
    void insertExcellUser(List<RegisterRequest> ListRequest);

    void saveUserToken(User user, String jwtToken);

    void revokeAllUserTokens(User user);

   void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException;
    String handleLoginSuccess(User user);
    boolean checkEmailExist(String email);
    Optional<User> findByEmail(String email);
    Integer getUserId();
    User getUser();
    String generateVerifyCode(int length);
    String checkStatusAccount(String email);
    void verifyCode(VerifyCodeRequest verifyCodeRequest);
}