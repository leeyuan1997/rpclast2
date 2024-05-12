package common.message;

import java.io.Serializable;

public class HeartbeatMessage implements Serializable {
    // 可以添加一些元数据字段,如时间戳等
    private  MessageType messageType = MessageType.HEARTBEAT;

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }
}

