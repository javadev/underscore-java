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
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import org.junit.jupiter.api.Test;

/**
 * Underscore library unit test.
 *
 * @author Valentyn Kolesnikov
 */
class ArraysTest {

    /*
    _.first([5, 4, 3, 2, 1]);
    => 5
    _.first([5, 4, 3, 2, 1], 2);
    => [5, 4]
    */
    @Test
    void first() {
        // static, chain, object
        assertEquals("5", Underscore.first(asList(5, 4, 3, 2, 1)).toString());
        assertEquals("5", Underscore.chain(asList(5, 4, 3, 2, 1)).first().item().toString());
        assertEquals("0", new Underscore<>(Underscore.range(3)).first().toString());
        // static, chain, object with int
        assertEquals("[5, 4]", Underscore.chain(asList(5, 4, 3, 2, 1)).first(2).value().toString());
        assertEquals("[5, 4]", Underscore.first(asList(5, 4, 3, 2, 1), 2).toString());
        assertEquals("[0, 1]", new Underscore<>(Underscore.range(3)).first(2).toString());
        // static, chain, object with larger int
        assertEquals("[a, b]", Underscore.first(asList("a", "b"), 4).toString());
        assertEquals("[a, b]", Underscore.chain(asList("a", "b")).first(4).toString());
        assertEquals("[0, 1, 2]", new Underscore<>(Underscore.range(3)).first(4).toString());
        // static, chain, object with wrong int
        assertEquals("[]", Underscore.first(asList("a", "b"), 0).toString());
        assertEquals("[]", Underscore.first(Underscore.range(3), -2).toString());
        assertEquals("[]", new Underscore<>(Underscore.range(3)).first(0).toString());
        assertEquals("[]", new Underscore<>(Underscore.range(3)).first(-1).toString());
        assertEquals("[]", Underscore.chain(singletonList("a")).first(-100).value().toString());
        // array
        assertEquals(5, Underscore.first(new Integer[] {5, 4, 3, 2, 1}).intValue());
        // static, chain, object with predicate
        final int resultPred = Underscore.first(asList(5, 4, 3, 2, 1), item -> item % 2 == 0);
        assertEquals(4, resultPred);
        final int resultPredObj =
                new Underscore<>(asList(5, 4, 3, 2, 1)).first(item -> item % 2 == 0);
        assertEquals(4, resultPredObj);
        final int resultChainPred =
                Underscore.chain(asList(5, 4, 3, 2, 1)).first(item -> item % 2 == 0).item();
        assertEquals(4, resultChainPred);
        // static, chain, object with predicate and int
        final List<Integer> result1 =
                new Underscore<>(Underscore.range(7)).first(item -> item % 2 == 0, 2);
        assertEquals("[0, 2]", result1.toString());
        final List<Integer> result2 = Underscore.first(Underscore.range(7), item -> item < 1, 4);
        assertEquals("[0]", result2.toString());
        final Underscore.Chain<Integer> result3 =
                Underscore.chain(Underscore.range(7)).first(item -> item < 2, 4);
        assertEquals("[0, 1]", result3.toString());
        final List<Integer> result4 =
                new Underscore<>(Underscore.range(3)).first(item -> item > 2, -5);
        assertEquals("[]", result4.toString());
        final List<String> result5 =
                Underscore.first(asList("aa", "bbbb"), item -> item.length() < 3, -2);
        assertEquals("[]", result5.toString());
        final Underscore.Chain<Integer> result6 =
                Underscore.chain(Underscore.range(7)).first(item -> item < 2, -1);
        assertEquals("[]", result6.toString());
    }

    @Test
    void firstOrNull() {
        final Integer result = Underscore.firstOrNull(asList(5, 4, 3, 2, 1));
        assertEquals("5", result.toString());
        final Integer resultObj = new Underscore<>(asList(5, 4, 3, 2, 1)).firstOrNull();
        assertEquals("5", resultObj.toString());
        final Integer resultChain = Underscore.chain(asList(5, 4, 3, 2, 1)).firstOrNull().item();
        assertEquals("5", resultChain.toString());
        assertNull(Underscore.firstOrNull(Collections.emptyList()));
        assertNull(new Underscore<>(Collections.<Integer>emptyList()).firstOrNull());
        final int resultPred = Underscore.firstOrNull(asList(5, 4, 3, 2, 1), item -> item % 2 == 0);
        assertEquals(4, resultPred);
        final int resultPredChain =
                Underscore.chain(asList(5, 4, 3, 2, 1)).firstOrNull(item -> item % 2 == 0).item();
        assertEquals(4, resultPredChain);
        assertNull(Underscore.firstOrNull(Collections.<Integer>emptyList(), item -> item % 2 == 0));
        final int resultPredObj =
                new Underscore<>(asList(5, 4, 3, 2, 1)).firstOrNull(item -> item % 2 == 0);
        assertEquals(4, resultPredObj);
        assertNull(
                new Underscore<>(Collections.<Integer>emptyList())
                        .firstOrNull(item -> item % 2 == 0));
    }

    @Test
    @SuppressWarnings("unchecked")
    void firstEmpty() {
        List<Object> iterable = Collections.emptyList();
        assertThrows(NoSuchElementException.class, () -> Underscore.first(iterable));
    }

    /*
    _.head([5, 4, 3, 2, 1]);
    => 5
    _.head([5, 4, 3, 2, 1], 2);
    => [5, 4]
    */
    @Test
    void head() {
        final Integer result = Underscore.head(asList(5, 4, 3, 2, 1));
        assertEquals("5", result.toString());
        final Integer resultObj = new Underscore<>(asList(5, 4, 3, 2, 1)).head();
        assertEquals("5", resultObj.toString());
        final List<Integer> resultList = Underscore.head(asList(5, 4, 3, 2, 1), 2);
        assertEquals("[5, 4]", resultList.toString());
        final List<Integer> resultListObj = new Underscore<>(asList(5, 4, 3, 2, 1)).head(2);
        assertEquals("[5, 4]", resultListObj.toString());
        final int resultInt = Underscore.head(new Integer[] {5, 4, 3, 2, 1});
        assertEquals(5, resultInt);
    }

