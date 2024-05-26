package com.dorkalorahalasz.locomotionmonitor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
// TODO make it configurable
@PropertySource("classpath:xpath_expressions.properties")
public class XpathConfig {

    @Value("${xpath}")
    protected String xpath;

    public static void main(String[] args) {
        SpringApplication.run(XpathConfig.class, args);
    }
}
