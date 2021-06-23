package com.changgou.content.dao;
import com.changgou.content.pojo.Content;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/****
 * @Author:admin
 * @Description:ContentDao
 * @Date 2019/6/14 0:12
 *****/
public interface ContentMapper extends Mapper<Content> {
    @Select(value="select * from tb_content where category_id=#{id} and status=1")
    List<Content> findByCategory(Long id);
}
