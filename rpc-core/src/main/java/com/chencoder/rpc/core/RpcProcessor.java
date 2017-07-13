package com.chencoder.rpc.core;

import java.util.List;

import com.chencoder.rpc.common.bean.RpcRequest;
import com.chencoder.rpc.common.bean.RpcResponse;
import com.chencoder.rpc.common.interceptor.RpcInvokerInterceptor;

public interface RpcProcessor {
	
	RpcResponse proccess(RpcRequest request);
	
	void addServiceProvider(Class<?> interfaceClass, Object impl);

	void addServiceProvider(Class<?> serviceClass, Object implObject, List<RpcInvokerInterceptor> interceptors);

}
