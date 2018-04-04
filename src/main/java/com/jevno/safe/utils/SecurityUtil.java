package com.jevno.safe.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jevno.safe.core.extend.BASE64Encoder;
import com.jevno.safe.exception.DecryptException;
import com.jevno.safe.exception.EncryptException;
import com.jevno.safe.exception.ExceptionCodes;
import com.jevno.safe.request.RespEntity;

/**
 * @title 		数据加密辅助类(默认编码UTF-8)
 * @description	
 * @usage		
 * @copyright	Copyright 2017  marsmob Corporation. All rights reserved.
 * @company		marsmob
 * @author		jevno
 * @create		2017年6月7日 下午3:10:39
 */
public class SecurityUtil {
	private static final Logger _logger = LoggerFactory.getLogger(SecurityUtil.class);
	
	private SecurityUtil() {}

	/**
	 * BASE64解码
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String decryptBASE64(String key) {
		try {
			byte[] data = new BASE64Encoder().decode(key);
			return new String(data);
		} catch (Exception e) {
			_logger.error("解密异常！key:{}",key);
			RespEntity respEntity = new RespEntity().setStatus(Boolean.FALSE)
					.setCode(null == key ? ExceptionCodes.DECRYPT_NULL_ERROR.key() : ExceptionCodes.DECRYPT_ERROR.key())
					.setMsg(key);
			throw new DecryptException().setRespEntity(respEntity);
		}
	}

	/**
	 * BASE64编码
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String encryptBASE64(String data) {
		if(StringUtils.isBlank(data))
			return data;
		try {
			byte[] key = data.getBytes();
			return new BASE64Encoder().encode(key);
		} catch (Exception e) {
			_logger.error("加密异常！data:{}",data);
			RespEntity respEntity = new RespEntity().setStatus(Boolean.FALSE)
					.setCode(null == data ? ExceptionCodes.ENCRYPT_NULL_ERROR.key(): ExceptionCodes.ENCRYPT_ERROR.key())
					.setMsg(data);
			throw new EncryptException().setRespEntity(respEntity);
		}
	}

//	public static void main(String[] args) {
//		String encode = encryptBASE64("{\"type\":\"2\",\"phone\":\"13611652606\"}");
//		System.out.println("编码："+encode);
//		String decode = decryptBASE64(encode);
//		System.out.println("解码："+decode);
//	}
}
