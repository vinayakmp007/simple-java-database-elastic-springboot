/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vinayaksproject.simpleelasticproject.dao.iterator;

import com.vinayaksproject.simpleelasticproject.dao.SuggestionDAO;
import com.vinayaksproject.simpleelasticproject.utils.SliceIterator;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

/**This gives an iterator to access all Suggestion elements
 *
 * @author vinayak
 */

public final class NewListSuggestionIter extends SliceIterator{
 protected List<Integer> newList;   
 
    public SuggestionDAO baseDAO;
    
    public NewListSuggestionIter(SuggestionDAO suggestionDAO,List<Integer> newList,Pageable initialPage){
      super(initialPage);
      this.baseDAO=suggestionDAO;
      this.newList=newList;
    }
    @Override
    protected Slice daoFunction(Pageable nextPageable) {
       return getBaseDAO().findByIdIn(getNewList(),nextPageable);
    }

    /**
     * @return the baseDAO
     */
    public SuggestionDAO getBaseDAO() {
        return baseDAO;
    }

    /**
     * @param baseDAO the baseDAO to set
     */
    public void setBaseDAO(SuggestionDAO baseDAO) {
        this.baseDAO = baseDAO;
    }

    /**
     * @return the newList
     */
    public List<Integer> getNewList() {
        return newList;
    }

    /**
     * @param newList the newList to set
     */
    public void setNewList(List<Integer> newList) {
        this.newList = newList;
    }
    
}
