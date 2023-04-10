package com.dqj.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity  //@EnableWebSecurity开启SpringSecurity的默认行为
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
}
