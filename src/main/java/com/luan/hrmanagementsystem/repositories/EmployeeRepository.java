package com.luan.hrmanagementsystem.repositories;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.luan.hrmanagementsystem.models.EmployeeEntity;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {
    List<EmployeeEntity> findByNameContaining(String name);
    Page<EmployeeEntity> findAllBy(Pageable pageable);
    Page<EmployeeEntity> findByDateOfBirthBetween(Date startDate, Date endDate, Pageable pageable);
}

