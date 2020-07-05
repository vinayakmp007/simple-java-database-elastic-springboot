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

import com.vinayaksproject.simpleelasticproject.entity.EntityAudit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This the event generated for all entity change event
 *
 * @author vinayak
 */
public class EntityChangeEvent extends Event {

    public enum EntityChangeEventType {
        INSERT,
        DELETE,
        UPDATE,
        MIXED

    }

    private final EntityAudit entity;
    private final EntityChangeEventType type;

    public EntityChangeEvent(Object source, String fromServer, EntityAudit entity, EntityChangeEventType type) {
        super(source, fromServer);
        EntityAudit tempEntity;
        try {
            tempEntity = (EntityAudit) entity.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(EntityChangeEvent.class.getName()).log(Level.SEVERE, "Cloning of Entity failed using the same reference", ex);
            tempEntity = entity;
        }
        this.entity = tempEntity;
        this.type = type;
    }

    public EntityAudit getEntity() {
        try {
            return (EntityAudit) entity.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(EntityChangeEvent.class.getName()).log(Level.SEVERE, "This entity does not support clone hence return null", ex);
            return null;
        }
    }

    public EntityChangeEventType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "EntityChangeEvent{" + "entity=" + entity + ", type=" + type + '}';
    }

}
