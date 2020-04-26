/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vinayaksproject.simpleelasticproject.utils;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

/**This abstract class is used to make Iterators using Slice in spring.
 *
 * @author vinayak
 */
public abstract class SliceIterator implements Iterator{
    private Slice slice;
    private List<Object> currentList;
    private Iterator iterator;
    public SliceIterator(){
    }

    protected void resetCurrentList(){
            setCurrentList(slice.getContent());
            resetIterator();
       
    }
    @Override
    public boolean hasNext() {
       if(getIterator()!=null){
       
           if(iterator.hasNext()){
               return iterator.hasNext();
           }
           else return slice.hasNext();
       }
       return false;
    }

    @Override
    public Object next() {
        if(hasNext()){
            if(!iterator.hasNext()){
               setNextSlice();
            }
           if(iterator.hasNext())return iterator.next();
        }
        throw new NoSuchElementException(); 
    }
    /**This is to be overridden by the child classes with the dao function that accepts pageable
     * @param nextPageable The pageable to be provided to the dao function
     * @return the Slice for the given pageable
     */
    protected abstract Slice daoFunction(Pageable nextPageable);  
    @Override
    
    
    public void remove() {
         throw new UnsupportedOperationException("This is List cannot be modified");
    }

    @Override
    public void forEachRemaining(Consumer action) {
        Iterator.super.forEachRemaining(action); //To change body of generated methods, choose Tools | Templates.
    }
    /**Sets the Iterators to the contents of next slice
     * @return the currentList
     */
    private void setNextSlice() {
       if(getSlice()!=null&&getSlice().hasNext()){
           Pageable next=getSlice().nextPageable();
            setSlice(daoFunction(next));   
            applySlice();
       }
    }

    /**
     * @return the currentList
     */
    private List<Object> getCurrentList() {
        return currentList;
    }

    /**
     * @return the slice
     */
    private Slice getSlice() {
        return slice;
    }

    /**
     * @param slice the slice to set
     */
    protected void setSlice(Slice slice) {
        this.slice = slice;
    }
    /**
     *
     */
    protected void applySlice(){      
    resetCurrentList();
    }

    /**
     * @param currentList the currentList to set
     */
    private void setCurrentList(List<Object> currentList) {
        this.currentList = currentList;
    }

    /**
     * @return the iterator
     */
    private Iterator getIterator() {
        return iterator;
    }

    /**Sets the iterator to the the starting of current list
     * @return reset the iterator
     */
    private void resetIterator() {
       if(getCurrentList()!=null){
           setIterator(getCurrentList().iterator());
       }
       else setIterator(null);
    }
    
    /**Sets the internal Iterator
     * @param iterator the iterator to set
     */
    private void setIterator(Iterator iterator) {
        this.iterator = iterator;
    }
    
}
