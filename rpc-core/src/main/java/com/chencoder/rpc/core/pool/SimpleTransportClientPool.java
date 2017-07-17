package com.chencoder.rpc.core.pool;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chencoder.rpc.common.bean.RpcException;
import com.chencoder.rpc.common.bean.ServerInfo;
import com.chencoder.rpc.core.transport.TransportClient;
import com.chencoder.rpc.core.transport.TransportClientFactory;

public class SimpleTransportClientPool {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private ConcurrentHashMap<ServerInfo, TransportClient> poolMap = new ConcurrentHashMap<ServerInfo, TransportClient>();
	
	private ReentrantLock lock = new ReentrantLock();

	public SimpleTransportClientPool(){
	}
	
	public TransportClient getObject(ServerInfo key) throws InterruptedException{
		TransportClient client = poolMap.get(key);
		if(client == null){
			try{
				lock.lock();
				TransportClient v2 = poolMap.get(key);
				if(v2 != null){
					return v2;
				}
				v2 =  TransportClientFactory.newTransportClient(key);
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
