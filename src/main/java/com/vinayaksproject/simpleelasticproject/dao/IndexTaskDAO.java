/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vinayaksproject.simpleelasticproject.dao;

import com.vinayaksproject.simpleelasticproject.entity.TaskEntry;
import com.vinayaksproject.simpleelasticproject.enums.IndexJobType;
import com.vinayaksproject.simpleelasticproject.enums.JobStatus;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author vinayak
 */
@Repository
public interface IndexTaskDAO extends CrudRepository<TaskEntry, Integer> {

    List<TaskEntry> findByStatus(JobStatus statusm, Pageable page);

    @Modifying
    @Query("DELETE FROM tasks")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteAllInSingleQuery();

    @Modifying
    @Query("update tasks set version=version+1,serverName=:serverName,status=:newStatus   where id=:taskId and status=:status")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void lockTaskforServer(String serverName, Integer taskId, JobStatus status, JobStatus newStatus);

    @Query("SELECT t FROM tasks t WHERE t.id=(SELECT max(t.id) FROM tasks t WHERE t.status =:status and t.taskType=:jobType)")
    public TaskEntry findLatestOfJobTypeAndStatus(IndexJobType jobType, JobStatus status);
}
