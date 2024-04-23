package com.luan.Schedule.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
	info = @Info(
		title = "Exam API",
		description = "Doing CRUD Operation",
		summary = "This book-api will add, delete, create and update",
		termsOfService = "T&C",
		contact = @Contact(
			name = "Luan NT",
			email = "nguyentranluanit@gmail.com"
		),
		license = @License(
			name = "Your License No"
		),
		version = "v1"
	),
	servers = {
		@Server(
			description = "Dev",
			url = "http://localhost:8008"
		),
		@Server(
			description = "Test",
			url = "http://localhost:8080"
		),
	}
)
public class OpenApiConfig {
}
