package com.chencoder.rpc.core.transport;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chencoder.rpc.common.bean.Message;
import com.chencoder.rpc.common.bean.ServerInfo;
import com.chencoder.rpc.core.transport.client.Promise;
import com.chencoder.rpc.core.transport.codec.NettyClientHandler;
import com.chencoder.rpc.core.transport.codec.NettyDecoder;
import com.chencoder.rpc.core.transport.codec.NettyEncoder;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 */
public class NettyClient implements Client {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyClient.class);

    protected Bootstrap b;
    protected EventLoopGroup group;
    private String host;
    private int port;
    
    private ChannelFuture channelFuture;

    public NettyClient(ServerInfo info) throws InterruptedException {
        this.host = info.getHost();
        this.port = info.getPort();
        init();
        Executors.newScheduledThreadPool(1).scheduleWithFixedDelay(
                new TimeoutMonitor("timeout_monitor_" + host + "_" + port), 100, 100, TimeUnit.MILLISECONDS);
    }

    private void init() throws InterruptedException {
        b = new Bootstrap();
        group = new NioEventLoopGroup(4);
        b.group(group)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        initClientChannel(ch);
                    }
                });

    }

    public void initClientChannel(SocketChannel ch) {
        ch.pipeline().addLast("encode", new NettyEncoder());
        ch.pipeline().addLast("decode", new NettyDecoder());
        ch.pipeline().addLast("handler", new NettyClientHandler());
    }

    public void connect() {
        ChannelFuture connect = b.connect(host, port);
        connect.awaitUninterruptibly();
        channelFuture =  connect;
    }

    @Override
    public void close() {
        group.shutdownGracefully();
    }

    class TimeoutMonitor implements Runnable {
        private String name;

        public TimeoutMonitor(String name) {
            this.name = name;
        }

        public void run() {
            long currentTime = System.currentTimeMillis();
           /* for (Map.Entry<Long, NettyResponseFuture<Response>> entry : Connection.callbackMap.entrySet()) {
                try {
                    NettyResponseFuture future = entry.getValue();
                    if (future.getCreateTime() + future.getTimeOut() < currentTime) {
                        // timeout: remove from callback list, and then cancel
                        Connection.callbackMap.remove(entry.getKey());
                    }
                } catch (Exception e) {
                    LOGGER.error(name + " clear timeout future Error: methodName="
                            + entry.getValue().getRequest().getHeader() + " requestId=" + entry.getKey(), e);
                }
            }*/
        }
    }

	@Override
	public ResponseFuture<?> request(Message message, long timeout) {
		if(!isConnect()){
			connect();
		}
		ResponseFuture responseFuture = new ResponseFuture(System.currentTimeMillis(), timeout, message, new Promise());
		channelFuture.channel().writeAndFlush(message);
		return responseFuture;
	}
	
	private boolean isConnect(){
		return channelFuture != null && channelFuture.channel().isActive();
	}

	@Override
	public ServerInfo getServerInfo() {
		return new ServerInfo(host,port);
	}

}
