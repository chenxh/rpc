package com.chencoder.rpc.core.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;

import com.chencoder.rpc.common.CompressType;
import com.chencoder.rpc.common.SerializeType;
import com.chencoder.rpc.common.bean.Header;
import com.chencoder.rpc.common.bean.Request;
import com.chencoder.rpc.common.bean.RpcContext;
import com.chencoder.rpc.common.bean.RpcException;
import com.chencoder.rpc.common.bean.RpcRequest;
import com.chencoder.rpc.common.config.ClientConfig;
import com.chencoder.rpc.common.util.RpcUtil;
import com.chencoder.rpc.core.RpcInvoker;
import com.chencoder.rpc.core.invoker.ClusterClientInvoker;
import com.chencoder.rpc.core.invoker.SingleClientInvoker;
import com.chencoder.rpc.core.transport.TransportClientFactory;

public class JdkRpcDynamicProxy implements InvocationHandler{
	
	private ClientConfig clientConfig;
	
	private SerializeType serializeType;
	
	private CompressType compressType;
	
	private RpcInvoker invoker;
	
	public JdkRpcDynamicProxy(ClientConfig config){
		this.clientConfig = config;
		if(!StringUtils.isEmpty(config.getRegistryAddress())){
			//client = new DefaultCluster(config);
			invoker = new ClusterClientInvoker(config);
		}else{
			invoker = new SingleClientInvoker(
					TransportClientFactory.newTransportClient(config));
		}
		serializeType = SerializeType.getSerializeTypeByName(clientConfig.getSerializeType());
		if(serializeType == null){
			throw new RpcException("no soupport serializeType ["+ clientConfig.getSerializeType() +"]");
		}
		compressType = CompressType.getCompressTypeByName(clientConfig.getCompressType());
		if(compressType == null){
			throw new RpcException("no soupport compressType ["+ clientConfig.getCompressType() +"]");
		}
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
		setRpcContext();
		return invoker.invoke(message);
	}

	private void setRpcContext() {
		RpcContext.getContext().setTimout(clientConfig.getTimeOut());
		//TODO:other
		//RpcContext.getContext().setTimout(clientConfig.getTimeOut());
	}

}
