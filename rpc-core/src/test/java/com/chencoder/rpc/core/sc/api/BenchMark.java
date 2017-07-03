package com.chencoder.rpc.core.sc.api;

import java.lang.reflect.Proxy;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chencoder.rpc.common.config.ClientConfig;
import com.chencoder.rpc.core.proxy.JdkRpcDynamicProxy;

public class BenchMark {
	 private final static Logger LOGGER = LoggerFactory.getLogger(BenchMark.class);
	public static void main(String[] args) {
		ClientConfig config = new ClientConfig();
		//config.setRemoteIp("127.0.0.1");
		//config.setRemotePort(1121);
		config.setRegistryAddress("localhost:2181");
		config.setServiceName(DemoService.class.getName());
		config.setCompressType("None");
		config.setSerializeType("Kyro");
		JdkRpcDynamicProxy proxy =  new JdkRpcDynamicProxy(config);
		DemoService demoService = (DemoService) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{DemoService.class}, proxy);
		
		ExecutorService pool = Executors.newFixedThreadPool(10);
		try{
			int size = 10000;
			long start = System.currentTimeMillis();
			CountDownLatch latch = new CountDownLatch(size);
			for(int i=0; i< size; i++){
				pool.execute(new Runnable() {
					@Override
					public void run() {
						demoService.testArgs("nihao");
						latch.countDown();
					}
				});
			}
			latch.await();
			LOGGER.info("cost["+(System.currentTimeMillis()-start) + "]ms");
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
}
