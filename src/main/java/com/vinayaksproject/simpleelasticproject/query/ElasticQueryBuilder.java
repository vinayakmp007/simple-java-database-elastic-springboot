/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vinayaksproject.simpleelasticproject.query;

import java.util.Map;
import org.apache.commons.lang3.builder.Builder;

/**
 * This is the interface for builder classes for elasticQueryBuilder
 *
 * @author vinayak
 */
public interface ElasticQueryBuilder<T> extends Builder<T> {

    ElasticQueryBuilder<T> buildQuery(Map<String, Object> queryMap);

    ElasticQueryBuilder<T> buildAggregrationQuery(Map<String, Object> queryMap);

    ElasticQueryBuilder<T> buildHightLight(Map<String, Object> queryMap);

    ElasticQueryBuilder<T> buildSort(Map<String, Object> queryMap);
}
