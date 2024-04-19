package com.luan.hrmanagementsystem.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.luan.hrmanagementsystem.dto.EmployeeDTO;

public interface EmployeeService {
	List<EmployeeDTO> getAllEmployees();
	EmployeeDTO saveEmployee(EmployeeDTO employee);
	EmployeeDTO updateEmployee(Long id, EmployeeDTO employee);
	EmployeeDTO getEmployeeById(long id);
	void deleteEmployeeById(long id);
	Page<EmployeeDTO> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);
	List<EmployeeDTO> searchEmployee(String name);
	Page<EmployeeDTO> getAllEmployees(int page, int size);
}
