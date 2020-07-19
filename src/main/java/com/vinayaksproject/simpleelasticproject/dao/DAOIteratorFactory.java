/*
 * The MIT License
 *
 * Copyright 2020 vinayak.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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
 * This class produces iterators for access objects sequntially from database
 * without having large memory usage and avoiding count query
 *
 * @author vinayak
 */
@Configuration
public class DAOIteratorFactory {

    @Autowired
    SuggestionDAO suggestionDAO;

    /**
     * if itemIds is not null then output will only contain itemids else it will
     * return entites changed between start and end date if both are provided
     * else will provide all the non deleted items in the Database
     *
     * @param isDeleted Whether this iterator is to pick up deleted items
     * @param startDate Pick up entites modified on or after this start date
     * @param itemIds List of ids of items that needs to be queried from
     * database
     * @param initialPage Page details
     * @return An iterator which iterates through the output of the query
     */
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
