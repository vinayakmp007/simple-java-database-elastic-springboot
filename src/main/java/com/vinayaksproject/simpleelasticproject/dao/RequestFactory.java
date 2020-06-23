/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vinayaksproject.simpleelasticproject.dao;

import com.vinayaksproject.simpleelasticproject.ElasticConfig;
import com.vinayaksproject.simpleelasticproject.enums.ElasticRequest;
import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.MultiGetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 *
 * @author vinayak
 */
@Configuration
public class RequestFactory {

    @Autowired
    private ElasticConfig elasticConfig;

    @Bean(autowireCandidate = false)
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ActionRequest newActionRequest(ElasticRequest indexOperation) {
        ActionRequest actionRequest = null;
        if (null != indexOperation) {
            switch (indexOperation) {
                case CREATE:
                    actionRequest = new IndexRequest(elasticConfig.getEsIndexName());
                    break;
                case UPDATE:
                    actionRequest = new UpdateRequest().index(elasticConfig.getEsIndexName());
                    break;
                case DELETE:
                    actionRequest = new DeleteRequest(elasticConfig.getEsIndexName());
                    break;
                case GET:
                    actionRequest = new GetRequest(elasticConfig.getEsIndexName());
                    break;
                case SEARCH:
                    actionRequest = new SearchRequest(elasticConfig.getEsIndexName());
                    break;
                case BULK:
                    actionRequest = new BulkRequest();
                    break;
                case MULTI:
                    actionRequest = new MultiGetRequest();
                    break;
                default:
                    break;
            }
        }
        return actionRequest;
    }

    @Bean(autowireCandidate = false)
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public MultiGetRequest.Item newMultiGetRequestItem(String id) {
        return new MultiGetRequest.Item(elasticConfig.getEsIndexName(), id);
    }
}
