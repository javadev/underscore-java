/*
 * The MIT License (MIT)
 *
 * Copyright 2015-2025 Valentyn Kolesnikov
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

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import org.junit.jupiter.api.Test;

/**
 * Underscore library unit test.
 *
 * @author Valentyn Kolesnikov
 */
@SuppressWarnings("java:S1186")
class CollectionsTest {

    /*
    _.each([1, 2, 3], alert);
    => alerts each number in turn...
    */
    @Test
    @SuppressWarnings("unchecked")
    void each() {
        final List<Integer> result = new ArrayList<>();
        Underscore.each(asList(1, 2, 3), result::add);
        assertEquals("[1, 2, 3]", result.toString());
        final List<Integer> result2 = new ArrayList<>();
        new Underscore<>(asList(1, 2, 3)).each(result2::add);
        assertEquals("[1, 2, 3]", result2.toString());
    }

    /*
    _.eachRight([1, 2, 3], alert);
    => alerts each number in turn from right to left...
    */
    @Test
    @SuppressWarnings("unchecked")
    void eachRight() {
        final List<Integer> result = new ArrayList<>();
        Underscore.eachRight(asList(1, 2, 3), result::add);
        assertEquals("[3, 2, 1]", result.toString());
        final List<Integer> result2 = new ArrayList<>();
        new Underscore<>(asList(1, 2, 3)).eachRight(result2::add);
        assertEquals("[3, 2, 1]", result2.toString());
    }

    /*
    _.forEach([1, 2, 3], alert);
    => alerts each number in turn...
    */
    @Test
    void forEach() {
        final List<Integer> result = new ArrayList<>();
        Underscore.forEach(asList(1, 2, 3), result::add);
        assertEquals("[1, 2, 3]", result.toString());
        final List<Map.Entry<String, Integer>> resultChain = new ArrayList<>();
        Underscore.chain(
                        (new LinkedHashMap<String, Integer>() {
                                    {
                                        put("a", 1);
                                        put("b", 2);
                                        put("c", 3);
                                    }
                                })
                                .entrySet())
                .forEach(resultChain::add);
        assertEquals("[a=1, b=2, c=3]", resultChain.toString());
    }

    /*
    _.forEachIndexed([1, 2, 3], alert);
    => alerts each number in turn...
    */
    @Test
    @SuppressWarnings("unchecked")
    void forEachIndexed() {
        final List<Integer> result = new ArrayList<>();
        Underscore.forEachIndexed(asList(1, 2, 3), (index, item) -> result.add(item));
        assertEquals("[1, 2, 3]", result.toString());
        final List<Integer> resultObj = new ArrayList<>();
        new Underscore<>(asList(1, 2, 3)).forEachIndexed((index, item) -> resultObj.add(item));
        assertEquals("[1, 2, 3]", resultObj.toString());
    }

    /*
    _.forEach([1, 2, 3], alert);
    => alerts each number in turn from right to left...
    */
    @Test
    @SuppressWarnings("unchecked")
    void forEachRight() {
        final List<Integer> result = new ArrayList<>();
        Underscore.forEachRight(asList(1, 2, 3), result::add);
        assertEquals("[3, 2, 1]", result.toString());
        final List<Integer> result2 = new ArrayList<>();
        new Underscore<>(asList(1, 2, 3)).forEachRight(result2::add);
        assertEquals("[3, 2, 1]", result2.toString());
        final List<Map.Entry<String, Integer>> resultChain = new ArrayList<>();
        Underscore.chain(
                        (new LinkedHashMap<String, Integer>() {
                                    {
                                        put("a", 1);
                                        put("b", 2);
                                        put("c", 3);
                                    }
                                })
                                .entrySet())
                .forEachRight(resultChain::add);
        assertEquals("[c=3, b=2, a=1]", resultChain.toString());
    }

    /*
    _([1, 2, 3]).forEach(alert);
    => alerts each number in turn...
    */
    @Test
    @SuppressWarnings("unchecked")
    void forEachObj() {
        final List<Integer> result = new ArrayList<>();
        new Underscore<>(asList(1, 2, 3)).forEach(result::add);
        assertEquals("[1, 2, 3]", result.toString());
    }

    /*
    _.each({one: 1, two: 2, three: 3}, alert);
    => alerts each number value in turn...
    */
    @Test
    void eachMap() {
        final List<String> result = new ArrayList<>();
        Underscore.each(
                (new LinkedHashMap<String, Integer>() {
                            {
                                put("one", 1);
                                put("two", 2);
                                put("three", 3);
                            }
                        })
                        .entrySet(),
                item -> result.add(item.getKey()));
        assertEquals("[one, two, three]", result.toString());
    }

    /*
    _.map([1, 2, 3], function(num){ return num * 3; });
    => [3, 6, 9]
    */
    @Test
    void map() {
        List<Integer> result = Underscore.map(asList(1, 2, 3), item -> item * 3);
        assertEquals("[3, 6, 9]", result.toString());
        List<Integer> resultObject = new Underscore<>(asList(1, 2, 3)).map(item -> item * 3);
        assertEquals("[3, 6, 9]", resultObject.toString());
        List<Integer> result1 = Underscore.map(new int[] {1, 2, 3}, item -> item * 3);
        assertEquals("[3, 6, 9]", result1.toString());
    }

    @Test
    void mapMulti() {
        List<Integer> result =
                Underscore.mapMulti(
                        asList("Java", "Python", "C#"),
                        (str, consumer) -> {
                            for (int i = 0; i < str.length(); i++) {
                                consumer.accept(str.length());
                            }
                        });
        assertEquals("[4, 4, 4, 4, 6, 6, 6, 6, 6, 6, 2, 2]", result.toString());
        List<Object> resultChain =
                Underscore.chain(asList("Java", "Python", "C#"))
                        .mapMulti(
                                (str, consumer) -> {
                                    for (int i = 0; i < str.length(); i++) {
                                        consumer.accept(str.length());
                                    }
                                })
                        .value();
        assertEquals("[4, 4, 4, 4, 6, 6, 6, 6, 6, 6, 2, 2]", resultChain.toString());
    }

    /*
    _.map(_.range(3), function(num){ return (num + 1) * 3; });
    => [3, 6, 9]
    */
    @Test
    void mapArray() {
        List<Integer> result = Underscore.map(Underscore.range(3), item -> (item + 1) * 3);
        assertEquals("[3, 6, 9]", result.toString());
    }

    /*
    _.map({one: 1, two: 2, three: 3}, function(num, key){ return num * 3; });
    => [3, 6, 9]
    */
    @Test
    void mapMap() {
        final Set<Integer> result =
                Underscore.map(
                        (new LinkedHashMap<Integer, String>() {
                                    {
                                        put(1, "one");
                                        put(2, "two");
                                        put(3, "three");
                                    }
                                })
                                .entrySet(),
                        item -> item.getKey() * 3);
        assertEquals("[3, 6, 9]", result.toString());
    }

