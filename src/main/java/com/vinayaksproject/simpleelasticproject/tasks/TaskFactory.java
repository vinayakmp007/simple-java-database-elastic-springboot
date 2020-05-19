/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vinayaksproject.simpleelasticproject.tasks;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinayaksproject.simpleelasticproject.JobServerConfig;
import com.vinayaksproject.simpleelasticproject.dao.DAOIteratorFactory;
import com.vinayaksproject.simpleelasticproject.dao.SuggestionDocumentDAO;
import com.vinayaksproject.simpleelasticproject.entity.IndexTaskEntry;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author vinayak
 */
@Configuration
public class TaskFactory {
    @Autowired
    ObjectMapper defaultObjectMapper;
    @Autowired
    Pageable initialPage;
    @Autowired
    SuggestionDocumentDAO elasticDAO;
    @Autowired
    DAOIteratorFactory iterFactory;
    @Autowired
    JobServerConfig jobDetails;
    
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public IndexTask NewIndexTask(IndexTaskEntry taskMetaData) throws JsonProcessingException {
        Map paramsMap = defaultObjectMapper.readValue(taskMetaData.getParameters(), Map.class);
       IndexTask task=new IndexTask(taskMetaData.getId(), paramsMap);
         return task; 
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public IndexTaskExecutorImpl NewIndexTaskExecutorImpl(Taskable task, IndexTaskEntry taskMetaData) {

        return new IndexTaskExecutorImpl(task, taskMetaData);

    }
}
