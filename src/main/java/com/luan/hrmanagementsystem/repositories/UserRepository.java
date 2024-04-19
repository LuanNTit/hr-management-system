package com.luan.hrmanagementsystem.repositories;
import java.util.List;
import java.util.Optional;

import com.luan.hrmanagementsystem.models.EmployeeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.luan.hrmanagementsystem.models.UserEntity;
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
	boolean existsByUserName(String userName);
	Optional<UserEntity> findByUserName(String userName);
	Optional<UserEntity> findByEmail(String email);
	List<UserEntity> findByUserNameContaining(String userName);
	Page<UserEntity> findAll(Pageable pageable);
}
