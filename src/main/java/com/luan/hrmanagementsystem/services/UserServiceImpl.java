package com.luan.hrmanagementsystem.services;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.luan.hrmanagementsystem.database.Database;
import com.luan.hrmanagementsystem.models.User;
import com.luan.hrmanagementsystem.repositories.UserRepository;

@Service
public class UserService implements IUserService,  UserDetailsService {
	private static final Logger logger = LoggerFactory.getLogger(Database.class);
	@Autowired
	private UserRepository userRepository;

	@Override
	public List<User> getAllUsers() {
		try {
			List<User> users = userRepository.findAll();
			logger.info("Number of users fetched: {}", users.size());
			return users;
		} catch (Exception e) {
			throw new RuntimeException("Error while fetching users", e);
		}
	}

	@Override
	public User getUserById(Long userId) {
		// Kiểm tra xem nhân viên có tồn tại không
		Optional<User> existingUserOptional = userRepository.findById(userId);
		if (existingUserOptional.isPresent()) {
			return existingUserOptional.orElse(null);
		} else {
			throw new IllegalArgumentException("User not found with ID: " + userId);
		}
	}

	@Override
	public User createUser(User user) {
		// Bổ sung logic kiểm tra trước khi lưu người dùng
		Optional<User> existingUser = userRepository.findByUserName(user.getUserName());
		if (existingUser.isPresent()) {
			// Xử lý trường hợp trùng tên người dùng (hoặc bất kỳ điều kiện nào khác)
			throw new IllegalArgumentException("Username already exists: " + user.getUserName());
		}

		/*
		 * user.setEncryptedPassword(passwordEncoder.encode(user.getEncryptedPassword())
		 * );
		 */

		// Lưu người dùng mới vào cơ sở dữ liệu
		User savedUser = userRepository.save(user);
		return savedUser;
	}

	@Override
	public User updateUser(Long userId, User updatedUser) {
		Optional<User> existingUserOptional = userRepository.findById(userId);

		if (existingUserOptional.isPresent()) {
			User existingUser = existingUserOptional.get();
			existingUser.setEnabled(updatedUser.isEnabled());
			// Mã hóa mật khẩu trước khi lưu vào cơ sở dữ liệu
			String encodedPassword = BCrypt.hashpw(existingUser.getEncryptedPassword(), BCrypt.gensalt());
			existingUser.setEncryptedPassword(encodedPassword);
			existingUser.setUserName(updatedUser.getUserName());

			return userRepository.save(existingUser);
		} else {
			throw new IllegalArgumentException("User not found with id: " + userId);
		}
	}

	@Override
	public void deleteUser(Long userId) {
		userRepository.deleteById(userId);
	}

	@Override
	public List<User> getAllUsers(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		return userRepository.findAll(pageable).getContent();
	}

	@Override
	public List<User> getAllUsers(String sortBy) {
		Sort sort = Sort.by(sortBy);
		return userRepository.findAll(sort);
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = userRepository.findByUserName(username);
		if (user.isPresent()) {
			var userObj = user.get();
			return org.springframework.security.core.userdetails.User.builder()
					.username(userObj.getUserName())
					.password(userObj.getEncryptedPassword())
					.roles(getRoles(userObj))
					.build();
		} else {
			throw new UsernameNotFoundException(username);
		}
	}
	private String[] getRoles(User user){
		if (user.getRole() == null) {
			return new String[] {"USER"};
		}
		return user.getRole().split(",");
	}
}
