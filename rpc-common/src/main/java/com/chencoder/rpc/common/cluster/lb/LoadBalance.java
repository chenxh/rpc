package com.chencoder.rpc.common.cluster.lb;

import java.util.List;

import org.apache.curator.x.discovery.ServiceInstance;

import com.chencoder.rpc.common.bean.MetaInfo;

public interface LoadBalance {

	ServiceInstance<MetaInfo> select(List<ServiceInstance<MetaInfo>> serviceList);
	
}
