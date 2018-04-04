package com.jevno.safe.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.jevno.safe.request.RespEntity;

/**
 * @title 		异常处理器
 * @description	
 * @usage		
 * @copyright	Copyright 2017  marsmob Corporation. All rights reserved.
 * @company		marsmob
 * @author		jevno
 * @create		2017年7月20日 上午10:07:49
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
	private static final Logger _logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	/**
	 * 参数格式错误处理器
	 * @param e
	 * @return
	 */
	@ExceptionHandler(ArgumentException.class)
	public RespEntity handleArgumentException(ArgumentException e){
		_logger.info("参数认证失败！msg:{}",e.getRespEntity().getMsg());
		return e.getRespEntity();
	}
	
	/**
	 * 解密错误处理器
	 * @param e
	 * @return
	 */
	@ExceptionHandler(DecryptException.class)
	public RespEntity handleDecryptException(DecryptException e){
		_logger.info("解密失败！msg:{}",e.getRespEntity().getMsg());
		return e.getRespEntity();
	}
	
	/**
	 * 加密错误处理器
	 * @param e
	 * @return
	 */
	@ExceptionHandler(EncryptException.class)
	public RespEntity handleEncryptException(EncryptException e){
		_logger.info("加密失败！msg:{}",e.getRespEntity().getMsg());
		return e.getRespEntity();
	}
	
	/**
	 * json错误处理器
	 * @param e
	 * @return
	 */
	@ExceptionHandler(JsonException.class)
	public RespEntity handleJsonException(JsonException e){
		_logger.info("json字符串转换对象失败！msg:{}",e.getRespEntity().getMsg());
		return e.getRespEntity();
	}
}
