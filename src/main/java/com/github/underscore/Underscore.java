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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * Underscore-java is a java port of Underscore.js.
 *
 * @param <T> the type parameter
 * @author Valentyn Kolesnikov
 */
@SuppressWarnings({
    "java:S106",
    "java:S2189",
    "java:S2272",
    "java:S2789",
    "java:S3740",
    "java:S5852"
})
public class Underscore<T> {
    private static final Map<String, Function<String, String>> FUNCTIONS = new LinkedHashMap<>();
    private static final Map<String, String> TEMPLATE_SETTINGS = new HashMap<>();
    private static final int MIN_PASSWORD_LENGTH_8 = 8;
    private static final long CAPACITY_SIZE_5 = 5L;
    private static final long CAPACITY_COEFF_2 = 2L;
    private static final long CAPACITY_SIZE_16 = 16L;
    private static final java.util.concurrent.atomic.AtomicInteger UNIQUE_ID =
            new java.util.concurrent.atomic.AtomicInteger(0);
    private static final String ALL_SYMBOLS = "([\\s\\S]+?)";
    private static final String EVALUATE = "evaluate";
    private static final String INTERPOLATE = "interpolate";
    private static final String ESCAPE = "escape";
    private static final String S_Q = "\\s*\\Q";
    private static final String E_S = "\\E\\s*";
    private static final java.util.regex.Pattern FORMAT_PATTERN =
            java.util.regex.Pattern.compile("\\{\\s*(\\d*)\\s*\\}");
    private static final Map<Character, String> ESCAPES = new HashMap<>();
    private final Iterable<T> iterable;
    private final Optional<String> string;

    static {
        TEMPLATE_SETTINGS.put(EVALUATE, "<%([\\s\\S]+?)%>");
        TEMPLATE_SETTINGS.put(INTERPOLATE, "<%=([\\s\\S]+?)%>");
        TEMPLATE_SETTINGS.put(ESCAPE, "<%-([\\s\\S]+?)%>");
        ESCAPES.put('&', "&amp;");
        ESCAPES.put('<', "&lt;");
        ESCAPES.put('>', "&gt;");
        ESCAPES.put('"', "&quot;");
        ESCAPES.put('\'', "&#x27;");
        ESCAPES.put('`', "&#x60;");
    }

    /**
     * Instantiates a new Underscore.
     *
     * @param iterable the iterable
     */
    public Underscore(final Iterable<T> iterable) {
        this.iterable = iterable;
        this.string = Optional.empty();
    }

    /**
     * Instantiates a new Underscore.
     *
     * @param string the string
     */
    public Underscore(final String string) {
        this.iterable = null;
        this.string = Optional.of(string);
    }

    private static void setTemplateKey(
            final Map<String, String> templateSettings, final String key) {
        if (templateSettings.containsKey(key) && templateSettings.get(key).contains(ALL_SYMBOLS)) {
            TEMPLATE_SETTINGS.put(key, templateSettings.get(key));
        }
    }

    /**
     * Template settings.
     *
     * @param templateSettings the template settings
     */
    public static void templateSettings(final Map<String, String> templateSettings) {
        setTemplateKey(templateSettings, EVALUATE);
        setTemplateKey(templateSettings, INTERPOLATE);
        setTemplateKey(templateSettings, ESCAPE);
    }

    private static final class WherePredicate<E, T> implements Predicate<E> {
        private final List<Map.Entry<String, T>> properties;

        private WherePredicate(List<Map.Entry<String, T>> properties) {
            this.properties = properties;
        }

        @Override
        public boolean test(final E elem) {
            for (Map.Entry<String, T> prop : properties) {
                try {
                    if (!elem.getClass()
                            .getField(prop.getKey())
                            .get(elem)
                            .equals(prop.getValue())) {
                        return false;
                    }
                } catch (Exception ex) {
                    try {
                        if (!elem.getClass()
                                .getMethod(prop.getKey())
                                .invoke(elem)
                                .equals(prop.getValue())) {
                            return false;
                        }
                    } catch (Exception ignored) {
                        // ignored
                    }
                }
            }
            return true;
        }
    }

    private static final class TemplateImpl<K, V> implements Template<Map<K, V>> {
        private final String template;

        private TemplateImpl(String template) {
            this.template = template;
        }

        @Override
        public String apply(Map<K, V> value) {
            final String evaluate = TEMPLATE_SETTINGS.get(EVALUATE);
            final String interpolate = TEMPLATE_SETTINGS.get(INTERPOLATE);
            final String escape = TEMPLATE_SETTINGS.get(ESCAPE);
            String result = template;
            for (final Map.Entry<K, V> element : value.entrySet()) {
                final String value1 =
                        String.valueOf(element.getValue())
                                .replace("\\", "\\\\")
                                .replace("$", "\\$");
                result =
                        java.util.regex.Pattern.compile(
                                        interpolate.replace(
                                                ALL_SYMBOLS, S_Q + element.getKey() + E_S))
                                .matcher(result)
                                .replaceAll(value1);
                result =
                        java.util.regex.Pattern.compile(
                                        escape.replace(ALL_SYMBOLS, S_Q + element.getKey() + E_S))
                                .matcher(result)
                                .replaceAll(escape(value1));
                result =
                        java.util.regex.Pattern.compile(
                                        evaluate.replace(ALL_SYMBOLS, S_Q + element.getKey() + E_S))
                                .matcher(result)
                                .replaceAll(value1);
            }
            return result;
        }

        @Override
        public List<String> check(Map<K, V> value) {
            final String evaluate = TEMPLATE_SETTINGS.get(EVALUATE);
            final String interpolate = TEMPLATE_SETTINGS.get(INTERPOLATE);
            final String escape = TEMPLATE_SETTINGS.get(ESCAPE);
            String result = template;
            final List<String> notFound = new ArrayList<>();
            final List<String> valueKeys = new ArrayList<>();
            for (final Map.Entry<K, V> element : value.entrySet()) {
                final String key = "" + element.getKey();
                java.util.regex.Matcher matcher =
                        java.util.regex.Pattern.compile(
                                        interpolate.replace(ALL_SYMBOLS, S_Q + key + E_S))
                                .matcher(result);
                boolean isFound = matcher.find();
                result = matcher.replaceAll(String.valueOf(element.getValue()));
                matcher =
                        java.util.regex.Pattern.compile(
                                        escape.replace(ALL_SYMBOLS, S_Q + key + E_S))
                                .matcher(result);
                isFound |= matcher.find();
                result = matcher.replaceAll(escape(String.valueOf(element.getValue())));
                matcher =
                        java.util.regex.Pattern.compile(
                                        evaluate.replace(ALL_SYMBOLS, S_Q + key + E_S))
                                .matcher(result);
                isFound |= matcher.find();
                result = matcher.replaceAll(String.valueOf(element.getValue()));
                if (!isFound) {
                    notFound.add(key);
                }
                valueKeys.add(key);
            }
            final List<String> templateVars = new ArrayList<>();
            java.util.regex.Matcher matcher =
                    java.util.regex.Pattern.compile(interpolate).matcher(result);
            while (matcher.find()) {
                templateVars.add(matcher.group(1).trim());
            }
            result = matcher.replaceAll("");
            matcher = java.util.regex.Pattern.compile(escape).matcher(result);
            while (matcher.find()) {
                templateVars.add(matcher.group(1).trim());
            }
            result = matcher.replaceAll("");
            matcher = java.util.regex.Pattern.compile(evaluate).matcher(result);
            while (matcher.find()) {
                templateVars.add(matcher.group(1).trim());
            }
            notFound.addAll(difference(templateVars, valueKeys));
            return notFound;
        }
    }

    private static final class MyIterable<T> implements Iterable<T> {
        private final UnaryOperator<T> unaryOperator;
        private boolean firstRun = true;
        private T value;

        /**
         * Instantiates a new My iterable.
         *
         * @param seed the seed
         * @param unaryOperator the unary operator
         */
        MyIterable(final T seed, final UnaryOperator<T> unaryOperator) {
            this.value = seed;
            this.unaryOperator = unaryOperator;
        }

        public Iterator<T> iterator() {
            return new Iterator<>() {
                @Override
                public boolean hasNext() {
                    return true;
                }

                @Override
                public T next() {
                    if (firstRun) {
                        firstRun = false;
                    } else {
                        value = unaryOperator.apply(value);
                    }
                    return value;
                }

                @Override
                public void remove() {
                    // ignored
                }
            };
        }
    }

    /**
     * Iteratee function.
     *
     * @param <K> the type parameter
     * @param <V> the type parameter
     * @param key the key
     * @return the function
     */
    public static <K, V> Function<Map<K, V>, V> iteratee(final K key) {
        return item -> item.get(key);
    }

    /**
     * Each.
     *
     * @param <T> the type parameter
     * @param iterable the iterable
     * @param func the func
     */
    /*
     * Documented, #each
     */
    public static <T> void each(final Iterable<T> iterable, final Consumer<? super T> func) {
        for (T element : iterable) {
            func.accept(element);
        }
    }

    /**
     * Each indexed.
     *
     * @param <T> the type parameter
     * @param iterable the iterable
     * @param func the func
     */
    public static <T> void eachIndexed(
            final Iterable<T> iterable, final BiConsumer<Integer, ? super T> func) {
        int index = 0;
        for (T element : iterable) {
            func.accept(index, element);
            index += 1;
        }
    }

    /**
     * Each.
     *
     * @param func the func
     */
    public void each(final Consumer<? super T> func) {
        each(iterable, func);
    }

    /**
     * Each right.
     *
     * @param <T> the type parameter
     * @param iterable the iterable
     * @param func the func
     */
    public static <T> void eachRight(final Iterable<T> iterable, final Consumer<? super T> func) {
        each(reverse(iterable), func);
    }

    /**
     * Each right.
     *
     * @param func the func
     */
    public void eachRight(final Consumer<? super T> func) {
        eachRight(iterable, func);
    }

    /**
     * For each.
     *
     * @param <T> the type parameter
     * @param iterable the iterable
     * @param func the func
     */
    public static <T> void forEach(final Iterable<T> iterable, final Consumer<? super T> func) {
        each(iterable, func);
    }

    /**
     * For each indexed.
     *
     * @param <T> the type parameter
     * @param iterable the iterable
     * @param func the func
     */
    public static <T> void forEachIndexed(
            final Iterable<T> iterable, final BiConsumer<Integer, ? super T> func) {
        eachIndexed(iterable, func);
    }

    /**
     * For each.
     *
     * @param func the func
     */
    public void forEach(final Consumer<? super T> func) {
        each(iterable, func);
    }

    /**
     * For each indexed.
     *
     * @param func the func
     */
    public void forEachIndexed(final BiConsumer<Integer, ? super T> func) {
        eachIndexed(iterable, func);
    }

    /**
     * For each right.
     *
     * @param <T> the type parameter
     * @param iterable the iterable
     * @param func the func
     */
    public static <T> void forEachRight(
            final Iterable<T> iterable, final Consumer<? super T> func) {
        eachRight(iterable, func);
    }

    /**
     * For each right.
     *
     * @param func the func
     */
    public void forEachRight(final Consumer<? super T> func) {
        eachRight(iterable, func);
    }

    /**
     * Map list.
     *
     * @param <T> the type parameter
     * @param <E> the type parameter
     * @param list the list
     * @param func the func
     * @return the list
     */
    /*
     * Documented, #map
     */
    public static <T, E> List<T> map(final List<E> list, final Function<? super E, T> func) {
        final List<T> transformed = newArrayListWithExpectedSize(list.size());
        for (E element : list) {
            transformed.add(func.apply(element));
        }
        return transformed;
    }

    /**
     * Map multi list.
     *
     * @param <T> the type parameter
     * @param <E> the type parameter
     * @param list the list
     * @param mapper the mapper
     * @return the list
     */
    public static <T, E> List<T> mapMulti(
            final List<E> list, final BiConsumer<? super E, ? super Consumer<T>> mapper) {
        final List<T> transformed = newArrayListWithExpectedSize(list.size());
        for (E element : list) {
            Consumer<T> value = transformed::add;
            mapper.accept(element, value);
        }
        return transformed;
    }

    /**
     * Map list.
     *
     * @param <F> the type parameter
     * @param func the func
     * @return the list
     */
    public <F> List<F> map(final Function<? super T, F> func) {
        return map(newArrayList(iterable), func);
    }

    /**
     * Map list.
     *
     * @param <T> the type parameter
     * @param array the array
     * @param func the func
     * @return the list
     */
    public static <T> List<T> map(final int[] array, final Function<? super Integer, T> func) {
        final List<T> transformed = newArrayListWithExpectedSize(array.length);
        for (int element : array) {
            transformed.add(func.apply(element));
        }
        return transformed;
    }

    /**
     * Map set.
     *
     * @param <T> the type parameter
     * @param <E> the type parameter
     * @param set the set
     * @param func the func
     * @return the set
     */
    public static <T, E> Set<T> map(final Set<E> set, final Function<? super E, T> func) {
        final Set<T> transformed = newLinkedHashSetWithExpectedSize(set.size());
        for (E element : set) {
            transformed.add(func.apply(element));
        }
        return transformed;
    }

    /**
     * Map indexed list.
     *
     * @param <T> the type parameter
     * @param <E> the type parameter
     * @param list the list
     * @param func the func
     * @return the list
     */
    public static <T, E> List<T> mapIndexed(
            final List<E> list, final BiFunction<Integer, ? super E, T> func) {
        final List<T> transformed = newArrayListWithExpectedSize(list.size());
        int index = 0;
        for (E element : list) {
            transformed.add(func.apply(index, element));
            index += 1;
        }
        return transformed;
    }

    /**
     * Replace list.
     *
     * @param <T> the type parameter
     * @param iter the iter
     * @param pred the pred
     * @param value the value
     * @return the list
     */
    public static <T> List<T> replace(
            final Iterable<T> iter, final Predicate<T> pred, final T value) {
        List<T> list = newArrayList(iter);
        if (pred == null) {
            return list;
        }
        ListIterator<T> itera = list.listIterator();
        while (itera.hasNext()) {
            if (pred.test(itera.next())) {
                itera.set(value);
            }
        }
        return list;
    }

    /**
     * Replace list.
     *
     * @param pred the pred
     * @param value the value
     * @return the list
     */
    public List<T> replace(final Predicate<T> pred, final T value) {
        return replace(value(), pred, value);
    }

    /**
     * Replace indexed list.
     *
     * @param <T> the type parameter
     * @param iter the iter
     * @param pred the pred
     * @param value the value
     * @return the list
     */
    public static <T> List<T> replaceIndexed(
            final Iterable<T> iter, final PredicateIndexed<T> pred, final T value) {
        List<T> list = newArrayList(iter);
        if (pred == null) {
            return list;
        }
        ListIterator<T> itera = list.listIterator();
        int index = 0;
        while (itera.hasNext()) {
            if (pred.test(index, itera.next())) {
                itera.set(value);
            }
            index++;
        }
        return list;
    }

    /**
     * Replace indexed list.
     *
     * @param pred the pred
     * @param value the value
     * @return the list
     */
    public List<T> replaceIndexed(final PredicateIndexed<T> pred, final T value) {
        return replaceIndexed(value(), pred, value);
    }

    /**
     * Map indexed list.
     *
     * @param <F> the type parameter
     * @param func the func
     * @return the list
     */
    public <F> List<F> mapIndexed(final BiFunction<Integer, ? super T, F> func) {
        return mapIndexed(newArrayList(iterable), func);
    }

    /**
     * Collect list.
     *
     * @param <T> the type parameter
     * @param <E> the type parameter
     * @param list the list
     * @param func the func
     * @return the list
     */
    public static <T, E> List<T> collect(final List<E> list, final Function<? super E, T> func) {
        return map(list, func);
    }

    /**
     * Collect set.
     *
     * @param <T> the type parameter
     * @param <E> the type parameter
     * @param set the set
     * @param func the func
     * @return the set
     */
    public static <T, E> Set<T> collect(final Set<E> set, final Function<? super E, T> func) {
        return map(set, func);
    }

    /**
     * Reduce e.
     *
     * @param <T> the type parameter
     * @param <E> the type parameter
     * @param iterable the iterable
     * @param func the func
     * @param zeroElem the zero elem
     * @return the e
     */
    /*
     * Documented, #reduce
     */
    public static <T, E> E reduce(
            final Iterable<T> iterable, final BiFunction<E, T, E> func, final E zeroElem) {
        E accum = zeroElem;
        for (T element : iterable) {
            accum = func.apply(accum, element);
        }
        return accum;
    }

    /**
     * Reduce optional.
     *
     * @param <T> the type parameter
     * @param iterable the iterable
     * @param func the func
     * @return the optional
     */
    public static <T> Optional<T> reduce(final Iterable<T> iterable, final BinaryOperator<T> func) {
        boolean foundAny = false;
        T accum = null;
        for (T element : iterable) {
            if (foundAny) {
                accum = func.apply(accum, element);
            } else {
                foundAny = true;
                accum = element;
            }
        }
        return foundAny ? Optional.of(accum) : Optional.empty();
    }

    /**
     * Reduce e.
     *
     * @param <E> the type parameter
     * @param array the array
     * @param func the func
     * @param zeroElem the zero elem
     * @return the e
     */
    public static <E> E reduce(
            final int[] array, final BiFunction<E, ? super Integer, E> func, final E zeroElem) {
        E accum = zeroElem;
        for (int element : array) {
            accum = func.apply(accum, element);
        }
        return accum;
    }

    /**
     * Reduce e.
     *
     * @param <T> the type parameter
     * @param <E> the type parameter
     * @param array the array
     * @param func the func
     * @param zeroElem the zero elem
     * @return the e
     */
    public static <T, E> E reduce(
            final T[] array, final BiFunction<E, T, E> func, final E zeroElem) {
        E accum = zeroElem;
        for (T element : array) {
            accum = func.apply(accum, element);
        }
        return accum;
    }

    /**
     * Foldl e.
     *
     * @param <T> the type parameter
     * @param <E> the type parameter
     * @param iterable the iterable
     * @param func the func
     * @param zeroElem the zero elem
     * @return the e
     */
    public static <T, E> E foldl(
            final Iterable<T> iterable, final BiFunction<E, T, E> func, final E zeroElem) {
        return reduce(iterable, func, zeroElem);
    }

    /**
     * Inject e.
     *
     * @param <T> the type parameter
     * @param <E> the type parameter
     * @param iterable the iterable
     * @param func the func
     * @param zeroElem the zero elem
     * @return the e
     */
    public static <T, E> E inject(
            final Iterable<T> iterable, final BiFunction<E, T, E> func, final E zeroElem) {
        return reduce(iterable, func, zeroElem);
    }

    /**
     * Reduce right e.
     *
     * @param <T> the type parameter
     * @param <E> the type parameter
     * @param iterable the iterable
     * @param func the func
     * @param zeroElem the zero elem
     * @return the e
     */
    /*
     * Documented, #reduceRight
     */
    public static <T, E> E reduceRight(
            final Iterable<T> iterable, final BiFunction<E, T, E> func, final E zeroElem) {
        return reduce(reverse(iterable), func, zeroElem);
    }

    /**
     * Reduce right optional.
     *
     * @param <T> the type parameter
     * @param iterable the iterable
     * @param func the func
     * @return the optional
     */
    public static <T> Optional<T> reduceRight(
            final Iterable<T> iterable, final BinaryOperator<T> func) {
        return reduce(reverse(iterable), func);
    }

    /**
     * Reduce right e.
     *
     * @param <E> the type parameter
     * @param array the array
     * @param func the func
     * @param zeroElem the zero elem
     * @return the e
     */
    public static <E> E reduceRight(
            final int[] array, final BiFunction<E, ? super Integer, E> func, final E zeroElem) {
        E accum = zeroElem;
        for (Integer element : reverse(array)) {
            accum = func.apply(accum, element);
        }
        return accum;
    }

    /**
     * Reduce right e.
     *
     * @param <T> the type parameter
     * @param <E> the type parameter
     * @param array the array
     * @param func the func
     * @param zeroElem the zero elem
     * @return the e
     */
    public static <T, E> E reduceRight(
            final T[] array, final BiFunction<E, T, E> func, final E zeroElem) {
        return reduce(reverse(array), func, zeroElem);
    }

    /**
     * Foldr e.
     *
     * @param <T> the type parameter
     * @param <E> the type parameter
     * @param iterable the iterable
     * @param func the func
     * @param zeroElem the zero elem
     * @return the e
     */
    public static <T, E> E foldr(
            final Iterable<T> iterable, final BiFunction<E, T, E> func, final E zeroElem) {
        return reduceRight(iterable, func, zeroElem);
    }

