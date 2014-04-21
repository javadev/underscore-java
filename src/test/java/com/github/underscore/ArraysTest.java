/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2014 Valentyn Kolesnikov
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

/**
 * Underscore library unit test.
 *
 * @author Valentyn Kolesnikov
 */
public class ArraysTest {
  
    public <T> List<T>array(T ... items) {
        return asList(items);
    }

    public <T> List<T>array() {
        return Collections.<T>emptyList();
    }

    @Test
    public void test_first() {
        Integer res = new _<Integer>(array(1, 2, 3, 4, 5)).first();
        assertEquals("first one item did not work", 1, res);
        List<Integer> resList = new _(array(1, 2, 3, 4, 5)).first(3);
        assertEquals("first multi item did not wok", array(1, 2, 3), resList);
    }

    @Test
    public void test_initial() {
        List<Integer> res = new _(array(1, 2, 3, 4, 5)).initial();
        assertEquals("initial one item did not work", array(1, 2, 3, 4), res);
        res = new _(array(1, 2, 3, 4, 5)).initial(3);
        assertEquals("initial multi item did not wok", array(1, 2), res);
    }

    @Test
    public void test_last() {
        Integer res = new _<Integer>(array(1, 2, 3, 4, 5)).last();
        assertEquals("last one item did not work", 5, res);
        List<Integer> resList = new _(array(1, 2, 3, 4, 5)).last(3);
        assertEquals("last multi item did not wok", array(3, 4, 5), resList);
    }

/*
    def test_rest(self):
        res = _([1, 2, 3, 4, 5]).rest()
        self.assertEqual([2, 3, 4, 5], res, "rest one item did not work")
        res = _([1, 2, 3, 4, 5]).rest(3)
        self.assertEqual([4, 5], res, "rest multi item did not wok")

    def test_compact(self):
        res = _([False, 1, 0, "foo", None, -1]).compact()
        self.assertEqual([1, "foo", -1], res, "compact did not work")

    def test_flatten(self):
        llist = [1, [2], [3, [[[4]]]]]
        self.assertEqual(_.flatten(llist),
                         [1, 2, 3, 4], 'can flatten nested arrays')
        self.assertEqual(_.flatten(llist, True),
                         [1, 2, 3, [[[4]]]], 'can shallowly'
                         ' flatten nested arrays')

    def test_uniq(self):
        tlist = [1, 2, 1, 3, 1, 4]
        self.assertEqual([1, 2, 3, 4], _.uniq(tlist),
                         'can find the unique values of an unsorted array')

        tlist = [1, 1, 1, 2, 2, 3]
        self.assertEqual([1, 2, 3], _.uniq(tlist, True),
                         'can find the unique values of a sorted array faster')

        tlist = [{"name": 'moe'}, {"name": 'curly'},
                 {"name": 'larry'}, {"name": 'curly'}]
        iterator = lambda value, *args: value.get('name')
        self.assertEqual(
            ["moe", "curly", "larry"], _.uniq(tlist, False, iterator),
            'can find the unique values of an array using a custom iterator')

        tlist = [1, 2, 2, 3, 4, 4]
        iterator = lambda value, *args: value + 1
        self.assertEqual([2, 3, 4, 5], _.uniq(tlist, True, iterator),
                         'iterator works with sorted array')

    def test_without(self):
        tlist = [1, 2, 1, 0, 3, 1, 4]
        self.assertEqual([2, 3, 4], _.without(tlist, 0, 1),
                         'can remove all instances of an object')

        tlist = [{"one": 1}, {"two": 2}]

        self.assertTrue(len(_.without(tlist, {"one": 1}))
                        == 2, 'uses real object identity for comparisons.')
        self.assertTrue(len(_.without(tlist, tlist[0])) == 1, 'ditto.')

    def test_intersection(self):
        stooges = ['moe', 'curly', 'larry'],
        leaders = ['moe', 'groucho']
        self.assertEqual(['moe'], _.intersection(stooges, leaders),
                         'can take the set intersection of two string arrays')
        self.assertEqual(
            [1, 2], _.intersection([1, 2, 3], [101, 2, 1, 10], [2, 1]),
            'can take the set intersection of two int arrays')
        self.assertEqual(['moe'], _(stooges).intersection(leaders),
                         'can perform an OO-style intersection')

    def test_union(self):
        result = _.union([1, 2, 3], [2, 30, 1], [1, 40])
        self.assertEqual([1, 2, 3, 30, 40], result,
                         'takes the union of a list of arrays')

        result = _.union([1, 2, 3], [2, 30, 1], [1, 40, [1]])
        self.assertEqual([1, 2, 3, 30, 40, [1]], result,
                         'takes the union of a list of nested arrays')

    def test_difference(self):
        result = _.difference([1, 2, 3], [2, 30, 40])
        self.assertEqual([1, 3], result, 'takes the difference of two arrays')

        result = _.difference([1, 2, 3, 4], [2, 30, 40], [1, 11, 111])
        self.assertEqual([3, 4], result,
                         'takes the difference of three arrays')

    def test_zip(self):
        names = ['moe', 'larry', 'curly']
        ages = [30, 40, 50]
        leaders = [True]
        stooges = list(_(names).zip(ages, leaders))
        self.assertEqual("[('moe', 30, True), ('larry', 40, None),"
                         " ('curly', 50, None)]", str(
                             stooges), 'zipped together arrays of different lengths')

    def test_zipObject(self):
        result = _.zipObject(['moe', 'larry', 'curly'], [30, 40, 50])
        shouldBe = {"moe": 30, "larry": 40, "curly": 50}
        self.assertEqual(result, shouldBe,
                         "two arrays zipped together into an object")

    def test_indexOf(self):
        numbers = [1, 2, 3]
        self.assertEqual(_.indexOf(numbers, 2), 1,
                         'can compute indexOf, even '
                         'without the native function')
        self.assertEqual(_.indexOf(None, 2), -1, 'handles nulls properly')

        numbers = [10, 20, 30, 40, 50]
        num = 35
        index = _.indexOf(numbers, num, True)
        self.assertEqual(index, -1, '35 is not in the list')

        numbers = [10, 20, 30, 40, 50]
        num = 40
        index = _.indexOf(numbers, num, True)
        self.assertEqual(index, 3, '40 is in the list')

        numbers = [1, 40, 40, 40, 40, 40, 40, 40, 50, 60, 70]
        num = 40
        index = _.indexOf(numbers, num, True)
        self.assertEqual(index, 1, '40 is in the list')

    def test_lastIndexOf(self):
        numbers = [2, 1, 0, 1, 0, 0, 1, 0, 0, 0]
        self.assertEqual(_.lastIndexOf(numbers, 1), 6,
                         'can compute lastIndexOf, '
                         'even without the native function')
        self.assertEqual(_.lastIndexOf(numbers, 0), 9,
                         'lastIndexOf the other element')
        self.assertEqual(_.lastIndexOf(numbers, 2), 0,
                         'lastIndexOf the other element')
        self.assertEqual(_.indexOf(None, 2), -1, 'handles nulls properly')

    def test_range(self):
        self.assertEqual(
            list(_.range(0)), [], 'range with 0 as a first argument'
                                  ' generates an empty array')
        self.assertEqual(list(_.range(4)), [0, 1, 2, 3],
                         'range with a single positive argument generates'
                         ' an array of elements 0,1,2,...,n-1')
        self.assertEqual(list(_.range(5, 8)),
                         [5, 6, 7], 'range with two arguments a &amp; b,'
                         ' a&lt;b generates an array of elements '
                         ' a,a+1,a+2,...,b-2,b-1')
        self.assertEqual(list(_.range(8, 5)),
                         [], 'range with two arguments a &amp; b, b&lt;a'
                         ' generates an empty array')
        self.assertEqual(list(_.range(3, 10, 3)),
                         [3, 6, 9], 'range with three arguments a &amp; b'
                         ' &amp; c, c &lt; b-a, a &lt; b generates an array '
                         ' of elements a,a+c,a+2c,...,b - (multiplier of a) '
                         ' &lt; c')
        self.assertEqual(list(_.range(3, 10, 15)),
                         [3], 'range with three arguments a &amp; b &amp;'
                         ' c, c &gt; b-a, a &lt; b generates an array with '
                         'a single element, equal to a')
        self.assertEqual(list(_.range(12, 7, -2)), [12, 10, 8],
                         'range with three arguments a &amp; b &amp; c, a'
                         ' &gt; b, c &lt; 0 generates an array of elements'
                         ' a,a-c,a-2c and ends with the number not less than b')
        self.assertEqual(list(_.range(0, -10, -1)),
                         [0, -1, -2, -3, -4, -5, -6, -7, -8, -9], 'final'
                         ' example in the Python docs')
*/
}
