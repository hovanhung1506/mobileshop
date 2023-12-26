package com.example.mobileshop.config;

import com.example.mobileshop.handler.CustomAuthenticationFailureHandler;
import com.example.mobileshop.handler.CustomAuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/admin/**", "/cart/**", "/user/**")
                                .hasAnyRole("ADMIN", "USER")
                                .anyRequest()
                                .permitAll())
                .formLogin(form ->
                        form.loginPage("/login")
                                .loginProcessingUrl("/perform_login")
                                .successHandler(authenticationSuccessHandler())
                                .failureHandler(authenticationFailureHandler()))
                .logout(logout ->
                        logout.logoutUrl("/logout")
                                .deleteCookies("JSESSIONID"));

        return http.build();
    }

    @Bean
    public CustomAuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

    @Bean
    public CustomAuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }
}
