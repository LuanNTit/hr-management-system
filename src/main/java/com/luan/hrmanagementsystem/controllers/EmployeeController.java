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
import com.luan.hrmanagementsystem.services.IEmployeeService;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

	@Autowired
	private IEmployeeService employeeService;

	@GetMapping("")
	public ResponseEntity<ResponseObject> getAllEmployees() {
		try {
			List<Employee> employees = employeeService.getAllEmployees();
			return ResponseEntity.ok(new ResponseObject("ok", "List employees successfully", employees));
		} catch (Exception exception) {
			return ResponseEntity.ok(new ResponseObject("failed", exception.getMessage(), new String[] {}));
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<ResponseObject> getEmployeeById(@PathVariable Long id) {
		try {
			Employee employee = employeeService.getEmployeeById(id);
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseObject("ok", "Query employee successfully", employee));
		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ResponseObject("failed", exception.getMessage(), ""));
		}
	}

	@PostMapping("")
	public ResponseEntity<ResponseObject> createEmployee(@RequestBody Employee employee) {
		try {
			Employee savedEmployee = employeeService.createEmployee(employee);
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseObject("ok", "Employee created successfully", savedEmployee));
		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
					.body(new ResponseObject("failed", exception.getMessage(), ""));
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<ResponseObject> updateEmployee(@PathVariable Long id, @RequestBody Employee newEmployee) {
		try {
			Employee updateEmployee = employeeService.updateEmployee(id, newEmployee);
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseObject("ok", "Update Employee successfully", updateEmployee));
		} catch (IllegalArgumentException e) {
			// Xử lý ngoại lệ khi Employee not found with ID
			if (e.getMessage().contains("Employee not found with ID")) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new ResponseObject("failed", e.getMessage(), ""));
			}
			// Xử lý ngoại lệ khi Employee with the same details already exists
			if (e.getMessage().contains("Employee with the same details already exists")) {
				return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
						.body(new ResponseObject("failed", e.getMessage(), ""));
			}
			// Xử lý các trường hợp khác nếu cần
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseObject("failed", "Internal Server Error", ""));
		} catch (Exception exception2) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ResponseObject("failed", exception2.getMessage(), ""));
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseObject> deleteEmployee(@PathVariable Long id) {
		try {
			employeeService.deleteEmployee(id);
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseObject("ok", "Delete product successfully", ""));
		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ResponseObject("failed", exception.getMessage(), ""));
		}
	}
}