    /*
    _.singleOrNull([5, 4, 3, 2, 1]);
    => null
    _.singleOrNull([5]);
    => 5
    */
    @Test
    void singleOrNull() {
        Underscore<Integer> uWithMoreElement = new Underscore<>(asList(1, 2, 3));
        Underscore<Integer> uWithOneElement = new Underscore<>(singletonList(1));

        final Integer result1 = Underscore.singleOrNull(asList(1, 2, 3));
        assertNull(result1);
        final int result2 = Underscore.singleOrNull(singletonList(1));
        assertEquals(1, result2);
        final Integer result3 = Underscore.singleOrNull(new ArrayList<>());
        assertNull(result3);
        final Integer result4 = Underscore.singleOrNull(asList(1, 2, 3), item -> item % 2 == 1);
        assertNull(result4);
        final int result5 = Underscore.singleOrNull(asList(1, 2, 3), item -> item % 2 == 0);
        assertEquals(2, result5);
        final Integer result6 = Underscore.singleOrNull(asList(1, 2, 3), item -> item == 5);
        assertNull(result6);
        final Integer result7 = uWithMoreElement.singleOrNull();
        assertNull(result7);
        final Integer result8 = uWithOneElement.singleOrNull();
        assertEquals(result8, Integer.valueOf(1));
        final Integer result9 = uWithMoreElement.singleOrNull(item -> item % 2 == 0);
        assertEquals(result9, Integer.valueOf(2));
        final Integer result10 = uWithMoreElement.singleOrNull(item -> item % 2 == 1);
        assertNull(result10);
    }

    /*
    _.rest([5, 4, 3, 2, 1]);
    => [4, 3, 2, 1]
    _.rest([5, 4, 3, 2, 1], 2);
    => [3, 2, 1]
    */
    @Test
    void rest() {
        final List<Integer> result = Underscore.rest(asList(5, 4, 3, 2, 1));
        assertEquals("[4, 3, 2, 1]", result.toString());
        final List<Integer> resultChain = Underscore.chain(asList(5, 4, 3, 2, 1)).rest().value();
        assertEquals("[4, 3, 2, 1]", resultChain.toString());
        final List<Integer> result2 = Underscore.rest(asList(5, 4, 3, 2, 1), 2);
        assertEquals("[3, 2, 1]", result2.toString());
        final List<Integer> result2Chain = Underscore.chain(asList(5, 4, 3, 2, 1)).rest(2).value();
        assertEquals("[3, 2, 1]", result2Chain.toString());
        final Object[] resultArray = Underscore.rest(new Integer[] {5, 4, 3, 2, 1});
        assertEquals("[4, 3, 2, 1]", asList(resultArray).toString());
        final Object[] resultArray2 = Underscore.rest(new Integer[] {5, 4, 3, 2, 1}, 2);
        assertEquals("[3, 2, 1]", asList(resultArray2).toString());
    }

    /*
    _.chunk(['a', 'b', 'c', 'd'], 2);
    // → [['a', 'b'], ['c', 'd']]

    _.chunk(['a', 'b', 'c', 'd'], 3);
    // → [['a', 'b', 'c'], ['d']]

    _.chunk(['a', 'b', 'c', 'd', 'e', 'f', 'g'], 2, 3);
    // → [['a', 'b'], ['d', 'e'], ['g']]
    */
    @Test
    void chunk() {
        assertEquals(
                "[[a, b, c], [d]]", Underscore.chunk(asList("a", "b", "c", "d"), 3).toString());
        assertEquals(
                "[[a, b], [c, d]]", Underscore.chunk(asList("a", "b", "c", "d"), 2).toString());
        assertEquals("[]", Underscore.chunk(asList("a", "b", "c", "d"), 0).toString());
        assertEquals("[]", Underscore.chunk(asList(1.1, 2.2, 3.3, 4.4), -2).toString());
        assertEquals(
                "[[0, 1], [3, 4], [6]]", Underscore.chunk(Underscore.range(7), 2, 3).toString());
        assertEquals("[[], [], []]", Underscore.chunk(Underscore.range(7), 0, 3).toString());
        assertEquals("[]", Underscore.chunk(Underscore.range(7), -2, 3).toString());
        assertEquals("[]", Underscore.chunk(Underscore.range(7), 2, 0).toString());
        assertEquals("[]", Underscore.chunk(Underscore.range(7), 2, -2).toString());
        assertEquals(
                "[[a, b], [c, d]]",
                new Underscore<>(asList("a", "b", "c", "d")).chunk(2).toString());
        assertEquals("[]", new Underscore<>(asList("a", "b", "c", "d")).chunk(0).toString());
        assertEquals(
                "[[0, 1, 2], [2, 3, 4], [4, 5]]",
                new Underscore<>(Underscore.range(6)).chunk(3, 2).toString());
        assertEquals("[]", new Underscore<>(Underscore.range(7)).chunk(3, 0).toString());
        assertEquals(
                "[[a, b], [c, d]]",
                Underscore.chain(asList("a", "b", "c", "d")).chunk(2).value().toString());
        assertEquals(
                "[]", Underscore.chain(asList("a", "b", "c", "d")).chunk(0).value().toString());
        assertEquals(
                "[[a, b], [b, c], [c, d], [d]]",
                Underscore.chain(asList("a", "b", "c", "d")).chunk(2, 1).value().toString());
        assertEquals(
                "[]", Underscore.chain(asList("a", "b", "c", "d")).chunk(4, 0).value().toString());
    }

    @Test
    void chunkFill() {
        assertEquals(
                "[[a, b, c], [d, fill, fill]]",
                Underscore.chunkFill(asList("a", "b", "c", "d"), 3, "fill").toString());
        assertEquals(
                "[[a, b], [c, d]]",
                Underscore.chunkFill(asList("a", "b", "c", "d"), 2, "fill").toString());
        assertEquals("[]", Underscore.chunkFill(asList("a", "b", "c", "d"), 0, "fill").toString());
        assertEquals("[]", Underscore.chunkFill(asList(1.1, 2.2, 3.3, 4.4), -2, 0.0).toString());
        assertEquals(
                "[[0, 1], [3, 4], [6, 500]]",
                Underscore.chunkFill(Underscore.range(7), 2, 3, 500).toString());
        assertEquals(
                "[[], [], []]", Underscore.chunkFill(Underscore.range(7), 0, 3, 500).toString());
        assertEquals("[]", Underscore.chunkFill(Underscore.range(7), -2, 3, 500).toString());
        assertEquals("[]", Underscore.chunkFill(Underscore.range(7), 2, 0, 500).toString());
        assertEquals("[]", Underscore.chunkFill(Underscore.range(7), 2, -2, 500).toString());
        assertEquals(
                "[[a, b, c], [d, fill, fill]]",
                new Underscore<>(asList("a", "b", "c", "d")).chunkFill(3, "fill").toString());
        assertEquals(
                "[]", new Underscore<>(asList("a", "b", "c", "d")).chunkFill(0, "fill").toString());
        assertEquals(
                "[[0, 1, 2], [2, 3, 4], [4, 5, 500]]",
                new Underscore<>(Underscore.range(6)).chunkFill(3, 2, 500).toString());
        assertEquals("[]", new Underscore<>(Underscore.range(7)).chunkFill(3, 0, 500).toString());
        assertEquals(
                "[[a, b], [c, d]]",
                Underscore.chain(asList("a", "b", "c", "d"))
                        .chunkFill(2, "fill")
                        .value()
                        .toString());
        assertEquals(
                "[]",
                Underscore.chain(asList("a", "b", "c", "d"))
                        .chunkFill(0, "fill")
                        .value()
                        .toString());
        assertEquals(
                "[[a, b], [b, c], [c, d], [d, fill]]",
                Underscore.chain(asList("a", "b", "c", "d"))
                        .chunkFill(2, 1, "fill")
                        .value()
                        .toString());
        assertEquals(
                "[]",
                Underscore.chain(asList("a", "b", "c", "d"))
                        .chunkFill(4, 0, "fill")
                        .value()
                        .toString());
    }

