package com.wtc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableMongoAuditing
public class WtcApplication {
    public static void main(String[] args) {
        SpringApplication.run(WtcApplication.class, args);
    }
}
