package com.catnap.core.query.processor;

import com.catnap.core.annotation.CatnapOrder;
import com.catnap.core.exception.CatnapException;
import com.catnap.core.util.ClassUtil;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests verifying correct sorting of fields based on both the {@link com.catnap.core.annotation.CatnapOrder}
 * and {@link com.fasterxml.jackson.annotation.JsonPropertyOrder} annotations.
 *
 * @author gwhit7
 */
public class AnnotationComparatorTest
{
    @CatnapOrder({
            "second",
            "first"
    })
    static class BeanWithCatnapOrder
    {
        private String first = "first";
        private String second = "second";

        public String getFirst() { return first; }

        public String getSecond() { return second; }
    }

    @JsonPropertyOrder({
            "second",
            "first"
    })
    static class BeanWithJsonPropertyOrder
    {
        private String first = "first";
        private String second = "second";

        public String getFirst() { return first; }

        public String getSecond() { return second; }
    }

    static class BeanWithoutOrderAnnotation
    {
        private String first = "first";
        private String second = "second";

        public String getFirst() { return first; }

        public String getSecond() { return second; }
    }

    @CatnapOrder(value = {
            "second",
            "first"
    }, alphabetic = true)
    static class BeanWithOrphans
    {
        private String first = "first";
        private String second = "second";
        private String orphanC = "orphanC";
        private String orphanB = "orphanB";
        private String orphanA = "orphanA";

        public String getFirst() { return first; }

        public String getSecond() { return second; }

        public String getOrphanC() { return orphanC; }

        public String getOrphanB() { return orphanB; }

        public String getOrphanA() { return orphanA; }
    }

    @Test
    public void compareCatnapOrderLessThan()
    {
        AnnotationComparator<BeanWithCatnapOrder> comparator = new AnnotationComparator<BeanWithCatnapOrder>(BeanWithCatnapOrder.class);

        BeanWithCatnapOrder model = new BeanWithCatnapOrder();
        Property<BeanWithCatnapOrder> first = new SimpleProperty<BeanWithCatnapOrder>(model,
                ClassUtil.getReadableProperty("first", BeanWithCatnapOrder.class));
        Property<BeanWithCatnapOrder> second = new SimpleProperty<BeanWithCatnapOrder>(model,
                ClassUtil.getReadableProperty("second", BeanWithCatnapOrder.class));

        assertEquals(-1, comparator.compare(second, first));
    }

    @Test
    public void compareCatnapOrderEquals()
    {
        AnnotationComparator<BeanWithCatnapOrder> comparator = new AnnotationComparator<BeanWithCatnapOrder>(BeanWithCatnapOrder.class);

        BeanWithCatnapOrder model = new BeanWithCatnapOrder();
        Property<BeanWithCatnapOrder> first = new SimpleProperty<BeanWithCatnapOrder>(model,
                ClassUtil.getReadableProperty("first", BeanWithCatnapOrder.class));

        assertEquals(0, comparator.compare(first, first));
    }

    @Test
    public void compareCatnapOrderGreaterThan()
    {
        AnnotationComparator<BeanWithCatnapOrder> comparator = new AnnotationComparator<BeanWithCatnapOrder>(BeanWithCatnapOrder.class);

        BeanWithCatnapOrder model = new BeanWithCatnapOrder();
        Property<BeanWithCatnapOrder> first = new SimpleProperty<BeanWithCatnapOrder>(model,
                ClassUtil.getReadableProperty("first", BeanWithCatnapOrder.class));
        Property<BeanWithCatnapOrder> second = new SimpleProperty<BeanWithCatnapOrder>(model,
                ClassUtil.getReadableProperty("second", BeanWithCatnapOrder.class));

        assertEquals(1, comparator.compare(first, second));
    }

    @Test
    public void compareJsonPropertyOrderLessThan()
    {
        AnnotationComparator<BeanWithJsonPropertyOrder> comparator = new AnnotationComparator<BeanWithJsonPropertyOrder>(BeanWithJsonPropertyOrder.class);

        BeanWithJsonPropertyOrder model = new BeanWithJsonPropertyOrder();
        Property<BeanWithJsonPropertyOrder> first = new SimpleProperty<BeanWithJsonPropertyOrder>(model,
                ClassUtil.getReadableProperty("first", BeanWithJsonPropertyOrder.class));
        Property<BeanWithJsonPropertyOrder> second = new SimpleProperty<BeanWithJsonPropertyOrder>(model,
                ClassUtil.getReadableProperty("second", BeanWithJsonPropertyOrder.class));

        assertEquals(-1, comparator.compare(second, first));
    }

