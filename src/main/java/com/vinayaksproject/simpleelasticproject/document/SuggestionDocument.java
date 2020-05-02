/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vinayaksproject.simpleelasticproject.document;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.sql.Timestamp;
import java.util.Objects;
import javax.persistence.Id;

/**This is the document that will be stored in the elastic search.
 *
 * @author vinayak
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SuggestionDocument {
      @Id
    private String id;
    
    private String suggestion;

    
    private Timestamp dbCreationDate;
 
    private Timestamp dbLastUpdateDate;
  
    private Integer dbVersion;

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
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
     * @return the dbCreationDate
     */
    public Timestamp getDbCreationDate() {
        return dbCreationDate;
    }

    /**
     * @param dbCreationDate the dbCreationDate to set
     */
    public void setDbCreationDate(Timestamp dbCreationDate) {
        this.dbCreationDate = dbCreationDate;
    }

    /**
     * @return the dbLastUpdateDate
     */
    public Timestamp getDbLastUpdateDate() {
        return dbLastUpdateDate;
    }

    /**
     * @param dbLastUpdateDate the dbLastUpdateDate to set
     */
    public void setDbLastUpdateDate(Timestamp dbLastUpdateDate) {
        this.dbLastUpdateDate = dbLastUpdateDate;
    }

    /**
     * @return the dbVersion
     */
    public Integer getDbVersion() {
        return dbVersion;
    }

    /**
     * @param dbVersion the dbVersion to set
     */
    public void setDbVersion(Integer dbVersion) {
        this.dbVersion = dbVersion;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.id);
        hash = 89 * hash + Objects.hashCode(this.suggestion);
        hash = 89 * hash + Objects.hashCode(this.dbCreationDate);
        hash = 89 * hash + Objects.hashCode(this.dbLastUpdateDate);
        hash = 89 * hash + Objects.hashCode(this.dbVersion);
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
        final SuggestionDocument other = (SuggestionDocument) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.suggestion, other.suggestion)) {
            return false;
        }
        if (!Objects.equals(this.dbCreationDate, other.dbCreationDate)) {
            return false;
        }
        if (!Objects.equals(this.dbLastUpdateDate, other.dbLastUpdateDate)) {
            return false;
        }
        if (!Objects.equals(this.dbVersion, other.dbVersion)) {
            return false;
        }
        return true;
    }
    
    
    
}
