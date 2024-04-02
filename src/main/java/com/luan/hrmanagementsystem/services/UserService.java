package com.luan.hrmanagementsystem.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.luan.hrmanagementsystem.dto.UserDTO;
public interface UserService {
	List<UserDTO> getAllUsers();
	UserDTO saveUser(UserDTO user);
	UserDTO getUserById(Long id);
	void deleteUserById(Long id);
	Page<UserDTO> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);
}
