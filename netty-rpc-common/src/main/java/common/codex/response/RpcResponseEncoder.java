package common.codex.response;

import common.compressor.Compressor;
import common.compressor.GzipCompressor;
import common.constant.RpcConstants;
import common.message.RpcResponse;
import common.serializer.JsonSerializer;
import common.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class RpcResponseEncoder extends MessageToByteEncoder<RpcResponse> {
    private static final Serializer serializer = new JsonSerializer();
    private static final Compressor compressor = new GzipCompressor();

    @Override
    protected void encode(ChannelHandlerContext ctx, RpcResponse msg, ByteBuf out) throws Exception {
        byte[] body = serializer.serialize(msg);
        body = compressor.compress(body);

        out.writeInt(RpcConstants.MAGIC)
                .writeInt(RpcConstants.VERSION)
                .writeLong(msg.getRequestId())
                .writeByte(RpcConstants.SERIALIZATION_JSON)
                .writeByte(RpcConstants.COMPRESS_GZIP)
                .writeInt(body.length)
                .writeBytes(body);
    }
}