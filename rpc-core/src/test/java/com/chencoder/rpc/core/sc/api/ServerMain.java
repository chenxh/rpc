package com.chencoder.rpc.core.sc.api;

import com.chencoder.rpc.common.config.ServerConfig;
import com.chencoder.rpc.common.config.ServiceConfig;
import com.chencoder.rpc.core.provider.Providers;
import com.chencoder.rpc.core.transport.NettyServer;
import com.chencoder.rpc.core.transport.NettyServerFactory;

public class ServerMain {

	ServerConfig serverConfig;
	
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
		
	}
	
	public void start(){
		initConfig();
		try {
			Providers.exportService(DemoService.class, new DemoServiceImpl());
			NettyServer server = NettyServerFactory.createServer(serverConfig);
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
