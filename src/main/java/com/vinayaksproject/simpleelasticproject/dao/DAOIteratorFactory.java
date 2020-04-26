/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vinayaksproject.simpleelasticproject.dao;

import com.vinayaksproject.simpleelasticproject.dao.iterator.FullActiveSuggesionIter;
import com.vinayaksproject.simpleelasticproject.dao.iterator.NewActiveSuggesionIter;
import com.vinayaksproject.simpleelasticproject.dao.iterator.NewInactiveSuggesionIter;
import com.vinayaksproject.simpleelasticproject.dao.iterator.NewListSuggesionIter;
import com.vinayaksproject.simpleelasticproject.utils.SliceIterator;
import java.sql.Timestamp;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

/**
 *
 * @author vinayak
 */
@Configuration
@Component
public class DAOIteratorFactory {
    @Autowired
    SuggestionDAO suggestionDAO;

    @Bean(autowireCandidate = false) 
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public SliceIterator NewSuggesionIterator(Boolean isDeleted,Timestamp startDate,List<Integer> itemIds,Pageable initialPage){
      SliceIterator iterator=null;
        if(itemIds!=null){
            iterator= new NewListSuggesionIter(suggestionDAO,itemIds,initialPage);
        }
        else if(startDate!=null&&isDeleted!=null){
            if(isDeleted){
               iterator=  new NewInactiveSuggesionIter(suggestionDAO,startDate,initialPage);
            }
            else{
               iterator=   new NewActiveSuggesionIter(suggestionDAO,startDate,initialPage);
            }
            
        }
        else{
             iterator= new FullActiveSuggesionIter(suggestionDAO,initialPage);
        }
        return iterator;
    }
    
}
