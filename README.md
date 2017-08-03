# rpc
使用Netty 实现的一个简单rpc。

# features
- 直连调用完成
- 集群方式调用完成
- 网络连接池完成 (修改)
- 负载均衡完成
- HA
- 配置方式：Java配置
- 拦截器实现完成



# 直连调用

##客户端

``` java
		ClientConfig config = new ClientConfig.Builder().withDirect("127.0.0.1", 1122).build();

		RpcClient client = new RpcClient(config);
		TestService refer = client.refer(TestService.class);
		
		DemoReq req = new DemoReq();
		req.setP1("p1");
		req.setP2("p2");
		refer.testObj(req);
```
##服务端

``` java
		ServerConfig serverConfig = new ServerConfig.Builder(1122).build();
		
		RpcServer server = new RpcServer(serverConfig);
		server.export(TestService.class, new TestServiceImpl());
		server.startServer();
```
	