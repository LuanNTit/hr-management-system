package com.luan.hrmanagementsystem.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.luan.hrmanagementsystem.dto.UserDTO;
import com.luan.hrmanagementsystem.models.UserEntity;
import com.luan.hrmanagementsystem.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService,  UserDetailsService {
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<UserEntity> user = userRepository.findByUserName(username);
		if (user.isPresent()) {
			var userObj = convertToDTO(user.get());
			return User.builder()
					.username(userObj.getUserName())
					.password(userObj.getEncryptedPassword())
					.roles(getRoles(userObj))
					.build();
		} else {
			throw new UsernameNotFoundException(username);
		}
	}
	
	private String[] getRoles(UserDTO user){
		if (user.getRole() == null) {
			return new String[] {"USER"};
		}
		return user.getRole().split(",");
	}

	@Override
	public void deleteUserById(Long id) {
		this.userRepository.deleteById(id);
	}

	@Override
	public Page<UserDTO> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
			Sort.by(sortField).descending();
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        Page<UserEntity> pages = this.userRepository.findAll(pageable);
        List<UserDTO> dtos = pages.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, pages.getTotalElements());
	}

	@Override
	public List<UserDTO> getAllUsers() {
		List<UserEntity> users = userRepository.findAll();
		return users.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	@Override
	public UserDTO getUserById(Long id) {
		Optional<UserEntity> optional = userRepository.findById(id);
		UserEntity user = null;
		if (optional.isPresent()) {
			user = optional.get();
		} else {
			throw new RuntimeException("Employee not found for id :: " + id);
		}
		return convertToDTO(user);
	}

	@Override
	public UserDTO saveUser(UserDTO userDTO) {
		UserEntity userEntity = new UserEntity();
		userEntity.setUserId(userDTO.getUserId());
		userEntity.setUserName(userDTO.getUserName());
		userEntity.setEncryptedPassword(userDTO.getEncryptedPassword());
		userEntity.setRole(userDTO.getRole());
		userEntity.setEnabled(userDTO.isEnabled());
		UserEntity user = this.userRepository.save(userEntity);
		return convertToDTO(user);
	}
	
	public UserDTO convertToDTO(UserEntity userEntity) {
		if (userEntity == null) {
			return null;
		}
		UserDTO userDTO = new UserDTO();
		userDTO.setUserId(userEntity.getUserId());
		userDTO.setUserName(userEntity.getUserName());
		userDTO.setEncryptedPassword(userEntity.getEncryptedPassword());
		userDTO.setRole(userEntity.getRole());
		userDTO.setEnabled(userEntity.isEnabled());
		return userDTO;
	}
	
}
