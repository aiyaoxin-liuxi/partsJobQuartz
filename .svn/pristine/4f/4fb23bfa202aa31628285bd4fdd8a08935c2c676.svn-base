package com.dhb.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.pub.util.security.MessageSecurity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dhb.util.JsonUtil;
import com.dhb.util.Stringer;
import com.dhb.util.http.HttpClientHelper;



/**
 * 
  * @ClassName: ANQueryService
  * @author zhaotisheng	
  * @date 2017年3月22日 下午2:42:02
  * Copyright (c) 2016, zhaotisheng@qq.com All Rights Reserved.
 */
@Service
public class ANQueryService {

	private final static Logger logs = Logger.getLogger(ANQueryService.class);
	
	public static final String enkey="cashier";
	
	//,PAYING("6","支付中")
	private final static String ORDER_STATUS= "6";
	//
	private final static String QUERY_FLAG="anCornQuery";//爱农的定时查询标志
	
	@Value("${cashier.root}")
	String cashierRoot;
	@Value("${an.query.url}")
	String queryUrl;
	@Value("${an.query.order.status.url}")
	String queryStatusUrl;
	
	//--------------兼职
	@Value("${parts.job.root}")
	String partsjobRoot;
	@Value("${parts.job.pay.notice}")
	String partsjobPayNotice;
	
	
	public Object query() throws Exception{
		logs.info("爱农的查询开始.........");
		//1.查询支付中的订单 orderQuery  【】
		Map<String,Object> param=getParamMap();
		
		String url=cashierRoot+queryUrl;String logMsg="爱农的查询支付中";
		String o=getHttpRes(url,param,logMsg);
		if(Stringer.isNullOrEmpty(o)){
			logs.info(" 【"+logMsg+"】返回的结果空\r\n\t");
			return null;
		}
		logs.info(" 【"+logMsg+"】返回的结果\r\n\t"+o);
		//2.循环查询 订单状态
		//2.1.如果成功，通知兼职
		getOrderTransAndNoticePartJob(o);
		
		
		
		
		
		return "";
	}
	

	private void getOrderTransAndNoticePartJob(String o) throws Exception {
		logs.info(" 解析orderTrans\r\n\t");
		JsonNode rootNode = JsonUtil.toJsonNode(o);
		JsonNode returnCode = rootNode.path("returnCode");
		
		if(Stringer.isNullOrEmpty(returnCode) || !returnCode.getTextValue().toString().equals("000000")){
			logs.info(" ##>>>>returnCode 不是 000000");
			return ;
		}
		
		JsonNode orderTransNode = rootNode.path("orderTrans");
		if(Stringer.isNullOrEmpty(orderTransNode)){
			logs.info(" ##>>>>orderTrans 为空");
			return ;
		}
		
		Iterator<JsonNode> iterator = orderTransNode.iterator();
		while(iterator.hasNext()){
			JsonNode next = iterator.next();
			JsonNode oriordercore2Node = next.path("oriordercore2");
			if(!Stringer.isNullOrEmpty(oriordercore2Node) && !Stringer.isNullOrEmpty(oriordercore2Node.getTextValue()) ){
				String status = next.path("status").getTextValue();
				
				logs.info(" ##>>>>oriordercore2: "+oriordercore2Node.getTextValue() +" \t paystatus:"+next.path("paystatus").getTextValue());
				
				if(status.equals(ORDER_STATUS)){//3 支付中的去查询，大学生的去通知
					// orderTran Platcode   
					invokeChannel2Query(next);
				}
			}
		}
	}


	/**
	 * 
	  * invokeChannel2Query
	  * 支付中的调起通道查询
	  * .如果成功，通知兼职 
	  * @Title: invokeChannel2Query
	  * @Description: TODO
	  * @param @param oriordercore2Node    设定文件
	  * @return void    返回类型
	  * @throws
	 */
	private void invokeChannel2Query(JsonNode oriordercore2Node) {
//		COLLEGE_PART_TIME("04","大学生兼职"), {"queryFlag":"anCornQuery","oriordercore2":"CZ_17032117150846089790"}
		HashMap<String, Object> param = new HashMap<String,Object>();
		String oriordercore2 = oriordercore2Node.path("oriordercore2").getTextValue();
		if(Stringer.isNullOrEmpty(oriordercore2)){
			return;
		}
		param.put("queryFlag", QUERY_FLAG);param.put("oriordercore2", oriordercore2 );
		String logMsg="查询支付中的订单 是否完成支付";
		String o=getHttpRes(cashierRoot+queryStatusUrl,param,logMsg);
		if(Stringer.isNullOrEmpty(o)){
			logs.info(" 【"+logMsg+"】返回的结果空\r\n\t");
			return ;
		}
		logs.info(" 【"+logMsg+"】返回的结果\r\n\t"+o);
		Map<String, Object> map = JsonUtil.toMap(new StringBuilder(o));
		Object paystatusObj = map.get("paystatus");
		if(Stringer.isNullOrEmpty(paystatusObj) || !paystatusObj.toString().equals("50")){//不成功的就不通知了
			
			return;
		}
		noticePartJob(oriordercore2Node);
	}


	/**
	 * 通知兼职
	  * 
	  *
	  * @Title: noticePartJob
	  * @Description: TODO
	  * @param @param oriordercore2Node    设定文件
	  * @return void    返回类型
	  * @throws
	 */
	private void noticePartJob(JsonNode oriordercore2Node) {
		logs.info(" ##>>>>通知兼职 start....");
		//COLLEGE_PART_TIME("04","大学生兼职"),
		JsonNode platcodeNode = oriordercore2Node.path("platcode");
		if(!Stringer.isNullOrEmpty(platcodeNode) && platcodeNode.getTextValue().equals("04")){
			//通知
			String url=partsjobRoot+partsjobPayNotice;
			Map<String,Object> map=new HashMap<String,Object>();
			Stringer.getOrderTranAttribute(map,oriordercore2Node);
			map.put("paystatus", "50");//支付成功
			String sign = MessageSecurity.getMapObjMessageSecurity(map,enkey);
			map.put("sign", sign);
			StringBuilder json = JsonUtil.toJson(map);
			logs.info(url.toString()+"<--url 【通知兼职项目】参数： start \t"+json);
			String o = HttpClientHelper.doHttpClient(url, "POST", "utf-8", json.toString(), "60000","application/json","");
			//规定给我返回 success    
			logs.info("【通知兼职项目】返回的结果： start \t"+o);
			
		}
		
	}




	private String getHttpRes(String url, Map<String, Object> paramMap, String logMsg) {
		StringBuilder json = JsonUtil.toJson(paramMap);
//		String url = Constant.RECHARGE_URL;
		logs.info(url+" <--上送的url，【"+logMsg+"】上送的param："+json.toString());
		String o = HttpClientHelper.doHttpClient(url, "POST", "utf-8", json.toString(), "60000","application/json","");
		return o;
	}




	private Map<String, Object> getParamMap() {
		HashMap<String, Object> paramMap = new  HashMap<String, Object>();
		//{"queryJob":"anCornQuery","pageNumber":20,"pageSize":10,"status":6}
		paramMap.put("queryJob", QUERY_FLAG);
		paramMap.put("pageNumber", 200);
		paramMap.put("pageSize", 0);
		//支付中的是6 
		paramMap.put("status", ORDER_STATUS);
		
		return paramMap;
	}
}
