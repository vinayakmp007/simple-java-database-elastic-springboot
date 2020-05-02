/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vinayaksproject.simpleelasticproject.dao;

import com.vinayaksproject.simpleelasticproject.document.SuggestionDocument;
import com.vinayaksproject.simpleelasticproject.enums.IndexOperations;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author vinayak
 */

public interface ElasticSuggestionDAO {
    public String save(SuggestionDocument suggestionDocument) throws IOException;
    public void delete(String id) throws IOException;
    public void bulkAPI(List<SuggestionDocument> suggestionDocumentList,IndexOperations indexOperation) throws IOException;
    public SuggestionDocument get(String suggestionDocumentId) throws IOException;
    public List<SuggestionDocument> get(List<String> suggestionDocumentIds) throws IOException;
    public boolean createIndex() throws IOException;
    public boolean deleteIndex() throws IOException;
}
