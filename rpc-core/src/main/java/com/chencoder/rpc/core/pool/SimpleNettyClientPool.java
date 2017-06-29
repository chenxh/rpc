package com.chencoder.rpc.core.pool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import com.chencoder.rpc.common.bean.ServerInfo;
import com.chencoder.rpc.core.transport.netty.NettyClient;

public class SimpleNettyClientPool {
	
	public static final int DEFAULT_SIZE_PER_KEY = 3;
	
	private ConcurrentHashMap<ServerInfo, NettyClient[]> poolMap = new ConcurrentHashMap<ServerInfo, NettyClient[]>();
	
	private ConcurrentHashMap<ServerInfo, AtomicInteger> keyIndexMap = new ConcurrentHashMap<ServerInfo, AtomicInteger>();

	private int maxPoolSize = DEFAULT_SIZE_PER_KEY;
	
	private ReentrantLock lock = new ReentrantLock();

	public SimpleNettyClientPool(){
		
	}
	
	public SimpleNettyClientPool(int maxPoolSize){
		this.maxPoolSize = maxPoolSize <= 0? DEFAULT_SIZE_PER_KEY : maxPoolSize;
	}
	
	public NettyClient getObject(ServerInfo key) throws InterruptedException{
		keyIndexMap.putIfAbsent(key, new AtomicInteger(0));
		AtomicInteger integer = keyIndexMap.get(key);
		poolMap.putIfAbsent(key, new NettyClient[maxPoolSize]);
		NettyClient[] list = poolMap.get(key);
		int index = integer.getAndIncrement() % maxPoolSize;
		NettyClient v = list[index];
		if(v == null){
			lock.lock();
			NettyClient v2 = list[index];
			if(v2 == null){
				list[index] = new NettyClient(key);
			}
			lock.unlock();
			return list[index];
		}else{
			return v;
		}
	}
	
}
