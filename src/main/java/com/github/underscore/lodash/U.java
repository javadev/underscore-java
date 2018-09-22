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
package com.github.underscore.lodash;

import com.github.underscore.BinaryOperator;
import com.github.underscore.BiFunction;
import com.github.underscore.Consumer;
import com.github.underscore.Function;
import com.github.underscore.Function3;
import com.github.underscore.Predicate;
import com.github.underscore.PredicateIndexed;
import com.github.underscore.Tuple;
import com.github.underscore.Optional;
import java.util.*;

public class U<T> extends com.github.underscore.U<T> {
    private static final int DEFAULT_TRUNC_LENGTH = 30;
    private static final String DEFAULT_TRUNC_OMISSION = "...";
    private static final String NULL = "null";
    private static final String ELEMENT_TEXT = "element";
    private static final String TEXT = "#text";
    private static final String ELEMENT = "<" + ELEMENT_TEXT + ">";
    private static final String CLOSED_ELEMENT = "</" + ELEMENT_TEXT + ">";
    private static final String EMPTY_ELEMENT = ELEMENT + CLOSED_ELEMENT;
    private static final String NULL_ELEMENT = ELEMENT + NULL + CLOSED_ELEMENT;
    private static final java.nio.charset.Charset UTF_8 = java.nio.charset.Charset.forName("UTF-8");
    private static final java.util.regex.Pattern RE_LATIN_1 = java.util.regex.Pattern.compile(
        "[\\xc0-\\xd6\\xd8-\\xde\\xdf-\\xf6\\xf8-\\xff]");
    private static final java.util.regex.Pattern RE_PROP_NAME = java.util.regex.Pattern.compile(
        "[^.\\[\\]]+|\\[(?:(-?\\d+(?:\\.\\d+)?)|([\"'])((?:(?!\2)\\[^\\]|\\.)*?)\2)\\]|(?=(\\.|\\[\\])(?:\4|$))");
    private static final Map<String, String> DEBURRED_LETTERS = new LinkedHashMap<String, String>();
    private static final Map<String, List<String>> DEFAULT_HEADER_FIELDS = new HashMap<String, List<String>>();
    private static final Set<String> SUPPORTED_HTTP_METHODS = new HashSet<String>(
        Arrays.asList("GET", "POST", "PUT", "DELETE"));
    private static final int BUFFER_LENGTH_1024 = 1024;
    private static final int RESPONSE_CODE_400 = 400;
    private static String upper = "[A-Z\\xc0-\\xd6\\xd8-\\xde\\u0400-\\u04FF]";
    private static String lower = "[a-z\\xdf-\\xf6\\xf8-\\xff]+";
    private static java.util.regex.Pattern reWords = java.util.regex.Pattern.compile(
        upper + "+(?=" + upper + lower + ")|" + upper + "?" + lower + "|" + upper + "+|[0-9]+");

    static {
        String[] deburredLetters = new String[] {
        "\u00c0", "A", "\u00c1", "A", "\u00c2", "A", "\u00c3", "A",
        "\u00c4", "A", "\u00c5", "A",
        "\u00e0", "a", "\u00e1", "a", "\u00e2", "a", "\u00e3", "a",
        "\u00e4", "a", "\u00e5", "a",
        "\u00c7", "C", "\u00e7", "c",
        "\u00d0", "D", "\u00f0", "d",
        "\u00c8", "E", "\u00c9", "E", "\u00ca", "E", "\u00cb", "E",
        "\u00e8", "e", "\u00e9", "e", "\u00ea", "e", "\u00eb", "e",
        "\u00cC", "I", "\u00cd", "I", "\u00ce", "I", "\u00cf", "I",
        "\u00eC", "i", "\u00ed", "i", "\u00ee", "i", "\u00ef", "i",
        "\u00d1", "N", "\u00f1", "n",
        "\u00d2", "O", "\u00d3", "O", "\u00d4", "O", "\u00d5", "O",
        "\u00d6", "O", "\u00d8", "O",
        "\u00f2", "o", "\u00f3", "o", "\u00f4", "o", "\u00f5", "o",
        "\u00f6", "o", "\u00f8", "o",
        "\u00d9", "U", "\u00da", "U", "\u00db", "U", "\u00dc", "U",
        "\u00f9", "u", "\u00fa", "u", "\u00fb", "u", "\u00fc", "u",
        "\u00dd", "Y", "\u00fd", "y", "\u00ff", "y",
        "\u00c6", "Ae", "\u00e6", "ae",
        "\u00de", "Th", "\u00fe", "th",
        "\u00df", "ss"};
        for (int index = 0; index < deburredLetters.length; index += 2) {
            DEBURRED_LETTERS.put(deburredLetters[index], deburredLetters[index + 1]);
        }
        DEFAULT_HEADER_FIELDS.put("Content-Type", Arrays.asList("application/json", "charset=utf-8"));
    }

    public U(final Iterable<T> iterable) {
        super(iterable);
    }

    public U(final String string) {
        super(string);
    }

    public static class Chain<T> extends com.github.underscore.U.Chain<T> {
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
            return new Chain(U.flatten(value()));
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

