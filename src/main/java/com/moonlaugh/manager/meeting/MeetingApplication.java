package com.moonlaugh.manager.meeting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
