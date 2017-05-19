package com.chencoder.rpc.core.registry;

import java.util.Collection;

import org.apache.curator.test.TestingServer;
import org.apache.curator.x.discovery.ServiceInstance;
import org.junit.BeforeClass;
import org.junit.Test;

import com.chencoder.rpc.common.bean.MetaInfo;
import com.chencoder.rpc.core.registry.impl.ZkServiceDiscovery;

import org.junit.Assert;

public class ServiceDiscoveryTest {
	
	private static ServiceDiscovery discovery;
	
	private static TestingServer zkServer;
	
	private static final String SERVER_NAME = "test.service" ;
	
	@BeforeClass
	public static void init(){
		try {
			zkServer = new TestingServer(2181);
			zkServer.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testRegister() throws Exception{
		
		
		ZkServiceDiscovery zkServiceDiscovery  = new ZkServiceDiscovery();
		zkServiceDiscovery.setAddress("localhost:2181");
		zkServiceDiscovery.start();
		zkServiceDiscovery.subscribe(SERVER_NAME, new AbstractServiceEventListener<MetaInfo>(){
			@Override
			public void onRemove(ServiceInstance<MetaInfo> serviceInstance) {
				System.out.println("serviceInstance removed " + serviceInstance);
			}
			
		});
		
		Thread.sleep(1*1000);
		int serverPort = 2182;
        ServiceInstance<MetaInfo> serviceInstance = ServiceInstance.<MetaInfo>builder()
                .name(SERVER_NAME)
                .address("localhost")
                .port(serverPort)
                .build();
		zkServiceDiscovery.registerService(serviceInstance);
		Collection<ServiceInstance<MetaInfo>> instances = zkServiceDiscovery.queryForInstances(SERVER_NAME);
		Assert.assertEquals(1, instances.size());
		zkServiceDiscovery.unregisterService(serviceInstance);
		
		Thread.sleep(1*1000);
		
		instances = zkServiceDiscovery.queryForInstances(SERVER_NAME);
		Assert.assertEquals(0, instances.size());
		
		Thread.sleep(2*1000);
		
	}
}
