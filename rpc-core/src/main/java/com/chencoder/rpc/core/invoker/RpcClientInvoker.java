package com.chencoder.rpc.core.invoker;

import com.chencoder.rpc.common.bean.RpcContext;
import com.chencoder.rpc.common.bean.RpcException;
import com.chencoder.rpc.common.bean.RpcMessage;
import com.chencoder.rpc.common.bean.RpcRequest;
import com.chencoder.rpc.core.RpcInvoker;
import com.chencoder.rpc.core.transport.ResponseFuture;
import com.chencoder.rpc.core.transport.TransportClient;

public abstract class RpcClientInvoker implements RpcInvoker{
	
	RpcClientInvoker(){
		
	}

	@Override
	public Object invoke(RpcMessage message) {
		if(!(message instanceof RpcRequest)){
			throw new UnsupportedOperationException();
		}
		RpcContext context = RpcContext.getContext();
		int retryTimes = context.getRetry()==0?1:context.getRetry();
		TransportClient client = null;
		for(int i=0; i<retryTimes; i++){
			try {
				client =  getTransportClient();
				ResponseFuture<?> resp = client.request((RpcRequest)message, context.getTimout());
				return resp.getPromise().await();
			} catch (Throwable e) {
				addFailedClient(client);
			}
		}
		
		throw new RpcException("remote call[" + message + "] failed after " + context.getRetry() + " times retry");
	}
	
	abstract TransportClient getTransportClient();
	abstract void addFailedClient(TransportClient client);
	
}
	
	
