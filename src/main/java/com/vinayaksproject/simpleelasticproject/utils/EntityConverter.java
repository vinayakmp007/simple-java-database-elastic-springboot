/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vinayaksproject.simpleelasticproject.utils;

import com.vinayaksproject.simpleelasticproject.document.SuggestionDocument;
import com.vinayaksproject.simpleelasticproject.entity.Suggestion;

/**
 *
 * @author vinayak
 */
public class EntityConverter {

    public static SuggestionDocument SuggestionEntitytoDocument(Suggestion suggestionEntity) {
        SuggestionDocument document = new SuggestionDocument();
        if (suggestionEntity == null) {
            throw new IllegalArgumentException("The argument Suggestion cannot be null");
        }
        document.setId(String.valueOf(suggestionEntity.getId()));
        document.setSuggestion(suggestionEntity.getSuggestion());
        document.setDbCreationDate(suggestionEntity.getCreationDate());
        document.setDbLastUpdateDate(suggestionEntity.getLastUpdateDate());
        document.setDbVersion(suggestionEntity.getVersion());
        document.setDbdeleted(suggestionEntity.isDeleted());
        return document;
    }

}
