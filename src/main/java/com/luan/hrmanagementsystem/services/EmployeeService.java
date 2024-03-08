package com.luan.hrmanagementsystem.services;

import java.util.List;
import java.util.Optional;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.luan.hrmanagementsystem.models.Employee;
import com.luan.hrmanagementsystem.repositories.EmployeeRepository;
@Service
public class EmployeeService implements IEmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Employee createEmployee(Employee employee) {
        try {
            // Kiểm tra hợp lệ trước khi lưu vào cơ sở dữ liệu
            validateEmployee(employee);

            return employeeRepository.save(employee);
        } catch (DataIntegrityViolationException | ConstraintViolationException e) {
            // Xử lý ngoại lệ khi vi phạm ràng buộc cơ sở dữ liệu
            throw new IllegalArgumentException("Duplicate employee details or data integrity violation");
        } catch (DataAccessException e) {
            // Xử lý ngoại lệ cơ sở dữ liệu khác
            throw new RuntimeException("Error while accessing the database", e);
        }
    }


    @Override
    public Employee updateEmployee(Long id, Employee employee) {
        // Kiểm tra xem nhân viên có tồn tại không
        Optional<Employee> existingEmployeeOptional = employeeRepository.findById(id);
        if (existingEmployeeOptional.isPresent()) {
            Employee existingEmployee = existingEmployeeOptional.get();
            
            // kiểm tra thông tin có bị trùng lặp không
            validateEmployee(employee);
            
            // Cập nhật thông tin nhân viên
            existingEmployee.setName(employee.getName());
            existingEmployee.setDateOfBirth(employee.getDateOfBirth());
            existingEmployee.setPosition(employee.getPosition());
            existingEmployee.setSalary(employee.getSalary());
            existingEmployee.setAge(employee.getAge());
            // Cập nhật các trường khác tùy theo yêu cầu

            // Lưu vào cơ sở dữ liệu
            employeeRepository.save(existingEmployee);
            return existingEmployee;
        } else {
            throw new IllegalArgumentException("Employee not found with ID: " + id);
        }
    }

    @Override
    public void deleteEmployee(Long id) {
        // Kiểm tra xem nhân viên có tồn tại không
        Optional<Employee> existingEmployeeOptional = employeeRepository.findById(id);
        if (existingEmployeeOptional.isPresent()) {
            // Xóa nhân viên nếu tồn tại
            employeeRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Employee not found with ID: " + id);
        }
    }

    @Override
    public Employee getEmployeeById(Long id) {
    	// Kiểm tra xem nhân viên có tồn tại không
        Optional<Employee> existingEmployeeOptional = employeeRepository.findById(id);
        if (existingEmployeeOptional.isPresent()) {
        	return existingEmployeeOptional.orElse(null);
        } else {
            throw new IllegalArgumentException("Employee not found with ID: " + id);
        }
    }

    @Override
    public List<Employee> getAllEmployees() {
        try {
            // Lấy danh sách tất cả nhân viên
            return employeeRepository.findAll();
        } catch (DataAccessException e) {
            // Xử lý ngoại lệ chung của truy xuất cơ sở dữ liệu
            throw new RuntimeException("Error while accessing the database", e);
        }
    }

    private void validateEmployee(Employee employee) {
        // Tìm tất cả nhân viên có cùng các thuộc tính như employee
        List<Employee> foundEmployees = employeeRepository.findByName(employee.getName());

     // Kiểm tra tính đồng đạc giữa các nhân viên đã tìm được và employee
        if (foundEmployees.stream().anyMatch(existingEmployee ->
                existingEmployee.getName().equals(employee.getName()) &&
        		existingEmployee.getDateOfBirth().getTime() == employee.getDateOfBirth().getTime()
                )) {
            throw new IllegalArgumentException("Employee with the same details already exists");
        }

    }

}