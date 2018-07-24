package com.prototypes.aug.extract;

import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

import com.prototypes.aug.extract.services.JobLauncherService;

/**
 * Dev-note. 
 * The annotation @springBootApplication gives us three annotations:
 * @EnableAutoConfiguration: enable Spring Boot’s auto-configuration mechanism
 * @ComponentScan: enable @Component scan on the package where the application is located (see the best practices)
 * and any sub-packages
 * @Configuration: allow to register extra beans in the context or import additional configuration classes
 * 
 * See: https://docs.spring.io/spring-boot/docs/current/reference/html/using-boot-using-springbootapplication-annotation.html
 * 
 * SpringBoot by default will by default launch any jobs it finds in the ApplicatinContext.
 * If you don't want this behaviour configure, spring.batch.job.enabled=false in Application.properties
 */
@SpringBootApplication
@ComponentScan(basePackages="com.prototypes.aug.extract")
@EnableBatchProcessing
public class ExtractApplication {

	public static void main(String[] args) {
		System.out.println(">>main() args=" + args);
		SpringApplication.run(ExtractApplication.class, args);
		// To do get a spring exit working to return exit code
		// System.exit(SpringApplication.exit(SpringApplication.run(ExtractApplication.class, args)));
	}
	
	// Dev - note
	// When SpringApplication.exit() is called, this will be called to return an exit code,
	// see: https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-spring-application.html
	// todo get this working
	@Bean
	public ExitCodeGenerator exitCodeGenerator() {
		return () -> 42;
	}
}


