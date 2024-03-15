package com.luan.hrmanagementsystem.repositories;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.luan.hrmanagementsystem.models.MyUser;
@Repository
public interface UserRepository extends JpaRepository<MyUser, Long> {

	boolean existsByUserName(String userName);
    // Các phương thức truy vấn đặc biệt có thể được thêm ở đây
	Optional<MyUser> findByUserName(String userName);
}
