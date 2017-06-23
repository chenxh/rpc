package com.chencoder.rpc.common.cluster.lb;

import java.util.List;
import java.util.Random;

import org.apache.curator.x.discovery.ServiceInstance;

import com.chencoder.rpc.common.bean.MetaInfo;

public class RandomLoadBalance implements LoadBalance{

	@Override
	public ServiceInstance<MetaInfo> select(List<ServiceInstance<MetaInfo>> serviceList) {
		if(serviceList == null || serviceList.size() == 0){
			return null;
		}
		Random r = new Random();
		return  serviceList.get(r.nextInt(serviceList.size()));
	}

}
