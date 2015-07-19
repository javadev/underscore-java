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
import org.junit.Test;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Underscore library unit test.
 *
 * @author Valentyn Kolesnikov
 */
public class CollectionsTest {

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
        final List<Integer> result2 = new ArrayList<Integer>();
        new $(asList(1, 2, 3)).each(new Block<Integer>() {
            public void apply(Integer item) {
                result2.add(item);
            }
        });
        assertEquals("[1, 2, 3]", result2.toString());
    }

/*
_.eachRight([1, 2, 3], alert);
=> alerts each number in turn from right to left...
*/
    @Test
    public void eachRight() {
        final List<Integer> result = new ArrayList<Integer>();
        $.eachRight(asList(1, 2, 3), new Block<Integer>() {
            public void apply(Integer item) {
                result.add(item);
            }
        });
        assertEquals("[3, 2, 1]", result.toString());
        final List<Integer> result2 = new ArrayList<Integer>();
        new $(asList(1, 2, 3)).eachRight(new Block<Integer>() {
            public void apply(Integer item) {
                result2.add(item);
            }
        });
        assertEquals("[3, 2, 1]", result2.toString());
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
_.forEach([1, 2, 3], alert);
=> alerts each number in turn from right to left...
*/
    @Test
    public void forEachRight() {
        final List<Integer> result = new ArrayList<Integer>();
        $.forEachRight(asList(1, 2, 3), new Block<Integer>() {
            public void apply(Integer item) {
                result.add(item);
            }
        });
        assertEquals("[3, 2, 1]", result.toString());
        final List<Integer> result2 = new ArrayList<Integer>();
        new $(asList(1, 2, 3)).forEachRight(new Block<Integer>() {
            public void apply(Integer item) {
                result2.add(item);
            }
        });
        assertEquals("[3, 2, 1]", result2.toString());
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
        $.<Map.Entry<String, Integer>>each((new LinkedHashMap<String, Integer>() { { put("one", 1); put("two", 2); put("three", 3); } }).entrySet(),
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
_.map({one: 1, two: 2, three: 3}, function(num, key){ return num * 3; });
=> [3, 6, 9]
*/
    @Test
    public void mapMap() {
        final Set<Integer> result =
        $.map((new LinkedHashMap<Integer, String>() { { put(1, "one"); put(2, "two"); put(3, "three"); } }).entrySet(),
            new Function1<Map.Entry<Integer, String>, Integer>() {
            public Integer apply(Map.Entry<Integer, String> item) {
                return item.getKey() * 3;
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
_.invoke([" foo ", "  bar"], "trim"); // ["foo", "bar"]
*/
    @Test
    public void invoke() {
        assertEquals($.invoke(asList(" foo ", "  bar"), "trim"), asList("foo", "bar"));
        assertEquals($.invoke(asList("foo", "bar"), "concat", Arrays.<Object>asList("1")), asList("foo1", "bar1"));
        assertEquals($.invoke(asList($.chain(asList(5, 1, 7)), $.chain(asList(3, 2, 1))), "sort").toString(), asList("[1, 5, 7]", "[1, 2, 3]").toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void invokeError() {
        $.invoke(asList("foo", 123), "concat", Arrays.<Object>asList("1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void invokeError2() {
        $.invoke(asList(" foo ", "  bar"), "trim2");
    }

/*
var stooges = [{name: 'moe', age: 40}, {name: 'larry', age: 50}, {name: 'curly', age: 60}];
_.pluck(stooges, 'name');
=> ["moe", "larry", "curly"]
*/
    @Test
    public void pluck() {
        class Person {
            public final String name;
            public final Integer age;
            public Person(final String name, final Integer age) {
                this.name = name;
                this.age = age;
            }
        }
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
    public void pluck2() {
        class Person {
            public final String name;
            public final Integer age;
            public Person(final String name, final Integer age) {
                this.name = name;
                this.age = age;
            }
        }
        $.pluck(asList(new Person("moe", 40), new Person("larry", 50), new Person("curly", 40)), "name2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void pluck3() {
        class Person {
            public final String name;
            public final Integer age;
            public Person(final String name, final Integer age) {
                this.name = name;
                this.age = age;
            }
        }
        $.pluck(new LinkedHashSet(asList(new Person("moe", 40), new Person("larry", 50), new Person("curly", 40))), "name2");
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
        }
        List<Book> listOfPlays =
            new ArrayList<Book>() { {
              add(new Book("Cymbeline2", "Shakespeare", 1614));
              add(new Book("Cymbeline", "Shakespeare", 1611));
              add(new Book("The Tempest", "Shakespeare", 1611));
            } };
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
        }
        List<Book> listOfPlays =
            new ArrayList<Book>() { {
              add(new Book("Cymbeline2", "Shakespeare", 1614));
              add(new Book("Cymbeline", "Shakespeare", 1611));
              add(new Book("The Tempest", "Shakespeare", 1611));
            } };
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
        }
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
        }
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
_.sortBy([1, 2, 3, 4, 5, 6], function(num){ return Math.sin(num); });
=> [5, 4, 6, 3, 1, 2]
*/
    @Test
    public void sortBy() {
        final List<Integer> result =
        $.sortBy(asList(1, 2, 3, 4, 5, 6),
            new Function1<Integer, Integer>() {
            public Integer apply(Integer item) {
                return Double.valueOf(Math.sin(item) * 1000).intValue();
            }
        });
        assertEquals("[5, 4, 6, 3, 1, 2]", result.toString());
        final List<Integer> resultObj =
        new $(asList(1, 2, 3, 4, 5, 6)).sortBy(
            new Function1<Integer, Integer>() {
            public Integer apply(Integer item) {
                return Double.valueOf(Math.sin(item) * 1000).intValue();
            }
        });
        assertEquals("[5, 4, 6, 3, 1, 2]", resultObj.toString());
    }

/*
var stooges = [{name: 'moe', age: 40}, {name: 'larry', age: 50}, {name: 'curly', age: 60}];
_.sortBy(stooges, 'name');
=> [{name: 'curly', age: 60}, {name: 'larry', age: 50}, {name: 'moe', age: 40}];
*/
    @Test
    public void sortByMap() {
        final List<Map<String, Comparable>> result = $.sortBy(asList(
            (Map<String, Comparable>) new LinkedHashMap<String, Comparable>() { { put("name", "moe"); put("age", 40); } },
            (Map<String, Comparable>) new LinkedHashMap<String, Comparable>() { { put("name", "larry"); put("age", 50); } },
            (Map<String, Comparable>) new LinkedHashMap<String, Comparable>() { { put("name", "curly"); put("age", 60); } }
        ), "name");
        assertEquals("[{name=curly, age=60}, {name=larry, age=50}, {name=moe, age=40}]", result.toString());
        final List<Map<String, Comparable>> resultChain = $.chain(asList(
            (Map<String, Comparable>) new LinkedHashMap<String, Comparable>() { { put("name", "moe"); put("age", 40); } },
            (Map<String, Comparable>) new LinkedHashMap<String, Comparable>() { { put("name", "larry"); put("age", 50); } },
            (Map<String, Comparable>) new LinkedHashMap<String, Comparable>() { { put("name", "curly"); put("age", 60); } }
        )).sortBy("name").value();
        assertEquals("[{name=curly, age=60}, {name=larry, age=50}, {name=moe, age=40}]", resultChain.toString());
    }

/*
_.groupBy([1.3, 2.1, 2.4], function(num){ return Math.floor(num); });
=> {1: [1.3], 2: [2.1, 2.4]}
*/
    @Test
    public void groupBy() {
        final Map<Double, List<Double>> result =
        $.groupBy(asList(1.3, 2.1, 2.4),
            new Function1<Double, Double>() {
            public Double apply(Double num) {
                return Math.floor(num);
            }
        });
        assertEquals("{1.0=[1.3], 2.0=[2.1, 2.4]}", result.toString());
        final Map<Double, List<Double>> resultObj =
        new $(asList(1.3, 2.1, 2.4)).groupBy(
            new Function1<Double, Double>() {
            public Double apply(Double num) {
                return Math.floor(num);
            }
        });
        assertEquals("{1.0=[1.3], 2.0=[2.1, 2.4]}", resultObj.toString());
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
    public void indexBy() {
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
        }
        final Map<String, List<Person>> result =
        $.indexBy(asList(new Person("moe", 40), new Person("larry", 50), new Person("curly", 60)), "age");
        assertEquals("{40=[moe, 40], 50=[larry, 50], 60=[curly, 60]}", result.toString());
        final Map<String, List<Person>> resultObj =
        new $(asList(new Person("moe", 40), new Person("larry", 50), new Person("curly", 60))).indexBy("age");
        assertEquals("{40=[moe, 40], 50=[larry, 50], 60=[curly, 60]}", resultObj.toString());
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
    public void countBy() {
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
        }
        final Map<String, Integer> result =
        $.countBy(asList(new Person("moe", 40), new Person("moe", 50), new Person("curly", 60)),
            new Function1<Person, String>() {
            public String apply(Person person) {
                return person.name;
            }
        });
        assertEquals("{moe=2, curly=1}", result.toString());
        final Map<String, Integer> resultObj =
        new $(asList(new Person("moe", 40), new Person("moe", 50), new Person("curly", 60))).countBy(
            new Function1<Person, String>() {
            public String apply(Person person) {
                return person.name;
            }
        });
        assertEquals("{moe=2, curly=1}", resultObj.toString());
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
(function(){ return _.toArray(arguments).slice(1); })(1, 2, 3, 4);
=> [2, 3, 4]
*/
    @Test
    public void toArray() {
        final Object[] result = $.<Integer>toArray(asList(1, 2, 3, 4));
        assertEquals("1", result[0].toString());
        final Object[] resultObj = new $(asList(1, 2, 3, 4)).toArray();
        assertEquals("1", resultObj[0].toString());
    }

    @Test
    public void toMap() {
        assertEquals("{name1=one, name2=two}", $.toMap((new LinkedHashMap<String, String>() { {
            put("name1", "one");
            put("name2", "two");
        } }).entrySet()).toString());
        assertEquals("{name1=one, name2=two}", new $((new LinkedHashMap<String, String>() { {
            put("name1", "one");
            put("name2", "two");
        } }).entrySet()).toMap().toString());
    }

/*
_.size({one: 1, two: 2, three: 3});
=> 3
*/
    @Test
    public void size() {
        final int result = $.size(asList(1, 2, 3, 4));
        assertEquals(4, result);
    }

/*
_.partition([0, 1, 2, 3, 4, 5], isOdd);
=> [[1, 3, 5], [0, 2, 4]]
*/
    @Test
    public void partition() {
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
}
