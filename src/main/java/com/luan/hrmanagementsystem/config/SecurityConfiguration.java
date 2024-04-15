package com.luan.hrmanagementsystem.config;

import com.luan.hrmanagementsystem.services.UserDetailsServiceImp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.luan.hrmanagementsystem.filter.JwtAuthenticationFilter;
import com.luan.hrmanagementsystem.services.UserServiceImpl;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
	private final UserDetailsServiceImp userDetailsServiceImp;

	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	private final CustomLogoutHandler logoutHandler;

	@Bean
	public SecurityFilterChain securityChain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(registry -> {
					registry.requestMatchers("/home/**", "/login/**", "/register/**", "/api/logout/**").permitAll();
					registry.requestMatchers("/admin/**").hasRole("ADMIN");
					registry.requestMatchers("/user/**").hasRole("USER");
					registry.requestMatchers("/api/**").hasRole("ADMIN");
					registry.anyRequest().authenticated();
				}).userDetailsService(userDetailsServiceImp)
				.sessionManagement(session->session
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
				.exceptionHandling(
						e->e.accessDeniedHandler(
										(request, response, accessDeniedException)->response.setStatus(403)
								)
								.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
				.logout(l->l
						.logoutUrl("/logout")
						.addLogoutHandler(logoutHandler)
						.logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext()
						))
				.build();
	}

	@Bean
	public UserDetailsService userDetailsService() {
		return userDetailsServiceImp;
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsServiceImp);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
