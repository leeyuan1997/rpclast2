package provider.registry;

import common.registry.CuratorClient;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServiceRegistry {
    CuratorClient curatorClient;
    public void registerService(String serviceName,String serviceAddress) {
        String path = curatorClient.getServicePath() + "/" +serviceName+"/"+serviceAddress;
        try {
            curatorClient.getCuratorFramework()
                    .create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(path,null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Autowired
    public void setCuratorClient(CuratorClient curatorClient) {
        this.curatorClient = curatorClient;
    }

}
