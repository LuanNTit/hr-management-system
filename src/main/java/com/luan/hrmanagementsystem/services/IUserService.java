package com.luan.hrmanagementsystem.services;

import java.util.List;

import com.luan.hrmanagementsystem.models.MyUser;
public interface IUserService {
    List<MyUser> getAllUsers();
    List<MyUser> getAllUsers(int page, int size);
    List<MyUser> getAllUsers(String sortBy);
    MyUser createUser(MyUser user);
    MyUser updateUser(Long userId, MyUser updatedUser);
    void deleteUser(Long userId);
	MyUser getUserById(Long userId);
}
