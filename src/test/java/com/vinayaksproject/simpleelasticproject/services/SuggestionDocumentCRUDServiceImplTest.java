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
package com.vinayaksproject.simpleelasticproject.services;

import com.vinayaksproject.simpleelasticproject.dao.SuggestionDocumentDAO;
import com.vinayaksproject.simpleelasticproject.document.SuggestionDocument;
import com.vinayaksproject.simpleelasticproject.query.ElasticQuery;
import com.vinayaksproject.simpleelasticproject.query.ElasticQueryPrimitiveImpl;
import com.vinayaksproject.simpleelasticproject.query.ElasticResult;
import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

/**
 *
 * @author vinayak
 */
@SpringBootTest
@ActiveProfiles("test")
public class SuggestionDocumentCRUDServiceImplTest {

    @Mock
    SuggestionDocumentDAO dao;
    @InjectMocks
    SuggestionDocumentCRUDServiceImpl instance;

    public SuggestionDocumentCRUDServiceImplTest() {
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
     * Test of findById method, of class SuggestionDocumentCRUDServiceImpl.
     */
    @Test
    public void testFindById() throws IOException {
        System.out.println("findById");
        String id = "abc";
        SuggestionDocument document = new SuggestionDocument();
        when(dao.get(id)).thenReturn(document);
        SuggestionDocument result = instance.findById(id);
        verify(dao, times(1)).get(id);
        assertEquals(document, result);
    }

    /**
     * Test of save method, of class SuggestionDocumentCRUDServiceImpl.
     */
    @Test
    public void testSave() throws IOException {
        System.out.println("save");
        SuggestionDocument document = new SuggestionDocument();
        String id = "abc";
        when(dao.save(document)).thenReturn("abc");
        when(dao.get(id)).thenReturn(document);
        SuggestionDocument result = instance.save(document);
        verify(dao, times(1)).save(document);
        assertEquals(document, result);
    }

    /**
     * Test of delete method, of class SuggestionDocumentCRUDServiceImpl.
     */
    @Test
    public void testDelete() throws IOException {
        System.out.println("delete");
        SuggestionDocument document = new SuggestionDocument();
        String id = "abc";
        when(dao.get(id)).thenReturn(document);
        SuggestionDocument result = instance.delete(id);
        assertEquals(document, result);
        verify(dao, times(1)).delete(id);
        assertEquals(document, result);
    }

    /**
     * Test of findByQuery method, of class SuggestionDocumentCRUDServiceImpl.
     */
    @Test
    public void testFindByQuery() throws IOException {
        System.out.println("findByQuery");
        ElasticQueryPrimitiveImpl.ElasticQueryPrimitiveBuilder builder = new ElasticQueryPrimitiveImpl.ElasticQueryPrimitiveBuilder();
        ElasticQuery query = builder.build();
        ElasticResult<SuggestionDocument> expResult = ElasticResult.<SuggestionDocument>newInstance(null, 10);
        Pageable page = PageRequest.of(0, 10);
        when(dao.findByQuery(query, page)).thenReturn(expResult);
        ElasticResult<SuggestionDocument> result = instance.findByQuery(query, page);
        assertTrue(expResult == result);
    }

}
