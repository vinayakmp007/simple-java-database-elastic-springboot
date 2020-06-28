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

import com.vinayaksproject.simpleelasticproject.enums.IndexJobType;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 * This interface manages Scheduling of tasks
 *
 * @author vinayak
 */
public interface TaskManagementService {

    /**
     * This function is used to schedule jobs that needs to be executed once or
     * periodic.
     *
     * @param jobType
     * @param interval time period in milliseconds ,if time period -1 means it
     * task be executed once only.
     * @param arguments arguments to be passed to the task
     */
    void scheduleTask(IndexJobType jobType, long interval, Map arguments);

    /**
     * poll for task periodically with the given polling interval
     *
     *
     */
    void schedulePollFortasks();

}
