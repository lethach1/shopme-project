package com.shopme.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.shopme.security.oauth.CustomerOAuth2UserService;
import com.shopme.security.oauth.OAuth2LoginSuccessHandler;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {


    private CustomerOAuth2UserService oAuth2UserService;
    
    private OAuth2LoginSuccessHandler oauth2LoginHandler;
     
    private DatabaseLoginSuccessHandler databaseLoginHandler;

   
    
    public WebSecurityConfig(CustomerOAuth2UserService oAuth2UserService, @Lazy OAuth2LoginSuccessHandler oauth2LoginHandler,
			@Lazy DatabaseLoginSuccessHandler databaseLoginHandler) {
		this.oAuth2UserService = oAuth2UserService;
		this.oauth2LoginHandler = oauth2LoginHandler;
		this.databaseLoginHandler = databaseLoginHandler;
	}

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/account_details", "/update_account_details", "/orders/**",
                    "/cart", "/address_book/**", "/checkout", "/place_order", "/reviews/**",
                    "/process_paypal_order", "/write_review/**", "/post_review",
                    "/ask_question/**", "/post_question/**", "/customer/questions/**")
                    .authenticated()
                .anyRequest().permitAll()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .usernameParameter("email")
                .successHandler(databaseLoginHandler)
                .permitAll()
            )
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/login")
                .userInfoEndpoint(userInfo -> userInfo
                    .userService(oAuth2UserService)
                )
                .successHandler(oauth2LoginHandler)
            )
            .logout(logout -> logout
                .permitAll()
            )
            .rememberMe(remember -> remember
                .key("1234567890_aBcDeFgHiJkLmNoPqRsTuVwXyZ")
                .tokenValiditySeconds(14 * 24 * 60 * 60)
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
            );

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomerUserDetailsService();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
            .requestMatchers("/images/**", "/js/**", "/webjars/**");
    }
}
