package com.HighwayManagment.ATMS_WebApp.security;
import com.HighwayManagment.ATMS_WebApp.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final PermissionFilter permissionFilter;

    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http
                .cors(Customizer.withDefaults())

                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/auth/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).permitAll()

                        .requestMatchers("/admin/**")
                        .hasRole("ADMIN")

                        .requestMatchers("/super-admin/**")
                        .hasRole("SUPER_ADMIN")

                        .anyRequest()
                        .authenticated()
                )

                // JWT Authentication Filter
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )

                // Permission Filter
                .addFilterAfter(
                        permissionFilter,
                        JwtAuthenticationFilter.class
                );
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider();

        provider.setUserDetailsService(
                (UserDetailsService) customUserDetailsService
        );

        provider.setPasswordEncoder(
                passwordEncoder()
        );

        return provider;
    }
}
//package com.HighwayManagment.ATMS_WebApp.security;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//@EnableMethodSecurity
//@RequiredArgsConstructor
//public class SecurityConfig {
//
//    private final JwtAuthenticationFilter jwtAuthenticationFilter;
//
//    @Bean
//    public BCryptPasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(
//            HttpSecurity http
//    ) throws Exception {
//
//        http
//                .cors(Customizer.withDefaults())
//                .csrf(csrf -> csrf.disable())
//                .sessionManagement(session ->
//                        session.sessionCreationPolicy(
//                                SessionCreationPolicy.STATELESS
//                        )
//                )
//                .authorizeHttpRequests(auth -> auth
//
//                        .requestMatchers(
//                                "/auth/**",
//                                "/swagger-ui/**",
//                                "/v3/api-docs/**"
//                        ).permitAll()
//
//                        .requestMatchers("/admin/**")
//                        .hasRole("ADMIN")
//
//                        .requestMatchers("/super-admin/**")
//                        .hasRole("SUPER_ADMIN")
//
//                        .anyRequest()
//                        .authenticated()
//                )
//                .addFilterBefore(
//                        jwtAuthenticationFilter,
//                        UsernamePasswordAuthenticationFilter.class
//                );
//
//        return http.build();
//    }
//    @Bean
//    public AuthenticationProvider authenticationProvider(
//            CustomUserDetailsService userDetailsService
//    ) {
//
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//
//        provider.setUserDetailsService((UserDetailsService) userDetailsService);
//
//        provider.setPasswordEncoder(passwordEncoder());
//
//        return provider;
//    }
//}