/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vinayaksproject.simpleelasticproject.tasks;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinayaksproject.simpleelasticproject.entity.IndexTaskEntry;
import java.util.Map;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 *
 * @author vinayak
 */
@Configuration
public class TaskFactory {

    ObjectMapper defaultObjectMapper;

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public IndexTask NewIndexTask(IndexTaskEntry taskMetaData) {
        Map paramsMap = defaultObjectMapper.convertValue(taskMetaData.getParameters(), Map.class);
        return new IndexTask(taskMetaData.getId(), paramsMap);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public IndexTaskExecutorImpl NewIndexTaskExecutorImpl(Taskable task, IndexTaskEntry taskMetaData) {

        return new IndexTaskExecutorImpl(task, taskMetaData);

    }
}
