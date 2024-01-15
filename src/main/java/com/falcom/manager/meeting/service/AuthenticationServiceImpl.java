package com.falcom.manager.meeting.service;

import com.falcom.manager.meeting.api.dto.AuthenticationResponse;
import com.falcom.manager.meeting.api.dto.RegisterRequest;
import com.falcom.manager.meeting.persistence.token.Token;
import com.falcom.manager.meeting.persistence.token.TokenRepository;
import com.falcom.manager.meeting.persistence.token.TokenType;
import com.falcom.manager.meeting.persistence.user.Role;
import com.falcom.manager.meeting.persistence.user.User;
import com.falcom.manager.meeting.persistence.user.UserRepository;
import com.falcom.manager.meeting.untils.ConstantMessages;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.ws.rs.InternalServerErrorException;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
        public User register(RegisterRequest request) {
        User savedUser;
        Role roleUser =  Role.fromName(request.getRole());
//        SimpleDateFormat dateFormat =new SimpleDateFormat("dd/MM/yyyy");
//        Date userBrithDay = null;
//        if (request.getBirthDay() != null && !request.getBirthDay().isEmpty()) {
//            try {
//                userBrithDay = dateFormat.parse(request.getBirthDay());
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }
        try {
            RegisterRequest a = request;
            User user = User.builder()
                    .fullName(request.getFullName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
//                    .birthDay(userBrithDay)
                    .role(roleUser)
                    .build();
            savedUser = userRepository.save(user);
        } catch (DataIntegrityViolationException ex){
            DataIntegrityViolationException a = ex;
            return null;
        }
        return savedUser;
//        return AuthenticationResponse.builder()
//                .accessToken(jwtToken)
//                .refreshToken(refreshToken)
//                .build();
    }
//    public List<User> registerExcell(List<RegisterRequest> listRegisterRequest) {
//       for(RegisterRequest registerReq: listRegisterRequest) {
//           User savedUser;
//           Role roleUser =  Role.fromName(registerReq.getRole());
//           SimpleDateFormat dateFormat =new SimpleDateFormat("dd/MM/yyyy");
//           Date userBrithDay = null;
//           if (registerReq.getBirthDay() != null && !registerReq.getBirthDay().isEmpty()) {
//               try {
//                   userBrithDay = dateFormat.parse(registerReq.getBirthDay());
//               } catch (ParseException e) {
//                   e.printStackTrace();
//               }
//           }
//           try {
//               User user = User.builder()
//                       .fullName(registerReq.getFullName())
//                       .email(registerReq.getEmail())
//                       .password(passwordEncoder.encode(registerReq.getPassword()))
//                       .birthDay(userBrithDay)
//                       .role(roleUser)
//                       .build();
//               savedUser = userRepository.save(user);
//           } catch (DataIntegrityViolationException ex){
//               return null;
//           }
//       }
//
//    }

    @Override
    public User authenticate(String email, String password) {
        Authentication authentication;
        try {
            // authenticate user using authenticationManager
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(email , password));

        } catch (BadCredentialsException ex) { //authenticate failed
            return null;
        }
        var  user = userRepository.findByEmail(email)
                .orElseThrow();
        if (user == null) {
            throw new InternalServerErrorException(ConstantMessages.MESSAGE_SOMETHING_WENT_WRONG);
        }
        return user;
    }

    @Override
     public void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    @Override
    public  void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
    @Transactional
    public void insertExcellUser(List<RegisterRequest> ListRequest) {
        List<User> listUser = new ArrayList<>();
        for(RegisterRequest request:ListRequest ) {
                User user = createUser(request);
//                entityManager.persist(user);
            listUser.add(user);
        }
        userRepository.saveAll(listUser);
    }
    private User createUser(RegisterRequest request) {
        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(Role.fromName(request.getRole()));
        return user;
    }
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = this.userRepository.findByEmail(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
    @Override
    public String handleLoginSuccess(User user) {
        String jwtToken = jwtService.generateToken(user);
        if (jwtToken == null) {
            throw new InternalServerErrorException(ConstantMessages.MESSAGE_SOMETHING_WENT_WRONG);
        }
//        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(user, jwtToken);

        // store generated token into redis: key=email+current time, value=token with timeout
//        redisTemplate.ohandlepsForValue().set(user.getEmail() + jwtUtils.getTime(jwtToken).toString(), jwtToken, jwtUtils.getExpiration(), TimeUnit.MILLISECONDS);
//        // reset info about login failed
//        redisTemplate.delete(user.getEmail() + this.redisLoginFailedPostfix);

        return jwtToken;
    }
    @Override
    public boolean checkEmailExist(String email) {
        return userRepository.existsByEmail(email);
    }
    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    @Override
    public Integer getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Integer userId = user.getId();
        String id = userId.toString();
        System.out.println("userId" +  id);
        return userId;
    }
    @Override
    public User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return user;
    }
}
