package com.theelixrlabs.healthcare.config;

import com.theelixrlabs.healthcare.constants.SecurityConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@Profile(SecurityConstants.PROD_PROFILE_CONSTANT)
public class ProdSecurityConfig {

    /**
     * This bean configures spring security to bypass authentication in prod env
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().permitAll());
        return httpSecurity.build();
    }
}
