package com.changgou.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.search.dao.SkuEsMapper;
import com.changgou.search.pojo.SkuInfo;
import com.changgou.search.service.SearchResultMapperImpl;
import com.changgou.search.service.SkuService;
import entity.Result;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author ljh
 * @version 1.0
 * @date 2020/11/14 11:24
 * @description 标题
 * @package com.changgou.search.service.impl
 */
@Service
public class SkuServiceImpl implements SkuService {

    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private SkuEsMapper skuEsMapper;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;//提供了很多的搜索的方法



    @Override
    public void importSku() {
        //1.通过商品微服务调用feign 获取所有的符合条件的sku的列表数据
        //1.1 需要在goods-api中创建feign接口
        //1.2 需要goods微服务中实现接口（根据状态获取所有的sku的数据）
        //1.3 需要添加goods-api的依赖里面有feign
        //1.4 需要开启feignclients
        //1.5 注入 并调用
        Result<List<Sku>> result = skuFeign.findByStatus("1");
        List<Sku> skuList = result.getData();
        String jsonString = JSON.toJSONString(skuList);
        List<SkuInfo> infos = JSON.parseArray(jsonString, SkuInfo.class);
        //需要将spec的字符串 转成MAP 存储到规格搜索的属性中
        for (SkuInfo info : infos) {
            //{"电视音响效果":"环绕","电视屏幕尺寸":"60英寸","尺码":"165"}
            String spec = info.getSpec();
            Map<String,Object> specMap = JSON.parseObject(spec, Map.class);
            info.setSpecMap(specMap);
        }

        //2.将数据导入到es服务器中
        skuEsMapper.saveAll(infos);
    }

