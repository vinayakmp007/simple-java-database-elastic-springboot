/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vinayaksproject.simpleelasticproject.utils;

import com.vinayaksproject.simpleelasticproject.document.AbstractDocument;
import com.vinayaksproject.simpleelasticproject.dtos.AbstractDTO;
import com.vinayaksproject.simpleelasticproject.dtos.ResultRowDTO;
import com.vinayaksproject.simpleelasticproject.entity.EntityAudit;
import com.vinayaksproject.simpleelasticproject.query.ElasticEntityResultRow;

/**
 *
 * @author vinayak
 * @param <T>
 * @param <U>
 * @param <W>
 */
public interface Converter<T extends EntityAudit,U extends AbstractDTO,W extends AbstractDocument>  {

    /**This is the interface for converting entities,documents,DTOs 
     *
     * @param entity
     * @return
     */
    W EntitytoDocument(T entity);
    U DocumentoDTO(W document);
    U EntityToDTO(T entity);
    ResultRowDTO<U> ResultRowToDTO(ElasticEntityResultRow<W> resultRow);
}
