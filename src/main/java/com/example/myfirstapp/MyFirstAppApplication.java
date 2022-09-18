package com.example.myfirstapp;

import com.example.myfirstapp.telegram.MyAmazingBot;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


@SpringBootApplication
public class MyFirstAppApplication {

	private static final String MESSAGE = "Время дейли пришло!!!";
	private static final String CHAT_ID = "126927462";
	private static final String CRON_TASK = "0 0 11 ? * MON-FRI *";


	public static void main(String[] args) throws SchedulerException {
//		SpringApplication.run(MyFirstAppApplication.class, args);

		setUpTelegramBot();
		setUpScheduler();
	}

	private static MyAmazingBot bot = new MyAmazingBot();
	public static void setUpTelegramBot() {
		try {
			TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
			botsApi.registerBot(bot);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}

	public static class MySuperAmazingJob implements Job {
		@Override
		public void execute(JobExecutionContext context) throws JobExecutionException {
			try {
				bot.sendMessage(CHAT_ID, MESSAGE);
			} catch (TelegramApiException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static void setUpScheduler() throws SchedulerException {
		SchedulerFactory factory = new StdSchedulerFactory();
		Scheduler scheduler = factory.getScheduler();

		JobDetail jobDetail = JobBuilder
								  .newJob(MySuperAmazingJob.class)
								  .withIdentity("my super amazing job", "jusaf group")
								  .build();

		Trigger trigger = TriggerBuilder.newTrigger()
							  .withIdentity("my super amazing trigger", "jusaf group")
							  .startNow()
							  .withSchedule(CronScheduleBuilder.cronSchedule(CRON_TASK))
							  .build();

		scheduler.scheduleJob(jobDetail, trigger);
		scheduler.start();
	}


}
