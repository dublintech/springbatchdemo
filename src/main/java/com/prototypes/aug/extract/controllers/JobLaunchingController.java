package com.prototypes.aug.extract.controllers;

import java.util.Arrays;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ManagedResource
public class JobLaunchingController {
	// Commented out as the JobOperator is a more powerful abstraction
	// So not going to use JobLauncher. 
	// If you go for this approach you need to autowire the job bean as well. 
	//@Autowired 
	//private JobLauncher jobLauncher;
	
	@Autowired
	private JobOperator jobOperator;
	
	
	@RequestMapping(value = "/dogjob", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.ACCEPTED)
	public Long launch(@RequestParam("name") String name) throws Exception {
		System.out.println(">>launch" + name);
		JobParameters jobParamaters = 
				new JobParametersBuilder().addString("name", name).toJobParameters();
        // If I was using JobLauncher, code would be 
		// this.jobLauncher.run(job, jobParamaters);
		// However, instead of JobLauncher, we use JobOperator because it is more powerful
		System.out.println("jobNames=" + Arrays.toString(jobOperator.getJobNames().toArray()));
		return this.jobOperator.start("ExtractDogInfo", String.format("name=%s", name));
	}
	
	@RequestMapping(value = "/dogjob/{id}", method=RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void stop(@PathVariable("id") Long id) throws Exception {
		System.out.println(">>launch" + id);
		// Here we use the JobOperator to gracefully shut down. 
		this.jobOperator.stop(id);
	}
	
}
