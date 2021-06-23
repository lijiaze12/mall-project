package com.changgou.search.dao;

import com.changgou.search.pojo.SkuInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author ljh
 * @version 1.0
 * @date 2020/11/14 11:14
 * @description 标题
 * @package com.changgou.search.dao
 */
public interface SkuEsMapper extends ElasticsearchRepository<SkuInfo,Long> {

}
