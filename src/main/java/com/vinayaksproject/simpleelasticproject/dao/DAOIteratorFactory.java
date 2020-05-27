/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vinayaksproject.simpleelasticproject.dao;

import com.vinayaksproject.simpleelasticproject.dao.iterator.FullActiveSuggestionIter;
import com.vinayaksproject.simpleelasticproject.dao.iterator.NewActiveSuggestionIter;
import com.vinayaksproject.simpleelasticproject.dao.iterator.NewInactiveSuggestionIter;
import com.vinayaksproject.simpleelasticproject.dao.iterator.NewListSuggestionIter;
import com.vinayaksproject.simpleelasticproject.dao.iterator.NewSuggestionIter;
import com.vinayaksproject.simpleelasticproject.utils.SliceIterator;
import java.sql.Timestamp;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author vinayak
 */
@Configuration
public class DAOIteratorFactory {

    @Autowired
    SuggestionDAO suggestionDAO;

    @Bean(autowireCandidate = false)
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public SliceIterator NewSuggesionIterator(Boolean isDeleted, Timestamp startDate, List<Integer> itemIds, Pageable initialPage) {
        SliceIterator iterator = null;
        if (itemIds != null) {
            iterator = new NewListSuggestionIter(suggestionDAO, itemIds, initialPage);
        } else if (startDate != null && isDeleted != null) {
            if (isDeleted) {
                iterator = new NewInactiveSuggestionIter(suggestionDAO, startDate, initialPage);
            } else {
                iterator = new NewActiveSuggestionIter(suggestionDAO, startDate, initialPage);
            }

        } else if (startDate != null && isDeleted == null) {
            iterator = new NewSuggestionIter(suggestionDAO, startDate, initialPage);
        } else {
            iterator = new FullActiveSuggestionIter(suggestionDAO, initialPage);
        }
        return iterator;
    }

}
