package com.prototypes.aug.extract.services;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Service;

/**
 * This class just demostrates launch via a service. 
 */
@Service
@ManagedResource
public class JobLauncherService {
	
	@Autowired
	private JobLauncher jobLauncher;
	
	@Autowired
	private JdbcService JdbcService;
	
	@Autowired
	private Job job;
	
	public void launchJob() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		System.out.println(">>launchJob()");
		JobExecution jobExecution=jobLauncher.run(job, getJobParameters());
		System.out.println(jobExecution.getStatus());
	}
	
	public JobParameters getJobParameters() {
	    JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
	    jobParametersBuilder.addLong("time", System.currentTimeMillis());
	    jobParametersBuilder.addDate("date", new Date());
	    JobParameters jobParams = jobParametersBuilder.toJobParameters();
	    System.out.println("<<getJobParameters(), jobParams=" + jobParams);
	    
	    return jobParams;
	}

}
