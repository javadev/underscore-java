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
package com.github.underscore.math;

import com.github.underscore.BinaryOperator;
import com.github.underscore.BiFunction;
import com.github.underscore.Consumer;
import com.github.underscore.Function;
import com.github.underscore.Predicate;
import com.github.underscore.PredicateIndexed;
import com.github.underscore.Tuple;
import com.github.underscore.Optional;
import java.util.*;

public class U<T> extends com.github.underscore.lodash.U<T> {
    public U(final Iterable<T> iterable) {
        super(iterable);
    }

    public U(final String string) {
        super(string);
    }

    public static class Chain<T> extends com.github.underscore.lodash.U.Chain<T> {
        public Chain(final T item) {
            super(item);
        }

        public Chain(final List<T> list) {
            super(list);
        }

        public Chain<T> first() {
            return new Chain<T>(U.first(value()));
        }

        public Chain<T> first(int n) {
            return new Chain<T>(U.first(value(), n));
        }

        public Chain<T> firstOrNull() {
            return new Chain<T>(U.firstOrNull(value()));
        }

        public Chain<T> firstOrNull(final Predicate<T> pred) {
            return new Chain<T>(U.firstOrNull(value(), pred));
        }

        public Chain<T> initial() {
            return new Chain<T>(U.initial(value()));
        }

        public Chain<T> initial(int n) {
            return new Chain<T>(U.initial(value(), n));
        }

        public Chain<T> last() {
            return new Chain<T>(U.last(value()));
        }

        public Chain<T> last(int n) {
            return new Chain<T>(U.last(value(), n));
        }

        public Chain<T> lastOrNull() {
            return new Chain<T>(U.lastOrNull(value()));
        }

        public Chain<T> lastOrNull(final Predicate<T> pred) {
            return new Chain<T>(U.lastOrNull(value(), pred));
        }

        public Chain<T> rest() {
            return new Chain<T>(U.rest(value()));
        }

        public Chain<T> rest(int n) {
            return new Chain<T>(U.rest(value(), n));
        }

        public Chain<T> compact() {
            return new Chain<T>(U.compact(value()));
        }

        public Chain<T> compact(final T falsyValue) {
            return new Chain<T>(U.compact(value(), falsyValue));
        }

        @SuppressWarnings("unchecked")
        public Chain flatten() {
            return new Chain((List<T>) U.flatten(value()));
        }

        public <F> Chain<F> map(final Function<? super T, F> func) {
            return new Chain<F>(U.map(value(), func));
        }

        public <F> Chain<F> mapIndexed(final BiFunction<Integer, ? super T, F> func) {
            return new Chain<F>(U.mapIndexed(value(), func));
        }

        public Chain<T> filter(final Predicate<T> pred) {
            return new Chain<T>(U.filter(value(), pred));
        }

        public Chain<T> filterIndexed(final PredicateIndexed<T> pred) {
            return new Chain<T>(U.filterIndexed(value(), pred));
        }

        public Chain<T> rejectIndexed(final PredicateIndexed<T> pred) {
            return new Chain<T>(U.rejectIndexed(value(), pred));
        }

        public Chain<T> reject(final Predicate<T> pred) {
            return new Chain<T>(U.reject(value(), pred));
        }

        public Chain<T> filterFalse(final Predicate<T> pred) {
            return new Chain<T>(U.filterFalse(value(), pred));
        }

        public <F> Chain<F> reduce(final BiFunction<F, T, F> func, final F zeroElem) {
            return new Chain<F>(U.reduce(value(), func, zeroElem));
        }

        public Chain<Optional<T>> reduce(final BinaryOperator<T> func) {
            return new Chain<Optional<T>>(U.reduce(value(), func));
        }

        public <F> Chain<F> reduceRight(final BiFunction<F, T, F> func, final F zeroElem) {
            return new Chain<F>(U.reduceRight(value(), func, zeroElem));
        }

        public Chain<Optional<T>> reduceRight(final BinaryOperator<T> func) {
            return new Chain<Optional<T>>(U.reduceRight(value(), func));
        }

        public Chain<Optional<T>> find(final Predicate<T> pred) {
            return new Chain<Optional<T>>(U.find(value(), pred));
        }

        public Chain<Optional<T>> findLast(final Predicate<T> pred) {
            return new Chain<Optional<T>>(U.findLast(value(), pred));
        }

        @SuppressWarnings("unchecked")
        public Chain<Comparable> max() {
            return new Chain<Comparable>(U.max((Collection) value()));
        }

        public <F extends Comparable<? super F>> Chain<T> max(final Function<T, F> func) {
            return new Chain<T>(U.max(value(), func));
        }

        @SuppressWarnings("unchecked")
        public Chain<Comparable> min() {
            return new Chain<Comparable>(U.min((Collection) value()));
        }

