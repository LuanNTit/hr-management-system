package com.luan.hrmanagementsystem.services;

import java.util.List;

import com.luan.hrmanagementsystem.models.Employee;
public interface IEmployeeService {
	public Employee createEmployee(Employee employee);
	public Employee updateEmployee(Long id, Employee employee);
	public void deleteEmployee(Long id);
    	public Employee getEmployeeById(Long id);
    	public List<Employee> getAllEmployees();

    // Các phương thức khác liên quan đến quản lý nhân sự
}
