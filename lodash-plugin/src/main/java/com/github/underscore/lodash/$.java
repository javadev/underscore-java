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
import com.github.underscore.Predicate;

public class $<T> extends com.github.underscore.$<T> {

    public $(final Iterable<T> iterable) {
        super(iterable);
    }

    public $(final String string) {
        super(string);
    }

    public static class Chain<T> extends com.github.underscore.$.Chain<T> {
        public Chain(final T item) {
            super(item);
        }
        public Chain(final List<T> list) {
            super(list);
        }

        public Chain<List<List<T>>> chunk(final Integer size) {
            return new Chain<List<List<T>>>($.chunk(value(), size));
        }

        public Chain<List<T>> drop() {
            return new Chain<List<T>>($.drop(value()));
        }

        public Chain<List<T>> drop(final Integer n) {
            return new Chain<List<T>>($.drop(value(), n));
        }

        public Chain<List<T>> dropRight() {
            return new Chain<List<T>>($.dropRight(value()));
        }

        public Chain<List<T>> dropRight(final Integer n) {
            return new Chain<List<T>>($.dropRight(value(), n));
        }

        public Chain<List<T>> dropWhile(final Predicate<T> pred) {
            return new Chain<List<T>>($.dropWhile(value(), pred));
        }

        public Chain<List<T>> dropRightWhile(final Predicate<T> pred) {
            return new Chain<List<T>>($.dropRightWhile(value(), pred));
        }

        public Chain<List<Object>> fill(Object value) {
            return new Chain<List<Object>>($.fill((List<Object>) value(), value));
        }

        public Chain<List<Object>> fill(Object value, Integer start, Integer end) {
            return new Chain<List<Object>>($.fill((List<Object>) value(), value, start, end));
        }

        public Chain<List<?>> flattenDeep() {
            return new Chain<List<?>>($.flattenDeep((List<?>) value()));
        }

        public Chain<List<Object>> pull(Object ... values) {
            return new Chain<List<Object>>($.pull((List<Object>) value(), values));
        }

        public Chain<List<Object>> pullAt(Integer ... indexes) {
            return new Chain<List<Object>>($.pullAt((List<Object>) value(), indexes));
        }

        public Chain<List<T>> xor(final List<T> list) {
            return new Chain<List<T>>($.xor(value(), list));
        }
    }

    public static Chain chain(final String item) {
        return new $.Chain<String>(item);
    }

    public static <T> Chain chain(final List<T> list) {
        return new $.Chain<T>(list);
    }

    public static <T> Chain chain(final Set<T> list) {
        return new $.Chain<T>(newArrayList(list));
    }

    public static <T> Chain chain(final T ... list) {
        return new $.Chain<T>(Arrays.asList(list));
    }

    public static <T> List<List<T>> chunk(final Iterable<T> iterable, final Integer size) {
        int index = 0;
        int length = size(iterable);
        final List<List<T>> result = new ArrayList<List<T>>(length / size);
        while (index < length) {
            result.add(newArrayList(iterable).subList(index, Math.min(length, index + size)));
            index += size;
        }
        return result;
    }

    public List<List<T>> chunk(final Integer size) {
        return chunk(getIterable(), size);
    }

    public static <T> List<T> drop(final Iterable<T> iterable) {
        return rest(newArrayList(iterable));
    }

    public List<T> drop() {
        return drop(getIterable());
    }

    public static <T> List<T> drop(final Iterable<T> iterable, final Integer n) {
        return rest(newArrayList(iterable), n);
    }

    public List<T> drop(final Integer n) {
        return drop(getIterable(), n);
    }

    public static <T> List<T> dropRight(final Iterable<T> iterable) {
        return initial(newArrayList(iterable));
    }

    public List<T> dropRight() {
        return dropRight(getIterable());
    }

    public static <T> List<T> dropRight(final Iterable<T> iterable, final Integer n) {
        return initial(newArrayList(iterable), n);
    }

    public List<T> dropRight(final Integer n) {
        return dropRight(getIterable(), n);
    }

    public static <T> List<T> dropWhile(final Iterable<T> iterable, final Predicate<T> pred) {
        return rest(newArrayList(iterable), findIndex(newArrayList(iterable), negate(pred)));
    }

    public List<T> dropWhile(final Predicate<T> pred) {
        return dropWhile(getIterable(), pred);
    }

    public static <T> List<T> dropRightWhile(final Iterable<T> iterable, final Predicate<T> pred) {
        return dropWhile(reverse(iterable), pred);
    }

    public List<T> dropRightWhile(final Predicate<T> pred) {
        return dropRightWhile(getIterable(), pred);
    }

    public static List<Object> fill(final List<Object> list, Object value) {
        for (int index = 0; index < list.size(); index += 1) {
            list.set(index, value);
        }
        return list;
    }

    public List<Object> fill(Object value) {
        return fill((List<Object>) getIterable(), value);
    }

    public static List<Object> fill(final List<Object> list, Object value, Integer start, Integer end) {
        for (int index = start; index < end; index += 1) {
            list.set(index, value);
        }
        return list;
    }

    public List<Object> fill(Object value, Integer start, Integer end) {
        return fill((List<Object>) getIterable(), value, start, end);
    }

    public static <E> List<E> flattenDeep(final List<?> list) {
        return flatten(list, false);
    }

    public List<T> flattenDeep() {
        return flattenDeep((List<?>) getIterable());
    }

    public static List<Object> pull(final List<Object> list, Object ... values) {
        final List<Object> valuesList = Arrays.asList(values);
        for (final Iterator<Object> iterator = list.iterator(); iterator.hasNext(); ) {
            final Object object = iterator.next();
            if (valuesList.contains(object)) {
                iterator.remove();
            }
        }
        return list;
    }

    public List<Object> pull(Object ... values) {
        return pull((List<Object>) getIterable(), values);
    }

    public static List<Object> pullAt(final List<Object> list, Integer ... indexes) {
        final List<Object> result = newArrayList();
        final List<Integer> indexesList = Arrays.asList(indexes);
        int index = 0;
        for (final Iterator<Object> iterator = list.iterator(); iterator.hasNext(); ) {
            final Object object = iterator.next();
            if (indexesList.contains(index)) {
                result.add(object);
                iterator.remove();
            }
            index += 1;
        }
        return result;
    }

    public List<Object> pullAt(Integer ... indexes) {
        return pullAt((List<Object>) getIterable(), indexes);
    }

    public static <T> List<T> xor(final List<T> ... lists) {
        int index = -1;
        int length = lists.length;
        List<T> result = null;
        while (++index < length) {
            final List<T> array = lists[index];
            result = result == null ? array : concat(difference(result, array), difference(array, result));
        }
        return uniq(result);
    }

    public List<T> xor(final List<T> list) {
        return xor((List<T>) getIterable(), list);
    }

    public static void main(String ... args) {
        final String message = "Underscore-java-lodash is a lodash plugin for underscore-java.\n\n"
            + "For docs, license, tests, and downloads, see: http://javadev.github.io/underscore-java";
        System.out.println(message);
    }
}
