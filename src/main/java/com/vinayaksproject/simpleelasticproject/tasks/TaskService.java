/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vinayaksproject.simpleelasticproject.tasks;

import java.util.List;

/**
 * This is service class for handling tasks
 *
 * @author vinayak
 */
public interface TaskService {

    public void executeTask(Task task);

    public boolean lockTasktoServer(com.vinayaksproject.simpleelasticproject.entity.IndexTaskEntry task);

    public List<com.vinayaksproject.simpleelasticproject.entity.IndexTaskEntry> getAvailableTasks(int maxno);

    public Task generateExecutableTask(com.vinayaksproject.simpleelasticproject.entity.IndexTaskEntry task);

    public void pollForTasks();
}
