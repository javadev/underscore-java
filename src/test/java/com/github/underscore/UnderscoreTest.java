/*
 * The MIT License (MIT)
 * 
 * Copyright 2015 Valentyn Kolesnikov
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
import java.lang.reflect.Method;
import org.junit.Test;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

/**
 * Underscore library unit test.
 *
 * @author Valentyn Kolesnikov
 */
public class UnderscoreTest {

/*
_.each([1, 2, 3], alert);
=> alerts each number in turn...
*/
    @Test
    public void each() {
        final List<Integer> result = new ArrayList<Integer>();
        $.<Integer>each(asList(1, 2, 3), new Block<Integer>() {
            public void apply(Integer item) {
                result.add(item);
            }
        });
        assertEquals("[1, 2, 3]", result.toString());
    }

/*
_.forEach([1, 2, 3], alert);
=> alerts each number in turn...
*/
    @Test
    public void forEach() {
        final List<Integer> result = new ArrayList<Integer>();
        $.forEach(asList(1, 2, 3), new Block<Integer>() {
            public void apply(Integer item) {
                result.add(item);
            }
        });
        assertEquals("[1, 2, 3]", result.toString());
    }

/*
_([1, 2, 3]).forEach(alert);
=> alerts each number in turn...
*/
    @Test
    public void forEachObj() {
        final List<Integer> result = new ArrayList<Integer>();
        new $(asList(1, 2, 3)).forEach(new Block<Integer>() {
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
        $.<Map.Entry<String, Integer>>each(new LinkedHashMap<String, Integer>() {{ put("one", 1); put("two", 2); put("three", 3); }}.entrySet(),
            new Block<Map.Entry<String, Integer>>() {
            public void apply(Map.Entry<String, Integer> item) {
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
        List<Integer> result = $.map(asList(1, 2, 3), new Function1<Integer, Integer>() {
            public Integer apply(Integer item) {
                return item * 3;
            }
        });
        assertEquals("[3, 6, 9]", result.toString());
    }

/*
_.mapObject({start: 5, end: 12}, function(val, key) {
  return val + 5;
});
=> {start: 10, end: 17}
*/
    @Test
    public void mapObject() {
        List<Tuple<String, Integer>> result = $.mapObject(new LinkedHashMap<String, Integer>() {{ put("start", 5); put("end", 12); }}, new Function1<Integer, Integer>() {
            public Integer apply(Integer item) {
                return item + 5;
            }
        });
        assertEquals("[(start, 10), (end, 17)]", result.toString());
    }

/*
_.pairs({one: 1, two: 2, three: 3});
=> [["one", 1], ["two", 2], ["three", 3]]
*/
    @Test
    public void pairs() {
        List<Tuple<String, Integer>> result = $.pairs(new LinkedHashMap<String, Integer>() {{ put("one", 1); put("two", 2); put("three", 3); }});
        assertEquals("[(one, 1), (two, 2), (three, 3)]", result.toString());
    }

/*
_.invert({Moe: "Moses", Larry: "Louis", Curly: "Jerome"});
=> {Moses: "Moe", Louis: "Larry", Jerome: "Curly"};
*/
    @Test
    public void invert() {
        List<Tuple<String, String>> result = $.invert(new LinkedHashMap<String, String>() {{ put("Moe", "Moses"); put("Larry", "Louis"); put("Curly", "Jerome"); }});
        assertEquals("[(Moses, Moe), (Louis, Larry), (Jerome, Curly)]", result.toString());
    }

/*
_.functions(_);
=> ["all", "any", "bind", "bindAll", "clone", "compact", "compose" ...
*/
    @Test
    public void functions() {
        List<String> result = $.functions(new $(""));
        assertEquals("[after, all, any, before, bind]", $.first(result, 5).toString());
    }

/*
_.collect([1, 2, 3], function(num){ return num * 3; });
=> [3, 6, 9]
*/
    @Test
    public void collect() {
        List<Integer> result = $.collect(asList(1, 2, 3), new Function1<Integer, Integer>() {
            public Integer apply(Integer item) {
                return item * 3;
            }
        });
        assertEquals("[3, 6, 9]", result.toString());
        Set<Integer> resultSet = $.collect(new LinkedHashSet(asList(1, 2, 3)), new Function1<Integer, Integer>() {
            public Integer apply(Integer item) {
                return item * 3;
            }
        });
        assertEquals("[3, 6, 9]", resultSet.toString());
    }
/*
_.map({one: 1, two: 2, three: 3}, function(num, key){ return num * 3; });
=> [3, 6, 9]
*/
    @Test
    public void mapMap() {
        final Set<Integer> result =
        $.map(new LinkedHashMap<Integer, String>() {{ put(1, "one"); put(2, "two"); put(3, "three"); }}.entrySet(),
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
        $.reduce(asList(1, 2, 3),
            new FunctionAccum<Integer, Integer>() {
            public Integer apply(Integer item1, Integer item2) {
                return item1 + item2;
            }
        },
        0);
        assertEquals("6", result.toString());
    }

/*
var list = [[0, 1], [2, 3], [4, 5]];
var flat = _.reduceRight(list, function(a, b) { return a.concat(b); }, []);
=> [4, 5, 2, 3, 0, 1]
*/
    @Test
    public void reduceRight() {
        final List<Integer> result =
        $.reduceRight(asList(asList(0, 1), asList(2, 3), asList(4, 5)),
            new FunctionAccum<List<Integer>, List<Integer>>() {
            public List<Integer> apply(List<Integer> item1, List<Integer> item2) {
                List<Integer> list = new ArrayList<Integer>(item1);
                list.addAll(item2);
                return list;
            }
        },
        Collections.<Integer>emptyList()
        );
        assertEquals("[4, 5, 2, 3, 0, 1]", result.toString());
    }

/*
var list = [[0, 1], [2, 3], [4, 5]];
var flat = _.inject(list, function(a, b) { return a.concat(b); }, []);
=> [0, 1, 2, 3, 4, 5]
*/
    @Test
    public void inject() {
        final List<Integer> result =
        $.inject(asList(asList(0, 1), asList(2, 3), asList(4, 5)),
            new FunctionAccum<List<Integer>, List<Integer>>() {
            public List<Integer> apply(List<Integer> item1, List<Integer> item2) {
                List<Integer> list = new ArrayList<Integer>(item1);
                list.addAll(item2);
                return list;
            }
        },
        Collections.<Integer>emptyList()
        );
        assertEquals("[0, 1, 2, 3, 4, 5]", result.toString());
    }

/*
var list = [[0, 1], [2, 3], [4, 5]];
var flat = _.foldl(list, function(a, b) { return a.concat(b); }, []);
=> [0, 1, 2, 3, 4, 5]
*/
    @Test
    public void foldl() {
        final List<Integer> result =
        $.foldl(asList(asList(0, 1), asList(2, 3), asList(4, 5)),
            new FunctionAccum<List<Integer>, List<Integer>>() {
            public List<Integer> apply(List<Integer> item1, List<Integer> item2) {
                List<Integer> list = new ArrayList<Integer>(item1);
                list.addAll(item2);
                return list;
            }
        },
        Collections.<Integer>emptyList()
        );
        assertEquals("[0, 1, 2, 3, 4, 5]", result.toString());
    }

/*
var list = [[0, 1], [2, 3], [4, 5]];
var flat = _.foldr(list, function(a, b) { return a.concat(b); }, []);
=> [4, 5, 2, 3, 0, 1]
*/
    @Test
    public void foldr() {
        final List<Integer> result =
        $.foldr(asList(asList(0, 1), asList(2, 3), asList(4, 5)),
            new FunctionAccum<List<Integer>, List<Integer>>() {
            public List<Integer> apply(List<Integer> item1, List<Integer> item2) {
                List<Integer> list = new ArrayList<Integer>(item1);
                list.addAll(item2);
                return list;
            }
        },
        Collections.<Integer>emptyList()
        );
        assertEquals("[4, 5, 2, 3, 0, 1]", result.toString());
    }

/*
_.contains([1, 2, 3], 3);
=> true
*/
    @Test
    public void contains() {
        final boolean result = $.contains(asList(1, 2, 3), 3);
        assertTrue(result);
        final boolean resultChain = (Boolean) $.chain(asList(1, 2, 3)).contains(3).item();
        assertTrue(resultChain);
        final boolean result2 = $.contains(asList(1, 2, 3), 3, 1);
        assertTrue(result2);
        final boolean result3 = $.contains(asList(1, 2, 3), 1, 1);
        assertFalse(result3);
        final boolean result4 = $.contains(asList(1, 2, null), null);
        assertTrue(result4);
    }

/*
var even = _.find([1, 2, 3, 4, 5, 6], function(num){ return num % 2 == 0; });
=> 2
*/
    @Test
    public void find() {
        final Optional<Integer> result = $.find(asList(1, 2, 3, 4, 5, 6),
            new Predicate<Integer>() {
            public Boolean apply(Integer item) {
                return item % 2 == 0;
            }
        });
        assertEquals("Optional.of(2)", result.toString());
        final Optional<Integer> resultChain = (Optional<Integer>) $.chain(asList(1, 2, 3, 4, 5, 6)).find(
            new Predicate<Integer>() {
            public Boolean apply(Integer item) {
                return item % 2 == 0;
            }
        }).item();
        assertEquals("Optional.of(2)", resultChain.toString());
        final Optional<Integer> resultChain2 = (Optional<Integer>) $.chain(asList(1, 2, 3, 4, 5, 6)).find(
            new Predicate<Integer>() {
            public Boolean apply(Integer item) {
                return item > 6;
            }
        }).item();
        assertEquals("Optional.absent()", resultChain2.toString());
    }

/*
var even = _.findLast([1, 2, 3, 4, 5, 6], function(num){ return num % 2 == 0; });
=> 6
*/
    @Test
    public void findLast() {
        final Optional<Integer> result = $.findLast(asList(1, 2, 3, 4, 5, 6),
            new Predicate<Integer>() {
            public Boolean apply(Integer item) {
                return item % 2 == 0;
            }
        });
        assertEquals("Optional.of(6)", result.toString());
        final Optional<Integer> result2 = $.findLast(asList(1, 2, 3, 4, 5, 6),
            new Predicate<Integer>() {
            public Boolean apply(Integer item) {
                return item > 6;
            }
        });
        assertEquals("Optional.absent()", result2.toString());
        final Optional<Integer> resultChain = (Optional<Integer>) $.chain(asList(1, 2, 3, 4, 5, 6)).findLast(
            new Predicate<Integer>() {
            public Boolean apply(Integer item) {
                return item % 2 == 0;
            }
        }).item();
        assertEquals("Optional.of(6)", resultChain.toString());
        final Optional<Integer> resultChain2 = (Optional<Integer>) $.chain(asList(1, 2, 3, 4, 5, 6)).findLast(
            new Predicate<Integer>() {
            public Boolean apply(Integer item) {
                return item > 6;
            }
        }).item();
        assertEquals("Optional.absent()", resultChain2.toString());
    }

/*
var even = _.find([1, 2, 3, 4, 5, 6], function(num){ return num % 2 == 0; });
=> 2
*/
    @Test
    public void detect() {
        final Optional<Integer> result = $.detect(asList(1, 2, 3, 4, 5, 6),
            new Predicate<Integer>() {
            public Boolean apply(Integer item) {
                return item % 2 == 0;
            }
        });
        assertEquals("Optional.of(2)", result.toString());
    }

/*
var evens = _.filter([1, 2, 3, 4, 5, 6], function(num){ return num % 2 == 0; });
=> [2, 4, 6]
*/
    @Test
    public void filter() {
        final List<Integer> result = $.filter(asList(1, 2, 3, 4, 5, 6), 
            new Predicate<Integer>() {
            public Boolean apply(Integer item) {
                return item % 2 == 0;
            }
        });
        assertEquals("[2, 4, 6]", result.toString());
    }

/*
var evens = _.reject([1, 2, 3, 4, 5, 6], function(num){ return num % 2 == 0; });
=> [2, 4, 6]
*/
    @Test
    public void reject() {
        final List<Integer> result = $.reject(asList(1, 2, 3, 4, 5, 6), 
            new Predicate<Integer>() {
            public Boolean apply(Integer item) {
                return item % 2 == 0;
            }
        });
        assertEquals("[1, 3, 5]", result.toString());
        final Set<Integer> resultSet = $.reject(new LinkedHashSet(asList(1, 2, 3, 4, 5, 6)), 
            new Predicate<Integer>() {
            public Boolean apply(Integer item) {
                return item % 2 == 0;
            }
        });
        assertEquals("[1, 3, 5]", resultSet.toString());
    }
    
/*
var evens = _.filter([1, 2, 3, 4, 5, 6], function(num){ return num % 2 == 0; });
=> [2, 4, 6]
*/
    @Test
    public void select() {
        final List<Integer> result = $.select(asList(1, 2, 3, 4, 5, 6), 
            new Predicate<Integer>() {
            public Boolean apply(Integer item) {
                return item % 2 == 0;
            }
        });
        assertEquals("[2, 4, 6]", result.toString());
        final Set<Integer> resultSet = $.select(new LinkedHashSet(asList(1, 2, 3, 4, 5, 6)), 
            new Predicate<Integer>() {
            public Boolean apply(Integer item) {
                return item % 2 == 0;
            }
        });
        assertEquals("[2, 4, 6]", resultSet.toString());
    }    

/*
_.all([1, 2, 3, 4], function(num) { return num % 2 === 0; }); // false
_.all([1, 2, 3, 4], function(num) { return num < 5; }); // true
*/
    @Test
    public void all() {
        final Boolean result1 = $.all(asList(1, 2, 3, 4), 
            new Predicate<Integer>() {
            public Boolean apply(Integer item) {
                return item % 2 == 0;
            }
        });
        final Boolean result2 = $.all(asList(1, 2, 3, 4), 
            new Predicate<Integer>() {
            public Boolean apply(Integer item) {
                return item < 5;
            }
        });
        assertFalse(result1);
        assertTrue(result2);
    }

/*
_.any([1, 2, 3, 4], function(num) { return num % 2 === 0; }); // true
_.any([1, 2, 3, 4], function(num) { return num === 5; }); // false
*/
    @Test
    public void any() {
        final Boolean result1 = $.any(asList(1, 2, 3, 4),
            new Predicate<Integer>() {
            public Boolean apply(Integer item) {
                return item % 2 == 0;
            }
        });
        final Boolean result2 = $.any(asList(1, 2, 3, 4),
            new Predicate<Integer>() {
            public Boolean apply(Integer item) {
                return item == 5;
            }
        });
        assertTrue(result1);
        assertFalse(result2);
    }

/*
_.include([1, 2, 3], 3); // true
*/
    @Test
    public void include() {
        final Boolean result = $.include(asList(1, 2, 3), 3); 
        assertTrue(result);
    }

/*
_.invoke([" foo ", "  bar"], "trim"); // ["foo", "bar"]
*/
    @Test
    public void invoke() throws Exception {
        assertEquals($.invoke(asList(" foo ", "  bar"), "trim"), asList("foo", "bar"));
        assertEquals($.invoke(asList("foo", "bar"), "concat", Arrays.<Object>asList("1")), asList("foo1", "bar1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void invoke2() throws Exception {
        $.invoke(asList("foo", 123), "concat", Arrays.<Object>asList("1"));
    }

/*
_.where(listOfPlays, {author: "Shakespeare", year: 1611});
=> [{title: "Cymbeline", author: "Shakespeare", year: 1611},
    {title: "The Tempest", author: "Shakespeare", year: 1611}]
*/
    @Test
    public void where() {
        class Book {
            public final String title;
            public final String author;
            public final Integer year;
            public Book(final String title, final String author, final Integer year) {
                this.title = title;
                this.author = author;
                this.year = year;
            }
            public String toString() {
                return "title: " + title + ", author: " + author + ", year: " + year;
            }
        };
        List<Book> listOfPlays =
            new ArrayList<Book>() {{
              add(new Book("Cymbeline2", "Shakespeare", 1614));
              add(new Book("Cymbeline", "Shakespeare", 1611));
              add(new Book("The Tempest", "Shakespeare", 1611));
            }};
        assertEquals("[title: Cymbeline, author: Shakespeare, year: 1611,"
            + " title: The Tempest, author: Shakespeare, year: 1611]",
            $.where(listOfPlays, asList(
            Tuple.<String, Object>create("author", "Shakespeare"),
            Tuple.<String, Object>create("year", Integer.valueOf(1611)))).toString());
        assertEquals("[title: Cymbeline, author: Shakespeare, year: 1611,"
            + " title: The Tempest, author: Shakespeare, year: 1611]",
            $.where(listOfPlays, asList(
            Tuple.<String, Object>create("author", "Shakespeare"),
            Tuple.<String, Object>create("author2", "Shakespeare"),
            Tuple.<String, Object>create("year", Integer.valueOf(1611)))).toString());
        assertEquals("[title: Cymbeline, author: Shakespeare, year: 1611,"
            + " title: The Tempest, author: Shakespeare, year: 1611]",
            $.where(new LinkedHashSet<Book>(listOfPlays), asList(
            Tuple.<String, Object>create("author", "Shakespeare"),
            Tuple.<String, Object>create("year", Integer.valueOf(1611)))).toString());
        assertEquals("[title: Cymbeline, author: Shakespeare, year: 1611,"
            + " title: The Tempest, author: Shakespeare, year: 1611]",
            $.where(new LinkedHashSet<Book>(listOfPlays), asList(
            Tuple.<String, Object>create("author", "Shakespeare"),
            Tuple.<String, Object>create("author2", "Shakespeare"),
            Tuple.<String, Object>create("year", Integer.valueOf(1611)))).toString());
    }

/*
_.findWhere(listOfPlays, {author: "Shakespeare", year: 1611})
=> {title: "Cymbeline", author: "Shakespeare", year: 1611}
*/
    @Test
    public void findWhere() {
        class Book {
            public final String title;
            public final String author;
            public final Integer year;
            public Book(final String title, final String author, final Integer year) {
                this.title = title;
                this.author = author;
                this.year = year;
            }
            public String toString() {
                return "title: " + title + ", author: " + author + ", year: " + year;
            }
        };
        List<Book> listOfPlays =
            new ArrayList<Book>() {{
              add(new Book("Cymbeline2", "Shakespeare", 1614));
              add(new Book("Cymbeline", "Shakespeare", 1611));
              add(new Book("The Tempest", "Shakespeare", 1611));
            }};
        assertEquals("title: Cymbeline, author: Shakespeare, year: 1611",
            $.findWhere(listOfPlays, asList(
            Tuple.<String, Object>create("author", "Shakespeare"),
            Tuple.<String, Object>create("year", Integer.valueOf(1611)))).get().toString());
        assertEquals("title: Cymbeline, author: Shakespeare, year: 1611",
            $.findWhere(listOfPlays, asList(
            Tuple.<String, Object>create("author", "Shakespeare"),
            Tuple.<String, Object>create("author2", "Shakespeare"),
            Tuple.<String, Object>create("year", Integer.valueOf(1611)))).get().toString());
    }

/*
_.first([5, 4, 3, 2, 1]);
=> 5
_.first([5, 4, 3, 2, 1], 2);
=> [5, 4]
*/
    @Test
    public void first() {
        final Integer result = $.first(asList(5, 4, 3, 2, 1));
        assertEquals("5", result.toString());
        final Object resultChain = $.chain(asList(5, 4, 3, 2, 1)).first().item();
        assertEquals("5", resultChain.toString());
        final Object resultChainTwo = $.chain(asList(5, 4, 3, 2, 1)).first(2).value();
        assertEquals("[5, 4]", resultChainTwo.toString());
        final List<Integer> resultList = $.first(asList(5, 4, 3, 2, 1), 2);
        assertEquals("[5, 4]", resultList.toString());
        final int resultInt = $.first(new Integer[] {5, 4, 3, 2, 1});
        assertEquals(5, resultInt);
    }

/*
_.head([5, 4, 3, 2, 1]);
=> 5
_.head([5, 4, 3, 2, 1], 2);
=> [5, 4]
*/
    @Test
    public void head() {
        final Integer result = $.head(asList(5, 4, 3, 2, 1));
        assertEquals("5", result.toString());
        final Integer resultObj = new $<Integer>(asList(5, 4, 3, 2, 1)).head();
        assertEquals("5", resultObj.toString());
        final List<Integer> resultList = $.head(asList(5, 4, 3, 2, 1), 2);
        assertEquals("[5, 4]", resultList.toString());
        final List<Integer> resultListObj = new $<Integer>(asList(5, 4, 3, 2, 1)).head(2);
        assertEquals("[5, 4]", resultListObj.toString());
        final int resultInt = $.head(new Integer[] {5, 4, 3, 2, 1});
        assertEquals(5, resultInt);
    }

/*
_.initial([5, 4, 3, 2, 1]);
=> [5, 4, 3, 2]
_.initial([5, 4, 3, 2, 1], 2);
=> [5, 4, 3]
*/
    @Test
    public void initial() {
        final List<Integer> result = $.initial(asList(5, 4, 3, 2, 1));
        assertEquals("[5, 4, 3, 2]", result.toString());
        final List<Integer> resultChain = $.chain(asList(5, 4, 3, 2, 1)).initial().value();
        assertEquals("[5, 4, 3, 2]", resultChain.toString());
        final List<Integer> resultList = $.initial(asList(5, 4, 3, 2, 1), 2);
        assertEquals("[5, 4, 3]", resultList.toString());
        final List<Integer> resultListChain = $.chain(asList(5, 4, 3, 2, 1)).initial(2).value();
        assertEquals("[5, 4, 3]", resultListChain.toString());
        final Integer[] resultArray = $.initial(new Integer[] {5, 4, 3, 2, 1});
        assertEquals("[5, 4, 3, 2]", asList(resultArray).toString());
        final Integer[] resultListArray = $.initial(new Integer[] {5, 4, 3, 2, 1}, 2);
        assertEquals("[5, 4, 3]", asList(resultListArray).toString());
    }

/*
_.last([5, 4, 3, 2, 1]);
=> 1
*/
    @Test
    public void last() {
        final Integer result = $.last(asList(5, 4, 3, 2, 1));
        assertEquals("1", result.toString());
        final List<Integer> resultTwo = $.last(asList(5, 4, 3, 2, 1), 2);
        assertEquals("[2, 1]", resultTwo.toString());
        final Object resultChain = $.chain(asList(5, 4, 3, 2, 1)).last().item();
        assertEquals("1", resultChain.toString());
        final Object resultChainTwo = $.chain(asList(5, 4, 3, 2, 1)).last(2).value();
        assertEquals("[2, 1]", resultChainTwo.toString());
        final Integer resultArray = $.last(new Integer[] {5, 4, 3, 2, 1});
        assertEquals("1", resultArray.toString());
    }

/*
_.tail([5, 4, 3, 2, 1]);
=> [4, 3, 2, 1]
_.tail([5, 4, 3, 2, 1], 2);
=> [3, 2, 1]
*/
    @Test
    public void tail() {
        final List<Integer> result = $.tail(asList(5, 4, 3, 2, 1));
        assertEquals("[4, 3, 2, 1]", result.toString());
        final List<Integer> result2 = $.tail(asList(5, 4, 3, 2, 1), 2);
        assertEquals("[3, 2, 1]", result2.toString());
        final Object[] resultArray = $.tail(new Integer[] {5, 4, 3, 2, 1});
        assertEquals("[4, 3, 2, 1]", asList(resultArray).toString());
        final List<Integer> resultArrayObj = new $<Integer>(asList(5, 4, 3, 2, 1)).tail();
        assertEquals("[4, 3, 2, 1]", resultArrayObj.toString());
        final Object[] resultArray2 = $.tail(new Integer[] {5, 4, 3, 2, 1}, 2);
        assertEquals("[3, 2, 1]", asList(resultArray2).toString());
        final List<Integer> resultArray2Obj = new $<Integer>(asList(5, 4, 3, 2, 1)).tail(2);
        assertEquals("[3, 2, 1]", resultArray2Obj.toString());
    }

/*
_.drop([5, 4, 3, 2, 1]);
=> [4, 3, 2, 1]
_.drop([5, 4, 3, 2, 1], 2);
=> [3, 2, 1]
*/
    @Test
    public void drop() {
        final List<Integer> result = $.drop(asList(5, 4, 3, 2, 1));
        assertEquals("[4, 3, 2, 1]", result.toString());
        final List<Integer> result2 = $.drop(asList(5, 4, 3, 2, 1), 2);
        assertEquals("[3, 2, 1]", result2.toString());
        final Object[] resultArray = $.drop(new Integer[] {5, 4, 3, 2, 1});
        assertEquals("[4, 3, 2, 1]", asList(resultArray).toString());
        final Object[] resultArray2 = $.drop(new Integer[] {5, 4, 3, 2, 1}, 2);
        assertEquals("[3, 2, 1]", asList(resultArray2).toString());
    }

/*
_.rest([5, 4, 3, 2, 1]);
=> [4, 3, 2, 1]
_.rest([5, 4, 3, 2, 1], 2);
=> [3, 2, 1]
*/
    @Test
    public void rest() {
        final List<Integer> result = $.rest(asList(5, 4, 3, 2, 1));
        assertEquals("[4, 3, 2, 1]", result.toString());
        final List<Integer> resultChain = $.chain(asList(5, 4, 3, 2, 1)).rest().value();
        assertEquals("[4, 3, 2, 1]", resultChain.toString());
        final List<Integer> result2 = $.rest(asList(5, 4, 3, 2, 1), 2);
        assertEquals("[3, 2, 1]", result2.toString());
        final List<Integer> result2Chain = $.chain(asList(5, 4, 3, 2, 1)).rest(2).value();
        assertEquals("[3, 2, 1]", result2Chain.toString());
        final Object[] resultArray = $.rest(new Integer[] {5, 4, 3, 2, 1});
        assertEquals("[4, 3, 2, 1]", asList(resultArray).toString());
        final Object[] resultArray2 = $.rest(new Integer[] {5, 4, 3, 2, 1}, 2);
        assertEquals("[3, 2, 1]", asList(resultArray2).toString());
    }


/*
_.flatten([1, [2], [3, [[4]]]]);
=> [1, 2, 3, 4];
*/
    @Test
    public void flatten() {
        final List<Integer> result = $.flatten(asList(1, asList(2, asList(3, asList(asList(4))))));
        assertEquals("[1, 2, 3, 4]", result.toString());
        final List<Integer> result2 = $.flatten(asList(1, asList(2, asList(3, asList(asList(4))))), true);
        assertEquals("[1, 2, [3, [[4]]]]", result2.toString());
        final List<Integer> result3 = $.flatten(asList(1, asList(2, asList(3, asList(asList(4))))), false);
        assertEquals("[1, 2, 3, 4]", result3.toString());
        final List<Integer> resultObj = new $(asList(1, asList(2, asList(3, asList(asList(4)))))).flatten();
        assertEquals("[1, 2, 3, 4]", resultObj.toString());
        final List<Integer> resultObj2 = new $(asList(1, asList(2, asList(3, asList(asList(4)))))).flatten(true);
        assertEquals("[1, 2, [3, [[4]]]]", resultObj2.toString());
        final List<Integer> resultObj3 = new $(asList(1, asList(2, asList(3, asList(asList(4)))))).flatten(false);
        assertEquals("[1, 2, 3, 4]", resultObj3.toString());
    }

/*
_.compact([0, 1, false, 2, '', 3]);
=> [1, 2, 3]
*/
    @Test
    public void compact() {
        final List<?> result = $.compact(asList(0, 1, false, 2, "", 3));
        assertEquals("[1, 2, 3]", result.toString());
        final List<?> result2 = $.compact(asList(0, 1, false, 2, "", 3), 1);
        assertEquals("[0, false, 2, , 3]", result2.toString());
        final List<?> result3 = new $(asList(0, 1, false, 2, "", 3)).compact();
        assertEquals("[1, 2, 3]", result3.toString());
        final List<?> result4 = new $(asList(0, 1, false, 2, "", 3)).compact(1);
        assertEquals("[0, false, 2, , 3]", result4.toString());
        final Object[] resultArray = $.compact(new Object[] {0, 1, false, 2, "", 3});
        assertEquals("[1, 2, 3]", asList(resultArray).toString());
        final Object[] resultArray2 = $.compact(new Object[] {0, 1, false, 2, "", 3}, 1);
        assertEquals("[0, false, 2, , 3]", asList(resultArray2).toString());
    }

/*
_.without([1, 2, 1, 0, 3, 1, 4], 0, 1);
=> [2, 3, 4]
*/
    @Test
    public void without() {
        final List<Integer> result = $.without(asList(1, 2, 1, 0, 3, 1, 4), 0, 1);
        assertEquals("[2, 3, 4]", result.toString());
        final List<Integer> result2 = $.without(asList(1, 2, 1, 0, 3, 1, 4), 1);
        assertEquals("[2, 0, 3, 4]", result2.toString());
        final List<Integer> result3 = $.without(asList(null, 2, null, 0, 3, null, 4), (Integer) null);
        assertEquals("[2, 0, 3, 4]", result3.toString());
        final Object[] resultArray = $.without(new Integer[] {1, 2, 1, 0, 3, 1, 4}, 0, 1);
        assertEquals("[2, 3, 4]", asList(resultArray).toString());
        final Object[] resultArray2 = $.without(new Integer[] {1, 2, 1, 0, 3, 1, 4}, 1);
        assertEquals("[2, 0, 3, 4]", asList(resultArray2).toString());
    }

/*
var numbers = [10, 5, 100, 2, 1000];
_.max(numbers);
=> 1000
*/
    @Test
    public void max() {
        final Integer result = $.max(asList(10, 5, 100, 2, 1000));
        assertEquals("1000", result.toString());
        final Integer resultChain = (Integer) $.chain(asList(10, 5, 100, 2, 1000)).max().item();
        assertEquals("1000", resultChain.toString());
        final Integer resultComp = $.max(asList(10, 5, 100, 2, 1000),
                new Function1<Integer, Integer>() {
            public Integer apply(Integer item) {
                return -item;
            }
        });
        assertEquals("2", resultComp.toString());
        final Integer resultCompChain = (Integer) $.chain(asList(10, 5, 100, 2, 1000)).max(
                new Function1<Integer, Integer>() {
            public Integer apply(Integer item) {
                return -item;
            }
        }).item();
        assertEquals("2", resultCompChain.toString());
        class Person {
            public final String name;
            public final Integer age;
            public Person(final String name, final Integer age) {
                this.name = name;
                this.age = age;
            }
        };
        final Person resultPerson = $.max(asList(new Person("moe", 40), new Person("larry", 50), new Person("curly", 60)),
                new Function1<Person, Integer>() {
            public Integer apply(Person item) {
                return item.age;
            }
        });
        assertEquals("curly", resultPerson.name);
        assertEquals(60, resultPerson.age);
    }

/*
var numbers = [10, 5, 100, 2, 1000];
_.min(numbers);
=> 2
*/
    @Test
    public void min() {
        final Integer result = $.min(asList(10, 5, 100, 2, 1000));
        assertEquals("2", result.toString());
        final Integer resultChain = (Integer) $.chain(asList(10, 5, 100, 2, 1000)).min().item();
        assertEquals("2", resultChain.toString());
        final Integer resultComp = $.min(asList(10, 5, 100, 2, 1000),
                new Function1<Integer, Integer>() {
            public Integer apply(Integer item) {
                return -item;
            }
        });
        assertEquals("1000", resultComp.toString());
        final Integer resultCompChain = (Integer) $.chain(asList(10, 5, 100, 2, 1000)).min(
                new Function1<Integer, Integer>() {
            public Integer apply(Integer item) {
                return -item;
            }
        }).item();
        assertEquals("1000", resultCompChain.toString());
        class Person {
            public final String name;
            public final Integer age;
            public Person(final String name, final Integer age) {
                this.name = name;
                this.age = age;
            }
        };
        final Person resultPerson = $.min(asList(new Person("moe", 40), new Person("larry", 50), new Person("curly", 60)),
                new Function1<Person, Integer>() {
            public Integer apply(Person item) {
                return item.age;
            }
        });
        assertEquals("moe", resultPerson.name);
        assertEquals(40, resultPerson.age);
    }

/*
_.shuffle([1, 2, 3, 4, 5, 6]);
=> [4, 1, 6, 3, 5, 2]
*/
    @Test
    public void shuffle() {
        final List<Integer> result = $.shuffle(asList(1, 2, 3, 4, 5, 6));
        assertEquals(6, result.size());
        final List<Integer> resultChain = $.chain(asList(1, 2, 3, 4, 5, 6)).shuffle().value();
        assertEquals(6, resultChain.size());
    }

/*
_.sample([1, 2, 3, 4, 5, 6]);
=> 4

_.sample([1, 2, 3, 4, 5, 6], 3);
=> [1, 6, 2]
*/
    @Test
    public void sample() {
        final Integer result = $.sample(asList(1, 2, 3, 4, 5, 6));
        assertTrue(result >= 1 && result <= 6);
        final Set<Integer> resultList = $.sample(asList(1, 2, 3, 4, 5, 6), 3);
        assertEquals(3, resultList.size());
        final Integer resultChain = (Integer) $.chain(asList(1, 2, 3, 4, 5, 6)).sample().item();
        assertTrue(resultChain >= 1 && resultChain <= 6);
        final List<Integer> resultListChain = $.chain(asList(1, 2, 3, 4, 5, 6)).sample(3).value();
        assertEquals(3, resultListChain.size());
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
        final List<?> resultEmpty =
        $.pluck(asList(), "name");
        assertEquals("[]", resultEmpty.toString());
        final List<?> result =
        $.pluck(asList(new Person("moe", 40), new Person("larry", 50), new Person("curly", 40)), "name");
        assertEquals("[moe, larry, curly]", result.toString());
        final Set<?> resultEmpty2 =
        $.pluck(new LinkedHashSet(asList()), "name");
        assertEquals("[]", resultEmpty2.toString());
        final Set<?> resultSet =
        $.pluck(new LinkedHashSet(asList(new Person("moe", 40), new Person("larry", 50), new Person("curly", 40))), "name");
        assertEquals("[moe, larry, curly]", resultSet.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void pluck2() throws Exception {
        class Person {
            public final String name;
            public final Integer age;
            public Person(final String name, final Integer age) {
                this.name = name;
                this.age = age;
            }
        };
        $.pluck(asList(new Person("moe", 40), new Person("larry", 50), new Person("curly", 40)), "name2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void pluck3() throws Exception {
        class Person {
            public final String name;
            public final Integer age;
            public Person(final String name, final Integer age) {
                this.name = name;
                this.age = age;
            }
        };
        $.pluck(new LinkedHashSet(asList(new Person("moe", 40), new Person("larry", 50), new Person("curly", 40))), "name2");
    }
/*
_.sortBy([1, 2, 3, 4, 5, 6], function(num){ return Math.sin(num); });
=> [5, 4, 6, 3, 1, 2]
*/
    @Test
    public void sortBy() throws Exception {
        final List<Integer> result =
        $.sortBy(asList(1, 2, 3, 4, 5, 6),
            new Function1<Integer, Integer>() {
            public Integer apply(Integer item) {
                return Double.valueOf(Math.sin(item) * 1000).intValue();
            }
        });
        assertEquals("[5, 4, 6, 3, 1, 2]", result.toString());
    }

/*
var stooges = [{name: 'moe', age: 40}, {name: 'larry', age: 50}, {name: 'curly', age: 60}];
_.sortBy(stooges, 'name');
=> [{name: 'curly', age: 60}, {name: 'larry', age: 50}, {name: 'moe', age: 40}];
*/
    @Test
    public void sortByMap() throws Exception {
        final List<Map<String, Comparable>> result = $.sortBy(asList(
            (Map<String, Comparable>) new LinkedHashMap<String, Comparable>() {{ put("name", "moe"); put("age", 40); }},
            (Map<String, Comparable>) new LinkedHashMap<String, Comparable>() {{ put("name", "larry"); put("age", 50); }},
            (Map<String, Comparable>) new LinkedHashMap<String, Comparable>() {{ put("name", "curly"); put("age", 60); }}
        ), "name");
        assertEquals("[{name=curly, age=60}, {name=larry, age=50}, {name=moe, age=40}]", result.toString());
        final List<Map<String, Comparable>> resultChain = $.chain(asList(
            (Map<String, Comparable>) new LinkedHashMap<String, Comparable>() {{ put("name", "moe"); put("age", 40); }},
            (Map<String, Comparable>) new LinkedHashMap<String, Comparable>() {{ put("name", "larry"); put("age", 50); }},
            (Map<String, Comparable>) new LinkedHashMap<String, Comparable>() {{ put("name", "curly"); put("age", 60); }}
        )).sortBy("name").value();
        assertEquals("[{name=curly, age=60}, {name=larry, age=50}, {name=moe, age=40}]", resultChain.toString());
    }

/*
_.groupBy([1.3, 2.1, 2.4], function(num){ return Math.floor(num); });
=> {1: [1.3], 2: [2.1, 2.4]}
*/
    @Test
    public void groupBy() throws Exception {
        final Map<Double, List<Double>> result =
        $.groupBy(asList(1.3, 2.1, 2.4),
            new Function1<Double, Double>() {
            public Double apply(Double num) {
                return Math.floor(num);
            }
        });
        assertEquals("{1.0=[1.3], 2.0=[2.1, 2.4]}", result.toString());
    }

/*
var stooges = [{name: 'moe', age: 40}, {name: 'larry', age: 50}, {name: 'curly', age: 60}];
_.indexBy(stooges, 'age');
=> {
  "40": {name: 'moe', age: 40},
  "50": {name: 'larry', age: 50},
  "60": {name: 'curly', age: 60}
}
*/
    @Test
    public void indexBy() throws Exception {
        class Person {
            public final String name;
            public final Integer age;
            public Person(final String name, final Integer age) {
                this.name = name;
                this.age = age;
            }
            public String toString() {
                return name + ", " + age;
            }
        };
        final Map<String, List<Person>> result =
        $.indexBy(asList(new Person("moe", 40), new Person("larry", 50), new Person("curly", 60)), "age");
        assertEquals("{40=[moe, 40], 50=[larry, 50], 60=[curly, 60]}", result.toString());
        final Map<String, List<Person>> result2 =
        $.indexBy(asList(new Person("moe", 40), new Person("larry", 50), new Person("curly", 60)), "age2");
        assertEquals("{null=[moe, 40, larry, 50, curly, 60]}", result2.toString());
    }

/*
var stooges = [{name: 'moe', age: 40}, {name: 'moe', age: 50}, {name: 'curly', age: 60}];
_.countBy(stooges, 'age');
=> {moe: 2, curly: 1}
*/
    @Test
    public void countBy() throws Exception {
        class Person {
            public final String name;
            public final Integer age;
            public Person(final String name, final Integer age) {
                this.name = name;
                this.age = age;
            }
            public String toString() {
                return name + ", " + age;
            }
        };
        final Map<String, Integer> result =
        $.countBy(asList(new Person("moe", 40), new Person("moe", 50), new Person("curly", 60)),
            new Function1<Person, String>() {
            public String apply(Person person) {
                return person.name;
            }
        });        
        assertEquals("{moe=2, curly=1}", result.toString());
    }

/*
(function(){ return _.toArray(arguments).slice(1); })(1, 2, 3, 4);
=> [2, 3, 4]
*/
    @Test
    public void toArray() throws Exception {
        final Object[] result = $.<Integer>toArray(asList(1, 2, 3, 4));
        assertEquals("1", result[0].toString());
    }

/*
_.size({one: 1, two: 2, three: 3});
=> 3
*/
    @Test
    public void size() throws Exception {
        final int result = $.size(asList(1, 2, 3, 4));
        assertEquals(4, result);
    }

/*
_.union([1, 2, 3], [101, 2, 1, 10], [2, 1]);
=> [1, 2, 3, 101, 10]
*/
    @Test
    public void union() throws Exception {
        final List<Integer> result = $.union(asList(1, 2, 3), asList(101, 2, 1, 10), asList(2, 1));
        assertEquals("[1, 2, 3, 101, 10]", result.toString());
        final Object[] resultArray = $.union(new Integer[] {1, 2, 3}, new Integer[] {101, 2, 1, 10});
        assertEquals("[[1, 2, 3, 101, 10]]", asList(result).toString());
    }

/*
_.intersection([1, 2, 3], [101, 2, 1, 10], [2, 1]);
=> [1, 2]
*/
    @Test
    public void intersection() throws Exception {
        final List<Integer> result = $.intersection(asList(1, 2, 3), asList(101, 2, 1, 10), asList(2, 1));
        assertEquals("[1, 2]", result.toString());
        final Object[] resultArray = $.intersection(new Integer[] {1, 2, 3}, new Integer[] {101, 2, 1, 10});
        assertEquals("[1, 2]", asList(resultArray).toString());
    }

/*
_.difference([1, 2, 3, 4, 5], [5, 2, 10]);
=> [1, 3, 4]
*/
    @Test
    public void difference() throws Exception {
        final List<Integer> result = $.difference(asList(1, 2, 3, 4, 5), asList(5, 2, 10));
        assertEquals("[1, 3, 4]", result.toString());
        final Object[] resultArray = $.difference(new Integer[] {1, 2, 3, 4, 5}, new Integer[] {5, 2, 10});
        assertEquals("[1, 3, 4]", asList(resultArray).toString());
    }

/*
_.uniq([1, 2, 1, 3, 1, 4]);
=> [1, 2, 3, 4]
*/
    @Test
    public void uniq() throws Exception {
        final List<Integer> result = $.uniq(asList(1, 2, 1, 3, 1, 4));
        assertEquals("[1, 2, 3, 4]", result.toString());
        final Object[] resultArray = $.uniq(new Integer[] {1, 2, 1, 3, 1, 4});
        assertEquals("[1, 2, 3, 4]", asList(resultArray).toString());
        class Person {
            public final String name;
            public final Integer age;
            public Person(final String name, final Integer age) {
                this.name = name;
                this.age = age;
            }
            public String toString() {
                return name + ", " + age;
            }
        };
        final Collection<Person> resultObject =
        $.uniq(asList(new Person("moe", 40), new Person("moe", 50), new Person("curly", 60)),
            new Function1<Person, String>() {
            public String apply(Person person) {
                return person.name;
            }
        });
        assertEquals("[moe, 50, curly, 60]", resultObject.toString());
        final List<Person> resultObjectChain =
        $.chain(asList(new Person("moe", 40), new Person("moe", 50), new Person("curly", 60))).uniq(
            new Function1<Person, String>() {
            public String apply(Person person) {
                return person.name;
            }
        }).value();
        assertEquals("[moe, 50, curly, 60]", resultObjectChain.toString());
        assertEquals("[1, 2, 3, 4, 5]", $.chain(asList(1, 2, 3, 3, 4, 5)).uniq().value().toString());
        final Object[] resultObjectArray =
        $.uniq(asList(new Person("moe", 40), new Person("moe", 50), new Person("curly", 60)).toArray(new Person[]{}),
            new Function1<Person, String>() {
            public String apply(Person person) {
                return person.name;
            }
        });
        assertEquals("[moe, 50, curly, 60]", asList(resultObjectArray).toString());
    }

/*
_.unzip(["moe", 30, true], ["larry", 40, false], ["curly", 50, false]);
=> [['moe', 'larry', 'curly'], [30, 40, 50], [true, false, false]]
*/
    @Test
    public void unzip() throws Exception {
        final List<List<String>> result = $.unzip(
            asList("moe", "30", "true"), asList("larry", "40", "false"), asList("curly", "50", "false"));
        assertEquals("[[moe, larry, curly], [30, 40, 50], [true, false, false]]", result.toString());
    }

/*
_.zip(['moe', 'larry', 'curly'], [30, 40, 50], [true, false, false]);
=> [["moe", 30, true], ["larry", 40, false], ["curly", 50, false]]
*/
    @Test
    public void zip() throws Exception {
        final List<List<String>> result = $.zip(
            asList("moe", "larry", "curly"), asList("30", "40", "50"), asList("true", "false", "false"));
        assertEquals("[[moe, 30, true], [larry, 40, false], [curly, 50, false]]", result.toString());
    }

/*
_.object(['moe', 'larry', 'curly'], [30, 40, 50]);
=> {moe: 30, larry: 40, curly: 50}
*/
    @Test
    public void object() throws Exception {
        final List<Tuple<String, String>> result = $.object(
            asList("moe", "larry", "curly"), asList("30", "40", "50"));
        assertEquals("[(moe, 30), (larry, 40), (curly, 50)]", result.toString());
    }

/*
_.pick({name: 'moe', age: 50, userid: 'moe1'}, 'name', 'age');
=> {name: 'moe', age: 50}
_.pick({name: 'moe', age: 50, userid: 'moe1'}, function(value, key, object) {
  return _.isNumber(value);
});
=> {age: 50}
*/
    @Test
    public void pick() throws Exception {
        final List<Tuple<String, Object>> result = $.pick(
            new LinkedHashMap<String, Object>() {{ put("name", "moe"); put("age", 50); put("userid", "moe1"); }},
            "name", "age"
        );
        assertEquals("[(name, moe), (age, 50)]", result.toString());
        final List<Tuple<String, Object>> result2 = $.pick(
            new LinkedHashMap<String, Object>() {{ put("name", "moe"); put("age", 50); put("userid", "moe1"); }},
            new Predicate<Object>() { public Boolean apply(Object value) { return value instanceof Number; } }
        );
        assertEquals("[(age, 50)]", result2.toString());
    }

/*
_.omit({name: 'moe', age: 50, userid: 'moe1'}, 'userid');
=> {name: 'moe', age: 50}
_.omit({name: 'moe', age: 50, userid: 'moe1'}, function(value, key, object) {
  return _.isNumber(value);
});
=> {name: 'moe', userid: 'moe1'}
*/
    @Test
    public void omit() throws Exception {
        final List<Tuple<String, Object>> result = $.omit(
            new LinkedHashMap<String, Object>() {{ put("name", "moe"); put("age", 50); put("userid", "moe1"); }},
            "userid"
        );
        assertEquals("[(name, moe), (age, 50)]", result.toString());
        final List<Tuple<String, Object>> result2 = $.omit(
            new LinkedHashMap<String, Object>() {{ put("name", "moe"); put("age", 50); put("userid", "moe1"); }},
            new Predicate<Object>() { public Boolean apply(Object value) { return value instanceof Number; } }
        );
        assertEquals("[(name, moe), (userid, moe1)]", result2.toString());
    }

/*
_.indexOf([1, 2, 3], 2);
=> 1
*/
    @Test
    public void indexOf() throws Exception {
        final Integer result = $.indexOf(asList(1, 2, 3), 2);
        assertEquals(1, result);
        final Integer resultArray = $.indexOf(new Integer[] {1, 2, 3}, 2);
        assertEquals(1, resultArray);
    }

/*
_.lastIndexOf([1, 2, 3, 1, 2, 3], 2);
=> 4
*/
    @Test
    public void lastIndexOf() throws Exception {
        final Integer result = $.lastIndexOf(asList(1, 2, 3, 1, 2, 3), 2);
        assertEquals(4, result);
        final Integer resultArray = $.lastIndexOf(new Integer[] {1, 2, 3, 1, 2, 3}, 2);
        assertEquals(4, resultArray);
    }

/*
_.findIndex([1, 2, 3], function(item) {return item % 2  === 0; });
=> 1
*/
    @Test
    public void findIndex() throws Exception {
        final Integer result = $.findIndex(asList(1, 2, 3), new Predicate<Integer>() {
            public Boolean apply(Integer item) {
                return item % 2 == 0;
            }
        });
        assertEquals(1, result);
        final Integer resultNotFound = $.findIndex(asList(1, 2, 3), new Predicate<Integer>() {
            public Boolean apply(Integer item) {
                return item > 3;
            }
        });
        assertEquals(-1, resultNotFound);
        final Integer resultArray = $.findIndex(new Integer[] {1, 2, 3}, new Predicate<Integer>() {
            public Boolean apply(Integer item) {
                return item % 2 == 0;
            }
        });
        assertEquals(1, resultArray);
    }

/*
_.findKey([1, 2, 3], function(item) {return item % 2  === 0; });
=> 2
*/
    @Test
    public void findKey() throws Exception {
        final Integer result = $.findKey(asList(1, 2, 3), new Predicate<Integer>() {
            public Boolean apply(Integer item) {
                return item % 2 == 0;
            }
        });
        assertEquals(2, result);
        final Integer resultNotFound = $.findKey(asList(1, 2, 3), new Predicate<Integer>() {
            public Boolean apply(Integer item) {
                return item > 3;
            }
        });
        assertNull(resultNotFound);
        final Integer resultArray = $.findKey(new Integer[] {1, 2, 3}, new Predicate<Integer>() {
            public Boolean apply(Integer item) {
                return item % 2 == 0;
            }
        });
        assertEquals(2, resultArray);
    }

/*
_.findLastIndex([1, 2, 3, 4, 5], function(item) {return item % 2  === 0; });
=> 3
*/
    @Test
    public void findLastIndex() throws Exception {
        final Integer result = $.findLastIndex(asList(1, 2, 3, 4, 5), new Predicate<Integer>() {
            public Boolean apply(Integer item) {
                return item % 2 == 0;
            }
        });
        assertEquals(3, result);
        final Integer resultNotFound = $.findLastIndex(asList(1, 2, 3, 4, 5), new Predicate<Integer>() {
            public Boolean apply(Integer item) {
                return item > 5;
            }
        });
        assertEquals(-1, resultNotFound);
        final Integer resultArray = $.findLastIndex(new Integer[] {1, 2, 3, 4, 5}, new Predicate<Integer>() {
            public Boolean apply(Integer item) {
                return item % 2 == 0;
            }
        });
        assertEquals(3, resultArray);
    }

/*
_.findLastKey([1, 2, 3, 4, 5], function(item) {return item % 2  === 0; });
=> 4
*/
    @Test
    public void findLastKey() throws Exception {
        final Integer result = $.findLastKey(asList(1, 2, 3, 4, 5), new Predicate<Integer>() {
            public Boolean apply(Integer item) {
                return item % 2 == 0;
            }
        });
        assertEquals(4, result);
        final Integer resultNotFound = $.findLastKey(asList(1, 2, 3, 4, 5), new Predicate<Integer>() {
            public Boolean apply(Integer item) {
                return item > 5;
            }
        });
        assertNull(resultNotFound);
        final Integer resultArray = $.findLastKey(new Integer[] {1, 2, 3, 4, 5}, new Predicate<Integer>() {
            public Boolean apply(Integer item) {
                return item % 2 == 0;
            }
        });
        assertEquals(4, resultArray);
    }

/*
_.sortedIndex([10, 20, 30, 40, 50], 35);
=> 3
*/
    @Test
    public void sortedIndex() throws Exception {
        final Integer result = $.sortedIndex(asList(10, 20, 30, 40, 50), 35);
        assertEquals(3, result);
        final Integer result2 = $.sortedIndex(new Integer[] {10, 20, 30, 40, 50}, 35);
        assertEquals(3, result2);
        final Integer result3 = $.sortedIndex(asList(10, 20, 30, 40, 50), 60);
        assertEquals(-1, result3);
    }

    @Test
    public void sortedIndex2() throws Exception {
        class Person implements Comparable<Person> {
            public final String name;
            public final Integer age;
            public Person(final String name, final Integer age) {
                this.name = name;
                this.age = age;
            }
            public int compareTo(Person person) {
                return person.age - this.age;
            }
            public String toString() {
                return name + ", " + age;
            }
        };
        final int result =
        $.<Person>sortedIndex(asList(new Person("moe", 40), new Person("moe", 50), new Person("curly", 60)), new Person("moe", 50), "age");
        assertEquals(1, result);
        final int result2 =
        $.<Person>sortedIndex(asList(new Person("moe", 40), new Person("moe", 50), new Person("curly", 60)), new Person("moe", 70), "age");
        assertEquals(-1, result2);
        final int resultArray =
        $.<Person>sortedIndex(new Person[] {new Person("moe", 40), new Person("moe", 50), new Person("curly", 60)}, new Person("moe", 50), "age");
        assertEquals(1, resultArray);
    }

/*
_.range(10);
=> [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
_.range(1, 11);
=> [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
_.range(0, 30, 5);
=> [0, 5, 10, 15, 20, 25]
_.range(0, -10, -1);
=> [0, -1, -2, -3, -4, -5, -6, -7, -8, -9]
_.range(0);
=> []
*/
    @Test
    public void range() throws Exception {
        final int[] result = $.range(10);
        assertArrayEquals(new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, result);
        final int[] result2 = $.range(1, 11);
        assertArrayEquals(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, result2);
        final int[] result3 = $.range(0, 30, 5);
        assertArrayEquals(new int[] {0, 5, 10, 15, 20, 25}, result3);
        final int[] result4 = $.range(0, -10, -1);
        assertArrayEquals(new int[] {0, -1, -2, -3, -4, -5, -6, -7, -8, -9}, result4);
        final int[] result5 = $.range(0);
        assertArrayEquals(new int[] {}, result5);
    }

/*
_.partition([0, 1, 2, 3, 4, 5], isOdd);
=> [[1, 3, 5], [0, 2, 4]]
*/
    @Test
    public void partition() throws Exception {
        final List<List<Integer>> result = $.partition(asList(0, 1, 2, 3, 4, 5), new Predicate<Integer>() {
            public Boolean apply(final Integer item) {
                return item % 2 == 1;
            }
        });
        assertEquals("[1, 3, 5]", result.get(0).toString());
        assertEquals("[0, 2, 4]", result.get(1).toString());
        final List<Integer>[] resultArray = $.partition(new Integer[] {0, 1, 2, 3, 4, 5}, new Predicate<Integer>() {
            public Boolean apply(final Integer item) {
                return item % 2 == 1;
            }
        });
        assertEquals("[1, 3, 5]", resultArray[0].toString());
        assertEquals("[0, 2, 4]", resultArray[1].toString());
    }

/*
var stooges = [{name: 'curly', age: 25}, {name: 'moe', age: 21}, {name: 'larry', age: 23}];
var youngest = _.chain(stooges)
  .sortBy(function(stooge){ return stooge.age; })
  .map(function(stooge){ return stooge.name + ' is ' + stooge.age; })
  .first()
  .value();
=> "moe is 21"
*/
    @Test
    public void chain() throws Exception {
        final List<Map<String, Object>> stooges = new ArrayList<Map<String, Object>>() {{
            add(new LinkedHashMap<String, Object>() {{ put("name", "curly"); put("age", 25); }});
            add(new LinkedHashMap<String, Object>() {{ put("name", "moe"); put("age", 21); }});
            add(new LinkedHashMap<String, Object>() {{ put("name", "larry"); put("age", 23); }});
        }};
        final String youngest = $.chain(stooges)
            .sortBy(
                new Function1<Map<String, Object>, String>() {
                public String apply(Map<String, Object> item) {
                    return item.get("age").toString();
                }
            })
            .map(
                new Function1<Map<String, Object>, String>() {
                public String apply(Map<String, Object> item) {
                    return item.get("name") + " is " + item.get("age");
                }
            })
            .first().item().toString();
        assertEquals("moe is 21", youngest);
    }

    @Test
    public void chainSet() throws Exception {
        final Set<Map<String, Object>> stooges = new HashSet<Map<String, Object>>() {{
            add(new LinkedHashMap<String, Object>() {{ put("name", "curly"); put("age", 25); }});
            add(new LinkedHashMap<String, Object>() {{ put("name", "moe"); put("age", 21); }});
            add(new LinkedHashMap<String, Object>() {{ put("name", "larry"); put("age", 23); }});
        }};
        final String youngest = $.chain(stooges)
            .sortBy(
                new Function1<Map<String, Object>, String>() {
                public String apply(Map<String, Object> item) {
                    return item.get("age").toString();
                }
            })
            .map(
                new Function1<Map<String, Object>, String>() {
                public String apply(Map<String, Object> item) {
                    return item.get("name") + " is " + item.get("age");
                }
            })
            .first().item().toString();
        assertEquals("moe is 21", youngest);
    }

    @Test
    public void chainArray() throws Exception {
        final List<Map<String, Object>> stooges = new ArrayList<Map<String, Object>>() {{
            add(new LinkedHashMap<String, Object>() {{ put("name", "curly"); put("age", 25); }});
            add(new LinkedHashMap<String, Object>() {{ put("name", "moe"); put("age", 21); }});
            add(new LinkedHashMap<String, Object>() {{ put("name", "larry"); put("age", 23); }});
        }};
        final String youngest = $.chain(stooges.toArray())
            .sortBy(
                new Function1<Map<String, Object>, String>() {
                public String apply(Map<String, Object> item) {
                    return item.get("age").toString();
                }
            })
            .map(
                new Function1<Map<String, Object>, String>() {
                public String apply(Map<String, Object> item) {
                    return item.get("name") + " is " + item.get("age");
                }
            })
            .first().item().toString();
        assertEquals("moe is 21", youngest);
    }

/*
var lyrics = [
  {line: 1, words: "I'm a lumberjack and I'm okay"},
  {line: 2, words: "I sleep all night and I work all day"},
  {line: 3, words: "He's a lumberjack and he's okay"},
  {line: 4, words: "He sleeps all night and he works all day"}
];

_.chain(lyrics)
  .map(function(line) { return line.words.split(' '); })
  .flatten()
  .reduce(function(counts, word) {
    counts[word] = (counts[word] || 0) + 1;
    return counts;
  }, {})
  .value();

=> {lumberjack: 2, all: 4, night: 2 ... }
*/
    @Test
    public void chain2() throws Exception {
        final List<Map<String, Object>> lyrics = new ArrayList<Map<String, Object>>() {{
            add(new LinkedHashMap<String, Object>() {{ put("line", 1); put("words", "I'm a lumberjack and I'm okay"); }});
            add(new LinkedHashMap<String, Object>() {{ put("line", 2); put("words", "I sleep all night and I work all day"); }});
            add(new LinkedHashMap<String, Object>() {{ put("line", 3); put("words", "He's a lumberjack and he's okay"); }});
            add(new LinkedHashMap<String, Object>() {{ put("line", 4); put("words", "He sleeps all night and he works all day"); }});
        }};
        final String result = $.chain(lyrics)
            .map(
                new Function1<Map<String, Object>, List<String>>() {
                public List<String> apply(Map<String, Object> item) {
                    return asList(String.valueOf(item.get("words")).split(" "));
                }
            })
            .flatten()
            .reduce(
                new FunctionAccum<Map<String, Object>, String>() {
                public Map<String, Object> apply(Map<String, Object> accum, String item) {
                    if (accum.get(item) == null) {
                        accum.put(item, 1);
                    } else {
                        accum.put(item, ((Integer) accum.get(item)) + 1);
                    }
                    return accum;
                }
            },
            new LinkedHashMap<String, Object>()
            )
            .item().toString();
        assertEquals("{I'm=2, a=2, lumberjack=2, and=4, okay=2, I=2, sleep=1, all=4, night=2, work=1, day=2, He's=1,"
            + " he's=1, He=1, sleeps=1, he=1, works=1}", result);
    }

/*
var lyrics = [
  {line: 1, words: "I'm a lumberjack and I'm okay"},
  {line: 2, words: "I sleep all night and I work all day"},
  {line: 3, words: "He's a lumberjack and he's okay"},
  {line: 4, words: "He sleeps all night and he works all day"}
];

_.chain(lyrics)
  .map(function(line) { return line.words.split(' '); })
  .flatten()
  .reduceRight(function(counts, word) {
    counts[word] = (counts[word] || 0) + 1;
    return counts;
  }, {})
  .value();

=> {day=2, all=4, works=1 ... }
*/
    @Test
    public void chain3() throws Exception {
        final List<Map<String, Object>> lyrics = new ArrayList<Map<String, Object>>() {{
            add(new LinkedHashMap<String, Object>() {{ put("line", 1); put("words", "I'm a lumberjack and I'm okay"); }});
            add(new LinkedHashMap<String, Object>() {{ put("line", 2); put("words", "I sleep all night and I work all day"); }});
            add(new LinkedHashMap<String, Object>() {{ put("line", 3); put("words", "He's a lumberjack and he's okay"); }});
            add(new LinkedHashMap<String, Object>() {{ put("line", 4); put("words", "He sleeps all night and he works all day"); }});
        }};
        final String result = $.chain(lyrics)
            .map(
                new Function1<Map<String, Object>, List<String>>() {
                public List<String> apply(Map<String, Object> item) {
                    return asList(String.valueOf(item.get("words")).split(" "));
                }
            })
            .flatten()
            .reduceRight(
                new FunctionAccum<Map<String, Object>, String>() {
                public Map<String, Object> apply(Map<String, Object> accum, String item) {
                    if (accum.get(item) == null) {
                        accum.put(item, 1);
                    } else {
                        accum.put(item, ((Integer) accum.get(item)) + 1);
                    }
                    return accum;
                }
            },
            new LinkedHashMap<String, Object>()
            )
            .item().toString();
        assertEquals("{day=2, all=4, works=1, he=1, and=4, night=2, sleeps=1,"
                + " He=1, okay=2, he's=1, lumberjack=2, a=2, He's=1, work=1, I=2, sleep=1, I'm=2}", result);
    }

/*
var doctors = [
    { number: 1,  actor: "William Hartnell",      begin: 1963, end: 1966 },
    { number: 9,  actor: "Christopher Eccleston", begin: 2005, end: 2005 },
    { number: 10, actor: "David Tennant",         begin: 2005, end: 2010 }
];
_.chain(doctors)
    .filter(function(doctor) {
        return doctor.begin > 2000;
    })
    .reject(function(doctor) {
        return doctor.begin > 2009;
    })
    .map(function(doctor) {
        return {
            doctorNumber: "#" + doctor.number,
            playedBy: doctor.actor,
            yearsPlayed: doctor.end - doctor.begin + 1
        };
    })
    .value();

=>  [{ doctorNumber: "#9",  playedBy: "Christopher Eccleston", yearsPlayed: 1 }]
*/
    @Test
    public void chain4() throws Exception {
        final List<Map<String, Object>> doctors = new ArrayList<Map<String, Object>>() {{
            add(new LinkedHashMap<String, Object>() {{ put("number", 1); put("actor", "William Hartnell"); put("begin", 1963); put("end", 1966); }});
            add(new LinkedHashMap<String, Object>() {{ put("number", 9); put("actor", "Christopher Eccleston"); put("begin", 2005); put("end", 2005); }});
            add(new LinkedHashMap<String, Object>() {{ put("number", 10); put("actor", "David Tennant"); put("begin", 2005); put("end", 2010); }});
        }};
        final String result = $.chain(doctors)
            .filter(
                new Predicate<Map<String, Object>>() {
                public Boolean apply(Map<String, Object> item) {
                    return (Integer) item.get("begin") > 2000;
                }
            })
            .reject(
                new Predicate<Map<String, Object>>() {
                public Boolean apply(Map<String, Object> item) {
                    return (Integer) item.get("end") > 2009;
                }
            })
            .map(
                new Function1<Map<String, Object>, Map<String, Object>>() {
                public Map<String, Object> apply(final Map<String, Object> item) {
                    return new LinkedHashMap<String, Object>() {{ 
                        put("doctorNumber", "#" + item.get("number"));
                        put("playedBy", item.get("actor"));
                        put("yearsPlayed", (Integer) item.get("end") - (Integer) item.get("begin") + 1);
                    }};
                }
            })
            .value().toString();
        assertEquals("[{doctorNumber=#9, playedBy=Christopher Eccleston, yearsPlayed=1}]", result);
    }

/*
var doctors = [
    { number: 1,  actor: "William Hartnell",      begin: 1963, end: 1966 },
    { number: 9,  actor: "Christopher Eccleston", begin: 2005, end: 2005 },
    { number: 10, actor: "David Tennant",         begin: 2005, end: 2010 }
];
_.chain(doctors)
    .skip(1)
    .limit(1)
    .value();

=>  [{ number: 9,  actor: "Christopher Eccleston", begin: 2005, end: 2005 }]
*/
    @Test
    public void chain5() throws Exception {
        final List<Map<String, Object>> doctors = new ArrayList<Map<String, Object>>() {{
            add(new LinkedHashMap<String, Object>() {{ put("number", 1); put("actor", "William Hartnell"); put("begin", 1963); put("end", 1966); }});
            add(new LinkedHashMap<String, Object>() {{ put("number", 9); put("actor", "Christopher Eccleston"); put("begin", 2005); put("end", 2005); }});
            add(new LinkedHashMap<String, Object>() {{ put("number", 10); put("actor", "David Tennant"); put("begin", 2005); put("end", 2010); }});
        }};
        final String result = $.chain(doctors)
            .skip(1)
            .limit(1)
            .value().toString();
        assertEquals("[{number=9, actor=Christopher Eccleston, begin=2005, end=2005}]", result);
    }

    @Test
    public void chain6() throws Exception {
        final List<String> result = $.chain($.class.getDeclaredMethods())
            .reduce(new FunctionAccum<List<String>, Method>() {
                public List<String> apply(final List<String> accum, final Method method) {
                    accum.add(method.getName());
                    return accum;
                }
            }, new ArrayList<String>())
            .reject(new Predicate<String>() {
                public Boolean apply(final String name) {
                    return name.contains("$");
                }
            })
            .uniq()
            .sort()
            .first(4)
            .value();
        assertEquals("[after, all, any, before]", result.toString());
    }

/*
_.now();
=> 1392066795351
*/
    @Test
    public void now() throws Exception {
        assertTrue($.now() >= new Date().getTime());
    }

/*
var compiled = _.template("hello: <%= name %>");
compiled({name: 'moe'});
=> "hello: moe"
*/
    @Test
    public void template() throws Exception {
        Template<Set<Map.Entry<String,Object>>> compiled = $.template("hello: <%= name %>");
        assertEquals("hello: moe", compiled.apply(new LinkedHashMap<String, Object>() {{ put("name", "moe"); }}.entrySet()));
    }

    @Test
    public void template2() throws Exception {
        Template<Set<Map.Entry<String,Object>>> compiled = $.template("hello: <%= name %>, hello2: <%= name %>");
        assertEquals("hello: moe, hello2: moe", compiled.apply(new LinkedHashMap<String, Object>() {{ put("name", "moe"); }}.entrySet()));
    }

    @Test
    public void template3() throws Exception {
        Template<Set<Map.Entry<String,Object>>> compiled = $.template("hello: <%= name %>, hello2: <%= name2 %>");
        assertEquals("hello: moe, hello2: moe2", compiled.apply(
            new LinkedHashMap<String, Object>() {{ put("name", "moe"); put("name2", "moe2"); }}.entrySet()));
    }
/*
var list = "<% _.each(people, function(name) { %> <li><%= name %></li> <% }); %>";
_.template(list, {people: ['moe', 'curly', 'larry']});
=> "<li>moe</li><li>curly</li><li>larry</li>"
*/
    @Test
    public void templateEach() throws Exception {
        String list = "<% _.each(people, function(name) { %> <li><%= name %></li> <% }); %>";
        Template<Set<Map.Entry<String,Object>>> compiled = $.template(list);
        assertEquals(" <li>moe</li>  <li>curly</li>  <li>larry</li> ",
            compiled.apply(new LinkedHashMap<String, Object>() {{ put("people", asList("moe", "curly", "larry")); }}.entrySet()));
        String list2 = "<% _.each(people2, function(name) { %> <li><%= name %></li> <% }); %>";
        Template<Set<Map.Entry<String,Object>>> compiled2 = $.template(list2);
        assertEquals("<% _.each(people2, function(name) { %> <li><%= name %></li> <% }); %>",
            compiled2.apply(new LinkedHashMap<String, Object>() {{ put("people", asList("moe", "curly", "larry")); }}.entrySet()));
    }

/*
var template = _.template("<b><%- value %></b>");
template({value: '<script>'});
=> "<b>&lt;script&gt;</b>"
*/
    @Test
    public void templateValue() throws Exception {
        Template<Set<Map.Entry<String,Object>>> template = $.template("<b><%- value %></b>");
        assertEquals("<b>&lt;script&gt;</b>",
            template.apply(new LinkedHashMap<String, Object>() {{ put("value", "<script>"); }}.entrySet()));
    }

    @Test
    public void templateValue2() throws Exception {
        Template<Set<Map.Entry<String,Object>>> template = $.template("hello: <%= name %>, <b><%- value %></b>");
        assertEquals("hello: moe, <b>&lt;script&gt;</b>",
            template.apply(new LinkedHashMap<String, Object>() {{ put("name", "moe"); put("value", "<script>"); }}.entrySet()));
    }
/*
var compiled = _.template("<% print('Hello ' + epithet); %>");
compiled({epithet: "stooge"});
=> "Hello stooge"
*/
    @Test
    public void templatePrint() throws Exception {
        Template<Set<Map.Entry<String,Object>>> compiled = $.template("<% print('Hello ' + epithet); %>");
        assertEquals("Hello stooge",
            compiled.apply(new LinkedHashMap<String, Object>() {{ put("epithet", "stooge"); }}.entrySet()));
        Template<Set<Map.Entry<String,Object>>> compiled2 = $.template("<% print('Hello ' + epithet2); %>");
        assertEquals("<% print('Hello ' + epithet2); %>",
            compiled2.apply(new LinkedHashMap<String, Object>() {{ put("epithet", "stooge"); }}.entrySet()));
    }

/*
_.escape('Curly, Larry & Moe');
=> "Curly, Larry &amp; Moe"
*/
    @Test
    public void escape() throws Exception {
        assertEquals("Curly, Larry &amp; Moe", $.escape("Curly, Larry & Moe"));
    }

/*
_.unescape('Curly, Larry &amp; Moe');
=> "Curly, Larry & Moe"
*/
    @Test
    public void unescape() throws Exception {
        assertEquals("Curly, Larry & Moe", $.unescape("Curly, Larry &amp; Moe"));
    }

/*
var object = {cheese: 'crumpets', stuff: function(){ return 'nonsense'; }};
_.result(object, 'cheese');
=> "crumpets"
_.result(object, 'stuff');
=> "nonsense"
*/
    @Test
    public void result() throws Exception {
        Map<String, Object> object = new LinkedHashMap<String, Object>() {{
            put("cheese", "crumpets");
            put("stuff", new Function<String>() { public String apply() { return "nonsense"; }});
        }};

        assertEquals("crumpets", $.result(object.entrySet(), new Predicate<Map.Entry<String, Object>>() {
            public Boolean apply(Map.Entry<String, Object> item) {
                return item.getKey().equals("cheese");
            }
        }));
        assertEquals("nonsense", $.result(object.entrySet(), new Predicate<Map.Entry<String, Object>>() {
            public Boolean apply(Map.Entry<String, Object> item) {
                return item.getKey().equals("stuff");
            }
        }));
        assertEquals("result1", $.result(asList("result1", "result2"), new Predicate<String>() {
            public Boolean apply(String item) {
                return item.equals("result1");
            }
        }));
        assertEquals(null, $.result(asList("result1", "result2"), new Predicate<String>() {
            public Boolean apply(String item) {
                return item.equals("result3");
            }
        }));
    }

/*
var renderNotes = _.after(notes.length, render);
_.each(notes, function(note) {
  note.asyncSave({success: renderNotes});
});
// renderNotes is run once, after all notes have saved.
*/
    @Test
    public void after() throws Exception {
        final List<Integer> notes = asList(1, 2, 3);
        final Function<Integer> renderNotes = $.after(notes.size(),
            new Function<Integer>() { public Integer apply() { return 4; }});
        final List<Integer> result = new ArrayList<Integer>();
        $.<Integer>each(notes, new Block<Integer>() {
            public void apply(Integer item) {
                result.add(item);
                Integer afterResult = renderNotes.apply();
                if (afterResult != null) {
                    result.add(afterResult);
                }
            }
        });
        assertEquals("[1, 2, 3, 4]", result.toString());
    }

/*
var monthlyMeeting = _.before(3, askForRaise);
monthlyMeeting();
monthlyMeeting();
monthlyMeeting();
// the result of any subsequent calls is the same as the second call
*/
    @Test
    public void before() throws Exception {
        final List<Integer> notes = asList(1, 2, 3);
        final Function<Integer> renderNotes = $.before(notes.size() - 1,
            new Function<Integer>() { public Integer apply() { return 4; }});
        final List<Integer> result = new ArrayList<Integer>();
        $.<Integer>each(notes, new Block<Integer>() {
            public void apply(Integer item) {
                result.add(item);
                Integer afterResult = renderNotes.apply();
                if (afterResult != null) {
                    result.add(afterResult);
                }
            }
        });
        assertEquals("[1, 4, 2, 4, 3]", result.toString());
    }

/*
var hello = function(name) { return "hello: " + name; };
hello = _.wrap(hello, function(func) {
  return "before, " + func("moe") + ", after";
});
hello();
=> 'before, hello: moe, after'
*/
    @Test
    public void wrap() throws Exception {
        Function1<String, String> hello = new Function1<String, String>() {
            public String apply(final String name) {
                return "hello: " + name;
            }
        };
        Function1<Void, String> result = $.wrap(hello, new Function1<Function1<String, String>, String>() {
            public String apply(final Function1<String, String> func) {
                return "before, " + func.apply("moe") + ", after";
            }
        });
        assertEquals("before, hello: moe, after", result.apply(null));
    }

/*
var isFalsy = _.negate(Boolean);
_.find([-2, -1, 0, 1, 2], isFalsy);
=> 0
*/
    @Test
    public void negate() throws Exception {
        Predicate<Integer> isFalsy = $.negate(new Predicate<Integer>() {
            public Boolean apply(final Integer item) {
                return item != 0;
            }
        });
        Optional<Integer> result = $.find(asList(-2, -1, 0, 1, 2), isFalsy);
        assertEquals(0, result.get());
    }

/*
var greet    = function(name){ return "hi: " + name; };
var exclaim  = function(statement){ return statement.toUpperCase() + "!"; };
var welcome = _.compose(greet, exclaim);
welcome('moe');
=> 'hi: MOE!'
*/
    @Test
    public void compose() throws Exception {
        Function1<String, String> greet = new Function1<String, String>() {
            public String apply(final String name) {
                return "hi: " + name;
            }
        };
        Function1<String, String> exclaim = new Function1<String, String>() {
            public String apply(final String statement) {
                return statement.toUpperCase() + "!";
            }
        };
        Function1<String, String> welcome = $.compose(greet, exclaim);
        assertEquals("hi: MOE!", welcome.apply("moe"));
    }

/*
_.keys({one: 1, two: 2, three: 3});
=> ["one", "two", "three"]
*/
    @Test
    public void keys() throws Exception {
        Set<String> result = $.keys(new LinkedHashMap<String, Object>() {{ put("one", 1); put("two", 2); put("three", 3); }});
        assertEquals("[one, two, three]", result.toString());
    }

/*
_.values({one: 1, two: 2, three: 3});
=> [1, 2, 3]
*/
    @Test
    public void values() throws Exception {
        List<Integer> result = $.values(new LinkedHashMap<String, Integer>() {{ put("one", 1); put("two", 2); put("three", 3); }});
        assertEquals("[1, 2, 3]", result.toString());
    }

/*
var iceCream = {flavor: "chocolate"};
_.defaults(iceCream, {flavor: "vanilla", sprinkles: "lots"});
=> {flavor: "chocolate", sprinkles: "lots"}
*/
    @Test
    public void defaults() throws Exception {
        Map<String, String> iceCream = new LinkedHashMap<String, String>() {{ put("flavor", "chocolate"); }};
        Map<String, String> result = $.defaults(iceCream, new LinkedHashMap<String, String>() {{ put("flavor", "vanilla"); put("sprinkles", "lots"); }});
        assertEquals("{flavor=chocolate, sprinkles=lots}", result.toString());
    }

/*
_.clone({name: 'moe'});
=> {name: 'moe'};
*/
    @Test
    public void cloneMap() throws Exception {
        Map<String, String> result = (Map<String, String>) $.clone(new LinkedHashMap<String, String>() {{ put("name", "moe"); }});
        assertEquals("{name=moe}", result.toString());
        Integer[] result2 = (Integer[]) $.clone(new Integer[] { 1, 2, 3, 4, 5 });
        assertEquals("[1, 2, 3, 4, 5]", asList(result2).toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void cloneError() throws Exception {
        class Test {};
        $.clone(new Test());
    }

    @Test(expected = IllegalArgumentException.class)
    public void cloneError2() throws Exception {
        class Test implements Cloneable {
            public Object clone(String arg) {return null;}
        };
        $.clone(new Test());
    }

    @Test(expected = IllegalArgumentException.class)
    public void cloneError3() throws Exception {
        class Test implements Cloneable {
            public Object clone() {throw new RuntimeException();}
        };
        $.clone(new Test());
    }
/*
_.chain([1,2,3,200])
  .filter(function(num) { return num % 2 == 0; })
  .tap(alert)
  .map(function(num) { return num * num })
  .value();
=> // [2, 200] (alerted)
=> [4, 40000]
*/
    @Test
    public void tap() throws Exception {
        final List<Map.Entry<String, Integer>> result = new ArrayList<Map.Entry<String, Integer>>();
        $.tap(new LinkedHashMap<String, Integer>() {{ put("a", 1); put("b", 2); put("c", 3); }}.entrySet(), 
            new Block<Map.Entry<String, Integer>>() {
                public void apply(final Map.Entry<String, Integer> item) {
                    result.add(item);
                }
            });
        assertEquals("[a=1, b=2, c=3]", result.toString());
        final List<Map.Entry<String, Integer>> resultChain = new ArrayList<Map.Entry<String, Integer>>();
        $.chain(new LinkedHashMap<String, Integer>() {{ put("a", 1); put("b", 2); put("c", 3); }}.entrySet())
            .tap(new Block<Map.Entry<String, Integer>>() {
                public void apply(final Map.Entry<String, Integer> item) {
                    resultChain.add(item);
                }
            });
        assertEquals("[a=1, b=2, c=3]", resultChain.toString());
    }

/*
_.has({a: 1, b: 2, c: 3}, "b");
=> true
*/
    @Test
    public void has() throws Exception {
        boolean result = $.has(new LinkedHashMap<String, Integer>() {{ put("a", 1); put("b", 2); put("c", 3); }}, "b");
        assertTrue(result);
    }

/*
var stooge = {name: 'moe'};
'moe' === _.property('name')(stooge);
=> true
*/
    @Test
    public void property() throws Exception {
        Map<String, Object> stooge = new LinkedHashMap<String, Object>() {{ put("name", "moe"); }};
        assertEquals("moe", $.property("name").apply(stooge));
    }

/*
var stooge = {name: 'moe'};
_.propertyOf(stooge)('name');
=> 'moe'
*/
    @Test
    public void propertyOf() throws Exception {
        Map<String, String> stooge = new LinkedHashMap<String, String>() {{ put("name", "moe"); }};
        assertEquals("moe", $.propertyOf(stooge).apply("name"));
    }

/*
var func = function(greeting){ return greeting + ': ' + this.name };
func = _.bind(func, {name: 'moe'}, 'hi');
func();
=> 'hi: moe'
*/
    @Test
    public void bind() throws Exception {
        class GreetingFunction implements Function1<String, String> {
            private final String name;
            public GreetingFunction(final String name) {
                this.name = name;
            }
            public String apply(final String greeting) {
                return greeting + ": " + this.name;
            }
        };
        assertEquals("hi: moe", $.bind(new GreetingFunction("moe")).apply("hi"));
    }

/*
var subtract = function(a, b) { return b - a; };
sub5 = _.partial(subtract, 5);
sub5(20);
=> 15
*/
    @Test
    public void partial() throws Exception {
        class SubtractFunction implements Function1<Integer, Integer> {
            private final Integer arg1;
            public SubtractFunction(final Integer arg1) {
                this.arg1 = arg1;
            }
            public Integer apply(final Integer arg2) {
                return arg2 - arg1;
            }
        };
        Function1<Integer, Integer> sub5 = new SubtractFunction(5);
        assertEquals(15, sub5.apply(20));
    }

/*
var counter = 0;
var incr = function(){ counter++; };
var debouncedIncr = _.debounce(incr, 32);
debouncedIncr(); debouncedIncr();
_.delay(debouncedIncr, 16);
_.delay(function(){ equal(counter, 1, 'incr was debounced'); }, 96);
*/

    @Test
    public void debounce() throws Exception {
        final Integer[] counter = new Integer[] {0};
        Function<Void> incr = new Function<Void>() { public Void apply() { counter[0]++; return null; }};
        Function<Void> debouncedIncr = $.debounce(incr, 50);
        debouncedIncr.apply();
        debouncedIncr.apply();
        $.delay(debouncedIncr, 16);
        $.delay(new Function<Void>() {
            public Void apply() {
                assertEquals("incr was debounced", 1, counter[0]);
                return null;
            }
        }, 32);
        Thread.sleep(120);
    }

/*
var initialize = _.once(createApplication);
initialize();
initialize();
// Application is only created once.
*/

    @Test
    public void once() throws Exception {
        final Integer[] counter = new Integer[] {0};
        Function<Void> incr = new Function<Void>() { public Void apply() { counter[0]++; return null; }};
        Function<Void> onceIncr = $.once(incr);
        onceIncr.apply();
        onceIncr.apply();
        Thread.sleep(16);
        assertEquals("incr was called only once", 1, counter[0]);
    }

/*
_.defer(function(){ alert('deferred'); });
// Returns from the function before the alert runs.
*/
    @Test
    public void defer() throws Exception {
        final Integer[] counter = new Integer[] {0};
        $.defer(new Function<Void>() { public Void apply() { counter[0]++; return null; }});
        assertEquals("incr was debounced", 0, counter[0]);
        Thread.sleep(16);
        assertEquals("incr was debounced", 1, counter[0]);
    }

    @Test
    public void main() throws Exception {
        $.main(new String[] {});
    }

/*
['some', 'words', 'example'].sort();
=> ['example', 'some', 'words']
*/
    @Test
    public void sort() throws Exception {
        assertEquals("[example, some, words]", $.sort(asList("some", "words", "example")).toString());
        assertEquals("[example, some, words]", $.chain(asList("some", "words", "example")).sort().value().toString());
        assertEquals("[4, 5, 7]", $.chain(asList("some", "words", "example"))
            .map(new Function1<String, Integer>() {
                public Integer apply(String arg) {
                    return arg.length();
                }
            }).sort().value().toString());
        assertEquals("[example, some, words]", asList($.sort(new String[] {"some", "words", "example"})).toString());
    }

/*
['some', 'words', 'example'].join('-');
=> 'some-words-example'
*/
    @Test
    public void join() throws Exception {
        assertEquals("some-words-example", $.join(asList("some", "words", "example"), "-"));
        assertEquals("some-words-example", $.join(new String[] {"some", "words", "example"}, "-"));
        assertEquals("some-words-example", $.chain(asList("some", "words", "example")).join("-").item());
    }

    @Test
    public void compareStrings() throws Exception {
        assertEquals($.sort("CAT".split("")), $.sort("CTA".split("")));
    }

/*
_.concat([1, 2], [3, 4]);
=> [1, 2, 3, 4]
*/
    @Test
    public void concat() throws Exception {
        assertEquals(asList(1, 2, 3, 4), asList($.concat(new Integer[] {1, 2}, new Integer[] {3, 4})));
        assertEquals(asList(1, 2, 3, 4), $.concat(asList(1, 2), asList(3, 4)));
        assertEquals("[1, 2, 3, 4]", $.chain(asList(1, 2)).concat(asList(3, 4)).value().toString());
        assertEquals(asList(1, 2, 3, 4), asList($.concat(new Integer[] {1, 2}, new Integer[] {3}, new Integer[] {4})));
        assertEquals(asList(1, 2, 3, 4), $.concat(asList(1, 2), asList(3), asList(4)));
    }

/*
var arr = [ 1, 2, 3, 4, 5 ]
arr.slice(2) // => [3, 4, 5]
arr.slice(1, 4) // => [2, 3, 4]
arr.slice(2, 3) // => [3]
arr.slice(-2) // => [4, 5]
arr.slice(-3, -1) // [3, 4]
*/
    @Test
    public void slice() {
        assertEquals(asList(3, 4, 5), $.slice(asList(1, 2, 3, 4, 5), 2));
        assertEquals(asList(2, 3, 4), $.slice(asList(1, 2, 3, 4, 5), 1, 4));
        assertEquals(asList(2, 3, 4), $.slice(asList(1, 2, 3, 4, 5), 1, -1));
        assertEquals(asList(3), $.slice(asList(1, 2, 3, 4, 5), 2, 3));
        assertEquals(asList(4, 5), $.slice(asList(1, 2, 3, 4, 5), -2));
        assertEquals(asList(3, 4), $.slice(asList(1, 2, 3, 4, 5), -3, -1));
        assertEquals(asList(3, 4), $.slice(asList(1, 2, 3, 4, 5), -3, 4));
        assertEquals(asList(3, 4, 5), $.chain(asList(1, 2, 3, 4, 5)).slice(2).value());
        assertEquals(asList(2, 3, 4), $.chain(asList(1, 2, 3, 4, 5)).slice(1, 4).value());
        assertEquals(asList(3, 4, 5), asList($.slice(new Integer[] {1, 2, 3, 4, 5}, 2)));
        assertEquals(asList(2, 3, 4), asList($.slice(new Integer[] {1, 2, 3, 4, 5}, 1, 4)));
    }

/*
[1, 2, 3].reverse() // [3, 2, 1]
*/
    @Test
    public void reverse() {
        assertEquals("[3, 2, 1]", $.reverse(asList(1, 2, 3)).toString());
        assertEquals("[3, 2, 1]", asList($.reverse(new Integer[] {1, 2, 3})).toString());
        assertEquals("[3, 2, 1]", $.chain(asList(1, 2, 3)).reverse().value().toString());
    }

/*
_.random(0, 100);
=> 42
*/
    @Test
    public void random() {
        int result = $.random(0, 100);
        assertTrue(result >= 0);
        assertTrue(result <= 100);
    }

/*
_.mixin({
  capitalize: function(string) {
    return string.charAt(0).toUpperCase() + string.substring(1).toLowerCase();
  }
});
_("fabio").capitalize();
=> "Fabio"
*/
    @Test
    public void mixin() {
        $.mixin("capitalize", new Function1<String, String>() {
            public String apply(final String string) {
                return String.valueOf(string.charAt(0)).toUpperCase() + string.substring(1).toLowerCase();
            }
        });
        assertEquals("Fabio", new $("fabio").call("capitalize").get());
        assertFalse(new $("fabio").call("capitalize2").isPresent());
        assertFalse(new $(asList(1)).call("capitalize2").isPresent());
    }

/*
_.uniqueId('contact_');
=> 'contact_104'
*/
    @Test
    public void uniqueId() {
        assertEquals("contact_1", $.uniqueId("contact_"));
    }

/*
_.times(3, function(n){ genie.grantWishNumber(n); });
*/
    @Test
    public void times() {
        final List<Integer> result = new ArrayList<Integer>();
        $.times(3, new Function<Integer>() {
            public Integer apply() {
                result.add(1);
                return null;
            }
        });
        assertEquals("[1, 1, 1]", result.toString());
    }

/*
var stooge = {name: 'moe', luckyNumbers: [13, 27, 34]};
var clone  = {name: 'moe', luckyNumbers: [13, 27, 34]};
stooge == clone;
=> false
_.isEqual(stooge, clone);
=> true
*/
    @Test
    public void isEqual() {
        Map<String, Object> stooge = new LinkedHashMap<String, Object>() {{ put("name", "moe"); put("luckyNumbers", asList(13, 27, 34)); }};
        Map<String, Object> clone = new LinkedHashMap<String, Object>() {{ put("name", "moe"); put("luckyNumbers", asList(13, 27, 34)); }};
        assertFalse(stooge == clone);
        assertTrue($.isEqual(stooge, clone));
        assertTrue($.isEqual(null, null));
        assertFalse($.isEqual(stooge, null));
        assertFalse($.isEqual(null, clone));
    }

/*
_.isArray([1,2,3]);
=> true
*/
    @Test
    public void isArray() {
        assertTrue($.isArray(new int[] {1, 2, 3, 4, 5}));
        assertFalse($.isArray(null));
        assertFalse($.isArray("string"));
    }

/*
_.isObject({});
=> true
_.isObject(1);
=> false
*/
    @Test
    public void isObject() {
        assertTrue($.isObject(new LinkedHashMap<String, String>()));
        assertFalse($.isObject(null));
        assertFalse($.isObject("string"));
    }

/*
_.isFunction(alert);
=> true
*/
    @Test
    public void isFunction() {
        assertTrue($.isFunction(new Function1<String, Integer>() { public Integer apply(final String arg) { return null; }}));
    }

/*
_.isString("moe");
=> true
*/
    @Test
    public void isString() {
        assertTrue($.isString("moe"));
    }

/*
_.isNumber(8.4 * 5);
=> true
*/
    @Test
    public void isNumber() {
        assertTrue($.isNumber(8.4 * 5));
    }

/*
_.isBoolean(null);
=> false
*/
    @Test
    public void isBoolean() {
        assertTrue($.isBoolean(false));
        assertFalse($.isBoolean(null));
    }

/*
_.isDate(new Date());
=> true
*/
    @Test
    public void isDate() {
        assertTrue($.isDate(new java.util.Date()));
        assertFalse($.isDate(null));
    }

/*
_.isRegExp(/moe/);
=> true
*/
    @Test
    public void isRegExp() {
        assertTrue($.isRegExp(java.util.regex.Pattern.compile("moe")));
        assertFalse($.isRegExp(null));
    }

/*
try {
  throw new TypeError("Example");
} catch (o_O) {
  _.isError(o_O)
}
=> true
*/
    @Test
    public void isError() {
        assertTrue($.isError(new Exception()));
        assertFalse($.isError(null));
    }

/*
_.isNull(null);
=> true
*/
    @Test
    public void isNull() {
        assertTrue($.isNull(null));
        assertFalse($.isNull(""));
    }

    @Test
    public void classForName_without_guava() {
        $.classForName = new $.ClassForName() {
            public Class<?> call(final String name) throws Exception {
                throw new Exception("expected");
            }
        };
        final List<Integer> result1 = $.filter(asList(1, 2, 3, 4, 5, 6),
            new Predicate<Integer>() {
            public Boolean apply(Integer item) {
                return item % 2 == 0;
            }
        });
        assertEquals("[2, 4, 6]", result1.toString());
        final List<Integer> result2 = $.shuffle(asList(1, 2, 3, 4, 5, 6));
        assertEquals(6, result2.size());
        List<Integer> result3 = $.map(asList(1, 2, 3), new Function1<Integer, Integer>() {
            public Integer apply(Integer item) {
                return item * 3;
            }
        });
        assertEquals("[3, 6, 9]", result3.toString());
        final Set<Integer> result4 =
        $.map(new LinkedHashMap<Integer, String>() {{ put(1, "one"); put(2, "two"); put(3, "three"); }}.entrySet(),
            new Function1<Map.Entry<Integer, String>, Integer>() {
            public Integer apply(Map.Entry<Integer, String> item) {
                return item.getKey() * 3;
            }
        });
        assertEquals("[3, 6, 9]", result4.toString());
        final List<Integer> result5 = $.union(asList(1, 2, 3), asList(101, 2, 1, 10), asList(2, 1));
        assertEquals("[1, 2, 3, 101, 10]", result5.toString());
        final Map<Double, List<Double>> result6 =
        $.groupBy(asList(1.3, 2.1, 2.4),
            new Function1<Double, Double>() {
            public Double apply(Double num) {
                return Math.floor(num);
            }
        });
        assertEquals("{1.0=[1.3], 2.0=[2.1, 2.4]}", result6.toString());
        final List<Integer> result7 = $.uniq(asList(1, 2, 1, 3, 1, 4));
        assertEquals("[1, 2, 3, 4]", result7.toString());
        final int[] array = new int[] {1, 2, 3, 4, 5, 6};
        Iterable<Integer> iterable = new Iterable<Integer>() {
            public Iterator<Integer> iterator() {
                return new Iterator<Integer>() {
                    private int index = 0;
                    public boolean hasNext() {
                        return array.length > index;
                    }
                    public Integer next() {
                        return array[index++];
                    }
                    public void remove() {
                    }
                };
            }
        };
        final Optional<Integer> result8 = $.findLast(iterable,
            new Predicate<Integer>() {
            public Boolean apply(Integer item) {
                return item % 2 == 0;
            }
        });
        assertEquals("Optional.of(6)", result8.toString());
        $.classForName = new $.ClassForName();
    }

    @Test
    public void optional() {
        assertTrue(Optional.absent().equals(Optional.absent()));
        assertTrue(Optional.of(1).equals(Optional.of(1)));
        assertTrue(Optional.of(null).equals(Optional.of(null)));
        assertFalse(Optional.absent().equals(Optional.of(1)));
        assertFalse(Optional.of(null).equals(Optional.of(1)));
        assertFalse(Optional.of(1).equals(Optional.of(null)));
        assertFalse(Optional.of(1).equals(Optional.absent()));
        assertFalse(Optional.of(1).equals(Optional.of(2)));
        assertFalse(Optional.of(1).equals("test"));
        assertEquals(0, Optional.absent().hashCode());
        assertEquals("123".hashCode(), Optional.of("123").hashCode());
        assertEquals("Optional.absent()", Optional.absent().toString());
        assertEquals("Optional.of(1)", Optional.of(1).toString());
    }
}
