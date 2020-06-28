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
import com.vinayaksproject.simpleelasticproject.dao.IndexTaskDAO;
import com.vinayaksproject.simpleelasticproject.dao.SuggestionDAO;
import com.vinayaksproject.simpleelasticproject.entity.TaskEntry;
import com.vinayaksproject.simpleelasticproject.enums.IndexJobType;
import com.vinayaksproject.simpleelasticproject.enums.JobStatus;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

/**
 *
 * @author vinayak
 */
@SpringBootTest
@ActiveProfiles("test")
public class IndexTaskServiceTest {

    @Mock
    SuggestionDAO suggestionDAO;

    @Mock
    JobServerConfig jobConfig;

    @Mock
    TaskFactory taskFactory;
    @Mock
    ElasticSuggestionDAO elasticDAO;

    @Mock
    ThreadPoolExecutor executor;

    @Mock
    ObjectMapper defaultObjectMapper;

    @InjectMocks
    IndexTaskService service;

    @Mock
    IndexTaskDAO indexdao;

    public IndexTaskServiceTest() {
    }

    @BeforeAll
    public static void setUpClass() {

    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {

    }

    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of lockTasktoServer method, of class IndexTaskService.
     */
    @Test
    public void testLockTasktoServer() {
        System.out.println("lockTasktoServer");
        TaskEntry task = new TaskEntry();
        task.setId(1);
        when(jobConfig.getName()).thenReturn("testserv");
        task.setServerName("testserv");
        task.setStatus(JobStatus.CREATED);

        when(indexdao.findById(1)).thenReturn(Optional.of(task));
        when(indexdao.findById(2)).thenReturn(Optional.empty());
        assertTrue(service.lockTasktoServer(task));

        task.setId(2);
        assertFalse(service.lockTasktoServer(task));

        task.setId(1);
        task.setServerName("Anotherserver");
        assertFalse(service.lockTasktoServer(task));

    }

    /**
     * Test of getAvailableTasks method, of class IndexTaskService.
     */
    @Test
    public void testGetAvailableTasks() {
        System.out.println("getAvailableTasks");

        List<TaskEntry> list = new ArrayList();
        for (int i = 0; i < 10; i++) {
            TaskEntry task = new TaskEntry();
            task.setId(i);
            when(jobConfig.getName()).thenReturn("testserv");
            task.setServerName("testserv");
            task.setStatus(JobStatus.CREATED);
            list.add(task);
        }
        when(indexdao.findByStatus(JobStatus.CREATED, PageRequest.of(0, 10, Sort.by("Id")))).thenReturn(list);

        assertEquals(list, service.getAvailableTasks(10));
    }

    /**
     * Test of generateExecutableTask method, of class IndexTaskService.
     */
    @Test
    public void testGenerateExecutableTask() throws JsonProcessingException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        TaskEntry task = new TaskEntry();
        task.setId(1);
        when(jobConfig.getName()).thenReturn("testserv");
        task.setServerName("testserv");
        task.setStatus(JobStatus.CREATED);
        IndexTask returnTask = new IndexTask(1, new HashMap());
        when(taskFactory.NewIndexTask(task)).thenReturn(returnTask);
        service.setTaskFactory(taskFactory);
        service.generateExecutableTask(task);
        assertEquals(returnTask, service.generateExecutableTask(task));
        task.setId(2);

        Class<?> clazz = JsonProcessingException.class;
        Constructor<?> constructor;
        Class[] classArray = new Class[]{Throwable.class};
        constructor = clazz.getDeclaredConstructor(classArray);
        constructor.setAccessible(true);
        JsonProcessingException exception = (JsonProcessingException) constructor.newInstance(new Exception("test"));

        when(taskFactory.NewIndexTask(task)).thenThrow(exception);
        assertNull(service.generateExecutableTask(task));
    }

