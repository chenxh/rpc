package com.chencoder.rpc.core.provider;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.curator.x.discovery.ServiceInstance;

import com.chencoder.rpc.common.bean.MetaInfo;
import com.chencoder.rpc.common.bean.RpcException;
import com.chencoder.rpc.common.config.RegistryConfig;
import com.chencoder.rpc.common.util.NetUtils;
import com.chencoder.rpc.core.registry.RpcServiceDiscovery;
import com.chencoder.rpc.core.registry.ZkServiceDiscovery;

public class Exporter {
	
	private Map<String, DefaultServiceProvider> providers = new ConcurrentHashMap<>();
	private RpcServiceDiscovery<MetaInfo> serviceDiscovery ;
	private RegistryConfig registryConfig;
	
	public Exporter(RegistryConfig registryConfig){
		this.setRegistryConfig(registryConfig);
		if(registryConfig != null){
			ZkServiceDiscovery zkServiceDiscovery = new ZkServiceDiscovery();
			zkServiceDiscovery.setAddress(registryConfig.getRegistryAddress());
			try {
				zkServiceDiscovery.start();
			} catch (Exception e) {
				throw new RpcException("连接注册中心失败");
			}
			serviceDiscovery = zkServiceDiscovery;
		}
	}
	
	public void exportService(Class<?> interfaceClass, Object impl, int port){
		providers.put(interfaceClass.getName(), new DefaultServiceProvider(interfaceClass, impl));
		if(serviceDiscovery != null){
			try {
				ServiceInstance<MetaInfo> serviceInstance = ServiceInstance.<MetaInfo>builder()
		                 .name(interfaceClass.getName())
		                 .address(NetUtils.getLocalAddress().getHostAddress())
		                 .port(port)
		                 .build();
				serviceDiscovery.registerService(serviceInstance);
			} catch (Exception e) {
				throw new RpcException("注册服务失败");
			}
		}
	}
	
	public DefaultServiceProvider getProvider(String serviceName){
		return providers.get(serviceName);
	}

	public RegistryConfig getRegistryConfig() {
		return registryConfig;
	}

	public void setRegistryConfig(RegistryConfig registryConfig) {
		this.registryConfig = registryConfig;
	}

}
