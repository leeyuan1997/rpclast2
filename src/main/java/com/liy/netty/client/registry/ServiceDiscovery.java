package com.liy.netty.client.registry;

import com.liy.netty.provider.registry.CuratorClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class ServiceDiscovery {

    CuratorClient curatorClient;
    @Autowired
    public void setCuratorClient(CuratorClient curatorClient) {
        this.curatorClient = curatorClient;
    }
    public List<String> discoverService(String serviceName) {
        String path = curatorClient.getServicePath()+"/"+serviceName;
        List<String> serviceAddresses = new ArrayList<>();
        try {
            List<String>nodes = curatorClient.getCuratorFramework().getChildren().forPath(path);
            serviceAddresses.addAll(nodes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return serviceAddresses;
    }

}
