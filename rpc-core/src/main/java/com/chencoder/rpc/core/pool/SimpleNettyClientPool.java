package com.chencoder.rpc.core.pool;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import com.chencoder.rpc.common.bean.RpcException;
import com.chencoder.rpc.common.bean.ServerInfo;
import com.chencoder.rpc.core.transport.netty.NettyClient;

public class SimpleNettyClientPool {
	
	
	private ConcurrentHashMap<ServerInfo, NettyClient> poolMap = new ConcurrentHashMap<ServerInfo, NettyClient>();
	
	private ReentrantLock lock = new ReentrantLock();

	public SimpleNettyClientPool(){
	}
	
	public NettyClient getObject(ServerInfo key) throws InterruptedException{
		NettyClient client = poolMap.get(key);
		if(client == null){
			try{
				lock.lock();
				NettyClient v2 =  new NettyClient(key);
				poolMap.putIfAbsent(key, v2);
				return v2;
			}catch(Exception e){
				throw new RpcException("create NettyClient failed: " + e.getMessage());
			}finally{
				lock.unlock();
			}
			
		}else{
			return client;
		}
	}
	
}
