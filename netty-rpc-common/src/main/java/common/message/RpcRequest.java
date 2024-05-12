package common.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RpcRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private int magic; // 魔术字,用于识别RPC请求
    private int version; // 版本号,用于兼容性处理
    private long requestId; // 请求ID,用于匹配请求和响应
    private String serviceName; // 服务名称
    private String methodName; // 方法名称
    private Class<?>[] parameterTypes; // 方法参数类型
    private Object[] arguments; // 方法参数
    private int timeout; // 超时时间,单位为毫秒
    private byte serializationType; // 序列化方式
    private byte compressType; // 压缩方式

    // 构造函数、getter和setter方法
    // ...
}
