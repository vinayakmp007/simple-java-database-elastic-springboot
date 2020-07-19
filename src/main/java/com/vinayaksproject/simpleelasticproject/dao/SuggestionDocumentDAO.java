/*
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinayaksproject.simpleelasticproject.ElasticConfig;
import com.vinayaksproject.simpleelasticproject.document.SuggestionDocument;
import com.vinayaksproject.simpleelasticproject.enums.ElasticRequest;
import com.vinayaksproject.simpleelasticproject.query.ElasticEntityResultRow;
import com.vinayaksproject.simpleelasticproject.query.ElasticQuery;
import com.vinayaksproject.simpleelasticproject.query.ElasticResult;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetRequest;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.ShardSearchFailure;
import org.elasticsearch.action.support.WriteRequest.RefreshPolicy;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

/**
 *
 * @author vinayak
 */
@Repository
public class SuggestionDocumentDAO implements ElasticSuggestionDAO {

    static final Logger LOG = Logger.getLogger(SuggestionDocumentDAO.class.getName());
    private ElasticConfig elasticConfig;
    RestHighLevelClient restHighLevelClient;
    ObjectMapper objectMapper;

    private RequestFactory requestFactory;

    @Autowired
    SuggestionDocumentDAO(RestHighLevelClient restHighLevelClient, ObjectMapper defaultObjectMapper) {
        this.restHighLevelClient = restHighLevelClient;
        this.objectMapper = defaultObjectMapper;
    }

    @Override
    public String save(SuggestionDocument suggestionDocument) throws IOException {
        if (suggestionDocument.getId() == null) {
            throw new IllegalArgumentException("The id of suggestion document must not be null");
        }
        Map documentMap = objectMapper.convertValue(suggestionDocument, Map.class);
        IndexRequest indexRequest = (IndexRequest) requestFactory.newActionRequest(ElasticRequest.CREATE);
        indexRequest.id(suggestionDocument.getId())
                .source(documentMap)
                .setRefreshPolicy(RefreshPolicy.WAIT_UNTIL);
        IndexResponse response = null;

        try {
            response = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "The save operation to elasticsearch failed", ex);
            throw ex;
        }
        ReplicationResponse.ShardInfo shardInfo = response.getShardInfo();
        if (LOG.isLoggable(Level.INFO) && shardInfo.getTotal() != shardInfo.getSuccessful()) {
            if (LOG.isLoggable(Level.INFO)) {
                LOG.log(Level.INFO, "Writing to some shards failed Success:{0} total:{1}", new Object[]{shardInfo.getSuccessful(), shardInfo.getTotal()});
            }
        }
        if (LOG.isLoggable(Level.INFO) && shardInfo.getFailed() > 0) {
            for (ReplicationResponse.ShardInfo.Failure failure
                    : shardInfo.getFailures()) {
                String reason = failure.reason();
                if (LOG.isLoggable(Level.INFO)) {
                    LOG.log(Level.INFO, "Writing to some shards failed due to {0}", reason);
                }
            }
        }

