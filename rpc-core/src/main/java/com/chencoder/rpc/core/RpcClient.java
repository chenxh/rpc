package com.chencoder.rpc.core;

import java.lang.reflect.Proxy;

import com.chencoder.rpc.common.config.ClientConfig;
import com.chencoder.rpc.core.proxy.JdkRpcDynamicProxy;
import com.chencoder.rpc.core.proxy.JdkRpcDynamicProxy2;
import com.chencoder.rpc.core.sc.api.DemoService;

public class RpcClient {
	
	private ClientConfig config;
	
	public RpcClient(ClientConfig config){
		setConfig(config);
	}
	
	public <T> T refer(Class<T> clazz){
		JdkRpcDynamicProxy2 proxy =  new JdkRpcDynamicProxy2(config);
		return  (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{DemoService.class}, proxy);
	}

	public ClientConfig getConfig() {
		return config;
	}

	public void setConfig(ClientConfig config) {
		this.config = config;
	}
	
	
	
	
	
}
