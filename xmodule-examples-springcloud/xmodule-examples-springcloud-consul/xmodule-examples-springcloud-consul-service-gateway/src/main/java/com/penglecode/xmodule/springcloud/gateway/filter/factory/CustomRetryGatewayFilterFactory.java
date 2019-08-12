package com.penglecode.xmodule.springcloud.gateway.filter.factory;

import java.util.function.Predicate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.RetryGatewayFilterFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;

import com.penglecode.xmodule.common.util.CollectionUtils;

import reactor.retry.Repeat;
import reactor.retry.RepeatContext;
import reactor.retry.Retry;
import reactor.retry.RetryContext;

/**
 * 自定义的RetryGatewayFilterFactory
 * 修改了处理statusCode与statusSeries的逻辑
 * 
 * 原来的逻辑无法满足当重试配置为series(SERVER_ERROR), statuses(BAD_GATEWAY,GATEWAY_TIMEOUT,SERVICE_UNAVAILABLE)时排除500(SERVER_INTERNAL_ERROR)的重试需求
 * 
 * @author 	pengpeng
 * @date	2019年7月16日 上午10:30:42
 */
public class CustomRetryGatewayFilterFactory extends RetryGatewayFilterFactory {

	protected static final Log log = LogFactory.getLog(RetryGatewayFilterFactory.class);
	
	@Override
	public GatewayFilter apply(RetryConfig retryConfig) {
		retryConfig.validate();

		Repeat<ServerWebExchange> statusCodeRepeat = null;
		if (!retryConfig.getStatuses().isEmpty() || !retryConfig.getSeries().isEmpty()) {
			Predicate<RepeatContext<ServerWebExchange>> repeatPredicate = context -> {
				ServerWebExchange exchange = context.applicationContext();
				if (exceedsMaxIterations(exchange, retryConfig)) {
					return false;
				}

				HttpStatus statusCode = exchange.getResponse().getStatusCode();

				boolean retryableStatusCode = retryConfig.getStatuses().contains(statusCode);
				
				//修改处理statusCode与statusSeries的逻辑
				if(retryableStatusCode && !CollectionUtils.isEmpty(retryConfig.getSeries())) {
					// try the series
					retryableStatusCode = retryConfig.getSeries().stream()
							.anyMatch(series -> statusCode.series().equals(series));
				}
				/*if (!retryableStatusCode && statusCode != null) { // null status code might mean a network exception?
					// try the series
					retryableStatusCode = retryConfig.getSeries().stream()
							.anyMatch(series -> statusCode.series().equals(series));
				}*/

				trace("retryableStatusCode: %b, statusCode %s, configured statuses %s, configured series %s",
						retryableStatusCode, statusCode, retryConfig.getStatuses(), retryConfig.getSeries());

				HttpMethod httpMethod = exchange.getRequest().getMethod();
				boolean retryableMethod = retryConfig.getMethods().contains(httpMethod);

				trace("retryableMethod: %b, httpMethod %s, configured methods %s",
						retryableMethod, httpMethod, retryConfig.getMethods());
				return retryableMethod && retryableStatusCode;
			};

			statusCodeRepeat = Repeat.onlyIf(repeatPredicate)
					.doOnRepeat(context -> reset(context.applicationContext()));
		}

		//TODO: support timeout, backoff, jitter, etc... in Builder

		Retry<ServerWebExchange> exceptionRetry = null;
		if (!retryConfig.getExceptions().isEmpty()) {
			Predicate<RetryContext<ServerWebExchange>> retryContextPredicate = context -> {
				if (exceedsMaxIterations(context.applicationContext(), retryConfig)) {
					return false;
				}

				for (Class<? extends Throwable> clazz : retryConfig.getExceptions()) {
					if (clazz.isInstance(context.exception())) {
						trace("exception is retryable %s, configured exceptions",
								context.exception().getClass().getName(), retryConfig.getExceptions());
						return true;
					}
				}
				trace("exception is not retryable %s, configured exceptions",
						context.exception().getClass().getName(), retryConfig.getExceptions());
				return false;
			};
			exceptionRetry = Retry.onlyIf(retryContextPredicate)
					.doOnRetry(context -> reset(context.applicationContext()))
					.retryMax(retryConfig.getRetries());
		}


		return apply(statusCodeRepeat, exceptionRetry);
	}
	
	protected void trace(String message, Object... args) {
		if (log.isTraceEnabled()) {
			log.trace(String.format(message, args));
		}
	}
	
}
