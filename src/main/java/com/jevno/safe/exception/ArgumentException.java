package com.jevno.safe.exception;

import com.jevno.safe.request.RespEntity;

/**
 * @title 		用于Hibernate Validator的校验异常
 * @description	
 * @usage		
 * @copyright	Copyright 2017  marsmob Corporation. All rights reserved.
 * @company		marsmob
 * @author		jevno
 * @create		2017年7月20日 上午10:02:09
 */
public class ArgumentException extends RuntimeException{

	private static final long serialVersionUID = 3349889034989987834L;

	private RespEntity respEntity;

	public RespEntity getRespEntity() {
		return respEntity;
	}

	public ArgumentException setRespEntity(RespEntity respEntity) {
		this.respEntity = respEntity;
		return this;
	}
}
