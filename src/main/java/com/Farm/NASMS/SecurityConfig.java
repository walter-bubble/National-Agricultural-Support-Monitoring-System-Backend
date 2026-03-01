package com.Farm.NASMS;

import org.apache.coyote.Adapter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityBuilder;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity
@Configuration
public class SecurityConfig implements WebSecurityConfigurer{
    @Override
    public void init(SecurityBuilder builder) {

    }

    @Override
    public void configure(SecurityBuilder builder) {

    }
}
