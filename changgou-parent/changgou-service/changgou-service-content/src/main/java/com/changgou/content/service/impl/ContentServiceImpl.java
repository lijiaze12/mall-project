package com.changgou.content.service.impl;

import com.changgou.content.dao.ContentMapper;
import com.changgou.content.pojo.Content;
import com.changgou.content.service.ContentService;
import com.changgou.core.service.impl.CoreServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/****
 * @Author:admin
 * @Description:Content业务层接口实现类
 * @Date 2019/6/14 0:16
 *****/
@Service
public class ContentServiceImpl extends CoreServiceImpl<Content> implements ContentService {

    private ContentMapper contentMapper;

    @Autowired
    public ContentServiceImpl(ContentMapper contentMapper) {
        super(contentMapper, Content.class);
        this.contentMapper = contentMapper;
    }

    @Override
    public List<Content> findByCategory(Long id) {
        //select * from tb_content where category_id=1
        return contentMapper.findByCategory(id);
    }
}