    /**
     * Test of executeTask method, of class IndexTaskService.
     */
    @Test
    public void testExecuteTask() throws InterruptedException, ExecutionException {
        System.out.println("executeTask");
        IndexTask returnTask = new IndexTask(1, new HashMap());
        TaskEntry taskEntry = new TaskEntry();
        taskEntry.setId(1);
        taskEntry.setServerName("testserv");
        taskEntry.setStatus(JobStatus.CREATED);
        when(indexdao.findById(1)).thenReturn(Optional.of(taskEntry));
        IndexTaskExecutorImpl executoir = new IndexTaskExecutorImpl(returnTask, taskEntry);
        service.setExecutor(executor);
        when(taskFactory.NewIndexTaskExecutorImpl(returnTask, taskEntry)).thenReturn(executoir);
        when(executor.submit(any(IndexTaskExecutorImpl.class))).thenReturn(CompletableFuture.completedFuture(taskEntry));
        service.setTaskFactory(taskFactory);
        service.executeTask(returnTask);
        assertEquals(taskEntry, service.getTaskMap().get(1).get());
    }

    /**
     * Test of pollForTasks method, of class IndexTaskService.
     *
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    @Test
    public void testPollForTasks() throws JsonProcessingException {
        System.out.println("pollForTasks");

        List<TaskEntry> list = new ArrayList();
        TaskEntry task;
        service.setTaskFactory(taskFactory);
        service.setExecutor(executor);
        for (int i = 0; i < 10; i++) {
            task = new TaskEntry();
            task.setId(i);
            when(jobConfig.getName()).thenReturn("testserv");
            task.setServerName("testserv");
            task.setStatus(JobStatus.CREATED);
            list.add(task);
            when(indexdao.findById(i)).thenReturn(Optional.of(task));
            IndexTask returnTask = new IndexTask(i, new HashMap());
            when(taskFactory.NewIndexTask(task)).thenReturn(returnTask);
            IndexTaskExecutorImpl temp = new IndexTaskExecutorImpl(returnTask, task);
            when(taskFactory.NewIndexTaskExecutorImpl(returnTask, task)).thenReturn(temp);

            when(executor.submit(temp)).thenReturn(CompletableFuture.completedFuture(task));
        }

        when(indexdao.findByStatus(any(JobStatus.class), any(Pageable.class))).thenReturn(list);

        service.pollForTasks();
        assertEquals(10, service.getTaskMap().size());

    }

    @Test
    public void testCreateTaskEntry() throws JsonProcessingException {
        TaskEntry taskEntry = new TaskEntry();
        Map map = new HashMap<String, Object>();
        taskEntry.setId(1);
        taskEntry.setServerName("testserv");
        taskEntry.setStatus(JobStatus.SUCCESSFUL);
        taskEntry.setCreationDate(new Timestamp(Instant.now().toEpochMilli()));
        taskEntry.setTaskType(IndexJobType.UPDATE_INDEX);
        when(indexdao.findLatestOfJobTypeAndStatus(IndexJobType.UPDATE_INDEX, JobStatus.SUCCESSFUL)).thenReturn(taskEntry);
        when(indexdao.findLatestOfJobTypeAndStatus(IndexJobType.FULL_INDEX, JobStatus.SUCCESSFUL)).thenReturn(taskEntry);
        when(indexdao.save(isA(TaskEntry.class))).thenReturn(taskEntry);
        service.setDefaultObjectMapper(defaultObjectMapper);
        assertEquals(1, service.createTaskEntry(IndexJobType.FULL_INDEX, map));
        try {
            assertEquals(1, service.createTaskEntry(IndexJobType.INSTANT_UPDATE, map));
            fail("The exception was not thrown");
        } catch (Exception ex) {
            assertTrue(ex instanceof IllegalArgumentException);
        }
        try {
            List idList = new ArrayList<Integer>();
            idList.add(324);
            idList.add(122);
            map.put("idList", idList);
            assertEquals(1, service.createTaskEntry(IndexJobType.INSTANT_UPDATE, map));
        } catch (Exception ex) {
            fail("An exception was thrown");
        }
        assertEquals(1, service.createTaskEntry(IndexJobType.UPDATE_INDEX, map));
    }
}
