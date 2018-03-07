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

import org.junit.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Underscore library unit test.
 *
 * @author Valentyn Kolesnikov
 */
public class ArraysTest {

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
        final int resultPred = $.first(asList(5, 4, 3, 2, 1), new Predicate<Integer>() {
            public boolean test(Integer item) {
                return item % 2 == 0;
            }
        });
        assertEquals(4, resultPred);
        final int resultPredObj = new $<Integer>(asList(5, 4, 3, 2, 1)).first(new Predicate<Integer>() {
            public boolean test(Integer item) {
                return item % 2 == 0;
            }
        });
        assertEquals(4, resultPredObj);
    }

    @Test
    public void firstOrNull() {
        final Integer result = $.firstOrNull(asList(5, 4, 3, 2, 1));
        assertEquals("5", result.toString());
        final Integer resultObj = new $<Integer>(asList(5, 4, 3, 2, 1)).firstOrNull();
        assertEquals("5", resultObj.toString());
        final Integer resultChain = $.chain(asList(5, 4, 3, 2, 1)).firstOrNull().item();
        assertEquals("5", resultChain.toString());
        assertNull($.firstOrNull(Collections.emptyList()));
        assertNull(new $<Integer>(Collections.<Integer>emptyList()).firstOrNull());
        final int resultPred = $.firstOrNull(asList(5, 4, 3, 2, 1), new Predicate<Integer>() {
            public boolean test(Integer item) {
                return item % 2 == 0;
            }
        });
        assertEquals(4, resultPred);
        final int resultPredChain = $.chain(asList(5, 4, 3, 2, 1)).firstOrNull(new Predicate<Integer>() {
            public boolean test(Integer item) {
                return item % 2 == 0;
            }
        }).item();
        assertEquals(4, resultPredChain);
        assertNull($.firstOrNull(Collections.<Integer>emptyList(), new Predicate<Integer>() {
            public boolean test(Integer item) {
                return item % 2 == 0;
            }
        }));
        final int resultPredObj = new $<Integer>(asList(5, 4, 3, 2, 1)).firstOrNull(new Predicate<Integer>() {
            public boolean test(Integer item) {
                return item % 2 == 0;
            }
        });
        assertEquals(4, resultPredObj);
        assertNull(new $<Integer>(Collections.<Integer>emptyList()).firstOrNull(new Predicate<Integer>() {
            public boolean test(Integer item) {
                return item % 2 == 0;
            }
        }));
    }

    @Test(expected = NoSuchElementException.class)
    @SuppressWarnings("unchecked")
    public void firstEmpty() {
        $.first(asList());
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
_.rest([5, 4, 3, 2, 1]);
=> [4, 3, 2, 1]
_.rest([5, 4, 3, 2, 1], 2);
=> [3, 2, 1]
*/
    @Test
    @SuppressWarnings("unchecked")
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
_.chunk(['a', 'b', 'c', 'd'], 2);
// → [['a', 'b'], ['c', 'd']]

_.chunk(['a', 'b', 'c', 'd'], 3);
// → [['a', 'b', 'c'], ['d']]
*/
    @Test
    @SuppressWarnings("unchecked")
    public void chunk() {
        assertEquals("[[a, b], [c, d]]", $.chunk(asList("a", "b", "c", "d"), 2).toString());
        assertEquals("[[a, b], [c, d]]", new $(asList("a", "b", "c", "d")).chunk(2).toString());
        assertEquals("[[a, b], [c, d]]", $.chain(asList("a", "b", "c", "d")).chunk(2).value().toString());
        assertEquals("[[a, b, c], [d]]", $.chunk(asList("a", "b", "c", "d"), 3).toString());
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
_.initial([5, 4, 3, 2, 1]);
=> [5, 4, 3, 2]
_.initial([5, 4, 3, 2, 1], 2);
=> [5, 4, 3]
*/
    @Test
    @SuppressWarnings("unchecked")
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
        List<Integer> res = new $(asList(1, 2, 3, 4, 5)).initial();
        assertEquals("initial one item did not work", asList(1, 2, 3, 4), res);
        res = new $(asList(1, 2, 3, 4, 5)).initial(3);
        assertEquals("initial multi item did not wok", asList(1, 2), res);
    }

/*
_.last([5, 4, 3, 2, 1]);
=> 1
*/
    @Test
    @SuppressWarnings("unchecked")
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
        Integer res = new $<Integer>(asList(1, 2, 3, 4, 5)).last();
        assertEquals("last one item did not work", 5, res.intValue());
        List<Integer> resList = new $(asList(1, 2, 3, 4, 5)).last(3);
        assertEquals("last multi item did not wok", asList(3, 4, 5), resList);
        final int resultPred = $.last(asList(5, 4, 3, 2, 1), new Predicate<Integer>() {
            public boolean test(Integer item) {
                return item % 2 == 0;
            }
        });
        assertEquals(2, resultPred);
        final int resultPredObj = new $<Integer>(asList(5, 4, 3, 2, 1)).last(new Predicate<Integer>() {
            public boolean test(Integer item) {
                return item % 2 == 0;
            }
        });
        assertEquals(2, resultPredObj);
    }

    @Test
    public void lastOrNull() {
        final Integer result = $.lastOrNull(asList(5, 4, 3, 2, 1));
        assertEquals("1", result.toString());
        final Integer resultObj = new $<Integer>(asList(5, 4, 3, 2, 1)).lastOrNull();
        assertEquals("1", resultObj.toString());
        final Integer resultChain = $.chain(asList(5, 4, 3, 2, 1)).lastOrNull().item();
        assertEquals("1", resultChain.toString());
        assertNull($.lastOrNull(Collections.emptyList()));
        assertNull(new $<Integer>(Collections.<Integer>emptyList()).lastOrNull());
        final int resultPred = $.lastOrNull(asList(5, 4, 3, 2, 1), new Predicate<Integer>() {
            public boolean test(Integer item) {
                return item % 2 == 0;
            }
        });
        assertEquals(2, resultPred);
        final int resultPredChain = $.chain(asList(5, 4, 3, 2, 1)).lastOrNull(new Predicate<Integer>() {
            public boolean test(Integer item) {
                return item % 2 == 0;
            }
        }).item();
        assertEquals(2, resultPredChain);
        assertNull($.lastOrNull(Collections.<Integer>emptyList(), new Predicate<Integer>() {
            public boolean test(Integer item) {
                return item % 2 == 0;
            }
        }));
        final int resultPredObj = new $<Integer>(asList(5, 4, 3, 2, 1)).lastOrNull(new Predicate<Integer>() {
            public boolean test(Integer item) {
                return item % 2 == 0;
            }
        });
        assertEquals(2, resultPredObj);
        assertNull(new $<Integer>(Collections.<Integer>emptyList()).lastOrNull(new Predicate<Integer>() {
            public boolean test(Integer item) {
                return item % 2 == 0;
            }
        }));
    }
