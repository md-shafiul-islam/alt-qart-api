package com.altqart.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.altqart.services.UserServices;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private static final String[] PUBLIC_MATCHERS = {

			"/build/**", "/dist/**", "/plugins/**", "/asset-img/**", "/api/v1/users/login", "/uimages/**",
			"/api/v1/users/sign-up", "/api/v1/products", "/api/v1/products/**", "/api/v1/categories",
			"/api/v1/categories/**", "/api/v1/carts", "/api/v1/carts/**", "/api/v1/areas", "/api/v1/areas/**",
			"/api/v1/cities", "/api/v1/cities/**", "/api/v1/zones", "/api/v1/zones/**", "/api/v1/addresses",
			"/api/v1/addresses/**", "/api/v1/orders", "/api/v1/orders/**", "/api/v1/pathao/webhook",
			"/api/v1/pathao/price"

	};

	@Autowired
	private UserServices userSecurityServices;

	@Autowired
	private JwtAuthenticationEntryPoint unauthorizedHandler;

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.cors().and().csrf().disable().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
				.requestMatchers(PUBLIC_MATCHERS).permitAll().anyRequest().authenticated();

		httpSecurity.httpBasic(basic -> basic.authenticationEntryPoint(unauthorizedHandler));
		httpSecurity.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

		return httpSecurity.build();

	}

	@Bean
	public AuthenticationProvider authenticationProvider(AuthenticationManagerBuilder auth) throws Exception {

		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userSecurityServices);
		authenticationProvider.setPasswordEncoder(getPasswordEncoder());

		return authenticationProvider;
	}

	@Bean
	public PasswordEncoder getPasswordEncoder() {

		return new BCryptPasswordEncoder();
	}

	@Bean(BeanIds.AUTHENTICATION_MANAGER)
	protected AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

		return configuration.getAuthenticationManager();
	}

}