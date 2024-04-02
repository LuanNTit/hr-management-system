package com.luan.hrmanagementsystem.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmployeeDTO {
	private Long id;
    private String name;
    private Date dateOfBirth;
    private String position;
    private double salary;
    private int age;
}
