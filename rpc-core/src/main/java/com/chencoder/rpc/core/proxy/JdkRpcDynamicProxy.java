package com.chencoder.rpc.core.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;

import com.chencoder.rpc.common.config.ClientConfig;
import com.chencoder.rpc.core.transport.Client;

public class JdkRpcDynamicProxy implements InvocationHandler{
	
	private ClientConfig clientConfig;
	
	private Client client;
	
	public JdkRpcDynamicProxy(ClientConfig config){
		this.clientConfig = config;
		
		if(!StringUtils.isEmpty(config.getRegistryAddress())){
			
		}
	}

	@Override
	public Object invoke(Object target, Method method, Object[] args) throws Throwable {
		return null;
	}

}
