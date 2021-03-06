package com.chencoder.rpc.core.invoker;

import com.chencoder.rpc.common.bean.Response;
import com.chencoder.rpc.common.bean.RpcContext;
import com.chencoder.rpc.common.bean.RpcException;
import com.chencoder.rpc.common.bean.RpcRequest;
import com.chencoder.rpc.core.RpcInvoker;
import com.chencoder.rpc.core.transport.ResponseFuture;
import com.chencoder.rpc.core.transport.TransportClient;

public abstract class RpcClientInvoker implements RpcInvoker{
	
	RpcClientInvoker(){
		
	}

	@Override
	public Object invoke(RpcRequest request) {
		RpcContext context = RpcContext.getContext();
		int retryTimes = context.getRetry()==0?1:context.getRetry();
		TransportClient client = null;
		for(int i=0; i<retryTimes; i++){
			try {
				client =  getTransportClient(request);
				ResponseFuture<?> resp = client.request(request, context.getTimout());
				Response response = (Response)resp.getPromise().await();
				return response.getResult();
			} catch (Throwable e) {
				addFailedClient(client);
			}
		}
		
		throw new RpcException("remote call[" + request + "] failed after " + context.getRetry() + " times retry");
	}
	
	abstract TransportClient getTransportClient(RpcRequest request);
	abstract void addFailedClient(TransportClient client);
	
}
	
	
