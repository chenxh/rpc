package com.chencoder.rpc.core.transport.netty;


import com.chencoder.rpc.common.CompressType;
import com.chencoder.rpc.common.SerializeType;
import com.chencoder.rpc.common.bean.Header;
import com.chencoder.rpc.common.bean.RpcMessage;
import com.chencoder.rpc.common.compress.Compress;
import com.chencoder.rpc.common.serialize.Serialization;
import com.chencoder.rpc.common.EventType;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 */
public class NettyEncoder extends MessageToByteEncoder<RpcMessage> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, RpcMessage message, ByteBuf byteBuf) throws Exception {
        Header header = message.getHeader();
        byteBuf.writeShort(header.getMagic());
        byteBuf.writeByte(header.getVersion());
        byteBuf.writeByte(header.getExtend());
        byteBuf.writeLong(header.getMessageID());
        Object content = message.getContent();
        // 心跳消息,没有body
        if (content == null && EventType.typeofHeartBeat(header.getExtend())) {
            byteBuf.writeInt(0);
            return;
        }
        Serialization serialization = SerializeType.getSerializationByExtend(header.getExtend());
        Compress compress = CompressType.getCompressTypeByValueByExtend(header.getExtend());
        byte[] payload = compress.compress(serialization.serialize(content));
        byteBuf.writeInt(payload.length);
        byteBuf.writeBytes(payload);
    }
}
