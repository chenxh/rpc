package com.chencoder.rpc.core.transport.netty;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.chencoder.rpc.common.EventType;
import com.chencoder.rpc.common.MessageType;
import com.chencoder.rpc.common.bean.Request;
import com.chencoder.rpc.common.bean.Response;
import com.chencoder.rpc.common.bean.RpcRequest;
import com.chencoder.rpc.common.bean.RpcResponse;
import com.chencoder.rpc.common.config.ActionMethod;
import com.chencoder.rpc.core.provider.DefaultServiceProvider;
import com.chencoder.rpc.core.provider.Exporter;
import com.google.common.base.Preconditions;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class NettyProcessorHandler extends SimpleChannelInboundHandler<RpcRequest> {
	
	private Exporter exporter;
	
	public static final ExecutorService threadPool = Executors.newFixedThreadPool(10);
	
	public NettyProcessorHandler(Exporter exporter){
		this.exporter = exporter;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, RpcRequest message) throws Exception {
		if(message != null){
			if(EventType.typeofHeartBeat(message.getHeader().getExtend())){
				ctx.writeAndFlush(message);
			}
			try{
				Request req = message.getRequest();
				if(exporter == null){
					return;
				}
				
				threadPool.execute(new Runnable(){

					@Override
					public void run() {
						DefaultServiceProvider provider = exporter.getProvider(req.getServiceName());
						Preconditions.checkNotNull(provider);
						
						ActionMethod actionMethod = provider.findActionMethod(req);
						Preconditions.checkNotNull(actionMethod);
						
						Object result;
						try {
							result = actionMethod.call(req.getArgs());
							Response response = new Response();
							byte extend = (byte) (message.getHeader().getExtend() | MessageType.RESPONSE_MESSAGE_TYPE);
						    message.getHeader().setExtend(extend);
						    response.setResult(result);
							ctx.writeAndFlush(new RpcResponse(message.getHeader(), message.getHeader().getMessageID(), response));
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						}
					}
				});
				
			}catch(Exception e){
				ctx.newFailedFuture(e);
				e.printStackTrace();
			}
			
		}
	}

}
