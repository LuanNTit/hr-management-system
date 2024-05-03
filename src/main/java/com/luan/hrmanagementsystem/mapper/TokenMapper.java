package com.luan.hrmanagementsystem.mapper;

import com.luan.hrmanagementsystem.dto.TokenDTO;
import com.luan.hrmanagementsystem.models.TokenEntity;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public interface TokenMapper {
    List<TokenDTO> mapToTokenDTOs(List<TokenEntity> tokenEntities);
}
