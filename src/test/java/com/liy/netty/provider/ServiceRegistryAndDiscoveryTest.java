package com.liy.netty.provider;

import com.liy.netty.client.registry.ServiceDiscovery;
import com.liy.netty.provider.registry.ServiceRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

@SpringBootTest
@ContextConfiguration(classes = {Config.class})
public class ServiceRegistryAndDiscoveryTest {
    @Autowired
    private ServiceDiscovery serviceDiscovery;

    @Autowired
    private ServiceRegistry serviceRegistry;

    @Test
    public void regiserService() throws InterruptedException {
        serviceRegistry.registerService("abc","192.168.0.1:9999");
        Thread.sleep(20000);
    }

    @Test
    public void discoverService(){
        String serviceName = "abc";
        List<String> list = serviceDiscovery.discoverService(serviceName);
        System.out.println(list);
    }
}
