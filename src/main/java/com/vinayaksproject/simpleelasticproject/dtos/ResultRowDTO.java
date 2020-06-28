/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vinayaksproject.simpleelasticproject.dtos;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * This call compose of entity DTO and highlight strings
 *
 * @author vinayak
 * @param <C> The entity abstract class we need to
 */
public class ResultRowDTO<C extends AbstractDTO> extends AbstractDTO {

    private C entityDTO;
    private Map<String, String[]> highlights;

    public C getEntityDTO() {
        return entityDTO;
    }

    public void setEntityDTO(C entityDTO) {
        this.entityDTO = entityDTO;
    }

    public Map<String, String[]> getHighlights() {
        return highlights;
    }

    public void setHighlights(Map<String, String[]> highlights) {
        this.highlights = highlights;
    }

    @Override
    public int hashCode() {
        int hash = 7;
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
        final ResultRowDTO<?> other = (ResultRowDTO<?>) obj;
        if (!Objects.equals(this.entityDTO, other.entityDTO)) {
            return false;
        }
        if (!Objects.equals(this.highlights.keySet(), other.highlights.keySet())) {
            return false;
        }
        if (!this.highlights.entrySet().stream().allMatch((entry) -> Arrays.equals(entry.getValue(), other.getHighlights().get(entry.getKey())))) {
            return false;
        }
        return true;
    }

}
