/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vinayaksproject.simpleelasticproject.services;

import com.vinayaksproject.simpleelasticproject.dao.SuggestionDAO;
import com.vinayaksproject.simpleelasticproject.entity.Suggestion;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

/**This class provides crud service for suggestion entity
 *
 * @author vinayak
 */
public class SuggestionCrudService implements CrudService<Suggestion> {

    @Autowired
    SuggestionDAO suggestiondao;

    @Override
    public Suggestion findById(int id) {
        Optional<Suggestion> optional = suggestiondao.findById(id);
        return optional.isPresent() ? optional.get() : null;
    }

    @Override
    public Suggestion save(Suggestion entity) {
        return suggestiondao.save(entity);
    }

    @Override
    public Suggestion delete(int id) {
        Suggestion entity = findById(id);
        if (entity == null) {
            return null;
        }
        entity.setDeleted(true);
        return save(entity);
    }

}
