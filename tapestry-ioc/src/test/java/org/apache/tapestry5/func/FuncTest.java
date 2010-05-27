// Copyright 2010 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry5.func;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.tapestry5.ioc.internal.util.Defense;
import org.apache.tapestry5.ioc.test.TestBase;
import org.testng.annotations.Test;

public class FuncTest extends TestBase
{
    private Mapper<String, Integer> stringToLength = new Mapper<String, Integer>()
    {
        public Integer map(String input)
        {
            return input.length();
        }
    };

    private Mapper<Integer, Boolean> toEven = new Mapper<Integer, Boolean>()
    {
        public Boolean map(Integer input)
        {
            return evenp.accept(input);
        }
    };

    private Predicate<Number> evenp = new Predicate<Number>()
    {
        public boolean accept(Number object)
        {
            return object.longValue() % 2 == 0;
        };
    };

    @Test
    public void map()
    {
        List<String> source = Arrays.asList("Mary", "had", "a", "little", "lamb");
        Defense.notNull(source, "source");

        List<Integer> lengths = F.flow(source).map(stringToLength).toList();

        assertListsEquals(lengths, 4, 3, 1, 6, 4);
    }

    @Test
    public void flow_map()
    {
        assertListsEquals(F.flow("Mary", "had", "a", "little", "lamb").map(stringToLength).toList(), 4, 3, 1, 6, 4);
    }

    @Test
    public void flow_reverse()
    {
        assertListsEquals(F.flow(1, 2, 3).reverse().toList(), 3, 2, 1);
    }

    @Test
    public void combine_mappers()
    {
        List<Boolean> even = F.flow("Mary", "had", "a", "little", "lamb").map(stringToLength.combine(toEven)).toList();

        assertListsEquals(even, true, false, false, true, true);
    }

    @Test
    public void map_empty_collection_is_the_empty_list()
    {
        List<String> source = Arrays.asList();
        Defense.notNull(source, "source");

        List<Integer> lengths = F.flow(source).map(stringToLength).toList();

        assertSame(lengths, Collections.EMPTY_LIST);
    }

    @Test
    public void each()
    {
        List<String> source = Arrays.asList("Mary", "had", "a", "little", "lamb");

        final StringBuffer buffer = new StringBuffer();

        Worker<String> worker = new Worker<String>()
        {
            public void work(String value)
            {
                if (buffer.length() > 0)
                    buffer.append(" ");

                buffer.append(value);
            }
        };

        F.flow(source).each(worker);

        assertEquals(buffer.toString(), "Mary had a little lamb");
    }

    @Test
    public void flow_each()
    {
        Flow<String> flow = F.flow("Mary", "had", "a", "little", "lamb");

        final StringBuffer buffer = new StringBuffer();

        Worker<String> worker = new Worker<String>()
        {
            public void work(String value)
            {
                if (buffer.length() > 0)
                    buffer.append(" ");

                buffer.append(value);
            }
        };

        assertSame(flow.each(worker), flow);

        assertEquals(buffer.toString(), "Mary had a little lamb");
    }

    @Test
    public void combine_workers()
    {
        final StringBuffer buffer = new StringBuffer();

        Worker<String> appendWorker = new Worker<String>()
        {
            public void work(String value)
            {
                if (buffer.length() > 0)
                    buffer.append(" ");

                buffer.append(value);
            }
        };

        Worker<String> appendLength = new Worker<String>()
        {
            public void work(String value)
            {
                buffer.append("(");
                buffer.append(value.length());
                buffer.append(")");
            }
        };

        F.flow("Mary", "had", "a", "little", "lamb").each(appendWorker.combine(appendLength));

        assertEquals(buffer.toString(), "Mary(4) had(3) a(1) little(6) lamb(4)");
    }

    @Test
    public void filter()
    {
        List<Integer> input = Arrays.asList(1, 2, 3, 4, 5, 6, 7);

        List<Integer> output = F.flow(input).filter(evenp).toList();

        assertListsEquals(output, 2, 4, 6);
    }

    @Test
    public void flow_filter()
    {
        assertListsEquals(F.flow(1, 2, 3, 4, 5, 6, 7).filter(evenp).toList(), 2, 4, 6);
    }

    @Test
    public void remove()
    {
        List<Integer> input = Arrays.asList(1, 2, 3, 4, 5, 6, 7);

        List<Integer> output = F.flow(input).remove(evenp).toList();

        assertListsEquals(output, 1, 3, 5, 7);
    }

    @Test
    public void flow_remove()
    {
        List<Integer> output = F.flow(1, 2, 3, 4, 5, 6, 7).remove(evenp).toList();

        assertListsEquals(output, 1, 3, 5, 7);
    }

    @Test
    public void filter_empty_is_the_empty_list()
    {
        List<Integer> input = Arrays.asList();

        List<Integer> output = F.flow(input).filter(evenp).toList();

        assertSame(output, Collections.EMPTY_LIST);
    }

    @Test
    public void combine_predicate_with_and()
    {
        List<Integer> input = Arrays.asList(1, 2, 3, 4, 5, 6, 7);

        List<Integer> output = F.flow(input).filter(evenp.and(F.gt(3))).toList();

        assertListsEquals(output, 4, 6);
    }

    @Test
    public void numeric_comparison()
    {
        List<Integer> input = Arrays.asList(1, 2, 3, 4, 5, 6, 7);

        assertEquals(F.flow(input).filter(F.eq(3)).toList(), Arrays.asList(3));
        assertEquals(F.flow(input).filter(F.neq(3)).toList(), Arrays.asList(1, 2, 4, 5, 6, 7));
        assertEquals(F.flow(input).filter(F.lt(3)).toList(), Arrays.asList(1, 2));
        assertEquals(F.flow(input).filter(F.lteq(3)).toList(), Arrays.asList(1, 2, 3));
        assertEquals(F.flow(input).filter(F.gteq(3)).toList(), Arrays.asList(3, 4, 5, 6, 7));
    }

    @Test
    public void select_and_filter()
    {
        Predicate<String> combinedp = F.toPredicate(stringToLength.combine(toEven));

        Mapper<String, String> identity = F.identity();
        Predicate<String> isNull = F.isNull();

        // Converting to null and then filtering out nulls is the hard way to do filter or remove,
        // but exercises the code we want to test.

        List<String> filtered = F.flow("Mary", "had", "a", "little", "lamb").map(F.select(combinedp, identity)).remove(
                isNull).toList();

        assertListsEquals(filtered, "Mary", "little", "lamb");
    }

    @Test
    public void null_and_not_null()
    {
        Predicate<String> isNull = F.isNull();
        Predicate<String> isNotNull = F.notNull();

        assertEquals(isNull.accept(null), true);
        assertEquals(isNotNull.accept(null), false);

        assertEquals(isNull.accept("foo"), false);
        assertEquals(isNotNull.accept("bar"), true);
    }

    @Test
    public void reduce()
    {
        int total = F.flow(F.flow("Mary", "had", "a", "little", "lamb").map(stringToLength).toList()).reduce(F.SUM_INTS, 0);

        assertEquals(total, 18);
    }

    @Test
    public void flow_reduce()
    {
        int total = F.flow("Mary", "had", "a", "little", "lamb").map(stringToLength).reduce(F.SUM_INTS, 0);

        assertEquals(total, 18);
    }
}