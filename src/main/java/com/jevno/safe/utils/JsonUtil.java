package com.jevno.safe.utils;

import java.util.List;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jevno.safe.exception.ExceptionCodes;
import com.jevno.safe.exception.JsonException;
import com.jevno.safe.request.RespEntity;

public class JsonUtil {
	private static final Logger _logger = LoggerFactory.getLogger(JsonUtil.class);
	
	private static final ObjectMapper objectMapper;

	static {
		objectMapper = new ObjectMapper();
		//去掉默认的时间戳格式
		//objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		//设置为中国上海时区
		objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
		//空值不序列化
		//objectMapper.setSerializationInclusion(Include.NON_NULL);
		//反序列化时，属性不存在的兼容处理
		objectMapper.getDeserializationConfig().withoutFeatures(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		//序列化时，日期的统一格式
		//objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		//单引号处理
		objectMapper.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
	}
	
	/**
	 * 将对象转换为json字符串
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static String obj2string(Object obj) {
		if(null == obj) return null;
		try {
			return objectMapper.writeValueAsString(obj);
		} catch (Exception e) {
			_logger.error("obj2string error:[{}]",e.getMessage());
			return null;
		}
	}

	/**
	 * 将字符串转list对象
	 * 
	 * @param <T>
	 * @param jsonStr
	 * @param cls
	 * @return
	 */
	public static <T> List<T> str2list(String jsonStr, Class<T> cls){
		List<T> objList = null;
		try {
			JavaType t = objectMapper.getTypeFactory().constructParametricType(
					List.class, cls);
			objList = objectMapper.readValue(jsonStr, t);
		} catch (Exception e) {
			_logger.error("str2list error:[{}]",e.getMessage());
			RespEntity respEntity = new RespEntity().setStatus(Boolean.FALSE).setCode(ExceptionCodes.INVALID_MODEL_STATUS.key()).setMsg(jsonStr);
			throw new JsonException().setRespEntity(respEntity);
		}
		return objList;
	}

	/**
	 * 将字符串转为对象
	 * 
	 * @param <T>
	 * @param jsonStr
	 * @param cls
	 * @return
	 */
	public static <T> T str2obj(String jsonStr, Class<T> cls){
		T obj = null;
		try {
			obj = objectMapper.readValue(jsonStr, cls);
		} catch (Exception e) {
			_logger.error("str2obj error:[{}]",e.getMessage());
			RespEntity respEntity = new RespEntity().setStatus(Boolean.FALSE).setCode(ExceptionCodes.INVALID_MODEL_STATUS.key()).setMsg(jsonStr);
			throw new JsonException().setRespEntity(respEntity);
		}
		return obj;
	}
}