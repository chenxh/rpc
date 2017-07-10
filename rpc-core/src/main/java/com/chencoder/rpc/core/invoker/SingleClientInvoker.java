package com.chencoder.rpc.core.invoker;

import com.chencoder.rpc.common.config.RpcRuntimeConfig;
import com.chencoder.rpc.core.transport.TransportClient;

public class SingleClientInvoker extends RpcClientInvoker{
	
	private TransportClient client ;
	
	private RpcRuntimeConfig runtimeConfig;
	
	public SingleClientInvoker(TransportClient client, RpcRuntimeConfig rpcRuntimeConfig){
		setClient(client);
		setRuntimeConfig(rpcRuntimeConfig);
	}

	@Override
	TransportClient getTransportClient() {
		return client;
	}

	public void setClient(TransportClient client) {
		this.client = client;
	}

	public RpcRuntimeConfig getRuntimeConfig() {
		return runtimeConfig;
	}

	public void setRuntimeConfig(RpcRuntimeConfig runtimeConfig) {
		this.runtimeConfig = runtimeConfig;
	}

}
