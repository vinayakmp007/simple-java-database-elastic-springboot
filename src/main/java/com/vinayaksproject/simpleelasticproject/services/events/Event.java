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

import java.sql.Timestamp;
import java.time.Instant;
import javax.persistence.MappedSuperclass;
import org.springframework.context.ApplicationEvent;

/**
 * This is the top class for all custom events.
 *
 * @author vinayak
 */
@MappedSuperclass
public abstract class Event extends ApplicationEvent {

    private final Timestamp generationTime;
    private final String fromServer;

    public Event(Object source, String fromServer) {
        super(source);
        generationTime = new Timestamp(Instant.now().toEpochMilli());
        this.fromServer = fromServer;
    }

    public Timestamp getGenerationTime() {
        return new Timestamp(generationTime.getTime());
    }

    public String getFromServer() {
        return fromServer;
    }

}
