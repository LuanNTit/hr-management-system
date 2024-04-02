package com.luan.hrmanagementsystem.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.luan.hrmanagementsystem.dto.AuthenticationResponse;
import com.luan.hrmanagementsystem.models.UserEntity;
import com.luan.hrmanagementsystem.services.AuthenticationServiceImpl;

@RestController
public class AuthenticationController {
	private AuthenticationServiceImpl authService;

	public AuthenticationController(AuthenticationServiceImpl authService) {
		super();
		this.authService = authService;
	}
	
	@PostMapping("/register")
	public ResponseEntity<AuthenticationResponse> register(
		@RequestBody UserEntity request
	) {
		return ResponseEntity.ok(authService.register(request));
	}

	@PostMapping("/login")
	public ResponseEntity<AuthenticationResponse> login(
		@RequestBody UserEntity request
	) {
		return ResponseEntity.ok(authService.authenticate(request));
	}

	
}
