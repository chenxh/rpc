package com.chencoder.rpc.core.transport;

import com.chencoder.rpc.common.bean.ServerInfo;
import com.chencoder.rpc.common.config.ClientConfig;
import com.chencoder.rpc.core.transport.netty.NettyClient;


/**
 * 网络传输客户端工厂,现在只支持netty
 * @author chenxh
 *
 */
public class TransportClientFactory {
	
	public static TransportClient newTransportClient(ClientConfig config){
		ServerInfo serverInfo = new ServerInfo(config.getRemoteIp(), config.getRemotePort());
		try {
			return new NettyClient(serverInfo);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static TransportClient newTransportClient(ServerInfo serverInfo){
		try {
			return new NettyClient(serverInfo);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}
}
