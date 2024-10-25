package com.shopme.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.shopme.security.oauth.CustomerOAuth2UserService;
import com.shopme.security.oauth.OAuth2LoginSuccessHandler;

@Configuration

public class WebSecurityConfig {

	@Autowired
	private CustomerOAuth2UserService oAuth2UserService;

	private final DatabaseLoginSuccessHandler databaseLoginHandler;
	private final OAuth2LoginSuccessHandler oauth2LoginHandler;

	public WebSecurityConfig(@Lazy OAuth2LoginSuccessHandler oauth2LoginHandler, @Lazy DatabaseLoginSuccessHandler databaseLoginHandler) {
		this.databaseLoginHandler = databaseLoginHandler;
		this.oauth2LoginHandler = oauth2LoginHandler;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests(
				authorize -> authorize
				.requestMatchers( "/account_details", "/update_account_details", "/cart").authenticated().anyRequest().permitAll())
				

				.formLogin(form -> form.loginPage("/login").usernameParameter("email").defaultSuccessUrl("/", true)
						.successHandler(databaseLoginHandler).permitAll())
				.oauth2Login(oauth2 -> oauth2.loginPage("/login")
						.userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService))
						.successHandler(oauth2LoginHandler))

				.logout(logout -> logout.permitAll())
				.rememberMe(rem -> rem.key("AbcDefgHijKlmnOpqrs_1234567890").tokenValiditySeconds(7 * 24 * 60 * 60))
				
				.sessionManagement(session -> session
		                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS));
		;

		return http.build();
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().requestMatchers("/images/**", "/js/**", "/webjars/**");
	}

	@Bean
	UserDetailsService userDetailsService() {
		return new CustomerUserDetailsService();
	}

	@Bean
	DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}
}
