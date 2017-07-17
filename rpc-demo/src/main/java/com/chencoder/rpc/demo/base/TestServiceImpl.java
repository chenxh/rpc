package com.chencoder.rpc.demo.base;

public class TestServiceImpl implements TestService{

	@Override
	public void testVoid() {
		System.out.println("void");
	}

	@Override
	public void testString(String req) {
		System.out.println("req:"+req);
	}

	@Override
	public String testObj(DemoReq req) {
		System.out.println("req:"+req);
		return "req:success";
	}

}
