package com.chencoder.rpc.core.transport;

import com.chencoder.rpc.common.bean.Message;
import com.chencoder.rpc.common.bean.ServerInfo;

public interface Client {
	
	void connect();
	
	void close();
	
	ResponseFuture<?> request(Message message, long timeout);
	
	ServerInfo getServerInfo();

}
