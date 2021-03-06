package com.catnap.core.query.processor;

import static org.junit.Assert.*;

import com.catnap.core.annotation.CatnapIgnore;
import com.catnap.core.query.model.Query;
import com.catnap.core.util.ClassUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.junit.Test;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author gwhit7
 */
public class QueryProcessorTest
{
    static class MockQueryProcessor extends QueryProcessor
    {
        private List<String> processOrder = new ArrayList<String>(3);

        @Override
        public boolean supports(Class<? extends Query<?>> query)
        {
            return true;
        }

        @Override
        protected <T> void preProcess(Query query, T instance, Class<T> instanceClazz)
        {
            super.preProcess(query, instance, instanceClazz);
            processOrder.add("preProcess");
        }

        @Override
        protected <T> List<Property<T>> processInternal(Query query, T instance, Class<T> instanceClazz)
        {
            processOrder.add("processInternal");
            return null;
        }

        @Override
        protected <T> void postProcess(List<Property<T>> properties, Query query, T instance, Class<T> instanceClazz)
        {
            super.postProcess(properties, query, instance, instanceClazz);
            processOrder.add("postProcess");
        }

        @Override
        protected boolean ignoreProperty(PropertyDescriptor descriptor) {
            return super.ignoreProperty(descriptor);
        }

        public List<String> getProcessOrder()
        {
            return processOrder;
        }
    }

    static class MockModel
    {
        private String property1 = "value1";
        private String property2 = "value2";    //Ignored by @CatnapIgnore
        private String property3 = "value3";    //Ignored by @JsonIgnore
        private String property4 = "value4";    //Ignored by "No Getter"

        public String getProperty1()
        {
            return property1;
        }

        @CatnapIgnore
        public String getProperty2()
        {
            return property2;
        }

        @JsonIgnore
        public String getProperty3()
        {
            return property3;
        }
    }

    @Test
    public void validateProcessOrder()
    {
        MockQueryProcessor processor = new MockQueryProcessor();
        processor.process(null, new MockModel(), MockModel.class);

        assertEquals("preProcess", processor.getProcessOrder().get(0));
        assertEquals("processInternal", processor.getProcessOrder().get(1));
        assertEquals("postProcess", processor.getProcessOrder().get(2));
    }

    @Test
    public void doNotIgnoreMethodsWithoutIgnoreAnnotations()
    {
        PropertyDescriptor descriptor = ClassUtil.getReadableProperty("property1", MockModel.class);

        MockQueryProcessor processor = new MockQueryProcessor();
        assertFalse(processor.ignoreProperty(descriptor));
    }

    @Test
    public void ignoreMethodsAnnotatedWithCatnapIgnore()
    {
        PropertyDescriptor descriptor = ClassUtil.getReadableProperty("property2", MockModel.class);

        MockQueryProcessor processor = new MockQueryProcessor();
        assertTrue(processor.ignoreProperty(descriptor));
    }

    @Test
    public void ignoreMethodsAnnotatedWithJsonIgnore()
    {
        PropertyDescriptor descriptor = ClassUtil.getReadableProperty("property3", MockModel.class);

        MockQueryProcessor processor = new MockQueryProcessor();
        assertTrue(processor.ignoreProperty(descriptor));
    }

    @Test
    public void ignoreMethodsWithoutGetters()
    {
        PropertyDescriptor descriptor = ClassUtil.getReadableProperty("property4", MockModel.class);

        MockQueryProcessor processor = new MockQueryProcessor();
        assertTrue(processor.ignoreProperty(descriptor));
    }

    @Test
    public void ignoreMethodsWithNullDescriptors()
    {
        MockQueryProcessor processor = new MockQueryProcessor();
        assertTrue(processor.ignoreProperty(null));
    }
}
