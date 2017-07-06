package com.chencoder.rpc.core.sc.api;

import java.lang.reflect.Proxy;

import com.chencoder.rpc.common.config.ClientConfig;
import com.chencoder.rpc.core.proxy.JdkRpcDynamicProxy;

public class ClientTest {
	
	public static void main(String[] args) {
		
		ClientConfig config = new ClientConfig();
		config.setRegistryAddress("localhost:2181");
		//config.setRemoteIp("127.0.0.1");
		//config.setRemotePort(1121);
		config.setServiceName(DemoService.class.getName());
		config.setCompressType("None");
		config.setSerializeType("Kyro");
		JdkRpcDynamicProxy proxy =  new JdkRpcDynamicProxy(config);
		DemoService demoService = (DemoService) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{DemoService.class}, proxy);
		//System.out.println(demoService.testString());
		//System.out.println(demoService.testArgs("nihao"));
		//demoService.testVoid();
		//System.out.println("end");
		
		try {
			while(true){
				Thread.sleep(20*1000);
				demoService.testVoid();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