    /**
     * Find optional.
     *
     * @param <E> the type parameter
     * @param iterable the iterable
     * @param pred the pred
     * @return the optional
     */
    /*
     * Documented, #find
     */
    public static <E> Optional<E> find(final Iterable<E> iterable, final Predicate<E> pred) {
        for (E element : iterable) {
            if (pred.test(element)) {
                return isNull(element) ? null : Optional.of(element);
            }
        }
        return Optional.empty();
    }

    /**
     * Detect optional.
     *
     * @param <E> the type parameter
     * @param iterable the iterable
     * @param pred the pred
     * @return the optional
     */
    public static <E> Optional<E> detect(final Iterable<E> iterable, final Predicate<E> pred) {
        return find(iterable, pred);
    }

    /**
     * Find last optional.
     *
     * @param <E> the type parameter
     * @param iterable the iterable
     * @param pred the pred
     * @return the optional
     */
    public static <E> Optional<E> findLast(final Iterable<E> iterable, final Predicate<E> pred) {
        return find(reverse(iterable), pred);
    }

    /**
     * Filter list.
     *
     * @param <E> the type parameter
     * @param iterable the iterable
     * @param pred the pred
     * @return the list
     */
    /*
     * Documented, #filter
     */
    public static <E> List<E> filter(final Iterable<E> iterable, final Predicate<E> pred) {
        final List<E> filtered = new ArrayList<>();
        for (E element : iterable) {
            if (pred.test(element)) {
                filtered.add(element);
            }
        }
        return filtered;
    }

    /**
     * Filter list.
     *
     * @param <E> the type parameter
     * @param list the list
     * @param pred the pred
     * @return the list
     */
    public static <E> List<E> filter(final List<E> list, final Predicate<E> pred) {
        final List<E> filtered = new ArrayList<>();
        for (E element : list) {
            if (pred.test(element)) {
                filtered.add(element);
            }
        }
        return filtered;
    }

    /**
     * Filter list.
     *
     * @param pred the pred
     * @return the list
     */
    public List<T> filter(final Predicate<T> pred) {
        final List<T> filtered = new ArrayList<>();
        for (final T element : value()) {
            if (pred.test(element)) {
                filtered.add(element);
            }
        }
        return filtered;
    }

    /**
     * Filter indexed list.
     *
     * @param <E> the type parameter
     * @param list the list
     * @param pred the pred
     * @return the list
     */
    public static <E> List<E> filterIndexed(final List<E> list, final PredicateIndexed<E> pred) {
        final List<E> filtered = new ArrayList<>();
        int index = 0;
        for (E element : list) {
            if (pred.test(index, element)) {
                filtered.add(element);
            }
            index += 1;
        }
        return filtered;
    }

    /**
     * Filter set.
     *
     * @param <E> the type parameter
     * @param set the set
     * @param pred the pred
     * @return the set
     */
    public static <E> Set<E> filter(final Set<E> set, final Predicate<E> pred) {
        final Set<E> filtered = new LinkedHashSet<>();
        for (E element : set) {
            if (pred.test(element)) {
                filtered.add(element);
            }
        }
        return filtered;
    }

    /**
     * Select list.
     *
     * @param <E> the type parameter
     * @param list the list
     * @param pred the pred
     * @return the list
     */
    public static <E> List<E> select(final List<E> list, final Predicate<E> pred) {
        return filter(list, pred);
    }

    /**
     * Select set.
     *
     * @param <E> the type parameter
     * @param set the set
     * @param pred the pred
     * @return the set
     */
    public static <E> Set<E> select(final Set<E> set, final Predicate<E> pred) {
        return filter(set, pred);
    }

    /**
     * Reject list.
     *
     * @param <E> the type parameter
     * @param list the list
     * @param pred the pred
     * @return the list
     */
    /*
     * Documented, #reject
     */
    public static <E> List<E> reject(final List<E> list, final Predicate<E> pred) {
        return filter(list, input -> !pred.test(input));
    }

    /**
     * Reject list.
     *
     * @param pred the pred
     * @return the list
     */
    public List<T> reject(final Predicate<T> pred) {
        return filter(input -> !pred.test(input));
    }

    /**
     * Reject indexed list.
     *
     * @param <E> the type parameter
     * @param list the list
     * @param pred the pred
     * @return the list
     */
    public static <E> List<E> rejectIndexed(final List<E> list, final PredicateIndexed<E> pred) {
        return filterIndexed(list, (index, input) -> !pred.test(index, input));
    }

    /**
     * Reject set.
     *
     * @param <E> the type parameter
     * @param set the set
     * @param pred the pred
     * @return the set
     */
    public static <E> Set<E> reject(final Set<E> set, final Predicate<E> pred) {
        return filter(set, input -> !pred.test(input));
    }

    /**
     * Filter false list.
     *
     * @param <E> the type parameter
     * @param list the list
     * @param pred the pred
     * @return the list
     */
    public static <E> List<E> filterFalse(final List<E> list, final Predicate<E> pred) {
        return reject(list, pred);
    }

    /**
     * Filter false list.
     *
     * @param pred the pred
     * @return the list
     */
    public List<T> filterFalse(final Predicate<T> pred) {
        return reject(pred);
    }

    /**
     * Filter false set.
     *
     * @param <E> the type parameter
     * @param set the set
     * @param pred the pred
     * @return the set
     */
    public static <E> Set<E> filterFalse(final Set<E> set, final Predicate<E> pred) {
        return reject(set, pred);
    }

