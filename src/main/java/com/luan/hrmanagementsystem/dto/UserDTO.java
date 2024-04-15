package com.luan.hrmanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDTO {
	private Long userId;
    private String userName;
    private String encryptedPassword;
    private boolean enabled;
    private String role; //Eg: USER, ADMIN

    public UserDTO(String userName, String encryptedPassword) {
        this.userName = userName;
        this.encryptedPassword = encryptedPassword;
    }
}
