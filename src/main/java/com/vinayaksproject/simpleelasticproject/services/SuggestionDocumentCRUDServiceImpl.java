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
import com.vinayaksproject.simpleelasticproject.query.ElasticResult;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 *
 * @author vinayak
 */
@Service
public class SuggestionDocumentCRUDServiceImpl implements SuggestionDocumentCRUDService {

    @Autowired
    SuggestionDocumentDAO dao;

    @Override
    public SuggestionDocument findById(String id) {
        SuggestionDocument doc = null;
        try {
            doc = dao.get(id);
        } catch (IOException ex) {
            Logger.getLogger(SuggestionDocumentCRUDServiceImpl.class.getName()).log(Level.SEVERE, "Exception occured while finding docuemt for id " + id, ex);
        }
        return doc;
    }

    @Override
    public SuggestionDocument save(SuggestionDocument document) {
        SuggestionDocument doc = null;
        try {
            String id = dao.save(document);
            if (id != null) {
                doc = dao.get(id);
            }
        } catch (IOException ex) {
            Logger.getLogger(SuggestionDocumentCRUDServiceImpl.class.getName()).log(Level.SEVERE, "Exception occured while saving document" + document, ex);
        }
        return doc;
    }

    @Override
    public SuggestionDocument delete(String id) {
        SuggestionDocument doc = null;
        try {
            if (id != null) {
                doc = dao.get(id);
            }
            dao.delete(id);
        } catch (IOException ex) {
            Logger.getLogger(SuggestionDocumentCRUDServiceImpl.class.getName()).log(Level.SEVERE, "Exception occured while deleting document " + id, ex);
        }
        return doc;
    }

    @Override
    public ElasticResult<SuggestionDocument> findByQuery(ElasticQuery query, Pageable page) {
        ElasticResult result = null;
        try {
            result = dao.findByQuery(query, page);
        } catch (IOException ex) {
            Logger.getLogger(SuggestionDocumentCRUDServiceImpl.class.getName()).log(Level.SEVERE, "Exception occured while getting  documents for query " + query, ex);
        }
        return result;
    }

}
