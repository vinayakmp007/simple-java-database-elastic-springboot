/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vinayaksproject.simpleelasticproject.query;

import org.elasticsearch.search.builder.SearchSourceBuilder;

/**This is the interface for all Elastic query classes
 *
 * @author vinayak
 */
public abstract class ElasticQuery {

    public abstract boolean containsQuery();

    public abstract boolean containsAggregration();

    public abstract boolean containsHighLight();

    public abstract SearchSourceBuilder getSearchSourceBuilder();

}
