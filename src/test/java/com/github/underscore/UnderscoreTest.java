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
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
        Underscore.<Integer>each(asList(1, 2, 3), new Block<Integer>() {
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
        Underscore.forEach(asList(1, 2, 3), new Block<Integer>() {
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
        new Underscore(asList(1, 2, 3)).forEach(new Block<Integer>() {
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
        Underscore.<Map.Entry<String, Integer>>each(new LinkedHashMap<String, Integer>() {{ put("one", 1); put("two", 2); put("three", 3); }}.entrySet(),
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
        List<Integer> result = Underscore.map(asList(1, 2, 3), new Function1<Integer, Integer>() {
            public Integer apply(Integer item) {
                return item * 3;
            }
        });
        assertEquals("[3, 6, 9]", result.toString());
    }

/*
_.collect([1, 2, 3], function(num){ return num * 3; });
=> [3, 6, 9]
*/
    @Test
    public void collect() {
        List<Integer> result = Underscore.collect(asList(1, 2, 3), new Function1<Integer, Integer>() {
            public Integer apply(Integer item) {
                return item * 3;
            }
        });
        assertEquals("[3, 6, 9]", result.toString());
        Set<Integer> resultSet = Underscore.collect(new LinkedHashSet(asList(1, 2, 3)), new Function1<Integer, Integer>() {
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
        Underscore.map(new LinkedHashMap<Integer, String>() {{ put(1, "one"); put(2, "two"); put(3, "three"); }}.entrySet(),
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
        Underscore.reduce(asList(1, 2, 3),
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
        Underscore.reduceRight(asList(asList(0, 1), asList(2, 3), asList(4, 5)),
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
        Underscore.inject(asList(asList(0, 1), asList(2, 3), asList(4, 5)),
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
        Underscore.foldl(asList(asList(0, 1), asList(2, 3), asList(4, 5)),
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
        Underscore.foldr(asList(asList(0, 1), asList(2, 3), asList(4, 5)),
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
        final boolean result = Underscore.contains(asList(1, 2, 3), 3);
        assertTrue(result);
    }

/*
var even = _.find([1, 2, 3, 4, 5, 6], function(num){ return num % 2 == 0; });
=> 2
*/
    @Test
    public void find() {
        final Integer result = Underscore.find(asList(1, 2, 3, 4, 5, 6), 
            new Predicate<Integer>() {
            public Boolean apply(Integer item) {
                return item % 2 == 0;
            }
        });
        assertEquals("2", result.toString());
    }

/*
var even = _.find([1, 2, 3, 4, 5, 6], function(num){ return num % 2 == 0; });
=> 2
*/
    @Test
    public void detect() {
        final Integer result = Underscore.detect(asList(1, 2, 3, 4, 5, 6), 
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
        final List<Integer> result = Underscore.filter(asList(1, 2, 3, 4, 5, 6), 
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
        final List<Integer> result = Underscore.reject(asList(1, 2, 3, 4, 5, 6), 
            new Predicate<Integer>() {
            public Boolean apply(Integer item) {
                return item % 2 == 0;
            }
        });
        assertEquals("[1, 3, 5]", result.toString());
        final Set<Integer> resultSet = Underscore.reject(new LinkedHashSet(asList(1, 2, 3, 4, 5, 6)), 
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
        final List<Integer> result = Underscore.select(asList(1, 2, 3, 4, 5, 6), 
            new Predicate<Integer>() {
            public Boolean apply(Integer item) {
                return item % 2 == 0;
            }
        });
        assertEquals("[2, 4, 6]", result.toString());
        final Set<Integer> resultSet = Underscore.select(new LinkedHashSet(asList(1, 2, 3, 4, 5, 6)), 
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
        final Boolean result1 = Underscore.all(asList(1, 2, 3, 4), 
            new Predicate<Integer>() {
            public Boolean apply(Integer item) {
                return item % 2 == 0;
            }
        });
        final Boolean result2 = Underscore.all(asList(1, 2, 3, 4), 
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
        final Boolean result1 = Underscore.any(asList(1, 2, 3, 4), 
            new Predicate<Integer>() {
            public Boolean apply(Integer item) {
                return item % 2 == 0;
            }
        });
        final Boolean result2 = Underscore.any(asList(1, 2, 3, 4), 
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
        final Boolean result = Underscore.include(asList(1, 2, 3), 3); 
        assertTrue(result);
    }

/*
_.invoke([" foo ", "  bar"], "trim"); // ["foo", "bar"]
*/
    @Test
    public void invoke() throws Exception {
        assertEquals(Underscore.invoke(asList(" foo ", "  bar"), "trim"), asList("foo", "bar"));
        assertEquals(Underscore.invoke(asList("foo", "bar"), "concat", Arrays.<Object>asList("1")), asList("foo1", "bar1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void invoke2() throws Exception {
        Underscore.invoke(asList("foo", 123), "concat", Arrays.<Object>asList("1"));
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
            Underscore.where(listOfPlays, asList(
            Tuple.<String, Object>create("author", "Shakespeare"),
            Tuple.<String, Object>create("year", Integer.valueOf(1611)))).toString());
        assertEquals("[title: Cymbeline, author: Shakespeare, year: 1611,"
            + " title: The Tempest, author: Shakespeare, year: 1611]",
            Underscore.where(listOfPlays, asList(
            Tuple.<String, Object>create("author", "Shakespeare"),
            Tuple.<String, Object>create("author2", "Shakespeare"),
            Tuple.<String, Object>create("year", Integer.valueOf(1611)))).toString());
        assertEquals("[title: Cymbeline, author: Shakespeare, year: 1611,"
            + " title: The Tempest, author: Shakespeare, year: 1611]",
            Underscore.where(new LinkedHashSet<Book>(listOfPlays), asList(
            Tuple.<String, Object>create("author", "Shakespeare"),
            Tuple.<String, Object>create("year", Integer.valueOf(1611)))).toString());
        assertEquals("[title: Cymbeline, author: Shakespeare, year: 1611,"
            + " title: The Tempest, author: Shakespeare, year: 1611]",
            Underscore.where(new LinkedHashSet<Book>(listOfPlays), asList(
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
            Underscore.findWhere(listOfPlays, asList(
            Tuple.<String, Object>create("author", "Shakespeare"),
            Tuple.<String, Object>create("year", Integer.valueOf(1611)))).toString());
        assertEquals("title: Cymbeline, author: Shakespeare, year: 1611",
            Underscore.findWhere(listOfPlays, asList(
            Tuple.<String, Object>create("author", "Shakespeare"),
            Tuple.<String, Object>create("author2", "Shakespeare"),
            Tuple.<String, Object>create("year", Integer.valueOf(1611)))).toString());
    }

/*
_.first([5, 4, 3, 2, 1]);
=> 5
_.first([5, 4, 3, 2, 1], 2);
=> [5, 4]
*/
    @Test
    public void first() {
        final Integer result = Underscore.first(asList(5, 4, 3, 2, 1));
        assertEquals("5", result.toString());
        final List<Integer> resultList = Underscore.first(asList(5, 4, 3, 2, 1), 2);
        assertEquals("[5, 4]", resultList.toString());
        final int resultInt = Underscore.first(new Integer[] {5, 4, 3, 2, 1});
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
        final Integer result = Underscore.head(asList(5, 4, 3, 2, 1));
        assertEquals("5", result.toString());
        final Integer resultObj = new Underscore<Integer>(asList(5, 4, 3, 2, 1)).head();
        assertEquals("5", resultObj.toString());
        final List<Integer> resultList = Underscore.head(asList(5, 4, 3, 2, 1), 2);
        assertEquals("[5, 4]", resultList.toString());
        final List<Integer> resultListObj = new Underscore<Integer>(asList(5, 4, 3, 2, 1)).head(2);
        assertEquals("[5, 4]", resultListObj.toString());
        final int resultInt = Underscore.head(new Integer[] {5, 4, 3, 2, 1});
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
        final List<Integer> result = Underscore.initial(asList(5, 4, 3, 2, 1));
        assertEquals("[5, 4, 3, 2]", result.toString());
        final List<Integer> resultList = Underscore.initial(asList(5, 4, 3, 2, 1), 2);
        assertEquals("[5, 4, 3]", resultList.toString());
        final Integer[] resultArray = Underscore.initial(new Integer[] {5, 4, 3, 2, 1});
        assertEquals("[5, 4, 3, 2]", asList(resultArray).toString());
        final Integer[] resultListArray = Underscore.initial(new Integer[] {5, 4, 3, 2, 1}, 2);
        assertEquals("[5, 4, 3]", asList(resultListArray).toString());
    }

/*
_.last([5, 4, 3, 2, 1]);
=> 1
*/
    @Test
    public void last() {
        final Integer result = Underscore.last(asList(5, 4, 3, 2, 1));
        assertEquals("1", result.toString());
        final Integer resultArray = Underscore.last(new Integer[] {5, 4, 3, 2, 1});
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
        final List<Integer> result = Underscore.tail(asList(5, 4, 3, 2, 1));
        assertEquals("[4, 3, 2, 1]", result.toString());
        final List<Integer> result2 = Underscore.tail(asList(5, 4, 3, 2, 1), 2);
        assertEquals("[3, 2, 1]", result2.toString());
        final Object[] resultArray = Underscore.tail(new Integer[] {5, 4, 3, 2, 1});
        assertEquals("[4, 3, 2, 1]", asList(resultArray).toString());
        final List<Integer> resultArrayObj = new Underscore<Integer>(asList(5, 4, 3, 2, 1)).tail();
        assertEquals("[4, 3, 2, 1]", resultArrayObj.toString());
        final Object[] resultArray2 = Underscore.tail(new Integer[] {5, 4, 3, 2, 1}, 2);
        assertEquals("[3, 2, 1]", asList(resultArray2).toString());
        final List<Integer> resultArray2Obj = new Underscore<Integer>(asList(5, 4, 3, 2, 1)).tail(2);
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
        final List<Integer> result = Underscore.drop(asList(5, 4, 3, 2, 1));
        assertEquals("[4, 3, 2, 1]", result.toString());
        final List<Integer> result2 = Underscore.drop(asList(5, 4, 3, 2, 1), 2);
        assertEquals("[3, 2, 1]", result2.toString());
        final Object[] resultArray = Underscore.drop(new Integer[] {5, 4, 3, 2, 1});
        assertEquals("[4, 3, 2, 1]", asList(resultArray).toString());
        final Object[] resultArray2 = Underscore.drop(new Integer[] {5, 4, 3, 2, 1}, 2);
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
        final List<Integer> result = Underscore.rest(asList(5, 4, 3, 2, 1));
        assertEquals("[4, 3, 2, 1]", result.toString());
        final List<Integer> result2 = Underscore.rest(asList(5, 4, 3, 2, 1), 2);
        assertEquals("[3, 2, 1]", result2.toString());
        final Object[] resultArray = Underscore.rest(new Integer[] {5, 4, 3, 2, 1});
        assertEquals("[4, 3, 2, 1]", asList(resultArray).toString());
        final Object[] resultArray2 = Underscore.rest(new Integer[] {5, 4, 3, 2, 1}, 2);
        assertEquals("[3, 2, 1]", asList(resultArray2).toString());
    }


/*
_.flatten([1, [2], [3, [[4]]]]);
=> [1, 2, 3, 4];
*/
    @Test
    public void flatten() {
        final List<Integer> result = Underscore.flatten(asList(1, asList(2, asList(3, asList(asList(4))))));
        assertEquals("[1, 2, 3, 4]", result.toString());
        final List<Integer> result2 = Underscore.flatten(asList(1, asList(2, asList(3, asList(asList(4))))), true);
        assertEquals("[1, 2, [3, [[4]]]]", result2.toString());
        final List<Integer> resultObj = new Underscore(asList(1, asList(2, asList(3, asList(asList(4)))))).flatten();
        assertEquals("[1, 2, 3, 4]", resultObj.toString());
        final List<Integer> resultObj2 = new Underscore(asList(1, asList(2, asList(3, asList(asList(4)))))).flatten(true);
        assertEquals("[1, 2, [3, [[4]]]]", resultObj2.toString());
    }

/*
_.compact([0, 1, false, 2, '', 3]);
=> [1, 2, 3]
*/
    @Test
    public void compact() {
        final List<?> result = Underscore.compact(asList(0, 1, false, 2, "", 3));
        assertEquals("[1, 2, 3]", result.toString());
        final List<?> result2 = Underscore.compact(asList(0, 1, false, 2, "", 3), 1);
        assertEquals("[0, false, 2, , 3]", result2.toString());
        final List<?> result3 = new Underscore(asList(0, 1, false, 2, "", 3)).compact();
        assertEquals("[1, 2, 3]", result3.toString());
        final List<?> result4 = new Underscore(asList(0, 1, false, 2, "", 3)).compact(1);
        assertEquals("[0, false, 2, , 3]", result4.toString());
        final Object[] resultArray = Underscore.compact(new Object[] {0, 1, false, 2, "", 3});
        assertEquals("[1, 2, 3]", asList(resultArray).toString());
        final Object[] resultArray2 = Underscore.compact(new Object[] {0, 1, false, 2, "", 3}, 1);
        assertEquals("[0, false, 2, , 3]", asList(resultArray2).toString());
    }

/*
_.without([1, 2, 1, 0, 3, 1, 4], 0, 1);
=> [2, 3, 4]
*/
    @Test
    public void without() {
        final List<Integer> result = Underscore.without(asList(1, 2, 1, 0, 3, 1, 4), 0, 1);
        assertEquals("[2, 3, 4]", result.toString());
        final List<Integer> result2 = Underscore.without(asList(1, 2, 1, 0, 3, 1, 4), 1);
        assertEquals("[2, 0, 3, 4]", result2.toString());
        final Object[] resultArray = Underscore.without(new Integer[] {1, 2, 1, 0, 3, 1, 4}, 0, 1);
        assertEquals("[2, 3, 4]", asList(resultArray).toString());
        final Object[] resultArray2 = Underscore.without(new Integer[] {1, 2, 1, 0, 3, 1, 4}, 1);
        assertEquals("[2, 0, 3, 4]", asList(resultArray2).toString());
    }

/*
var numbers = [10, 5, 100, 2, 1000];
_.max(numbers);
=> 1000
*/
    @Test
    public void max() {
        final Integer result = Underscore.max(asList(10, 5, 100, 2, 1000));
        assertEquals("1000", result.toString());
        final Integer resultComp = Underscore.max(asList(10, 5, 100, 2, 1000),
                new Function1<Integer, Integer>() {
            public Integer apply(Integer item) {
                return -item;
            }
        });
        assertEquals("2", resultComp.toString());
        class Person {
            public final String name;
            public final Integer age;
            public Person(final String name, final Integer age) {
                this.name = name;
                this.age = age;
            }
        };
        final Person resultPerson = Underscore.max(asList(new Person("moe", 40), new Person("larry", 50), new Person("curly", 60)),
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
        final Integer result = Underscore.min(asList(10, 5, 100, 2, 1000));
        assertEquals("2", result.toString());
        final Integer resultComp = Underscore.min(asList(10, 5, 100, 2, 1000),
                new Function1<Integer, Integer>() {
            public Integer apply(Integer item) {
                return -item;
            }
        });
        assertEquals("1000", resultComp.toString());
        class Person {
            public final String name;
            public final Integer age;
            public Person(final String name, final Integer age) {
                this.name = name;
                this.age = age;
            }
        };
        final Person resultPerson = Underscore.min(asList(new Person("moe", 40), new Person("larry", 50), new Person("curly", 60)),
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
        final List<Integer> result = Underscore.shuffle(asList(1, 2, 3, 4, 5, 6));
        assertEquals(6, result.size());
    }

/*
_.sample([1, 2, 3, 4, 5, 6]);
=> 4

_.sample([1, 2, 3, 4, 5, 6], 3);
=> [1, 6, 2]
*/
    @Test
    public void sample() {
        final Integer result = Underscore.sample(asList(1, 2, 3, 4, 5, 6));
        assertTrue(result >= 1 && result <= 6);
        final Set<Integer> resultList = Underscore.sample(asList(1, 2, 3, 4, 5, 6), 3);
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
        final List<?> resultEmpty =
        Underscore.pluck(asList(), "name");
        assertEquals("[]", resultEmpty.toString());
        final List<?> result =
        Underscore.pluck(asList(new Person("moe", 40), new Person("larry", 50), new Person("curly", 40)), "name");
        assertEquals("[moe, larry, curly]", result.toString());
        final Set<?> resultEmpty2 =
        Underscore.pluck(new LinkedHashSet(asList()), "name");
        assertEquals("[]", resultEmpty2.toString());
        final Set<?> resultSet =
        Underscore.pluck(new LinkedHashSet(asList(new Person("moe", 40), new Person("larry", 50), new Person("curly", 40))), "name");
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
        Underscore.pluck(asList(new Person("moe", 40), new Person("larry", 50), new Person("curly", 40)), "name2");
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
        Underscore.pluck(new LinkedHashSet(asList(new Person("moe", 40), new Person("larry", 50), new Person("curly", 40))), "name2");
    }
/*
_.sortBy([1, 2, 3, 4, 5, 6], function(num){ return Math.sin(num); });
=> [5, 4, 6, 3, 1, 2]
*/
    @Test
    public void sortBy() throws Exception {
        final List<Integer> result =
        Underscore.sortBy(asList(1, 2, 3, 4, 5, 6),
            new Function1<Integer, Integer>() {
            public Integer apply(Integer item) {
                return Double.valueOf(Math.sin(item) * 1000).intValue();
            }
        });
        assertEquals("[5, 4, 6, 3, 1, 2]", result.toString());
    }

/*
_.groupBy([1.3, 2.1, 2.4], function(num){ return Math.floor(num); });
=> {1: [1.3], 2: [2.1, 2.4]}
*/
    @Test
    public void groupBy() throws Exception {
        final Map<Double, List<Double>> result =
        Underscore.groupBy(asList(1.3, 2.1, 2.4),
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
        Underscore.indexBy(asList(new Person("moe", 40), new Person("larry", 50), new Person("curly", 60)), "age");
        assertEquals("{40=[moe, 40], 50=[larry, 50], 60=[curly, 60]}", result.toString());
        final Map<String, List<Person>> result2 =
        Underscore.indexBy(asList(new Person("moe", 40), new Person("larry", 50), new Person("curly", 60)), "age2");
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
        Underscore.countBy(asList(new Person("moe", 40), new Person("moe", 50), new Person("curly", 60)),
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
        final Object[] result = Underscore.<Integer>toArray(asList(1, 2, 3, 4));
        assertEquals("1", result[0].toString());
    }

/*
_.size({one: 1, two: 2, three: 3});
=> 3
*/
    @Test
    public void size() throws Exception {
        final int result = Underscore.size(asList(1, 2, 3, 4));
        assertEquals(4, result);
    }

/*
_.union([1, 2, 3], [101, 2, 1, 10], [2, 1]);
=> [1, 2, 3, 101, 10]
*/
    @Test
    public void union() throws Exception {
        final List<Integer> result = Underscore.union(asList(1, 2, 3), asList(101, 2, 1, 10), asList(2, 1));
        assertEquals("[1, 2, 3, 101, 10]", result.toString());
        final Object[] resultArray = Underscore.union(new Integer[] {1, 2, 3}, new Integer[] {101, 2, 1, 10});
        assertEquals("[[1, 2, 3, 101, 10]]", asList(result).toString());
    }

/*
_.intersection([1, 2, 3], [101, 2, 1, 10], [2, 1]);
=> [1, 2]
*/
    @Test
    public void intersection() throws Exception {
        final List<Integer> result = Underscore.intersection(asList(1, 2, 3), asList(101, 2, 1, 10), asList(2, 1));
        assertEquals("[1, 2]", result.toString());
        final Object[] resultArray = Underscore.intersection(new Integer[] {1, 2, 3}, new Integer[] {101, 2, 1, 10});
        assertEquals("[1, 2]", asList(resultArray).toString());
    }

/*
_.difference([1, 2, 3, 4, 5], [5, 2, 10]);
=> [1, 3, 4]
*/
    @Test
    public void difference() throws Exception {
        final List<Integer> result = Underscore.difference(asList(1, 2, 3, 4, 5), asList(5, 2, 10));
        assertEquals("[1, 3, 4]", result.toString());
        final Object[] resultArray = Underscore.difference(new Integer[] {1, 2, 3, 4, 5}, new Integer[] {5, 2, 10});
        assertEquals("[1, 3, 4]", asList(resultArray).toString());
    }

/*
_.uniq([1, 2, 1, 3, 1, 4]);
=> [1, 2, 3, 4]
*/
    @Test
    public void uniq() throws Exception {
        final List<Integer> result = Underscore.uniq(asList(1, 2, 1, 3, 1, 4));
        assertEquals("[1, 2, 3, 4]", result.toString());
        final Object[] resultArray = Underscore.uniq(new Integer[] {1, 2, 1, 3, 1, 4});
        assertEquals("[1, 2, 3, 4]", asList(resultArray).toString());
    }

/*
_.zip(['moe', 'larry', 'curly'], [30, 40, 50], [true, false, false]);
=> [["moe", 30, true], ["larry", 40, false], ["curly", 50, false]]
*/
    @Test
    public void zip() throws Exception {
        final List<List<String>> result = Underscore.zip(
            asList("moe", "larry", "curly"), asList("30", "40", "50"), asList("true", "false", "false"));
        assertEquals("[[moe, 30, true], [larry, 40, false], [curly, 50, false]]", result.toString());
    }

/*
_.object(['moe', 'larry', 'curly'], [30, 40, 50]);
=> {moe: 30, larry: 40, curly: 50}
*/
    @Test
    public void object() throws Exception {
        final List<Tuple<String, String>> result = Underscore.object(
            asList("moe", "larry", "curly"), asList("30", "40", "50"));
        assertEquals("[(moe, 30), (larry, 40), (curly, 50)]", result.toString());
    }

/*
_.indexOf([1, 2, 3], 2);
=> 1
*/
    @Test
    public void indexOf() throws Exception {
        final Integer result = Underscore.indexOf(asList(1, 2, 3), 2);
        assertEquals(1, result);
        final Integer resultArray = Underscore.indexOf(new Integer[] {1, 2, 3}, 2);
        assertEquals(1, resultArray);
    }

/*
_.lastIndexOf([1, 2, 3, 1, 2, 3], 2);
=> 4
*/
    @Test
    public void lastIndexOf() throws Exception {
        final Integer result = Underscore.lastIndexOf(asList(1, 2, 3, 1, 2, 3), 2);
        assertEquals(4, result);
        final Integer resultArray = Underscore.lastIndexOf(new Integer[] {1, 2, 3, 1, 2, 3}, 2);
        assertEquals(4, resultArray);
    }

/*
_.sortedIndex([10, 20, 30, 40, 50], 35);
=> 3
*/
    @Test
    public void sortedIndex() throws Exception {
        final Integer result = Underscore.sortedIndex(asList(10, 20, 30, 40, 50), 35);
        assertEquals(3, result);
        final Integer result2 = Underscore.sortedIndex(new Integer[] {10, 20, 30, 40, 50}, 35);
        assertEquals(3, result2);
        final Integer result3 = Underscore.sortedIndex(asList(10, 20, 30, 40, 50), 60);
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
        Underscore.<Person>sortedIndex(asList(new Person("moe", 40), new Person("moe", 50), new Person("curly", 60)), new Person("moe", 50), "age");
        assertEquals(1, result);
        final int result2 =
        Underscore.<Person>sortedIndex(asList(new Person("moe", 40), new Person("moe", 50), new Person("curly", 60)), new Person("moe", 70), "age");
        assertEquals(-1, result2);
        final int resultArray =
        Underscore.<Person>sortedIndex(new Person[] {new Person("moe", 40), new Person("moe", 50), new Person("curly", 60)}, new Person("moe", 50), "age");
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
        final int[] result = Underscore.range(10);
        assertArrayEquals(new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, result);
        final int[] result2 = Underscore.range(1, 11);
        assertArrayEquals(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, result2);
        final int[] result3 = Underscore.range(0, 30, 5);
        assertArrayEquals(new int[] {0, 5, 10, 15, 20, 25}, result3);
        final int[] result4 = Underscore.range(0, -10, -1);
        assertArrayEquals(new int[] {0, -1, -2, -3, -4, -5, -6, -7, -8, -9}, result4);
        final int[] result5 = Underscore.range(0);
        assertArrayEquals(new int[] {}, result5);
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
        final String youngest = Underscore.chain(stooges)
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
            .first().value().toString();
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
        final String result = Underscore.chain(lyrics)
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
            .value();
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
        final String result = Underscore.chain(lyrics)
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
            .value();
        assertEquals("{day=2, all=4, works=1, he=1, and=4, night=2, sleeps=1,"
                + " He=1, okay=2, he's=1, lumberjack=2, a=2, He's=1, work=1, I=2, sleep=1, I'm=2}", result);
    }

/*
var compiled = _.template("hello: <%= name %>");
compiled({name: 'moe'});
=> "hello: moe"
*/
    @Test
    public void template() throws Exception {
        Template<Set<Map.Entry<String,Object>>> compiled = Underscore.template("hello: <%= name %>");
        assertEquals("hello: moe", compiled.apply(new LinkedHashMap<String, Object>() {{ put("name", "moe"); }}.entrySet()));
    }

    @Test
    public void template2() throws Exception {
        Template<Set<Map.Entry<String,Object>>> compiled = Underscore.template("hello: <%= name %>, hello2: <%= name %>");
        assertEquals("hello: moe, hello2: moe", compiled.apply(new LinkedHashMap<String, Object>() {{ put("name", "moe"); }}.entrySet()));
    }

    @Test
    public void template3() throws Exception {
        Template<Set<Map.Entry<String,Object>>> compiled = Underscore.template("hello: <%= name %>, hello2: <%= name2 %>");
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
        Template<Set<Map.Entry<String,Object>>> compiled = Underscore.template(list);
        assertEquals(" <li>moe</li>  <li>curly</li>  <li>larry</li> ",
            compiled.apply(new LinkedHashMap<String, Object>() {{ put("people", asList("moe", "curly", "larry")); }}.entrySet()));
    }

/*
var template = _.template("<b><%- value %></b>");
template({value: '<script>'});
=> "<b>&lt;script&gt;</b>"
*/
    @Test
    public void templateValue() throws Exception {
        Template<Set<Map.Entry<String,Object>>> template = Underscore.template("<b><%- value %></b>");
        assertEquals("<b>&lt;script&gt;</b>",
            template.apply(new LinkedHashMap<String, Object>() {{ put("value", "<script>"); }}.entrySet()));
    }

    @Test
    public void templateValue2() throws Exception {
        Template<Set<Map.Entry<String,Object>>> template = Underscore.template("hello: <%= name %>, <b><%- value %></b>");
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
        Template<Set<Map.Entry<String,Object>>> compiled = Underscore.template("<% print('Hello ' + epithet); %>");
        assertEquals("Hello stooge",
            compiled.apply(new LinkedHashMap<String, Object>() {{ put("epithet", "stooge"); }}.entrySet()));
    }

/*
_.escape('Curly, Larry & Moe');
=> "Curly, Larry &amp; Moe"
*/
    @Test
    public void escape() throws Exception {
        assertEquals("Curly, Larry &amp; Moe", Underscore.escape("Curly, Larry & Moe"));
    }

/*
_.unescape('Curly, Larry &amp; Moe');
=> "Curly, Larry & Moe"
*/
    @Test
    public void unescape() throws Exception {
        assertEquals("Curly, Larry & Moe", Underscore.unescape("Curly, Larry &amp; Moe"));
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

        assertEquals("crumpets", Underscore.result(object.entrySet(), new Predicate<Map.Entry<String, Object>>() {
            public Boolean apply(Map.Entry<String, Object> item) {
                return item.getKey().equals("cheese");
            }
        }));
        assertEquals("nonsense", Underscore.result(object.entrySet(), new Predicate<Map.Entry<String, Object>>() {
            public Boolean apply(Map.Entry<String, Object> item) {
                return item.getKey().equals("stuff");
            }
        }));
        assertEquals("result1", Underscore.result(asList("result1", "result2"), new Predicate<String>() {
            public Boolean apply(String item) {
                return item.equals("result1");
            }
        }));
        assertEquals(null, Underscore.result(asList("result1", "result2"), new Predicate<String>() {
            public Boolean apply(String item) {
                return item.equals("result3");
            }
        }));
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
        Function<Void> debouncedIncr = Underscore.debounce(incr, 50);
        debouncedIncr.apply();
        debouncedIncr.apply();
        Underscore.delay(debouncedIncr, 16);
        Underscore.delay(new Function<Void>() {
            public Void apply() {
                assertEquals("incr was debounced", 1, counter[0]);
                return null;
            }
        }, 32);
        Thread.sleep(120);
    }

    @Test
    public void main() throws Exception {
        Underscore.main(new String[] {});
    }

/*
['some', 'words', 'example'].sort();
=> ['example', 'some', 'words']
*/
    @Test
    public void sort() throws Exception {
        assertEquals("[example, some, words]", Underscore.sort(asList("some", "words", "example")).toString());
        assertEquals("[example, some, words]", asList(Underscore.sort(new String[] {"some", "words", "example"})).toString());
    }

/*
['some', 'words', 'example'].join('-');
=> 'some-words-example'
*/
    @Test
    public void join() throws Exception {
        assertEquals("some-words-example", Underscore.join(asList("some", "words", "example"), "-"));
        assertEquals("some-words-example", Underscore.join(new String[] {"some", "words", "example"}, "-"));
    }

    @Test
    public void compareStrings() throws Exception {
        assertEquals(Underscore.sort("CAT".split("")), Underscore.sort("CTA".split("")));
    }

/*
_.concat([1, 2], [3, 4]);
=> [1, 2, 3, 4]
*/
    @Test
    public void concat() throws Exception {
        assertEquals(asList(1, 2, 3, 4), asList(Underscore.concat(new Integer[] {1, 2}, new Integer[] {3, 4})));
        assertEquals(asList(1, 2, 3, 4), Underscore.concat(asList(1, 2), asList(3, 4)));
        assertEquals("[1, 2, 3, 4]", Underscore.chain(asList(1, 2)).concat(asList(3, 4)).value());
        assertEquals(asList(1, 2, 3, 4), asList(Underscore.concat(new Integer[] {1, 2}, new Integer[] {3}, new Integer[] {4})));
        assertEquals(asList(1, 2, 3, 4), Underscore.concat(asList(1, 2), asList(3), asList(4)));
    }
}
