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
public enum QueryFields {
    OPERATION("operation", String.class),
    SEARCH_STRING("search", String.class),
    HIGHLIGHT("highlight", Boolean.class),
    SEARCH_FIELD("field", String.class);

    private final String name;
    private final Class fieldType;

    QueryFields(String name, Class fieldType) {
        this.name = name;
        this.fieldType = fieldType;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the fieldType
     */
    public Class getFieldType() {
        return fieldType;
    }

}
