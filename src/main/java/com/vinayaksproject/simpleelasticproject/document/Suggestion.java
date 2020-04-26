/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vinayaksproject.simpleelasticproject.document;

import java.sql.Timestamp;
import javax.persistence.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**This is the document that will be stored in the elastic search.
 *
 * @author vinayak
 */
@Document(indexName = "suggestion", type = "text")
public class Suggestion {
      @Id
    private String id;
    
    private String suggestion;

    
    protected Timestamp dbCreationDate;
 
    protected Timestamp dbLastUpdateDate;
  
    protected Integer dbVersion;
    
}
