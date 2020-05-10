/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vinayaksproject.simpleelasticproject.dao;

import com.vinayaksproject.simpleelasticproject.entity.IndexTaskEntry;
import com.vinayaksproject.simpleelasticproject.enums.JobStatus;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author vinayak
 */
@Repository
public interface IndexTaskDAO extends CrudRepository<IndexTaskEntry, Integer> {
   List<IndexTaskEntry> findByStatus(JobStatus statusm,Pageable page);
    @Modifying
    @Query("DELETE FROM indextask")
    @Transactional
    public void deleteAllInSingleQuery();
    
    @Modifying
    @Query("update indextask set version=version+1,serverName=:serverName   where id=:taskId and status=:status")
    @Transactional
    public void lockTaskforServer(String serverName,Integer taskId,JobStatus status);
   
    
    
    
}