    /*
    _.cycle([1, 2, 3], 3);
    // → [1, 2, 3, 1, 2, 3, 1, 2, 3]
    _.cycle([1, 2, 3], -3);
    // → [3, 2, 1, 3, 2, 1, 3, 2, 1]
    _.cycle([1, 2, 3], 0);
    // → []
    */
    @Test
    void cycle() {
        assertEquals("[]", Underscore.cycle(Underscore.range(5), 0).toString());
        assertEquals("[]", Underscore.cycle(new ArrayList<>(), 5).toString());
        assertEquals("[4, 3, 2, 1, 0]", Underscore.cycle(Underscore.range(5), -1).toString());
        assertEquals(
                "[0, 1, 2, 0, 1, 2, 0, 1, 2]", Underscore.cycle(Underscore.range(3), 3).toString());
        assertEquals("[]", new Underscore<>(asList("a", "b", "c")).cycle(0).toString());
        assertEquals(
                "[c, b, a, c, b, a]", new Underscore<>(asList("a", "b", "c")).cycle(-2).toString());
        assertEquals(
                "[a, b, c, a, b, c, a, b, c]",
                new Underscore<>(asList("a", "b", "c")).cycle(3).toString());
        assertEquals("[]", Underscore.chain(Underscore.range(10)).cycle(0).value().toString());
        assertEquals(
                "[0, 0, 0, 0, 0]",
                Underscore.chain(Underscore.range(1)).cycle(5).value().toString());
        assertEquals(
                "[3, 2, 1, 0]", Underscore.chain(Underscore.range(4)).cycle(-1).value().toString());
    }

    /*
    _.repeat('a', 5);
    => [a, a, a, a, a]
    _.repeat('a', 0);
    => []
    _.repeat('a', -1);
    => []
    _.repeat(null, 3);
    => [null, null, null]
    */
    @Test
    void repeat() {
        assertEquals("[a, a, a, a, a]", Underscore.repeat('a', 5).toString());
        assertEquals("[]", Underscore.repeat('a', 0).toString());
        assertEquals("[]", Underscore.repeat('a', -1).toString());
        assertEquals("[apple, apple, apple]", Underscore.repeat("apple", 3).toString());
        assertEquals("[100, 100, 100]", Underscore.repeat(100, 3).toString());
        assertEquals("[2.5, 2.5, 2.5]", Underscore.repeat(2.5, 3).toString());
        assertEquals("[null, null, null]", Underscore.repeat(null, 3).toString());
    }

    /*
    _.interpose([1, 2, 3], 500);
    // → [1, 500, 2, 500, 3]
    _.interpose([], 500);
    // → []
    _.interpose([1], 500);
    // → [1]
    */
    @Test
    void interpose() {
        assertEquals(
                "[0, 500, 1, 500, 2, 500, 3]",
                Underscore.interpose(Underscore.range(4), 500).toString());
        assertEquals("[]", Underscore.interpose(new ArrayList<>(), 500).toString());
        assertEquals("[]", Underscore.interpose(new ArrayList<>(), null).toString());
        assertEquals(
                "[0, 1, 2, 3]",
                Underscore.interpose(Underscore.newArrayList(Underscore.range(4)), null)
                        .toString());
        assertEquals("[0]", Underscore.interpose(Underscore.range(1), 500).toString());
        assertEquals(
                "[a, interpose, b, interpose, c]",
                new Underscore<>(asList("a", "b", "c")).interpose("interpose").toString());
        assertEquals("[a]", new Underscore<>(singletonList("a")).interpose("interpose").toString());
        assertEquals("[a, b]", new Underscore<>(singletonList("a, b")).interpose(null).toString());
        assertEquals("[a]", Underscore.chain(singletonList("a")).interpose("interpose").toString());
        assertEquals(
                "[]", Underscore.chain(new ArrayList<>()).interpose("interpose").toString());
        assertEquals(
                "[a, b, c]", Underscore.chain(asList("a", "b", "c")).interpose(null).toString());
        assertEquals(
                "[?, interpose, !, interpose, -]",
                Underscore.chain(asList("?", "!", "-")).interpose("interpose").toString());
    }

