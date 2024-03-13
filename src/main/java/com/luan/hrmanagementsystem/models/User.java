package com.luan.hrmanagementsystem.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_table")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "encrypted_password")
    private String encryptedPassword;

    @Column(name = "user_name")
    private String userName;

	public User(boolean enabled, String encryptedPassword, String userName) {
		super();
		this.enabled = enabled;
		this.encryptedPassword = encryptedPassword;
		this.userName = userName;
	}

	public User(String userName, String encryptedPassword) {
		super();
		this.userName = userName;
		this.encryptedPassword = encryptedPassword;
	}
	
    // Constructors, getters, and setters
    
}