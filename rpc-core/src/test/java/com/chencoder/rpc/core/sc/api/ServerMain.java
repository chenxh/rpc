package com.chencoder.rpc.core.sc.api;

import com.chencoder.rpc.common.config.RegistryConfig;
import com.chencoder.rpc.common.config.ServerConfig;
import com.chencoder.rpc.core.RpcServer;
import com.chencoder.rpc.core.sc.api.interceptor.SimpleInterceptor;

public class ServerMain {

	ServerConfig serverConfig;
	RegistryConfig registryConfig = null;
	
	private void initConfig(){
		serverConfig = new ServerConfig.Builder(1134).tcpNoDelay(true).addInterceptor(new SimpleInterceptor()).build();
		
		//服务配置
		registryConfig = new RegistryConfig.Builder().registryAddress("localhost:2181").build();
		
	}
	
	public void start(){
		initConfig();
		RpcServer server = new RpcServer(serverConfig, registryConfig);
		server.export(DemoService.class, new DemoServiceImpl());
		server.startServer();
	}
	
	public static void main(String[] args) {
		ServerMain m = new ServerMain();
		m.start();
	}

}
