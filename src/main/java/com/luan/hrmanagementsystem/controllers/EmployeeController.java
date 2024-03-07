package com.luan.hrmanagementsystem.controllers;

import java.util.List;

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

import com.luan.hrmanagementsystem.models.Employee;
import com.luan.hrmanagementsystem.models.ResponseObject;
import com.luan.hrmanagementsystem.repositories.EmployeeRepository;
import com.luan.hrmanagementsystem.services.IEmployeeService;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

	@Autowired
	private IEmployeeService employeeService;

	@GetMapping("")
	public List<Employee> getAllEmployees() {
		return employeeService.getAllEmployees();
	}

	@GetMapping("/{id}")
	public Employee getEmployeeById(@PathVariable Long id) {
		return employeeService.getEmployeeById(id);
	}

	@PostMapping("")
	public ResponseEntity<ResponseObject> createEmployee(@RequestBody Employee employee) {
	    try {
	        Employee savedEmployee = employeeService.createEmployee(employee);
	        return ResponseEntity.status(HttpStatus.OK)
	                .body(new ResponseObject("ok", "Employee created successfully", savedEmployee));
	    } catch (Exception exception) {
	        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
	                .body(new ResponseObject("error", exception.getMessage(), ""));
	    }
	}


	@PutMapping("/{id}")
	public Employee updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
		employee.setId(id);
		return employeeService.updateEmployee(id, employee);
	}

	@DeleteMapping("/{id}")
	public void deleteEmployee(@PathVariable Long id) {
		employeeService.deleteEmployee(id);
	}
}
