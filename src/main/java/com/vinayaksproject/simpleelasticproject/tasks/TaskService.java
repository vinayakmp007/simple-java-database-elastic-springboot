/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vinayaksproject.simpleelasticproject.tasks;

import com.vinayaksproject.simpleelasticproject.enums.IndexJobType;
import java.util.List;
import java.util.Map;

/**
 * This is service class for handling tasks
 *
 * @author vinayak
 */
public interface TaskService {

    public void executeTask(AbstractTask task);

    public boolean lockTasktoServer(com.vinayaksproject.simpleelasticproject.entity.TaskEntry task);

    public List<com.vinayaksproject.simpleelasticproject.entity.TaskEntry> getAvailableTasks(int maxno);

    public AbstractTask generateExecutableTask(com.vinayaksproject.simpleelasticproject.entity.TaskEntry task);

    public void pollForTasks();

    /**
     *
     * @param jobType
     * @param parameterMap
     * @return
     */
    public int createTaskEntry(IndexJobType jobType, Map parameterMap);
}
