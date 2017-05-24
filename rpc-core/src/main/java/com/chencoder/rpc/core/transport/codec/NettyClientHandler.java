package com.chencoder.rpc.core.transport.codec;

import io.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chencoder.rpc.common.bean.Message;
import com.chencoder.rpc.common.bean.Response;
import com.chencoder.rpc.core.transport.ResponseFuture;
import com.chencoder.rpc.common.Constants;
import com.chencoder.rpc.common.EventType;

/**
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<Message<Response>> {

    private final static Logger LOGGER = LoggerFactory.getLogger(ChannelHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message<Response> message) throws Exception {
        ResponseFuture responseFuture = ResponseFuture.CALLBACKS.remove(message.getHeader().getMessageID());
        if (responseFuture == null) {
            // 服务端响应超时，NettyResponseFuture已经被回收，理论上应该将callbackMap的超时回收时间大于客户端设置的服务超时时间
            LOGGER.warn("response future is null for messageID:{}, It is likely to be time out.");
            return;
        }
        Response response = message.getContent();
        // 心跳消息特殊处理
        if (EventType.typeofHeartBeat(message.getHeader().getExtend()) && response == null) {
            response = new Response();
            response.setCode(Constants.DEF_PING_CODE);
        }
        responseFuture.getPromise().onReceive(response);
    }


    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        ctx.channel().close().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                LOGGER.info("close channel address:{}", ctx.channel().remoteAddress());
            }
        });
    }
}

