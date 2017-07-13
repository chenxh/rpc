package com.chencoder.rpc.core;

import java.lang.reflect.Proxy;

import com.chencoder.rpc.common.config.ClientConfig;
import com.chencoder.rpc.core.proxy.JdkRpcDynamicProxy;

public class RpcClient {
	
	private ClientConfig config;
	
	public RpcClient(ClientConfig config){
		setConfig(config);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T refer(Class<T> clazz){
		JdkRpcDynamicProxy proxy =  new JdkRpcDynamicProxy(config);
		return  (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{clazz}, proxy);
	}

	public ClientConfig getConfig() {
		return config;
	}

	public void setConfig(ClientConfig config) {
		this.config = config;
	}
	
}
