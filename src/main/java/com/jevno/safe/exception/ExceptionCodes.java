package com.jevno.safe.exception;

import com.jevno.safe.request.DescribableEnum;

/**
 * @title 		参数错误码表
 * @description	
 * @usage		
 * @copyright	Copyright 2017  marsmob Corporation. All rights reserved.
 * @company		marsmob
 * @author		jevno
 * @create		2017年7月18日 下午6:50:59
 */
public enum ExceptionCodes implements DescribableEnum {
	SRR_TO_JSON_ERROR(9001, "字符串转换为json失败"),
	OBJECT_TO_JSONSTR_ERROR(9002, "转换json字符串失败"),
	DECRYPT_ERROR(9003, "解密错误"),
	DECRYPT_NULL_ERROR(9004, "解密参数为空"),
	ENCRYPT_ERROR(9005, "加密错误"),
	ENCRYPT_NULL_ERROR(9006, "加密参数为空"),
	;
	private int key;
	private String desc;

	private ExceptionCodes(int key, String desc) {
		this.key = key;
		this.desc = desc;
	}

	@Override
	public int key() {
		return key;
	}

	@Override
	public String desc() {
		return desc;
	}

}
