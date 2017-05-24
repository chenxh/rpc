package com.chencoder.rpc.core.sc.api;

public class DemoServiceImpl implements DemoService{

	@Override
	public void testVoid() {
		System.out.println("void");
	}

	@Override
	public String testString() {
		System.out.println("String");
		return "hello";
	}

	@Override
	public String testArgs(String greeting) {
		System.out.println(greeting);
		return "hello :" + greeting;
	}

}
