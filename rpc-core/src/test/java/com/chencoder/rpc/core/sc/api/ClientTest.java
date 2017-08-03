package com.chencoder.rpc.core.sc.api;

import com.chencoder.rpc.common.config.ClientConfig;
import com.chencoder.rpc.core.RpcClient;

public class ClientTest {
	
	public static void main(String[] args) {
		
		ClientConfig config = new ClientConfig.Builder()
				.withRegistry("localhost:2181")
				.compressType("None")
				.serializeType("Kryo").build();
		
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
