package com.luan.hrmanagementsystem.services;

import com.luan.hrmanagementsystem.dto.AuthenticationResponse;
import com.luan.hrmanagementsystem.models.UserEntity;

public interface AuthenticationService {
	AuthenticationResponse register(UserEntity request);
	AuthenticationResponse authenticate(UserEntity request);
}
