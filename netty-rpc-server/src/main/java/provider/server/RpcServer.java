package provider.server;

import common.codex.request.RpcRequestDecoder;
import common.codex.response.RpcResponseEncoder;
import provider.registry.ServiceRegistry;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import common.service.RpcService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import provider.server.Handler.RpcServerHandler;
import provider.server.properties.RpcServerProperties;

import java.util.Map;
@Component
public class RpcServer implements InitializingBean {
    private volatile boolean running = false;
    private int port;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    @Autowired
    private RpcServerHandler rpcServerHandler;

    @Autowired
    private RpcServerProperties rpcServerProperties;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ServiceRegistry serviceRegistry;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.port = rpcServerProperties.getPort();
        this.bossGroup = new NioEventLoopGroup();
        this.workerGroup = new NioEventLoopGroup();
        registerServices();
        start();
    }
    private void registerServices() {
        Map<String, Object> serviceBeans = applicationContext.getBeansWithAnnotation(RpcService.class);
        for (Object serviceBean : serviceBeans.values()) {
            Class<?>[] interfaces = serviceBean.getClass().getInterfaces();
            for (Class<?> interfaceClass : interfaces) {
                String interfaceName = interfaceClass.getName();
                serviceRegistry.registerService(interfaceName, rpcServerProperties.getServiceAddress());
            }
        }
    }


    public void start() throws Exception {
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new RpcRequestDecoder())
                                    .addLast(new RpcResponseEncoder())
                                    .addLast(rpcServerHandler);
                        }
                    });

            ChannelFuture future = bootstrap.bind(port).sync();
            running=true;
            System.out.println("RPC server started on port " + port);
            future.channel().closeFuture().sync();
        } finally {
            shutdown();
        }
    }

    public void shutdown() {
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }
        running=false;
    }

    public boolean isRunning() {
        return running;
    }
}

//