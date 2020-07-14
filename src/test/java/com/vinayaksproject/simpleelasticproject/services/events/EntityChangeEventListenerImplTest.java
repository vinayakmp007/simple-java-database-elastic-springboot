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
package com.vinayaksproject.simpleelasticproject.services.events;

import com.vinayaksproject.simpleelasticproject.entity.Suggestion;
import com.vinayaksproject.simpleelasticproject.enums.IndexJobType;
import com.vinayaksproject.simpleelasticproject.services.TaskManagementService;
import com.vinayaksproject.simpleelasticproject.services.events.EntityChangeEvent.EntityChangeEventType;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.ActiveProfiles;

/**
 *
 * @author vinayak
 */
@SpringBootTest
@ActiveProfiles("test")
public class EntityChangeEventListenerImplTest {

    @Mock
    TaskManagementService taskManagementService;
    @InjectMocks
    EntityChangeEventListenerImpl instance;

    public EntityChangeEventListenerImplTest() {
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
     * Test of onApplicationEvent method, of class
     * EntityChangeEventListenerImpl.
     */
    @Test
    public void testOnApplicationEvent() {
        System.out.println("onApplicationEvent");
        Suggestion entity = new Suggestion();
        EntityChangeEvent e = new EntityChangeEvent(this, "server", entity, EntityChangeEventType.POST_INSERT);
        instance.onApplicationEvent(e);
        verify(taskManagementService, times(1)).scheduleTask(eq(IndexJobType.INSTANT_UPDATE), eq(-1l), isA(Map.class));
    }

}
