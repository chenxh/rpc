package com.chencoder.rpc.core.provider;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Providers {
	
	public static final Providers DEFAULT = new Providers();
	
	private Map<String, DefaultServiceProvider> providers = new ConcurrentHashMap<>();
	
	
	public static void exportService(Class interfaceClass, Object impl){
		DEFAULT.providers.put(interfaceClass.getName(), new DefaultServiceProvider(interfaceClass, impl));
	}
	
	public static DefaultServiceProvider getProvider(String serviceName){
		return DEFAULT.providers.get(serviceName);
	}

}
