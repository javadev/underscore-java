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
package com.github.underscore.math;

import com.github.underscore.BinaryOperator;
import com.github.underscore.BiFunction;
import com.github.underscore.Consumer;
import com.github.underscore.Function;
import com.github.underscore.Predicate;
import com.github.underscore.PredicateIndexed;
import com.github.underscore.Tuple;
import java.util.*;
import org.junit.Test;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

/**
 * Underscore library unit test.
 *
 * @author Valentyn Kolesnikov
 */
public class MathTest {

    @SuppressWarnings("unchecked")
    @Test
    public void createPermutationWithRepetition() {
        List<List<String>> result = U.createPermutationWithRepetition(asList("apple", "orange"), 3);
        assertEquals("[[apple, apple, apple],"
                   + " [orange, apple, apple],"
                   + " [apple, orange, apple],"
                   + " [orange, orange, apple],"
                   + " [apple, apple, orange],"
                   + " [orange, apple, orange],"
                   + " [apple, orange, orange],"
                   + " [orange, orange, orange]]", result.toString());
        List<List<String>> result2 = new U(asList("apple", "orange")).createPermutationWithRepetition(3);
        assertEquals("[[apple, apple, apple],"
                   + " [orange, apple, apple],"
                   + " [apple, orange, apple],"
                   + " [orange, orange, apple],"
                   + " [apple, apple, orange],"
                   + " [orange, apple, orange],"
                   + " [apple, orange, orange],"
                   + " [orange, orange, orange]]", result2.toString());
        List<List<String>> resultChain = U.chain(asList("apple", "orange")).createPermutationWithRepetition(3).value();
        assertEquals("[[apple, apple, apple],"
                   + " [orange, apple, apple],"
                   + " [apple, orange, apple],"
                   + " [orange, orange, apple],"
                   + " [apple, apple, orange],"
                   + " [orange, apple, orange],"
                   + " [apple, orange, orange],"
                   + " [orange, orange, orange]]", resultChain.toString());
    }

