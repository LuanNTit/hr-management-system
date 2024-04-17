package com.luan.hrmanagementsystem.services;

import com.luan.hrmanagementsystem.models.TokenEntity;
import com.luan.hrmanagementsystem.repositories.TokenRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.luan.hrmanagementsystem.dto.AuthenticationResponse;
import com.luan.hrmanagementsystem.dto.UserDTO;
import com.luan.hrmanagementsystem.models.UserEntity;
import com.luan.hrmanagementsystem.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
	private final UserRepository repository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;
	private final TokenRepository tokenRepository;

	public AuthenticationResponse register(UserDTO request) {
		UserEntity userEntity = convertToEntity(request);
		userEntity.setEncryptedPassword(passwordEncoder.encode(request.getEncryptedPassword()));
		UserEntity user = repository.save(userEntity);
		String jwt = jwtService.generateToken(user);

		saveUserToken(jwt, user);

		return new AuthenticationResponse(jwt, "User registration was successful");
	}
	
	public AuthenticationResponse authenticate(UserDTO request) {
		authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(
				request.getUserName(),
				request.getEncryptedPassword()
			)
		);

		UserEntity user = repository.findByUserName(request.getUserName()).orElseThrow();
		String token = jwtService.generateToken(user);
		String jwt = jwtService.generateToken(user);

		revokeAllTokenByUser(user);
		saveUserToken(jwt, user);

		return new AuthenticationResponse(jwt, "User login was successful");
	}

	@Override
	public String lockUser(String username) {
		Optional<UserEntity> findUser = repository.findByUserName(username);
		if (findUser.isPresent()) {
			UserEntity user = findUser.get();
			user.setLocked(true);
			repository.save(user);
			return "Tài khoản '" + username + "' đã bị khóa.";
		} else {
			return "Không tìm thấy tài khoản với tên người dùng '" + username + "'.";
		}
	}

	private void revokeAllTokenByUser(UserEntity user) {
		List<TokenEntity> validTokens = tokenRepository.findAllTokensByUser(user.getUserId());
		if(validTokens.isEmpty()) {
			return;
		}

		validTokens.forEach(t-> {
			t.setLoggedOut(true);
		});

		tokenRepository.saveAll(validTokens);
	}
	private void saveUserToken(String jwt, UserEntity user) {
		TokenEntity token = new TokenEntity();
		token.setToken(jwt);
		token.setLoggedOut(false);
		token.setUser(user);
		tokenRepository.save(token);
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
	public UserEntity convertToEntity(UserDTO userDTO) {
		if (userDTO == null) {
			return null;
		}
		UserEntity userEntity = new UserEntity();
		userEntity.setUserId(userDTO.getUserId());
		userEntity.setEncryptedPassword(userDTO.getEncryptedPassword());
		userEntity.setRole(userDTO.getRole());
		userEntity.setUserName(userDTO.getUserName());
		userEntity.setEnabled(userDTO.isEnabled());
		return userEntity;
	}
}