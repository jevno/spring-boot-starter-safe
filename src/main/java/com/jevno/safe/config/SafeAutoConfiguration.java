package com.jevno.safe.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.jevno.safe.properties.SafeProperties;

@Configuration
@EnableConfigurationProperties(SafeProperties.class)
public class SafeAutoConfiguration {
	@Autowired SafeProperties safeProperties;
}
