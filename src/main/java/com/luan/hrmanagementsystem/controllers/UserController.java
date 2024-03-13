package com.luan.hrmanagementsystem.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.luan.hrmanagementsystem.models.ResponseObject;
import com.luan.hrmanagementsystem.models.User;
import com.luan.hrmanagementsystem.services.IUserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private IUserService userService;
    
    @PostMapping("/register")
    public ResponseEntity<ResponseObject> registerUser(@RequestBody User user) {
        try {
            User createdUser = userService.registerUser(user.getUserName(), user.getEncryptedPassword());
            return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseObject("ok", "User register successfully", createdUser));
        } catch (Exception e) {
            // Xử lý trường hợp trùng tên người dùng hoặc các điều kiện khác
        	return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
					.body(new ResponseObject("failed", e.getMessage(), ""));
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<ResponseObject> login(@RequestBody User user) {
        try {
            User userLogin = userService.loginUser(user.getUserName(), user.getEncryptedPassword());
            return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseObject("ok", "Login successfully", userLogin));
        } catch (Exception e) {
        	return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ResponseObject("failed", "Not login successfully", ""));
        }
    }


    @GetMapping
    public ResponseEntity<ResponseObject> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseObject("ok", "List users successfully", users));
        } catch (Exception e) {
        	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseObject("failed", e.getMessage(), ""));
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ResponseObject> getUserById(@PathVariable Long userId) {
        try {
        	User user = userService.getUserById(userId);
        	return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseObject("ok", "Query employee successfully", user));
        } catch (Exception e) {
        	return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ResponseObject("failed", e.getMessage(), ""));
        }
    }

    @PostMapping
    public ResponseEntity<ResponseObject> createUser(@RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseObject("ok", "User created successfully", createdUser));
        } catch (IllegalArgumentException e) {
        	return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
					.body(new ResponseObject("failed", e.getMessage(), ""));
        } catch (Exception e) {
        	return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ResponseObject("failed", e.getMessage(), ""));
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ResponseObject> updateUser(@PathVariable Long userId, @RequestBody User updatedUser) {
        try {
            User user = userService.updateUser(userId, updatedUser);
            return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseObject("ok", "Update Employee successfully", user));
        } catch (IllegalArgumentException e) {
        	return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ResponseObject("failed", e.getMessage(), ""));
        } catch (Exception e) {
        	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseObject("failed", e.getMessage(), ""));
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ResponseObject> deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseObject("ok", "Delete product successfully", ""));
        } catch (Exception e) {
        	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseObject("failed", "Delete product successfully", ""));
        }
    }

    @GetMapping("/paged")
    public ResponseEntity<ResponseObject> getAllUsersPaged(@RequestParam int page, @RequestParam int size) {
        try {
            List<User> users = userService.getAllUsers(page, size);
            return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseObject("ok", "Successfully retrieved paged users", users));
        } catch (Exception e) {
        	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseObject("failed", "Delete product successfully", ""));
        }
    }

    @GetMapping("/sorted")
    public ResponseEntity<ResponseObject> getAllUsersSorted(@RequestParam String sortBy) {
        try {
            List<User> users = userService.getAllUsers(sortBy);
            return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseObject("ok", "Successfully retrieved sorted users", users));
        } catch (Exception e) {
        	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseObject("failed", "Delete product successfully", ""));
        }
    }
}

