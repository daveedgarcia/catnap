package com.catnap.core.model;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 *
 * @author gwhit7
 */
public class DefaultMapBackedModelTest
{
    @Test
    public void addSingleValueToModel()
    {
        DefaultMapBackedModel model = new DefaultMapBackedModel();
        model.addValue("test", "value");

        assertEquals(1, model.getResult().size());
        assertEquals("value", model.getResult().get("test"));
    }

    @Test
    public void createChildMap()
    {
        DefaultMapBackedModel model = new DefaultMapBackedModel();
        model.createChildMap("childMap");

        assertEquals(1, model.getResult().size());
        assertTrue(model.getResult().get("childMap") instanceof LinkedHashMap);
    }

    @Test
    public void createChildList()
    {
        DefaultMapBackedModel model = new DefaultMapBackedModel();
        model.createChildList("childList");

        assertEquals(1, model.getResult().size());
        assertTrue(model.getResult().get("childList") instanceof LinkedList);
    }

    @Test
    public void modelWithoutElementsIsEmpty()
    {
        assertTrue(new DefaultMapBackedModel().isEmpty());
    }

    @Test
    public void modelWithElementsIsNotEmpty()
    {
        DefaultMapBackedModel model = new DefaultMapBackedModel();
        model.addValue("test", "value");

        assertFalse(model.isEmpty());
    }
}
