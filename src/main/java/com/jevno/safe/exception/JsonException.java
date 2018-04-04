package com.jevno.safe.exception;

import com.jevno.safe.request.RespEntity;

/**
 * @title 		json转换异常
 * @description	
 * @usage		
 * @copyright	Copyright 2017  marsmob Corporation. All rights reserved.
 * @company		marsmob
 * @author		jevno
 * @create		2017年7月20日 下午2:32:36
 */
public class JsonException extends RuntimeException{

	private static final long serialVersionUID = 3349889034989987834L;

	private RespEntity respEntity;

	public RespEntity getRespEntity() {
		return respEntity;
	}

	public JsonException setRespEntity(RespEntity respEntity) {
		this.respEntity = respEntity;
		return this;
	}
}
