package com.chencoder.rpc.core.invoker;

import com.chencoder.rpc.common.bean.Message;
import com.chencoder.rpc.common.bean.RpcException;
import com.chencoder.rpc.common.config.RpcRuntimeConfig;
import com.chencoder.rpc.core.RpcInvoker;
import com.chencoder.rpc.core.transport.ResponseFuture;
import com.chencoder.rpc.core.transport.TransportClient;

public abstract class RpcClientInvoker implements RpcInvoker{
	
	RpcClientInvoker(){
		
	}

	@Override
	public Object invoke(Message<?> message, RpcRuntimeConfig invokeConfig) {
		try {
			TransportClient client =  getTransportClient();
			//TODO:TIMEOUT
			ResponseFuture<?> resp = client.request(message, 0L);
			return resp.getPromise().await();
		} catch (Exception e) {
			throw new RpcException(e);
		}
	}
	
	abstract TransportClient getTransportClient();
}
	
	
