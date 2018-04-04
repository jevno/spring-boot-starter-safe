package com.jevno.safe.utils;

import java.util.Map;
import java.util.Scanner;

import com.google.common.base.Strings;

public class TemplateParser {
	
	public static String parse(String template, String... params) {
		if (Strings.isNullOrEmpty(template)) {
			return "";
		}

		if(params.length == 0) {
			return template;
		}
		Scanner sc = new Scanner(template);
		int i=0;
		while (sc.hasNext()) {
			String lineStr = sc.nextLine();
			Scanner line = new Scanner(lineStr);
			while (line.hasNext()) {
				String paramkey = line.findInLine("\\$[^\\}]+\\}");
				if (!Strings.isNullOrEmpty(paramkey)) {
					if(i+1 <= params.length) {
						template = template.replace(paramkey, params[i]);
						i++;
					}
				} else {
					break;
				}
			}
			line.close();
		}
		sc.close();

		return template;
	}
	
	public static String parse(String template, Map<String,Object> params) {
		if (Strings.isNullOrEmpty(template)) {
			return "";
		}
		
		if(null == params) {
			return template;
		}
		Scanner sc = new Scanner(template);
		while (sc.hasNext()) {
			String lineStr = sc.nextLine();
			Scanner line = new Scanner(lineStr);
			while (line.hasNext()) {
				String key = line.findInLine("\\$[^\\}]+\\}");
				if (!Strings.isNullOrEmpty(key)) {
					String keyName = key.replace("${", "").replace("}","");
					Object val = params.get(keyName.toString());
					if(val != null) {
						template = template.replace(key, val.toString());
					}
				} else {
					break;
				}
			}
			line.close();
		}
		sc.close();

		return template;
	}
	
	
//	public static void main(String[] args) {
//		String tpl = "如希望修改见面时间、见面地点，  请尽快与客服或粉丝联系。见面后务必要扫描粉丝端二维码。" + 
//				"期号：${param1},\n" + 
//				"昵称：${param2},\n" + 
//				"联系电话：${param3},\n" + 
//				"见面城市：${param4}\n";
//		
//		String temp = TemplateParser.parse(tpl,"13452501","YIYUAN_345235","17323287180","四川 成都");
//		System.out.println(temp);
//		
//		Map<String,Object> params = Maps.newHashMap();
//		params.put("param1", "13452501");
//		params.put("param2", "YIYUAN_345235");
//		params.put("param3", "17323287180");
//		params.put("param4", "四川 成都");
//		String temp1 = TemplateParser.parse(tpl,params);
//		System.out.println(temp1);
//	}
}

