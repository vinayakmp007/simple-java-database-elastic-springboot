/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vinayaksproject.simpleelasticproject;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

/**
 *
 * @author vinayak
 */
@Component
@ConfigurationProperties("jobserver")
public class JobServerConfig {
  protected String name;
 private int threads;  
 private int bulkDocCount; 
 private int  bulkSize;
 private int  pageSize;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the description
     */
    public int getThreads() {
        return threads;
    }

    /**
     * @param threads the Threads to set
     */
    public void setDescription(int threads) {
        this.setThreads(threads);
    }

    /**
     * @param threads the threads to set
     */
    public void setThreads(int threads) {
        this.threads = threads;
    }

    /**
     * @return the bulkSize
     */
    public int getBulkSize() {
        return bulkSize;
    }

    /**
     * @param bulkSize the bulkSize to set
     */
    public void setBulkSize(int bulkSize) {
        this.bulkSize = bulkSize;
    }

    /**
     * @return the bulkDocCount
     */
    public int getBulkDocCount() {
        return bulkDocCount;
    }

    /**
     * @param bulkDocCount the bulkDocCount to set
     */
    public void setBulkDocCount(int bulkDocCount) {
        this.bulkDocCount = bulkDocCount;
    }

    @Bean
     public Pageable getInitialPage(){
         return PageRequest.of(0, getPageSize());
     }

    /**
     * @return the pageSize
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * @param pageSize the pageSize to set
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
