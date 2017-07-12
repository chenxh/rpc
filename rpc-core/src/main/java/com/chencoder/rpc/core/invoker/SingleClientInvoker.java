package com.chencoder.rpc.core.invoker;

import com.chencoder.rpc.core.transport.TransportClient;

public class SingleClientInvoker extends RpcClientInvoker{
	
	private TransportClient client ;
	
	
	public SingleClientInvoker(TransportClient client){
		setClient(client);
	}

	@Override
	TransportClient getTransportClient() {
		return client;
	}

	@Override
	void addFailedClient(TransportClient client) {
	}
	
	public void setClient(TransportClient client) {
		this.client = client;
	}

}
