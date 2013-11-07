package com.innoplus.javaapitables.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.CronScheduleBuilder;
import org.quartz.DateBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.innoplus.javaapitables.core.Configuration;

public class CronJobServlet extends HttpServlet {
	
	private static Log log = LogFactory.getLog(CronJobServlet.class);
	private static String SCHEDULE = Configuration.getInstance().getProperty("SCHEDULE");
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		log.info("CronJobServlet start");

		try {
			
			

	        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
	        scheduler.start();
	        
	        // 9:50
	        JobDetail job950 = JobBuilder
	                .newJob(UpdateTablesJob.class)
	                .withIdentity("job950")
	                .build();
	        Trigger trigger950 = TriggerBuilder
	                .newTrigger()
	                .withIdentity("trigger950")
	                 .withSchedule(CronScheduleBuilder.atHourAndMinuteOnGivenDaysOfWeek(10, 50, DateBuilder.MONDAY, DateBuilder.TUESDAY, DateBuilder.WEDNESDAY, DateBuilder.THURSDAY, DateBuilder.FRIDAY))
	                .build();
	        scheduler.scheduleJob(job950, trigger950);

	        // 13:15
	        JobDetail job1315 = JobBuilder
	                .newJob(UpdateTablesJob.class)
	                .withIdentity("job1315")
	                .build();
	        Trigger trigger1315 = TriggerBuilder
	                .newTrigger()
	                .withIdentity("trigger1315")
	                 .withSchedule(CronScheduleBuilder.atHourAndMinuteOnGivenDaysOfWeek(13, 15, DateBuilder.MONDAY, DateBuilder.TUESDAY, DateBuilder.WEDNESDAY, DateBuilder.THURSDAY, DateBuilder.FRIDAY))
	                .build();
	        scheduler.scheduleJob(job1315, trigger1315);
	        
	        // 16:20
	        JobDetail job1620 = JobBuilder
	                .newJob(UpdateTablesJob.class)
	                .withIdentity("job1620")
	                .build();
	        Trigger trigger1620 = TriggerBuilder
	                .newTrigger()
	                .withIdentity("trigger1620")
	                 .withSchedule(CronScheduleBuilder.atHourAndMinuteOnGivenDaysOfWeek(16, 20, DateBuilder.MONDAY, DateBuilder.TUESDAY, DateBuilder.WEDNESDAY, DateBuilder.THURSDAY, DateBuilder.FRIDAY))
	                .build();
	        scheduler.scheduleJob(job1620, trigger1620);
	        
	        // weekly job
	        JobDetail job1 = JobBuilder
	                .newJob(WeeklyJob.class)
	                .withIdentity("WeeklyJob")
	                .build();
	        Trigger trigger1 = TriggerBuilder
	                .newTrigger()
	                .withIdentity("WeeklyJob")
	                .withSchedule(CronScheduleBuilder.weeklyOnDayAndHourAndMinute(1, 21, 0))
	                .build();
	        scheduler.scheduleJob(job1, trigger1);
	        
	        JobDetail job2 = JobBuilder
	                .newJob(MonthlyJob.class)
	                .withIdentity("MonthlyJob")
	                .build();
	        Trigger trigger2 = TriggerBuilder
	                .newTrigger()
	                .withIdentity("MonthlyJob")
	                .withSchedule(CronScheduleBuilder.monthlyOnDayAndHourAndMinute(1, 6, 0))
	                .build();
	        scheduler.scheduleJob(job2, trigger2);
	        
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("CronJobServlet Error", ex);
		}
	}

}
