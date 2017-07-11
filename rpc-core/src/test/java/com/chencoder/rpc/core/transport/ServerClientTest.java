package com.chencoder.rpc.core.transport;

import com.chencoder.rpc.common.EventType;
import com.chencoder.rpc.common.bean.Header;
import com.chencoder.rpc.common.bean.RpcMessage;
import com.chencoder.rpc.common.bean.RpcRequest;
import com.chencoder.rpc.common.bean.ServerInfo;
import com.chencoder.rpc.common.config.ServerConfig;
import com.chencoder.rpc.core.transport.netty.NettyClient;
import com.chencoder.rpc.core.transport.netty.NettyServer;

import io.netty.channel.ChannelFuture;

public class ServerClientTest {
	
	private static final int port = 1121;
	
	public static void main(String[] args) {
		ServerConfig config = new ServerConfig();
		config.setPort(port);
		config.setSoBacklog(128);
		config.setSoKeepAlive(true);
		config.setTcpNoDelay(true);
		try {
			NettyServer server = new NettyServer(config , port);
			server.start();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		ServerInfo info = new ServerInfo();
		info.setHost("127.0.0.1");
		info.setPort(port);
		try {
			NettyClient client = new NettyClient(info);
			client.connect();
			client.request(createHearbeatMsg(), -1L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private static RpcRequest createHearbeatMsg(){
		Header heartBeatHeader = Header.HeaderMaker.newMaker().make();
        heartBeatHeader.setExtend(EventType.HEARTBEAT.getValue());
        RpcRequest message = new RpcRequest(heartBeatHeader, null);
        return message;
	}

}
