package consumer.config;

import common.registry.CuratorClient;
import common.service.HelloService;
import consumer.discovery.ServiceDiscovery;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

import java.util.List;

@Configuration
@ComponentScan("consumer")
@PropertySource("curator.properties")
public class Config {
    @Bean
    public CuratorClient curatorClient(
            @Value("${service.path}") String servicePath,
            @Value("${server.address}") String serverAddress,
            @Value("${client.session.time}") int sessionTimeout,
            @Value("${client.connection.time}") int connectionTimeout
    ) {
        return new CuratorClient(servicePath, serverAddress, sessionTimeout, connectionTimeout);
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        ServiceDiscovery bean = context.getBean(ServiceDiscovery.class);
        List<String> list = bean.discoverService(HelloService.class.getName());
        System.out.println(list);
        while (true) {
        }
    }
}
