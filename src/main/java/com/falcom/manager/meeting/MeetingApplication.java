package com.falcom.manager.meeting;

import com.falcom.manager.meeting.api.dto.RegisterRequest;
import com.falcom.manager.meeting.persistence.user.Role;
import com.falcom.manager.meeting.service.AuthenticationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MeetingApplication {

	public static void main(String[] args) {
		SpringApplication.run(MeetingApplication.class, args);
	}
//	@Bean
//	public CommandLineRunner commandLineRunner(
//			AuthenticationService service
//	) {
//		return args -> {
//			var admin = RegisterRequest.builder()
//					.fullName("Admin")
//					.email("admin@gmail.com")
//					.password("admin")
//					.role("ADMIN")
//					.build();
//			System.out.println("Admin: " + service.register(admin));
//
//			var user = RegisterRequest.builder()
//					.fullName("User")
//					.email("user@gmail.com")
//					.password("user")
//					.role("USER")
//					.build();
//			System.out.println("USER: " + service.register(user));
//
//		};
//	}

}
