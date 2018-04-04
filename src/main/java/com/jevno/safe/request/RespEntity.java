package com.jevno.safe.request;

import java.io.Serializable;

import org.springframework.http.HttpStatus;

import com.jevno.safe.utils.Jsons;

/**
 * @title 		返回对象
 * @description	
 * @usage		
 * @copyright	Copyright 2017  marsmob Corporation. All rights reserved.
 * @company		marsmob
 * @author		jevno
 * @param <T>
 * @create		2017年7月10日 下午5:30:53
 */
public class RespEntity implements Serializable{
	
	private static final long serialVersionUID = 8156667515837714381L;
	
	private Boolean status;			//请求结果
	private int		code;			//返回业务状态位
	private String	msg;			//信息
	private Object	data;
	
	public int getCode()
	{
		return code;
	}
	public RespEntity setCode(int code)
	{
		this.code = code;
		return this;
	}
	public String getMsg()
	{
		return msg;
	}
	public RespEntity setMsg(String msg)
	{
		this.msg = msg;
		return this;
	}
	public Object getData()
	{
		return data==null ? "" : data;
	}
	public RespEntity setData(Object data)
	{
		this.data = data;
		return this;
	}
	public Boolean getStatus()
	{
		return status;
	}
	public RespEntity setStatus(Boolean status)
	{
		this.status = status;
		return this;
	}
	
	public static RespEntity SUCCESS()
	{
		return new RespEntity()
				.setStatus(Boolean.TRUE)
				.setCode(HttpStatus.OK.value())
				.setMsg("success!");
	}
	
	public static RespEntity ERROR(DescribableEnum describableEnum)
	{
		return new RespEntity().setStatus(Boolean.FALSE).setCode(describableEnum.key()).setMsg(describableEnum.desc());
	}
	
	public static RespEntity ERROR()
	{
		return new RespEntity().setStatus(Boolean.FALSE);
	}
	
	@Override
	public String toString()
	{
		return Jsons.of(this).toJson();
	}
	
}
