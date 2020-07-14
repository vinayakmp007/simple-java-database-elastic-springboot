/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vinayaksproject.simpleelasticproject.services;

import com.vinayaksproject.simpleelasticproject.entity.EntityAudit;

/**
 * This is the class for basic CRUD Services
 *
 * @author vinayak
 * @param <T>
 */
public interface EntityCRUDService<T extends EntityAudit> {

    T findById(int id);

    T save(T entity);

    T delete(int id);

}
