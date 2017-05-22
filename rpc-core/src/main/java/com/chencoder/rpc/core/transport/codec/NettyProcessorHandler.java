package com.chencoder.rpc.core.transport.codec;

import com.chencoder.rpc.common.EventType;
import com.chencoder.rpc.common.bean.Message;
import com.chencoder.rpc.common.bean.Request;
import com.chencoder.rpc.common.config.ActionMethod;
import com.chencoder.rpc.common.config.ServiceConfig;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class NettyProcessorHandler extends SimpleChannelInboundHandler<Message<Request>> {

	private ServiceConfig serviceConfig;
	
	public NettyProcessorHandler(ServiceConfig serviceConfig){
		this.serviceConfig = serviceConfig;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Message<Request> message) throws Exception {
		if(message != null){
			if(EventType.typeofHeartBeat(message.getHeader().getExtend())){
				ctx.writeAndFlush(message);
			}
			Request req = message.getContent();
			ActionMethod actionMethod = serviceConfig.getActionMethod(req.getServiceName(), req.getMethodName());
			ctx.writeAndFlush(actionMethod.call(req.getArgs()));
		}
	}

}