    @Test
    public void findByName() {
        File file = new File("name", null, 0L);
        assertEquals(1, U.findByName(file, "name").size());
        assertEquals(0, U.findByName(file, "name1").size());
        Directory directory = new Directory("name", null);
        directory.addEntry(file);
        assertEquals(1, U.findByName(directory, "name").size());
        Directory directory2 = new Directory("name", null);
        directory2.addEntry(file);
        directory.addEntry(directory2);
        assertEquals(2, U.findByName(directory, "name").size());
        assertEquals(0, U.findByName(directory, "name1").size());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void chain() {
        new U("");
        new U(asList()).chain();
        U.chain(new ArrayList<String>(), 1);
        U.chain(new String[] {});
        U.chain(new String[] {""}).first();
        U.chain(new String[] {""}).first(1);
        U.chain(new String[] {""}).firstOrNull();
        U.chain(new String[] {""}).firstOrNull(new Predicate<String>() {
            public boolean test(String str) { return true; } });
        U.chain(new String[] {""}).initial();
        U.chain(new String[] {""}).initial(1);
        U.chain(new String[] {""}).last();
        U.chain(new String[] {""}).last(1);
        U.chain(new String[] {""}).lastOrNull();
        U.chain(new String[] {""}).lastOrNull(new Predicate<String>() {
            public boolean test(String str) { return true; } });
        U.chain(new String[] {""}).rest();
        U.chain(new String[] {""}).rest(1);
        U.chain(new String[] {""}).compact();
        U.chain(new String[] {""}).compact("1");
        U.chain(new String[] {""}).flatten();
        U.chain(new Integer[] {0}).map(new Function<Integer, Integer>() {
            public Integer apply(Integer value) { return value; } });
        U.chain(new Integer[] {0}).mapIndexed(new BiFunction<Integer, Integer, Integer>() {
            public Integer apply(Integer index, Integer value) { return value; } });
        U.chain(new String[] {""}).filter(new Predicate<String>() {
            public boolean test(String str) { return true; } });
        U.chain(new String[] {""}).filterIndexed(new PredicateIndexed<String>() {
            public boolean test(int index, String str) { return true; } });
        U.chain(new String[] {""}).reject(new Predicate<String>() {
            public boolean test(String str) { return true; } });
        U.chain(new String[] {""}).rejectIndexed(new PredicateIndexed<String>() {
            public boolean test(int index, String str) { return true; } });
        U.chain(new String[] {""}).filterFalse(new Predicate<String>() {
            public boolean test(String str) { return true; } });
        U.chain(new String[] {""}).reduce(new BiFunction<String, String, String>() {
            public String apply(String accum, String str) { return null; } }, "");
        U.chain(new String[] {""}).reduce(new BinaryOperator<String>() {
            public String apply(String accum, String str) { return null; } });
        U.chain(new String[] {""}).reduceRight(new BiFunction<String, String, String>() {
            public String apply(String accum, String str) { return null; } }, "");
        U.chain(new String[] {""}).reduceRight(new BinaryOperator<String>() {
            public String apply(String accum, String str) { return null; } });
        U.chain(new String[] {""}).find(new Predicate<String>() {
            public boolean test(String str) { return true; } });
        U.chain(new String[] {""}).findLast(new Predicate<String>() {
            public boolean test(String str) { return true; } });
        U.chain(new Integer[] {0}).max();
        U.chain(new Integer[] {0}).max(new Function<Integer, Integer>() {
            public Integer apply(Integer value) { return value; } });
        U.chain(new Integer[] {0}).min();
        U.chain(new Integer[] {0}).min(new Function<Integer, Integer>() {
            public Integer apply(Integer value) { return value; } });
        U.chain(new Integer[] {0}).sort();
        U.chain(new Integer[] {0}).sortWith(new Comparator<Integer>() {
            public int compare(Integer value1, Integer value2) { return value1; } });
        U.chain(new Integer[] {0}).sortBy(new Function<Integer, Integer>() {
            public Integer apply(Integer value) { return value; } });
        U.chain(new LinkedHashMap<Integer, Integer>().entrySet()).sortBy("");
        U.chain(new Integer[] {0}).groupBy(new Function<Integer, Integer>() {
            public Integer apply(Integer value) { return value; } });
        U.chain(new Integer[] {0}).indexBy("");
        U.chain(new Integer[] {0}).countBy(new Function<Integer, Integer>() {
            public Integer apply(Integer value) { return value; } });
        U.chain(new Integer[] {0}).shuffle();
        U.chain(new Integer[] {0}).sample();
        U.chain(new Integer[] {0}).sample(1);
        U.chain(new int[] {0}).value();
        U.chain(new String[] {""}).tap(new Consumer<String>() {
            public void accept(String str) {
            } });
        U.chain(new String[] {""}).forEach(new Consumer<String>() {
            public void accept(String str) {
            } });
        U.chain(new String[] {""}).forEachRight(new Consumer<String>() {
            public void accept(String str) {
            } });
        U.chain(new String[] {""}).every(new Predicate<String>() {
            public boolean test(String str) { return true; } });
        U.chain(new String[] {""}).some(new Predicate<String>() {
            public boolean test(String str) { return true; } });
        U.chain(new String[] {""}).contains("");
        U.chain(new String[] {""}).invoke("toString", Collections.emptyList());
        U.chain(new String[] {""}).invoke("toString");
        U.chain(new String[] {""}).pluck("toString");
        U.chain(new String[] {""}).where(Collections.<Tuple<String, String>>emptyList());
        U.chain(new String[] {""}).findWhere(Collections.<Tuple<String, String>>emptyList());
        U.chain(new Integer[] {0}).uniq();
        U.chain(new Integer[] {0}).uniq(new Function<Integer, Integer>() {
            public Integer apply(Integer value) { return value; } });
        U.chain(new Integer[] {0}).distinct();
        U.chain(new Integer[] {0}).distinctBy(new Function<Integer, Integer>() {
            public Integer apply(Integer value) { return value; } });
        U.chain(new String[] {""}).union();
        U.chain(new String[] {""}).intersection();
        U.chain(new String[] {""}).difference();
        U.chain(new String[] {""}).range(0);
        U.chain(new String[] {""}).range(0, 0);
        U.chain(new String[] {""}).range(0, 0, 1);
        U.chain(new String[] {""}).chunk(1);
        U.chain(new String[] {""}).concat();
        U.chain(new String[] {""}).slice(0);
        U.chain(new String[] {""}).slice(0, 0);
        U.chain(new String[] {""}).reverse();
        U.chain(new String[] {""}).join();
        U.chain(new String[] {""}).join("");
        U.chain(new String[] {""}).skip(0);
        U.chain(new String[] {""}).limit(0);
        U.chain(new Integer[] {0}).sum(new Function<Integer, Integer>() {
            public Integer apply(Integer value) { return value; } });
        U.chain(new Integer[] {0}).mean();
        U.chain(new Integer[] {0}).median();
        U.chain(new Integer[] {0}).sum();
        U.chain(new Integer[] {0}).at(new Integer[] {0});
        U.chain(new String[] {""}).camelCase();
        U.chain(new String[] {""}).capitalize();
        U.chain(new String[] {""}).deburr();
        U.chain(new String[] {""}).drop();
        U.chain(new String[] {""}).drop(0);
        U.chain(new String[] {""}).dropRight();
        U.chain(new String[] {""}).dropRight(0);
        U.chain(asList(1, 2, 3)).dropRightWhile(new Predicate<Integer>() {
            public boolean test(Integer n) {
                return n > 2;
            }
        });
        U.chain(asList(1, 2, 3)).dropWhile(new Predicate<Integer>() {
            public boolean test(Integer n) {
                return n > 2;
            }
        });
        U.chain(new String[] {""}).endsWith("");
        U.chain(new String[] {""}).endsWith("", 0);
        U.chain("http://www.dragonsofmugloar.com/api/game/483159").fetch();
        U.chain("http://www.dragonsofmugloar.com/api/game/31906/solution").fetch("PUT", "{"
            + "    \"dragon\": {"
            + "        \"scaleThickness\": 4,"
            + "        \"clawSharpness\": 2,"
            + "        \"wingStrength\": 4,"
            + "        \"fireBreath\": 10"
            + "    }"
            + "}");
        U.chain(new String[] {""}).fill("");
        U.chain(new String[] {""}).fill("", 0, 0);
        U.chain(new String[] {""}).flattenDeep();
        U.chain("{\"a\":[{\"b\":{\"c\":\"d\"}}]}").fromJson();
        U.chain("<root>0</root>").fromXml();
        U.chain(new String[] {""}).kebabCase();
        U.chain(new String[] {""}).lowerFirst();
        U.chain("abc").pad(2);
        U.chain("abc").pad(8, "_-");
        U.chain("abc").padEnd(6);
        U.chain("abc").padEnd(6, "_-");
        U.chain("abc").padStart(6);
        U.chain("abc").padStart(6, "_-");
        U.chain(new ArrayList<Object>(asList(1, 2, 3))).pull(1, 2);
        U.chain(new ArrayList<Object>(asList(1, 2, 3))).pullAt(1, 2);
        U.chain(new ArrayList<Object>(asList(1, 2, 3))).remove(new Predicate<Object>() {
            public boolean test(Object n) {
                return false;
            }
        });
        U.chain(asList(1)).repeat(1);
        U.chain("abc").snakeCase();
        U.chain("abc").startCase();
        U.chain("abc").startsWith("");
        U.chain("abc").startsWith("", 0);
        U.chain(new String[] {""}).take();
        U.chain(new String[] {""}).take(0);
        U.chain(new String[] {""}).takeRight();
        U.chain(new String[] {""}).takeRight(0);
        U.chain(asList(1, 2, 3)).takeRightWhile(new Predicate<Integer>() {
            public boolean test(Integer n) {
                return n > 1;
            }
        });
        U.chain(asList(1, 2, 3)).takeWhile(new Predicate<Integer>() {
            public boolean test(Integer n) {
                return n > 1;
            }
        });
        U.chain(asList(1, 2, 3)).toJson();
        U.chain(asList(1, 2, 3)).toXml();
        U.chain("abc").trim();
        U.chain("abc").trim("");
        U.chain("abc").trimEnd();
        U.chain("abc").trimEnd("");
        U.chain("abc").trimStart();
        U.chain("abc").trimStart("");
        U.chain("hi-did").trunc();
        U.chain("hi-did").trunc(3);
        U.chain("abc").uncapitalize();
        U.chain("abc").upperFirst();
        U.chain("abc").words();
        U.chain(asList(1, 2)).xor(asList(4, 2));
        U.chain(new LinkedHashMap<Integer, Integer>().entrySet()).toMap();
    }
}
