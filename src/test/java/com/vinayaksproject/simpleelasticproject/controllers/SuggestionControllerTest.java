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
package com.vinayaksproject.simpleelasticproject.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinayaksproject.simpleelasticproject.JobServerConfig;
import com.vinayaksproject.simpleelasticproject.SimpleElasticSuggestionBoxApplication;
import com.vinayaksproject.simpleelasticproject.dao.SuggestionDAO;
import com.vinayaksproject.simpleelasticproject.dao.SuggestionDocumentDAO;
import com.vinayaksproject.simpleelasticproject.document.SuggestionDocument;
import com.vinayaksproject.simpleelasticproject.dtos.QueryDTO;
import com.vinayaksproject.simpleelasticproject.dtos.SuggestionDTO;
import com.vinayaksproject.simpleelasticproject.entity.Suggestion;
import com.vinayaksproject.simpleelasticproject.enums.IndexJobType;
import com.vinayaksproject.simpleelasticproject.enums.QueryFields;
import com.vinayaksproject.simpleelasticproject.query.ElasticResult;
import com.vinayaksproject.simpleelasticproject.services.SuggestionCrudService;
import com.vinayaksproject.simpleelasticproject.services.SuggestionDocumentCRUDService;
import com.vinayaksproject.simpleelasticproject.services.TaskManagementService;
import com.vinayaksproject.simpleelasticproject.services.TaskManagementServiceImpl;
import com.vinayaksproject.simpleelasticproject.utils.SuggestionConverter;
import java.util.HashMap;
import java.util.Map;
import org.elasticsearch.ElasticsearchStatusException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 *
 * @author vinayak
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SuggestionControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    SuggestionCrudService service;
    @Autowired
    SuggestionDocumentCRUDService documentService;
    @Autowired
    SuggestionConverter converter;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    SuggestionDAO suggestiondao;
    @Autowired
    SuggestionDocumentDAO documentdao;

    public SuggestionControllerTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of delete method, of class SuggestionController.
     */
    @Test
    public void testDelete() throws Exception {
        System.out.println("delete");
        String id = "1";
        service.delete(1);
        mockMvc.perform(delete("/suggestion/1")).andExpect(status().isNotFound());
        Suggestion entity = new Suggestion();
        entity = service.save(entity);
        SuggestionDTO dto = converter.EntityToDTO(entity);
        id = String.valueOf(dto.getId());
        mockMvc.perform(delete("/suggestion/" + id)).andExpect(status().isOk()).andExpect(content().json(mapper.writeValueAsString(dto)));

    }

    /**
     * Test of get method, of class SuggestionController.
     */
    @Test
    public void testGet() throws Exception {
        System.out.println("get");
        String id = "1";
        service.delete(1);
        mockMvc.perform(get("/suggestion/1")).andExpect(status().isNotFound());
        Suggestion entity = new Suggestion();
        entity = service.save(entity);
        SuggestionDTO dto = converter.EntityToDTO(entity);
        mockMvc.perform(get("/suggestion/1")).andExpect(status().isOk()).andExpect(content().json(mapper.writeValueAsString(dto)));
    }

    /**
     * Test of put method, of class SuggestionController.
     */
    @Test
    public void testPut() throws Exception {
        System.out.println("put");
        suggestiondao.deleteAllInSingleQuery();
        String id = "1";
        Suggestion entity = new Suggestion();
        entity.setSuggestion("old Text");
        entity = service.save(entity);
        SuggestionDTO dto = converter.EntityToDTO(entity);
        mockMvc.perform(put("/suggestion/1").content(mapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isNotFound());

        entity = service.save(entity);
        id = String.valueOf(entity.getId());
        dto = converter.EntityToDTO(entity);
        dto.setSuggestion("New text");
        mockMvc.perform(put("/suggestion/" + id)
                .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(dto)));

    }

    /**
     * Test of post method, of class SuggestionController.
     */
    @Test
    public void testPost() throws Exception {
        System.out.println("post");
        service.delete(1);
        Suggestion entity = new Suggestion();
        entity.setSuggestion("old Text");
        SuggestionDTO dto = converter.EntityToDTO(entity);
        mockMvc.perform(post("/suggestion").content(mapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk());
    }

    /**
     * Test of search method, of class SuggestionController.
     */
    @Test
    public void testSearch() throws JsonProcessingException, Exception {
        System.out.println("search");

        service.delete(1);
        suggestiondao.deleteAllInSingleQuery();
        try {
            documentdao.deleteIndex();
        } catch (ElasticsearchStatusException ex) {

        }
        documentdao.createIndex();
        for (int i = 0; i < 40; i++) {
            Suggestion entity = new Suggestion();
            entity.setSuggestion(" Text " + i);
            entity = service.save(entity);
            SuggestionDocument doc = converter.EntitytoDocument(entity);
            documentdao.save(doc);
        }
        Map query = new HashMap();
        query.put(QueryFields.OPERATION.getName(), "search");
        query.put(QueryFields.SEARCH_FIELD.getName(), "suggestion");
        query.put(QueryFields.SEARCH_STRING.getName(), "30");
        query.put(QueryFields.HIGHLIGHT.getName(), true);
        QueryDTO dto = new QueryDTO();
        dto.setAggregrateField("hits");  //not implemented
        dto.setQuery(query);
        String result = mockMvc.perform(post("/suggestion/_search").content(mapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        System.out.print(result);
        assertTrue(result.contains("\"suggestion\":\" Text 30\""));
        assertTrue(result.contains("Text <em>30</em>"));

    }

}
