package com.penglecode.xmodule.springcloud.consulexample.consul;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import com.ecwid.consul.transport.TransportException;
import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.QueryParams;
import com.penglecode.xmodule.common.util.DateTimeUtils;

public class RetryTemplateTest {

	public static void main(String[] args) {
		ConsulClient client = new ConsulClient("172.16.94.27", 8500);
		
		RetryListener retryListener = new RetryListener() {

			@Override
			public <T, E extends Throwable> boolean open(RetryContext context, RetryCallback<T, E> callback) {
				System.out.println(">>> open : " + context.getRetryCount());
				return true;
			}

			@Override
			public <T, E extends Throwable> void close(RetryContext context, RetryCallback<T, E> callback,
					Throwable throwable) {
				System.out.println(">>> close : " + context.getRetryCount() + ", client = " + context.getAttribute("currentClient"));
			}

			@Override
			public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback,
					Throwable throwable) {
				System.out.println(">>> onError : " + context.getRetryCount() + ", client = " + context.getAttribute("currentClient"));
			}
			
		};
		
		RetryTemplate retryTemplate = new RetryTemplate();
		SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy(3, Collections.<Class<? extends Throwable>, Boolean> singletonMap(TransportException.class, true), true);
		retryTemplate.setRetryPolicy(retryPolicy);
		retryTemplate.setListeners(new RetryListener[] {retryListener});
		
		try {
			Map<String,List<String>> services = retryTemplate.execute(new RetryCallback<Map<String,List<String>>,RuntimeException>(){
				@Override
				public Map<String,List<String>> doWithRetry(RetryContext context) throws RuntimeException {
					System.out.println(">>> retry " + context.getRetryCount() + " times, " + DateTimeUtils.formatNow());
					context.setAttribute("currentClient", client);
					return client.getCatalogServices(QueryParams.DEFAULT).getValue();
				}
			}, new RecoveryCallback<Map<String,List<String>>>() {
				@Override
				public Map<String,List<String>> recover(RetryContext context) throws Exception {
					System.out.println("here1");
					throw (Exception)context.getLastThrowable();
				}
			});
			System.out.println(services);
		} catch (Exception e) {
			System.out.println("here2");
			e.printStackTrace();
		}
	}

}
