package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
// import org.apache.catalina.core.ApplicationContext;

import java.util.Arrays;

@SpringBootApplication
public class TransferApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(TransferApplication.class, args);
        System.out.println("transfer 服务启动完成. profile 为：" + Arrays.asList(context.getEnvironment().getActiveProfiles()));
    }

}
