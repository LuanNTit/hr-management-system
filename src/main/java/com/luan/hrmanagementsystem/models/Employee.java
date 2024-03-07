package com.luan.hrmanagementsystem.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
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
    
    @Transient
    private int age;

    @PrePersist
    @PreUpdate
    private void calculateAge() {
    	Calendar currentDate = Calendar.getInstance();
        Calendar birthDate = Calendar.getInstance();
        birthDate.setTime(dateOfBirth);

        age = currentDate.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);

        // Kiểm tra xem đã qua ngày sinh nhật chưa để điều chỉnh giá trị age
        if (currentDate.get(Calendar.DAY_OF_YEAR) < birthDate.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
    }
    
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

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Employee other = (Employee) obj;
		return age == other.age && Objects.equals(dateOfBirth, other.dateOfBirth)
				&& Objects.equals(name, other.name) && Objects.equals(position, other.position)
				&& Double.doubleToLongBits(salary) == Double.doubleToLongBits(other.salary);
	}

	@Override
	public int hashCode() {
		return Objects.hash(age, dateOfBirth, id, name, position, salary);
	}
    
    // Getters and setters
	
}
