package com.jevno.safe.extend;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.google.common.collect.Maps;
import com.jevno.safe.properties.SafeProperties;
import com.jevno.safe.request.RespEntity;
import com.jevno.safe.utils.JsonUtil;
import com.jevno.safe.utils.Jsons;
import com.jevno.safe.utils.SecurityUtil;
import com.jevno.safe.utils.TemplateParser;

/**
 * @title 		返回统一加密
 * @description	
 * @usage		
 * @copyright	Copyright 2017  marsmob Corporation. All rights reserved.
 * @company		marsmob
 * @author		jevno
 * @create		2018年1月4日下午5:50:08
 */
@Component
@ControllerAdvice
public class RespEntityResponseBodyAdvice implements ResponseBodyAdvice<Object> {
	private static final Logger _logger = LoggerFactory.getLogger(RespEntityResponseBodyAdvice.class);
	@Autowired SafeProperties safeProperties;
	
	@Override
	public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> converterType)
	{
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
		 * 方法
		 */
		if(methodParameter.getMethod().isAnnotationPresent(ResponseEncryptBody.class))
		{
			return methodParameter.getMethodAnnotation(ResponseEncryptBody.class).encrypt();
		}
		
		/**
		 * 类级别
		 */
		if(methodParameter.getContainingClass().isAnnotationPresent(ResponseEncryptBody.class))
		{
			return methodParameter.getContainingClass().getAnnotation(ResponseEncryptBody.class).encrypt();
		}
		
		return Boolean.FALSE;
	}
	
	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
			ServerHttpResponse response)
	{
		Object retObj = body;
		Map<String,Object> responseMap = Maps.newHashMap();
		if(safeProperties.getEnableLog())
		{
			responseMap.put("method", returnType.getMethod().getDeclaringClass().getName()+"."+returnType.getMethod().getName());
		}
		
		if (retObj instanceof RespEntity)
		{
			RespEntity respEntity = (RespEntity) retObj;
			_logger.info("respEntity返回：{}",Jsons.of(respEntity).toPrettyJson());
			if(safeProperties.getEnableLog())
			{
				responseMap.put("result", Jsons.of(respEntity).toJson());
			}
			
			String data = "";
			if(respEntity.getStatus())
			{
				if(null == respEntity.getData())
				{
					data = SecurityUtil.encryptBASE64(Jsons.of(Maps.newHashMap()).toJson());
					
				}else {
					data = SecurityUtil.encryptBASE64(JsonUtil.obj2string(respEntity.getData()));
				}
				
			}else {
				data = SecurityUtil.encryptBASE64(Jsons.of(Maps.newHashMap()).toJson());
			}
			if(safeProperties.getEnableLog())
			{
				responseMap.put("data", data);
				_logger.info("{}",TemplateParser.parse("\n返回方法：${method},\n返回结果:${result},\n返回结果data加密:${data}", responseMap));
			}
			return respEntity.setData(data);
		}
		return retObj;
	}
}
