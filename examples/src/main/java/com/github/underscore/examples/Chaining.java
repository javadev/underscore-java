/*
 * The MIT License (MIT)
 *
 * Copyright 2015-2026 Valentyn Kolesnikov <0009-0003-9608-3364@orcid.org>
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
import java.util.function.Function;

/**
 * Examples for underscore-java.
 *
 * @author Valentyn Kolesnikov
 */
public class Chaining {
    public static <E> E first(final Iterable<E> iterable) {
        return iterable.iterator().next();
    }

    public static <T, E> List<T> map(final List<E> list, final Function<? super E, T> func) {
        final List<T> transformed = new ArrayList<>(list.size());
        for (E element : list) {
            func.andThen(transformed::add).apply(element);
        }
        return transformed;
    }

    public static <E, T extends Comparable<? super T>> List<E> sortBy(
            final List<E> iterable, final Function<E, T> func) {
        final List<E> sortedList = new ArrayList<>(iterable);
        sortedList.sort(Comparator.comparing(func::apply));
        return sortedList;
    }

    /*
    var stooges = [{name: 'curly', age: 25}, {name: 'moe', age: 21}, {name: 'larry', age: 23}];
    var youngest = _.chain(stooges)
      .sortBy(function(stooge){ return stooge.age; })
      .map(function(stooge){ return stooge.name + ' is ' + stooge.age; })
      .first()
      .value();
    => "moe is 21"
    */
    public static <T> Chain<T> chain(final List<T> list) {
        return new Chaining.Chain<>(list);
    }

    public static class Chain<T> {
        private final T item;
        private final List<T> list;

        @SuppressWarnings("unchecked")
        public Chain(final T item) {
            this.item = item;
            this.list = null;
        }

        public Chain(final List<T> list) {
            this.item = null;
            this.list = list;
        }

        public Chain<T> first() {
            return new Chain<>(Chaining.first(list));
        }

        public <F> Chain<F> map(final Function<? super T, F> func) {
            return new Chain<>(Chaining.map(list, func));
        }

        public <F extends Comparable<? super F>> Chain<T> sortBy(final Function<T, F> func) {
            return new Chain<>(Chaining.sortBy(list, func));
        }

        public T item() {
            return item;
        }
    }
}
