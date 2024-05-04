package com.luan.hrmanagementsystem.services;

import java.util.List;
import java.util.Optional;

import com.luan.hrmanagementsystem.exception.DuplicateRecordException;
import com.luan.hrmanagementsystem.exception.NotFoundException;
import com.luan.hrmanagementsystem.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import com.luan.hrmanagementsystem.dto.UserDTO;
import com.luan.hrmanagementsystem.models.UserEntity;
import com.luan.hrmanagementsystem.repositories.UserRepository;

@Component
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserMapper userMapper;
	public void deleteUserById(Long id) {
		this.userRepository.deleteById(id);
	}

	@Override
	public UserDTO updateUser(Long id, UserDTO user) {
		// find employee by id
		Optional<UserEntity> findUser = this.userRepository.findById(id);
		if (findUser.isPresent()) {
			UserEntity updateUserEntity = userMapper.mapToUserEntity(user);
			if (updateUserEntity.getUserName().equals(findUser.get().getUserName())) {
				throw new DuplicateRecordException("User already exists in the system");
			}
			updateUserEntity.setUserId(id);
			return userMapper.mapToUserDTO(this.userRepository.save(updateUserEntity));
		}
		return null;
	}

	@Override
	public List<UserDTO> searchUser(String username) {
		List<UserEntity> userByUserNames = userRepository.findByUserNameContaining(username);
		return userMapper.mapToUserDTOs(userByUserNames);
	}

	@Override
	public Page<UserDTO> getAllUsers(int page, int size, String sortField, String sortDirection) {
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
				Sort.by(sortField).descending();
		Page<UserEntity> pageEmployeeEntity = userRepository.findAllBy(PageRequest.of(page - 1, size, sort));
		return pageEmployeeEntity.map(userMapper::mapToUserDTO);
	}

	@Override
	public List<UserDTO> getAllUsers() {
		List<UserEntity> users = userRepository.findAll();
		return userMapper.mapToUserDTOs(users);
	}

	@Override
	public UserDTO getUserById(Long id) {
		Optional<UserEntity> optional = userRepository.findById(id);

		if (optional.isPresent()) {
			UserEntity user = optional.get();
			return userMapper.mapToUserDTO(user);
		}
		throw new NotFoundException("Employee not found for id : " + id);
	}

	@Override
	public UserDTO saveUser(UserDTO userDTO) {
		UserEntity userEntity = userMapper.mapToUserEntity(userDTO);
		userEntity.setEncryptedPassword(userDTO.getEncryptedPassword());
		UserEntity user = this.userRepository.save(userEntity);
		return userMapper.mapToUserDTO(user);
	}
}