    /*
    _.interpose([1, 2, 3], [100, 200, 300]);
    // → [1, 100, 2, 200, 3]
    _.interpose([1, 2, 3], [100]);
    // → [1, 100, 2, 3]
    _.interpose([1], [500]);
    // → [1]
    _.interpose([], [500, 600, 700]);
    // → []
    */
    @Test
    void interposeByList() {
        List<String> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();
        assertEquals(
                "[0, 100, 1, 200, 2, 300, 3]",
                Underscore.interposeByList(Underscore.range(4), Underscore.range(100, 600, 100))
                        .toString());
        assertEquals(
                "[]", Underscore.interposeByList(list2, Underscore.range(100, 300, 50)).toString());
        assertEquals(
                "[100, 200, 300]",
                Underscore.interposeByList(Underscore.range(100, 400, 100), list2).toString());
        assertEquals(
                "[100, 200, 300]",
                Underscore.interposeByList(Underscore.range(100, 400, 100), null).toString());
        list2.add(1);
        assertEquals(
                "[1]",
                Underscore.interposeByList(list2, Underscore.range(100, 300, 50)).toString());
        assertEquals(
                "[0, 100, 1, 2, 3]",
                Underscore.interposeByList(Underscore.range(4), Underscore.newIntegerList(100))
                        .toString());
        assertEquals(
                "[a, zzz, b, c]",
                new Underscore<>(asList("a", "b", "c"))
                        .interposeByList(singletonList("zzz"))
                        .toString());
        assertEquals(
                "[a, b, c]",
                new Underscore<>(asList("a", "b", "c")).interposeByList(null).toString());
        assertEquals(
                "[a]",
                new Underscore<>(singletonList("a"))
                        .interposeByList(singletonList("zzz"))
                        .toString());
        assertEquals(
                "[a, b, c]",
                new Underscore<>(asList("a", "b", "c")).interposeByList(list1).toString());
        assertEquals(
                "[a, aaa, b, bbb, c]",
                new Underscore<>(asList("a", "b", "c"))
                        .interposeByList(asList("aaa", "bbb", "ccc"))
                        .toString());
        assertEquals(
                "[a]",
                Underscore.chain(singletonList("a"))
                        .interposeByList(asList("aaa", "bbb", "ccc"))
                        .toString());
        assertEquals(
                "[aaa, bbb, ccc]",
                Underscore.chain(asList("aaa", "bbb", "ccc")).interposeByList(null).toString());
        list2.clear();
        assertEquals("[]", Underscore.chain(list2).interposeByList(Underscore.range(6)).toString());
        assertEquals(
                "[?, aaa, !, bbb, -]",
                Underscore.chain(asList("?", "!", "-"))
                        .interposeByList(asList("aaa", "bbb", "ccc"))
                        .toString());
    }

    /*
    _.tail([5, 4, 3, 2, 1]);
    => [4, 3, 2, 1]
    _.tail([5, 4, 3, 2, 1], 2);
    => [3, 2, 1]
    */
    @Test
    void tail() {
        final List<Integer> result = Underscore.tail(asList(5, 4, 3, 2, 1));
        assertEquals("[4, 3, 2, 1]", result.toString());
        final List<Integer> result2 = Underscore.tail(asList(5, 4, 3, 2, 1), 2);
        assertEquals("[3, 2, 1]", result2.toString());
        final Object[] resultArray = Underscore.tail(new Integer[] {5, 4, 3, 2, 1});
        assertEquals("[4, 3, 2, 1]", asList(resultArray).toString());
        final List<Integer> resultArrayObj = new Underscore<>(asList(5, 4, 3, 2, 1)).tail();
        assertEquals("[4, 3, 2, 1]", resultArrayObj.toString());
        final Object[] resultArray2 = Underscore.tail(new Integer[] {5, 4, 3, 2, 1}, 2);
        assertEquals("[3, 2, 1]", asList(resultArray2).toString());
        final List<Integer> resultArray2Obj = new Underscore<>(asList(5, 4, 3, 2, 1)).tail(2);
        assertEquals("[3, 2, 1]", resultArray2Obj.toString());
    }