        public <F> Chain<Map<F, Optional<T>>> groupBy(final Function<T, F> func,
            final BinaryOperator<T> binaryOperator) {
            return new Chain<Map<F, Optional<T>>>(U.groupBy(value(), func, binaryOperator));
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

        @SuppressWarnings("unchecked")
        public Chain<T> union(final List<T> ... lists) {
            return new Chain<T>(U.union(value(), lists));
        }

        @SuppressWarnings("unchecked")
        public Chain<T> intersection(final List<T> ... lists) {
            return new Chain<T>(U.intersection(value(), lists));
        }

        @SuppressWarnings("unchecked")
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

        @SuppressWarnings("unchecked")
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

        public Chain<String> toJsonJavaString() {
            return new Chain<String>(U.toJsonJavaString((Collection) value()));
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

    @SuppressWarnings("unchecked")
    public static <T> Chain<T> chain(final T ... list) {
        return new U.Chain<T>(Arrays.asList(list));
    }

    public static Chain<Integer> chain(final int[] array) {
        return new U.Chain<Integer>(newIntegerList(array));
    }

    public Chain<T> chain() {
        return new U.Chain<T>(newArrayList(value()));
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
        return reverse(dropWhile(reverse(iterable), pred));
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

    @SuppressWarnings("unchecked")
    public List<Object> fill(Object value) {
        return fill((List<Object>) getIterable(), value);
    }

    public static List<Object> fill(final List<Object> list, Object value, Integer start, Integer end) {
        for (int index = start; index < end; index += 1) {
            list.set(index, value);
        }
        return list;
    }

    @SuppressWarnings("unchecked")
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

    @SuppressWarnings("unchecked")
    public List<Object> pull(Object ... values) {
        return pull((List<Object>) getIterable(), values);
    }

    public static List<Object> pullAt(final List<Object> list, final Integer ... indexes) {
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

    @SuppressWarnings("unchecked")
    public List<Object> pullAt(final Integer ... indexes) {
        return pullAt((List<Object>) getIterable(), indexes);
    }

    public static <T> List<T> remove(final List<T> list, final Predicate<T> pred) {
        final List<T> result = newArrayList();
        for (final Iterator<T> iterator = list.iterator(); iterator.hasNext(); ) {
            final T object = iterator.next();
            if (pred.test(object)) {
                result.add(object);
                iterator.remove();
            }
        }
        return result;
    }

    public List<T> remove(final Predicate<T> pred) {
        return remove((List<T>) getIterable(), pred);
    }

    public static <T> List<T> take(final Iterable<T> iterable) {
        return first(newArrayList(iterable), 1);
    }

    public List<T> take() {
        return take(getIterable());
    }

    public static <T> List<T> takeRight(final Iterable<T> iterable) {
        return last(newArrayList(iterable), 1);
    }

    public List<T> takeRight() {
        return takeRight(getIterable());
    }

    public static <T> List<T> take(final Iterable<T> iterable, final Integer n) {
        return first(newArrayList(iterable), n);
    }

    public List<T> take(final Integer n) {
        return take(getIterable(), n);
    }

    public static <T> List<T> takeRight(final Iterable<T> iterable, final Integer n) {
        return last(newArrayList(iterable), n);
    }

    public List<T> takeRight(final Integer n) {
        return takeRight(getIterable(), n);
    }

    public static <T> List<T> takeWhile(final Iterable<T> iterable, final Predicate<T> pred) {
        return first(newArrayList(iterable), findIndex(newArrayList(iterable), negate(pred)));
    }

    public List<T> takeWhile(final Predicate<T> pred) {
        return takeWhile(getIterable(), pred);
    }

    public static <T> List<T> takeRightWhile(final Iterable<T> iterable, final Predicate<T> pred) {
        return reverse(takeWhile(reverse(iterable), pred));
    }

    public List<T> takeRightWhile(final Predicate<T> pred) {
        return takeRightWhile(getIterable(), pred);
    }

    @SuppressWarnings("unchecked")
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

    @SuppressWarnings("unchecked")
    public List<T> xor(final List<T> list) {
        return xor((List<T>) getIterable(), list);
    }

    public static <T> List<T> at(final List<T> list, final Integer ... indexes) {
        final List<T> result = newArrayList();
        final List<Integer> indexesList = Arrays.asList(indexes);
        int index = 0;
        for (final Iterator<T> iterator = list.iterator(); iterator.hasNext(); ) {
            final T object = iterator.next();
            if (indexesList.contains(index)) {
                result.add(object);
            }
            index += 1;
        }
        return result;
    }

    public List<T> at(final Integer ... indexes) {
        return at((List<T>) getIterable(), indexes);
    }

    public static <T extends Number> T sum(final Iterable<T> iterable) {
        T result = null;
        for (final T item : iterable) {
            result = add(result, item);
        }
        return result;
    }

    public static <E, F extends  Number> F sum(final Iterable<E> iterable, final Function<E, F> func) {
        F result = null;
        for (final E item : iterable) {
            result = add(result, func.apply(item));
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public <F extends Number> F sum() {
        return sum((List<F>) getIterable());
    }

    @SuppressWarnings("unchecked")
    public <E, F extends  Number> F sum(final Function<E, F> func) {
        return sum((List<E>) getIterable(), func);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Number> T add(final T first, final T second) {
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
            result = add(result, item);
            count += 1;
        }
        if (result == null) {
            return 0d;
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

    public static String camelCase(final String string) {
        return createCompounder(new Function3<String, String, Integer, String>() {
            public String apply(final String result, final String word, final Integer index) {
                final String localWord = word.toLowerCase(Locale.getDefault());
                return result + (index > 0 ? word.substring(0, 1).toUpperCase(Locale.getDefault())
                    + word.substring(1) : localWord);
            }
        }).apply(string);
    }

    public static String lowerFirst(final String string) {
        return createCaseFirst("toLowerCase").apply(string);
    }

    public static String upperFirst(final String string) {
        return createCaseFirst("toUpperCase").apply(string);
    }

    public static String capitalize(final String string) {
        return upperFirst(baseToString(string).toLowerCase());
    }

    public static String uncapitalize(final String string) {
        return lowerFirst(baseToString(string).toLowerCase());
    }

    private static String baseToString(String value) {
        return value == null ? "" : value;
    }

    public static String deburr(final String string) {
        final String localString = baseToString(string);
        final StringBuilder sb = new StringBuilder();
        for (final String str : localString.split("")) {
            if (RE_LATIN_1.matcher(str).matches()) {
                sb.append(DEBURRED_LETTERS.get(str));
            } else {
                sb.append(str);
            }
        }
        return sb.toString();
    }

    public static List<String> words(final String string) {
        final String localString = baseToString(string);
        final List<String> result = new ArrayList<String>();
        final java.util.regex.Matcher matcher = reWords.matcher(localString);
        while (matcher.find()) {
            result.add(matcher.group());
        }
        return result;
    }

    private static Function<String, String> createCompounder(
        final Function3<String, String, Integer, String> callback) {
        return new Function<String, String>() {
            public String apply(final String string) {
                int index = -1;
                List<String> array = words(deburr(string));
                int length = array.size();
                String result = "";

                while (++index < length) {
                    result = callback.apply(result, array.get(index), index);
                }
                return result;
            }
        };
    }

    private static Function<String, String> createCaseFirst(final String methodName) {
        return new Function<String, String>() {
            public String apply(final String string) {
                final String localString = baseToString(string);
                final String chr = localString.isEmpty() ? "" : localString.substring(0, 1);
                final String trailing = localString.length() > 1 ? localString.substring(1) : "";
                return U.invoke(Arrays.asList(chr), methodName).get(0) + trailing;
            }
        };
    }

    public static boolean endsWith(final String string, final String target) {
        return endsWith(string, target, null);
    }

    public static boolean endsWith(final String string, final String target, final Integer position) {
        if (string == null || target == null) {
            return false;
        }
        final String localString = baseToString(string);

        final int length = localString.length();
        final int fixedPosition = position == null || position < 0 ? 0 : position;
        final int localPosition = position == null ? length : Math.min(fixedPosition, length);

        final int localPosition2 = localPosition - target.length();
      return localPosition2 >= 0 && localString.indexOf(target, localPosition2) == localPosition2;
    }

    public static String kebabCase(final String string) {
        return createCompounder(new Function3<String, String, Integer, String>() {
            public String apply(final String result, final String word, final Integer index) {
                return result + (index > 0 ? "-" : "") + word.toLowerCase(Locale.getDefault());
            }
        }).apply(string);
    }

    public static String repeat(final String string, final int length) {
        final StringBuilder result = new StringBuilder();
        final StringBuilder localString = new StringBuilder(baseToString(string));
        if (length < 1 || string == null) {
            return result.toString();
        }
        int n = length;
        do {
            if (n % 2 != 0) {
                result.append(localString);
            }
            n = (int) Math.floor(n / (double) 2);
            localString.append(localString.toString());
        } while (n > 0);
        return result.toString();
    }

    private static String createPadding(final String string, final int length, final String chars) {
        final int strLength = string.length();
        final int padLength = length - strLength;
        final String localChars = chars == null ? " " : chars;
        return repeat(localChars, (int) Math.ceil(padLength / (double) localChars.length())).substring(0, padLength);
    }

    public static String pad(final String string, final int length) {
        return pad(string, length, null);
    }

    public static String pad(final String string, final int length, final String chars) {
        final String localString = baseToString(string);
        final int strLength = localString.length();
        if (strLength >= length) {
            return localString;
        }
        final double mid = (length - strLength) / (double) 2;
        final int leftLength = (int) Math.floor(mid);
        final int rightLength = (int) Math.ceil(mid);
        final String localChars = createPadding("", rightLength, chars);
        return localChars.substring(0, leftLength) + localString + localChars;
    }

    private static Function3<String, Integer, String, String> createPadDir(final boolean fromRight) {
        return new Function3<String, Integer, String, String>() {
            public String apply(String string, Integer length, String chars) {
                final String localString = baseToString(string);
                return (fromRight ? localString : "") + createPadding(localString, length, chars)
                    + (fromRight ? "" : localString);
            }
        };
    }

    public static String padStart(final String string, final Integer length) {
         return createPadDir(false).apply(string, length, null);
    }

    public static String padStart(final String string, final Integer length, final String chars) {
         return createPadDir(false).apply(string, length, chars);
    }

    public static String padEnd(final String string, final Integer length) {
         return createPadDir(true).apply(string, length, null);
    }

    public static String padEnd(final String string, final Integer length, final String chars) {
         return createPadDir(true).apply(string, length, chars);
    }

    public static String snakeCase(final String string) {
        return createCompounder(new Function3<String, String, Integer, String>() {
            public String apply(final String result, final String word, final Integer index) {
                return result + (index > 0 ? "_" : "") + word.toLowerCase(Locale.getDefault());
            }
        }).apply(string);
    }

    public static String startCase(final String string) {
        return createCompounder(new Function3<String, String, Integer, String>() {
            public String apply(final String result, final String word, final Integer index) {
                return result + (index > 0 ? " " : "") + word.substring(0, 1).toUpperCase(Locale.getDefault())
                    + word.substring(1);
            }
        }).apply(string);
    }

    public static boolean startsWith(final String string, final String target) {
        return startsWith(string, target, null);
    }

    public static boolean startsWith(final String string, final String target, final Integer position) {
        if (string == null || target == null) {
            return false;
        }
        final String localString = baseToString(string);

        final int length = localString.length();
        final int localPosition = position == null ? 0
          : Math.min(position < 0 ? 0 : position, length);

        return localString.lastIndexOf(target, localPosition) == localPosition;
    }

    private static int charsLeftIndex(final String string, final String chars) {
        int index = 0;
        final int length = string.length();
        while (index < length && chars.indexOf(string.charAt(index)) > -1) {
            index += 1;
        }
        return index == length ? -1 : index;
    }

    private static int charsRightIndex(final String string, final String chars) {
        int index = string.length() - 1;
        while (index >= 0 && chars.indexOf(string.charAt(index)) > -1) {
            index -= 1;
        }
        return index;
    }

    public static String trim(final String string) {
        return trim(string, null);
    }

    public static String trim(final String string, final String chars) {
        final String localString = baseToString(string);
        if (localString.isEmpty()) {
            return localString;
        }
        final String localChars;
        if (chars == null) {
            localChars = " ";
        } else {
            localChars = chars;
        }
        final int leftIndex = charsLeftIndex(localString, localChars);
        final int rightIndex = charsRightIndex(localString, localChars);
        return leftIndex > -1 ? localString.substring(leftIndex, rightIndex + 1) : localString;
    }

    public static String trimStart(final String string) {
        return trimStart(string, null);
    }

    public static String trimStart(final String string, final String chars) {
        final String localString = baseToString(string);
        if (localString.isEmpty()) {
            return localString;
        }
        final String localChars;
        if (chars == null) {
            localChars = " ";
        } else {
            localChars = chars;
        }
        final int leftIndex = charsLeftIndex(localString, localChars);
        return leftIndex > -1 ? localString.substring(leftIndex, localString.length()) : localString;
    }

    public static String trimEnd(final String string) {
        return trimEnd(string, null);
    }

    public static String trimEnd(final String string, final String chars) {
        final String localString = baseToString(string);
        if (localString.isEmpty()) {
            return localString;
        }
        final String localChars;
        if (chars == null) {
            localChars = " ";
        } else {
            localChars = chars;
        }
        final int rightIndex = charsRightIndex(localString, localChars);
        return rightIndex > -1 ? localString.substring(0, rightIndex + 1) : localString;
    }

    public static String trunc(final String string) {
        return trunc(string, DEFAULT_TRUNC_LENGTH);
    }

    public static String trunc(final String string, final Integer length) {
        final String localString = baseToString(string);
        final String omission = DEFAULT_TRUNC_OMISSION;
        if (length >= localString.length()) {
            return localString;
        }
        final int end = length - omission.length();
        final String result = localString.substring(0, end);
        return result + omission;
    }

    public static List<String> stringToPath(final String string) {
        final List<String> result = new ArrayList<String>();
        final java.util.regex.Matcher matcher = RE_PROP_NAME.matcher(baseToString(string));
        while (matcher.find()) {
            result.add(matcher.group(1) == null ? matcher.group(0) : matcher.group(1));
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private static <T> T baseGet(final Map<String, Object> object, final String path) {
        final List<String> paths = stringToPath(path);
        int index = 0;
        final int length = paths.size();

        Object localObject = object;
        while (localObject != null && index < length) {
            if (localObject instanceof Map) {
                localObject = ((Map) localObject).get(paths.get(index));
            } else if (localObject instanceof List) {
                localObject = ((List) localObject).get(Integer.parseInt(paths.get(index)));
            } else {
                break;
            }
            index += 1;
        }
        if (index > 0 && index == length) {
            return (T) localObject;
        }
        return null;
    }

    public static <T> T get(final Map<String, Object> object, final String path) {
        return baseGet(object, path);
    }

    public static class JsonStringBuilder {
        public enum Step {
            TWO_SPACES(2), THREE_SPACES(3), FOUR_SPACES(4), COMPACT(0), TABS(1);
            private int ident;
            Step(int ident) {
                this.ident = ident;
            }
            public int getIdent() {
                return ident;
            }
        }
        private final StringBuilder builder;
        private final Step identStep;
        private int ident;

        public JsonStringBuilder(Step identStep) {
            builder = new StringBuilder();
            this.identStep = identStep;
        }

        public JsonStringBuilder() {
            builder = new StringBuilder();
            this.identStep = Step.TWO_SPACES;
        }

        public JsonStringBuilder append(final char character) {
            builder.append(character);
            return this;
        }

        public JsonStringBuilder append(final String string) {
            builder.append(string);
            return this;
        }

        public JsonStringBuilder fillSpaces() {
            for (int index = 0; index < ident; index += 1) {
                builder.append(identStep == Step.TABS ? '\t' : ' ');
            }
            return this;
        }

        public JsonStringBuilder incIdent() {
            ident += identStep.getIdent();
            return this;
        }

        public JsonStringBuilder decIdent() {
            ident -= identStep.getIdent();
            return this;
        }

        public JsonStringBuilder newLine() {
            if (identStep != Step.COMPACT) {
                builder.append("\n");
            }
            return this;
        }

        public Step getIdentStep() {
            return identStep;
        }

        public String toString() {
            return builder.toString();
        }
    }

    public static class JsonArray {
        public static void writeJson(Collection collection, JsonStringBuilder builder) {
            if (collection == null) {
                builder.append(NULL);
                return;
            }

            Iterator iter = collection.iterator();

            builder.append('[').incIdent();
            if (!collection.isEmpty()) {
                builder.newLine();
            }
            while (iter.hasNext()) {
                Object value = iter.next();
                if (value == null) {
                    builder.fillSpaces().append(NULL);
                    continue;
                }

                builder.fillSpaces();
                JsonValue.writeJson(value, builder);
                if (iter.hasNext()) {
                    builder.append(',').newLine();
                }
            }
            builder.newLine().decIdent().fillSpaces().append(']');
        }

        public static void writeJson(byte[] array, JsonStringBuilder builder) {
            if (array == null) {
                builder.append(NULL);
            } else if (array.length == 0) {
                builder.append("[]");
            } else {
                builder.append('[').incIdent().newLine();
                builder.fillSpaces().append(String.valueOf(array[0]));

                for (int i = 1; i < array.length; i++) {
                    builder.append(',').newLine().fillSpaces();
                    builder.append(String.valueOf(array[i]));
                }

                builder.newLine().decIdent().fillSpaces().append(']');
            }
        }

        public static void writeJson(short[] array, JsonStringBuilder builder) {
            if (array == null) {
                builder.append(NULL);
            } else if (array.length == 0) {
                builder.append("[]");
            } else {
                builder.append('[').incIdent().newLine();
                builder.fillSpaces().append(String.valueOf(array[0]));

                for (int i = 1; i < array.length; i++) {
                    builder.append(',').newLine().fillSpaces();
                    builder.append(String.valueOf(array[i]));
                }

                builder.newLine().decIdent().fillSpaces().append(']');
            }
        }

        public static void writeJson(int[] array, JsonStringBuilder builder) {
            if (array == null) {
                builder.append(NULL);
            } else if (array.length == 0) {
                builder.append("[]");
            } else {
                builder.append('[').incIdent().newLine();
                builder.fillSpaces().append(String.valueOf(array[0]));

                for (int i = 1; i < array.length; i++) {
                    builder.append(',').newLine().fillSpaces();
                    builder.append(String.valueOf(array[i]));
                }

                builder.newLine().decIdent().fillSpaces().append(']');
            }
        }

        public static void writeJson(long[] array, JsonStringBuilder builder) {
            if (array == null) {
                builder.append(NULL);
            } else if (array.length == 0) {
                builder.append("[]");
            } else {
                builder.append('[').incIdent().newLine();
                builder.fillSpaces().append(String.valueOf(array[0]));

                for (int i = 1; i < array.length; i++) {
                    builder.append(',').newLine().fillSpaces();
                    builder.append(String.valueOf(array[i]));
                }

                builder.newLine().decIdent().fillSpaces().append(']');
            }
        }

        public static void writeJson(float[] array, JsonStringBuilder builder) {
            if (array == null) {
                builder.append(NULL);
            } else if (array.length == 0) {
                builder.append("[]");
            } else {
                builder.append('[').incIdent().newLine();
                builder.fillSpaces().append(String.valueOf(array[0]));

                for (int i = 1; i < array.length; i++) {
                    builder.append(',').newLine().fillSpaces();
                    builder.append(String.valueOf(array[i]));
                }

                builder.newLine().decIdent().fillSpaces().append(']');
            }
        }

        public static void writeJson(double[] array, JsonStringBuilder builder) {
            if (array == null) {
                builder.append(NULL);
            } else if (array.length == 0) {
                builder.append("[]");
            } else {
                builder.append('[').incIdent().newLine();
                builder.fillSpaces().append(String.valueOf(array[0]));

                for (int i = 1; i < array.length; i++) {
                    builder.append(',').newLine().fillSpaces();
                    builder.append(String.valueOf(array[i]));
                }

                builder.newLine().decIdent().fillSpaces().append(']');
            }
        }

        public static void writeJson(boolean[] array, JsonStringBuilder builder) {
            if (array == null) {
                builder.append(NULL);
            } else if (array.length == 0) {
                builder.append("[]");
            } else {
                builder.append('[').incIdent().newLine();
                builder.fillSpaces().append(String.valueOf(array[0]));

                for (int i = 1; i < array.length; i++) {
                    builder.append(',').newLine().fillSpaces();
                    builder.append(String.valueOf(array[i]));
                }

                builder.newLine().decIdent().fillSpaces().append(']');
            }
        }

        public static void writeJson(char[] array, JsonStringBuilder builder) {
            if (array == null) {
                builder.append(NULL);
            } else if (array.length == 0) {
                builder.append("[]");
            } else {
                builder.append('[').incIdent().newLine();
                builder.fillSpaces().append('\"').append(String.valueOf(array[0])).append('\"');

                for (int i = 1; i < array.length; i++) {
                    builder.append(',').newLine().fillSpaces();
                    builder.append('\"').append(String.valueOf(array[i])).append('\"');
                }

                builder.newLine().decIdent().fillSpaces().append(']');
            }
        }

        public static void writeJson(Object[] array, JsonStringBuilder builder) {
            if (array == null) {
                builder.append(NULL);
            } else if (array.length == 0) {
                builder.append("[]");
            } else {
                builder.append('[').newLine().incIdent().fillSpaces();
                JsonValue.writeJson(array[0], builder);

                for (int i = 1; i < array.length; i++) {
                    builder.append(',').newLine().fillSpaces();
                    JsonValue.writeJson(array[i], builder);
                }

                builder.newLine().decIdent().fillSpaces().append(']');
            }
        }
    }

    public static class JsonObject {
        public static void writeJson(Map map, JsonStringBuilder builder) {
            if (map == null) {
                builder.append(NULL);
                return;
            }

            Iterator iter = map.entrySet().iterator();

            builder.append('{').incIdent();
            if (!map.isEmpty()) {
                builder.newLine();
            }
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                builder.fillSpaces().append('\"');
                builder.append(JsonValue.unescapeName(String.valueOf(entry.getKey())));
                builder.append('\"');
                builder.append(':');
                if (builder.getIdentStep() != JsonStringBuilder.Step.COMPACT) {
                    builder.append(' ');
                }
                JsonValue.writeJson(entry.getValue(), builder);
                if (iter.hasNext()) {
                    builder.append(',').newLine();
                }
            }
            builder.newLine().decIdent().fillSpaces().append('}');
        }
    }

    public static class JsonValue {
        public static void writeJson(Object value, JsonStringBuilder builder) {
            if (value == null) {
                builder.append(NULL);
            } else if (value instanceof String) {
                builder.append('"').append(escape((String) value)).append('"');
            } else if (value instanceof Double) {
                if (((Double) value).isInfinite() || ((Double) value).isNaN()) {
                    builder.append(NULL);
                } else {
                    builder.append(value.toString());
                }
            } else if (value instanceof Float) {
                if (((Float) value).isInfinite() || ((Float) value).isNaN()) {
                    builder.append(NULL);
                } else {
                    builder.append(value.toString());
                }
            } else if (value instanceof Number) {
                builder.append(value.toString());
            } else if (value instanceof Boolean) {
                builder.append(value.toString());
            } else if (value instanceof Map) {
                JsonObject.writeJson((Map) value, builder);
            } else if (value instanceof Collection) {
                JsonArray.writeJson((Collection) value, builder);
            } else if (value instanceof byte[]) {
                JsonArray.writeJson((byte[]) value, builder);
            } else if (value instanceof short[]) {
                JsonArray.writeJson((short[]) value, builder);
            } else if (value instanceof int[]) {
                JsonArray.writeJson((int[]) value, builder);
            } else if (value instanceof long[]) {
                JsonArray.writeJson((long[]) value, builder);
            } else if (value instanceof float[]) {
                JsonArray.writeJson((float[]) value, builder);
            } else if (value instanceof double[]) {
                JsonArray.writeJson((double[]) value, builder);
            } else if (value instanceof boolean[]) {
                JsonArray.writeJson((boolean[]) value, builder);
            } else if (value instanceof char[]) {
                JsonArray.writeJson((char[]) value, builder);
            } else if (value instanceof Object[]) {
                JsonArray.writeJson((Object[]) value, builder);
            } else {
                builder.append(value.toString());
            }
        }

        public static String unescapeName(final String name) {
            final int length = name.length();
            if (length == 0 || "__EE__EMPTY__EE__".equals(name)) {
                return "";
            }
            if ("-__EE__EMPTY__EE__".equals(name)) {
                return "-";
            }
            StringBuilder result = new StringBuilder();
            int underlineCount = 0;
            StringBuilder lastChars = new StringBuilder();
            outer:
            for (int i = 0; i < length; ++i) {
                char ch = name.charAt(i);
                if (ch == '_') {
                    lastChars.append(ch);
                } else {
                    if (lastChars.length() == 2) {
                        StringBuilder nameToDecode = new StringBuilder();
                        for (int j = i; j < length; ++j) {
                            if (name.charAt(j) == '_') {
                                underlineCount += 1;
                                if (underlineCount == 2) {
                                    result.append(JsonValue.escape(Base32.decode(nameToDecode.toString())));
                                    i = j;
                                    underlineCount = 0;
                                    lastChars.setLength(0);
                                    continue outer;
                                }
                            } else {
                                nameToDecode.append(name.charAt(j));
                                underlineCount = 0;
                            }
                        }
                    }
                    result.append(lastChars).append(ch);
                    lastChars.setLength(0);
                }
            }
            return result.append(lastChars).toString();
        }

        public static String escape(String s) {
            if (s == null) {
                return null;
            }
            StringBuilder sb = new StringBuilder();
            escape(s, sb);
            return sb.toString();
        }

        private static void escape(String s, StringBuilder sb) {
            final int len = s.length();
            for (int i = 0; i < len; i++) {
                char ch = s.charAt(i);
                switch (ch) {
                case '"':
                    sb.append("\\\"");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                case '/':
                    sb.append("\\/");
                    break;
                default:
                    if (ch <= '\u001F' || ch >= '\u007F' && ch <= '\u009F'
                        || ch >= '\u2000' && ch <= '\u20FF') {
                        String ss = Integer.toHexString(ch);
                        sb.append("\\u");
                        for (int k = 0; k < 4 - ss.length(); k++) {
                            sb.append('0');
                        }
                        sb.append(ss.toUpperCase());
                    } else {
                        sb.append(ch);
                    }
                    break;
                }
            }
        }
    }

    public static String toJson(Collection collection, JsonStringBuilder.Step identStep) {
        final JsonStringBuilder builder = new JsonStringBuilder(identStep);

        JsonArray.writeJson(collection, builder);
        return builder.toString();
    }

    public static String toJson(Collection collection) {
        return toJson(collection, JsonStringBuilder.Step.TWO_SPACES);
    }

    public String toJson() {
        return toJson((Collection) getIterable());
    }

    public static String toJson(Map map, JsonStringBuilder.Step identStep) {
        final JsonStringBuilder builder = new JsonStringBuilder(identStep);

        JsonObject.writeJson(map, builder);
        return builder.toString();
    }

    public static String toJson(Map map) {
        return toJson(map, JsonStringBuilder.Step.TWO_SPACES);
    }

    public static class XmlStringBuilder {
        public enum Step {
            TWO_SPACES(2), THREE_SPACES(3), FOUR_SPACES(4), COMPACT(0), TABS(1);
            private int ident;
            Step(int ident) {
                this.ident = ident;
            }
            public int getIdent() {
                return ident;
            }
        }

        protected final StringBuilder builder;
        private final Step identStep;
        private int ident;

        public XmlStringBuilder() {
            builder = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n");
            identStep = Step.TWO_SPACES;
            ident = 2;
        }

        public XmlStringBuilder(StringBuilder builder, Step identStep, int ident) {
            this.builder = builder;
            this.identStep = identStep;
            this.ident = ident;
        }

        public XmlStringBuilder append(final String string) {
            builder.append(string);
            return this;
        }

        public XmlStringBuilder fillSpaces() {
            for (int index = 0; index < ident; index += 1) {
                builder.append(identStep == Step.TABS ? '\t' : ' ');
            }
            return this;
        }

        public XmlStringBuilder incIdent() {
            ident += identStep.getIdent();
            return this;
        }

        public XmlStringBuilder decIdent() {
            ident -= identStep.getIdent();
            return this;
        }

        public XmlStringBuilder newLine() {
            if (identStep != Step.COMPACT) {
                builder.append("\n");
            }
            return this;
        }

        public int getIdent() {
            return ident;
        }

        public Step getIdentStep() {
            return identStep;
        }

        public String toString() {
            return builder.toString() + "\n</root>";
        }
    }

    public static class XmlStringBuilderWithoutRoot extends XmlStringBuilder {
        public XmlStringBuilderWithoutRoot(XmlStringBuilder.Step identStep) {
            super(new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"), identStep, 0);
        }

        public String toString() {
            return builder.toString();
        }
    }

    public static class XmlStringBuilderWithoutHeader extends XmlStringBuilder {
        public XmlStringBuilderWithoutHeader(XmlStringBuilder.Step identStep, int ident) {
            super(new StringBuilder(), identStep, ident);
        }

        public String toString() {
            return builder.toString();
        }
    }

    public static class XmlStringBuilderText extends XmlStringBuilderWithoutHeader {
        public XmlStringBuilderText(XmlStringBuilder.Step identStep, int ident) {
            super(identStep, ident);
        }
    }

    public static class XmlArray {
        public static void writeXml(Collection collection, String name, XmlStringBuilder builder,
            boolean parentTextFound, Set<String> namespaces) {
            if (collection == null) {
                builder.append(NULL);
                return;
            }

            if (name != null) {
                builder.fillSpaces().append("<").append(XmlValue.escapeName(name, namespaces)).append(">").incIdent();
                if (!collection.isEmpty()) {
                    builder.newLine();
                }
            }
            writeXml(collection, builder, name, parentTextFound, namespaces);
            if (name != null) {
                builder.decIdent().newLine().fillSpaces().append("</")
                        .append(XmlValue.escapeName(name, namespaces)).append(">");
            }
        }

        private static void writeXml(Collection collection, XmlStringBuilder builder, String name,
            boolean parentTextFound, Set<String> namespaces) {
            Iterator iter = collection.iterator();
            while (iter.hasNext()) {
                Object value = iter.next();
                if (value == null) {
                    builder.fillSpaces()
                        .append("<" + (name == null ? ELEMENT_TEXT : XmlValue.escapeName(name, namespaces)) + ">"
                        + NULL + "</" + (name == null ? ELEMENT_TEXT : XmlValue.escapeName(name, namespaces)) + ">");
                } else {
                    if (value instanceof Map && ((Map) value).size() == 1
                        && (String.valueOf(((Map.Entry) ((Map) value).entrySet().iterator()
                            .next()).getKey()).startsWith(TEXT)
                        || String.valueOf(((Map.Entry) ((Map) value).entrySet().iterator()
                            .next()).getKey()).startsWith("#comment")
                        || String.valueOf(((Map.Entry) ((Map) value).entrySet().iterator()
                            .next()).getKey()).startsWith("#cdata-section"))) {
                        XmlObject.writeXml((Map) value, null, builder, parentTextFound, namespaces);
                    } else {
                        XmlValue.writeXml(value, name == null ? ELEMENT_TEXT : name, builder, parentTextFound,
                            namespaces);
                    }
                    parentTextFound = false;
                }
                if (iter.hasNext()) {
                    builder.newLine();
                }
            }
        }

        public static void writeXml(byte[] array, XmlStringBuilder builder) {
            if (array == null) {
                builder.fillSpaces().append(NULL_ELEMENT);
            } else if (array.length == 0) {
                builder.fillSpaces().append(EMPTY_ELEMENT);
            } else {
                for (int i = 0; i < array.length; i++) {
                    builder.fillSpaces().append(ELEMENT);
                    builder.append(String.valueOf(array[i]));
                    builder.append(CLOSED_ELEMENT);
                    if (i != array.length - 1) {
                        builder.newLine();
                    }
                }
            }
        }

        public static void writeXml(short[] array, XmlStringBuilder builder) {
            if (array == null) {
                builder.fillSpaces().append(NULL_ELEMENT);
            } else if (array.length == 0) {
                builder.fillSpaces().append(EMPTY_ELEMENT);
            } else {
                for (int i = 0; i < array.length; i++) {
                    builder.fillSpaces().append(ELEMENT);
                    builder.append(String.valueOf(array[i]));
                    builder.append(CLOSED_ELEMENT);
                    if (i != array.length - 1) {
                        builder.newLine();
                    }
                }
            }
        }

        public static void writeXml(int[] array, XmlStringBuilder builder) {
            if (array == null) {
                builder.fillSpaces().append(NULL_ELEMENT);
            } else if (array.length == 0) {
                builder.fillSpaces().append(EMPTY_ELEMENT);
            } else {
                for (int i = 0; i < array.length; i++) {
                    builder.fillSpaces().append(ELEMENT);
                    builder.append(String.valueOf(array[i]));
                    builder.append(CLOSED_ELEMENT);
                    if (i != array.length - 1) {
                        builder.newLine();
                    }
                }
            }
        }

        public static void writeXml(long[] array, XmlStringBuilder builder) {
            if (array == null) {
                builder.fillSpaces().append(NULL_ELEMENT);
            } else if (array.length == 0) {
                builder.fillSpaces().append(EMPTY_ELEMENT);
            } else {
                for (int i = 0; i < array.length; i++) {
                    builder.fillSpaces().append(ELEMENT);
                    builder.append(String.valueOf(array[i]));
                    builder.append(CLOSED_ELEMENT);
                    if (i != array.length - 1) {
                        builder.newLine();
                    }
                }
            }
        }

        public static void writeXml(float[] array, XmlStringBuilder builder) {
            if (array == null) {
                builder.fillSpaces().append(NULL_ELEMENT);
            } else if (array.length == 0) {
                builder.fillSpaces().append(EMPTY_ELEMENT);
            } else {
                for (int i = 0; i < array.length; i++) {
                    builder.fillSpaces().append(ELEMENT);
                    builder.append(String.valueOf(array[i]));
                    builder.append(CLOSED_ELEMENT);
                    if (i != array.length - 1) {
                        builder.newLine();
                    }
                }
            }
        }

        public static void writeXml(double[] array, XmlStringBuilder builder) {
            if (array == null) {
                builder.fillSpaces().append(NULL_ELEMENT);
            } else if (array.length == 0) {
                builder.fillSpaces().append(EMPTY_ELEMENT);
            } else {
                for (int i = 0; i < array.length; i++) {
                    builder.fillSpaces().append(ELEMENT);
                    builder.append(String.valueOf(array[i]));
                    builder.append(CLOSED_ELEMENT);
                    if (i != array.length - 1) {
                        builder.newLine();
                    }
                }
            }
        }

        public static void writeXml(boolean[] array, XmlStringBuilder builder) {
            if (array == null) {
                builder.fillSpaces().append(NULL_ELEMENT);
            } else if (array.length == 0) {
                builder.fillSpaces().append(EMPTY_ELEMENT);
            } else {
                for (int i = 0; i < array.length; i++) {
                    builder.fillSpaces().append(ELEMENT);
                    builder.append(String.valueOf(array[i]));
                    builder.append(CLOSED_ELEMENT);
                    if (i != array.length - 1) {
                        builder.newLine();
                    }
                }
            }
        }

        public static void writeXml(char[] array, XmlStringBuilder builder) {
            if (array == null) {
                builder.fillSpaces().append(NULL_ELEMENT);
            } else if (array.length == 0) {
                builder.fillSpaces().append(EMPTY_ELEMENT);
            } else {
                for (int i = 0; i < array.length; i++) {
                    builder.fillSpaces().append(ELEMENT);
                    builder.append(String.valueOf(array[i]));
                    builder.append(CLOSED_ELEMENT);
                    if (i != array.length - 1) {
                        builder.newLine();
                    }
                }
            }
        }

        public static void writeXml(Object[] array, String name, XmlStringBuilder builder, boolean parentTextFound,
                Set<String> namespaces) {
            if (array == null) {
                builder.fillSpaces().append(NULL_ELEMENT);
            } else if (array.length == 0) {
                builder.fillSpaces().append(EMPTY_ELEMENT);
            } else {
                for (int i = 0; i < array.length; i++) {
                    XmlValue.writeXml(array[i], name == null ? ELEMENT_TEXT : name, builder,
                        parentTextFound, namespaces);
                    if (i != array.length - 1) {
                        builder.newLine();
                    }
                }
            }
        }
    }

    public static class XmlObject {
        @SuppressWarnings("unchecked")
        public static void writeXml(Map map, String name, final XmlStringBuilder builder,
            final boolean parentTextFound, final Set<String> namespaces) {
            if (map == null) {
                XmlValue.writeXml(NULL, name, builder, false, namespaces);
                return;
            }

            final List<XmlStringBuilder> elems = newArrayList();
            final List<String> attrs = newArrayList();
            final XmlStringBuilder.Step identStep = builder.getIdentStep();
            final int ident = builder.getIdent() + (name == null ? 0 : builder.getIdentStep().getIdent());
            boolean textFoundSave = false;
            final List<Map.Entry> entries = newArrayList(map.entrySet());
            for (int index = 0; index < entries.size(); index += 1) {
                final Map.Entry entry = entries.get(index);
                final boolean addNewLine = index < entries.size() - 1
                    && !String.valueOf(entries.get(index + 1).getKey()).startsWith(TEXT);
                if (String.valueOf(entry.getKey()).startsWith("-") && !(entry.getValue() instanceof Map)
                    && !(entry.getValue() instanceof List)) {
                    attrs.add(" " + XmlValue.escapeName(String.valueOf(entry.getKey()).substring(1), namespaces)
                        + "=\"" + escape(String.valueOf(entry.getValue())) + "\"");
                    if (String.valueOf(entry.getKey()).startsWith("-xmlns:")) {
                        namespaces.add(String.valueOf(entry.getKey()).substring(7));
                    }
                } else if (escape(String.valueOf(entry.getKey())).startsWith(TEXT)) {
                    if (elems.isEmpty()) {
                        textFoundSave = true;
                    }
                    addText(entry, elems, identStep, ident);
                } else {
                    processElements(entry, identStep, ident, addNewLine, elems, namespaces);
                }
            }
            if (name != null) {
                if (!parentTextFound) {
                    builder.fillSpaces();
                }
                builder.append("<").append(XmlValue.escapeName(name, namespaces)).append(U.join(attrs, ""))
                        .append(">").incIdent();
                if (!textFoundSave && !elems.isEmpty()) {
                    builder.newLine();
                }
            }
            for (XmlStringBuilder localBuilder1 : elems) {
                builder.append(localBuilder1.toString());
            }
            if (name != null) {
                builder.decIdent();
                if (elems.isEmpty() || !(elems.get(elems.size() - 1) instanceof XmlStringBuilderText)) {
                    builder.newLine().fillSpaces();
                }
                builder.append("</").append(XmlValue.escapeName(name, namespaces)).append(">");
            }
        }

        private static void processElements(final Map.Entry entry, final XmlStringBuilder.Step identStep,
                final int ident, final boolean addNewLine, final List<XmlStringBuilder> elems,
                final Set<String> namespaces) {
            if (escape(String.valueOf(entry.getKey())).startsWith("#comment")) {
                addComment(entry, identStep, ident, addNewLine, elems, "<!--", "-->");
            } else if (escape(String.valueOf(entry.getKey())).startsWith("#cdata-section")) {
                addComment(entry, identStep, ident, addNewLine, elems, "<![CDATA[", "]]>");
            } else if (entry.getValue() instanceof List && !((List) entry.getValue()).isEmpty()) {
                addElements(identStep, ident, entry, namespaces, elems, addNewLine);
            } else {
                addElement(identStep, ident, entry, namespaces, elems, addNewLine);
            }
        }

        private static void addText(final Map.Entry entry, final List<XmlStringBuilder> elems,
                final XmlStringBuilder.Step identStep, final int ident) {
            if (entry.getValue() instanceof List) {
                for (Object value : (List) entry.getValue()) {
                    elems.add(new XmlStringBuilderText(identStep, ident).append(escape(String.valueOf(value))));
                }
            } else {
                elems.add(new XmlStringBuilderText(identStep, ident).append(
                        escape(String.valueOf(entry.getValue()))));
            }
        }

        private static void addElements(final XmlStringBuilder.Step identStep, final int ident, Map.Entry entry,
                Set<String> namespaces, final List<XmlStringBuilder> elems, final boolean addNewLine) {
            boolean parentTextFound = !elems.isEmpty() && elems.get(elems.size() - 1) instanceof XmlStringBuilderText;
            XmlStringBuilder localBuilder = new XmlStringBuilderWithoutHeader(identStep, ident);
            XmlArray.writeXml((List) entry.getValue(), localBuilder,
                    String.valueOf(entry.getKey()), parentTextFound, namespaces);
            if (addNewLine) {
                localBuilder.newLine();
            }
            elems.add(localBuilder);
        }

        private static void addElement(final XmlStringBuilder.Step identStep, final int ident, Map.Entry entry,
                Set<String> namespaces, final List<XmlStringBuilder> elems, final boolean addNewLine) {
            boolean parentTextFound = !elems.isEmpty() && elems.get(elems.size() - 1) instanceof XmlStringBuilderText;
            XmlStringBuilder localBuilder = new XmlStringBuilderWithoutHeader(identStep, ident);
            XmlValue.writeXml(entry.getValue(), String.valueOf(entry.getKey()),
                    localBuilder, parentTextFound, namespaces);
            if (addNewLine) {
                localBuilder.newLine();
            }
            elems.add(localBuilder);
        }

        private static void addComment(Map.Entry entry, XmlStringBuilder.Step identStep, int ident,
                boolean addNewLine, List<XmlStringBuilder> elems, String openElement, String closeElement) {
            if (entry.getValue() instanceof List) {
                for (Iterator iterator = ((List) entry.getValue()).iterator(); iterator.hasNext(); ) {
                    addCommentValue(identStep, ident, String.valueOf(iterator.next()),
                            iterator.hasNext() || addNewLine, elems, openElement, closeElement);
                }
            } else {
                addCommentValue(identStep, ident, String.valueOf(entry.getValue()), addNewLine, elems,
                        openElement, closeElement);
            }
        }

        private static void addCommentValue(XmlStringBuilder.Step identStep, int ident, String value,
                boolean addNewLine, List<XmlStringBuilder> elems, String openElement, String closeElement) {
            boolean parentTextFound = !elems.isEmpty() && elems.get(elems.size() - 1) instanceof XmlStringBuilderText;
            XmlStringBuilder localBuilder = new XmlStringBuilderWithoutHeader(identStep, ident);
            if (!parentTextFound) {
                localBuilder.fillSpaces();
            }
            localBuilder.append(openElement).append(value).append(closeElement);
            if (addNewLine) {
                localBuilder.newLine();
            }
            elems.add(localBuilder);
        }
    }

    public static class XmlValue {
        public static void writeXml(Object value, String name, XmlStringBuilder builder, boolean parentTextFound,
            Set<String> namespaces) {
            if (value instanceof Map) {
                XmlObject.writeXml((Map) value,  name, builder, parentTextFound, namespaces);
                return;
            }
            if (value instanceof Collection) {
                XmlArray.writeXml((Collection) value, name, builder, parentTextFound, namespaces);
                return;
            }
            if (!parentTextFound) {
                builder.fillSpaces();
            }
            builder.append("<" + XmlValue.escapeName(name, namespaces) + ">");
            if (value == null) {
                builder.append(NULL);
            } else if (value instanceof String) {
                builder.append(escape((String) value));
            } else {
                processArrays(value, builder, name, parentTextFound, namespaces);
            }
            builder.append("</" + XmlValue.escapeName(name, namespaces) + ">");
        }

        private static void processArrays(Object value, XmlStringBuilder builder, String name,
                boolean parentTextFound, Set<String> namespaces) {
            if (value instanceof Double) {
                if (((Double) value).isInfinite() || ((Double) value).isNaN()) {
                    builder.append(NULL);
                } else {
                    builder.append(value.toString());
                }
            } else if (value instanceof Float) {
                if (((Float) value).isInfinite() || ((Float) value).isNaN()) {
                    builder.append(NULL);
                } else {
                    builder.append(value.toString());
                }
            } else if (value instanceof Number) {
                builder.append(value.toString());
            } else if (value instanceof Boolean) {
                builder.append(value.toString());
            } else if (value instanceof byte[]) {
                builder.newLine().incIdent();
                XmlArray.writeXml((byte[]) value, builder);
                builder.decIdent().newLine().fillSpaces();
            } else if (value instanceof short[]) {
                builder.newLine().incIdent();
                XmlArray.writeXml((short[]) value, builder);
                builder.decIdent().newLine().fillSpaces();
            } else {
                processArrays2(value, builder, name, parentTextFound, namespaces);
            }
        }

        private static void processArrays2(Object value, XmlStringBuilder builder, String name,
                boolean parentTextFound, Set<String> namespaces) {
            if (value instanceof int[]) {
                builder.newLine().incIdent();
                XmlArray.writeXml((int[]) value, builder);
                builder.decIdent().newLine().fillSpaces();
            } else if (value instanceof long[]) {
                builder.newLine().incIdent();
                XmlArray.writeXml((long[]) value, builder);
                builder.decIdent().newLine().fillSpaces();
            } else if (value instanceof float[]) {
                builder.newLine().incIdent();
                XmlArray.writeXml((float[]) value, builder);
                builder.decIdent().newLine().fillSpaces();
            } else if (value instanceof double[]) {
                builder.newLine().incIdent();
                XmlArray.writeXml((double[]) value, builder);
                builder.decIdent().newLine().fillSpaces();
            } else if (value instanceof boolean[]) {
                builder.newLine().incIdent();
                XmlArray.writeXml((boolean[]) value, builder);
                builder.decIdent().newLine().fillSpaces();
            } else if (value instanceof char[]) {
                builder.newLine().incIdent();
                XmlArray.writeXml((char[]) value, builder);
                builder.decIdent().newLine().fillSpaces();
            } else if (value instanceof Object[]) {
                builder.newLine().incIdent();
                XmlArray.writeXml((Object[]) value, name, builder, parentTextFound, namespaces);
                builder.decIdent().newLine().fillSpaces();
            } else {
                builder.append(value.toString());
            }
        }

        public static String escapeName(String name, Set<String> namespaces) {
            final int length = name.length();
            if (length == 0) {
                return "__EE__EMPTY__EE__";
            }
            final StringBuilder result = new StringBuilder();
            char ch = name.charAt(0);
            if (com.sun.org.apache.xerces.internal.util.XMLChar.isNameStart(ch) && ch != ':') {
                result.append(ch);
            } else {
                result.append("__").append(Base32.encode(Character.toString(ch))).append("__");
            }
            for (int i = 1; i < length; ++i) {
                ch = name.charAt(i);
                if (ch == ':' && ("xmlns".equals(name.substring(0, i))
                        || namespaces.contains(name.substring(0, i)))) {
                    result.append(ch);
                } else if (com.sun.org.apache.xerces.internal.util.XMLChar.isName(ch) && ch != ':') {
                    result.append(ch);
                } else {
                    result.append("__").append(Base32.encode(Character.toString(ch))).append("__");
                }
            }
            return result.toString();
        }

        public static String escape(String s) {
            if (s == null) {
                return null;
            }
            StringBuilder sb = new StringBuilder();
            escape(s, sb);
            return sb.toString();
        }

        private static void escape(String s, StringBuilder sb) {
            final int len = s.length();
            for (int i = 0; i < len; i++) {
                char ch = s.charAt(i);
                switch (ch) {
                case '"':
                    sb.append("&quot;");
                    break;
                case '\'':
                    sb.append("'");
                    break;
                case '&':
                    sb.append("&amp;");
                    break;
                case '<':
                    sb.append("&lt;");
                    break;
                case '>':
                    sb.append("&gt;");
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\n':
                    sb.append("\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\t");
                    break;
                default:
                    if (ch <= '\u001F' || ch >= '\u007F' && ch <= '\u009F'
                        || ch >= '\u2000' && ch <= '\u20FF') {
                        String ss = Integer.toHexString(ch);
                        sb.append("&#x");
                        for (int k = 0; k < 4 - ss.length(); k++) {
                            sb.append('0');
                        }
                        sb.append(ss.toUpperCase()).append(";");
                    } else {
                        sb.append(ch);
                    }
                    break;
                }
            }
        }
    }

    public static String toXml(Collection collection, XmlStringBuilder.Step identStep) {
        final XmlStringBuilder builder = new XmlStringBuilderWithoutRoot(identStep);
        builder.append("<root>").incIdent();
        if (collection == null || !collection.isEmpty()) {
            builder.newLine();
        }
        XmlArray.writeXml(collection, null, builder, false, U.<String>newLinkedHashSet());
        return builder.newLine().append("</root>").toString();
    }

    public static String toXml(Collection collection) {
        return toXml(collection, XmlStringBuilder.Step.TWO_SPACES);
    }

    public String toXml() {
        return toXml((Collection) getIterable());
    }

    public static String toXml(Map map, XmlStringBuilder.Step identStep) {
        final XmlStringBuilder builder = new XmlStringBuilderWithoutRoot(identStep);
        if (map == null || map.size() != 1
            || ((String) ((Map.Entry) map.entrySet().iterator().next()).getKey()).startsWith("-")
            || ((Map.Entry) map.entrySet().iterator().next()).getValue() instanceof List) {
            XmlObject.writeXml(map, "root", builder, false, U.<String>newLinkedHashSet());
        } else {
            XmlObject.writeXml(map, null, builder, false, U.<String>newLinkedHashSet());
        }
        return builder.toString();
    }

    public static String toXml(Map map) {
        return toXml(map, XmlStringBuilder.Step.TWO_SPACES);
    }

    public static class ParseException extends RuntimeException {
        private final int offset;
        private final int line;
        private final int column;

        public ParseException(String message, int offset, int line, int column) {
            super(message + " at " + line + ":" + column);
            this.offset = offset;
            this.line = line;
            this.column = column;
        }

        public int getOffset() {
            return offset;
        }

        public int getLine() {
            return line;
        }

        public int getColumn() {
            return column;
        }
    }

    public static class JsonParser {
        private final String json;
        private int index;
        private int line;
        private int lineOffset;
        private int current;
        private StringBuilder captureBuffer;
        private int captureStart;

        public JsonParser(String string) {
            this.json = string;
            line = 1;
            captureStart = -1;
        }

        public Object parse() {
            read();
            skipWhiteSpace();
            final Object result = readValue();
            skipWhiteSpace();
            if (!isEndOfText()) {
                throw error("Unexpected character");
            }
            return result;
        }

        private Object readValue() {
            switch (current) {
            case 'n':
                return readNull();
            case 't':
                return readTrue();
            case 'f':
                return readFalse();
            case '"':
                return readString();
            case '[':
                return readArray();
            case '{':
                return readObject();
            case '-':
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                return readNumber();
            default:
                throw expected("value");
            }
        }

        private List<Object> readArray() {
            read();
            List<Object> array = newArrayList();
            skipWhiteSpace();
            if (readChar(']')) {
                return array;
            }
            do {
                skipWhiteSpace();
                array.add(readValue());
                skipWhiteSpace();
            } while (readChar(','));
            if (!readChar(']')) {
                throw expected("',' or ']'");
            }
            return array;
        }

        private Map<String, Object> readObject() {
            read();
            Map<String, Object> object = newLinkedHashMap();
            skipWhiteSpace();
            if (readChar('}')) {
                return object;
            }
            do {
                skipWhiteSpace();
                String name = readName();
                skipWhiteSpace();
                if (!readChar(':')) {
                    throw expected("':'");
                }
                skipWhiteSpace();
                object.put(name, readValue());
                skipWhiteSpace();
            } while (readChar(','));
            if (!readChar('}')) {
                throw expected("',' or '}'");
            }
            return object;
        }

        private String readName() {
            if (current != '"') {
                throw expected("name");
            }
            return readString();
        }

        private String readNull() {
            read();
            readRequiredChar('u');
            readRequiredChar('l');
            readRequiredChar('l');
            return null;
        }

        private Boolean readTrue() {
            read();
            readRequiredChar('r');
            readRequiredChar('u');
            readRequiredChar('e');
            return Boolean.TRUE;
        }

        private Boolean readFalse() {
            read();
            readRequiredChar('a');
            readRequiredChar('l');
            readRequiredChar('s');
            readRequiredChar('e');
            return Boolean.FALSE;
        }

        private void readRequiredChar(char ch) {
            if (!readChar(ch)) {
                throw expected("'" + ch + "'");
            }
        }

        private String readString() {
            read();
            startCapture();
            while (current != '"') {
                if (current == '\\') {
                    pauseCapture();
                    readEscape();
                    startCapture();
                } else if (current < 0x20) {
                    throw expected("valid string character");
                } else {
                    read();
                }
            }
            String string = endCapture();
            read();
            return string;
        }

        private void readEscape() {
            read();
            switch (current) {
            case '"':
            case '/':
            case '\\':
                captureBuffer.append((char) current);
                break;
            case 'b':
                captureBuffer.append('\b');
                break;
            case 'f':
                captureBuffer.append('\f');
                break;
            case 'n':
                captureBuffer.append('\n');
                break;
            case 'r':
                captureBuffer.append('\r');
                break;
            case 't':
                captureBuffer.append('\t');
                break;
            case 'u':
                char[] hexChars = new char[4];
                boolean isHexCharsDigits = true;
                for (int i = 0; i < 4; i++) {
                    read();
                    if (!isHexDigit()) {
                        isHexCharsDigits = false;
                    }
                    hexChars[i] = (char) current;
                }
                if (isHexCharsDigits) {
                    captureBuffer.append((char) Integer.parseInt(new String(hexChars), 16));
                } else {
                    captureBuffer.append("\\u").append(hexChars[0]).append(hexChars[1]).append(hexChars[2])
                        .append(hexChars[3]);
                }
                break;
            default:
                throw expected("valid escape sequence");
            }
            read();
        }

        private Number readNumber() {
            startCapture();
            readChar('-');
            int firstDigit = current;
            if (!readDigit()) {
                throw expected("digit");
            }
            if (firstDigit != '0') {
                while (readDigit()) {
                }
            }
            readFraction();
            readExponent();
            final String number = endCapture();
            if (number.contains(".") || number.contains("e") || number.contains("E")) {
                return Double.valueOf(number);
            } else {
                return Long.valueOf(number);
            }
        }

        private boolean readFraction() {
            if (!readChar('.')) {
                return false;
            }
            if (!readDigit()) {
                throw expected("digit");
            }
            while (readDigit()) {
            }
            return true;
        }

        private boolean readExponent() {
            if (!readChar('e') && !readChar('E')) {
                return false;
            }
            if (!readChar('+')) {
                readChar('-');
            }
            if (!readDigit()) {
                throw expected("digit");
            }
            while (readDigit()) {
            }
            return true;
        }

        private boolean readChar(char ch) {
            if (current != ch) {
                return false;
            }
            read();
            return true;
        }

        private boolean readDigit() {
            if (!isDigit()) {
                return false;
            }
            read();
            return true;
        }

        private void skipWhiteSpace() {
            while (isWhiteSpace()) {
                read();
            }
        }

        private void read() {
            if (index == json.length()) {
                current = -1;
                return;
            }
            if (current == '\n') {
                line++;
                lineOffset = index;
            }
            current = json.charAt(index++);
        }

        private void startCapture() {
            if (captureBuffer == null) {
                captureBuffer = new StringBuilder();
            }
            captureStart = index - 1;
        }

        private void pauseCapture() {
            captureBuffer.append(json.substring(captureStart, index - 1));
            captureStart = -1;
        }

        private String endCapture() {
            int end = current == -1 ? index : index - 1;
            String captured;
            if (captureBuffer.length() > 0) {
                captureBuffer.append(json.substring(captureStart, end));
                captured = captureBuffer.toString();
                captureBuffer.setLength(0);
            } else {
                captured = json.substring(captureStart, end);
            }
            captureStart = -1;
            return captured;
        }

        private ParseException expected(String expected) {
            if (isEndOfText()) {
                return error("Unexpected end of input");
            }
            return error("Expected " + expected);
        }

        private ParseException error(String message) {
            int absIndex = index;
            int column = absIndex - lineOffset;
            int offset = isEndOfText() ? absIndex : absIndex - 1;
            return new ParseException(message, offset, line, column - 1);
        }

        private boolean isWhiteSpace() {
            return current == ' ' || current == '\t' || current == '\n' || current == '\r';
        }

        private boolean isDigit() {
            return current >= '0' && current <= '9';
        }

        private boolean isHexDigit() {
            return isDigit() || current >= 'a' && current <= 'f' || current >= 'A'
                    && current <= 'F';
        }

        private boolean isEndOfText() {
            return current == -1;
        }

    }

    public static Object fromJson(String string) {
        return new JsonParser(string).parse();
    }

    public Object fromJson() {
        return fromJson(getString().get());
    }

    @SuppressWarnings("unchecked")
    private static Object getValue(final Object value) {
        if (value instanceof Map && ((Map<String, Object>) value).entrySet().size() == 1) {
            final Map.Entry<String, Object> entry = ((Map<String, Object>) value).entrySet().iterator().next();
            if (TEXT.equals(entry.getKey()) || entry.getKey().equals(ELEMENT_TEXT)) {
                return entry.getValue();
            }
        }
        return value;
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> createMap(final org.w3c.dom.Node node,
        final Function<Object, Object> nodeMapper, Map<String, Object> attrMap, int[] uniqueIds) {
        final Map<String, Object> map = newLinkedHashMap();
        map.putAll(attrMap);
        final org.w3c.dom.NodeList nodeList = node.getChildNodes();
        for (int index = 0; index < nodeList.getLength(); index++) {
            final org.w3c.dom.Node currentNode = nodeList.item(index);
            final String name = currentNode.getNodeName();
            final Object value;
            final int attributesLength = currentNode.getAttributes() == null
                    ? 0 : currentNode.getAttributes().getLength();
            if (currentNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                final Map<String, Object> attrMapLocal = newLinkedHashMap();
                for (int indexAttr = 0; indexAttr < attributesLength; indexAttr += 1) {
                    final org.w3c.dom.Node currentNodeAttr = currentNode.getAttributes().item(indexAttr);
                    addNodeValue(attrMapLocal, '-' + currentNodeAttr.getNodeName(),
                            currentNodeAttr.getTextContent(), nodeMapper, uniqueIds);
                }
                value = createMap(currentNode, nodeMapper, attrMapLocal, uniqueIds);
            } else {
                value = currentNode.getTextContent();
            }
            if (TEXT.equals(name) && value.toString().trim().isEmpty()) {
                continue;
            }
            addNodeValue(map, name, value, nodeMapper, uniqueIds);
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    private static void addNodeValue(final Map<String, Object> map, final String name, final Object value,
            final Function<Object, Object> nodeMapper, int[] uniqueIds) {
        if (map.containsKey(name)) {
            if (TEXT.equals(name)) {
                addText(map, name + uniqueIds[0], value, nodeMapper);
                uniqueIds[0] += 1;
            } else if ("#comment".equals(name)) {
                addText(map, name + uniqueIds[1], value, nodeMapper);
                uniqueIds[1] += 1;
            } else if ("#cdata-section".equals(name)) {
                addText(map, name + uniqueIds[2], value, nodeMapper);
                uniqueIds[2] += 1;
            } else {
                final Object object = map.get(name);
                if (object instanceof List) {
                    ((List<Object>) object).add(getValue(value));
                } else {
                    addNodeList(map, name, object, value);
                }
            }
        } else {
            if (TEXT.equals(name) || "#comment".equals(name) || "#cdata-section".equals(name)) {
                addText(map, name, value, nodeMapper);
            } else {
                map.put(name, nodeMapper.apply(getValue(value)));
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static void addNodeList(final Map<String, Object> map, final String name, final Object oldValue,
        final Object value) {
        final List<Object> objects = newArrayList();
        objects.add(oldValue);
        int lastIndex = map.size() - 1;
        while (true) {
            final Map.Entry lastElement = (Map.Entry) map.entrySet().toArray()[lastIndex];
            if (String.valueOf(lastElement.getKey()).startsWith(TEXT)
                || String.valueOf(lastElement.getKey()).startsWith("#comment")
                || String.valueOf(lastElement.getKey()).startsWith("#cdata-section")) {
                final Map<String, Object> text = newLinkedHashMap();
                text.put(String.valueOf(lastElement.getKey()), map.remove(lastElement.getKey()));
                objects.add(1, text);
            } else {
                if (name.equals(String.valueOf(lastElement.getKey()))) {
                    break;
                }
            }
            lastIndex -= 1;
        }
        objects.add(getValue(value));
        map.put(name, objects);
    }

    @SuppressWarnings("unchecked")
    private static void addText(final Map<String, Object> map, final String name, final Object value,
            final Function<Object, Object> nodeMapper) {
        if (!map.isEmpty() && ((Map.Entry) map.entrySet().toArray()[map.size() - 1]).getValue() instanceof List) {
            final Object lastElement = ((Map.Entry) map.entrySet().toArray()[map.size() - 1]).getValue();
            final Map<String, Object> text = newLinkedHashMap();
            text.put(name, nodeMapper.apply(getValue(value)));
            ((List) lastElement).add(text);
        } else {
            map.put(name, nodeMapper.apply(getValue(value)));
        }
    }

    @SuppressWarnings("unchecked")
    public static Object fromXml(final String xml) {
        if (xml == null) {
            return null;
        }
        try {
            org.w3c.dom.Document document = createDocument(xml);
            final Object result = createMap(document, new Function<Object, Object>() {
                public Object apply(Object object) {
                    return object;
                }
            }, Collections.<String, Object>emptyMap(), new int[] {1, 1, 1});
            if (((Map.Entry) ((Map) result).entrySet().iterator().next()).getKey().equals("root")
                && (((Map.Entry) ((Map) result).entrySet().iterator().next()).getValue() instanceof List
                || ((Map.Entry) ((Map) result).entrySet().iterator().next()).getValue() instanceof Map)) {
                return ((Map.Entry) ((Map) result).entrySet().iterator().next()).getValue();
            }
            return result;
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    private static org.w3c.dom.Document createDocument(final String xml) throws java.io.IOException,
            javax.xml.parsers.ParserConfigurationException, org.xml.sax.SAXException {
        final java.io.InputStream stream = new java.io.ByteArrayInputStream(xml.getBytes(UTF_8));
        final javax.xml.parsers.DocumentBuilderFactory factory =
                javax.xml.parsers.DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setFeature(javax.xml.XMLConstants.FEATURE_SECURE_PROCESSING, true);
        final javax.xml.parsers.DocumentBuilder builder = factory.newDocumentBuilder();
        builder.setErrorHandler(new org.xml.sax.helpers.DefaultHandler());
        return builder.parse(stream);
    }

    public static Object fromXmlMakeArrays(final String xml) {
        try {
            org.w3c.dom.Document document = createDocument(xml);
            return createMap(document, new Function<Object, Object>() {
                public Object apply(Object object) {
                    return object instanceof List ? object : newArrayList(Arrays.asList(object));
                }
            }, Collections.<String, Object>emptyMap(), new int[] {1, 1, 1});
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    public Object fromXml() {
        return fromXml(getString().get());
    }

    public static class JsonJavaStringBuilder {
        private final StringBuilder builder;
        private int ident;

        public JsonJavaStringBuilder() {
            builder = new StringBuilder("\"");
        }

        public JsonJavaStringBuilder append(final char character) {
            builder.append(character);
            return this;
        }

        public JsonJavaStringBuilder append(final String string) {
            builder.append(string);
            return this;
        }

        public JsonJavaStringBuilder fillSpaces() {
            for (int index = 0; index < ident; index += 1) {
                builder.append(' ');
            }
            return this;
        }

        public JsonJavaStringBuilder incIdent() {
            ident += 2;
            return this;
        }

        public JsonJavaStringBuilder decIdent() {
            ident -= 2;
            return this;
        }

        public JsonJavaStringBuilder newLine() {
            builder.append("\\n\"\n + \"");
            return this;
        }

        public String toString() {
            return builder.toString() + "\";";
        }
    }

    public static class JsonJavaArray {
        public static void writeJson(Collection collection, JsonJavaStringBuilder builder) {
            if (collection == null) {
                builder.append(NULL);
                return;
            }

            Iterator iter = collection.iterator();

            builder.append('[').incIdent().newLine();
            while (iter.hasNext()) {
                Object value = iter.next();
                if (value == null) {
                    builder.fillSpaces().append(NULL);
                    continue;
                }

                builder.fillSpaces();
                JsonJavaValue.writeJson(value, builder);
                if (iter.hasNext()) {
                    builder.append(',').newLine();
                }
            }
            builder.newLine().decIdent().fillSpaces().append(']');
        }

        public static void writeJson(byte[] array, JsonJavaStringBuilder builder) {
            if (array == null) {
                builder.append(NULL);
            } else if (array.length == 0) {
                builder.append("[]");
            } else {
                builder.append('[').incIdent().newLine();
                builder.fillSpaces().append(String.valueOf(array[0]));

                for (int i = 1; i < array.length; i++) {
                    builder.append(',').newLine().fillSpaces();
                    builder.append(String.valueOf(array[i]));
                }

                builder.newLine().decIdent().fillSpaces().append(']');
            }
        }

        public static void writeJson(short[] array, JsonJavaStringBuilder builder) {
            if (array == null) {
                builder.append(NULL);
            } else if (array.length == 0) {
                builder.append("[]");
            } else {
                builder.append('[').incIdent().newLine();
                builder.fillSpaces().append(String.valueOf(array[0]));

                for (int i = 1; i < array.length; i++) {
                    builder.append(',').newLine().fillSpaces();
                    builder.append(String.valueOf(array[i]));
                }

                builder.newLine().decIdent().fillSpaces().append(']');
            }
        }

        public static void writeJson(int[] array, JsonJavaStringBuilder builder) {
            if (array == null) {
                builder.append(NULL);
            } else if (array.length == 0) {
                builder.append("[]");
            } else {
                builder.append('[').incIdent().newLine();
                builder.fillSpaces().append(String.valueOf(array[0]));

                for (int i = 1; i < array.length; i++) {
                    builder.append(',').newLine().fillSpaces();
                    builder.append(String.valueOf(array[i]));
                }

                builder.newLine().decIdent().fillSpaces().append(']');
            }
        }

        public static void writeJson(long[] array, JsonJavaStringBuilder builder) {
            if (array == null) {
                builder.append(NULL);
            } else if (array.length == 0) {
                builder.append("[]");
            } else {
                builder.append('[').incIdent().newLine();
                builder.fillSpaces().append(String.valueOf(array[0]));

                for (int i = 1; i < array.length; i++) {
                    builder.append(',').newLine().fillSpaces();
                    builder.append(String.valueOf(array[i]));
                }

                builder.newLine().decIdent().fillSpaces().append(']');
            }
        }

        public static void writeJson(float[] array, JsonJavaStringBuilder builder) {
            if (array == null) {
                builder.append(NULL);
            } else if (array.length == 0) {
                builder.append("[]");
            } else {
                builder.append('[').incIdent().newLine();
                builder.fillSpaces().append(String.valueOf(array[0]));

                for (int i = 1; i < array.length; i++) {
                    builder.append(',').newLine().fillSpaces();
                    builder.append(String.valueOf(array[i]));
                }

                builder.newLine().decIdent().fillSpaces().append(']');
            }
        }

        public static void writeJson(double[] array, JsonJavaStringBuilder builder) {
            if (array == null) {
                builder.append(NULL);
            } else if (array.length == 0) {
                builder.append("[]");
            } else {
                builder.append('[').incIdent().newLine();
                builder.fillSpaces().append(String.valueOf(array[0]));

                for (int i = 1; i < array.length; i++) {
                    builder.append(',').newLine().fillSpaces();
                    builder.append(String.valueOf(array[i]));
                }

                builder.newLine().decIdent().fillSpaces().append(']');
            }
        }

        public static void writeJson(boolean[] array, JsonJavaStringBuilder builder) {
            if (array == null) {
                builder.append(NULL);
            } else if (array.length == 0) {
                builder.append("[]");
            } else {
                builder.append('[').incIdent().newLine();
                builder.fillSpaces().append(String.valueOf(array[0]));

                for (int i = 1; i < array.length; i++) {
                    builder.append(',').newLine().fillSpaces();
                    builder.append(String.valueOf(array[i]));
                }

                builder.newLine().decIdent().fillSpaces().append(']');
            }
        }

        public static void writeJson(char[] array, JsonJavaStringBuilder builder) {
            if (array == null) {
                builder.append(NULL);
            } else if (array.length == 0) {
                builder.append("[]");
            } else {
                builder.append('[').incIdent().newLine();
                builder.fillSpaces().append('\"').append(String.valueOf(array[0])).append('\"');

                for (int i = 1; i < array.length; i++) {
                    builder.append(',').newLine().fillSpaces();
                    builder.append('\"').append(String.valueOf(array[i])).append('\"');
                }

                builder.newLine().decIdent().fillSpaces().append(']');
            }
        }

        public static void writeJson(Object[] array, JsonJavaStringBuilder builder) {
            if (array == null) {
                builder.append(NULL);
            } else if (array.length == 0) {
                builder.append("[]");
            } else {
                builder.append('[').newLine().incIdent().fillSpaces();
                JsonJavaValue.writeJson(array[0], builder);

                for (int i = 1; i < array.length; i++) {
                    builder.append(',').newLine().fillSpaces();
                    JsonJavaValue.writeJson(array[i], builder);
                }

                builder.newLine().decIdent().fillSpaces().append(']');
            }
        }
    }

    public static class JsonJavaObject {
        public static void writeJson(Map map, JsonJavaStringBuilder builder) {
            if (map == null) {
                builder.append(NULL);
                return;
            }

            Iterator iter = map.entrySet().iterator();

            builder.append('{').newLine().incIdent();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                builder.fillSpaces().append("\\\"");
                builder.append(escape(String.valueOf(entry.getKey())));
                builder.append("\\\"");
                builder.append(':').append(' ');
                JsonJavaValue.writeJson(entry.getValue(), builder);
                if (iter.hasNext()) {
                    builder.append(',').newLine();
                }
            }
            builder.newLine().decIdent().fillSpaces().append('}');
        }
    }

    public static class JsonJavaValue {
        public static void writeJson(Object value, JsonJavaStringBuilder builder) {
            if (value == null) {
                builder.append(NULL);
            } else if (value instanceof String) {
                builder.append("\\\"").append(escape((String) value)).append("\\\"");
            } else if (value instanceof Double) {
                if (((Double) value).isInfinite() || ((Double) value).isNaN()) {
                    builder.append(NULL);
                } else {
                    builder.append(value.toString());
                }
            } else if (value instanceof Float) {
                if (((Float) value).isInfinite() || ((Float) value).isNaN()) {
                    builder.append(NULL);
                } else {
                    builder.append(value.toString());
                }
            } else if (value instanceof Number) {
                builder.append(value.toString());
            } else if (value instanceof Boolean) {
                builder.append(value.toString());
            } else if (value instanceof Map) {
                JsonJavaObject.writeJson((Map) value, builder);
            } else if (value instanceof Collection) {
                JsonJavaArray.writeJson((Collection) value, builder);
            } else if (value instanceof byte[]) {
                JsonJavaArray.writeJson((byte[]) value, builder);
            } else if (value instanceof short[]) {
                JsonJavaArray.writeJson((short[]) value, builder);
            } else if (value instanceof int[]) {
                JsonJavaArray.writeJson((int[]) value, builder);
            } else if (value instanceof long[]) {
                JsonJavaArray.writeJson((long[]) value, builder);
            } else if (value instanceof float[]) {
                JsonJavaArray.writeJson((float[]) value, builder);
            } else if (value instanceof double[]) {
                JsonJavaArray.writeJson((double[]) value, builder);
            } else if (value instanceof boolean[]) {
                JsonJavaArray.writeJson((boolean[]) value, builder);
            } else if (value instanceof char[]) {
                JsonJavaArray.writeJson((char[]) value, builder);
            } else if (value instanceof Object[]) {
                JsonJavaArray.writeJson((Object[]) value, builder);
            } else {
                builder.append(value.toString());
            }
        }

        public static String escape(String s) {
            if (s == null) {
                return null;
            }
            StringBuilder sb = new StringBuilder();
            escape(s, sb);
            return sb.toString();
        }

        private static void escape(String s, StringBuilder sb) {
            final int len = s.length();
            for (int i = 0; i < len; i++) {
                char ch = s.charAt(i);
                switch (ch) {
                case '"':
                    sb.append("\\\"");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                case '/':
                    sb.append("\\/");
                    break;
                default:
                    if (ch <= '\u001F' || ch >= '\u007F' && ch <= '\u009F'
                        || ch >= '\u2000' && ch <= '\u20FF') {
                        String ss = Integer.toHexString(ch);
                        sb.append("\\u");
                        for (int k = 0; k < 4 - ss.length(); k++) {
                            sb.append('0');
                        }
                        sb.append(ss.toUpperCase());
                    } else {
                        sb.append(ch);
                    }
                    break;
                }
            }
        }
    }

    public static String toJsonJavaString(Collection collection) {
        final JsonJavaStringBuilder builder = new JsonJavaStringBuilder();

        JsonJavaArray.writeJson(collection, builder);
        return builder.toString();
    }

    public String toJsonJavaString() {
        return toJsonJavaString((Collection) getIterable());
    }


    public static String toJsonJavaString(Map map) {
        final JsonJavaStringBuilder builder = new JsonJavaStringBuilder();

        JsonJavaObject.writeJson(map, builder);
        return builder.toString();
    }

    public static class FetchResponse {
        private final boolean ok;
        private final int status;
        private final Map<String, List<String>> headerFields;
        private final java.io.ByteArrayOutputStream stream;

        public FetchResponse(final boolean ok, final int status, final Map<String, List<String>> headerFields,
            final java.io.ByteArrayOutputStream stream) {
            this.ok = ok;
            this.status = status;
            this.stream = stream;
            this.headerFields = headerFields;
        }

        public boolean isOk() {
            return ok;
        }

        public int getStatus() {
            return status;
        }

        public Map<String, List<String>> getHeaderFields() {
            return headerFields;
        }

        public byte[] blob() {
            return stream.toByteArray();
        }

        public String text() {
            try {
                return stream.toString("UTF-8");
            } catch (java.io.UnsupportedEncodingException ex) {
                throw new UnsupportedOperationException(ex);
            }
        }

        public Object json() {
            return U.fromJson(text());
        }

        public Object xml() {
            return U.fromXml(text());
        }
    }

    public static FetchResponse fetch(final String url) {
        return fetch(url, null, null, DEFAULT_HEADER_FIELDS, null, null);
    }

    public static FetchResponse fetch(final String url, final Integer connectTimeout, final Integer readTimeout) {
        return fetch(url, null, null, DEFAULT_HEADER_FIELDS, connectTimeout, readTimeout);
    }

    public static FetchResponse fetch(final String url, final String method, final String body) {
        return fetch(url, method, body, DEFAULT_HEADER_FIELDS, null, null);
    }

    private static class BaseHttpSSLSocketFactory extends javax.net.ssl.SSLSocketFactory {
        private javax.net.ssl.SSLContext getSSLContext() {
            return createEasySSLContext();
        }

        @Override
        public java.net.Socket createSocket(java.net.InetAddress arg0, int arg1, java.net.InetAddress arg2,
                        int arg3) throws java.io.IOException {
                return getSSLContext().getSocketFactory().createSocket(arg0, arg1,
                                arg2, arg3);
        }

        @Override
        public java.net.Socket createSocket(String arg0, int arg1, java.net.InetAddress arg2, int arg3)
                throws java.io.IOException {
            return getSSLContext().getSocketFactory().createSocket(arg0, arg1,
                    arg2, arg3);
        }

        @Override
        public java.net.Socket createSocket(java.net.InetAddress arg0, int arg1) throws java.io.IOException {
            return getSSLContext().getSocketFactory().createSocket(arg0, arg1);
        }

        @Override
        public java.net.Socket createSocket(String arg0, int arg1) throws java.io.IOException {
            return getSSLContext().getSocketFactory().createSocket(arg0, arg1);
        }

        @Override
        public String[] getSupportedCipherSuites() {
            return new String[]{};
        }

        @Override
        public String[] getDefaultCipherSuites() {
            return new String[]{};
        }

        @Override
        public java.net.Socket createSocket(java.net.Socket arg0, String arg1, int arg2, boolean arg3)
                throws java.io.IOException {
            return getSSLContext().getSocketFactory().createSocket(arg0, arg1,
                    arg2, arg3);
        }

        private javax.net.ssl.SSLContext createEasySSLContext() {
            try {
                javax.net.ssl.SSLContext context = javax.net.ssl.SSLContext.getInstance("SSL");
                context.init(null, new javax.net.ssl.TrustManager[] { MyX509TrustManager.manger }, null);
                return context;
            } catch (Exception ex) {
                throw new UnsupportedOperationException(ex);
            }
        }

        public static class MyX509TrustManager implements javax.net.ssl.X509TrustManager {

            static MyX509TrustManager manger = new MyX509TrustManager();

            public MyX509TrustManager() {
            }

            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{};
            }

            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
            }

            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
            }
        }
    }

    static class NoHostnameVerifier implements javax.net.ssl.HostnameVerifier {
        public boolean verify(String hostname, javax.net.ssl.SSLSession session) {
              return true;
        }
    }

    private static void setupConnection(final java.net.HttpURLConnection connection, final String method,
        final Map<String, List<String>> headerFields, final Integer connectTimeout, final Integer readTimeout)
        throws java.io.IOException {
        final String localMethod;
        if (SUPPORTED_HTTP_METHODS.contains(method)) {
            localMethod = method;
        } else {
            localMethod = "GET";
        }
        connection.setRequestMethod(localMethod);
        if (connectTimeout != null) {
            connection.setConnectTimeout(connectTimeout);
        }
        if (readTimeout != null) {
            connection.setReadTimeout(readTimeout);
        }
        if (connection instanceof javax.net.ssl.HttpsURLConnection) {
            ((javax.net.ssl.HttpsURLConnection) connection).setSSLSocketFactory(new BaseHttpSSLSocketFactory());
            ((javax.net.ssl.HttpsURLConnection) connection).setHostnameVerifier(new NoHostnameVerifier());
        }
        if (headerFields != null) {
            for (final Map.Entry<String, List<String>> header : headerFields.entrySet()) {
                connection.setRequestProperty(header.getKey(), join(header.getValue(), ";"));
            }
        }
    }

    public static FetchResponse fetch(final String url, final String method, final String body,
        final Map<String, List<String>> headerFields, final Integer connectTimeout, final Integer readTimeout) {
        try {
            final java.net.URL localUrl = new java.net.URL(url);
            final java.net.HttpURLConnection connection = (java.net.HttpURLConnection) localUrl.openConnection();
            setupConnection(connection, method, headerFields, connectTimeout, readTimeout);
            if (body != null) {
                connection.setDoOutput(true);
                final java.io.DataOutputStream outputStream =
                    new java.io.DataOutputStream(connection.getOutputStream());
                outputStream.writeBytes(body);
                outputStream.close();
            }
            final int responseCode = connection.getResponseCode();
            final java.io.InputStream inputStream;
            if (responseCode < RESPONSE_CODE_400) {
                inputStream = connection.getInputStream();
            } else {
                inputStream = connection.getErrorStream();
            }
            final java.io.ByteArrayOutputStream result = new java.io.ByteArrayOutputStream();
            final byte[] buffer = new byte[BUFFER_LENGTH_1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            inputStream.close();
            return new FetchResponse(responseCode < RESPONSE_CODE_400, responseCode, connection.getHeaderFields(),
                result);
        } catch (java.io.IOException ex) {
            throw new UnsupportedOperationException(ex);
        }
    }

    public String camelCase() {
        return camelCase(getString().get());
    }

    public String lowerFirst() {
        return lowerFirst(getString().get());
    }

    public String upperFirst() {
        return upperFirst(getString().get());
    }

    public String capitalize() {
        return capitalize(getString().get());
    }

    public String deburr() {
        return deburr(getString().get());
    }

    public boolean endsWith(final String target) {
        return endsWith(getString().get(), target);
    }

    public boolean endsWith(final String target, final Integer position) {
        return endsWith(getString().get(), target, position);
    }

    public String kebabCase() {
        return kebabCase(getString().get());
    }

    public String repeat(final int length) {
        return repeat(getString().get(), length);
    }

    public String pad(final int length) {
        return pad(getString().get(), length);
    }

    public String pad(final int length, final String chars) {
        return pad(getString().get(), length, chars);
    }

    public String padStart(final int length) {
        return padStart(getString().get(), length);
    }

    public String padStart(final int length, final String chars) {
        return padStart(getString().get(), length, chars);
    }

    public String padEnd(final int length) {
        return padEnd(getString().get(), length);
    }

    public String padEnd(final int length, final String chars) {
        return padEnd(getString().get(), length, chars);
    }

    public String snakeCase() {
        return snakeCase(getString().get());
    }

    public String startCase() {
        return startCase(getString().get());
    }

    public boolean startsWith(final String target) {
        return startsWith(getString().get(), target);
    }

    public boolean startsWith(final String target, final Integer position) {
        return startsWith(getString().get(), target, position);
    }

    public String trim() {
        return trim(getString().get());
    }

    public String trimWith(final String chars) {
        return trim(getString().get(), chars);
    }

    public String trimStart() {
        return trimStart(getString().get());
    }

    public String trimStartWith(final String chars) {
        return trimStart(getString().get(), chars);
    }

    public String trimEnd() {
        return trimEnd(getString().get());
    }

    public String trimEndWith(final String chars) {
        return trimEnd(getString().get(), chars);
    }

    public String trunc() {
        return trunc(getString().get());
    }

    public String trunc(final int length) {
        return trunc(getString().get(), length);
    }

    public String uncapitalize() {
        return uncapitalize(getString().get());
    }

    public List<String> words() {
        return words(getString().get());
    }

    public static class LRUCache<K, V> {
        private static final boolean SORT_BY_ACCESS = true;
        private static final float LOAD_FACTOR = 0.75F;
        private final Map<K, V> lruCacheMap;
        private final int capacity;

        public LRUCache(int capacity) {
            this.capacity = capacity;
            this.lruCacheMap = new LinkedHashMap<K, V>(capacity, LOAD_FACTOR, SORT_BY_ACCESS);
        }

        public V get(K key) {
            return lruCacheMap.get(key);
        }

        public void put(K key, V value) {
            if (lruCacheMap.containsKey(key)) {
                lruCacheMap.remove(key);
            } else if (lruCacheMap.size() >= capacity) {
                lruCacheMap.remove(lruCacheMap.keySet().iterator().next());
            }
            lruCacheMap.put(key, value);
        }
    }

    public static <K, V> LRUCache<K, V> createLRUCache(final int capacity) {
        return new LRUCache<K, V>(capacity);
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

    @SuppressWarnings("unchecked")
    public static String jsonToXml(String json) {
        Object result = fromJson(json);
        if (result instanceof Map) {
            return toXml((Map) result);
        }
        return toXml((List) result);
    }

    @SuppressWarnings("unchecked")
    public static String xmlToJson(String xml) {
        Object result = fromXml(xml);
        if (result instanceof Map) {
            return toJson((Map) result);
        }
        return toJson((List) result);
    }

    @SuppressWarnings("unchecked")
    public static String formatJson(String json, JsonStringBuilder.Step identStep) {
        Object result = fromJson(json);
        if (result instanceof Map) {
            return toJson((Map) result, identStep);
        }
        return toJson((List) result, identStep);
    }

    public static String formatJson(String json) {
        return formatJson(json, JsonStringBuilder.Step.THREE_SPACES);
    }

    @SuppressWarnings("unchecked")
    public static String formatXml(String xml, XmlStringBuilder.Step identStep) {
        Object result = fromXml(xml);
        if (result instanceof Map) {
            return toXml((Map) result, identStep);
        }
        return toXml((List) result, identStep);
    }

    public static String formatXml(String xml) {
        return formatXml(xml, XmlStringBuilder.Step.THREE_SPACES);
    }
}
