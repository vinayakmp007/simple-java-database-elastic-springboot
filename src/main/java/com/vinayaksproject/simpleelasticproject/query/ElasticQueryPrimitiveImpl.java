/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vinayaksproject.simpleelasticproject.query;

import com.vinayaksproject.simpleelasticproject.enums.QueryFields;
import java.util.Map;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;

/**This is primitive class for ElasticQuery.This implementation provides very few and primitive features.
 *
 * @author vinayak
 */
public class ElasticQueryPrimitiveImpl extends ElasticQuery {

    private final boolean containsQuery;
    private final boolean containsAggregration;
    private final boolean containsHighLight;
    private final SearchSourceBuilder searchSourceBuilder;

    public static final class ElasticQueryPrimitiveBuilder implements ElasticQueryBuilder<ElasticQueryPrimitiveImpl> {

        private boolean containsQuery;
        private boolean containsAggs;
        private boolean containsHighLights;
        private final SearchSourceBuilder searchSourceBuilder;
        private QueryBuilder queryBuilder;
        private HighlightBuilder highlightBuilder;
        public ElasticQueryPrimitiveBuilder() {
            searchSourceBuilder = new SearchSourceBuilder();
            containsQuery = false;
            containsAggs = false;
            containsHighLights = false;
        }

        @Override
        public ElasticQueryPrimitiveImpl build() {
            return new ElasticQueryPrimitiveImpl(this);
        }

        @Override
        public ElasticQueryBuilder<ElasticQueryPrimitiveImpl> buildQuery(Map<String, Object> queryMap) {
            queryBuilder = null;
            if (queryMap.containsKey(QueryFields.OPERATION.getName())) {
                String operation = (String) queryMap.get(QueryFields.OPERATION.getName());
                String field = (String) queryMap.get(QueryFields.SEARCH_FIELD.getName());
                String value = (String) queryMap.get(QueryFields.SEARCH_STRING.getName());
                if (operation == null) {
                    throw new IllegalArgumentException("The provided Operation value was null");
                }
                if (field == null) {
                    throw new IllegalArgumentException("The provided field value was null");
                }
                if (value != null) {
                    queryBuilder = QueryBuilders.matchQuery(field, value);
                } else {
                    queryBuilder = QueryBuilders.matchAllQuery();
                }
            }
            if (queryBuilder != null) {
                searchSourceBuilder.query(queryBuilder);
                containsQuery = true;
            }
            return this;
        }

        @Override
        public ElasticQueryBuilder<ElasticQueryPrimitiveImpl> buildAggregrationQuery(Map<String, Object> queryMap) {
            return this;
        }

        @Override
        public ElasticQueryBuilder<ElasticQueryPrimitiveImpl> buildHightLight(Map<String, Object> queryMap) {
            highlightBuilder = null;
            if ( null != queryMap.get(QueryFields.HIGHLIGHT.getName())&&true==(Boolean)queryMap.get(QueryFields.HIGHLIGHT.getName())) {

                String field = (String) queryMap.get(QueryFields.SEARCH_FIELD.getName());

                if (field == null) {
                    throw new IllegalArgumentException("The provided field value was null");
                }
                highlightBuilder = new HighlightBuilder();
                HighlightBuilder.Field highlightTitle
                        = new HighlightBuilder.Field(field);
                highlightTitle.highlighterType("unified");
                highlightBuilder.field(highlightTitle);
            }
            if (highlightBuilder != null) {
                searchSourceBuilder.highlighter(highlightBuilder);
                containsHighLights = true;
            }
            return this;
        }

        private boolean containsQuery() {
            return containsQuery;
        }

        private boolean containsAggregration() {
            return containsAggs;
        }

        private boolean containsHighLight() {
            return containsHighLights;
        }

        SearchSourceBuilder getsearchSourceBuilder() {

            return searchSourceBuilder;
        }

        @Override
        public ElasticQueryBuilder<ElasticQueryPrimitiveImpl> buildSort(Map<String, Object> queryMap) {
            return this;
        }
        
       QueryBuilder getQueryBuilder(){
            return queryBuilder;
        }
        
        HighlightBuilder getHighlightBuilder(){
            return highlightBuilder;
        }
        

    }

    private ElasticQueryPrimitiveImpl(ElasticQueryPrimitiveBuilder elasticQueryPrimitiveBuilder) {
        this.containsQuery = elasticQueryPrimitiveBuilder.containsQuery();
        this.containsAggregration = elasticQueryPrimitiveBuilder.containsAggregration();
        this.containsHighLight = elasticQueryPrimitiveBuilder.containsHighLight();
        this.searchSourceBuilder = elasticQueryPrimitiveBuilder.getsearchSourceBuilder();
    }

    @Override
    public boolean containsQuery() {
        return containsQuery;
    }

    @Override
    public boolean containsAggregration() {
        return containsAggregration;
    }

    @Override
    public boolean containsHighLight() {
        return containsHighLight;
    }

    @Override
    public SearchSourceBuilder getSearchSourceBuilder() {
        return searchSourceBuilder;
    }

}
