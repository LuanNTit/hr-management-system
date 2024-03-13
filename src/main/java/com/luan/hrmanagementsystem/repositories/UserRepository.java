package com.luan.hrmanagementsystem.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luan.hrmanagementsystem.models.User;

public interface UserRepository extends JpaRepository<User, Long> {

	boolean existsByUserName(String userName);
    // Các phương thức truy vấn đặc biệt có thể được thêm ở đây
	User findByUserName(String userName);
}
