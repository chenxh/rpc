package com.chencoder.rpc.core.provider;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.chencoder.rpc.common.bean.Request;
import com.chencoder.rpc.common.interceptor.RpcInvokerInterceptor;
import com.chencoder.rpc.core.RpcInvoker;
import com.chencoder.rpc.core.invoker.RpcInvokerWraper;
import com.chencoder.rpc.core.invoker.RpcServiceInvoker;
import com.google.common.base.Preconditions;

public class DefaultServiceProvider {
	
	private String serviceName;
	
	private Map<ServiceMethod, RpcInvoker> invokers = new ConcurrentHashMap<>();
	
	public DefaultServiceProvider(Class<?> interfaceClass, Object impl){
		this(interfaceClass, impl, null);
	}
	
	public DefaultServiceProvider(Class<?> interfaceClass, Object impl, List<RpcInvokerInterceptor> interceptors){
		serviceName = interfaceClass.getName();
		Class<? extends Object> clazz = impl.getClass();
		for(Method method : interfaceClass.getMethods()){
			Method implMethod;
			try {
				implMethod = clazz.getMethod(method.getName(), method.getParameterTypes());
				Preconditions.checkNotNull(implMethod, "服务注册失败[实现和接口不一致]");
				RpcServiceInvoker invoker = new RpcServiceInvoker(impl, method);
				RpcInvokerWraper wrapedInvoker = new RpcInvokerWraper(invoker, interceptors);
				invokers.put(createServiceMethod(serviceName,method), wrapedInvoker);
			} catch (NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
		}
	}
	
	private ServiceMethod createServiceMethod(String serviceName,Method method){
		return new ServiceMethod(serviceName,method.getName(), method.getParameterTypes());
	}
	public RpcInvoker findInvoker(Request req){
		Class[] paramTypes = new Class[0];
		if(req.getArgs() != null && req.getArgs().length > 0){
			paramTypes = new Class[req.getArgs().length];
			for(int i=0; i<req.getArgs().length; i++ ){
				paramTypes[i] = req.getArgs()[i].getClass();
			}
		}
		ServiceMethod key = new ServiceMethod(req.getServiceName(), req.getMethodName(), paramTypes);
		return invokers.get(key);
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {	
		this.serviceName = serviceName;
	}

}
