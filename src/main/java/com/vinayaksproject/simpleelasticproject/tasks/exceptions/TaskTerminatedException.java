/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vinayaksproject.simpleelasticproject.tasks.exceptions;

/**
 *
 * @author vinayak
 */
public abstract class TaskTerminatedException extends RuntimeException{

    public TaskTerminatedException(String message, Throwable cause) {
        super(message, cause);
    }

    public TaskTerminatedException(String message) {
        super(message);
    }

  
  
    
}
