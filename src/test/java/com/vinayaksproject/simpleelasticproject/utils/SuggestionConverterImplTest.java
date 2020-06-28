/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vinayaksproject.simpleelasticproject.utils;

import com.vinayaksproject.simpleelasticproject.document.SuggestionDocument;
import com.vinayaksproject.simpleelasticproject.dtos.ResultRowDTO;
import com.vinayaksproject.simpleelasticproject.dtos.SuggestionDTO;
import com.vinayaksproject.simpleelasticproject.entity.Suggestion;
import com.vinayaksproject.simpleelasticproject.query.ElasticEntityResultRow;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author vinayak
 */
public class SuggestionConverterImplTest {

    public SuggestionConverterImplTest() {
    }
    private static Suggestion entity;
    private static SuggestionDocument document;
    private static SuggestionDTO dto;
    private static ElasticEntityResultRow<SuggestionDocument> resultRow;
    private static Map<String, String[]> highlights;
    private static ResultRowDTO<SuggestionDTO> resultRowDTO;

    @BeforeAll
    public static void setUpClass() {
        entity = new Suggestion();
        entity.setId(132);
        entity.setHits(2);
        entity.setCreationDate(Timestamp.from(Instant.now()));
        entity.setDeleted(false);
        entity.setSuggestion("Suggestion");
        entity.setVersion(6);

        document = new SuggestionDocument();
        document.setId(String.valueOf(entity.getId()));
        document.setDbCreationDate(entity.getCreationDate());
        document.setDbdeleted(entity.isDeleted());
        document.setSuggestion(entity.getSuggestion());
        document.setDbVersion(entity.getVersion());
        document.setSuggestion(entity.getSuggestion());

        dto = new SuggestionDTO();
        dto.setId(entity.getId());
        dto.setSuggestion(entity.getSuggestion());
        highlights = new HashMap();
        highlights.put("field", new String[]{"<b>sdsdsa</b>"});
        resultRow = ElasticEntityResultRow.<SuggestionDocument>newInstance(document, highlights);

        resultRowDTO = new ResultRowDTO<>();
        resultRowDTO.setEntityDTO(dto);
        resultRowDTO.setHighlights(highlights);
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
     * Test of EntitytoDocument method, of class SuggestionConverterImpl.
     */
    @Test
    public void testEntitytoDocument() {
        System.out.println("EntitytoDocument");

        SuggestionConverterImpl instance = new SuggestionConverterImpl();

        SuggestionDocument result = instance.EntitytoDocument(entity);
        assertEquals(document, result);

    }

    /**
     * Test of DocumentoDTO method, of class SuggestionConverterImpl.
     */
    @Test
    public void testDocumentoDTO() {
        System.out.println("DocumentoDTO");
        SuggestionConverterImpl instance = new SuggestionConverterImpl();
        SuggestionDTO result = instance.DocumentoDTO(document);
        assertEquals(dto, result);
    }

    /**
     * Test of EntityToDTO method, of class SuggestionConverterImpl.
     */
    @Test
    public void testEntityToDTO() {
        System.out.println("EntityToDTO");
        SuggestionConverterImpl instance = new SuggestionConverterImpl();
        SuggestionDTO result = instance.EntityToDTO(entity);
        assertEquals(dto, result);
    }

    /**
     * Test of ResultRowToDTO method, of class SuggestionConverterImpl.
     */
    @Test
    public void testResultRowToDTO() {
        System.out.println("ResultRowToDTO");
        SuggestionConverterImpl instance = new SuggestionConverterImpl();
        ResultRowDTO<SuggestionDTO> result = instance.ResultRowToDTO(resultRow);
        assertEquals(resultRowDTO, result);
    }

}
