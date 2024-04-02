package com.luan.hrmanagementsystem.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.luan.hrmanagementsystem.models.EmployeeEntity;
import com.luan.hrmanagementsystem.repositories.EmployeeRepository;

@Configuration
public class Database {
	//Logger
		private static final Logger logger = LoggerFactory.getLogger(Database.class);
		@Bean
		CommandLineRunner initDatabase(EmployeeRepository employeeRepository) {
			return new CommandLineRunner() {
				
				@Override
				public void run(String... args) throws Exception {
					// Dữ liệu mẫu
//			        String name = "Nguyen Tran Luan";
//			        String dateOfBirthString = "2002-01-20"; // Định dạng: yyyy-MM-dd
//			        String position = "Software Developer";
//			        double salary = 60000.0;
//
//			        // Tạo đối tượng Employee bằng cách sử dụng constructor
//			        Employee employee = new Employee(name, dateOfBirthString, position, salary);
//			        Employee employee2 = new Employee("Jane Smith", new Date(), "HR Manager", 60000.0);
//					logger.info("insert data: {}", employeeRepository.save(employee));
//					logger.info("insert data: {}", employeeRepository.save(employee2));
				}
			};
		}
}
