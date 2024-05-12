package com.liy.netty.provider;

import com.liy.netty.provider.server.RpcServer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ContextConfiguration(classes = {Config.class})
public class RpcServerTest {
    @Autowired
    RpcServer rpcServer;

    @Test
    public void trainServer(){
        System.out.println(rpcServer);
    }
    @Test
    public void testStartServer() throws Exception {
        // 启动 RPC 服务端
        new Thread(() -> {
            try {
                rpcServer.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        // 等待一段时间,确保服务端已启动
        Thread.sleep(1000000);

        // 检查服务端是否已启动
        assertTrue(rpcServer.isRunning());

        // 关闭服务端
        rpcServer.shutdown();

        assertFalse(rpcServer.isRunning());
    }
}
