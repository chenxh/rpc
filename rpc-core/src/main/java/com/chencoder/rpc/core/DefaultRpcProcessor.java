package com.chencoder.rpc.core;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.chencoder.rpc.common.MessageType;
import com.chencoder.rpc.common.bean.Request;
import com.chencoder.rpc.common.bean.Response;
import com.chencoder.rpc.common.bean.RpcRequest;
import com.chencoder.rpc.common.bean.RpcResponse;
import com.chencoder.rpc.common.interceptor.RpcInvokerInterceptor;
import com.chencoder.rpc.core.provider.DefaultServiceProvider;
import com.google.common.base.Preconditions;

public class DefaultRpcProcessor implements RpcProcessor{
	
	private Map<String, DefaultServiceProvider> providers = new ConcurrentHashMap<>();
	
	public DefaultRpcProcessor(){
		
	}

	@Override
	public RpcResponse proccess(RpcRequest request) {
		Request req = request.getRequest();
		DefaultServiceProvider provider = providers.get(req.getServiceName());
		Preconditions.checkNotNull(provider);
		
		RpcInvoker invoker = provider.findInvoker(req);
		Preconditions.checkNotNull(invoker);
		Object result;
		try {
			result = invoker.invoke(request);
			Response response = new Response();
			byte extend = (byte) (request.getHeader().getExtend() | MessageType.RESPONSE_MESSAGE_TYPE);
			request.getHeader().setExtend(extend);
		    response.setResult(result);
			return new RpcResponse(request.getHeader(), request.getHeader().getMessageID(), response);
		} catch(Throwable e){
			Response response = new Response();
			response.setResult(e);
			return new RpcResponse(request.getHeader(), request.getHeader().getMessageID(), response);
		}
	}
	
	public void addServiceProvider(Class<?> interfaceClass, Object impl){
		providers.put(interfaceClass.getName(), new DefaultServiceProvider(interfaceClass, impl));
	}

	@Override
	public void addServiceProvider(Class<?> interfaceClass, Object impl, List<RpcInvokerInterceptor> interceptors) {
		providers.put(interfaceClass.getName(), new DefaultServiceProvider(interfaceClass, impl, interceptors));
	}

}
