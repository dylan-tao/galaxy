package org.javaosc.framework.assist;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.javaosc.framework.context.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class AsyncThreadPool {
	
	private static final Logger log = LoggerFactory.getLogger(AsyncThreadPool.class);
	
	private final Object poolSizeMonitor = new Object();

	private int corePoolSize;

	private int maxPoolSize;
	
	private int queueCapacity;

	private int keepAliveSeconds;
	
	private int rejectedHandler;

	private boolean allowCoreThreadTimeOut;

	private ThreadPoolExecutor threadPoolExecutor;
	

	public AsyncThreadPool() {
		synchronized (this.poolSizeMonitor) {
			this.corePoolSize = Configuration.getValue("thread.executor.pool.coresize", 1);
			this.maxPoolSize = Configuration.getValue("thread.executor.pool.maxsize", 2147483647);
			this.queueCapacity = Configuration.getValue("thread.executor.queue.capacity", 2147483647);
			this.keepAliveSeconds = Configuration.getValue("thread.executor.keepalive.second", 60);
			this.rejectedHandler = Configuration.getValue("thread.executor.rejected.handler", 3);
			this.allowCoreThreadTimeOut = Configuration.getValue("thread.executor.allow.timeout", false);
			
			BlockingQueue<Runnable> queue = createQueue(this.queueCapacity);
			ThreadPoolExecutor executor = new ThreadPoolExecutor(this.corePoolSize, this.maxPoolSize, this.keepAliveSeconds, TimeUnit.SECONDS, queue);
			
			if(this.rejectedHandler==1){
				executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
			}else if(this.rejectedHandler==2){
				executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
			}else if(this.rejectedHandler==3){
				executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
			}else if(this.rejectedHandler==4){
				executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());
			}
			
			if (this.allowCoreThreadTimeOut) {
				executor.allowCoreThreadTimeOut(true);
			}
			this.threadPoolExecutor = executor;
		}
	}

	public void execute(Runnable task) {
		Executor executor = getThreadPoolExecutor();
		try {
			executor.execute(task);
		} catch (RejectedExecutionException ex) {
			log.error("Executor [" + executor + "] did not accept task: " + task, ex);
		}
	}

	public Future<?> submit(Runnable task) {
		ExecutorService executor = getThreadPoolExecutor();
		try {
			return executor.submit(task);
		} catch (RejectedExecutionException ex) {
			log.error("Executor [" + executor + "] did not accept task: " + task, ex);
			return null;
		}
	}

	public <T> Future<T> submit(Callable<T> task) {
		ExecutorService executor = getThreadPoolExecutor();
		try {
			return executor.submit(task);
		} catch (RejectedExecutionException ex) {
			log.error("Executor [" + executor + "] did not accept task: " + task, ex);
			return null;
		}
	}
	
	public ThreadPoolExecutor getThreadPoolExecutor(){
		return this.threadPoolExecutor;
	}
	
	public int getCorePoolSize() {
		synchronized (this.poolSizeMonitor) {
			return this.corePoolSize;
		}
	}

	public int getMaxPoolSize() {
		synchronized (this.poolSizeMonitor) {
			return this.maxPoolSize;
		}
	}

	public int getKeepAliveSeconds() {
		synchronized (this.poolSizeMonitor) {
			return this.keepAliveSeconds;
		}
	}

	public int getPoolSize() {
		return getThreadPoolExecutor().getPoolSize();
	}

	public int getActiveCount() {
		return getThreadPoolExecutor().getActiveCount();
	}
	
	public boolean prefersShortLivedTasks() {
		return true;
	}
	
	protected BlockingQueue<Runnable> createQueue(int queueCapacity) {
		if (queueCapacity > 0) {
			return new LinkedBlockingQueue<Runnable>(queueCapacity);
		}
		return new SynchronousQueue<Runnable>();
	}
}
