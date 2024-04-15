package com.luan.hrmanagementsystem.controllers;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luan.hrmanagementsystem.dto.EmployeeDTO;
import com.luan.hrmanagementsystem.models.ResponseObject;
import com.luan.hrmanagementsystem.services.EmployeeService;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {
	private final EmployeeService employeeService;

    @GetMapping("")
	public ResponseEntity<ResponseObject> getAllEmployees() {
		List<EmployeeDTO> employees = employeeService.getAllEmployees();
		return ResponseEntity.ok(new ResponseObject("ok", "List employees successfully", employees));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ResponseObject> getEmployeeById(@PathVariable Long id) {
		try {
			EmployeeDTO employee = employeeService.getEmployeeById(id);
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseObject("ok", "Query employee successfully", employee));
		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ResponseObject("failed", exception.getMessage(), ""));
		}
	}

	@PostMapping("")
	public ResponseEntity<ResponseObject> createEmployee(@RequestBody EmployeeDTO employee) {
		EmployeeDTO savedEmployee = employeeService.saveEmployee(employee);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ResponseObject("ok", "Employee created successfully", savedEmployee));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ResponseObject> updateEmployee(@PathVariable Long id, @RequestBody EmployeeDTO employee) {
		EmployeeDTO updateEmployee = employeeService.updateEmployee(id, employee);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ResponseObject("ok", "Update Employee successfully", updateEmployee));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseObject> deleteEmployee(@PathVariable Long id) {
		employeeService.deleteEmployeeById(id);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ResponseObject("ok", "Delete Employee successfully", ""));
	}
}
