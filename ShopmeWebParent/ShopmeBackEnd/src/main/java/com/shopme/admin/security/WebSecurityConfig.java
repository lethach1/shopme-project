package com.shopme.admin.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

	@Bean
	UserDetailsService userDetailsService() {
		return new ShopmeUserDetailsService();
	}

	
	/*
	 * DaoAuthenticationProvider:
	 * 
	 * Trong phương thức authenticationProvider(), UserDetailsService được set cho
	 * DaoAuthenticationProvider.
	 * authProvider.setUserDetailsService(userDetailsService()); Điều này kết nối
	 * UserDetailsService với quá trình xác thực.
	 * Toàn bộ quy trình xác thực (username, password) sẽ dc xử lý bên trong phương thức aythenticate
	 */
	@Bean
	DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authenticationProvider(authenticationProvider());

		http.authorizeHttpRequests(authorize -> authorize
				.requestMatchers("/states/list_by_country/**").hasAnyAuthority("Admin", "Salesperson")
				.requestMatchers("/users/**", "/settings/**", "/countries/**", "/states/**").hasAuthority("Admin")
				.requestMatchers("/categories/**", "/brands/**").hasAnyAuthority("Admin", "Editor")
				
				.requestMatchers("/products/new", "/products/delete/**").hasAnyAuthority("Admin", "Editor")
				
				.requestMatchers("/products/edit/**", "/products/save", "/products/check_unique")
					.hasAnyAuthority("Admin", "Editor", "Salesperson")
					
				.requestMatchers("/products", "/products/", "/products/detail/**", "/products/page/**")
					.hasAnyAuthority("Admin", "Editor", "Salesperson", "Shipper")
					
				.requestMatchers("/products/**").hasAnyAuthority("Admin", "Editor")
				
				.requestMatchers("/orders", "/orders/", "/orders/page/**", "/orders/detail/**").hasAnyAuthority("Admin", "Salesperson", "Shipper")
				
				.requestMatchers("/products/detail/**", "/customers/detail/**").hasAnyAuthority("Admin", "Editor", "Salesperson", "Assistant")

				.requestMatchers("/customers/**", "/orders/**", "/get_shipping_cost", "/reports/**").hasAnyAuthority("Admin", "Salesperson")
				
				.requestMatchers("/orders_shipper/update/**").hasAuthority("Shipper")
				
				.requestMatchers("/reviews/**").hasAnyAuthority("Admin", "Assistant")
				.anyRequest().authenticated())
		
				.formLogin(form -> form.loginPage("/login").usernameParameter("email").defaultSuccessUrl("/", true)
						.permitAll())
				.logout(logout -> logout.permitAll())
				.rememberMe(rem -> rem.key("AbcDefgHijKlmnOpqrs_1234567890").tokenValiditySeconds(7 * 24 * 60 * 60));
		;

		return http.build();
	}

	@Bean
	 WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().requestMatchers("/images/**", "/js/**", "/webjars/**");
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
