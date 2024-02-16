package com.moonlaugh.manager.meeting.api;

import com.moonlaugh.manager.meeting.api.dto.GoogleLoginRequest;
import com.moonlaugh.manager.meeting.api.dto.RegisterRequest;
import com.moonlaugh.manager.meeting.persistence.user.User;
import com.moonlaugh.manager.meeting.service.AuthenticationService;
import com.moonlaugh.manager.meeting.untils.ConstantMessages;
import com.moonlaugh.manager.meeting.untils.GenerateRandomPassword;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Base64;
import com.google.api.client.json.JsonObjectParser;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.io.StringReader;

@RestController
@RequestMapping("/api/v1/auth/google")
@Tag(name="Login google")
@RequiredArgsConstructor
public class GoogleLoginController {
    private final AuthenticationService service;
    private final PasswordEncoder passwordEncoder;
    @GetMapping("/test")
    public String loginGoogle() {
        System.out.println("success api call");
        return "hello 12356";
    }
    @PostMapping("/login")
    public ResponseEntity<Object> verifyToken(
            @RequestBody GoogleLoginRequest googleToken
    ) throws GeneralSecurityException, IOException {
        User savedUser;
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> errors = new HashMap<>();
        final String CLIENT_ID = "67981564986-ntu4kt0kjlc60hvsbcjkq92tl23p1ltf.apps.googleusercontent.com";
        String token = googleToken.getGoogleToken();
        String[] tokenParts = token.split("\\.");
        byte[] decodedPayloadBytes = Base64.decodeBase64(tokenParts[1]);
        String decodedPayload = new String(decodedPayloadBytes, StandardCharsets.UTF_8);
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        JsonObjectParser parser = new JsonObjectParser(jsonFactory);
        Map<String, Object> payloadMap = parser.parseAndClose(new StringReader(decodedPayload), Map.class);
        long expirationTimeSeconds = ((Number) payloadMap.get("exp")).longValue();
        HttpTransport transport;
        transport = GoogleNetHttpTransport.newTrustedTransport();
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();

// (Receive idTokenString by HTTPS POST)
        User user;
        GoogleIdToken idToken = verifier.verify(token);
        if (idToken != null) {
            Payload payload = idToken.getPayload();
            String userId = payload.getSubject();
            System.out.println("User ID: " + userId);
            String email = payload.getEmail();
            String name = (String) payload.get("name");
            String password = GenerateRandomPassword.generatePassayPassword();
            System.out.println(password);
            if(service.checkEmailExist(email)) {
                user = service.findByEmail(email).orElse(null);
                if(user != null)  {
                    return this.handleLoginSuccess(user, response);
                } else {
                    errors.put(ConstantMessages.INTERNAL_SYSTEM, ConstantMessages.MESSAGE_SOMETHING_WENT_WRONG);
                }

            } else {
                try {
//            Role roleNameConvertRole = Role.fromName(role);
//            RegisterRequest newRegRquest = new RegisterRequest(fullName, email, password, birthDay, role);
                    RegisterRequest newRegRquest = new RegisterRequest(name, email, password, "USER");
                    user = service.register(newRegRquest);
                } catch (Exception ex) {
                    response.put(ConstantMessages.MESSAGE, ConstantMessages.MESSAGE_SOMETHING_WENT_WRONG);
                    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
                }
                if( user== null) {
                    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
                }
                response.put(ConstantMessages.MESSAGE, ConstantMessages.MESSAGE_LOGIN_GOOGLE_SUCCESS);
            }
//            String pictureUrl = (String) payload.get("picture");
//            String locale = (String) payload.get("locale");
        } else {
            errors.put(ConstantMessages.INTERNAL_SYSTEM, ConstantMessages.MESSAGE_SOMETHING_WENT_WRONG);
            return new ResponseEntity<>(new JSONObject(response), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (!errors.isEmpty()) {
            response.put(ConstantMessages.MESSAGE, ConstantMessages.MESSAGE_SOMETHING_WENT_WRONG);
            response.put(ConstantMessages.ERRORS, errors);
            return new ResponseEntity<>(new JSONObject(response), HttpStatus.INTERNAL_SERVER_ERROR);
        }

//        return "hello 1234567";
//        return new ResponseEntity<>(response, HttpStatus.OK);
        return this.handleLoginSuccess(user, response);
    }
    private ResponseEntity<Object> handleLoginSuccess(User user, Map<String, Object> response) {
        String jwtToken;
        try {
            jwtToken = service.handleLoginSuccess(user);
        } catch (Exception ex) {
            response.put(ConstantMessages.MESSAGE, ConstantMessages.MESSAGE_SOMETHING_WENT_WRONG);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("accessToken", jwtToken);
        response.put("user", this.buildReturnedUser(user));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    private Map<String, String> buildReturnedUser(User user) {
        Map<String, String> returnedUser = new HashMap<>();
        returnedUser.put("id", Integer.toString(user.getId()));
        returnedUser.put("email", user.getEmail());
        returnedUser.put("role", user.getRole().getName());
        return returnedUser;
    }
}
