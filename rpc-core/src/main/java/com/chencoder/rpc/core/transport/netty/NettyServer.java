package com.chencoder.rpc.core.transport.netty;


import com.chencoder.rpc.common.config.ServerConfig;
import com.chencoder.rpc.core.provider.Exporter;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * Created by Dempe on 2016/12/9.
 */
public class NettyServer extends AbstractServer {
	
	private Exporter exporter;
    public NettyServer(ServerConfig config, int port,Exporter exporter) throws InterruptedException {
        super(config, port);
        this.exporter = exporter;
    }

    public NettyServer(ServerConfig config, Integer port) throws InterruptedException {
    	this(config, port, null);
	}

	public ChannelInitializer<SocketChannel> newChannelInitializer() {
        return new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast("decoder", new NettyDecoder());
                ch.pipeline().addLast("encoder", new NettyEncoder());
				ch.pipeline().addLast("processor", new NettyProcessorHandler(exporter));
            }
        };

    }
}
