package com.chencoder.rpc.core.transport.netty;

import java.lang.reflect.Field;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import sun.misc.Unsafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chencoder.rpc.common.bean.Message;
import com.chencoder.rpc.common.bean.RpcException;
import com.chencoder.rpc.common.bean.ServerInfo;
import com.chencoder.rpc.core.transport.Client;
import com.chencoder.rpc.core.transport.ResponseFuture;
import com.chencoder.rpc.core.transport.client.Promise;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import sun.misc.Unsafe;

/**
 */
public class NettyClient implements Client {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyClient.class);

    protected Bootstrap b;
    protected EventLoopGroup group;
    private String host;
    private int port;
    
    private ReentrantLock lock = new ReentrantLock();
    
    private final static Unsafe unsafe;
    private static final long stateOffset;
    static {
    	try {
    		
    		Field field = Unsafe.class.getDeclaredField("theUnsafe");  
            field.setAccessible(true);  
            unsafe = (Unsafe)field.get(null);
			stateOffset = unsafe.objectFieldOffset(NettyClient.class.getDeclaredField("state"));
		} catch (Exception e) {
			throw new Error(e);
		}
    }
    
    
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
    
    private volatile ChannelFuture channelFuture;

    public NettyClient(ServerInfo info) throws InterruptedException {
        this.host = info.getHost();
        this.port = info.getPort();
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
        connect();
    }

    public void initClientChannel(SocketChannel ch) {
        ch.pipeline().addLast("encode", new NettyEncoder());
        ch.pipeline().addLast("decode", new NettyDecoder());
        ch.pipeline().addLast("handler", new NettyClientHandler());
    }

    public void connect() {
    	int stat = state;
    	if(stat == State.UN_CONN.getValue() && changeState(stat, State.CONNING.getValue())){
    		 long start = System.currentTimeMillis();
    		 ChannelFuture connect = b.connect(host, port);
    		 channelFuture = connect.awaitUninterruptibly();
    	     state = State.CONNECTED.getValue();
    	}else if(state == State.CONNING.getValue()){
    		while(state == State.CONNING.getValue()){
    			if(state == State.CONNECTED.getValue()){
    				break;
    			}
    		}
    	}
    }
    
    private boolean changeState(int srcState, int destState){
    	return unsafe.compareAndSwapInt(this, stateOffset , srcState, destState);
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

	@Override
	public ResponseFuture<?> request(Message message, long timeout) {
		if(state != State.CONNECTED.getValue()){
			connect();
		}
		ResponseFuture responseFuture = new ResponseFuture(System.currentTimeMillis(), timeout, message, new Promise());
		ResponseFuture.CALLBACKS.putIfAbsent(message.getHeader().getMessageID(), responseFuture);
		channelFuture.channel().writeAndFlush(message);
		return responseFuture;
	}
	
	public boolean isConnect(){
		return channelFuture != null && channelFuture.channel().isActive();
	}
	
	public boolean isActive(){
		return channelFuture != null && channelFuture.channel().isActive() && channelFuture.channel().isOpen();
	}

}