        public <F extends Comparable<? super F>> Chain<T> min(final Function<T, F> func) {
            return new Chain<T>(U.min(value(), func));
        }

        @SuppressWarnings("unchecked")
        public Chain<Comparable> sort() {
            return new Chain<Comparable>(U.sort((List<Comparable>) value()));
        }

        @SuppressWarnings("unchecked")
        public <F extends Comparable<? super F>> Chain<F> sortWith(final Comparator<F> comparator) {
            return new Chain<F>(U.sortWith((List<F>) value(), comparator));
        }

        public <F extends Comparable<? super F>> Chain<T> sortBy(final Function<T, F> func) {
            return new Chain<T>(U.sortBy(value(), func));
        }

        @SuppressWarnings("unchecked")
        public <K> Chain<Map<K, Comparable>> sortBy(final K key) {
            return new Chain<Map<K, Comparable>>(U.sortBy((List<Map<K, Comparable>>) value(), key));
        }

        public <F> Chain<Map<F, List<T>>> groupBy(final Function<T, F> func) {
            return new Chain<Map<F, List<T>>>(U.groupBy(value(), func));
        }

        public Chain<Map<Object, List<T>>> indexBy(final String property) {
            return new Chain<Map<Object, List<T>>>(U.indexBy(value(), property));
        }

        public <F> Chain<Map<F, Integer>> countBy(final Function<T, F> func) {
            return new Chain<Map<F, Integer>>(U.countBy(value(), func));
        }

        public Chain<T> shuffle() {
            return new Chain<T>(U.shuffle(value()));
        }

        public Chain<T> sample() {
            return new Chain<T>(U.sample(value()));
        }

        public Chain<T> sample(final int howMany) {
            return new Chain<T>(U.newArrayList(U.sample(value(), howMany)));
        }

        public Chain<T> tap(final Consumer<T> func) {
            U.tap(value(), func);
            return new Chain<T>(value());
        }

        public Chain<T> forEach(final Consumer<T> func) {
            U.forEach(value(), func);
            return new Chain<T>(value());
        }

        public Chain<T> forEachRight(final Consumer<T> func) {
            U.forEachRight(value(), func);
            return new Chain<T>(value());
        }

        public Chain<Boolean> every(final Predicate<T> pred) {
            return new Chain<Boolean>(U.every(value(), pred));
        }

        public Chain<Boolean> some(final Predicate<T> pred) {
            return new Chain<Boolean>(U.some(value(), pred));
        }

        public Chain<Boolean> contains(final T elem) {
            return new Chain<Boolean>(U.contains(value(), elem));
        }

        public Chain<T> invoke(final String methodName, final List<Object> args) {
            return new Chain<T>(U.invoke(value(), methodName, args));
        }

        public Chain<T> invoke(final String methodName) {
            return new Chain<T>(U.invoke(value(), methodName));
        }

        public Chain<Object> pluck(final String propertyName) {
            return new Chain<Object>(U.pluck(value(), propertyName));
        }

        public <E> Chain<T> where(final List<Tuple<String, E>> properties) {
            return new Chain<T>(U.where(value(), properties));
        }

        public <E> Chain<Optional<T>> findWhere(final List<Tuple<String, E>> properties) {
            return new Chain<Optional<T>>(U.findWhere(value(), properties));
        }

        public Chain<T> uniq() {
            return new Chain<T>(U.uniq(value()));
        }

        @SuppressWarnings("unchecked")
        public <F> Chain<T> uniq(final Function<T, F> func) {
            return new Chain<T>(U.newArrayList(U.uniq(value(), func)));
        }

        public Chain<T> distinct() {
            return new Chain<T>(U.uniq(value()));
        }

        @SuppressWarnings("unchecked")
        public <F> Chain<F> distinctBy(final Function<T, F> func) {
            return new Chain<F>(U.newArrayList((Iterable<F>) U.uniq(value(), func)));
        }

        public Chain<T> union(final List<T> ... lists) {
            return new Chain<T>(U.union(value(), lists));
        }

        public Chain<T> intersection(final List<T> ... lists) {
            return new Chain<T>(U.intersection(value(), lists));
        }

        public Chain<T> difference(final List<T> ... lists) {
            return new Chain<T>(U.difference(value(), lists));
        }

        public Chain<Integer> range(final int stop) {
            return new Chain<Integer>(newIntegerList(U.range(stop)));
        }

        public Chain<Integer> range(final int start, final int stop) {
            return new Chain<Integer>(newIntegerList(U.range(start, stop)));
        }

        public Chain<Integer> range(final int start, final int stop, final int step) {
            return new Chain<Integer>(newIntegerList(U.range(start, stop, step)));
        }

