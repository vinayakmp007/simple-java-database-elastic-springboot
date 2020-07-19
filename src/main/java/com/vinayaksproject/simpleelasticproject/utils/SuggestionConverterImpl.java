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
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 * This class used for conversion of Suggestion Entity
 *
 * @author vinayak
 */
@Service  //TODO convert it to generic
public final class SuggestionConverterImpl implements SuggestionConverter {

    @Override
    public SuggestionDocument EntitytoDocument(Suggestion entity) {
        SuggestionDocument document = new SuggestionDocument();
        if (entity == null) {
            throw new IllegalArgumentException("The argument Suggestion cannot be null");
        }
        document.setId(String.valueOf(entity.getId()));
        document.setSuggestion(entity.getSuggestion());
        document.setDbCreationDate(entity.getCreationDate() != null ? new Timestamp(entity.getCreationDate().getTime()) : null);
        document.setDbLastUpdateDate(entity.getLastUpdateDate() != null ? new Timestamp(entity.getLastUpdateDate().getTime()) : null);
        document.setDbVersion(entity.getVersion());
        document.setDbdeleted(entity.isDeleted());
        return document;
    }

    @Override
    public SuggestionDTO DocumentoDTO(SuggestionDocument document) {
        if (document == null) {
            throw new IllegalArgumentException("The argument document cannot be null");
        }
        SuggestionDTO suggestionDTO = new SuggestionDTO();
        suggestionDTO.setId(Integer.valueOf(document.getId()));
        suggestionDTO.setSuggestion(document.getSuggestion());
        return suggestionDTO;
    }

    @Override
    public SuggestionDTO EntityToDTO(Suggestion entity) {
        if (entity == null) {
            throw new IllegalArgumentException("The argument Suggestion cannot be null");
        }
        return DocumentoDTO(EntitytoDocument(entity));
    }

    @Override
    public ResultRowDTO<SuggestionDTO> ResultRowToDTO(ElasticEntityResultRow<SuggestionDocument> resultRow) {

        if (resultRow == null) {
            throw new IllegalArgumentException("The argument resultRow cannot be null");
        }
        ResultRowDTO result = new ResultRowDTO();
        result.setEntityDTO(DocumentoDTO(resultRow.getObject()));
        Map tempMap = new HashMap<String, String[]>();
        resultRow.getHighlight().forEach((k, v) -> {
            tempMap.put(k, v.clone());
        });
        result.setHighlights(tempMap);
        return result;
    }

    @Override
    public Suggestion DTOEntity(SuggestionDTO dto) {
        SuggestionDTO newDTO = new SuggestionDTO();       //implenet copy constructor for suggestion
        newDTO.setSuggestion(dto.getSuggestion());
        newDTO.setId(dto.getId());

        Suggestion entity = new Suggestion();
        if (dto.getId() != null&&dto.getId() != 0) {
            entity.setId(newDTO.getId());
        }
        entity.setSuggestion(newDTO.getSuggestion());
        return entity;
    }

}
