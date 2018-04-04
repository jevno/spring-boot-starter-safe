package com.jevno.safe.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(SafeProperties.prdfix)
public class SafeProperties {
	final static String prdfix = "spring.safe";
	
	private Boolean enableSafe = Boolean.FALSE;
	
	private Boolean enableLog = Boolean.TRUE;

	public Boolean getEnableSafe()
	{
		return enableSafe;
	}

	public SafeProperties setEnableSafe(Boolean enableSafe)
	{
		this.enableSafe = enableSafe;
		return this;
	}
	
	public Boolean getEnableLog()
	{
		return enableLog;
	}

	public SafeProperties setEnableLog(Boolean enableLog)
	{
		this.enableLog = enableLog;
		return this;
	}
}
