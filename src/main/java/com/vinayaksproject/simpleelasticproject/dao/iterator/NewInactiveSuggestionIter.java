/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vinayaksproject.simpleelasticproject.dao.iterator;

import com.vinayaksproject.simpleelasticproject.dao.SuggestionDAO;
import com.vinayaksproject.simpleelasticproject.utils.SliceIterator;
import java.sql.Timestamp;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

/**
 * This gives an iterator to access all Suggestion elements
 *
 * @author vinayak
 */
public final class NewInactiveSuggestionIter extends SliceIterator {

    protected Timestamp fromDate;

    public SuggestionDAO baseDAO;

    public NewInactiveSuggestionIter(SuggestionDAO suggestionDAO, Timestamp fromDate, Pageable initialPage) {
        super(initialPage);
        this.baseDAO = suggestionDAO;
        setFromDate(fromDate);
    }

    @Override
    protected Slice daoFunction(Pageable nextPageable) {
        return getBaseDAO().findBylastUpdateDateAfterAndDeletedTrue(getFromDate(), nextPageable);
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
     * @return the fromDate
     */
    public Timestamp getFromDate() {
        return fromDate;
    }

    /**
     * @param fromDate the fromDate to set
     */
    public void setFromDate(Timestamp fromDate) {
        this.fromDate = fromDate;
    }

}
