/*
 * The MIT License (MIT)
 *
 * Copyright 2015-2022 Valentyn Kolesnikov
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.zip.GZIPInputStream;

@SuppressWarnings({
    "java:S135",
    "java:S1168",
    "java:S3740",
    "java:S3776",
    "java:S4423",
    "java:S4830",
    "java:S5843",
    "java:S5996",
    "java:S5998"
})
public class U<T> extends Underscore<T> {
    private static final int DEFAULT_TRUNC_LENGTH = 30;
    private static final String DEFAULT_TRUNC_OMISSION = "...";
    private static final java.util.regex.Pattern RE_LATIN_1 =
            java.util.regex.Pattern.compile("[\\xc0-\\xd6\\xd8-\\xde\\xdf-\\xf6\\xf8-\\xff]");
    private static final java.util.regex.Pattern RE_PROP_NAME =
            java.util.regex.Pattern.compile(
                    "[^.\\[\\]]+|\\[(?:(-?\\d+(?:\\.\\d+)?)|([\"'])((?:(?!\2)\\[^\\]|\\.)*?)\2)\\]|"
                            + "(?=(\\.|\\[\\])(?:\4|$))");
    private static final Map<String, String> DEBURRED_LETTERS = new LinkedHashMap<>();
    private static final Map<String, List<String>> DEFAULT_HEADER_FIELDS = new HashMap<>();
    private static final Set<String> SUPPORTED_HTTP_METHODS =
            new HashSet<>(Arrays.asList("GET", "POST", "PUT", "DELETE"));
    private static final int BUFFER_LENGTH_1024 = 1024;
    private static final int RESPONSE_CODE_400 = 400;
    private static final String ROOT = "root";
    private static String upper = "[A-Z\\xc0-\\xd6\\xd8-\\xde\\u0400-\\u04FF]";
    private static String lower = "[a-z\\xdf-\\xf6\\xf8-\\xff]+";
    private static String selfClosing = "-self-closing";
    private static String nilKey = "-nil";
    private static String nilKeyNs = ":nil";
    private static java.util.regex.Pattern reWords =
            java.util.regex.Pattern.compile(
                    upper
                            + "+(?="
                            + upper
                            + lower
                            + ")|"
                            + upper
                            + "?"
                            + lower
                            + "|"
                            + upper
                            + "+|[0-9]+");

    static {
        String[] deburredLetters =
                new String[] {
                    "\u00c0", "A", "\u00c1", "A", "\u00c2", "A", "\u00c3", "A", "\u00c4", "A",
                    "\u00c5", "A", "\u00e0", "a", "\u00e1", "a", "\u00e2", "a", "\u00e3", "a",
                    "\u00e4", "a", "\u00e5", "a", "\u00c7", "C", "\u00e7", "c", "\u00d0", "D",
                    "\u00f0", "d", "\u00c8", "E", "\u00c9", "E", "\u00ca", "E", "\u00cb", "E",
                    "\u00e8", "e", "\u00e9", "e", "\u00ea", "e", "\u00eb", "e", "\u00cC", "I",
                    "\u00cd", "I", "\u00ce", "I", "\u00cf", "I", "\u00eC", "i", "\u00ed", "i",
                    "\u00ee", "i", "\u00ef", "i", "\u00d1", "N", "\u00f1", "n", "\u00d2", "O",
                    "\u00d3", "O", "\u00d4", "O", "\u00d5", "O", "\u00d6", "O", "\u00d8", "O",
                    "\u00f2", "o", "\u00f3", "o", "\u00f4", "o", "\u00f5", "o", "\u00f6", "o",
                    "\u00f8", "o", "\u00d9", "U", "\u00da", "U", "\u00db", "U", "\u00dc", "U",
                    "\u00f9", "u", "\u00fa", "u", "\u00fb", "u", "\u00fc", "u", "\u00dd", "Y",
                    "\u00fd", "y", "\u00ff", "y", "\u00c6", "Ae", "\u00e6", "ae", "\u00de", "Th",
                    "\u00fe", "th", "\u00df", "ss"
                };
        for (int index = 0; index < deburredLetters.length; index += 2) {
            DEBURRED_LETTERS.put(deburredLetters[index], deburredLetters[index + 1]);
        }
        DEFAULT_HEADER_FIELDS.put(
                "Content-Type", Arrays.asList("application/json", "charset=utf-8"));
    }

    public enum Mode {
        REPLACE_SELF_CLOSING_WITH_NULL,
        REPLACE_SELF_CLOSING_WITH_EMPTY,
        REPLACE_EMPTY_VALUE_WITH_NULL,
        REPLACE_EMPTY_TAG_WITH_NULL,
        REPLACE_EMPTY_TAG_WITH_EMPTY_STRING,
        FORCE_ATTRIBUTE_USAGE,
        DEFINE_ROOT_NAME,
        REPLACE_NULL_WITH_EMPTY_VALUE,
        REPLACE_EMPTY_STRING_WITH_EMPTY_VALUE,
        REMOVE_FIRST_LEVEL_XML_TO_JSON,
        FORCE_ADD_ROOT_JSON_TO_XML,
        FORCE_REMOVE_ARRAY_ATTRIBUTE_JSON_TO_XML,
        FORCE_REMOVE_ARRAY_BOOLEAN_NUMBER_ATTRIBUTES_JSON_TO_XML
    }

    public U(final Iterable<T> iterable) {
        super(iterable);
    }

    public U(final String string) {
        super(string);
    }

    public static class Chain<T> extends Underscore.Chain<T> {
        public Chain(final T item) {
            super(item);
        }

        public Chain(final List<T> list) {
            super(list);
        }

        public Chain(final Map<String, Object> map) {
            super(map);
        }

        @Override
        public Chain<T> first() {
            return new Chain<>(Underscore.first(value()));
        }

        @Override
        public Chain<T> first(int n) {
            return new Chain<>(Underscore.first(value(), n));
        }

        @Override
        public Chain<T> firstOrNull() {
            return new Chain<>(Underscore.firstOrNull(value()));
        }

        @Override
        public Chain<T> firstOrNull(final Predicate<T> pred) {
            return new Chain<>(Underscore.firstOrNull(value(), pred));
        }

        @Override
        public Chain<T> initial() {
            return new Chain<>(Underscore.initial(value()));
        }

        @Override
        public Chain<T> initial(int n) {
            return new Chain<>(Underscore.initial(value(), n));
        }

        @Override
        public Chain<T> last() {
            return new Chain<>(Underscore.last(value()));
        }

        @Override
        public Chain<T> last(int n) {
            return new Chain<>(Underscore.last(value(), n));
        }

        @Override
        public Chain<T> lastOrNull() {
            return new Chain<>(Underscore.lastOrNull(value()));
        }

        @Override
        public Chain<T> lastOrNull(final Predicate<T> pred) {
            return new Chain<>(Underscore.lastOrNull(value(), pred));
        }

        @Override
        public Chain<T> rest() {
            return new Chain<>(Underscore.rest(value()));
        }

        @Override
        public Chain<T> rest(int n) {
            return new Chain<>(Underscore.rest(value(), n));
        }

        @Override
        public Chain<T> compact() {
            return new Chain<>(Underscore.compact(value()));
        }

        @Override
        public Chain<T> compact(final T falsyValue) {
            return new Chain<>(Underscore.compact(value(), falsyValue));
        }

        @Override
        public Chain flatten() {
            return new Chain<>(Underscore.flatten(value()));
        }

        @Override
        public <F> Chain<F> map(final Function<? super T, F> func) {
            return new Chain<>(Underscore.map(value(), func));
        }

        @Override
        public <F> Chain<F> mapMulti(final BiConsumer<? super T, ? super Consumer<F>> mapper) {
            return new Chain<>(Underscore.mapMulti(value(), mapper));
        }

        @Override
        public <F> Chain<F> mapIndexed(final BiFunction<Integer, ? super T, F> func) {
            return new Chain<>(Underscore.mapIndexed(value(), func));
        }

        @Override
        public Chain<T> filter(final Predicate<T> pred) {
            return new Chain<>(Underscore.filter(value(), pred));
        }

        @Override
        public Chain<T> filterIndexed(final PredicateIndexed<T> pred) {
            return new Chain<>(Underscore.filterIndexed(value(), pred));
        }

        @Override
        public Chain<T> rejectIndexed(final PredicateIndexed<T> pred) {
            return new Chain<>(Underscore.rejectIndexed(value(), pred));
        }

        @Override
        public Chain<T> reject(final Predicate<T> pred) {
            return new Chain<>(Underscore.reject(value(), pred));
        }

        @Override
        public Chain<T> filterFalse(final Predicate<T> pred) {
            return new Chain<>(Underscore.filterFalse(value(), pred));
        }

        @Override
        public <F> Chain<F> reduce(final BiFunction<F, T, F> func, final F zeroElem) {
            return new Chain<>(Underscore.reduce(value(), func, zeroElem));
        }

        @Override
        public Chain<Optional<T>> reduce(final BinaryOperator<T> func) {
            return new Chain<>(Underscore.reduce(value(), func));
        }

        @Override
        public <F> Chain<F> reduceRight(final BiFunction<F, T, F> func, final F zeroElem) {
            return new Chain<>(Underscore.reduceRight(value(), func, zeroElem));
        }

        @Override
        public Chain<Optional<T>> reduceRight(final BinaryOperator<T> func) {
            return new Chain<>(Underscore.reduceRight(value(), func));
        }

        @Override
        public Chain<Optional<T>> find(final Predicate<T> pred) {
            return new Chain<>(Underscore.find(value(), pred));
        }

        @Override
        public Chain<Optional<T>> findLast(final Predicate<T> pred) {
            return new Chain<>(Underscore.findLast(value(), pred));
        }

        @Override
        @SuppressWarnings("unchecked")
        public Chain<Comparable> max() {
            return new Chain<>(Underscore.max((Collection) value()));
        }

        @Override
        public <F extends Comparable<? super F>> Chain<T> max(final Function<T, F> func) {
            return new Chain<>(Underscore.max(value(), func));
        }

        @Override
        @SuppressWarnings("unchecked")
        public Chain<Comparable> min() {
            return new Chain<>(Underscore.min((Collection) value()));
        }

        @Override
        public <F extends Comparable<? super F>> Chain<T> min(final Function<T, F> func) {
            return new Chain<>(Underscore.min(value(), func));
        }

        @Override
        @SuppressWarnings("unchecked")
        public Chain<Comparable> sort() {
            return new Chain<>(Underscore.sort((List<Comparable>) value()));
        }

        @Override
        @SuppressWarnings("unchecked")
        public <F extends Comparable<? super F>> Chain<F> sortWith(final Comparator<F> comparator) {
            return new Chain<>(Underscore.sortWith((List<F>) value(), comparator));
        }

        @Override
        public <F extends Comparable<? super F>> Chain<T> sortBy(final Function<T, F> func) {
            return new Chain<>(Underscore.sortBy(value(), func));
        }

        @Override
        @SuppressWarnings("unchecked")
        public <K> Chain<Map<K, Comparable>> sortBy(final K key) {
            return new Chain<>(Underscore.sortBy((List<Map<K, Comparable>>) value(), key));
        }

        @Override
        public <F> Chain<Map<F, List<T>>> groupBy(final Function<T, F> func) {
            return new Chain<>(Underscore.groupBy(value(), func));
        }

        @Override
        public <F> Chain<Map<F, Optional<T>>> groupBy(
                final Function<T, F> func, final BinaryOperator<T> binaryOperator) {
            return new Chain<>(Underscore.groupBy(value(), func, binaryOperator));
        }

        @Override
        public Chain<Map<Object, List<T>>> indexBy(final String property) {
            return new Chain<>(Underscore.indexBy(value(), property));
        }

        @Override
        public <F> Chain<Map<F, Integer>> countBy(final Function<T, F> func) {
            return new Chain<>(Underscore.countBy(value(), func));
        }

        @Override
        public Chain<Map<T, Integer>> countBy() {
            return new Chain<>(Underscore.countBy(value()));
        }

        @Override
        public Chain<T> shuffle() {
            return new Chain<>(Underscore.shuffle(value()));
        }

        @Override
        public Chain<T> sample() {
            return new Chain<>(Underscore.sample(value()));
        }

        @Override
        public Chain<T> sample(final int howMany) {
            return new Chain<>(newArrayList(Underscore.sample(value(), howMany)));
        }

        @Override
        public Chain<T> tap(final Consumer<T> func) {
            Underscore.tap(value(), func);
            return new Chain<>(value());
        }

        @Override
        public Chain<T> forEach(final Consumer<T> func) {
            Underscore.forEach(value(), func);
            return new Chain<>(value());
        }

        @Override
        public Chain<T> forEachRight(final Consumer<T> func) {
            Underscore.forEachRight(value(), func);
            return new Chain<>(value());
        }

        @Override
        public Chain<Boolean> every(final Predicate<T> pred) {
            return new Chain<>(Underscore.every(value(), pred));
        }

        @Override
        public Chain<Boolean> some(final Predicate<T> pred) {
            return new Chain<>(Underscore.some(value(), pred));
        }

        @Override
        public Chain<Integer> count(final Predicate<T> pred) {
            return new Chain<>(Underscore.count(value(), pred));
        }

        @Override
        public Chain<Boolean> contains(final T elem) {
            return new Chain<>(Underscore.contains(value(), elem));
        }

        @Override
        public Chain<Boolean> containsWith(final T elem) {
            return new Chain<>(Underscore.containsWith(value(), elem));
        }

        @Override
        public Chain<T> invoke(final String methodName, final List<Object> args) {
            return new Chain<>(Underscore.invoke(value(), methodName, args));
        }

        @Override
        public Chain<T> invoke(final String methodName) {
            return new Chain<>(Underscore.invoke(value(), methodName));
        }

        @Override
        public Chain<Object> pluck(final String propertyName) {
            return new Chain<>(Underscore.pluck(value(), propertyName));
        }

        @Override
        public <E> Chain<T> where(final List<Tuple<String, E>> properties) {
            return new Chain<>(Underscore.where(value(), properties));
        }

        @Override
        public <E> Chain<Optional<T>> findWhere(final List<Tuple<String, E>> properties) {
            return new Chain<>(Underscore.findWhere(value(), properties));
        }

        @Override
        public Chain<T> uniq() {
            return new Chain<>(Underscore.uniq(value()));
        }

        @Override
        public <F> Chain<T> uniq(final Function<T, F> func) {
            return new Chain<>(newArrayList(Underscore.uniq(value(), func)));
        }

        @Override
        public Chain<T> distinct() {
            return new Chain<>(Underscore.uniq(value()));
        }

        @Override
        @SuppressWarnings("unchecked")
        public <F> Chain<F> distinctBy(final Function<T, F> func) {
            return new Chain<>(newArrayList((Iterable<F>) Underscore.uniq(value(), func)));
        }

        @Override
        @SuppressWarnings("unchecked")
        public Chain<T> union(final List<T>... lists) {
            return new Chain<>(Underscore.union(value(), lists));
        }

        @Override
        @SuppressWarnings("unchecked")
        public Chain<T> intersection(final List<T>... lists) {
            return new Chain<>(Underscore.intersection(value(), lists));
        }

        @Override
        @SuppressWarnings("unchecked")
        public Chain<T> difference(final List<T>... lists) {
            return new Chain<>(Underscore.difference(value(), lists));
        }

        @Override
        public Chain<Integer> range(final int stop) {
            return new Chain<>(Underscore.range(stop));
        }

        @Override
        public Chain<Integer> range(final int start, final int stop) {
            return new Chain<>(Underscore.range(start, stop));
        }

        @Override
        public Chain<Integer> range(final int start, final int stop, final int step) {
            return new Chain<>(Underscore.range(start, stop, step));
        }

        @Override
        public Chain<List<T>> chunk(final int size) {
            return new Chain<>(Underscore.chunk(value(), size, size));
        }

        @Override
        public Chain<List<T>> chunk(final int size, final int step) {
            return new Chain<>(Underscore.chunk(value(), size, step));
        }

        @Override
        public Chain<List<T>> chunkFill(final int size, final T fillValue) {
            return new Chain<>(Underscore.chunkFill(value(), size, size, fillValue));
        }

        @Override
        public Chain<List<T>> chunkFill(final int size, final int step, final T fillValue) {
            return new Chain<>(Underscore.chunkFill(value(), size, step, fillValue));
        }

        @Override
        public Chain<T> cycle(final int times) {
            return new Chain<>(Underscore.cycle(value(), times));
        }

        @Override
        public Chain<T> interpose(final T element) {
            return new Chain<>(Underscore.interpose(value(), element));
        }

        @Override
        public Chain<T> interposeByList(final Iterable<T> interIter) {
            return new Chain<>(Underscore.interposeByList(value(), interIter));
        }

        @Override
        @SuppressWarnings("unchecked")
        public Chain<T> concat(final List<T>... lists) {
            return new Chain<>(Underscore.concat(value(), lists));
        }

        @Override
        public Chain<T> slice(final int start) {
            return new Chain<>(Underscore.slice(value(), start));
        }

        @Override
        public Chain<T> slice(final int start, final int end) {
            return new Chain<>(Underscore.slice(value(), start, end));
        }

        public Chain<Map<String, Object>> set(final String path, Object value) {
            U.set(map(), path, value);
            return new Chain<>(map());
        }

        @Override
        public Chain<T> reverse() {
            return new Chain<>(Underscore.reverse(value()));
        }

        @Override
        public Chain<String> join() {
            return new Chain<>(Underscore.join(value()));
        }

        @Override
        public Chain<String> join(final String separator) {
            return new Chain<>(Underscore.join(value(), separator));
        }

        @Override
        public Chain<T> skip(final int numberToSkip) {
            return new Chain<>(value().subList(numberToSkip, value().size()));
        }

        @Override
        public Chain<T> limit(final int size) {
            return new Chain<>(value().subList(0, size));
        }

        @Override
        @SuppressWarnings("unchecked")
        public <K, V> Chain<Map<K, V>> toMap() {
            return new Chain<>(Underscore.toMap((Iterable<Map.Entry<K, V>>) value()));
        }

        public Chain<T> drop() {
            return new Chain<>(Underscore.drop(value()));
        }

        public Chain<T> drop(final Integer n) {
            return new Chain<>(Underscore.drop(value(), n));
        }

        public Chain<T> dropRight() {
            return new Chain<>(U.dropRight(value()));
        }

        public Chain<T> dropRight(final Integer n) {
            return new Chain<>(U.dropRight(value(), n));
        }

        public Chain<T> dropWhile(final Predicate<T> pred) {
            return new Chain<>(U.dropWhile(value(), pred));
        }

        public Chain<T> dropRightWhile(final Predicate<T> pred) {
            return new Chain<>(U.dropRightWhile(value(), pred));
        }

        @SuppressWarnings("unchecked")
        public Chain<Object> fill(final Object value) {
            return new Chain<>(U.fill((List<Object>) value(), value));
        }

        @SuppressWarnings("unchecked")
        public Chain<Object> fill(final Object value, final Integer start, final Integer end) {
            return new Chain<>(U.fill((List<Object>) value(), value, start, end));
        }

        public Chain<Object> flattenDeep() {
            return new Chain<>(U.flattenDeep(value()));
        }

        @SuppressWarnings("unchecked")
        public Chain<Object> pull(final Object... values) {
            return new Chain<>(U.pull((List<Object>) value(), values));
        }

        @SuppressWarnings("unchecked")
        public Chain<Object> pullAt(final Integer... indexes) {
            return new Chain<>(U.pullAt((List<Object>) value(), indexes));
        }

        public Chain<T> remove(final Predicate<T> pred) {
            return new Chain<>(U.remove(value(), pred));
        }

        public Chain<T> take() {
            return new Chain<>(U.take(value()));
        }

        public Chain<T> takeRight() {
            return new Chain<>(U.takeRight(value()));
        }

        public Chain<T> take(final Integer n) {
            return new Chain<>(U.take(value(), n));
        }

        public Chain<T> takeRight(final Integer n) {
            return new Chain<>(U.takeRight(value(), n));
        }

        public Chain<T> takeWhile(final Predicate<T> pred) {
            return new Chain<>(U.takeWhile(value(), pred));
        }

        public Chain<T> takeRightWhile(final Predicate<T> pred) {
            return new Chain<>(U.takeRightWhile(value(), pred));
        }

        @SuppressWarnings("unchecked")
        public Chain<T> xor(final List<T> list) {
            return new Chain<>(U.xor(value(), list));
        }

        public Chain<T> at(final Integer... indexes) {
            return new Chain<>(U.at(value(), indexes));
        }

        @SuppressWarnings("unchecked")
        public <F extends Number> Chain<F> sum() {
            return new Chain<>(U.sum((List<F>) value()));
        }

        public <F extends Number> Chain<F> sum(final Function<T, F> func) {
            return new Chain<>(U.sum(value(), func));
        }

        @SuppressWarnings("unchecked")
        public Chain<Double> mean() {
            return new Chain<>(U.mean((List<Number>) value()));
        }

        @SuppressWarnings("unchecked")
        public Chain<Double> median() {
            return new Chain<>(U.median((List<Number>) value()));
        }

        public Chain<String> camelCase() {
            return new Chain<>(U.camelCase((String) item()));
        }

        public Chain<String> lowerFirst() {
            return new Chain<>(U.lowerFirst((String) item()));
        }

        public Chain<String> upperFirst() {
            return new Chain<>(U.upperFirst((String) item()));
        }

        public Chain<String> capitalize() {
            return new Chain<>(U.capitalize((String) item()));
        }

        public Chain<String> deburr() {
            return new Chain<>(U.deburr((String) item()));
        }

        public Chain<Boolean> endsWith(final String target) {
            return new Chain<>(U.endsWith((String) item(), target));
        }

        public Chain<Boolean> endsWith(final String target, final Integer position) {
            return new Chain<>(U.endsWith((String) item(), target, position));
        }

        public Chain<String> kebabCase() {
            return new Chain<>(U.kebabCase((String) item()));
        }

        public Chain<String> repeat(final int length) {
            return new Chain<>(U.repeat((String) item(), length));
        }

        public Chain<String> pad(final int length) {
            return new Chain<>(U.pad((String) item(), length));
        }

        public Chain<String> pad(final int length, final String chars) {
            return new Chain<>(U.pad((String) item(), length, chars));
        }

        public Chain<String> padStart(final int length) {
            return new Chain<>(U.padStart((String) item(), length));
        }

        public Chain<String> padStart(final int length, final String chars) {
            return new Chain<>(U.padStart((String) item(), length, chars));
        }

        public Chain<String> padEnd(final int length) {
            return new Chain<>(U.padEnd((String) item(), length));
        }

        public Chain<String> padEnd(final int length, final String chars) {
            return new Chain<>(U.padEnd((String) item(), length, chars));
        }

        public Chain<String> snakeCase() {
            return new Chain<>(U.snakeCase((String) item()));
        }

        public Chain<String> startCase() {
            return new Chain<>(U.startCase((String) item()));
        }

        public Chain<Boolean> startsWith(final String target) {
            return new Chain<>(U.startsWith((String) item(), target));
        }

        public Chain<Boolean> startsWith(final String target, final Integer position) {
            return new Chain<>(U.startsWith((String) item(), target, position));
        }

        public Chain<String> trim() {
            return new Chain<>(U.trim((String) item()));
        }

        public Chain<String> trim(final String chars) {
            return new Chain<>(U.trim((String) item(), chars));
        }

        public Chain<String> trimStart() {
            return new Chain<>(U.trimStart((String) item()));
        }

        public Chain<String> trimStart(final String chars) {
            return new Chain<>(U.trimStart((String) item(), chars));
        }

        public Chain<String> trimEnd() {
            return new Chain<>(U.trimEnd((String) item()));
        }

        public Chain<String> trunc() {
            return new Chain<>(U.trunc((String) item()));
        }

        public Chain<String> trunc(final int length) {
            return new Chain<>(U.trunc((String) item(), length));
        }

        public Chain<String> trimEnd(final String chars) {
            return new Chain<>(U.trimEnd((String) item(), chars));
        }

        public Chain<String> uncapitalize() {
            return new Chain<>(U.uncapitalize((String) item()));
        }

        public Chain<String> words() {
            return new Chain<>(U.words((String) item()));
        }

        public Chain<String> toJson() {
            return new Chain<>(Json.toJson(value()));
        }

        public Chain<Object> fromJson() {
            return new Chain<>(Json.fromJson((String) item()));
        }

        public Chain<String> toXml() {
            return new Chain<>(Xml.toXml(value()));
        }

        public Chain<Object> fromXml() {
            return new Chain<>(Xml.fromXml((String) item()));
        }

        public Chain<String> fetch() {
            return new Chain<>(U.fetch((String) item()).text());
        }

        public Chain<String> fetch(final String method, final String body) {
            return new Chain<>(U.fetch((String) item(), method, body).text());
        }

        public Chain<List<T>> createPermutationWithRepetition(final int permutationLength) {
            return new Chain<>(U.createPermutationWithRepetition(value(), permutationLength));
        }

        public Chain<String> toJsonJavaString() {
            return new Chain<>(Json.toJsonJavaString(value()));
        }

        public Chain<String> xmlToJson() {
            return new Chain<>(U.xmlToJson((String) item()));
        }

        public Chain<String> jsonToXml() {
            return new Chain<>(U.jsonToXml((String) item()));
        }
    }

    public static Chain<String> chain(final String item) {
        return new U.Chain<>(item);
    }

    public static <T> Chain<T> chain(final List<T> list) {
        return new U.Chain<>(list);
    }

    public static Chain<Map<String, Object>> chain(final Map<String, Object> map) {
        return new U.Chain<>(map);
    }

    public static <T> Chain<T> chain(final Iterable<T> iterable) {
        return new U.Chain<>(newArrayList(iterable));
    }

    public static <T> Chain<T> chain(final Iterable<T> iterable, int size) {
        return new U.Chain<>(newArrayList(iterable, size));
    }

    @SuppressWarnings("unchecked")
    public static <T> Chain<T> chain(final T... list) {
        return new U.Chain<>(Arrays.asList(list));
    }

    public static Chain<Integer> chain(final int[] array) {
        return new U.Chain<>(newIntegerList(array));
    }

    @Override
    public Chain<T> chain() {
        return new U.Chain<>(newArrayList(value()));
    }

    public static Chain<String> of(final String item) {
        return new U.Chain<>(item);
    }

    public static <T> Chain<T> of(final List<T> list) {
        return new U.Chain<>(list);
    }

    public static Chain<Map<String, Object>> of(final Map<String, Object> map) {
        return new U.Chain<>(map);
    }

    public static <T> Chain<T> of(final Iterable<T> iterable) {
        return new U.Chain<>(newArrayList(iterable));
    }

    public static <T> Chain<T> of(final Iterable<T> iterable, int size) {
        return new U.Chain<>(newArrayList(iterable, size));
    }

    @SuppressWarnings("unchecked")
    public static <T> Chain<T> of(final T... list) {
        return new U.Chain<>(Arrays.asList(list));
    }

    public static Chain<Integer> of(final int[] array) {
        return new U.Chain<>(newIntegerList(array));
    }

    @Override
    public Chain<T> of() {
        return new U.Chain<>(newArrayList(value()));
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

    public static <T> List<T> fill(List<T> list, T item) {
        for (int i = 0; i < size(list); i++) {
            list.set(i, item);
        }
        return list;
    }

    public static <T> T[] fill(T[] array, T item) {
        Arrays.fill(array, item);
        return array;
    }

    @SuppressWarnings("unchecked")
    public List<Object> fill(Object value) {
        return fill((List<Object>) getIterable(), value);
    }

    public static List<Object> fill(
            final List<Object> list, Object value, Integer start, Integer end) {
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

    public static List<Object> pull(final List<Object> list, Object... values) {
        final List<Object> valuesList = Arrays.asList(values);
        list.removeIf(valuesList::contains);
        return list;
    }

    @SuppressWarnings("unchecked")
    public List<Object> pull(Object... values) {
        return pull((List<Object>) getIterable(), values);
    }

    public static List<Object> pullAt(final List<Object> list, final Integer... indexes) {
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
    public List<Object> pullAt(final Integer... indexes) {
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
    public static <T> List<T> xor(final List<T>... lists) {
        int index = -1;
        int length = lists.length;
        List<T> result = null;
        while (++index < length) {
            final List<T> array = lists[index];
            result =
                    result == null
                            ? array
                            : concat(difference(result, array), difference(array, result));
        }
        return uniq(result);
    }

    @SuppressWarnings("unchecked")
    public List<T> xor(final List<T> list) {
        return xor((List<T>) getIterable(), list);
    }

    public static <T> List<T> at(final List<T> list, final Integer... indexes) {
        final List<T> result = newArrayList();
        final List<Integer> indexesList = Arrays.asList(indexes);
        int index = 0;
        for (final T object : list) {
            if (indexesList.contains(index)) {
                result.add(object);
            }
            index += 1;
        }
        return result;
    }

    public List<T> at(final Integer... indexes) {
        return at((List<T>) getIterable(), indexes);
    }

    public static <T extends Number> Double average(final Iterable<T> iterable) {
        T sum = sum(iterable);
        if (sum == null) {
            return null;
        }
        return sum.doubleValue() / size(iterable);
    }

    public static <E, F extends Number> Double average(
            final Iterable<E> iterable, final Function<E, F> func) {
        F sum = sum(iterable, func);
        if (sum == null) {
            return null;
        }
        return sum.doubleValue() / size(iterable);
    }

    public static <N extends Number> Double average(N[] array) {
        N sum = sum(array);
        if (sum == null) {
            return null;
        }
        return sum.doubleValue() / array.length;
    }

    public static Double average(java.math.BigDecimal first, java.math.BigDecimal second) {
        if (first == null || second == null) {
            return null;
        }
        return sum(first, second).doubleValue() / 2;
    }

    public static Double average(java.math.BigInteger first, java.math.BigInteger second) {
        if (first == null || second == null) {
            return null;
        }
        return sum(first, second).doubleValue() / 2;
    }

    public static Double average(Byte first, Byte second) {
        if (first == null || second == null) {
            return null;
        }
        return sum(first, second).doubleValue() / 2;
    }

    public static Double average(Double first, Double second) {
        if (first == null || second == null) {
            return null;
        }
        return sum(first, second) / 2;
    }

    public static Double average(Float first, Float second) {
        if (first == null || second == null) {
            return null;
        }
        return sum(first, second).doubleValue() / 2;
    }

    public static Double average(Integer first, Integer second) {
        if (first == null || second == null) {
            return null;
        }
        return sum(first, second).doubleValue() / 2;
    }

    public static Double average(Long first, Long second) {
        if (first == null || second == null) {
            return null;
        }
        return sum(first, second).doubleValue() / 2;
    }

    public static <T extends Number> T sum(final Iterable<T> iterable) {
        T result = null;
        for (final T item : iterable) {
            result = add(result, item);
        }
        return result;
    }

    public static <E, F extends Number> F sum(
            final Iterable<E> iterable, final Function<E, F> func) {
        F result = null;
        for (final E item : iterable) {
            result = add(result, func.apply(item));
        }
        return result;
    }

    public static <N extends Number> N sum(N[] array) {
        N result = null;
        for (final N item : array) {
            result = add(result, item);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public <F extends Number> F sum() {
        return sum((List<F>) getIterable());
    }

    @SuppressWarnings("unchecked")
    public <E, F extends Number> F sum(final Function<E, F> func) {
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
            throw new UnsupportedOperationException(
                    "Sum only supports official subclasses of Number");
        }
    }

    private static java.math.BigDecimal sum(
            java.math.BigDecimal first, java.math.BigDecimal second) {
        return first.add(second);
    }

    private static java.math.BigInteger sum(
            java.math.BigInteger first, java.math.BigInteger second) {
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

    @SuppressWarnings("unchecked")
    public static <T extends Number> T subtract(final T... values) {
        if (values.length == 0) {
            return null;
        }
        T result = values[0];
        for (int i = 1; i < values.length; i++) {
            if (result instanceof java.math.BigDecimal) {
                java.math.BigDecimal value = (java.math.BigDecimal) values[i];
                result = add(result, (T) value.negate());
            } else if (result instanceof java.math.BigInteger) {
                java.math.BigInteger value = (java.math.BigInteger) values[i];
                result = add(result, (T) value.negate());
            } else if (result instanceof Byte) {
                result = add(result, (T) Byte.valueOf((byte) (values[i].byteValue() * -1)));
            } else if (result instanceof Double) {
                result = add(result, (T) Double.valueOf(values[i].doubleValue() * -1));
            } else if (result instanceof Float) {
                result = add(result, (T) Float.valueOf(values[i].floatValue() * -1));
            } else if (result instanceof Integer) {
                result = add(result, (T) Integer.valueOf(values[i].intValue() * -1));
            } else if (result instanceof Long) {
                result = add(result, (T) Long.valueOf(values[i].longValue() * -1));
            } else if (result instanceof Short) {
                result = add(result, (T) Short.valueOf((short) (values[i].shortValue() * -1)));
            } else {
                throw new UnsupportedOperationException(
                        "Subtract only supports official subclasses of Number");
            }
        }
        return result;
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
        return createCompounder(
                        (result, word, index) -> {
                            final String localWord = word.toLowerCase(Locale.getDefault());
                            return result
                                    + (index > 0
                                            ? localWord
                                                            .substring(0, 1)
                                                            .toUpperCase(Locale.getDefault())
                                                    + localWord.substring(1)
                                            : localWord);
                        })
                .apply(string);
    }

    public static String lowerFirst(final String string) {
        return createCaseFirst("toLowerCase").apply(string);
    }

    public static String upperFirst(final String string) {
        return createCaseFirst("toUpperCase").apply(string);
    }

    public static String capitalize(final String string) {
        return upperFirst(baseToString(string));
    }

    public static String uncapitalize(final String string) {
        return lowerFirst(baseToString(string));
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
        final List<String> result = new ArrayList<>();
        final java.util.regex.Matcher matcher = reWords.matcher(localString);
        while (matcher.find()) {
            result.add(matcher.group());
        }
        return result;
    }

    private static Function<String, String> createCompounder(
            final Function3<String, String, Integer, String> callback) {
        return string -> {
            int index = -1;
            List<String> array = words(deburr(string));
            int length = array.size();
            String result = "";

            while (++index < length) {
                result = callback.apply(result, array.get(index), index);
            }
            return result;
        };
    }

    private static Function<String, String> createCaseFirst(final String methodName) {
        return string -> {
            final String localString = baseToString(string);
            final String chr = localString.isEmpty() ? "" : localString.substring(0, 1);
            final String trailing = localString.length() > 1 ? localString.substring(1) : "";
            return Underscore.invoke(Collections.singletonList(chr), methodName).get(0) + trailing;
        };
    }

    public static boolean endsWith(final String string, final String target) {
        return endsWith(string, target, null);
    }

    public static boolean endsWith(
            final String string, final String target, final Integer position) {
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
        return createCompounder(
                        (result, word, index) ->
                                result
                                        + (index > 0 ? "-" : "")
                                        + word.toLowerCase(Locale.getDefault()))
                .apply(string);
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
            localString.append(localString);
        } while (n > 0);
        return result.toString();
    }

    private static String createPadding(final String string, final int length, final String chars) {
        final int strLength = string.length();
        final int padLength = length - strLength;
        final String localChars = chars == null ? " " : chars;
        return repeat(localChars, (int) Math.ceil(padLength / (double) localChars.length()))
                .substring(0, padLength);
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

    private static Function3<String, Integer, String, String> createPadDir(
            final boolean fromRight) {
        return (string, length, chars) -> {
            final String localString = baseToString(string);
            return (fromRight ? localString : "")
                    + createPadding(localString, length, chars)
                    + (fromRight ? "" : localString);
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
        return createCompounder(
                        (result, word, index) ->
                                result
                                        + (index > 0 ? "_" : "")
                                        + word.toLowerCase(Locale.getDefault()))
                .apply(string);
    }

    public static String startCase(final String string) {
        return createCompounder(
                        (result, word, index) ->
                                result
                                        + (index > 0 ? " " : "")
                                        + word.substring(0, 1).toUpperCase(Locale.getDefault())
                                        + word.substring(1))
                .apply(string);
    }

    public static boolean startsWith(final String string, final String target) {
        return startsWith(string, target, null);
    }

    public static boolean startsWith(
            final String string, final String target, final Integer position) {
        if (string == null || target == null) {
            return false;
        }
        final String localString = baseToString(string);

        final int length = localString.length();
        final int localPosition;
        if (position == null) {
            localPosition = 0;
        } else {
            final int from = position < 0 ? 0 : position;
            localPosition = Math.min(from, length);
        }

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
        return leftIndex > -1 ? localString.substring(leftIndex) : localString;
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
        final List<String> result = new ArrayList<>();
        final java.util.regex.Matcher matcher = RE_PROP_NAME.matcher(baseToString(string));
        while (matcher.find()) {
            result.add(matcher.group(1) == null ? matcher.group(0) : matcher.group(1));
        }
        return result;
    }

    private enum OperationType {
        GET,
        SET,
        REMOVE
    }

    @SuppressWarnings("unchecked")
    private static <T> T baseGetOrSetOrRemove(
            final Map<String, Object> object,
            final String path,
            final Object value,
            OperationType operationType) {
        final List<String> paths = stringToPath(path);
        int index = 0;
        final int length = paths.size();

        Object localObject = object;
        Object savedLocalObject = null;
        String savedPath = null;
        while (localObject != null && index < length) {
            if (localObject instanceof Map) {
                Map.Entry mapEntry = getMapEntry((Map) localObject);
                if (mapEntry != null && "#item".equals(mapEntry.getKey())) {
                    localObject = mapEntry.getValue();
                    continue;
                }
                savedLocalObject = localObject;
                savedPath = paths.get(index);
                localObject = ((Map) localObject).get(paths.get(index));
            } else if (localObject instanceof List) {
                savedLocalObject = localObject;
                savedPath = paths.get(index);
                localObject = ((List) localObject).get(Integer.parseInt(paths.get(index)));
            } else {
                break;
            }
            index += 1;
        }
        if (index > 0 && index == length) {
            checkSetAndRemove(value, operationType, savedLocalObject, savedPath);
            return (T) localObject;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private static void checkSetAndRemove(
            Object value, OperationType operationType, Object savedLocalObject, String savedPath) {
        if (operationType == OperationType.SET) {
            if (savedLocalObject instanceof Map) {
                ((Map) savedLocalObject).put(savedPath, value);
            } else {
                ((List) savedLocalObject).set(Integer.parseInt(savedPath), value);
            }
        } else if (operationType == OperationType.REMOVE) {
            if (savedLocalObject instanceof Map) {
                ((Map) savedLocalObject).remove(savedPath);
            } else {
                ((List) savedLocalObject).remove(Integer.parseInt(savedPath));
            }
        }
    }

    private static Map.Entry getMapEntry(Map map) {
        return map.isEmpty() ? null : (Map.Entry) map.entrySet().iterator().next();
    }

    public static <T> T get(final Map<String, Object> object, final String path) {
        return baseGetOrSetOrRemove(object, path, null, OperationType.GET);
    }

    public static <T> T set(final Map<String, Object> object, final String path, Object value) {
        return baseGetOrSetOrRemove(object, path, value, OperationType.SET);
    }

    public static <T> T remove(final Map<String, Object> object, final String path) {
        return baseGetOrSetOrRemove(object, path, null, OperationType.REMOVE);
    }

    public static Map<String, Object> rename(
            final Map<String, Object> map, final String oldKey, final String newKey) {
        Map<String, Object> outMap = newLinkedHashMap();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getKey().equals(oldKey)) {
                outMap.put(newKey, makeObjectForRename(entry.getValue(), oldKey, newKey));
            } else {
                outMap.put(entry.getKey(), makeObjectForRename(entry.getValue(), oldKey, newKey));
            }
        }
        return outMap;
    }

    @SuppressWarnings("unchecked")
    private static Object makeObjectForRename(
            Object value, final String oldKey, final String newKey) {
        final Object result;
        if (value instanceof List) {
            List<Object> values = newArrayList();
            for (Object item : (List) value) {
                values.add(
                        item instanceof Map
                                ? rename((Map<String, Object>) item, oldKey, newKey)
                                : item);
            }
            result = values;
        } else if (value instanceof Map) {
            result = rename((Map<String, Object>) value, oldKey, newKey);
        } else {
            result = value;
        }
        return result;
    }

    public static Map<String, Object> setValue(
            final Map<String, Object> map, final String key, final Object newValue) {
        return setValue(map, key, (key1, value) -> newValue);
    }

    public static Map<String, Object> setValue(
            final Map<String, Object> map,
            final String key,
            final BiFunction<String, Object, Object> newValue) {
        Map<String, Object> outMap = newLinkedHashMap();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getKey().equals(key)) {
                outMap.put(
                        key,
                        makeObjectForSetValue(
                                newValue.apply(key, entry.getValue()), key, newValue));
            } else {
                outMap.put(entry.getKey(), makeObjectForSetValue(entry.getValue(), key, newValue));
            }
        }
        return outMap;
    }

    @SuppressWarnings("unchecked")
    private static Object makeObjectForSetValue(
            Object value, final String key, final BiFunction<String, Object, Object> newValue) {
        final Object result;
        if (value instanceof List) {
            List<Object> values = newArrayList();
            for (Object item : (List) value) {
                values.add(
                        item instanceof Map
                                ? setValue((Map<String, Object>) item, key, newValue)
                                : item);
            }
            result = values;
        } else if (value instanceof Map) {
            result = setValue((Map<String, Object>) value, key, newValue);
        } else {
            result = value;
        }
        return result;
    }

    public static Map<String, Object> update(
            final Map<String, Object> map1, final Map<String, Object> map2) {
        Map<String, Object> outMap = newLinkedHashMap();
        for (Map.Entry<String, Object> entry : map1.entrySet()) {
            String key = entry.getKey();
            Object value2 = entry.getValue();
            if (map2.containsKey(key)) {
                createKey(map2, key, value2, outMap);
            } else {
                outMap.put(key, value2);
            }
        }
        for (Map.Entry<String, Object> entry : map2.entrySet()) {
            String key = entry.getKey();
            Object value2 = entry.getValue();
            if (map1.containsKey(key)) {
                createKey(map1, key, value2, outMap);
            } else {
                outMap.put(key, value2);
            }
        }
        return outMap;
    }

    @SuppressWarnings("unchecked")
    private static void createKey(
            final Map<String, Object> map, String key, Object value2, Map<String, Object> outMap) {
        Object value1 = map.get(key);
        if (value1 instanceof Map && value2 instanceof Map) {
            outMap.put(key, update((Map<String, Object>) value1, (Map<String, Object>) value2));
        } else if (value1 instanceof List && value2 instanceof List) {
            outMap.put(key, merge((List<Object>) value1, (List<Object>) value2));
        } else if (value1 instanceof List) {
            outMap.put(key, merge((List<Object>) value1, newArrayList(value2)));
        } else if (value2 instanceof List) {
            outMap.put(key, merge(newArrayList(value1), (List<Object>) value2));
        } else {
            outMap.put(key, value2);
        }
    }

    public static List<Object> merge(List<Object> list1, List<Object> list2) {
        List<Object> outList1 = newArrayList(list1);
        List<Object> outList2 = newArrayList(list2);
        outList2.removeAll(list1);
        outList1.addAll(outList2);
        return outList1;
    }

    public static class FetchResponse {
        private final boolean ok;
        private final int status;
        private final Map<String, List<String>> headerFields;
        private final java.io.ByteArrayOutputStream stream;

        public FetchResponse(
                final boolean ok,
                final int status,
                final Map<String, List<String>> headerFields,
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
                return stream.toString(StandardCharsets.UTF_8.name());
            } catch (java.io.UnsupportedEncodingException ex) {
                throw new UnsupportedOperationException(ex);
            }
        }

        public Object json() {
            return Json.fromJson(text());
        }

        public Map<String, Object> jsonMap() {
            return fromJsonMap(text());
        }

        public Object xml() {
            return Xml.fromXml(text());
        }

        public Map<String, Object> xmlMap() {
            return fromXmlMap(text());
        }
    }

    public static long downloadUrl(final String url, final String fileName) throws IOException {
        final URL website = new URL(url);
        try (ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                final FileOutputStream fos = new FileOutputStream(fileName)) {
            return fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        }
    }

    public static void decompressGzip(final String sourceFileName, final String targetFileName)
            throws IOException {
        try (GZIPInputStream gis =
                new GZIPInputStream(new FileInputStream(new File(sourceFileName)))) {
            Files.copy(gis, Paths.get(targetFileName));
        }
    }

    public static FetchResponse fetch(final String url) {
        return fetch(url, null, null, DEFAULT_HEADER_FIELDS, null, null);
    }

    public static FetchResponse fetch(
            final String url, final Integer connectTimeout, final Integer readTimeout) {
        return fetch(url, null, null, DEFAULT_HEADER_FIELDS, connectTimeout, readTimeout);
    }

    public static FetchResponse fetch(
            final String url,
            final Integer connectTimeout,
            final Integer readTimeout,
            final Integer retryCount,
            final Integer timeBetweenRetry) {
        return Fetch.fetch(
                url,
                null,
                null,
                DEFAULT_HEADER_FIELDS,
                connectTimeout,
                readTimeout,
                retryCount,
                timeBetweenRetry);
    }

    public static FetchResponse fetch(final String url, final String method, final String body) {
        return fetch(url, method, body, DEFAULT_HEADER_FIELDS, null, null);
    }

    public static class BaseHttpSslSocketFactory extends javax.net.ssl.SSLSocketFactory {
        private javax.net.ssl.SSLContext getSslContext() {
            return createEasySslContext();
        }

        @Override
        public java.net.Socket createSocket(
                java.net.InetAddress arg0, int arg1, java.net.InetAddress arg2, int arg3)
                throws java.io.IOException {
            return getSslContext().getSocketFactory().createSocket(arg0, arg1, arg2, arg3);
        }

        @Override
        public java.net.Socket createSocket(
                String arg0, int arg1, java.net.InetAddress arg2, int arg3)
                throws java.io.IOException {
            return getSslContext().getSocketFactory().createSocket(arg0, arg1, arg2, arg3);
        }

        @Override
        public java.net.Socket createSocket(java.net.InetAddress arg0, int arg1)
                throws java.io.IOException {
            return getSslContext().getSocketFactory().createSocket(arg0, arg1);
        }

        @Override
        public java.net.Socket createSocket(String arg0, int arg1) throws java.io.IOException {
            return getSslContext().getSocketFactory().createSocket(arg0, arg1);
        }

        @Override
        public String[] getSupportedCipherSuites() {
            return new String[] {};
        }

        @Override
        public String[] getDefaultCipherSuites() {
            return new String[] {};
        }

        @Override
        public java.net.Socket createSocket(
                java.net.Socket arg0, String arg1, int arg2, boolean arg3)
                throws java.io.IOException {
            return getSslContext().getSocketFactory().createSocket(arg0, arg1, arg2, arg3);
        }

        private javax.net.ssl.SSLContext createEasySslContext() {
            try {
                javax.net.ssl.SSLContext context = javax.net.ssl.SSLContext.getInstance("SSL");
                context.init(
                        null, new javax.net.ssl.TrustManager[] {MyX509TrustManager.manger}, null);
                return context;
            } catch (Exception ex) {
                throw new UnsupportedOperationException(ex);
            }
        }

        public static class MyX509TrustManager implements javax.net.ssl.X509TrustManager {

            static MyX509TrustManager manger = new MyX509TrustManager();

            public MyX509TrustManager() {
                // ignore MyX509TrustManager
            }

            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[] {};
            }

            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] chain, String authType) {
                // ignore checkClientTrusted
            }

            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] chain, String authType) {
                // ignore checkServerTrusted
            }
        }
    }

    public static void setupConnection(
            final java.net.HttpURLConnection connection,
            final String method,
            final Map<String, List<String>> headerFields,
            final Integer connectTimeout,
            final Integer readTimeout)
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
            ((javax.net.ssl.HttpsURLConnection) connection)
                    .setSSLSocketFactory(new BaseHttpSslSocketFactory());
        }
        if (headerFields != null) {
            for (final Map.Entry<String, List<String>> header : headerFields.entrySet()) {
                connection.setRequestProperty(header.getKey(), join(header.getValue(), ";"));
            }
        }
    }

    public static FetchResponse fetch(
            final String url,
            final String method,
            final String body,
            final Map<String, List<String>> headerFields,
            final Integer connectTimeout,
            final Integer readTimeout) {
        try {
            final java.net.URL localUrl = new java.net.URL(url);
            final java.net.HttpURLConnection connection =
                    (java.net.HttpURLConnection) localUrl.openConnection();
            setupConnection(connection, method, headerFields, connectTimeout, readTimeout);
            if (body != null) {
                connection.setDoOutput(true);
                final java.io.DataOutputStream outputStream =
                        new java.io.DataOutputStream(connection.getOutputStream());
                outputStream.write(body.getBytes(StandardCharsets.UTF_8));
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
            return new FetchResponse(
                    responseCode < RESPONSE_CODE_400,
                    responseCode,
                    connection.getHeaderFields(),
                    result);
        } catch (java.io.IOException ex) {
            throw new UnsupportedOperationException(ex);
        }
    }

    public static class Fetch {
        private Fetch() {}

        @SuppressWarnings("java:S107")
        public static FetchResponse fetch(
                final String url,
                final String method,
                final String body,
                final Map<String, List<String>> headerFields,
                final Integer connectTimeout,
                final Integer readTimeout,
                final Integer retryCount,
                final Integer timeBetweenRetry) {
            if (nonNull(retryCount)
                    && retryCount > 0
                    && retryCount <= 10
                    && nonNull(timeBetweenRetry)
                    && timeBetweenRetry > 0) {
                int localRetryCount = 0;
                UnsupportedOperationException saveException;
                do {
                    try {
                        final FetchResponse fetchResponse =
                                U.fetch(
                                        url,
                                        method,
                                        body,
                                        headerFields,
                                        connectTimeout,
                                        readTimeout);
                        if (fetchResponse.getStatus() == 429) {
                            saveException = new UnsupportedOperationException("Too Many Requests");
                        } else {
                            return fetchResponse;
                        }
                    } catch (UnsupportedOperationException ex) {
                        saveException = ex;
                    }
                    localRetryCount += 1;
                    try {
                        java.util.concurrent.TimeUnit.MILLISECONDS.sleep(timeBetweenRetry);
                    } catch (InterruptedException ex) {
                        saveException = new UnsupportedOperationException(ex);
                        Thread.currentThread().interrupt();
                    }
                } while (localRetryCount <= retryCount);
                throw saveException;
            }
            return U.fetch(url, method, body, headerFields, connectTimeout, readTimeout);
        }
    }

    public static List<String> explode(final String input) {
        List<String> result = newArrayList();
        if (isNull(input)) {
            return result;
        }
        for (char character : input.toCharArray()) {
            result.add(String.valueOf(character));
        }
        return result;
    }

    public static String implode(final String[] input) {
        StringBuilder builder = new StringBuilder();
        for (String character : input) {
            if (nonNull(character)) {
                builder.append(character);
            }
        }
        return builder.toString();
    }

    public static String implode(final Iterable<String> input) {
        StringBuilder builder = new StringBuilder();
        for (String character : input) {
            if (nonNull(character)) {
                builder.append(character);
            }
        }
        return builder.toString();
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

    public static class LruCache<K, V> {
        private static final boolean SORT_BY_ACCESS = true;
        private static final float LOAD_FACTOR = 0.75F;
        private final Map<K, V> lruCacheMap;
        private final int capacity;

        public LruCache(int capacity) {
            this.capacity = capacity;
            this.lruCacheMap = new LinkedHashMap<>(capacity, LOAD_FACTOR, SORT_BY_ACCESS);
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

    public static <K, V> LruCache<K, V> createLruCache(final int capacity) {
        return new LruCache<>(capacity);
    }

    public static <T> List<List<T>> createPermutationWithRepetition(
            final List<T> list, final int permutationLength) {
        final long resultSize = (long) Math.pow(list.size(), permutationLength);
        final List<List<T>> result = new ArrayList<>((int) resultSize);
        final int[] bitVector = new int[permutationLength];
        for (int index = 0; index < resultSize; index += 1) {
            List<T> result2 = new ArrayList<>(permutationLength);
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

    public List<List<T>> createPermutationWithRepetition(final int permutationLength) {
        return createPermutationWithRepetition((List<T>) value(), permutationLength);
    }

    protected static <T> List<T> newArrayList() {
        return Underscore.newArrayList();
    }

    protected static <T> List<T> newArrayList(final Iterable<T> iterable) {
        return Underscore.newArrayList(iterable);
    }

    protected static <T> Set<T> newLinkedHashSet() {
        return Underscore.newLinkedHashSet();
    }

    protected static <K, E> Map<K, E> newLinkedHashMap() {
        return Underscore.newLinkedHashMap();
    }

    public static <T> String join(final Iterable<T> iterable, final String separator) {
        return Underscore.join(iterable, separator);
    }

    public static String toJson(Collection collection) {
        return Json.toJson(collection);
    }

    public static String toJson(Map map) {
        return Json.toJson(map);
    }

    public String toJson() {
        return Json.toJson((Collection) getIterable());
    }

    public static String toJsonJavaString(Collection collection) {
        return Json.toJsonJavaString(collection);
    }

    public static String toJsonJavaString(Map map) {
        return Json.toJsonJavaString(map);
    }

    public String toJsonJavaString() {
        return Json.toJsonJavaString((Collection) getIterable());
    }

    @SuppressWarnings("unchecked")
    public static <T> T fromXml(final String xml) {
        return (T) Xml.fromXml(xml);
    }

    public static Map<String, Object> fromXmlMap(final String xml) {
        return fromXmlMap(xml, Xml.FromType.FOR_CONVERT);
    }

    public static Map<String, Object> fromXmlMap(final String xml, final Xml.FromType fromType) {
        final Object object = Xml.fromXml(xml, fromType);
        return getStringObjectMap(object);
    }

    @SuppressWarnings("unchecked")
    public static <T> T fromXml(final String xml, final Xml.FromType fromType) {
        return (T) Xml.fromXml(xml, fromType);
    }

    @SuppressWarnings("unchecked")
    public static <T> T fromXmlMakeArrays(final String xml) {
        return (T) Xml.fromXmlMakeArrays(xml);
    }

    @SuppressWarnings("unchecked")
    public static <T> T fromXmlWithoutNamespaces(final String xml) {
        return (T) Xml.fromXmlWithoutNamespaces(xml);
    }

    @SuppressWarnings("unchecked")
    public static <T> T fromXmlWithoutAttributes(final String xml) {
        return (T) Xml.fromXmlWithoutAttributes(xml);
    }

    @SuppressWarnings("unchecked")
    public static <T> T fromXmlWithoutNamespacesAndAttributes(final String xml) {
        return (T) Xml.fromXmlWithoutNamespacesAndAttributes(xml);
    }

    public static String toXml(Collection collection) {
        return Xml.toXml(collection);
    }

    public static String toXml(Map map) {
        return Xml.toXml(map);
    }

    @SuppressWarnings("unchecked")
    public static <T> T fromJson(String string) {
        return (T) Json.fromJson(string);
    }

    public Object fromJson() {
        return Json.fromJson(getString().get());
    }

    public static Map<String, Object> fromJsonMap(final String string) {
        final Object object = Json.fromJson(string);
        return getStringObjectMap(object);
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> getStringObjectMap(Object object) {
        final Map<String, Object> result;
        if (object instanceof Map) {
            result = (Map<String, Object>) object;
        } else {
            result = newLinkedHashMap();
            result.put("value", object);
        }
        return result;
    }

    public String toXml() {
        return Xml.toXml((Collection) getIterable());
    }

    public Object fromXml() {
        return Xml.fromXml(getString().get());
    }

    @SuppressWarnings("unchecked")
    public static String jsonToXml(
            String json, Xml.XmlStringBuilder.Step identStep, Mode mode, String newRootName) {
        Object object = Json.fromJson(json);
        final String result;
        if (object instanceof Map) {
            if (mode == Mode.FORCE_ATTRIBUTE_USAGE) {
                result = Xml.toXml(forceAttributeUsage((Map) object), identStep, newRootName);
            } else if (mode == Mode.DEFINE_ROOT_NAME) {
                result = Xml.toXml((Map) object, identStep, newRootName);
            } else if (mode == Mode.REPLACE_NULL_WITH_EMPTY_VALUE) {
                result = Xml.toXml(replaceNullWithEmptyValue((Map) object), identStep, newRootName);
            } else if (mode == Mode.REPLACE_EMPTY_STRING_WITH_EMPTY_VALUE) {
                result =
                        Xml.toXml(
                                replaceEmptyStringWithEmptyValue((Map) object),
                                identStep,
                                newRootName);
            } else if (mode == Mode.FORCE_ADD_ROOT_JSON_TO_XML
                    && !Xml.XmlValue.getMapKey(object).equals(ROOT)) {
                final Map<String, Object> map = U.newLinkedHashMap();
                map.put(newRootName, object);
                result = Xml.toXml(map, identStep);
            } else if (mode == Mode.FORCE_REMOVE_ARRAY_ATTRIBUTE_JSON_TO_XML) {
                result = Xml.toXml((Map) object, identStep, newRootName, Xml.ArrayTrue.SKIP);
            } else if (mode == Mode.FORCE_REMOVE_ARRAY_BOOLEAN_NUMBER_ATTRIBUTES_JSON_TO_XML) {
                result =
                        Xml.toXml(
                                replaceNumberAndBooleanWithString((Map) object),
                                identStep,
                                newRootName,
                                Xml.ArrayTrue.SKIP);
            } else {
                result = Xml.toXml((Map) object, identStep);
            }
            return result;
        }
        return Xml.toXml((List) object, identStep);
    }

    public static String jsonToXml(String json, Xml.XmlStringBuilder.Step identStep) {
        return jsonToXml(json, identStep, null, ROOT);
    }

    public static String jsonToXml(String json, Mode mode) {
        return jsonToXml(json, Xml.XmlStringBuilder.Step.TWO_SPACES, mode, ROOT);
    }

    public static String jsonToXml(String json, Mode mode, String newRootName) {
        return jsonToXml(json, Xml.XmlStringBuilder.Step.TWO_SPACES, mode, newRootName);
    }

    public static String jsonToXml(String json, String newRootName) {
        return jsonToXml(
                json, Xml.XmlStringBuilder.Step.TWO_SPACES, Mode.DEFINE_ROOT_NAME, newRootName);
    }

    public static String jsonToXml(String json) {
        return jsonToXml(json, Xml.XmlStringBuilder.Step.TWO_SPACES, null, null);
    }

    @SuppressWarnings("unchecked")
    public static String xmlToJson(String xml, Json.JsonStringBuilder.Step identStep, Mode mode) {
        Object object = Xml.fromXml(xml);
        final String result;
        if (object instanceof Map) {
            if (mode == Mode.REPLACE_SELF_CLOSING_WITH_NULL) {
                result = Json.toJson(replaceSelfClosingWithNull((Map) object), identStep);
            } else if (mode == Mode.REPLACE_SELF_CLOSING_WITH_EMPTY) {
                result = Json.toJson(replaceSelfClosingWithEmpty((Map) object), identStep);
            } else if (mode == Mode.REPLACE_EMPTY_VALUE_WITH_NULL) {
                result = Json.toJson(replaceEmptyValueWithNull((Map) object), identStep);
            } else if (mode == Mode.REPLACE_EMPTY_TAG_WITH_NULL) {
                result =
                        Json.toJson(
                                replaceEmptyValueWithNull(replaceSelfClosingWithNull((Map) object)),
                                identStep);
            } else if (mode == Mode.REPLACE_EMPTY_TAG_WITH_EMPTY_STRING) {
                result =
                        Json.toJson(
                                (Map<String, Object>)
                                        replaceEmptyValueWithEmptyString(
                                                replaceSelfClosingWithEmpty((Map) object)),
                                identStep);
            } else if (mode == Mode.REMOVE_FIRST_LEVEL_XML_TO_JSON) {
                result = Json.toJson(replaceFirstLevel((Map) object), identStep);
            } else {
                result = Json.toJson((Map) object, identStep);
            }
            return result;
        }
        return Json.toJson((List) object, identStep);
    }

    public static String xmlToJson(String xml) {
        return xmlToJson(xml, Json.JsonStringBuilder.Step.TWO_SPACES, null);
    }

    public static String xmlToJson(String xml, Json.JsonStringBuilder.Step identStep) {
        return xmlToJson(xml, identStep, null);
    }

    public static String xmlToJson(String xml, Mode mode) {
        return xmlToJson(xml, Json.JsonStringBuilder.Step.TWO_SPACES, mode);
    }

    public static String formatJson(String json, Json.JsonStringBuilder.Step identStep) {
        return Json.formatJson(json, identStep);
    }

    public static String formatJson(String json) {
        return Json.formatJson(json);
    }

    public static String formatXml(String xml, Xml.XmlStringBuilder.Step identStep) {
        return Xml.formatXml(xml, identStep);
    }

    public static String formatXml(String xml) {
        return Xml.formatXml(xml);
    }

    public static String changeXmlEncoding(
            String xml, Xml.XmlStringBuilder.Step identStep, String encoding) {
        return Xml.changeXmlEncoding(xml, identStep, encoding);
    }

    public static String changeXmlEncoding(String xml, String encoding) {
        return Xml.changeXmlEncoding(xml, encoding);
    }

    public static Map<String, Object> removeMinusesAndConvertNumbers(Map<String, Object> map) {
        Map<String, Object> outMap = newLinkedHashMap();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            final String newKey;
            if (entry.getKey().startsWith("-")) {
                newKey = entry.getKey().substring(1);
            } else {
                newKey = entry.getKey();
            }
            if (!entry.getKey().equals(selfClosing)
                    && !entry.getKey().equals("#omit-xml-declaration")) {
                outMap.put(newKey, makeObject(entry.getValue()));
            }
        }
        return outMap;
    }

    @SuppressWarnings("unchecked")
    private static Object makeObject(Object value) {
        final Object result;
        if (value instanceof List) {
            List<Object> values = newArrayList();
            for (Object item : (List) value) {
                values.add(
                        item instanceof Map
                                ? removeMinusesAndConvertNumbers((Map<String, Object>) item)
                                : item);
            }
            result = values;
        } else if (value instanceof Map) {
            result = removeMinusesAndConvertNumbers((Map) value);
        } else {
            String stringValue = String.valueOf(value);
            result = isJsonNumber(stringValue) ? Xml.stringToNumber(stringValue) : value;
        }
        return result;
    }

    public static boolean isJsonNumber(final String string) {
        boolean eFound = false;
        boolean periodValid = true;
        boolean pmValid = true;
        boolean numberEncountered = false;
        for (char ch : string.toCharArray()) {
            if (pmValid) {
                pmValid = false;
                if (ch == '-') {
                    continue;
                }
            }
            if (!eFound && (ch == 'e' || ch == 'E')) {
                eFound = true;
                periodValid = false;
                pmValid = true;
                numberEncountered = false;
                continue;
            }
            if (periodValid && ch == '.') {
                periodValid = false;
                continue;
            }
            if (ch < '0' || ch > '9') {
                return false;
            }
            numberEncountered = true;
        }
        return numberEncountered;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> replaceSelfClosingWithNull(Map<String, Object> map) {
        return (Map<String, Object>) replaceSelfClosingWithValue(map, null);
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> replaceSelfClosingWithEmpty(Map<String, Object> map) {
        return (Map<String, Object>) replaceSelfClosingWithValue(map, "");
    }

    @SuppressWarnings("unchecked")
    public static Object replaceSelfClosingWithValue(Map<String, Object> map, String value) {
        Object outMap = newLinkedHashMap();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (selfClosing.equals(entry.getKey()) && "true".equals(entry.getValue())) {
                if (map.size() == 1) {
                    outMap = value;
                    break;
                }
            } else {
                ((Map<String, Object>) outMap)
                        .put(
                                String.valueOf(entry.getKey()),
                                makeObjectSelfClose(entry.getValue(), value));
            }
        }
        return outMap;
    }

    @SuppressWarnings("unchecked")
    private static Object makeObjectSelfClose(Object value, String newValue) {
        final Object result;
        if (value instanceof List) {
            List<Object> values = newArrayList();
            for (Object item : (List) value) {
                values.add(
                        item instanceof Map
                                ? replaceSelfClosingWithValue((Map) item, newValue)
                                : item);
            }
            result = values;
        } else if (value instanceof Map) {
            result = replaceSelfClosingWithValue((Map) value, newValue);
        } else {
            result = value;
        }
        return result;
    }

    public static Map<String, Object> replaceEmptyValueWithNull(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, Object> outMap = newLinkedHashMap();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            outMap.put(String.valueOf(entry.getKey()), makeObjectEmptyValue(entry.getValue()));
        }
        return outMap;
    }

    @SuppressWarnings("unchecked")
    private static Object makeObjectEmptyValue(Object value) {
        final Object result;
        if (value instanceof List) {
            List<Object> values = newArrayList();
            for (Object item : (List) value) {
                values.add(item instanceof Map ? replaceEmptyValueWithNull((Map) item) : item);
            }
            result = values;
        } else if (value instanceof Map) {
            result = replaceEmptyValueWithNull((Map) value);
        } else {
            result = value;
        }
        return result;
    }

    public static Object replaceEmptyValueWithEmptyString(Map<String, Object> map) {
        if (map.isEmpty()) {
            return "";
        }
        Map<String, Object> outMap = newLinkedHashMap();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            outMap.put(String.valueOf(entry.getKey()), makeObjectEmptyString(entry.getValue()));
        }
        return outMap;
    }

    @SuppressWarnings("unchecked")
    private static Object makeObjectEmptyString(Object value) {
        final Object result;
        if (value instanceof List) {
            List<Object> values = newArrayList();
            for (Object item : (List) value) {
                values.add(
                        item instanceof Map ? replaceEmptyValueWithEmptyString((Map) item) : item);
            }
            result = values;
        } else if (value instanceof Map) {
            result = replaceEmptyValueWithEmptyString((Map) value);
        } else {
            result = value;
        }
        return result;
    }

    public static Map<String, Object> forceAttributeUsage(Map<String, Object> map) {
        Map<String, Object> outMap = newLinkedHashMap();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            outMap.put(
                    entry.getValue() instanceof Map
                                    || entry.getValue() instanceof List
                                    || String.valueOf(entry.getKey()).startsWith("-")
                            ? String.valueOf(entry.getKey())
                            : "-" + entry.getKey(),
                    makeAttributeUsage(entry.getValue()));
        }
        return outMap;
    }

    @SuppressWarnings("unchecked")
    private static Object makeAttributeUsage(Object value) {
        final Object result;
        if (value instanceof List) {
            List<Object> values = newArrayList();
            for (Object item : (List) value) {
                values.add(item instanceof Map ? forceAttributeUsage((Map) item) : item);
            }
            result = values;
        } else if (value instanceof Map) {
            result = forceAttributeUsage((Map) value);
        } else {
            result = value;
        }
        return result;
    }

    public static Map<String, Object> replaceNullWithEmptyValue(Map<String, Object> map) {
        Map<String, Object> outMap = newLinkedHashMap();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            outMap.put(
                    entry.getKey(),
                    entry.getValue() == null
                            ? newLinkedHashMap()
                            : makeReplaceNullValue(entry.getValue()));
        }
        return outMap;
    }

    @SuppressWarnings("unchecked")
    private static Object makeReplaceNullValue(Object value) {
        final Object result;
        if (value instanceof List) {
            List<Object> values = newArrayList();
            for (Object item : (List) value) {
                values.add(item instanceof Map ? replaceNullWithEmptyValue((Map) item) : item);
            }
            result = values;
        } else if (value instanceof Map) {
            result = replaceNullWithEmptyValue((Map) value);
        } else {
            result = value;
        }
        return result;
    }

    public static Map<String, Object> replaceEmptyStringWithEmptyValue(Map<String, Object> map) {
        Map<String, Object> outMap = newLinkedHashMap();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            outMap.put(
                    entry.getKey(),
                    "".equals(entry.getValue())
                            ? newLinkedHashMap()
                            : makeReplaceEmptyString(entry.getValue()));
        }
        return outMap;
    }

    @SuppressWarnings("unchecked")
    private static Object makeReplaceEmptyString(Object value) {
        final Object result;
        if (value instanceof List) {
            List<Object> values = newArrayList();
            for (Object item : (List) value) {
                values.add(
                        item instanceof Map ? replaceEmptyStringWithEmptyValue((Map) item) : item);
            }
            result = values;
        } else if (value instanceof Map) {
            result = replaceEmptyStringWithEmptyValue((Map) value);
        } else {
            result = value;
        }
        return result;
    }

    public static Map<String, Object> replaceNumberAndBooleanWithString(Map<String, Object> map) {
        Map<String, Object> outMap = newLinkedHashMap();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            outMap.put(
                    entry.getKey(),
                    entry.getValue() instanceof Boolean || entry.getValue() instanceof Number
                            ? String.valueOf(entry.getValue())
                            : makeReplaceNumberAndBoolean(entry.getValue()));
        }
        return outMap;
    }

    @SuppressWarnings("unchecked")
    private static Object makeReplaceNumberAndBoolean(Object value) {
        final Object result;
        if (value instanceof List) {
            List<Object> values = newArrayList();
            for (Object item : (List) value) {
                values.add(
                        item instanceof Map ? replaceNumberAndBooleanWithString((Map) item) : item);
            }
            result = values;
        } else if (value instanceof Map) {
            result = replaceNumberAndBooleanWithString((Map) value);
        } else {
            result = value;
        }
        return result;
    }

    public static Map<String, Object> replaceFirstLevel(Map<String, Object> map) {
        return replaceFirstLevel(map, 0);
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> replaceFirstLevel(Map<String, Object> map, int level) {
        Map<String, Object> outMap = newLinkedHashMap();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            outMap.put(entry.getKey(), makeReplaceFirstLevel(entry.getValue(), level + 1));
        }
        if (level == 0 && Xml.XmlValue.getMapValue(outMap) instanceof Map) {
            Map<String, Object> outMap2 = (Map<String, Object>) Xml.XmlValue.getMapValue(outMap);
            if (selfClosing.equals(Xml.XmlValue.getMapKey(outMap2))
                    && "true".equals(Xml.XmlValue.getMapValue(outMap2))) {
                outMap2.remove(selfClosing);
            }
            return outMap2;
        }
        return outMap;
    }

    @SuppressWarnings("unchecked")
    private static Object makeReplaceFirstLevel(Object value, int level) {
        final Object result;
        if (value instanceof List) {
            List<Object> values = newArrayList();
            for (Object item : (List) value) {
                values.add(item instanceof Map ? replaceFirstLevel((Map) item, level + 1) : item);
            }
            result = values;
        } else if (value instanceof Map) {
            result = replaceFirstLevel((Map) value, level + 1);
        } else {
            result = value;
        }
        return result;
    }

    public static Map<String, Object> replaceNilWithNull(Map<String, Object> map) {
        Map<String, Object> outMap = newLinkedHashMap();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object outValue = makeReplaceNilWithNull(entry.getValue());
            if (outValue instanceof Map
                    && (nilKey.equals(Xml.XmlValue.getMapKey(outValue))
                            || Xml.XmlValue.getMapKey(outValue).endsWith(nilKeyNs))
                    && "true".equals(Xml.XmlValue.getMapValue(outValue))
                    && ((Map) outValue).containsKey(selfClosing)
                    && "true".equals(((Map) outValue).get(selfClosing))) {
                outValue = null;
            }
            outMap.put(entry.getKey(), outValue);
        }
        return outMap;
    }

    @SuppressWarnings("unchecked")
    private static Object makeReplaceNilWithNull(Object value) {
        final Object result;
        if (value instanceof List) {
            List<Object> values = newArrayList();
            for (Object item : (List) value) {
                values.add(item instanceof Map ? replaceNilWithNull((Map) item) : item);
            }
            result = values;
        } else if (value instanceof Map) {
            result = replaceNilWithNull((Map) value);
        } else {
            result = value;
        }
        return result;
    }

    public static Map<String, Object> deepCopyMap(Map<String, Object> map) {
        Map<String, Object> outMap = newLinkedHashMap();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            outMap.put(entry.getKey(), makeDeepCopyMap(entry.getValue()));
        }
        return outMap;
    }

    @SuppressWarnings("unchecked")
    private static Object makeDeepCopyMap(Object value) {
        final Object result;
        if (value instanceof List) {
            List<Object> values = newArrayList();
            for (Object item : (List) value) {
                values.add(item instanceof Map ? deepCopyMap((Map) item) : item);
            }
            result = values;
        } else if (value instanceof Map) {
            result = deepCopyMap((Map) value);
        } else {
            result = value;
        }
        return result;
    }

    public static long gcd(long value1, long value2) {
        if (value1 == 0) {
            return value2;
        }
        return gcd(value2 % value1, value1);
    }

    public static long findGcd(long... array) {
        long result = array[0];
        for (int index = 1; index < array.length; index += 1) {
            result = gcd(array[index], result);
        }
        return result;
    }

    public static Builder objectBuilder() {
        return new U.Builder();
    }

    public static class Builder {
        private final Map<String, Object> data;

        public Builder() {
            data = newLinkedHashMap();
        }

        public Builder add(final String key, final Object value) {
            data.put(key, value);
            return this;
        }

        public Builder add(final Object value) {
            data.put(String.valueOf(data.size()), value);
            return this;
        }

        public Builder set(final String path, final Object value) {
            U.set(data, path, value);
            return this;
        }

        public Builder remove(final String key) {
            U.remove(data, key);
            return this;
        }

        public Builder clear() {
            data.clear();
            return this;
        }

        public Builder add(final Builder builder) {
            data.put(String.valueOf(data.size()), builder.build());
            return this;
        }

        public Builder add(final String key, final ArrayBuilder builder) {
            data.put(key, builder.build());
            return this;
        }

        public Builder add(final String key, final Builder builder) {
            data.put(key, builder.build());
            return this;
        }

        public Builder add(final Map<String, Object> map) {
            deepCopyMap(map).forEach(data::put);
            return this;
        }

        public Builder addNull(final String key) {
            data.put(key, null);
            return this;
        }

        @SuppressWarnings("unchecked")
        public Map<String, Object> build() {
            return (Map<String, Object>) ((LinkedHashMap) data).clone();
        }

        public String toXml() {
            return Xml.toXml(data);
        }

        public static Builder fromXml(final String xml) {
            final Builder builder = new Builder();
            builder.data.putAll(fromXmlMap(xml));
            return builder;
        }

        public static Builder fromMap(final Map<String, Object> map) {
            final Builder builder = new Builder();
            deepCopyMap(map).forEach(builder.data::put);
            return builder;
        }

        public String toJson() {
            return Json.toJson(data);
        }

        public static Builder fromJson(final String json) {
            final Builder builder = new Builder();
            builder.data.putAll(fromJsonMap(json));
            return builder;
        }

        public Chain<Object> toChain() {
            return new U.Chain<>(data.entrySet());
        }

        @Override
        public String toString() {
            return data.toString();
        }
    }

    public static ArrayBuilder arrayBuilder() {
        return new U.ArrayBuilder();
    }

    public static class ArrayBuilder {
        private final List<Object> data;

        public ArrayBuilder() {
            data = newArrayList();
        }

        public ArrayBuilder add(final Object value) {
            data.add(value);
            return this;
        }

        public ArrayBuilder addNull() {
            data.add(null);
            return this;
        }

        public ArrayBuilder set(final int index, final Object value) {
            data.set(index, value);
            return this;
        }

        public ArrayBuilder remove(final int index) {
            data.remove(index);
            return this;
        }

        public ArrayBuilder clear() {
            data.clear();
            return this;
        }

        public ArrayBuilder add(final ArrayBuilder builder) {
            data.addAll(builder.build());
            return this;
        }

        public ArrayBuilder add(final Builder builder) {
            data.add(builder.build());
            return this;
        }

        @SuppressWarnings("unchecked")
        public List<Object> build() {
            return (List<Object>) ((ArrayList) data).clone();
        }

        public String toXml() {
            return Xml.toXml(data);
        }

        public static ArrayBuilder fromXml(final String xml) {
            final ArrayBuilder builder = new ArrayBuilder();
            builder.data.addAll(U.<List<Object>>fromXml(xml));
            return builder;
        }

        public String toJson() {
            return Json.toJson(data);
        }

        public static ArrayBuilder fromJson(final String json) {
            final ArrayBuilder builder = new ArrayBuilder();
            builder.data.addAll(U.<List<Object>>fromJson(json));
            return builder;
        }

        public Chain<Object> toChain() {
            return new U.Chain<>(data);
        }

        @Override
        public String toString() {
            return data.toString();
        }
    }
}
