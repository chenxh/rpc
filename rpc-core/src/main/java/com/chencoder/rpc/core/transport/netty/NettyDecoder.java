package com.chencoder.rpc.core.transport.netty;

import java.util.List;

import com.chencoder.rpc.common.CompressType;
import com.chencoder.rpc.common.Constants;
import com.chencoder.rpc.common.EventType;
import com.chencoder.rpc.common.MessageType;
import com.chencoder.rpc.common.SerializeType;
import com.chencoder.rpc.common.bean.Header;
import com.chencoder.rpc.common.bean.Message;
import com.chencoder.rpc.common.compress.Compress;
import com.chencoder.rpc.common.serialize.Serialization;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 */
public class NettyDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.readableBytes() < Constants.HEADER_SIZE) {
            return;
        }
        byteBuf.markReaderIndex();
        short magic = byteBuf.readShort();
        if (magic != Constants.MAGIC) {
            byteBuf.resetReaderIndex();
            throw new RuntimeException("Decoder transport header not support, type: " + magic);
        }
        byte version = byteBuf.readByte();
        byte extend = byteBuf.readByte();
        long messageID = byteBuf.readLong();
        int size = byteBuf.readInt();
        Object req = null;
        if (!(size == 0 && EventType.typeofHeartBeat(extend))) {
            if (byteBuf.readableBytes() < size) {
                byteBuf.resetReaderIndex();
                return;
            }
            // TODO 限制最大包长
            byte[] payload = new byte[size];
            byteBuf.readBytes(payload);
            Serialization serialization = SerializeType.getSerializationByExtend(extend);
            Compress compress = CompressType.getCompressTypeByValueByExtend(extend);
            req = serialization.deserialize(compress.unCompress(payload), MessageType.getMessageTypeByExtend(extend));
        }
        Header header = new Header(magic, version, extend, messageID, size);
        Message<?> message = new Message<>(header, req);
        list.add(message);
    }
}
