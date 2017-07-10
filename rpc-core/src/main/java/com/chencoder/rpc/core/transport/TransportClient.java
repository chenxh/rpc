package com.chencoder.rpc.core.transport;

import com.chencoder.rpc.common.bean.Message;

public interface TransportClient {
	
	void connect();
	
	void close();
	
	ResponseFuture<?> request(Message message, long timeout);

}
