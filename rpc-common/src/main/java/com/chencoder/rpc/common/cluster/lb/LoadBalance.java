package com.chencoder.rpc.common.cluster.lb;

import java.util.List;

import org.apache.curator.x.discovery.ServiceInstance;

import com.chencoder.rpc.common.bean.MetaInfo;
import com.chencoder.rpc.common.bean.RpcRequest;
import com.chencoder.rpc.common.bean.ServerInfo;

public interface LoadBalance {

	ServerInfo select(List<ServerInfo> serviceList, RpcRequest request);
	
}
