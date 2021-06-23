package com.changgou.search.dao;

import com.changgou.search.pojo.SkuInfo;
import com.changgou.search.pojo.SpuInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpuEsMapper extends ElasticsearchRepository<SpuInfo,Long> {
}