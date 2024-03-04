package com.prico.config;

import com.prico.security.JwtDecoderUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public JwtDecoderUtil jwtDecoderUtil() {
        return new JwtDecoderUtil();
    }
}
