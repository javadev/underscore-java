/*
 * The MIT License (MIT)
 *
 * Copyright 2015-2018 Valentyn Kolesnikov
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
    @SuppressWarnings("unchecked")
    public void each() {
        final List<Integer> result = new ArrayList<Integer>();
        U.<Integer>each(asList(1, 2, 3), new Consumer<Integer>() {
            public void accept(Integer item) {
                result.add(item);
            }
        });
        assertEquals("[1, 2, 3]", result.toString());
        final List<Integer> result2 = new ArrayList<Integer>();
        new U(asList(1, 2, 3)).each(new Consumer<Integer>() {
            public void accept(Integer item) {
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
    @SuppressWarnings("unchecked")
    public void eachRight() {
        final List<Integer> result = new ArrayList<Integer>();
        U.eachRight(asList(1, 2, 3), new Consumer<Integer>() {
            public void accept(Integer item) {
                result.add(item);
            }
        });
        assertEquals("[3, 2, 1]", result.toString());
        final List<Integer> result2 = new ArrayList<Integer>();
        new U(asList(1, 2, 3)).eachRight(new Consumer<Integer>() {
            public void accept(Integer item) {
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
        U.forEach(asList(1, 2, 3), new Consumer<Integer>() {
            public void accept(Integer item) {
                result.add(item);
            }
        });
        assertEquals("[1, 2, 3]", result.toString());
        final List<Map.Entry<String, Integer>> resultChain = new ArrayList<Map.Entry<String, Integer>>();
        U.chain((new LinkedHashMap<String, Integer>() { { put("a", 1); put("b", 2); put("c", 3); } }).entrySet())
            .forEach(new Consumer<Map.Entry<String, Integer>>() {
                public void accept(final Map.Entry<String, Integer> item) {
                    resultChain.add(item);
                }
            });
        assertEquals("[a=1, b=2, c=3]", resultChain.toString());
    }

/*
_.forEachIndexed([1, 2, 3], alert);
=> alerts each number in turn...
*/
    @Test
    @SuppressWarnings("unchecked")
    public void forEachIndexed() {
        final List<Integer> result = new ArrayList<Integer>();
        U.forEachIndexed(asList(1, 2, 3), new BiConsumer<Integer, Integer>() {
            public void accept(Integer index, Integer item) {
                result.add(item);
            }
        });
        assertEquals("[1, 2, 3]", result.toString());
        final List<Integer> resultObj = new ArrayList<Integer>();
        new U(asList(1, 2, 3)).forEachIndexed(new BiConsumer<Integer, Integer>() {
            public void accept(Integer index, Integer item) {
                resultObj.add(item);
            }
        });
        assertEquals("[1, 2, 3]", resultObj.toString());
    }

/*
_.forEach([1, 2, 3], alert);
=> alerts each number in turn from right to left...
*/
    @Test
    @SuppressWarnings("unchecked")
    public void forEachRight() {
        final List<Integer> result = new ArrayList<Integer>();
        U.forEachRight(asList(1, 2, 3), new Consumer<Integer>() {
            public void accept(Integer item) {
                result.add(item);
            }
        });
        assertEquals("[3, 2, 1]", result.toString());
        final List<Integer> result2 = new ArrayList<Integer>();
        new U(asList(1, 2, 3)).forEachRight(new Consumer<Integer>() {
            public void accept(Integer item) {
                result2.add(item);
            }
        });
        assertEquals("[3, 2, 1]", result2.toString());
        final List<Map.Entry<String, Integer>> resultChain = new ArrayList<Map.Entry<String, Integer>>();
        U.chain((new LinkedHashMap<String, Integer>() { { put("a", 1); put("b", 2); put("c", 3); } }).entrySet())
            .forEachRight(new Consumer<Map.Entry<String, Integer>>() {
                public void accept(final Map.Entry<String, Integer> item) {
                    resultChain.add(item);
                }
            });
        assertEquals("[c=3, b=2, a=1]", resultChain.toString());
    }

