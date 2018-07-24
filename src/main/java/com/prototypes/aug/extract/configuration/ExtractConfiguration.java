package com.prototypes.aug.extract.configuration;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.prototypes.aug.extract.data.DogMapper;

import org.apache.catalina.Context;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.converter.DefaultJobParametersConverter;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.H2PagingQueryProvider;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import com.prototypes.aug.extract.data.CaseHeaderMapper;
import com.prototypes.aug.extract.listeners.JobLoggingListener;
import com.prototypes.aug.extract.pojos.CaseHeaderPojo;
import com.prototypes.aug.extract.pojos.DogPojo;
import com.prototypes.aug.extract.processor.DogProcessor;
import com.prototypes.aug.extract.processor.SampleProcessor;
import com.prototypes.aug.extract.validator.AcceptedValidator;
import com.prototypes.aug.extract.writers.SampleWriter;

/**
 * dev nots - joblaunchers are provided out of the box. Job operators are not. 
 */
@Configuration
@Import(DatasourceConfiguration.class)
@ImportResource("classpath:mybeans.xml")
public class ExtractConfiguration extends DefaultBatchConfigurer implements ApplicationContextAware {
	
	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Autowired
	public DataSource oltpDataSource;

	// Added below when I made it a web app
	private ApplicationContext applicationContext;
	
	@Autowired
	private JobRegistry jobRegistry;
	
	@Autowired
	private JobLauncher jobLauncher;
    
	@Autowired
	private JobRepository jobRepository;
	
	@Autowired
	private JobExplorer jobExplorer;
	
	@Bean
	@StepScope
	// Dev-Note -> the job parameters will persist to the BATCH_JOB_EXECUTION_PARAMS table. 
	public JdbcPagingItemReader<CaseHeaderPojo> pagingItemReader(@Value("#{jobParameters['message']}") String message, 
			@Value("#{jobParameters['time']}") Long time) throws SQLException {
		System.out.println (">>pagingItemReader(time=" + time + ")");
		JdbcPagingItemReader<CaseHeaderPojo> reader = new JdbcPagingItemReader<>();
		
		reader.setDataSource(this.oltpDataSource);
		reader.setFetchSize(10);
		reader.setRowMapper(new CaseHeaderMapper());

		H2PagingQueryProvider queryProvider = new H2PagingQueryProvider();
		queryProvider.setSelectClause("ch.caseid as CASEHEADERCASEID,  ccd.CREOLECASEDETERMINATIONID as ccdid");
		queryProvider.setFromClause("from caseheader ch inner join creolecasedetermination ccd on ch.caseid = ccd.caseid");
		
		Map<String, Order> sortKeys = new HashMap<>(1);

		sortKeys.put("CASEHEADERCASEID", Order.ASCENDING);
		sortKeys.put("ccdid", Order.ASCENDING);

		queryProvider.setSortKeys(sortKeys);
		reader.setQueryProvider(queryProvider);

		return reader;
	}
	
	
	@Bean
	@StepScope
	// Dev-Note -> the job parameters will persist to the BATCH_JOB_EXECUTION_PARAMS table. 
	public JdbcPagingItemReader<DogPojo> dogItemReader(@Value("#{jobParameters['message']}") String message, 
			@Value("#{jobParameters['time']}") Long time) throws SQLException {
		System.out.println (">>personItemReader(time=" + time + ")");
		JdbcPagingItemReader<DogPojo> reader = new JdbcPagingItemReader<>();
		
		reader.setDataSource(this.oltpDataSource);
		reader.setFetchSize(10);
		reader.setRowMapper(new DogMapper());

		H2PagingQueryProvider queryProvider = new H2PagingQueryProvider();
		queryProvider.setSelectClause("p.name as OWNERNAME,  d.DOGNAME as DOGNAME");
		queryProvider.setFromClause("from person p inner join dog d on p.id = d.ownerid");
		
		Map<String, Order> sortKeys = new HashMap<>(1);

		sortKeys.put("OWNERNAME", Order.ASCENDING);
		sortKeys.put("DOGNAME", Order.ASCENDING);

		queryProvider.setSortKeys(sortKeys);
		reader.setQueryProvider(queryProvider);

		return reader;
	}
	
