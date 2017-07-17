package com.chencoder.rpc.core;

import java.io.IOException;
import java.lang.reflect.Proxy;
import java.util.List;

import com.chencoder.rpc.common.config.ClientConfig;
import com.chencoder.rpc.core.proxy.JdkRpcDynamicProxy;
import com.google.common.collect.Lists;

public class RpcClient {
	
	private ClientConfig config;
	private List<JdkRpcDynamicProxy> proxys = Lists.newArrayList();
	
	public RpcClient(ClientConfig config){
		setConfig(config);
		
		Runtime.getRuntime().addShutdownHook(new Thread(){

			@Override
			public void start() {
				close();
			}
			
		});
	}
	
	@SuppressWarnings("unchecked")
	public <T> T refer(Class<T> clazz){
		JdkRpcDynamicProxy proxy =  new JdkRpcDynamicProxy(config, clazz);
		proxys.add(proxy);
		return  (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{clazz}, proxy);
	}

	public ClientConfig getConfig() {
		return config;
	}

	public void setConfig(ClientConfig config) {
		this.config = config;
	}
	
	public void close(){
		for(JdkRpcDynamicProxy proxy: proxys){
			try {
				proxy.close();
			} catch (IOException e) {
			}
		}
	}
	
}