    /*
    _.drop([5, 4, 3, 2, 1]);
    => [4, 3, 2, 1]
    _.drop([5, 4, 3, 2, 1], 2);
    => [3, 2, 1]
    */
    @Test
    void drop() {
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
    _.replace([1, 2, 3, 4], predicate(a) { return a > 2; }, 100);
    => [1, 2, 100, 100]
    _.replace([1, 2, 3, 4], null, 100);
    => [1, 2, 3, 4]
    */
    @SuppressWarnings("serial")
    @Test
    void replace() {
        assertEquals(
                "[100, 1, 100, 3, 100, 5]",
                Underscore.replace(Underscore.range(6), arg -> arg % 2 == 0, 100).toString());
        assertEquals(
                "[0, 1, 2, 3, 4]", Underscore.replace(Underscore.range(5), null, 100).toString());
        assertEquals(
                "[a, aa, b, b]",
                new Underscore<>(asList("a", "aa", "aaa", "aaaa"))
                        .replace(arg -> arg.length() > 2, "b")
                        .toString());
        assertEquals(
                "[a, aa, cc, ccc]",
                new Underscore<>(asList("a", "aa", "cc", "ccc")).replace(null, "b").toString());
        Set<Integer> set = new LinkedHashSet<>();
        set.addAll(Underscore.range(7));
        assertEquals(
                "[0, 1, 2, 100, 100, 100, 100]",
                Underscore.chain(set).replace(arg -> arg > 2, 100).toString());
    }

    /*
    _.replaceIndexed([a, b, c, d], predicateIndexed(a, b) { return a > 2; }, z);
    => [a, b, z, z]
    _.replaceIndexed([a, b, c, d], null, z);
    => [a, b, c, d]
    */
    @SuppressWarnings("serial")
    @Test
    void replaceIndexed() {
        assertEquals(
                "[0, 1, 2, 3, 100, 100]",
                Underscore.replaceIndexed(Underscore.range(6), (i, arg) -> i > 2 && arg > 3, 100)
                        .toString());
        assertEquals(
                "[0, 1, 2, 3, 4]",
                Underscore.replaceIndexed(Underscore.range(5), null, 100).toString());
        assertEquals(
                "[a, bc, ddd, f]",
                new Underscore<>(asList("a", "bc", "ddd", "eeee"))
                        .replaceIndexed((i, arg) -> arg.length() > 2 && i > 2, "f")
                        .toString());
        assertEquals(
                "[a, aa, cc, ccc]",
                new Underscore<>(asList("a", "aa", "cc", "ccc"))
                        .replaceIndexed(null, "b")
                        .toString());
        List<Integer> list = new ArrayList<>();
        list.add(100);
        list.add(22);
        list.add(88);
        list.add(6530);
        list.add(-25);
        list.add(-1000);
        assertEquals(
                "[100, 0, 88, 6530, 0, -1000]",
                Underscore.chain(list).replaceIndexed((i, arg) -> arg < 23 && i < 5, 0).toString());
    }

    /*
    _.initial([5, 4, 3, 2, 1]);
    => [5, 4, 3, 2]
    _.initial([5, 4, 3, 2, 1], 2);
    => [5, 4, 3]
    */
    @Test
    @SuppressWarnings("unchecked")
    void initial() {
        final List<Integer> result = Underscore.initial(asList(5, 4, 3, 2, 1));
        assertEquals("[5, 4, 3, 2]", result.toString());
        final List<Integer> resultChain = Underscore.chain(asList(5, 4, 3, 2, 1)).initial().value();
        assertEquals("[5, 4, 3, 2]", resultChain.toString());
        final List<Integer> resultList = Underscore.initial(asList(5, 4, 3, 2, 1), 2);
        assertEquals("[5, 4, 3]", resultList.toString());
        final List<Integer> resultListChain =
                Underscore.chain(asList(5, 4, 3, 2, 1)).initial(2).value();
        assertEquals("[5, 4, 3]", resultListChain.toString());
        final Integer[] resultArray = Underscore.initial(new Integer[] {5, 4, 3, 2, 1});
        assertEquals("[5, 4, 3, 2]", asList(resultArray).toString());
        final Integer[] resultListArray = Underscore.initial(new Integer[] {5, 4, 3, 2, 1}, 2);
        assertEquals("[5, 4, 3]", asList(resultListArray).toString());
        List<Integer> res = new Underscore(asList(1, 2, 3, 4, 5)).initial();
        assertEquals(asList(1, 2, 3, 4), res, "initial one item did not work");
        res = new Underscore(asList(1, 2, 3, 4, 5)).initial(3);
        assertEquals(asList(1, 2), res, "initial multi item did not wok");
    }

    /*
    _.last([5, 4, 3, 2, 1]);
    => 1
    */
    @Test
    @SuppressWarnings("unchecked")
    void last() {
        final Integer result = Underscore.last(asList(5, 4, 3, 2, 1));
        assertEquals("1", result.toString());
        final List<Integer> resultTwo = Underscore.last(asList(5, 4, 3, 2, 1), 2);
        assertEquals("[2, 1]", resultTwo.toString());
        final Object resultChain = Underscore.chain(asList(5, 4, 3, 2, 1)).last().item();
        assertEquals("1", resultChain.toString());
        final Object resultChainTwo = Underscore.chain(asList(5, 4, 3, 2, 1)).last(2).value();
        assertEquals("[2, 1]", resultChainTwo.toString());
        final Integer resultArray = Underscore.last(new Integer[] {5, 4, 3, 2, 1});
        assertEquals("1", resultArray.toString());
        Integer res = new Underscore<>(asList(1, 2, 3, 4, 5)).last();
        assertEquals(5, res.intValue(), "last one item did not work");
        List<Integer> resList = new Underscore(asList(1, 2, 3, 4, 5)).last(3);
        assertEquals(asList(3, 4, 5), resList, "last multi item did not wok");
        final int resultPred = Underscore.last(asList(5, 4, 3, 2, 1), item -> item % 2 == 0);
        assertEquals(2, resultPred);
        final int resultPredObj =
                new Underscore<>(asList(5, 4, 3, 2, 1)).last(item -> item % 2 == 0);
        assertEquals(2, resultPredObj);
    }

    @Test
    void lastOrNull() {
        final Integer result = Underscore.lastOrNull(asList(5, 4, 3, 2, 1));
        assertEquals("1", result.toString());
        final Integer resultObj = new Underscore<>(asList(5, 4, 3, 2, 1)).lastOrNull();
        assertEquals("1", resultObj.toString());
        final Integer resultChain = Underscore.chain(asList(5, 4, 3, 2, 1)).lastOrNull().item();
        assertEquals("1", resultChain.toString());
        assertNull(Underscore.lastOrNull(Collections.emptyList()));
        assertNull(new Underscore<>(Collections.<Integer>emptyList()).lastOrNull());
        final int resultPred = Underscore.lastOrNull(asList(5, 4, 3, 2, 1), item -> item % 2 == 0);
        assertEquals(2, resultPred);
        final int resultPredChain =
                Underscore.chain(asList(5, 4, 3, 2, 1)).lastOrNull(item -> item % 2 == 0).item();
        assertEquals(2, resultPredChain);
        assertNull(Underscore.lastOrNull(Collections.<Integer>emptyList(), item -> item % 2 == 0));
        final int resultPredObj =
                new Underscore<>(asList(5, 4, 3, 2, 1)).lastOrNull(item -> item % 2 == 0);
        assertEquals(2, resultPredObj);
        assertNull(
                new Underscore<>(Collections.<Integer>emptyList())
                        .lastOrNull(item -> item % 2 == 0));
    }
    /*
    _.compact([0, 1, false, 2, '', 3]);
    => [1, 2, 3]
    */
    @Test
    @SuppressWarnings("unchecked")
    void compact() {
        final List<?> result = Underscore.compact(asList(0, 1, false, 2, "", 3));
        assertEquals("[1, 2, 3]", result.toString());
        final List<?> result2 =
                Underscore.compactList(Arrays.<Object>asList(0, 1, false, 2, "", 3), 1);
        assertEquals("[0, false, 2, , 3]", result2.toString());
        final List<?> result3 = Underscore.compact(asList(0, 1, null, 2, "", 3));
        assertEquals("[1, 2, 3]", result3.toString());
        final List<?> resultChain =
                Underscore.chain(asList(0, 1, false, 2, "", 3)).compact().value();
        assertEquals("[1, 2, 3]", resultChain.toString());
        final List<?> result2Chain =
                Underscore.chain(Arrays.<Object>asList(0, 1, false, 2, "", 3)).compact(1).value();
        assertEquals("[0, false, 2, , 3]", result2Chain.toString());
        final List<?> result4 = new Underscore(asList(0, 1, false, 2, "", 3)).compact();
        assertEquals("[1, 2, 3]", result4.toString());
        final List<?> result5 = new Underscore(asList(0, 1, false, 2, "", 3)).compact(1);
        assertEquals("[0, false, 2, , 3]", result5.toString());
        final List<?> result6 = new Underscore(asList(0, 1, null, 2, "", 3)).compact(1);
        assertEquals("[0, null, 2, , 3]", result6.toString());
        final List<?> result7 =
                new Underscore(asList(0, 1, null, 2, "", 3)).compact((Integer) null);
        assertEquals("[0, 1, 2, , 3]", result7.toString());
        final Object[] resultArray = Underscore.compact(new Object[] {0, 1, false, 2, "", 3});
        assertEquals("[1, 2, 3]", asList(resultArray).toString());
        final Object[] resultArray2 = Underscore.compact(new Object[] {0, 1, false, 2, "", 3}, 1);
        assertEquals("[0, false, 2, , 3]", asList(resultArray2).toString());
    }

    /*
    _.flatten([1, [2], [3, [[4]]]]);
    => [1, 2, 3, 4];
    */
    @Test
    @SuppressWarnings("unchecked")
    void flatten() {
        final List<Integer> result =
                Underscore.flatten(
                        asList(1, asList(2, asList(3, singletonList(singletonList(4))))));
        assertEquals("[1, 2, 3, 4]", result.toString());
        final List<Integer> result2 =
                Underscore.flatten(
                        asList(1, asList(2, asList(3, singletonList(singletonList(4))))), true);
        assertEquals("[1, 2, [3, [[4]]]]", result2.toString());
        final List<Integer> result3 =
                Underscore.flatten(
                        asList(1, asList(2, asList(3, singletonList(singletonList(4))))), false);
        assertEquals("[1, 2, 3, 4]", result3.toString());
        final List<Integer> resultObj =
                new Underscore(asList(1, asList(2, asList(3, singletonList(singletonList(4))))))
                        .flatten();
        assertEquals("[1, 2, 3, 4]", resultObj.toString());
        final List<Integer> resultObj2 =
                new Underscore(asList(1, asList(2, asList(3, singletonList(singletonList(4))))))
                        .flatten(true);
        assertEquals("[1, 2, [3, [[4]]]]", resultObj2.toString());
        final List<Integer> resultObj3 =
                new Underscore(asList(1, asList(2, asList(3, singletonList(singletonList(4))))))
                        .flatten(false);
        assertEquals("[1, 2, 3, 4]", resultObj3.toString());
    }

    /*
    _.without([1, 2, 1, 0, 3, 1, 4], 0, 1);
    => [2, 3, 4]
    */
    @Test
    void without() {
        final List<Integer> result = Underscore.without(asList(1, 2, 1, 0, 3, 1, 4), 0, 1);
        assertEquals("[2, 3, 4]", result.toString());
        final List<Integer> result2 = Underscore.without(asList(1, 2, 1, 0, 3, 1, 4), 1);
        assertEquals("[2, 0, 3, 4]", result2.toString());
        final List<Integer> result3 =
                Underscore.without(asList(null, 2, null, 0, 3, null, 4), (Integer) null);
        assertEquals("[2, 0, 3, 4]", result3.toString());
        final Object[] resultArray = Underscore.without(new Integer[] {1, 2, 1, 0, 3, 1, 4}, 0, 1);
        assertEquals("[2, 3, 4]", asList(resultArray).toString());
        final Object[] resultArray2 = Underscore.without(new Integer[] {1, 2, 1, 0, 3, 1, 4}, 1);
        assertEquals("[2, 0, 3, 4]", asList(resultArray2).toString());
    }

    /*
    _.sortedIndex([10, 20, 30, 40, 50], 35);
    => 3
    */
    @Test
    void sortedIndex() {
        final Integer result = Underscore.sortedIndex(asList(10, 20, 30, 40, 50), 35);
        assertEquals(3, result.intValue());
        final Integer result2 = Underscore.sortedIndex(new Integer[] {10, 20, 30, 40, 50}, 35);
        assertEquals(3, result2.intValue());
        final Integer result3 = Underscore.sortedIndex(asList(10, 20, 30, 40, 50), 60);
        assertEquals(-1, result3.intValue());
    }

    @Test
    void sortedIndex2() {
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
        }
        final int result =
                Underscore.sortedIndex(
                        asList(
                                new Person("moe", 40),
                                new Person("moe", 50),
                                new Person("curly", 60)),
                        new Person("moe", 50),
                        "age");
        assertEquals(1, result);
        final int result2 =
                Underscore.sortedIndex(
                        asList(
                                new Person("moe", 40),
                                new Person("moe", 50),
                                new Person("curly", 60)),
                        new Person("moe", 70),
                        "age");
        assertEquals(-1, result2);
        final int resultArray =
                Underscore.sortedIndex(
                        new Person[] {
                            new Person("moe", 40), new Person("moe", 50), new Person("curly", 60)
                        },
                        new Person("moe", 50),
                        "age");
        assertEquals(1, resultArray);
    }

