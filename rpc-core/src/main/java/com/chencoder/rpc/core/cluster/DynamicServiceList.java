package com.chencoder.rpc.core.cluster;

import org.apache.curator.x.discovery.ServiceInstance;

import com.chencoder.rpc.core.registry.IServiceEventListener;

public class DynamicServiceList implements IServiceEventListener{

	@Override
	public void onRegister(ServiceInstance serviceInstance) {
		
	}

	@Override
	public void onRemove(ServiceInstance serviceInstance) {
		
	}

	@Override
	public void onUpdate(ServiceInstance serviceInstance) {
		
	}

}
