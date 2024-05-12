package consumer.client;

import common.constant.RpcUtils;
import common.message.RpcRequest;
import common.message.RpcResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
@Component
public class RpcProxy {
    @Autowired
    private final RpcClient client;

    public RpcProxy(RpcClient client) {
        this.client = client;
    }

    @SuppressWarnings("unchecked")
    public <T> T createProxy(Class<T> serviceInterface) {
        return (T) Proxy.newProxyInstance(
                serviceInterface.getClassLoader(),
                new Class[]{serviceInterface},
                (proxy, method, args) -> invoke(serviceInterface, method, args)
        );
    }

    private Object invoke(Class<?> serviceInterface, Method method, Object[] args) throws Throwable {
        // 创建 RpcRequest 对象
        RpcRequest request = new RpcRequest();
        request.setRequestId(RpcUtils.generateRequestId());
        request.setServiceName(serviceInterface.getName());
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setArguments(args);

        // 发送请求并获取响应
        RpcResponse response = client.sendRequest(request);

        // 处理响应结果
        if (response.getCode() == 0) {
            return response.getData();
        } else {
            throw new RuntimeException("Error: " + response.getMessage());
        }
    }
}