    @Test
    void sortedIndex2Error() {
        class Person implements Comparable<Person> {
            public int compareTo(Person person) {
                return 0;
            }
        }
        Person person = new Person();
        List<Person> personList = singletonList(person);
        assertThrows(
                IllegalArgumentException.class,
                () -> Underscore.sortedIndex(personList, person, "age"));
    }

    /*
    _.uniq([1, 2, 1, 3, 1, 4]);
    => [1, 2, 3, 4]
    */
    @Test
    void uniq() {
        final List<Integer> result = Underscore.uniq(asList(1, 2, 1, 3, 1, 4));
        assertEquals("[1, 2, 3, 4]", result.toString());
        final Object[] resultArray = Underscore.uniq(new Integer[] {1, 2, 1, 3, 1, 4});
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
        }
        final Collection<Person> resultObject =
                Underscore.uniq(
                        asList(
                                new Person("moe", 40),
                                new Person("moe", 50),
                                new Person("curly", 60)),
                        person -> person.name);
        assertEquals("[moe, 50, curly, 60]", resultObject.toString());
        final List<Person> resultObjectChain =
                Underscore.chain(
                                asList(
                                        new Person("moe", 40),
                                        new Person("moe", 50),
                                        new Person("curly", 60)))
                        .uniq(person -> person.name)
                        .value();
        assertEquals("[moe, 50, curly, 60]", resultObjectChain.toString());
        assertEquals(
                "[1, 2, 3, 4, 5]",
                Underscore.chain(asList(1, 2, 3, 3, 4, 5)).uniq().value().toString());
        final Object[] resultObjectArray =
                Underscore.uniq(
                        asList(
                                        new Person("moe", 40),
                                        new Person("moe", 50),
                                        new Person("curly", 60))
                                .toArray(new Person[] {}),
                        person -> person.name);
        assertEquals("[moe, 50, curly, 60]", asList(resultObjectArray).toString());
    }

    /*
    _.distinct([1, 2, 1, 3, 1, 4]);
    => [1, 2, 3, 4]
    */
    @Test
    void distinct() {
        final List<Integer> result = Underscore.distinct(asList(1, 2, 1, 3, 1, 4));
        assertEquals("[1, 2, 3, 4]", result.toString());
        final Object[] resultArray = Underscore.distinct(new Integer[] {1, 2, 1, 3, 1, 4});
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
        }
        final Collection<Person> resultObject =
                Underscore.distinctBy(
                        asList(
                                new Person("moe", 40),
                                new Person("moe", 50),
                                new Person("curly", 60)),
                        person -> person.name);
        assertEquals("[moe, 50, curly, 60]", resultObject.toString());
        final List<String> resultObjectChain =
                Underscore.chain(
                                asList(
                                        new Person("moe", 40),
                                        new Person("moe", 50),
                                        new Person("curly", 60)))
                        .distinctBy(person -> person.name)
                        .value();
        assertEquals("[moe, 50, curly, 60]", resultObjectChain.toString());
        assertEquals(
                "[1, 2, 3, 4, 5]",
                Underscore.chain(asList(1, 2, 3, 3, 4, 5)).distinct().value().toString());
        final Object[] resultObjectArray =
                Underscore.distinctBy(
                        asList(
                                        new Person("moe", 40),
                                        new Person("moe", 50),
                                        new Person("curly", 60))
                                .toArray(new Person[] {}),
                        person -> person.name);
        assertEquals("[moe, 50, curly, 60]", asList(resultObjectArray).toString());
    }

    /*
    _.intersection([1, 2, 3], [101, 2, 1, 10], [2, 1]);
    => [1, 2]
    */
    @Test
    @SuppressWarnings("unchecked")
    void intersection() {
        final List<Integer> result =
                Underscore.intersection(asList(1, 2, 3), asList(101, 2, 1, 10), asList(2, 1));
        assertEquals("[1, 2]", result.toString());
        final List<Integer> resultObj =
                new Underscore(asList(1, 2, 3))
                        .intersectionWith(asList(101, 2, 1, 10), asList(2, 1));
        assertEquals("[1, 2]", resultObj.toString());
        final List<Integer> resultChain =
                Underscore.chain(asList(1, 2, 3))
                        .intersection(asList(101, 2, 1, 10), asList(2, 1))
                        .value();
        assertEquals("[1, 2]", resultChain.toString());
        final Object[] resultArray =
                Underscore.intersection(new Integer[] {1, 2, 3}, new Integer[] {101, 2, 1, 10});
        assertEquals("[1, 2]", asList(resultArray).toString());
    }

    /*
    _.union([1, 2, 3], [101, 2, 1, 10], [2, 1]);
    => [1, 2, 3, 101, 10]
    */
    @Test
    @SuppressWarnings("unchecked")
    void union() {
        final List<Integer> result =
                Underscore.union(asList(1, 2, 3), asList(101, 2, 1, 10), asList(2, 1));
        assertEquals("[1, 2, 3, 101, 10]", result.toString());
        final List<Integer> resultObj =
                new Underscore(asList(1, 2, 3)).unionWith(asList(101, 2, 1, 10), asList(2, 1));
        assertEquals("[1, 2, 3, 101, 10]", resultObj.toString());
        final List<Integer> resultChain =
                Underscore.chain(asList(1, 2, 3))
                        .union(asList(101, 2, 1, 10), asList(2, 1))
                        .value();
        assertEquals("[1, 2, 3, 101, 10]", resultChain.toString());
        final Object[] resultArray =
                Underscore.union(new Integer[] {1, 2, 3}, new Integer[] {101, 2, 1, 10});
        assertEquals("[1, 2, 3, 101, 10]", asList(resultArray).toString());
    }

    /*
    _.difference([1, 2, 3, 4, 5], [5, 2, 10]);
    => [1, 3, 4]
    */
    @Test
    @SuppressWarnings("unchecked")
    void difference() {
        final List<Integer> result = Underscore.difference(asList(1, 2, 3, 4, 5), asList(5, 2, 10));
        assertEquals("[1, 3, 4]", result.toString());
        final List<Integer> resultObj =
                new Underscore(asList(1, 2, 3, 4, 5)).differenceWith(asList(5, 2, 10));
        assertEquals("[1, 3, 4]", resultObj.toString());
        final List<Integer> resultChain =
                Underscore.chain(asList(1, 2, 3, 4, 5)).difference(asList(5, 2, 10)).value();
        assertEquals("[1, 3, 4]", resultChain.toString());
        final List<Integer> resultList =
                Underscore.difference(asList(1, 2, 3, 4, 5), asList(5, 2, 10), asList(8, 4));
        assertEquals("[1, 3]", resultList.toString());
        final Object[] resultArray =
                Underscore.difference(new Integer[] {1, 2, 3, 4, 5}, new Integer[] {5, 2, 10});
        assertEquals("[1, 3, 4]", asList(resultArray).toString());
        final Object[] resultArray2 =
                Underscore.difference(
                        new Integer[] {1, 2, 3, 4, 5},
                        new Integer[] {5, 2, 10},
                        new Integer[] {8, 4});
        assertEquals("[1, 3]", asList(resultArray2).toString());
    }

    /*
    _.zip(['moe', 'larry', 'curly'], [30, 40, 50], [true, false, false]);
    => [["moe", 30, true], ["larry", 40, false], ["curly", 50, false]]
    */
    @Test
    @SuppressWarnings("unchecked")
    void zip() {
        final List<List<String>> result =
                Underscore.zip(
                        asList("moe", "larry", "curly"),
                        asList("30", "40", "50"),
                        asList("true", "false", "false"));
        assertEquals(
                "[[moe, 30, true], [larry, 40, false], [curly, 50, false]]", result.toString());
    }

    /*
    _.unzip(["moe", 30, true], ["larry", 40, false], ["curly", 50, false]);
    => [['moe', 'larry', 'curly'], [30, 40, 50], [true, false, false]]
    */
    @Test
    @SuppressWarnings("unchecked")
    void unzip() {
        final List<List<String>> result =
                Underscore.unzip(
                        asList("moe", "30", "true"),
                        asList("larry", "40", "false"),
                        asList("curly", "50", "false"));
        assertEquals(
                "[[moe, larry, curly], [30, 40, 50], [true, false, false]]", result.toString());
    }

    /*
    _.object(['moe', 'larry', 'curly'], [30, 40, 50]);
    => {moe: 30, larry: 40, curly: 50}
    */
    @Test
    void object() {
        final List<Map.Entry<String, String>> result =
                Underscore.object(asList("moe", "larry", "curly"), asList("30", "40", "50"));
        assertEquals("[moe=30, larry=40, curly=50]", result.toString());
    }

    /*
    _.indexOf([1, 2, 3], 2);
    => 1
    */
    @Test
    void indexOf() {
        final Integer result = Underscore.indexOf(asList(1, 2, 3), 2);
        assertEquals(1, result.intValue());
        final Integer resultArray = Underscore.indexOf(new Integer[] {1, 2, 3}, 2);
        assertEquals(1, resultArray.intValue());
    }

    /*
    _.lastIndexOf([1, 2, 3, 1, 2, 3], 2);
    => 4
    */
    @Test
    void lastIndexOf() {
        final Integer result = Underscore.lastIndexOf(asList(1, 2, 3, 1, 2, 3), 2);
        assertEquals(4, result.intValue());
        final Integer resultArray = Underscore.lastIndexOf(new Integer[] {1, 2, 3, 1, 2, 3}, 2);
        assertEquals(4, resultArray.intValue());
    }

    /*
    _.findIndex([1, 2, 3], function(item) {return item % 2  === 0; });
    => 1
    */
    @Test
    void findIndex() {
        final Integer result = Underscore.findIndex(asList(1, 2, 3), item -> item % 2 == 0);
        assertEquals(1, result.intValue());
        final Integer resultNotFound = Underscore.findIndex(asList(1, 2, 3), item -> item > 3);
        assertEquals(-1, resultNotFound.intValue());
        final Integer resultArray =
                Underscore.findIndex(new Integer[] {1, 2, 3}, item -> item % 2 == 0);
        assertEquals(1, resultArray.intValue());
    }

    /*
    _.findLastIndex([1, 2, 3, 4, 5], function(item) {return item % 2  === 0; });
    => 3
    */
    @Test
    void findLastIndex() {
        final Integer result =
                Underscore.findLastIndex(asList(1, 2, 3, 4, 5), item -> item % 2 == 0);
        assertEquals(3, result.intValue());
        final Integer resultNotFound =
                Underscore.findLastIndex(asList(1, 2, 3, 4, 5), item -> item > 5);
        assertEquals(-1, resultNotFound.intValue());
        final Integer resultArray =
                Underscore.findLastIndex(new Integer[] {1, 2, 3, 4, 5}, item -> item % 2 == 0);
        assertEquals(3, resultArray.intValue());
    }

    /*
    _.binarySearch([1, 3, 5], 3);
    => 1
    _.binarySearch([1, 3, 5], 2);
    => -2
    _.binarySearch([1, 3, 5], null);
    => -1
    _.binarySearch([null, 2, 4, 6], null);
    => 0
    */
    @Test
    void binarySearch() {
        final Integer[] array = {1, 3, 5};
        assertEquals(1, Underscore.binarySearch(array, 3));
        assertEquals(-2, Underscore.binarySearch(array, 2));
        assertEquals(-1, Underscore.binarySearch(array, null));
        final Integer[] array2 = {null, 2, 4, 6};
        assertEquals(0, Underscore.binarySearch(array2, null));
        assertEquals(1, Underscore.binarySearch(array2, 2));
        assertEquals(-2, Underscore.binarySearch(array2, 1));
        final Character[] array3 = {'b', 'c', 'e'};
        assertEquals(0, Underscore.binarySearch(array3, 'b'));
        assertEquals(-3, Underscore.binarySearch(array3, 'd'));
        final String[] array4 = {"bird", "camel", "elephant"};
        assertEquals(0, Underscore.binarySearch(array4, "bird"));
        assertEquals(-1, Underscore.binarySearch(array4, "ant"));
        final List<Integer> list1 = asList(1, 3, 5);
        assertEquals(1, Underscore.binarySearch(list1, 3));
        assertEquals(-2, Underscore.binarySearch(list1, 2));
        assertEquals(-1, Underscore.binarySearch(list1, null));
        final List<Integer> list2 = asList(null, 2, 4, 6);
        assertEquals(0, Underscore.binarySearch(list2, null));
        assertEquals(1, Underscore.binarySearch(list2, 2));
        assertEquals(-2, Underscore.binarySearch(list2, 1));
        final List<Character> list3 = asList('b', 'c', 'e');
        assertEquals(0, Underscore.binarySearch(list3, 'b'));
        assertEquals(-3, Underscore.binarySearch(list3, 'd'));
        final List<String> list4 = asList("bird", "camel", "elephant");
        assertEquals(0, Underscore.binarySearch(list4, "bird"));
        assertEquals(-1, Underscore.binarySearch(list4, "ant"));
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
    void range() {
        final List<Integer> result = Underscore.range(10);
        assertEquals("[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]", result.toString());
        final List<Integer> resultChain = Underscore.chain("").range(10).value();
        assertEquals("[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]", resultChain.toString());
        final List<Integer> result2 = Underscore.range(1, 11);
        assertEquals("[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]", result2.toString());
        final List<Integer> result2Chain = Underscore.chain("").range(1, 11).value();
        assertEquals("[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]", result2Chain.toString());
        final List<Integer> result3 = Underscore.range(0, 30, 5);
        assertEquals("[0, 5, 10, 15, 20, 25]", result3.toString());
        final List<Integer> result3Chain = Underscore.chain("").range(0, 30, 5).value();
        assertEquals("[0, 5, 10, 15, 20, 25]", result3Chain.toString());
        final List<Integer> result4 = Underscore.range(0, -10, -1);
        assertEquals("[0, -1, -2, -3, -4, -5, -6, -7, -8, -9]", result4.toString());
        final List<Integer> result5 = Underscore.range(0);
        assertEquals("[]", result5.toString());
        final List<Integer> result6 = Underscore.range(8, 5);
        assertEquals("[8, 7, 6]", result6.toString());
        final List<Integer> result7 = Underscore.range(0);
        assertEquals("[]", result7.toString());
        final List<Integer> result8 = Underscore.range(1, 10, 0);
        assertEquals("[]", result8.toString());
        final List<Character> result9 = Underscore.range('a', 'd', 1);
        assertEquals("[a, b, c]", result9.toString());
        final List<Character> result10 = Underscore.range('a', 'd');
        assertEquals("[a, b, c]", result10.toString());
        final List<Character> result11 = Underscore.range('d');
        assertEquals("[a, b, c]", result11.toString());
        final List<Character> result12 = Underscore.range('d', 'a', -1);
        assertEquals("[d, c, b]", result12.toString());
        final List<Character> result13 = Underscore.range('d', 'a');
        assertEquals("[d, c, b]", result13.toString());
        final List<Character> result14 = Underscore.range('d', 'a', 0);
        assertEquals("[]", result14.toString());
    }

    /*
    _.lastIndex([1, 2, 3, 4, 5]);
    => 4
    */
    @Test
    void lastIndex() {
        assertEquals(4, Underscore.lastIndex(asList(1, 2, 3, 4, 5)));
        assertEquals(4, Underscore.lastIndex(new Integer[] {1, 2, 3, 4, 5}));
        assertEquals(4, Underscore.lastIndex(new int[] {1, 2, 3, 4, 5}));
    }
}
