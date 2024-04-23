package com.luan.hrmanagementsystem.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.luan.hrmanagementsystem.dto.UserDTO;
import com.luan.hrmanagementsystem.models.UserEntity;
import com.luan.hrmanagementsystem.repositories.UserRepository;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;

	@Override
	public void deleteUserById(Long id) {
		this.userRepository.deleteById(id);
	}

	@Override
	public UserDTO updateUser(Long id, UserDTO user) {
		// find employee by id
		Optional<UserEntity> findUser = this.userRepository.findById(id);
		if (findUser.isPresent()) {
			UserEntity updateUserEntity = findUser.get();
			updateUserEntity.setUserName(user.getUserName());
			updateUserEntity.setEmail(user.getEmail());
			updateUserEntity.setRole(user.getRole());
			updateUserEntity.setLocked(user.isLocked());
			updateUserEntity.setEnabled(user.isEnabled());
			updateUserEntity.setEncryptedPassword(user.getEncryptedPassword());
			return convertToDTO(this.userRepository.save(updateUserEntity));
		}
		return null;
	}

	@Override
	public List<UserDTO> searchUser(String username) {
		List<UserEntity> userByUserNames = userRepository.findByUserNameContaining(username);
		return userByUserNames.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	@Override
	public Page<UserDTO> getAllUsers(int page, int size, String sortField, String sortDirection) {
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
				Sort.by(sortField).descending();
		Page<UserEntity> pageEmployeeEntity = userRepository.findAllBy(PageRequest.of(page - 1, size, sort));
		return pageEmployeeEntity.map(this::convertToDTO);
	}

	@Override
	public List<UserDTO> getAllUsers() {
		List<UserEntity> users = userRepository.findAll();
		return users.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	@Override
	public UserDTO getUserById(Long id) {
		Optional<UserEntity> optional = userRepository.findById(id);

		if (optional.isPresent()) {
			UserEntity user = optional.get();
			return convertToDTO(user);
		} else {
			throw new RuntimeException("Employee not found for id :: " + id);
		}
	}

	@Override
	public UserDTO saveUser(UserDTO userDTO) {
		UserEntity userEntity = new UserEntity();
		userEntity.setUserId(userDTO.getUserId());
		userEntity.setUserName(userDTO.getUserName());
		userEntity.setEncryptedPassword(userDTO.getEncryptedPassword());
		userEntity.setRole(userDTO.getRole());
		userEntity.setEnabled(userDTO.isEnabled());
		userEntity.setEmail(userDTO.getEmail());
		UserEntity user = this.userRepository.save(userEntity);
		return convertToDTO(user);
	}
	
	public UserDTO convertToDTO(UserEntity userEntity) {
		if (userEntity != null) {
			UserDTO userDTO = new UserDTO();
			userDTO.setUserId(userEntity.getUserId());
			userDTO.setUserName(userEntity.getUserName());
			userDTO.setEncryptedPassword(userEntity.getEncryptedPassword());
			userDTO.setRole(userEntity.getRole());
			userDTO.setEmail(userEntity.getEmail());
			userDTO.setLocked(userEntity.isLocked());
			userDTO.setEnabled(userEntity.isEnabled());
			return userDTO;
		}
		else {
			throw new RuntimeException("User entity null");
		}
	}
}
