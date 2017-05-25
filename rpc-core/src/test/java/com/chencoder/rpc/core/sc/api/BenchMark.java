package com.chencoder.rpc.core.sc.api;

import java.lang.reflect.Proxy;

import com.chencoder.rpc.common.config.ClientConfig;
import com.chencoder.rpc.core.proxy.JdkRpcDynamicProxy;

public class BenchMark {
public static void main(String[] args) {
		ClientConfig config = new ClientConfig();
		config.setRemoteIp("127.0.0.1");
		config.setRemotePort(1121);
		config.setServiceName(DemoService.class.getName());
		config.setCompressType("None");
		config.setSerializeType("Kyro");
		JdkRpcDynamicProxy proxy =  new JdkRpcDynamicProxy(config);
		DemoService demoService = (DemoService) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{DemoService.class}, proxy);
		
		try{
			long start = System.currentTimeMillis();
			for(int i=0; i< 100000; i++){
				demoService.testArgs("nihao");
			}
			System.out.println("cost["+(System.currentTimeMillis()-start) + "]ms");
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
}
