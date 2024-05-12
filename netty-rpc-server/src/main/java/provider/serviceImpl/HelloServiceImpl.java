package provider.serviceImpl;

import common.service.HelloService;
import common.service.RpcService;
import org.springframework.stereotype.Component;

@RpcService
@Component
public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String str) {
        return "服务端处理的"+str;
    }
}
