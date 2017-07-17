package com.chencoder.rpc.core.sc.api;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chencoder.rpc.common.config.ClientConfig;
import com.chencoder.rpc.core.RpcClient;

public class BenchMark {
	 private final static Logger LOGGER = LoggerFactory.getLogger(BenchMark.class);
	public static void main(String[] args) {
		ClientConfig config = new ClientConfig();
		config.setRegistryAddress("localhost:2181");
		//config.setRemoteIp("127.0.0.1");
		//config.setRemotePort(1122);
		config.setCompressType("None");
		config.setSerializeType("Kyro");
		
		RpcClient client = new RpcClient(config);
		DemoService demoService = client.refer(DemoService.class);
		ExecutorService pool = Executors.newFixedThreadPool(30);
		try{
			int size = 100000;
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
