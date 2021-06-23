package com.changgou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author ljh
 * @version 1.0
 * @date 2020/11/8 10:45
 * @description 标题
 * @package com.changgou
 */
@SpringBootApplication
@EnableEurekaServer//启用eureka的服务端
public class EurekaApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaApplication.class,args);
    }
}