/*
_([1, 2, 3]).forEach(alert);
=> alerts each number in turn...
*/
    @Test
    @SuppressWarnings("unchecked")
    public void forEachObj() {
        final List<Integer> result = new ArrayList<Integer>();
        new U(asList(1, 2, 3)).forEach(new Consumer<Integer>() {
            public void accept(Integer item) {
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
        U.<Map.Entry<String, Integer>>each((new LinkedHashMap<String, Integer>() { {
            put("one", 1); put("two", 2); put("three", 3); } }).entrySet(),
            new Consumer<Map.Entry<String, Integer>>() {
            public void accept(Map.Entry<String, Integer> item) {
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
        List<Integer> result = U.map(asList(1, 2, 3), new Function<Integer, Integer>() {
            public Integer apply(Integer item) {
                return item * 3;
            }
        });
        assertEquals("[3, 6, 9]", result.toString());
        List<Integer> resultObject = new U<Integer>(asList(1, 2, 3)).map(new Function<Integer, Integer>() {
            public Integer apply(Integer item) {
                return item * 3;
            }
        });
        assertEquals("[3, 6, 9]", resultObject.toString());
    }

/*
_.map(_.range(3), function(num){ return (num + 1) * 3; });
=> [3, 6, 9]
*/
    @Test
    public void mapArray() {
        List<Integer> result = U.map(U.range(3), new Function<Integer, Integer>() {
            public Integer apply(Integer item) {
                return (item + 1) * 3;
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
        U.map((new LinkedHashMap<Integer, String>() { { put(1, "one"); put(2, "two"); put(3, "three"); } }).entrySet(),
            new Function<Map.Entry<Integer, String>, Integer>() {
            public Integer apply(Map.Entry<Integer, String> item) {
                return item.getKey() * 3;
            }
        });
        assertEquals("[3, 6, 9]", result.toString());
    }

/*
_.mapIndexed([1, 2, 3], function(num){ return num * 3; });
=> [3, 6, 9]
*/
    @Test
    public void mapIndexed() {
        List<Integer> result = U.mapIndexed(asList(1, 2, 3), new BiFunction<Integer, Integer, Integer>() {
            public Integer apply(Integer index, Integer item) {
                return item * 3;
            }
        });
        assertEquals("[3, 6, 9]", result.toString());
        List<Integer> resultObject = new U<Integer>(asList(1, 2, 3)).mapIndexed(
            new BiFunction<Integer, Integer, Integer>() {
            public Integer apply(Integer index, Integer item) {
                return item * 3;
            }
        });
        assertEquals("[3, 6, 9]", resultObject.toString());
        List<Integer> resultChain = U.chain(asList(1, 2, 3)).mapIndexed(new BiFunction<Integer, Integer, Integer>() {
            public Integer apply(Integer index, Integer item) {
                return item * 3;
            }
        }).value();
        assertEquals("[3, 6, 9]", resultChain.toString());
    }

/*
_.collect([1, 2, 3], function(num){ return num * 3; });
=> [3, 6, 9]
*/
    @Test
    @SuppressWarnings("unchecked")
    public void collect() {
        List<Integer> result = U.collect(asList(1, 2, 3), new Function<Integer, Integer>() {
            public Integer apply(Integer item) {
                return item * 3;
            }
        });
        assertEquals("[3, 6, 9]", result.toString());
        Set<Integer> resultSet = U.collect(new LinkedHashSet(asList(1, 2, 3)), new Function<Integer, Integer>() {
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
        U.reduce(asList(1, 2, 3),
            new BiFunction<Integer, Integer, Integer>() {
            public Integer apply(Integer item1, Integer item2) {
                return item1 + item2;
            }
        },
        0);
        assertEquals("6", result.toString());
    }

/*
var sum = _.reduce([1, 2, 3], function(memo, num){ return memo + num; });
=> 6
*/
    @Test
    public void reduceWithoutInit() {
        final Integer result =
        U.reduce(asList(1, 2, 3),
            new BinaryOperator<Integer>() {
            public Integer apply(Integer item1, Integer item2) {
                return item1 + item2;
            }
        }).get();
        assertEquals("6", result.toString());
        final Integer resultChain =
        U.chain(asList(1, 2, 3))
            .reduce(
            new BinaryOperator<Integer>() {
            public Integer apply(Integer item1, Integer item2) {
                return item1 + item2;
            }
        }).item().get();
        assertEquals("6", resultChain.toString());
        U.reduce(new ArrayList<Integer>(),
            new BinaryOperator<Integer>() {
            public Integer apply(Integer item1, Integer item2) {
                return item1 + item2;
            }
        });
    }

/*
var sum = _.reduceRight([1, 2, 3], function(memo, num){ return memo + num; });
=> 6
*/
    @Test
    public void reduceRightWithoutInit() {
        final Integer result =
        U.reduceRight(asList(1, 2, 3),
            new BinaryOperator<Integer>() {
            public Integer apply(Integer item1, Integer item2) {
                return item1 + item2;
            }
        }).get();
        assertEquals("6", result.toString());
        final Integer resultChain =
        U.chain(asList(1, 2, 3))
            .reduceRight(
            new BinaryOperator<Integer>() {
            public Integer apply(Integer item1, Integer item2) {
                return item1 + item2;
            }
        }).item().get();
        assertEquals("6", resultChain.toString());
    }

/*
var sum = _.reduce([1, 2, 3], function(memo, num){ return memo + num; }, 0);
=> 6
*/
    @Test
    public void reduceIntArray() {
        final Integer result =
        U.reduce(new int[]{1, 2, 3},
            new BiFunction<Integer, Integer, Integer>() {
            public Integer apply(Integer item1, Integer item2) {
                return item1 + item2;
            }
        },
        0);
        assertEquals("6", result.toString());
    }

/*
var sum = _.reduce([1, 2, 3], function(memo, num){ return memo + num; }, 0);
=> 6
*/
    @Test
    public void reduceArray() {
        final Integer result =
        U.reduce(new Integer[]{1, 2, 3},
            new BiFunction<Integer, Integer, Integer>() {
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
    @SuppressWarnings("unchecked")
    public void inject() {
        final List<Integer> result =
        U.inject(asList(asList(0, 1), asList(2, 3), asList(4, 5)),
            new BiFunction<List<Integer>, List<Integer>, List<Integer>>() {
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
    @SuppressWarnings("unchecked")
    public void foldl() {
        final List<Integer> result =
        U.foldl(asList(asList(0, 1), asList(2, 3), asList(4, 5)),
            new BiFunction<List<Integer>, List<Integer>, List<Integer>>() {
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
    @SuppressWarnings("unchecked")
    public void reduceRight() {
        final List<Integer> result =
        U.reduceRight(asList(asList(0, 1), asList(2, 3), asList(4, 5)),
            new BiFunction<List<Integer>, List<Integer>, List<Integer>>() {
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
var sum = _.reduceRight([1, 2, 3], function(memo, num){ return memo + num; }, 0);
=> 6
*/
    @Test
    public void reduceRightIntArray() {
        final Integer result =
        U.reduceRight(new int[]{1, 2, 3},
            new BiFunction<Integer, Integer, Integer>() {
            public Integer apply(Integer item1, Integer item2) {
                return item1 + item2;
            }
        },
        0);
        assertEquals("6", result.toString());
    }

/*
var sum = _.reduceRight([1, 2, 3], function(memo, num){ return memo + num; }, 0);
=> 6
*/
    @Test
    public void reduceRightArray() {
        final Integer result =
        U.reduceRight(new Integer[]{1, 2, 3},
            new BiFunction<Integer, Integer, Integer>() {
            public Integer apply(Integer item1, Integer item2) {
                return item1 + item2;
            }
        },
        0);
        assertEquals("6", result.toString());
    }

/*
var list = [[0, 1], [2, 3], [4, 5]];
var flat = _.foldr(list, function(a, b) { return a.concat(b); }, []);
=> [4, 5, 2, 3, 0, 1]
*/
    @Test
    @SuppressWarnings("unchecked")
    public void foldr() {
        final List<Integer> result =
        U.foldr(asList(asList(0, 1), asList(2, 3), asList(4, 5)),
            new BiFunction<List<Integer>, List<Integer>, List<Integer>>() {
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
    @SuppressWarnings("unchecked")
    public void find() {
        final Optional<Integer> result = U.find(asList(1, 2, 3, 4, 5, 6),
            new Predicate<Integer>() {
            public boolean test(Integer item) {
                return item % 2 == 0;
            }
        });
        assertEquals("Optional.of(2)", result.toString());
        final Optional<Integer> resultChain = U.chain(asList(1, 2, 3, 4, 5, 6)).find(
            new Predicate<Integer>() {
            public boolean test(Integer item) {
                return item % 2 == 0;
            }
        }).item();
        assertEquals("Optional.of(2)", resultChain.toString());
        final Optional<Integer> resultChain2 = U.chain(asList(1, 2, 3, 4, 5, 6)).find(
            new Predicate<Integer>() {
            public boolean test(Integer item) {
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
    @SuppressWarnings("unchecked")
    public void findLast() {
        final Optional<Integer> result = U.findLast(asList(1, 2, 3, 4, 5, 6),
            new Predicate<Integer>() {
            public boolean test(Integer item) {
                return item % 2 == 0;
            }
        });
        assertEquals("Optional.of(6)", result.toString());
        final Optional<Integer> result2 = U.findLast(asList(1, 2, 3, 4, 5, 6),
            new Predicate<Integer>() {
            public boolean test(Integer item) {
                return item > 6;
            }
        });
        assertEquals("Optional.absent()", result2.toString());
        final Optional<Integer> resultChain = U.chain(asList(1, 2, 3, 4, 5, 6)).findLast(
            new Predicate<Integer>() {
            public boolean test(Integer item) {
                return item % 2 == 0;
            }
        }).item();
        assertEquals("Optional.of(6)", resultChain.toString());
        final Optional<Integer> resultChain2 = U.chain(asList(1, 2, 3, 4, 5, 6)).findLast(
            new Predicate<Integer>() {
            public boolean test(Integer item) {
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
        final Optional<Integer> result = U.detect(asList(1, 2, 3, 4, 5, 6),
            new Predicate<Integer>() {
            public boolean test(Integer item) {
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
        final List<Integer> result = U.filter(asList(1, 2, 3, 4, 5, 6),
            new Predicate<Integer>() {
            public boolean test(Integer item) {
                return item % 2 == 0;
            }
        });
        assertEquals("[2, 4, 6]", result.toString());
        final List<Integer> resultObject = new U<Integer>(asList(1, 2, 3, 4, 5, 6))
            .filter(new Predicate<Integer>() {
            public boolean test(Integer item) {
                return item % 2 == 0;
            }
        });
        assertEquals("[2, 4, 6]", resultObject.toString());
    }

/*
var evens = _.filterFalse([1, 2, 3, 4, 5, 6], function(num){ return num % 2 == 0; });
=> [1, 3, 5]
*/
    @Test
    @SuppressWarnings("unchecked")
    public void filterFalse() {
        final List<Integer> result = U.filterFalse(asList(1, 2, 3, 4, 5, 6),
            new Predicate<Integer>() {
            public boolean test(Integer item) {
                return item % 2 == 0;
            }
        });
        assertEquals("[1, 3, 5]", result.toString());
        final List<Integer> resultObject = new U<Integer>(asList(1, 2, 3, 4, 5, 6))
            .filterFalse(new Predicate<Integer>() {
            public boolean test(Integer item) {
                return item % 2 == 0;
            }
        });
        assertEquals("[1, 3, 5]", resultObject.toString());
        final Set<Integer> resultSet = U.filterFalse(new LinkedHashSet(asList(1, 2, 3, 4, 5, 6)),
            new Predicate<Integer>() {
            public boolean test(Integer item) {
                return item % 2 == 0;
            }
        });
        assertEquals("[1, 3, 5]", resultSet.toString());
    }

/*
var evens = _.filterIndexed([1, 2, 3, 4, 5, 6], function(index, num){ return index !== 1 && num % 2 == 0; });
=> [4, 6]
*/
    @Test
    public void filterIndexed() {
        final List<Integer> result = U.filterIndexed(asList(1, 2, 3, 4, 5, 6),
            new PredicateIndexed<Integer>() {
            public boolean test(int index, Integer item) {
                return index != 1 && item % 2 == 0;
            }
        });
        assertEquals("[4, 6]", result.toString());
        final List<Integer> resultChain = U.chain(asList(1, 2, 3, 4, 5, 6))
            .filterIndexed(
            new PredicateIndexed<Integer>() {
            public boolean test(int index, Integer item) {
                return index != 1 && item % 2 == 0;
            }
        }).value();
        assertEquals("[4, 6]", resultChain.toString());
    }

/*
var evens = _.filter([1, 2, 3, 4, 5, 6], function(num){ return num % 2 == 0; });
=> [2, 4, 6]
*/
    @Test
    @SuppressWarnings("unchecked")
    public void select() {
        final List<Integer> result = U.select(asList(1, 2, 3, 4, 5, 6),
            new Predicate<Integer>() {
            public boolean test(Integer item) {
                return item % 2 == 0;
            }
        });
        assertEquals("[2, 4, 6]", result.toString());
        final Set<Integer> resultSet = U.select(new LinkedHashSet(asList(1, 2, 3, 4, 5, 6)),
            new Predicate<Integer>() {
            public boolean test(Integer item) {
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
    @SuppressWarnings("unchecked")
    public void reject() {
        final List<Integer> result = U.reject(asList(1, 2, 3, 4, 5, 6),
            new Predicate<Integer>() {
            public boolean test(Integer item) {
                return item % 2 == 0;
            }
        });
        assertEquals("[1, 3, 5]", result.toString());
        final List<Integer> resultObject = new U<Integer>(asList(1, 2, 3, 4, 5, 6))
            .reject(new Predicate<Integer>() {
            public boolean test(Integer item) {
                return item % 2 == 0;
            }
        });
        assertEquals("[1, 3, 5]", resultObject.toString());
        final Set<Integer> resultSet = U.reject(new LinkedHashSet(asList(1, 2, 3, 4, 5, 6)),
            new Predicate<Integer>() {
            public boolean test(Integer item) {
                return item % 2 == 0;
            }
        });
        assertEquals("[1, 3, 5]", resultSet.toString());
    }

/*
var evens = _.rejectIndexed([1, 2, 3, 4, 5, 6], function(index, num){ return index !== 1 && num % 2 == 0; });
=> [1, 2, 3, 5]
*/
    @Test
    public void rejectIndexed() {
        final List<Integer> result = U.rejectIndexed(asList(1, 2, 3, 4, 5, 6),
            new PredicateIndexed<Integer>() {
            public boolean test(int index, Integer item) {
                return index != 1 && item % 2 == 0;
            }
        });
        assertEquals("[1, 2, 3, 5]", result.toString());
        final List<Integer> resultChain = U.chain(asList(1, 2, 3, 4, 5, 6))
            .rejectIndexed(
            new PredicateIndexed<Integer>() {
            public boolean test(int index, Integer item) {
                return index != 1 && item % 2 == 0;
            }
        }).value();
        assertEquals("[1, 2, 3, 5]", resultChain.toString());
    }

/*
_.every([1, 2, 3, 4], function(num) { return num % 2 === 0; }); // false
_.every([1, 2, 3, 4], function(num) { return num < 5; }); // true
*/
    @Test
    @SuppressWarnings("unchecked")
    public void every() {
        final boolean result1 = U.every(asList(1, 2, 3, 4),
            new Predicate<Integer>() {
            public boolean test(Integer item) {
                return item % 2 == 0;
            }
        });
        final boolean result1obj = new U(asList(1, 2, 3, 4))
            .every(
            new Predicate<Integer>() {
            public boolean test(Integer item) {
                return item % 2 == 0;
            }
        });
        final boolean result1chain = U.chain(asList(1, 2, 3, 4))
            .every(
            new Predicate<Integer>() {
            public boolean test(Integer item) {
                return item % 2 == 0;
            }
        }).item();
        final boolean result2 = U.every(asList(1, 2, 3, 4),
            new Predicate<Integer>() {
            public boolean test(Integer item) {
                return item < 5;
            }
        });
        final boolean result2obj = new U(asList(1, 2, 3, 4))
            .every(
            new Predicate<Integer>() {
            public boolean test(Integer item) {
                return item < 5;
            }
        });
        final boolean result2chain = U.chain(asList(1, 2, 3, 4))
            .every(
            new Predicate<Integer>() {
            public boolean test(Integer item) {
                return item < 5;
            }
        }).item();
        assertFalse(result1);
        assertFalse(result1obj);
        assertFalse(result1chain);
        assertTrue(result2);
        assertTrue(result2obj);
        assertTrue(result2chain);
    }

/*
_.all([1, 2, 3, 4], function(num) { return num % 2 === 0; }); // false
_.all([1, 2, 3, 4], function(num) { return num < 5; }); // true
*/
    @Test
    @SuppressWarnings("unchecked")
    public void all() {
        final boolean result1 = U.all(asList(1, 2, 3, 4),
            new Predicate<Integer>() {
            public boolean test(Integer item) {
                return item % 2 == 0;
            }
        });
        final boolean result1obj = new U(asList(1, 2, 3, 4))
            .all(
            new Predicate<Integer>() {
            public boolean test(Integer item) {
                return item % 2 == 0;
            }
        });
        final boolean result2 = U.all(asList(1, 2, 3, 4),
            new Predicate<Integer>() {
            public boolean test(Integer item) {
                return item < 5;
            }
        });
        final boolean result2obj = new U(asList(1, 2, 3, 4))
            .all(
            new Predicate<Integer>() {
            public boolean test(Integer item) {
                return item < 5;
            }
        });
        assertFalse(result1);
        assertFalse(result1obj);
        assertTrue(result2);
        assertTrue(result2obj);
    }

/*
_.any([1, 2, 3, 4], function(num) { return num % 2 === 0; }); // true
_.any([1, 2, 3, 4], function(num) { return num === 5; }); // false
*/
    @Test
    @SuppressWarnings("unchecked")
    public void any() {
        final boolean result1 = U.any(asList(1, 2, 3, 4),
            new Predicate<Integer>() {
            public boolean test(Integer item) {
                return item % 2 == 0;
            }
        });
        final boolean result1obj = new U(asList(1, 2, 3, 4))
            .any(
            new Predicate<Integer>() {
            public boolean test(Integer item) {
                return item % 2 == 0;
            }
        });
        final boolean result2 = U.any(asList(1, 2, 3, 4),
            new Predicate<Integer>() {
            public boolean test(Integer item) {
                return item == 5;
            }
        });
        final boolean result2obj = new U(asList(1, 2, 3, 4))
            .any(
            new Predicate<Integer>() {
            public boolean test(Integer item) {
                return item == 5;
            }
        });
        assertTrue(result1);
        assertTrue(result1obj);
        assertFalse(result2);
        assertFalse(result2obj);
    }

/*
_.some([1, 2, 3, 4], function(num) { return num % 2 === 0; }); // true
_.some([1, 2, 3, 4], function(num) { return num === 5; }); // false
*/
    @Test
    @SuppressWarnings("unchecked")
    public void some() {
        final boolean result1 = U.some(asList(1, 2, 3, 4),
            new Predicate<Integer>() {
            public boolean test(Integer item) {
                return item % 2 == 0;
            }
        });
        final boolean result1obj = new U(asList(1, 2, 3, 4))
            .some(
            new Predicate<Integer>() {
            public boolean test(Integer item) {
                return item % 2 == 0;
            }
        });
        final boolean result1chain = U.chain(asList(1, 2, 3, 4))
            .some(
            new Predicate<Integer>() {
            public boolean test(Integer item) {
                return item % 2 == 0;
            }
        }).item();
        final boolean result2 = U.some(asList(1, 2, 3, 4),
            new Predicate<Integer>() {
            public boolean test(Integer item) {
                return item == 5;
            }
        });
        final boolean result2obj = new U(asList(1, 2, 3, 4))
            .some(
            new Predicate<Integer>() {
            public boolean test(Integer item) {
                return item == 5;
            }
        });
        final boolean result2chain = U.chain(asList(1, 2, 3, 4))
            .some(
            new Predicate<Integer>() {
            public boolean test(Integer item) {
                return item == 5;
            }
        }).item();
        assertTrue(result1);
        assertTrue(result1obj);
        assertTrue(result1chain);
        assertFalse(result2);
        assertFalse(result2obj);
        assertFalse(result2chain);
    }

/*
_.include([1, 2, 3], 3); // true
*/
    @Test
    public void include() {
        final Boolean result = U.include(asList(1, 2, 3), 3);
        assertTrue(result);
    }

/*
_.contains([1, 2, 3], 3);
=> true
*/
    @Test
    @SuppressWarnings("unchecked")
    public void contains() {
        final boolean result = U.contains(asList(1, 2, 3), 3);
        assertTrue(result);
        final boolean resultObj = new U(asList(1, 2, 3)).contains(3);
        assertTrue(resultObj);
        final boolean resultChain = U.chain(asList(1, 2, 3)).contains(3).item();
        assertTrue(resultChain);
        final boolean result2 = U.contains(asList(1, 2, 3), 3, 1);
        assertTrue(result2);
        final boolean result3 = U.contains(asList(1, 2, 3), 1, 1);
        assertFalse(result3);
        final boolean result4 = U.contains(asList(1, 2, null), null);
        assertTrue(result4);
    }

/*
_.invoke([" foo ", "  bar"], "trim"); // ["foo", "bar"]
*/
    @Test
    @SuppressWarnings("unchecked")
    public void invoke() {
        assertEquals(U.invoke(asList(" foo ", "  bar"), "trim"), asList("foo", "bar"));
        assertEquals(new U(asList(" foo ", "  bar")).invoke("trim"), asList("foo", "bar"));
        assertEquals(U.chain(asList(" foo ", "  bar")).invoke("trim").value(), asList("foo", "bar"));
        assertEquals(U.invoke(asList("foo", "bar"), "concat", Arrays.<Object>asList("1")), asList("foo1", "bar1"));
        assertEquals(new U(asList("foo", "bar")).invoke("concat",
            Arrays.<Object>asList("1")), asList("foo1", "bar1"));
        assertEquals(U.chain(asList("foo", "bar")).invoke("concat",
            Arrays.<Object>asList("1")).value(), asList("foo1", "bar1"));
        assertEquals(U.invoke(asList(U.chain(asList(5, 1, 7)),
            U.chain(asList(3, 2, 1))), "sort").toString(), asList("[1, 5, 7]", "[1, 2, 3]").toString());
    }

    @Test(expected = IllegalArgumentException.class)
    @SuppressWarnings("unchecked")
    public void invokeError() {
        U.invoke(asList("foo", 123), "concat", Arrays.<Object>asList("1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void invokeError2() {
        U.invoke(asList(" foo ", "  bar"), "trim2");
    }

/*
var stooges = [{name: 'moe', age: 40}, {name: 'larry', age: 50}, {name: 'curly', age: 60}];
_.pluck(stooges, 'name');
=> ["moe", "larry", "curly"]
*/
    @Test
    @SuppressWarnings("unchecked")
    public void pluck() {
        class Person {
            public final String name;
            public final Integer age;
            public Person(final String name, final Integer age) {
                this.name = name;
                this.age = age;
            }
        }
        class Person2 {
            private final String name;
            private final Integer age;
            public Person2(final String name, final Integer age) {
                this.name = name;
                this.age = age;
            }
            public String getName() {
                return name;
            }
            public Integer getAge() {
                return age;
            }
        }
        assertEquals("[]", U.pluck(asList(), "name").toString());
        final List<?> result =
        U.pluck(asList(new Person("moe", 40), new Person("larry", 50), new Person("curly", 40)), "name");
        assertEquals("[moe, larry, curly]", result.toString());
        final List<?> result2 =
        U.pluck(asList(new Person2("moe", 40), new Person2("larry", 50), new Person2("curly", 40)), "getName");
        assertEquals("[moe, larry, curly]", result2.toString());
        final List<?> resultObj =
        new U(asList(new Person("moe", 40), new Person("larry", 50), new Person("curly", 40))).pluck("name");
        assertEquals("[moe, larry, curly]", resultObj.toString());
        final List<Object> resultChain =
        U.chain(asList(new Person("moe", 40), new Person("larry", 50), new Person("curly", 40))).pluck("name").value();
        assertEquals("[moe, larry, curly]", resultChain.toString());
        final Set<?> resultEmpty2 =
        U.pluck(new LinkedHashSet(asList()), "name");
        assertEquals("[]", resultEmpty2.toString());
        final Set<?> resultSet =
        U.pluck(new LinkedHashSet(
            asList(new Person("moe", 40), new Person("larry", 50), new Person("curly", 40))), "name");
        assertEquals("[moe, larry, curly]", resultSet.toString());
        final Set<?> resultSet2 =
        U.pluck(new LinkedHashSet(
            asList(new Person2("moe", 40), new Person2("larry", 50), new Person2("curly", 40))), "getName");
        assertEquals("[moe, larry, curly]", resultSet2.toString());
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
        U.pluck(asList(new Person("moe", 40), new Person("larry", 50), new Person("curly", 40)), "name2");
    }

    @Test(expected = IllegalArgumentException.class)
    @SuppressWarnings("unchecked")
    public void pluck3() {
        class Person {
            public final String name;
            public final Integer age;
            public Person(final String name, final Integer age) {
                this.name = name;
                this.age = age;
            }
        }
        U.pluck(new LinkedHashSet(
            asList(new Person("moe", 40), new Person("larry", 50), new Person("curly", 40))), "name2");
    }

/*
_.where(listOfPlays, {author: "Shakespeare", year: 1611});
=> [{title: "Cymbeline", author: "Shakespeare", year: 1611},
    {title: "The Tempest", author: "Shakespeare", year: 1611}]
*/
    @Test
    @SuppressWarnings("unchecked")
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
            U.where(listOfPlays, asList(
            Tuple.<String, Object>create("author", "Shakespeare"),
            Tuple.<String, Object>create("year", Integer.valueOf(1611)))).toString());
        assertEquals("[title: Cymbeline, author: Shakespeare, year: 1611,"
            + " title: The Tempest, author: Shakespeare, year: 1611]",
            U.where(listOfPlays, asList(
            Tuple.<String, Object>create("author", "Shakespeare"),
            Tuple.<String, Object>create("author2", "Shakespeare"),
            Tuple.<String, Object>create("year", Integer.valueOf(1611)))).toString());
        assertEquals("[title: Cymbeline, author: Shakespeare, year: 1611,"
            + " title: The Tempest, author: Shakespeare, year: 1611]",
            U.where(new LinkedHashSet<Book>(listOfPlays), asList(
            Tuple.<String, Object>create("author", "Shakespeare"),
            Tuple.<String, Object>create("year", Integer.valueOf(1611)))).toString());
        assertEquals("[title: Cymbeline, author: Shakespeare, year: 1611,"
            + " title: The Tempest, author: Shakespeare, year: 1611]",
            U.where(new LinkedHashSet<Book>(listOfPlays), asList(
            Tuple.<String, Object>create("author", "Shakespeare"),
            Tuple.<String, Object>create("author2", "Shakespeare"),
            Tuple.<String, Object>create("year", Integer.valueOf(1611)))).toString());
        assertEquals("[title: Cymbeline, author: Shakespeare, year: 1611,"
            + " title: The Tempest, author: Shakespeare, year: 1611]",
            new U(listOfPlays).where(asList(
            Tuple.<String, Object>create("author", "Shakespeare"),
            Tuple.<String, Object>create("year", Integer.valueOf(1611)))).toString());
        assertEquals("[title: Cymbeline, author: Shakespeare, year: 1611,"
            + " title: The Tempest, author: Shakespeare, year: 1611]",
            U.chain(listOfPlays).where(asList(
            Tuple.<String, Object>create("author", "Shakespeare"),
            Tuple.<String, Object>create("year", Integer.valueOf(1611)))).value().toString());
        class Book2 {
            public final String title;
            public final Integer year;
            private final String author;
            public Book2(final String title, final String author, final Integer year) {
                this.title = title;
                this.author = author;
                this.year = year;
            }
            public String getAuthor() {
                return author;
            }
            public String toString() {
                return "title: " + title + ", author: " + author + ", year: " + year;
            }
        }
        List<Book2> listOfPlays2 =
            new ArrayList<Book2>() { {
              add(new Book2("Cymbeline2", "Shakespeare", 1614));
              add(new Book2("Cymbeline", "Shakespeare", 1611));
              add(new Book2("The Tempest", "Shakespeare", 1611));
            } };
        assertEquals("[title: Cymbeline, author: Shakespeare, year: 1611,"
            + " title: The Tempest, author: Shakespeare, year: 1611]",
            U.where(listOfPlays2, asList(
            Tuple.<String, Object>create("getAuthor", "Shakespeare"),
            Tuple.<String, Object>create("author2", "Shakespeare"),
            Tuple.<String, Object>create("year", Integer.valueOf(1611)))).toString());
    }

/*
_.findWhere(listOfPlays, {author: "Shakespeare", year: 1611})
=> {title: "Cymbeline", author: "Shakespeare", year: 1611}
*/
    @Test
    @SuppressWarnings("unchecked")
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
            U.findWhere(listOfPlays, asList(
            Tuple.<String, Object>create("author", "Shakespeare"),
            Tuple.<String, Object>create("year", Integer.valueOf(1611)))).get().toString());
        assertEquals("title: Cymbeline, author: Shakespeare, year: 1611",
            new U(listOfPlays).findWhere(asList(
            Tuple.<String, Object>create("author", "Shakespeare"),
            Tuple.<String, Object>create("year", Integer.valueOf(1611)))).get().toString());
        assertEquals("title: Cymbeline, author: Shakespeare, year: 1611",
            (U.chain(listOfPlays).findWhere(asList(
            Tuple.<String, Object>create("author", "Shakespeare"),
            Tuple.<String, Object>create("year", Integer.valueOf(1611)))).item()).get().toString());
        assertEquals("title: Cymbeline, author: Shakespeare, year: 1611",
            U.findWhere(listOfPlays, asList(
            Tuple.<String, Object>create("author", "Shakespeare"),
            Tuple.<String, Object>create("author2", "Shakespeare"),
            Tuple.<String, Object>create("year", Integer.valueOf(1611)))).get().toString());
        class Book2 {
            public final String title;
            public final Integer year;
            private final String author;
            public Book2(final String title, final String author, final Integer year) {
                this.title = title;
                this.author = author;
                this.year = year;
            }
            public String getAuthor() {
                return author;
            }
            public String toString() {
                return "title: " + title + ", author: " + author + ", year: " + year;
            }
        }
        List<Book2> listOfPlays2 =
            new ArrayList<Book2>() { {
              add(new Book2("Cymbeline2", "Shakespeare", 1614));
              add(new Book2("Cymbeline", "Shakespeare", 1611));
              add(new Book2("The Tempest", "Shakespeare", 1611));
            } };
        assertEquals("title: Cymbeline, author: Shakespeare, year: 1611",
            U.findWhere(listOfPlays2, asList(
            Tuple.<String, Object>create("getAuthor", "Shakespeare"),
            Tuple.<String, Object>create("year", Integer.valueOf(1611)))).get().toString());
        assertEquals(Optional.<Book2>absent(),
            U.findWhere(listOfPlays2, asList(
            Tuple.<String, Object>create("getAuthor", "Shakespeare2"),
            Tuple.<String, Object>create("year", Integer.valueOf(1611)))));
    }

/*
var numbers = [10, 5, 100, 2, 1000];
_.max(numbers);
=> 1000
*/
    @Test
    @SuppressWarnings("unchecked")
    public void max() {
        final Integer result = U.max(asList(10, 5, 100, 2, 1000));
        assertEquals("1000", result.toString());
        final Integer resultObj = new U<Integer>(asList(10, 5, 100, 2, 1000)).max();
        assertEquals("1000", resultObj.toString());
        final Integer resultChain = (Integer) U.chain(asList(10, 5, 100, 2, 1000)).max().item();
        assertEquals("1000", resultChain.toString());
        final Integer resultComp = U.max(asList(10, 5, 100, 2, 1000),
                new Function<Integer, Integer>() {
            public Integer apply(Integer item) {
                return -item;
            }
        });
        assertEquals("2", resultComp.toString());
        final Integer resultCompObj = new U<Integer>(asList(10, 5, 100, 2, 1000)).max(
                new Function<Integer, Integer>() {
            public Integer apply(Integer item) {
                return -item;
            }
        });
        assertEquals("2", resultCompObj.toString());
        final Integer resultCompChain = (Integer) U.chain(asList(10, 5, 100, 2, 1000)).max(
                new Function<Integer, Integer>() {
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
        final Person resultPerson = U.max(
            asList(new Person("moe", 40), new Person("larry", 50), new Person("curly", 60)),
                new Function<Person, Integer>() {
            public Integer apply(Person item) {
                return item.age;
            }
        });
        assertEquals("curly", resultPerson.name);
        assertEquals(60, resultPerson.age.intValue());
    }

/*
var numbers = [10, 5, 100, 2, 1000];
_.min(numbers);
=> 2
*/
    @Test
    @SuppressWarnings("unchecked")
    public void min() {
        final Integer result = U.min(asList(10, 5, 100, 2, 1000));
        assertEquals("2", result.toString());
        final Integer resultObj = new U<Integer>(asList(10, 5, 100, 2, 1000)).min();
        assertEquals("2", resultObj.toString());
        final Integer resultChain = (Integer) U.chain(asList(10, 5, 100, 2, 1000)).min().item();
        assertEquals("2", resultChain.toString());
        final Integer resultComp = U.min(asList(10, 5, 100, 2, 1000),
                new Function<Integer, Integer>() {
            public Integer apply(Integer item) {
                return -item;
            }
        });
        assertEquals("1000", resultComp.toString());
        final Integer resultCompObj = new U<Integer>(asList(10, 5, 100, 2, 1000)).min(
                new Function<Integer, Integer>() {
            public Integer apply(Integer item) {
                return -item;
            }
        });
        assertEquals("1000", resultCompObj.toString());
        final Integer resultCompChain = (Integer) U.chain(asList(10, 5, 100, 2, 1000)).min(
                new Function<Integer, Integer>() {
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
        final Person resultPerson = U.min(
            asList(new Person("moe", 40), new Person("larry", 50), new Person("curly", 60)),
                new Function<Person, Integer>() {
            public Integer apply(Person item) {
                return item.age;
            }
        });
        assertEquals("moe", resultPerson.name);
        assertEquals(40, resultPerson.age.intValue());
    }

/*
_.sortWith([1, 2, 3, 4, 5, 6], function(num1, num2){ return Math.sin(num1) - Math.sin(num2); });
=> [5, 4, 6, 3, 1, 2]
*/
    @Test
    @SuppressWarnings("unchecked")
    public void sortWith() {
        final List<Integer> result =
        U.sortWith(asList(1, 2, 3, 4, 5, 6),
            new Comparator<Integer>() {
            public int compare(Integer item1, Integer item2) {
                return Double.valueOf(Math.sin(item1) * 1000).intValue()
                    - Double.valueOf(Math.sin(item2) * 1000).intValue();
            }
        });
        assertEquals("[5, 4, 6, 3, 1, 2]", result.toString());
        final List<Integer> resultObj =
        new U(asList(1, 2, 3, 4, 5, 6)).sortWith(
            new Comparator<Integer>() {
            public int compare(Integer item1, Integer item2) {
                return Double.valueOf(Math.sin(item1) * 1000).intValue()
                    - Double.valueOf(Math.sin(item2) * 1000).intValue();
            }
        });
        assertEquals("[5, 4, 6, 3, 1, 2]", resultObj.toString());
        final List<Integer> resultChain =
        U.chain(asList(1, 2, 3, 4, 5, 6)).sortWith(
            new Comparator<Integer>() {
            public int compare(Integer item1, Integer item2) {
                return Double.valueOf(Math.sin(item1) * 1000).intValue()
                    - Double.valueOf(Math.sin(item2) * 1000).intValue();
            }
        }).value();
        assertEquals("[5, 4, 6, 3, 1, 2]", resultChain.toString());
    }

/*
_.sortBy([1, 2, 3, 4, 5, 6], function(num){ return Math.sin(num); });
=> [5, 4, 6, 3, 1, 2]
*/
    @Test
    @SuppressWarnings("unchecked")
    public void sortBy() {
        final List<Integer> result =
        U.sortBy(asList(1, 2, 3, 4, 5, 6),
            new Function<Integer, Integer>() {
            public Integer apply(Integer item) {
                return Double.valueOf(Math.sin(item) * 1000).intValue();
            }
        });
        assertEquals("[5, 4, 6, 3, 1, 2]", result.toString());
        final List<Integer> resultObj =
        new U(asList(1, 2, 3, 4, 5, 6)).sortBy(
            new Function<Integer, Integer>() {
            public Integer apply(Integer item) {
                return Double.valueOf(Math.sin(item) * 1000).intValue();
            }
        });
        assertEquals("[5, 4, 6, 3, 1, 2]", resultObj.toString());
        final List<Integer> resultChain =
        U.chain(asList(1, 2, 3, 4, 5, 6)).sortBy(
            new Function<Integer, Integer>() {
            public Integer apply(Integer item) {
                return Double.valueOf(Math.sin(item) * 1000).intValue();
            }
        }).value();
        assertEquals("[5, 4, 6, 3, 1, 2]", resultChain.toString());
    }

/*
var stooges = [{name: 'moe', age: 40}, {name: 'larry', age: 50}, {name: 'curly', age: 60}];
_.sortBy(stooges, 'name');
=> [{name: 'curly', age: 60}, {name: 'larry', age: 50}, {name: 'moe', age: 40}];
*/
    @Test
    @SuppressWarnings("unchecked")
    public void sortByMap() {
        final List<Map<String, Comparable>> result = U.sortBy(asList(
            (Map<String, Comparable>) new LinkedHashMap<String, Comparable>() { {
                put("name", "moe"); put("age", 40); } },
            (Map<String, Comparable>) new LinkedHashMap<String, Comparable>() { {
                put("name", "larry"); put("age", 50); } },
            (Map<String, Comparable>) new LinkedHashMap<String, Comparable>() { {
                put("name", "curly"); put("age", 60); } }
        ), "name");
        assertEquals("[{name=curly, age=60}, {name=larry, age=50}, {name=moe, age=40}]", result.toString());
        final List<Map<String, Comparable>> resultChain = U.chain(asList(
            (Map<String, Comparable>) new LinkedHashMap<String, Comparable>() { {
                put("name", "moe"); put("age", 40); } },
            (Map<String, Comparable>) new LinkedHashMap<String, Comparable>() { {
                put("name", "larry"); put("age", 50); } },
            (Map<String, Comparable>) new LinkedHashMap<String, Comparable>() { {
                put("name", "curly"); put("age", 60); } }
        )).sortBy("name").value();
        assertEquals("[{name=curly, age=60}, {name=larry, age=50}, {name=moe, age=40}]", resultChain.toString());
    }

/*
_.groupBy([1.3, 2.1, 2.4], function(num){ return Math.floor(num); });
=> {1: [1.3], 2: [2.1, 2.4]}
*/
    @Test
    @SuppressWarnings("unchecked")
    public void groupBy() {
        final Map<Double, List<Double>> result =
        U.groupBy(asList(1.3, 2.1, 2.4),
            new Function<Double, Double>() {
            public Double apply(Double num) {
                return Math.floor(num);
            }
        });
        assertEquals("{1.0=[1.3], 2.0=[2.1, 2.4]}", result.toString());
        final Map<Double, List<Double>> resultObj =
        new U(asList(1.3, 2.1, 2.4)).groupBy(
            new Function<Double, Double>() {
            public Double apply(Double num) {
                return Math.floor(num);
            }
        });
        assertEquals("{1.0=[1.3], 2.0=[2.1, 2.4]}", resultObj.toString());
        final Map<Double, List<Double>> resultChain =
        (Map<Double, List<Double>>) U.chain(asList(1.3, 2.1, 2.4)).groupBy(
            new Function<Double, Double>() {
            public Double apply(Double num) {
                return Math.floor(num);
            }
        }).item();
        assertEquals("{1.0=[1.3], 2.0=[2.1, 2.4]}", resultChain.toString());
    }

/*
_.groupBy([1.3, 2.1, 2.4], function(num){ return Math.floor(num); });
=> {1: [1.3], 2: [2.1, 2.4]}
*/
    @Test
    @SuppressWarnings("unchecked")
    public void groupByWithSumming() {
        final Map<Double, Optional<Double>> result =
        U.groupBy(asList(1.3, 2.1, 2.4),
            new Function<Double, Double>() {
            public Double apply(Double num) {
                return Math.floor(num);
            }
            },
            new BinaryOperator<Double>() {
            public Double apply(Double a, Double b) {
                return a + b;
            }
            }
        );
        assertEquals("{1.0=Optional.of(1.3), 2.0=Optional.of(4.5)}", result.toString());
        final Map<Double, Optional<Double>> resultObj =
        new U(asList(1.3, 2.1, 2.4)).groupBy(
            new Function<Double, Double>() {
            public Double apply(Double num) {
                return Math.floor(num);
            }
            },
            new BinaryOperator<Double>() {
            public Double apply(Double a, Double b) {
                return a + b;
            }
            });
        assertEquals("{1.0=Optional.of(1.3), 2.0=Optional.of(4.5)}", resultObj.toString());
        final Map<Double, Optional<Double>> resultChain =
        U.chain(asList(1.3, 2.1, 2.4)).groupBy(
            new Function<Double, Double>() {
            public Double apply(Double num) {
                return Math.floor(num);
            }
            },
            new BinaryOperator<Double>() {
            public Double apply(Double a, Double b) {
                return a + b;
            }
            }).item();
        assertEquals("{1.0=Optional.of(1.3), 2.0=Optional.of(4.5)}", resultChain.toString());
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
    @SuppressWarnings("unchecked")
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
        U.indexBy(asList(new Person("moe", 40), new Person("larry", 50), new Person("curly", 60)), "age");
        assertEquals("{40=[moe, 40], 50=[larry, 50], 60=[curly, 60]}", result.toString());
        final Map<String, List<Person>> resultObj =
        new U(asList(new Person("moe", 40), new Person("larry", 50), new Person("curly", 60))).indexBy("age");
        assertEquals("{40=[moe, 40], 50=[larry, 50], 60=[curly, 60]}", resultObj.toString());
        final Map<Object, List<Person>> resultChain =
        (Map<Object, List<Person>>) U.chain(asList(new Person("moe", 40), new Person("larry", 50),
            new Person("curly", 60))).indexBy("age").item();
        assertEquals("{40=[moe, 40], 50=[larry, 50], 60=[curly, 60]}", resultChain.toString());
        final Map<String, List<Person>> result2 =
        U.indexBy(asList(new Person("moe", 40), new Person("larry", 50), new Person("curly", 60)), "age2");
        assertEquals("{null=[moe, 40, larry, 50, curly, 60]}", result2.toString());
    }

/*
var stooges = [{name: 'moe', age: 40}, {name: 'moe', age: 50}, {name: 'curly', age: 60}];
_.countBy(stooges, 'age');
=> {moe: 2, curly: 1}
*/
    @Test
    @SuppressWarnings("unchecked")
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
        U.countBy(asList(new Person("moe", 40), new Person("moe", 50), new Person("curly", 60)),
            new Function<Person, String>() {
            public String apply(Person person) {
                return person.name;
            }
        });
        assertEquals("{moe=2, curly=1}", result.toString());
        final Map<String, Integer> resultObj =
        new U(asList(new Person("moe", 40), new Person("moe", 50), new Person("curly", 60))).countBy(
            new Function<Person, String>() {
            public String apply(Person person) {
                return person.name;
            }
        });
        assertEquals("{moe=2, curly=1}", resultObj.toString());
        final Map<String, Integer> resultChain =
        (Map<String, Integer>) U.chain(asList(new Person("moe", 40), new Person("moe", 50),
            new Person("curly", 60))).countBy(
            new Function<Person, String>() {
            public String apply(Person person) {
                return person.name;
            }
        }).item();
        assertEquals("{moe=2, curly=1}", resultChain.toString());
    }

/*
_.shuffle([1, 2, 3, 4, 5, 6]);
=> [4, 1, 6, 3, 5, 2]
*/
    @Test
    @SuppressWarnings("unchecked")
    public void shuffle() {
        final List<Integer> result = U.shuffle(asList(1, 2, 3, 4, 5, 6));
        assertEquals(6, result.size());
        final List<Integer> resultObj = new U(asList(1, 2, 3, 4, 5, 6)).shuffle();
        assertEquals(6, resultObj.size());
        final List<Integer> resultChain = U.chain(asList(1, 2, 3, 4, 5, 6)).shuffle().value();
        assertEquals(6, resultChain.size());
    }

/*
_.sample([1, 2, 3, 4, 5, 6]);
=> 4
_.sample([1, 2, 3, 4, 5, 6], 3);
=> [1, 6, 2]
*/
    @Test
    @SuppressWarnings("unchecked")
    public void sample() {
        final Integer result = U.sample(asList(1, 2, 3, 4, 5, 6));
        assertTrue(result >= 1 && result <= 6);
        final Integer resultObj = new U<Integer>(asList(1, 2, 3, 4, 5, 6)).sample();
        assertTrue(resultObj >= 1 && resultObj <= 6);
        final Set<Integer> resultList = U.sample(asList(1, 2, 3, 4, 5, 6), 3);
        assertEquals(3, resultList.size());
        final Integer resultChain = (Integer) U.chain(asList(1, 2, 3, 4, 5, 6)).sample().item();
        assertTrue(resultChain >= 1 && resultChain <= 6);
        final List<Integer> resultListChain = U.chain(asList(1, 2, 3, 4, 5, 6)).sample(3).value();
        assertEquals(3, resultListChain.size());
    }

/*
(function(){ return _.toArray(arguments).slice(1); })(1, 2, 3, 4);
=> [2, 3, 4]
*/
    @Test
    @SuppressWarnings("unchecked")
    public void toArray() {
        final Object[] result = U.<Integer>toArray(asList(1, 2, 3, 4));
        assertEquals("1", result[0].toString());
        final Object[] resultObj = new U(asList(1, 2, 3, 4)).toArray();
        assertEquals("1", resultObj[0].toString());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void toMap() {
        assertEquals("{name1=one, name2=two}", U.toMap((new LinkedHashMap<String, String>() { {
            put("name1", "one");
            put("name2", "two");
        } }).entrySet()).toString());
        assertEquals("{name1=one, name2=two}", new U((new LinkedHashMap<String, String>() { {
            put("name1", "one");
            put("name2", "two");
        } }).entrySet()).toMap().toString());
        assertEquals("{name1=one, name2=two}", U.toMap(new ArrayList<Tuple<String, String>>() { {
            add(Tuple.create("name1", "one"));
            add(Tuple.create("name2", "two"));
        } }).toString());
    }

/*
_.size({one: 1, two: 2, three: 3});
=> 3
*/
    @Test
    @SuppressWarnings("unchecked")
    public void size() {
        final int result = U.size(asList(1, 2, 3, 4));
        assertEquals(4, result);
        final int resultObj = new U(asList(1, 2, 3, 4)).size();
        assertEquals(4, resultObj);
        final int resultChain = U.chain(asList(1, 2, 3, 4)).size();
        assertEquals(4, resultChain);
        final int[] array = new int[] {1, 2, 3, 4, 5, 6};
        Iterable<Integer> iterable = new Iterable<Integer>() {
            public Iterator<Integer> iterator() {
                return new Iterator<Integer>() {
                    private int index;
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
        assertEquals(6, U.size(iterable));
        assertEquals(5, U.size(new Integer[] {5, 4, 3, 2, 1}));
        assertEquals(5, U.size(5, 4, 3, 2, 1));
    }

/*
_.partition([0, 1, 2, 3, 4, 5], isOdd);
=> [[1, 3, 5], [0, 2, 4]]
*/
    @Test
    public void partition() {
        final List<List<Integer>> result = U.partition(asList(0, 1, 2, 3, 4, 5), new Predicate<Integer>() {
            public boolean test(final Integer item) {
                return item % 2 == 1;
            }
        });
        assertEquals("[1, 3, 5]", result.get(0).toString());
        assertEquals("[0, 2, 4]", result.get(1).toString());
        final List<Integer>[] resultArray = U.partition(new Integer[] {0, 1, 2, 3, 4, 5}, new Predicate<Integer>() {
            public boolean test(final Integer item) {
                return item % 2 == 1;
            }
        });
        assertEquals("[1, 3, 5]", resultArray[0].toString());
        assertEquals("[0, 2, 4]", resultArray[1].toString());
    }
}
