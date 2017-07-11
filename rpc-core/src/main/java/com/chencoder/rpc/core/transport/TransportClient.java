package com.chencoder.rpc.core.transport;

import com.chencoder.rpc.common.bean.RpcRequest;

public interface TransportClient {
	
	void connect();
	
	void close();
	
	ResponseFuture<?> request(RpcRequest message, long timeout);

}