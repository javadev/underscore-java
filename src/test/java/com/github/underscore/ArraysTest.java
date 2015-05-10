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
        Integer res = new $<Integer>(array(1, 2, 3, 4, 5)).first();
        assertEquals("first one item did not work", 1, res);
        List<Integer> resList = new $(array(1, 2, 3, 4, 5)).first(3);
        assertEquals("first multi item did not wok", array(1, 2, 3), resList);
    }

    @Test
    public void test_initial() {
        List<Integer> res = new $(array(1, 2, 3, 4, 5)).initial();
        assertEquals("initial one item did not work", array(1, 2, 3, 4), res);
        res = new $(array(1, 2, 3, 4, 5)).initial(3);
        assertEquals("initial multi item did not wok", array(1, 2), res);
    }

    @Test
    public void test_last() {
        Integer res = new $<Integer>(array(1, 2, 3, 4, 5)).last();
        assertEquals("last one item did not work", 5, res);
        List<Integer> resList = new $(array(1, 2, 3, 4, 5)).last(3);
        assertEquals("last multi item did not wok", array(3, 4, 5), resList);
    }

    @Test
    public void test_rest() {
        List<Integer> res = new $(array(1, 2, 3, 4, 5)).rest();
        assertEquals("rest one item did not work", array(2, 3, 4, 5), res);
        res = new $(array(1, 2, 3, 4, 5)).rest(3);
        assertEquals("rest multi item did not wok", array(4, 5), res);
    }

    @Test
    public void test_compact() {
        List<Integer> res = new $(array(false, 1, 0, "foo", null, -1)).compact();
        assertEquals("compact did not work", array(1, "foo", -1), res);
    }

    @Test
    public void test_flatten() {
        List<?> llist = array(1, array(2), array(3, array(array(array(4)))));
        assertEquals("can flatten nested arrays", $.flatten(llist),
            array(1, 2, 3, 4));
        assertEquals("can shallowly flatten nested arrays", $.flatten(llist, true),
            array(1, 2, 3, array(array(array(4)))));
    }

    @Test
    public void test_uniq() {
        List<Integer> tlist = array(1, 2, 1, 3, 1, 4);
        assertEquals("can find the unique values of an unsorted array",
            array(1, 2, 3, 4), $.uniq(tlist));

        tlist = array(1, 1, 1, 2, 2, 3);
        assertEquals("can find the unique values of a sorted array faster",
            array(1, 2, 3), $.uniq(tlist));
    }
}
