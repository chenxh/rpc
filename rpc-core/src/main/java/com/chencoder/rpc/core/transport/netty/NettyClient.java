package com.chencoder.rpc.core.transport.netty;

import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chencoder.rpc.common.bean.RpcException;
import com.chencoder.rpc.common.bean.RpcRequest;
import com.chencoder.rpc.common.bean.ServerInfo;
import com.chencoder.rpc.core.transport.Promise;
import com.chencoder.rpc.core.transport.ResponseFuture;
import com.chencoder.rpc.core.transport.TransportClient;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

/**
 */
public class NettyClient implements TransportClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyClient.class);

    protected Bootstrap b;
    protected EventLoopGroup group;
    private String host;
    private int port;
    
    private ServerInfo serverInfo;
    
    enum State{
    	UN_CONN(1),CONNING(2),CONNECTED(3);
    	private int value;
    	State(int value){
    		this.value = value;
    	}
		public int getValue() {
			return value;
		}
		public void setValue(int value) {
			this.value = value;
		}
    	
    }
    private volatile int state = State.UN_CONN.getValue();
    
    private final AtomicInteger  stat = new AtomicInteger(State.UN_CONN.getValue());
    		
    private volatile Channel channel;

    public NettyClient(ServerInfo info) throws InterruptedException {
        this.host = info.getHost();
        this.port = info.getPort();
        this.serverInfo = info;
        try{
        	init();
        }catch(Throwable e){
        	throw new RpcException("init netty client faliry :" + e.getMessage());
        }
/*        Executors.newScheduledThreadPool(1).scheduleWithFixedDelay(
                new TimeoutMonitor("timeout_monitor_" + host + "_" + port), 100, 100, TimeUnit.MILLISECONDS);*/
    }

    private void init() throws InterruptedException {
        b = new Bootstrap();
        group = new NioEventLoopGroup(2);
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
        connect();
    }

    public void initClientChannel(SocketChannel ch) {
    	ch.pipeline().addLast("idleStateHandler", new IdleStateHandler(10, 0, 0) );
    	ch.pipeline().addLast("heartBeatHandler", new HeartBeatHandler());
        ch.pipeline().addLast("encode", new NettyEncoder());
        ch.pipeline().addLast("decode", new NettyDecoder());
        ch.pipeline().addLast("handler", new NettyClientHandler());
    }

    public void connect() {
    	if(isConnect()){
    		return ;
    	}
    	int stat = this.stat.get();
    	if(stat == State.UN_CONN.getValue() && changeState(stat, State.CONNING.getValue())){
    		 ChannelFuture connect = b.connect(host, port);
    		 connect = connect.awaitUninterruptibly();
    		 channel = connect.channel();
    	     changeState(stat, State.CONNECTED.getValue());
    	}else if(this.stat.get() == State.CONNING.getValue()){
    		while(this.stat.get() == State.CONNING.getValue()){
    			if(this.stat.get() == State.CONNECTED.getValue()){
    				break;
    			}
    		}
    	}
    }
    
    private boolean changeState(int srcState, int destState){
    	//return unsafe.compareAndSwapInt(this, stateOffset , srcState, destState);
    	return stat.compareAndSet(srcState, destState);
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
            for (Entry<Long, ResponseFuture<?>> entry : ResponseFuture.CALLBACKS.entrySet()) {
                try {
                    ResponseFuture future = entry.getValue();
                    if (future.getTimeOut() > 0 && future.getCreateTime() + future.getTimeOut() < currentTime) {
                        // timeout: remove from callback list, and then cancel
                    	ResponseFuture.CALLBACKS.remove(entry.getKey());
                    }
                } catch (Exception e) {
                    LOGGER.error(name + " clear timeout future Error: methodName="
                            + entry.getValue().getRequest().getHeader() + " requestId=" + entry.getKey(), e);
                }
            }
        }
    }

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public ResponseFuture<?> request(RpcRequest message, long timeout) {
		if(state != State.CONNECTED.getValue()){
			connect();
		}
/*		if(!isConnect()){
			state = State.UN_CONN.getValue();
			connect();
		}*/
		ResponseFuture responseFuture = new ResponseFuture(System.currentTimeMillis(), timeout, message, new Promise());
		ResponseFuture.CALLBACKS.putIfAbsent(message.getHeader().getMessageID(), responseFuture);
		channel.writeAndFlush(message);
		return responseFuture;
	}
	
	public boolean isConnect(){
		return channel != null && channel.isActive();
	}

	@Override
	public ServerInfo getServerInfo() {
		return serverInfo;
	}

}
