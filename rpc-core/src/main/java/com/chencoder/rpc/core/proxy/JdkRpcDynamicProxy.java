package com.chencoder.rpc.core.proxy;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
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
import com.chencoder.rpc.core.invoker.RpcInvokerWraper;
import com.chencoder.rpc.core.invoker.SingleClientInvoker;
import com.chencoder.rpc.core.transport.TransportClientFactory;
import org.apache.commons.lang3.tuple.Pair;

public class JdkRpcDynamicProxy implements InvocationHandler,Closeable{
	
	private ClientConfig clientConfig;
	
	private SerializeType serializeType;
	
	private CompressType compressType;
	
	private RpcInvoker invoker;
	
	private Class<?> serviceClass;
	
	public JdkRpcDynamicProxy(ClientConfig config, Class<?> serviceClass){
		this.clientConfig = config;
		this.serviceClass = serviceClass;
		if(!StringUtils.isEmpty(config.getRegistryAddress())){
			invoker = new ClusterClientInvoker(config, serviceClass);
		}else{
			invoker = new SingleClientInvoker(
					TransportClientFactory.newTransportClient(config));
		}
		
		invoker = new RpcInvokerWraper(invoker,config.getInterceptors());
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
		Pair<Boolean, Object> result = invokeObjectMethod(target, method, args);
		if(result.getKey()){
			return result.getValue();
		}
		Request req = new Request();
		req.setServiceName(serviceClass.getName());
		req.setMethodName(method.getName());
		req.setArgs(args);
		
		Header header = Header.HeaderMaker.newMaker()
                .make();
		header.setExtend(RpcUtil.getExtend(serializeType, compressType));
		RpcRequest message = new RpcRequest(header, req);
		setRpcContext();
		return invoker.invoke(message);
	}

	private Pair<Boolean, Object> invokeObjectMethod(Object target, Method method, Object[] args) {
		if (method.getDeclaringClass().equals(Object.class)) {
			try {
				return Pair.of(true, method.invoke(target, args));
			} catch (IllegalAccessException e) {
			} catch (InvocationTargetException e) {
			}
		}
		return Pair.of(false, null);
	}

	private void setRpcContext() {
		RpcContext.getContext().setTimout(clientConfig.getTimeOut());
		//TODO:other
		//RpcContext.getContext().setTimout(clientConfig.getTimeOut());
	}

	@Override
	public void close() throws IOException {
		invoker.close();
	}

}
