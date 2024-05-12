package common.codex.request;

import common.compressor.Compressor;
import common.compressor.GzipCompressor;
import common.constant.RpcConstants;
import common.message.RpcRequest;
import common.serializer.JsonSerializer;
import common.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class RpcRequestEncoder extends MessageToByteEncoder<RpcRequest> {
    @Override
    protected void encode(ChannelHandlerContext ctx, RpcRequest msg, ByteBuf out) throws Exception {
        Serializer serializer = getSerializer(msg.getSerializationType());
        Compressor compressor = getCompressor(msg.getCompressType());

        byte[] body = serializer.serialize(msg);
        body = compressor.compress(body);

        out.writeInt(RpcConstants.MAGIC)
                .writeInt(RpcConstants.VERSION)
                .writeLong(msg.getRequestId())
                .writeByte(msg.getSerializationType())
                .writeByte(msg.getCompressType())
                .writeInt(body.length)
                .writeBytes(body);
    }

    private Serializer getSerializer(byte serializationType) {
        // 根据序列化类型获取对应的序列化器
        // ...
        return new JsonSerializer();
    }

    private Compressor getCompressor(byte compressType) {
        // 根据压缩类型获取对应的压缩器
        // ...
        return  new GzipCompressor();
    }
}