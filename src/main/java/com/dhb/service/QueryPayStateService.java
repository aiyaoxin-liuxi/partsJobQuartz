package com.dhb.service;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.dhb.util.JsonUtil;
import com.dhb.util.http.HttpClientHelper;



@Service
public class QueryPayStateService {

	private final static Logger logs = Logger.getLogger(QueryPayStateService.class);
	
	//--------------兼职
	@Value("${parts.job.root}")
	String partsjobRoot;
	@Value("${parts.job.pay.query}")
	String partsjobqueryUrl;
	
	
	public Object query() throws Exception{
		logs.info("提现定时查询开始.........");
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("startDate", "20170401");
		String url = partsjobRoot + partsjobqueryUrl;
		String logMsg ="提现定时查询";
		getHttpRes(url,param,logMsg);
		return "";
	}

	private String getHttpRes(String url, Map<String, Object> paramMap, String logMsg) {
		StringBuilder json = JsonUtil.toJson(paramMap);
		logs.info(url+" <--上送的url，【"+logMsg+"】上送的param："+json.toString());
		String o = HttpClientHelper.doHttpClient(url, "POST", "utf-8", json.toString(), "60000","application/json","");
		return o;
	}
}
