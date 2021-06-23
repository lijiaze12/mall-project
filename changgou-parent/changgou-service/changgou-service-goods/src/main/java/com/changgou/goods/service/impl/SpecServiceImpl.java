package com.changgou.goods.service.impl;

import com.changgou.core.service.impl.CoreServiceImpl;
import com.changgou.goods.dao.CategoryMapper;
import com.changgou.goods.dao.SpecMapper;
import com.changgou.goods.pojo.Spec;
import com.changgou.goods.service.SpecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/****
 * @Author:admin
 * @Description:Spec业务层接口实现类
 * @Date 2019/6/14 0:16
 *****/
@Service
public class SpecServiceImpl extends CoreServiceImpl<Spec> implements SpecService {

    private SpecMapper specMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    public SpecServiceImpl(SpecMapper specMapper) {
        super(specMapper, Spec.class);
        this.specMapper = specMapper;
    }

    @Override
    public List<Spec> findByCategoryId(Integer id) {
        //1.根据商品分类的ID 获取模板的ID 的值
        Integer templateId = categoryMapper.selectByPrimaryKey(id).getTemplateId();
        //2.根据模板的ID 值获取该模板下的所有的规格的列表数据 select * from tb_spec wehre template_id=?
        Spec condition = new Spec();
        condition.setTemplateId(templateId); //where template_id=?
        //select * from tb_spec
        List<Spec> specList = specMapper.select(condition);
        //3.返回列表
        return specList;
    }
}
