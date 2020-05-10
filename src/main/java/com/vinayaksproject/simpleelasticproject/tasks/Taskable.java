/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vinayaksproject.simpleelasticproject.tasks;

import com.vinayaksproject.simpleelasticproject.tasks.exceptions.TaskTerminatedException;

/**
 * This is the interface every task need to implement
 *
 * @author vinayak
 */
public interface Taskable {

    public void start() throws TaskTerminatedException;
    public void cancel() throws TaskTerminatedException;

}
