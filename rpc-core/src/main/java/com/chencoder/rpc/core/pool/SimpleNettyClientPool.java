package com.chencoder.rpc.core.pool;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chencoder.rpc.common.bean.RpcException;
import com.chencoder.rpc.common.bean.ServerInfo;
import com.chencoder.rpc.core.transport.netty.NettyClient;

public class SimpleNettyClientPool {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private ConcurrentHashMap<ServerInfo, NettyClient> poolMap = new ConcurrentHashMap<ServerInfo, NettyClient>();
	
	private ReentrantLock lock = new ReentrantLock();

	public SimpleNettyClientPool(){
	}
	
	public NettyClient getObject(ServerInfo key) throws InterruptedException{
		NettyClient client = poolMap.get(key);
		if(client == null){
			try{
				lock.lock();
				NettyClient v2 = poolMap.get(key);
				if(v2 != null){
					return v2;
				}
				v2 =  new NettyClient(key);
				poolMap.putIfAbsent(key, v2);
				logger.debug("create netty client success");
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
