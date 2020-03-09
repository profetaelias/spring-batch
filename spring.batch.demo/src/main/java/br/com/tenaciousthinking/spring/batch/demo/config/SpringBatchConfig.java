package br.com.tenaciousthinking.spring.batch.demo.config;


import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import br.com.tenaciousthinking.spring.batch.demo.batch.Processor;
import br.com.tenaciousthinking.spring.batch.demo.model.User;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {
	
	
	public Processor processor() {
		return new Processor();
	}

	@Bean
	public Job job(JobBuilderFactory jobBuilderFactory, 
			StepBuilderFactory stepBuilderFactory, 
			ItemReader<User> itemReader, 
			ItemWriter<User> itemWriter) {
		
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setCorePoolSize(4);
		taskExecutor.setMaxPoolSize(4);
		taskExecutor.afterPropertiesSet();
		
		Step step = stepBuilderFactory.get("ETL-file-load")
					.<User, User>chunk(100)
					.reader(itemReader)
					.processor(processor())
					.writer(itemWriter).taskExecutor(taskExecutor)
					.build();
		
		return jobBuilderFactory.get("ETL-LOAD")
			.incrementer(new RunIdIncrementer())
			.start(step)
			.build();		
	}
	
	@Bean
	public FlatFileItemReader<User> fileItemReader(@Value("${input}") Resource resource) {
		FlatFileItemReader<User> flatFileItemReader = new FlatFileItemReader<>();
		flatFileItemReader.setResource(resource);
		flatFileItemReader.setName("CSV-reader");
		flatFileItemReader.setLinesToSkip(1);
		flatFileItemReader.setLineMapper(lineMapper());
		
		return flatFileItemReader;
	}

	private LineMapper<User> lineMapper() {
		DefaultLineMapper<User> defaultLineMapper = new DefaultLineMapper<User>();
		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		
		lineTokenizer.setDelimiter(",");
		lineTokenizer.setStrict(false);
		lineTokenizer.setNames(new String[] {"name", "dept", "salary"});
		
		BeanWrapperFieldSetMapper<User> beanWrapperFieldSetMapper = new BeanWrapperFieldSetMapper<User>();
		beanWrapperFieldSetMapper.setTargetType(User.class);
		
		defaultLineMapper.setLineTokenizer(lineTokenizer);
		defaultLineMapper.setFieldSetMapper(beanWrapperFieldSetMapper);
		
		return defaultLineMapper;
	}
}
