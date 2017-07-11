package com.chencoder.rpc.core.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;

import com.chencoder.rpc.common.CompressType;
import com.chencoder.rpc.common.SerializeType;
import com.chencoder.rpc.common.bean.Header;
import com.chencoder.rpc.common.bean.RpcMessage;
import com.chencoder.rpc.common.bean.RpcRequest;
import com.chencoder.rpc.common.bean.Request;
import com.chencoder.rpc.common.bean.Response;
import com.chencoder.rpc.common.bean.ServerInfo;
import com.chencoder.rpc.common.config.ClientConfig;
import com.chencoder.rpc.common.util.RpcUtil;
import com.chencoder.rpc.core.cluster.DefaultCluster;
import com.chencoder.rpc.core.transport.TransportClient;
import com.chencoder.rpc.core.transport.ResponseFuture;
import com.chencoder.rpc.core.transport.netty.NettyClient;

public class JdkRpcDynamicProxy implements InvocationHandler{
	
	private ClientConfig clientConfig;
	
	private TransportClient client;
	
	private SerializeType serializeType;
	
	private CompressType compressType;
	
	public JdkRpcDynamicProxy(ClientConfig config){
		this.clientConfig = config;
		if(!StringUtils.isEmpty(config.getRegistryAddress())){
			client = new DefaultCluster(config);
		}else{
			ServerInfo serverInfo = new ServerInfo(config.getRemoteIp(), config.getRemotePort());
			try {
				client = new NettyClient(serverInfo);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		serializeType = SerializeType.getSerializeTypeByName(clientConfig.getSerializeType());
		compressType = CompressType.getCompressTypeByName(clientConfig.getCompressType());
	}

	@Override
	public Object invoke(Object target, Method method, Object[] args) throws Throwable {
		Request req = new Request();
		req.setServiceName(clientConfig.getServiceName());
		req.setMethodName(method.getName());
		req.setArgs(args);
		
		Header header = Header.HeaderMaker.newMaker()
                .make();
		header.setExtend(RpcUtil.getExtend(serializeType, compressType));
		RpcRequest message = new RpcRequest(header, req);
		ResponseFuture<?> future = client.request(message, clientConfig.getConnectionTimeout());
		Response resp =  (Response)future.getPromise().await();
		return resp.getResult();
	}

}
