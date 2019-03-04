1、OAuth2认证授权URL:

	http://127.0.0.1:8081/oauth/authorize?response_type=code&client_id=monkey&redirect_uri=http://www.baidu.com&scope=app
	
2、如果authorization-server端使用InMemoryTokenStore的话，那么resource-server端只能配置RemoteTokenServices(通过HTTP访问authorization-server端的check_token服务来进行token鉴权)