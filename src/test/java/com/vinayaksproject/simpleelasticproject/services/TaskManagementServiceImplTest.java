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
import com.vinayaksproject.simpleelasticproject.tasks.IndexTaskService;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 *
 * @author vinayak
 */
@SpringBootTest
@ActiveProfiles("test")
public class TaskManagementServiceImplTest {

    @Mock
    IndexTaskService indexTaskService;
    @Mock
    JobServerConfig config;
    @InjectMocks
    TaskManagementServiceImpl taskManagementService;

    public TaskManagementServiceImplTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of scheduleTask method, of class TaskManagementServiceImpl.
     *
     * @throws java.lang.InterruptedException
     */
    @Test
    public void testScheduleTask() throws InterruptedException {
        System.out.println("scheduleTask");
        int j = 1;
        Map argumentMaps = new HashMap<String, Object>();
        when(config.isScheduleEnabled()).thenReturn(true);
        taskManagementService.initialize();
        assertTrue(taskManagementService.isScheduleEnabled());
        when(indexTaskService.createTaskEntry(eq(IndexJobType.FULL_INDEX), isA(Map.class))).thenReturn(1);
        taskManagementService.scheduleTask(IndexJobType.FULL_INDEX, -1, argumentMaps);
        when(indexTaskService.createTaskEntry(eq(IndexJobType.INSTANT_UPDATE), isA(Map.class))).thenReturn(2);
        taskManagementService.scheduleTask(IndexJobType.INSTANT_UPDATE, -1, argumentMaps);
        when(indexTaskService.createTaskEntry(eq(IndexJobType.UPDATE_INDEX), isA(Map.class))).thenReturn(3);
        taskManagementService.scheduleTask(IndexJobType.UPDATE_INDEX, 1000, argumentMaps);
        Thread.sleep(9000);
        verify(indexTaskService, times(10)).createTaskEntry(eq(IndexJobType.UPDATE_INDEX), isA(Map.class));
        when(config.isScheduleEnabled()).thenReturn(false);
        taskManagementService.initialize();
        try {
            taskManagementService.scheduleTask(IndexJobType.UPDATE_INDEX, 1000, argumentMaps);
            fail("An exception was not thrown");
        } catch (Exception ex) {
            assertTrue(ex instanceof IllegalStateException);

        }
    }

    /**
     * Test of schedulePollFortasks method, of class TaskManagementServiceImpl.
     */
    @Test
    public void testSchedulePollFortasks() throws InterruptedException {
        System.out.println("schedulePollFortasks");
        when(config.isPollingEnabled()).thenReturn(true);
        when(config.getPollingIntervalInMillis()).thenReturn(2000l);
        taskManagementService.initialize();
        assertTrue(taskManagementService.isPollingEnabled());
        taskManagementService.schedulePollFortasks();
        Thread.sleep(4100);
        verify(indexTaskService, times(2)).pollForTasks();
        when(config.isPollingEnabled()).thenReturn(false);
        taskManagementService.initialize();
        try {
            taskManagementService.schedulePollFortasks();
        } catch (Exception ex) {
            assertTrue(ex instanceof IllegalStateException);
        }
    }

}
