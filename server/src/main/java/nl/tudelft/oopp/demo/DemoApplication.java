package nl.tudelft.oopp.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class DemoApplication {

    /* @Bean
     public TaskExecutor taskExecutor() {
         ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
         executor.setCorePoolSize(5);
         executor.setMaxPoolSize(10);
         executor.setQueueCapacity(25);
         return executor;
     }
     public class ThreadPoolTaskSchedulerConfig {

         @Bean
         public ThreadPoolTaskScheduler threadPoolTaskScheduler(){
             ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
             threadPoolTaskScheduler.setPoolSize(5);
             threadPoolTaskScheduler.setThreadNamePrefix("ThreadPoolTaskScheduler");
             return threadPoolTaskScheduler;
         }
     }

     */
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
