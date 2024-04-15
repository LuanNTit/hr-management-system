package com.luan.hrmanagementsystem.controllers;

import com.luan.hrmanagementsystem.dto.UserDTO;
import com.luan.hrmanagementsystem.services.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.luan.hrmanagementsystem.dto.AuthenticationResponse;
import com.luan.hrmanagementsystem.models.UserEntity;
import com.luan.hrmanagementsystem.services.AuthenticationServiceImpl;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {
	private final AuthenticationService authService;


	@PostMapping("/register")
	public ResponseEntity<AuthenticationResponse> register(
			@RequestBody UserDTO request
	) {
		return ResponseEntity.ok(authService.register(request));
	}

	@PostMapping("/login")
	public ResponseEntity<AuthenticationResponse> login(
			@RequestBody UserDTO request
	) {
		return ResponseEntity.ok(authService.authenticate(request));
	}
}