    @Test
    public void compareJsonPropertyOrderEquals()
    {
        AnnotationComparator<BeanWithJsonPropertyOrder> comparator = new AnnotationComparator<BeanWithJsonPropertyOrder>(BeanWithJsonPropertyOrder.class);

        BeanWithJsonPropertyOrder model = new BeanWithJsonPropertyOrder();
        Property<BeanWithJsonPropertyOrder> first = new SimpleProperty<BeanWithJsonPropertyOrder>(model,
                ClassUtil.getReadableProperty("first", BeanWithJsonPropertyOrder.class));

        assertEquals(0, comparator.compare(first, first));
    }

    @Test
    public void compareJsonPropertyOrderGreaterThan()
    {
        AnnotationComparator<BeanWithJsonPropertyOrder> comparator = new AnnotationComparator<BeanWithJsonPropertyOrder>(BeanWithJsonPropertyOrder.class);

        BeanWithJsonPropertyOrder model = new BeanWithJsonPropertyOrder();
        Property<BeanWithJsonPropertyOrder> first = new SimpleProperty<BeanWithJsonPropertyOrder>(model,
                ClassUtil.getReadableProperty("first", BeanWithJsonPropertyOrder.class));
        Property<BeanWithJsonPropertyOrder> second = new SimpleProperty<BeanWithJsonPropertyOrder>(model,
                ClassUtil.getReadableProperty("second", BeanWithJsonPropertyOrder.class));

        assertEquals(1, comparator.compare(first, second));
    }

    @Test(expected = CatnapException.class)
    public void verifyBeanWithMissingOrderAnnotationThrowsException()
    {
        AnnotationComparator<BeanWithoutOrderAnnotation> comparator = new AnnotationComparator<BeanWithoutOrderAnnotation>(BeanWithoutOrderAnnotation.class);

        BeanWithoutOrderAnnotation model = new BeanWithoutOrderAnnotation();
        Property<BeanWithoutOrderAnnotation> first = new SimpleProperty<BeanWithoutOrderAnnotation>(model,
                ClassUtil.getReadableProperty("first", BeanWithoutOrderAnnotation.class));
        Property<BeanWithoutOrderAnnotation> second = new SimpleProperty<BeanWithoutOrderAnnotation>(model,
                ClassUtil.getReadableProperty("second", BeanWithoutOrderAnnotation.class));

        comparator.compare(first, second);
    }

    @Test
    public void verifyOrphanFieldsAreSortedAlphabetically()
    {
        BeanWithOrphans model = new BeanWithOrphans();
        List<Property<BeanWithOrphans>> props = new ArrayList<Property<BeanWithOrphans>>();

        props.add(new SimpleProperty<BeanWithOrphans>(model, ClassUtil.getReadableProperty("first", BeanWithOrphans.class)));
        props.add(new SimpleProperty<BeanWithOrphans>(model, ClassUtil.getReadableProperty("second", BeanWithOrphans.class)));
        props.add(new SimpleProperty<BeanWithOrphans>(model, ClassUtil.getReadableProperty("orphanA", BeanWithOrphans.class)));
        props.add(new SimpleProperty<BeanWithOrphans>(model, ClassUtil.getReadableProperty("orphanB", BeanWithOrphans.class)));
        props.add(new SimpleProperty<BeanWithOrphans>(model, ClassUtil.getReadableProperty("orphanC", BeanWithOrphans.class)));

        Collections.sort(props, new AnnotationComparator<BeanWithOrphans>(BeanWithOrphans.class));

        assertEquals("second", props.get(0).getName());
        assertEquals("first", props.get(1).getName());
        assertEquals("orphanA", props.get(2).getName());
        assertEquals("orphanB", props.get(3).getName());
        assertEquals("orphanC", props.get(4).getName());
    }
}
