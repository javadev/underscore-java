/*
 * The MIT License (MIT)
 *
 * Copyright 2015-2017 Valentyn Kolesnikov
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
package com.github.underscore.math;

import com.github.underscore.BiFunction;
import com.github.underscore.Consumer;
import com.github.underscore.Function;
import com.github.underscore.Predicate;
import com.github.underscore.PredicateIndexed;
import com.github.underscore.Tuple;
import com.github.underscore.Optional;
import java.util.*;

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

        public Chain<T> first() {
            return new Chain<T>($.first(value()));
        }

        public Chain<T> first(int n) {
            return new Chain<T>($.first(value(), n));
        }

        public Chain<T> firstOrNull() {
            return new Chain<T>($.firstOrNull(value()));
        }

        public Chain<T> firstOrNull(final Predicate<T> pred) {
            return new Chain<T>($.firstOrNull(value(), pred));
        }

        public Chain<T> initial() {
            return new Chain<T>($.initial(value()));
        }

        public Chain<T> initial(int n) {
            return new Chain<T>($.initial(value(), n));
        }

        public Chain<T> last() {
            return new Chain<T>($.last(value()));
        }

        public Chain<T> last(int n) {
            return new Chain<T>($.last(value(), n));
        }

        public Chain<T> lastOrNull() {
            return new Chain<T>($.lastOrNull(value()));
        }

        public Chain<T> lastOrNull(final Predicate<T> pred) {
            return new Chain<T>($.lastOrNull(value(), pred));
        }

        public Chain<T> rest() {
            return new Chain<T>($.rest(value()));
        }

        public Chain<T> rest(int n) {
            return new Chain<T>($.rest(value(), n));
        }

        public Chain<T> compact() {
            return new Chain<T>($.compact(value()));
        }

        public Chain<T> compact(final T falsyValue) {
            return new Chain<T>($.compact(value(), falsyValue));
        }

        @SuppressWarnings("unchecked")
        public Chain flatten() {
            return new Chain((List<T>) $.flatten(value()));
        }

        public <F> Chain<F> map(final Function<? super T, F> func) {
            return new Chain<F>($.map(value(), func));
        }

        public Chain<T> filter(final Predicate<T> pred) {
            return new Chain<T>($.filter(value(), pred));
        }

        public Chain<T> filterIndexed(final PredicateIndexed<T> pred) {
            return new Chain<T>($.filterIndexed(value(), pred));
        }

        public Chain<T> rejectIndexed(final PredicateIndexed<T> pred) {
            return new Chain<T>($.rejectIndexed(value(), pred));
        }

        public Chain<T> reject(final Predicate<T> pred) {
            return new Chain<T>($.reject(value(), pred));
        }

        public Chain<T> filterFalse(final Predicate<T> pred) {
            return new Chain<T>($.filterFalse(value(), pred));
        }

        public <F> Chain<F> reduce(final BiFunction<F, T, F> func, final F zeroElem) {
            return new Chain<F>($.reduce(value(), func, zeroElem));
        }

        public <F> Chain<F> reduceRight(final BiFunction<F, T, F> func, final F zeroElem) {
            return new Chain<F>($.reduceRight(value(), func, zeroElem));
        }

        public Chain<Optional<T>> find(final Predicate<T> pred) {
            return new Chain<Optional<T>>($.find(value(), pred));
        }

        public Chain<Optional<T>> findLast(final Predicate<T> pred) {
            return new Chain<Optional<T>>($.findLast(value(), pred));
        }

        @SuppressWarnings("unchecked")
        public Chain<Comparable> max() {
            return new Chain<Comparable>($.max((Collection) value()));
        }

        public <F extends Comparable<? super F>> Chain<T> max(final Function<T, F> func) {
            return new Chain<T>($.max(value(), func));
        }

        @SuppressWarnings("unchecked")
        public Chain<Comparable> min() {
            return new Chain<Comparable>($.min((Collection) value()));
        }

        public <F extends Comparable<? super F>> Chain<T> min(final Function<T, F> func) {
            return new Chain<T>($.min(value(), func));
        }

        @SuppressWarnings("unchecked")
        public Chain<Comparable> sort() {
            return new Chain<Comparable>($.sort((List<Comparable>) value()));
        }

        @SuppressWarnings("unchecked")
        public <F extends Comparable<? super F>> Chain<F> sortWith(final Comparator<F> comparator) {
            return new Chain<F>($.sortWith((List<F>) value(), comparator));
        }

        public <F extends Comparable<? super F>> Chain<T> sortBy(final Function<T, F> func) {
            return new Chain<T>($.sortBy(value(), func));
        }

        @SuppressWarnings("unchecked")
        public <K> Chain<Map<K, Comparable>> sortBy(final K key) {
            return new Chain<Map<K, Comparable>>($.sortBy((List<Map<K, Comparable>>) value(), key));
        }

        public <F> Chain<Map<F, List<T>>> groupBy(final Function<T, F> func) {
            return new Chain<Map<F, List<T>>>($.groupBy(value(), func));
        }

        public Chain<Map<Object, List<T>>> indexBy(final String property) {
            return new Chain<Map<Object, List<T>>>($.indexBy(value(), property));
        }

        public <F> Chain<Map<F, Integer>> countBy(final Function<T, F> func) {
            return new Chain<Map<F, Integer>>($.countBy(value(), func));
        }

        public Chain<T> shuffle() {
            return new Chain<T>($.shuffle(value()));
        }

        public Chain<T> sample() {
            return new Chain<T>($.sample(value()));
        }

        public Chain<T> sample(final int howMany) {
            return new Chain<T>($.newArrayList($.sample(value(), howMany)));
        }

        public Chain<T> tap(final Consumer<T> func) {
            $.tap(value(), func);
            return new Chain<T>(value());
        }

        public Chain<T> forEach(final Consumer<T> func) {
            $.forEach(value(), func);
            return new Chain<T>(value());
        }

        public Chain<T> forEachRight(final Consumer<T> func) {
            $.forEachRight(value(), func);
            return new Chain<T>(value());
        }

        public Chain<Boolean> every(final Predicate<T> pred) {
            return new Chain<Boolean>($.every(value(), pred));
        }

        public Chain<Boolean> some(final Predicate<T> pred) {
            return new Chain<Boolean>($.some(value(), pred));
        }

        public Chain<Boolean> contains(final T elem) {
            return new Chain<Boolean>($.contains(value(), elem));
        }

        public Chain<T> invoke(final String methodName, final List<Object> args) {
            return new Chain<T>($.invoke(value(), methodName, args));
        }

        public Chain<T> invoke(final String methodName) {
            return new Chain<T>($.invoke(value(), methodName));
        }

        public Chain<Object> pluck(final String propertyName) {
            return new Chain<Object>($.pluck(value(), propertyName));
        }

        public <E> Chain<T> where(final List<Tuple<String, E>> properties) {
            return new Chain<T>($.where(value(), properties));
        }

        public <E> Chain<Optional<T>> findWhere(final List<Tuple<String, E>> properties) {
            return new Chain<Optional<T>>($.findWhere(value(), properties));
        }

        public Chain<T> uniq() {
            return new Chain<T>($.uniq(value()));
        }

        @SuppressWarnings("unchecked")
        public <F> Chain<T> uniq(final Function<T, F> func) {
            return new Chain<T>($.newArrayList($.uniq(value(), func)));
        }

        public Chain<T> distinct() {
            return new Chain<T>($.uniq(value()));
        }

        @SuppressWarnings("unchecked")
        public <F> Chain<F> distinctBy(final Function<T, F> func) {
            return new Chain<F>($.newArrayList((Iterable<F>) $.uniq(value(), func)));
        }

        public Chain<T> union(final List<T> ... lists) {
            return new Chain<T>($.union(value(), lists));
        }

        public Chain<T> intersection(final List<T> ... lists) {
            return new Chain<T>($.intersection(value(), lists));
        }

        public Chain<T> difference(final List<T> ... lists) {
            return new Chain<T>($.difference(value(), lists));
        }

        public Chain<Integer> range(final int stop) {
            return new Chain<Integer>(newIntegerList($.range(stop)));
        }

        public Chain<Integer> range(final int start, final int stop) {
            return new Chain<Integer>(newIntegerList($.range(start, stop)));
        }

        public Chain<Integer> range(final int start, final int stop, final int step) {
            return new Chain<Integer>(newIntegerList($.range(start, stop, step)));
        }

        public Chain<List<T>> chunk(final int size) {
            return new Chain<List<T>>($.chunk(value(), size));
        }

        public Chain<T> concat(final List<T> ... lists) {
            return new Chain<T>($.concat(value(), lists));
        }

        public Chain<T> slice(final int start) {
            return new Chain<T>($.slice(value(), start));
        }

        public Chain<T> slice(final int start, final int end) {
            return new Chain<T>($.slice(value(), start, end));
        }

        public Chain<T> reverse() {
            return new Chain<T>($.reverse(value()));
        }

        public Chain<String> join() {
            return new Chain<String>($.join(value()));
        }

        public Chain<String> join(final String separator) {
            return new Chain<String>($.join(value(), separator));
        }

        public Chain<T> skip(final int numberToSkip) {
            return new Chain<T>(value().subList(numberToSkip, value().size()));
        }

        public Chain<T> limit(final int size) {
            return new Chain<T>(value().subList(0, size));
        }

        @SuppressWarnings("unchecked")
        public <K, V> Chain<Map<K, V>> toMap() {
            return new Chain<Map<K, V>>($.toMap((Iterable<Map.Entry<K, V>>) value()));
        }

        @SuppressWarnings("unchecked")
        public <T extends Number> Chain<T> sum() {
            return new Chain<T>($.sum((List<T>) value()));
        }

        public <F extends Number> Chain<F> sum(final Function<T, F> func) {
            return new Chain<F>($.sum(value(), func));
        }

        @SuppressWarnings("unchecked")
        public Chain<Double> mean() {
            return new Chain<Double>($.mean((List<Number>) value()));
        }

        @SuppressWarnings("unchecked")
        public Chain<Double> median() {
            return new Chain<Double>($.median((List<Number>) value()));
        }

        @SuppressWarnings("unchecked")
        public Chain<List<T>> createPermutationWithRepetition(final int permutationLength) {
            return new Chain<List<T>>($.createPermutationWithRepetition((List<T>) value(), permutationLength));
        }
    }

    public static <T> Chain<T> chain(final List<T> list) {
        return new $.Chain<T>(list);
    }

    public static <T> Chain<T> chain(final Iterable<T> iterable) {
        return new $.Chain<T>(newArrayList(iterable));
    }

    public static <T> Chain<T> chain(final Iterable<T> iterable, int size) {
        return new $.Chain<T>(newArrayList(iterable, size));
    }

    public static <T> Chain<T> chain(final T ... array) {
        return new $.Chain<T>(Arrays.asList(array));
    }

    public static Chain<Integer> chain(final int[] array) {
        return new $.Chain<Integer>(newIntegerList(array));
    }

    @SuppressWarnings("unchecked")
    public Chain<T> chain() {
        return new $.Chain<T>(newArrayList(value()));
    }

    public static <T extends Number> T sum(final Iterable<T> iterable) {
        T result = null;
        for (final T item : iterable) {
            result = sum(result, item);
        }
        return result;
    }

    public static <E, F extends  Number> F sum(final Iterable<E> iterable, final Function<E, F> func) {
        F result = null;
        for (final E item : iterable) {
            result = sum(result, func.apply(item));
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public <T extends Number> T sum() {
        return (T) sum((List<T>) getIterable());
    }

    @SuppressWarnings("unchecked")
    public <E, F extends  Number> F sum(final Function<E, F> func) {
        return (F) sum((List<E>) getIterable(), func);
    }

    @SuppressWarnings("unchecked")
    private static <T extends Number> T sum(final T first, final T second) {
        if (first == null) {
            return second;
        } else if (second == null) {
            return first;
        } else if (first instanceof java.math.BigDecimal) {
            return (T) sum((java.math.BigDecimal) first, (java.math.BigDecimal) second);
        } else if (second instanceof java.math.BigInteger) {
            return (T) sum((java.math.BigInteger) first, (java.math.BigInteger) second);
        } else if (first instanceof Byte) {
            return (T) sum((Byte) first, (Byte) second);
        } else if (first instanceof Double) {
            return (T) sum((Double) first, (Double) second);
        } else if (first instanceof Float) {
            return (T) sum((Float) first, (Float) second);
        } else if (first instanceof Integer) {
            return (T) sum((Integer) first, (Integer) second);
        } else if (first instanceof Long) {
            return (T) sum((Long) first, (Long) second);
        } else if (first instanceof Short) {
            return (T) sum((Short) first, (Short) second);
        } else {
            throw new UnsupportedOperationException("Sum only supports official subclasses of Number");
        }
    }

    private static java.math.BigDecimal sum(java.math.BigDecimal first, java.math.BigDecimal second) {
        return first.add(second);
    }

    private static java.math.BigInteger sum(java.math.BigInteger first, java.math.BigInteger second) {
        return first.add(second);
    }

    private static Byte sum(Byte first, Byte second) {
        return (byte) (first + second);
    }

    private static Double sum(Double first, Double second) {
        return first + second;
    }

    private static Float sum(Float first, Float second) {
        return first + second;
    }

    private static Integer sum(Integer first, Integer second) {
        return first + second;
    }

    private static Long sum(Long first, Long second) {
        return first + second;
    }

    private static Short sum(Short first, Short second) {
        return (short) (first + second);
    }

    public static <T extends Number> double mean(final Iterable<T> iterable) {
        T result = null;
        int count = 0;
        for (final T item : iterable) {
            result = sum(result, item);
            count += 1;
        }
        return result.doubleValue() / count;
    }

    @SuppressWarnings("unchecked")
    public double mean() {
        return mean((Iterable<Number>) getIterable());
    }

    @SuppressWarnings("unchecked")
    public static <T extends Number> double median(final Iterable<T> iterable) {
        final List<T> result = newArrayList((Collection) iterable);
        final int size = size(iterable);
        if (size == 0) {
            throw new IllegalArgumentException("Iterable cannot be empty");
        }
        if (size % 2 != 0) {
            return result.get(size / 2).doubleValue();
        }
        return (result.get(size / 2 - 1).doubleValue() + result.get(size / 2).doubleValue()) / 2;
    }

    @SuppressWarnings("unchecked")
    public double median() {
        return median((Iterable<Number>) getIterable());
    }

    public static <T> List<List<T>> createPermutationWithRepetition(final List<T> list, final int permutationLength) {
        final long resultSize = (long) Math.pow(list.size(), permutationLength);
        final List<List<T>> result = new ArrayList<List<T>>((int) resultSize);
        final int[] bitVector = new int[permutationLength];
        for (int index = 0; index < resultSize; index += 1) {
            List<T> result2 = new ArrayList<T>(permutationLength);
            for (int index2 = 0; index2 < permutationLength; index2 += 1) {
                result2.add(list.get(bitVector[index2]));
            }
            int index3 = 0;
            while (index3 < permutationLength && bitVector[index3] == list.size() - 1) {
                bitVector[index3] = 0;
                index3 += 1;
            }
            if (index3 < permutationLength) {
                bitVector[index3] += 1;
            }
            result.add(result2);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public List<List<T>> createPermutationWithRepetition(final int permutationLength) {
        return createPermutationWithRepetition((List<T>) value(), permutationLength);
    }

    public static class LRUCache<K, V> {
        private int capacity;
        private Map<K, Node<K, V>> map = new HashMap<K, Node<K, V>>();
        private Node head;
        private Node end;

        public LRUCache(int capacity) {
            this.capacity = capacity;
        }

        public V get(K key) {
            if (map.containsKey(key)) {
                Node<K, V> n = map.get(key);
                remove(n);
                setHead(n);
                return n.value;
            }
            return null;
        }

        public void remove(Node n) {
            if (n.pre != null) {
                n.pre.next = n.next;
            } else {
                head = n.next;
            }
            if (n.next != null) {
                n.next.pre = n.pre;
            } else {
                end = n.pre;
            }
        }

        public void setHead(Node n) {
            n.next = head;
            n.pre = null;
            if (head != null) {
                head.pre = n;
            }
            head = n;
            if (end == null) {
                end = head;
            }
        }

        public void set(K key, V value) {
            if (map.containsKey(key)) {
                Node<K, V> old = map.get(key);
                old.value = value;
                remove(old);
                setHead(old);
            } else {
                Node<K, V> created = new Node<K, V>(key, value);
                if (map.size() >= capacity) {
                    map.remove(end.key);
                    remove(end);
                    setHead(created);
                } else {
                    setHead(created);
                }
                map.put(key, created);
            }
        }
    }

    public static class Node<K, V> {
        K key;
        V value;
        Node pre;
        Node next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    public static <K, V> LRUCache<K, V> createLRUCache(final int capacity) {
        return new LRUCache<K, V>(capacity);
    }

    public static List<Entry> findByName(final Entry entry, final String name) {
        final List<Entry> result = new ArrayList<Entry>();
        final Queue<Entry> allFiles = new LinkedList<Entry>();
        allFiles.add(entry);
        while (!allFiles.isEmpty()) {
            final Entry localEntry = allFiles.poll();
            if (localEntry instanceof Directory) {
                final List<Entry> files = ((Directory) localEntry).getContents();
                for (final Entry innerFile : files) {
                    if (innerFile instanceof Directory) {
                        allFiles.add(innerFile);
                    } else if (innerFile.getName().equals(name)) {
                        result.add(innerFile);
                    }
                }
            } else if (localEntry.getName().equals(name)) {
                result.add(localEntry);
            }
        }
        return result;
    }

    public static void main(String ... args) {
        final String message = "Underscore-java-math is a math plugin for underscore-java.\n\n"
            + "For docs, license, tests, and downloads, see: http://javadev.github.io/underscore-java";
        System.out.println(message);
    }
}
