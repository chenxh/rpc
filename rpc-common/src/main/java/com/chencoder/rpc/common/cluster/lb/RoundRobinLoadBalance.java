package com.chencoder.rpc.common.cluster.lb;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.curator.x.discovery.ServiceInstance;

import com.chencoder.rpc.common.bean.MetaInfo;

public class RoundRobinLoadBalance implements LoadBalance{
	
	private AtomicLong next = new AtomicLong(0);

	@Override
	public ServiceInstance<MetaInfo> select(List<ServiceInstance<MetaInfo>> serviceList) {
		if(serviceList == null || serviceList.size() == 0){
			return null;
		}
		return serviceList.get( (int) (next.getAndIncrement() % serviceList.size()));
	}

}
