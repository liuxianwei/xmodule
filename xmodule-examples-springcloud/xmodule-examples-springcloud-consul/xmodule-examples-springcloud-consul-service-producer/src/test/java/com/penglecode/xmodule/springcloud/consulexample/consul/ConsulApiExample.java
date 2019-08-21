package com.penglecode.xmodule.springcloud.consulexample.consul;

import static org.springframework.cloud.consul.discovery.ConsulServerUtils.findHost;
import static org.springframework.cloud.consul.discovery.ConsulServerUtils.getMetadata;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.QueryParams;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.agent.model.Self;
import com.ecwid.consul.v1.health.model.HealthService;

public class ConsulApiExample {

	private static final ConsulClient consulClient = new ConsulClient("172.16.94.27", 8500);
	
	public static void getAgentSelf() {
		Response<Self> response = consulClient.getAgentSelf();
		System.out.println(response.getValue());
	}
	
	public static void getServices() {
		Response<Map<String,List<String>>> response = consulClient.getCatalogServices(QueryParams.DEFAULT);
		System.out.println(">>> response = " + response);
		System.out.println(">>> services = " + response.getValue());
	}
	
	public static void getInstances() {
		List<ServiceInstance> instances = new ArrayList<>();
		String serviceName = "springcloud-consul-service-producer";
		Response<List<HealthService>> response = consulClient.getHealthServices(serviceName, false, QueryParams.DEFAULT);
		List<HealthService> services = response.getValue();
		
		for(HealthService service : services) {
			String host = findHost(service);
			
			System.out.println(service.getNode());
			
			Map<String, String> metadata = getMetadata(service);
			boolean secure = false;
			if (metadata.containsKey("secure")) {
				secure = Boolean.parseBoolean(metadata.get("secure"));
			}
			instances.add(new DefaultServiceInstance(service.getService().getId(), serviceName, host, service
					.getService().getPort(), secure, metadata));
		}
		for(ServiceInstance instance : instances) {
			System.out.println(instance);
		}
	}
	
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		try {
			//getAgentSelf();
			//getServices();
			getInstances();
		} catch (Exception e) {
			long end = System.currentTimeMillis();
			System.out.println(">>> const = " + (end - start) * 1.0 / 1000);
			e.printStackTrace();
		}
	}

}
