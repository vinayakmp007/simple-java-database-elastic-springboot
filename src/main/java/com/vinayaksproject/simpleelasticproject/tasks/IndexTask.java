/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vinayaksproject.simpleelasticproject.tasks;

import com.vinayaksproject.simpleelasticproject.JobServerConfig;
import com.vinayaksproject.simpleelasticproject.dao.DAOIteratorFactory;
import com.vinayaksproject.simpleelasticproject.dao.ElasticSuggestionDAO;
import com.vinayaksproject.simpleelasticproject.document.SuggestionDocument;
import com.vinayaksproject.simpleelasticproject.entity.Suggestion;
import com.vinayaksproject.simpleelasticproject.tasks.exceptions.TaskFailedException;
import com.vinayaksproject.simpleelasticproject.tasks.exceptions.TaskSuccessfulException;
import com.vinayaksproject.simpleelasticproject.tasks.exceptions.TaskTerminatedException;
import com.vinayaksproject.simpleelasticproject.utils.EntityConverter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**The task class for indexing operations
 *
 * @author vinayak
 */

public final class IndexTask extends Task{
    boolean fullActiveIndex;
    Boolean instantIndex;
    Boolean isCancellable;
    Boolean isDeleted;
    JobServerConfig jobDetails;
    DAOIteratorFactory iterFactory;
    Iterator<Suggestion> daoIterator;
    Timestamp lastUpdateDate;
    List<Integer> docIDs;
    Pageable initialPage;
    ElasticSuggestionDAO elasticDao;
    private static final Logger LOG = Logger.getLogger(IndexTask.class.getName());
    
    public IndexTask(int taskid, Map paramsMap) {
        super(taskid, paramsMap);
        initialize();
    }

    @Override
    protected void initialize() {
        fullActiveIndex=true;
        instantIndex=false;
        isCancellable=false;
        isDeleted=null;
        lastUpdateDate=null;
        docIDs=null;
     if(getParamsMap().containsKey("lastIndexTime")){
         fullActiveIndex=false;
         lastUpdateDate=(Timestamp) getParamsMap().get("lastIndexTime");
     }
     if(getParamsMap().containsKey("suggestionids")){
         instantIndex=true;
         fullActiveIndex=false;
         docIDs=(List<Integer>) getParamsMap().get("lastIndexTime");
     }
     daoIterator = iterFactory.NewSuggesionIterator(isDeleted, lastUpdateDate, docIDs, initialPage);
     if(LOG.isLoggable(Level.FINEST)){
         LOG.log(Level.FINEST, "Created iterator of {0} for index job", daoIterator.getClass());
     }
    }

    @Override
    public void start()  throws TaskTerminatedException{
        try {
            doBulkIndex();
        } catch (Exception ex) {
            throw new TaskFailedException("The bulk index operation failed of the index task",ex);
        }
        throw new TaskSuccessfulException("The bulk index operation was successful for the index task");
    }

    @Override
    public void cancel() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    void doBulkIndex() throws IOException{
       if(LOG.isLoggable(Level.FINEST)){
         LOG.log(Level.FINEST, "Bulk Operation started ");
     }    
        List<SuggestionDocument> list=new ArrayList();
        while(daoIterator.hasNext()){
            list.add(EntityConverter.SuggestionEntitytoDocument(daoIterator.next()));
        if(list.size()>=jobDetails.getBulkDocCount()) {
            elasticDao.bulkAPI(list);
            list.clear();
        }   
            
        }
    if(LOG.isLoggable(Level.FINEST)){
         LOG.log(Level.FINEST, "Bulk Operation completed successfully");
     }     
    }

    
}
