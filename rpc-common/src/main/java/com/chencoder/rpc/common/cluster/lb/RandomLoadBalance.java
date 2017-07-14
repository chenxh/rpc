package com.chencoder.rpc.common.cluster.lb;

import java.util.List;
import java.util.Random;

import com.chencoder.rpc.common.bean.RpcRequest;
import com.chencoder.rpc.common.bean.ServerInfo;

public class RandomLoadBalance implements LoadBalance{


	@Override
	public ServerInfo select(List<ServerInfo> serviceList, RpcRequest request) {
		if(serviceList == null || serviceList.size() == 0){
			return null;
		}
		Random r = new Random();
		return  serviceList.get(r.nextInt(serviceList.size()));
	}

}
