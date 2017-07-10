package com.chencoder.rpc.core.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;

import com.chencoder.rpc.common.CompressType;
import com.chencoder.rpc.common.SerializeType;
import com.chencoder.rpc.common.bean.Header;
import com.chencoder.rpc.common.bean.Message;
import com.chencoder.rpc.common.bean.Request;
import com.chencoder.rpc.common.bean.Response;
import com.chencoder.rpc.common.bean.ServerInfo;
import com.chencoder.rpc.common.config.ClientConfig;
import com.chencoder.rpc.common.config.RpcRuntimeConfig;
import com.chencoder.rpc.common.util.RpcUtil;
import com.chencoder.rpc.core.RpcInvoker;
import com.chencoder.rpc.core.cluster.DefaultCluster;
import com.chencoder.rpc.core.invoker.ClusterClientInvoker;
import com.chencoder.rpc.core.invoker.SingleClientInvoker;
import com.chencoder.rpc.core.transport.TransportClient;
import com.chencoder.rpc.core.transport.TransportClientFactory;
import com.chencoder.rpc.core.transport.ResponseFuture;
import com.chencoder.rpc.core.transport.netty.NettyClient;

public class JdkRpcDynamicProxy2 implements InvocationHandler{
	
	private ClientConfig clientConfig;
	
	private TransportClient client;
	
	private SerializeType serializeType;
	
	private CompressType compressType;
	
	private RpcInvoker invoker;
	
	public JdkRpcDynamicProxy2(ClientConfig config){
		this.clientConfig = config;
		if(!StringUtils.isEmpty(config.getRegistryAddress())){
			//client = new DefaultCluster(config);
			invoker = new ClusterClientInvoker(config);
		}else{
			invoker = new SingleClientInvoker(
					TransportClientFactory.newTransportClient(config),
					config.newRuntimeConfig());
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
		Message<Request> message = new Message<Request>(header, req);
		return invoker.invoke(message, null);
		/*ResponseFuture<?> future = invoker.request(message, clientConfig.getConnectionTimeout());
		Response resp =  (Response)future.getPromise().await();
		return resp.getResult();*/
	}

}
