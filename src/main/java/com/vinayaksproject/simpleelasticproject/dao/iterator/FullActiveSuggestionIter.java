/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vinayaksproject.simpleelasticproject.dao.iterator;

import com.vinayaksproject.simpleelasticproject.dao.SuggestionDAO;
import com.vinayaksproject.simpleelasticproject.utils.SliceIterator;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

/**This gives an iterator to access all Suggestion elements
 *
 * @author vinayak
 */

public final class FullActiveSuggestionIter extends SliceIterator{
    
    private SuggestionDAO suggestionDAO;

    
   
    public FullActiveSuggestionIter(SuggestionDAO suggestionDAO,Pageable initialPage){
      super();
      this.suggestionDAO=suggestionDAO;
      setSlice(daoFunction(initialPage));
      applySlice();
    }
    @Override
    protected Slice daoFunction(Pageable nextPageable) {
       return getSuggestionDAO().findByDeletedFalse(nextPageable);
    }

    /**
     * @return the suggestionDAO
     */
    public SuggestionDAO getSuggestionDAO() {
        return suggestionDAO;
    }

    /**
     * @param suggestionDAO the suggestionDAO to set
     */
    public void setSuggestionDAO(SuggestionDAO suggestionDAO) {
        this.suggestionDAO = suggestionDAO;
    }
    
}
