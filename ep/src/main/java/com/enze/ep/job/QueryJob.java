package com.enze.ep.job;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class QueryJob {

//  每分钟启动
  @Scheduled(initialDelay = 3000,fixedRate = 12000)
  public void queryOrderPayState(){
      //System.out.println("now time:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
  
  }


}
