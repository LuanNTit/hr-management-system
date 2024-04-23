package com.luan.hrmanagementsystem.services;

import java.util.List;

import com.luan.hrmanagementsystem.dto.EmployeeDTO;
import org.springframework.data.domain.Page;

import com.luan.hrmanagementsystem.dto.UserDTO;
public interface UserService {
	List<UserDTO> getAllUsers();
	UserDTO saveUser(UserDTO user);
	UserDTO getUserById(Long id);
	void deleteUserById(Long id);
	UserDTO updateUser(Long id, UserDTO user);
	List<UserDTO> searchUser(String name);
	Page<UserDTO> getAllUsers(int page, int size, String sortField, String sortDirection);
}
