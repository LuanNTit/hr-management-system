package com.luan.hrmanagementsystem.services;

import com.luan.hrmanagementsystem.dto.AuthenticationResponse;
import com.luan.hrmanagementsystem.dto.TokenDTO;
import com.luan.hrmanagementsystem.dto.UserDTO;

import java.util.List;

public interface AuthenticationService {
	AuthenticationResponse register(UserDTO request);
	AuthenticationResponse authenticate(UserDTO request);
	String lockUser(String username);
	String processForgotPassword(String email);
	void sendResetPasswordEmail(String toEmail, String resetPasswordLink);
	List<TokenDTO> viewActiveAccounts();
}
