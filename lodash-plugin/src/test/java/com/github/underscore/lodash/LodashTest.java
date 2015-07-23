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
  return n > 2;
});
// → [1, 2]
*/
    @Test
    public void dropRightWhile() {
        assertEquals("[1, 2]", $.dropRightWhile(asList(1, 2, 3), new Predicate<Integer>() {
            public Boolean apply(Integer n) {
                return n > 2;
            }
        }).toString());
        assertEquals("[1, 2]", new $(asList(1, 2, 3)).dropRightWhile(new Predicate<Integer>() {
            public Boolean apply(Integer n) {
                return n > 2;
            }
        }).toString());
        assertEquals("[1, 2]", $.chain(asList(1, 2, 3)).dropRightWhile(new Predicate<Integer>() {
            public Boolean apply(Integer n) {
                return n > 2;
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

/*
_.flattenDeep([1, [2, 3, [4]]]);
// → [1, 2, 3, 4]
*/
    @Test
    public void flattenDeep() {
        final List<Integer> result = $.flattenDeep(asList(1, asList(2, 3, asList(asList(4)))));
        assertEquals("[1, 2, 3, 4]", result.toString());
        final List<Integer> result2 = new $(asList(1, asList(2, 3, asList(asList(4))))).flattenDeep();
        assertEquals("[1, 2, 3, 4]", result2.toString());
        final List<Integer> resultChain = $.chain(asList(1, asList(2, 3, asList(asList(4))))).flattenDeep().value();
        assertEquals("[1, 2, 3, 4]", resultChain.toString());
    }

/*
var array = [1, 2, 3, 1, 2, 3];

_.pull(array, 2, 3);
console.log(array);
// → [1, 1]
*/
    @Test
    public void pull() {
        List<Object> array = new ArrayList<Object>(asList(1, 2, 3, 1, 2, 3));
        $.pull(array, 2, 3);
        assertEquals("[1, 1]", array.toString());
        array = new ArrayList<Object>(asList(1, 2, 3, 1, 2, 3));
        new $(array).pull(2, 3);
        assertEquals("[1, 1]", array.toString());
        array = new ArrayList<Object>(asList(1, 2, 3, 1, 2, 3));
        $.chain(array).pull(2, 3);
        assertEquals("[1, 1]", array.toString());
    }

/*
var array = [5, 10, 15, 20];
var evens = _.pullAt(array, 1, 3);

console.log(array);
// → [5, 15]

console.log(evens);
// → [10, 20]
*/
    @Test
    public void pullAt() {
        List<Object> array = new ArrayList<Object>(asList(5, 10, 15, 20));
        List<Object> events = $.pullAt(array, 1, 3);
        assertEquals("[5, 15]", array.toString());
        assertEquals("[10, 20]", events.toString());
        array = new ArrayList<Object>(asList(5, 10, 15, 20));
        events = new $(array).pullAt(1, 3);
        assertEquals("[5, 15]", array.toString());
        assertEquals("[10, 20]", events.toString());
        array = new ArrayList<Object>(asList(5, 10, 15, 20));
        events = $.chain(array).pullAt(1, 3).value();
        assertEquals("[5, 15]", array.toString());
        assertEquals("[10, 20]", events.toString());
    }

/*
var array = [1, 2, 3, 4];
var evens = _.remove(array, function(n) {
  return n % 2 == 0;
});

console.log(array);
// → [1, 3]

console.log(evens);
// → [2, 4]
*/
    @Test
    public void remove() {
        List<Integer> array = new ArrayList<Integer>(asList(1, 2, 3, 4));
        List<Integer> evens = $.remove(array, new Predicate<Integer>() {
            public Boolean apply(final Integer n) {
                return n % 2 == 0;
            }
        });
        assertEquals("[1, 3]", array.toString());
        assertEquals("[2, 4]", evens.toString());
        array = new ArrayList<Integer>(asList(1, 2, 3, 4));
        evens = new $(array).remove(new Predicate<Integer>() {
            public Boolean apply(final Integer n) {
                return n % 2 == 0;
            }
        });
        assertEquals("[1, 3]", array.toString());
        assertEquals("[2, 4]", evens.toString());
        array = new ArrayList<Integer>(asList(1, 2, 3, 4));
        evens = $.chain(array).remove(new Predicate<Integer>() {
            public Boolean apply(final Integer n) {
                return n % 2 == 0;
            }
        }).value();
        assertEquals("[1, 3]", array.toString());
        assertEquals("[2, 4]", evens.toString());
    }

/*
_.take([1, 2, 3]);
// → [1]

_.take([1, 2, 3], 2);
// → [1, 2]

_.take([1, 2, 3], 5);
// → [1, 2, 3]

_.take([1, 2, 3], 0);
// → []
*/
    @Test
    public void take() {
        assertEquals("[1]", $.take(asList(1, 2, 3)).toString());
        assertEquals("[1]", new $(asList(1, 2, 3)).take().toString());
        assertEquals("[1]", $.chain(asList(1, 2, 3)).take().value().toString());
        assertEquals("[1, 2]", $.take(asList(1, 2, 3), 2).toString());
        assertEquals("[1, 2]", new $(asList(1, 2, 3)).take(2).toString());
        assertEquals("[1, 2]", $.chain(asList(1, 2, 3)).take(2).value().toString());
        assertEquals("[1, 2, 3]", $.take(asList(1, 2, 3), 5).toString());
        assertEquals("[]", $.take(asList(1, 2, 3), 0).toString());
    }

/*
_.takeRight([1, 2, 3]);
// → [3]

_.takeRight([1, 2, 3], 2);
// → [2, 3]

_.takeRight([1, 2, 3], 5);
// → [1, 2, 3]

_.takeRight([1, 2, 3], 0);
// → []
*/
    @Test
    public void takeRight() {
        assertEquals("[3]", $.takeRight(asList(1, 2, 3)).toString());
        assertEquals("[3]", new $(asList(1, 2, 3)).takeRight().toString());
        assertEquals("[3]", $.chain(asList(1, 2, 3)).takeRight().value().toString());
        assertEquals("[2, 3]", $.takeRight(asList(1, 2, 3), 2).toString());
        assertEquals("[2, 3]", new $(asList(1, 2, 3)).takeRight(2).toString());
        assertEquals("[2, 3]", $.chain(asList(1, 2, 3)).takeRight(2).value().toString());
        assertEquals("[1, 2, 3]", $.takeRight(asList(1, 2, 3), 5).toString());
        assertEquals("[]", $.takeRight(asList(1, 2, 3), 0).toString());
    }

/*
_.takeWhile([1, 2, 3], function(n) {
  return n < 3;
});
// → [1, 2]
*/
    @Test
    public void takeWhile() {
        assertEquals("[1, 2]", $.takeWhile(asList(1, 2, 3), new Predicate<Integer>() {
            public Boolean apply(Integer n) {
                return n < 3;
            }
        }).toString());
        assertEquals("[1, 2]", new $(asList(1, 2, 3)).takeWhile(new Predicate<Integer>() {
            public Boolean apply(Integer n) {
                return n < 3;
            }
        }).toString());
        assertEquals("[1, 2]", $.chain(asList(1, 2, 3)).takeWhile(new Predicate<Integer>() {
            public Boolean apply(Integer n) {
                return n < 3;
            }
        }).toString());
    }

/*
_.takeRightWhile([1, 2, 3], function(n) {
  return n > 1;
});
// → [2, 3]
*/
    @Test
    public void takeRightWhile() {
        assertEquals("[2, 3]", $.takeRightWhile(asList(1, 2, 3), new Predicate<Integer>() {
            public Boolean apply(Integer n) {
                return n > 1;
            }
        }).toString());
        assertEquals("[2, 3]", new $(asList(1, 2, 3)).takeRightWhile(new Predicate<Integer>() {
            public Boolean apply(Integer n) {
                return n > 1;
            }
        }).toString());
        assertEquals("[2, 3]", $.chain(asList(1, 2, 3)).takeRightWhile(new Predicate<Integer>() {
            public Boolean apply(Integer n) {
                return n > 1;
            }
        }).toString());
    }

/*
_.xor([1, 2], [4, 2]);
// → [1, 4]
*/
    @Test
    public void xor() {
        assertEquals("[1, 4]", $.xor(asList(1, 2), asList(4, 2)).toString());
        assertEquals("[1, 4]", new $(asList(1, 2)).xor(asList(4, 2)).toString());
        assertEquals("[1, 4]", $.chain(asList(1, 2)).xor(asList(4, 2)).toString());
    }


/*
_.at(['a', 'b', 'c'], 0, 2);
// → ['a', 'c']
*/
    @Test
    public void at() {
        assertEquals("[a, c]", $.at(asList("a", "b", "c"), 0, 2).toString());
        assertEquals("[a, c]", new $(asList("a", "b", "c")).at(0, 2).toString());
        assertEquals("[a, c]", $.chain(asList("a", "b", "c")).at(0, 2).value().toString());
    }

    @Test
    public void main() {
        $.main(new String[] {});
        new $(new ArrayList<String>());
        new $("");
        new $(asList()).chain();
        $.chain(new ArrayList<String>());
        $.chain(new HashSet<String>());
        $.chain(new String[] {});
        $.chain("");
    }
}