        return response.getId();
    }

    @Override
    public SuggestionDocument get(String suggestionDocumentId) throws IOException {
        GetRequest getRequest = (GetRequest) requestFactory.newActionRequest(ElasticRequest.GET);
        getRequest.id(suggestionDocumentId);
        GetResponse getResponse = null;
        SuggestionDocument document = null;
        try {
            getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
        } catch (IOException ex) {
            Logger.getLogger(SuggestionDocumentDAO.class.getName()).log(Level.SEVERE, "Retriveing document with id " + suggestionDocumentId + "failed", ex);
            throw ex;
        }
        if (getResponse != null && getResponse.isExists()) {
            Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();
            document = objectMapper.convertValue(sourceAsMap, SuggestionDocument.class);
        } else {
            throw new NoSuchElementException("The document with id " + suggestionDocumentId + " was not found in server");
        }
        return document;
    }

    @Override
    public List<SuggestionDocument> get(List<String> suggestionDocumentIds) throws IOException {
        List<SuggestionDocument> list = new ArrayList();
        MultiGetRequest multiGetRequest = (MultiGetRequest) requestFactory.newActionRequest(ElasticRequest.MULTI);
        for (String id : suggestionDocumentIds) {
            MultiGetRequest.Item getRequest = requestFactory.newMultiGetRequestItem(id);
            multiGetRequest.add(getRequest);
        }
        MultiGetResponse response = null;
        try {
            response = restHighLevelClient.mget(multiGetRequest, RequestOptions.DEFAULT);
        } catch (IOException ex) {
            Logger.getLogger(SuggestionDocumentDAO.class.getName()).log(Level.SEVERE, "Multiget request failed", ex);
            throw ex;
        }
        for (MultiGetItemResponse multiGetResponse : response.getResponses()) {
            if (multiGetResponse.isFailed() && LOG.isLoggable(Level.INFO)) {
                LOG.log(Level.INFO, "One of the multigetResponse failed {0}", multiGetResponse.getFailure());
            } else {
                GetResponse getResponse = multiGetResponse.getResponse();
                if (getResponse.isExists()) {
                    Map<String, Object> sourceAsMap;
                    sourceAsMap = getResponse.getSourceAsMap();
                    SuggestionDocument document = objectMapper.convertValue(sourceAsMap, SuggestionDocument.class);
                    list.add(document);
                } else {
                    list.add(null);
                }
            }
        }
        return list;
    }

    /**
     * @return the elasticConfig
     */
    public ElasticConfig getElasticConfig() {
        return elasticConfig;
    }

    /**
     * @param elasticConfig the elasticConfig to set
     */
    @Autowired
    public void setElasticConfig(ElasticConfig elasticConfig) {
        this.elasticConfig = elasticConfig;
    }

    @Override
    public boolean createIndex() throws IOException {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(elasticConfig.getEsIndexName());
        CreateIndexResponse createIndexResponse = null;
        try {
            createIndexResponse = restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Create Index failed", ex);
            throw ex;
        }
        return checkAcknowlegedResponse(createIndexResponse);
    }

    @Override
    public boolean deleteIndex() throws IOException {
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(elasticConfig.getEsIndexName());
        AcknowledgedResponse deleteIndexResponse = null;
        try {
            deleteIndexResponse = restHighLevelClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);

        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Delete Index failed", ex);
            throw ex;
        }
        return checkAcknowlegedResponse(deleteIndexResponse);
    }

    private boolean checkAcknowlegedResponse(AcknowledgedResponse acknowledgedResponse) {
        if (!acknowledgedResponse.isAcknowledged() && LOG.isLoggable(Level.INFO)) {
            LOG.log(Level.INFO, "The request was not acknowledged for {0}", acknowledgedResponse.getClass());
        }

        return acknowledgedResponse.isAcknowledged();
    }

    /**
     * @return the requestFactory
     */
    public RequestFactory getRequestFactory() {
        return requestFactory;
    }

    /**
     * @param requestFactory the requestFactory to set
     */
    @Autowired
    public void setRequestFactory(RequestFactory requestFactory) {
        this.requestFactory = requestFactory;
    }

    @Override
    public void bulkAPI(List<SuggestionDocument> suggestionDocumentList, ElasticRequest indexOperation) throws IOException {
        ElasticRequest operationforEntity = null;
        BulkRequest bulkRequest = (BulkRequest) requestFactory.newActionRequest(ElasticRequest.BULK);
        bulkRequest.setRefreshPolicy(RefreshPolicy.NONE)
                .timeout(TimeValue.timeValueMinutes(5));
        for (SuggestionDocument doc : suggestionDocumentList) {
            DocWriteRequest docwriteRequest = null;
            Map documentMap = objectMapper.convertValue(doc, Map.class);
            if (null == indexOperation) {
                if (doc.getDbdeleted()) {
                    operationforEntity = ElasticRequest.DELETE;
                } else {
                    if (doc.getDbVersion() > 0) {
                        operationforEntity = ElasticRequest.UPDATE;
                    } else {
                        operationforEntity = ElasticRequest.CREATE;
                    }
                }
            } else {
                operationforEntity = indexOperation;
            }

            switch (operationforEntity) {
                case CREATE:
                    docwriteRequest = ((IndexRequest) requestFactory.newActionRequest(operationforEntity)).id(doc.getId())
                            .source(documentMap)
                            .setRefreshPolicy(RefreshPolicy.NONE);
                    break;
                case UPDATE:
                    docwriteRequest = ((UpdateRequest) requestFactory.newActionRequest(operationforEntity)).id(doc.getId())
                            .doc(documentMap)
                            .setRefreshPolicy(RefreshPolicy.NONE);
                    break;
                case DELETE:
                    docwriteRequest = ((DeleteRequest) requestFactory.newActionRequest(operationforEntity)).id(doc.getId())
                            .setRefreshPolicy(RefreshPolicy.NONE);
                    break;
                default:
                    throw new IllegalArgumentException("Selected operation not feasible in bulk");

            }

            bulkRequest.add(docwriteRequest);
        }
        try {
            BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
            if (LOG.isLoggable(Level.INFO) && bulkResponse.hasFailures()) {
                for (BulkItemResponse bulkItemResponse : bulkResponse) {
                    if (bulkItemResponse.isFailed()) {
                        BulkItemResponse.Failure failure
                                = bulkItemResponse.getFailure();
                        LOG.log(Level.INFO, "Bulk api failed for  {0} with {1}", new Object[]{failure.getId(), failure.getMessage()});
                    }
                }

            }
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Bulk API failed", ex);
            throw ex;
        }

    }

    @Override
    public void delete(String id) throws IOException {
        if (id == null) {
            throw new IllegalArgumentException("The id of suggestion document mustnotbe null");
        }

        DeleteRequest deleteRequest = (DeleteRequest) requestFactory.newActionRequest(ElasticRequest.DELETE);
        deleteRequest.id(id)
                .setRefreshPolicy(RefreshPolicy.WAIT_UNTIL);
        DeleteResponse response = null;

        try {
            response = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "The delete operation to elasticsearch failed", ex);
            throw ex;
        }
        ReplicationResponse.ShardInfo shardInfo = response.getShardInfo();
        if (LOG.isLoggable(Level.INFO) && shardInfo.getTotal() != shardInfo.getSuccessful()) {
            if (LOG.isLoggable(Level.INFO)) {
                LOG.log(Level.INFO, "Deleting to some shards failed Success:{0} total:{1}", new Object[]{shardInfo.getSuccessful(), shardInfo.getTotal()});
            }
        }
        if (LOG.isLoggable(Level.INFO) && shardInfo.getFailed() > 0) {
            for (ReplicationResponse.ShardInfo.Failure failure
                    : shardInfo.getFailures()) {
                String reason = failure.reason();
                if (LOG.isLoggable(Level.INFO)) {
                    LOG.log(Level.INFO, "Deleting from to some shards failed due to {0}", reason);
                }
            }
        }
    }

    @Override
    public void bulkAPI(List<SuggestionDocument> suggestionDocumentList) throws IOException {
        bulkAPI(suggestionDocumentList, null);
    }

    @Override
    public ElasticResult<SuggestionDocument> findByQuery(ElasticQuery elasticQuery, Pageable page) throws IOException {

        List<ElasticEntityResultRow<SuggestionDocument>> list = new ArrayList();
        ElasticResult<SuggestionDocument> result;

        long totalHits;
        SearchSourceBuilder searchSourceBuilder = elasticQuery.getSearchSourceBuilder();
        SearchRequest searchRequest = (SearchRequest) requestFactory.newActionRequest(ElasticRequest.SEARCH);
        searchSourceBuilder.from((int) page.getOffset());
        searchSourceBuilder.size(page.getPageSize());
        if (!page.getSort().isSorted()) {
            searchSourceBuilder.sort(new ScoreSortBuilder().order(SortOrder.DESC));
        }
        searchRequest.source(searchSourceBuilder);
        searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        SearchResponse searchResponse = null;
        try {
            searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "The search operation to elasticsearch failed.For Query " + elasticQuery.toString(), ex);
            throw new RuntimeException("Failed for " + elasticQuery.toString(), ex);
        }
        if (searchResponse.isTimedOut()) {
            throw new RuntimeException("Request Timed out  in elastic server");
        }

        if (LOG.isLoggable(Level.INFO)) {
            for (ShardSearchFailure failure : searchResponse.getShardFailures()) {
                LOG.log(Level.INFO, "Failed for shard", failure.toString());
            }
        }

        SearchHits hits = searchResponse.getHits();
        TotalHits totalHit = hits.getTotalHits();
        totalHits = totalHit.value;
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            Map<String, String[]> highLightMap = new HashMap();
            SuggestionDocument document = objectMapper.convertValue(sourceAsMap, SuggestionDocument.class);
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            for (String field : highlightFields.keySet()) {
                HighlightField highlight = highlightFields.get(field);
                Text[] fragments = highlight.fragments();
                List highLightList = new ArrayList();
                for (Text text : fragments) {
                    highLightList.add(text.toString());
                }
                highLightMap.put(field, (String[]) highLightList.toArray(new String[highLightList.size()]));

            }
            list.add(ElasticEntityResultRow.newInstance(document, highLightMap));
        }
        result = ElasticResult.newInstance(list, totalHits);
        return result;
    }

}
