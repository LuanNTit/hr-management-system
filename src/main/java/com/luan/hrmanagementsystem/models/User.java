package com.luan.hrmanagementsystem.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "user_table")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String userName;
    private String encryptedPassword;
    private boolean enabled;
    private String role; //Eg: USER, ADMIN
	public User(String userName, String encryptedPassword, String role, boolean enabled) {
		super();
		this.userName = userName;
		this.encryptedPassword = encryptedPassword;
		this.role = role;
		this.enabled = enabled;
	}
}