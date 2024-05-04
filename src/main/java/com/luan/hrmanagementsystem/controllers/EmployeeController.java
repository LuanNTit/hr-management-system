package com.luan.hrmanagementsystem.controllers;

import java.text.ParseException;
import java.util.List;
import java.util.Locale;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.luan.hrmanagementsystem.dto.EmployeeDTO;
import com.luan.hrmanagementsystem.dto.ResponseObject;
import com.luan.hrmanagementsystem.services.EmployeeService;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
@Tag(name = "employee-MG-services")
public class EmployeeController {
	private final EmployeeService employeeService;
	@GetMapping("/search")
	public ResponseEntity<ResponseObject> searchByName(@RequestParam String name) {
		List<EmployeeDTO> employeeSearchByName = employeeService.searchEmployee(name);
		return ResponseEntity.ok(new ResponseObject("ok", "List employees search by name successfully", employeeSearchByName));
	}
	@Operation(
		description = "Get endpoint for manager",
		summary = "This is a summary for manager get endpoint"
	)
    @GetMapping("")
	public ResponseEntity<ResponseObject> getAllEmployees(@RequestParam(defaultValue = "1") int page,
														  @RequestParam(defaultValue = "5") int size,
														  @RequestParam(defaultValue = "name") String sortField,
														  @RequestParam(defaultValue = "asc") String sortDirection,
														  @RequestParam(defaultValue = "0") int minAge,
														  @RequestParam(defaultValue = "100") int maxAge,
														  @RequestParam(defaultValue = "1970-01-01") String startDate,
														  @RequestParam(defaultValue = "3000-01-01") String endDate
														  ) throws ParseException {
		Page<EmployeeDTO> pagingEmployees = employeeService.getAllEmployees(page, size, sortField, sortDirection, minAge, maxAge, startDate, endDate);

		return ResponseEntity.ok(new ResponseObject("ok", "List paging employees successfully", pagingEmployees));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ResponseObject> getEmployeeById(@PathVariable Long id) {
		EmployeeDTO employee = employeeService.getEmployeeById(id);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ResponseObject("ok", "Query employee successfully", employee));
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
