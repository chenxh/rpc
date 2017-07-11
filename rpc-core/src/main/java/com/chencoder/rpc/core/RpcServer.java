package com.chencoder.rpc.core;

import com.chencoder.rpc.common.bean.RpcException;
import com.chencoder.rpc.common.config.RegistryConfig;
import com.chencoder.rpc.common.config.ServerConfig;
import com.chencoder.rpc.core.provider.Exporter;
import com.chencoder.rpc.core.transport.TransportServer;
import com.chencoder.rpc.core.transport.TransportServerFactory;

public class RpcServer {
	
	private ServerConfig config;
	
	private Exporter exporter = null;
	
	private TransportServer transportServer = null;

	public RpcServer(ServerConfig config){
		setConfig(config);
	}
	
	public RpcServer(ServerConfig config, RegistryConfig registryConfig){
		setConfig(config);
		exporter = new Exporter(registryConfig);
	}
	
	public <T> void exportService(Class<T> serviceClass, T implObject){			
		if(exporter != null)
			exporter.exportService(serviceClass, implObject, config.getPort());
	}
	
	public void startServer(){
		try {
			transportServer = TransportServerFactory.newTransportServer(config, exporter);
			transportServer.start();
		} catch (InterruptedException e) {
			throw new RpcException(e);
		}
	}

	public ServerConfig getConfig() {
		return config;
	}

	public void setConfig(ServerConfig config) {
		this.config = config;
	}

}
