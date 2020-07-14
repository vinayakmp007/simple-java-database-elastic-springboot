/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vinayaksproject.simpleelasticproject.entity;

import com.vinayaksproject.simpleelasticproject.services.EntityListenerServiceImpl;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * This is abstract class to update created and last modified date for all
 * entities inheriting this class class will date audit fields and will
 * automatically updated when these entities are persisted into the database.
 *
 * @author vinayak
 */
@MappedSuperclass
@EntityListeners({AuditingEntityListener.class, EntityListenerServiceImpl.class})
public abstract class EntityAudit implements Serializable, Cloneable {

    @CreationTimestamp
    protected Timestamp creationDate;

    @UpdateTimestamp
    protected Timestamp lastUpdateDate;

    protected boolean deleted;
    @Version
    protected Integer version;

    /**
     * @return the creationDate
     */
    public Timestamp getCreationDate() {
        return creationDate;
    }

    /**
     * @param creationDate the creationDate to set
     */
    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * @return the lastUpdateDate
     */
    public Timestamp getLastUpdateDate() {
        return lastUpdateDate;
    }

    /**
     * @param lastUpdateDate the lastUpdateDate to set
     */
    public void setLastUpdateDate(Timestamp lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    /**
     * @return the deleted
     */
    public boolean isDeleted() {
        return deleted;
    }

    /**
     * @param deleted the deleted to set
     */
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * @return the version
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        EntityAudit cloned = (EntityAudit) super.clone();
        if (creationDate != null) {
            cloned.creationDate = new Timestamp(creationDate.getTime());
        }
        if (lastUpdateDate != null) {
            cloned.lastUpdateDate = new Timestamp(lastUpdateDate.getTime());
        }
        return cloned;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + Objects.hashCode(this.creationDate);
        hash = 41 * hash + Objects.hashCode(this.lastUpdateDate);
        hash = 41 * hash + (this.deleted ? 1 : 0);
        hash = 41 * hash + Objects.hashCode(this.version);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EntityAudit other = (EntityAudit) obj;
        if (this.deleted != other.deleted) {
            return false;
        }
        if (!Objects.equals(this.creationDate, other.creationDate)) {
            return false;
        }
        if (!Objects.equals(this.lastUpdateDate, other.lastUpdateDate)) {
            return false;
        }
        if (!Objects.equals(this.version, other.version)) {
            return false;
        }
        return true;
    }

}
