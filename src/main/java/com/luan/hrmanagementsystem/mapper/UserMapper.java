package com.luan.hrmanagementsystem.mapper;

import com.luan.hrmanagementsystem.dto.UserDTO;
import com.luan.hrmanagementsystem.models.UserEntity;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
@Mapper(componentModel = "spring")
@Component
public interface UserMapper {
    UserEntity mapToUserEntity(UserDTO userDTO);
    UserDTO mapToUserDTO(UserEntity userEntity);
    List<UserDTO> mapToUserDTOs(List<UserEntity> userEntities);
    List<UserEntity> mapToUserEntities(List<UserDTO> userDTOs);
}
