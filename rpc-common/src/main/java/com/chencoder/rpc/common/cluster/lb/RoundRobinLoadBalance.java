package com.chencoder.rpc.common.cluster.lb;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.chencoder.rpc.common.bean.RpcRequest;
import com.chencoder.rpc.common.bean.ServerInfo;

public class RoundRobinLoadBalance implements LoadBalance{
	
	private AtomicLong next = new AtomicLong(0);

	@Override
	public ServerInfo select(List<ServerInfo> serviceList, RpcRequest request) {
		if(serviceList == null || serviceList.size() == 0){
			return null;
		}
		return serviceList.get( (int) (next.getAndIncrement() % serviceList.size()));
	}

}
