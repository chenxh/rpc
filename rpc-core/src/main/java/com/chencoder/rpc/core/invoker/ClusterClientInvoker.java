package com.chencoder.rpc.core.invoker;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.apache.curator.x.discovery.ServiceInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chencoder.rpc.common.LoadBalanceType;
import com.chencoder.rpc.common.bean.MetaInfo;
import com.chencoder.rpc.common.bean.RpcException;
import com.chencoder.rpc.common.bean.RpcRequest;
import com.chencoder.rpc.common.bean.ServerInfo;
import com.chencoder.rpc.common.cluster.lb.LoadBalance;
import com.chencoder.rpc.common.cluster.lb.RandomLoadBalance;
import com.chencoder.rpc.common.config.ClientConfig;
import com.chencoder.rpc.core.pool.SimpleTransportClientPool;
import com.chencoder.rpc.core.registry.RpcServiceDiscovery;
import com.chencoder.rpc.core.registry.ZkServiceDiscovery;
import com.chencoder.rpc.core.transport.TransportClient;
import com.google.common.collect.Lists;

public class ClusterClientInvoker extends RpcClientInvoker{
	
	private static final Logger logger = LoggerFactory.getLogger(ClusterClientInvoker.class);
	
	private ClientConfig config;
	
	private RpcServiceDiscovery<MetaInfo> discovery;
	
	private String serviceName;
	
	private LoadBalance loadBalance;
	
	private SimpleTransportClientPool pool = new SimpleTransportClientPool();
	
	private List<ServerInfo> fails = Lists.newArrayList();
	
	public ClusterClientInvoker(){
		
	}
	
	public ClusterClientInvoker(ClientConfig config, Class<?> serviceClass){
		this.config = config;
		this.serviceName = serviceClass.getName();
		init();
	}

	@Override
	TransportClient getTransportClient(RpcRequest request) {
		try {
			Collection<ServiceInstance<MetaInfo>> servers = discovery.queryForInstances(serviceName);
			if(servers != null && servers.size() > 0){
				ServerInfo serverInfo = select(servers, request);
				if(serverInfo == null){
					throw new RpcException("no provider service selected");
				}
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
	
	public ServerInfo select(Collection<ServiceInstance<MetaInfo>> servers, RpcRequest request){
		List<ServerInfo> serverInfos = getServers(servers);
		ServerInfo selected = loadBalance.select(serverInfos, request);
		return selected;
	}

	private List<ServerInfo> getServers(Collection<ServiceInstance<MetaInfo>> servers) {
		if(servers == null || servers.isEmpty()){
			return null;
		}
		List<ServerInfo> res = Lists.newArrayListWithCapacity(servers.size());
		for(ServiceInstance<MetaInfo> inst: servers){
			res.add(new ServerInfo(inst));
		}
		return res;
	}

	@Override
	void addFailedClient(TransportClient client) {
		fails.add(client.getServerInfo());
	}

	@Override
	public void close() throws IOException {
		pool.close();
	}

}
