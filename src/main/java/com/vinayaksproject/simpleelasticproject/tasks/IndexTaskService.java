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

    @Autowired
    IndexTaskDAO indexTaskDAO;
    @Autowired
    JobServerConfig jobServer;
    @Autowired
    TaskFactory taskFactory;
    @Autowired
    ObjectMapper defaultObjectMapper;
    ThreadPoolExecutor executor;
    ConcurrentHashMap<Integer, Future> taskMap;

    public IndexTaskService(IndexTaskDAO indexTaskDAO, JobServerConfig jobServer) {
        this.indexTaskDAO = indexTaskDAO;
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(jobServer.getThreads());
        taskMap = new ConcurrentHashMap();
    }

    @Override
    public boolean lockTasktoServer(com.vinayaksproject.simpleelasticproject.entity.IndexTaskEntry task) {
        indexTaskDAO.lockTaskforServer(jobServer.getName(), task.getId(), JobStatus.CREATED);
        Optional<IndexTaskEntry> updatedTask = indexTaskDAO.findById(task.getId());
        return updatedTask.isPresent() && jobServer.getName().equals(updatedTask.get().getServerName());
    }

    @Override
    public List<com.vinayaksproject.simpleelasticproject.entity.IndexTaskEntry> getAvailableTasks(int maxno) {
        return indexTaskDAO.findByStatus(JobStatus.CREATED, PageRequest.of(0, maxno, Sort.by("Id")));
    }

    @Override
    public Task generateExecutableTask(com.vinayaksproject.simpleelasticproject.entity.IndexTaskEntry task) {
        try {
            return taskFactory.NewIndexTask(task);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(IndexTaskService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public void executeTask(Task task) {

       Future<IndexTaskEntry> result = executor.submit(taskFactory.NewIndexTaskExecutorImpl(task, indexTaskDAO.findById(task.getTaskid()).get()));
       taskMap.put(task.getTaskid(), result);
    }

    @Override
    public void pollForTasks() {
        List<IndexTaskEntry> newTaskEntries = getAvailableTasks(25);
        newTaskEntries.stream().filter((entry) -> (lockTasktoServer(entry))).map((entry) -> indexTaskDAO.findById(entry.getId())).map((updatedTask) -> generateExecutableTask(updatedTask.get())).forEachOrdered((newTask) -> {
            executeTask(newTask);
        });

    }

}
