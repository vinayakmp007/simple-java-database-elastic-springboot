/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vinayaksproject.simpleelasticproject.dtos;

import java.util.Map;

/**
 * This class is the DTO object for elastic search queries from client
 *
 * @author vinayak
 */
public class QueryDTO extends AbstractDTO {

    Map<String, Object> query;
    String aggregrateField;
    String limit;

}
