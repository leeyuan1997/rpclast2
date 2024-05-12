package com.liy.netty.provider;

import com.liy.netty.provider.registry.ServiceRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = {Config.class})
public class ServiceRegistryTest {
    @Autowired
    ServiceRegistry serviceRegistry;
    @Test
    public void testRegisterAndGetService() {
        // 创建一个服务实例
//        HelloService helloService = new HelloServiceImpl();
//
//        // 注册服务
//        serviceRegistry.registerService(helloService。);
//
//        // 获取服务
//        Object service = serviceRegistry.getService("hello");

        // 检查获取的服务是否正确
//        assertNotNull(service);
//        assertTrue(service instanceof HelloService);
//        assertEquals("Hello, World!", ((HelloService) service).hello("World"));
    }

}
