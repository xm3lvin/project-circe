package com.kjt.circe.config;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.io.Resource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import jpos.util.JposPropertiesConst;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableAsync
@Slf4j
public class ApplicationConfiguration {
	
	@Value("classpath:jpos.xml")
	private Resource jposConfig;
	
	@PostConstruct
	private void init() {
		try {
			log.info("Loading JavaPOS configuration [{}] ...", jposConfig.getFile().getAbsolutePath());
			System.setProperty(JposPropertiesConst.JPOS_POPULATOR_FILE_PROP_NAME, jposConfig.getFile().getAbsolutePath());
		} catch (IOException e) {
			throw new RuntimeException("Failed to load JavaPOS configuration", e);
		}
	}
	
	@Bean(name = "applicationEventMulticaster")
    public ApplicationEventMulticaster simpleApplicationEventMulticaster() {
		log.info("Loading ApplicationEventMulticaster ...");
        SimpleApplicationEventMulticaster eventMulticaster = new SimpleApplicationEventMulticaster();
        eventMulticaster.setTaskExecutor(new SimpleAsyncTaskExecutor());
        return eventMulticaster;
    }
	
	@SuppressWarnings("rawtypes")
	@Bean
	public RestTemplate restTemplate() {
		log.info("Loading RestTemplate ...");
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setErrorHandler(new DefaultResponseErrorHandler());
		restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		restTemplate.getMessageConverters().add(new SourceHttpMessageConverter());
		return restTemplate;
	}

}