        public Chain<List<T>> chunk(final int size) {
            return new Chain<List<T>>(U.chunk(value(), size));
        }

        public Chain<T> concat(final List<T> ... lists) {
            return new Chain<T>(U.concat(value(), lists));
        }

        public Chain<T> slice(final int start) {
            return new Chain<T>(U.slice(value(), start));
        }

        public Chain<T> slice(final int start, final int end) {
            return new Chain<T>(U.slice(value(), start, end));
        }

        public Chain<T> reverse() {
            return new Chain<T>(U.reverse(value()));
        }

        public Chain<String> join() {
            return new Chain<String>(U.join(value()));
        }

        public Chain<String> join(final String separator) {
            return new Chain<String>(U.join(value(), separator));
        }

        public Chain<T> skip(final int numberToSkip) {
            return new Chain<T>(value().subList(numberToSkip, value().size()));
        }

        public Chain<T> limit(final int size) {
            return new Chain<T>(value().subList(0, size));
        }

        @SuppressWarnings("unchecked")
        public <K, V> Chain<Map<K, V>> toMap() {
            return new Chain<Map<K, V>>(U.toMap((Iterable<Map.Entry<K, V>>) value()));
        }

        public Chain<T> drop() {
            return new Chain<T>(U.drop(value()));
        }

        public Chain<T> drop(final Integer n) {
            return new Chain<T>(U.drop(value(), n));
        }

        public Chain<T> dropRight() {
            return new Chain<T>(U.dropRight(value()));
        }

        public Chain<T> dropRight(final Integer n) {
            return new Chain<T>(U.dropRight(value(), n));
        }

        public Chain<T> dropWhile(final Predicate<T> pred) {
            return new Chain<T>(U.dropWhile(value(), pred));
        }

        public Chain<T> dropRightWhile(final Predicate<T> pred) {
            return new Chain<T>(U.dropRightWhile(value(), pred));
        }

        @SuppressWarnings("unchecked")
        public Chain<Object> fill(final Object value) {
            return new Chain<Object>(U.fill((List<Object>) value(), value));
        }

        @SuppressWarnings("unchecked")
        public Chain<Object> fill(final Object value, final Integer start, final Integer end) {
            return new Chain<Object>(U.fill((List<Object>) value(), value, start, end));
        }

        public Chain<Object> flattenDeep() {
            return new Chain<Object>(U.flattenDeep(value()));
        }

        @SuppressWarnings("unchecked")
        public Chain<Object> pull(final Object ... values) {
            return new Chain<Object>(U.pull((List<Object>) value(), values));
        }

        @SuppressWarnings("unchecked")
        public Chain<Object> pullAt(final Integer ... indexes) {
            return new Chain<Object>(U.pullAt((List<Object>) value(), indexes));
        }

        public Chain<T> remove(final Predicate<T> pred) {
            return new Chain<T>(U.remove(value(), pred));
        }

        public Chain<T> take() {
            return new Chain<T>(U.take(value()));
        }

        public Chain<T> takeRight() {
            return new Chain<T>(U.takeRight(value()));
        }

        public Chain<T> take(final Integer n) {
            return new Chain<T>(U.take(value(), n));
        }

        public Chain<T> takeRight(final Integer n) {
            return new Chain<T>(U.takeRight(value(), n));
        }

        public Chain<T> takeWhile(final Predicate<T> pred) {
            return new Chain<T>(U.takeWhile(value(), pred));
        }

        public Chain<T> takeRightWhile(final Predicate<T> pred) {
            return new Chain<T>(U.takeRightWhile(value(), pred));
        }

        @SuppressWarnings("unchecked")
        public Chain<T> xor(final List<T> list) {
            return new Chain<T>(U.xor(value(), list));
        }

        public Chain<T> at(final Integer ... indexes) {
            return new Chain<T>(U.at(value(), indexes));
        }

        @SuppressWarnings("unchecked")
        public <F extends Number> Chain<F> sum() {
            return new Chain<F>(U.sum((List<F>) value()));
        }

        public <F extends Number> Chain<F> sum(final Function<T, F> func) {
            return new Chain<F>(U.sum(value(), func));
        }

        @SuppressWarnings("unchecked")
        public Chain<Double> mean() {
            return new Chain<Double>(U.mean((List<Number>) value()));
        }

        @SuppressWarnings("unchecked")
        public Chain<Double> median() {
            return new Chain<Double>(U.median((List<Number>) value()));
        }

        public Chain<String> camelCase() {
            return new Chain<String>(U.camelCase((String) item()));
        }

        public Chain<String> lowerFirst() {
            return new Chain<String>(U.lowerFirst((String) item()));
        }

        public Chain<String> upperFirst() {
            return new Chain<String>(U.upperFirst((String) item()));
        }

