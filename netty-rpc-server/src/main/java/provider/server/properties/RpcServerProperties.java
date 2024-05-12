package provider.server.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:rpcserver.properties")
public class RpcServerProperties {
    private int port;

    private String ipAddress;

    @Value("${port}")
    public void setPort(int port) {
        this.port = port;
    }

    @Value("${host}")
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }


    public String  getServiceAddress(){
        return ipAddress+":"+port;
    }

    public int getPort() {
        return port;
    }

    public String getIpAddress() {
        return ipAddress;
    }
}
