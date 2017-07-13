package com.chencoder.rpc.core.invoker;

import com.chencoder.rpc.common.bean.RpcRequest;
import com.chencoder.rpc.core.RpcInvoker;
import com.chencoder.rpc.core.transport.TransportClient;

public abstract class RpcServerInvoker implements RpcInvoker{
	
	RpcServerInvoker(){
		
	}

	@Override
	public Object invoke(RpcRequest request) {
		return null;
	}
	
	abstract TransportClient getTransportClient();
}
	
	
