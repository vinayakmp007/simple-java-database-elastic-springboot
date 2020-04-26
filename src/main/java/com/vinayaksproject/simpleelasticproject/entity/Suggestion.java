/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vinayaksproject.simpleelasticproject.entity;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * This is the entity class to store the suggestions
 * @author vinayak
 */
@Entity(name="suggestion")
@Table(name="suggestion")
public class Suggestion extends EntityAudit {
    @Id
    @GeneratedValue
    private int id;
    
    private String suggestion;

    private int hits;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the suggestion
     */
    public String getSuggestion() {
        return suggestion;
    }

    /**
     * @param suggestion the suggestion to set
     */
    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    /**
     * @return the hits
     */
    public int getHits() {
        return hits;
    }

    /**
     * @param hits the hits to set
     */
    public void setHits(int hits) {
        this.hits = hits;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.id;
        hash = 97 * hash + Objects.hashCode(this.suggestion);
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
        final Suggestion other = (Suggestion) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.suggestion, other.suggestion)) {
            return false;
        }
        return true;
    }
    
}
