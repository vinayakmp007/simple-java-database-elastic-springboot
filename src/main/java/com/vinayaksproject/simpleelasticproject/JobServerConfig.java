/*
/*
 * The MIT License
 *
 * Copyright 2020 vinayak.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.vinayaksproject.simpleelasticproject;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
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

    private String name;
    private int threads;
    private int bulkDocCount;
    private int bulkSize;
    private int pageSize;
    private int maxTasksToPoll;
    private boolean scheduleEnabled;
    private boolean generateScheduleTasks;
    private boolean pollingEnabled;
    private long pollingIntervalInMillis;
    private long numberOfTasksAllowed;

    public long getNumberOfTasksAllowed() {
        return numberOfTasksAllowed;
    }

    public void setNumberOfTasksAllowed(long numberOfTasksAllowed) {
        this.numberOfTasksAllowed = numberOfTasksAllowed;
    }

    public boolean isScheduleEnabled() {
        return scheduleEnabled;
    }

    public void setScheduleEnabled(boolean scheduleEnabled) {
        this.scheduleEnabled = scheduleEnabled;
    }

    public boolean isPollingEnabled() {
        return pollingEnabled;
    }

    public void setPollingEnabled(boolean pollingEnabled) {
        this.pollingEnabled = pollingEnabled;
    }

    public long getPollingIntervalInMillis() {
        return pollingIntervalInMillis;
    }

    public void setPollingIntervalInMillis(long pollingIntervalInMillis) {
        this.pollingIntervalInMillis = pollingIntervalInMillis;
    }

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
    public Pageable getInitialPage() {
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

    /**
     * @return the maxTasksToPoll
     */
    public int getMaxTasksToPoll() {
        return maxTasksToPoll;
    }

    /**
     * @param maxTasksToPoll the maxTasksToPoll to set
     */
    public void setMaxTasksToPoll(int maxTasksToPoll) {
        this.maxTasksToPoll = maxTasksToPoll;
    }

    public boolean isGenerateScheduleTasks() {
        return generateScheduleTasks;
    }

    public void setGenerateScheduleTasks(boolean generateScheduleTasks) {
        this.generateScheduleTasks = generateScheduleTasks;
    }

    @Override
    public String toString() {
        return "JobServerConfig{" + "name=" + name + ", threads=" + threads + ", bulkDocCount=" + bulkDocCount + ", bulkSize=" + bulkSize + ", pageSize=" + pageSize + ", maxTasksToPoll=" + maxTasksToPoll + ", scheduleEnabled=" + scheduleEnabled + ", generateScheduleTasks=" + generateScheduleTasks + ", pollingEnabled=" + pollingEnabled + ", pollingIntervalInMillis=" + pollingIntervalInMillis + ", numberOfTasksAllowed=" + numberOfTasksAllowed + '}';
    }



}
