package com.changgou.goods.service.impl;

import com.changgou.core.service.impl.CoreServiceImpl;
import com.changgou.goods.dao.CategoryMapper;
import com.changgou.goods.dao.TemplateMapper;
import com.changgou.goods.pojo.Category;
import com.changgou.goods.pojo.Template;
import com.changgou.goods.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/****
 * @Author:admin
 * @Description:Template业务层接口实现类
 * @Date 2019/6/14 0:16
 *****/
@Service
public class TemplateServiceImpl extends CoreServiceImpl<Template> implements TemplateService {

    @Autowired
    private CategoryMapper categoryMapper;

    private TemplateMapper templateMapper;

    @Autowired
    public TemplateServiceImpl(TemplateMapper templateMapper) {
        super(templateMapper, Template.class);
        this.templateMapper = templateMapper;
    }

    /***
     * 根据分类ID查询模板信息
     * @param id
     * @return
     */
    @Override
    public Template findByCategoryId(Integer id) {
        //先根据分类id   获取到对应的分类对象数据
        Category category = categoryMapper.selectByPrimaryKey(id);

        //再从分类对象中获取到模板id  这个模板id就是模板表中的主键id。直接根据主键查询模板数据
        return templateMapper.selectByPrimaryKey(category.getTemplateId());
    }

}
