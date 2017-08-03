package com.chencoder.rpc.demo.base;

import com.chencoder.rpc.common.config.ClientConfig;
import com.chencoder.rpc.core.RpcClient;
import com.chencoder.rpc.demo.base.TestService.DemoReq;

public class ClientMain {

	public static void main(String[] args) {
		ClientConfig config = new ClientConfig.Builder().withDirect("127.0.0.1", 1122).build();

		RpcClient client = new RpcClient(config);
		TestService refer = client.refer(TestService.class);
		
		DemoReq req = new DemoReq();
		req.setP1("p1");
		req.setP2("p2");
		refer.testObj(req);
		
	}

}
