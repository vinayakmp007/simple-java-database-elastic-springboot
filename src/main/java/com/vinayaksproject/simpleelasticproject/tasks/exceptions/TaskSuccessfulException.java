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
public class TaskSuccessfulException extends TaskTerminatedException{

    public TaskSuccessfulException(String message, Throwable cause) {
        super(message, cause);
    }

    public TaskSuccessfulException(String message) {
        super(message);
    }

    
}
