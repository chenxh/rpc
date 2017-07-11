package com.chencoder.rpc.core.transport;

import com.chencoder.rpc.common.config.ServerConfig;
import com.chencoder.rpc.core.provider.Exporter;
import com.chencoder.rpc.core.transport.netty.NettyServer;

public class TransportServerFactory {
	
	public static TransportServer newTransportServer(ServerConfig config, Exporter exporter) throws InterruptedException{
		return new NettyServer(config, config.getPort(), exporter);
	}
}
