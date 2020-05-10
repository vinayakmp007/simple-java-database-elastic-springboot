/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vinayaksproject.simpleelasticproject.tasks;

import java.util.concurrent.Callable;

/**
 *
 * @author vinayak
 */
public interface IndexTaskExecutor extends Callable{

    public long getExecutionTime();
    
}
