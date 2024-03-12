package com.moonlaugh.manager.meeting.api;


import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.ws.rs.BadRequestException;

@Tag(name = "Test api no auth")
@RestController
@RequestMapping(value = "/api/v1/test/noAuth")
public class TestApiNoAuthController {
    @PostMapping("/exception")
    public ResponseEntity<Object> testException() {
        throw new BadRequestException();
    }

}
