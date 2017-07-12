package com.chencoder.rpc.core;

import com.chencoder.rpc.common.bean.RpcRequest;
import com.chencoder.rpc.common.bean.RpcResponse;

public interface RpcProcessor {
	
	RpcResponse proccess(RpcRequest request);
	
	void addServiceProvider(Class<?> interfaceClass, Object impl);

}
