package com.wtc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class WtcApplication {
    public static void main(String[] args) {
        SpringApplication.run(WtcApplication.class, args);
    }
}
