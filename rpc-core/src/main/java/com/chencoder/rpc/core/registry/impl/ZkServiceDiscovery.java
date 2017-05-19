package com.chencoder.rpc.core.registry.impl;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceCache;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.InstanceSerializer;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;

import com.chencoder.rpc.common.Constants;
import com.chencoder.rpc.common.bean.MetaInfo;
import com.chencoder.rpc.core.registry.AbstractServiceDiscovery;

/**
 */
public class ZkServiceDiscovery extends AbstractServiceDiscovery<MetaInfo> {

    private final static InstanceSerializer<MetaInfo> serializer = new JsonInstanceSerializer<>(MetaInfo.class);

    private ServiceDiscovery<MetaInfo> serviceDiscovery;

    private String address = "localhost:2181";
    
    private final static ConcurrentHashMap<String, ServiceCache<MetaInfo>> cacheMap = new ConcurrentHashMap<String, ServiceCache<MetaInfo>>(); 

    public void start() throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.newClient(address, new ExponentialBackoffRetry(1000, 3));
        client.start();
        serviceDiscovery = ServiceDiscoveryBuilder.builder(MetaInfo.class)
                .client(client)
                .basePath(Constants.BASE_PATH)
                .serializer(serializer)
                .build();
        serviceDiscovery.start();
    }

    @Override
    public void registerService(ServiceInstance<MetaInfo> service) throws Exception {
        serviceDiscovery.registerService(service);
    }

    @Override
    public void updateService(ServiceInstance<MetaInfo> service) throws Exception {
        serviceDiscovery.unregisterService(service);
    }

    @Override
    public void unregisterService(ServiceInstance<MetaInfo> service) throws Exception {
        serviceDiscovery.unregisterService(service);
    }

    @Override
    public Collection<String> queryForNames() throws Exception {
        return serviceDiscovery.queryForNames();
    }

    @Override
    public Collection<ServiceInstance<MetaInfo>> queryForInstances(String name) throws Exception {
    	ServiceCache<MetaInfo> serviceCache = getServiceCache(name);
    	if(serviceCache != null){
    		return serviceCache.getInstances();
    	}
        return serviceDiscovery.queryForInstances(name);
    }

    @Override
    public ServiceInstance<MetaInfo> queryForInstance(String name, String id) throws Exception {
        return serviceDiscovery.queryForInstance(name, id);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
    private ServiceCache<MetaInfo> getServiceCache(String name) throws Exception{
    	ServiceCache<MetaInfo> cache = cacheMap.get(name);
        if(cache == null){
        	final ServiceCache<MetaInfo> newCache = serviceDiscovery.serviceCacheBuilder().name(name).build();
        	newCache.start();
            cacheMap.putIfAbsent(name, newCache);
            return newCache;
        }
        return cache;
    }
}
