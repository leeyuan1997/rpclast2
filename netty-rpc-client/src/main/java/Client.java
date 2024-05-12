import common.service.HelloService;
import consumer.client.RpcProxy;
import consumer.config.Config;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Client {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context
                = new AnnotationConfigApplicationContext(Config.class);
        RpcProxy rpcProxy = context.getBean(RpcProxy.class);
        HelloService proxy = rpcProxy.createProxy(HelloService.class);
        String response = proxy.hello("dsdsd");
        System.out.println(response);
    }
}
