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
import com.vinayaksproject.simpleelasticproject.entity.TaskEntry;
import com.vinayaksproject.simpleelasticproject.enums.IndexJobType;
import com.vinayaksproject.simpleelasticproject.enums.JobStatus;
import com.vinayaksproject.simpleelasticproject.enums.ParameterFieldNames;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public boolean lockTasktoServer(com.vinayaksproject.simpleelasticproject.entity.TaskEntry task) {
        getIndexTaskDAO().lockTaskforServer(getJobServer().getName(), task.getId(), JobStatus.CREATED);
        Optional<TaskEntry> updatedTask = getIndexTaskDAO().findById(task.getId());
        return updatedTask.isPresent() && getJobServer().getName().equals(updatedTask.get().getServerName());
    }

    @Override
    public List<com.vinayaksproject.simpleelasticproject.entity.TaskEntry> getAvailableTasks(int maxno) {
        return getIndexTaskDAO().findByStatus(JobStatus.CREATED, PageRequest.of(0, maxno, Sort.by("Id")));
    }

    @Override
    public AbstractTask generateExecutableTask(com.vinayaksproject.simpleelasticproject.entity.TaskEntry task) {
        try {
            return getTaskFactory().NewIndexTask(task);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(IndexTaskService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public void executeTask(AbstractTask task) {

        Future<TaskEntry> result = getExecutor().submit(getTaskFactory().NewIndexTaskExecutorImpl(task, getIndexTaskDAO().findById(task.getTaskid()).get()));
        getTaskMap().put(task.getTaskid(), result);
    }

    @Override
    public void pollForTasks() {
        List<TaskEntry> newTaskEntries = getAvailableTasks(25);
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

    @Override
    public int createTaskEntry(IndexJobType jobType, Map parameterMap) {
        TaskEntry taskEntry = new TaskEntry();
        Map argumentMap = new HashMap<String, Object>();
        switch (jobType) {
            case FULL_INDEX:

                break;
            case UPDATE_INDEX:
                long lastExecutedTime;
                TaskEntry lastSuccessfulJob = indexTaskDAO.findLatestOfJobTypeAndStatus(IndexJobType.UPDATE_INDEX, JobStatus.SUCCESSFUL);
                if (lastSuccessfulJob == null) {
                    lastSuccessfulJob = indexTaskDAO.findLatestOfJobTypeAndStatus(IndexJobType.FULL_INDEX, JobStatus.SUCCESSFUL);
                }
                lastExecutedTime = (lastSuccessfulJob != null)
                        ? lastSuccessfulJob.getCreationDate().getTime()
                        : Timestamp.from(Instant.MIN).getTime();

                argumentMap.put(ParameterFieldNames.lastIndexTime, new Timestamp(lastExecutedTime));
                break;
            case INSTANT_UPDATE:
                List<Integer> suggestionList = new ArrayList<>();
                if (parameterMap != null && !parameterMap.containsKey("idList") || parameterMap.get("idList") == null) {
                    throw new IllegalArgumentException("The argument  map didn't contain list of ids for Instant Update Task");
                }
                List<Integer> itemList = (List) parameterMap.get("idList");
                for (Integer i : itemList) {
                    if (i == null) {
                        continue;
                    }
                    suggestionList.add(i);
                }
                argumentMap.put(ParameterFieldNames.suggestionids, suggestionList);
                break;
            default:
                throw new IllegalArgumentException("The jobtype " + jobType + " is not supported");

        }

        try {
            taskEntry.setParameters(defaultObjectMapper.writeValueAsString(argumentMap));
        } catch (JsonProcessingException ex) {
            Logger.getLogger(IndexTaskService.class.getName()).log(Level.SEVERE, "JsonProcessing failed", ex);
            throw new IllegalArgumentException("converting map to json failed for " + parameterMap, ex);
        }
        taskEntry.setTaskType(jobType);
        taskEntry.setStatus(JobStatus.CREATED);
        taskEntry = indexTaskDAO.save(taskEntry);
        return taskEntry.getId();
    }

}
