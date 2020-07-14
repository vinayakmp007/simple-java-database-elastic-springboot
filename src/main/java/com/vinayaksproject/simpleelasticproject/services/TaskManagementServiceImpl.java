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
package com.vinayaksproject.simpleelasticproject.services;

import com.vinayaksproject.simpleelasticproject.JobServerConfig;
import com.vinayaksproject.simpleelasticproject.enums.IndexJobType;
import com.vinayaksproject.simpleelasticproject.tasks.TaskService;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This is the implementation class for TaskManagementService
 *
 * @author vinayak
 */
@Service
public class TaskManagementServiceImpl implements TaskManagementService {

    private static final long INITIAL_DELAY = 1000;
    private long pollingPeriod;
    TaskService indexTaskService;
    ScheduledExecutorService scheduler;
    private boolean scheduleEnabled;
    private boolean pollingEnabled;
    JobServerConfig config;

    @Autowired
    TaskManagementServiceImpl(TaskService indexTaskService, JobServerConfig config) {
        this.indexTaskService = indexTaskService;

        this.config = config;
        initialize();
    }

    final void initialize() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
            try {
                scheduler.awaitTermination(10, TimeUnit.MINUTES);
            } catch (InterruptedException ex) {
                Logger.getLogger(TaskManagementServiceImpl.class.getName()).log(Level.SEVERE, "Waiting for previus executor to shut down failed.", ex);
            }
        }
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduleEnabled = config.isScheduleEnabled();
        pollingEnabled = config.isPollingEnabled();
        pollingPeriod = config.getPollingIntervalInMillis();
    }

    @Override
    public void scheduleTask(IndexJobType jobType, long interval, Map arguments) {
        if (!this.scheduleEnabled) {
            throw new IllegalStateException("Schedule was not enabled");
        }
        if (interval < 0) {
            indexTaskService.createTaskEntry(jobType, arguments);
        } else {

            Runnable task = new Runnable() {
                @Override
                public void run() {
                    indexTaskService.createTaskEntry(jobType, arguments);
                }
            };
            scheduler.scheduleAtFixedRate(task, 0, interval, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public void schedulePollFortasks() {
        if (!this.pollingEnabled) {
            throw new IllegalStateException("Polling was not enabled");
        }
        Runnable task = new Runnable() {
            @Override
            public void run() {
                indexTaskService.pollForTasks();
            }
        };
        scheduler.scheduleAtFixedRate(task, TaskManagementServiceImpl.INITIAL_DELAY, pollingPeriod, TimeUnit.MILLISECONDS);
    }

    boolean isScheduleEnabled() {
        return scheduleEnabled;
    }

    void setScheduleEnabled(boolean scheduleEnabled) {
        this.scheduleEnabled = scheduleEnabled;
    }

    boolean isPollingEnabled() {
        return pollingEnabled;
    }

    void setPollingEnabled(boolean pollingEnabled) {
        this.pollingEnabled = pollingEnabled;
    }

}
