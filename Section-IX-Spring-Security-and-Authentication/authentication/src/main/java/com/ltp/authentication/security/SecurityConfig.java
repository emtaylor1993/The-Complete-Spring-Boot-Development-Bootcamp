package com.ltp.authentication.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import com.ltp.authentication.security.filter.AuthenticationFilter;
import com.ltp.authentication.security.filter.ExceptionHandlerFilter;
import com.ltp.authentication.security.filter.JWTAuthorizationFilter;
import com.ltp.authentication.security.manager.CustomAuthenticationManager;
import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class SecurityConfig {
    private final CustomAuthenticationManager customAuthenticationManager;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(customAuthenticationManager);
        authenticationFilter.setFilterProcessesUrl("/authenticate");
        http
            .headers().frameOptions().disable()
            .and()
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/h2/**").permitAll()
            .antMatchers(HttpMethod.GET).permitAll()
            .antMatchers(HttpMethod.POST, SecurityConstants.REGISTER_PATH).permitAll()
            .antMatchers(HttpMethod.PUT, SecurityConstants.UPDATE_PATH).permitAll()
            .anyRequest().authenticated()
            .and()
            .addFilterBefore(new ExceptionHandlerFilter(), AuthenticationFilter.class)
            .addFilter(authenticationFilter)
            .addFilterAfter(new JWTAuthorizationFilter(), AuthenticationFilter.class)
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        
        return http.build();
    }
}
