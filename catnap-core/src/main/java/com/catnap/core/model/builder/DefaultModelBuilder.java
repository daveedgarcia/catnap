package com.catnap.core.model.builder;

import com.catnap.core.context.CatnapContext;
import com.catnap.core.model.*;
import com.catnap.core.query.model.Query;
import com.catnap.core.query.processor.Property;
import com.catnap.core.query.processor.QueryProcessor;
import com.catnap.core.query.processor.QueryProcessorFactory;
import com.catnap.core.util.ClassUtil;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author gwhit7
 */
public class DefaultModelBuilder implements ModelBuilder
{
    @Override
    public Model<?> build(Object instance, CatnapContext context)
    {
        Model<?> result = null;

        if(instance instanceof Iterable<?>)
        {
            result = new DefaultListBackedModel();
            buildList((Iterable<?>) instance, context.getQuery(), (ListBackedModel<?>) result, context);
        }
        else if(instance instanceof Map<?,?>)
        {
            result = new DefaultMapBackedModel();
            buildMap((Map<?,?>) instance, context.getQuery(), (MapBackedModel<?>) result, context);
        }
        else
        {
            result = new DefaultMapBackedModel();
            buildObject(instance, context.getQuery(), (MapBackedModel<?>) result, context);
        }

        return result;
    }

    private void buildList(Iterable<?> instance, Query query, ListBackedModel<?> result, CatnapContext context)
    {
        if(instance != null)
        {
            for(Object item : instance)
            {
                if(item == null || ClassUtil.isPrimitiveType(item.getClass()))
                {
                    result.addValue(item);
                }
                else
                {
                    //Complex type - We need to go deeper!
                    buildObject(item, query, result.createChildMap(), context);
                }
            }
        }
    }

    private void buildMap(Map<?,?> instance, Query query, MapBackedModel<?> result, CatnapContext context)
    {
        if(instance != null)
        {
            Iterator iter = instance.keySet().iterator();

            while(iter.hasNext())
            {
                Object key = iter.next();
                Object value = instance.get(key);

                if(value == null || ClassUtil.isPrimitiveType(value.getClass()))
                {
                    result.addValue(key.toString(), value);
                }
                else
                {
                    buildObject(value, query, result.createChildMap(key.toString()), context);
                }
            }
        }
    }

    private <T> void buildObject(T instance, Query query, MapBackedModel<?> result, CatnapContext context)
    {
        if(instance != null)
        {
            Class<T> instanceClazz = ClassUtil.loadClass(instance);
            filterObject(instance, instanceClazz, query, result, context);
        }
    }

    private <T> void filterObject(T instance, Class<T> instanceClazz, Query query, MapBackedModel<?> result, CatnapContext context)
    {
        QueryProcessor queryProcessor = QueryProcessorFactory.createQueryProcessor(query, instanceClazz);
        List<Property<T>> properties = queryProcessor.process(query, instance, instanceClazz);

        for(Property<T> property : properties)
        {
            Object value = property.getValue();

            if(value == null)
            {
                continue;
            }

            String name = property.getName();

            if(property.isPrimitive())
            {
                result.addValue(name, value);
            }
            else
            {
                //Recursively filtering nested subqueries
                Query subQuery = null;

                if(query != null)
                {
                    subQuery = query.getSubquery(name);
                }

                if(Iterable.class.isAssignableFrom(value.getClass()))
                {
                    buildList((Iterable<?>) value, subQuery, result.createChildList(name), context);
                }
                else if(Map.class.isAssignableFrom(value.getClass()))
                {
                    buildMap((Map<?,?>) value, subQuery, result.createChildMap(name), context);
                }
                else
                {
                    buildObject(value, subQuery, result.createChildMap(name), context);
                }
            }
        }
    }
}
