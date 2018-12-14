package com.penglecode.xmodule.common.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import com.penglecode.xmodule.common.support.NamedThreadFactory;

/**
 * 任务迟延执行工具类
 * 
 * @author 	pengpeng
 * @date	2018年7月6日 上午10:19:59
 */
public class TaskLazyExecuteUtils {

	private static final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors() * 4, new NamedThreadFactory("TaskLazyExecutor-"));
	
	public static void asyncLazyExecute(Runnable task, int delay, TimeUnit unit) {
		scheduledExecutorService.schedule(task, delay, unit);
	}
	
	public static void syncLazyExecute(Runnable task, int delay, TimeUnit unit) {
		scheduledExecutorService.schedule(task, 0, unit);
		LockSupport.parkNanos(unit.toNanos(delay));
	}
	
}
