package com.luan.hrmanagementsystem.mapper;

import com.luan.hrmanagementsystem.dto.EmployeeDTO;
import com.luan.hrmanagementsystem.models.EmployeeEntity;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public interface EmployeeMapper {
    EmployeeDTO mapToEmployeeDTO(EmployeeEntity employeeEntity);
    EmployeeEntity mapToEmployeeEntity(EmployeeDTO employeeDTO);
    List<EmployeeDTO> mapToEmployeeDTOs(List<EmployeeEntity> employeeEntities);
    List<EmployeeEntity> mapToEmployeeEntities(List<EmployeeDTO> employeeDTOs);
}
