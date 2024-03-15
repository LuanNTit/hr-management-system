package com.luan.hrmanagementsystem.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.luan.hrmanagementsystem.services.MyUserDetailService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	@Autowired
	private MyUserDetailService userDetailService;


	@Bean
	public SecurityFilterChain securityChain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(registry -> {
			registry.requestMatchers("/home", "/api/users/register/**").permitAll();
			registry.requestMatchers("/admin/**").hasRole("ADMIN");
			registry.requestMatchers("/user/**").hasRole("USER");
			registry.requestMatchers("/api/users/**").hasRole("ADMIN");
			registry.requestMatchers("/api/employees/**").hasRole("ADMIN");
			registry.anyRequest().authenticated();
		})
		.formLogin(httpSecurityFormLoginConfigurer -> {
			httpSecurityFormLoginConfigurer.loginPage("/login")
							.successHandler(new AuthenticationSuccessHandler())
							.permitAll();
		})
		.build();
	}

//	@Bean
//	public UserDetailsService userDetailsService() {
//		UserDetails normalUser = User.builder()
//			.username("luanit")
//			.password("$2a$12$psdfXPgoH3f2Rla5M/IZVuhWHmN/XpRY54gp6wWu9zNVeZRArZp.u")
//			.roles("USER")
//			.build();
//		UserDetails adminUser = User.builder()
//				.username("admin")
//				.password("$2a$12$1TMDknPME8FlRq9w/krPs.lxe2ITcx8S78TOZNVsx.NjH2YMYFYDG")
//				.roles("ADMIN", "USER")
//				.build();
//		return new InMemoryUserDetailsManager(normalUser, adminUser);
//	}
	@Bean
	public UserDetailsService userDetailsService() {
		return userDetailService;
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailService);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}

