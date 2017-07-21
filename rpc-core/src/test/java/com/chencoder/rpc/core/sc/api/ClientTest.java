package com.chencoder.rpc.core.sc.api;

import com.chencoder.rpc.common.config.ClientConfig;
import com.chencoder.rpc.core.RpcClient;

public class ClientTest {
	
	public static void main(String[] args) {
		
		ClientConfig config = new ClientConfig();
		config.setRegistryAddress("localhost:2181");
		//config.setRemoteIp("127.0.0.1");
		//config.setRemotePort(1122);
		config.setCompressType("None");
		config.setSerializeType("K");
		//config.setInterceptors(Lists.newArrayList(new SimpleInterceptor()));
		
		RpcClient client = new RpcClient(config);
		DemoService demoService = client.refer(DemoService.class);
		System.out.println(demoService.testString());
		/*try {
			while(true){
				Thread.sleep(20*1000);
				
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
	}

}
