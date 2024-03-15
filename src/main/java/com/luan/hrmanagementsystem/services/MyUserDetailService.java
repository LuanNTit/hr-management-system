package com.luan.hrmanagementsystem.services;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.luan.hrmanagementsystem.database.Database;
import com.luan.hrmanagementsystem.models.MyUser;
import com.luan.hrmanagementsystem.repositories.UserRepository;

@Service
public class MyUserDetailService implements IUserService, UserDetailsService {
	private static final Logger logger = LoggerFactory.getLogger(Database.class);
	@Autowired
	private UserRepository userRepository;
	
	/*
	 * @Autowired private PasswordEncoder passwordEncoder;
	 */

	@Override
	public List<MyUser> getAllUsers() {
		try {
			List<MyUser> users = userRepository.findAll();
			logger.info("Number of users fetched: {}", users.size());
			return users;
		} catch (Exception e) {
			throw new RuntimeException("Error while fetching users", e);
		}
	}

	@Override
	public MyUser getUserById(Long userId) {
		// Kiểm tra xem nhân viên có tồn tại không
		Optional<MyUser> existingUserOptional = userRepository.findById(userId);
		if (existingUserOptional.isPresent()) {
			return existingUserOptional.orElse(null);
		} else {
			throw new IllegalArgumentException("User not found with ID: " + userId);
		}
	}

	@Override
	public MyUser createUser(MyUser user) {
		// Bổ sung logic kiểm tra trước khi lưu người dùng
		Optional<MyUser> existingUser = userRepository.findByUserName(user.getUserName());
		if (existingUser.isPresent()) {
			// Xử lý trường hợp trùng tên người dùng (hoặc bất kỳ điều kiện nào khác)
			throw new IllegalArgumentException("Username already exists: " + user.getUserName());
		}

		/*
		 * user.setEncryptedPassword(passwordEncoder.encode(user.getEncryptedPassword())
		 * );
		 */

		// Lưu người dùng mới vào cơ sở dữ liệu
		MyUser savedUser = userRepository.save(user);
		return savedUser;
	}

	@Override
	public MyUser updateUser(Long userId, MyUser updatedUser) {
		Optional<MyUser> existingUserOptional = userRepository.findById(userId);

		if (existingUserOptional.isPresent()) {
			MyUser existingUser = existingUserOptional.get();
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
	public List<MyUser> getAllUsers(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		return userRepository.findAll(pageable).getContent();
	}

	@Override
	public List<MyUser> getAllUsers(String sortBy) {
		Sort sort = Sort.by(sortBy);
		return userRepository.findAll(sort);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<MyUser> user = userRepository.findByUserName(username);
		if (user.isPresent()) {
			var userObj = user.get();
			return User.builder()
					.username(userObj.getUserName())
					.password(userObj.getEncryptedPassword())
					.roles(getRoles(userObj))
					.build();
		} else {
			throw new UsernameNotFoundException(username);
		}
	}
	private String[] getRoles(MyUser user){
		if (user.getRole() == null) {
			return new String[] {"USER"};
		}
		return user.getRole().split(",");
	}
}
