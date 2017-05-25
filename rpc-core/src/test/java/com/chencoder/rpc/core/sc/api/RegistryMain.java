package com.chencoder.rpc.core.sc.api;

import org.apache.curator.test.TestingServer;

public class RegistryMain {

	private static TestingServer zkServer;

	public static void main(String[] args) {
		try {
			zkServer = new TestingServer(2181);
			zkServer.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	// TODO Auto-generated method stub

	}

}
