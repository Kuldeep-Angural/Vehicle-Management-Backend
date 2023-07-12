package com.navv.securityconfig;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

import static com.navv.model.Permission.*;
import static com.navv.model.Permission.ADMIN_DELETE;
import static com.navv.model.Role.ADMIN;
import static com.navv.model.Role.MANAGER;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.HttpMethod.DELETE;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebConfig {

    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthFilter jwtAuthFilter;
    private static final Long MAX_AGE = 3600L;
    private static final int CORS_FILTER_ORDER = -102;
    private final LogoutHandler logoutHandler;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors().disable()
                .authorizeHttpRequests()
                .requestMatchers("/api/v1/**")
                .permitAll()
//                .requestMatchers("/api/v1/auth/**").hasAnyRole(ADMIN.name(), MANAGER.name())
//
//                .requestMatchers(GET,"/api/v1/auth/**").hasAnyAuthority(ADMIN_READ.name() , MANAGER_READ.name())
//                .requestMatchers(POST,"/api/v1/auth/**").hasAnyAuthority(ADMIN_CREATE.name() , MANAGER_CREATE.name())
//                .requestMatchers(PUT,"/api/v1/auth/**").hasAnyAuthority(ADMIN_UPDATE.name() , MANAGER_UPDATE.name())
//                .requestMatchers(DELETE,"/api/v1/auth/**").hasAnyAuthority(ADMIN_DELETE.name() , MANAGER_DELETE.name())
//
//
//                .requestMatchers("/api/v1/auth/**") .hasRole(ADMIN.name())
//
//                .requestMatchers(GET,"/api/v1/auth/**").hasAuthority(ADMIN_READ.name())
//                .requestMatchers(POST,"/api/v1/auth/**").hasAuthority(ADMIN_CREATE.name())
//                .requestMatchers(PUT,"/api/v1/auth/**").hasAuthority(ADMIN_UPDATE.name())
//                .requestMatchers(DELETE,"/api/v1/auth/**").hasAuthority(ADMIN_DELETE.name())

                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout()
                .logoutUrl("/api/v1/auth/logout")

                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler(((request, response, authentication) -> SecurityContextHolder.clearContext()));

        return http.build();
    }

    @Bean
    public FilterRegistrationBean corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:3000");
        config.setAllowedHeaders(Arrays.asList(
                HttpHeaders.AUTHORIZATION,
                HttpHeaders.CONTENT_TYPE,
                HttpHeaders.ACCEPT));

        config.setAllowedMethods(Arrays.asList(
                HttpMethod.GET.name(),
                HttpMethod.POST.name(),
                HttpMethod.PUT.name(),
                HttpMethod.DELETE.name()));
        config.setMaxAge(MAX_AGE);
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));

        // should be set order to -100 because we need to CorsFilter before SpringSecurityFilter
        bean.setOrder(CORS_FILTER_ORDER);
        return bean;
    }
}
