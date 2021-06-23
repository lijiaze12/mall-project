package com.changgou.goods.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.changgou.core.service.impl.CoreServiceImpl;
import com.changgou.goods.dao.BrandMapper;
import com.changgou.goods.dao.CategoryMapper;
import com.changgou.goods.dao.SkuMapper;
import com.changgou.goods.dao.SpuMapper;
import com.changgou.goods.pojo.Goods;
import com.changgou.goods.pojo.Sku;
import com.changgou.goods.pojo.Spu;
import com.changgou.goods.service.SpuService;
import entity.IdWorker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/****
 * @Author:admin
 * @Description:Spu业务层接口实现类
 * @Date 2019/6/14 0:16
 *****/
@Service
public class SpuServiceImpl extends CoreServiceImpl<Spu> implements SpuService {

    private SpuMapper spuMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    public SpuServiceImpl(SpuMapper spuMapper) {
        super(spuMapper, Spu.class);
        this.spuMapper = spuMapper;
    }

    @Autowired
    private IdWorker idWorker;

    @Override
    @Transactional(rollbackFor = Exception.class)//spring的本地事务 并当 发送了EXCEpition就回滚
    public void saveGoods(Goods goods) {
        //1.获取到页面传递过来的spu的数据 添加到spu表
        //spu
        Spu spu = goods.getSpu();


        long id;



        //判断如果有spu的ID 传递过来说明就是更新
        if(spu.getId()!=null){
            id=spu.getId();
            //更新spu
            spuMapper.updateByPrimaryKeySelective(spu);
            //更新sku 先删除掉原来的数据库中的旧数据，再进行添加
            //1 delete from tb_sku where spu_id=?
            Sku condition = new Sku();//等号的条件
            condition.setSpuId(spu.getId());
            skuMapper.delete(condition);
            //2.添加sku列表
        }else {
            //否则就是添加 spu

            //1.1 生成主键
             id= idWorker.nextId();
            spu.setId(id);
            spuMapper.insertSelective(spu);
            //添加sku列表
        }


        //多个sku
        List<Sku> skuList = goods.getSkuList();

        //创建一个集合 存放sku的价格的数据集合

        List<Integer> list = new ArrayList<>();


        for (Sku sku : skuList) {

            Integer price = sku.getPrice();
            list.add(price);

            //2.1 生成主键
            long skuId = idWorker.nextId();
            sku.setId(skuId);
            //2.2 设置名称 通过SPU名称+空格+规格属性名+++++++
            String name = spu.getName();
            // spec中有 {"灰色长型"}
            // 将spec 转成MAP 循环遍历map 获取到元素中的vlaue的值 将值进行拼接即可
            String spec = sku.getSpec();

            name+=" "+spec;

            sku.setName(name);
            //2.3设置创建时间和更新时间
            sku.setCreateTime(new Date());
            sku.setUpdateTime(sku.getCreateTime());
            //2.4 设置spu_id
            sku.setSpuId(spu.getId());
            //2.5 设置三级分类的ID 和 分类名
            sku.setCategoryId(spu.getCategory3Id());
            sku.setCategoryName(categoryMapper.selectByPrimaryKey(spu.getCategory3Id()).getName());

            //2.6 设置品牌的名称
            sku.setBrandName(brandMapper.selectByPrimaryKey(spu.getBrandId()).getName());

            skuMapper.insertSelective(sku);
        }

        if(list.size()>0) {//判断price的集合是否为空
            //对多个sku的price的集合进行冒泡排序
            for (int i = 0; i < list.size() - 1; i++) {
                for (int j = 0; j < list.size() - i - 1; j++) {//比较两个整数
                    if (list.get(j) > list.get(j + 1)) {
                        /*交换*/
                        Integer temp = list.get(j);
                        list.set(j, list.get(j + 1));
                        list.set(j + 1, temp);
                    }
                }
            }
            //把最小的价格 设置到spu的price中 更新方式
            Spu spu1 = new Spu();
            spu1.setId(id);
            Integer price = list.get(0);
            spu1.setDefaultPrice(price);

            //更新spu
            spuMapper.updateByPrimaryKeySelective(spu1);
        }



    }

    @Override
    public Goods findGoodsById(Long id) {
        //1.根据ID 从spu表中获取spu的记录
        Spu spu = spuMapper.selectByPrimaryKey(id);
        String specItems = spu.getSpecItems();
        Map<String,String> specMap = JSON.parseObject(specItems, Map.class);


        ArrayList<Map> arrayList = new ArrayList<>();



        //entrySet()方法将键和值的映射关系作为对象存储到了Set集合中
        Set<Map.Entry<String, String>> entrySet=specMap.entrySet();
        Iterator<Map.Entry<String, String>> it2=entrySet.iterator();
        while(it2.hasNext()){
            HashMap<String, Object> map = new HashMap<>();
            Map.Entry<String, String> me=it2.next();
            String key = me.getKey();



            map.put("title",key);
            map.put("list",me.getValue());

            arrayList.add(map);
        }

        spu.setSpecMap(arrayList);




        //2.根据spu的ID 从sku表中获取sku的列表数据
        //select * from tb_sku where spu_id=?
        Sku condition = new Sku();
        condition.setSpuId(id);//where spu_id=?

        List<Sku> skuList = skuMapper.select(condition);
        //3.组装组合
        return new Goods(spu,skuList);
    }

    /***
     * 商品审核
     * @param spuId
     */
    @Override
    public void audit(Long spuId) {
        //查询商品
        Spu spu = spuMapper.selectByPrimaryKey(spuId);
        //判断商品是否已经删除
        if(spu.getIsDelete().equalsIgnoreCase("1")){
            throw new RuntimeException("该商品已经删除！");
        }
        //实现审核
        spu.setStatus("1"); //审核通过
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    /**
     * 商品下架
     * @param spuId
     */
    @Override
    public void pull(Long spuId) {
        Spu spu = spuMapper.selectByPrimaryKey(spuId);
        if(spu.getIsDelete().equals("1")){
            throw new RuntimeException("此商品已删除！");
        }
        spu.setIsMarketable("0");//下架状态
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    /***
     * 商品上架
     * @param spuId
     */
    @Override
    public void put(Long spuId) {
        Spu spu = spuMapper.selectByPrimaryKey(spuId);
        //检查是否删除的商品
        if(spu.getIsDelete().equals("1")){
            throw new RuntimeException("此商品已删除！");
        }
        //上架状态
        spu.setIsMarketable("1");
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    /***
     * 批量上架
     * @param ids:需要上架的商品ID集合
     * @return
     */
    @Override
    public int putMany(Long[] ids) {
        Spu spu=new Spu();
        spu.setIsMarketable("1");//上架
        //批量修改
        Example example=new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", Arrays.asList(ids));//id
        //下架
        criteria.andEqualTo("isMarketable","0");
        //审核通过的
        criteria.andEqualTo("status","1");
        //非删除的
        criteria.andEqualTo("isDelete","0");
        return spuMapper.updateByExampleSelective(spu, example);
    }

    /***
     * 根据状态查询SKU列表
     * @return
     */
    @Override
    public List<Spu> findByStatus(String status) {
        Spu spu = new Spu();
        spu.setStatus(status);
        return spuMapper.select(spu);
    }

    @Override
    public int  addSaleNum(Long id, Integer saleNum) {
        return spuMapper.addSaleNum(id,saleNum);
    }





}
