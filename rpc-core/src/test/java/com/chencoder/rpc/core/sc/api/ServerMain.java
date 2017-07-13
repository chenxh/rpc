package com.chencoder.rpc.core.sc.api;

import org.apache.curator.shaded.com.google.common.collect.Lists;

import com.chencoder.rpc.common.config.RegistryConfig;
import com.chencoder.rpc.common.config.ServerConfig;
import com.chencoder.rpc.core.RpcServer;
import com.chencoder.rpc.core.sc.api.interceptor.SimpleInterceptor;

public class ServerMain {

	ServerConfig serverConfig;
	RegistryConfig registryConfig = null;
	
	private void initConfig(){
		serverConfig = new ServerConfig();
		serverConfig.setPort(1134);
		serverConfig.setSoBacklog(128);
		serverConfig.setSoKeepAlive(true);
		serverConfig.setTcpNoDelay(true);
		serverConfig.setInterceptors(Lists.newArrayList(new SimpleInterceptor()));
		
		//服务配置
		registryConfig = new RegistryConfig();
		registryConfig.setRegistryAddress("localhost:2181");
		
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