	@Bean
	public ItemWriter<String> writer() {
		return new SampleWriter();
	}

	@Bean
	public Step step() throws Exception {
		return stepBuilderFactory.get("step")
				.<CaseHeaderPojo, String>chunk(2)
				.faultTolerant()
				//.listener(new ChunkListener())
				.reader(pagingItemReader(null, null))
				.processor(itemProcessor())
				//.writer(flatFileItemWriter())
				.writer(writer())
				.build();
	}
	
	@Bean
	public Step dogStep() throws Exception {
		return stepBuilderFactory.get("dogStep")
				.<DogPojo, String>chunk(2)
				.faultTolerant()
				//.listener(new ChunkListener())
				.reader(dogItemReader(null, null))
				.processor(dogProcessor())
				//.writer(flatFileItemWriter())
				.writer(writer())
				.build();
	}

	@Bean
	public Job job() throws Exception {
		System.out.println(">>job()");
		return jobBuilderFactory.get("ExtractDisplayRules")
				.start(step())
				.listener(new JobLoggingListener())
				.validator(new AcceptedValidator())
				.build();
	}
	
	@Bean
	public Job dogJob() throws Exception {
		System.out.println(">>dogJob()");
		return jobBuilderFactory.get("ExtractDogInfo")
				.start(dogStep())
				.listener(new JobLoggingListener())
				.validator(new AcceptedValidator())
				.build();
	}
	
	@Bean
	public SampleProcessor itemProcessor() { return new SampleProcessor(); }

	@Bean
	public DogProcessor dogProcessor() { return new DogProcessor(); }

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// TODO Auto-generated method stub
		this.applicationContext = applicationContext;
		
      System.out.println("Let's inspect the beans provided by Spring Boot:");

      String[] beanNames = this.applicationContext.getBeanDefinitionNames();
      Arrays.sort(beanNames);
      for (String beanName : beanNames) {
        System.out.println(beanName);
      }
      
      System.out.println("Print out the registered Jobs");
      String[] jobs = this.applicationContext.getBeanNamesForType(Job.class);
      Arrays.sort(jobs);
      for (String job : jobs) {
        System.out.println(job);
      }
      
	}
	
	// Beginning code for job operator
	@Bean
	public JobRegistryBeanPostProcessor jobRegistrar() throws Exception {
		//JobRegistryBeanPostProcessor will register all jobs inthe application context
		JobRegistryBeanPostProcessor registrar = new JobRegistryBeanPostProcessor();
		registrar.setJobRegistry(this.jobRegistry);
		registrar.setBeanFactory(this.applicationContext.getAutowireCapableBeanFactory());	
		registrar.afterPropertiesSet();
		
		return registrar;
	}
	

	@Bean
	public JobOperator jobOperator() throws Exception {
		SimpleJobOperator simpleJobOperator = new SimpleJobOperator();
		// the operator wraps the launcher
		simpleJobOperator.setJobLauncher(this.jobLauncher);
		simpleJobOperator.setJobParametersConverter(new DefaultJobParametersConverter());
		// In order to handle restarting, stopping jobs etc we need access to a job repository
		// to tell what is running in the past, what is currently running
		simpleJobOperator.setJobRepository(this.jobRepository);
		// Used to do some querying. 
		simpleJobOperator.setJobExplorer(this.jobExplorer);
		// a jobreg is a map, is job being the job the name and the value being the job to run. 
		// you need to register your jobs with the job registrry - this is done in jobRegistrar()
		simpleJobOperator.setJobRegistry(this.jobRegistry);
		simpleJobOperator.afterPropertiesSet();
		
		return simpleJobOperator;		
	}
	
	
	/** 
	 * We want to customise the job launch so it launches asynchronously. 
	 * This means it won't block the servlet thread. 
	 * 
	 * JobOperator --> JobLauncher --> JobRepository -> 
	 */
	@Bean 
	public JobLauncher getJobLauncher() {
		// ending code for job operator
		SimpleJobLauncher simpleJobLauncher = null;
		try {
			simpleJobLauncher = new SimpleJobLauncher();
			simpleJobLauncher.setJobRepository(this.jobRepository);
			simpleJobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
			simpleJobLauncher.afterPropertiesSet();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return simpleJobLauncher;
	}

}
