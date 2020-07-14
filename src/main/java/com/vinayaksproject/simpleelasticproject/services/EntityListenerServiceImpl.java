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
import com.vinayaksproject.simpleelasticproject.entity.EntityAudit;
import com.vinayaksproject.simpleelasticproject.services.events.EntityChangeEvent;
import com.vinayaksproject.simpleelasticproject.services.events.EntityChangeEventPublisher;
import javax.persistence.PostPersist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation class for EntityListenerServiceImpl
 *
 * @author vinayak
 */
@Service
public class EntityListenerServiceImpl implements EntityListenerService {

    @Autowired
    private EntityChangeEventPublisher publisher;
    @Autowired
    private JobServerConfig config;

    @Override
    public void entityPrePersist(EntityAudit entity) {
        if (publisher != null) {
            publisher.publishEntityChangeEvent(this, config.getName(), entity, EntityChangeEvent.EntityChangeEventType.PRE_INSERT);
        }
    }

    @Override
    public void entityPostPersist(EntityAudit entity) {
        if (publisher != null) {
            publisher.publishEntityChangeEvent(this, config.getName(), entity, EntityChangeEvent.EntityChangeEventType.POST_INSERT);
        }
    }

    @Override
    public void entityPostLoad(EntityAudit entity) {
        if (publisher != null) {
            publisher.publishEntityChangeEvent(this, config.getName(), entity, EntityChangeEvent.EntityChangeEventType.POST_LOAD);
        }
    }

    @Override
    public void entityPreUpdate(EntityAudit entity) {
        if (publisher != null) {
            publisher.publishEntityChangeEvent(this, config.getName(), entity, EntityChangeEvent.EntityChangeEventType.PRE_UPDATE);
        }
    }

    @Override
    public void entityPostUpdate(EntityAudit entity) {
        if (publisher != null) {
            publisher.publishEntityChangeEvent(this, config.getName(), entity, EntityChangeEvent.EntityChangeEventType.POST_UPDATE);
        }
    }

    @Override
    public void entityPreRemove(EntityAudit entity) {
        if (publisher != null) {
            publisher.publishEntityChangeEvent(this, config.getName(), entity, EntityChangeEvent.EntityChangeEventType.PRE_DELETE);
        }
    }

    @Override
    public void entityPostRemove(EntityAudit entity) {
        if (publisher != null) {
            publisher.publishEntityChangeEvent(this, config.getName(), entity, EntityChangeEvent.EntityChangeEventType.POST_DELETE);
        }
    }

}
