package com.cart.shoppy.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShoppyConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
