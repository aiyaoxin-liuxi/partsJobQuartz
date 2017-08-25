package com.dhb.quartz;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.dhb.service.ANQueryService;

/**
 * 	爱农的查询定时
  * @ClassName: ANQueryJob
  * @Description: TODO
  * @author zhaotisheng	
  * @date 2017年3月22日 下午2:38:10
  * Copyright (c) 2016, zhaotisheng@qq.com All Rights Reserved.
 */
@Component
@Lazy(value=false)
public class ANQueryJob {
	
	private final static Logger logger = Logger.getLogger(ANQueryJob.class);
	
	@Autowired
	ANQueryService aNQueryService;
	
	@Scheduled(cron = "${an.query.corn}")//每天0:00AM
	public void query(){
		try {
			aNQueryService.query();
		} catch (Exception e) {
			logger.error("爱农的查询定时失败");
			e.printStackTrace();
		}
	}

}