/*
_.compact([0, 1, false, 2, '', 3]);
=> [1, 2, 3]
*/
    @Test
    @SuppressWarnings("unchecked")
    public void compact() {
        final List<?> result = $.compact(asList(0, 1, false, 2, "", 3));
        assertEquals("[1, 2, 3]", result.toString());
        final List<?> result2 = $.compact(asList(0, 1, false, 2, "", 3), 1);
        assertEquals("[0, false, 2, , 3]", result2.toString());
        final List<?> result3 = $.compact(asList(0, 1, null, 2, "", 3));
        assertEquals("[1, 2, 3]", result3.toString());
        final List<?> resultChain = $.chain(asList(0, 1, false, 2, "", 3)).compact().value();
        assertEquals("[1, 2, 3]", resultChain.toString());
        final List<?> result2Chain = $.chain(asList(0, 1, false, 2, "", 3)).compact(1).value();
        assertEquals("[0, false, 2, , 3]", result2Chain.toString());
        final List<?> result4 = new $(asList(0, 1, false, 2, "", 3)).compact();
        assertEquals("[1, 2, 3]", result4.toString());
        final List<?> result5 = new $(asList(0, 1, false, 2, "", 3)).compact(1);
        assertEquals("[0, false, 2, , 3]", result5.toString());
        final List<?> result6 = new $(asList(0, 1, null, 2, "", 3)).compact(1);
        assertEquals("[0, null, 2, , 3]", result6.toString());
        final List<?> result7 = new $(asList(0, 1, null, 2, "", 3)).compact((Integer) null);
        assertEquals("[0, 1, 2, , 3]", result7.toString());
        final Object[] resultArray = $.compact(new Object[] {0, 1, false, 2, "", 3});
        assertEquals("[1, 2, 3]", asList(resultArray).toString());
        final Object[] resultArray2 = $.compact(new Object[] {0, 1, false, 2, "", 3}, 1);
        assertEquals("[0, false, 2, , 3]", asList(resultArray2).toString());
    }

