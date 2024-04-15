package com.luan.hrmanagementsystem.dto;

import lombok.Data;
import lombok.Getter;

@Data
public class AuthenticationResponse {
	private String token;
	private String message;

	public AuthenticationResponse(String token, String message) {
		this.token = token;
		this.message = message;
	}
}

