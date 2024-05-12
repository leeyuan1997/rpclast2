package provider;

import common.registry.CuratorClient;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan("provider")
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
        Object bean = context.getBean("curatorClient");
        while (true) {

        }
    }
}
