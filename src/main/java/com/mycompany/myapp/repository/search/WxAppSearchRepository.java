package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.WxApp;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the WxApp entity.
 */
public interface WxAppSearchRepository extends ElasticsearchRepository<WxApp, Long> {
}
