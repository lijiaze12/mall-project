package com.changgou.timer;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RedisToSqlTask {
    //每55秒执行一次  讲redis中的点赞数数据插入到mysql中评价表中点赞数字段中

    public void loadGoodsPushRedis(){
        //从redis中的点赞数据的
    }


}
