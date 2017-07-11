package com.chencoder.rpc.core.invoker;

import java.util.Collection;
import java.util.List;

import org.apache.curator.x.discovery.ServiceInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chencoder.rpc.common.LoadBalanceType;
import com.chencoder.rpc.common.bean.MetaInfo;
import com.chencoder.rpc.common.bean.RpcException;
import com.chencoder.rpc.common.bean.ServerInfo;
import com.chencoder.rpc.common.cluster.lb.LoadBalance;
import com.chencoder.rpc.common.cluster.lb.RandomLoadBalance;
import com.chencoder.rpc.common.config.ClientConfig;
import com.chencoder.rpc.core.cluster.DefaultCluster;
import com.chencoder.rpc.core.pool.SimpleNettyClientPool;
import com.chencoder.rpc.core.registry.RpcServiceDiscovery;
import com.chencoder.rpc.core.registry.ZkServiceDiscovery;
import com.chencoder.rpc.core.transport.TransportClient;
import com.google.common.collect.Lists;

public class ClusterClientInvoker extends RpcClientInvoker{
	
	private static final Logger logger = LoggerFactory.getLogger(DefaultCluster.class);
	
	private ClientConfig config;
	
	private RpcServiceDiscovery<MetaInfo> discovery;
	
	private String serviceName;
	
	private LoadBalance loadBalance;
	
	private SimpleNettyClientPool pool = new SimpleNettyClientPool();
	
	public ClusterClientInvoker(){
		
	}
	
	public ClusterClientInvoker(ClientConfig config){
		this.config = config;
		this.serviceName = config.getServiceName();
		init();
	}

	@Override
	TransportClient getTransportClient() {
		try {
			Collection<ServiceInstance<MetaInfo>> servers = discovery.queryForInstances(serviceName);
			if(servers != null && servers.size() > 0){
				ServiceInstance<MetaInfo> select = select(servers);
				if(select == null){
					throw new RpcException("no provider service selected");
				}
				ServerInfo serverInfo = new ServerInfo(select);
				return pool.getObject(serverInfo);
			}else{
				throw new RpcException("no provider services");
			}
		} catch (Exception e) {
			logger.error("get transport client error:", e);
			throw new RpcException(e);
		}
	}
	
	

	
	protected void init() {
		ZkServiceDiscovery zk = new ZkServiceDiscovery();
		zk.setAddress(config.getRegistryAddress());
		try {
			zk.start();
		} catch (Exception e) {
			throw new RpcException("连接注册中心失败");
		}
		discovery = zk;
		if(config.getLoadBalance() == null){
			loadBalance = new RandomLoadBalance();
		}else{
			loadBalance = LoadBalanceType.getLoadBalance(config.getLoadBalance());
		}
	}
	
	public ServiceInstance<MetaInfo> select(Collection<ServiceInstance<MetaInfo>> servers){
		List<ServiceInstance<MetaInfo>> newArrayList = Lists.newArrayList(servers);
		ServiceInstance<MetaInfo> selected = loadBalance.select(newArrayList);
		return selected;
	}

	@Override
	TransportClient nextBackTransport() {
		// TODO Auto-generated method stub
		return null;
	}

}
