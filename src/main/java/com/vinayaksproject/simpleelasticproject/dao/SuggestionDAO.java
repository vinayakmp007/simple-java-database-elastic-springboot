/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vinayaksproject.simpleelasticproject.dao;

import com.vinayaksproject.simpleelasticproject.entity.Suggestion;
import java.sql.Timestamp;
import java.util.Collection;
import javax.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *This is the basic repository class for CRUD operations on Suggestions entity
 * @author vinayak
 */
@Repository
public interface SuggestionDAO extends CrudRepository<Suggestion, Integer> {
    @Modifying
    @Query("DELETE FROM suggestion")
    @Transactional
    public void deleteAllInSingleQuery();
    
    Slice<Suggestion> findByDeletedFalse(Pageable pageable);
    Slice<Suggestion> findBylastUpdateDateAfterAndDeletedFalse(Timestamp lastUpdateDate, Pageable pageable);
    Slice<Suggestion> findBylastUpdateDateAfterAndDeletedTrue(Timestamp lastUpdateDate, Pageable pageable);
    Slice<Suggestion> findByIdIn(Collection<Integer> ids, Pageable pageable);
     Slice<Suggestion> findBylastUpdateDateAfter(Timestamp lastUpdateDate, Pageable pageable);
    
    
}
