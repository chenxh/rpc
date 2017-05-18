package com.chencoder.rpc.core.transport;

import com.chencoder.rpc.common.config.ServerConfig;
import com.chencoder.rpc.common.config.ServiceConfig;

/**
 * Created by Dempe on 2016/12/9.
 */
public class NettyServerFactory {

    private ServerConfig serverConfig;

    public NettyServerFactory(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    public NettyServer createServer(ServerConfig config) throws InterruptedException {
        NettyServer forestServer = new NettyServer(config, config.getPort());
        return forestServer;
    }

}
