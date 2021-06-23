package com.changgou;


import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import entity.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * 商品微服务的启动类
 * @author ljh
 * @version 1.0
 * @date 2020/11/8 10:58
 * @description 标题
 * @package com.changgou
 */
@EnableApolloConfig
@SpringBootApplication
@EnableEurekaClient//交给eureka服务端进行管理
@EnableScheduling  //开启定时调度
@MapperScan(basePackages = "com.changgou.goods.dao")//包扫描 一定要使用通用的mapper的组件扫描注解
public class GoodsApplication {
    public static void main(String[] args) {
        SpringApplication.run(GoodsApplication.class,args);
    }

    //将idwork 交给spring容器管理
    @Bean
    public IdWorker idWorker(){
        return new IdWorker(0,0);
    }
}
