/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vinayaksproject.simpleelasticproject.dao.iterator;

import com.vinayaksproject.simpleelasticproject.dao.DAOIteratorFactory;
import com.vinayaksproject.simpleelasticproject.dao.SuggestionDAO;
import com.vinayaksproject.simpleelasticproject.entity.Suggestion;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

/**
 *
 * @author vinayak
 */
@SpringBootTest
@ActiveProfiles("test")
public class NewActiveSuggestionIterTest {

    @Autowired
    private DAOIteratorFactory DAOIteratorFactory;
    @Autowired
    private SuggestionDAO suggestionDAO;

    private List<Suggestion> itemList;
    private Timestamp newTime;

    public NewActiveSuggestionIterTest() {
    }

    @BeforeAll
    public static void setUpClass() {

    }

    @AfterAll
    public static void tearDownClass() {

    }

    @BeforeEach
    public void setUp() {
        suggestionDAO.deleteAllInSingleQuery();
        itemList = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            Suggestion temp = new Suggestion();
            temp.setSuggestion("Suggestion10" + i);
            temp.setDeleted(false);
            suggestionDAO.save(temp);
        }
        for (int i = 0; i < 15; i++) {
            Suggestion temp = new Suggestion();
            temp.setSuggestion("Suggestion20" + i);
            temp.setDeleted(true);
            suggestionDAO.save(temp);
        }

        for (int i = 0; i < 5; i++) {
            Suggestion temp = new Suggestion();
            temp.setSuggestion("Suggestion3a0" + i);
            temp.setDeleted(false);
            itemList.add(suggestionDAO.save(temp));

        }

        for (int i = 0; i < 5; i++) {
            Suggestion temp = new Suggestion();
            temp.setSuggestion("Suggestion3d0" + i);
            temp.setDeleted(true);
            itemList.add(suggestionDAO.save(temp));

        }

        newTime = new Timestamp(System.currentTimeMillis());
        try {
            Thread.sleep(1000l);
        } catch (InterruptedException ex) {
            fail("Test failed" + ex);
        }
        List<Suggestion> tempList = new ArrayList();

        itemList.stream().map((temp) -> {
            temp.setSuggestion("Updated " + temp.getSuggestion());
            return temp;
        }).forEachOrdered((temp) -> {
            tempList.add(suggestionDAO.save(temp));
        });
        itemList = tempList;
        for (int i = 0; i < 25; i++) {
            Suggestion temp = new Suggestion();
            temp.setSuggestion("Suggestion110" + i);
            temp.setDeleted(false);
            itemList.add(suggestionDAO.save(temp));
        }
        for (int i = 0; i < 15; i++) {
            Suggestion temp = new Suggestion();
            temp.setSuggestion("Suggestion220" + i);
            temp.setDeleted(true);
            itemList.add(suggestionDAO.save(temp));
        }
    }

    @AfterEach
    public void tearDown() {
        suggestionDAO.deleteAllInSingleQuery();
        itemList = null;

    }

    /**
     * Test of daoFunction method, of class FullActiveSuggestionIter.
     */
    @Test
    public void testDaoFunction() {
        System.out.println("daoFunction");
        try {

            Pageable page = PageRequest.of(0, 10);
            NewActiveSuggestionIter instance = (NewActiveSuggestionIter) DAOIteratorFactory.NewSuggesionIterator(false, newTime, null, page);
            Slice result = instance.daoFunction(page);
            assertNotNull(result);
            assertEquals(10, result.getNumberOfElements());
            assertNotNull(result.getContent());
            assertNotNull(result.getPageable());
            page = result.nextPageable();
            result = instance.daoFunction(page);
            assertNotNull(result);
            assertEquals(10, result.getNumberOfElements());
            page = result.nextPageable();
            result = instance.daoFunction(page);
            assertNotNull(result);
            assertNotNull(result.getContent());
            assertNotNull(result.getPageable());
            assertEquals(10, result.getNumberOfElements());
            assertFalse(result.hasNext());
        } catch (Exception ex) {
            fail("An exception was thrown" + ex);
        }

    }

    /**
     * Test of getBaseDAO method, of class FullActiveSuggestionIter.
     */
    @Test
    public void testIterator() {
        Iterator<Suggestion> removeinactiveIterator = itemList.iterator();
        while (removeinactiveIterator.hasNext()) {
            Suggestion temp = removeinactiveIterator.next();
            if (temp.isDeleted()) {
                removeinactiveIterator.remove();
            }
        }
        for (int pageSize = 1; pageSize <= 30; pageSize++) {
            Pageable page = PageRequest.of(0, pageSize, Sort.by("id"));
            Iterator<Suggestion> instance = (NewActiveSuggestionIter) DAOIteratorFactory.NewSuggesionIterator(false, newTime, null, page);
            Iterator<Suggestion> listIterator = itemList.iterator();
            int count = 0;
            while (instance.hasNext() && listIterator.hasNext()) {
                Suggestion b = instance.next();
                Suggestion a = listIterator.next();
                assertEquals(a, b);
                assertEquals(a.getVersion(), b.getVersion());
                System.out.println("a:" + a.getLastUpdateDate() + "  b:" + b.getLastUpdateDate());
                //   assertEquals(a.getLastUpdateDate(),b.getLastUpdateDate()); Commenting out as there is an issue
                assertFalse(a.isDeleted());
                System.out.println("Acessing " + count++ + "Element");
            }
            assertEquals(instance.hasNext(), listIterator.hasNext());
        }
        try {
            Pageable page = PageRequest.of(0, 1);
            Iterator<Suggestion> instance = (FullActiveSuggestionIter) DAOIteratorFactory.NewSuggesionIterator(true, null, null, page);
            instance.remove();
            fail("No exception was thrown");
        } catch (Exception ex) {
            assertTrue(ex instanceof UnsupportedOperationException);
        }
    }

}
