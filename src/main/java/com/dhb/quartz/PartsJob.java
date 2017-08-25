package com.dhb.quartz;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.dhb.util.UrlCommon;

/*
 * 兼职系统定时器类
 */
@Component
@Lazy(value=false)
public class PartsJob {
	
	private final static Logger logger = Logger.getLogger(PartsJob.class);
	
	@Value("${parts.job.root}")
	String partsjobRoot;
	@Value("${buildSettlement}")
	String buildSettlement;
	@Value("${finishJob}")
	String finishJob;
	
	/**
	 * 定时器————每日生成结算单和计算记录信息，修改生成结算记录的申请单状态和工作状态
	 */
//	@Scheduled(cron = "0 0/1 * * * ?")//每2分钟一次
	@Scheduled(cron = "0 0 0 * * ?")//每天0:00AM
    public void buildSettlement() {
		try {
			logger.info("【定时开始】兼职系统--生成每日结算：地址：" + partsjobRoot + buildSettlement);
			String returnStr = UrlCommon.getHtml(partsjobRoot + buildSettlement);
			returnStr = URLDecoder.decode(returnStr, "utf-8");
			logger.info("【定时结束】兼职系统--生成每日结算--->" + returnStr);
		} catch (UnsupportedEncodingException e) {
			logger.info(e.getMessage());
		}
    }
	
	/**
	 * 每日检查全部完成并结算成功的记录，改状态为已完成
	 */
//	@Scheduled(cron = "0 0/2 * * * ?")//每2分钟一次
	@Scheduled(cron = "0 0 2 * * ?")//每天2:00AM
    public void finishJob() {
		try {
			logger.info("【定时开始】兼职系统--检查全部完成结算：地址：" + partsjobRoot + finishJob);
			String returnStr = UrlCommon.getHtml(partsjobRoot + finishJob);
			returnStr = URLDecoder.decode(returnStr, "utf-8");
			logger.info("【定时结束】兼职系统--检查全部完成结算--->" + returnStr);
		} catch (UnsupportedEncodingException e) {
			logger.info(e.getMessage());
		}
    }
	
    
    
}
