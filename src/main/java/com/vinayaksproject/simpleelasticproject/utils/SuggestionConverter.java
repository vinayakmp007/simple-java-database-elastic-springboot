/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vinayaksproject.simpleelasticproject.utils;

import com.vinayaksproject.simpleelasticproject.document.SuggestionDocument;
import com.vinayaksproject.simpleelasticproject.dtos.SuggestionDTO;
import com.vinayaksproject.simpleelasticproject.entity.Suggestion;

/**The converter class for suggestion Entity
 *
 * @author vinayak
 */
public interface SuggestionConverter extends Converter<Suggestion,SuggestionDTO,SuggestionDocument> {
    
}
