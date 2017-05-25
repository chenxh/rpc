package com.chencoder.rpc.core.transport.netty;

import com.chencoder.rpc.common.EventType;
import com.chencoder.rpc.common.MessageType;
import com.chencoder.rpc.common.bean.Message;
import com.chencoder.rpc.common.bean.Request;
import com.chencoder.rpc.common.bean.Response;
import com.chencoder.rpc.common.config.ActionMethod;
import com.chencoder.rpc.core.provider.DefaultServiceProvider;
import com.chencoder.rpc.core.provider.Exporter;
import com.google.common.base.Preconditions;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class NettyProcessorHandler extends SimpleChannelInboundHandler<Message<Request>> {
	
	private Exporter exporter;
	
	public NettyProcessorHandler(Exporter exporter){
		this.exporter = exporter;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Message<Request> message) throws Exception {
		if(message != null){
			if(EventType.typeofHeartBeat(message.getHeader().getExtend())){
				ctx.writeAndFlush(message);
			}
			try{
				Request req = message.getContent();
				DefaultServiceProvider provider = exporter.getProvider(req.getServiceName());
				Preconditions.checkNotNull(provider);
				
				ActionMethod actionMethod = provider.findActionMethod(req);
				Preconditions.checkNotNull(actionMethod);
				
				Object result = actionMethod.call(req.getArgs());
				Response response = new Response();
				byte extend = (byte) (message.getHeader().getExtend() | MessageType.RESPONSE_MESSAGE_TYPE);
			    message.getHeader().setExtend(extend);
			    response.setResult(result);
				ctx.writeAndFlush(new Message<Response>(message.getHeader(), response));
			}catch(Exception e){
				ctx.newFailedFuture(e);
				e.printStackTrace();
			}
			
		}
	}

}
