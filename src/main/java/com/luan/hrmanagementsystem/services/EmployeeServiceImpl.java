package com.luan.hrmanagementsystem.services;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Filter;
import java.util.stream.Collectors;

import com.luan.hrmanagementsystem.exception.DuplicateRecordException;
import com.luan.hrmanagementsystem.exception.NotFoundException;
import com.luan.hrmanagementsystem.mapper.EmployeeMapper;
import com.luan.hrmanagementsystem.mapper.UserMapper;
import javassist.bytecode.DuplicateMemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.luan.hrmanagementsystem.dto.EmployeeDTO;
import com.luan.hrmanagementsystem.models.EmployeeEntity;
import com.luan.hrmanagementsystem.repositories.EmployeeRepository;
@Component
public class EmployeeServiceImpl implements EmployeeService {
	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private EmployeeMapper employeeMapper;

	@Override
	public List<EmployeeDTO> getAllEmployees() {
		List<EmployeeEntity> employees = employeeRepository.findAll();
		return employeeMapper.mapToEmployeeDTOs(employees);
	}

	@Override
	public EmployeeDTO getEmployeeById(long id) {
		Optional<EmployeeEntity> optional = employeeRepository.findById(id);
		if (optional.isPresent()) {
			EmployeeEntity employee = optional.get();
			return employeeMapper.mapToEmployeeDTO(employee);
		}
		throw new NotFoundException("Employee not found for id : " + id);
	}

	@Override
	public void deleteEmployeeById(long id) {
		this.employeeRepository.deleteById(id);
		
	}

	@Override
	public List<EmployeeDTO> searchEmployee(String name) {
		List<EmployeeEntity> employeeByNames = employeeRepository.findByNameContaining(name);
		return employeeMapper.mapToEmployeeDTOs(employeeByNames);
	}

	@Override
	public Page<EmployeeDTO> getAllEmployees(int page, int size,
											 String sortField, String sortDirection,
											 int minAge, int maxAge,
											 String startDate, String endDate) throws ParseException {
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
				Sort.by(sortField).descending();
		PageRequest pageRequest = PageRequest.of(page - 1, size, sort);

		// convert type string to date
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date startDated = dateFormat.parse(startDate);
		Date endDated = dateFormat.parse(endDate);
		// Get one page of the employee list
		Page<EmployeeEntity> employeesInRange = employeeRepository.findByDateOfBirthBetween(startDated, endDated, pageRequest);
		// Filter employee list by age condition from 20 to 30 and convert to DTO
		List<EmployeeDTO> filteredEmployees = employeesInRange
				.getContent()
				.stream()
				.filter(emp -> emp.getAge() >= minAge && emp.getAge() <= maxAge)
				.map(employeeMapper::mapToEmployeeDTO)
				.toList();
		// Create a new page from the filtered and returned list
		return new PageImpl<>(filteredEmployees, pageRequest, employeesInRange.getTotalElements());
	}

	@Override
	public EmployeeDTO saveEmployee(EmployeeDTO employeeDTO) {
		EmployeeEntity employeeEntity = employeeMapper.mapToEmployeeEntity(employeeDTO);
		EmployeeEntity employee = employeeRepository.save(employeeEntity);
		return employeeMapper.mapToEmployeeDTO(employee);
	}

	@Override
	public EmployeeDTO updateEmployee(Long id, EmployeeDTO employee) {
		// find employee by id
		Optional<EmployeeEntity> findEmployee = this.employeeRepository.findById(id);
		if (findEmployee.isPresent()) {
			EmployeeEntity updateEmployeeEntity = employeeMapper.mapToEmployeeEntity(employee);
			updateEmployeeEntity.setId(id);
			return employeeMapper.mapToEmployeeDTO(this.employeeRepository.save(updateEmployeeEntity));
		}
		return null;
	}
}