package com.changgou.goods.service.impl;

import com.changgou.core.service.impl.CoreServiceImpl;
import com.changgou.goods.dao.CategoryMapper;
import com.changgou.goods.dao.ParaMapper;
import com.changgou.goods.pojo.Para;
import com.changgou.goods.pojo.Spec;
import com.changgou.goods.service.ParaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/****
 * @Author:admin
 * @Description:Para业务层接口实现类
 * @Date 2019/6/14 0:16
 *****/
@Service
public class ParaServiceImpl extends CoreServiceImpl<Para> implements ParaService {

    private ParaMapper paraMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    public ParaServiceImpl(ParaMapper paraMapper) {
        super(paraMapper, Para.class);
        this.paraMapper = paraMapper;
    }

    @Override
    public List<Para> findByCategoryId(Integer id) {
        //1.根据商品分类的ID 获取模板的ID 的值
        Integer templateId = categoryMapper.selectByPrimaryKey(id).getTemplateId();
        //2.根据模板的ID 值获取该模板下的所有的参数的列表数据 select * from tb_para wehre template_id=?
        Para condition = new Para();
        condition.setTemplateId(templateId); //where template_id=?
        //select * from tb_para
        List<Para> paramList = paraMapper.select(condition);
        //3.返回列表
        return paramList;
    }
}
