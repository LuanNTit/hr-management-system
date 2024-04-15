package com.luan.hrmanagementsystem.services;
import com.luan.hrmanagementsystem.dto.UserDTO;
import com.luan.hrmanagementsystem.models.UserEntity;
import com.luan.hrmanagementsystem.repositories.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImp(UserRepository repository) {
        this.userRepository = repository;
    }

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
    public UserDTO convertToDTO(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(userEntity.getUserId());
        userDTO.setEncryptedPassword(userEntity.getEncryptedPassword());
        userDTO.setRole(userEntity.getRole());
        userDTO.setUserName(userEntity.getUserName());
        userDTO.setEnabled(userEntity.isEnabled());
        return userDTO;
    }
    private String[] getRoles(UserDTO user){
        if (user.getRole() == null) {
            return new String[] {"USER"};
        }
        return user.getRole().split(",");
    }
}