/*
_.flatten([1, [2], [3, [[4]]]]);
=> [1, 2, 3, 4];
*/
    @Test
    @SuppressWarnings("unchecked")
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
_.sortedIndex([10, 20, 30, 40, 50], 35);
=> 3
*/
    @Test
    public void sortedIndex() {
        final Integer result = $.sortedIndex(asList(10, 20, 30, 40, 50), 35);
        assertEquals(3, result.intValue());
        final Integer result2 = $.sortedIndex(new Integer[] {10, 20, 30, 40, 50}, 35);
        assertEquals(3, result2.intValue());
        final Integer result3 = $.sortedIndex(asList(10, 20, 30, 40, 50), 60);
        assertEquals(-1, result3.intValue());
    }

    @Test
    public void sortedIndex2() {
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
        $.<Person>sortedIndex(asList(new Person("moe", 40), new Person("moe", 50),
            new Person("curly", 60)), new Person("moe", 50), "age");
        assertEquals(1, result);
        final int result2 =
        $.<Person>sortedIndex(asList(new Person("moe", 40), new Person("moe", 50),
            new Person("curly", 60)), new Person("moe", 70), "age");
        assertEquals(-1, result2);
        final int resultArray =
        $.<Person>sortedIndex(new Person[] {new Person("moe", 40), new Person("moe", 50),
            new Person("curly", 60)}, new Person("moe", 50), "age");
        assertEquals(1, resultArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void sortedIndex2Error() {
        class Person implements Comparable<Person> {
            public int compareTo(Person person) {
                return 0;
            }
        }
        $.<Person>sortedIndex(asList(new Person()), new Person(), "age");
    }

/*
_.uniq([1, 2, 1, 3, 1, 4]);
=> [1, 2, 3, 4]
*/
    @Test
    @SuppressWarnings("unchecked")
    public void uniq() {
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
        }
        final Collection<Person> resultObject =
        $.uniq(asList(new Person("moe", 40), new Person("moe", 50), new Person("curly", 60)),
            new Function<Person, String>() {
            public String apply(Person person) {
                return person.name;
            }
        });
        assertEquals("[moe, 50, curly, 60]", resultObject.toString());
        final List<Person> resultObjectChain =
        $.chain(asList(new Person("moe", 40), new Person("moe", 50), new Person("curly", 60))).uniq(
            new Function<Person, String>() {
            public String apply(Person person) {
                return person.name;
            }
        }).value();
        assertEquals("[moe, 50, curly, 60]", resultObjectChain.toString());
        assertEquals("[1, 2, 3, 4, 5]", $.chain(asList(1, 2, 3, 3, 4, 5)).uniq().value().toString());
        final Object[] resultObjectArray =
        $.uniq(asList(new Person("moe", 40), new Person("moe", 50), new Person("curly", 60)).toArray(new Person[]{}),
            new Function<Person, String>() {
            public String apply(Person person) {
                return person.name;
            }
        });
        assertEquals("[moe, 50, curly, 60]", asList(resultObjectArray).toString());
    }

/*
_.distinct([1, 2, 1, 3, 1, 4]);
=> [1, 2, 3, 4]
*/
    @Test
    @SuppressWarnings("unchecked")
    public void distinct() {
        final List<Integer> result = $.distinct(asList(1, 2, 1, 3, 1, 4));
        assertEquals("[1, 2, 3, 4]", result.toString());
        final Object[] resultArray = $.distinct(new Integer[] {1, 2, 1, 3, 1, 4});
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
        $.distinctBy(asList(new Person("moe", 40), new Person("moe", 50), new Person("curly", 60)),
            new Function<Person, String>() {
            public String apply(Person person) {
                return person.name;
            }
        });
        assertEquals("[moe, 50, curly, 60]", resultObject.toString());
        final List<String> resultObjectChain =
        $.chain(asList(new Person("moe", 40), new Person("moe", 50), new Person("curly", 60))).distinctBy(
            new Function<Person, String>() {
            public String apply(Person person) {
                return person.name;
            }
        }).value();
        assertEquals("[moe, 50, curly, 60]", resultObjectChain.toString());
        assertEquals("[1, 2, 3, 4, 5]", $.chain(asList(1, 2, 3, 3, 4, 5)).distinct().value().toString());
        final Object[] resultObjectArray =
        $.distinctBy(asList(new Person("moe", 40), new Person("moe", 50), new Person("curly", 60)).toArray(new Person[]{}),
            new Function<Person, String>() {
            public String apply(Person person) {
                return person.name;
            }
        });
        assertEquals("[moe, 50, curly, 60]", asList(resultObjectArray).toString());
    }

/*
_.intersection([1, 2, 3], [101, 2, 1, 10], [2, 1]);
=> [1, 2]
*/
    @Test
    @SuppressWarnings("unchecked")
    public void intersection() {
        final List<Integer> result = $.intersection(asList(1, 2, 3), asList(101, 2, 1, 10), asList(2, 1));
        assertEquals("[1, 2]", result.toString());
        final List<Integer> resultObj = new $(asList(1, 2, 3)).intersectionWith(asList(101, 2, 1, 10), asList(2, 1));
        assertEquals("[1, 2]", resultObj.toString());
        final List<Integer> resultChain = $.chain(asList(1, 2, 3)).intersection(asList(101, 2, 1, 10),
            asList(2, 1)).value();
        assertEquals("[1, 2]", resultChain.toString());
        final Object[] resultArray = $.intersection(new Integer[] {1, 2, 3}, new Integer[] {101, 2, 1, 10});
        assertEquals("[1, 2]", asList(resultArray).toString());
    }

/*
_.union([1, 2, 3], [101, 2, 1, 10], [2, 1]);
=> [1, 2, 3, 101, 10]
*/
    @Test
    @SuppressWarnings("unchecked")
    public void union() {
        final List<Integer> result = $.union(asList(1, 2, 3), asList(101, 2, 1, 10), asList(2, 1));
        assertEquals("[1, 2, 3, 101, 10]", result.toString());
        final List<Integer> resultObj = new $(asList(1, 2, 3)).unionWith(asList(101, 2, 1, 10), asList(2, 1));
        assertEquals("[1, 2, 3, 101, 10]", resultObj.toString());
        final List<Integer> resultChain = $.chain(asList(1, 2, 3)).union(asList(101, 2, 1, 10), asList(2, 1)).value();
        assertEquals("[1, 2, 3, 101, 10]", resultChain.toString());
        final Object[] resultArray = $.union(new Integer[] {1, 2, 3}, new Integer[] {101, 2, 1, 10});
        assertEquals("[1, 2, 3, 101, 10]", asList(resultArray).toString());
    }

/*
_.difference([1, 2, 3, 4, 5], [5, 2, 10]);
=> [1, 3, 4]
*/
    @Test
    @SuppressWarnings("unchecked")
    public void difference() {
        final List<Integer> result = $.difference(asList(1, 2, 3, 4, 5), asList(5, 2, 10));
        assertEquals("[1, 3, 4]", result.toString());
        final List<Integer> resultObj = new $(asList(1, 2, 3, 4, 5)).differenceWith(asList(5, 2, 10));
        assertEquals("[1, 3, 4]", resultObj.toString());
        final List<Integer> resultChain = $.chain(asList(1, 2, 3, 4, 5)).difference(asList(5, 2, 10)).value();
        assertEquals("[1, 3, 4]", resultChain.toString());
        final List<Integer> resultList = $.difference(asList(1, 2, 3, 4, 5), asList(5, 2, 10), asList(8, 4));
        assertEquals("[1, 3]", resultList.toString());
        final Object[] resultArray = $.difference(new Integer[] {1, 2, 3, 4, 5}, new Integer[] {5, 2, 10});
        assertEquals("[1, 3, 4]", asList(resultArray).toString());
        final Object[] resultArray2 = $.difference(new Integer[] {1, 2, 3, 4, 5},
            new Integer[] {5, 2, 10}, new Integer[] {8, 4});
        assertEquals("[1, 3]", asList(resultArray2).toString());
    }

/*
_.zip(['moe', 'larry', 'curly'], [30, 40, 50], [true, false, false]);
=> [["moe", 30, true], ["larry", 40, false], ["curly", 50, false]]
*/
    @Test
    @SuppressWarnings("unchecked")
    public void zip() {
        final List<List<String>> result = $.zip(
            asList("moe", "larry", "curly"), asList("30", "40", "50"), asList("true", "false", "false"));
        assertEquals("[[moe, 30, true], [larry, 40, false], [curly, 50, false]]", result.toString());
    }

/*
_.unzip(["moe", 30, true], ["larry", 40, false], ["curly", 50, false]);
=> [['moe', 'larry', 'curly'], [30, 40, 50], [true, false, false]]
*/
    @Test
    @SuppressWarnings("unchecked")
    public void unzip() {
        final List<List<String>> result = $.unzip(
            asList("moe", "30", "true"), asList("larry", "40", "false"), asList("curly", "50", "false"));
        assertEquals("[[moe, larry, curly], [30, 40, 50], [true, false, false]]", result.toString());
    }

/*
_.object(['moe', 'larry', 'curly'], [30, 40, 50]);
=> {moe: 30, larry: 40, curly: 50}
*/
    @Test
    public void object() {
        final List<Tuple<String, String>> result = $.object(
            asList("moe", "larry", "curly"), asList("30", "40", "50"));
        assertEquals("[(moe, 30), (larry, 40), (curly, 50)]", result.toString());
    }

/*
_.indexOf([1, 2, 3], 2);
=> 1
*/
    @Test
    public void indexOf() {
        final Integer result = $.indexOf(asList(1, 2, 3), 2);
        assertEquals(1, result.intValue());
        final Integer resultArray = $.indexOf(new Integer[] {1, 2, 3}, 2);
        assertEquals(1, resultArray.intValue());
    }

/*
_.lastIndexOf([1, 2, 3, 1, 2, 3], 2);
=> 4
*/
    @Test
    public void lastIndexOf() {
        final Integer result = $.lastIndexOf(asList(1, 2, 3, 1, 2, 3), 2);
        assertEquals(4, result.intValue());
        final Integer resultArray = $.lastIndexOf(new Integer[] {1, 2, 3, 1, 2, 3}, 2);
        assertEquals(4, resultArray.intValue());
    }

/*
_.findIndex([1, 2, 3], function(item) {return item % 2  === 0; });
=> 1
*/
    @Test
    public void findIndex() {
        final Integer result = $.findIndex(asList(1, 2, 3), new Predicate<Integer>() {
            public boolean test(Integer item) {
                return item % 2 == 0;
            }
        });
        assertEquals(1, result.intValue());
        final Integer resultNotFound = $.findIndex(asList(1, 2, 3), new Predicate<Integer>() {
            public boolean test(Integer item) {
                return item > 3;
            }
        });
        assertEquals(-1, resultNotFound.intValue());
        final Integer resultArray = $.findIndex(new Integer[] {1, 2, 3}, new Predicate<Integer>() {
            public boolean test(Integer item) {
                return item % 2 == 0;
            }
        });
        assertEquals(1, resultArray.intValue());
    }

/*
_.findLastIndex([1, 2, 3, 4, 5], function(item) {return item % 2  === 0; });
=> 3
*/
    @Test
    public void findLastIndex() {
        final Integer result = $.findLastIndex(asList(1, 2, 3, 4, 5), new Predicate<Integer>() {
            public boolean test(Integer item) {
                return item % 2 == 0;
            }
        });
        assertEquals(3, result.intValue());
        final Integer resultNotFound = $.findLastIndex(asList(1, 2, 3, 4, 5), new Predicate<Integer>() {
            public boolean test(Integer item) {
                return item > 5;
            }
        });
        assertEquals(-1, resultNotFound.intValue());
        final Integer resultArray = $.findLastIndex(new Integer[] {1, 2, 3, 4, 5}, new Predicate<Integer>() {
            public boolean test(Integer item) {
                return item % 2 == 0;
            }
        });
        assertEquals(3, resultArray.intValue());
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
    public void range() {
        final int[] result = $.range(10);
        assertArrayEquals(new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, result);
        final List<Integer> resultChain = $.chain("").range(10).value();
        assertEquals("[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]", resultChain.toString());
        final int[] result2 = $.range(1, 11);
        assertArrayEquals(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, result2);
        final List<Integer> result2Chain = $.chain("").range(1, 11).value();
        assertEquals("[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]", result2Chain.toString());
        final int[] result3 = $.range(0, 30, 5);
        assertArrayEquals(new int[] {0, 5, 10, 15, 20, 25}, result3);
        final List<Integer> result3Chain = $.chain("").range(0, 30, 5).value();
        assertEquals("[0, 5, 10, 15, 20, 25]", result3Chain.toString());
        final int[] result4 = $.range(0, -10, -1);
        assertArrayEquals(new int[] {0, -1, -2, -3, -4, -5, -6, -7, -8, -9}, result4);
        final int[] result5 = $.range(0);
        assertArrayEquals(new int[] {}, result5);
    }

/*
_.lastIndex([1, 2, 3, 4, 5]);
=> 4
*/
    @Test
    public void lastIndex() {
        assertEquals(4, $.lastIndex(asList(1, 2, 3, 4, 5)));
        assertEquals(4, $.lastIndex(new Integer[]{1, 2, 3, 4, 5}));
        assertEquals(4, $.lastIndex(new int[]{1, 2, 3, 4, 5}));
    }
}
