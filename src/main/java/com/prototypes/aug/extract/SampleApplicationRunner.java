package com.prototypes.aug.extract;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.prototypes.aug.extract.services.JdbcService;
import com.prototypes.aug.extract.services.JobLauncherService;

/**
 * SampleApplicationRunner is an example of marking a bean that should run when it is contained within a SpringApplication. 
 * 
 * Multiple ApplicationRunner beans can be defined within the same application context and can be ordered using the Ordered interface 
 * or @Order annotation.
 * 
 * Note: that is possible to easily get a handle to command line arguments. 
 */
@Component
public class SampleApplicationRunner implements ApplicationRunner  {
	
	@Autowired
	JobLauncherService jobLauncherService;
	
	@Autowired
	JdbcService jdbcService;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		// TODO Auto-generated method stub
		
		System.out.println("Command-line arguments: {}" +  Arrays.toString(args.getSourceArgs()));
		System.out.println("Non Option Args: {}" +  args.getNonOptionArgs());
		System.out.println("Option Names: {}"+  args.getOptionNames());
 
        for (String name : args.getOptionNames()){
        		System.out.println("arg-" + name + "=" + args.getOptionValues(name));
        }
        
        System.out.println("SampleApplicationRunner data..."); 
        System.out.println("strings=" + args);
		
		System.out.println("Try a command like: > curl --data 'name=foo5' localhost:8080/dogjob ");
	}

}
