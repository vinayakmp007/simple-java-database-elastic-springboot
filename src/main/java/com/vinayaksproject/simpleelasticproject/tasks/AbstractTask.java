/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vinayaksproject.simpleelasticproject.tasks;

import java.util.Map;

/**
 * The base AbstractTask class
 *
 * @author vinayak
 */
public abstract class AbstractTask implements Taskable {

    private Map<String, Object> paramsMap;
    private final int taskid;

    AbstractTask(int taskid, Map paramsMap) {
        this.taskid = taskid;
        this.paramsMap = paramsMap;

    }

    @Override
    public void initialize() {

    }

    @Override
    public void destroy() {

    }

    /**
     * @return the paramsMap
     */
    public Map<String, Object> getParamsMap() {
        return paramsMap;
    }

    /**
     * @param paramsMap the paramsMap to set
     */
    public void setParamsMap(Map<String, Object> paramsMap) {
        this.paramsMap = paramsMap;
    }

    /**
     * @return the taskid
     */
    public int getTaskid() {
        return taskid;
    }

}
