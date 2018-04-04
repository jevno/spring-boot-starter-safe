package com.jevno.safe.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

/**
 * @title 		gson 操作json工具类
 * @description	
 * @usage		
 * @copyright	Copyright 2017  marsmob Corporation. All rights reserved.
 * @company		marsmob
 * @author		jevno
 * @create		2017年7月28日 上午11:33:22
 */
public class Jsons {

	private Object root;
	private Boolean serializeNull = Boolean.FALSE;
	private Boolean toUnicode = Boolean.TRUE;
	private Boolean prettyPrint = Boolean.FALSE;
	private static Map<Class<?>, Object> adapters = Maps.newHashMap();
	private static List<TypeAdapterFactory> factories = Lists.newArrayList();
	
	/**
	 * 转换成为json时，表示不会将中文等符号转化成unicode 编码的转义字符
	 * 
	 * @return
	 */
	public Jsons disableUnicode() {
		toUnicode = Boolean.FALSE;
		return this;
	}

	/**
	 * 转化时是否序列化null 或者 empty str
	 * 
	 * @return
	 */
	public Jsons serializeNull() {
		serializeNull = Boolean.TRUE;
		return this;
	}

	/**
	 * 按照方便阅读的格式输出
	 * 
	 * @return
	 */
	public Jsons prettyPrint() {
		prettyPrint = Boolean.TRUE;
		return this;
	}

	public <T> T to(Class<T> clazz) {
		return getGson().fromJson(toJson(), clazz);
	}

	public <T> T to(TypeToken<T> typeToken) {
		return getGson().fromJson(toJson(), typeToken.getType());
	}

	/**
	 * 判断是否有这个属性
	 * 
	 * @param propName
	 * @return
	 */
	public Boolean has(String propName) {
		if(this.root == null){
			return false;
		}
		JsonElement ele = this.getJson();
		if(ele == null || !ele.isJsonObject()){
			return false;
		}
		return ele.getAsJsonObject().has(propName);
	}
	
	public JsonElement getJson(){
		if(this.root == null){
			return null;
		}
		if (this.root instanceof String) {
			// 传入的字符串
			return this.getGson().fromJson(this.root.toString(), JsonElement.class);
		}
		JsonElement ele = this.getGson().toJsonTree(this.root);
		return ele;
	}

	public static Jsons of(Object obj) {
		Jsons me = new Jsons();
		/*if (obj == null) {
			me.root = null;
			return me;
		}
		if (obj instanceof String) {
			// 传入的字符串
			me.root = me.getGson().fromJson(obj.toString(), JsonElement.class);
			return me;
		}

		if (obj instanceof JsonElement) {
			me.root = (JsonElement) obj;
			return me;
		}

		if (obj instanceof JsonObject) {
			me.root = (JsonObject) obj;
			return me;
		}

		if (obj instanceof Jsons) {
			me.root = ((Jsons) obj).getRoot();
			return me;
		}

		me.root = me.getGson().toJsonTree(obj);*/
		me.root = obj;

		return me;
	}

	/**
	 * 返回一个空的Jsons对象
	 * 
	 * @return
	 */
	public static Jsons empty() {
		return Jsons.of("{}");
	}

	/**
	 * 获取Gson对象
	 * 
	 * @return
	 */
	private Gson getGson() {

		GsonBuilder builder = new GsonBuilder();
		builder = serializeNull ? builder.serializeNulls() : builder;
		builder = toUnicode ? builder : builder.disableHtmlEscaping();
		builder = prettyPrint ? builder.setPrettyPrinting() : builder;

		// 追加外部Adapter
		if (adapters != null && !adapters.isEmpty()) {

			Set<Entry<Class<?>, Object>> sets = adapters.entrySet();
			for (Entry<Class<?>, Object> entry : sets) {
				builder.registerTypeHierarchyAdapter(entry.getKey(), entry.getValue());
			}
		}
		// 追加外部factory
		if (factories != null && !factories.isEmpty()) {
			for (TypeAdapterFactory f : factories) {
				builder.registerTypeAdapterFactory(f);
			}
		}

		return builder.create();
	}

	public String toJson() {
		return getGson().toJson(getJson());
	}

	public String toPrettyJson() {
		Jsons me = new Jsons();
		me.serializeNull = this.serializeNull;
		me.toUnicode = this.toUnicode;
		me.root = root;
		return me.prettyPrint().toJson();
	}

	public Jsons printPrettyJson() {
		return this;
	}

	public Jsons get(String... nodeName) {
		JsonElement ele = this.getJson();
		if (ele == null)
			return new Jsons();
		for (String node : nodeName) {
			if (ele.isJsonObject()) {
				if (ele.getAsJsonObject().has(node)) {
					// 拥有属性
					ele = ele.getAsJsonObject().get(node);
				} else {
					// 没有这个属性,返回空属性
					return Jsons.of(null);
				}
			} else if (ele.isJsonNull()) {
				// 空则返回空属性
				return Jsons.of(null);
			} else if (ele.isJsonPrimitive()) {
				// 基本类型则抛出错误
				throw new IllegalArgumentException("property [" + node + "] is primitive");
			} else if (ele.isJsonArray()) {
				// 数组类型则抛出错误
				throw new IllegalArgumentException("property [" + node + "] is array");
			}
		}
		Jsons ret = new Jsons();
		ret.root = ele;
		return ret;
	}

