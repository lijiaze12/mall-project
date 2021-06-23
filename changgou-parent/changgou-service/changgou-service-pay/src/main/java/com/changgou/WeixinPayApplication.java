package com.changgou;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

/**
 * @author ljh
 * @version 1.0
 * @date 2020/11/25 10:00
 * @description 标题
 * @package com.changgou
 */
@EnableApolloConfig
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableEurekaClient
public class WeixinPayApplication {
    public static void main(String[] args) {
        SpringApplication.run(WeixinPayApplication.class, args);
    }


    //mq:
    //pay:
    //exchange:
    //order: exchange.order
    //queue:
    //order: queue.order
    //routing:
    //key: queue.order


    @Autowired
    private Environment environment;

    //创建交换机 类型（发布订阅模式：1.广播 2.路由 3.通配符）
    @Bean
    public DirectExchange createExchange() {
        return new DirectExchange(environment.getProperty("mq.pay.exchange.order"));
    }

    //创建队列
    @Bean
    public Queue createQueue() {
        return new Queue(environment.getProperty("mq.pay.queue.order"));
    }

    //创建绑定
    @Bean
    public Binding createBinding() {
        return BindingBuilder.bind(createQueue()).to(createExchange()).with(environment.getProperty("mq.pay.routing.key"));
    }
}
