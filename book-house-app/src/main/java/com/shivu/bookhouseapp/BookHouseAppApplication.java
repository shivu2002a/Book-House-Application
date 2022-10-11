package com.shivu.bookhouseapp;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

@SpringBootApplication
public class BookHouseAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookHouseAppApplication.class, args);
	}

	@Value("${datastax.astra.secure-connect-bundle}")
	private String bundlePath;
	
	//Establish connection with Database
	@Bean
	CqlSessionBuilderCustomizer sessionBuilderCustomizer() {
		return builder -> {
			try {
				builder
				.withCloudSecureConnectBundle(new URL(bundlePath));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		};
	}
	
	//favicon 
	@Bean
    public SimpleUrlHandlerMapping customFaviconHandlerMapping() {
        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        mapping.setOrder(Integer.MIN_VALUE);
        mapping.setUrlMap(Collections.singletonMap(
                "/static/images/favicon.ico", faviconRequestHandler()));
        return mapping;
    }

    @Bean
    protected ResourceHttpRequestHandler faviconRequestHandler() {
        ResourceHttpRequestHandler requestHandler
                = new ResourceHttpRequestHandler();
        requestHandler.setLocations(Collections.singletonList(new ClassPathResource("/")));
        return requestHandler;
    }

}
