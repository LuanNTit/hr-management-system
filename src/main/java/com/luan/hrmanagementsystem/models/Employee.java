package com.luan.hrmanagementsystem.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Employee {
    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)// auto-increment
	// you can also use "sequence"
	@SequenceGenerator(
		name = "employee_sequence",
		sequenceName = "employee_sequence",
		allocationSize = 1 //increment by 1
	)
	@GeneratedValue(
		strategy = GenerationType.SEQUENCE,
		generator = "employee_sequence"
	)
    private Long id;
    private String name;
    private Date dateOfBirth;
    private String position;
    private double salary;
    
    public Employee(String name, String dateOfBirth, String position, double salary) {
        this.name = name;
        this.position = position;
        this.salary = salary;

        // Chuyển đổi chuỗi ngày tháng thành đối tượng Date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            this.dateOfBirth = sdf.parse(dateOfBirth);
        } catch (ParseException e) {
            e.printStackTrace(); // Xử lý nếu có lỗi chuyển đổi
        }
    }

    public Employee(Long id, String name, String dateOfBirth, String position, double salary) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.salary = salary;

        // Chuyển đổi chuỗi ngày tháng thành đối tượng Date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            this.dateOfBirth = sdf.parse(dateOfBirth);
        } catch (ParseException e) {
            e.printStackTrace(); // Xử lý nếu có lỗi chuyển đổi
        }
    }

	public Employee(String name, Date dateOfBirth, String position, double salary) {
		super();
		this.name = name;
		this.dateOfBirth = dateOfBirth;
		this.position = position;
		this.salary = salary;
	}
    
    // Getters and setters
}
