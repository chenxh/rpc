package com.chencoder.rpc.core.cluster;

import java.util.Collection;
import java.util.List;

import org.apache.curator.x.discovery.ServiceInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chencoder.rpc.common.LoadBalanceType;
import com.chencoder.rpc.common.bean.RpcMessage;
import com.chencoder.rpc.common.bean.RpcRequest;
import com.chencoder.rpc.common.bean.MetaInfo;
import com.chencoder.rpc.common.bean.RpcException;
import com.chencoder.rpc.common.bean.ServerInfo;
import com.chencoder.rpc.common.cluster.lb.LoadBalance;
import com.chencoder.rpc.common.cluster.lb.RandomLoadBalance;
import com.chencoder.rpc.common.config.ClientConfig;
import com.chencoder.rpc.core.pool.SimpleNettyClientPool;
import com.chencoder.rpc.core.registry.RpcServiceDiscovery;
import com.chencoder.rpc.core.registry.ZkServiceDiscovery;
import com.chencoder.rpc.core.transport.ResponseFuture;
import com.chencoder.rpc.core.transport.netty.NettyClient;
import com.google.common.collect.Lists;

public class DefaultCluster implements Cluster{
	
	private static final Logger logger = LoggerFactory.getLogger(DefaultCluster.class);
	
	private ClientConfig config;
	
	private RpcServiceDiscovery<MetaInfo> discovery;
	
	private String serviceName;
	
	private LoadBalance loadBalance;
	
	private SimpleNettyClientPool pool = new SimpleNettyClientPool();
	
	public DefaultCluster(ClientConfig config){
		this.config = config;
		this.serviceName = config.getServiceName();
		init();
	}
	
	protected void init() {
		ZkServiceDiscovery zk = new ZkServiceDiscovery(config.getRegistryAddress());
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

	@Override
	public void connect() {
		
	}

	@Override
	public void close() {
		
	}

	@Override
	public ResponseFuture<?> request(RpcRequest message, long timeout) {
		try {
			long start = System.currentTimeMillis();
			Collection<ServiceInstance<MetaInfo>> servers = discovery.queryForInstances(serviceName);
			if(servers != null && servers.size() > 0){
				ServiceInstance<MetaInfo> select = select(servers);
				if(select == null){
					throw new RpcException("no provider service selected");
				}
				ServerInfo serverInfo = new ServerInfo(select);
				NettyClient client = pool.getObject(serverInfo);
				long cost = System.currentTimeMillis() - start;
				if(cost > 0){
					logger.debug("query provider cost[{}]ms", cost);
				}
				ResponseFuture<?> resp = client.request(message, timeout);
				return resp;
			}else{
				throw new RpcException("no provider services");
			}
		} catch (Exception e) {
			throw new RpcException(e);
		}
	}
	
	public ServiceInstance<MetaInfo> select(Collection<ServiceInstance<MetaInfo>> servers){
		List<ServiceInstance<MetaInfo>> newArrayList = Lists.newArrayList(servers);
		ServiceInstance<MetaInfo> selected = loadBalance.select(newArrayList);
		return selected;
	}

	@Override
	public ServerInfo getServerInfo() {
		return null;
	}

}

