package com.prototypes.aug.extract.listeners;

import org.springframework.batch.core.annotation.AfterChunk;
import org.springframework.batch.core.annotation.BeforeChunk;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.JobContext;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

/**
 * You dont need to implement the JobExecutionListener when using the @BeforeChunk and @afterChunk annotations
 */
public class JobLoggingListener implements JobExecutionListener {
	@BeforeChunk
	public void beforeJob(JobExecution jobExecution) {
		System.out.println(jobExecution.getJobInstance().getJobName() + " is about to start");
	}
	
	@AfterChunk
	public void afterJob(JobExecution jobExecution) {
		System.out.println(jobExecution.getJobInstance().getJobName() + " has finished");
	}

}
