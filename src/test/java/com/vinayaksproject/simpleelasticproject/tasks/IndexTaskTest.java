/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vinayaksproject.simpleelasticproject.tasks;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinayaksproject.simpleelasticproject.JobServerConfig;
import com.vinayaksproject.simpleelasticproject.dao.ElasticSuggestionDAO;
import com.vinayaksproject.simpleelasticproject.dao.SuggestionDAO;
import com.vinayaksproject.simpleelasticproject.dao.iterator.FullActiveSuggestionIter;
import com.vinayaksproject.simpleelasticproject.dao.iterator.NewListSuggestionIter;
import com.vinayaksproject.simpleelasticproject.dao.iterator.NewSuggestionIter;
import com.vinayaksproject.simpleelasticproject.entity.IndexTaskEntry;
import com.vinayaksproject.simpleelasticproject.entity.Suggestion;
import com.vinayaksproject.simpleelasticproject.enums.ParameterFieldNames;
import com.vinayaksproject.simpleelasticproject.tasks.exceptions.TaskFailedException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isA;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 *
 * @author vinayak
 */
@SpringBootTest
@ActiveProfiles("test")
public class IndexTaskTest {

    int taskno = 0;
    @Autowired
    TaskFactory factory;
    @Autowired
    ObjectMapper defaultObjectMapper;

    @Mock
    SuggestionDAO suggestionDAO;

    @Mock
    JobServerConfig jobConfig;

    @Mock
    ElasticSuggestionDAO elasticDAO;

    public IndexTaskTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() throws JsonProcessingException {
        MockitoAnnotations.initMocks(this);

    }

    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of initialize method, of class IndexTask.
     *
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    @Test
    public void testInitialize() throws JsonProcessingException {
        IndexTask task = createSuggestionIdTAsk();
        task.initialize();
        assertTrue(task.getDaoIterator() instanceof NewListSuggestionIter);
        task = createFullIndexTask();
        task.initialize();
        assertTrue(task.getDaoIterator() instanceof FullActiveSuggestionIter);
        task = createUpdateIndexTask();
        task.initialize();
        assertTrue(task.getDaoIterator() instanceof NewSuggestionIter);

    }

    IndexTask createSuggestionIdTAsk() {

        IndexTaskEntry taskEntry = new IndexTaskEntry();
        taskEntry.setId(taskno++);
        Map map = new HashMap();
        List<Integer> id = new ArrayList();
        id.add(1);
        id.add(2);
        id.add(3);
        map.put(ParameterFieldNames.suggestionids, id);
        IndexTask task = null;
        try {
            taskEntry.setParameters(defaultObjectMapper.writeValueAsString(map));
            task = factory.NewIndexTask(taskEntry);
        } catch (JsonProcessingException ex) {
            fail("Parse exception occured");
        }

        return task;
    }

    IndexTask createFullIndexTask() throws JsonProcessingException {

        IndexTaskEntry taskEntry = new IndexTaskEntry();
        taskEntry.setId(taskno++);
        Map map = new HashMap();
        IndexTask task = null;
        try {
            taskEntry.setParameters(defaultObjectMapper.writeValueAsString(map));
            task = factory.NewIndexTask(taskEntry);
        } catch (JsonProcessingException ex) {
            fail("Parse exception occured");
        }

        return task;
    }

    IndexTaskEntry getNewTaskEntry(String map) {
        IndexTaskEntry taskEntry = new IndexTaskEntry();
        taskEntry.setId(taskno++);
        taskEntry.setParameters(map);
        return taskEntry;
    }

    IndexTask createUpdateIndexTask() throws JsonProcessingException {

        IndexTaskEntry taskEntry = new IndexTaskEntry();
        taskEntry.setId(taskno++);
        Map map = new HashMap();
        IndexTask task = null;
        map.put(ParameterFieldNames.lastIndexTime, new Timestamp(System.currentTimeMillis()));
        try {
            taskEntry.setParameters(defaultObjectMapper.writeValueAsString(map));
            task = factory.NewIndexTask(taskEntry);
        } catch (JsonProcessingException ex) {
            fail("PArse failed");
        }

        return task;
    }

    /**
     * Test of start method, of class IndexTask.
     *
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    @Test
    public void testStart() throws JsonProcessingException, IOException {
        IndexTask task1 = createSuggestionIdTAsk();

        IndexTask task2 = createFullIndexTask();
        task2.initialize();

        IndexTask task3 = createUpdateIndexTask();
        task3.initialize();

        try {
            task1.start();
            fail("Exception was not thrown");
        } catch (Exception ex) {
            System.out.println(ex);
            assertTrue(ex instanceof TaskFailedException);
        }
        try {
            task1.initialize();
            task1.start();
          
        } catch (Exception ex) {
            System.out.println(ex);
             fail("Exception was  thrown");
        }
        try {
            task2.initialize();
            task2.start();
             
        } catch (Exception ex) {

              fail("Exception was  thrown");
        }
        try {
            task3.initialize();
            task3.start();
           
        } catch (Exception ex) {

             fail("Exception was  thrown");
        }
        task1.initialize();
        task1.setElasticDao(elasticDAO);
        task1.setJobDetails(jobConfig);
        List list = new ArrayList();
        for (int i = 0; i < 1000; i++) {
            Suggestion temp = new Suggestion();
            temp.setSuggestion("Suggestion" + i);
            temp.setDeleted(false);
            list.add(temp);
        }

        int totalHits = 0;
        for (int maxCount = 1; maxCount < 100; maxCount++) {
            when(jobConfig.getBulkDocCount()).thenReturn(maxCount);
            task1.setDaoIterator(list.iterator());
            doNothing().when(elasticDAO).bulkAPI(isA(List.class));
            try {

                task1.start();
            } catch (Exception ex) {

                 fail("Exception was  thrown");
            }
            totalHits += list.size() / maxCount + ((list.size() % maxCount == 0) ? 0 : 1);
            verify(elasticDAO, times(totalHits)).bulkAPI(isA(List.class));
        }

    }

//    /**
//     * Test of cancel method, of class IndexTask.
//     */
//    @Test
//    public void testCancel() {
//        System.out.println("cancel");
//        IndexTask instance = null;
//        instance.cancel();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
}
