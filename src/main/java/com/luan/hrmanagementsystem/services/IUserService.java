package com.luan.hrmanagementsystem.services;

import java.util.List;

import com.luan.hrmanagementsystem.models.User;
public interface IUserService {
    List<User> getAllUsers();
    User createUser(User user);
    User updateUser(Long userId, User updatedUser);
    void deleteUser(Long userId);
    List<User> getAllUsers(int page, int size);
    List<User> getAllUsers(String sortBy);
    User registerUser(String username, String password);
    User loginUser(String username, String password);
    User registerUser(User user);
    User loginUser(User user);
	User getUserById(Long userId);
}
