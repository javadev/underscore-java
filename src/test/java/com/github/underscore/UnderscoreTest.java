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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Underscore library unit test.
 *
 * @author Valentyn Kolesnikov
 */
@SuppressWarnings({"java:S5785", "java:S5778"})
class UnderscoreTest {

    @Test
    @SuppressWarnings("unchecked")
    void testMain() {
        Underscore.main(new String[] {});
        new Underscore<>(singletonList("")).getIterable();
        assertEquals("[]", new Underscore<>(singletonList("")).value().toString());
        new Underscore<>("");
    }

    /*
    ['some', 'words', 'example'].sort();
    => ['example', 'some', 'words']
    */
    @Test
    @SuppressWarnings("unchecked")
    void sort() {
        assertEquals(
                "[example, some, words]",
                Underscore.sort(asList("some", "words", "example")).toString());
        assertEquals(
                "[example, some, words]",
                new Underscore<>(asList("some", "words", "example")).sort().toString());
        assertEquals(
                "[example, some, words]",
                Underscore.chain(asList("some", "words", "example")).sort().value().toString());
        assertEquals(
                "[4, 5, 7]",
                Underscore.chain(asList("some", "words", "example"))
                        .map(String::length)
                        .sort()
                        .value()
                        .toString());
        assertEquals(
                "[example, some, words]",
                asList(Underscore.sort(new String[] {"some", "words", "example"})).toString());
    }

    /*
    ['some', 'words', 'example'].join('-');
    => 'some-words-example'
    */
    @Test
    @SuppressWarnings("unchecked")
    void join() {
        assertEquals(
                "some-words-example", Underscore.join(asList("some", "words", "example"), "-"));
        assertEquals(
                "some-words-example",
                new Underscore<>(asList("some", "words", "example")).join("-"));
        assertEquals(
                "some-words-example",
                Underscore.join(new String[] {"some", "words", "example"}, "-"));
        assertEquals(
                "some-words-example",
                Underscore.chain(asList("some", "words", "example")).join("-").item());
        assertEquals("some words example", Underscore.join(asList("some", "words", "example")));
        assertEquals(
                "some words example", new Underscore<>(asList("some", "words", "example")).join());
        assertEquals(
                "some words example", Underscore.join(new String[] {"some", "words", "example"}));
        assertEquals(
                "some words example",
                Underscore.chain(asList("some", "words", "example")).join().item());
        assertEquals("--", Underscore.join(asList("", "", ""), "-"));
    }

    @Test
    void joinToString() {
        assertEquals("[]", Underscore.joinToString(List.of(), ",", "[", "]", 3, "...", null));
        assertEquals(
                "[1,2,3]",
                Underscore.joinToString(List.of(1, 2, 3), ",", "[", "]", -1, "...", null));
        assertEquals(
                "[1,2,3]",
                Underscore.joinToString(List.of(1, 2, 3), ",", "[", "]", 3, "...", null));
        assertEquals(
                "[1,2,3,...]",
                Underscore.joinToString(List.of(1, 2, 3, 4, 5), ",", "[", "]", 3, "...", null));
        Function<Integer, String> transform = i -> "Value-" + i;
        assertEquals(
                "[Value-1,Value-2,Value-3]",
                Underscore.joinToString(List.of(1, 2, 3), ",", "[", "]", -1, "...", transform));
        assertEquals(
                "[1,2,3]",
                Underscore.joinToString(List.of(1, 2, 3), ",", "[", "]", -1, "...", null));
        assertEquals(
                "{1, 2, 3}",
                Underscore.joinToString(List.of(1, 2, 3), ", ", "{", "}", -1, "...", null));
        assertEquals(
                "1, 2, ...",
                Underscore.joinToString(List.of(1, 2, 3, 4), ", ", "", "", 2, "...", null));
        assertEquals(
                "1, 2, ...",
                Underscore.joinToString(List.of(1, 2, 3, 4), ", ", null, null, 2, "...", null));
        assertEquals(
                "1, 2, ...",
                Underscore.joinToString(List.of(1, 2, 3, 4), ", ", null, null, 2, null, null));
    }

    /*
    _.push(['a', 'b', 'c'], 0, 2);
    // → ['a', 'b', 'c', 0, 2]
    */
    @SuppressWarnings("unchecked")
    @Test
    void push() {
        assertEquals(
                "[a, b, c, 0, 2]", Underscore.push(asList("a", "b", "c"), "0", "2").toString());
        assertEquals(
                "[a, b, c, 0, 2]",
                new Underscore<>(asList("a", "b", "c")).push("0", "2").toString());
        assertEquals(
                "[a, b, c, 0, 2]",
                Underscore.chain(asList("a", "b", "c")).push("0", "2").value().toString());
    }

    /*
    _.pop(['a', 'b', 'c']);
    // → 'c'
    */
    @SuppressWarnings("unchecked")
    @Test
    void pop() {
        assertEquals("c", Underscore.pop(asList("a", "b", "c")).getKey());
        assertEquals("c", new Underscore<>(asList("a", "b", "c")).pop().getKey());
        assertEquals("c", Underscore.chain(asList("a", "b", "c")).pop().item().getKey());
    }

    /*
    _.shift(['a', 'b', 'c']);
    // → 'a'
    */
    @SuppressWarnings("unchecked")
    @Test
    void shift() {
        assertEquals("a", Underscore.shift(asList("a", "b", "c")).getKey());
        assertEquals("a", new Underscore<>(asList("a", "b", "c")).shift().getKey());
        assertEquals("a", Underscore.chain(asList("a", "b", "c")).shift().item().getKey());
    }

    /*
    _.unshift(['a', 'b', 'c'], 0, 2);
    // → [0, 2, 'a', 'b', 'c']
    */
    @SuppressWarnings("unchecked")
    @Test
    void unshift() {
        assertEquals(
                "[0, 2, a, b, c]", Underscore.unshift(asList("a", "b", "c"), "0", "2").toString());
        assertEquals(
                "[0, 2, a, b, c]",
                new Underscore<>(asList("a", "b", "c")).unshift("0", "2").toString());
        assertEquals(
                "[0, 2, a, b, c]",
                Underscore.chain(asList("a", "b", "c")).unshift("0", "2").value().toString());
    }

    @Test
    void compareStrings() {
        assertArrayEquals(Underscore.sort("CAT".split("")), Underscore.sort("CTA".split("")));
    }

