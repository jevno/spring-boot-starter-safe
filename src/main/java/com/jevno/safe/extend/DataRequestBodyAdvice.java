package com.jevno.safe.extend;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import com.google.common.collect.Maps;
import com.jevno.safe.properties.SafeProperties;
import com.jevno.safe.utils.Jsons;
import com.jevno.safe.utils.SecurityUtil;
import com.jevno.safe.utils.TemplateParser;

/**
 * @title 		请求统一解密
 * @description	
 * @usage		
 * @copyright	Copyright 2017  marsmob Corporation. All rights reserved.
 * @company		marsmob
 * @author		jevno
 * @create		2018年1月4日下午5:48:35
 */
@Component
@ControllerAdvice
public class DataRequestBodyAdvice implements RequestBodyAdvice {
	private static final Logger _logger = LoggerFactory.getLogger(DataRequestBodyAdvice.class);
	@Autowired SafeProperties safeProperties;
	
	@Override
	public boolean supports(MethodParameter methodParameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) {
		if(!safeProperties.getEnableSafe())
		{
			return Boolean.FALSE;
		}
		
		//优先级 方法---类
		if(!(methodParameter.getMethod().isAnnotationPresent(PostMapping.class)
				|| methodParameter.getMethod().isAnnotationPresent(GetMapping.class)
				|| methodParameter.getMethod().isAnnotationPresent(RequestMapping.class)))
		{
			return Boolean.FALSE;
		}
		
		/**
		 * 方法级别
		 */
		if(methodParameter.getMethod().isAnnotationPresent(RequestDecryptBody.class))
		{
			return methodParameter.getMethodAnnotation(RequestDecryptBody.class).decrypt();
		}
		
		/**
		 * 类级别
		 */
		if(methodParameter.getContainingClass().isAnnotationPresent(RequestDecryptBody.class))
		{
			return methodParameter.getContainingClass().getAnnotation(RequestDecryptBody.class).decrypt();
		}
		
		return Boolean.FALSE;
	}

	@Override
	public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter,
			Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
		return body;
	}

	@Override
	public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
		InputStream is = inputMessage.getBody();
		ByteArrayOutputStream baos   = new ByteArrayOutputStream();
		int i=-1;
		while((i=is.read())!=-1){ 
			baos.write(i); 
		}
		String data = baos.toString();
		Jsons jsons = Jsons.of(data);
		Map<String,Object> requestMap = Maps.newHashMap();
		if(safeProperties.getEnableLog())
		{
			requestMap.put("method", parameter.getMethod().getDeclaringClass().getName()+"."+parameter.getMethod().getName());
			requestMap.put("paramType", targetType.getTypeName());
			requestMap.put("param", jsons.toJson());
		}
		if(jsons.has("data")) {
			data = Jsons.of(data).get("data").asString();
			if(StringUtils.isNotBlank(data))
			{
				data = SecurityUtil.decryptBASE64(data);
				if(safeProperties.getEnableLog())
				{
					requestMap.put("data", Jsons.of(data).toJson());
					_logger.info("{}", TemplateParser.parse("\n请求方法:${method},\n接收参数类型:${paramType},\n请求参数:${param},\n请求参数data解密:${data}", requestMap));
				}
			}
			return new DecryptHttpInputMessage(inputMessage.getHeaders(), new ByteArrayInputStream(data.getBytes("UTF-8")));
		}
		
		if(safeProperties.getEnableLog())
		{
			_logger.info("{}", TemplateParser.parse("\n请求方法:${method},\n接收参数类型:${paramType},\n请求参数:${param}", requestMap));
		}
		return inputMessage;
	}

	@Override
	public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) {
		return body;
	}
	
}