    /*
    _.mapIndexed([1, 2, 3], function(num){ return num * 3; });
    => [3, 6, 9]
    */
    @Test
    void mapIndexed() {
        List<Integer> result = Underscore.mapIndexed(asList(1, 2, 3), (index, item) -> item * 3);
        assertEquals("[3, 6, 9]", result.toString());
        List<Integer> resultObject =
                new Underscore<>(asList(1, 2, 3)).mapIndexed((index, item) -> item * 3);
        assertEquals("[3, 6, 9]", resultObject.toString());
        List<Integer> resultChain =
                Underscore.chain(asList(1, 2, 3)).mapIndexed((index, item) -> item * 3).value();
        assertEquals("[3, 6, 9]", resultChain.toString());
    }

    /*
    _.collect([1, 2, 3], function(num){ return num * 3; });
    => [3, 6, 9]
    */
    @Test
    @SuppressWarnings("unchecked")
    void collect() {
        List<Integer> result = Underscore.collect(asList(1, 2, 3), item -> item * 3);
        assertEquals("[3, 6, 9]", result.toString());
        Set<Integer> resultSet =
                Underscore.collect(new LinkedHashSet<>(asList(1, 2, 3)), item -> item * 3);
        assertEquals("[3, 6, 9]", resultSet.toString());
    }

    /*
    var sum = _.reduce([1, 2, 3], function(memo, num){ return memo + num; }, 0);
    => 6
    */
    @Test
    void reduce() {
        final Integer result = Underscore.reduce(asList(1, 2, 3), Integer::sum, 0);
        assertEquals("6", result.toString());
    }

    /*
    var sum = _.reduce([1, 2, 3], function(memo, num){ return memo + num; });
    => 6
    */
    @Test
    void reduceWithoutInit() {
        final Integer result = Underscore.reduce(asList(1, 2, 3), Integer::sum).get();
        assertEquals("6", result.toString());
        final Integer resultChain =
                Underscore.chain(asList(1, 2, 3)).reduce(Integer::sum).item().get();
        assertEquals("6", resultChain.toString());
        Underscore.reduce(new ArrayList<>(), Integer::sum);
    }

    /*
    var sum = _.reduceRight([1, 2, 3], function(memo, num){ return memo + num; });
    => 6
    */
    @Test
    void reduceRightWithoutInit() {
        final Integer result = Underscore.reduceRight(asList(1, 2, 3), Integer::sum).get();
        assertEquals("6", result.toString());
        final Integer resultChain =
                Underscore.chain(asList(1, 2, 3)).reduceRight(Integer::sum).item().get();
        assertEquals("6", resultChain.toString());
    }

    /*
    var sum = _.reduce([1, 2, 3], function(memo, num){ return memo + num; }, 0);
    => 6
    */
    @Test
    void reduceIntArray() {
        final Integer result = Underscore.reduce(new int[] {1, 2, 3}, Integer::sum, 0);
        assertEquals("6", result.toString());
    }

    /*
    var sum = _.reduce([1, 2, 3], function(memo, num){ return memo + num; }, 0);
    => 6
    */
    @Test
    void reduceArray() {
        final Integer result = Underscore.reduce(new Integer[] {1, 2, 3}, Integer::sum, 0);
        assertEquals("6", result.toString());
    }

    /*
    var list = [[0, 1], [2, 3], [4, 5]];
    var flat = _.inject(list, function(a, b) { return a.concat(b); }, []);
    => [0, 1, 2, 3, 4, 5]
    */
    @Test
    @SuppressWarnings("unchecked")
    void inject() {
        final List<Integer> result =
                Underscore.inject(
                        asList(asList(0, 1), asList(2, 3), asList(4, 5)),
                        (item1, item2) -> {
                            List<Integer> list = new ArrayList<>(item1);
                            list.addAll(item2);
                            return list;
                        },
                        Collections.emptyList());
        assertEquals("[0, 1, 2, 3, 4, 5]", result.toString());
    }

    /*
    var list = [[0, 1], [2, 3], [4, 5]];
    var flat = _.foldl(list, function(a, b) { return a.concat(b); }, []);
    => [0, 1, 2, 3, 4, 5]
    */
    @Test
    @SuppressWarnings("unchecked")
    void foldl() {
        final List<Integer> result =
                Underscore.foldl(
                        asList(asList(0, 1), asList(2, 3), asList(4, 5)),
                        (item1, item2) -> {
                            List<Integer> list = new ArrayList<>(item1);
                            list.addAll(item2);
                            return list;
                        },
                        Collections.emptyList());
        assertEquals("[0, 1, 2, 3, 4, 5]", result.toString());
    }

    /*
    var list = [[0, 1], [2, 3], [4, 5]];
    var flat = _.reduceRight(list, function(a, b) { return a.concat(b); }, []);
    => [4, 5, 2, 3, 0, 1]
    */
    @Test
    @SuppressWarnings("unchecked")
    void reduceRight() {
        final List<Integer> result =
                Underscore.reduceRight(
                        asList(asList(0, 1), asList(2, 3), asList(4, 5)),
                        (item1, item2) -> {
                            List<Integer> list = new ArrayList<>(item1);
                            list.addAll(item2);
                            return list;
                        },
                        Collections.emptyList());
        assertEquals("[4, 5, 2, 3, 0, 1]", result.toString());
    }

    /*
    var sum = _.reduceRight([1, 2, 3], function(memo, num){ return memo + num; }, 0);
    => 6
    */
    @Test
    void reduceRightIntArray() {
        final Integer result = Underscore.reduceRight(new int[] {1, 2, 3}, Integer::sum, 0);
        assertEquals("6", result.toString());
    }

    /*
    var sum = _.reduceRight([1, 2, 3], function(memo, num){ return memo + num; }, 0);
    => 6
    */
    @Test
    void reduceRightArray() {
        final Integer result = Underscore.reduceRight(new Integer[] {1, 2, 3}, Integer::sum, 0);
        assertEquals("6", result.toString());
    }

    /*
    var list = [[0, 1], [2, 3], [4, 5]];
    var flat = _.foldr(list, function(a, b) { return a.concat(b); }, []);
    => [4, 5, 2, 3, 0, 1]
    */
    @Test
    @SuppressWarnings("unchecked")
    void foldr() {
        final List<Integer> result =
                Underscore.foldr(
                        asList(asList(0, 1), asList(2, 3), asList(4, 5)),
                        (item1, item2) -> {
                            List<Integer> list = new ArrayList<>(item1);
                            list.addAll(item2);
                            return list;
                        },
                        Collections.emptyList());
        assertEquals("[4, 5, 2, 3, 0, 1]", result.toString());
    }

    /*
    var even = _.find([1, 2, 3, 4, 5, 6], function(num){ return num % 2 == 0; });
    => 2
    */
    @Test
    void find() {
        final Optional<Integer> result =
                Underscore.find(asList(1, 2, 3, 4, 5, 6), item -> item % 2 == 0);
        assertEquals("Optional[2]", result.toString());
        final Optional<Integer> resultChain =
                Underscore.chain(asList(1, 2, 3, 4, 5, 6)).find(item -> item % 2 == 0).item();
        assertEquals("Optional[2]", resultChain.toString());
        final Optional<Integer> resultChain2 =
                Underscore.chain(asList(1, 2, 3, 4, 5, 6)).find(item -> item > 6).item();
        assertEquals("Optional.empty", resultChain2.toString());
    }