	public Jsons get(int i) {
		JsonElement ele = this.getJson();
		if (ele.isJsonArray()) {
			Jsons ret = new Jsons();
			ret.root = ele.getAsJsonArray().get(i);
			return ret;
		} else {
			// 不是数组类型则抛出错误
			throw new IllegalArgumentException("Is not a array");
		}
	}

	public String asString() {
		return asString(null);
	}

	public String asString(String defaultValue) {
		JsonElement ele = this.getJson();
		return ele == null || ele.isJsonNull() ? defaultValue : ele.getAsString();
	}

	public BigDecimal asBigDecimal() {
		return asBigDecimal(null);
	}

	public BigDecimal asBigDecimal(BigDecimal defaultValue) {
		JsonElement ele = this.getJson();
		return ele == null || ele.isJsonNull() ? defaultValue : ele.getAsBigDecimal();
	}

	public BigInteger asBigInteger() {
		return asBigInteger(null);
	}

	public BigInteger asBigInteger(BigInteger defaultValue) {
		JsonElement ele = this.getJson();
		return ele == null || ele.isJsonNull() ? defaultValue : ele.getAsBigInteger();
	}

	public Boolean asBoolean() {
		return asBoolean(null);
	}

	public Boolean asBoolean(Boolean defaultValue) {
		JsonElement ele = this.getJson();
		return ele == null || ele.isJsonNull() ? defaultValue : ele.getAsBoolean();
	}

	public Byte asByte() {
		return asByte(null);
	}

	public Byte asByte(Byte defaultValue) {
		JsonElement ele = this.getJson();
		return ele == null || ele.isJsonNull() ? defaultValue : ele.getAsByte();
	}

	public Character asCharacter() {
		return asCharacter(null);
	}

	public Character asCharacter(Character defaultValue) {
		JsonElement ele = this.getJson();
		return ele == null || ele.isJsonNull() ? defaultValue : ele.getAsCharacter();
	}

	public Double asDouble() {
		return asDouble(null);
	}

	public Double asDouble(Double defaultValue) {
		JsonElement ele = this.getJson();
		return ele == null || ele.isJsonNull() ? defaultValue : ele.getAsDouble();
	}

	public Float asFloat() {
		return asFloat(null);
	}

	public Float asFloat(Float defaultValue) {
		JsonElement ele = this.getJson();
		return ele == null || ele.isJsonNull() ? defaultValue : ele.getAsFloat();
	}

	public Integer asInt() {
		return asInt(null);
	}

	public Integer asInt(Integer defaultValue) {
		JsonElement ele = this.getJson();
		return ele == null || ele.isJsonNull() ? defaultValue : ele.getAsInt();
	}

	public Long asLong() {
		return asLong(null);
	}

	public Long asLong(Long defaultValue) {
		JsonElement ele = this.getJson();
		return ele == null || ele.isJsonNull() ? defaultValue : ele.getAsLong();
	}

	public Number asNumber() {
		return asNumber(null);
	}

	public Number asNumber(Number defaultValue) {
		JsonElement ele = this.getJson();
		return ele == null || ele.isJsonNull() ? defaultValue : ele.getAsNumber();
	}

	public List<Jsons> asList() {
		JsonElement ele = this.getJson();
		List<Jsons> list = Lists.newArrayList();
		if (ele != null && ele.isJsonArray()) {
			ele.getAsJsonArray().forEach(e -> {
				Jsons j = new Jsons();
				j.root = e;
				list.add(j);
			});
		}
		return list;
	}

	public JsonElement getRoot() {
		JsonElement ele = this.getJson();
		return ele;
	}

	public Boolean isNull() {
		JsonElement ele = this.getJson();
		if (ele == null) {
			return Boolean.TRUE;
		} else if (ele.isJsonNull()) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	public Boolean isArray() {
		JsonElement ele = this.getJson();
		if (isNull()) {
			return Boolean.FALSE;
		} else if (ele.isJsonArray()) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	public Boolean isObject() {
		JsonElement ele = this.getJson();
		if (isNull()) {
			return Boolean.FALSE;
		} else if (ele.isJsonObject()) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	@Override
	public String toString() {
		return this.toPrettyJson();
	}

	/**
	 * 添加值
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Jsons put(String key, Object value) {
		JsonElement ele = this.getJson();
		if (value == null) {
			return this;
//		} else if (value instanceof Jsons) {
//			ele.getAsJsonObject().add(key, ((Jsons) value).getRoot());
		} else if (value instanceof String) {
			ele.getAsJsonObject().addProperty(key, (String) value);
		} else {
//			put(key, Jsons.of(value));
			ele.getAsJsonObject().add(key, Jsons.of(value).getRoot());
		}
		this.root = ele;
		return this;
	}

	public Jsons remove(String key) {
		JsonElement ele = this.getJson();
		ele.getAsJsonObject().remove(key);
		this.root = ele;
		return this;
	}

	public static void registerAdapter(Class<?> type, Object adapter) {
		if (type != null && adapter != null) {
			adapters.put(type, adapter);
		}
	}

	public static void registerAdapterFactory(TypeAdapterFactory factory) {
		if (factory != null) {
			factories.add(factory);
		}
	}

}
