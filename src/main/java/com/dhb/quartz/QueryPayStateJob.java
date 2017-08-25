package com.dhb.quartz;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.dhb.service.QueryPayStateService;

/**
 * 提现定时查询程序
 * @author wxw
 *
 */
@Component
@Lazy(value=false)
public class QueryPayStateJob {
	
	private final static Logger logger = Logger.getLogger(QueryPayStateJob.class);
	
	@Autowired
	QueryPayStateService queryPayStateService;
	
	@Scheduled(cron = "0 0/2 * * * ?")//每2分钟一次
	public void query(){
		try {
			queryPayStateService.query();
		} catch (Exception e) {
			logger.error("提现定时查询异常");
			e.printStackTrace();
		}
	}

}
