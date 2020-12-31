package com.hangyang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class SpringcloudOrderZkApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringcloudOrderZkApplication.class, args);
    }

}
