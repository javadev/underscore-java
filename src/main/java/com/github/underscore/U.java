/*
 * The MIT License (MIT)
 *
 * Copyright 2015-2024 Valentyn Kolesnikov
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
import java.net.URI;
import java.net.URISyntaxException;
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
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.zip.GZIPInputStream;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.NodeList;

/**
 * The type U.
 *
 * @param <T> the type parameter
 */
@SuppressWarnings({
    "java:S135",
    "java:S1168",
    "java:S3655",
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

    /** The enum Xml to json mode. */
    public enum XmlToJsonMode {
        /** Replace self closing with null xml to json mode. */
        REPLACE_SELF_CLOSING_WITH_NULL,
        /** Replace self closing with string xml to json mode. */
        REPLACE_SELF_CLOSING_WITH_STRING,
        /** Replace empty value with null xml to json mode. */
        REPLACE_EMPTY_VALUE_WITH_NULL,
        /** Replace empty tag with null xml to json mode. */
        REPLACE_EMPTY_TAG_WITH_NULL,
        /** Replace empty tag with string xml to json mode. */
        REPLACE_EMPTY_TAG_WITH_STRING,
        /** Remove first level xml to json mode. */
        REMOVE_FIRST_LEVEL,
        /** Without namespaces xml to json mode. */
        WITHOUT_NAMESPACES
    }

    /** The enum Json to xml mode. */
    public enum JsonToXmlMode {
        /** Force attribute usage json to xml mode. */
        FORCE_ATTRIBUTE_USAGE,
        /** Define root name json to xml mode. */
        DEFINE_ROOT_NAME,
        /** Replace null with empty value json to xml mode. */
        REPLACE_NULL_WITH_EMPTY_VALUE,
        /** Replace empty string with empty value json to xml mode. */
        REPLACE_EMPTY_STRING_WITH_EMPTY_VALUE,
        /** Add root json to xml mode. */
        ADD_ROOT,
        /** Remove array attribute json to xml mode. */
        REMOVE_ARRAY_ATTRIBUTE,
        /** Remove attributes json to xml mode. */
        REMOVE_ATTRIBUTES
    }

    /**
     * Instantiates a new U.
     *
     * @param iterable the iterable
     */
    public U(final Iterable<T> iterable) {
        super(iterable);
    }

    /**
     * Instantiates a new U.
     *
     * @param string the string
     */
    public U(final String string) {
        super(string);
    }

    /**
     * The type Chain.
     *
     * @param <T> the type parameter
     */
    public static class Chain<T> extends Underscore.Chain<T> {
        /**
         * Instantiates a new Chain.
         *
         * @param item the item
         */
        public Chain(final T item) {
            super(item);
        }

        /**
         * Instantiates a new Chain.
         *
         * @param list the list
         */
        public Chain(final List<T> list) {
            super(list);
        }

        /**
         * Instantiates a new Chain.
         *
         * @param map the map
         */
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
        public <F> Chain<Map<F, T>> associateBy(final Function<T, F> func) {
            return new Chain<>(Underscore.associateBy(value(), func));
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
        public <E> Chain<T> where(final List<Map.Entry<String, E>> properties) {
            return new Chain<>(Underscore.where(value(), properties));
        }

        @Override
        public <E> Chain<Optional<T>> findWhere(final List<Map.Entry<String, E>> properties) {
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

        /**
         * Set chain.
         *
         * @param path the path
         * @param value the value
         * @return the chain
         */
        public Chain<Map<String, Object>> set(final String path, Object value) {
            U.set(map(), path, value);
            return new Chain<>(map());
        }

        /**
         * Set chain.
         *
         * @param paths the paths
         * @param value the value
         * @return the chain
         */
        public Chain<Map<String, Object>> set(final List<String> paths, Object value) {
            U.set(map(), paths, value);
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

        /**
         * Drop chain.
         *
         * @return the chain
         */
        public Chain<T> drop() {
            return new Chain<>(Underscore.drop(value()));
        }

        /**
         * Drop chain.
         *
         * @param n the n
         * @return the chain
         */
        public Chain<T> drop(final Integer n) {
            return new Chain<>(Underscore.drop(value(), n));
        }

        /**
         * Drop right chain.
         *
         * @return the chain
         */
        public Chain<T> dropRight() {
            return new Chain<>(U.dropRight(value()));
        }

        /**
         * Drop right chain.
         *
         * @param n the n
         * @return the chain
         */
        public Chain<T> dropRight(final Integer n) {
            return new Chain<>(U.dropRight(value(), n));
        }

        /**
         * Drop while chain.
         *
         * @param pred the pred
         * @return the chain
         */
        public Chain<T> dropWhile(final Predicate<T> pred) {
            return new Chain<>(U.dropWhile(value(), pred));
        }

        /**
         * Drop right while chain.
         *
         * @param pred the pred
         * @return the chain
         */
        public Chain<T> dropRightWhile(final Predicate<T> pred) {
            return new Chain<>(U.dropRightWhile(value(), pred));
        }

        /**
         * Fill chain.
         *
         * @param value the value
         * @return the chain
         */
        @SuppressWarnings("unchecked")
        public Chain<Object> fill(final Object value) {
            return new Chain<>(U.fill((List<Object>) value(), value));
        }

        /**
         * Fill chain.
         *
         * @param value the value
         * @param start the start
         * @param end the end
         * @return the chain
         */
        @SuppressWarnings("unchecked")
        public Chain<Object> fill(final Object value, final Integer start, final Integer end) {
            return new Chain<>(U.fill((List<Object>) value(), value, start, end));
        }

        /**
         * Flatten deep chain.
         *
         * @return the chain
         */
        public Chain<Object> flattenDeep() {
            return new Chain<>(U.flattenDeep(value()));
        }

        /**
         * Pull chain.
         *
         * @param values the values
         * @return the chain
         */
        @SuppressWarnings("unchecked")
        public Chain<Object> pull(final Object... values) {
            return new Chain<>(U.pull((List<Object>) value(), values));
        }

        /**
         * Pull at chain.
         *
         * @param indexes the indexes
         * @return the chain
         */
        @SuppressWarnings("unchecked")
        public Chain<Object> pullAt(final Integer... indexes) {
            return new Chain<>(U.pullAt((List<Object>) value(), indexes));
        }

        /**
         * Remove chain.
         *
         * @param pred the pred
         * @return the chain
         */
        public Chain<T> remove(final Predicate<T> pred) {
            return new Chain<>(U.remove(value(), pred));
        }

        /**
         * Take chain.
         *
         * @return the chain
         */
        public Chain<T> take() {
            return new Chain<>(U.take(value()));
        }

        /**
         * Take right chain.
         *
         * @return the chain
         */
        public Chain<T> takeRight() {
            return new Chain<>(U.takeRight(value()));
        }

        /**
         * Take chain.
         *
         * @param n the n
         * @return the chain
         */
        public Chain<T> take(final Integer n) {
            return new Chain<>(U.take(value(), n));
        }

        /**
         * Take right chain.
         *
         * @param n the n
         * @return the chain
         */
        public Chain<T> takeRight(final Integer n) {
            return new Chain<>(U.takeRight(value(), n));
        }

        /**
         * Take while chain.
         *
         * @param pred the pred
         * @return the chain
         */
        public Chain<T> takeWhile(final Predicate<T> pred) {
            return new Chain<>(U.takeWhile(value(), pred));
        }

        /**
         * Take right while chain.
         *
         * @param pred the pred
         * @return the chain
         */
        public Chain<T> takeRightWhile(final Predicate<T> pred) {
            return new Chain<>(U.takeRightWhile(value(), pred));
        }

        /**
         * Xor chain.
         *
         * @param list the list
         * @return the chain
         */
        @SuppressWarnings("unchecked")
        public Chain<T> xor(final List<T> list) {
            return new Chain<>(U.xor(value(), list));
        }

        /**
         * At chain.
         *
         * @param indexes the indexes
         * @return the chain
         */
        public Chain<T> at(final Integer... indexes) {
            return new Chain<>(U.at(value(), indexes));
        }

        /**
         * Sum chain.
         *
         * @param <F> the type parameter
         * @return the chain
         */
        @SuppressWarnings("unchecked")
        public <F extends Number> Chain<F> sum() {
            return new Chain<>(U.sum((List<F>) value()));
        }

        /**
         * Sum chain.
         *
         * @param <F> the type parameter
         * @param func the func
         * @return the chain
         */
        public <F extends Number> Chain<F> sum(final Function<T, F> func) {
            return new Chain<>(U.sum(value(), func));
        }

        /**
         * Mean chain.
         *
         * @return the chain
         */
        @SuppressWarnings("unchecked")
        public Chain<Double> mean() {
            return new Chain<>(U.mean((List<Number>) value()));
        }

        /**
         * Median chain.
         *
         * @return the chain
         */
        @SuppressWarnings("unchecked")
        public Chain<Double> median() {
            return new Chain<>(U.median((List<Number>) value()));
        }

        /**
         * Camel case chain.
         *
         * @return the chain
         */
        public Chain<String> camelCase() {
            return new Chain<>(U.camelCase((String) item()));
        }

        /**
         * Lower first chain.
         *
         * @return the chain
         */
        public Chain<String> lowerFirst() {
            return new Chain<>(U.lowerFirst((String) item()));
        }

        /**
         * Upper first chain.
         *
         * @return the chain
         */
        public Chain<String> upperFirst() {
            return new Chain<>(U.upperFirst((String) item()));
        }

        /**
         * Capitalize chain.
         *
         * @return the chain
         */
        public Chain<String> capitalize() {
            return new Chain<>(U.capitalize((String) item()));
        }

        /**
         * Deburr chain.
         *
         * @return the chain
         */
        public Chain<String> deburr() {
            return new Chain<>(U.deburr((String) item()));
        }

        /**
         * Ends with chain.
         *
         * @param target the target
         * @return the chain
         */
        public Chain<Boolean> endsWith(final String target) {
            return new Chain<>(U.endsWith((String) item(), target));
        }

        /**
         * Ends with chain.
         *
         * @param target the target
         * @param position the position
         * @return the chain
         */
        public Chain<Boolean> endsWith(final String target, final Integer position) {
            return new Chain<>(U.endsWith((String) item(), target, position));
        }

        /**
         * Kebab case chain.
         *
         * @return the chain
         */
        public Chain<String> kebabCase() {
            return new Chain<>(U.kebabCase((String) item()));
        }

        /**
         * Repeat chain.
         *
         * @param length the length
         * @return the chain
         */
        public Chain<String> repeat(final int length) {
            return new Chain<>(U.repeat((String) item(), length));
        }

        /**
         * Pad chain.
         *
         * @param length the length
         * @return the chain
         */
        public Chain<String> pad(final int length) {
            return new Chain<>(U.pad((String) item(), length));
        }

        /**
         * Pad chain.
         *
         * @param length the length
         * @param chars the chars
         * @return the chain
         */
        public Chain<String> pad(final int length, final String chars) {
            return new Chain<>(U.pad((String) item(), length, chars));
        }

        /**
         * Pad start chain.
         *
         * @param length the length
         * @return the chain
         */
        public Chain<String> padStart(final int length) {
            return new Chain<>(U.padStart((String) item(), length));
        }

        /**
         * Pad start chain.
         *
         * @param length the length
         * @param chars the chars
         * @return the chain
         */
        public Chain<String> padStart(final int length, final String chars) {
            return new Chain<>(U.padStart((String) item(), length, chars));
        }

        /**
         * Pad end chain.
         *
         * @param length the length
         * @return the chain
         */
        public Chain<String> padEnd(final int length) {
            return new Chain<>(U.padEnd((String) item(), length));
        }

        /**
         * Pad end chain.
         *
         * @param length the length
         * @param chars the chars
         * @return the chain
         */
        public Chain<String> padEnd(final int length, final String chars) {
            return new Chain<>(U.padEnd((String) item(), length, chars));
        }

        /**
         * Snake case chain.
         *
         * @return the chain
         */
        public Chain<String> snakeCase() {
            return new Chain<>(U.snakeCase((String) item()));
        }

        /**
         * Start case chain.
         *
         * @return the chain
         */
        public Chain<String> startCase() {
            return new Chain<>(U.startCase((String) item()));
        }

        /**
         * Starts with chain.
         *
         * @param target the target
         * @return the chain
         */
        public Chain<Boolean> startsWith(final String target) {
            return new Chain<>(U.startsWith((String) item(), target));
        }

        /**
         * Starts with chain.
         *
         * @param target the target
         * @param position the position
         * @return the chain
         */
        public Chain<Boolean> startsWith(final String target, final Integer position) {
            return new Chain<>(U.startsWith((String) item(), target, position));
        }

        /**
         * Trim chain.
         *
         * @return the chain
         */
        public Chain<String> trim() {
            return new Chain<>(U.trim((String) item()));
        }

        /**
         * Trim chain.
         *
         * @param chars the chars
         * @return the chain
         */
        public Chain<String> trim(final String chars) {
            return new Chain<>(U.trim((String) item(), chars));
        }

        /**
         * Trim start chain.
         *
         * @return the chain
         */
        public Chain<String> trimStart() {
            return new Chain<>(U.trimStart((String) item()));
        }

        /**
         * Trim start chain.
         *
         * @param chars the chars
         * @return the chain
         */
        public Chain<String> trimStart(final String chars) {
            return new Chain<>(U.trimStart((String) item(), chars));
        }

        /**
         * Trim end chain.
         *
         * @return the chain
         */
        public Chain<String> trimEnd() {
            return new Chain<>(U.trimEnd((String) item()));
        }

        /**
         * Trunc chain.
         *
         * @return the chain
         */
        public Chain<String> trunc() {
            return new Chain<>(U.trunc((String) item()));
        }

        /**
         * Trunc chain.
         *
         * @param length the length
         * @return the chain
         */
        public Chain<String> trunc(final int length) {
            return new Chain<>(U.trunc((String) item(), length));
        }

        /**
         * Trim end chain.
         *
         * @param chars the chars
         * @return the chain
         */
        public Chain<String> trimEnd(final String chars) {
            return new Chain<>(U.trimEnd((String) item(), chars));
        }

        /**
         * Uncapitalize chain.
         *
         * @return the chain
         */
        public Chain<String> uncapitalize() {
            return new Chain<>(U.uncapitalize((String) item()));
        }

        /**
         * Words chain.
         *
         * @return the chain
         */
        public Chain<String> words() {
            return new Chain<>(U.words((String) item()));
        }

        /**
         * To json chain.
         *
         * @return the chain
         */
        public Chain<String> toJson() {
            return new Chain<>(Json.toJson(value()));
        }

        /**
         * From json chain.
         *
         * @return the chain
         */
        public Chain<Object> fromJson() {
            return new Chain<>(Json.fromJson((String) item()));
        }

        /**
         * To xml chain.
         *
         * @return the chain
         */
        public Chain<String> toXml() {
            return new Chain<>(Xml.toXml(value()));
        }

        /**
         * From xml chain.
         *
         * @return the chain
         */
        public Chain<Object> fromXml() {
            return new Chain<>(Xml.fromXml((String) item()));
        }

        /**
         * Fetch chain.
         *
         * @return the chain
         */
        public Chain<String> fetch() {
            return new Chain<>(U.fetch((String) item()).text());
        }

        /**
         * Fetch chain.
         *
         * @param method the method
         * @param body the body
         * @return the chain
         */
        public Chain<String> fetch(final String method, final String body) {
            return new Chain<>(U.fetch((String) item(), method, body).text());
        }

        /**
         * Create permutation with repetition chain.
         *
         * @param permutationLength the permutation length
         * @return the chain
         */
        public Chain<List<T>> createPermutationWithRepetition(final int permutationLength) {
            return new Chain<>(U.createPermutationWithRepetition(value(), permutationLength));
        }

        /**
         * Xml to json chain.
         *
         * @return the chain
         */
        public Chain<String> xmlToJson() {
            return new Chain<>(U.xmlToJson((String) item()));
        }

        /**
         * Json to xml chain.
         *
         * @return the chain
         */
        public Chain<String> jsonToXml() {
            return new Chain<>(U.jsonToXml((String) item()));
        }
    }

    /**
     * Chain chain.
     *
     * @param item the item
     * @return the chain
     */
    public static Chain<String> chain(final String item) {
        return new U.Chain<>(item);
    }

    /**
     * Chain chain.
     *
     * @param <T> the type parameter
     * @param list the list
     * @return the chain
     */
    public static <T> Chain<T> chain(final List<T> list) {
        return new U.Chain<>(list);
    }

    /**
     * Chain chain.
     *
     * @param map the map
     * @return the chain
     */
    public static Chain<Map<String, Object>> chain(final Map<String, Object> map) {
        return new U.Chain<>(map);
    }

    /**
     * Chain chain.
     *
     * @param <T> the type parameter
     * @param iterable the iterable
     * @return the chain
     */
    public static <T> Chain<T> chain(final Iterable<T> iterable) {
        return new U.Chain<>(newArrayList(iterable));
    }

    /**
     * Chain chain.
     *
     * @param <T> the type parameter
     * @param iterable the iterable
     * @param size the size
     * @return the chain
     */
    public static <T> Chain<T> chain(final Iterable<T> iterable, int size) {
        return new U.Chain<>(newArrayList(iterable, size));
    }

    /**
     * Chain chain.
     *
     * @param <T> the type parameter
     * @param list the list
     * @return the chain
     */
    @SuppressWarnings("unchecked")
    public static <T> Chain<T> chain(final T... list) {
        return new U.Chain<>(Arrays.asList(list));
    }

    /**
     * Chain chain.
     *
     * @param array the array
     * @return the chain
     */
    public static Chain<Integer> chain(final int[] array) {
        return new U.Chain<>(newIntegerList(array));
    }

    @Override
    public Chain<T> chain() {
        return new U.Chain<>(newArrayList(value()));
    }

    /**
     * Of chain.
     *
     * @param item the item
     * @return the chain
     */
    public static Chain<String> of(final String item) {
        return new U.Chain<>(item);
    }

    /**
     * Of chain.
     *
     * @param <T> the type parameter
     * @param list the list
     * @return the chain
     */
    public static <T> Chain<T> of(final List<T> list) {
        return new U.Chain<>(list);
    }

    /**
     * Of chain.
     *
     * @param map the map
     * @return the chain
     */
    public static Chain<Map<String, Object>> of(final Map<String, Object> map) {
        return new U.Chain<>(map);
    }

    /**
     * Of chain.
     *
     * @param <T> the type parameter
     * @param iterable the iterable
     * @return the chain
     */
    public static <T> Chain<T> of(final Iterable<T> iterable) {
        return new U.Chain<>(newArrayList(iterable));
    }

    /**
     * Of chain.
     *
     * @param <T> the type parameter
     * @param iterable the iterable
     * @param size the size
     * @return the chain
     */
    public static <T> Chain<T> of(final Iterable<T> iterable, int size) {
        return new U.Chain<>(newArrayList(iterable, size));
    }

    /**
     * Of chain.
     *
     * @param <T> the type parameter
     * @param list the list
     * @return the chain
     */
    @SuppressWarnings("unchecked")
    public static <T> Chain<T> of(final T... list) {
        return new U.Chain<>(Arrays.asList(list));
    }

    /**
     * Of chain.
     *
     * @param array the array
     * @return the chain
     */
    public static Chain<Integer> of(final int[] array) {
        return new U.Chain<>(newIntegerList(array));
    }

    @Override
    public Chain<T> of() {
        return new U.Chain<>(newArrayList(value()));
    }

    /**
     * Drop list.
     *
     * @param <T> the type parameter
     * @param iterable the iterable
     * @return the list
     */
    public static <T> List<T> drop(final Iterable<T> iterable) {
        return rest(newArrayList(iterable));
    }

    /**
     * Drop list.
     *
     * @return the list
     */
    public List<T> drop() {
        return drop(getIterable());
    }

    /**
     * Drop list.
     *
     * @param <T> the type parameter
     * @param iterable the iterable
     * @param n the n
     * @return the list
     */
    public static <T> List<T> drop(final Iterable<T> iterable, final Integer n) {
        return rest(newArrayList(iterable), n);
    }

    /**
     * Drop list.
     *
     * @param n the n
     * @return the list
     */
    public List<T> drop(final Integer n) {
        return drop(getIterable(), n);
    }

    /**
     * Drop right list.
     *
     * @param <T> the type parameter
     * @param iterable the iterable
     * @return the list
     */
    public static <T> List<T> dropRight(final Iterable<T> iterable) {
        return initial(newArrayList(iterable));
    }

    /**
     * Drop right list.
     *
     * @return the list
     */
    public List<T> dropRight() {
        return dropRight(getIterable());
    }

    /**
     * Drop right list.
     *
     * @param <T> the type parameter
     * @param iterable the iterable
     * @param n the n
     * @return the list
     */
    public static <T> List<T> dropRight(final Iterable<T> iterable, final Integer n) {
        return initial(newArrayList(iterable), n);
    }

    /**
     * Drop right list.
     *
     * @param n the n
     * @return the list
     */
    public List<T> dropRight(final Integer n) {
        return dropRight(getIterable(), n);
    }

    /**
     * Drop while list.
     *
     * @param <T> the type parameter
     * @param iterable the iterable
     * @param pred the pred
     * @return the list
     */
    public static <T> List<T> dropWhile(final Iterable<T> iterable, final Predicate<T> pred) {
        return rest(newArrayList(iterable), findIndex(newArrayList(iterable), negate(pred)));
    }

    /**
     * Drop while list.
     *
     * @param pred the pred
     * @return the list
     */
    public List<T> dropWhile(final Predicate<T> pred) {
        return dropWhile(getIterable(), pred);
    }

    /**
     * Drop right while list.
     *
     * @param <T> the type parameter
     * @param iterable the iterable
     * @param pred the pred
     * @return the list
     */
    public static <T> List<T> dropRightWhile(final Iterable<T> iterable, final Predicate<T> pred) {
        return reverse(dropWhile(reverse(iterable), pred));
    }

    /**
     * Drop right while list.
     *
     * @param pred the pred
     * @return the list
     */
    public List<T> dropRightWhile(final Predicate<T> pred) {
        return dropRightWhile(getIterable(), pred);
    }

    /**
     * Fill list.
     *
     * @param <T> the type parameter
     * @param list the list
     * @param item the item
     * @return the list
     */
    public static <T> List<T> fill(List<T> list, T item) {
        for (int i = 0; i < size(list); i++) {
            list.set(i, item);
        }
        return list;
    }

    /**
     * Fill t [ ].
     *
     * @param <T> the type parameter
     * @param array the array
     * @param item the item
     * @return the t [ ]
     */
    public static <T> T[] fill(T[] array, T item) {
        Arrays.fill(array, item);
        return array;
    }

    /**
     * Fill list.
     *
     * @param value the value
     * @return the list
     */
    @SuppressWarnings("unchecked")
    public List<Object> fill(Object value) {
        return fill((List<Object>) getIterable(), value);
    }

    /**
     * Fill list.
     *
     * @param list the list
     * @param value the value
     * @param start the start
     * @param end the end
     * @return the list
     */
    public static List<Object> fill(
            final List<Object> list, Object value, Integer start, Integer end) {
        for (int index = start; index < end; index += 1) {
            list.set(index, value);
        }
        return list;
    }

    /**
     * Fill list.
     *
     * @param value the value
     * @param start the start
     * @param end the end
     * @return the list
     */
    @SuppressWarnings("unchecked")
    public List<Object> fill(Object value, Integer start, Integer end) {
        return fill((List<Object>) getIterable(), value, start, end);
    }

    /**
     * Flatten deep list.
     *
     * @param <E> the type parameter
     * @param list the list
     * @return the list
     */
    public static <E> List<E> flattenDeep(final List<?> list) {
        return flatten(list, false);
    }

    /**
     * Flatten deep list.
     *
     * @return the list
     */
    public List<T> flattenDeep() {
        return flattenDeep((List<?>) getIterable());
    }

    /**
     * Pull list.
     *
     * @param list the list
     * @param values the values
     * @return the list
     */
    public static List<Object> pull(final List<Object> list, Object... values) {
        final List<Object> valuesList = Arrays.asList(values);
        list.removeIf(valuesList::contains);
        return list;
    }

    /**
     * Pull list.
     *
     * @param values the values
     * @return the list
     */
    @SuppressWarnings("unchecked")
    public List<Object> pull(Object... values) {
        return pull((List<Object>) getIterable(), values);
    }

    /**
     * Pull at list.
     *
     * @param list the list
     * @param indexes the indexes
     * @return the list
     */
    public static List<Object> pullAt(final List<Object> list, final Integer... indexes) {
        final List<Object> result = new ArrayList<>();
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

    /**
     * Pull at list.
     *
     * @param indexes the indexes
     * @return the list
     */
    @SuppressWarnings("unchecked")
    public List<Object> pullAt(final Integer... indexes) {
        return pullAt((List<Object>) getIterable(), indexes);
    }

    /**
     * Remove list.
     *
     * @param <T> the type parameter
     * @param list the list
     * @param pred the pred
     * @return the list
     */
    public static <T> List<T> remove(final List<T> list, final Predicate<T> pred) {
        final List<T> result = new ArrayList<>();
        for (final Iterator<T> iterator = list.iterator(); iterator.hasNext(); ) {
            final T object = iterator.next();
            if (pred.test(object)) {
                result.add(object);
                iterator.remove();
            }
        }
        return result;
    }

    /**
     * Remove list.
     *
     * @param pred the pred
     * @return the list
     */
    public List<T> remove(final Predicate<T> pred) {
        return remove((List<T>) getIterable(), pred);
    }

    /**
     * Take list.
     *
     * @param <T> the type parameter
     * @param iterable the iterable
     * @return the list
     */
    public static <T> List<T> take(final Iterable<T> iterable) {
        return first(newArrayList(iterable), 1);
    }

    /**
     * Take list.
     *
     * @return the list
     */
    public List<T> take() {
        return take(getIterable());
    }

    /**
     * Take right list.
     *
     * @param <T> the type parameter
     * @param iterable the iterable
     * @return the list
     */
    public static <T> List<T> takeRight(final Iterable<T> iterable) {
        return last(newArrayList(iterable), 1);
    }

    /**
     * Take right list.
     *
     * @return the list
     */
    public List<T> takeRight() {
        return takeRight(getIterable());
    }

    /**
     * Take list.
     *
     * @param <T> the type parameter
     * @param iterable the iterable
     * @param n the n
     * @return the list
     */
    public static <T> List<T> take(final Iterable<T> iterable, final Integer n) {
        return first(newArrayList(iterable), n);
    }

    /**
     * Take list.
     *
     * @param n the n
     * @return the list
     */
    public List<T> take(final Integer n) {
        return take(getIterable(), n);
    }

    /**
     * Take right list.
     *
     * @param <T> the type parameter
     * @param iterable the iterable
     * @param n the n
     * @return the list
     */
    public static <T> List<T> takeRight(final Iterable<T> iterable, final Integer n) {
        return last(newArrayList(iterable), n);
    }

    /**
     * Take right list.
     *
     * @param n the n
     * @return the list
     */
    public List<T> takeRight(final Integer n) {
        return takeRight(getIterable(), n);
    }

    /**
     * Take while list.
     *
     * @param <T> the type parameter
     * @param iterable the iterable
     * @param pred the pred
     * @return the list
     */
    public static <T> List<T> takeWhile(final Iterable<T> iterable, final Predicate<T> pred) {
        return first(newArrayList(iterable), findIndex(newArrayList(iterable), negate(pred)));
    }

    /**
     * Take while list.
     *
     * @param pred the pred
     * @return the list
     */
    public List<T> takeWhile(final Predicate<T> pred) {
        return takeWhile(getIterable(), pred);
    }

    /**
     * Take right while list.
     *
     * @param <T> the type parameter
     * @param iterable the iterable
     * @param pred the pred
     * @return the list
     */
    public static <T> List<T> takeRightWhile(final Iterable<T> iterable, final Predicate<T> pred) {
        return reverse(takeWhile(reverse(iterable), pred));
    }

    /**
     * Take right while list.
     *
     * @param pred the pred
     * @return the list
     */
    public List<T> takeRightWhile(final Predicate<T> pred) {
        return takeRightWhile(getIterable(), pred);
    }

    /**
     * Xor list.
     *
     * @param <T> the type parameter
     * @param lists the lists
     * @return the list
     */
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

    /**
     * Xor list.
     *
     * @param list the list
     * @return the list
     */
    @SuppressWarnings("unchecked")
    public List<T> xor(final List<T> list) {
        return xor((List<T>) getIterable(), list);
    }

    /**
     * At list.
     *
     * @param <T> the type parameter
     * @param list the list
     * @param indexes the indexes
     * @return the list
     */
    public static <T> List<T> at(final List<T> list, final Integer... indexes) {
        final List<T> result = new ArrayList<>();
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

    /**
     * At list.
     *
     * @param indexes the indexes
     * @return the list
     */
    public List<T> at(final Integer... indexes) {
        return at((List<T>) getIterable(), indexes);
    }

    /**
     * Average double.
     *
     * @param <T> the type parameter
     * @param iterable the iterable
     * @return the double
     */
    public static <T extends Number> Double average(final Iterable<T> iterable) {
        T sum = sum(iterable);
        if (sum == null) {
            return null;
        }
        return sum.doubleValue() / size(iterable);
    }

    /**
     * Average double.
     *
     * @param <E> the type parameter
     * @param <F> the type parameter
     * @param iterable the iterable
     * @param func the func
     * @return the double
     */
    public static <E, F extends Number> Double average(
            final Iterable<E> iterable, final Function<E, F> func) {
        F sum = sum(iterable, func);
        if (sum == null) {
            return null;
        }
        return sum.doubleValue() / size(iterable);
    }

    /**
     * Average double.
     *
     * @param <N> the type parameter
     * @param array the array
     * @return the double
     */
    public static <N extends Number> Double average(N[] array) {
        N sum = sum(array);
        if (sum == null) {
            return null;
        }
        return sum.doubleValue() / array.length;
    }

    /**
     * Average double.
     *
     * @param first the first
     * @param second the second
     * @return the double
     */
    public static Double average(java.math.BigDecimal first, java.math.BigDecimal second) {
        if (first == null || second == null) {
            return null;
        }
        return sum(first, second).doubleValue() / 2;
    }

    /**
     * Average double.
     *
     * @param first the first
     * @param second the second
     * @return the double
     */
    public static Double average(java.math.BigInteger first, java.math.BigInteger second) {
        if (first == null || second == null) {
            return null;
        }
        return sum(first, second).doubleValue() / 2;
    }

    /**
     * Average double.
     *
     * @param first the first
     * @param second the second
     * @return the double
     */
    public static Double average(Byte first, Byte second) {
        if (first == null || second == null) {
            return null;
        }
        return sum(first, second).doubleValue() / 2;
    }

    /**
     * Average double.
     *
     * @param first the first
     * @param second the second
     * @return the double
     */
    public static Double average(Double first, Double second) {
        if (first == null || second == null) {
            return null;
        }
        return sum(first, second) / 2;
    }

    /**
     * Average double.
     *
     * @param first the first
     * @param second the second
     * @return the double
     */
    public static Double average(Float first, Float second) {
        if (first == null || second == null) {
            return null;
        }
        return sum(first, second).doubleValue() / 2;
    }

    /**
     * Average double.
     *
     * @param first the first
     * @param second the second
     * @return the double
     */
    public static Double average(Integer first, Integer second) {
        if (first == null || second == null) {
            return null;
        }
        return sum(first, second).doubleValue() / 2;
    }

    /**
     * Average double.
     *
     * @param first the first
     * @param second the second
     * @return the double
     */
    public static Double average(Long first, Long second) {
        if (first == null || second == null) {
            return null;
        }
        return sum(first, second).doubleValue() / 2;
    }

    /**
     * Sum t.
     *
     * @param <T> the type parameter
     * @param iterable the iterable
     * @return the t
     */
    public static <T extends Number> T sum(final Iterable<T> iterable) {
        T result = null;
        for (final T item : iterable) {
            result = add(result, item);
        }
        return result;
    }

    /**
     * Sum f.
     *
     * @param <E> the type parameter
     * @param <F> the type parameter
     * @param iterable the iterable
     * @param func the func
     * @return the f
     */
    public static <E, F extends Number> F sum(
            final Iterable<E> iterable, final Function<E, F> func) {
        F result = null;
        for (final E item : iterable) {
            result = add(result, func.apply(item));
        }
        return result;
    }

    /**
     * Sum n.
     *
     * @param <N> the type parameter
     * @param array the array
     * @return the n
     */
    public static <N extends Number> N sum(N[] array) {
        N result = null;
        for (final N item : array) {
            result = add(result, item);
        }
        return result;
    }

    /**
     * Sum f.
     *
     * @param <F> the type parameter
     * @return the f
     */
    @SuppressWarnings("unchecked")
    public <F extends Number> F sum() {
        return sum((List<F>) getIterable());
    }

    /**
     * Sum f.
     *
     * @param <E> the type parameter
     * @param <F> the type parameter
     * @param func the func
     * @return the f
     */
    @SuppressWarnings("unchecked")
    public <E, F extends Number> F sum(final Function<E, F> func) {
        return sum((List<E>) getIterable(), func);
    }

    /**
     * Add t.
     *
     * @param <T> the type parameter
     * @param first the first
     * @param second the second
     * @return the t
     */
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

    /**
     * Subtract t.
     *
     * @param <T> the type parameter
     * @param values the values
     * @return the t
     */
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

    /**
     * Mean double.
     *
     * @param <T> the type parameter
     * @param iterable the iterable
     * @return the double
     */
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

    /**
     * Mean double.
     *
     * @return the double
     */
    @SuppressWarnings("unchecked")
    public double mean() {
        return mean((Iterable<Number>) getIterable());
    }

    /**
     * Median double.
     *
     * @param <T> the type parameter
     * @param iterable the iterable
     * @return the double
     */
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

    /**
     * Median double.
     *
     * @return the double
     */
    @SuppressWarnings("unchecked")
    public double median() {
        return median((Iterable<Number>) getIterable());
    }

    /**
     * Camel case string.
     *
     * @param string the string
     * @return the string
     */
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

    /**
     * Lower first string.
     *
     * @param string the string
     * @return the string
     */
    public static String lowerFirst(final String string) {
        return createCaseFirst("toLowerCase").apply(string);
    }

    /**
     * Upper first string.
     *
     * @param string the string
     * @return the string
     */
    public static String upperFirst(final String string) {
        return createCaseFirst("toUpperCase").apply(string);
    }

    /**
     * Capitalize string.
     *
     * @param string the string
     * @return the string
     */
    public static String capitalize(final String string) {
        return upperFirst(baseToString(string));
    }

    /**
     * Uncapitalize string.
     *
     * @param string the string
     * @return the string
     */
    public static String uncapitalize(final String string) {
        return lowerFirst(baseToString(string));
    }

    private static String baseToString(String value) {
        return value == null ? "" : value;
    }

    /**
     * Deburr string.
     *
     * @param string the string
     * @return the string
     */
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

    /**
     * Words list.
     *
     * @param string the string
     * @return the list
     */
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

    /**
     * Ends with boolean.
     *
     * @param string the string
     * @param target the target
     * @return the boolean
     */
    public static boolean endsWith(final String string, final String target) {
        return endsWith(string, target, null);
    }

    /**
     * Ends with boolean.
     *
     * @param string the string
     * @param target the target
     * @param position the position
     * @return the boolean
     */
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

    /**
     * Kebab case string.
     *
     * @param string the string
     * @return the string
     */
    public static String kebabCase(final String string) {
        return createCompounder(
                        (result, word, index) ->
                                result
                                        + (index > 0 ? "-" : "")
                                        + word.toLowerCase(Locale.getDefault()))
                .apply(string);
    }

    /**
     * Repeat string.
     *
     * @param string the string
     * @param length the length
     * @return the string
     */
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

    /**
     * Pad string.
     *
     * @param string the string
     * @param length the length
     * @return the string
     */
    public static String pad(final String string, final int length) {
        return pad(string, length, null);
    }

    /**
     * Pad string.
     *
     * @param string the string
     * @param length the length
     * @param chars the chars
     * @return the string
     */
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

    /**
     * Pad start string.
     *
     * @param string the string
     * @param length the length
     * @return the string
     */
    public static String padStart(final String string, final Integer length) {
        return createPadDir(false).apply(string, length, null);
    }

    /**
     * Pad start string.
     *
     * @param string the string
     * @param length the length
     * @param chars the chars
     * @return the string
     */
    public static String padStart(final String string, final Integer length, final String chars) {
        return createPadDir(false).apply(string, length, chars);
    }

    /**
     * Pad end string.
     *
     * @param string the string
     * @param length the length
     * @return the string
     */
    public static String padEnd(final String string, final Integer length) {
        return createPadDir(true).apply(string, length, null);
    }

    /**
     * Pad end string.
     *
     * @param string the string
     * @param length the length
     * @param chars the chars
     * @return the string
     */
    public static String padEnd(final String string, final Integer length, final String chars) {
        return createPadDir(true).apply(string, length, chars);
    }

    /**
     * Snake case string.
     *
     * @param string the string
     * @return the string
     */
    public static String snakeCase(final String string) {
        return createCompounder(
                        (result, word, index) ->
                                result
                                        + (index > 0 ? "_" : "")
                                        + word.toLowerCase(Locale.getDefault()))
                .apply(string);
    }

    /**
     * Start case string.
     *
     * @param string the string
     * @return the string
     */
    public static String startCase(final String string) {
        return createCompounder(
                        (result, word, index) ->
                                result
                                        + (index > 0 ? " " : "")
                                        + word.substring(0, 1).toUpperCase(Locale.getDefault())
                                        + word.substring(1))
                .apply(string);
    }

    /**
     * Starts with boolean.
     *
     * @param string the string
     * @param target the target
     * @return the boolean
     */
    public static boolean startsWith(final String string, final String target) {
        return startsWith(string, target, null);
    }

    /**
     * Starts with boolean.
     *
     * @param string the string
     * @param target the target
     * @param position the position
     * @return the boolean
     */
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

    /**
     * Trim string.
     *
     * @param string the string
     * @return the string
     */
    public static String trim(final String string) {
        return trim(string, null);
    }

    /**
     * Trim string.
     *
     * @param string the string
     * @param chars the chars
     * @return the string
     */
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

    /**
     * Trim start string.
     *
     * @param string the string
     * @return the string
     */
    public static String trimStart(final String string) {
        return trimStart(string, null);
    }

    /**
     * Trim start string.
     *
     * @param string the string
     * @param chars the chars
     * @return the string
     */
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

    /**
     * Trim end string.
     *
     * @param string the string
     * @return the string
     */
    public static String trimEnd(final String string) {
        return trimEnd(string, null);
    }

    /**
     * Trim end string.
     *
     * @param string the string
     * @param chars the chars
     * @return the string
     */
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

    /**
     * Trunc string.
     *
     * @param string the string
     * @return the string
     */
    public static String trunc(final String string) {
        return trunc(string, DEFAULT_TRUNC_LENGTH);
    }

    /**
     * Trunc string.
     *
     * @param string the string
     * @param length the length
     * @return the string
     */
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

    /**
     * String to path list.
     *
     * @param string the string
     * @return the list
     */
    public static List<String> stringToPath(final String string) {
        final List<String> result = new ArrayList<>();
        final java.util.regex.Matcher matcher = RE_PROP_NAME.matcher(baseToString(string));
        while (matcher.find()) {
            result.add(matcher.group(1) == null ? matcher.group(0) : matcher.group(1));
        }
        return result;
    }

    private enum OperationType {
        /** Get operation type. */
        GET,
        /** Set operation type. */
        SET,
        /** Update operation type. */
        UPDATE,
        /** Remove operation type. */
        REMOVE
    }

    @SuppressWarnings("unchecked")
    private static <T> T baseGetOrSetOrRemove(
            final Map<String, Object> object,
            final List<String> paths,
            final Object value,
            OperationType operationType) {
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
        if (operationType == OperationType.SET || operationType == OperationType.UPDATE) {
            if (savedLocalObject instanceof Map) {
                checkSetOrUpdate(
                        value, operationType, (Map<String, Object>) savedLocalObject, savedPath);
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

    private static void checkSetOrUpdate(
            Object value,
            OperationType operationType,
            Map<String, Object> savedLocalObject,
            String savedPath) {
        if (operationType == OperationType.UPDATE && savedLocalObject.containsKey(savedPath)) {
            savedLocalObject.put(Underscore.uniqueId(savedPath), value);
        } else {
            savedLocalObject.put(savedPath, value);
        }
    }

    private static Map.Entry getMapEntry(Map map) {
        return map.isEmpty() ? null : (Map.Entry) map.entrySet().iterator().next();
    }

    /**
     * Get t.
     *
     * @param <T> the type parameter
     * @param object the object
     * @param path the path
     * @return the t
     */
    public static <T> T get(final Map<String, Object> object, final String path) {
        return get(object, stringToPath(path));
    }

    /**
     * Get t.
     *
     * @param <T> the type parameter
     * @param object the object
     * @param paths the paths
     * @return the t
     */
    public static <T> T get(final Map<String, Object> object, final List<String> paths) {
        return baseGetOrSetOrRemove(object, paths, null, OperationType.GET);
    }

    /**
     * Select token string.
     *
     * @param object the object
     * @param expression the expression
     * @return the string
     */
    public static String selectToken(final Map<String, Object> object, final String expression) {
        final String xml = toXml(object);
        try {
            final XPath xPath = XPathFactory.newInstance().newXPath();
            final org.w3c.dom.Document document = Xml.Document.createDocument(xml);
            final NodeList nodes =
                    (NodeList) xPath.compile(expression).evaluate(document, XPathConstants.NODESET);
            if (nodes.getLength() == 0) {
                return null;
            }
            return nodes.item(0).getNodeValue();
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    /**
     * Select tokens list.
     *
     * @param object the object
     * @param expression the expression
     * @return the list
     */
    public static List<String> selectTokens(
            final Map<String, Object> object, final String expression) {
        final String xml = toXml(object);
        try {
            final XPath xPath = XPathFactory.newInstance().newXPath();
            final org.w3c.dom.Document document = Xml.Document.createDocument(xml);
            final NodeList nodes =
                    (NodeList) xPath.compile(expression).evaluate(document, XPathConstants.NODESET);
            final List<String> result = new ArrayList<>();
            for (int i = 0; i < nodes.getLength(); i++) {
                result.add(nodes.item(i).getNodeValue());
            }
            return result;
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    /**
     * Set t.
     *
     * @param <T> the type parameter
     * @param object the object
     * @param path the path
     * @param value the value
     * @return the t
     */
    public static <T> T set(final Map<String, Object> object, final String path, Object value) {
        return set(object, stringToPath(path), value);
    }

    /**
     * Set t.
     *
     * @param <T> the type parameter
     * @param object the object
     * @param paths the paths
     * @param value the value
     * @return the t
     */
    public static <T> T set(
            final Map<String, Object> object, final List<String> paths, Object value) {
        return baseGetOrSetOrRemove(object, paths, value, OperationType.SET);
    }

    /**
     * Update t.
     *
     * @param <T> the type parameter
     * @param object the object
     * @param path the path
     * @param value the value
     * @return the t
     */
    public static <T> T update(final Map<String, Object> object, final String path, Object value) {
        return update(object, stringToPath(path), value);
    }

    /**
     * Update t.
     *
     * @param <T> the type parameter
     * @param object the object
     * @param paths the paths
     * @param value the value
     * @return the t
     */
    public static <T> T update(
            final Map<String, Object> object, final List<String> paths, Object value) {
        return baseGetOrSetOrRemove(object, paths, value, OperationType.UPDATE);
    }

    /**
     * Remove t.
     *
     * @param <T> the type parameter
     * @param object the object
     * @param path the path
     * @return the t
     */
    public static <T> T remove(final Map<String, Object> object, final String path) {
        return remove(object, stringToPath(path));
    }

    /**
     * Remove t.
     *
     * @param <T> the type parameter
     * @param object the object
     * @param paths the paths
     * @return the t
     */
    public static <T> T remove(final Map<String, Object> object, final List<String> paths) {
        return baseGetOrSetOrRemove(object, paths, null, OperationType.REMOVE);
    }

    /**
     * Rename map.
     *
     * @param map the map
     * @param oldKey the old key
     * @param newKey the new key
     * @return the map
     */
    public static Map<String, Object> rename(
            final Map<String, Object> map, final String oldKey, final String newKey) {
        Map<String, Object> outMap = new LinkedHashMap<>();
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
            List<Object> values = new ArrayList<>();
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

    /**
     * Sets value.
     *
     * @param map the map
     * @param key the key
     * @param newValue the new value
     * @return the value
     */
    public static Map<String, Object> setValue(
            final Map<String, Object> map, final String key, final Object newValue) {
        return setValue(map, key, (key1, value) -> newValue);
    }

    /**
     * Sets value.
     *
     * @param map the map
     * @param key the key
     * @param newValue the new value
     * @return the value
     */
    public static Map<String, Object> setValue(
            final Map<String, Object> map,
            final String key,
            final BiFunction<String, Object, Object> newValue) {
        Map<String, Object> outMap = new LinkedHashMap<>();
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
            List<Object> values = new ArrayList<>();
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

    /**
     * Update map.
     *
     * @param map1 the map 1
     * @param map2 the map 2
     * @return the map
     */
    public static Map<String, Object> update(
            final Map<String, Object> map1, final Map<String, Object> map2) {
        Map<String, Object> outMap = new LinkedHashMap<>();
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

    /**
     * Merge list.
     *
     * @param list1 the list 1
     * @param list2 the list 2
     * @return the list
     */
    public static List<Object> merge(List<Object> list1, List<Object> list2) {
        List<Object> outList1 = newArrayList(list1);
        List<Object> outList2 = newArrayList(list2);
        outList2.removeAll(list1);
        outList1.addAll(outList2);
        return outList1;
    }

    /** The type Fetch response. */
    public static class FetchResponse {
        private final boolean ok;
        private final int status;
        private final Map<String, List<String>> headerFields;
        private final java.io.ByteArrayOutputStream stream;

        /**
         * Instantiates a new Fetch response.
         *
         * @param ok the ok
         * @param status the status
         * @param headerFields the header fields
         * @param stream the stream
         */
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

        /**
         * Is ok boolean.
         *
         * @return the boolean
         */
        public boolean isOk() {
            return ok;
        }

        /**
         * Gets status.
         *
         * @return the status
         */
        public int getStatus() {
            return status;
        }

        /**
         * Gets header fields.
         *
         * @return the header fields
         */
        public Map<String, List<String>> getHeaderFields() {
            return headerFields;
        }

        /**
         * Blob byte [ ].
         *
         * @return the byte [ ]
         */
        public byte[] blob() {
            return stream.toByteArray();
        }

        /**
         * Text string.
         *
         * @return the string
         */
        public String text() {
            return stream.toString(StandardCharsets.UTF_8);
        }

        /**
         * Json object.
         *
         * @return the object
         */
        public Object json() {
            return Json.fromJson(text());
        }

        /**
         * Json map map.
         *
         * @return the map
         */
        public Map<String, Object> jsonMap() {
            return fromJsonMap(text());
        }

        /**
         * Xml object.
         *
         * @return the object
         */
        public Object xml() {
            return Xml.fromXml(text());
        }

        /**
         * Xml map map.
         *
         * @return the map
         */
        public Map<String, Object> xmlMap() {
            return fromXmlMap(text());
        }
    }

    /**
     * Download url long.
     *
     * @param url the url
     * @param fileName the file name
     * @return the long
     * @throws IOException the io exception
     * @throws URISyntaxException the uri syntax exception
     */
    public static long downloadUrl(final String url, final String fileName)
            throws IOException, URISyntaxException {
        final URL website = new URI(url).toURL();
        try (ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                final FileOutputStream fos = new FileOutputStream(fileName)) {
            return fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        }
    }

    /**
     * Decompress gzip.
     *
     * @param sourceFileName the source file name
     * @param targetFileName the target file name
     * @throws IOException the io exception
     */
    public static void decompressGzip(final String sourceFileName, final String targetFileName)
            throws IOException {
        try (GZIPInputStream gis =
                new GZIPInputStream(new FileInputStream(new File(sourceFileName)))) {
            Files.copy(gis, Paths.get(targetFileName));
        }
    }

    /**
     * Fetch fetch response.
     *
     * @param url the url
     * @return the fetch response
     */
    public static FetchResponse fetch(final String url) {
        return fetch(url, null, null, DEFAULT_HEADER_FIELDS, null, null);
    }

    /**
     * Fetch fetch response.
     *
     * @param url the url
     * @param connectTimeout the connect timeout
     * @param readTimeout the read timeout
     * @return the fetch response
     */
    public static FetchResponse fetch(
            final String url, final Integer connectTimeout, final Integer readTimeout) {
        return fetch(url, null, null, DEFAULT_HEADER_FIELDS, connectTimeout, readTimeout);
    }

    /**
     * Fetch fetch response.
     *
     * @param url the url
     * @param connectTimeout the connect timeout
     * @param readTimeout the read timeout
     * @param retryCount the retry count
     * @param timeBetweenRetry the time between retry
     * @return the fetch response
     */
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

    /**
     * Fetch fetch response.
     *
     * @param url the url
     * @param method the method
     * @param body the body
     * @return the fetch response
     */
    public static FetchResponse fetch(final String url, final String method, final String body) {
        return fetch(url, method, body, DEFAULT_HEADER_FIELDS, null, null);
    }

    /** The type Base http ssl socket factory. */
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

        /** The type My x 509 trust manager. */
        public static class MyX509TrustManager implements javax.net.ssl.X509TrustManager {

            /** The Manger. */
            static MyX509TrustManager manger = new MyX509TrustManager();

            /** Instantiates a new My x 509 trust manager. */
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

    /**
     * Sets connection.
     *
     * @param connection the connection
     * @param method the method
     * @param headerFields the header fields
     * @param connectTimeout the connect timeout
     * @param readTimeout the read timeout
     * @throws IOException the io exception
     */
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

    /**
     * Fetch fetch response.
     *
     * @param url the url
     * @param method the method
     * @param body the body
     * @param headerFields the header fields
     * @param connectTimeout the connect timeout
     * @param readTimeout the read timeout
     * @return the fetch response
     */
    public static FetchResponse fetch(
            final String url,
            final String method,
            final String body,
            final Map<String, List<String>> headerFields,
            final Integer connectTimeout,
            final Integer readTimeout) {
        try {
            final java.net.URL localUrl = new java.net.URI(url).toURL();
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
        } catch (java.io.IOException | java.net.URISyntaxException ex) {
            throw new UnsupportedOperationException(ex);
        }
    }

    /** The type Fetch. */
    public static class Fetch {
        private Fetch() {}

        /**
         * Fetch fetch response.
         *
         * @param url the url
         * @param method the method
         * @param body the body
         * @param headerFields the header fields
         * @param connectTimeout the connect timeout
         * @param readTimeout the read timeout
         * @param retryCount the retry count
         * @param timeBetweenRetry the time between retry
         * @return the fetch response
         */
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

    /**
     * Explode list.
     *
     * @param input the input
     * @return the list
     */
    public static List<String> explode(final String input) {
        List<String> result = new ArrayList<>();
        if (isNull(input)) {
            return result;
        }
        for (char character : input.toCharArray()) {
            result.add(String.valueOf(character));
        }
        return result;
    }

    /**
     * Implode string.
     *
     * @param input the input
     * @return the string
     */
    public static String implode(final String[] input) {
        StringBuilder builder = new StringBuilder();
        for (String character : input) {
            if (nonNull(character)) {
                builder.append(character);
            }
        }
        return builder.toString();
    }

    /**
     * Implode string.
     *
     * @param input the input
     * @return the string
     */
    public static String implode(final Iterable<String> input) {
        StringBuilder builder = new StringBuilder();
        for (String character : input) {
            if (nonNull(character)) {
                builder.append(character);
            }
        }
        return builder.toString();
    }

    /**
     * Camel case string.
     *
     * @return the string
     */
    public String camelCase() {
        return camelCase(getString().get());
    }

    /**
     * Lower first string.
     *
     * @return the string
     */
    public String lowerFirst() {
        return lowerFirst(getString().get());
    }

    /**
     * Upper first string.
     *
     * @return the string
     */
    public String upperFirst() {
        return upperFirst(getString().get());
    }

    /**
     * Capitalize string.
     *
     * @return the string
     */
    public String capitalize() {
        return capitalize(getString().get());
    }

    /**
     * Deburr string.
     *
     * @return the string
     */
    public String deburr() {
        return deburr(getString().get());
    }

    /**
     * Ends with boolean.
     *
     * @param target the target
     * @return the boolean
     */
    public boolean endsWith(final String target) {
        return endsWith(getString().get(), target);
    }

    /**
     * Ends with boolean.
     *
     * @param target the target
     * @param position the position
     * @return the boolean
     */
    public boolean endsWith(final String target, final Integer position) {
        return endsWith(getString().get(), target, position);
    }

    /**
     * Kebab case string.
     *
     * @return the string
     */
    public String kebabCase() {
        return kebabCase(getString().get());
    }

    /**
     * Repeat string.
     *
     * @param length the length
     * @return the string
     */
    public String repeat(final int length) {
        return repeat(getString().get(), length);
    }

    /**
     * Pad string.
     *
     * @param length the length
     * @return the string
     */
    public String pad(final int length) {
        return pad(getString().get(), length);
    }

    /**
     * Pad string.
     *
     * @param length the length
     * @param chars the chars
     * @return the string
     */
    public String pad(final int length, final String chars) {
        return pad(getString().get(), length, chars);
    }

    /**
     * Pad start string.
     *
     * @param length the length
     * @return the string
     */
    public String padStart(final int length) {
        return padStart(getString().get(), length);
    }

    /**
     * Pad start string.
     *
     * @param length the length
     * @param chars the chars
     * @return the string
     */
    public String padStart(final int length, final String chars) {
        return padStart(getString().get(), length, chars);
    }

    /**
     * Pad end string.
     *
     * @param length the length
     * @return the string
     */
    public String padEnd(final int length) {
        return padEnd(getString().get(), length);
    }

    /**
     * Pad end string.
     *
     * @param length the length
     * @param chars the chars
     * @return the string
     */
    public String padEnd(final int length, final String chars) {
        return padEnd(getString().get(), length, chars);
    }

    /**
     * Snake case string.
     *
     * @return the string
     */
    public String snakeCase() {
        return snakeCase(getString().get());
    }

    /**
     * Start case string.
     *
     * @return the string
     */
    public String startCase() {
        return startCase(getString().get());
    }

    /**
     * Starts with boolean.
     *
     * @param target the target
     * @return the boolean
     */
    public boolean startsWith(final String target) {
        return startsWith(getString().get(), target);
    }

    /**
     * Starts with boolean.
     *
     * @param target the target
     * @param position the position
     * @return the boolean
     */
    public boolean startsWith(final String target, final Integer position) {
        return startsWith(getString().get(), target, position);
    }

    /**
     * Trim string.
     *
     * @return the string
     */
    public String trim() {
        return trim(getString().get());
    }

    /**
     * Trim with string.
     *
     * @param chars the chars
     * @return the string
     */
    public String trimWith(final String chars) {
        return trim(getString().get(), chars);
    }

    /**
     * Trim start string.
     *
     * @return the string
     */
    public String trimStart() {
        return trimStart(getString().get());
    }

    /**
     * Trim start with string.
     *
     * @param chars the chars
     * @return the string
     */
    public String trimStartWith(final String chars) {
        return trimStart(getString().get(), chars);
    }

    /**
     * Trim end string.
     *
     * @return the string
     */
    public String trimEnd() {
        return trimEnd(getString().get());
    }

    /**
     * Trim end with string.
     *
     * @param chars the chars
     * @return the string
     */
    public String trimEndWith(final String chars) {
        return trimEnd(getString().get(), chars);
    }

    /**
     * Trunc string.
     *
     * @return the string
     */
    public String trunc() {
        return trunc(getString().get());
    }

    /**
     * Trunc string.
     *
     * @param length the length
     * @return the string
     */
    public String trunc(final int length) {
        return trunc(getString().get(), length);
    }

    /**
     * Uncapitalize string.
     *
     * @return the string
     */
    public String uncapitalize() {
        return uncapitalize(getString().get());
    }

    /**
     * Words list.
     *
     * @return the list
     */
    public List<String> words() {
        return words(getString().get());
    }

    /**
     * The type Lru cache.
     *
     * @param <K> the type parameter
     * @param <V> the type parameter
     */
    public static class LruCache<K, V> {
        private static final boolean SORT_BY_ACCESS = true;
        private static final float LOAD_FACTOR = 0.75F;
        private final Map<K, V> lruCacheMap;
        private final int capacity;

        /**
         * Instantiates a new Lru cache.
         *
         * @param capacity the capacity
         */
        public LruCache(int capacity) {
            this.capacity = capacity;
            this.lruCacheMap = new LinkedHashMap<>(capacity, LOAD_FACTOR, SORT_BY_ACCESS);
        }

        /**
         * Get v.
         *
         * @param key the key
         * @return the v
         */
        public V get(K key) {
            return lruCacheMap.get(key);
        }

        /**
         * Put.
         *
         * @param key the key
         * @param value the value
         */
        public void put(K key, V value) {
            if (lruCacheMap.containsKey(key)) {
                lruCacheMap.remove(key);
            } else if (lruCacheMap.size() >= capacity) {
                lruCacheMap.remove(lruCacheMap.keySet().iterator().next());
            }
            lruCacheMap.put(key, value);
        }
    }

    /**
     * Create lru cache lru cache.
     *
     * @param <K> the type parameter
     * @param <V> the type parameter
     * @param capacity the capacity
     * @return the lru cache
     */
    public static <K, V> LruCache<K, V> createLruCache(final int capacity) {
        return new LruCache<>(capacity);
    }

    /**
     * Create permutation with repetition list.
     *
     * @param <T> the type parameter
     * @param list the list
     * @param permutationLength the permutation length
     * @return the list
     */
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

    /**
     * Create permutation with repetition list.
     *
     * @param permutationLength the permutation length
     * @return the list
     */
    public List<List<T>> createPermutationWithRepetition(final int permutationLength) {
        return createPermutationWithRepetition((List<T>) value(), permutationLength);
    }

    /**
     * New array list list.
     *
     * @param <T> the type parameter
     * @param iterable the iterable
     * @return the list
     */
    protected static <T> List<T> newArrayList(final Iterable<T> iterable) {
        return Underscore.newArrayList(iterable);
    }

    /**
     * Join string.
     *
     * @param <T> the type parameter
     * @param iterable the iterable
     * @param separator the separator
     * @return the string
     */
    public static <T> String join(final Iterable<T> iterable, final String separator) {
        return Underscore.join(iterable, separator);
    }

    /**
     * To json string.
     *
     * @param collection the collection
     * @return the string
     */
    public static String toJson(Collection collection) {
        return Json.toJson(collection);
    }

    /**
     * To json string.
     *
     * @param map the map
     * @return the string
     */
    public static String toJson(Map map) {
        return Json.toJson(map);
    }

    /**
     * To json string.
     *
     * @return the string
     */
    public String toJson() {
        return Json.toJson((Collection) getIterable());
    }

    /**
     * From xml t.
     *
     * @param <T> the type parameter
     * @param xml the xml
     * @return the t
     */
    @SuppressWarnings("unchecked")
    public static <T> T fromXml(final String xml) {
        return (T) Xml.fromXml(xml);
    }

    /**
     * From xml map map.
     *
     * @param xml the xml
     * @return the map
     */
    public static Map<String, Object> fromXmlMap(final String xml) {
        return fromXmlMap(xml, Xml.FromType.FOR_CONVERT);
    }

    /**
     * From xml map map.
     *
     * @param xml the xml
     * @param fromType the from type
     * @return the map
     */
    public static Map<String, Object> fromXmlMap(final String xml, final Xml.FromType fromType) {
        final Object object = Xml.fromXml(xml, fromType);
        return getStringObjectMap(object);
    }

    /**
     * From xml t.
     *
     * @param <T> the type parameter
     * @param xml the xml
     * @param fromType the from type
     * @return the t
     */
    @SuppressWarnings("unchecked")
    public static <T> T fromXml(final String xml, final Xml.FromType fromType) {
        return (T) Xml.fromXml(xml, fromType);
    }

    /**
     * From xml make arrays t.
     *
     * @param <T> the type parameter
     * @param xml the xml
     * @return the t
     */
    @SuppressWarnings("unchecked")
    public static <T> T fromXmlMakeArrays(final String xml) {
        return (T) Xml.fromXmlMakeArrays(xml);
    }

    /**
     * From xml without namespaces t.
     *
     * @param <T> the type parameter
     * @param xml the xml
     * @return the t
     */
    @SuppressWarnings("unchecked")
    public static <T> T fromXmlWithoutNamespaces(final String xml) {
        return (T) Xml.fromXmlWithoutNamespaces(xml);
    }

    /**
     * From xml without namespaces map map.
     *
     * @param xml the xml
     * @return the map
     */
    public static Map<String, Object> fromXmlWithoutNamespacesMap(final String xml) {
        final Object object = Xml.fromXmlWithoutNamespaces(xml);
        return getStringObjectMap(object);
    }

    /**
     * From xml without attributes t.
     *
     * @param <T> the type parameter
     * @param xml the xml
     * @return the t
     */
    @SuppressWarnings("unchecked")
    public static <T> T fromXmlWithoutAttributes(final String xml) {
        return (T) Xml.fromXmlWithoutAttributes(xml);
    }

    /**
     * From xml without namespaces and attributes t.
     *
     * @param <T> the type parameter
     * @param xml the xml
     * @return the t
     */
    @SuppressWarnings("unchecked")
    public static <T> T fromXmlWithoutNamespacesAndAttributes(final String xml) {
        return (T) Xml.fromXmlWithoutNamespacesAndAttributes(xml);
    }

    /**
     * To xml string.
     *
     * @param collection the collection
     * @return the string
     */
    public static String toXml(Collection collection) {
        return Xml.toXml(collection);
    }

    /**
     * To xml string.
     *
     * @param map the map
     * @return the string
     */
    public static String toXml(Map map) {
        return Xml.toXml(map);
    }

    /**
     * From json t.
     *
     * @param <T> the type parameter
     * @param string the string
     * @return the t
     */
    @SuppressWarnings("unchecked")
    public static <T> T fromJson(String string) {
        return (T) Json.fromJson(string);
    }

    /**
     * From json object.
     *
     * @return the object
     */
    public Object fromJson() {
        return Json.fromJson(getString().get());
    }

    /**
     * From json map map.
     *
     * @param string the string
     * @return the map
     */
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
            result = new LinkedHashMap<>();
            result.put("value", object);
        }
        return result;
    }

    /**
     * To xml string.
     *
     * @return the string
     */
    public String toXml() {
        return Xml.toXml((Collection) getIterable());
    }

    /**
     * From xml object.
     *
     * @return the object
     */
    public Object fromXml() {
        return Xml.fromXml(getString().get());
    }

    /**
     * Json to xml string.
     *
     * @param json the json
     * @param identStep the ident step
     * @param mode the mode
     * @param newRootName the new root name
     * @return the string
     */
    @SuppressWarnings("unchecked")
    public static String jsonToXml(
            String json,
            Xml.XmlStringBuilder.Step identStep,
            JsonToXmlMode mode,
            String newRootName) {
        Object object = Json.fromJson(json);
        final String result;
        if (object instanceof Map) {
            if (mode == JsonToXmlMode.FORCE_ATTRIBUTE_USAGE) {
                result = Xml.toXml(forceAttributeUsage((Map) object), identStep, newRootName);
            } else if (mode == JsonToXmlMode.DEFINE_ROOT_NAME) {
                result = Xml.toXml((Map) object, identStep, newRootName);
            } else if (mode == JsonToXmlMode.REPLACE_NULL_WITH_EMPTY_VALUE) {
                result = Xml.toXml(replaceNullWithEmptyValue((Map) object), identStep, newRootName);
            } else if (mode == JsonToXmlMode.REPLACE_EMPTY_STRING_WITH_EMPTY_VALUE) {
                result =
                        Xml.toXml(
                                replaceEmptyStringWithEmptyValue((Map) object),
                                identStep,
                                newRootName);
            } else if (mode == JsonToXmlMode.ADD_ROOT
                    && !Xml.XmlValue.getMapKey(object).equals(ROOT)) {
                final Map<String, Object> map = new LinkedHashMap<>();
                map.put(newRootName, object);
                result = Xml.toXml(map, identStep);
            } else if (mode == JsonToXmlMode.REMOVE_ARRAY_ATTRIBUTE) {
                result = Xml.toXml((Map) object, identStep, newRootName, Xml.ArrayTrue.SKIP);
            } else if (mode == JsonToXmlMode.REMOVE_ATTRIBUTES) {
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

    /**
     * Json to xml string.
     *
     * @param json the json
     * @param identStep the ident step
     * @return the string
     */
    public static String jsonToXml(String json, Xml.XmlStringBuilder.Step identStep) {
        return jsonToXml(json, identStep, null, ROOT);
    }

    /**
     * Json to xml string.
     *
     * @param json the json
     * @param mode the mode
     * @return the string
     */
    public static String jsonToXml(String json, JsonToXmlMode mode) {
        return jsonToXml(json, Xml.XmlStringBuilder.Step.TWO_SPACES, mode, ROOT);
    }

    /**
     * Json to xml string.
     *
     * @param json the json
     * @param mode the mode
     * @param newRootName the new root name
     * @return the string
     */
    public static String jsonToXml(String json, JsonToXmlMode mode, String newRootName) {
        return jsonToXml(json, Xml.XmlStringBuilder.Step.TWO_SPACES, mode, newRootName);
    }

    /**
     * Json to xml string.
     *
     * @param json the json
     * @param newRootName the new root name
     * @return the string
     */
    public static String jsonToXml(String json, String newRootName) {
        return jsonToXml(
                json,
                Xml.XmlStringBuilder.Step.TWO_SPACES,
                JsonToXmlMode.DEFINE_ROOT_NAME,
                newRootName);
    }

    /**
     * Json to xml string.
     *
     * @param json the json
     * @return the string
     */
    public static String jsonToXml(String json) {
        return jsonToXml(json, Xml.XmlStringBuilder.Step.TWO_SPACES, null, null);
    }

    /**
     * Xml to json string.
     *
     * @param xml the xml
     * @param identStep the ident step
     * @param mode the mode
     * @return the string
     */
    @SuppressWarnings("unchecked")
    public static String xmlToJson(
            String xml, Json.JsonStringBuilder.Step identStep, XmlToJsonMode mode) {
        Object object = Xml.fromXml(xml);
        final String result;
        if (object instanceof Map) {
            if (mode == XmlToJsonMode.REPLACE_SELF_CLOSING_WITH_NULL) {
                result = Json.toJson(replaceSelfClosingWithNull((Map) object), identStep);
            } else if (mode == XmlToJsonMode.REPLACE_SELF_CLOSING_WITH_STRING) {
                result = Json.toJson(replaceSelfClosingWithEmpty((Map) object), identStep);
            } else if (mode == XmlToJsonMode.REPLACE_EMPTY_VALUE_WITH_NULL) {
                result = Json.toJson(replaceEmptyValueWithNull((Map) object), identStep);
            } else if (mode == XmlToJsonMode.REPLACE_EMPTY_TAG_WITH_NULL) {
                result =
                        Json.toJson(
                                replaceEmptyValueWithNull(replaceSelfClosingWithNull((Map) object)),
                                identStep);
            } else if (mode == XmlToJsonMode.REPLACE_EMPTY_TAG_WITH_STRING) {
                result =
                        Json.toJson(
                                (Map<String, Object>)
                                        replaceEmptyValueWithEmptyString(
                                                replaceSelfClosingWithEmpty((Map) object)),
                                identStep);
            } else if (mode == XmlToJsonMode.REMOVE_FIRST_LEVEL) {
                result = Json.toJson(replaceFirstLevel((Map) object), identStep);
            } else if (mode == XmlToJsonMode.WITHOUT_NAMESPACES) {
                result = Json.toJson((Map) Xml.fromXmlWithoutNamespaces(xml), identStep);
            } else {
                result = Json.toJson((Map) object, identStep);
            }
            return result;
        }
        return Json.toJson((List) object, identStep);
    }

    /**
     * Xml to json string.
     *
     * @param xml the xml
     * @return the string
     */
    public static String xmlToJson(String xml) {
        return xmlToJson(xml, Json.JsonStringBuilder.Step.TWO_SPACES, null);
    }

    /**
     * Xml to json string.
     *
     * @param xml the xml
     * @param identStep the ident step
     * @return the string
     */
    public static String xmlToJson(String xml, Json.JsonStringBuilder.Step identStep) {
        return xmlToJson(xml, identStep, null);
    }

    /**
     * Xml to json string.
     *
     * @param xml the xml
     * @param mode the mode
     * @return the string
     */
    public static String xmlToJson(String xml, XmlToJsonMode mode) {
        return xmlToJson(xml, Json.JsonStringBuilder.Step.TWO_SPACES, mode);
    }

    /**
     * Xml or json to json string.
     *
     * @param xmlOrJson the xml or json
     * @param identStep the ident step
     * @return the string
     */
    public static String xmlOrJsonToJson(String xmlOrJson, Json.JsonStringBuilder.Step identStep) {
        TextType textType = getTextType(xmlOrJson);
        final String result;
        if (textType == TextType.JSON) {
            result = getJsonString(identStep, fromJson(xmlOrJson));
        } else if (textType == TextType.XML) {
            result = getJsonString(identStep, fromXml(xmlOrJson));
        } else {
            result = xmlOrJson;
        }
        return result;
    }

    /**
     * Xml or json to json string.
     *
     * @param xmlOrJson the xml or json
     * @return the string
     */
    public static String xmlOrJsonToJson(String xmlOrJson) {
        return xmlOrJsonToJson(xmlOrJson, Json.JsonStringBuilder.Step.TWO_SPACES);
    }

    @SuppressWarnings("unchecked")
    private static String getJsonString(Json.JsonStringBuilder.Step identStep, Object object) {
        final String result;
        if (object instanceof Map) {
            result = Json.toJson((Map) object, identStep);
        } else {
            result = Json.toJson((List) object, identStep);
        }
        return result;
    }

    /**
     * Xml or json to xml string.
     *
     * @param xmlOrJson the xml or json
     * @param identStep the ident step
     * @return the string
     */
    public static String xmlOrJsonToXml(String xmlOrJson, Xml.XmlStringBuilder.Step identStep) {
        TextType textType = getTextType(xmlOrJson);
        final String result;
        if (textType == TextType.JSON) {
            result = getXmlString(identStep, fromJson(xmlOrJson));
        } else if (textType == TextType.XML) {
            result = getXmlString(identStep, fromXml(xmlOrJson));
        } else {
            result = xmlOrJson;
        }
        return result;
    }

    /**
     * Xml or json to xml string.
     *
     * @param xmlOrJson the xml or json
     * @return the string
     */
    public static String xmlOrJsonToXml(String xmlOrJson) {
        return xmlOrJsonToXml(xmlOrJson, Xml.XmlStringBuilder.Step.TWO_SPACES);
    }

    @SuppressWarnings("unchecked")
    private static String getXmlString(Xml.XmlStringBuilder.Step identStep, Object object) {
        final String result;
        if (object instanceof Map) {
            result = Xml.toXml((Map) object, identStep);
        } else {
            result = Xml.toXml((List) object, identStep);
        }
        return result;
    }

    /** The enum Text type. */
    public enum TextType {
        /** Json text type. */
        JSON,
        /** Xml text type. */
        XML,
        /** Other text type. */
        OTHER
    }

    /**
     * Gets text type.
     *
     * @param text the text
     * @return the text type
     */
    public static TextType getTextType(String text) {
        String trimmed = trim(text);
        final TextType textType;
        if (trimmed.startsWith("{") && trimmed.endsWith("}")
                || trimmed.startsWith("[") && trimmed.endsWith("]")) {
            textType = TextType.JSON;
        } else if (trimmed.startsWith("<") && trimmed.endsWith(">")) {
            textType = TextType.XML;
        } else {
            textType = TextType.OTHER;
        }
        return textType;
    }

    /**
     * Format json or xml string.
     *
     * @param jsonOrXml the json or xml
     * @param identStep the ident step
     * @return the string
     */
    public static String formatJsonOrXml(String jsonOrXml, String identStep) {
        TextType textType = getTextType(jsonOrXml);
        final String result;
        if (textType == TextType.JSON) {
            result = formatJson(jsonOrXml, Json.JsonStringBuilder.Step.valueOf(identStep));
        } else if (textType == TextType.XML) {
            result = formatXml(jsonOrXml, Xml.XmlStringBuilder.Step.valueOf(identStep));
        } else {
            result = jsonOrXml;
        }
        return result;
    }

    /**
     * Format json or xml string.
     *
     * @param jsonOrXml the json or xml
     * @return the string
     */
    public static String formatJsonOrXml(String jsonOrXml) {
        return formatJsonOrXml(jsonOrXml, "TWO_SPACES");
    }

    /**
     * Format json string.
     *
     * @param json the json
     * @param identStep the ident step
     * @return the string
     */
    public static String formatJson(String json, Json.JsonStringBuilder.Step identStep) {
        return Json.formatJson(json, identStep);
    }

    /**
     * Format json string.
     *
     * @param json the json
     * @return the string
     */
    public static String formatJson(String json) {
        return Json.formatJson(json);
    }

    /**
     * Format xml string.
     *
     * @param xml the xml
     * @param identStep the ident step
     * @return the string
     */
    public static String formatXml(String xml, Xml.XmlStringBuilder.Step identStep) {
        return Xml.formatXml(xml, identStep);
    }

    /**
     * Format xml string.
     *
     * @param xml the xml
     * @return the string
     */
    public static String formatXml(String xml) {
        return Xml.formatXml(xml);
    }

    /**
     * Change xml encoding string.
     *
     * @param xml the xml
     * @param identStep the ident step
     * @param encoding the encoding
     * @return the string
     */
    public static String changeXmlEncoding(
            String xml, Xml.XmlStringBuilder.Step identStep, String encoding) {
        return Xml.changeXmlEncoding(xml, identStep, encoding);
    }

    /**
     * Change xml encoding string.
     *
     * @param xml the xml
     * @param encoding the encoding
     * @return the string
     */
    public static String changeXmlEncoding(String xml, String encoding) {
        return Xml.changeXmlEncoding(xml, encoding);
    }

    /**
     * Remove minuses and convert numbers map.
     *
     * @param map the map
     * @return the map
     */
    public static Map<String, Object> removeMinusesAndConvertNumbers(Map<String, Object> map) {
        Map<String, Object> outMap = new LinkedHashMap<>();
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
            List<Object> values = new ArrayList<>();
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

    /**
     * Is json number boolean.
     *
     * @param string the string
     * @return the boolean
     */
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

    /**
     * Replace self closing with null map.
     *
     * @param map the map
     * @return the map
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> replaceSelfClosingWithNull(Map<String, Object> map) {
        return (Map<String, Object>) replaceSelfClosingWithValue(map, null);
    }

    /**
     * Replace self closing with empty map.
     *
     * @param map the map
     * @return the map
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> replaceSelfClosingWithEmpty(Map<String, Object> map) {
        Object result = replaceSelfClosingWithValue(map, "");
        if (result instanceof Map) {
            return (Map<String, Object>) result;
        }
        return Collections.emptyMap();
    }

    /**
     * Replace self closing with value object.
     *
     * @param map the map
     * @param value the value
     * @return the object
     */
    @SuppressWarnings("unchecked")
    public static Object replaceSelfClosingWithValue(Map<String, Object> map, String value) {
        Object outMap = new LinkedHashMap<>();
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
            List<Object> values = new ArrayList<>();
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

    /**
     * Replace empty value with null map.
     *
     * @param map the map
     * @return the map
     */
    public static Map<String, Object> replaceEmptyValueWithNull(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, Object> outMap = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            outMap.put(String.valueOf(entry.getKey()), makeObjectEmptyValue(entry.getValue()));
        }
        return outMap;
    }

    @SuppressWarnings("unchecked")
    private static Object makeObjectEmptyValue(Object value) {
        final Object result;
        if (value instanceof List) {
            List<Object> values = new ArrayList<>();
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

    /**
     * Replace empty value with empty string object.
     *
     * @param map the map
     * @return the object
     */
    public static Object replaceEmptyValueWithEmptyString(Map<String, Object> map) {
        if (map.isEmpty()) {
            return "";
        }
        Map<String, Object> outMap = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            outMap.put(String.valueOf(entry.getKey()), makeObjectEmptyString(entry.getValue()));
        }
        return outMap;
    }

    @SuppressWarnings("unchecked")
    private static Object makeObjectEmptyString(Object value) {
        final Object result;
        if (value instanceof List) {
            List<Object> values = new ArrayList<>();
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

    /**
     * Force attribute usage map.
     *
     * @param map the map
     * @return the map
     */
    public static Map<String, Object> forceAttributeUsage(Map<String, Object> map) {
        Map<String, Object> outMap = new LinkedHashMap<>();
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
            List<Object> values = new ArrayList<>();
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

    /**
     * Replace null with empty value map.
     *
     * @param map the map
     * @return the map
     */
    public static Map<String, Object> replaceNullWithEmptyValue(Map<String, Object> map) {
        Map<String, Object> outMap = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            outMap.put(
                    entry.getKey(),
                    entry.getValue() == null
                            ? new LinkedHashMap<>()
                            : makeReplaceNullValue(entry.getValue()));
        }
        return outMap;
    }

    @SuppressWarnings("unchecked")
    private static Object makeReplaceNullValue(Object value) {
        final Object result;
        if (value instanceof List) {
            List<Object> values = new ArrayList<>();
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

    /**
     * Replace empty string with empty value map.
     *
     * @param map the map
     * @return the map
     */
    public static Map<String, Object> replaceEmptyStringWithEmptyValue(Map<String, Object> map) {
        Map<String, Object> outMap = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            outMap.put(
                    entry.getKey(),
                    "".equals(entry.getValue())
                            ? new LinkedHashMap<>()
                            : makeReplaceEmptyString(entry.getValue()));
        }
        return outMap;
    }

    @SuppressWarnings("unchecked")
    private static Object makeReplaceEmptyString(Object value) {
        final Object result;
        if (value instanceof List) {
            List<Object> values = new ArrayList<>();
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

    /**
     * Replace number and boolean with string map.
     *
     * @param map the map
     * @return the map
     */
    public static Map<String, Object> replaceNumberAndBooleanWithString(Map<String, Object> map) {
        Map<String, Object> outMap = new LinkedHashMap<>();
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
            List<Object> values = new ArrayList<>();
            for (Object item : (List) value) {
                if (item instanceof Map) {
                    values.add(replaceNumberAndBooleanWithString((Map) item));
                } else if (item instanceof Number || item instanceof Boolean || isNull(item)) {
                    values.add(String.valueOf(item));
                } else {
                    values.add(item);
                }
            }
            result = values;
        } else if (value instanceof Map) {
            result = replaceNumberAndBooleanWithString((Map) value);
        } else if (isNull(value)) {
            result = "null";
        } else {
            result = value;
        }
        return result;
    }

    /**
     * Replace first level map.
     *
     * @param map the map
     * @return the map
     */
    public static Map<String, Object> replaceFirstLevel(Map<String, Object> map) {
        return replaceFirstLevel(map, 0);
    }

    /**
     * Replace first level map.
     *
     * @param map the map
     * @param level the level
     * @return the map
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> replaceFirstLevel(Map<String, Object> map, int level) {
        Map<String, Object> outMap = new LinkedHashMap<>();
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
            List<Object> values = new ArrayList<>();
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

    /**
     * Replace nil with null map.
     *
     * @param map the map
     * @return the map
     */
    public static Map<String, Object> replaceNilWithNull(Map<String, Object> map) {
        Map<String, Object> outMap = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object outValue = makeReplaceNilWithNull(entry.getValue());
            if (outValue instanceof Map
                    && (nilKey.equals(Xml.XmlValue.getMapKey(outValue))
                            || Xml.XmlValue.getMapKey(outValue).endsWith(":nil"))
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
            List<Object> values = new ArrayList<>();
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

    /**
     * Deep copy map map.
     *
     * @param map the map
     * @return the map
     */
    public static Map<String, Object> deepCopyMap(Map<String, Object> map) {
        Map<String, Object> outMap = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            outMap.put(entry.getKey(), makeDeepCopyMap(entry.getValue()));
        }
        return outMap;
    }

    @SuppressWarnings("unchecked")
    private static Object makeDeepCopyMap(Object value) {
        final Object result;
        if (value instanceof List) {
            List<Object> values = new ArrayList<>();
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

    /**
     * Object builder builder.
     *
     * @return the builder
     */
    public static Builder objectBuilder() {
        return new U.Builder();
    }

    /** The type Builder. */
    public static class Builder {
        private final Map<String, Object> data;

        /** Instantiates a new Builder. */
        public Builder() {
            data = new LinkedHashMap<>();
        }

        /**
         * Add builder.
         *
         * @param key the key
         * @param value the value
         * @return the builder
         */
        public Builder add(final String key, final Object value) {
            data.put(key, value);
            return this;
        }

        /**
         * Add builder.
         *
         * @param value the value
         * @return the builder
         */
        public Builder add(final Object value) {
            data.put(String.valueOf(data.size()), value);
            return this;
        }

        /**
         * Get t.
         *
         * @param <T> the type parameter
         * @param path the path
         * @return the t
         */
        public <T> T get(final String path) {
            return U.get(data, path);
        }

        /**
         * Get t.
         *
         * @param <T> the type parameter
         * @param paths the paths
         * @return the t
         */
        public <T> T get(final List<String> paths) {
            return U.get(data, paths);
        }

        /**
         * Set builder.
         *
         * @param path the path
         * @param value the value
         * @return the builder
         */
        public Builder set(final String path, final Object value) {
            U.set(data, path, value);
            return this;
        }

        /**
         * Set builder.
         *
         * @param paths the paths
         * @param value the value
         * @return the builder
         */
        public Builder set(final List<String> paths, final Object value) {
            U.set(data, paths, value);
            return this;
        }

        /**
         * Remove builder.
         *
         * @param key the key
         * @return the builder
         */
        public Builder remove(final String key) {
            U.remove(data, key);
            return this;
        }

        /**
         * Remove builder.
         *
         * @param keys the keys
         * @return the builder
         */
        public Builder remove(final List<String> keys) {
            U.remove(data, keys);
            return this;
        }

        /**
         * Clear builder.
         *
         * @return the builder
         */
        public Builder clear() {
            data.clear();
            return this;
        }

        /**
         * Is empty boolean.
         *
         * @return the boolean
         */
        public boolean isEmpty() {
            return data.isEmpty();
        }

        /**
         * Size int.
         *
         * @return the int
         */
        public int size() {
            return data.size();
        }

        /**
         * Add builder.
         *
         * @param builder the builder
         * @return the builder
         */
        public Builder add(final Builder builder) {
            data.put(String.valueOf(data.size()), builder.build());
            return this;
        }

        /**
         * Add builder.
         *
         * @param key the key
         * @param builder the builder
         * @return the builder
         */
        public Builder add(final String key, final ArrayBuilder builder) {
            data.put(key, builder.build());
            return this;
        }

        /**
         * Add builder.
         *
         * @param key the key
         * @param builder the builder
         * @return the builder
         */
        public Builder add(final String key, final Builder builder) {
            data.put(key, builder.build());
            return this;
        }

        /**
         * Add builder.
         *
         * @param map the map
         * @return the builder
         */
        public Builder add(final Map<String, Object> map) {
            data.putAll(deepCopyMap(map));
            return this;
        }

        /**
         * Update builder.
         *
         * @param map the map
         * @return the builder
         */
        public Builder update(final Map<String, Object> map) {
            U.update(data, deepCopyMap(map));
            return this;
        }

        /**
         * Add null builder.
         *
         * @param key the key
         * @return the builder
         */
        public Builder addNull(final String key) {
            data.put(key, null);
            return this;
        }

        /**
         * Build map.
         *
         * @return the map
         */
        @SuppressWarnings("unchecked")
        public Map<String, Object> build() {
            return (Map<String, Object>) ((LinkedHashMap) data).clone();
        }

        /**
         * To xml string.
         *
         * @return the string
         */
        public String toXml() {
            return Xml.toXml(data);
        }

        /**
         * From xml builder.
         *
         * @param xml the xml
         * @return the builder
         */
        public static Builder fromXml(final String xml) {
            final Builder builder = new Builder();
            builder.data.putAll(fromXmlMap(xml));
            return builder;
        }

        /**
         * From map builder.
         *
         * @param map the map
         * @return the builder
         */
        public static Builder fromMap(final Map<String, Object> map) {
            final Builder builder = new Builder();
            builder.data.putAll(deepCopyMap(map));
            return builder;
        }

        /**
         * To json string.
         *
         * @return the string
         */
        public String toJson() {
            return Json.toJson(data);
        }

        /**
         * From json builder.
         *
         * @param json the json
         * @return the builder
         */
        public static Builder fromJson(final String json) {
            final Builder builder = new Builder();
            builder.data.putAll(fromJsonMap(json));
            return builder;
        }

        /**
         * To chain chain.
         *
         * @return the chain
         */
        public Chain<Object> toChain() {
            return new U.Chain<>(data.entrySet());
        }

        @Override
        public String toString() {
            return data.toString();
        }
    }

    /**
     * Array builder array builder.
     *
     * @return the array builder
     */
    public static ArrayBuilder arrayBuilder() {
        return new U.ArrayBuilder();
    }

    /** The type Array builder. */
    public static class ArrayBuilder {
        private final List<Object> data;

        /** Instantiates a new Array builder. */
        public ArrayBuilder() {
            data = new ArrayList<>();
        }

        /**
         * Add array builder.
         *
         * @param value the value
         * @return the array builder
         */
        public ArrayBuilder add(final Object value) {
            data.add(value);
            return this;
        }

        /**
         * Add null array builder.
         *
         * @return the array builder
         */
        public ArrayBuilder addNull() {
            data.add(null);
            return this;
        }

        /**
         * Get t.
         *
         * @param <T> the type parameter
         * @param path the path
         * @return the t
         */
        public <T> T get(final String path) {
            return U.get(U.getStringObjectMap(data), "value." + path);
        }

        /**
         * Get t.
         *
         * @param <T> the type parameter
         * @param paths the paths
         * @return the t
         */
        public <T> T get(final List<String> paths) {
            List<String> newPaths = new ArrayList<>();
            newPaths.add("value");
            newPaths.addAll(paths);
            return U.get(U.getStringObjectMap(data), newPaths);
        }

        /**
         * Set array builder.
         *
         * @param index the index
         * @param value the value
         * @return the array builder
         */
        public ArrayBuilder set(final int index, final Object value) {
            data.set(index, value);
            return this;
        }

        /**
         * Remove array builder.
         *
         * @param index the index
         * @return the array builder
         */
        public ArrayBuilder remove(final int index) {
            data.remove(index);
            return this;
        }

        /**
         * Clear array builder.
         *
         * @return the array builder
         */
        public ArrayBuilder clear() {
            data.clear();
            return this;
        }

        /**
         * Is empty boolean.
         *
         * @return the boolean
         */
        public boolean isEmpty() {
            return data.isEmpty();
        }

        /**
         * Size int.
         *
         * @return the int
         */
        public int size() {
            return data.size();
        }

        /**
         * Add array builder.
         *
         * @param builder the builder
         * @return the array builder
         */
        public ArrayBuilder add(final ArrayBuilder builder) {
            data.addAll(builder.build());
            return this;
        }

        /**
         * Add array builder.
         *
         * @param builder the builder
         * @return the array builder
         */
        public ArrayBuilder add(final Builder builder) {
            data.add(builder.build());
            return this;
        }

        /**
         * Merge array builder.
         *
         * @param list the list
         * @return the array builder
         */
        @SuppressWarnings("unchecked")
        public ArrayBuilder merge(final List<Object> list) {
            U.merge(data, (List<Object>) ((ArrayList) list).clone());
            return this;
        }

        /**
         * Build list.
         *
         * @return the list
         */
        @SuppressWarnings("unchecked")
        public List<Object> build() {
            return (List<Object>) ((ArrayList) data).clone();
        }

        /**
         * To xml string.
         *
         * @return the string
         */
        public String toXml() {
            return Xml.toXml(data);
        }

        /**
         * From xml array builder.
         *
         * @param xml the xml
         * @return the array builder
         */
        public static ArrayBuilder fromXml(final String xml) {
            final ArrayBuilder builder = new ArrayBuilder();
            builder.data.addAll(U.<List<Object>>fromXml(xml));
            return builder;
        }

        /**
         * To json string.
         *
         * @return the string
         */
        public String toJson() {
            return Json.toJson(data);
        }

        /**
         * From json array builder.
         *
         * @param json the json
         * @return the array builder
         */
        public static ArrayBuilder fromJson(final String json) {
            final ArrayBuilder builder = new ArrayBuilder();
            builder.data.addAll(U.<List<Object>>fromJson(json));
            return builder;
        }

        /**
         * To chain chain.
         *
         * @return the chain
         */
        public Chain<Object> toChain() {
            return new U.Chain<>(data);
        }

        @Override
        public String toString() {
            return data.toString();
        }
    }

    /**
     * Properties to map map.
     *
     * @param properties the properties
     * @return the map
     */
    public static Map<String, Object> propertiesToMap(Properties properties) {
        Map<String, Object> map = new LinkedHashMap<>();
        if (properties != null && !properties.isEmpty()) {
            Enumeration<?> enumProperties = properties.propertyNames();
            while (enumProperties.hasMoreElements()) {
                String name = (String) enumProperties.nextElement();
                map.put(name, properties.getProperty(name));
            }
        }
        return map;
    }

    /**
     * Map to properties properties.
     *
     * @param map the map
     * @return the properties
     */
    public static Properties mapToProperties(Map<String, Object> map) {
        Properties properties = new Properties();
        if (map != null) {
            for (final Map.Entry<String, Object> entry : map.entrySet()) {
                if (!isNull(entry.getValue())) {
                    properties.put(entry.getKey(), String.valueOf(entry.getValue()));
                }
            }
        }
        return properties;
    }
}
