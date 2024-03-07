package com.luan.hrmanagementsystem.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luan.hrmanagementsystem.models.Employee;
import com.luan.hrmanagementsystem.repositories.EmployeeRepository;
@Service
public class EmployeeService implements IEmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Employee createEmployee(Employee employee) {
        // Kiểm tra hợp lệ trước khi lưu vào cơ sở dữ liệu
        validateEmployee(employee);
        
        return employeeRepository.save(employee);
    }

    @Override
    public Employee updateEmployee(Long id, Employee employee) {
        // Kiểm tra xem nhân viên có tồn tại không
        Optional<Employee> existingEmployeeOptional = employeeRepository.findById(id);
        if (existingEmployeeOptional.isPresent()) {
            Employee existingEmployee = existingEmployeeOptional.get();

            // Cập nhật thông tin nhân viên
            existingEmployee.setName(employee.getName());
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
        // Lấy thông tin nhân viên theo ID
        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        return employeeOptional.orElse(null);
    }

    @Override
    public List<Employee> getAllEmployees() {
        // Lấy danh sách tất cả nhân viên
        return employeeRepository.findAll();
    }

    private void validateEmployee(Employee employee) {
        // Tìm tất cả nhân viên có cùng các thuộc tính như employee
        List<Employee> foundEmployees = employeeRepository.findByName(employee.getName());

     // Kiểm tra tính đồng đạc giữa các nhân viên đã tìm được và employee
        if (foundEmployees.stream().anyMatch(existingEmployee ->
                existingEmployee.getName().equals(employee.getName()) &&
                existingEmployee.getAge() == employee.getAge()
                )) {
            throw new IllegalArgumentException("Employee with the same details already exists");
        }

    }

}