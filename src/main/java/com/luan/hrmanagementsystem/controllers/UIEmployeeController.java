package com.luan.hrmanagementsystem.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.luan.hrmanagementsystem.dto.EmployeeDTO;
import com.luan.hrmanagementsystem.services.EmployeeService;

@Controller
//@RequestMapping("/admin/employee")
public class UIEmployeeController {
	@Autowired
	private EmployeeService employeeService;
	// display list of employees
	@GetMapping("/employees")
	public String viewHomePage(Model model) {
		return findPaginated(1, "name", "asc", model);
	}

	@GetMapping("/showNewEmployeeForm")
	public String showNewEmployeeForm(Model model) {
		// create model attribute to bind form data
		EmployeeDTO employee = new EmployeeDTO();
		model.addAttribute("employee", employee);
		return "new_employee";
		
	}

	@PostMapping("/saveEmployee")
	public String saveEmployee(@ModelAttribute("employee") EmployeeDTO employee) {
		// save employee to database
		employeeService.saveEmployee(employee);
		return "redirect:/";
	}
	
	@GetMapping("/showFormForUpdate/{id}")
	public String showFormForUpdate(@PathVariable (value = "id") long id, Model model) {
		// get employee from the service
		EmployeeDTO employee = employeeService.getEmployeeById(id);
		// set employee as model attribute to pre-populate the form
		model.addAttribute("employee", employee);
		return "update_employee";
	}
	
	@GetMapping("/deleteEmployee/{id}")
	public String deleteEmployee(@PathVariable(value = "id") long id ) {
		// call delete employee method
		this.employeeService.deleteEmployeeById(id);
		return "redirect:/";
	}

	// /page/1?sortField=name&sortDir=asc
	
	@GetMapping("/page/{pageNo}")
	public String findPaginated(@PathVariable (value = "pageNo") int pageNo, 
		@RequestParam("sortField") String sortField,
		@RequestParam("sortDir") String sortDir,
		Model model) {
		int pageSize = 5;
		Page<EmployeeDTO> page = employeeService.findPaginated(pageNo, pageSize, sortField, sortDir);
		List<EmployeeDTO> listEmployees = page.getContent();
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
		model.addAttribute("listEmployees", listEmployees);
		return "list_employees";
	}
}
