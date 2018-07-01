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
package com.github.underscore.examples;

import java.util.*;

/**
 * Examples for underscore-java.
 *
 * @author Valentyn Kolesnikov
 */
public class Intersection {
    public static <E> Optional<E> find(final Iterable<E> iterable, final Predicate<E> pred) {
        for (E element : iterable) {
            if (pred.test(element)) {
                return Optional.of(element);
            }
        }
        return Optional.absent();
    }

    public static <E> boolean some(final Iterable<E> iterable, final Predicate<E> pred) {
        return find(iterable, pred).isPresent();
    }

    public static <E> boolean contains(final Iterable<E> iterable, final E elem) {
        return some(iterable, new Predicate<E>() {
            @Override
            public boolean test(E e) {
                return elem == null ? e == null : elem.equals(e);
            }
        });
    }

    public static <E> List<E> filter(final List<E> list, final Predicate<E> pred) {
        final List<E> filtered = new ArrayList<E>();
        for (E element : list) {
            if (pred.test(element)) {
                filtered.add(element);
            }
        }
        return filtered;
    }

    public static <E> List<E> intersection(final List<E> list1, final List<E> list2) {
        return filter(list1, new Predicate<E>() {
            @Override
            public boolean test(E elem) {
                return contains(list2, elem);
            }
        });
    }

/*
_.intersection([1, 2, 3], [101, 2, 1, 10], [2, 1]);
=> [1, 2]
*/
    @SuppressWarnings("unchecked")
    public static <E> List<E> intersection(final List<E> list, final List<E> ... lists) {
        final Stack<List<E>> stack = new Stack<List<E>>();
        stack.push(list);
        for (int index = 0; index < lists.length; index += 1) {
          stack.push(intersection(stack.peek(), lists[index]));
        }
        return stack.peek();
    }
}
