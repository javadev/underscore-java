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
}
