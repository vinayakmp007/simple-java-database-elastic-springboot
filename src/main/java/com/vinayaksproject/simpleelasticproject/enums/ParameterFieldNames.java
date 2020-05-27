/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vinayaksproject.simpleelasticproject.enums;

/**
 *
 * @author vinayak
 */
public enum ParameterFieldNames {
    lastIndexTime("lastIndexTime"), suggestionids("suggestionids");
    private final String fieldName;

    ParameterFieldNames(String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * @return the fieldName
     */
    public String getFieldName() {
        return fieldName;
    }

}
