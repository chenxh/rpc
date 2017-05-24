package com.chencoder.rpc.common.config;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.chencoder.rpc.common.bean.RpcException;

/**
 */
public class ServiceConfig {

    private String serviceName;
    
    private Class<?> serviceInterface;

    private int connectionTimeout;
    
    private Object target;

    private Map<String, MethodConfig> methodConfigMap = new ConcurrentHashMap<>();
    
    private Map<String, ActionMethod> actionMethods = new ConcurrentHashMap<>();
    
    public void init(){
    	if(target == null){
    		throw new RpcException("无服务对象");
    	}
    	
    	if(serviceName == null){
    		serviceName = getServiceInterface().getSimpleName();
    	}
    	Class<? extends Object> clazz = target.getClass();
    	Method[] methods = clazz.getMethods();
    	for(Method method : methods){
    		if(method.getName().startsWith("$")){
    			continue;
    		}
    		actionMethods.put(createServiceMethodKey(serviceName, method), new ActionMethod(target, method));
    	}
    }
    
    private String createServiceMethodKey(String serviceName, Method method){
    	return serviceName + "-" + method.getName();
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Map<String, MethodConfig> getMethodConfigMap() {
        return methodConfigMap;
    }

    public void setMethodConfigMap(Map<String, MethodConfig> methodConfigMap) {
        this.methodConfigMap = methodConfigMap;
    }

    public MethodConfig getMethodConfig(String methodName) {
        return methodConfigMap.get(methodName);
    }

    public MethodConfig registerMethodConfig(String methodName, MethodConfig methodConfig) {
        return methodConfigMap.put(methodName, methodConfig);
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public Object getTarget() {
		return target;
	}

	public void setTarget(Object target) {
		this.target = target;
	}

	public static class Builder {
        private ServiceConfig config;

        public static Builder newBuilder() {
            Builder builder = new Builder();
            builder.config = new ServiceConfig();
            return builder;
        }

        public ServiceConfig build() {
            return config;
        }

        public Builder withConnectionTimeout(int connectionTimeout) {
            config.setConnectionTimeout(connectionTimeout);
            return this;
        }
        public Builder withMethodConfig(String methodName, MethodConfig methodConfig) {
            config.registerMethodConfig(methodName, methodConfig);
            return this;
        }

    }

	public ActionMethod getActionMethod(String serviceName, String methodName) {
		return actionMethods.get(serviceName + "-" + methodName);
	}

	public Class<?> getServiceInterface() {
		return serviceInterface;
	}

	public void setServiceInterface(Class<?> serviceInterface) {
		this.serviceInterface = serviceInterface;
	}
}
