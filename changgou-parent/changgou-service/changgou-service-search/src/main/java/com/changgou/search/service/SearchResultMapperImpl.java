package com.changgou.search.service;

import com.alibaba.fastjson.JSON;
import com.changgou.search.pojo.SkuInfo;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 反射 + 自定义注解
 * 专门获取高亮数据 进行返回-->（获取原生的数据进行封装 返回）
 * @author ljh
 * @version 1.0
 * @date 2020/11/16 14:37
 * @description 标题
 * @package com.changgou.search.service
 */
public class SearchResultMapperImpl implements SearchResultMapper {

    @Override
    public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
        //需要获取原生的数据
        //[获取高亮的数据]
        //再进行封装

        //1.获取当前页的记录集合
        List<T> content = new ArrayList<T>();
        SearchHits hits = response.getHits();
        if(hits==null || hits.getTotalHits()<=0){
            return new AggregatedPageImpl<T>(content);
        }
        for (SearchHit hit : hits) {//hit就是当前的文档(document)对象--->JSON
            SkuInfo info = JSON.parseObject(hit.getSourceAsString(), SkuInfo.class);//JSON 没高亮的数据

            //获取字段为name的高亮的数据
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            if(highlightFields!=null
                    && highlightFields.get("name")!=null
                    && highlightFields.get("name").fragments()!=null ){
                HighlightField highlightField = highlightFields.get("name");
                Text[] fragments = highlightField.fragments();
                //看需求 将数据进行拼接 返回
                StringBuffer sb = new StringBuffer();
                for (Text fragment : fragments) {
                    sb.append(fragment.string());
                }
                String nameHight = sb.toString();//高亮的数据
                if(!StringUtils.isEmpty(nameHight)){
                    info.setName(nameHight);
                }
            }
            content.add((T)info);
        }
        //2.获取分页的对象数据
        //3.获取总记录数
        long totalHits = hits.getTotalHits();
        //4.聚合的结果
        Aggregations aggregations = response.getAggregations();
        //5.获取scrollId (可以不要)
        String scrollId = response.getScrollId();

        return new AggregatedPageImpl<T>(content,pageable,totalHits,aggregations,scrollId);
    }
}
