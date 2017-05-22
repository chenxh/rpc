package com.chencoder.rpc.core.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.chencoder.rpc.common.CompressType;
import com.chencoder.rpc.common.SerializeType;
import com.chencoder.rpc.common.bean.Header;
import com.chencoder.rpc.common.bean.Message;
import com.chencoder.rpc.common.bean.Request;
import com.chencoder.rpc.common.bean.ServerInfo;
import com.chencoder.rpc.common.config.ClientConfig;
import com.chencoder.rpc.common.util.RpcUtil;
import com.chencoder.rpc.core.cluster.DefaultCluster;
import com.chencoder.rpc.core.transport.Client;
import com.chencoder.rpc.core.transport.NettyClient;
import com.chencoder.rpc.core.transport.ResponseFuture;
import com.google.common.collect.Maps;

public class JdkRpcDynamicProxy implements InvocationHandler{
	
	private ClientConfig clientConfig;
	
	private Client client;
	
	private SerializeType serializeType;
	
	private CompressType compressType;
	
	public Map<Method, Header> headerMapCache = Maps.newConcurrentMap();
	
	public JdkRpcDynamicProxy(ClientConfig config) throws InterruptedException{
		this.clientConfig = config;
		if(!StringUtils.isEmpty(config.getRegistryAddress())){
			client = new DefaultCluster(config);
		}else{
			ServerInfo serverInfo = new ServerInfo(config.getRemoteIp(), config.getRemotePort());
			client = new NettyClient(serverInfo);
		}
		
		serializeType = SerializeType.getSerializeTypeByName(clientConfig.getSerializeType());
		compressType = CompressType.getCompressTypeByName(clientConfig.getCompressType());
	}

	@Override
	public Object invoke(Object target, Method method, Object[] args) throws Throwable {
		Request req = new Request();
		req.setServiceName(target.getClass().getSimpleName());
		req.setMethodName(method.getName());
		req.setArgs(args);
		
		Header header = headerMapCache.get(method);
		if (header == null) {
            header = Header.HeaderMaker.newMaker()
                    .make();
            header.setExtend(RpcUtil.getExtend(serializeType, compressType));
            headerMapCache.put(method, header);
        }
		Message<Request> message = new Message<Request>(header, req);
		ResponseFuture<?> future = client.request(message, clientConfig.getConnectionTimeout());
		return future.getPromise().await();
	}

}
