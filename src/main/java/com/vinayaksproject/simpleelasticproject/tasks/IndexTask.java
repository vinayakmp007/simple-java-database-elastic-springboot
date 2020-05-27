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
import com.vinayaksproject.simpleelasticproject.enums.ParameterFieldNames;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

/**
 * The task class for indexing operations
 *
 * @author vinayak
 */
public final class IndexTask extends Task {

    private Boolean fullActiveIndex;
    private Boolean instantIndex;
    private boolean cancellable;
    private Boolean deleted;
    private boolean initialized;
    private JobServerConfig jobDetails;
    private DAOIteratorFactory iterFactory;
    private Iterator<Suggestion> daoIterator;
    private Timestamp lastUpdateDate;
    private List<Integer> docIDs;
    private Pageable initialPage;
    private ElasticSuggestionDAO elasticDao;
    private static Logger LOG = Logger.getLogger(IndexTask.class.getName());

    public IndexTask(int taskid, Map paramsMap) {
        super(taskid, paramsMap);
        setInitialized(false);
    }

    @Override
    public void initialize() {

        setFullActiveIndex(true);
        setInstantIndex((Boolean) false);
        setCancellable((Boolean) false);
        setDeleted(null);
        setLastUpdateDate(null);
        setDocIDs(null);
        if (getParamsMap().containsKey(ParameterFieldNames.lastIndexTime.getFieldName())) {
            setFullActiveIndex(false);
            setLastUpdateDate(new Timestamp((long) getParamsMap().get(ParameterFieldNames.lastIndexTime.getFieldName())));
        }
        if (getParamsMap().containsKey(ParameterFieldNames.suggestionids.getFieldName())) {
            setInstantIndex((Boolean) true);
            setFullActiveIndex(false);
            setDocIDs((List<Integer>) getParamsMap().get(ParameterFieldNames.suggestionids.getFieldName()));
        }
        setDaoIterator((Iterator<Suggestion>) getIterFactory().NewSuggesionIterator(getDeleted(), getLastUpdateDate(), getDocIDs(), getInitialPage()));
        if (getLOG().isLoggable(Level.FINEST)) {
            getLOG().log(Level.FINEST, "Created iterator of {0} for index job", getDaoIterator().getClass());
        }
        setInitialized(true);
    }

    @Override
    public void start() throws TaskTerminatedException {
        if (!isInitialized()) {
            throw new TaskFailedException("The Task was not initialised before starting execution");
        }
        try {
            doBulkIndex();
        } catch (Exception ex) {
            throw new TaskFailedException("The bulk index operation failed of the index task", ex);
        }
        throw new TaskSuccessfulException("The bulk index operation was successful for the index task");
    }

    @Override
    public void cancel() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void doBulkIndex() throws IOException {
        if (getLOG().isLoggable(Level.FINEST)) {
            getLOG().log(Level.FINEST, "Bulk Operation started ");
        }
        List<SuggestionDocument> list = new ArrayList();
        while (getDaoIterator().hasNext()) {
            list.add(EntityConverter.SuggestionEntitytoDocument(getDaoIterator().next()));
            if (list.size() >= getJobDetails().getBulkDocCount()) {
                getElasticDao().bulkAPI(list);
                list.clear();
            }

        }
        if (list.size() > 0) {
            getElasticDao().bulkAPI(list);
            list.clear();
        }
        if (getLOG().isLoggable(Level.FINEST)) {
            getLOG().log(Level.FINEST, "Bulk Operation completed successfully");
        }
    }

    /**
     * @return the fullActiveIndex
     */
    public Boolean getFullActiveIndex() {
        return fullActiveIndex;
    }

    /**
     * @param fullActiveIndex the fullActiveIndex to set
     */
    public void setFullActiveIndex(Boolean fullActiveIndex) {
        this.fullActiveIndex = fullActiveIndex;
    }

    /**
     * @return the instantIndex
     */
    public Boolean getInstantIndex() {
        return instantIndex;
    }

    /**
     * @param instantIndex the instantIndex to set
     */
    public void setInstantIndex(Boolean instantIndex) {
        this.instantIndex = instantIndex;
    }

    /**
     * @return the jobDetails
     */
    public JobServerConfig getJobDetails() {
        return jobDetails;
    }

    /**
     * @param jobDetails the jobDetails to set
     */
    @Autowired
    public void setJobDetails(JobServerConfig jobDetails) {
        this.jobDetails = jobDetails;
    }

    /**
     * @return the iterFactory
     */
    public DAOIteratorFactory getIterFactory() {
        return iterFactory;
    }

    /**
     * @param iterFactory the iterFactory to set
     */
    @Autowired
    public void setIterFactory(DAOIteratorFactory iterFactory) {
        this.iterFactory = iterFactory;
    }

    /**
     * @return the daoIterator
     */
    public Iterator<Suggestion> getDaoIterator() {
        return daoIterator;
    }

    /**
     * @param daoIterator the daoIterator to set
     */
    public void setDaoIterator(Iterator<Suggestion> daoIterator) {
        this.daoIterator = daoIterator;
    }

    /**
     * @return the lastUpdateDate
     */
    public Timestamp getLastUpdateDate() {
        return lastUpdateDate;
    }

    /**
     * @param lastUpdateDate the lastUpdateDate to set
     */
    public void setLastUpdateDate(Timestamp lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    /**
     * @return the docIDs
     */
    public List<Integer> getDocIDs() {
        return docIDs;
    }

    /**
     * @param docIDs the docIDs to set
     */
    public void setDocIDs(List<Integer> docIDs) {
        this.docIDs = docIDs;
    }

    /**
     * @return the initialPage
     */
    public Pageable getInitialPage() {
        return initialPage;
    }

    /**
     * @param initialPage the initialPage to set
     */
    @Autowired
    public void setInitialPage(Pageable initialPage) {
        this.initialPage = initialPage;
    }

    /**
     * @return the elasticDao
     */
    public ElasticSuggestionDAO getElasticDao() {
        return elasticDao;
    }

    /**
     * @param elasticDao the elasticDao to set
     */
    @Autowired
    public void setElasticDao(ElasticSuggestionDAO elasticDao) {
        this.elasticDao = elasticDao;
    }

    /**
     * @return the LOG
     */
    public static Logger getLOG() {
        return LOG;
    }

    /**
     * @param aLOG the LOG to set
     */
    public static void setLOG(Logger aLOG) {
        LOG = aLOG;
    }

    /**
     * @return the cancellable
     */
    public boolean isCancellable() {
        return cancellable;
    }

    /**
     * @param cancellable the cancellable to set
     */
    public void setCancellable(boolean cancellable) {
        this.cancellable = cancellable;
    }

    /**
     * @return the initialized
     */
    public boolean isInitialized() {
        return initialized;
    }

    /**
     * @param initialized the initialized to set
     */
    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    /**
     * @return the deleted
     */
    public Boolean getDeleted() {
        return deleted;
    }

    /**
     * @param deleted the deleted to set
     */
    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

}
