package consumer.discovery;

import common.registry.CuratorClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        List<Map<String,String>>res = new ArrayList<>();
        for(String address : serviceAddresses) {
            String[] split = address.split(":");
            Map<String,String>map = new HashMap<>();
            map.put("ip",split[0]);
            map.put("port",split[1]);
        }
        return serviceAddresses;
    }

}


//麻烦你从最开始就给我一个最好的设计方案哈，因为我想进行学习。
//现在就针对客户端，假设我有一个ServiceDiscovery，它的作用是客户端给出全限定名，如"com.liy.helloservice"后，他会从zookeeper服务器找出所有能提供这个接口实现的地址
//其形式为为。
//
//客户端该如何设计代码，假设其他都和前面你给出的一样