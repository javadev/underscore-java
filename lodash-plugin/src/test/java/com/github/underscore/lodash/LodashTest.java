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
package com.github.underscore.lodash;

import java.util.*;
import org.junit.Test;
import com.github.underscore.Predicate;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

/**
 * Underscore library unit test.
 *
 * @author Valentyn Kolesnikov
 */
public class LodashTest {

/*
_.chunk(['a', 'b', 'c', 'd'], 2);
// → [['a', 'b'], ['c', 'd']]

_.chunk(['a', 'b', 'c', 'd'], 3);
// → [['a', 'b', 'c'], ['d']]
*/
    @Test
    public void chunk() {
        assertEquals("[[a, b], [c, d]]", $.chunk(asList("a", "b", "c", "d"), 2).toString());
        assertEquals("[[a, b], [c, d]]", new $(asList("a", "b", "c", "d")).chunk(2).toString());
        assertEquals("[[a, b], [c, d]]", $.chain(asList("a", "b", "c", "d")).chunk(2).value().toString());
        assertEquals("[[a, b, c], [d]]", $.chunk(asList("a", "b", "c", "d"), 3).toString());
    }

/*
_.drop([1, 2, 3]);
// → [2, 3]

_.drop([1, 2, 3], 2);
// → [3]

_.drop([1, 2, 3], 5);
// → []

_.drop([1, 2, 3], 0);
// → [1, 2, 3]
*/
    @Test
    public void drop() {
        assertEquals("[2, 3]", $.drop(asList(1, 2, 3)).toString());
        assertEquals("[2, 3]", new $(asList(1, 2, 3)).drop().toString());
        assertEquals("[2, 3]", $.chain(asList(1, 2, 3)).drop().toString());
        assertEquals("[3]", $.drop(asList(1, 2, 3), 2).toString());
        assertEquals("[3]", new $(asList(1, 2, 3)).drop(2).toString());
        assertEquals("[3]", $.chain(asList(1, 2, 3)).drop(2).toString());
        assertEquals("[]", $.drop(asList(1, 2, 3), 5).toString());
        assertEquals("[1, 2, 3]", $.drop(asList(1, 2, 3), 0).toString());
    }

/*
_.dropRight([1, 2, 3]);
// → [1, 2]

_.dropRight([1, 2, 3], 2);
// → [1]

_.dropRight([1, 2, 3], 5);
// → []

_.dropRight([1, 2, 3], 0);
// → [1, 2, 3]
*/
    @Test
    public void dropRight() {
        assertEquals("[1, 2]", $.dropRight(asList(1, 2, 3)).toString());
        assertEquals("[1, 2]", new $(asList(1, 2, 3)).dropRight().toString());
        assertEquals("[1, 2]", $.chain(asList(1, 2, 3)).dropRight().toString());
        assertEquals("[1]", $.dropRight(asList(1, 2, 3), 2).toString());
        assertEquals("[1]", new $(asList(1, 2, 3)).dropRight(2).toString());
        assertEquals("[1]", $.chain(asList(1, 2, 3)).dropRight(2).toString());
        assertEquals("[]", $.dropRight(asList(1, 2, 3), 5).toString());
        assertEquals("[1, 2, 3]", $.dropRight(asList(1, 2, 3), 0).toString());
    }

/*
_.dropWhile([1, 2, 3], function(n) {
  return n < 3;
});
// → [3]
*/
    @Test
    public void dropWhile() {
        assertEquals("[3]", $.dropWhile(asList(1, 2, 3), new Predicate<Integer>() {
            public Boolean apply(Integer n) {
                return n < 3;
            }
        }).toString());
        assertEquals("[3]", new $(asList(1, 2, 3)).dropWhile(new Predicate<Integer>() {
            public Boolean apply(Integer n) {
                return n < 3;
            }
        }).toString());
        assertEquals("[3]", $.chain(asList(1, 2, 3)).dropWhile(new Predicate<Integer>() {
            public Boolean apply(Integer n) {
                return n < 3;
            }
        }).toString());
    }

/*
_.dropRightWhile([1, 2, 3], function(n) {
  return n > 1;
});
// → [1]
*/
    @Test
    public void dropRightWhile() {
        assertEquals("[1]", $.dropRightWhile(asList(1, 2, 3), new Predicate<Integer>() {
            public Boolean apply(Integer n) {
                return n > 1;
            }
        }).toString());
        assertEquals("[1]", new $(asList(1, 2, 3)).dropRightWhile(new Predicate<Integer>() {
            public Boolean apply(Integer n) {
                return n > 1;
            }
        }).toString());
        assertEquals("[1]", $.chain(asList(1, 2, 3)).dropRightWhile(new Predicate<Integer>() {
            public Boolean apply(Integer n) {
                return n > 1;
            }
        }).toString());
    }

/*
var array = [1, 2, 3];

_.fill(array, 'a');
console.log(array);
// → ['a', 'a', 'a']

_.fill(Array(3), 2);
// → [2, 2, 2]

_.fill([4, 6, 8], '*', 1, 2);
// → [4, '*', 8]
*/
    @Test
    public void fill() {
        List<Object> array = new ArrayList<Object>(asList(1, 2, 3));
        $.fill(array, "a");
        assertEquals("[a, a, a]", array.toString());
        array = new ArrayList<Object>(asList(1, 2, 3));
        new $(array).fill("a");
        assertEquals("[a, a, a]", array.toString());
        array = new ArrayList<Object>(asList(1, 2, 3));
        $.chain(array).fill("a");
        assertEquals("[a, a, a]", array.toString());
        assertEquals("[2, 2, 2]", $.fill(new ArrayList<Object>(Collections.nCopies(3, 0)), 2).toString());
        array = new ArrayList<Object>(asList(4, 6, 8));
        $.fill(array, "*", 1, 2);
        assertEquals("[4, *, 8]", array.toString());
        array = new ArrayList<Object>(asList(4, 6, 8));
        new $(array).fill("*", 1, 2);
        assertEquals("[4, *, 8]", array.toString());
        array = new ArrayList<Object>(asList(4, 6, 8));
        $.chain(array).fill("*", 1, 2);
        assertEquals("[4, *, 8]", array.toString());
    }

    @Test
    public void main() {
        $.main(new String[] {});
        new $(new ArrayList<String>());
        new $("");
        $.chain(new ArrayList<String>());
        $.chain(new HashSet<String>());
        $.chain(new String[] {});
        $.chain("");
    }
}
