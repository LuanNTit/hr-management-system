package com.luan.hrmanagementsystem.services;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.luan.hrmanagementsystem.dto.AuthenticationResponse;
import com.luan.hrmanagementsystem.models.User;
import com.luan.hrmanagementsystem.repositories.UserRepository;

@Service
public class AuthenticationService {
	private final UserRepository repository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;

	public AuthenticationService(UserRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService,
			AuthenticationManager authenticationManager) {
		super();
		this.repository = repository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
		this.authenticationManager = authenticationManager;
	}

	public AuthenticationResponse register(User request) {
		request.setEnabled(true);
		request.setEncryptedPassword(passwordEncoder.encode(request.getEncryptedPassword()));
		User user = repository.save(request);
		String token = jwtService.generateToken(user);
		return new AuthenticationResponse(token);
	}
	
	public AuthenticationResponse authenticate(User request) {
		authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(
				request.getUserName(),
				request.getEncryptedPassword()
			)
		);

		User user = repository.findByUserName(request.getUserName()).orElseThrow();
		String token = jwtService.generateToken(user);
		
		return new AuthenticationResponse(token);
	}
}