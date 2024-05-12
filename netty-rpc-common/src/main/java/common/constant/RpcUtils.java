package common.constant;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicLong;

public class RpcUtils {
    private static final AtomicLong REQUEST_ID_GENERATOR = new AtomicLong(0);

    public static long generateRequestId() {
        return REQUEST_ID_GENERATOR.incrementAndGet();
    }

    public static boolean isValidRequest(byte[] data) {
        if (data == null || data.length < 8) {
            return false;
        }
        int magic = ByteBuffer.wrap(data, 0, 4).getInt();
        int version = ByteBuffer.wrap(data, 4, 4).getInt();
        return magic == RpcConstants.MAGIC && version == RpcConstants.VERSION;
    }

    // 其他工具方法
}
