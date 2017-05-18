package com.chencoder.rpc.core.cluster;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.curator.x.discovery.ServiceInstance;

import com.chencoder.rpc.common.bean.Message;
import com.chencoder.rpc.common.bean.MetaInfo;
import com.chencoder.rpc.common.bean.ServerInfo;
import com.chencoder.rpc.common.config.ClientConfig;
import com.chencoder.rpc.core.registry.AbstractServiceEventListener;
import com.chencoder.rpc.core.registry.ServiceDiscovery;
import com.chencoder.rpc.core.registry.impl.ZkServiceDiscovery;
import com.chencoder.rpc.core.transport.Client;
import com.chencoder.rpc.core.transport.NettyClient;
import com.chencoder.rpc.core.transport.ResponseFuture;
import com.google.common.collect.Lists;

public class DefaultCluster extends AbstractServiceEventListener implements Cluster{
	
	private ClientConfig config;
	
	private ServiceDiscovery discovery;
	
	private List<Client> clients;
	
	public DefaultCluster(ClientConfig config){
		this.config = config;
		ZkServiceDiscovery zk = new ZkServiceDiscovery();
		zk.setAddress(config.getRegistryAddress());
		zk.subscribe(config.getServiceName(), this);
		try {
			Collection<ServiceInstance<MetaInfo>> instances = zk.queryForInstances(config.getServiceName());
			clients = Lists.newArrayList();
			for(ServiceInstance<MetaInfo> instance : instances){
				NettyClient nettyClient = new NettyClient(new ServerInfo(instance));
				clients.add(nettyClient);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		discovery = zk;
	}

	@Override
	public void connect() {
		
	}

	@Override
	public void close() {
		
	}

	@Override
	public ResponseFuture<?> request(Message message, long timeout) {
		return null;
	}

	@Override
	public void onRemove(ServiceInstance serviceInstance) {
		if(clients != null && clients.size() > 0){
			Iterator<Client> iterator = clients.iterator();
			while(iterator.hasNext()){
				Client client = iterator.next();
				ServerInfo serverInfo = client.getServerInfo();
				if(serverInfo.getHost().equals(serviceInstance.getAddress()) && serverInfo.getPort() == serviceInstance.getPort()){
					iterator.remove();
				}
			}
		}
	}

	@Override
	public ServerInfo getServerInfo() {
		return null;
	}

}

