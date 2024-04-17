package com.luan.hrmanagementsystem.services;

import com.luan.hrmanagementsystem.dto.AuthenticationResponse;
import com.luan.hrmanagementsystem.dto.UserDTO;
import com.luan.hrmanagementsystem.models.UserEntity;

public interface AuthenticationService {
	AuthenticationResponse register(UserDTO request);
	AuthenticationResponse authenticate(UserDTO request);
	String lockUser(String username);
}
