/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vinayaksproject.simpleelasticproject.dao;

import com.vinayaksproject.simpleelasticproject.document.SuggestionDocument;
import com.vinayaksproject.simpleelasticproject.enums.IndexOperations;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.elasticsearch.ElasticsearchStatusException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;

/**
 *
 * @author vinayak
 */
@SpringBootTest
public class SuggestionDocumentDAOTest {

    @Autowired
    ElasticSuggestionDAO ElasticSuggestionDAO;

    @BeforeAll
    public static void setUpClass() {

    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
        try {
            ElasticSuggestionDAO.createIndex();
        } catch (ElasticsearchStatusException | IOException ex) {
            Logger.getLogger(SuggestionDocumentDAOTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @AfterEach
    public void tearDown() {
        try {
            ElasticSuggestionDAO.deleteIndex();
        } catch (ElasticsearchStatusException | IOException ex) {
            Logger.getLogger(SuggestionDocumentDAOTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testCreateAndDeleteIndex() {
        SuggestionDocument document = new SuggestionDocument();
        String id = null;
        document.setId("One");
        try {

            assertTrue(ElasticSuggestionDAO.deleteIndex());
            assertTrue(ElasticSuggestionDAO.createIndex());
            id = ElasticSuggestionDAO.save(document);
            assertTrue(ElasticSuggestionDAO.deleteIndex());
        } catch (IOException ex) {
            fail("Failed with ex " + ex);
        }
        try {
            ElasticSuggestionDAO.deleteIndex();
        } catch (Exception ex) {
            assertTrue(ex instanceof ElasticsearchStatusException);

        }
        try {
            assertTrue(ElasticSuggestionDAO.createIndex());
        } catch (IOException ex) {
            fail("Failed with ex " + ex);
        }
    }

    @Test
    public void testSaveAndGet() {
        SuggestionDocument document = new SuggestionDocument();
        document.setId("One");
        String id = null;
        try {
            id = ElasticSuggestionDAO.save(document);
        } catch (IOException ex) {
            fail("Failed with ex " + ex);
        }
        SuggestionDocument documentFromEs = null;
        try {
            documentFromEs = ElasticSuggestionDAO.get("One");
        } catch (IOException ex) {
            fail("Failed with ex " + ex);
        }
        assertEquals(documentFromEs, document);
        assertEquals(document.getId(), id);
        assertEquals("One", id);
        document.setSuggestion("TEst");
        try {
            id = ElasticSuggestionDAO.save(document);
        } catch (IOException ex) {
            fail("Failed with ex " + ex);
        }
        try {
            documentFromEs = ElasticSuggestionDAO.get("One");
        } catch (IOException ex) {
            fail("Failed with ex " + ex);
        }
        assertEquals(documentFromEs, document);
        assertEquals(document.getId(), id);
        assertEquals("One", id);
        document.setId(null);
                try {
            ElasticSuggestionDAO.save(document);
            fail("No exception was thrown");
        } catch (Exception ex) {
            System.out.println(ex);
            assertTrue(ex instanceof InvalidDataAccessApiUsageException);
        }
    }

    @Test
    public void testBulkAPIandMultiGet() {
        List<SuggestionDocument> itemList = new ArrayList();
        final List idList = new ArrayList<String>();
        List<SuggestionDocument>  documentList = null;
        for (int i = 0; i < 2500; i++) {
            SuggestionDocument temp = new SuggestionDocument();
            temp.setSuggestion("Suggestion" + i);
            temp.setId(String.valueOf(i));
            temp.setDbCreationDate(new Timestamp(System.currentTimeMillis()));
            temp.setDbLastUpdateDate(new Timestamp(System.currentTimeMillis()));
            temp.setDbVersion(1);
            idList.add(temp.getId());
            itemList.add(temp);
        }
        try {
          ElasticSuggestionDAO.bulkAPI(itemList, IndexOperations.CREATE);
          documentList=ElasticSuggestionDAO.get(idList);
          assertNotNull(documentList);
          assertEquals(2500,documentList.size());
            Map<String, SuggestionDocument> map = (Map<String, SuggestionDocument>) itemList.stream().collect(
                Collectors.toMap(x->x.getId(), x->x));
          for(SuggestionDocument temp:documentList){
              assertTrue(map.containsKey(temp.getId()));
              assertEquals(map.get(temp.getId()),temp);
          }
        } catch (IOException ex) {
            Logger.getLogger(SuggestionDocumentDAOTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Exception was thrown" + ex);
        }
        
        //check update requests
        for(SuggestionDocument temp:itemList){
            temp.setDbLastUpdateDate(new Timestamp(System.currentTimeMillis()));
            temp.setSuggestion(temp.getSuggestion()+"Test");
        }
        
        
                try {
          ElasticSuggestionDAO.bulkAPI(itemList, IndexOperations.UPDATE);
          documentList=ElasticSuggestionDAO.get(idList);
          assertNotNull(documentList);
          assertEquals(2500,documentList.size());
            Map<String, SuggestionDocument> map = (Map<String, SuggestionDocument>) itemList.stream().collect(
                Collectors.toMap(x->x.getId(), x->x));
          for(SuggestionDocument temp:documentList){
              assertTrue(map.containsKey(temp.getId()));
              assertEquals(map.get(temp.getId()),temp);
          }
        } catch (Exception ex) {
            Logger.getLogger(SuggestionDocumentDAOTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Exception was thrown" + ex);
        }
                
                //verfy bulkdelete
          List<SuggestionDocument> deletedList = itemList.subList(0, 800);
         itemList=itemList.subList(800,2500); 
                  try {
          ElasticSuggestionDAO.bulkAPI(deletedList, IndexOperations.DELETE);
          documentList=ElasticSuggestionDAO.get(idList);
          assertNotNull(documentList);
          documentList= documentList.stream().filter(x->x!=null).collect(Collectors.toList());//null will be returned with deleted ids
          assertEquals(itemList.size(),documentList.size());
            Map<String, SuggestionDocument> map = (Map<String, SuggestionDocument>) itemList.stream().collect(
                Collectors.toMap(x->x.getId(), x->x));
          for(SuggestionDocument temp:documentList){
              assertTrue(map.containsKey(temp.getId()));
              assertEquals(map.get(temp.getId()),temp);
          }
        } catch (IOException ex) {
            Logger.getLogger(SuggestionDocumentDAOTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Exception was thrown" + ex);
        }     
    }
}
