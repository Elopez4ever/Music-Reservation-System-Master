package com.bookingsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling //开启定时任务
@SpringBootApplication
@ServletComponentScan
public class ClassroomBookingSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClassroomBookingSystemApplication.class, args);
    }

}
