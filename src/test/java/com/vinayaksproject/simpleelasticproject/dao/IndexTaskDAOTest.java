/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vinayaksproject.simpleelasticproject.dao;

import com.vinayaksproject.simpleelasticproject.JobServer;
import com.vinayaksproject.simpleelasticproject.entity.IndexTask;
import com.vinayaksproject.simpleelasticproject.enums.IndexJobType;
import com.vinayaksproject.simpleelasticproject.enums.JobStatus;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

/**
 *
 * @author vinayak
 */
@SpringBootTest   
public class IndexTaskDAOTest {
    @Autowired
    private IndexTaskDAO instance;
    private List itemList;
    
    @Autowired
    JobServer jobServer;
    public IndexTaskDAOTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
         instance.deleteAllInSingleQuery();
        itemList = new ArrayList<>();
        for(int i=0;i<3;i++){
            IndexTask temp = new IndexTask();
            temp.setDetails("IndexTask"+i);
            temp.setTaskType(IndexJobType.FULL_INDEX);
            itemList.add(instance.save(temp));
        }
         for(int i=0;i<15;i++){
            IndexTask temp = new IndexTask();
            temp.setDetails("IndexTask"+i);
            temp.setTaskType(IndexJobType.UPDATE_INDEX);
            itemList.add(instance.save(temp));
        }
        for(int i=0;i<25;i++){
            IndexTask temp = new IndexTask();
            temp.setDetails("IndexTask"+i);
            temp.setTaskType(IndexJobType.INSTANT_UPDATE);
            itemList.add(instance.save(temp));
        } 
    }
    
    @AfterEach
    public void tearDown() {
        instance.deleteAllInSingleQuery();
    }

    /**
     * Test of findByStatus method, of class IndexTaskDAO.
     */
    @Test
    public void testTransaction(){
        
        List<IndexTask> result = instance.findByStatus(JobStatus.CREATED, PageRequest.of(0, 10, Sort.by("id")));
        
      
       for(IndexTask task:result){
           task.setServerName(jobServer.getName());
           instance.lockTaskforServer("aNotherServer", task.getId(), JobStatus.CREATED);
           try{
           instance.save(task);
           fail("An ObjectOptimisticLockingFailureException shouldbe thrown");
           }
           catch(Exception ex){
               assertTrue(ex instanceof ObjectOptimisticLockingFailureException);
           }
       }

    }

    
    
}
