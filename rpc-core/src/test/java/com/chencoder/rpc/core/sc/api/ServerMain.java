package com.chencoder.rpc.core.sc.api;

import com.chencoder.rpc.common.config.RegistryConfig;
import com.chencoder.rpc.common.config.ServerConfig;
import com.chencoder.rpc.common.config.ServiceConfig;
import com.chencoder.rpc.core.provider.Exporter;
import com.chencoder.rpc.core.transport.netty.NettyServer;
import com.chencoder.rpc.core.transport.netty.NettyServerFactory;

public class ServerMain {

	ServerConfig serverConfig;
	RegistryConfig registryConfig;
	
	private void initConfig(){
		serverConfig = new ServerConfig();
		serverConfig.setPort(1121);
		serverConfig.setSoBacklog(128);
		serverConfig.setSoKeepAlive(true);
		serverConfig.setTcpNoDelay(true);
		
		//服务配置
		ServiceConfig serviceConfig = new ServiceConfig();
		serviceConfig.setTarget(new DemoServiceImpl());
		serviceConfig.setServiceName(DemoService.class.getName());
		serviceConfig.setServiceInterface(DemoService.class);
		serverConfig.addServiceConfig(serviceConfig);
		
		registryConfig = new RegistryConfig();
		registryConfig.setRegistryAddress("localhost:2181");
		
	}
	
	public void start(){
		initConfig();
		try {
			Exporter exporter = new Exporter(registryConfig);
			exporter.exportService(DemoService.class, new DemoServiceImpl(),serverConfig.getPort());
			NettyServer server = NettyServerFactory.createServer(serverConfig, exporter);
			server.startAndWait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		ServerMain m = new ServerMain();
		m.start();
	}

}
