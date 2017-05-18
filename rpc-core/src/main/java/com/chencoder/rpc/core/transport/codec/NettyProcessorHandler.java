package com.chencoder.rpc.core.transport.codec;

import com.chencoder.rpc.common.bean.Message;
import com.chencoder.rpc.common.bean.Request;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class NettyProcessorHandler extends SimpleChannelInboundHandler<Message<Request>> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Message<Request> msg) throws Exception {
		if(msg != null){
			System.out.println(msg.toString());
		}
	}

}
