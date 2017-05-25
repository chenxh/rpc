package com.chencoder.rpc.core.transport.netty;

import com.chencoder.rpc.common.config.ServerConfig;
import com.chencoder.rpc.core.provider.Exporter;

/**
 */
public class NettyServerFactory {

    public static NettyServer createServer(ServerConfig config) throws InterruptedException {
        NettyServer nettyServer = new NettyServer(config, config.getPort());
        return nettyServer;
    }

	public static NettyServer createServer(ServerConfig serverConfig, Exporter exporter) throws InterruptedException {
		 NettyServer nettyServer = new NettyServer(serverConfig, serverConfig.getPort(), exporter);
	     return nettyServer;
	}

}
