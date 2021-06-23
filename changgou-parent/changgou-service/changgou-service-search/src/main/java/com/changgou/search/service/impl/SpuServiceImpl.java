package com.changgou.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.goods.feign.SpuFeign;
import com.changgou.goods.pojo.Spu;
import com.changgou.search.dao.SpuEsMapper;
import com.changgou.search.pojo.SpuInfo;
import com.changgou.search.service.SpuService;
import entity.Result;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SpuServiceImpl implements SpuService {

    @Autowired
    private SpuFeign spuFeign;

    @Autowired
    private SpuEsMapper spuEsMapper;

    @Autowired
    private ElasticsearchTemplate esTemplate;

    /**
     * 导入spu数据到es
     */
    @Override
    public void importSpu(){
        //调用changgou-service-goods微服务
        Result<List<Spu>> spuListResult = spuFeign.findByStatus("1");
        //将数据转成search.Spu
        List<SpuInfo> spuInfos=  JSON.parseArray(JSON.toJSONString(spuListResult.getData()),SpuInfo.class);
        for(SpuInfo spuInfo:spuInfos){
            Map<String, Object> specMap= JSON.parseObject(spuInfo.getSpecItems()) ;
            spuInfo.setSpecMap(specMap);
        }
        spuEsMapper.saveAll(spuInfos);
    }

    /*
      搜索功能的实现  返回数据和分类id
     */

    public Map search(Map<String, String> searchMap) {
        //1.获取页面传递过来的关键字
        String keywords = searchMap.get("keywords");

        //2.创建查询对象 的构建对象
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();

        //3.设置查询的条件

        //设置分组条件  二级分类id
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("spuCategorygroup").field("category2Id").size(50));

        //设置分组条件  三级分类id
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("CateTgroup").field("category3Id").size(50));

        if (!StringUtils.isEmpty(keywords)) {
            //关键词查询
            nativeSearchQueryBuilder.withQuery(QueryBuilders.matchQuery("name", keywords));
        }

        //分类作为条件查询 组合bool查询
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        //二级分类条件
        if (!StringUtils.isEmpty(searchMap.get("category2Id"))) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("category2Id", searchMap.get("category2Id")));
        }
        //三级分类条件
        if (!StringUtils.isEmpty(searchMap.get("category3Id"))) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("category3Id", searchMap.get("category3Id")));
        }

        //构建过滤查询
        nativeSearchQueryBuilder.withFilter(boolQueryBuilder);


        //构建分页查询
        Integer pageNum = 1; //默认为第一页
        if (!StringUtils.isEmpty(searchMap.get("pageNum"))) {
            try {
                pageNum = Integer.valueOf(searchMap.get("pageNum"));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                pageNum=1;
            }
        }
        Integer pageSize = 8;   //每一页显示8条数据
        nativeSearchQueryBuilder.withPageable(PageRequest.of(pageNum - 1, pageSize));


        //构建排序查询 前端请求格式为：{"keywords":"手机","pageNum":"1","sortRule":"ASC","sortField":"price"}
        String sortRule = searchMap.get("sortRule");
        String sortField = searchMap.get("sortField");
        if (!StringUtils.isEmpty(sortRule) && !StringUtils.isEmpty(sortField)) {
            nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort(sortField).order(sortRule.equals("DESC") ? SortOrder.DESC : SortOrder.ASC));
        }


        //4.构建查询对象
        NativeSearchQuery query = nativeSearchQueryBuilder.build();

        //5.执行查询
        AggregatedPage<SpuInfo> spuPage = esTemplate.queryForPage(query, SpuInfo.class);

        //根据分组名称获取结果
        StringTerms stringTermsCategory = (StringTerms) spuPage.getAggregation("spuCategorygroup");

        StringTerms stringTermsBrand = (StringTerms) spuPage.getAggregation("CateTgroup");

         //调用方法获取分组列表数据
        List<String> category2List =getStringsCategoryList(stringTermsCategory);

        List<String> category3List = getStringsBrandList(stringTermsBrand);


        //6.返回结果
        Map resultMap = new HashMap<>();
        resultMap.put("category2List", category2List);
        resultMap.put("category3List", category3List);
        resultMap.put("rows", spuPage.getContent());
        resultMap.put("total", spuPage.getTotalElements());
        resultMap.put("totalPages", spuPage.getTotalPages());

        return resultMap;
    }

    /**
     * 获取2分类列表数据
     *
     * @param stringTerms
     * @return
     */
    private List<String> getStringsCategoryList(StringTerms stringTerms) {
        List<String> categoryList = new ArrayList<>();
        if (stringTerms != null) {
            for (StringTerms.Bucket bucket : stringTerms.getBuckets()) {
                String keyAsString = bucket.getKeyAsString();//分组的值
                categoryList.add(keyAsString);
            }
        }
        return categoryList;
    }

    /**
     * 获取3分类列表
     *
     * @param stringTermsBrand
     * @return
     */
    private List<String> getStringsBrandList(StringTerms stringTermsBrand) {
        List<String> brandList = new ArrayList<>();
        if (stringTermsBrand != null){
            for (StringTerms.Bucket bucket : stringTermsBrand.getBuckets()) {
                brandList.add(bucket.getKeyAsString());
            }
        }
        return brandList;
    }



}
