package com.chencoder.rpc.core.transport;

import com.chencoder.rpc.common.config.ServerConfig;

/**
 */
public class NettyServerFactory {

    public static NettyServer createServer(ServerConfig config) throws InterruptedException {
        NettyServer nettyServer = new NettyServer(config, config.getPort());
        return nettyServer;
    }

}
