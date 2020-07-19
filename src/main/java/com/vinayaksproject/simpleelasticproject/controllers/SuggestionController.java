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

import com.vinayaksproject.simpleelasticproject.document.SuggestionDocument;
import com.vinayaksproject.simpleelasticproject.dtos.QueryDTO;
import com.vinayaksproject.simpleelasticproject.dtos.SuggestionDTO;
import com.vinayaksproject.simpleelasticproject.entity.Suggestion;
import com.vinayaksproject.simpleelasticproject.query.ElasticQuery;
import com.vinayaksproject.simpleelasticproject.query.ElasticQueryPrimitiveImpl;
import com.vinayaksproject.simpleelasticproject.query.ElasticResult;
import com.vinayaksproject.simpleelasticproject.services.SuggestionCrudService;
import com.vinayaksproject.simpleelasticproject.services.SuggestionDocumentCRUDService;
import com.vinayaksproject.simpleelasticproject.utils.SuggestionConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * The rest controller for all suggestion related activies
 *
 * @author vinayak
 */
@RestController
public class SuggestionController {

    @Autowired
    SuggestionCrudService service;
    @Autowired
    SuggestionDocumentCRUDService documentService;
    @Autowired
    SuggestionConverter converter;

    /**
     * This is used to delete suggestions This updates the Database and elastic
     * (if configured)
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/suggestion/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> delete(@PathVariable("id") String id) {
        ResponseEntity result;
        Suggestion suggestion = service.findById(Integer.parseInt(id));
        if (suggestion != null) {
            service.delete(Integer.parseInt(id));
            result = new ResponseEntity<>(suggestion, HttpStatus.OK);
        } else {
            result = new ResponseEntity<>("Requested element was not found", HttpStatus.NOT_FOUND);
        }
        return result;
    }

    /**
     * This is used to get the suggestion entity This updates the Database and
     * elastic (if configured)
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/suggestion/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> get(@PathVariable("id") String id) {
        Suggestion suggestion = service.findById(Integer.parseInt(id));
        ResponseEntity result;
        if (suggestion != null) {
            result = new ResponseEntity<>(suggestion, HttpStatus.OK);

        } else {
            result = new ResponseEntity<>("Requested element was not found", HttpStatus.NOT_FOUND);
        }
        return result;
    }

    /**
     * This endpoint is used to update the suggestion entity only if it exists
     * This updates the Database and elastic (if configured)
     *
     * @param id
     * @param suggestionDTO
     * @return
     */
    @RequestMapping(value = "/suggestion/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Object> put(@PathVariable("id") String id, @RequestBody SuggestionDTO suggestionDTO) {
        Suggestion suggestion = converter.DTOEntity(suggestionDTO);
        Suggestion originalSuggestion = service.findById(Integer.parseInt(id));
        ResponseEntity result;
        if (originalSuggestion != null) {
            originalSuggestion.setSuggestion(suggestion.getSuggestion());
            originalSuggestion.setHits(suggestion.getHits());
            suggestion = service.save(originalSuggestion);
            result = new ResponseEntity<>(suggestion, HttpStatus.OK);
        } else {
            result = new ResponseEntity<>("Requested element was not found", HttpStatus.NOT_FOUND);
        }
        return result;
    }

    /**
     * This end point is to create suggestion entitys. This updates the Database
     * and elastic (if configured)
     *
     * @param suggestionDTO
     * @return
     */
    @RequestMapping(value = "/suggestion", method = RequestMethod.POST)
    public ResponseEntity<Object> post(@RequestBody SuggestionDTO suggestionDTO) {
        Suggestion suggestion = converter.DTOEntity(suggestionDTO);
        suggestion = service.save(suggestion);
        return new ResponseEntity<>(suggestion, HttpStatus.OK);
    }

    /**
     * This end point is to do a search on suggestion entities. This calls to
     * the elastic server
     *
     * @param queryDTO
     * @param pageable
     * @return
     */
    @RequestMapping(value = "/suggestion/_search", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity<Object> search(@RequestBody QueryDTO queryDTO, Pageable pageable) {
        ElasticQueryPrimitiveImpl.ElasticQueryPrimitiveBuilder builder = new ElasticQueryPrimitiveImpl.ElasticQueryPrimitiveBuilder();
        ElasticQuery query = builder.buildQuery(queryDTO.getQuery()).
                buildHightLight(queryDTO.getQuery()).
                buildSort(queryDTO.getQuery()).build();

        ElasticResult<SuggestionDocument> suggestion = documentService.findByQuery(query, pageable);
        return new ResponseEntity<>(suggestion, HttpStatus.OK);
    }
}
