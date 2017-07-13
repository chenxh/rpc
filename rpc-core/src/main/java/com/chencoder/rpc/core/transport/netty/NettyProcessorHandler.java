package com.chencoder.rpc.core.transport.netty;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.chencoder.rpc.common.EventType;
import com.chencoder.rpc.common.bean.RpcRequest;
import com.chencoder.rpc.common.bean.RpcResponse;
import com.chencoder.rpc.core.RpcProcessor;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class NettyProcessorHandler extends SimpleChannelInboundHandler<RpcRequest> {
	
	private RpcProcessor processor;
	
	public static final ExecutorService threadPool = Executors.newFixedThreadPool(10);
	
	public NettyProcessorHandler(RpcProcessor processor){
		this.processor = processor;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, RpcRequest message) throws Exception {
		if(message != null){
			if(EventType.typeofHeartBeat(message.getHeader().getExtend())){
				ctx.writeAndFlush(message);
			}
			try{
				if(processor == null){
					return;
				}
				RpcResponse response = processor.proccess(message);
				ctx.writeAndFlush(response);
			}catch(Exception e){
				ctx.newFailedFuture(e);
			}
		}
	}

}
