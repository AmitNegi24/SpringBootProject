package com.amit.customMethods;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class ProjectSecurityConfig {
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JWTFilter jwtFilter;

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
       return  http
               .csrf(customizer -> customizer.disable())
               .cors(withDefaults())
                .authorizeHttpRequests((requests) ->requests
                        .requestMatchers(
                                "/api/product-api/product",
                                "/api/checkout-api/checkout",
                                "/api/payment-api/confirm",
                                "/api/payment-api/verify-payment",
                                "/api/payment-api/create-razorpay-order")
                        .authenticated()
                        .requestMatchers("/api/customer-api/register","/api/customer-api/login","/error","/api/product-api/products").permitAll())
                        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                        .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

//    @Bean
//    public UserDetailsService userDetailsService() {
//        PasswordEncoder encoder = passwordEncoder();
////        UserDetails user = User.withUsername("user").password("{noop}12345").authorities("USER").build();
//        UserDetails user = User
//                .withUsername("user")
//                .password(encoder.encode("@Dmin*9627"))
//                .authorities("USER")
//                .build();
//        UserDetails admin = User
//                .withUsername("admin")
//                .password("$2a$12$4Sshvc6yXytgxQDGPoiIQeez09tvsseSY7wAXKyqc.A2Ge5RGPmrC")
//                .authorities("ADMIN")
//                .build();
//
//
//
//        return new InMemoryUserDetailsManager(user, admin);
//    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    CompromisedPasswordChecker compromisedPasswordChecker(){
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }
}
