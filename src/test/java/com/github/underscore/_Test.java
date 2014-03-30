/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2014 Valentyn Kolesnikov
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.github.underscore;

import java.util.*;
import org.junit.Test;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Underscore library unit test.
 *
 * @author Valentyn Kolesnikov
 */
public class _Test {

/*
_.each([1, 2, 3], alert);
=> alerts each number in turn...
*/
    @Test
    public void each() {
        final List<Integer> result = new ArrayList<Integer>();
        _.<Integer>each(asList(1, 2, 3), new Block<Integer>() {
            public void apply(Integer item) {
                result.add(item);
            }
        });
        assertEquals("[1, 2, 3]", result.toString());
    }

/*
_.each({one: 1, two: 2, three: 3}, alert);
=> alerts each number value in turn...
*/
    @Test
    public void eachMap() {
        final List<String> result = new ArrayList<String>();
        _.<Map.Entry<String, Integer>>each(new LinkedHashMap<String, Integer>() {{ put("one", 1); put("two", 2); put("three", 3); }}.entrySet(),
            new Block<Map.Entry<String, Integer>>() {
            public void apply(Map.Entry<String,Integer> item) {
                result.add(item.getKey());
            }
        });
        assertEquals("[one, two, three]", result.toString());
    }

/*
_.map([1, 2, 3], function(num){ return num * 3; });
=> [3, 6, 9]
*/
    @Test
    public void map() {
        List<Integer> result = _.<Integer, Integer>map(asList(1, 2, 3), new Function1<Integer, Integer>() {
            public Integer apply(Integer item) {
                return item * 3;
            }
        });
        assertEquals("[3, 6, 9]", result.toString());
    }

/*
_.map({one: 1, two: 2, three: 3}, function(num, key){ return num * 3; });
=> [3, 6, 9]
*/
    @Test
    public void mapMap() {
        final Set<Integer> result =
        _.map(new LinkedHashMap<Integer, String>() {{ put(1, "one"); put(2, "two"); put(3, "three"); }}.entrySet(),
            new Function1<Map.Entry<Integer, String>, Integer>() {
            public Integer apply(Map.Entry<Integer, String> item) {
                return item.getKey() * 3;
            }
        });
        assertEquals("[3, 6, 9]", result.toString());
    }

/*
var sum = _.reduce([1, 2, 3], function(memo, num){ return memo + num; }, 0);
=> 6
*/
    @Test
    public void reduce() {
        final Integer result =
        _.reduce(asList(1, 2, 3),
            0,
            new Function2<Integer, Integer, Integer>() {
            public Integer apply(Integer item1, Integer item2) {
                return item1 + item2;
            }
        });
        assertEquals("6", result.toString());
    }

/*
var list = [[0, 1], [2, 3], [4, 5]];
var flat = _.foldl(list, function(a, b) { return a.concat(b); }, []);
=> [4, 5, 2, 3, 0, 1]
*/
    @Test
    public void foldl() {
        final List<Integer> result =
        _.foldl(asList(asList(0, 1), asList(2, 3), asList(4, 5)),
            Collections.<Integer>emptyList(),
            new Function2<List<Integer>, List<Integer>, List<Integer>>() {
            public List<Integer> apply(List<Integer> item1, List<Integer> item2) {
                List<Integer> list = new ArrayList<Integer>(item2);
                list.addAll(item1);
                return list;
            }
        });
        assertEquals("[4, 5, 2, 3, 0, 1]", result.toString());
    }

/*
_.contains([1, 2, 3], 3);
=> true
*/
    @Test
    public void contains() {
        final boolean result = _.contains(asList(1, 2, 3), 3);
        assertTrue(result);
    }

/*
var even = _.find([1, 2, 3, 4, 5, 6], function(num){ return num % 2 == 0; });
=> 2
*/
    @Test
    public void find() {
        final Integer result = _.find(asList(1, 2, 3, 4, 5, 6), 
            new Predicate<Integer>() {
            public Boolean apply(Integer item) {
                return item % 2 == 0;
            }
        });
        assertEquals("2", result.toString());
    }

/*
var evens = _.filter([1, 2, 3, 4, 5, 6], function(num){ return num % 2 == 0; });
=> [2, 4, 6]
*/
    @Test
    public void filter() {
        final List<Integer> result = _.filter(asList(1, 2, 3, 4, 5, 6), 
            new Predicate<Integer>() {
            public Boolean apply(Integer item) {
                return item % 2 == 0;
            }
        });
        assertEquals("[2, 4, 6]", result.toString());
    }

/*
_.first([5, 4, 3, 2, 1]);
=> 5
*/
    @Test
    public void first() {
        final Integer result = _.first(asList(5, 4, 3, 2, 1));
        assertEquals("5", result.toString());
    }

/*
_.initial([5, 4, 3, 2, 1]);
=> [5, 4, 3, 2]
*/
    @Test
    public void initial() {
        final List<Integer> result = _.initial(asList(5, 4, 3, 2, 1), 1);
        assertEquals("[5, 4, 3, 2]", result.toString());
    }

/*
_.last([5, 4, 3, 2, 1]);
=> 1
*/
    @Test
    public void last() {
        final Integer result = _.last(asList(5, 4, 3, 2, 1));
        assertEquals("1", result.toString());
    }

/*
_.flatten([1, [2], [3, [[4]]]]);
=> [1, 2, 3, 4];
*/
    @Test
    public void flatten() {
        final List<Integer> result = _.flatten(asList(1, asList(2, asList(3, asList(asList(4))))));
        assertEquals("[1, 2, 3, 4]", result.toString());
    }

/*
_.compact([0, 1, false, 2, '', 3]);
=> [1, 2, 3]
*/
    @Test
    public void compact() {
        final List<?> result = _.compact(asList(0, 1, false, 2, "", 3));
        assertEquals("[1, 2, 3]", result.toString());
    }

/*
_.without([1, 2, 1, 0, 3, 1, 4], 0, 1);
=> [2, 3, 4]
*/
    @Test
    public void without() {
        final List<Integer> result = _.without(asList(1, 2, 1, 0, 3, 1, 4), 0, 1);
        assertEquals("[2, 3, 4]", result.toString());
    }

/*
var numbers = [10, 5, 100, 2, 1000];
_.max(numbers);
=> 1000
*/
    @Test
    public void max() {
        final Integer result = _.max(asList(10, 5, 100, 2, 1000));
        assertEquals("1000", result.toString());
    }

/*
var numbers = [10, 5, 100, 2, 1000];
_.min(numbers);
=> 2
*/
    @Test
    public void min() {
        final Integer result = _.min(asList(10, 5, 100, 2, 1000));
        assertEquals("2", result.toString());
    }

/*
_.sample([1, 2, 3, 4, 5, 6]);
=> 4

_.sample([1, 2, 3, 4, 5, 6], 3);
=> [1, 6, 2]
*/
    @Test
    public void sample() {
        final Integer result = _.sample(asList(1, 2, 3, 4, 5, 6));
        assertTrue(result >= 1 && result <= 6);
        final Set<Integer> resultList = _.sample(asList(1, 2, 3, 4, 5, 6), 3);
        assertEquals(3, resultList.size());
    }

/*
var stooges = [{name: 'moe', age: 40}, {name: 'larry', age: 50}, {name: 'curly', age: 60}];
_.pluck(stooges, 'name');
=> ["moe", "larry", "curly"]
*/
    @Test
    public void pluck() throws Exception {
        class Person {
            public final String name;
            public final Integer age;
            public Person(final String name, final Integer age) {
                this.name = name;
                this.age = age;
            }
        };
        final List<?> result =
        _.pluck(new ArrayList<Person>() {{ add(new Person("moe", 40)); add(new Person("larry", 50)); add(new Person("curly", 40));}}, "name");
        assertEquals("[moe, larry, curly]", result.toString());
    }
}
