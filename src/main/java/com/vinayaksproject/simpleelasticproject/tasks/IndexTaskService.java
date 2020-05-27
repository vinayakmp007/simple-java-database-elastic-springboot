/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vinayaksproject.simpleelasticproject.tasks;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinayaksproject.simpleelasticproject.JobServerConfig;
import com.vinayaksproject.simpleelasticproject.dao.IndexTaskDAO;
import com.vinayaksproject.simpleelasticproject.entity.IndexTaskEntry;
import com.vinayaksproject.simpleelasticproject.enums.JobStatus;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * This class implements TaskService
 *
 * @author vinayak
 */
@Service
public class IndexTaskService implements TaskService {

    private IndexTaskDAO indexTaskDAO;

    private JobServerConfig jobServer;

    private TaskFactory taskFactory;

    private ObjectMapper defaultObjectMapper;
    private ThreadPoolExecutor executor;
    private ConcurrentHashMap<Integer, Future> taskMap;

    @Autowired
    public IndexTaskService(IndexTaskDAO indexTaskDAO, JobServerConfig jobServer) {
        this.indexTaskDAO = indexTaskDAO;
        this.jobServer = jobServer;
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(jobServer.getThreads() == 0 ? 1 : jobServer.getThreads());
        taskMap = new ConcurrentHashMap();
    }

    @Override
    public boolean lockTasktoServer(com.vinayaksproject.simpleelasticproject.entity.IndexTaskEntry task) {
        getIndexTaskDAO().lockTaskforServer(getJobServer().getName(), task.getId(), JobStatus.CREATED);
        Optional<IndexTaskEntry> updatedTask = getIndexTaskDAO().findById(task.getId());
        return updatedTask.isPresent() && getJobServer().getName().equals(updatedTask.get().getServerName());
    }

    @Override
    public List<com.vinayaksproject.simpleelasticproject.entity.IndexTaskEntry> getAvailableTasks(int maxno) {
        return getIndexTaskDAO().findByStatus(JobStatus.CREATED, PageRequest.of(0, maxno, Sort.by("Id")));
    }

    @Override
    public Task generateExecutableTask(com.vinayaksproject.simpleelasticproject.entity.IndexTaskEntry task) {
        try {
            return getTaskFactory().NewIndexTask(task);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(IndexTaskService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public void executeTask(Task task) {

        Future<IndexTaskEntry> result = getExecutor().submit(getTaskFactory().NewIndexTaskExecutorImpl(task, getIndexTaskDAO().findById(task.getTaskid()).get()));
        getTaskMap().put(task.getTaskid(), result);
    }

    @Override
    public void pollForTasks() {
        List<IndexTaskEntry> newTaskEntries = getAvailableTasks(25);
        newTaskEntries.stream().filter((entry) -> (lockTasktoServer(entry))).map((entry) -> getIndexTaskDAO().findById(entry.getId())).map((updatedTask) -> generateExecutableTask(updatedTask.get())).forEachOrdered((newTask) -> {
            executeTask(newTask);
        });

    }

    /**
     * @return the indexTaskDAO
     */
    protected IndexTaskDAO getIndexTaskDAO() {
        return indexTaskDAO;
    }

    /**
     * @param indexTaskDAO the indexTaskDAO to set
     */
    protected void setIndexTaskDAO(IndexTaskDAO indexTaskDAO) {
        this.indexTaskDAO = indexTaskDAO;
    }

    /**
     * @return the jobServer
     */
    protected JobServerConfig getJobServer() {
        return jobServer;
    }

    /**
     * @param jobServer the jobServer to set
     */
    protected void setJobServer(JobServerConfig jobServer) {
        this.jobServer = jobServer;
    }

    /**
     * @return the taskFactory
     */
    protected TaskFactory getTaskFactory() {
        return taskFactory;
    }

    /**
     * @param taskFactory the taskFactory to set
     */
    @Autowired
    protected void setTaskFactory(TaskFactory taskFactory) {
        this.taskFactory = taskFactory;
    }

    /**
     * @return the defaultObjectMapper
     */
    protected ObjectMapper getDefaultObjectMapper() {
        return defaultObjectMapper;
    }

    /**
     * @param defaultObjectMapper the defaultObjectMapper to set
     */
    protected void setDefaultObjectMapper(ObjectMapper defaultObjectMapper) {
        this.defaultObjectMapper = defaultObjectMapper;
    }

    /**
     * @return the executor
     */
    protected ThreadPoolExecutor getExecutor() {
        return executor;
    }

    /**
     * @param executor the executor to set
     */
    protected void setExecutor(ThreadPoolExecutor executor) {
        this.executor = executor;
    }

    /**
     * @return the taskMap
     */
    protected ConcurrentHashMap<Integer, Future> getTaskMap() {
        return taskMap;
    }

    /**
     * @param taskMap the taskMap to set
     */
    protected void setTaskMap(ConcurrentHashMap<Integer, Future> taskMap) {
        this.taskMap = taskMap;
    }

}
