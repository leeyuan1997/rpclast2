package common.registry;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;


public class CuratorClient implements InitializingBean {
    public CuratorFramework getCuratorFramework() {
        return curatorFramework;
    }

    CuratorFramework curatorFramework;

    String servicePath;


    String serverAddress;

    int sessionTimeout;
    int connectionTimeout;

    public CuratorClient(String servicePath,String serverAddress,int sessionTimeout,int connectionTimeout){
        this.serverAddress =serverAddress;
        this.servicePath =servicePath;
        this.sessionTimeout = sessionTimeout;
        this.connectionTimeout = connectionTimeout;

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.curatorFramework= CuratorFrameworkFactory.newClient(serverAddress, sessionTimeout,connectionTimeout,new ExponentialBackoffRetry(2, 2));
        curatorFramework.start();
    }

    public String getServicePath() {
        return servicePath;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    @Override
    public String toString() {
        return "CuratorClient{" +
                "curatorFramework=" + curatorFramework +
                ", servicePath='" + servicePath + '\'' +
                ", serverAddress='" + serverAddress + '\'' +
                ", sessionTimeout=" + sessionTimeout +
                ", connectionTimeout=" + connectionTimeout +
                '}';
    }
}
