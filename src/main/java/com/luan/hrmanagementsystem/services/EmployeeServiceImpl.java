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

import com.luan.hrmanagementsystem.dto.EmployeeDTO;
import com.luan.hrmanagementsystem.models.EmployeeEntity;
import com.luan.hrmanagementsystem.repositories.EmployeeRepository;
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
	private final EmployeeRepository employeeRepository;
	
	@Override
	public List<EmployeeDTO> getAllEmployees() {
		List<EmployeeEntity> employees = employeeRepository.findAll();
		return employees.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	@Override
	public EmployeeDTO getEmployeeById(long id) {
		Optional<EmployeeEntity> optional = employeeRepository.findById(id);
		if (optional.isPresent()) {
			EmployeeEntity employee = optional.get();
			return convertToDTO(employee);
		} else {
			throw new RuntimeException("Employee not found for id :: " + id);
		}

	}

	@Override
	public void deleteEmployeeById(long id) {
		this.employeeRepository.deleteById(id);
		
	}

	@Override
	public Page<EmployeeDTO> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
			Sort.by(sortField).descending();
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        Page<EmployeeEntity> pages = this.employeeRepository.findAll(pageable);
        List<EmployeeDTO> dtos = pages.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, pages.getTotalElements());
	}

	@Override
	public List<EmployeeDTO> searchEmployee(String name) {
		List<EmployeeEntity> employeeByNames = employeeRepository.findByNameContaining(name);
		return employeeByNames.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	@Override
	public Page<EmployeeDTO> getAllEmployees(int page, int size) {
		Page<EmployeeEntity> pageEmployeeEntity = employeeRepository.findAll(PageRequest.of(page, size));
		return pageEmployeeEntity.map(this::convertToDTO);
	}

	@Override
	public EmployeeDTO saveEmployee(EmployeeDTO employeeDTO) {
		EmployeeEntity employeeEntity = new EmployeeEntity();
		employeeEntity.setId(employeeDTO.getId());
		employeeEntity.setName(employeeDTO.getName());
		employeeEntity.setAge(employeeDTO.getAge());
		employeeEntity.setDateOfBirth(employeeDTO.getDateOfBirth());
		employeeEntity.setPosition(employeeDTO.getPosition());
		employeeEntity.setSalary(employeeDTO.getSalary());
		EmployeeEntity employee = employeeRepository.save(employeeEntity);
		return convertToDTO(employee);
	}
	
	public EmployeeDTO convertToDTO(EmployeeEntity employeeEntity) {
		if (employeeEntity == null) {
			return null;
		}
		EmployeeDTO employeeDTO = new EmployeeDTO();
		employeeDTO.setId(employeeEntity.getId());
		employeeDTO.setName(employeeEntity.getName());
		employeeDTO.setDateOfBirth(employeeEntity.getDateOfBirth());
		employeeDTO.setAge(employeeEntity.getAge());
		employeeDTO.setPosition(employeeEntity.getPosition());
		employeeDTO.setSalary(employeeEntity.getSalary());
		return employeeDTO;
	}

	@Override
	public EmployeeDTO updateEmployee(Long id, EmployeeDTO employee) {
		// find employee by id
		Optional<EmployeeEntity> findEmployee = this.employeeRepository.findById(id);
		if (findEmployee.isPresent()) {
			EmployeeEntity updateEmmployeeEntity = findEmployee.get();
			updateEmmployeeEntity.setName(employee.getName());
			updateEmmployeeEntity.setDateOfBirth(employee.getDateOfBirth());
			updateEmmployeeEntity.setPosition(employee.getPosition());
			updateEmmployeeEntity.setAge(employee.getAge());
			updateEmmployeeEntity.setSalary(employee.getSalary());
			return convertToDTO(this.employeeRepository.save(updateEmmployeeEntity));
		}
		return null;
	}
}