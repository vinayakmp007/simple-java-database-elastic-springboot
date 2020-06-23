/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vinayaksproject.simpleelasticproject.query;

import java.util.List;

/**This class is used store and manipulated the results from elasticsearch server
 *
 * @author vinayak
 * @param <T>
 */
public final class ElasticResult<T> {

    List<ElasticEntityResultRow<T>> entityResults;
    long totalHits;

    private ElasticResult(List<ElasticEntityResultRow<T>> entityResults, long totalHits) {
        this.entityResults = entityResults;
        this.totalHits = totalHits;
    }

    public static final <T> ElasticResult<T> newInstance(List<ElasticEntityResultRow<T>> entityResults, long totalHits) {
        return new ElasticResult<>(entityResults, totalHits);
    }

    public List<ElasticEntityResultRow<T>> getEntityResults() {
        return entityResults;
    }

    public long getTotalHits() {
        return totalHits;
    }
    
}