    // {keywords:"手机"}
    @Override
    public Map<String, Object> search(Map<String, String> searchMap) {
        //1.获取页面传递过来的关键字
        String keywords = searchMap.get("keywords");
        //2.判断关键字是否为null 如果为null 给定一个默认值（通过方法生成）
        if(StringUtils.isEmpty(keywords)){
            keywords="华为";
        }
        //3.创建查询对象 的构建对象
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        //4.添加查询的条件

        //4.1 设置【商品分类】分组统计的条件  类似于 group by column
        // terms 相当于group by
        // terms中的参数 指定的是聚合结果的 别名 skuCategorygroup
        // field("categoryName") 指定聚合统计的字段
        // size(100000)  设置统计结果的大小

        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("skuCategorygroup").field("categoryName").size(100000));
        //4.2 设置【商品品牌】分组统计的条件  类似于 group by column
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("skuBrandgroup").field("brandName").size(100000));

        //4.3 设置【商品规格】 分组统计的条件 类似于 group by column  spec.keyword?????????????
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("skuSpecgroup").field("spec.keyword").size(100000));

        //=================================================高亮设置start=================================

        //设置高亮的前缀和后缀
        nativeSearchQueryBuilder.withHighlightBuilder(
                new HighlightBuilder()
                        .preTags("<em style=\"color:red\">")
                        .postTags("</em>"));
        //设置高亮的字段
        nativeSearchQueryBuilder.withHighlightFields(new HighlightBuilder.Field("name"));
        //=================================================高亮设置end===================================





        //匹配查询 从name上搜索内容为指定的关键字的数据（关键字需要进行分词再查询）
        //nativeSearchQueryBuilder.withQuery(QueryBuilders.matchQuery("name",keywords));
        // 多字段查询
        nativeSearchQueryBuilder.withQuery(QueryBuilders.multiMatchQuery(keywords,"name","brandName","categoryName"));



        //4.4 过滤查询  bool查询（多个条件组合查询）
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        //MUST   MUST_NOT  SHOULD  FILTER
        //4.4.1 必须满足 【商品分类】过滤查询 词条查询
        String category = searchMap.get("category");
        if(!StringUtils.isEmpty(category)) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("categoryName", category));
        }

        //4.4.2 必须满足 【商品品牌】过滤查询 词条查询
        String brand = searchMap.get("brand");
        if(!StringUtils.isEmpty(brand)) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("brandName", brand));
        }

        //4.4.3 必须满足 【商品规格选项】过滤查询 词条查询
        for (Map.Entry<String, String> stringStringEntry : searchMap.entrySet()) {
            String key = stringStringEntry.getKey();// spec_网络制式
            //获取规格数据 不需要获取别的数据
            if(key.startsWith("spec_")){
                String value = stringStringEntry.getValue();//电信2G

                //词条查询 不能分词
                boolQueryBuilder.filter(QueryBuilders.termQuery("specMap."+key.substring(5)+".keyword", value));
            }

        }
        //4.4.4 必须满足 【商品价格区间】过滤查询 范围查询
        String price = searchMap.get("price");//0-500,3000-*
        if(!StringUtils.isEmpty(price)){
            String[] split = price.split("-");
            if(split[1].equals("*")){
                //大于 3000
                boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").gte(split[0]));
            }else {
                boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").from(split[0], true).to(split[1], true));
            }
        }


        //设置过滤查询
        nativeSearchQueryBuilder.withFilter(boolQueryBuilder);

        //4.5 分页查询  设置
        //分页对象 参数1 指定当前的页码值 0 标识第一页 参数2 指定每页显示的行

        String pageNumString = searchMap.get("pageNum");
        if(StringUtils.isEmpty(pageNumString)){
            pageNumString="1";
        }
        Integer pageNum = Integer.parseInt(pageNumString);
        Integer pageSize = 40;

        Pageable pageble = PageRequest.of(pageNum-1,pageSize);
        nativeSearchQueryBuilder.withPageable(pageble);

        //4.6 设置排序 排序的字段 price 和排序的类型 DESC
        String sortField = searchMap.get("sortField");
        String sortRule= searchMap.get("sortRule");
        if(!StringUtils.isEmpty(sortField) && !StringUtils.isEmpty(sortRule)){
            //order by price desc/asc
            nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort(sortField).order(SortOrder.valueOf(sortRule)));
        }

        //5.构建查询对象
        NativeSearchQuery query =nativeSearchQueryBuilder.build();
        //6.执行分页查询
        //参数1 指定查询的对象 参数2 指定要返回的JSON对应的POJO的的字节码对象
        // 获取到的是非高亮的数据
        AggregatedPage<SkuInfo> skuInfos = elasticsearchTemplate.queryForPage(query, SkuInfo.class,new SearchResultMapperImpl());


        //7.获取结果（总记录数 总页数 当前的页的记录等数据）
        List<SkuInfo> content = skuInfos.getContent();//获取当前的页的记录
        long totalElements = skuInfos.getTotalElements();//获取总记录数
        int totalPages = skuInfos.getTotalPages();//获取总页数


        //7.1 获取【商品分类】的聚合结果
        List<String> categoryList = getStringsList(skuInfos, "skuCategorygroup");
        //7.2 获取【商品品牌】的聚合结果
        List<String> brandList = getStringsList(skuInfos, "skuBrandgroup");
        //7.3 获取【规格列表】聚合的结果
        Map<String, Set<String>> specMap = getStringSetMap((StringTerms) skuInfos.getAggregation("skuSpecgroup"));
        //8.组装到map中返回即可
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("rows",content);
        resultMap.put("totalPages",totalPages);
        resultMap.put("total",totalElements);
        //添加商品分类的列表展示返回给页面
        resultMap.put("categoryList",categoryList);
        //添加品牌列表的数据展示
        resultMap.put("brandList",brandList);

        //规格数据展示
        resultMap.put("specMap",specMap);

        return resultMap;
    }

    /**
     *
     * /**
     *          *
     *          * "aggregations": {
     *          *     "sum1": {
     *          *       "value": 3931377
     *          *     },
     *          *     "def": {
     *          *       "doc_count_error_upper_bound": 0,
     *          *       "sum_other_doc_count": 0,
     *          *       "buckets": [
     *          *         {
     *          *           "key": "笔记本",
     *          *           "doc_count": 7776
     *          *         },
     *          *         {
     *          *           "key": "平板电视",
     *          *           "doc_count": 26
     *          *         },
     *          *         {
     *          *           "key": "电视",
     *          *           "doc_count": 1
     *          *         }
     *          *       ]
     *          *     }
     *          *   }
     *          *
     *          *
     *          */
    private List<String> getStringsList(AggregatedPage<SkuInfo> skuInfos, String group) {
        List<String> list = new ArrayList<String>();
        Aggregation aggregation = skuInfos.getAggregation(group);

        StringTerms stringTerms = (StringTerms) skuInfos.getAggregation(group);
        if (stringTerms != null) {
            for (StringTerms.Bucket bucket : stringTerms.getBuckets()) {
                list.add(bucket.getKeyAsString());
            }
        }
        return list;
    }


    /**
     * 一顿操作（将规格数据解析 转换成Map对象返回前端）
     * @param stringTermsSpec
     * @return
     */
    private Map<String, Set<String>> getStringSetMap(StringTerms stringTermsSpec) {
        //{"手机屏幕尺寸":["5.5存","5存"],"网络制式":["2g","3g","4g"]}
        Map<String,Set<String>> specMap = new HashMap<>();
        if(stringTermsSpec!=null){
            Set<String> values = new HashSet<String>();

            for (StringTerms.Bucket bucket : stringTermsSpec.getBuckets()) {
                // {"手机屏幕尺寸":"5.5寸","网络":"电信4G","颜色":"白","测试":"学习","机身内存":"128G","存储":"32G","像素":"800万像素"}
                //{"手机屏幕尺寸":"5寸","网络":"电信4G","颜色":"白","测试":"学习","机身内存":"128G","存储":"64G","像素":"300万像素"}
                String keyAsString = bucket.getKeyAsString();
                Map<String,String> map = JSON.parseObject(keyAsString,Map.class);
                for (Map.Entry<String, String> stringSetEntry : map.entrySet()) {
                    String key = stringSetEntry.getKey();//手机屏幕尺寸
                    String value = stringSetEntry.getValue();//5.5寸
                    values = specMap.get(key);
                    if(values==null){
                        values = new HashSet<String>();
                    }
                    values.add(value);
                    specMap.put(key,values);
                }
            }
        }
        return specMap;
    }
}
