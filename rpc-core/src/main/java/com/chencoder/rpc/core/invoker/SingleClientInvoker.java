package com.chencoder.rpc.core.invoker;

import java.io.IOException;

import com.chencoder.rpc.common.bean.RpcRequest;
import com.chencoder.rpc.core.transport.TransportClient;

public class SingleClientInvoker extends RpcClientInvoker{
	
	private TransportClient client ;
	
	
	public SingleClientInvoker(TransportClient client){
		setClient(client);
	}

	@Override
	TransportClient getTransportClient(RpcRequest request) {
		return client;
	}

	@Override
	void addFailedClient(TransportClient client) {
	}
	
	public void setClient(TransportClient client) {
		this.client = client;
	}

	@Override
	public void close() throws IOException {
		client.close();
	}

}
