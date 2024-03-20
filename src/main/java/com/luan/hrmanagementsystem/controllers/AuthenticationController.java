package com.luan.hrmanagementsystem.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.luan.hrmanagementsystem.dto.AuthenticationResponse;
import com.luan.hrmanagementsystem.models.User;
import com.luan.hrmanagementsystem.services.AuthenticationService;

@RestController
public class AuthenticationController {
	private AuthenticationService authService;

	public AuthenticationController(AuthenticationService authService) {
		super();
		this.authService = authService;
	}
	
	@PostMapping("/register")
	public ResponseEntity<AuthenticationResponse> register(
		@RequestBody User request
	) {
		return ResponseEntity.ok(authService.register(request));
	}

	@PostMapping("/login")
	public ResponseEntity<AuthenticationResponse> login(
		@RequestBody User request
	) {
		return ResponseEntity.ok(authService.authenticate(request));
	}

	
}
