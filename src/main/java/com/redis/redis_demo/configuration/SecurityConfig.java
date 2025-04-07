package com.redis.redis_demo.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() // Cho phép tất cả các yêu cầu mà không cần xác thực
            )
            .csrf(csrf -> csrf.disable()); // Vô hiệu hóa CSRF nếu không cần thiết

        return http.build();
    }
}
