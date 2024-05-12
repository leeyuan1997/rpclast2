package consumer.client;

import common.codex.handler.RpcClientHandler;
import common.codex.request.RpcRequestEncoder;
import common.codex.response.RpcResponseDecoder;
import common.constant.RpcUtils;
import common.message.RpcRequest;
import common.message.RpcResponse;
import common.service.HelloService;
import consumer.discovery.ServiceDiscovery;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RpcClient {
    private final Bootstrap bootstrap;
    private final EventLoopGroup eventLoopGroup;
    private final Map<String, List<Channel>> channelPools = new ConcurrentHashMap<>();
    private final ServiceDiscovery serviceDiscovery;

    public RpcClient(ServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
        this.bootstrap = new Bootstrap();
        this.eventLoopGroup = new NioEventLoopGroup();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new RpcResponseDecoder(), new RpcRequestEncoder(), new RpcClientHandler());
                    }
                });
    }

    public void start() {
        // 启动时不需要做特殊处理
    }

    public void stop() {
        for (List<Channel> channels : channelPools.values()) {
            for (Channel channel : channels) {
                channel.close();
            }
        }
        channelPools.clear();
        eventLoopGroup.shutdownGracefully();
    }

    public RpcResponse sendRequest(RpcRequest request) throws Exception {
        String serviceName = request.getServiceName();
        Channel channel = getChannel(serviceName);
        CompletableFuture<RpcResponse> future = new CompletableFuture<>();
        RpcClientHandler handler = channel.pipeline().get(RpcClientHandler.class);
        handler.setFuture(future);
        channel.writeAndFlush(request);
        return future.get();
    }

    private Channel getChannel(String serviceName) throws Exception {
        List<Channel> channels = channelPools.get(serviceName);
        if (channels == null) {
            synchronized (channelPools) {
                channels = channelPools.get(serviceName);
                if (channels == null) {
                    channels = new ArrayList<>();
                    List<String> addresses = serviceDiscovery.discoverService(serviceName);
                    for (String address : addresses) {
                        String[] split = address.split(":");
                        String host = split[0];
                        int port = Integer.parseInt(split[1]);
                        Channel channel = bootstrap.connect(host, port).sync().channel();
                        channels.add(channel);
                    }
                    channelPools.put(serviceName, channels);
                }
            }
        }
        // 从 Channel 列表中选择一个 Channel,可以实现负载均衡等策略
        return channels.get(0);
    }
}