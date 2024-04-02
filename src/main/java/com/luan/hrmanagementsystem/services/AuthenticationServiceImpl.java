package com.luan.hrmanagementsystem.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.luan.hrmanagementsystem.dto.AuthenticationResponse;
import com.luan.hrmanagementsystem.dto.UserDTO;
import com.luan.hrmanagementsystem.models.UserEntity;
import com.luan.hrmanagementsystem.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
	private final UserRepository repository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;

	public AuthenticationResponse register(UserEntity request) {
		request.setEnabled(true);
		request.setEncryptedPassword(passwordEncoder.encode(request.getEncryptedPassword()));
		UserEntity user = repository.save(request);
		UserDTO userDTO = convertToDTO(user);
		String token = jwtService.generateToken(userDTO);
		return new AuthenticationResponse(token);
	}
	
	public AuthenticationResponse authenticate(UserEntity request) {
		authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(
				request.getUserName(),
				request.getEncryptedPassword()
			)
		);

		UserEntity user = repository.findByUserName(request.getUserName()).orElseThrow();
		UserDTO userDTO = convertToDTO(user);
		String token = jwtService.generateToken(userDTO);
		
		return new AuthenticationResponse(token);
	}
	
	public UserDTO convertToDTO(UserEntity userEntity) {
		if (userEntity == null) {
			return null;
		}
		UserDTO userDTO = new UserDTO();
		userDTO.setUserId(userEntity.getUserId());
		userDTO.setEncryptedPassword(userEntity.getEncryptedPassword());
		userDTO.setRole(userEntity.getRole());
		userDTO.setUserName(userEntity.getUserName());
		userDTO.setEnabled(userEntity.isEnabled());
		return userDTO;
	}
}