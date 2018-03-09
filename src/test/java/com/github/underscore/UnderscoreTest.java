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
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
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
            .map(new Function<String, Integer>() {
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
        assertEquals("--", $.join(asList("", "", ""), "-"));
    }

/*
_.push(['a', 'b', 'c'], 0, 2);
// → ['a', 'b', 'c', 0, 2]
*/
    @SuppressWarnings("unchecked")
    @Test
    public void push() {
        assertEquals("[a, b, c, 0, 2]", $.push(asList("a", "b", "c"), "0", "2").toString());
        assertEquals("[a, b, c, 0, 2]", new $(asList("a", "b", "c")).push("0", "2").toString());
        assertEquals("[a, b, c, 0, 2]", $.chain(asList("a", "b", "c")).push("0", "2").value().toString());
    }

/*
_.pop(['a', 'b', 'c']);
// → 'c'
*/
    @SuppressWarnings("unchecked")
    @Test
    public void pop() {
        assertEquals("c", $.pop(asList("a", "b", "c")).fst().toString());
        assertEquals("c", new $(asList("a", "b", "c")).pop().fst().toString());
        assertEquals("c", $.chain(asList("a", "b", "c")).pop().item().fst().toString());
    }

/*
_.shift(['a', 'b', 'c']);
// → 'a'
*/
    @SuppressWarnings("unchecked")
    @Test
    public void shift() {
        assertEquals("a", $.shift(asList("a", "b", "c")).fst().toString());
        assertEquals("a", new $(asList("a", "b", "c")).shift().fst().toString());
        assertEquals("a", $.chain(asList("a", "b", "c")).shift().item().fst().toString());
    }

/*
_.unshift(['a', 'b', 'c'], 0, 2);
// → [0, 2, 'a', 'b', 'c']
*/
    @SuppressWarnings("unchecked")
    @Test
    public void unshift() {
        assertEquals("[0, 2, a, b, c]", $.unshift(asList("a", "b", "c"), "0", "2").toString());
        assertEquals("[0, 2, a, b, c]", new $(asList("a", "b", "c")).unshift("0", "2").toString());
        assertEquals("[0, 2, a, b, c]", $.chain(asList("a", "b", "c")).unshift("0", "2").value().toString());
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
        assertEquals(asList(4, 5), asList($.slice(new Integer[] {1, 2, 3, 4, 5}, -2)));
        assertEquals(asList(2, 3, 4), asList($.slice(new Integer[] {1, 2, 3, 4, 5}, 1, 4)));
        assertEquals(asList(2, 3, 4), asList($.slice(new Integer[] {1, 2, 3, 4, 5}, 1, -1)));
        assertEquals(asList(3, 4), asList($.slice(new Integer[] {1, 2, 3, 4, 5}, -3, 4)));
        assertEquals(asList(3, 4), asList($.slice(new Integer[] {1, 2, 3, 4, 5}, -3, -1)));
    }

/*
var arr = [ 1, 2, 3 ]
_.copyOf(arr) // => [1, 2, 3]
*/
    @Test
    @SuppressWarnings("unchecked")
    public void copyOf() {
        assertEquals(asList(1, 2, 3), $.copyOf(asList(1, 2, 3)));
        assertEquals(asList(1, 2, 3), new $(asList(1, 2, 3)).copyOf());
        assertFalse(asList(1, 2, 3) == $.copyOf(asList(1, 2, 3)));
        assertEquals(asList("a", "b"), $.copyOfRange(asList("a", "b", "c", "d"), 0, 2));
        assertEquals(asList("a", "b"), new $(asList("a", "b", "c", "d")).copyOfRange(0, 2));
    }

/*
var arr = [ 1, 2, 3 ]
_.elementAt(arr, 1) // => 2
*/
    @Test
    @SuppressWarnings("unchecked")
    public void elementAt() {
        assertEquals(2, $.<Integer>elementAt(asList(1, 2, 3), 1).intValue());
        assertEquals(2, new $<Integer>(asList(1, 2, 3)).elementAt(1).intValue());
    }

/*
var arr = [ 1, 2, 3 ]
_.get(arr, 1) // => 2
*/
    @Test
    @SuppressWarnings("unchecked")
    public void get() {
        assertEquals(2, $.<Integer>get(asList(1, 2, 3), 1).intValue());
        assertEquals(2, new $<Integer>(asList(1, 2, 3)).get(1).intValue());
    }

/*
var arr = [ 1, 2, 3 ]
_.set(arr, 1, 100) // => 2
*/
    @Test
    @SuppressWarnings("unchecked")
    public void set() {
        Tuple<Integer, List<Integer>> result = $.<Integer>set(asList(1, 2, 3), 1, 100);
        assertEquals(2, result.fst().intValue());
        assertEquals(100, $.<Integer>get(result.snd(), 1).intValue());
        Tuple<Integer, List<Integer>> result2 = new $<Integer>(asList(1, 2, 3)).set(2, 200);
        assertEquals(3, result2.fst().intValue());
        assertEquals(200, result2.snd().get(2).intValue());
    }

/*
var arr = [ 1, 2, 3 ]
_.elementAt(arr, 3) // => IndexOutOfBoundsException
*/
    @Test(expected = IndexOutOfBoundsException.class)
    public void elementAtOutOfBounds() {
        $.<Integer>elementAt(asList(1, 2, 3), 3);
    }

/*
var arr = [ 1, 2, 3 ]
_.elementAtOrElse(arr, 1, 0) // => 2
_.elementAtOrElse(arr, 3, 0) // => 0
*/
    @Test
    @SuppressWarnings("unchecked")
    public void elementAtOrElse() {
        assertEquals(2, $.<Integer>elementAtOrElse(asList(1, 2, 3), 1, 0).intValue());
        assertEquals(2, new $<Integer>(asList(1, 2, 3)).elementAtOrElse(1, 0).intValue());
        assertEquals(0, $.<Integer>elementAtOrElse(asList(1, 2, 3), 3, 0).intValue());
        assertEquals(0, new $<Integer>(asList(1, 2, 3)).elementAtOrElse(3, 0).intValue());
    }

/*
var arr = [ 1, 2, 3 ]
_.elementAtOrNull(arr, 1) // => 2
_.elementAtOrNull(arr, 3) // => null
*/
    @Test
    @SuppressWarnings("unchecked")
    public void elementAtOrNull() {
        assertEquals(2, $.<Integer>elementAtOrNull(asList(1, 2, 3), 1).intValue());
        assertEquals(2, new $<Integer>(asList(1, 2, 3)).elementAtOrNull(1).intValue());
        assertNull($.<Integer>elementAtOrNull(asList(1, 2, 3), 3));
        assertNull(new $<Integer>(asList(1, 2, 3)).elementAtOrNull(3));
    }

/*
[1, 2, 3].reverse() // [3, 2, 1]
*/
    @Test
    @SuppressWarnings("unchecked")
    public void reverse() {
        assertEquals("[3, 2, 1]", $.reverse(asList(1, 2, 3)).toString());
        assertEquals("[3, 2, 1]", $.reverse(new int[]{1, 2, 3}).toString());
        assertEquals("[3, 2, 1]", new $(asList(1, 2, 3)).reverse().toString());
        assertEquals("[3, 2, 1]", asList($.reverse(new Integer[] {1, 2, 3})).toString());
        assertEquals("[3, 2, 1]", $.chain(asList(1, 2, 3)).reverse().value().toString());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void findLastWithCustomIterable() {
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
        final Optional<Integer> result = $.findLast(iterable,
            new Predicate<Integer>() {
            public boolean test(Integer item) {
                return item % 2 == 0;
            }
        });
        assertEquals("Optional.of(6)", result.toString());
    }

    @Test
    public void iterate() {
        Iterable<long[]> iterable = $.<long[]>iterate(new long[] {1, 1}, new UnaryOperator<long[]>() {
                public long[] apply(long[] arg) {
                    return new long[] {arg[1], arg[0] + arg[1]};
                }
            });
        iterable.iterator().remove();
        assertTrue(iterable.iterator().hasNext());
        assertArrayEquals(new long[] {1, 1}, iterable.iterator().next());
        assertArrayEquals(new long[] {1, 2}, iterable.iterator().next());
    }

    @Test
    public void iterateChain() {
        Iterable<long[]> iterable = $.<long[]>iterate(new long[] {1, 1}, new UnaryOperator<long[]>() {
                public long[] apply(long[] arg) {
                    return new long[] {arg[1], arg[0] + arg[1]};
                }
            });
        assertEquals(1L, $.chain(iterable, 5).first().item()[0]);
        class MyIterable<T> implements Iterable<T> {
            public Iterator<T> iterator() {
                return new Iterator<T>() {
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
                    }
                };
            }
        }
        assertTrue($.chain(new MyIterable<Integer>(), 5).isEmpty());
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
        assertFalse(Optional.<Integer>absent().map(new Function<Integer, String>() {
                public String apply(Integer arg) {
                    return "" + arg;
                }
            }).isPresent());
        assertEquals("1", Optional.of(1).map(new Function<Integer, String>() {
                public String apply(Integer arg) {
                    return "" + arg;
                }
            }).get().toString());
        try {
            Optional.absent().get();
            fail("IllegalStateException expected");
        } catch (IllegalStateException ex) {
        }
    }

    @Test(expected = Exception.class)
    public void optionalOrThrow() throws RuntimeException {
        Optional.absent().orThrow(new Supplier<RuntimeException>() {
            public RuntimeException get() {
                return new RuntimeException();
            }
        });
    }

    @Test
    public void optionalOrThrowWithValue() {
        assertEquals("1", Optional.of(1).orThrow(new Supplier<RuntimeException>() {
            public RuntimeException get() {
                return new RuntimeException();
            }
        }).toString());
    }

    @Test(expected = NullPointerException.class)
    public void checkNotNull() {
        $.checkNotNull(null);
    }

    @Test
    public void checkNotNullWithObject() {
        assertEquals("123", $.checkNotNull("123"));
    }

    @Test(expected = NullPointerException.class)
    public void checkNotNullWithMessage() {
        $.checkNotNull(null, "Error message");
    }

    @Test
    public void checkNotNullWithObjectAndMessage() {
        assertEquals("123", $.checkNotNull("123", "Error message"));
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
        } }).entrySet()).sortBy(new Function<Map.Entry<String, Double>, Double>() {
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
        } }).entrySet()).sortBy(new Function<Map.Entry<String, Integer>, Integer>() {
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
        } }).entrySet()).sortBy(new Function<Map.Entry<String, Integer>, Integer>() {
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

    @Test
    @SuppressWarnings("unchecked")
    public void jobtest() {
        String[] strings = {
            "Sound boy proceed to blast into the galaxy",
            "Go back rocket man into the sky you'll see",
            "Hear it all the time, come back rewind",
            "Aliens are watching up in the sky",
            "Sound boy process to blast into the galaxy",
            "No one gonna harm you",
            "They all want you to play I watch the birds of prey"
        };
        List<Map<String, Object>> result = (List<Map<String, Object>>) $.chain(asList(strings))
            .map(
                new Function<String, Map<String, Object>>() {
                public Map<String, Object> apply(String item) {
                    Map<String, Object> resultItem = new LinkedHashMap<String, Object>();
                    resultItem.put("string", item);
                    resultItem.put("longestWord", $.chain(asList(item.split("\\s+"))).map(
                        new Function<String, Integer>() {
                            public Integer apply(String item) {
                                return item.length();
                            }
                        })
                        .max().item());
                    return resultItem;
                }
            })
            .sortBy(new Function<Map<String, Object>, Integer>() {
                public Integer apply(Map<String, Object> item) {
                    return -((Integer) item.get("longestWord"));
                }
            })
            .limit(5)
            .value();
        assertEquals("[{string=Aliens are watching up in the sky, longestWord=8}, "
            + "{string=Sound boy proceed to blast into the galaxy, longestWord=7}, "
            + "{string=Sound boy process to blast into the galaxy, longestWord=7}, "
            + "{string=Go back rocket man into the sky you'll see, longestWord=6}, "
            + "{string=Hear it all the time, come back rewind, longestWord=6}]", result.toString());
    }
}
