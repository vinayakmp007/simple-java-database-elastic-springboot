/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vinayaksproject.simpleelasticproject.query;

import com.vinayaksproject.simpleelasticproject.enums.QueryFields;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Test class for builder primitive query builder
 *
 * @author vinayak
 */
@SpringBootTest
@ActiveProfiles("test")
public class ElasticQueryPrimitiveImplTest {

    static String QUERY = "{\n"
            + "  \"match\" : {\n"
            + "    \"suggestion\" : {\n"
            + "      \"query\" : \"test\",\n"
            + "      \"operator\" : \"OR\",\n"
            + "      \"prefix_length\" : 0,\n"
            + "      \"max_expansions\" : 50,\n"
            + "      \"fuzzy_transpositions\" : true,\n"
            + "      \"lenient\" : false,\n"
            + "      \"zero_terms_query\" : \"NONE\",\n"
            + "      \"auto_generate_synonyms_phrase_query\" : true,\n"
            + "      \"boost\" : 1.0\n"
            + "    }\n"
            + "  }\n"
            + "}";
    static final String MATCH_ALL_QUERY = "{\n"
            + "  \"match_all\" : {\n"
            + "    \"boost\" : 1.0\n"
            + "  }\n"
            + "}";

    static final String HIGHLIGHT_QUERY = "{\n"
            + "  \"fields\" : {\n"
            + "    \"suggestion\" : {\n"
            + "      \"type\" : \"unified\"\n"
            + "    }\n"
            + "  }\n"
            + "}";
    static final String SOURCE_BUILDER_QUERY = "{\"query\":{\"match\":{\"suggestion\":{\"query\":\"this works well\",\"operator\":\"OR\",\"prefix_length\":0,\"max_expansions\":50,\"fuzzy_transpositions\":true,\"lenient\":false,\"zero_terms_query\":\"NONE\",\"auto_generate_synonyms_phrase_query\":true,\"boost\":1.0}}},\"highlight\":{\"fields\":{\"suggestion\":{\"type\":\"unified\"}}}}";

    public ElasticQueryPrimitiveImplTest() {

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
     * Test of containsQuery method, of class ElasticQueryPrimitiveImpl.
     */
    @Test
    public void testContainsQuery() {
        ElasticQueryPrimitiveImpl.ElasticQueryPrimitiveBuilder builder = new ElasticQueryPrimitiveImpl.ElasticQueryPrimitiveBuilder();
        Map<String, Object> queryMap = new HashMap();
        queryMap.put(QueryFields.OPERATION.getName(), "search");
        try {
            builder.buildQuery(queryMap);
            fail("An exception was excepted");
        } catch (Exception ex) {
            assertTrue(ex instanceof IllegalArgumentException);
        }
        queryMap.put(QueryFields.SEARCH_FIELD.getName(), "suggestion");

        builder.buildQuery(queryMap);
        assertEquals(builder.getQueryBuilder().toString(), MATCH_ALL_QUERY);
        queryMap.put(QueryFields.SEARCH_STRING.getName(), "test");

        builder.buildQuery(queryMap);
        assertEquals(builder.getQueryBuilder().toString(), QUERY);

        ElasticQueryPrimitiveImpl instance = builder.build();
        assertTrue(instance.containsQuery());
    }

    /**
     * Test of containsAggregration method, of class ElasticQueryPrimitiveImpl.
     */
    @Test
    public void testContainsAggregration() {

    }

    /**
     * Test of containsHighLight method, of class ElasticQueryPrimitiveImpl.
     */
    @Test
    public void testContainsHighLight() {
        ElasticQueryPrimitiveImpl.ElasticQueryPrimitiveBuilder builder = new ElasticQueryPrimitiveImpl.ElasticQueryPrimitiveBuilder();
        Map<String, Object> queryMap = new HashMap();
        try {
            builder.buildHightLight(queryMap);
        } catch (Exception ex) {
            fail("An exception was thrown");
        }
        queryMap.put(QueryFields.HIGHLIGHT.getName(), false);
        try {
            builder.buildHightLight(queryMap);
        } catch (Exception ex) {
            fail("An exception was thrown");
        }
        queryMap.put(QueryFields.HIGHLIGHT.getName(), true);
        try {
            builder.buildHightLight(queryMap);
            fail("An exception was excepted");
        } catch (Exception ex) {
            assertTrue(ex instanceof IllegalArgumentException);;
        }
        queryMap.put(QueryFields.SEARCH_FIELD.getName(), "suggestion");
        builder.buildHightLight(queryMap);
        assertEquals(builder.getHighlightBuilder().toString(), HIGHLIGHT_QUERY);

        ElasticQueryPrimitiveImpl instance = builder.build();
        assertTrue(instance.containsHighLight());
    }

    /**
     * Test of getSearchSourceBuilder method, of class
     * ElasticQueryPrimitiveImpl.
     */
    @Test
    public void testGetSearchSourceBuilder() {
        System.out.println("getSearchSourceBuilder");
        ElasticQueryPrimitiveImpl.ElasticQueryPrimitiveBuilder builder = new ElasticQueryPrimitiveImpl.ElasticQueryPrimitiveBuilder();
        Map<String, Object> queryMap = new HashMap();

        queryMap.put(QueryFields.OPERATION.getName(), "search");
        queryMap.put(QueryFields.SEARCH_FIELD.getName(), "suggestion");
        queryMap.put(QueryFields.SEARCH_STRING.getName(), "this works well");
        queryMap.put(QueryFields.SEARCH_FIELD.getName(), "suggestion");
        queryMap.put(QueryFields.HIGHLIGHT.getName(), true);
        builder.buildQuery(queryMap);
        builder.buildAggregrationQuery(queryMap);
        builder.buildHightLight(queryMap);
        builder.buildSort(queryMap);
        builder.buildHightLight(queryMap);
        assertEquals(builder.getsearchSourceBuilder().toString(), SOURCE_BUILDER_QUERY);

        ElasticQueryPrimitiveImpl instance = builder.build();
        assertEquals(instance.getSearchSourceBuilder().toString(), SOURCE_BUILDER_QUERY);
    }

}
