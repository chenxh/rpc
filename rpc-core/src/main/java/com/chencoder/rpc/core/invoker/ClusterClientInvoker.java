package com.chencoder.rpc.core.invoker;

import com.chencoder.rpc.common.config.ClientConfig;
import com.chencoder.rpc.core.transport.TransportClient;

public class ClusterClientInvoker extends RpcClientInvoker{
	
	
	
	public ClusterClientInvoker(){
		
	}
	
	public ClusterClientInvoker(ClientConfig config){
		
	}

	@Override
	TransportClient getTransportClient() {
		return null;
	}

}