    /*
    var even = _.findLast([1, 2, 3, 4, 5, 6], function(num){ return num % 2 == 0; });
    => 6
    */
    @Test
    void findLast() {
        final Optional<Integer> result =
                Underscore.findLast(asList(1, 2, 3, 4, 5, 6), item -> item % 2 == 0);
        assertEquals("Optional[6]", result.toString());
        final Optional<Integer> result2 =
                Underscore.findLast(asList(1, 2, 3, 4, 5, 6), item -> item > 6);
        assertEquals("Optional.empty", result2.toString());
        final Optional<Integer> resultChain =
                Underscore.chain(asList(1, 2, 3, 4, 5, 6)).findLast(item -> item % 2 == 0).item();
        assertEquals("Optional[6]", resultChain.toString());
        final Optional<Integer> resultChain2 =
                Underscore.chain(asList(1, 2, 3, 4, 5, 6)).findLast(item -> item > 6).item();
        assertEquals("Optional.empty", resultChain2.toString());
    }

    /*
    var even = _.find([1, 2, 3, 4, 5, 6], function(num){ return num % 2 == 0; });
    => 2
    */
    @Test
    void detect() {
        final Optional<Integer> result =
                Underscore.detect(asList(1, 2, 3, 4, 5, 6), item -> item % 2 == 0);
        assertEquals("Optional[2]", result.toString());
    }

    /*
    var evens = _.filter([1, 2, 3, 4, 5, 6], function(num){ return num % 2 == 0; });
    => [2, 4, 6]
    */
    @Test
    void filter() {
        final List<Integer> result =
                Underscore.filter(
                        new ArrayDeque<>(asList(1, 2, 3, 4, 5, 6)), item -> item % 2 == 0);
        assertEquals("[2, 4, 6]", result.toString());
        final List<Integer> resultList =
                Underscore.filter(asList(1, 2, 3, 4, 5, 6), item -> item % 2 == 0);
        assertEquals("[2, 4, 6]", resultList.toString());
        final List<Integer> resultObject =
                new Underscore<>(asList(1, 2, 3, 4, 5, 6)).filter(item -> item % 2 == 0);
        assertEquals("[2, 4, 6]", resultObject.toString());
    }

    /*
    var evens = _.filterFalse([1, 2, 3, 4, 5, 6], function(num){ return num % 2 == 0; });
    => [1, 3, 5]
    */
    @Test
    @SuppressWarnings("unchecked")
    void filterFalse() {
        final List<Integer> result =
                Underscore.filterFalse(asList(1, 2, 3, 4, 5, 6), item -> item % 2 == 0);
        assertEquals("[1, 3, 5]", result.toString());
        final List<Integer> resultObject =
                new Underscore<>(asList(1, 2, 3, 4, 5, 6)).filterFalse(item -> item % 2 == 0);
        assertEquals("[1, 3, 5]", resultObject.toString());
        final Set<Integer> resultSet =
                Underscore.filterFalse(
                        new LinkedHashSet<>(asList(1, 2, 3, 4, 5, 6)), item -> item % 2 == 0);
        assertEquals("[1, 3, 5]", resultSet.toString());
    }

    /*
    var evens = _.filterIndexed([1, 2, 3, 4, 5, 6], function(index, num){ return index !== 1 && num % 2 == 0; });
    => [4, 6]
    */
    @Test
    void filterIndexed() {
        final List<Integer> result =
                Underscore.filterIndexed(
                        asList(1, 2, 3, 4, 5, 6), (index, item) -> index != 1 && item % 2 == 0);
        assertEquals("[4, 6]", result.toString());
        final List<Integer> resultChain =
                Underscore.chain(asList(1, 2, 3, 4, 5, 6))
                        .filterIndexed((index, item) -> index != 1 && item % 2 == 0)
                        .value();
        assertEquals("[4, 6]", resultChain.toString());
    }

    /*
    var evens = _.filter([1, 2, 3, 4, 5, 6], function(num){ return num % 2 == 0; });
    => [2, 4, 6]
    */
    @Test
    @SuppressWarnings("unchecked")
    void select() {
        final List<Integer> result =
                Underscore.select(asList(1, 2, 3, 4, 5, 6), item -> item % 2 == 0);
        assertEquals("[2, 4, 6]", result.toString());
        final Set<Integer> resultSet =
                Underscore.select(
                        new LinkedHashSet<>(asList(1, 2, 3, 4, 5, 6)), item -> item % 2 == 0);
        assertEquals("[2, 4, 6]", resultSet.toString());
    }

    /*
    var evens = _.reject([1, 2, 3, 4, 5, 6], function(num){ return num % 2 == 0; });
    => [2, 4, 6]
    */
    @Test
    @SuppressWarnings("unchecked")
    void reject() {
        final List<Integer> result =
                Underscore.reject(asList(1, 2, 3, 4, 5, 6), item -> item % 2 == 0);
        assertEquals("[1, 3, 5]", result.toString());
        final List<Integer> resultObject =
                new Underscore<>(asList(1, 2, 3, 4, 5, 6)).reject(item -> item % 2 == 0);
        assertEquals("[1, 3, 5]", resultObject.toString());
        final Set<Integer> resultSet =
                Underscore.reject(
                        new LinkedHashSet<>(asList(1, 2, 3, 4, 5, 6)), item -> item % 2 == 0);
        assertEquals("[1, 3, 5]", resultSet.toString());
    }

    /*
    var evens = _.rejectIndexed([1, 2, 3, 4, 5, 6], function(index, num){ return index !== 1 && num % 2 == 0; });
    => [1, 2, 3, 5]
    */
    @Test
    void rejectIndexed() {
        final List<Integer> result =
                Underscore.rejectIndexed(
                        asList(1, 2, 3, 4, 5, 6), (index, item) -> index != 1 && item % 2 == 0);
        assertEquals("[1, 2, 3, 5]", result.toString());
        final List<Integer> resultChain =
                Underscore.chain(asList(1, 2, 3, 4, 5, 6))
                        .rejectIndexed((index, item) -> index != 1 && item % 2 == 0)
                        .value();
        assertEquals("[1, 2, 3, 5]", resultChain.toString());
    }

