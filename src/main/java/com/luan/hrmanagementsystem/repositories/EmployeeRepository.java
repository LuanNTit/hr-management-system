package com.luan.hrmanagementsystem.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.luan.hrmanagementsystem.models.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}