        public Chain<String> capitalize() {
            return new Chain<String>(U.capitalize((String) item()));
        }

        public Chain<String> deburr() {
            return new Chain<String>(U.deburr((String) item()));
        }

        public Chain<Boolean> endsWith(final String target) {
            return new Chain<Boolean>(U.endsWith((String) item(), target));
        }

        public Chain<Boolean> endsWith(final String target, final Integer position) {
            return new Chain<Boolean>(U.endsWith((String) item(), target, position));
        }

        public Chain<String> kebabCase() {
            return new Chain<String>(U.kebabCase((String) item()));
        }

        public Chain<String> repeat(final int length) {
            return new Chain<String>(U.repeat((String) item(), length));
        }

        public Chain<String> pad(final int length) {
            return new Chain<String>(U.pad((String) item(), length));
        }

        public Chain<String> pad(final int length, final String chars) {
            return new Chain<String>(U.pad((String) item(), length, chars));
        }

        public Chain<String> padStart(final int length) {
            return new Chain<String>(U.padStart((String) item(), length));
        }

        public Chain<String> padStart(final int length, final String chars) {
            return new Chain<String>(U.padStart((String) item(), length, chars));
        }

        public Chain<String> padEnd(final int length) {
            return new Chain<String>(U.padEnd((String) item(), length));
        }

        public Chain<String> padEnd(final int length, final String chars) {
            return new Chain<String>(U.padEnd((String) item(), length, chars));
        }

        public Chain<String> snakeCase() {
            return new Chain<String>(U.snakeCase((String) item()));
        }

        public Chain<String> startCase() {
            return new Chain<String>(U.startCase((String) item()));
        }

        public Chain<Boolean> startsWith(final String target) {
            return new Chain<Boolean>(U.startsWith((String) item(), target));
        }

        public Chain<Boolean> startsWith(final String target, final Integer position) {
            return new Chain<Boolean>(U.startsWith((String) item(), target, position));
        }

        public Chain<String> trim() {
            return new Chain<String>(U.trim((String) item()));
        }

        public Chain<String> trim(final String chars) {
            return new Chain<String>(U.trim((String) item(), chars));
        }

        public Chain<String> trimStart() {
            return new Chain<String>(U.trimStart((String) item()));
        }

        public Chain<String> trimStart(final String chars) {
            return new Chain<String>(U.trimStart((String) item(), chars));
        }

        public Chain<String> trimEnd() {
            return new Chain<String>(U.trimEnd((String) item()));
        }

        public Chain<String> trunc() {
            return new Chain<String>(U.trunc((String) item()));
        }

        public Chain<String> trunc(final int length) {
            return new Chain<String>(U.trunc((String) item(), length));
        }

        public Chain<String> trimEnd(final String chars) {
            return new Chain<String>(U.trimEnd((String) item(), chars));
        }

        public Chain<String> uncapitalize() {
            return new Chain<String>(U.uncapitalize((String) item()));
        }

        public Chain<String> words() {
            return new Chain<String>(U.words((String) item()));
        }

        public Chain<String> toJson() {
            return new Chain<String>(U.toJson((Collection) value()));
        }

        public Chain<Object> fromJson() {
            return new Chain<Object>(U.fromJson((String) item()));
        }

        public Chain<String> toXml() {
            return new Chain<String>(U.toXml((Collection) value()));
        }

        public Chain<Object> fromXml() {
            return new Chain<Object>(U.fromXml((String) item()));
        }

        public Chain<String> fetch() {
            return new Chain<String>(U.fetch((String) item()).text());
        }

        public Chain<String> fetch(final String method, final String body) {
            return new Chain<String>(U.fetch((String) item(), method, body).text());
        }

        @SuppressWarnings("unchecked")
        public Chain<List<T>> createPermutationWithRepetition(final int permutationLength) {
            return new Chain<List<T>>(U.createPermutationWithRepetition((List<T>) value(), permutationLength));
        }
    }

    public static Chain<String> chain(final String item) {
        return new U.Chain<String>(item);
    }

    public static <T> Chain<T> chain(final List<T> list) {
        return new U.Chain<T>(list);
    }

    public static <T> Chain<T> chain(final Iterable<T> iterable) {
        return new U.Chain<T>(newArrayList(iterable));
    }

    public static <T> Chain<T> chain(final Iterable<T> iterable, int size) {
        return new U.Chain<T>(newArrayList(iterable, size));
    }

    public static <T> Chain<T> chain(final T ... array) {
        return new U.Chain<T>(Arrays.asList(array));
    }

    public static Chain<Integer> chain(final int[] array) {
        return new U.Chain<Integer>(newIntegerList(array));
    }

    @SuppressWarnings("unchecked")
    public Chain<T> chain() {
        return new U.Chain<T>(newArrayList(value()));
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
}
