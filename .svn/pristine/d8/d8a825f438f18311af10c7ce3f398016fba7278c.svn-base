package com.dhb.util;

import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.jackson.JsonNode;


/**
 * 
 *
 * <strong>Stringer </strong>. <br> 
 * <strong>Description :  封装一些和本框架相关的工具</strong> <br>
 * <strong>Create on : 2016年11月28日 上午10:52:29</strong>. <br>
 * <p>
 * <strong>Copyright (C) zhl Co.,Ltd.</strong> <br>
 * </p>
 * @author zts zhaotisheng@qq.com <br>
 * @version <strong>zhl-0.1.0</strong> <br>
 * <br>
 * <strong>修改历史: .</strong> <br>
 * 修改人 修改日期 修改描述<br>
 * Copyright ©  zhl by zts Inc. All Rights Reserved
 * -------------------------------------------<br>
 * <br>
 * <br>
 */
public class Stringer {

	public static String nullToEmpty(String string) {
		return isNullOrEmpty(string) ? "" : string;
	}
	/**
	 * 
	 * @desc toUperCaseFirstchar 大写第一个字母
	 * @author by zts 2016年11月25日下午7:54:36
	 * @param 
	 * String
	 * @exception
	 * @since  0.1.0
	 */
	public static  String toUperCaseFirstchar(String name) {
		if(isNullOrEmpty(name)){
			return null;
		}
		return name.substring(0,1).toUpperCase()+""+name.substring(1,name.length());
	}
	
	/**
	 * @author by zts Aug 21, 2015
	 *
	 * @desc 判断某对象是否为空.
	 * @param obj
	 * @return
	 */
	public static boolean isNullOrEmpty(Object obj) {

		boolean result = true;
		if (obj == null) {
			return true;
		}
		if (obj instanceof String) {
			result = (obj.toString().trim().length() == 0) || obj.toString().trim().equals("null");
		} else if (obj instanceof Collection) {
			result = ((Collection<?>) obj).size() == 0;
		} else if (obj instanceof Map) {
			result = ((java.util.Map<?,?>) obj).isEmpty();
		}else {
			result = ((obj == null) || (obj.toString().trim().length() < 1)) ? true : false;
		}
		return result;
	}

	
	/**
	 * 
	 * @desc checkNotNull(指定实体的属性不能为空)
	 * 
	 * 目前支持string参数，有空可以扩展
	 * @author by zts 2016年11月28日上午10:54:48
	 * @param 
	 * void
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @exception
	 * @since  0.1.0
	 * 
	 * 
	 * 
	 */
	public  static <T> void checkNotNull( T request, Object... obj) throws Exception {
		int len = obj.length;
		if(isNullOrEmpty(request) || isNullOrEmpty(obj) || !(len>=1))return ;		
		Class<? extends Object> c = request.getClass();
		for(int i=0;i<len;i++){
			Object o = obj[i];
			if(o instanceof java.lang.String){
				String s=(String)o;
				Object v = c.getMethod("get"+toUperCaseFirstchar(s)).invoke(request);
				if(isNullOrEmpty(v)){
						throw new RuntimeException(o+"不能为空");
				}
			}else{
				throw new RuntimeException("请扩展其他方法在使用");
			}
		}
	}


	public static boolean isURL(String str_url){  
        String strRegex = "^((https|http|ftp|rtsp|mms)://)"   
        + "(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?" //ftp的user@   
        + "(([0-9]{1,3}\\.){3}[0-9]{1,3}" // IP形式的URL- 199.194.52.184   
        + "|" // 允许IP和DOMAIN（域名）  
        + "(([0-9a-zA-Z_!~*'()-]+\\.)*" // 域名- www.   
        + "([0-9a-zA-Z][0-9a-zA-Z-]{0,61})?[0-9a-zA-Z]\\." // 二级域名   
        + "[a-zA-Z]{2,6})|([0-9a-zA-Z]{1,}))" // first level domain- .com or .museum 
        + "(:[0-9]{1,5})?" // 端口- :80   (爱农给的是5位的"http://27.115.49.122:38280/bas/FrontTrans"; 20160803修改)
        + "((/?)|" // a slash isn't required if there is no file name   
        + "(/[0-9a-zA-Z_!~*'().;?:@&=+$,%#-]+)+/?)$"; 
        Pattern pattern = Pattern.compile(strRegex);
        Matcher m=pattern.matcher(str_url);  
        return m.matches();
    }

	public static boolean isMobile(String mobile) {
		Pattern compile = Pattern.compile("^((13[0-9])|(15[0-9])|147|(17[0-9])|(18[0-9]))[0-9]{8}$");
		Matcher matcher = compile.matcher(mobile);
		return matcher.matches();
	} 
	//数字
	public static boolean isDigit(String str) {
		Pattern compile = Pattern.compile("^\\d+$");
		Matcher matcher = compile.matcher(str);
		return matcher.matches();
	}
	//身份证号（simple）
	public static boolean isIDNo(String str) {
		Pattern compile = Pattern.compile("(^\\d{18}$)|(^\\d{15}$)");
		Matcher matcher = compile.matcher(str);
		return matcher.matches();
	}
	
	public static void main(String[] args) {
//		System.out.println("he");
		System.out.println(isDigit("13d193"));
	}
	
	
	public static void getOrderTranAttribute(Map<String, Object> map, JsonNode orderTranNode) {
		map.put("oriordercore",orderTranNode.path("oriordercore2").getTextValue());//核心订单号
		map.put("orderid",orderTranNode.path("orderid").getTextValue());//核心订单号
		map.put("paystatus",orderTranNode.path("paystatus").getTextValue());
		map.put("amount",orderTranNode.path("amount").getTextValue());
	}
	public static void getTtransactionAttribute(Map<String, Object> map, JsonNode ttransactionNode) {
		//	new Date(path.getBigIntegerValue().intValue())
		//JsonNode path = ttransactionNode.path("plattime");;
		map.put("plattime",ttransactionNode.path("plattime").getBigIntegerValue());
	}




	
}