    /**
     * Every boolean.
     *
     * @param <E> the type parameter
     * @param iterable the iterable
     * @param pred the pred
     * @return the boolean
     */
    public static <E> boolean every(final Iterable<E> iterable, final Predicate<E> pred) {
        for (E item : iterable) {
            if (!pred.test(item)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Every boolean.
     *
     * @param pred the pred
     * @return the boolean
     */
    public boolean every(final Predicate<T> pred) {
        return every(iterable, pred);
    }

    /**
     * All boolean.
     *
     * @param <E> the type parameter
     * @param iterable the iterable
     * @param pred the pred
     * @return the boolean
     */
    /*
     * Documented, #all
     */
    public static <E> boolean all(final Iterable<E> iterable, final Predicate<E> pred) {
        return every(iterable, pred);
    }

    /**
     * All boolean.
     *
     * @param pred the pred
     * @return the boolean
     */
    public boolean all(final Predicate<T> pred) {
        return every(iterable, pred);
    }

    /**
     * Some boolean.
     *
     * @param <E> the type parameter
     * @param iterable the iterable
     * @param pred the pred
     * @return the boolean
     */
    public static <E> boolean some(final Iterable<E> iterable, final Predicate<E> pred) {
        Optional<E> optional = find(iterable, pred);
        return optional == null || optional.isPresent();
    }

    /**
     * Some boolean.
     *
     * @param pred the pred
     * @return the boolean
     */
    public boolean some(final Predicate<T> pred) {
        return some(iterable, pred);
    }

    /**
     * Any boolean.
     *
     * @param <E> the type parameter
     * @param iterable the iterable
     * @param pred the pred
     * @return the boolean
     */
    /*
     * Documented, #any
     */
    public static <E> boolean any(final Iterable<E> iterable, final Predicate<E> pred) {
        return some(iterable, pred);
    }

    /**
     * Any boolean.
     *
     * @param pred the pred
     * @return the boolean
     */
    public boolean any(final Predicate<T> pred) {
        return some(iterable, pred);
    }

    /**
     * Count int.
     *
     * @param <E> the type parameter
     * @param iterable the iterable
     * @param pred the pred
     * @return the int
     */
    public static <E> int count(final Iterable<E> iterable, final Predicate<E> pred) {
        int result = 0;
        for (E item : iterable) {
            if (pred.test(item)) {
                result += 1;
            }
        }
        return result;
    }

    /**
     * Count int.
     *
     * @param pred the pred
     * @return the int
     */
    public int count(final Predicate<T> pred) {
        return count(iterable, pred);
    }

    /**
     * Contains boolean.
     *
     * @param <E> the type parameter
     * @param iterable the iterable
     * @param elem the elem
     * @return the boolean
     */
    public static <E> boolean contains(final Iterable<E> iterable, final E elem) {
        return some(iterable, e -> Objects.equals(elem, e));
    }

    /**
     * Contains boolean.
     *
     * @param elem the elem
     * @return the boolean
     */
    public boolean contains(final T elem) {
        return contains(iterable, elem);
    }

    /**
     * Contains with boolean.
     *
     * @param <E> the type parameter
     * @param iterable the iterable
     * @param elem the elem
     * @return the boolean
     */
    public static <E> boolean containsWith(final Iterable<E> iterable, final E elem) {
        return some(
                iterable,
                e -> elem == null ? e == null : String.valueOf(e).contains(String.valueOf(elem)));
    }

    /**
     * Contains with boolean.
     *
     * @param elem the elem
     * @return the boolean
     */
    public boolean containsWith(final T elem) {
        return containsWith(iterable, elem);
    }

    /**
     * Contains boolean.
     *
     * @param <E> the type parameter
     * @param iterable the iterable
     * @param elem the elem
     * @param fromIndex the from index
     * @return the boolean
     */
    public static <E> boolean contains(
            final Iterable<E> iterable, final E elem, final int fromIndex) {
        final List<E> list = newArrayList(iterable);
        return contains(list.subList(fromIndex, list.size()), elem);
    }

    /**
     * Contains at least boolean.
     *
     * @param value the value
     * @param count the count
     * @return the boolean
     */
    public boolean containsAtLeast(final T value, final int count) {
        return Underscore.containsAtLeast(this.iterable, value, count);
    }

    /**
     * Contains at most boolean.
     *
     * @param value the value
     * @param count the count
     * @return the boolean
     */
    public boolean containsAtMost(final T value, final int count) {
        return Underscore.containsAtMost(this.iterable, value, count);
    }

    /**
     * Contains at least boolean.
     *
     * @param <E> the type parameter
     * @param iterable the iterable
     * @param value the value
     * @param count the count
     * @return the boolean
     */
    public static <E> boolean containsAtLeast(
            final Iterable<E> iterable, final E value, final int count) {
        int foundItems = 0;
        for (E element : iterable) {
            if (Objects.equals(element, value)) {
                foundItems += 1;
            }
            if (foundItems >= count) {
                break;
            }
        }
        return foundItems >= count;
    }

    /**
     * Contains at most boolean.
     *
     * @param <E> the type parameter
     * @param iterable the iterable
     * @param value the value
     * @param count the count
     * @return the boolean
     */
    public static <E> boolean containsAtMost(
            final Iterable<E> iterable, final E value, final int count) {
        int foundItems = size(iterable);
        for (E element : iterable) {
            if (!(Objects.equals(element, value))) {
                foundItems -= 1;
            }
            if (foundItems <= count) {
                break;
            }
        }
        return foundItems <= count;
    }

    /**
     * Include boolean.
     *
     * @param <E> the type parameter
     * @param iterable the iterable
     * @param elem the elem
     * @return the boolean
     */
    /*
     * Documented, #include
     */
    public static <E> boolean include(final Iterable<E> iterable, final E elem) {
        return contains(iterable, elem);
    }

    /**
     * Invoke list.
     *
     * @param <E> the type parameter
     * @param iterable the iterable
     * @param methodName the method name
     * @param args the args
     * @return the list
     */
    /*
     * Documented, #invoke
     */
    @SuppressWarnings("unchecked")
    public static <E> List<E> invoke(
            final Iterable<E> iterable, final String methodName, final List<Object> args) {
        final List<E> result = new ArrayList<>();
        final List<Class<?>> argTypes = map(args, Object::getClass);
        try {
            final Method method =
                    iterable.iterator()
                            .next()
                            .getClass()
                            .getMethod(methodName, argTypes.toArray(new Class[0]));
            for (E arg : iterable) {
                doInvoke(args, result, method, arg);
            }
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(e);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private static <E> void doInvoke(List<Object> args, List<E> result, Method method, E arg) {
        try {
            result.add((E) method.invoke(arg, args.toArray(new Object[0])));
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Invoke list.
     *
     * @param methodName the method name
     * @param args the args
     * @return the list
     */
    public List<T> invoke(final String methodName, final List<Object> args) {
        return invoke(iterable, methodName, args);
    }

    /**
     * Invoke list.
     *
     * @param <E> the type parameter
     * @param iterable the iterable
     * @param methodName the method name
     * @return the list
     */
    public static <E> List<E> invoke(final Iterable<E> iterable, final String methodName) {
        return invoke(iterable, methodName, Collections.emptyList());
    }

    /**
     * Invoke list.
     *
     * @param methodName the method name
     * @return the list
     */
    public List<T> invoke(final String methodName) {
        return invoke(iterable, methodName);
    }

    /**
     * Pluck list.
     *
     * @param <E> the type parameter
     * @param list the list
     * @param propertyName the property name
     * @return the list
     */
    /*
     * Documented, #pluck
     */
    public static <E> List<Object> pluck(final List<E> list, final String propertyName) {
        if (list.isEmpty()) {
            return Collections.emptyList();
        }
        return map(
                list,
                elem -> {
                    try {
                        return elem.getClass().getField(propertyName).get(elem);
                    } catch (Exception e) {
                        try {
                            return elem.getClass().getMethod(propertyName).invoke(elem);
                        } catch (Exception ex) {
                            throw new IllegalArgumentException(ex);
                        }
                    }
                });
    }

    /**
     * Pluck list.
     *
     * @param propertyName the property name
     * @return the list
     */
    public List<Object> pluck(final String propertyName) {
        return pluck(newArrayList(iterable), propertyName);
    }

    /**
     * Pluck set.
     *
     * @param <E> the type parameter
     * @param set the set
     * @param propertyName the property name
     * @return the set
     */
    public static <E> Set<Object> pluck(final Set<E> set, final String propertyName) {
        if (set.isEmpty()) {
            return Collections.emptySet();
        }
        return map(
                set,
                elem -> {
                    try {
                        return elem.getClass().getField(propertyName).get(elem);
                    } catch (Exception e) {
                        try {
                            return elem.getClass().getMethod(propertyName).invoke(elem);
                        } catch (Exception ex) {
                            throw new IllegalArgumentException(ex);
                        }
                    }
                });
    }

    /**
     * Where list.
     *
     * @param <T> the type parameter
     * @param <E> the type parameter
     * @param list the list
     * @param properties the properties
     * @return the list
     */
    /*
     * Documented, #where
     */
    public static <T, E> List<E> where(
            final List<E> list, final List<Map.Entry<String, T>> properties) {
        return filter(list, new WherePredicate<>(properties));
    }

    /**
     * Where list.
     *
     * @param <E> the type parameter
     * @param properties the properties
     * @return the list
     */
    public <E> List<T> where(final List<Map.Entry<String, E>> properties) {
        return where(newArrayList(iterable), properties);
    }

    /**
     * Where set.
     *
     * @param <T> the type parameter
     * @param <E> the type parameter
     * @param set the set
     * @param properties the properties
     * @return the set
     */
    public static <T, E> Set<E> where(
            final Set<E> set, final List<Map.Entry<String, T>> properties) {
        return filter(set, new WherePredicate<>(properties));
    }

    /**
     * Find where optional.
     *
     * @param <T> the type parameter
     * @param <E> the type parameter
     * @param iterable the iterable
     * @param properties the properties
     * @return the optional
     */
    /*
     * Documented, #findWhere
     */
    public static <T, E> Optional<E> findWhere(
            final Iterable<E> iterable, final List<Map.Entry<String, T>> properties) {
        return find(iterable, new WherePredicate<>(properties));
    }

    /**
     * Find where optional.
     *
     * @param <E> the type parameter
     * @param properties the properties
     * @return the optional
     */
    public <E> Optional<T> findWhere(final List<Map.Entry<String, E>> properties) {
        return findWhere(iterable, properties);
    }

    /**
     * Max e.
     *
     * @param <E> the type parameter
     * @param collection the collection
     * @return the e
     */
    /*
     * Documented, #max
     */
    public static <E extends Comparable<? super E>> E max(final Collection<E> collection) {
        return Collections.max(collection);
    }

    /**
     * Max t.
     *
     * @return the t
     */
    @SuppressWarnings("unchecked")
    public T max() {
        return (T) max((Collection) iterable);
    }

    /**
     * Max e.
     *
     * @param <E> the type parameter
     * @param <F> the type parameter
     * @param collection the collection
     * @param func the func
     * @return the e
     */
    @SuppressWarnings("unchecked")
    public static <E, F extends Comparable> E max(
            final Collection<E> collection, final Function<E, F> func) {
        return Collections.max(collection, (o1, o2) -> func.apply(o1).compareTo(func.apply(o2)));
    }

    /**
     * Max t.
     *
     * @param <F> the type parameter
     * @param func the func
     * @return the t
     */
    @SuppressWarnings("unchecked")
    public <F extends Comparable<? super F>> T max(final Function<T, F> func) {
        return (T) max((Collection) iterable, func);
    }

    /**
     * Min e.
     *
     * @param <E> the type parameter
     * @param collection the collection
     * @return the e
     */
    /*
     * Documented, #min
     */
    public static <E extends Comparable<? super E>> E min(final Collection<E> collection) {
        return Collections.min(collection);
    }

    /**
     * Min t.
     *
     * @return the t
     */
    @SuppressWarnings("unchecked")
    public T min() {
        return (T) min((Collection) iterable);
    }

    /**
     * Min e.
     *
     * @param <E> the type parameter
     * @param <F> the type parameter
     * @param collection the collection
     * @param func the func
     * @return the e
     */
    @SuppressWarnings("unchecked")
    public static <E, F extends Comparable> E min(
            final Collection<E> collection, final Function<E, F> func) {
        return Collections.min(collection, (o1, o2) -> func.apply(o1).compareTo(func.apply(o2)));
    }

    /**
     * Min t.
     *
     * @param <F> the type parameter
     * @param func the func
     * @return the t
     */
    @SuppressWarnings("unchecked")
    public <F extends Comparable<? super F>> T min(final Function<T, F> func) {
        return (T) min((Collection) iterable, func);
    }

    /**
     * Shuffle list.
     *
     * @param <E> the type parameter
     * @param iterable the iterable
     * @return the list
     */
    /*
     * Documented, #shuffle
     */
    public static <E> List<E> shuffle(final Iterable<E> iterable) {
        final List<E> shuffled = newArrayList(iterable);
        Collections.shuffle(shuffled);
        return shuffled;
    }

    /**
     * Shuffle list.
     *
     * @return the list
     */
    public List<T> shuffle() {
        return shuffle(iterable);
    }

    /**
     * Sample e.
     *
     * @param <E> the type parameter
     * @param iterable the iterable
     * @return the e
     */
    /*
     * Documented, #sample
     */
    public static <E> E sample(final Iterable<E> iterable) {
        return newArrayList(iterable).get(new java.security.SecureRandom().nextInt(size(iterable)));
    }

    /**
     * Sample t.
     *
     * @return the t
     */
    public T sample() {
        return sample(iterable);
    }

    /**
     * Sample set.
     *
     * @param <E> the type parameter
     * @param list the list
     * @param howMany the how many
     * @return the set
     */
    public static <E> Set<E> sample(final List<E> list, final int howMany) {
        final int size = Math.min(howMany, list.size());
        final Set<E> samples = newLinkedHashSetWithExpectedSize(size);
        while (samples.size() < size) {
            E sample = sample(list);
            samples.add(sample);
        }
        return samples;
    }

    /**
     * Sort with list.
     *
     * @param <T> the type parameter
     * @param iterable the iterable
     * @param comparator the comparator
     * @return the list
     */
    public static <T extends Comparable<? super T>> List<T> sortWith(
            final Iterable<T> iterable, final Comparator<T> comparator) {
        final List<T> sortedList = newArrayList(iterable);
        sortedList.sort(comparator);
        return sortedList;
    }

    /**
     * Sort with list.
     *
     * @param <E> the type parameter
     * @param comparator the comparator
     * @return the list
     */
    @SuppressWarnings("unchecked")
    public <E extends Comparable<? super E>> List<E> sortWith(final Comparator<E> comparator) {
        return sortWith((Iterable<E>) iterable, comparator);
    }

    /**
     * Sort by list.
     *
     * @param <E> the type parameter
     * @param <T> the type parameter
     * @param iterable the iterable
     * @param func the func
     * @return the list
     */
    /*
     * Documented, #sortBy
     */
    public static <E, T extends Comparable<? super T>> List<E> sortBy(
            final Iterable<E> iterable, final Function<E, T> func) {
        final List<E> sortedList = newArrayList(iterable);
        sortedList.sort(Comparator.comparing(func));
        return sortedList;
    }

    /**
     * Sort by list.
     *
     * @param <E> the type parameter
     * @param <V> the type parameter
     * @param func the func
     * @return the list
     */
    @SuppressWarnings("unchecked")
    public <E, V extends Comparable<? super V>> List<E> sortBy(final Function<E, V> func) {
        return sortBy((Iterable<E>) iterable, func);
    }

    /**
     * Sort by list.
     *
     * @param <K> the type parameter
     * @param <V> the type parameter
     * @param iterable the iterable
     * @param key the key
     * @return the list
     */
    public static <K, V extends Comparable<? super V>> List<Map<K, V>> sortBy(
            final Iterable<Map<K, V>> iterable, final K key) {
        final List<Map<K, V>> sortedList = newArrayList(iterable);
        sortedList.sort(Comparator.comparing(o -> o.get(key)));
        return sortedList;
    }

    /**
     * Group by map.
     *
     * @param <K> the type parameter
     * @param <E> the type parameter
     * @param iterable the iterable
     * @param func the func
     * @return the map
     */
    /*
     * Documented, #groupBy
     */
    public static <K, E> Map<K, List<E>> groupBy(
            final Iterable<E> iterable, final Function<E, K> func) {
        final Map<K, List<E>> retVal = new LinkedHashMap<>();
        for (E e : iterable) {
            final K key = func.apply(e);
            List<E> val;
            if (retVal.containsKey(key)) {
                val = retVal.get(key);
            } else {
                val = new ArrayList<>();
            }
            val.add(e);
            retVal.put(key, val);
        }
        return retVal;
    }

    /**
     * Group by map.
     *
     * @param <K> the type parameter
     * @param <E> the type parameter
     * @param func the func
     * @return the map
     */
    @SuppressWarnings("unchecked")
    public <K, E> Map<K, List<E>> groupBy(final Function<E, K> func) {
        return groupBy((Iterable<E>) iterable, func);
    }

    /**
     * Group by map.
     *
     * @param <K> the type parameter
     * @param <E> the type parameter
     * @param iterable the iterable
     * @param func the func
     * @param binaryOperator the binary operator
     * @return the map
     */
    public static <K, E> Map<K, Optional<E>> groupBy(
            final Iterable<E> iterable,
            final Function<E, K> func,
            final BinaryOperator<E> binaryOperator) {
        final Map<K, Optional<E>> retVal = new LinkedHashMap<>();
        for (Map.Entry<K, List<E>> entry : groupBy(iterable, func).entrySet()) {
            retVal.put(entry.getKey(), reduce(entry.getValue(), binaryOperator));
        }
        return retVal;
    }

    /**
     * Group by map.
     *
     * @param <K> the type parameter
     * @param <E> the type parameter
     * @param func the func
     * @param binaryOperator the binary operator
     * @return the map
     */
    @SuppressWarnings("unchecked")
    public <K, E> Map<K, Optional<E>> groupBy(
            final Function<E, K> func, final BinaryOperator<E> binaryOperator) {
        return groupBy((Iterable<E>) iterable, func, binaryOperator);
    }

    /**
     * Associate by map.
     *
     * @param <K> the type parameter
     * @param <E> the type parameter
     * @param iterable the iterable
     * @param func the func
     * @return the map
     */
    public static <K, E> Map<K, E> associateBy(
            final Iterable<E> iterable, final Function<E, K> func) {
        final Map<K, E> retVal = new LinkedHashMap<>();
        for (E e : iterable) {
            final K key = func.apply(e);
            retVal.putIfAbsent(key, e);
        }
        return retVal;
    }

    /**
     * Associate by map.
     *
     * @param <K> the type parameter
     * @param <E> the type parameter
     * @param func the func
     * @return the map
     */
    @SuppressWarnings("unchecked")
    public <K, E> Map<K, E> associateBy(final Function<E, K> func) {
        return associateBy((Iterable<E>) iterable, func);
    }

    /**
     * Index by map.
     *
     * @param <K> the type parameter
     * @param <E> the type parameter
     * @param iterable the iterable
     * @param property the property
     * @return the map
     */
    @SuppressWarnings("unchecked")
    public static <K, E> Map<K, List<E>> indexBy(
            final Iterable<E> iterable, final String property) {
        return groupBy(
                iterable,
                elem -> {
                    try {
                        return (K) elem.getClass().getField(property).get(elem);
                    } catch (Exception e) {
                        return null;
                    }
                });
    }

    /**
     * Index by map.
     *
     * @param <K> the type parameter
     * @param <E> the type parameter
     * @param property the property
     * @return the map
     */
    @SuppressWarnings("unchecked")
    public <K, E> Map<K, List<E>> indexBy(final String property) {
        return indexBy((Iterable<E>) iterable, property);
    }

    /**
     * Count by map.
     *
     * @param <K> the type parameter
     * @param <E> the type parameter
     * @param iterable the iterable
     * @param func the func
     * @return the map
     */
    /*
     * Documented, #countBy
     */
    public static <K, E> Map<K, Integer> countBy(final Iterable<E> iterable, Function<E, K> func) {
        final Map<K, Integer> retVal = new LinkedHashMap<>();
        for (E e : iterable) {
            final K key = func.apply(e);
            if (retVal.containsKey(key)) {
                retVal.put(key, 1 + retVal.get(key));
            } else {
                retVal.put(key, 1);
            }
        }
        return retVal;
    }

    /**
     * Count by map.
     *
     * @param <K> the type parameter
     * @param iterable the iterable
     * @return the map
     */
    public static <K> Map<K, Integer> countBy(final Iterable<K> iterable) {
        final Map<K, Integer> retVal = new LinkedHashMap<>();
        for (K key : iterable) {
            if (retVal.containsKey(key)) {
                retVal.put(key, 1 + retVal.get(key));
            } else {
                retVal.put(key, 1);
            }
        }
        return retVal;
    }

    /**
     * Count by map.
     *
     * @param <K> the type parameter
     * @param <E> the type parameter
     * @param func the func
     * @return the map
     */
    @SuppressWarnings("unchecked")
    public <K, E> Map<K, Integer> countBy(Function<E, K> func) {
        return countBy((Iterable<E>) iterable, func);
    }

    /**
     * Count by map.
     *
     * @param <K> the type parameter
     * @return the map
     */
    @SuppressWarnings("unchecked")
    public <K> Map<K, Integer> countBy() {
        return countBy((Iterable<K>) iterable);
    }

    /**
     * To array e [ ].
     *
     * @param <E> the type parameter
     * @param iterable the iterable
     * @return the e [ ]
     */
    /*
     * Documented, #toArray
     */
    @SuppressWarnings("unchecked")
    public static <E> E[] toArray(final Iterable<E> iterable) {
        return (E[]) newArrayList(iterable).toArray();
    }

    /**
     * To array e [ ].
     *
     * @param <E> the type parameter
     * @return the e [ ]
     */
    @SuppressWarnings("unchecked")
    public <E> E[] toArray() {
        return toArray((Iterable<E>) iterable);
    }

    /**
     * To map map.
     *
     * @param <K> the type parameter
     * @param <V> the type parameter
     * @param iterable the iterable
     * @return the map
     */
    /*
     * Documented, #toMap
     */
    public static <K, V> Map<K, V> toMap(final Iterable<Map.Entry<K, V>> iterable) {
        final Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : iterable) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    /**
     * To map map.
     *
     * @param <K> the type parameter
     * @param <V> the type parameter
     * @return the map
     */
    @SuppressWarnings("unchecked")
    public <K, V> Map<K, V> toMap() {
        return toMap((Iterable<Map.Entry<K, V>>) iterable);
    }

    /**
     * To map map.
     *
     * @param <K> the type parameter
     * @param <V> the type parameter
     * @param tuples the tuples
     * @return the map
     */
    public static <K, V> Map<K, V> toMap(final List<Map.Entry<K, V>> tuples) {
        final Map<K, V> result = new LinkedHashMap<>();
        for (final Map.Entry<K, V> entry : tuples) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    /**
     * To cardinality map map.
     *
     * @return the map
     */
    public Map<T, Integer> toCardinalityMap() {
        return toCardinalityMap(iterable);
    }

    /**
     * To cardinality map map.
     *
     * @param <K> the type parameter
     * @param iterable the iterable
     * @return the map
     */
    public static <K> Map<K, Integer> toCardinalityMap(final Iterable<K> iterable) {
        Iterator<K> iterator = iterable.iterator();
        Map<K, Integer> result = new LinkedHashMap<>();

        while (iterator.hasNext()) {
            K item = iterator.next();

            if (result.containsKey(item)) {
                result.put(item, result.get(item) + 1);
            } else {
                result.put(item, 1);
            }
        }
        return result;
    }

    /**
     * Size int.
     *
     * @param iterable the iterable
     * @return the int
     */
    /*
     * Documented, #size
     */
    public static int size(final Iterable<?> iterable) {
        if (iterable instanceof Collection) {
            return ((Collection) iterable).size();
        }
        int size;
        final Iterator<?> iterator = iterable.iterator();
        for (size = 0; iterator.hasNext(); size += 1) {
            iterator.next();
        }
        return size;
    }

    /**
     * Size int.
     *
     * @return the int
     */
    public int size() {
        return size(iterable);
    }

    /**
     * Size int.
     *
     * @param <E> the type parameter
     * @param array the array
     * @return the int
     */
    @SuppressWarnings("unchecked")
    public static <E> int size(final E... array) {
        return array.length;
    }

    /**
     * Partition list.
     *
     * @param <E> the type parameter
     * @param iterable the iterable
     * @param pred the pred
     * @return the list
     */
    public static <E> List<List<E>> partition(final Iterable<E> iterable, final Predicate<E> pred) {
        final List<E> retVal1 = new ArrayList<>();
        final List<E> retVal2 = new ArrayList<>();
        for (final E e : iterable) {
            if (pred.test(e)) {
                retVal1.add(e);
            } else {
                retVal2.add(e);
            }
        }
        return Arrays.asList(retVal1, retVal2);
    }

    /**
     * Partition list [ ].
     *
     * @param <E> the type parameter
     * @param iterable the iterable
     * @param pred the pred
     * @return the list [ ]
     */
    @SuppressWarnings("unchecked")
    public static <E> List<E>[] partition(final E[] iterable, final Predicate<E> pred) {
        return partition(Arrays.asList(iterable), pred).toArray(new ArrayList[0]);
    }

    /**
     * Single or null t.
     *
     * @return the t
     */
    public T singleOrNull() {
        return singleOrNull(iterable);
    }

    /**
     * Single or null t.
     *
     * @param pred the pred
     * @return the t
     */
    public T singleOrNull(Predicate<T> pred) {
        return singleOrNull(iterable, pred);
    }

    /**
     * Single or null e.
     *
     * @param <E> the type parameter
     * @param iterable the iterable
     * @return the e
     */
    public static <E> E singleOrNull(final Iterable<E> iterable) {
        Iterator<E> iterator = iterable.iterator();
        if (!iterator.hasNext()) {
            return null;
        }
        E result = iterator.next();

        if (iterator.hasNext()) {
            result = null;
        }
        return result;
    }

    /**
     * Single or null e.
     *
     * @param <E> the type parameter
     * @param iterable the iterable
     * @param pred the pred
     * @return the e
     */
    public static <E> E singleOrNull(final Iterable<E> iterable, Predicate<E> pred) {
        return singleOrNull(filter(iterable, pred));
    }

    /**
     * First e.
     *
     * @param <E> the type parameter
     * @param iterable the iterable
     * @return the e
     */
    /*
     * Documented, #first
     */
    public static <E> E first(final Iterable<E> iterable) {
        return iterable.iterator().next();
    }

    /**
     * First e.
     *
     * @param <E> the type parameter
     * @param array the array
     * @return the e
     */
    @SuppressWarnings("unchecked")
    public static <E> E first(final E... array) {
        return array[0];
    }

    /**
     * First list.
     *
     * @param <E> the type parameter
     * @param list the list
     * @param n the n
     * @return the list
     */
    public static <E> List<E> first(final List<E> list, final int n) {
        return list.subList(0, Math.min(n < 0 ? 0 : n, list.size()));
    }

    /**
     * First t.
     *
     * @return the t
     */
    public T first() {
        return first(iterable);
    }

    /**
     * First list.
     *
     * @param n the n
     * @return the list
     */
    public List<T> first(final int n) {
        return first(newArrayList(iterable), n);
    }

    /**
     * First e.
     *
     * @param <E> the type parameter
     * @param iterable the iterable
     * @param pred the pred
     * @return the e
     */
    public static <E> E first(final Iterable<E> iterable, final Predicate<E> pred) {
        return filter(newArrayList(iterable), pred).iterator().next();
    }

    /**
     * First list.
     *
     * @param <E> the type parameter
     * @param iterable the iterable
     * @param pred the pred
     * @param n the n
     * @return the list
     */
    public static <E> List<E> first(
            final Iterable<E> iterable, final Predicate<E> pred, final int n) {
        List<E> list = filter(newArrayList(iterable), pred);
        return list.subList(0, Math.min(n < 0 ? 0 : n, list.size()));
    }

    /**
     * First t.
     *
     * @param pred the pred
     * @return the t
     */
    public T first(final Predicate<T> pred) {
        return first(newArrayList(iterable), pred);
    }

    /**
     * First list.
     *
     * @param pred the pred
     * @param n the n
     * @return the list
     */
    public List<T> first(final Predicate<T> pred, final int n) {
        return first(newArrayList(iterable), pred, n);
    }

    /**
     * First or null e.
     *
     * @param <E> the type parameter
     * @param iterable the iterable
     * @return the e
     */
    public static <E> E firstOrNull(final Iterable<E> iterable) {
        final Iterator<E> iterator = iterable.iterator();
        return iterator.hasNext() ? iterator.next() : null;
    }

    /**
     * First or null t.
     *
     * @return the t
     */
    public T firstOrNull() {
        return firstOrNull(iterable);
    }

    /**
     * First or null e.
     *
     * @param <E> the type parameter
     * @param iterable the iterable
     * @param pred the pred
     * @return the e
     */
    public static <E> E firstOrNull(final Iterable<E> iterable, final Predicate<E> pred) {
        final Iterator<E> iterator = filter(newArrayList(iterable), pred).iterator();
        return iterator.hasNext() ? iterator.next() : null;
    }

    /**
     * First or null t.
     *
     * @param pred the pred
     * @return the t
     */
    public T firstOrNull(final Predicate<T> pred) {
        return firstOrNull(iterable, pred);
    }

    /**
     * Head e.
     *
     * @param <E> the type parameter
     * @param iterable the iterable
     * @return the e
     */
    public static <E> E head(final Iterable<E> iterable) {
        return first(iterable);
    }

    /**
     * Head e.
     *
     * @param <E> the type parameter
     * @param array the array
     * @return the e
     */
    @SuppressWarnings("unchecked")
    public static <E> E head(final E... array) {
        return first(array);
    }

    /**
     * Head list.
     *
     * @param <E> the type parameter
     * @param list the list
     * @param n the n
     * @return the list
     */
    public static <E> List<E> head(final List<E> list, final int n) {
        return first(list, n);
    }

    /**
     * Head t.
     *
     * @return the t
     */
    public T head() {
        return first();
    }

    /**
     * Head list.
     *
     * @param n the n
     * @return the list
     */
    public List<T> head(final int n) {
        return first(n);
    }

    /**
     * Initial list.
     *
     * @param <E> the type parameter
     * @param list the list
     * @return the list
     */
    /*
     * Documented, #initial
     */
    public static <E> List<E> initial(final List<E> list) {
        return initial(list, 1);
    }

    /**
     * Initial list.
     *
     * @param <E> the type parameter
     * @param list the list
     * @param n the n
     * @return the list
     */
    public static <E> List<E> initial(final List<E> list, final int n) {
        return list.subList(0, Math.max(0, list.size() - n));
    }

    /**
     * Initial e [ ].
     *
     * @param <E> the type parameter
     * @param array the array
     * @return the e [ ]
     */
    @SuppressWarnings("unchecked")
    public static <E> E[] initial(final E... array) {
        return initial(array, 1);
    }

    /**
     * Initial e [ ].
     *
     * @param <E> the type parameter
     * @param array the array
     * @param n the n
     * @return the e [ ]
     */
    public static <E> E[] initial(final E[] array, final int n) {
        return Arrays.copyOf(array, array.length - n);
    }

    /**
     * Initial list.
     *
     * @return the list
     */
    public List<T> initial() {
        return initial((List<T>) iterable, 1);
    }

    /**
     * Initial list.
     *
     * @param n the n
     * @return the list
     */
    public List<T> initial(final int n) {
        return initial((List<T>) iterable, n);
    }

    /**
     * Last e.
     *
     * @param <E> the type parameter
     * @param array the array
     * @return the e
     */
    @SuppressWarnings("unchecked")
    public static <E> E last(final E... array) {
        return array[array.length - 1];
    }

    /**
     * Last e.
     *
     * @param <E> the type parameter
     * @param list the list
     * @return the e
     */
    /*
     * Documented, #last
     */
    public static <E> E last(final List<E> list) {
        return list.get(list.size() - 1);
    }

    /**
     * Last list.
     *
     * @param <E> the type parameter
     * @param list the list
     * @param n the n
     * @return the list
     */
    public static <E> List<E> last(final List<E> list, final int n) {
        return list.subList(Math.max(0, list.size() - n), list.size());
    }

    /**
     * Last t.
     *
     * @return the t
     */
    public T last() {
        return last((List<T>) iterable);
    }

    /**
     * Last list.
     *
     * @param n the n
     * @return the list
     */
    public List<T> last(final int n) {
        return last((List<T>) iterable, n);
    }

    /**
     * Last e.
     *
     * @param <E> the type parameter
     * @param list the list
     * @param pred the pred
     * @return the e
     */
    public static <E> E last(final List<E> list, final Predicate<E> pred) {
        final List<E> filteredList = filter(list, pred);
        return filteredList.get(filteredList.size() - 1);
    }

    /**
     * Last t.
     *
     * @param pred the pred
     * @return the t
     */
    public T last(final Predicate<T> pred) {
        return last((List<T>) iterable, pred);
    }

    /**
     * Last or null e.
     *
     * @param <E> the type parameter
     * @param list the list
     * @return the e
     */
    public static <E> E lastOrNull(final List<E> list) {
        return list.isEmpty() ? null : list.get(list.size() - 1);
    }

    /**
     * Last or null t.
     *
     * @return the t
     */
    public T lastOrNull() {
        return lastOrNull((List<T>) iterable);
    }

    /**
     * Last or null e.
     *
     * @param <E> the type parameter
     * @param list the list
     * @param pred the pred
     * @return the e
     */
    public static <E> E lastOrNull(final List<E> list, final Predicate<E> pred) {
        final List<E> filteredList = filter(list, pred);
        return filteredList.isEmpty() ? null : filteredList.get(filteredList.size() - 1);
    }

    /**
     * Last or null t.
     *
     * @param pred the pred
     * @return the t
     */
    public T lastOrNull(final Predicate<T> pred) {
        return lastOrNull((List<T>) iterable, pred);
    }

    /**
     * Rest list.
     *
     * @param <E> the type parameter
     * @param list the list
     * @return the list
     */
    /*
     * Documented, #rest
     */
    public static <E> List<E> rest(final List<E> list) {
        return rest(list, 1);
    }

    /**
     * Rest list.
     *
     * @param <E> the type parameter
     * @param list the list
     * @param n the n
     * @return the list
     */
    public static <E> List<E> rest(final List<E> list, int n) {
        return list.subList(Math.min(n, list.size()), list.size());
    }

    /**
     * Rest e [ ].
     *
     * @param <E> the type parameter
     * @param array the array
     * @return the e [ ]
     */
    @SuppressWarnings("unchecked")
    public static <E> E[] rest(final E... array) {
        return rest(array, 1);
    }

    /**
     * Rest e [ ].
     *
     * @param <E> the type parameter
     * @param array the array
     * @param n the n
     * @return the e [ ]
     */
    @SuppressWarnings("unchecked")
    public static <E> E[] rest(final E[] array, final int n) {
        return (E[]) rest(Arrays.asList(array), n).toArray();
    }

    /**
     * Rest list.
     *
     * @return the list
     */
    public List<T> rest() {
        return rest((List<T>) iterable);
    }

    /**
     * Rest list.
     *
     * @param n the n
     * @return the list
     */
    public List<T> rest(int n) {
        return rest((List<T>) iterable, n);
    }

    /**
     * Tail list.
     *
     * @param <E> the type parameter
     * @param list the list
     * @return the list
     */
    public static <E> List<E> tail(final List<E> list) {
        return rest(list);
    }

    /**
     * Tail list.
     *
     * @param <E> the type parameter
     * @param list the list
     * @param n the n
     * @return the list
     */
    public static <E> List<E> tail(final List<E> list, final int n) {
        return rest(list, n);
    }

    /**
     * Tail e [ ].
     *
     * @param <E> the type parameter
     * @param array the array
     * @return the e [ ]
     */
    @SuppressWarnings("unchecked")
    public static <E> E[] tail(final E... array) {
        return rest(array);
    }

    /**
     * Tail e [ ].
     *
     * @param <E> the type parameter
     * @param array the array
     * @param n the n
     * @return the e [ ]
     */
    public static <E> E[] tail(final E[] array, final int n) {
        return rest(array, n);
    }

    /**
     * Tail list.
     *
     * @return the list
     */
    public List<T> tail() {
        return rest();
    }

    /**
     * Tail list.
     *
     * @param n the n
     * @return the list
     */
    public List<T> tail(final int n) {
        return rest(n);
    }

    /**
     * Drop list.
     *
     * @param <E> the type parameter
     * @param list the list
     * @return the list
     */
    public static <E> List<E> drop(final List<E> list) {
        return rest(list);
    }

    /**
     * Drop list.
     *
     * @param <E> the type parameter
     * @param list the list
     * @param n the n
     * @return the list
     */
    public static <E> List<E> drop(final List<E> list, final int n) {
        return rest(list, n);
    }

    /**
     * Drop e [ ].
     *
     * @param <E> the type parameter
     * @param array the array
     * @return the e [ ]
     */
    @SuppressWarnings("unchecked")
    public static <E> E[] drop(final E... array) {
        return rest(array);
    }

    /**
     * Drop e [ ].
     *
     * @param <E> the type parameter
     * @param array the array
     * @param n the n
     * @return the e [ ]
     */
    public static <E> E[] drop(final E[] array, final int n) {
        return rest(array, n);
    }

    /**
     * Compact list.
     *
     * @param <E> the type parameter
     * @param list the list
     * @return the list
     */
    /*
     * Documented, #compact
     */
    public static <E> List<E> compact(final List<E> list) {
        return filter(
                list,
                arg ->
                        !String.valueOf(arg).equals("null")
                                && !String.valueOf(arg).equals("0")
                                && !String.valueOf(arg).equals("false")
                                && !String.valueOf(arg).equals(""));
    }

    /**
     * Compact e [ ].
     *
     * @param <E> the type parameter
     * @param array the array
     * @return the e [ ]
     */
    @SuppressWarnings("unchecked")
    public static <E> E[] compact(final E... array) {
        return (E[]) compact(Arrays.asList(array)).toArray();
    }

    /**
     * Compact list.
     *
     * @param <E> the type parameter
     * @param list the list
     * @param falsyValue the falsy value
     * @return the list
     */
    public static <E> List<E> compact(final List<E> list, final E falsyValue) {
        return filter(list, arg -> !(Objects.equals(arg, falsyValue)));
    }

    /**
     * Compact e [ ].
     *
     * @param <E> the type parameter
     * @param array the array
     * @param falsyValue the falsy value
     * @return the e [ ]
     */
    @SuppressWarnings("unchecked")
    public static <E> E[] compact(final E[] array, final E falsyValue) {
        return (E[]) compact(Arrays.asList(array), falsyValue).toArray();
    }

    /**
     * Compact list.
     *
     * @return the list
     */
    public List<T> compact() {
        return compact((List<T>) iterable);
    }

    /**
     * Compact list.
     *
     * @param falsyValue the falsy value
     * @return the list
     */
    public List<T> compact(final T falsyValue) {
        return compact((List<T>) iterable, falsyValue);
    }

    /**
     * Flatten list.
     *
     * @param <E> the type parameter
     * @param list the list
     * @return the list
     */
    /*
     * Documented, #flatten
     */
    public static <E> List<E> flatten(final List<?> list) {
        List<E> flattened = new ArrayList<>();
        flatten(list, flattened, -1);
        return flattened;
    }

    /**
     * Flatten list.
     *
     * @param <E> the type parameter
     * @param list the list
     * @param shallow the shallow
     * @return the list
     */
    public static <E> List<E> flatten(final List<?> list, final boolean shallow) {
        List<E> flattened = new ArrayList<>();
        flatten(list, flattened, shallow ? 1 : -1);
        return flattened;
    }

    @SuppressWarnings("unchecked")
    private static <E> void flatten(
            final List<?> fromTreeList, final List<E> toFlatList, final int shallowLevel) {
        for (Object item : fromTreeList) {
            if (item instanceof List<?> && shallowLevel != 0) {
                flatten((List<?>) item, toFlatList, shallowLevel - 1);
            } else {
                toFlatList.add((E) item);
            }
        }
    }

    /**
     * Flatten list.
     *
     * @return the list
     */
    public List<T> flatten() {
        return flatten((List<T>) iterable);
    }

    /**
     * Flatten list.
     *
     * @param shallow the shallow
     * @return the list
     */
    public List<T> flatten(final boolean shallow) {
        return flatten((List<T>) iterable, shallow);
    }

    /**
     * Without list.
     *
     * @param <E> the type parameter
     * @param list the list
     * @param values the values
     * @return the list
     */
    /*
     * Documented, #without
     */
    @SuppressWarnings("unchecked")
    public static <E> List<E> without(final List<E> list, E... values) {
        final List<E> valuesList = Arrays.asList(values);
        return filter(list, elem -> !contains(valuesList, elem));
    }

    /**
     * Without e [ ].
     *
     * @param <E> the type parameter
     * @param array the array
     * @param values the values
     * @return the e [ ]
     */
    @SuppressWarnings("unchecked")
    public static <E> E[] without(final E[] array, final E... values) {
        return (E[]) without(Arrays.asList(array), values).toArray();
    }

    /**
     * Uniq list.
     *
     * @param <E> the type parameter
     * @param list the list
     * @return the list
     */
    /*
     * Documented, #uniq
     */
    public static <E> List<E> uniq(final List<E> list) {
        return newArrayList(newLinkedHashSet(list));
    }

    /**
     * Uniq e [ ].
     *
     * @param <E> the type parameter
     * @param array the array
     * @return the e [ ]
     */
    @SuppressWarnings("unchecked")
    public static <E> E[] uniq(final E... array) {
        return (E[]) uniq(Arrays.asList(array)).toArray();
    }

    /**
     * Uniq collection.
     *
     * @param <K> the type parameter
     * @param <E> the type parameter
     * @param iterable the iterable
     * @param func the func
     * @return the collection
     */
    public static <K, E> Collection<E> uniq(final Iterable<E> iterable, final Function<E, K> func) {
        final Map<K, E> retVal = new LinkedHashMap<>();
        for (final E e : iterable) {
            final K key = func.apply(e);
            retVal.put(key, e);
        }
        return retVal.values();
    }

    /**
     * Uniq e [ ].
     *
     * @param <K> the type parameter
     * @param <E> the type parameter
     * @param array the array
     * @param func the func
     * @return the e [ ]
     */
    @SuppressWarnings("unchecked")
    public static <K, E> E[] uniq(final E[] array, final Function<E, K> func) {
        return (E[]) uniq(Arrays.asList(array), func).toArray();
    }

    /**
     * Distinct list.
     *
     * @param <E> the type parameter
     * @param list the list
     * @return the list
     */
    public static <E> List<E> distinct(final List<E> list) {
        return uniq(list);
    }

    /**
     * Distinct e [ ].
     *
     * @param <E> the type parameter
     * @param array the array
     * @return the e [ ]
     */
    @SuppressWarnings("unchecked")
    public static <E> E[] distinct(final E... array) {
        return uniq(array);
    }

    /**
     * Distinct by collection.
     *
     * @param <K> the type parameter
     * @param <E> the type parameter
     * @param iterable the iterable
     * @param func the func
     * @return the collection
     */
    public static <K, E> Collection<E> distinctBy(
            final Iterable<E> iterable, final Function<E, K> func) {
        return uniq(iterable, func);
    }

    /**
     * Distinct by e [ ].
     *
     * @param <K> the type parameter
     * @param <E> the type parameter
     * @param array the array
     * @param func the func
     * @return the e [ ]
     */
    public static <K, E> E[] distinctBy(final E[] array, final Function<E, K> func) {
        return uniq(array, func);
    }

    /**
     * Union list.
     *
     * @param <E> the type parameter
     * @param list the list
     * @param lists the lists
     * @return the list
     */
    /*
     * Documented, #union
     */
    @SuppressWarnings("unchecked")
    public static <E> List<E> union(final List<E> list, final List<E>... lists) {
        final Set<E> union = new LinkedHashSet<>();
        union.addAll(list);
        for (List<E> localList : lists) {
            union.addAll(localList);
        }
        return newArrayList(union);
    }

    /**
     * Union with list.
     *
     * @param lists the lists
     * @return the list
     */
    @SuppressWarnings("unchecked")
    public List<T> unionWith(final List<T>... lists) {
        return union(newArrayList(iterable), lists);
    }

    /**
     * Union e [ ].
     *
     * @param <E> the type parameter
     * @param arrays the arrays
     * @return the e [ ]
     */
    @SuppressWarnings("unchecked")
    public static <E> E[] union(final E[]... arrays) {
        final Set<E> union = new LinkedHashSet<>();
        for (E[] array : arrays) {
            union.addAll(Arrays.asList(array));
        }
        return (E[]) newArrayList(union).toArray();
    }

    /**
     * Intersection list.
     *
     * @param <E> the type parameter
     * @param list1 the list 1
     * @param list2 the list 2
     * @return the list
     */
    /*
     * Documented, #intersection
     */
    public static <E> List<E> intersection(final List<E> list1, final List<E> list2) {
        final List<E> result = new ArrayList<>();
        for (final E item : list1) {
            if (list2.contains(item)) {
                result.add(item);
            }
        }
        return result;
    }

    /**
     * Intersection list.
     *
     * @param <E> the type parameter
     * @param list the list
     * @param lists the lists
     * @return the list
     */
    @SuppressWarnings("unchecked")
    public static <E> List<E> intersection(final List<E> list, final List<E>... lists) {
        final Deque<List<E>> stack = new ArrayDeque<>();
        stack.push(list);
        for (List<E> es : lists) {
            stack.push(intersection(stack.peek(), es));
        }
        return stack.peek();
    }

    /**
     * Intersection with list.
     *
     * @param lists the lists
     * @return the list
     */
    @SuppressWarnings("unchecked")
    public List<T> intersectionWith(final List<T>... lists) {
        return intersection(newArrayList(iterable), lists);
    }

    /**
     * Intersection e [ ].
     *
     * @param <E> the type parameter
     * @param arrays the arrays
     * @return the e [ ]
     */
    @SuppressWarnings("unchecked")
    public static <E> E[] intersection(final E[]... arrays) {
        final Deque<List<E>> stack = new ArrayDeque<>();
        stack.push(Arrays.asList(arrays[0]));
        for (int index = 1; index < arrays.length; index += 1) {
            stack.push(intersection(stack.peek(), Arrays.asList(arrays[index])));
        }
        return (E[]) stack.peek().toArray();
    }

    /**
     * Difference list.
     *
     * @param <E> the type parameter
     * @param list1 the list 1
     * @param list2 the list 2
     * @return the list
     */
    /*
     * Documented, #difference
     */
    public static <E> List<E> difference(final List<E> list1, final List<E> list2) {
        final List<E> result = new ArrayList<>();
        for (final E item : list1) {
            if (!list2.contains(item)) {
                result.add(item);
            }
        }
        return result;
    }

    /**
     * Difference list.
     *
     * @param <E> the type parameter
     * @param list the list
     * @param lists the lists
     * @return the list
     */
    @SuppressWarnings("unchecked")
    public static <E> List<E> difference(final List<E> list, final List<E>... lists) {
        final Deque<List<E>> stack = new ArrayDeque<>();
        stack.push(list);
        for (List<E> es : lists) {
            stack.push(difference(stack.peek(), es));
        }
        return stack.peek();
    }

    /**
     * Difference with list.
     *
     * @param lists the lists
     * @return the list
     */
    @SuppressWarnings("unchecked")
    public List<T> differenceWith(final List<T>... lists) {
        return difference(newArrayList(iterable), lists);
    }

    /**
     * Difference e [ ].
     *
     * @param <E> the type parameter
     * @param arrays the arrays
     * @return the e [ ]
     */
    @SuppressWarnings("unchecked")
    public static <E> E[] difference(final E[]... arrays) {
        final Deque<List<E>> stack = new ArrayDeque<>();
        stack.push(Arrays.asList(arrays[0]));
        for (int index = 1; index < arrays.length; index += 1) {
            stack.push(difference(stack.peek(), Arrays.asList(arrays[index])));
        }
        return (E[]) stack.peek().toArray();
    }

    /**
     * Zip list.
     *
     * @param <T> the type parameter
     * @param lists the lists
     * @return the list
     */
    /*
     * Documented, #zip
     */
    @SuppressWarnings("unchecked")
    public static <T> List<List<T>> zip(final List<T>... lists) {
        final List<List<T>> zipped = new ArrayList<>();
        each(
                Arrays.asList(lists),
                list -> {
                    int index = 0;
                    for (T elem : list) {
                        final List<T> nTuple;
                        nTuple = index >= zipped.size() ? new ArrayList<>() : zipped.get(index);
                        if (index >= zipped.size()) {
                            zipped.add(nTuple);
                        }
                        index += 1;
                        nTuple.add(elem);
                    }
                });
        return zipped;
    }

    /**
     * Unzip list.
     *
     * @param <T> the type parameter
     * @param lists the lists
     * @return the list
     */
    @SuppressWarnings("unchecked")
    public static <T> List<List<T>> unzip(final List<T>... lists) {
        final List<List<T>> unzipped = new ArrayList<>();
        for (int index = 0; index < lists[0].size(); index += 1) {
            final List<T> nTuple = new ArrayList<>();
            for (List<T> list : lists) {
                nTuple.add(list.get(index));
            }
            unzipped.add(nTuple);
        }
        return unzipped;
    }

    /**
     * Object list.
     *
     * @param <K> the type parameter
     * @param <V> the type parameter
     * @param keys the keys
     * @param values the values
     * @return the list
     */
    /*
     * Documented, #object
     */
    public static <K, V> List<Map.Entry<K, V>> object(final List<K> keys, final List<V> values) {
        return map(
                keys,
                new Function<>() {
                    private int index;

                    @Override
                    public Map.Entry<K, V> apply(K key) {
                        return Map.entry(key, values.get(index++));
                    }
                });
    }

    /**
     * Find index int.
     *
     * @param <E> the type parameter
     * @param list the list
     * @param pred the pred
     * @return the int
     */
    public static <E> int findIndex(final List<E> list, final Predicate<E> pred) {
        for (int index = 0; index < list.size(); index++) {
            if (pred.test(list.get(index))) {
                return index;
            }
        }
        return -1;
    }

    /**
     * Find index int.
     *
     * @param <E> the type parameter
     * @param array the array
     * @param pred the pred
     * @return the int
     */
    public static <E> int findIndex(final E[] array, final Predicate<E> pred) {
        return findIndex(Arrays.asList(array), pred);
    }

    /**
     * Find last index int.
     *
     * @param <E> the type parameter
     * @param list the list
     * @param pred the pred
     * @return the int
     */
    public static <E> int findLastIndex(final List<E> list, final Predicate<E> pred) {
        for (int index = list.size() - 1; index >= 0; index--) {
            if (pred.test(list.get(index))) {
                return index;
            }
        }
        return -1;
    }

    /**
     * Find last index int.
     *
     * @param <E> the type parameter
     * @param array the array
     * @param pred the pred
     * @return the int
     */
    public static <E> int findLastIndex(final E[] array, final Predicate<E> pred) {
        return findLastIndex(Arrays.asList(array), pred);
    }

    /**
     * Binary search int.
     *
     * @param <E> the type parameter
     * @param iterable the iterable
     * @param key the key
     * @return the int
     */
    public static <E extends Comparable<E>> int binarySearch(
            final Iterable<E> iterable, final E key) {
        if (key == null) {
            return first(iterable) == null ? 0 : -1;
        }
        int begin = 0;
        int end = size(iterable) - 1;
        int numberOfNullValues = 0;
        List<E> list = new ArrayList<>();
        for (E item : iterable) {
            if (item == null) {
                numberOfNullValues++;
                end--;
            } else {
                list.add(item);
            }
        }
        while (begin <= end) {
            int middle = begin + (end - begin) / 2;
            if (key.compareTo(list.get(middle)) < 0) {
                end = middle - 1;
            } else if (key.compareTo(list.get(middle)) > 0) {
                begin = middle + 1;
            } else {
                return middle + numberOfNullValues;
            }
        }
        return -(begin + numberOfNullValues + 1);
    }

    /**
     * Binary search int.
     *
     * @param <E> the type parameter
     * @param array the array
     * @param key the key
     * @return the int
     */
    public static <E extends Comparable<E>> int binarySearch(final E[] array, final E key) {
        return binarySearch(Arrays.asList(array), key);
    }

    /**
     * Sorted index int.
     *
     * @param <E> the type parameter
     * @param list the list
     * @param value the value
     * @return the int
     */
    /*
     * Documented, #sortedIndex
     */
    public static <E extends Comparable<E>> int sortedIndex(final List<E> list, final E value) {
        int index = 0;
        for (E elem : list) {
            if (elem.compareTo(value) >= 0) {
                return index;
            }
            index += 1;
        }
        return -1;
    }

    /**
     * Sorted index int.
     *
     * @param <E> the type parameter
     * @param array the array
     * @param value the value
     * @return the int
     */
    public static <E extends Comparable<E>> int sortedIndex(final E[] array, final E value) {
        return sortedIndex(Arrays.asList(array), value);
    }

    /**
     * Sorted index int.
     *
     * @param <E> the type parameter
     * @param list the list
     * @param value the value
     * @param propertyName the property name
     * @return the int
     */
    @SuppressWarnings("unchecked")
    public static <E extends Comparable<E>> int sortedIndex(
            final List<E> list, final E value, final String propertyName) {
        try {
            final Field property = value.getClass().getField(propertyName);
            final Object valueProperty = property.get(value);
            int index = 0;
            for (E elem : list) {
                if (((Comparable) property.get(elem)).compareTo(valueProperty) >= 0) {
                    return index;
                }
                index += 1;
            }
            return -1;
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Sorted index int.
     *
     * @param <E> the type parameter
     * @param array the array
     * @param value the value
     * @param propertyName the property name
     * @return the int
     */
    public static <E extends Comparable<E>> int sortedIndex(
            final E[] array, final E value, final String propertyName) {
        return sortedIndex(Arrays.asList(array), value, propertyName);
    }

    /**
     * Index of int.
     *
     * @param <E> the type parameter
     * @param list the list
     * @param value the value
     * @return the int
     */
    /*
     * Documented, #indexOf
     */
    public static <E> int indexOf(final List<E> list, final E value) {
        return list.indexOf(value);
    }

    /**
     * Index of int.
     *
     * @param <E> the type parameter
     * @param array the array
     * @param value the value
     * @return the int
     */
    public static <E> int indexOf(final E[] array, final E value) {
        return indexOf(Arrays.asList(array), value);
    }

    /**
     * Last index of int.
     *
     * @param <E> the type parameter
     * @param list the list
     * @param value the value
     * @return the int
     */
    /*
     * Documented, #lastIndexOf
     */
    public static <E> int lastIndexOf(final List<E> list, final E value) {
        return list.lastIndexOf(value);
    }

    /**
     * Last index of int.
     *
     * @param <E> the type parameter
     * @param array the array
     * @param value the value
     * @return the int
     */
    public static <E> int lastIndexOf(final E[] array, final E value) {
        return lastIndexOf(Arrays.asList(array), value);
    }

    /**
     * Range list.
     *
     * @param stop the stop
     * @return the list
     */
    /*
     * Documented, #range
     */
    public static List<Integer> range(int stop) {
        return range(0, stop, 1);
    }

    /**
     * Range list.
     *
     * @param start the start
     * @param stop the stop
     * @return the list
     */
    public static List<Integer> range(int start, int stop) {
        return range(start, stop, start < stop ? 1 : -1);
    }

    /**
     * Range list.
     *
     * @param start the start
     * @param stop the stop
     * @param step the step
     * @return the list
     */
    public static List<Integer> range(int start, int stop, int step) {
        List<Integer> list = new ArrayList<>();
        if (step == 0) {
            return list;
        }
        if (start < stop) {
            for (int value = start; value < stop; value += step) {
                list.add(value);
            }
        } else {
            for (int value = start; value > stop; value += step) {
                list.add(value);
            }
        }
        return list;
    }

    /**
     * Range list.
     *
     * @param stop the stop
     * @return the list
     */
    public static List<Character> range(char stop) {
        return range('a', stop, 1);
    }

    /**
     * Range list.
     *
     * @param start the start
     * @param stop the stop
     * @return the list
     */
    public static List<Character> range(char start, char stop) {
        return range(start, stop, start < stop ? 1 : -1);
    }

    /**
     * Range list.
     *
     * @param start the start
     * @param stop the stop
     * @param step the step
     * @return the list
     */
    public static List<Character> range(char start, char stop, int step) {
        List<Character> list = new ArrayList<>();
        if (step == 0) {
            return list;
        }
        if (start < stop) {
            for (char value = start; value < stop; value += step) {
                list.add(value);
            }
        } else {
            for (char value = start; value > stop; value += step) {
                list.add(value);
            }
        }
        return list;
    }

    /**
     * Chunk list.
     *
     * @param <T> the type parameter
     * @param iterable the iterable
     * @param size the size
     * @return the list
     */
    public static <T> List<List<T>> chunk(final Iterable<T> iterable, final int size) {
        if (size <= 0) {
            return new ArrayList<>();
        }
        return chunk(iterable, size, size);
    }

    /**
     * Chunk list.
     *
     * @param <T> the type parameter
     * @param iterable the iterable
     * @param size the size
     * @param step the step
     * @return the list
     */
    public static <T> List<List<T>> chunk(
            final Iterable<T> iterable, final int size, final int step) {
        if (step <= 0 || size < 0) {
            return new ArrayList<>();
        }
        int index = 0;
        int length = size(iterable);
        final List<List<T>> result = new ArrayList<>(size == 0 ? size : (length / size) + 1);
        while (index < length) {
            result.add(newArrayList(iterable).subList(index, Math.min(length, index + size)));
            index += step;
        }
        return result;
    }

    /**
     * Chunk fill list.
     *
     * @param <T> the type parameter
     * @param iterable the iterable
     * @param size the size
     * @param fillValue the fill value
     * @return the list
     */
    public static <T> List<List<T>> chunkFill(
            final Iterable<T> iterable, final int size, final T fillValue) {
        if (size <= 0) {
            return new ArrayList<>();
        }
        return chunkFill(iterable, size, size, fillValue);
    }

    /**
     * Chunk fill list.
     *
     * @param <T> the type parameter
     * @param iterable the iterable
     * @param size the size
     * @param step the step
     * @param fillValue the fill value
     * @return the list
     */
    public static <T> List<List<T>> chunkFill(
            final Iterable<T> iterable, final int size, final int step, final T fillValue) {
        if (step <= 0 || size < 0) {
            return new ArrayList<>();
        }
        final List<List<T>> result = chunk(iterable, size, step);
        int difference = size - result.get(result.size() - 1).size();
        for (int i = difference; 0 < i; i--) {
            result.get(result.size() - 1).add(fillValue);
        }
        return result;
    }

    /**
     * Chunk list.
     *
     * @param size the size
     * @return the list
     */
    public List<List<T>> chunk(final int size) {
        return chunk(getIterable(), size, size);
    }

    /**
     * Chunk list.
     *
     * @param size the size
     * @param step the step
     * @return the list
     */
    public List<List<T>> chunk(final int size, final int step) {
        return chunk(getIterable(), size, step);
    }

    /**
     * Chunk fill list.
     *
     * @param size the size
     * @param fillvalue the fillvalue
     * @return the list
     */
    public List<List<T>> chunkFill(final int size, final T fillvalue) {
        return chunkFill(getIterable(), size, size, fillvalue);
    }

    /**
     * Chunk fill list.
     *
     * @param size the size
     * @param step the step
     * @param fillvalue the fillvalue
     * @return the list
     */
    public List<List<T>> chunkFill(final int size, final int step, T fillvalue) {
        return chunkFill(getIterable(), size, step, fillvalue);
    }

    /**
     * Cycle list.
     *
     * @param <T> the type parameter
     * @param iterable the iterable
     * @param times the times
     * @return the list
     */
    public static <T> List<T> cycle(final Iterable<T> iterable, final int times) {
        int size = Math.abs(size(iterable) * times);
        if (size == 0) {
            return new ArrayList<>();
        }
        List<T> list = newArrayListWithExpectedSize(size);
        int round = 0;
        if (times > 0) {
            while (round < times) {
                for (T element : iterable) {
                    list.add(element);
                }
                round++;
            }
        } else {
            list = cycle(Underscore.reverse(iterable), -times);
        }
        return list;
    }

    /**
     * Cycle list.
     *
     * @param times the times
     * @return the list
     */
    public List<T> cycle(final int times) {
        return cycle(value(), times);
    }

    /**
     * Repeat list.
     *
     * @param <T> the type parameter
     * @param element the element
     * @param times the times
     * @return the list
     */
    public static <T> List<T> repeat(final T element, final int times) {
        if (times <= 0) {
            return new ArrayList<>();
        }
        List<T> result = newArrayListWithExpectedSize(times);
        for (int i = 0; i < times; i++) {
            result.add(element);
        }
        return result;
    }

    /**
     * Interpose list.
     *
     * @param <T> the type parameter
     * @param iterable the iterable
     * @param interElement the inter element
     * @return the list
     */
    public static <T> List<T> interpose(final Iterable<T> iterable, final T interElement) {
        if (interElement == null) {
            return newArrayList(iterable);
        }
        int size = size(iterable);
        int index = 0;
        List<T> array = newArrayListWithExpectedSize(size * 2);
        for (T elem : iterable) {
            array.add(elem);
            if (index + 1 < size) {
                array.add(interElement);
                index++;
            }
        }
        return array;
    }

    /**
     * Interpose by list list.
     *
     * @param <T> the type parameter
     * @param iterable the iterable
     * @param interIter the inter iter
     * @return the list
     */
    public static <T> List<T> interposeByList(
            final Iterable<T> iterable, final Iterable<T> interIter) {
        if (interIter == null) {
            return newArrayList(iterable);
        }
        List<T> interList = newArrayList(interIter);
        if (isEmpty(interIter)) {
            return newArrayList(iterable);
        }
        int size = size(iterable);
        List<T> array = newArrayListWithExpectedSize(size + interList.size());
        int index = 0;
        for (T element : iterable) {
            array.add(element);
            if (index < interList.size() && index + 1 < size) {
                array.add(interList.get(index));
                index++;
            }
        }
        return array;
    }

    /**
     * Interpose list.
     *
     * @param element the element
     * @return the list
     */
    public List<T> interpose(final T element) {
        return interpose(value(), element);
    }

    /**
     * Interpose by list list.
     *
     * @param interIter the inter iter
     * @return the list
     */
    public List<T> interposeByList(final Iterable<T> interIter) {
        return interposeByList(value(), interIter);
    }

    /**
     * Bind function.
     *
     * @param <T> the type parameter
     * @param <F> the type parameter
     * @param function the function
     * @return the function
     */
    /*
     * Documented, #bind
     */
    public static <T, F> Function<F, T> bind(final Function<F, T> function) {
        return function;
    }

    /**
     * Memoize function.
     *
     * @param <T> the type parameter
     * @param <F> the type parameter
     * @param function the function
     * @return the function
     */
    /*
     * Documented, #memoize
     */
    public static <T, F> Function<F, T> memoize(final Function<F, T> function) {
        return new MemoizeFunction<>() {
            @Override
            public T calc(F arg) {
                return function.apply(arg);
            }
        };
    }

    /**
     * Delay java . util . concurrent . scheduled future.
     *
     * @param <T> the type parameter
     * @param function the function
     * @param delayMilliseconds the delay milliseconds
     * @return the java . util . concurrent . scheduled future
     */
    /*
     * Documented, #delay
     */
    public static <T> java.util.concurrent.ScheduledFuture<T> delay(
            final Supplier<T> function, final int delayMilliseconds) {
        final java.util.concurrent.ScheduledExecutorService scheduler =
                java.util.concurrent.Executors.newSingleThreadScheduledExecutor();
        final java.util.concurrent.ScheduledFuture<T> future =
                scheduler.schedule(
                        function::get,
                        delayMilliseconds,
                        java.util.concurrent.TimeUnit.MILLISECONDS);
        scheduler.shutdown();
        return future;
    }

    /**
     * Defer java . util . concurrent . scheduled future.
     *
     * @param <T> the type parameter
     * @param function the function
     * @return the java . util . concurrent . scheduled future
     */
    public static <T> java.util.concurrent.ScheduledFuture<T> defer(final Supplier<T> function) {
        return delay(function, 0);
    }

    /**
     * Defer java . util . concurrent . scheduled future.
     *
     * @param runnable the runnable
     * @return the java . util . concurrent . scheduled future
     */
    public static java.util.concurrent.ScheduledFuture<Void> defer(final Runnable runnable) {
        return delay(
                () -> {
                    runnable.run();
                    return null;
                },
                0);
    }

    /**
     * Throttle supplier.
     *
     * @param <T> the type parameter
     * @param function the function
     * @param waitMilliseconds the wait milliseconds
     * @return the supplier
     */
    public static <T> Supplier<T> throttle(final Supplier<T> function, final int waitMilliseconds) {
        class ThrottleLater implements Supplier<T> {
            private final Supplier<T> localFunction;
            private java.util.concurrent.ScheduledFuture<T> timeout;
            private long previous;

            ThrottleLater(final Supplier<T> function) {
                this.localFunction = function;
            }

            @Override
            public T get() {
                previous = now();
                timeout = null;
                return localFunction.get();
            }

            java.util.concurrent.ScheduledFuture<T> getTimeout() {
                return timeout;
            }

            void setTimeout(java.util.concurrent.ScheduledFuture<T> timeout) {
                this.timeout = timeout;
            }

            long getPrevious() {
                return previous;
            }

            void setPrevious(long previous) {
                this.previous = previous;
            }
        }

        class ThrottleFunction implements Supplier<T> {
            private final Supplier<T> localFunction;
            private final ThrottleLater throttleLater;

            ThrottleFunction(final Supplier<T> function) {
                this.localFunction = function;
                this.throttleLater = new ThrottleLater(function);
            }

            @Override
            public T get() {
                final long now = now();
                if (throttleLater.getPrevious() == 0L) {
                    throttleLater.setPrevious(now);
                }
                final long remaining = waitMilliseconds - (now - throttleLater.getPrevious());
                T result = null;
                if (remaining <= 0) {
                    throttleLater.setPrevious(now);
                    result = localFunction.get();
                } else if (throttleLater.getTimeout() == null) {
                    throttleLater.setTimeout(delay(throttleLater, waitMilliseconds));
                }
                return result;
            }
        }
        return new ThrottleFunction(function);
    }

    /**
     * Debounce supplier.
     *
     * @param <T> the type parameter
     * @param function the function
     * @param delayMilliseconds the delay milliseconds
     * @return the supplier
     */
    /*
     * Documented, #debounce
     */
    public static <T> Supplier<T> debounce(
            final Supplier<T> function, final int delayMilliseconds) {
        return new Supplier<>() {
            private java.util.concurrent.ScheduledFuture<T> timeout;

            @Override
            public T get() {
                clearTimeout(timeout);
                timeout = delay(function, delayMilliseconds);
                return null;
            }
        };
    }

    /**
     * Wrap function.
     *
     * @param <T> the type parameter
     * @param function the function
     * @param wrapper the wrapper
     * @return the function
     */
    /*
     * Documented, #wrap
     */
    public static <T> Function<Void, T> wrap(
            final UnaryOperator<T> function, final Function<UnaryOperator<T>, T> wrapper) {
        return arg -> wrapper.apply(function);
    }

    /**
     * Negate predicate.
     *
     * @param <E> the type parameter
     * @param pred the pred
     * @return the predicate
     */
    public static <E> Predicate<E> negate(final Predicate<E> pred) {
        return item -> !pred.test(item);
    }

    /**
     * Compose function.
     *
     * @param <T> the type parameter
     * @param func the func
     * @return the function
     */
    /*
     * Documented, #compose
     */
    @SuppressWarnings("unchecked")
    public static <T> Function<T, T> compose(final Function<T, T>... func) {
        return arg -> {
            T result = arg;
            for (int index = func.length - 1; index >= 0; index -= 1) {
                result = func[index].apply(result);
            }
            return result;
        };
    }

    /**
     * After supplier.
     *
     * @param <E> the type parameter
     * @param count the count
     * @param function the function
     * @return the supplier
     */
    /*
     * Documented, #after
     */
    public static <E> Supplier<E> after(final int count, final Supplier<E> function) {
        class AfterFunction implements Supplier<E> {
            private final int count;
            private final Supplier<E> localFunction;
            private int index;
            private E result;

            AfterFunction(final int count, final Supplier<E> function) {
                this.count = count;
                this.localFunction = function;
            }

            public E get() {
                if (++index >= count) {
                    result = localFunction.get();
                }
                return result;
            }
        }
        return new AfterFunction(count, function);
    }

    /**
     * Before supplier.
     *
     * @param <E> the type parameter
     * @param count the count
     * @param function the function
     * @return the supplier
     */
    /*
     * Documented, #before
     */
    public static <E> Supplier<E> before(final int count, final Supplier<E> function) {
        class BeforeFunction implements Supplier<E> {
            private final int count;
            private final Supplier<E> localFunction;
            private int index;
            private E result;

            BeforeFunction(final int count, final Supplier<E> function) {
                this.count = count;
                this.localFunction = function;
            }

            public E get() {
                if (++index <= count) {
                    result = localFunction.get();
                }
                return result;
            }
        }
        return new BeforeFunction(count, function);
    }

    /**
     * Once supplier.
     *
     * @param <T> the type parameter
     * @param function the function
     * @return the supplier
     */
    /*
     * Documented, #once
     */
    public static <T> Supplier<T> once(final Supplier<T> function) {
        return new Supplier<>() {
            private volatile boolean executed;
            private T result;

            @Override
            public T get() {
                if (!executed) {
                    executed = true;
                    result = function.get();
                }
                return result;
            }
        };
    }

    /**
     * Keys set.
     *
     * @param <K> the type parameter
     * @param <V> the type parameter
     * @param object the object
     * @return the set
     */
    /*
     * Documented, #keys
     */
    public static <K, V> Set<K> keys(final Map<K, V> object) {
        return object.keySet();
    }

    /**
     * Values collection.
     *
     * @param <K> the type parameter
     * @param <V> the type parameter
     * @param object the object
     * @return the collection
     */
    /*
     * Documented, #values
     */
    public static <K, V> Collection<V> values(final Map<K, V> object) {
        return object.values();
    }

    /**
     * Map object list.
     *
     * @param <K> the type parameter
     * @param <V> the type parameter
     * @param object the object
     * @param func the func
     * @return the list
     */
    public static <K, V> List<Map.Entry<K, V>> mapObject(
            final Map<K, V> object, final Function<? super V, V> func) {
        return map(
                newArrayList(object.entrySet()),
                entry -> Map.entry(entry.getKey(), func.apply(entry.getValue())));
    }

    /**
     * Pairs list.
     *
     * @param <K> the type parameter
     * @param <V> the type parameter
     * @param object the object
     * @return the list
     */
    /*
     * Documented, #pairs
     */
    public static <K, V> List<Map.Entry<K, V>> pairs(final Map<K, V> object) {
        return map(
                newArrayList(object.entrySet()),
                entry -> Map.entry(entry.getKey(), entry.getValue()));
    }

    /**
     * Invert list.
     *
     * @param <K> the type parameter
     * @param <V> the type parameter
     * @param object the object
     * @return the list
     */
    /*
     * Documented, #invert
     */
    public static <K, V> List<Map.Entry<V, K>> invert(final Map<K, V> object) {
        return map(
                newArrayList(object.entrySet()),
                entry -> Map.entry(entry.getValue(), entry.getKey()));
    }

    /**
     * Functions list.
     *
     * @param object the object
     * @return the list
     */
    /*
     * Documented, #functions
     */
    public static List<String> functions(final Object object) {
        final List<String> result = new ArrayList<>();
        for (final Method method : object.getClass().getDeclaredMethods()) {
            result.add(method.getName());
        }
        return sort(uniq(result));
    }

    /**
     * Methods list.
     *
     * @param object the object
     * @return the list
     */
    public static List<String> methods(final Object object) {
        return functions(object);
    }

    /**
     * Extend map.
     *
     * @param <K> the type parameter
     * @param <V> the type parameter
     * @param destination the destination
     * @param sources the sources
     * @return the map
     */
    /*
     * Documented, #extend
     */
    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> extend(final Map<K, V> destination, final Map<K, V>... sources) {
        final Map<K, V> result = new LinkedHashMap<>();
        result.putAll(destination);
        for (final Map<K, V> source : sources) {
            result.putAll(source);
        }
        return result;
    }

    /**
     * Find key e.
     *
     * @param <E> the type parameter
     * @param list the list
     * @param pred the pred
     * @return the e
     */
    public static <E> E findKey(final List<E> list, final Predicate<E> pred) {
        for (E e : list) {
            if (pred.test(e)) {
                return e;
            }
        }
        return null;
    }

    /**
     * Find key e.
     *
     * @param <E> the type parameter
     * @param array the array
     * @param pred the pred
     * @return the e
     */
    public static <E> E findKey(final E[] array, final Predicate<E> pred) {
        return findKey(Arrays.asList(array), pred);
    }

    /**
     * Find last key e.
     *
     * @param <E> the type parameter
     * @param list the list
     * @param pred the pred
     * @return the e
     */
    public static <E> E findLastKey(final List<E> list, final Predicate<E> pred) {
        for (int index = list.size() - 1; index >= 0; index--) {
            if (pred.test(list.get(index))) {
                return list.get(index);
            }
        }
        return null;
    }

    /**
     * Find last key e.
     *
     * @param <E> the type parameter
     * @param array the array
     * @param pred the pred
     * @return the e
     */
    public static <E> E findLastKey(final E[] array, final Predicate<E> pred) {
        return findLastKey(Arrays.asList(array), pred);
    }

    /**
     * Pick list.
     *
     * @param <K> the type parameter
     * @param <V> the type parameter
     * @param object the object
     * @param keys the keys
     * @return the list
     */
    /*
     * Documented, #pick
     */
    @SuppressWarnings("unchecked")
    public static <K, V> List<Map.Entry<K, V>> pick(final Map<K, V> object, final K... keys) {
        return without(
                map(
                        newArrayList(object.entrySet()),
                        entry -> {
                            if (Arrays.asList(keys).contains(entry.getKey())) {
                                return Map.entry(entry.getKey(), entry.getValue());
                            } else {
                                return null;
                            }
                        }),
                (Map.Entry<K, V>) null);
    }

    /**
     * Pick list.
     *
     * @param <K> the type parameter
     * @param <V> the type parameter
     * @param object the object
     * @param pred the pred
     * @return the list
     */
    @SuppressWarnings("unchecked")
    public static <K, V> List<Map.Entry<K, V>> pick(
            final Map<K, V> object, final Predicate<V> pred) {
        return without(
                map(
                        newArrayList(object.entrySet()),
                        entry -> {
                            if (pred.test(object.get(entry.getKey()))) {
                                return Map.entry(entry.getKey(), entry.getValue());
                            } else {
                                return null;
                            }
                        }),
                (Map.Entry<K, V>) null);
    }

    /**
     * Omit list.
     *
     * @param <K> the type parameter
     * @param <V> the type parameter
     * @param object the object
     * @param keys the keys
     * @return the list
     */
    /*
     * Documented, #omit
     */
    @SuppressWarnings("unchecked")
    public static <K, V> List<Map.Entry<K, V>> omit(final Map<K, V> object, final K... keys) {
        return without(
                map(
                        newArrayList(object.entrySet()),
                        entry -> {
                            if (Arrays.asList(keys).contains(entry.getKey())) {
                                return null;
                            } else {
                                return Map.entry(entry.getKey(), entry.getValue());
                            }
                        }),
                (Map.Entry<K, V>) null);
    }

    /**
     * Omit list.
     *
     * @param <K> the type parameter
     * @param <V> the type parameter
     * @param object the object
     * @param pred the pred
     * @return the list
     */
    @SuppressWarnings("unchecked")
    public static <K, V> List<Map.Entry<K, V>> omit(
            final Map<K, V> object, final Predicate<V> pred) {
        return without(
                map(
                        newArrayList(object.entrySet()),
                        entry -> {
                            if (pred.test(entry.getValue())) {
                                return null;
                            } else {
                                return Map.entry(entry.getKey(), entry.getValue());
                            }
                        }),
                (Map.Entry<K, V>) null);
    }

    /**
     * Defaults map.
     *
     * @param <K> the type parameter
     * @param <V> the type parameter
     * @param object the object
     * @param defaults the defaults
     * @return the map
     */
    /*
     * Documented, #defaults
     */
    public static <K, V> Map<K, V> defaults(final Map<K, V> object, final Map<K, V> defaults) {
        final Map<K, V> result = new LinkedHashMap<>();
        result.putAll(defaults);
        result.putAll(object);
        return result;
    }

    /**
     * Clone object.
     *
     * @param obj the obj
     * @return the object
     */
    /*
     * Documented, #clone
     */
    public static Object clone(final Object obj) {
        try {
            if (obj instanceof Cloneable) {
                for (final Method method : obj.getClass().getMethods()) {
                    if (method.getName().equals("clone")
                            && method.getParameterTypes().length == 0) {
                        return method.invoke(obj);
                    }
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
        throw new IllegalArgumentException("Cannot clone object");
    }

    /**
     * Clone e [ ].
     *
     * @param <E> the type parameter
     * @param iterable the iterable
     * @return the e [ ]
     */
    @SuppressWarnings("unchecked")
    public static <E> E[] clone(final E... iterable) {
        return Arrays.copyOf(iterable, iterable.length);
    }

    /**
     * Tap.
     *
     * @param <T> the type parameter
     * @param iterable the iterable
     * @param func the func
     */
    public static <T> void tap(final Iterable<T> iterable, final Consumer<? super T> func) {
        each(iterable, func);
    }

    /**
     * Is match boolean.
     *
     * @param <K> the type parameter
     * @param <V> the type parameter
     * @param object the object
     * @param properties the properties
     * @return the boolean
     */
    public static <K, V> boolean isMatch(final Map<K, V> object, final Map<K, V> properties) {
        for (final K key : keys(properties)) {
            if (!object.containsKey(key) || !object.get(key).equals(properties.get(key))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Is equal boolean.
     *
     * @param object the object
     * @param other the other
     * @return the boolean
     */
    /*
     * Documented, #isEqual
     */
    public static boolean isEqual(final Object object, final Object other) {
        return Objects.equals(object, other);
    }

    /**
     * Is empty boolean.
     *
     * @param <K> the type parameter
     * @param <V> the type parameter
     * @param object the object
     * @return the boolean
     */
    public static <K, V> boolean isEmpty(final Map<K, V> object) {
        return object == null || object.isEmpty();
    }

    /**
     * Is empty boolean.
     *
     * @param <T> the type parameter
     * @param iterable the iterable
     * @return the boolean
     */
    /*
     * Documented, #isEmpty
     */
    public static <T> boolean isEmpty(final Iterable<T> iterable) {
        return iterable == null || !iterable.iterator().hasNext();
    }

    /**
     * Is empty boolean.
     *
     * @return the boolean
     */
    public boolean isEmpty() {
        return iterable == null || !iterable.iterator().hasNext();
    }

    /**
     * Is not empty boolean.
     *
     * @param <K> the type parameter
     * @param <V> the type parameter
     * @param object the object
     * @return the boolean
     */
    public static <K, V> boolean isNotEmpty(final Map<K, V> object) {
        return object != null && !object.isEmpty();
    }

    /**
     * Is not empty boolean.
     *
     * @param <T> the type parameter
     * @param iterable the iterable
     * @return the boolean
     */
    public static <T> boolean isNotEmpty(final Iterable<T> iterable) {
        return iterable != null && iterable.iterator().hasNext();
    }

    /**
     * Is not empty boolean.
     *
     * @return the boolean
     */
    public boolean isNotEmpty() {
        return iterable != null && iterable.iterator().hasNext();
    }

    /**
     * Is array boolean.
     *
     * @param object the object
     * @return the boolean
     */
    /*
     * Documented, #isArray
     */
    public static boolean isArray(final Object object) {
        return object != null && object.getClass().isArray();
    }

    /**
     * Is object boolean.
     *
     * @param object the object
     * @return the boolean
     */
    /*
     * Documented, #isObject
     */
    public static boolean isObject(final Object object) {
        return object instanceof Map;
    }

    /**
     * Is function boolean.
     *
     * @param object the object
     * @return the boolean
     */
    /*
     * Documented, #isFunction
     */
    public static boolean isFunction(final Object object) {
        return object instanceof Function;
    }

    /**
     * Is string boolean.
     *
     * @param object the object
     * @return the boolean
     */
    /*
     * Documented, #isString
     */
    public static boolean isString(final Object object) {
        return object instanceof String;
    }

    /**
     * Is number boolean.
     *
     * @param object the object
     * @return the boolean
     */
    /*
     * Documented, #isNumber
     */
    public static boolean isNumber(final Object object) {
        return object instanceof Number;
    }

    /**
     * Is date boolean.
     *
     * @param object the object
     * @return the boolean
     */
    /*
     * Documented, #isDate
     */
    public static boolean isDate(final Object object) {
        return object instanceof Date;
    }

    /**
     * Is reg exp boolean.
     *
     * @param object the object
     * @return the boolean
     */
    public static boolean isRegExp(final Object object) {
        return object instanceof java.util.regex.Pattern;
    }

    /**
     * Is error boolean.
     *
     * @param object the object
     * @return the boolean
     */
    public static boolean isError(final Object object) {
        return object instanceof Throwable;
    }

    /**
     * Is boolean boolean.
     *
     * @param object the object
     * @return the boolean
     */
    /*
     * Documented, #isBoolean
     */
    public static boolean isBoolean(final Object object) {
        return object instanceof Boolean;
    }

    /**
     * Is null boolean.
     *
     * @param object the object
     * @return the boolean
     */
    public static boolean isNull(final Object object) {
        return object == null;
    }

    /**
     * Has boolean.
     *
     * @param <K> the type parameter
     * @param <V> the type parameter
     * @param object the object
     * @param key the key
     * @return the boolean
     */
    /*
     * Documented, #has
     */
    public static <K, V> boolean has(final Map<K, V> object, final K key) {
        return object.containsKey(key);
    }

    /**
     * Identity e.
     *
     * @param <E> the type parameter
     * @param value the value
     * @return the e
     */
    public static <E> E identity(final E value) {
        return value;
    }

    /**
     * Constant supplier.
     *
     * @param <E> the type parameter
     * @param value the value
     * @return the supplier
     */
    public static <E> Supplier<E> constant(final E value) {
        return () -> value;
    }

    /**
     * Property function.
     *
     * @param <K> the type parameter
     * @param <V> the type parameter
     * @param key the key
     * @return the function
     */
    public static <K, V> Function<Map<K, V>, V> property(final K key) {
        return object -> object.get(key);
    }

    /**
     * Property of function.
     *
     * @param <K> the type parameter
     * @param <V> the type parameter
     * @param object the object
     * @return the function
     */
    public static <K, V> Function<K, V> propertyOf(final Map<K, V> object) {
        return object::get;
    }

    /**
     * Matcher predicate.
     *
     * @param <K> the type parameter
     * @param <V> the type parameter
     * @param object the object
     * @return the predicate
     */
    public static <K, V> Predicate<Map<K, V>> matcher(final Map<K, V> object) {
        return item -> {
            for (final K key : keys(object)) {
                if (!item.containsKey(key) || !item.get(key).equals(object.get(key))) {
                    return false;
                }
            }
            return true;
        };
    }

    /**
     * Times.
     *
     * @param count the count
     * @param runnable the runnable
     */
    /*
     * Documented, #times
     */
    public static void times(final int count, final Runnable runnable) {
        for (int index = 0; index < count; index += 1) {
            runnable.run();
        }
    }

    /**
     * Random int.
     *
     * @param min the min
     * @param max the max
     * @return the int
     */
    /*
     * Documented, #random
     */
    public static int random(final int min, final int max) {
        return min + new java.security.SecureRandom().nextInt(max - min + 1);
    }

    /**
     * Random int.
     *
     * @param max the max
     * @return the int
     */
    public static int random(final int max) {
        return new java.security.SecureRandom().nextInt(max + 1);
    }

    /**
     * Now long.
     *
     * @return the long
     */
    public static long now() {
        return new Date().getTime();
    }

    /**
     * Escape string.
     *
     * @param value the value
     * @return the string
     */
    /*
     * Documented, #escape
     */
    public static String escape(final String value) {
        final StringBuilder builder = new StringBuilder();
        for (final char ch : value.toCharArray()) {
            builder.append(ESCAPES.containsKey(ch) ? ESCAPES.get(ch) : ch);
        }
        return builder.toString();
    }

    /**
     * Unescape string.
     *
     * @param value the value
     * @return the string
     */
    public static String unescape(final String value) {
        return value.replace("&#x60;", "`")
                .replace("&#x27;", "'")
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&quot;", "\"")
                .replace("&amp;", "&");
    }

    /**
     * Result object.
     *
     * @param <E> the type parameter
     * @param iterable the iterable
     * @param pred the pred
     * @return the object
     */
    /*
     * Documented, #result
     */
    public static <E> Object result(final Iterable<E> iterable, final Predicate<E> pred) {
        for (E element : iterable) {
            if (pred.test(element)) {
                if (element instanceof Map.Entry) {
                    if (((Map.Entry) element).getValue() instanceof Supplier) {
                        return ((Supplier) ((Map.Entry) element).getValue()).get();
                    }
                    return ((Map.Entry) element).getValue();
                }
                return element;
            }
        }
        return null;
    }

    /**
     * Unique id string.
     *
     * @param prefix the prefix
     * @return the string
     */
    /*
     * Documented, #uniqueId
     */
    public static String uniqueId(final String prefix) {
        return (prefix == null ? "" : prefix) + UNIQUE_ID.incrementAndGet();
    }

    /**
     * Unique password string.
     *
     * @return the string
     */
    /*
     * Documented, #uniquePassword
     */
    public static String uniquePassword() {
        final String[] passwords =
                new String[] {
                    "ALKJVBPIQYTUIWEBVPQALZVKQRWORTUYOYISHFLKAJMZNXBVMNFGAHKJSDFALAPOQIERIUYTGSFGKMZNXBVJAHGFAKX",
                    "1234567890",
                    "qpowiealksdjzmxnvbfghsdjtreiuowiruksfhksajmzxncbvlaksjdhgqwetytopskjhfgvbcnmzxalksjdfhgbvzm",
                    ".@,-+/()#$%^&*!"
                };
        final StringBuilder result = new StringBuilder();
        final long passwordLength =
                Math.abs(UUID.randomUUID().getLeastSignificantBits() % MIN_PASSWORD_LENGTH_8)
                        + MIN_PASSWORD_LENGTH_8;
        for (int index = 0; index < passwordLength; index += 1) {
            final int passIndex = (int) (passwords.length * (long) index / passwordLength);
            final int charIndex =
                    (int)
                            Math.abs(
                                    UUID.randomUUID().getLeastSignificantBits()
                                            % passwords[passIndex].length());
            result.append(passwords[passIndex].charAt(charIndex));
        }
        return result.toString();
    }

    /**
     * Template template.
     *
     * @param <K> the type parameter
     * @param <V> the type parameter
     * @param template the template
     * @return the template
     */
    public static <K, V> Template<Map<K, V>> template(final String template) {
        return new TemplateImpl<>(template);
    }

    /**
     * Format string.
     *
     * @param template the template
     * @param params the params
     * @return the string
     */
    public static String format(final String template, final Object... params) {
        final java.util.regex.Matcher matcher = FORMAT_PATTERN.matcher(template);
        final StringBuffer buffer = new StringBuffer();
        int index = 0;
        while (matcher.find()) {
            if (matcher.group(1).isEmpty()) {
                matcher.appendReplacement(buffer, "<%" + index++ + "%>");
            } else {
                matcher.appendReplacement(buffer, "<%" + matcher.group(1) + "%>");
            }
        }
        matcher.appendTail(buffer);
        final String newTemplate = buffer.toString();
        final Map<Integer, String> args = new LinkedHashMap<>();
        index = 0;
        for (Object param : params) {
            args.put(index, param.toString());
            index += 1;
        }
        return new TemplateImpl<Integer, String>(newTemplate).apply(args);
    }

    /**
     * Iterate iterable.
     *
     * @param <T> the type parameter
     * @param seed the seed
     * @param unaryOperator the unary operator
     * @return the iterable
     */
    public static <T> Iterable<T> iterate(final T seed, final UnaryOperator<T> unaryOperator) {
        return new MyIterable<>(seed, unaryOperator);
    }

    /**
     * Chain chain.
     *
     * @param <T> the type parameter
     * @param list the list
     * @return the chain
     */
    /*
     * Documented, #chain
     */
    public static <T> Chain<T> chain(final List<T> list) {
        return new Underscore.Chain<>(list);
    }

    /**
     * Chain chain.
     *
     * @param map the map
     * @return the chain
     */
    public static Chain<Map<String, Object>> chain(final Map<String, Object> map) {
        return new Underscore.Chain<>(map);
    }

    /**
     * Chain chain.
     *
     * @param <T> the type parameter
     * @param iterable the iterable
     * @return the chain
     */
    public static <T> Chain<T> chain(final Iterable<T> iterable) {
        return new Underscore.Chain<>(newArrayList(iterable));
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
        return new Underscore.Chain<>(newArrayList(iterable, size));
    }

    /**
     * Chain chain.
     *
     * @param <T> the type parameter
     * @param array the array
     * @return the chain
     */
    @SuppressWarnings("unchecked")
    public static <T> Chain<T> chain(final T... array) {
        return new Underscore.Chain<>(Arrays.asList(array));
    }

    /**
     * Chain chain.
     *
     * @param array the array
     * @return the chain
     */
    public static Chain<Integer> chain(final int[] array) {
        return new Underscore.Chain<>(newIntegerList(array));
    }

    /**
     * Chain chain.
     *
     * @return the chain
     */
    public Chain<T> chain() {
        return new Underscore.Chain<>(newArrayList(iterable));
    }

    /**
     * Of chain.
     *
     * @param <T> the type parameter
     * @param list the list
     * @return the chain
     */
    public static <T> Chain<T> of(final List<T> list) {
        return new Underscore.Chain<>(list);
    }

    /**
     * Of chain.
     *
     * @param <T> the type parameter
     * @param iterable the iterable
     * @return the chain
     */
    public static <T> Chain<T> of(final Iterable<T> iterable) {
        return new Underscore.Chain<>(newArrayList(iterable));
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
        return new Underscore.Chain<>(newArrayList(iterable, size));
    }

    /**
     * Of chain.
     *
     * @param <T> the type parameter
     * @param array the array
     * @return the chain
     */
    @SuppressWarnings("unchecked")
    public static <T> Chain<T> of(final T... array) {
        return new Underscore.Chain<>(Arrays.asList(array));
    }

    /**
     * Of chain.
     *
     * @param array the array
     * @return the chain
     */
    public static Chain<Integer> of(final int[] array) {
        return new Underscore.Chain<>(newIntegerList(array));
    }

    /**
     * Of chain.
     *
     * @return the chain
     */
    public Chain<T> of() {
        return new Underscore.Chain<>(newArrayList(iterable));
    }

    /**
     * The type Chain.
     *
     * @param <T> the type parameter
     */
    public static class Chain<T> {
        private final T item;
        private final List<T> list;
        private final Map<String, Object> map;

        /**
         * Instantiates a new Chain.
         *
         * @param item the item
         */
        public Chain(final T item) {
            this.item = item;
            this.list = null;
            this.map = null;
        }

        /**
         * Instantiates a new Chain.
         *
         * @param list the list
         */
        public Chain(final List<T> list) {
            this.item = null;
            this.list = list;
            this.map = null;
        }

        /**
         * Instantiates a new Chain.
         *
         * @param map the map
         */
        public Chain(final Map<String, Object> map) {
            this.item = null;
            this.list = null;
            this.map = map;
        }

        /**
         * First chain.
         *
         * @return the chain
         */
        public Chain<T> first() {
            return new Chain<>(Underscore.first(list));
        }

        /**
         * First chain.
         *
         * @param n the n
         * @return the chain
         */
        public Chain<T> first(int n) {
            return new Chain<>(Underscore.first(list, n));
        }

        /**
         * First chain.
         *
         * @param pred the pred
         * @return the chain
         */
        public Chain<T> first(final Predicate<T> pred) {
            return new Chain<>(Underscore.first(list, pred));
        }

        /**
         * First chain.
         *
         * @param pred the pred
         * @param n the n
         * @return the chain
         */
        public Chain<T> first(final Predicate<T> pred, int n) {
            return new Chain<>(Underscore.first(list, pred, n));
        }

        /**
         * First or null chain.
         *
         * @return the chain
         */
        public Chain<T> firstOrNull() {
            return new Chain<>(Underscore.firstOrNull(list));
        }

        /**
         * First or null chain.
         *
         * @param pred the pred
         * @return the chain
         */
        public Chain<T> firstOrNull(final Predicate<T> pred) {
            return new Chain<>(Underscore.firstOrNull(list, pred));
        }

        /**
         * Initial chain.
         *
         * @return the chain
         */
        public Chain<T> initial() {
            return new Chain<>(Underscore.initial(list));
        }

        /**
         * Initial chain.
         *
         * @param n the n
         * @return the chain
         */
        public Chain<T> initial(int n) {
            return new Chain<>(Underscore.initial(list, n));
        }

        /**
         * Last chain.
         *
         * @return the chain
         */
        public Chain<T> last() {
            return new Chain<>(Underscore.last(list));
        }

        /**
         * Last chain.
         *
         * @param n the n
         * @return the chain
         */
        public Chain<T> last(int n) {
            return new Chain<>(Underscore.last(list, n));
        }

        /**
         * Last or null chain.
         *
         * @return the chain
         */
        public Chain<T> lastOrNull() {
            return new Chain<>(Underscore.lastOrNull(list));
        }

        /**
         * Last or null chain.
         *
         * @param pred the pred
         * @return the chain
         */
        public Chain<T> lastOrNull(final Predicate<T> pred) {
            return new Chain<>(Underscore.lastOrNull(list, pred));
        }

        /**
         * Rest chain.
         *
         * @return the chain
         */
        public Chain<T> rest() {
            return new Chain<>(Underscore.rest(list));
        }

        /**
         * Rest chain.
         *
         * @param n the n
         * @return the chain
         */
        public Chain<T> rest(int n) {
            return new Chain<>(Underscore.rest(list, n));
        }

        /**
         * Compact chain.
         *
         * @return the chain
         */
        public Chain<T> compact() {
            return new Chain<>(Underscore.compact(list));
        }

        /**
         * Compact chain.
         *
         * @param falsyValue the falsy value
         * @return the chain
         */
        public Chain<T> compact(final T falsyValue) {
            return new Chain<>(Underscore.compact(list, falsyValue));
        }

        /**
         * Flatten chain.
         *
         * @return the chain
         */
        @SuppressWarnings("unchecked")
        public Chain flatten() {
            return new Chain<>(Underscore.flatten(list));
        }

        /**
         * Map chain.
         *
         * @param <F> the type parameter
         * @param func the func
         * @return the chain
         */
        public <F> Chain<F> map(final Function<? super T, F> func) {
            return new Chain<>(Underscore.map(list, func));
        }

        /**
         * Map multi chain.
         *
         * @param <F> the type parameter
         * @param mapper the mapper
         * @return the chain
         */
        public <F> Chain<F> mapMulti(final BiConsumer<? super T, ? super Consumer<F>> mapper) {
            return new Chain<>(Underscore.mapMulti(list, mapper));
        }

        /**
         * Map indexed chain.
         *
         * @param <F> the type parameter
         * @param func the func
         * @return the chain
         */
        public <F> Chain<F> mapIndexed(final BiFunction<Integer, ? super T, F> func) {
            return new Chain<>(Underscore.mapIndexed(list, func));
        }

        /**
         * Replace chain.
         *
         * @param pred the pred
         * @param value the value
         * @return the chain
         */
        public Chain<T> replace(final Predicate<T> pred, final T value) {
            return new Chain<>(Underscore.replace(list, pred, value));
        }

        /**
         * Replace indexed chain.
         *
         * @param pred the pred
         * @param value the value
         * @return the chain
         */
        public Chain<T> replaceIndexed(final PredicateIndexed<T> pred, final T value) {
            return new Chain<>(Underscore.replaceIndexed(list, pred, value));
        }

        /**
         * Filter chain.
         *
         * @param pred the pred
         * @return the chain
         */
        public Chain<T> filter(final Predicate<T> pred) {
            return new Chain<>(Underscore.filter(list, pred));
        }

        /**
         * Filter indexed chain.
         *
         * @param pred the pred
         * @return the chain
         */
        public Chain<T> filterIndexed(final PredicateIndexed<T> pred) {
            return new Chain<>(Underscore.filterIndexed(list, pred));
        }

        /**
         * Reject chain.
         *
         * @param pred the pred
         * @return the chain
         */
        public Chain<T> reject(final Predicate<T> pred) {
            return new Chain<>(Underscore.reject(list, pred));
        }

        /**
         * Reject indexed chain.
         *
         * @param pred the pred
         * @return the chain
         */
        public Chain<T> rejectIndexed(final PredicateIndexed<T> pred) {
            return new Chain<>(Underscore.rejectIndexed(list, pred));
        }

        /**
         * Filter false chain.
         *
         * @param pred the pred
         * @return the chain
         */
        public Chain<T> filterFalse(final Predicate<T> pred) {
            return new Chain<>(Underscore.reject(list, pred));
        }

        /**
         * Reduce chain.
         *
         * @param <F> the type parameter
         * @param func the func
         * @param zeroElem the zero elem
         * @return the chain
         */
        public <F> Chain<F> reduce(final BiFunction<F, T, F> func, final F zeroElem) {
            return new Chain<>(Underscore.reduce(list, func, zeroElem));
        }

        /**
         * Reduce chain.
         *
         * @param func the func
         * @return the chain
         */
        public Chain<Optional<T>> reduce(final BinaryOperator<T> func) {
            return new Chain<>(Underscore.reduce(list, func));
        }

        /**
         * Reduce right chain.
         *
         * @param <F> the type parameter
         * @param func the func
         * @param zeroElem the zero elem
         * @return the chain
         */
        public <F> Chain<F> reduceRight(final BiFunction<F, T, F> func, final F zeroElem) {
            return new Chain<>(Underscore.reduceRight(list, func, zeroElem));
        }

        /**
         * Reduce right chain.
         *
         * @param func the func
         * @return the chain
         */
        public Chain<Optional<T>> reduceRight(final BinaryOperator<T> func) {
            return new Chain<>(Underscore.reduceRight(list, func));
        }

        /**
         * Find chain.
         *
         * @param pred the pred
         * @return the chain
         */
        public Chain<Optional<T>> find(final Predicate<T> pred) {
            return new Chain<>(Underscore.find(list, pred));
        }

        /**
         * Find last chain.
         *
         * @param pred the pred
         * @return the chain
         */
        public Chain<Optional<T>> findLast(final Predicate<T> pred) {
            return new Chain<>(Underscore.findLast(list, pred));
        }

        /**
         * Max chain.
         *
         * @return the chain
         */
        @SuppressWarnings("unchecked")
        public Chain<Comparable> max() {
            return new Chain<>(Underscore.max((Collection) list));
        }

        /**
         * Max chain.
         *
         * @param <F> the type parameter
         * @param func the func
         * @return the chain
         */
        public <F extends Comparable<? super F>> Chain<T> max(final Function<T, F> func) {
            return new Chain<>(Underscore.max(list, func));
        }

        /**
         * Min chain.
         *
         * @return the chain
         */
        @SuppressWarnings("unchecked")
        public Chain<Comparable> min() {
            return new Chain<>(Underscore.min((Collection) list));
        }

        /**
         * Min chain.
         *
         * @param <F> the type parameter
         * @param func the func
         * @return the chain
         */
        public <F extends Comparable<? super F>> Chain<T> min(final Function<T, F> func) {
            return new Chain<>(Underscore.min(list, func));
        }

        /**
         * Sort chain.
         *
         * @return the chain
         */
        @SuppressWarnings("unchecked")
        public Chain<Comparable> sort() {
            return new Chain<>(Underscore.sort((List<Comparable>) list));
        }

        /**
         * Sort with chain.
         *
         * @param <F> the type parameter
         * @param comparator the comparator
         * @return the chain
         */
        @SuppressWarnings("unchecked")
        public <F extends Comparable<? super F>> Chain<F> sortWith(final Comparator<F> comparator) {
            return new Chain<>(Underscore.sortWith((List<F>) list, comparator));
        }

        /**
         * Sort by chain.
         *
         * @param <F> the type parameter
         * @param func the func
         * @return the chain
         */
        public <F extends Comparable<? super F>> Chain<T> sortBy(final Function<T, F> func) {
            return new Chain<>(Underscore.sortBy(list, func));
        }

        /**
         * Sort by chain.
         *
         * @param <K> the type parameter
         * @param key the key
         * @return the chain
         */
        @SuppressWarnings("unchecked")
        public <K> Chain<Map<K, Comparable>> sortBy(final K key) {
            return new Chain<>(Underscore.sortBy((List<Map<K, Comparable>>) list, key));
        }

        /**
         * Group by chain.
         *
         * @param <F> the type parameter
         * @param func the func
         * @return the chain
         */
        public <F> Chain<Map<F, List<T>>> groupBy(final Function<T, F> func) {
            return new Chain<>(Underscore.groupBy(list, func));
        }

        /**
         * Associate by chain.
         *
         * @param <F> the type parameter
         * @param func the func
         * @return the chain
         */
        public <F> Chain<Map<F, T>> associateBy(final Function<T, F> func) {
            return new Chain<>(Underscore.associateBy(list, func));
        }

        /**
         * Group by chain.
         *
         * @param <F> the type parameter
         * @param func the func
         * @param binaryOperator the binary operator
         * @return the chain
         */
        public <F> Chain<Map<F, Optional<T>>> groupBy(
                final Function<T, F> func, final BinaryOperator<T> binaryOperator) {
            return new Chain<>(Underscore.groupBy(list, func, binaryOperator));
        }

        /**
         * Index by chain.
         *
         * @param property the property
         * @return the chain
         */
        public Chain<Map<Object, List<T>>> indexBy(final String property) {
            return new Chain<>(Underscore.indexBy(list, property));
        }

        /**
         * Count by chain.
         *
         * @param <F> the type parameter
         * @param func the func
         * @return the chain
         */
        public <F> Chain<Map<F, Integer>> countBy(final Function<T, F> func) {
            return new Chain<>(Underscore.countBy(list, func));
        }

        /**
         * Count by chain.
         *
         * @return the chain
         */
        public Chain<Map<T, Integer>> countBy() {
            return new Chain<>(Underscore.countBy(list));
        }

        /**
         * Shuffle chain.
         *
         * @return the chain
         */
        public Chain<T> shuffle() {
            return new Chain<>(Underscore.shuffle(list));
        }

        /**
         * Sample chain.
         *
         * @return the chain
         */
        public Chain<T> sample() {
            return new Chain<>(Underscore.sample(list));
        }

        /**
         * Sample chain.
         *
         * @param howMany the how many
         * @return the chain
         */
        public Chain<T> sample(final int howMany) {
            return new Chain<>(Underscore.newArrayList(Underscore.sample(list, howMany)));
        }

        /**
         * Tap chain.
         *
         * @param func the func
         * @return the chain
         */
        public Chain<T> tap(final Consumer<T> func) {
            Underscore.each(list, func);
            return new Chain<>(list);
        }

        /**
         * For each chain.
         *
         * @param func the func
         * @return the chain
         */
        public Chain<T> forEach(final Consumer<T> func) {
            return tap(func);
        }

        /**
         * For each right chain.
         *
         * @param func the func
         * @return the chain
         */
        public Chain<T> forEachRight(final Consumer<T> func) {
            Underscore.eachRight(list, func);
            return new Chain<>(list);
        }

        /**
         * Every chain.
         *
         * @param pred the pred
         * @return the chain
         */
        public Chain<Boolean> every(final Predicate<T> pred) {
            return new Chain<>(Underscore.every(list, pred));
        }

        /**
         * Some chain.
         *
         * @param pred the pred
         * @return the chain
         */
        public Chain<Boolean> some(final Predicate<T> pred) {
            return new Chain<>(Underscore.some(list, pred));
        }

        /**
         * Count chain.
         *
         * @param pred the pred
         * @return the chain
         */
        public Chain<Integer> count(final Predicate<T> pred) {
            return new Chain<>(Underscore.count(list, pred));
        }

        /**
         * Contains chain.
         *
         * @param elem the elem
         * @return the chain
         */
        public Chain<Boolean> contains(final T elem) {
            return new Chain<>(Underscore.contains(list, elem));
        }

        /**
         * Contains with chain.
         *
         * @param elem the elem
         * @return the chain
         */
        public Chain<Boolean> containsWith(final T elem) {
            return new Chain<>(Underscore.containsWith(list, elem));
        }

        /**
         * Invoke chain.
         *
         * @param methodName the method name
         * @param args the args
         * @return the chain
         */
        public Chain<T> invoke(final String methodName, final List<Object> args) {
            return new Chain<>(Underscore.invoke(list, methodName, args));
        }

        /**
         * Invoke chain.
         *
         * @param methodName the method name
         * @return the chain
         */
        public Chain<T> invoke(final String methodName) {
            return new Chain<>(Underscore.invoke(list, methodName));
        }

        /**
         * Pluck chain.
         *
         * @param propertyName the property name
         * @return the chain
         */
        public Chain<Object> pluck(final String propertyName) {
            return new Chain<>(Underscore.pluck(list, propertyName));
        }

        /**
         * Where chain.
         *
         * @param <E> the type parameter
         * @param properties the properties
         * @return the chain
         */
        public <E> Chain<T> where(final List<Map.Entry<String, E>> properties) {
            return new Chain<>(Underscore.where(list, properties));
        }

        /**
         * Find where chain.
         *
         * @param <E> the type parameter
         * @param properties the properties
         * @return the chain
         */
        public <E> Chain<Optional<T>> findWhere(final List<Map.Entry<String, E>> properties) {
            return new Chain<>(Underscore.findWhere(list, properties));
        }

        /**
         * Uniq chain.
         *
         * @return the chain
         */
        public Chain<T> uniq() {
            return new Chain<>(Underscore.uniq(list));
        }

        /**
         * Uniq chain.
         *
         * @param <F> the type parameter
         * @param func the func
         * @return the chain
         */
        public <F> Chain<T> uniq(final Function<T, F> func) {
            return new Chain<>(Underscore.newArrayList(Underscore.uniq(list, func)));
        }

        /**
         * Distinct chain.
         *
         * @return the chain
         */
        public Chain<T> distinct() {
            return new Chain<>(Underscore.uniq(list));
        }

        /**
         * Distinct by chain.
         *
         * @param <F> the type parameter
         * @param func the func
         * @return the chain
         */
        @SuppressWarnings("unchecked")
        public <F> Chain<F> distinctBy(final Function<T, F> func) {
            return new Chain<>(Underscore.newArrayList((Iterable<F>) Underscore.uniq(list, func)));
        }

        /**
         * Union chain.
         *
         * @param lists the lists
         * @return the chain
         */
        @SuppressWarnings("unchecked")
        public Chain<T> union(final List<T>... lists) {
            return new Chain<>(Underscore.union(list, lists));
        }

        /**
         * Intersection chain.
         *
         * @param lists the lists
         * @return the chain
         */
        @SuppressWarnings("unchecked")
        public Chain<T> intersection(final List<T>... lists) {
            return new Chain<>(Underscore.intersection(list, lists));
        }

        /**
         * Difference chain.
         *
         * @param lists the lists
         * @return the chain
         */
        @SuppressWarnings("unchecked")
        public Chain<T> difference(final List<T>... lists) {
            return new Chain<>(Underscore.difference(list, lists));
        }

        /**
         * Range chain.
         *
         * @param stop the stop
         * @return the chain
         */
        public Chain<Integer> range(final int stop) {
            return new Chain<>(Underscore.range(stop));
        }

        /**
         * Range chain.
         *
         * @param start the start
         * @param stop the stop
         * @return the chain
         */
        public Chain<Integer> range(final int start, final int stop) {
            return new Chain<>(Underscore.range(start, stop));
        }

        /**
         * Range chain.
         *
         * @param start the start
         * @param stop the stop
         * @param step the step
         * @return the chain
         */
        public Chain<Integer> range(final int start, final int stop, final int step) {
            return new Chain<>(Underscore.range(start, stop, step));
        }

        /**
         * Chunk chain.
         *
         * @param size the size
         * @return the chain
         */
        public Chain<List<T>> chunk(final int size) {
            return new Chain<>(Underscore.chunk(value(), size, size));
        }

        /**
         * Chunk chain.
         *
         * @param size the size
         * @param step the step
         * @return the chain
         */
        public Chain<List<T>> chunk(final int size, final int step) {
            return new Chain<>(Underscore.chunk(value(), size, step));
        }

        /**
         * Chunk fill chain.
         *
         * @param size the size
         * @param fillValue the fill value
         * @return the chain
         */
        public Chain<List<T>> chunkFill(final int size, final T fillValue) {
            return new Chain<>(Underscore.chunkFill(value(), size, size, fillValue));
        }

        /**
         * Chunk fill chain.
         *
         * @param size the size
         * @param step the step
         * @param fillValue the fill value
         * @return the chain
         */
        public Chain<List<T>> chunkFill(final int size, final int step, final T fillValue) {
            return new Chain<>(Underscore.chunkFill(value(), size, step, fillValue));
        }

        /**
         * Cycle chain.
         *
         * @param times the times
         * @return the chain
         */
        public Chain<T> cycle(final int times) {
            return new Chain<>(Underscore.cycle(value(), times));
        }

        /**
         * Interpose chain.
         *
         * @param element the element
         * @return the chain
         */
        public Chain<T> interpose(final T element) {
            return new Chain<>(Underscore.interpose(value(), element));
        }

        /**
         * Interpose by list chain.
         *
         * @param interIter the inter iter
         * @return the chain
         */
        public Chain<T> interposeByList(final Iterable<T> interIter) {
            return new Chain<>(Underscore.interposeByList(value(), interIter));
        }

        /**
         * Concat chain.
         *
         * @param lists the lists
         * @return the chain
         */
        @SuppressWarnings("unchecked")
        public Chain<T> concat(final List<T>... lists) {
            return new Chain<>(Underscore.concat(list, lists));
        }

        /**
         * Slice chain.
         *
         * @param start the start
         * @return the chain
         */
        public Chain<T> slice(final int start) {
            return new Chain<>(Underscore.slice(list, start));
        }

        /**
         * Slice chain.
         *
         * @param start the start
         * @param end the end
         * @return the chain
         */
        public Chain<T> slice(final int start, final int end) {
            return new Chain<>(Underscore.slice(list, start, end));
        }

        /**
         * Split at chain.
         *
         * @param position the position
         * @return the chain
         */
        public Chain<List<T>> splitAt(final int position) {
            return new Chain<>(Underscore.splitAt(list, position));
        }

        /**
         * Take skipping chain.
         *
         * @param stepSize the step size
         * @return the chain
         */
        public Chain<T> takeSkipping(final int stepSize) {
            return new Chain<>(Underscore.takeSkipping(list, stepSize));
        }

        /**
         * Reverse chain.
         *
         * @return the chain
         */
        public Chain<T> reverse() {
            return new Chain<>(Underscore.reverse(list));
        }

        /**
         * Join chain.
         *
         * @return the chain
         */
        public Chain<String> join() {
            return new Chain<>(Underscore.join(list));
        }

        /**
         * Join chain.
         *
         * @param separator the separator
         * @return the chain
         */
        public Chain<String> join(final String separator) {
            return new Chain<>(Underscore.join(list, separator));
        }

        /**
         * Push chain.
         *
         * @param values the values
         * @return the chain
         */
        @SuppressWarnings("unchecked")
        public Chain<T> push(final T... values) {
            return new Chain<>(Underscore.push(value(), values));
        }

        /**
         * Pop chain.
         *
         * @return the chain
         */
        public Chain<Map.Entry<T, List<T>>> pop() {
            return new Chain<>(Underscore.pop(value()));
        }

        /**
         * Shift chain.
         *
         * @return the chain
         */
        public Chain<Map.Entry<T, List<T>>> shift() {
            return new Chain<>(Underscore.shift(value()));
        }

        /**
         * Unshift chain.
         *
         * @param values the values
         * @return the chain
         */
        @SuppressWarnings("unchecked")
        public Chain<T> unshift(final T... values) {
            return new Chain<>(Underscore.unshift(value(), values));
        }

        /**
         * Skip chain.
         *
         * @param numberToSkip the number to skip
         * @return the chain
         */
        public Chain<T> skip(final int numberToSkip) {
            return new Chain<>(list.subList(numberToSkip, list.size()));
        }

        /**
         * Limit chain.
         *
         * @param size the size
         * @return the chain
         */
        public Chain<T> limit(final int size) {
            return new Chain<>(Underscore.first(list, size));
        }

        /**
         * To map chain.
         *
         * @param <K> the type parameter
         * @param <V> the type parameter
         * @return the chain
         */
        @SuppressWarnings("unchecked")
        public <K, V> Chain<Map<K, V>> toMap() {
            return new Chain<>(Underscore.toMap((Iterable<Map.Entry<K, V>>) list));
        }

        /**
         * Is empty boolean.
         *
         * @return the boolean
         */
        public boolean isEmpty() {
            return Underscore.isEmpty(list);
        }

        /**
         * Is not empty boolean.
         *
         * @return the boolean
         */
        public boolean isNotEmpty() {
            return Underscore.isNotEmpty(list);
        }

        /**
         * Size int.
         *
         * @return the int
         */
        public int size() {
            return Underscore.size(list);
        }

        /**
         * Item t.
         *
         * @return the t
         */
        public T item() {
            return item;
        }

        /**
         * Value list.
         *
         * @return the list
         */
        /*
         * Documented, #value
         */
        public List<T> value() {
            return list;
        }

        /**
         * Map map.
         *
         * @return the map
         */
        public Map<String, Object> map() {
            return map;
        }

        /**
         * To list list.
         *
         * @return the list
         */
        public List<T> toList() {
            return list;
        }

        public String toString() {
            return String.valueOf(list);
        }
    }

    /**
     * Mixin.
     *
     * @param funcName the func name
     * @param func the func
     */
    /*
     * Documented, #mixin
     */
    public static void mixin(final String funcName, final UnaryOperator<String> func) {
        FUNCTIONS.put(funcName, func);
    }

    /**
     * Call optional.
     *
     * @param funcName the func name
     * @return the optional
     */
    public Optional<String> call(final String funcName) {
        if (string.isPresent() && FUNCTIONS.containsKey(funcName)) {
            return Optional.of(FUNCTIONS.get(funcName).apply(string.get()));
        }
        return Optional.empty();
    }

    /**
     * Sort list.
     *
     * @param <T> the type parameter
     * @param iterable the iterable
     * @return the list
     */
    public static <T extends Comparable<T>> List<T> sort(final Iterable<T> iterable) {
        final List<T> localList = newArrayList(iterable);
        Collections.sort(localList);
        return localList;
    }

    /**
     * Sort t [ ].
     *
     * @param <T> the type parameter
     * @param array the array
     * @return the t [ ]
     */
    @SuppressWarnings("unchecked")
    public static <T extends Comparable<T>> T[] sort(final T... array) {
        final T[] localArray = array.clone();
        Arrays.sort(localArray);
        return localArray;
    }

    /**
     * Sort list.
     *
     * @return the list
     */
    @SuppressWarnings("unchecked")
    public List<Comparable> sort() {
        return sort((Iterable<Comparable>) iterable);
    }

    /**
     * Join string.
     *
     * @param <T> the type parameter
     * @param iterable the iterable
     * @param separator the separator
     * @return the string
     */
    /*
     * Documented, #join
     */
    public static <T> String join(final Iterable<T> iterable, final String separator) {
        final StringBuilder sb = new StringBuilder();
        int index = 0;
        for (final T item : iterable) {
            if (index > 0) {
                sb.append(separator);
            }
            sb.append(item.toString());
            index += 1;
        }
        return sb.toString();
    }

    /**
     * Join string.
     *
     * @param <T> the type parameter
     * @param iterable the iterable
     * @return the string
     */
    public static <T> String join(final Iterable<T> iterable) {
        return join(iterable, " ");
    }

    /**
     * Join string.
     *
     * @param <T> the type parameter
     * @param array the array
     * @param separator the separator
     * @return the string
     */
    public static <T> String join(final T[] array, final String separator) {
        return join(Arrays.asList(array), separator);
    }

    /**
     * Join string.
     *
     * @param <T> the type parameter
     * @param array the array
     * @return the string
     */
    public static <T> String join(final T[] array) {
        return join(array, " ");
    }

    /**
     * Join string.
     *
     * @param separator the separator
     * @return the string
     */
    public String join(final String separator) {
        return join(iterable, separator);
    }

    /**
     * Join string.
     *
     * @return the string
     */
    public String join() {
        return join(iterable);
    }

    /**
     * Push list.
     *
     * @param <T> the type parameter
     * @param list the list
     * @param values the values
     * @return the list
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> push(final List<T> list, final T... values) {
        final List<T> result = newArrayList(list);
        Collections.addAll(result, values);
        return result;
    }

    /**
     * Push list.
     *
     * @param values the values
     * @return the list
     */
    @SuppressWarnings("unchecked")
    public List<T> push(final T... values) {
        return push((List<T>) getIterable(), values);
    }

    /**
     * Pop map . entry.
     *
     * @param <T> the type parameter
     * @param list the list
     * @return the map . entry
     */
    public static <T> Map.Entry<T, List<T>> pop(final List<T> list) {
        return Map.entry(last(list), initial(list));
    }

    /**
     * Pop map . entry.
     *
     * @return the map . entry
     */
    public Map.Entry<T, List<T>> pop() {
        return pop((List<T>) getIterable());
    }

    /**
     * Unshift list.
     *
     * @param <T> the type parameter
     * @param list the list
     * @param values the values
     * @return the list
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> unshift(final List<T> list, final T... values) {
        final List<T> result = newArrayList(list);
        int index = 0;
        for (T value : values) {
            result.add(index, value);
            index += 1;
        }
        return result;
    }

    /**
     * Unshift list.
     *
     * @param values the values
     * @return the list
     */
    @SuppressWarnings("unchecked")
    public List<T> unshift(final T... values) {
        return unshift((List<T>) getIterable(), values);
    }

    /**
     * Shift map . entry.
     *
     * @param <T> the type parameter
     * @param list the list
     * @return the map . entry
     */
    public static <T> Map.Entry<T, List<T>> shift(final List<T> list) {
        return Map.entry(first(list), rest(list));
    }

    /**
     * Shift map . entry.
     *
     * @return the map . entry
     */
    public Map.Entry<T, List<T>> shift() {
        return shift((List<T>) getIterable());
    }

    /**
     * Concat t [ ].
     *
     * @param <T> the type parameter
     * @param first the first
     * @param other the other
     * @return the t [ ]
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] concat(final T[] first, final T[]... other) {
        int length = 0;
        for (T[] otherItem : other) {
            length += otherItem.length;
        }
        final T[] result = Arrays.copyOf(first, first.length + length);
        int index = 0;
        for (T[] otherItem : other) {
            System.arraycopy(otherItem, 0, result, first.length + index, otherItem.length);
            index += otherItem.length;
        }
        return result;
    }

    /**
     * Concat list.
     *
     * @param <T> the type parameter
     * @param first the first
     * @param other the other
     * @return the list
     */
    /*
     * Documented, #concat
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> concat(final Iterable<T> first, final Iterable<T>... other) {
        List<T> list = newArrayList(first);
        for (Iterable<T> iter : other) {
            list.addAll(newArrayList(iter));
        }
        return list;
    }

    /**
     * Concat with list.
     *
     * @param other the other
     * @return the list
     */
    @SuppressWarnings("unchecked")
    public List<T> concatWith(final Iterable<T>... other) {
        return concat(iterable, other);
    }

    /**
     * Slice list.
     *
     * @param <T> the type parameter
     * @param iterable the iterable
     * @param start the start
     * @return the list
     */
    /*
     * Documented, #slice
     */
    public static <T> List<T> slice(final Iterable<T> iterable, final int start) {
        final List<T> result;
        if (start >= 0) {
            result = newArrayList(iterable).subList(start, size(iterable));
        } else {
            result = newArrayList(iterable).subList(size(iterable) + start, size(iterable));
        }
        return result;
    }

    /**
     * Slice t [ ].
     *
     * @param <T> the type parameter
     * @param array the array
     * @param start the start
     * @return the t [ ]
     */
    public static <T> T[] slice(final T[] array, final int start) {
        final T[] result;
        if (start >= 0) {
            result = Arrays.copyOfRange(array, start, array.length);
        } else {
            result = Arrays.copyOfRange(array, array.length + start, array.length);
        }
        return result;
    }

    /**
     * Slice list.
     *
     * @param start the start
     * @return the list
     */
    public List<T> slice(final int start) {
        return slice(iterable, start);
    }

    /**
     * Slice list.
     *
     * @param <T> the type parameter
     * @param iterable the iterable
     * @param start the start
     * @param end the end
     * @return the list
     */
    public static <T> List<T> slice(final Iterable<T> iterable, final int start, final int end) {
        final List<T> result;
        if (start >= 0) {
            if (end > 0) {
                result = newArrayList(iterable).subList(start, end);
            } else {
                result = newArrayList(iterable).subList(start, size(iterable) + end);
            }
        } else {
            if (end > 0) {
                result = newArrayList(iterable).subList(size(iterable) + start, end);
            } else {
                result =
                        newArrayList(iterable)
                                .subList(size(iterable) + start, size(iterable) + end);
            }
        }
        return result;
    }

    /**
     * Slice t [ ].
     *
     * @param <T> the type parameter
     * @param array the array
     * @param start the start
     * @param end the end
     * @return the t [ ]
     */
    public static <T> T[] slice(final T[] array, final int start, final int end) {
        final T[] result;
        if (start >= 0) {
            if (end > 0) {
                result = Arrays.copyOfRange(array, start, end);
            } else {
                result = Arrays.copyOfRange(array, start, array.length + end);
            }
        } else {
            if (end > 0) {
                result = Arrays.copyOfRange(array, array.length + start, end);
            } else {
                result = Arrays.copyOfRange(array, array.length + start, array.length + end);
            }
        }
        return result;
    }

    /**
     * Slice list.
     *
     * @param start the start
     * @param end the end
     * @return the list
     */
    public List<T> slice(final int start, final int end) {
        return slice(iterable, start, end);
    }

    /**
     * Split at list.
     *
     * @param <T> the type parameter
     * @param iterable the iterable
     * @param position the position
     * @return the list
     */
    public static <T> List<List<T>> splitAt(final Iterable<T> iterable, final int position) {
        List<List<T>> result = new ArrayList<>();
        int size = size(iterable);
        final int index;
        if (position < 0) {
            index = 0;
        } else {
            index = position > size ? size : position;
        }
        result.add(newArrayList(iterable).subList(0, index));
        result.add(newArrayList(iterable).subList(index, size));
        return result;
    }

    /**
     * Split at list.
     *
     * @param <T> the type parameter
     * @param array the array
     * @param position the position
     * @return the list
     */
    public static <T> List<List<T>> splitAt(final T[] array, final int position) {
        return splitAt(Arrays.asList(array), position);
    }

    /**
     * Split at list.
     *
     * @param position the position
     * @return the list
     */
    public List<List<T>> splitAt(final int position) {
        return splitAt(iterable, position);
    }

    /**
     * Take skipping list.
     *
     * @param <T> the type parameter
     * @param iterable the iterable
     * @param stepSize the step size
     * @return the list
     */
    public static <T> List<T> takeSkipping(final Iterable<T> iterable, final int stepSize) {
        List<T> result = new ArrayList<>();
        if (stepSize <= 0) {
            return result;
        }
        int size = size(iterable);
        if (stepSize > size) {
            result.add(first(iterable));
            return result;
        }
        int i = 0;
        for (T element : iterable) {
            if (i++ % stepSize == 0) {
                result.add(element);
            }
        }
        return result;
    }

    /**
     * Take skipping list.
     *
     * @param <T> the type parameter
     * @param array the array
     * @param stepSize the step size
     * @return the list
     */
    public static <T> List<T> takeSkipping(final T[] array, final int stepSize) {
        return takeSkipping(Arrays.asList(array), stepSize);
    }

    /**
     * Take skipping list.
     *
     * @param stepSize the step size
     * @return the list
     */
    public List<T> takeSkipping(final int stepSize) {
        return takeSkipping(iterable, stepSize);
    }

    /**
     * Reverse list.
     *
     * @param <T> the type parameter
     * @param iterable the iterable
     * @return the list
     */
    /*
     * Documented, #reverse
     */
    public static <T> List<T> reverse(final Iterable<T> iterable) {
        final List<T> result = newArrayList(iterable);
        Collections.reverse(result);
        return result;
    }

    /**
     * Reverse t [ ].
     *
     * @param <T> the type parameter
     * @param array the array
     * @return the t [ ]
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] reverse(final T... array) {
        T temp;
        final T[] newArray = array.clone();
        for (int index = 0; index < array.length / 2; index += 1) {
            temp = newArray[index];
            newArray[index] = newArray[array.length - 1 - index];
            newArray[array.length - 1 - index] = temp;
        }
        return newArray;
    }

    /**
     * Reverse list.
     *
     * @param array the array
     * @return the list
     */
    public static List<Integer> reverse(final int[] array) {
        final List<Integer> result = newIntegerList(array);
        Collections.reverse(result);
        return result;
    }

    /**
     * Reverse list.
     *
     * @return the list
     */
    public List<T> reverse() {
        return reverse(iterable);
    }

    /**
     * Gets iterable.
     *
     * @return the iterable
     */
    public Iterable<T> getIterable() {
        return iterable;
    }

    /**
     * Value iterable.
     *
     * @return the iterable
     */
    public Iterable<T> value() {
        return iterable;
    }

    /**
     * Gets string.
     *
     * @return the string
     */
    public Optional<String> getString() {
        return string;
    }

    /**
     * Sets timeout.
     *
     * @param <T> the type parameter
     * @param function the function
     * @param delayMilliseconds the delay milliseconds
     * @return the timeout
     */
    public static <T> java.util.concurrent.ScheduledFuture<T> setTimeout(
            final Supplier<T> function, final int delayMilliseconds) {
        return delay(function, delayMilliseconds);
    }

    /**
     * Clear timeout.
     *
     * @param scheduledFuture the scheduled future
     */
    public static void clearTimeout(java.util.concurrent.ScheduledFuture<?> scheduledFuture) {
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
        }
    }

    /**
     * Sets interval.
     *
     * @param <T> the type parameter
     * @param function the function
     * @param delayMilliseconds the delay milliseconds
     * @return the interval
     */
    public static <T> java.util.concurrent.ScheduledFuture setInterval(
            final Supplier<T> function, final int delayMilliseconds) {
        final java.util.concurrent.ScheduledExecutorService scheduler =
                java.util.concurrent.Executors.newSingleThreadScheduledExecutor();
        return scheduler.scheduleAtFixedRate(
                function::get,
                delayMilliseconds,
                delayMilliseconds,
                java.util.concurrent.TimeUnit.MILLISECONDS);
    }

    /**
     * Clear interval.
     *
     * @param scheduledFuture the scheduled future
     */
    public static void clearInterval(java.util.concurrent.ScheduledFuture scheduledFuture) {
        clearTimeout(scheduledFuture);
    }

    /**
     * Copy of list.
     *
     * @param <T> the type parameter
     * @param iterable the iterable
     * @return the list
     */
    public static <T> List<T> copyOf(final Iterable<T> iterable) {
        return newArrayList(iterable);
    }

    /**
     * Copy of list.
     *
     * @return the list
     */
    public List<T> copyOf() {
        return newArrayList(value());
    }

    /**
     * Copy of range list.
     *
     * @param <T> the type parameter
     * @param iterable the iterable
     * @param start the start
     * @param end the end
     * @return the list
     */
    public static <T> List<T> copyOfRange(
            final Iterable<T> iterable, final int start, final int end) {
        return slice(iterable, start, end);
    }

    /**
     * Copy of range list.
     *
     * @param start the start
     * @param end the end
     * @return the list
     */
    public List<T> copyOfRange(final int start, final int end) {
        return slice(value(), start, end);
    }

    /**
     * Element at t.
     *
     * @param <T> the type parameter
     * @param list the list
     * @param index the index
     * @return the t
     */
    public static <T> T elementAt(final List<T> list, final int index) {
        return list.get(index);
    }

    /**
     * Element at t.
     *
     * @param index the index
     * @return the t
     */
    public T elementAt(final int index) {
        return elementAt((List<T>) value(), index);
    }

    /**
     * Get t.
     *
     * @param <T> the type parameter
     * @param list the list
     * @param index the index
     * @return the t
     */
    public static <T> T get(final List<T> list, final int index) {
        return elementAt(list, index);
    }

    /**
     * Get t.
     *
     * @param index the index
     * @return the t
     */
    public T get(final int index) {
        return elementAt((List<T>) value(), index);
    }

    /**
     * Set map . entry.
     *
     * @param <T> the type parameter
     * @param list the list
     * @param index the index
     * @param value the value
     * @return the map . entry
     */
    public static <T> Map.Entry<T, List<T>> set(
            final List<T> list, final int index, final T value) {
        final List<T> newList = newArrayList(list);
        return Map.entry(newList.set(index, value), newList);
    }

    /**
     * Set map . entry.
     *
     * @param index the index
     * @param value the value
     * @return the map . entry
     */
    public Map.Entry<T, List<T>> set(final int index, final T value) {
        return set((List<T>) value(), index, value);
    }

    /**
     * Element at or else t.
     *
     * @param <T> the type parameter
     * @param list the list
     * @param index the index
     * @param defaultValue the default value
     * @return the t
     */
    public static <T> T elementAtOrElse(final List<T> list, final int index, T defaultValue) {
        try {
            return list.get(index);
        } catch (IndexOutOfBoundsException ex) {
            return defaultValue;
        }
    }

    /**
     * Element at or else t.
     *
     * @param index the index
     * @param defaultValue the default value
     * @return the t
     */
    public T elementAtOrElse(final int index, T defaultValue) {
        return elementAtOrElse((List<T>) value(), index, defaultValue);
    }

    /**
     * Element at or null t.
     *
     * @param <T> the type parameter
     * @param list the list
     * @param index the index
     * @return the t
     */
    public static <T> T elementAtOrNull(final List<T> list, final int index) {
        try {
            return list.get(index);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    /**
     * Element at or null t.
     *
     * @param index the index
     * @return the t
     */
    public T elementAtOrNull(final int index) {
        return elementAtOrNull((List<T>) value(), index);
    }

    /**
     * Last index int.
     *
     * @param <T> the type parameter
     * @param iterable the iterable
     * @return the int
     */
    public static <T> int lastIndex(final Iterable<T> iterable) {
        return size(iterable) - 1;
    }

    /**
     * Last index int.
     *
     * @param <T> the type parameter
     * @param array the array
     * @return the int
     */
    public static <T> int lastIndex(final T[] array) {
        return array.length - 1;
    }

    /**
     * Last index int.
     *
     * @param array the array
     * @return the int
     */
    public static int lastIndex(final int[] array) {
        return array.length - 1;
    }

    /**
     * Check not null t.
     *
     * @param <T> the type parameter
     * @param reference the reference
     * @return the t
     */
    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }

    /**
     * Check not null elements list.
     *
     * @param <T> the type parameter
     * @param references the references
     * @return the list
     */
    public static <T> List<T> checkNotNullElements(List<T> references) {
        if (references == null) {
            throw new NullPointerException();
        }
        for (T reference : references) {
            checkNotNull(reference);
        }
        return references;
    }

    /**
     * Check not null t.
     *
     * @param <T> the type parameter
     * @param reference the reference
     * @param errorMessage the error message
     * @return the t
     */
    public static <T> T checkNotNull(T reference, Object errorMessage) {
        if (reference == null) {
            throw new NullPointerException(String.valueOf(errorMessage));
        }
        return reference;
    }

    /**
     * Non null boolean.
     *
     * @param obj the obj
     * @return the boolean
     */
    public static boolean nonNull(Object obj) {
        return obj != null;
    }

    /**
     * Default to t.
     *
     * @param <T> the type parameter
     * @param value the value
     * @param defaultValue the default value
     * @return the t
     */
    public static <T> T defaultTo(T value, T defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    /**
     * New array list list.
     *
     * @param <T> the type parameter
     * @param iterable the iterable
     * @return the list
     */
    protected static <T> List<T> newArrayList(final Iterable<T> iterable) {
        final List<T> result;
        if (iterable instanceof Collection) {
            result = new ArrayList<>((Collection<T>) iterable);
        } else {
            result = new ArrayList<>();
            for (final T item : iterable) {
                result.add(item);
            }
        }
        return result;
    }

    /**
     * New array list list.
     *
     * @param <T> the type parameter
     * @param object the object
     * @return the list
     */
    protected static <T> List<T> newArrayList(final T object) {
        final List<T> result = new ArrayList<>();
        result.add(object);
        return result;
    }

    /**
     * New array list list.
     *
     * @param <T> the type parameter
     * @param iterable the iterable
     * @param size the size
     * @return the list
     */
    protected static <T> List<T> newArrayList(final Iterable<T> iterable, final int size) {
        final List<T> result = new ArrayList<>();
        for (int index = 0; iterable.iterator().hasNext() && index < size; index += 1) {
            result.add(iterable.iterator().next());
        }
        return result;
    }

    /**
     * New integer list list.
     *
     * @param array the array
     * @return the list
     */
    protected static List<Integer> newIntegerList(int... array) {
        final List<Integer> result = new ArrayList<>(array.length);
        for (final int item : array) {
            result.add(item);
        }
        return result;
    }

    /**
     * New array list with expected size list.
     *
     * @param <T> the type parameter
     * @param size the size
     * @return the list
     */
    protected static <T> List<T> newArrayListWithExpectedSize(int size) {
        return new ArrayList<>((int) (CAPACITY_SIZE_5 + size + (size / 10)));
    }

    /**
     * New linked hash set set.
     *
     * @param <T> the type parameter
     * @param iterable the iterable
     * @return the set
     */
    protected static <T> Set<T> newLinkedHashSet(Iterable<T> iterable) {
        final Set<T> result = new LinkedHashSet<>();
        for (final T item : iterable) {
            result.add(item);
        }
        return result;
    }

    /**
     * New linked hash set with expected size set.
     *
     * @param <T> the type parameter
     * @param size the size
     * @return the set
     */
    protected static <T> Set<T> newLinkedHashSetWithExpectedSize(int size) {
        return new LinkedHashSet<>((int) Math.max(size * CAPACITY_COEFF_2, CAPACITY_SIZE_16));
    }

    /**
     * And predicate.
     *
     * @param <T> the type parameter
     * @param pred1 the pred 1
     * @param pred2 the pred 2
     * @param rest the rest
     * @return the predicate
     */
    @SuppressWarnings("unchecked")
    public static <T> Predicate<T> and(
            final Predicate<? super T> pred1,
            final Predicate<? super T> pred2,
            final Predicate<? super T>... rest) {
        checkNotNull(pred1);
        checkNotNull(pred2);
        checkNotNullElements(Arrays.asList(rest));
        return value -> {
            boolean result = pred1.test(value) && pred2.test(value);
            if (!result) {
                return false;
            }
            for (Predicate<? super T> predicate : rest) {
                if (!predicate.test(value)) {
                    return false;
                }
            }
            return true;
        };
    }

    /**
     * Or predicate.
     *
     * @param <T> the type parameter
     * @param pred1 the pred 1
     * @param pred2 the pred 2
     * @param rest the rest
     * @return the predicate
     */
    @SuppressWarnings("unchecked")
    public static <T> Predicate<T> or(
            final Predicate<? super T> pred1,
            final Predicate<? super T> pred2,
            final Predicate<? super T>... rest) {
        checkNotNull(pred1);
        checkNotNull(pred2);
        checkNotNullElements(Arrays.asList(rest));
        return value -> {
            boolean result = pred1.test(value) || pred2.test(value);
            if (result) {
                return true;
            }
            for (Predicate<? super T> predicate : rest) {
                if (predicate.test(value)) {
                    return true;
                }
            }
            return false;
        };
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String... args) {
        final String message =
                "Underscore-java is a java port of Underscore.js.\n\n"
                        + "In addition to porting Underscore's functionality,"
                        + " Underscore-java includes matching unit tests.\n\n"
                        + "For docs, license, tests, and downloads, see: https://javadev.github.io/underscore-java";
        System.out.println(message);
    }

    /**
     * The interface Function 3.
     *
     * @param <F1> the type parameter
     * @param <F2> the type parameter
     * @param <F3> the type parameter
     * @param <T> the type parameter
     */
    public static interface Function3<F1, F2, F3, T> {
        /**
         * Apply t.
         *
         * @param arg1 the arg 1
         * @param arg2 the arg 2
         * @param arg3 the arg 3
         * @return the t
         */
        T apply(F1 arg1, F2 arg2, F3 arg3);
    }

    /**
     * The type Memoize function.
     *
     * @param <F> the type parameter
     * @param <T> the type parameter
     */
    public abstract static class MemoizeFunction<F, T> implements Function<F, T> {
        private final Map<F, T> cache = new LinkedHashMap<>();

        /**
         * Calc t.
         *
         * @param n the n
         * @return the t
         */
        public abstract T calc(final F n);

        public T apply(final F key) {
            cache.putIfAbsent(key, calc(key));
            return cache.get(key);
        }
    }

    /**
     * The interface Predicate indexed.
     *
     * @param <T> the type parameter
     */
    public static interface PredicateIndexed<T> {
        /**
         * Test boolean.
         *
         * @param index the index
         * @param arg the arg
         * @return the boolean
         */
        boolean test(int index, T arg);
    }

    /**
     * The interface Template.
     *
     * @param <T> the type parameter
     */
    public static interface Template<T> extends Function<T, String> {
        /**
         * Check list.
         *
         * @param arg the arg
         * @return the list
         */
        List<String> check(T arg);
    }
}
