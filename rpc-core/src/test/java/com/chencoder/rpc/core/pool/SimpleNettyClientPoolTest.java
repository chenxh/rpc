package com.chencoder.rpc.core.pool;

import com.chencoder.rpc.common.bean.ServerInfo;

public class SimpleNettyClientPoolTest {
	
	public static void main(String[] args) {
		SimpleNettyClientPool pool = new SimpleNettyClientPool();
		
		ServerInfo info = new ServerInfo("127.0.0.1", 1333);
		try {
			for(int i=0; i<10; i++){
				System.out.println(pool.getObject(info));
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
