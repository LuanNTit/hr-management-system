package com.luan.hrmanagementsystem.services;

import java.util.List;

import com.luan.hrmanagementsystem.models.User;
public interface IUserService {
    List<User> getAllUsers();
    List<User> getAllUsers(int page, int size);
    List<User> getAllUsers(String sortBy);
    User createUser(User user);
    User updateUser(Long userId, User updatedUser);
    void deleteUser(Long userId);
    User getUserById(Long userId);
}
