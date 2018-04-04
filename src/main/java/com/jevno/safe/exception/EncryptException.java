package com.jevno.safe.exception;

import com.jevno.safe.request.RespEntity;

/**
 * @title 		SecurityUtil加密异常
 * @description	
 * @usage		
 * @copyright	Copyright 2017  marsmob Corporation. All rights reserved.
 * @company		marsmob
 * @author		jevno
 * @create		2017年7月20日 下午1:43:19
 */
public class EncryptException extends RuntimeException{
	private static final long serialVersionUID = -9212613671668541398L;

	private RespEntity respEntity;

	public RespEntity getRespEntity() {
		return respEntity;
	}

	public EncryptException setRespEntity(RespEntity respEntity) {
		this.respEntity = respEntity;
		return this;
	}
}
