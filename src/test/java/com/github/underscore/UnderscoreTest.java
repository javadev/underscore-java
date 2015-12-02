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
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Underscore library unit test.
 *
 * @author Valentyn Kolesnikov
 */
public class UnderscoreTest {

    @Test
    @SuppressWarnings("unchecked")
    public void main() {
        $.main(new String[] {});
        new $(asList("")).getIterable();
        new $(asList("")).value();
        new $("").getString();
    }

/*
['some', 'words', 'example'].sort();
=> ['example', 'some', 'words']
*/
    @Test
    @SuppressWarnings("unchecked")
    public void sort() {
        assertEquals("[example, some, words]", $.sort(asList("some", "words", "example")).toString());
        assertEquals("[example, some, words]", new $(asList("some", "words", "example")).sort().toString());
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
    @SuppressWarnings("unchecked")
    public void join() {
        assertEquals("some-words-example", $.join(asList("some", "words", "example"), "-"));
        assertEquals("some-words-example", new $(asList("some", "words", "example")).join("-"));
        assertEquals("some-words-example", $.join(new String[] {"some", "words", "example"}, "-"));
        assertEquals("some-words-example", $.chain(asList("some", "words", "example")).join("-").item());
        assertEquals("some words example", $.join(asList("some", "words", "example")));
        assertEquals("some words example", new $(asList("some", "words", "example")).join());
        assertEquals("some words example", $.join(new String[] {"some", "words", "example"}));
        assertEquals("some words example", $.chain(asList("some", "words", "example")).join().item());
    }

    @Test
    public void compareStrings() {
        assertArrayEquals($.sort("CAT".split("")), $.sort("CTA".split("")));
    }

/*
_.concat([1, 2], [3, 4]);
=> [1, 2, 3, 4]
*/
    @Test
    @SuppressWarnings("unchecked")
    public void concat() {
        assertEquals(asList(1, 2, 3, 4), asList($.concat(new Integer[] {1, 2}, new Integer[] {3, 4})));
        assertEquals(asList(1, 2, 3, 4), $.concat(asList(1, 2), asList(3, 4)));
        assertEquals(asList(1, 2, 3, 4), new $(asList(1, 2)).concatWith(asList(3, 4)));
        assertEquals("[1, 2, 3, 4]", $.chain(asList(1, 2)).concat(asList(3, 4)).value().toString());
        assertEquals("[1, 2, 3, 4, 5, 6]", $.chain(asList(1, 2)).concat(asList(3, 4), asList(5, 6)).value().toString());
        assertEquals(asList(1, 2, 3, 4), asList($.concat(new Integer[] {1, 2}, new Integer[] {3}, new Integer[] {4})));
        assertEquals(asList(1, 2, 3, 4), $.concat(asList(1, 2), asList(3), asList(4)));
        assertEquals(asList(1, 2, 3, 4), new $(asList(1, 2)).concatWith(asList(3), asList(4)));
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
    public void slice() {
        assertEquals(asList(3, 4, 5), $.slice(asList(1, 2, 3, 4, 5), 2));
        assertEquals(asList(3, 4, 5), new $(asList(1, 2, 3, 4, 5)).slice(2));
        assertEquals(asList(2, 3, 4), $.slice(asList(1, 2, 3, 4, 5), 1, 4));
        assertEquals(asList("a", "b"), $.slice(asList("a", "b", "c", "d"), 0, 2));
        assertEquals(asList(2, 3, 4), $.slice(asList(1, 2, 3, 4, 5), 1, -1));
        assertEquals(asList(3), $.slice(asList(1, 2, 3, 4, 5), 2, 3));
        assertEquals(asList(3), new $(asList(1, 2, 3, 4, 5)).slice(2, 3));
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
    @SuppressWarnings("unchecked")
    public void reverse() {
        assertEquals("[3, 2, 1]", $.reverse(asList(1, 2, 3)).toString());
        assertEquals("[3, 2, 1]", new $(asList(1, 2, 3)).reverse().toString());
        assertEquals("[3, 2, 1]", asList($.reverse(new Integer[] {1, 2, 3})).toString());
        assertEquals("[3, 2, 1]", $.chain(asList(1, 2, 3)).reverse().value().toString());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void classForNameWithoutGuava() {
        $.setClassForName(new $.ClassForName() {
            public Class<?> call(final String name) throws Exception {
                throw new Exception("expected");
            }
        });
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
        $.map((new LinkedHashMap<Integer, String>() { { put(1, "one"); put(2, "two"); put(3, "three"); } }).entrySet(),
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
        final Optional<Integer> result8 = $.findLast(iterable,
            new Predicate<Integer>() {
            public Boolean apply(Integer item) {
                return item % 2 == 0;
            }
        });
        assertEquals("Optional.of(6)", result8.toString());
        $.setClassForName(new $.ClassForName());
    }

    @Test
    public void optional() {
        assertTrue(Optional.absent().equals(Optional.absent()));
        assertTrue(Optional.of(1).equals(Optional.of(1)));
        Optional<Integer> one = Optional.of(1);
        assertTrue(one.equals(one));
        assertFalse(Optional.of(1L).equals(Optional.of(1)));
        assertFalse(Optional.of(1L).equals(null));
        assertTrue(Optional.of(null).equals(Optional.of(null)));
        assertFalse(Optional.absent().equals(Optional.of(1)));
        assertFalse(Optional.of(null).equals(Optional.of(1)));
        assertFalse(Optional.of(1).equals(Optional.of(null)));
        assertFalse(Optional.of(1).equals(Optional.absent()));
        assertFalse(Optional.of(1).equals(Optional.of(2)));
        assertFalse(Optional.of(1).equals("test"));
        assertEquals(1, Optional.absent().hashCode());
        assertEquals(Optional.of("123").hashCode(), Optional.of("123").hashCode());
        assertEquals("Optional.absent()", Optional.absent().toString());
        assertEquals("Optional.of(1)", Optional.of(1).toString());
        assertEquals("Optional.absent()", Optional.fromNullable(null).toString());
        assertEquals("Optional.of(1)", Optional.fromNullable(1).toString());
        assertEquals("1", Optional.absent().or(1).toString());
        assertEquals("1", Optional.of(1).or(2).toString());
        assertEquals(null, Optional.absent().orNull());
        assertEquals("1", Optional.of(1).orNull().toString());
        try {
            Optional.absent().get();
            fail("IllegalStateException expected");
        } catch (IllegalStateException ex) {
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void stackoverflow() {
        // http://stackoverflow.com/questions/109383/how-to-sort-a-mapkey-value-on-the-values-in-java?rq=1
        assertEquals("{D=67.3, B=67.4, C=67.4, A=99.5}", $.chain((new LinkedHashMap<String, Double>() { {
            put("A", 99.5);
            put("B", 67.4);
            put("C", 67.4);
            put("D", 67.3);
        } }).entrySet()).sortBy(new Function1<Map.Entry<String, Double>, Double>() {
            public Double apply(Map.Entry<String, Double> item) {
                return item.getValue();
            }
        }).toMap().item().toString());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void stackoverflow2() {
        // http://stackoverflow.com/questions/12229577/java-hashmap-sorting-string-integer-how-to-sort-it?lq=1
        assertEquals("{a=5, f=5, c=4, e=3, b=2, d=2}", $.chain((new LinkedHashMap<String, Integer>() { {
            put("a", 5);
            put("b", 2);
            put("c", 4);
            put("d", 2);
            put("e", 3);
            put("f", 5);
        } }).entrySet()).sortBy(new Function1<Map.Entry<String, Integer>, Integer>() {
            public Integer apply(Map.Entry<String, Integer> item) {
                return -item.getValue();
            }
        }).toMap().item().toString());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void stackoverflow3() {
// http://stackoverflow.com/questions/11647889/sorting-the-mapkey-value-in-descending-order-based-on-the-value?lq=1
        assertEquals("{C=50, A=34, B=25}", $.chain((new LinkedHashMap<String, Integer>() { {
            put("A", 34);
            put("B", 25);
            put("C", 50);
        } }).entrySet()).sortBy(new Function1<Map.Entry<String, Integer>, Integer>() {
            public Integer apply(Map.Entry<String, Integer> item) {
                return -item.getValue();
            }
        }).toMap().item().toString());
    }

    @Test
    public void stackoverflow4() {
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
        assertEquals("[1, 2]", $.pluck(asList(entity1, entity2), "getId").toString());
    }

    @Test
    public void stackoverflow5() {
        // http://stackoverflow.com/questions/4349369/list-intersection-in-java
        List<Integer> original = asList(12, 16, 17, 19, 101);
        List<Integer> selected = asList(16, 19, 107, 108, 109);
        assertEquals("[107, 108, 109]", $.difference(selected, $.intersection(original, selected)).toString());
        assertEquals("[12, 17, 101]", $.difference(original, selected).toString());
    }
}
