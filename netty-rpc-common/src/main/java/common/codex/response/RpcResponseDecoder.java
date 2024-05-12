package common.codex.response;
import common.compressor.Compressor;
import common.compressor.GzipCompressor;
import common.constant.RpcConstants;
import common.message.RpcResponse;
import common.serializer.JsonSerializer;
import common.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.List;

public class RpcResponseDecoder extends ByteToMessageDecoder {
    private static final Serializer serializer = new JsonSerializer();
    private static final Compressor compressor = new GzipCompressor();

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

        body = compressor.decompress(body);
        RpcResponse response = serializer.deserialize(body, RpcResponse.class);
        response.setRequestId(requestId);

        out.add(response);
    }
}