package com.tencent.wxcloudrun;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class WxCloudRunApplication {  

  public static void main(String[] args) {
    TimeZone.setDefault(TimeZone.getTimeZone("GMT+8:00"));
    SpringApplication.run(WxCloudRunApplication.class, args);
  }
}
