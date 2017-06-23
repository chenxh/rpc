package com.chencoder.rpc.core.cluster;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.curator.x.discovery.ServiceInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chencoder.rpc.common.bean.Message;
import com.chencoder.rpc.common.bean.MetaInfo;
import com.chencoder.rpc.common.bean.RpcException;
import com.chencoder.rpc.common.bean.ServerInfo;
import com.chencoder.rpc.common.cluster.lb.LoadBalance;
import com.chencoder.rpc.common.cluster.lb.RandomLoadBalance;
import com.chencoder.rpc.common.config.ClientConfig;
import com.chencoder.rpc.core.pool.KeyedNettyClientPool;
import com.chencoder.rpc.core.pool.KeyedNettyClientPoolFactory;
import com.chencoder.rpc.core.registry.ServiceDiscovery;
import com.chencoder.rpc.core.registry.impl.ZkServiceDiscovery;
import com.chencoder.rpc.core.transport.ResponseFuture;
import com.chencoder.rpc.core.transport.netty.NettyClient;
import com.google.common.collect.Lists;

public class DefaultCluster implements Cluster{
	
	private static final Logger logger = LoggerFactory.getLogger(DefaultCluster.class);
	
	private ClientConfig config;
	
	private ServiceDiscovery<MetaInfo> discovery;
	
	private String serviceName;
	
	private LoadBalance loadBalance;
	
	private KeyedNettyClientPool pool = new KeyedNettyClientPool(new KeyedNettyClientPoolFactory());
	
	public DefaultCluster(ClientConfig config){
		this.config = config;
		this.serviceName = config.getServiceName();
		init();
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
		loadBalance = new RandomLoadBalance();
	}

	@Override
	public void connect() {
		
	}

	@Override
	public void close() {
		
	}

	@Override
	public ResponseFuture<?> request(Message message, long timeout) {
		try {
			Collection<ServiceInstance<MetaInfo>> servers = discovery.queryForInstances(serviceName);
			if(servers != null && servers.size() > 0){
//				NettyClient client = new NettyClient(new ServerInfo(select(servers)));
				ServerInfo key = new ServerInfo(select(servers));
				NettyClient client = pool.borrowObject(key);
				ResponseFuture<?> resp = client.request(message, timeout);
				pool.returnObject(key, client);
				return resp;
			}
		} catch (Exception e) {
			throw new RpcException(e);
		}
		return null;
	}
	
	public ServiceInstance<MetaInfo> select(Collection<ServiceInstance<MetaInfo>> servers){
		List<ServiceInstance<MetaInfo>> newArrayList = Lists.newArrayList(servers);
		ServiceInstance<MetaInfo> selected = loadBalance.select(newArrayList);
		return selected;
	}

}

