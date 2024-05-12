package com.liy.netty.client;

import com.liy.netty.client.registry.ServiceDiscovery;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.liy.netty.client")
public class Config {
    @Bean
    public ServiceDiscovery registryDiscovery(){
        return  new ServiceDiscovery();
    }
}
