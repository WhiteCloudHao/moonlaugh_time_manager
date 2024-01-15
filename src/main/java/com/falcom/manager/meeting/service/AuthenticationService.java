package com.falcom.manager.meeting.service;
import com.falcom.manager.meeting.api.dto.AuthenticationRequest;
import com.falcom.manager.meeting.api.dto.AuthenticationResponse;
import com.falcom.manager.meeting.api.dto.RegisterRequest;
import com.falcom.manager.meeting.service.JwtService;
import com.falcom.manager.meeting.persistence.token.Token;
import com.falcom.manager.meeting.persistence.token.TokenRepository;
import com.falcom.manager.meeting.persistence.token.TokenType;
import com.falcom.manager.meeting.persistence.user.User;
import com.falcom.manager.meeting.persistence.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
}