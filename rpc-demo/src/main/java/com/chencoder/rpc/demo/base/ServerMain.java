package com.chencoder.rpc.demo.base;

import com.chencoder.rpc.common.config.ServerConfig;
import com.chencoder.rpc.core.RpcServer;

public class ServerMain {
	public static void main(String[] args) {
		ServerConfig serverConfig = new ServerConfig();
		serverConfig.setPort(1122);
		
		RpcServer server = new RpcServer(serverConfig);
		server.export(TestService.class, new TestServiceImpl());
		server.startServer();
	}
}