    /*
    _.every([1, 2, 3, 4], function(num) { return num % 2 === 0; }); // false
    _.every([1, 2, 3, 4], function(num) { return num < 5; }); // true
    */
    @Test
    @SuppressWarnings("unchecked")
    void every() {
        final boolean result1 = Underscore.every(asList(1, 2, 3, 4), item -> item % 2 == 0);
        final boolean result1obj =
                new Underscore<>(asList(1, 2, 3, 4)).every(item -> item % 2 == 0);
        final boolean result1chain =
                Underscore.chain(asList(1, 2, 3, 4)).every(item -> item % 2 == 0).item();
        final boolean result2 = Underscore.every(asList(1, 2, 3, 4), item -> item < 5);
        final boolean result2obj = new Underscore<>(asList(1, 2, 3, 4)).every(item -> item < 5);
        final boolean result2chain =
                Underscore.chain(asList(1, 2, 3, 4)).every(item -> item < 5).item();
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
    void all() {
        final boolean result1 = Underscore.all(asList(1, 2, 3, 4), item -> item % 2 == 0);
        final boolean result1obj = new Underscore<>(asList(1, 2, 3, 4)).all(item -> item % 2 == 0);
        final boolean result2 = Underscore.all(asList(1, 2, 3, 4), item -> item < 5);
        final boolean result2obj = new Underscore<>(asList(1, 2, 3, 4)).all(item -> item < 5);
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
    void any() {
        final boolean result1 = Underscore.any(asList(1, 2, 3, 4), item -> item % 2 == 0);
        final boolean result1obj = new Underscore<>(asList(1, 2, 3, 4)).any(item -> item % 2 == 0);
        final boolean result2 = Underscore.any(asList(1, 2, 3, 4), item -> item == 5);
        final boolean result2obj = new Underscore<>(asList(1, 2, 3, 4)).any(item -> item == 5);
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
    void some() {
        final boolean result1 = Underscore.some(asList(1, 2, 3, 4), item -> item % 2 == 0);
        final boolean result1obj = new Underscore<>(asList(1, 2, 3, 4)).some(item -> item % 2 == 0);
        final boolean result1chain =
                Underscore.chain(asList(1, 2, 3, 4)).some(item -> item % 2 == 0).item();
        final boolean result2 = Underscore.some(asList(1, 2, 3, 4), item -> item == 5);
        final boolean result2obj = new Underscore<>(asList(1, 2, 3, 4)).some(item -> item == 5);
        final boolean result2chain =
                Underscore.chain(asList(1, 2, 3, 4)).some(item -> item == 5).item();
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
    void include() {
        final Boolean result = Underscore.include(asList(1, 2, 3), 3);
        assertTrue(result);
    }

    /*
    _.count([1, 2, 3, 4], function(num) { return num % 2 === 0; }); // 2
    _.count([1, 2, 3, 4], function(num) { return num < 5; }); // 4
    */
    @Test
    @SuppressWarnings("unchecked")
    void count() {
        final int result1 = Underscore.count(asList(1, 2, 3, 4), item -> item % 2 == 0);
        final int result1obj = new Underscore<>(asList(1, 2, 3, 4)).count(item -> item % 2 == 0);
        final int result1chain =
                Underscore.chain(asList(1, 2, 3, 4)).count(item -> item % 2 == 0).item();
        final int result2 = Underscore.count(asList(1, 2, 3, 4), item -> item < 5);
        final int result2obj = new Underscore<>(asList(1, 2, 3, 4)).count(item -> item < 5);
        final int result2chain =
                Underscore.chain(asList(1, 2, 3, 4)).count(item -> item < 5).item();
        assertEquals(2, result1);
        assertEquals(2, result1obj);
        assertEquals(2, result1chain);
        assertEquals(4, result2);
        assertEquals(4, result2obj);
        assertEquals(4, result2chain);
    }

    /*
    _.contains([1, 2, 3], 3);
    => true
    */
    @Test
    void contains() {
        final boolean result = Underscore.contains(asList(1, 2, 3), 3);
        assertTrue(result);
        final boolean resultObj = new Underscore<>(asList(1, 2, 3)).contains(3);
        assertTrue(resultObj);
        final boolean resultChain = Underscore.chain(asList(1, 2, 3)).contains(3).item();
        assertTrue(resultChain);
        final boolean result2 = Underscore.contains(asList(1, 2, 3), 3, 1);
        assertTrue(result2);
        final boolean result3 = Underscore.contains(asList(1, 2, 3), 1, 1);
        assertFalse(result3);
        final boolean result4 = Underscore.contains(asList(1, 2, null), null);
        assertTrue(result4);
    }

    /*
    _.containsWith(["abc", "bcd", "cde"], "bc");
    => true
    */
    @Test
    void containsWith() {
        final boolean result = Underscore.containsWith(asList(1, 2, 3), 3);
        assertTrue(result);
        final boolean resultObj = new Underscore<>(asList(1, 2, 3)).containsWith(3);
        assertTrue(resultObj);
        final boolean resultChain = Underscore.chain(asList(1, 2, 3)).containsWith(3).item();
        assertTrue(resultChain);
        final boolean result2 = Underscore.containsWith(asList(1, 2, null), null);
        assertTrue(result2);
        final boolean result3 = Underscore.containsWith(asList("abc", "bcd", "cde"), "bc");
        assertTrue(result3);
    }

    @Test
    void containsAtLeast() {
        final boolean result = Underscore.containsAtLeast(asList(1, 2, 2), 2, 2);
        assertTrue(result);
        final boolean result2 = Underscore.containsAtLeast(asList(1, 2, 2), 2, 3);
        assertFalse(result2);
        final boolean result3 = new Underscore<>(asList(1, 2, 2)).containsAtLeast(2, 2);
        assertTrue(result3);
        final boolean result4 = new Underscore<>(asList(1, 2, 2)).containsAtLeast(2, 3);
        assertFalse(result4);
        final boolean result5 = Underscore.containsAtLeast(asList(null, null, 2), null, 2);
        assertTrue(result5);
        final boolean result6 = Underscore.containsAtLeast(asList(null, null, 2), 2, 1);
        assertTrue(result6);
    }

    @Test
    void containsAtMost() {
        final boolean result = Underscore.containsAtMost(asList(1, 2, 2), 2, 3);
        assertTrue(result);
        final boolean result2 = Underscore.containsAtMost(asList(1, 2, 2), 2, 1);
        assertFalse(result2);
        final boolean result3 = new Underscore<>(asList(1, 2, 2)).containsAtMost(3, 2);
        assertTrue(result3);
        final boolean result4 = new Underscore<>(asList(1, 2, 2)).containsAtMost(2, 1);
        assertFalse(result4);
        final boolean result5 = Underscore.containsAtMost(asList(null, null, 2), null, 2);
        assertTrue(result5);
        final boolean result6 = Underscore.containsAtMost(asList(null, null, 2), 2, 1);
        assertTrue(result6);
    }

    /*
    _.invoke([" foo ", "  bar"], "trim"); // ["foo", "bar"]
    */
    @Test
    @SuppressWarnings("unchecked")
    void invoke() {
        assertEquals(asList("foo", "bar"), Underscore.invoke(asList(" foo ", "  bar"), "trim"));
        assertEquals(
                asList("foo", "bar"), new Underscore<>(asList(" foo ", "  bar")).invoke("trim"));
        assertEquals(
                asList("foo", "bar"),
                Underscore.chain(asList(" foo ", "  bar")).invoke("trim").value());
        assertEquals(
                asList("foo1", "bar1"),
                Underscore.invoke(asList("foo", "bar"), "concat", Collections.singletonList("1")));
        assertEquals(
                asList("foo1", "bar1"),
                new Underscore<>(asList("foo", "bar"))
                        .invoke("concat", Collections.singletonList("1")));
        assertEquals(
                asList("foo1", "bar1"),
                Underscore.chain(asList("foo", "bar"))
                        .invoke("concat", Collections.singletonList("1"))
                        .value());
        assertEquals(
                asList("[1, 5, 7]", "[1, 2, 3]").toString(),
                Underscore.invoke(
                                asList(
                                        Underscore.chain(asList(5, 1, 7)),
                                        Underscore.chain(asList(3, 2, 1))),
                                "sort")
                        .toString());
    }

    @Test
    @SuppressWarnings("unchecked")
    void invokeError() {
        List<? extends Serializable> iterable = asList("foo", 123);
        List<Object> objects = Collections.singletonList("1");
        assertThrows(
                IllegalArgumentException.class,
                () -> Underscore.invoke(iterable, "concat", objects));
    }

    @Test
    void invokeError2() {
        List<String> iterable = asList(" foo ", "  bar");
        assertThrows(IllegalArgumentException.class, () -> Underscore.invoke(iterable, "trim2"));
    }

    /*
    var stooges = [{name: 'moe', age: 40}, {name: 'larry', age: 50}, {name: 'curly', age: 60}];
    _.pluck(stooges, 'name');
    => ["moe", "larry", "curly"]
    */
    @Test
    @SuppressWarnings("unchecked")
    void pluck() {
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
        assertEquals("[]", Underscore.pluck(Collections.emptyList(), "name").toString());
        final List<?> result =
                Underscore.pluck(
                        asList(
                                new Person("moe", 40),
                                new Person("larry", 50),
                                new Person("curly", 40)),
                        "name");
        assertEquals("[moe, larry, curly]", result.toString());
        final List<?> result2 =
                Underscore.pluck(
                        asList(
                                new Person2("moe", 40),
                                new Person2("larry", 50),
                                new Person2("curly", 40)),
                        "getName");
        assertEquals("[moe, larry, curly]", result2.toString());
        final List<?> resultObj =
                new Underscore<>(
                                asList(
                                        new Person("moe", 40),
                                        new Person("larry", 50),
                                        new Person("curly", 40)))
                        .pluck("name");
        assertEquals("[moe, larry, curly]", resultObj.toString());
        final List<Object> resultChain =
                Underscore.chain(
                                asList(
                                        new Person("moe", 40),
                                        new Person("larry", 50),
                                        new Person("curly", 40)))
                        .pluck("name")
                        .value();
        assertEquals("[moe, larry, curly]", resultChain.toString());
        final Set<?> resultEmpty2 =
                Underscore.pluck(new LinkedHashSet<>(Collections.emptyList()), "name");
        assertEquals("[]", resultEmpty2.toString());
        final Set<?> resultSet =
                Underscore.pluck(
                        new LinkedHashSet<>(
                                asList(
                                        new Person("moe", 40),
                                        new Person("larry", 50),
                                        new Person("curly", 40))),
                        "name");
        assertEquals("[moe, larry, curly]", resultSet.toString());
        final Set<?> resultSet2 =
                Underscore.pluck(
                        new LinkedHashSet<>(
                                asList(
                                        new Person2("moe", 40),
                                        new Person2("larry", 50),
                                        new Person2("curly", 40))),
                        "getName");
        assertEquals("[moe, larry, curly]", resultSet2.toString());
    }

    @Test
    void pluck2() {
        class Person {
            public final String name;
            public final Integer age;

            public Person(final String name, final Integer age) {
                this.name = name;
                this.age = age;
            }
        }
        List<Person> personList =
                asList(new Person("moe", 40), new Person("larry", 50), new Person("curly", 40));
        assertThrows(IllegalArgumentException.class, () -> Underscore.pluck(personList, "name2"));
    }

    @Test
    @SuppressWarnings("unchecked")
    void pluck3() {
        class Person {
            public final String name;
            public final Integer age;

            public Person(final String name, final Integer age) {
                this.name = name;
                this.age = age;
            }
        }
        List<Person> personList =
                asList(new Person("moe", 40), new Person("larry", 50), new Person("curly", 40));
        LinkedHashSet<Person> personSet = new LinkedHashSet<>(personList);
        assertThrows(IllegalArgumentException.class, () -> Underscore.pluck(personSet, "name2"));
    }

    /*
    _.where(listOfPlays, {author: "Shakespeare", year: 1611});
    => [{title: "Cymbeline", author: "Shakespeare", year: 1611},
        {title: "The Tempest", author: "Shakespeare", year: 1611}]
    */
    @Test
    @SuppressWarnings("unchecked")
    void where() {
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
                new ArrayList<>() {
                    {
                        add(new Book("Cymbeline2", "Shakespeare", 1614));
                        add(new Book("Cymbeline", "Shakespeare", 1611));
                        add(new Book("The Tempest", "Shakespeare", 1611));
                    }
                };
        assertEquals(
                "[title: Cymbeline, author: Shakespeare, year: 1611,"
                        + " title: The Tempest, author: Shakespeare, year: 1611]",
                Underscore.where(
                                listOfPlays,
                                asList(
                                        Map.entry("author", "Shakespeare"),
                                        Map.<String, Object>entry("year", 1611)))
                        .toString());
        assertEquals(
                "[title: Cymbeline, author: Shakespeare, year: 1611,"
                        + " title: The Tempest, author: Shakespeare, year: 1611]",
                Underscore.where(
                                listOfPlays,
                                asList(
                                        Map.entry("author", "Shakespeare"),
                                        Map.entry("author2", "Shakespeare"),
                                        Map.<String, Object>entry("year", 1611)))
                        .toString());
        assertEquals(
                "[title: Cymbeline, author: Shakespeare, year: 1611,"
                        + " title: The Tempest, author: Shakespeare, year: 1611]",
                Underscore.where(
                                new LinkedHashSet<>(listOfPlays),
                                asList(
                                        Map.entry("author", "Shakespeare"),
                                        Map.<String, Object>entry("year", 1611)))
                        .toString());
        assertEquals(
                "[title: Cymbeline, author: Shakespeare, year: 1611,"
                        + " title: The Tempest, author: Shakespeare, year: 1611]",
                Underscore.where(
                                new LinkedHashSet<>(listOfPlays),
                                asList(
                                        Map.entry("author", "Shakespeare"),
                                        Map.entry("author2", "Shakespeare"),
                                        Map.<String, Object>entry("year", 1611)))
                        .toString());
        assertEquals(
                "[title: Cymbeline, author: Shakespeare, year: 1611,"
                        + " title: The Tempest, author: Shakespeare, year: 1611]",
                new Underscore<>(listOfPlays)
                        .where(
                                asList(
                                        Map.entry("author", "Shakespeare"),
                                        Map.<String, Object>entry("year", 1611)))
                        .toString());
        assertEquals(
                "[title: Cymbeline, author: Shakespeare, year: 1611,"
                        + " title: The Tempest, author: Shakespeare, year: 1611]",
                Underscore.chain(listOfPlays)
                        .where(
                                asList(
                                        Map.entry("author", "Shakespeare"),
                                        Map.<String, Object>entry("year", 1611)))
                        .value()
                        .toString());
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
                new ArrayList<>() {
                    {
                        add(new Book2("Cymbeline2", "Shakespeare", 1614));
                        add(new Book2("Cymbeline", "Shakespeare", 1611));
                        add(new Book2("The Tempest", "Shakespeare", 1611));
                    }
                };
        assertEquals(
                "[title: Cymbeline, author: Shakespeare, year: 1611,"
                        + " title: The Tempest, author: Shakespeare, year: 1611]",
                Underscore.where(
                                listOfPlays2,
                                asList(
                                        Map.<String, Object>entry("getAuthor", "Shakespeare"),
                                        Map.<String, Object>entry("author2", "Shakespeare"),
                                        Map.<String, Object>entry("year", 1611)))
                        .toString());
    }

    /*
    _.findWhere(listOfPlays, {author: "Shakespeare", year: 1611})
    => {title: "Cymbeline", author: "Shakespeare", year: 1611}
    */
    @Test
    @SuppressWarnings("unchecked")
    void findWhere() {
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
                new ArrayList<Book>() {
                    {
                        add(new Book("Cymbeline2", "Shakespeare", 1614));
                        add(new Book("Cymbeline", "Shakespeare", 1611));
                        add(new Book("The Tempest", "Shakespeare", 1611));
                    }
                };
        assertEquals(
                "title: Cymbeline, author: Shakespeare, year: 1611",
                Underscore.findWhere(
                                listOfPlays,
                                asList(
                                        Map.<String, Object>entry("author", "Shakespeare"),
                                        Map.<String, Object>entry("year", 1611)))
                        .get()
                        .toString());
        assertEquals(
                "title: Cymbeline, author: Shakespeare, year: 1611",
                new Underscore<>(listOfPlays)
                        .findWhere(
                                asList(
                                        Map.<String, Object>entry("author", "Shakespeare"),
                                        Map.<String, Object>entry("year", 1611)))
                        .get()
                        .toString());
        assertEquals(
                "title: Cymbeline, author: Shakespeare, year: 1611",
                (Underscore.chain(listOfPlays)
                                .findWhere(
                                        asList(
                                                Map.<String, Object>entry("author", "Shakespeare"),
                                                Map.<String, Object>entry("year", 1611)))
                                .item())
                        .get()
                        .toString());
        assertEquals(
                "title: Cymbeline, author: Shakespeare, year: 1611",
                Underscore.findWhere(
                                listOfPlays,
                                asList(
                                        Map.<String, Object>entry("author", "Shakespeare"),
                                        Map.<String, Object>entry("author2", "Shakespeare"),
                                        Map.<String, Object>entry("year", 1611)))
                        .get()
                        .toString());
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
                new ArrayList<Book2>() {
                    {
                        add(new Book2("Cymbeline2", "Shakespeare", 1614));
                        add(new Book2("Cymbeline", "Shakespeare", 1611));
                        add(new Book2("The Tempest", "Shakespeare", 1611));
                    }
                };
        assertEquals(
                "title: Cymbeline, author: Shakespeare, year: 1611",
                Underscore.findWhere(
                                listOfPlays2,
                                asList(
                                        Map.<String, Object>entry("getAuthor", "Shakespeare"),
                                        Map.<String, Object>entry("year", 1611)))
                        .get()
                        .toString());
        assertEquals(
                Optional.<Book2>empty(),
                Underscore.findWhere(
                        listOfPlays2,
                        asList(
                                Map.<String, Object>entry("getAuthor", "Shakespeare2"),
                                Map.<String, Object>entry("year", 1611))));
    }

    /*
    var numbers = [10, 5, 100, 2, 1000];
    _.max(numbers);
    => 1000
    */
    @Test
    void max() {
        final Integer result = Underscore.max(asList(10, 5, 100, 2, 1000));
        assertEquals("1000", result.toString());
        final Integer resultObj = new Underscore<>(asList(10, 5, 100, 2, 1000)).max();
        assertEquals("1000", resultObj.toString());
        final Integer resultChain =
                (Integer) Underscore.chain(asList(10, 5, 100, 2, 1000)).max().item();
        assertEquals("1000", resultChain.toString());
        final Integer resultComp = Underscore.max(asList(10, 5, 100, 2, 1000), item -> -item);
        assertEquals("2", resultComp.toString());
        final Integer resultCompObj =
                new Underscore<>(asList(10, 5, 100, 2, 1000)).max(item -> -item);
        assertEquals("2", resultCompObj.toString());
        final Integer resultCompChain =
                Underscore.chain(asList(10, 5, 100, 2, 1000)).max(item -> -item).item();
        assertEquals("2", resultCompChain.toString());
        class Person {
            public final String name;
            public final Integer age;

            public Person(final String name, final Integer age) {
                this.name = name;
                this.age = age;
            }
        }
        final Person resultPerson =
                Underscore.max(
                        asList(
                                new Person("moe", 40),
                                new Person("larry", 50),
                                new Person("curly", 60)),
                        item -> item.age);
        assertEquals("curly", resultPerson.name);
        assertEquals(60, resultPerson.age.intValue());
    }

    /*
    var numbers = [10, 5, 100, 2, 1000];
    _.min(numbers);
    => 2
    */
    @Test
    void min() {
        final Integer result = Underscore.min(asList(10, 5, 100, 2, 1000));
        assertEquals("2", result.toString());
        final Integer resultObj = new Underscore<>(asList(10, 5, 100, 2, 1000)).min();
        assertEquals("2", resultObj.toString());
        final Integer resultChain =
                (Integer) Underscore.chain(asList(10, 5, 100, 2, 1000)).min().item();
        assertEquals("2", resultChain.toString());
        final Integer resultComp = Underscore.min(asList(10, 5, 100, 2, 1000), item -> -item);
        assertEquals("1000", resultComp.toString());
        final Integer resultCompObj =
                new Underscore<>(asList(10, 5, 100, 2, 1000)).min(item -> -item);
        assertEquals("1000", resultCompObj.toString());
        final Integer resultCompChain =
                Underscore.chain(asList(10, 5, 100, 2, 1000)).min(item -> -item).item();
        assertEquals("1000", resultCompChain.toString());
        class Person {
            public final String name;
            public final Integer age;

            public Person(final String name, final Integer age) {
                this.name = name;
                this.age = age;
            }
        }
        final Person resultPerson =
                Underscore.min(
                        asList(
                                new Person("moe", 40),
                                new Person("larry", 50),
                                new Person("curly", 60)),
                        item -> item.age);
        assertEquals("moe", resultPerson.name);
        assertEquals(40, resultPerson.age.intValue());
    }

    /*
    _.sortWith([1, 2, 3, 4, 5, 6], function(num1, num2){ return Math.sin(num1) - Math.sin(num2); });
    => [5, 4, 6, 3, 1, 2]
    */
    @Test
    @SuppressWarnings("unchecked")
    void sortWith() {
        final List<Integer> result =
                Underscore.sortWith(
                        asList(1, 2, 3, 4, 5, 6),
                        (item1, item2) ->
                                (int) (Math.sin(item1) * 1000) - (int) (Math.sin(item2) * 1000));
        assertEquals("[5, 4, 6, 3, 1, 2]", result.toString());
        final List<Integer> resultObj =
                new Underscore<>(asList(1, 2, 3, 4, 5, 6))
                        .sortWith(
                                (item1, item2) ->
                                        (int) (Math.sin(item1) * 1000)
                                                - (int) (Math.sin(item2) * 1000));
        assertEquals("[5, 4, 6, 3, 1, 2]", resultObj.toString());
        final List<Integer> resultChain =
                Underscore.chain(asList(1, 2, 3, 4, 5, 6))
                        .sortWith(
                                (Comparator<Integer>)
                                        (item1, item2) ->
                                                (int) (Math.sin(item1) * 1000)
                                                        - (int) (Math.sin(item2) * 1000))
                        .value();
        assertEquals("[5, 4, 6, 3, 1, 2]", resultChain.toString());
    }

    /*
    _.sortBy([1, 2, 3, 4, 5, 6], function(num){ return Math.sin(num); });
    => [5, 4, 6, 3, 1, 2]
    */
    @Test
    @SuppressWarnings("unchecked")
    void sortBy() {
        final List<Integer> result =
                Underscore.sortBy(asList(1, 2, 3, 4, 5, 6), item -> (int) (Math.sin(item) * 1000));
        assertEquals("[5, 4, 6, 3, 1, 2]", result.toString());
        final List<Integer> resultObj =
                new Underscore<>(asList(1, 2, 3, 4, 5, 6))
                        .sortBy(item -> (int) (Math.sin(item) * 1000));
        assertEquals("[5, 4, 6, 3, 1, 2]", resultObj.toString());
        final List<Integer> resultChain =
                Underscore.chain(asList(1, 2, 3, 4, 5, 6))
                        .sortBy(item -> (int) (Math.sin(item) * 1000))
                        .value();
        assertEquals("[5, 4, 6, 3, 1, 2]", resultChain.toString());
    }

    /*
    var stooges = [{name: 'moe', age: 40}, {name: 'larry', age: 50}, {name: 'curly', age: 60}];
    _.sortBy(stooges, 'name');
    => [{name: 'curly', age: 60}, {name: 'larry', age: 50}, {name: 'moe', age: 40}];
    */
    @Test
    @SuppressWarnings("unchecked")
    void sortByMap() {
        final List<Map<String, Comparable>> result =
                Underscore.sortBy(
                        asList(
                                new LinkedHashMap<>() {
                                    {
                                        put("name", "moe");
                                        put("age", 40);
                                    }
                                },
                                new LinkedHashMap<>() {
                                    {
                                        put("name", "larry");
                                        put("age", 50);
                                    }
                                },
                                (Map<String, Comparable>)
                                        new LinkedHashMap<String, Comparable>() {
                                            {
                                                put("name", "curly");
                                                put("age", 60);
                                            }
                                        }),
                        "name");
        assertEquals(
                "[{name=curly, age=60}, {name=larry, age=50}, {name=moe, age=40}]",
                result.toString());
        final List<Map<String, Comparable>> resultChain =
                Underscore.chain(
                                asList(
                                        new LinkedHashMap<String, Comparable>() {
                                            {
                                                put("name", "moe");
                                                put("age", 40);
                                            }
                                        },
                                        new LinkedHashMap<String, Comparable>() {
                                            {
                                                put("name", "larry");
                                                put("age", 50);
                                            }
                                        },
                                        (Map<String, Comparable>)
                                                new LinkedHashMap<String, Comparable>() {
                                                    {
                                                        put("name", "curly");
                                                        put("age", 60);
                                                    }
                                                }))
                        .sortBy("name")
                        .value();
        assertEquals(
                "[{name=curly, age=60}, {name=larry, age=50}, {name=moe, age=40}]",
                resultChain.toString());
    }

    /*
    _.groupBy([1.3, 2.1, 2.4], function(num){ return Math.floor(num); });
    => {1: [1.3], 2: [2.1, 2.4]}
    */
    @Test
    @SuppressWarnings("unchecked")
    void groupBy() {
        final Map<Double, List<Double>> result =
                Underscore.groupBy(asList(1.3, 2.1, 2.4), Math::floor);
        assertEquals("{1.0=[1.3], 2.0=[2.1, 2.4]}", result.toString());
        final Map<Double, List<Double>> resultObj =
                new Underscore<>(asList(1.3, 2.1, 2.4)).groupBy(Math::floor);
        assertEquals("{1.0=[1.3], 2.0=[2.1, 2.4]}", resultObj.toString());
        final Map<Double, List<Double>> resultChain =
                Underscore.chain(asList(1.3, 2.1, 2.4)).groupBy(Math::floor).item();
        assertEquals("{1.0=[1.3], 2.0=[2.1, 2.4]}", resultChain.toString());
    }

    /*
    _.associateBy([1.3, 2.1, 2.4], function(num){ return Math.floor(num); });
    => {1: [1.3], 2: [2.1, 2.4]}
    */
    @Test
    @SuppressWarnings("unchecked")
    void associateBy() {
        final Map<Double, Double> result =
                Underscore.associateBy(asList(1.3, 2.1, 2.4), Math::floor);
        assertEquals("{1.0=1.3, 2.0=2.1}", result.toString());
        final Map<Double, Double> resultObj =
                new Underscore<>(asList(1.3, 2.1, 2.4)).associateBy(Math::floor);
        assertEquals("{1.0=1.3, 2.0=2.1}", resultObj.toString());
        final Map<Double, Double> resultChain =
                Underscore.chain(asList(1.3, 2.1, 2.4)).associateBy(Math::floor).item();
        assertEquals("{1.0=1.3, 2.0=2.1}", resultChain.toString());
    }

    /*
    _.groupBy([1.3, 2.1, 2.4], function(num){ return Math.floor(num); });
    => {1: [1.3], 2: [2.1, 2.4]}
    */
    @Test
    @SuppressWarnings("unchecked")
    void groupByWithSumming() {
        final Map<Double, Optional<Double>> result =
                Underscore.groupBy(asList(1.3, 2.1, 2.4), Math::floor, Double::sum);
        assertEquals("{1.0=Optional[1.3], 2.0=Optional[4.5]}", result.toString());
        final Map<Double, Optional<Double>> resultObj =
                new Underscore<>(asList(1.3, 2.1, 2.4)).groupBy(Math::floor, Double::sum);
        assertEquals("{1.0=Optional[1.3], 2.0=Optional[4.5]}", resultObj.toString());
        final Map<Double, Optional<Double>> resultChain =
                Underscore.chain(asList(1.3, 2.1, 2.4)).groupBy(Math::floor, Double::sum).item();
        assertEquals("{1.0=Optional[1.3], 2.0=Optional[4.5]}", resultChain.toString());
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
    void indexBy() {
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
                Underscore.indexBy(
                        asList(
                                new Person("moe", 40),
                                new Person("larry", 50),
                                new Person("curly", 60)),
                        "age");
        assertEquals("{40=[moe, 40], 50=[larry, 50], 60=[curly, 60]}", result.toString());
        final Map<String, List<Person>> resultObj =
                new Underscore<>(
                                asList(
                                        new Person("moe", 40),
                                        new Person("larry", 50),
                                        new Person("curly", 60)))
                        .indexBy("age");
        assertEquals("{40=[moe, 40], 50=[larry, 50], 60=[curly, 60]}", resultObj.toString());
        final Map<Object, List<Person>> resultChain =
                Underscore.chain(
                                asList(
                                        new Person("moe", 40),
                                        new Person("larry", 50),
                                        new Person("curly", 60)))
                        .indexBy("age")
                        .item();
        assertEquals("{40=[moe, 40], 50=[larry, 50], 60=[curly, 60]}", resultChain.toString());
        final Map<String, List<Person>> result2 =
                Underscore.indexBy(
                        asList(
                                new Person("moe", 40),
                                new Person("larry", 50),
                                new Person("curly", 60)),
                        "age2");
        assertEquals("{null=[moe, 40, larry, 50, curly, 60]}", result2.toString());
    }

    /*
    var stooges = [{name: 'moe', age: 40}, {name: 'moe', age: 50}, {name: 'curly', age: 60}];
    _.countBy(stooges, 'age');
    => {moe: 2, curly: 1}
    */
    @Test
    @SuppressWarnings("unchecked")
    void countBy() {
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
                Underscore.countBy(
                        asList(
                                new Person("moe", 40),
                                new Person("moe", 50),
                                new Person("curly", 60)),
                        person -> person.name);
        assertEquals("{moe=2, curly=1}", result.toString());
        final Map<String, Integer> resultObj =
                new Underscore<>(
                                asList(
                                        new Person("moe", 40),
                                        new Person("moe", 50),
                                        new Person("curly", 60)))
                        .countBy((Function<Person, String>) person -> person.name);
        assertEquals("{moe=2, curly=1}", resultObj.toString());
        final Map<String, Integer> resultChain =
                Underscore.chain(
                                asList(
                                        new Person("moe", 40),
                                        new Person("moe", 50),
                                        new Person("curly", 60)))
                        .countBy(person -> person.name)
                        .item();
        assertEquals("{moe=2, curly=1}", resultChain.toString());
        Underscore.countBy(asList(1, 2, 3));
        new Underscore<>(asList(1, 2, 3)).countBy();
        Underscore.chain(asList(1, 2, 2, 3)).countBy().item();
    }

    /*
    _.shuffle([1, 2, 3, 4, 5, 6]);
    => [4, 1, 6, 3, 5, 2]
    */
    @Test
    @SuppressWarnings("unchecked")
    void shuffle() {
        final List<Integer> result = Underscore.shuffle(asList(1, 2, 3, 4, 5, 6));
        assertEquals(6, result.size());
        final List<Integer> resultObj = new Underscore<>(asList(1, 2, 3, 4, 5, 6)).shuffle();
        assertEquals(6, resultObj.size());
        final List<Integer> resultChain =
                Underscore.chain(asList(1, 2, 3, 4, 5, 6)).shuffle().value();
        assertEquals(6, resultChain.size());
    }

    /*
    _.sample([1, 2, 3, 4, 5, 6]);
    => 4
    _.sample([1, 2, 3, 4, 5, 6], 3);
    => [1, 6, 2]
    */
    @Test
    void sample() {
        final Integer result = Underscore.sample(asList(1, 2, 3, 4, 5, 6));
        assertTrue(result >= 1 && result <= 6);
        final Integer resultObj = new Underscore<>(asList(1, 2, 3, 4, 5, 6)).sample();
        assertTrue(resultObj >= 1 && resultObj <= 6);
        final Set<Integer> resultList = Underscore.sample(asList(1, 2, 3, 4, 5, 6), 3);
        assertEquals(3, resultList.size());
        final Integer resultChain = Underscore.chain(asList(1, 2, 3, 4, 5, 6)).sample().item();
        assertTrue(resultChain >= 1 && resultChain <= 6);
        final List<Integer> resultListChain =
                Underscore.chain(asList(1, 2, 3, 4, 5, 6)).sample(3).value();
        assertEquals(3, resultListChain.size());
    }

    /*
    (function(){ return _.toArray(arguments).slice(1); })(1, 2, 3, 4);
    => [2, 3, 4]
    */
    @Test
    @SuppressWarnings("unchecked")
    void toArray() {
        final Object[] result = Underscore.<Integer>toArray(asList(1, 2, 3, 4));
        assertEquals("1", result[0].toString());
        final Object[] resultObj = new Underscore<>(asList(1, 2, 3, 4)).toArray();
        assertEquals("1", resultObj[0].toString());
    }

    @Test
    @SuppressWarnings("unchecked")
    void toMap() {
        assertEquals(
                "{name1=one, name2=two}",
                Underscore.toMap(
                                (new LinkedHashMap<String, String>() {
                                            {
                                                put("name1", "one");
                                                put("name2", "two");
                                            }
                                        })
                                        .entrySet())
                        .toString());
        assertEquals(
                "{name1=one, name2=two}",
                new Underscore<>(
                                (new LinkedHashMap<String, String>() {
                                            {
                                                put("name1", "one");
                                                put("name2", "two");
                                            }
                                        })
                                        .entrySet())
                        .toMap()
                        .toString());
        assertEquals(
                "{name1=one, name2=two}",
                Underscore.toMap(
                                new ArrayList<Map.Entry<String, String>>() {
                                    {
                                        add(Map.entry("name1", "one"));
                                        add(Map.entry("name2", "two"));
                                    }
                                })
                        .toString());
    }

    @Test
    void toCardinalityMap() {
        assertEquals(
                "{a=2, b=1, c=2}",
                Underscore.toCardinalityMap(asList("a", "a", "b", "c", "c")).toString());
        assertEquals("{}", Underscore.toCardinalityMap(new ArrayList<>()).toString());
        assertEquals(
                "{a=2, b=1, c=2}",
                new Underscore<>(asList("a", "a", "b", "c", "c")).toCardinalityMap().toString());
        assertEquals("{}", new Underscore<>(new ArrayList<>()).toCardinalityMap().toString());
    }

    /*
    _.size({one: 1, two: 2, three: 3});
    => 3
    */
    @Test
    @SuppressWarnings("unchecked")
    void size() {
        final int result = Underscore.size(asList(1, 2, 3, 4));
        assertEquals(4, result);
        final int resultObj = new Underscore<>(asList(1, 2, 3, 4)).size();
        assertEquals(4, resultObj);
        final int resultChain = Underscore.chain(asList(1, 2, 3, 4)).size();
        assertEquals(4, resultChain);
        final int[] array = new int[] {1, 2, 3, 4, 5, 6};
        Iterable<Integer> iterable =
                () ->
                        new Iterator<>() {
                            private int index;

                            public boolean hasNext() {
                                return array.length > index;
                            }

                            public Integer next() {
                                return array[index++];
                            }

                            @Override
                            public void remove() {
                            }
                        };
        assertEquals(6, Underscore.size(iterable));
        assertEquals(5, Underscore.size(new Integer[] {5, 4, 3, 2, 1}));
        assertEquals(5, Underscore.size(5, 4, 3, 2, 1));
    }

    /*
    _.partition([0, 1, 2, 3, 4, 5], isOdd);
    => [[1, 3, 5], [0, 2, 4]]
    */
    @Test
    void partition() {
        final List<List<Integer>> result =
                Underscore.partition(asList(0, 1, 2, 3, 4, 5), item -> item % 2 == 1);
        assertEquals("[1, 3, 5]", result.get(0).toString());
        assertEquals("[0, 2, 4]", result.get(1).toString());
        final List<Integer>[] resultArray =
                Underscore.partition(new Integer[] {0, 1, 2, 3, 4, 5}, item -> item % 2 == 1);
        assertEquals("[1, 3, 5]", resultArray[0].toString());
        assertEquals("[0, 2, 4]", resultArray[1].toString());
    }
}
