package com.luan.hrmanagementsystem.services;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.luan.hrmanagementsystem.database.Database;
import com.luan.hrmanagementsystem.models.User;
import com.luan.hrmanagementsystem.repositories.UserRepository;

@Service
public class UserService implements IUserService {
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
    	User existingUser = userRepository.findByUserName(user.getUserName());
    	if (existingUser != null) {
    	    // Xử lý trường hợp trùng tên người dùng (hoặc bất kỳ điều kiện nào khác)
    	    throw new IllegalArgumentException("Username already exists: " + user.getUserName());
    	}

    	// Mã hóa mật khẩu trước khi lưu vào cơ sở dữ liệu
    	String encodedPassword = BCrypt.hashpw(user.getEncryptedPassword(), BCrypt.gensalt());
    	user.setEncryptedPassword(encodedPassword);

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
	public User registerUser(String username, String password) {
		User user = new User(username, password);
		user.setEnabled(true);
		
		return createUser(user);
	}

	@Override
	public User loginUser(String username, String password) {
		User user = userRepository.findByUserName(username);
	    if (user != null && BCrypt.checkpw(password, user.getEncryptedPassword())) {
	        return user;
	    } else {
	        throw new RuntimeException("Invalid username or password");
	    }
	}

	@Override
	public User registerUser(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User loginUser(User user) {
		// TODO Auto-generated method stub
		return null;
	}
}
