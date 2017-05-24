package com.chencoder.rpc.core.provider;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.chencoder.rpc.common.bean.Request;
import com.chencoder.rpc.common.config.ActionMethod;
import com.chencoder.rpc.common.config.ServiceConfig;
import com.google.common.base.Preconditions;

public class DefaultServiceProvider {
	
	private String serviceName;
	
	private Map<ServiceMethod, ActionMethod> actionMethods = new ConcurrentHashMap<>();
	
	public void init(ServiceConfig config){
		
	}
	
	public DefaultServiceProvider(Class<?> interfaceClass, Object impl){
		serviceName = interfaceClass.getName();
		Class<? extends Object> clazz = impl.getClass();
		for(Method method : interfaceClass.getMethods()){
			Method implMethod;
			try {
				implMethod = clazz.getMethod(method.getName(), method.getParameterTypes());
				Preconditions.checkNotNull(implMethod, "服务注册失败[实现和接口不一致]");
				actionMethods.put(createServiceMethod(serviceName,method), new ActionMethod(impl, implMethod));
			} catch (NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private ServiceMethod createServiceMethod(String serviceName,Method method){
		return new ServiceMethod(serviceName,method.getName(), method.getParameterTypes());
	}
	
	public ActionMethod findActionMethod(Request req){
		Class[] paramTypes = new Class[0];
		if(req.getArgs() != null && req.getArgs().length > 0){
			paramTypes = new Class[req.getArgs().length];
			for(int i=0; i<req.getArgs().length; i++ ){
				paramTypes[i] = req.getArgs()[i].getClass();
			}
		}
		ServiceMethod key = new ServiceMethod(req.getServiceName(), req.getMethodName(), paramTypes);
		return actionMethods.get(key);
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {	
		this.serviceName = serviceName;
	}

}
