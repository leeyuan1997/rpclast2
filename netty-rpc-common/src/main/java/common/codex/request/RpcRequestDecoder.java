package common.codex.request;
import common.compressor.Compressor;
import common.compressor.GzipCompressor;
import common.constant.RpcConstants;
import common.message.RpcRequest;
import common.serializer.JsonSerializer;
import common.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.List;

public class RpcRequestDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 28) {
            return;
        }

        in.markReaderIndex();

        int magic = in.readInt();
        if (magic != RpcConstants.MAGIC) {
            in.resetReaderIndex();
            throw new IllegalArgumentException("Invalid magic number: " + magic);
        }

        int version = in.readInt();
        if (version != RpcConstants.VERSION) {
            in.resetReaderIndex();
            throw new IllegalArgumentException("Invalid version number: " + version);
        }

        long requestId = in.readLong();
        byte serializationType = in.readByte();
        byte compressType = in.readByte();
        int length = in.readInt();

        if (in.readableBytes() < length) {
            in.resetReaderIndex();
            return;
        }

        byte[] body = new byte[length];
        in.readBytes(body);

        Serializer serializer = getSerializer(serializationType);
        Compressor compressor = getCompressor(compressType);

        body = compressor.decompress(body);
        RpcRequest request = serializer.deserialize(body, RpcRequest.class);
        request.setRequestId(requestId);
        request.setSerializationType(serializationType);
        request.setCompressType(compressType);

        out.add(request);
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