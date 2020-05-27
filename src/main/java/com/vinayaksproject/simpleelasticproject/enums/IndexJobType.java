/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vinayaksproject.simpleelasticproject.enums;

/**
 * This the enumerator for different types of index tasks FULL_INDEX is used to
 * completely copy data from database to elasticsearch. UPDATE_INDEX is used to
 * update data changes made to database into the elastic search after latest
 * FULL_INDEX or UPDATE_INDEX was executed.
 *
 * @author vinayak
 */
public enum IndexJobType {
    FULL_INDEX,
    UPDATE_INDEX,
    INSTANT_UPDATE
}
