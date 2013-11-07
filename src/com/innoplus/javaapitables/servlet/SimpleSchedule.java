package com.innoplus.javaapitables.servlet;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.core.jmx.CronTriggerSupport;
import org.quartz.impl.StdSchedulerFactory;
 
public class SimpleSchedule
{
	//9:50 am
	static String SCHEDULE11="0 0 21 ? * SUN";
	
	static String SCHEDULE1="5 50 19 ? * FRI";
	
	static String SCHEDULE22 = "0 0 6 1 * ?";
	
	static String SCHEDULE2 = "0 49 20 18 * ?";
	
	
	static String SCHEDULE3 = "0 0 21,6 1,? * SUN,?";
	//Monday - Friday
	
    public static void main( String[] args ) throws SchedulerException
    {
        JobDetail job1 = JobBuilder
                .newJob(SimpleJob.class)
                .withIdentity("SimpleJob1")
                .build();        
        
		Trigger trigger1 = TriggerBuilder
                .newTrigger()
                .withIdentity("SimpleJob1")
//				.withSchedule(
//						CronScheduleBuilder.weeklyOnDayAndHourAndMinute(6, 22, 8))
//				.withSchedule(
//						CronScheduleBuilder.monthlyOnDayAndHourAndMinute(1, 6, 0))
				.withSchedule(
						CronScheduleBuilder.monthlyOnDayAndHourAndMinute(17, 22, 12))		
                .build();
        
        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
        scheduler.start();
        scheduler.scheduleJob(job1,trigger1);
    }
}