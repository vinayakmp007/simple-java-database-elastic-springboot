/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vinayaksproject.simpleelasticproject.query;

import java.util.Map;

/**
 * This class is composition class to hold the entity document and related info
 * like highlights
 *
 * @author vinayak
 */
public final class ElasticEntityResultRow<T> {

    final T object;
    final Map<String, String[]> highlight;

    public static final <T> ElasticEntityResultRow<T> newInstance(T entity, Map<String, String[]> highlight) {
        return new ElasticEntityResultRow<>(entity, highlight);
    }

    private ElasticEntityResultRow(T entity, Map<String, String[]> highlight) {
        this.object = entity;
        this.highlight = highlight;
    }

    public T getObject() {
        return object;
    }

    public Map<String, String[]> getHighlight() {
        return highlight;
    }

}
