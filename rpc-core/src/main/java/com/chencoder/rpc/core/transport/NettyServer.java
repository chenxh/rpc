package com.chencoder.rpc.core.transport;


import com.chencoder.rpc.common.config.ServerConfig;
import com.chencoder.rpc.common.config.ServiceConfig;
import com.chencoder.rpc.core.transport.codec.NettyDecoder;
import com.chencoder.rpc.core.transport.codec.NettyEncoder;
import com.chencoder.rpc.core.transport.codec.NettyProcessorHandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * Created by Dempe on 2016/12/9.
 */
public class NettyServer extends AbstractServer {

	
	
    public NettyServer(ServerConfig config, int port) throws InterruptedException {
        super(config, port);
    }

    public ChannelInitializer<SocketChannel> newChannelInitializer() {
        return new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast("decoder", new NettyDecoder());
                ch.pipeline().addLast("encoder", new NettyEncoder());
				ch.pipeline().addLast("processor", new NettyProcessorHandler());
            }
        };

    }
}
