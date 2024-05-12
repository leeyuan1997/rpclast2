package common.codex.handler;
import common.message.RpcRequest;
import common.message.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
public class RpcServerHandler extends SimpleChannelInboundHandler<RpcRequest> {
    private final Map<String, Object> serviceMap;

    public RpcServerHandler(Map<String, Object> serviceMap) {
        this.serviceMap = serviceMap;
    }

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
    }

    private Object handleRequest(RpcRequest request) throws Exception {
        String serviceName = request.getServiceName();
        String methodName = request.getMethodName();
        Object[] arguments = request.getArguments();
        Class<?>[] parameterTypes = request.getParameterTypes();

        Object serviceBean = serviceMap.get(serviceName);
        if (serviceBean == null) {
            throw new IllegalArgumentException("Service not found: " + serviceName);
        }

        Method method = serviceBean.getClass().getMethod(methodName, parameterTypes);
        return method.invoke(serviceBean, arguments);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}