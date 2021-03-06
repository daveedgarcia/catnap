package com.catnap.core.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author gwhit7
 */
public class DefaultMapBackedModel implements MapBackedModel<Map<String, Object>>
{
    private Map<String, Object> values = new LinkedHashMap<String, Object>();

    @Override
    public void addValue(String name, Object result)
    {
        values.put(name, result);
    }

    @Override
    public MapBackedModel<?> createChildMap(String name)
    {
        MapBackedModel child = new DefaultMapBackedModel();
        values.put(name, child.getResult());

        return child;
    }

    @Override
    public ListBackedModel<?> createChildList(String name)
    {
        ListBackedModel child = new DefaultListBackedModel();
        values.put(name, child.getResult());

        return child;
    }

    @Override
    public Map<String, Object> getResult()
    {
        return values;
    }

    @Override
    public boolean isEmpty()
    {
        return values.isEmpty();
    }
}
