package provider.server.Handler;

import common.message.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import common.message.RpcRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;
@Component
public class RpcServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    private ApplicationContext applicationContext;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest request) throws Exception {
        RpcResponse response = new RpcResponse();
        response.setRequestId(request.getRequestId());
        try {
            Object result = handleRequest(request);
            response.setCode(0);
            response.setMessage("Success");
            response.setData(result);
        } catch (Throwable t) {
            response.setCode(-1);
            response.setMessage("Error: " + t.getMessage());
        }
        ctx.writeAndFlush(response);
        // 处理 RPC 请求
        // 发送 RPC 响应
    }

    private Object handleRequest(RpcRequest msg) throws Exception {
        String methodName = msg.getMethodName();
        Class<?>[] parameterTypes = msg.getParameterTypes();
        Object[] parameters = msg.getArguments();
        String interfaceName = msg.getServiceName();
        // 根据方法名找到对应的服务实例
        Map<String, ?> serviceBeans  = applicationContext.getBeansOfType(Class.forName(interfaceName));
        Object serviceInstance = serviceBeans.values().iterator().next();
        // 调用服务实例的方法
        Method method = serviceInstance.getClass().getMethod(methodName, parameterTypes);
        return method.invoke(serviceInstance, parameters);

    }
}
