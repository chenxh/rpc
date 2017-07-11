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
		try {
			TransportClient client =  getTransportClient();
			//TODO:TIMEOUT
			ResponseFuture<?> resp = client.request((RpcRequest)message, RpcContext.getContext().getTimout());
			return resp.getPromise().await();
		} catch (Exception e) {
			throw new RpcException(e);
		}
	}
	
	abstract TransportClient getTransportClient();
}
	
	
