package com.chencoder.rpc.core.invoker;

import com.chencoder.rpc.common.bean.RpcMessage;
import com.chencoder.rpc.core.RpcInvoker;
import com.chencoder.rpc.core.transport.TransportClient;

public abstract class RpcServerInvoker implements RpcInvoker{
	
	RpcServerInvoker(){
		
	}

	@Override
	public Object invoke(RpcMessage message) {
		return null;
	}
	
	abstract TransportClient getTransportClient();
}
	
	
