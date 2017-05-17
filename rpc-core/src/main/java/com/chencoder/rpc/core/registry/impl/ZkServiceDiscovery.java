package com.chencoder.rpc.core.registry.impl;

import java.util.Collection;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.InstanceSerializer;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;
import org.codehaus.jackson.map.annotate.JsonRootName;

import com.chencoder.rpc.common.Constants;
import com.chencoder.rpc.common.HaStrategyType;
import com.chencoder.rpc.common.LoadBalanceType;
import com.chencoder.rpc.core.registry.AbstractServiceDiscovery;
import com.chencoder.rpc.core.registry.IServiceEventListener;

/**
 * Created by Dempe on 2016/12/8.
 */
public class ZkServiceDiscovery extends AbstractServiceDiscovery<MetaInfo> implements TreeCacheListener {

    private final static InstanceSerializer serializer = new JsonInstanceSerializer<>(MetaInfo.class);

    private ServiceDiscovery<MetaInfo> serviceDiscovery;

    private String address = "localhost:2181";

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
    public void registerService(ServiceInstance service) throws Exception {
        serviceDiscovery.registerService(service);
    }

    @Override
    public void updateService(ServiceInstance service) throws Exception {
        serviceDiscovery.unregisterService(service);
    }

    @Override
    public void unregisterService(ServiceInstance service) throws Exception {
        serviceDiscovery.unregisterService(service);
    }

    @Override
    public Collection<String> queryForNames() throws Exception {
        return serviceDiscovery.queryForNames();
    }

    @Override
    public Collection<ServiceInstance<MetaInfo>> queryForInstances(String name) throws Exception {
        return serviceDiscovery.queryForInstances(name);
    }

    @Override
    public ServiceInstance<MetaInfo> queryForInstance(String name, String id) throws Exception {
        return serviceDiscovery.queryForInstance(name, id);
    }

    @Override
    public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
        ChildData data = event.getData();
        ServiceInstance serviceInstance = serializer.deserialize(data.getData());
        switch (event.getType()) {
            case NODE_ADDED: {
                notify(serviceInstance, IServiceEventListener.ServiceEvent.ON_REGISTER);
                break;
            }
            case NODE_UPDATED: {
                notify(serviceInstance, IServiceEventListener.ServiceEvent.ON_UPDATE);
                break;
            }
            case NODE_REMOVED: {
                notify(serviceInstance, IServiceEventListener.ServiceEvent.ON_REMOVE);
                break;
            }
            default:
        }
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

 @JsonRootName("metaInfo") 
 class MetaInfo {

    private HaStrategyType haStrategyType;

    private LoadBalanceType loadBalanceType;

    private int avgTime;

    private int total;

    private int successCount;

    private int maxTime;

    private int minTime;

    public HaStrategyType getHaStrategyType() {
        return haStrategyType;
    }

    public void setHaStrategyType(HaStrategyType haStrategyType) {
        this.haStrategyType = haStrategyType;
    }

    public LoadBalanceType getLoadBalanceType() {
        return loadBalanceType;
    }

    public void setLoadBalanceType(LoadBalanceType loadBalanceType) {
        this.loadBalanceType = loadBalanceType;
    }

    public int getAvgTime() {
        return avgTime;
    }

    public void setAvgTime(int avgTime) {
        this.avgTime = avgTime;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }

    public int getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(int maxTime) {
        this.maxTime = maxTime;
    }

    public int getMinTime() {
        return minTime;
    }

    public void setMinTime(int minTime) {
        this.minTime = minTime;
    }

    @Override
    public String toString() {
        return "MetaInfo{" +
                "haStrategyType=" + haStrategyType +
                ", loadBalanceType=" + loadBalanceType +
                ", avgTime=" + avgTime +
                ", total=" + total +
                ", successCount=" + successCount +
                ", maxTime=" + maxTime +
                ", minTime=" + minTime +
                '}';
    }
}