    /*
    _.concat([1, 2], [3, 4]);
    => [1, 2, 3, 4]
    */
    @Test
    @SuppressWarnings("unchecked")
    void concat() {
        assertEquals(
                asList(1, 2, 3, 4),
                asList(Underscore.concat(new Integer[] {1, 2}, new Integer[] {3, 4})));
        assertEquals(asList(1.0, 2.0), asList(Underscore.concat(new Double[] {1.0, 2.0})));
        assertEquals(asList(1, 2, 3, 4), Underscore.concat(asList(1, 2), asList(3, 4)));
        assertEquals(asList("a", "b"), Underscore.concat(asList("a", "b")));
        assertEquals(asList(1, 2, 3, 4), new Underscore<>(asList(1, 2)).concatWith(asList(3, 4)));
        assertEquals(
                "[1, 2, 3, 4]",
                Underscore.chain(asList(1, 2)).concat(asList(3, 4)).value().toString());
        assertEquals(
                "[1, 2, 3, 4, 5, 6]",
                Underscore.chain(asList(1, 2))
                        .concat(asList(3, 4), asList(5, 6))
                        .value()
                        .toString());
        assertEquals(
                asList(1, 2, 3, 4),
                asList(
                        Underscore.concat(
                                new Integer[] {1, 2}, new Integer[] {3}, new Integer[] {4})));
        assertEquals(
                asList(1, 2, 3, 4),
                Underscore.concat(asList(1, 2), singletonList(3), singletonList(4)));
        assertEquals(
                asList(1, 2, 3, 4),
                new Underscore<>(asList(1, 2)).concatWith(singletonList(3), singletonList(4)));
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
    @SuppressWarnings("unchecked")
    void slice() {
        assertEquals(asList(3, 4, 5), Underscore.slice(asList(1, 2, 3, 4, 5), 2));
        assertEquals(asList(3, 4, 5), new Underscore<>(asList(1, 2, 3, 4, 5)).slice(2));
        assertEquals(asList(2, 3, 4), Underscore.slice(asList(1, 2, 3, 4, 5), 1, 4));
        assertEquals(asList("a", "b"), Underscore.slice(asList("a", "b", "c", "d"), 0, 2));
        assertEquals(asList(2, 3, 4), Underscore.slice(asList(1, 2, 3, 4, 5), 1, -1));
        assertEquals(singletonList(3), Underscore.slice(asList(1, 2, 3, 4, 5), 2, 3));
        assertEquals(singletonList(3), new Underscore<>(asList(1, 2, 3, 4, 5)).slice(2, 3));
        assertEquals(asList(4, 5), Underscore.slice(asList(1, 2, 3, 4, 5), -2));
        assertEquals(asList(3, 4), Underscore.slice(asList(1, 2, 3, 4, 5), -3, -1));
        assertEquals(asList(3, 4), Underscore.slice(asList(1, 2, 3, 4, 5), -3, 4));
        assertEquals(asList(3, 4, 5), Underscore.chain(asList(1, 2, 3, 4, 5)).slice(2).value());
        assertEquals(asList(2, 3, 4), Underscore.chain(asList(1, 2, 3, 4, 5)).slice(1, 4).value());
        assertEquals(asList(3, 4, 5), asList(Underscore.slice(new Integer[] {1, 2, 3, 4, 5}, 2)));
        assertEquals(asList(4, 5), asList(Underscore.slice(new Integer[] {1, 2, 3, 4, 5}, -2)));
        assertEquals(
                asList(2, 3, 4), asList(Underscore.slice(new Integer[] {1, 2, 3, 4, 5}, 1, 4)));
        assertEquals(
                asList(2, 3, 4), asList(Underscore.slice(new Integer[] {1, 2, 3, 4, 5}, 1, -1)));
        assertEquals(asList(3, 4), asList(Underscore.slice(new Integer[] {1, 2, 3, 4, 5}, -3, 4)));
        assertEquals(asList(3, 4), asList(Underscore.slice(new Integer[] {1, 2, 3, 4, 5}, -3, -1)));
    }

    /*
    _.splitAt([1, 2, 3, 4, 5], 2);
    => [[1, 2], [3, 4, 5]]
    _.splitAt([1, 2, 3, 4, 5], 0);
    => [[], [1, 2, 3, 4, 5]]
    _.splitAt([1, 2, 3, 4, 5], 20000);
    => [[1, 2, 3, 4, 5], []]
    _.splitAt([1, 2, 3, 4, 5], -1000);
    => [[], [1, 2, 3, 4, 5]]
    _.splitAt([], 0);
    => [[], []]
    */
    @Test
    void splitAt() {
        assertEquals("[[0, 1], [2, 3, 4]]", Underscore.splitAt(Underscore.range(5), 2).toString());
        assertEquals(
                "[[], [0, 1, 2, 3, 4]]", Underscore.splitAt(Underscore.range(5), 0).toString());
        assertEquals(
                "[[0, 1, 2, 3, 4], []]", Underscore.splitAt(Underscore.range(5), 20000).toString());
        assertEquals(
                "[[], [0, 1, 2, 3, 4]]", Underscore.splitAt(Underscore.range(5), -1000).toString());
        assertEquals("[[], []]", Underscore.splitAt(Underscore.newIntegerList(), 0).toString());
        assertEquals(
                "[[0, 1], [2, 3, 4]]", new Underscore<>(Underscore.range(5)).splitAt(2).toString());
        assertEquals(
                "[[0, 1], [2, 3, 4]]",
                Underscore.chain(Underscore.range(5)).splitAt(2).value().toString());
        assertEquals(
                "[[a, b], [c, d, e]]",
                Underscore.splitAt(asList('a', 'b', 'c', 'd', 'e'), 2).toString());
        assertEquals(
                "[[ant, bird], [camel, dog, elephant]]",
                Underscore.splitAt(asList("ant", "bird", "camel", "dog", "elephant"), 2)
                        .toString());
        assertEquals(
                "[[0.1, 0.2], [0.3, 0.4, 0.5]]",
                Underscore.splitAt(asList(0.1, 0.2, 0.3, 0.4, 0.5), 2).toString());
        final Integer[] array = {0, 1, 2, 3, 4};
        assertEquals("[[0, 1], [2, 3, 4]]", Underscore.splitAt(array, 2).toString());
    }

    /*
    _.takeSkipping([1, 2, 3, 4, 5], 2);
    => [0, 2, 4]
    _.takeSkipping([1, 2, 3, 4, 5], 100000);
    => [0]
    _.takeSkipping([1, 2, 3, 4, 5], -100);
    => []
    */
    @Test
    void takeSkipping() {
        assertEquals("[0, 2, 4]", Underscore.takeSkipping(Underscore.range(5), 2).toString());
        assertEquals("[0]", Underscore.takeSkipping(Underscore.range(5), 100000).toString());
        assertEquals("[]", Underscore.takeSkipping(Underscore.range(5), -100).toString());
        assertEquals("[]", Underscore.takeSkipping(Underscore.range(5), 0).toString());
        assertEquals("[0, 2, 4]", new Underscore<>(Underscore.range(5)).takeSkipping(2).toString());
        assertEquals(
                "[0, 2, 4]",
                Underscore.chain(Underscore.range(5)).takeSkipping(2).value().toString());
        assertEquals(
                "[a, c, e]",
                Underscore.takeSkipping(asList('a', 'b', 'c', 'd', 'e'), 2).toString());
        assertEquals(
                "[ant, camel, elephant]",
                Underscore.takeSkipping(asList("ant", "bird", "camel", "dog", "elephant"), 2)
                        .toString());
        assertEquals(
                "[0.1, 0.3, 0.5]",
                Underscore.takeSkipping(asList(0.1, 0.2, 0.3, 0.4, 0.5), 2).toString());
        final Integer[] array = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        assertEquals("[0, 3, 6, 9]", Underscore.takeSkipping(array, 3).toString());
    }

    /*
    var arr = [ 1, 2, 3 ]
    _.copyOf(arr) // => [1, 2, 3]
    */
    @Test
    @SuppressWarnings("unchecked")
    void copyOf() {
        assertEquals(asList(1, 2, 3), Underscore.copyOf(asList(1, 2, 3)));
        assertEquals(asList(1, 2, 3), new Underscore<>(asList(1, 2, 3)).copyOf());
        assertNotSame(asList(1, 2, 3), Underscore.copyOf(asList(1, 2, 3)));
        assertEquals(asList("a", "b"), Underscore.copyOfRange(asList("a", "b", "c", "d"), 0, 2));
        assertEquals(
                asList("a", "b"), new Underscore<>(asList("a", "b", "c", "d")).copyOfRange(0, 2));
    }

    /*
    var arr = [ 1, 2, 3 ]
    _.elementAt(arr, 1) // => 2
    */
    @Test
    void elementAt() {
        assertEquals(2, Underscore.<Integer>elementAt(asList(1, 2, 3), 1).intValue());
        assertEquals(2, new Underscore<>(asList(1, 2, 3)).elementAt(1).intValue());
    }

    /*
    var arr = [ 1, 2, 3 ]
    _.get(arr, 1) // => 2
    */
    @Test
    void get() {
        assertEquals(2, Underscore.<Integer>get(asList(1, 2, 3), 1).intValue());
        assertEquals(2, new Underscore<>(asList(1, 2, 3)).get(1).intValue());
    }

    /*
    var arr = [ 1, 2, 3 ]
    _.set(arr, 1, 100) // => 2
    */
    @Test
    void set() {
        Map.Entry<Integer, List<Integer>> result = Underscore.set(asList(1, 2, 3), 1, 100);
        assertEquals(2, result.getKey().intValue());
        assertEquals(100, Underscore.<Integer>get(result.getValue(), 1).intValue());
        Map.Entry<Integer, List<Integer>> result2 = new Underscore<>(asList(1, 2, 3)).set(2, 200);
        assertEquals(3, result2.getKey().intValue());
        assertEquals(200, result2.getValue().get(2).intValue());
    }

    /*
    var arr = [ 1, 2, 3 ]
    _.elementAt(arr, 3) // => IndexOutOfBoundsException
    */
    @Test
    void elementAtOutOfBounds() {
        List<Integer> integers = asList(1, 2, 3);
        assertThrows(
                IndexOutOfBoundsException.class, () -> Underscore.<Integer>elementAt(integers, 3));
    }

    /*
    var arr = [ 1, 2, 3 ]
    _.elementAtOrElse(arr, 1, 0) // => 2
    _.elementAtOrElse(arr, 3, 0) // => 0
    */
    @Test
    void elementAtOrElse() {
        assertEquals(2, Underscore.<Integer>elementAtOrElse(asList(1, 2, 3), 1, 0).intValue());
        assertEquals(2, new Underscore<>(asList(1, 2, 3)).elementAtOrElse(1, 0).intValue());
        assertEquals(0, Underscore.<Integer>elementAtOrElse(asList(1, 2, 3), 3, 0).intValue());
        assertEquals(0, new Underscore<>(asList(1, 2, 3)).elementAtOrElse(3, 0).intValue());
    }

    /*
    var arr = [ 1, 2, 3 ]
    _.elementAtOrNull(arr, 1) // => 2
    _.elementAtOrNull(arr, 3) // => null
    */
    @Test
    void elementAtOrNull() {
        assertEquals(2, Underscore.<Integer>elementAtOrNull(asList(1, 2, 3), 1).intValue());
        assertEquals(2, new Underscore<>(asList(1, 2, 3)).elementAtOrNull(1).intValue());
        assertNull(Underscore.<Integer>elementAtOrNull(asList(1, 2, 3), 3));
        assertNull(new Underscore<>(asList(1, 2, 3)).elementAtOrNull(3));
    }

    /*
    [1, 2, 3].reverse() // [3, 2, 1]
    */
    @Test
    @SuppressWarnings("unchecked")
    void reverse() {
        assertEquals("[3, 2, 1]", Underscore.reverse(asList(1, 2, 3)).toString());
        assertEquals("[3, 2, 1]", Underscore.reverse(new int[] {1, 2, 3}).toString());
        assertEquals("[3, 2, 1]", new Underscore(asList(1, 2, 3)).reverse().toString());
        assertEquals("[3, 2, 1]", asList(Underscore.reverse(new Integer[] {1, 2, 3})).toString());
        assertEquals("[3, 2, 1]", Underscore.chain(asList(1, 2, 3)).reverse().value().toString());
    }

    @Test
    void findLastWithCustomIterable() {
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
                                // ignored
                            }
                        };
        final Optional<Integer> result = Underscore.findLast(iterable, item -> item % 2 == 0);
        assertEquals("Optional[6]", result.toString());
    }

    @Test
    void iterate() {
        Iterable<long[]> iterable =
                Underscore.iterate(new long[] {1, 1}, arg -> new long[] {arg[1], arg[0] + arg[1]});
        iterable.iterator().remove();
        assertTrue(iterable.iterator().hasNext());
        assertArrayEquals(new long[] {1, 1}, iterable.iterator().next());
        assertArrayEquals(new long[] {1, 2}, iterable.iterator().next());
    }

    @Test
    void iterateChain() {
        Iterable<long[]> iterable =
                Underscore.iterate(new long[] {1, 1}, arg -> new long[] {arg[1], arg[0] + arg[1]});
        assertEquals(1L, Underscore.chain(iterable, 5).first().item()[0]);
        Underscore.of(iterable, 5);
        class MyIterable<T> implements Iterable<T> {
            public Iterator<T> iterator() {
                return new Iterator<>() {
                    @Override
                    public boolean hasNext() {
                        return false;
                    }

                    @Override
                    public T next() {
                        return null;
                    }

                    @Override
                    public void remove() {
                        // ignored
                    }
                };
            }
        }
        assertTrue(Underscore.chain(new MyIterable<Integer>(), 5).isEmpty());
    }

    @Test
    @SuppressWarnings({"unlikely-arg-type", "java:S5961"})
    void optional() {
        assertTrue(Optional.empty().equals(Optional.empty()));
        assertTrue(Optional.of(1).equals(Optional.of(1)));
        Optional<Integer> one = Optional.of(1);
        assertTrue(one.equals(one));
        assertFalse(Optional.of(1L).equals(Optional.of(1)));
        assertFalse(Optional.of(1L).equals(null));
        assertTrue(Optional.ofNullable(null).equals(Optional.ofNullable(null)));
        assertFalse(Optional.empty().equals(Optional.of(1)));
        assertFalse(Optional.ofNullable(null).equals(Optional.of(1)));
        assertFalse(Optional.of(1).equals(Optional.ofNullable(null)));
        assertFalse(Optional.of(1).equals(Optional.empty()));
        assertFalse(Optional.of(1).equals(Optional.of(2)));
        assertFalse(Optional.of(1).equals("test"));
        assertEquals(0, Optional.empty().hashCode());
        assertEquals(Optional.of("123").hashCode(), Optional.of("123").hashCode());
        assertEquals("Optional.empty", Optional.empty().toString());
        assertEquals("Optional[1]", Optional.of(1).toString());
        assertEquals("Optional.empty", Optional.ofNullable(null).toString());
        assertEquals("Optional[1]", Optional.ofNullable(1).toString());
        assertEquals("1", Optional.<Integer>empty().orElse(1).toString());
        assertEquals("1", Optional.of(1).orElse(2).toString());
        assertNull(Optional.empty().orElse(null));
        assertEquals("1", Optional.of(1).orElse(null).toString());
        assertFalse(Optional.<Integer>empty().map(arg -> "" + arg).isPresent());
        assertTrue(Optional.<Integer>empty().map(arg -> "" + arg).isEmpty());
        assertEquals("1", Optional.of(1).map(arg -> "" + arg).get());
        try {
            Optional.empty().get();
            fail("IllegalStateException expected");
        } catch (NoSuchElementException ignored) {
            // ignored
        }
        assertFalse(Optional.<Integer>empty().filter(arg -> true).isPresent());
        assertTrue(Optional.<Integer>empty().filter(arg -> false).isEmpty());
        assertEquals("1", Optional.of(1).filter(arg -> true).get().toString());
        assertTrue(Optional.of(1).filter(arg -> false).isEmpty());
    }

    @Test
    void optionalOrThrow() throws RuntimeException {
        assertThrows(Exception.class, () -> Optional.empty().orElseThrow(RuntimeException::new));
    }

    @Test
    void optionalOrThrowWithValue() {
        assertEquals("1", Optional.of(1).orElseThrow(RuntimeException::new).toString());
    }

    @Test
    void checkNotNull() {
        assertThrows(NullPointerException.class, () -> Underscore.checkNotNull(null));
    }

    @Test
    void checkNotNullWithObject() {
        assertEquals("123", Underscore.checkNotNull("123"));
    }

    @Test
    void checkNotNullWithMessage() {
        assertThrows(
                NullPointerException.class, () -> Underscore.checkNotNull(null, "Error message"));
    }

    @Test
    void checkNotNull2() {
        assertThrows(NullPointerException.class, () -> Underscore.checkNotNullElements(null));
    }

    @Test
    void checkNotNullWithObjectAndMessage() {
        assertEquals("123", Underscore.checkNotNull("123", "Error message"));
    }

    @Test
    void nonNull() {
        assertFalse(Underscore.nonNull(null));
        assertTrue(Underscore.nonNull(""));
    }

    @Test
    void defaultTo() {
        assertNull(Underscore.defaultTo(null, null));
    }

    @Test
    void defaultTo2() {
        assertEquals("123", Underscore.defaultTo("123", "124"));
    }

    @Test
    @SuppressWarnings("unchecked")
    void and() {
        Predicate<Integer> predicate =
                Underscore.and(
                        (Predicate<Object>) Objects::nonNull,
                        (Predicate<Number>) value -> value.intValue() > 0,
                        value -> (50 <= value) && (value <= 60));
        assertTrue(predicate.test(50));
        assertFalse(predicate.test(null));
        assertFalse(predicate.test(-56));
        assertTrue(predicate.test(60));
        assertFalse(predicate.test(62));
        assertFalse(predicate.test(1002));
    }

    @Test
    @SuppressWarnings("unchecked")
    void or() {
        Predicate<Integer> predicate =
                Underscore.or(
                        (Predicate<Object>) Objects::isNull,
                        (Predicate<Number>) value -> value.intValue() > 2000,
                        value -> (50 <= value) && (value <= 60));
        assertTrue(predicate.test(50));
        assertTrue(predicate.test(55));
        assertTrue(predicate.test(60));
        assertTrue(predicate.test(null));
        assertFalse(predicate.test(1001));
        assertFalse(predicate.test(1001));
        assertTrue(predicate.test(2001));
    }

    @Test
    void stackoverflow() {
        // http://stackoverflow.com/questions/109383/how-to-sort-a-mapkey-value-on-the-values-in-java?rq=1
        assertEquals(
                "{D=67.3, B=67.4, C=67.4, A=99.5}",
                Underscore.chain(
                                (new LinkedHashMap<String, Double>() {
                                            {
                                                put("A", 99.5);
                                                put("B", 67.4);
                                                put("C", 67.4);
                                                put("D", 67.3);
                                            }
                                        })
                                        .entrySet())
                        .sortBy(Map.Entry::getValue)
                        .toMap()
                        .item()
                        .toString());
    }

    @Test
    void stackoverflow2() {
        // http://stackoverflow.com/questions/12229577/java-hashmap-sorting-string-integer-how-to-sort-it?lq=1
        assertEquals(
                "{a=5, f=5, c=4, e=3, b=2, d=2}",
                Underscore.chain(
                                (new LinkedHashMap<String, Integer>() {
                                            {
                                                put("a", 5);
                                                put("b", 2);
                                                put("c", 4);
                                                put("d", 2);
                                                put("e", 3);
                                                put("f", 5);
                                            }
                                        })
                                        .entrySet())
                        .sortBy(item -> -item.getValue())
                        .toMap()
                        .item()
                        .toString());
    }

    @Test
    void stackoverflow3() {
        // http://stackoverflow.com/questions/11647889/sorting-the-mapkey-value-in-descending-order-based-on-the-value?lq=1
        assertEquals(
                "{C=50, A=34, B=25}",
                Underscore.chain(
                                (new LinkedHashMap<String, Integer>() {
                                            {
                                                put("A", 34);
                                                put("B", 25);
                                                put("C", 50);
                                            }
                                        })
                                        .entrySet())
                        .sortBy(item -> -item.getValue())
                        .toMap()
                        .item()
                        .toString());
    }

    @Test
    void stackoverflow4() {
        // http://stackoverflow.com/questions/23812947/
        // most-efficient-way-to-find-the-collection-of-all-ids-in-a-collection-of-entities?rq=1
        class Entity {
            private long id;
            private String data;

            public Entity(long id, String data) {
                this.id = id;
                this.data = data;
            }

            public long getId() {
                return id;
            }

            public String getData() {
                return data;
            }
        }
        Entity entity1 = new Entity(1, "one");
        Entity entity2 = new Entity(2, "two");
        assertEquals("[1, 2]", Underscore.pluck(asList(entity1, entity2), "getId").toString());
    }

    @Test
    void stackoverflow5() {
        // http://stackoverflow.com/questions/4349369/list-intersection-in-java
        List<Integer> original = asList(12, 16, 17, 19, 101);
        List<Integer> selected = asList(16, 19, 107, 108, 109);
        assertEquals(
                "[107, 108, 109]",
                Underscore.difference(selected, Underscore.intersection(original, selected))
                        .toString());
        assertEquals("[12, 17, 101]", Underscore.difference(original, selected).toString());
    }

    @Test
    void jobtest() {
        String[] strings = {
            "Sound boy proceed to blast into the galaxy",
            "Go back rocket man into the sky you'll see",
            "Hear it all the time, come back rewind",
            "Aliens are watching up in the sky",
            "Sound boy process to blast into the galaxy",
            "No one gonna harm you",
            "They all want you to play I watch the birds of prey"
        };
        List<Map<String, Object>> result =
                Underscore.chain(asList(strings))
                        .map(
                                item -> {
                                    Map<String, Object> resultItem = new LinkedHashMap<>();
                                    resultItem.put("string", item);
                                    resultItem.put(
                                            "longestWord",
                                            Underscore.chain(asList(item.split("\\s+")))
                                                    .map(String::length)
                                                    .max()
                                                    .item());
                                    return resultItem;
                                })
                        .sortBy(item -> -((Integer) item.get("longestWord")))
                        .limit(5)
                        .value();
        assertEquals(
                "[{string=Aliens are watching up in the sky, longestWord=8}, "
                        + "{string=Sound boy proceed to blast into the galaxy, longestWord=7}, "
                        + "{string=Sound boy process to blast into the galaxy, longestWord=7}, "
                        + "{string=Go back rocket man into the sky you'll see, longestWord=6}, "
                        + "{string=Hear it all the time, come back rewind, longestWord=6}]",
                result.toString());
    }

    @Test
    void testPropertiesToMap() {
        Properties properties = new Properties();
        properties.setProperty("key1", "value1");
        properties.setProperty("key2", "value2");
        properties.setProperty("key3", "value3");
        Map<String, Object> map = U.propertiesToMap(properties);
        assertEquals(3, map.size());
        assertEquals("value1", map.get("key1"));
        assertEquals("value2", map.get("key2"));
        assertEquals("value3", map.get("key3"));
    }

    @Test
    void testPropertiesToMapWithEmptyProperties() {
        Properties properties = new Properties();
        Map<String, Object> map = U.propertiesToMap(properties);
        assertEquals(0, map.size());
        Map<String, Object> map2 = U.propertiesToMap(null);
        assertEquals(0, map2.size());
    }

    @Test
    void testMapToProperties() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", "value3");
        Properties properties = U.mapToProperties(map);
        assertEquals(3, properties.size());
        assertEquals("value1", properties.getProperty("key1"));
        assertEquals("value2", properties.getProperty("key2"));
        assertEquals("value3", properties.getProperty("key3"));
    }

    @Test
    void testMapToPropertiesWithNullValues() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("key1", "value1");
        map.put("key2", null);
        map.put("key3", "value3");
        Properties properties = U.mapToProperties(map);
        assertEquals(2, properties.size());
        assertEquals("value1", properties.getProperty("key1"));
        assertEquals("value3", properties.getProperty("key3"));
        Properties properties2 = U.mapToProperties(null);
        assertEquals(0, properties2.size());
    }

    @Test
    void testRemoveUtf8Bom() {
        // UTF-8 BOM: 0xEF,0xBB,0xBF == -17, -69, -65
        byte[] withBom = new byte[] {-17, -69, -65, 1, 2, 3};
        byte[] expected = new byte[] {1, 2, 3};
        assertArrayEquals(expected, U.removeBom(withBom), "UTF-8 BOM should be removed");
    }

    @Test
    void testRemoveUtf16BeBom() {
        // UTF-16BE BOM: 0xFE,0xFF == -2, -1
        byte[] withBom = new byte[] {-2, -1, 4, 5};
        byte[] expected = new byte[] {4, 5};
        assertArrayEquals(expected, U.removeBom(withBom), "UTF-16BE BOM should be removed");
    }

    @Test
    void testRemoveUtf16LeBom() {
        // UTF-16LE BOM: 0xFF,0xFE == -1, -2
        byte[] withBom = new byte[] {-1, -2, 9};
        byte[] expected = new byte[] {9};
        assertArrayEquals(expected, U.removeBom(withBom), "UTF-16LE BOM should be removed");
    }

    @Test
    void testNotShortBytesNoBom() {
        // Less than 2 bytes (not possible to have BOM)
        byte[] input = new byte[] {42};
        assertArrayEquals(
                input, U.removeBom(input), "Short arrays with no BOM should be unchanged");
    }

    @Test
    void testNoBomPresent() {
        // No BOM present
        byte[] input = new byte[] {3, 5, 0, -1, -7};
        assertArrayEquals(input, U.removeBom(input), "Arrays without BOM should be unchanged");
    }

    @Test
    void testAlmostBomButNotEnoughBytes() {
        // only 2 bytes, not enough for UTF-8 BOM
        byte[] input = new byte[] {-17, -69};
        assertArrayEquals(
                input, U.removeBom(input), "Arrays with too few BOM bytes should be unchanged");
    }

    @Test
    void testPrefixSimilarButNotABom() {
        byte[] input = new byte[] {-1, 0, 1};
        assertArrayEquals(
                input,
                U.removeBom(input),
                "Array starting with -1,0 is not a BOM, should be unchanged");
        input = new byte[] {-2, 0, 1};
        assertArrayEquals(
                input,
                U.removeBom(input),
                "Array starting with -2,0 is not a BOM, should be unchanged");
        // 3 bytes but third is not -65
        input = new byte[] {-17, -69, 0};
        assertArrayEquals(input, U.removeBom(input), "Array with -17,-69,<not -65> is not a BOM");
        input = new byte[] {-17, -69};
        assertArrayEquals(input, U.removeBom(input), "Should not remove BOM for length < 3");
        input = new byte[] {0, -69, -65, 33};
        assertArrayEquals(
                input, U.removeBom(input), "Should not remove BOM if first byte is not -17");
        input = new byte[] {-17, 0, -65, 13};
        assertArrayEquals(
                input, U.removeBom(input), "Should not remove BOM if second byte is not -69");
        input = new byte[] {-17, -69, 0, 14};
        assertArrayEquals(
                input, U.removeBom(input), "Should not remove BOM if third byte is not -65");
    }

    @Test
    void testLengthLessThan4() {
        byte[] buf0 = {};
        byte[] buf1 = {1};
        byte[] buf3 = {1, 2, 3};
        assertEquals("UTF8", U.detectEncoding(buf0), "Should return UTF8 for empty array");
        assertEquals("UTF8", U.detectEncoding(buf1), "Should return UTF8 for buffer length 1");
        assertEquals("UTF8", U.detectEncoding(buf3), "Should return UTF8 for buffer length 3");
    }

    @Test
    void testCase_0x0000FEFF() {
        byte[] buf = {(byte) 0x00, (byte) 0x00, (byte) 0xFE, (byte) 0xFF};
        assertEquals(
                "UTF_32BE", U.detectEncoding(buf), "Should return UTF_32BE for BOM 0x0000FEFF");
    }

    @Test
    void testCase_0x0000003C() {
        byte[] buf = {(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x3C};
        assertEquals("UTF_32BE", U.detectEncoding(buf), "Should return UTF_32BE for 0x0000003C");
    }

    @Test
    void testCase_0x003C003F() {
        byte[] buf = {(byte) 0x00, (byte) 0x3C, (byte) 0x00, (byte) 0x3F};
        assertEquals(
                "UnicodeBigUnmarked",
                U.detectEncoding(buf),
                "Should return UnicodeBigUnmarked for 0x003C003F");
    }

    @Test
    void testCase_0xFFFE0000() {
        byte[] buf = {(byte) 0xFF, (byte) 0xFE, (byte) 0x00, (byte) 0x00};
        assertEquals(
                "UTF_32LE", U.detectEncoding(buf), "Should return UTF_32LE for BOM 0xFFFE0000");
    }

    @Test
    void testCase_0x3C000000() {
        byte[] buf = {(byte) 0x3C, (byte) 0x00, (byte) 0x00, (byte) 0x00};
        assertEquals("UTF_32LE", U.detectEncoding(buf), "Should return UTF_32LE for 0x3C000000");
    }

    @Test
    void testCase_0x3C003F00() {
        byte[] buf = {(byte) 0x3C, (byte) 0x00, (byte) 0x3F, (byte) 0x00};
        assertEquals(
                "UnicodeLittleUnmarked",
                U.detectEncoding(buf),
                "Should return UnicodeLittleUnmarked for 0x3C003F00");
    }

    @Test
    void testCase_0x3C3F786D() {
        byte[] buf = {(byte) 0x3C, (byte) 0x3F, (byte) 0x78, (byte) 0x6D};
        assertEquals("UTF8", U.detectEncoding(buf), "Should return UTF8 for 0x3C3F786D");
    }

    @Test
    void testEfBbBf_UTF8() {
        // 0xEFBBBF??, so n >>> 8 == 0xEFBBBF
        // Let's set: [0xEF, 0xBB, 0xBF, 0x42] (0x42 is arbitrary)
        byte[] buf = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF, (byte) 0x42};
        assertEquals("UTF8", U.detectEncoding(buf), "Should return UTF8 for buffer with UTF-8 BOM");
    }

    @Test
    void test_nShift24_0x3C() {
        // (n >>> 24) == 0x3C, but not matching any above case
        byte[] buf = {(byte) 0x3C, 1, 2, 3};
        assertEquals(
                "UTF8",
                U.detectEncoding(buf),
                "Should return UTF8 when (n >>> 24) == 0x3C and no previous case matches");
    }

    @Test
    void test_nShift16_0xFFFE() {
        // (n >>> 16) == 0xFFFE (UnicodeLittleUnmarked branch)
        byte[] buf = {(byte) 0xFF, (byte) 0xFE, (byte) 0x21, (byte) 0x22};
        assertEquals(
                "UnicodeLittleUnmarked",
                U.detectEncoding(buf),
                "Should return UnicodeLittleUnmarked when (n >> 16) == 0xFFFE");
    }

    @Test
    void test_nShift16_0xFEFF() {
        // (n >>> 16) == 0xFEFF (UnicodeBigUnmarked branch)
        byte[] buf = {(byte) 0xFE, (byte) 0xFF, (byte) 0x99, (byte) 0x88};
        assertEquals(
                "UnicodeBigUnmarked",
                U.detectEncoding(buf),
                "Should return UnicodeBigUnmarked when (n >> 16) == 0xFEFF");
    }

    @Test
    void testDefaultCase() {
        // Random data, not matching any case nor any shift checks. Should default to UTF8
        byte[] buf = {(byte) 0x01, (byte) 0x23, (byte) 0x45, (byte) 0x67};
        assertEquals(
                "UTF8", U.detectEncoding(buf), "Should default to UTF8 for unknown byte patterns");
    }

    @Test
    void testFormatString() {
        // Test with \n line separator
        String input1 = "line1\nline2\nline3";
        assertEquals(
                input1,
                U.formatString(input1, "\n"),
                "Should not modify string when line separator is already \\n");

        // Test with different line separator
        String input2 = "line1\nline2\nline3";
        String expected2 = "line1\r\nline2\r\nline3";
        assertEquals(
                expected2,
                U.formatString(input2, "\r\n"),
                "Should replace \\n with specified line separator");

        // Test with empty string
        assertTrue(U.formatString("", "\n").isEmpty(), "Should handle empty string correctly");

        // Test with no line breaks
        String noBreaks = "text without breaks";
        assertEquals(
                noBreaks,
                U.formatString(noBreaks, "\r\n"),
                "Should not modify string without line breaks");
    }

    @Test
    void testFileXmlToJson(@TempDir Path tempDir) throws IOException {
        // Create temporary files
        Path xmlPath = tempDir.resolve("test.xml");
        Path jsonPath = tempDir.resolve("test.json");

        // Write test XML content
        String xml = "<?xml version=\"1.0\"?><root><item>value</item></root>";
        Files.write(xmlPath, xml.getBytes(StandardCharsets.UTF_8));

        // Test file conversion
        assertDoesNotThrow(
                () -> U.fileXmlToJson(xmlPath.toString(), jsonPath.toString()),
                "File conversion should not throw exceptions");

        // Verify the JSON file
        assertTrue(Files.exists(jsonPath), "JSON file should be created");

        String jsonContent = Files.readString(jsonPath);
        assertAll(
                "JSON file content verification",
                () -> assertNotNull(jsonContent, "JSON content should not be null"),
                () ->
                        assertTrue(
                                jsonContent.contains("\"item\": \"value\""),
                                "JSON should contain converted XML content"));
    }

    @Test
    void testFileXmlToJsonWithInvalidInput(@TempDir Path tempDir) {
        Path nonExistentXml = tempDir.resolve("nonexistent.xml");
        Path outputJson = tempDir.resolve("output.json");

        assertThrows(
                IOException.class,
                () -> U.fileXmlToJson(nonExistentXml.toString(), outputJson.toString()),
                "Should throw IOException when input file doesn't exist");
    }

    @Test
    void testStreamXmlToJson_validXml_writesJson() throws IOException {
        String xml = "<root><name>Test</name></root>";
        InputStream xmlStream = new ByteArrayInputStream(xml.getBytes());
        ByteArrayOutputStream jsonStream = new ByteArrayOutputStream();
        U.streamXmlToJson(xmlStream, jsonStream);
        String jsonOutput = jsonStream.toString(StandardCharsets.UTF_8);
        assertTrue(jsonOutput.contains("name"), "JSON output should contain 'name' field.");
        assertTrue(jsonOutput.contains("Test"), "JSON output should contain 'Test' value.");
        assertTrue(jsonOutput.startsWith("{"), "JSON output should start with '{'.");
        assertTrue(jsonOutput.endsWith("}"), "JSON output should end with '}'.");
    }

    @Test
    void testStreamXmlToJson_emptyInput_producesEmptyOrError() {
        InputStream xmlStream = new ByteArrayInputStream(new byte[0]);
        ByteArrayOutputStream jsonStream = new ByteArrayOutputStream();
        Exception exception =
                assertThrows(
                        Exception.class,
                        () ->
                                U.streamXmlToJson(
                                        xmlStream,
                                        jsonStream,
                                        Json.JsonStringBuilder.Step.TWO_SPACES),
                        "Should throw exception for empty input.");
        String msg = exception.getMessage();
        assertNotNull(msg, "Exception message should not be null.");
    }

    @Test
    void testStreamXmlToJson_invalidXml_throwsException() {
        // missing closing tag
        String invalidXml = "<root><name>Test</name>";
        InputStream xmlStream = new ByteArrayInputStream(invalidXml.getBytes());
        ByteArrayOutputStream jsonStream = new ByteArrayOutputStream();
        Exception exception =
                assertThrows(
                        Exception.class,
                        () ->
                                U.streamXmlToJson(
                                        xmlStream,
                                        jsonStream,
                                        Json.JsonStringBuilder.Step.TWO_SPACES),
                        "Should throw exception for invalid XML.");
        String msg = exception.getMessage();
        assertNotNull(msg, "Exception message for invalid XML should not be null.");
    }

    @Test
    void testStreamXmlToJson_withIndentSteps_producesIndentedJson() throws IOException {
        String xml = "<root><field>value</field></root>";
        InputStream xmlStream = new ByteArrayInputStream(xml.getBytes());
        ByteArrayOutputStream jsonStream = new ByteArrayOutputStream();
        U.streamXmlToJson(xmlStream, jsonStream, Json.JsonStringBuilder.Step.FOUR_SPACES);
        String jsonOutput = jsonStream.toString(StandardCharsets.UTF_8);
        assertTrue(jsonOutput.contains("    "), "JSON output should be indented with four spaces.");
    }

    @Test
    void testMapWithEncodingKey(@TempDir Path tempDir) throws IOException {
        // Arrange
        Path jsonFile = tempDir.resolve("in.json");
        Path xmlFile = tempDir.resolve("out.xml");
        String encoding = "UTF-16";
        // Write json
        String jsonText = "{\"#encoding\":\"" + encoding + "\"}";
        Files.write(jsonFile, jsonText.getBytes(StandardCharsets.UTF_8));
        // Act
        U.fileJsonToXml(jsonFile.toString(), xmlFile.toString());
        // Assert
        byte[] xmlBytes = Files.readAllBytes(xmlFile);
        String xmlStr = new String(xmlBytes, encoding);
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-16\"?>"
                        + System.lineSeparator()
                        + "<root></root>",
                xmlStr,
                "Should write XML with provided encoding when #encoding key present");
    }

    @Test
    void testMapWithoutEncodingKey(@TempDir Path tempDir) throws IOException {
        // Arrange
        Path jsonFile = tempDir.resolve("in.json");
        Path xmlFile = tempDir.resolve("out.xml");
        String jsonText = "{}";
        Files.write(jsonFile, jsonText.getBytes(StandardCharsets.UTF_8));
        // Act
        U.fileJsonToXml(
                jsonFile.toString(), xmlFile.toString(), Xml.XmlStringBuilder.Step.TWO_SPACES);
        // Assert
        byte[] xmlBytes = Files.readAllBytes(xmlFile);
        String xmlStr = new String(xmlBytes, StandardCharsets.UTF_8);
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                        + System.lineSeparator()
                        + "<root></root>",
                xmlStr,
                "Should write XML using UTF-8 when #encoding key not present");
    }

    @Test
    void testJsonFolderToXml(@TempDir Path tempDir) throws IOException {
        // Arrange
        Path jsonFile = tempDir.resolve("in.json");
        Path xmlFile = tempDir.resolve("in.xml");
        String jsonText = "{}";
        Files.write(jsonFile, jsonText.getBytes(StandardCharsets.UTF_8));
        Files.write(xmlFile, jsonText.getBytes(StandardCharsets.UTF_8));
        // Act
        U.jsonFolderToXml(jsonFile.getParent().toString(), xmlFile.getParent().toString());
        // Assert
        byte[] xmlBytes = Files.readAllBytes(xmlFile);
        String xmlStr = new String(xmlBytes, StandardCharsets.UTF_8);
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                        + System.lineSeparator()
                        + "<root></root>",
                xmlStr,
                "Should write XML using UTF-8 when #encoding key not present");
    }

    @Test
    void testXmlFolderToJson(@TempDir Path tempDir) throws IOException {
        // Arrange
        Path xmlFile = tempDir.resolve("in.xml");
        Path jsonFile = tempDir.resolve("in.json");
        String xmlText = "<a>1</a>";
        Files.write(xmlFile, xmlText.getBytes(StandardCharsets.UTF_8));
        Files.write(jsonFile, xmlText.getBytes(StandardCharsets.UTF_8));
        // Act
        U.xmlFolderToJson(xmlFile.getParent().toString(), jsonFile.getParent().toString());
        // Assert
        byte[] jsonBytes = Files.readAllBytes(jsonFile);
        String jsonStr = new String(jsonBytes, StandardCharsets.UTF_8);
        assertEquals(
                "{"
                        + System.lineSeparator()
                        + "  \"a\": \"1\","
                        + System.lineSeparator()
                        + "  \"#omit-xml-declaration\": \"yes\""
                        + System.lineSeparator()
                        + "}",
                jsonStr,
                "Should write JSON using UTF-8");
    }

    @Test
    void testListResult(@TempDir Path tempDir) throws IOException {
        // Arrange
        Path jsonFile = tempDir.resolve("in.json");
        Path xmlFile = tempDir.resolve("out.xml");
        Files.write(jsonFile, "[1,2,3]".getBytes(StandardCharsets.UTF_8));
        // Act
        U.fileJsonToXml(
                jsonFile.toString(), xmlFile.toString(), Xml.XmlStringBuilder.Step.TWO_SPACES);
        // Assert
        byte[] xmlBytes = Files.readAllBytes(xmlFile);
        String xmlStr = new String(xmlBytes, StandardCharsets.UTF_8);
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                        + System.lineSeparator()
                        + "<root>"
                        + System.lineSeparator()
                        + "  <element number=\"true\">1</element>"
                        + System.lineSeparator()
                        + "  <element number=\"true\">2</element>"
                        + System.lineSeparator()
                        + "  <element number=\"true\">3</element>"
                        + System.lineSeparator()
                        + "</root>",
                xmlStr,
                "Should write XML using UTF-8 when result is a List");
    }

    @Test
    void testStreamJsonToXml_ObjectMap_DefaultEncoding() throws Exception {
        String inputJson = "{\"root\":\"value\"}";
        ByteArrayInputStream inputStream =
                new ByteArrayInputStream(inputJson.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        U.streamJsonToXml(inputStream, outputStream);
        String expectedXml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                        + System.lineSeparator()
                        + "<root>value</root>";
        String actualXml = outputStream.toString(StandardCharsets.UTF_8);
        assertEquals(
                expectedXml,
                actualXml,
                "XML output should match expected XML for simple JSON object with default encoding");
    }

    @Test
    void testStreamJsonToXml_ObjectMap_CustomEncoding() throws Exception {
        String inputJson = "{\"root\":\"value\",\"#encoding\":\"UTF-16\"}";
        ByteArrayInputStream inputStream =
                new ByteArrayInputStream(inputJson.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        U.streamJsonToXml(inputStream, outputStream, Xml.XmlStringBuilder.Step.TWO_SPACES);
        String expectedXml =
                "<?xml version=\"1.0\" encoding=\"UTF-16\"?>"
                        + System.lineSeparator()
                        + "<root>value</root>";
        String actualXml = outputStream.toString(StandardCharsets.UTF_16);
        assertEquals(
                expectedXml,
                actualXml,
                "XML output should match expected XML for simple JSON object with default encoding");
    }

    @Test
    void testStreamJsonToXml_List() throws Exception {
        String inputJson = "[{\"item\":42}]";
        ByteArrayInputStream inputStream =
                new ByteArrayInputStream(inputJson.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        U.streamJsonToXml(inputStream, outputStream, Xml.XmlStringBuilder.Step.TWO_SPACES);
        String expectedXml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                        + System.lineSeparator()
                        + "<root>"
                        + System.lineSeparator()
                        + "  <element array=\"true\">"
                        + System.lineSeparator()
                        + "    <item number=\"true\">42</item>"
                        + System.lineSeparator()
                        + "  </element>"
                        + System.lineSeparator()
                        + "</root>";
        String actualXml = outputStream.toString(StandardCharsets.UTF_8);
        assertEquals(
                expectedXml,
                actualXml,
                "XML output should match expected XML for JSON array root with default encoding");
    }

    @Test
    void testStreamJsonToXml_InvalidJson_ThrowsException() {
        String inputJson = "invalid";
        ByteArrayInputStream inputStream =
                new ByteArrayInputStream(inputJson.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Exception exception =
                assertThrows(
                        Json.ParseException.class,
                        () ->
                                U.streamJsonToXml(
                                        inputStream,
                                        outputStream,
                                        Xml.XmlStringBuilder.Step.TWO_SPACES));
        String actualMessage = exception.getMessage();
        assertTrue(
                actualMessage.contains("Expected value"),
                "Should throw exception if JSON is invalid");
    }
}
