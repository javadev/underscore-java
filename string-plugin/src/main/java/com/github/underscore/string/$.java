/*
 * The MIT License (MIT)
 *
 * Copyright 2016-2018 Valentyn Kolesnikov
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
package com.github.underscore.string;

import com.github.underscore.BiFunction;
import com.github.underscore.Consumer;
import com.github.underscore.Function;
import com.github.underscore.Function3;
import com.github.underscore.Predicate;
import com.github.underscore.PredicateIndexed;
import com.github.underscore.Tuple;
import com.github.underscore.Optional;
import java.util.*;

public class $<T> extends com.github.underscore.$<T> {
    private static final int DEFAULT_TRUNC_LENGTH = 30;
    private static final String DEFAULT_TRUNC_OMISSION = "...";
    private static final String NULL = "null";
    private static final String ELEMENT = "<element>";
    private static final String CLOSED_ELEMENT = "</element>";
    private static final String EMPTY_ELEMENT = ELEMENT + CLOSED_ELEMENT;
    private static final String NULL_ELEMENT = ELEMENT + NULL + CLOSED_ELEMENT;
    private static final java.util.regex.Pattern RE_LATIN_1 = java.util.regex.Pattern.compile(
        "[\\xc0-\\xd6\\xd8-\\xde\\xdf-\\xf6\\xf8-\\xff]");
    private static Map<String, String> deburredLetters = new LinkedHashMap<String, String>() { {
        put("\u00c0", "A"); put("\u00c1", "A"); put("\u00c2", "A"); put("\u00c3", "A");
        put("\u00c4", "A"); put("\u00c5", "A");
        put("\u00e0", "a"); put("\u00e1", "a"); put("\u00e2", "a"); put("\u00e3", "a");
        put("\u00e4", "a"); put("\u00e5", "a");
        put("\u00c7", "C"); put("\u00e7", "c");
        put("\u00d0", "D"); put("\u00f0", "d");
        put("\u00c8", "E"); put("\u00c9", "E"); put("\u00ca", "E"); put("\u00cb", "E");
        put("\u00e8", "e"); put("\u00e9", "e"); put("\u00ea", "e"); put("\u00eb", "e");
        put("\u00cC", "I"); put("\u00cd", "I"); put("\u00ce", "I"); put("\u00cf", "I");
        put("\u00eC", "i"); put("\u00ed", "i"); put("\u00ee", "i"); put("\u00ef", "i");
        put("\u00d1", "N"); put("\u00f1", "n");
        put("\u00d2", "O"); put("\u00d3", "O"); put("\u00d4", "O"); put("\u00d5", "O");
        put("\u00d6", "O"); put("\u00d8", "O");
        put("\u00f2", "o"); put("\u00f3", "o"); put("\u00f4", "o"); put("\u00f5", "o");
        put("\u00f6", "o"); put("\u00f8", "o");
        put("\u00d9", "U"); put("\u00da", "U"); put("\u00db", "U"); put("\u00dc", "U");
        put("\u00f9", "u"); put("\u00fa", "u"); put("\u00fb", "u"); put("\u00fc", "u");
        put("\u00dd", "Y"); put("\u00fd", "y"); put("\u00ff", "y");
        put("\u00c6", "Ae"); put("\u00e6", "ae");
        put("\u00de", "Th"); put("\u00fe", "th");
        put("\u00df", "ss");
    } };
    private static String upper = "[A-Z\\xc0-\\xd6\\xd8-\\xde\\u0400-\\u04FF]";
    private static String lower = "[a-z\\xdf-\\xf6\\xf8-\\xff]+";
    private static java.util.regex.Pattern reWords = java.util.regex.Pattern.compile(
        upper + "+(?=" + upper + lower + ")|" + upper + "?" + lower + "|" + upper + "+|[0-9]+");

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

        @SuppressWarnings("unchecked")
        public Chain<T> union(final List<T> ... lists) {
            return new Chain<T>($.union(value(), lists));
        }

        @SuppressWarnings("unchecked")
        public Chain<T> intersection(final List<T> ... lists) {
            return new Chain<T>($.intersection(value(), lists));
        }

        @SuppressWarnings("unchecked")
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

        @SuppressWarnings("unchecked")
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

        public Chain<String> camelCase() {
            return new Chain<String>($.camelCase((String) item()));
        }

        public Chain<String> lowerFirst() {
            return new Chain<String>($.lowerFirst((String) item()));
        }

        public Chain<String> upperFirst() {
            return new Chain<String>($.upperFirst((String) item()));
        }

        public Chain<String> capitalize() {
            return new Chain<String>($.capitalize((String) item()));
        }

        public Chain<String> deburr() {
            return new Chain<String>($.deburr((String) item()));
        }

        public Chain<Boolean> endsWith(final String target) {
            return new Chain<Boolean>($.endsWith((String) item(), target));
        }

        public Chain<Boolean> endsWith(final String target, final Integer position) {
            return new Chain<Boolean>($.endsWith((String) item(), target, position));
        }

        public Chain<String> kebabCase() {
            return new Chain<String>($.kebabCase((String) item()));
        }

        public Chain<String> repeat(final int length) {
            return new Chain<String>($.repeat((String) item(), length));
        }

        public Chain<String> pad(final int length) {
            return new Chain<String>($.pad((String) item(), length));
        }

        public Chain<String> pad(final int length, final String chars) {
            return new Chain<String>($.pad((String) item(), length, chars));
        }

        public Chain<String> padStart(final int length) {
            return new Chain<String>($.padStart((String) item(), length));
        }

        public Chain<String> padStart(final int length, final String chars) {
            return new Chain<String>($.padStart((String) item(), length, chars));
        }

        public Chain<String> padEnd(final int length) {
            return new Chain<String>($.padEnd((String) item(), length));
        }

        public Chain<String> padEnd(final int length, final String chars) {
            return new Chain<String>($.padEnd((String) item(), length, chars));
        }

        public Chain<String> snakeCase() {
            return new Chain<String>($.snakeCase((String) item()));
        }

        public Chain<String> startCase() {
            return new Chain<String>($.startCase((String) item()));
        }

        public Chain<Boolean> startsWith(final String target) {
            return new Chain<Boolean>($.startsWith((String) item(), target));
        }

        public Chain<Boolean> startsWith(final String target, final Integer position) {
            return new Chain<Boolean>($.startsWith((String) item(), target, position));
        }

        public Chain<String> trim() {
            return new Chain<String>($.trim((String) item()));
        }

        public Chain<String> trim(final String chars) {
            return new Chain<String>($.trim((String) item(), chars));
        }

        public Chain<String> trimStart() {
            return new Chain<String>($.trimStart((String) item()));
        }

        public Chain<String> trimStart(final String chars) {
            return new Chain<String>($.trimStart((String) item(), chars));
        }

        public Chain<String> trimEnd() {
            return new Chain<String>($.trimEnd((String) item()));
        }

        public Chain<String> trunc() {
            return new Chain<String>($.trunc((String) item()));
        }

        public Chain<String> trunc(final int length) {
            return new Chain<String>($.trunc((String) item(), length));
        }

        public Chain<String> trimEnd(final String chars) {
            return new Chain<String>($.trimEnd((String) item(), chars));
        }

        public Chain<String> uncapitalize() {
            return new Chain<String>($.uncapitalize((String) item()));
        }

        public Chain<String> words() {
            return new Chain<String>($.words((String) item()));
        }

        public Chain<String> toJson() {
            return new Chain<String>($.toJson((Collection) value()));
        }

        public Chain<Object> fromJson() {
            return new Chain<Object>($.fromJson((String) item()));
        }

        public Chain<String> toXml() {
            return new Chain<String>($.toXml((Collection) value()));
        }

        public Chain<Object> fromXml() {
            return new Chain<Object>($.fromXml((String) item()));
        }

        public Chain<String> toJsonJavaString() {
            return new Chain<String>($.toJsonJavaString((Collection) value()));
        }
    }

    public static Chain<String> chain(final String item) {
        return new $.Chain<String>(item);
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

    @SuppressWarnings("unchecked")
    public static <T> Chain<T> chain(final T ... list) {
        return new $.Chain<T>(Arrays.asList(list));
    }

    public static Chain<Integer> chain(final int[] array) {
        return new $.Chain<Integer>(newIntegerList(array));
    }

    @SuppressWarnings("unchecked")
    public Chain<T> chain() {
        return new $.Chain<T>(newArrayList(value()));
    }

    public static String camelCase(final String string) {
        return createCompounder(new Function3<String, String, Integer, String>() {
            public String apply(final String result, final String word, final Integer index) {
                final String localWord = word.toLowerCase(Locale.getDefault());
                return result + (index > 0 ? (word.substring(0, 1).toUpperCase(Locale.getDefault())
                    + word.substring(1)) : localWord);
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
                sb.append(deburredLetters.get(str));
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
                return $.invoke(Arrays.asList(chr), methodName).get(0) + trailing;
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
        final int localPosition = position == null ? length
          : Math.min(position < 0 ? 0 : position, length);

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
        final int strLength = string.length();
        if (strLength >= length) {
            return localString;
        }
        final double mid = (length - strLength) / (double) 2;
        final int leftLength = (int) Math.floor(mid);
        final int rightLength = (int) Math.ceil(mid);
        final String localChars = createPadding("", rightLength, chars);
        return localChars.substring(0, leftLength) + string + localChars;
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
        final String result = string.substring(0, end);
        return result + omission;
    }

    public static class JsonStringBuilder {
        private final StringBuilder builder;
        private int ident;

        public JsonStringBuilder() {
            builder = new StringBuilder();
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
                builder.append(' ');
            }
            return this;
        }

        public JsonStringBuilder incIdent() {
            ident += 2;
            return this;
        }

        public JsonStringBuilder decIdent() {
            ident -= 2;
            return this;
        }

        public JsonStringBuilder newLine() {
            builder.append("\n");
            return this;
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

            builder.append('[').incIdent().newLine();
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

            builder.append('{').newLine().incIdent();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                builder.fillSpaces().append('\"');
                builder.append(escape(String.valueOf(entry.getKey())));
                builder.append('\"');
                builder.append(':').append(' ');
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

    public static String toJson(Collection collection) {
        final JsonStringBuilder builder = new JsonStringBuilder();

        JsonArray.writeJson(collection, builder);
        return builder.toString();
    }

    public String toJson() {
        return toJson((Collection) getIterable());
    }

    public static String toJson(Map map) {
        final JsonStringBuilder builder = new JsonStringBuilder();

        JsonObject.writeJson(map, builder);
        return builder.toString();
    }

    public static class XmlStringBuilder {
        protected final StringBuilder builder;
        private int ident;

        public XmlStringBuilder() {
            builder = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n");
            ident = 2;
        }

        public XmlStringBuilder(StringBuilder builder, int ident) {
            this.builder = builder;
            this.ident = ident;
        }

        public XmlStringBuilder append(final String string) {
            builder.append(string);
            return this;
        }

        public XmlStringBuilder fillSpaces() {
            for (int index = 0; index < ident; index += 1) {
                builder.append(' ');
            }
            return this;
        }

        public XmlStringBuilder incIdent() {
            ident += 2;
            return this;
        }

        public XmlStringBuilder decIdent() {
            ident -= 2;
            return this;
        }

        public XmlStringBuilder newLine() {
            builder.append("\n");
            return this;
        }

        public String toString() {
            return builder.toString() + "\n</root>";
        }
    }

    public static class XmlStringBuilderWithoutRoot extends XmlStringBuilder {
        public XmlStringBuilderWithoutRoot() {
            super(new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"), 0);
        }

        public String toString() {
            return builder.toString();
        }
    }

    public static class XmlArray {
        public static void writeXml(Collection collection, XmlStringBuilder builder) {
            if (collection == null) {
                builder.append(NULL);
                return;
            }

            Iterator iter = collection.iterator();

            while (iter.hasNext()) {
                Object value = iter.next();
                if (value == null) {
                    builder.fillSpaces().append(NULL_ELEMENT);
                    continue;
                }

                builder.fillSpaces().append(ELEMENT);
                XmlValue.writeXml(value, builder);
                builder.append(CLOSED_ELEMENT);
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

        public static void writeXml(Object[] array, XmlStringBuilder builder) {
            if (array == null) {
                builder.fillSpaces().append(NULL_ELEMENT);
            } else if (array.length == 0) {
                builder.fillSpaces().append(EMPTY_ELEMENT);
            } else {
                for (int i = 0; i < array.length; i++) {
                    builder.fillSpaces().append(ELEMENT);
                    XmlValue.writeXml(array[i], builder);
                    builder.append(CLOSED_ELEMENT);
                    if (i != array.length - 1) {
                        builder.newLine();
                    }
                }
            }
        }
    }

    public static class XmlObject {
        public static void writeXml(Map map, XmlStringBuilder builder) {
            if (map == null) {
                builder.append(NULL);
                return;
            }

            Iterator iter = map.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                builder.fillSpaces().append("<").append(escape(String.valueOf(entry.getKey()))).append(">");
                XmlValue.writeXml(entry.getValue(), builder);
                builder.append("</").append(escape(String.valueOf(entry.getKey()))).append(">");
                if (iter.hasNext()) {
                    builder.newLine();
                }
            }
        }
    }

    public static class XmlValue {
        public static void writeXml(Object value, XmlStringBuilder builder) {
            if (value == null) {
                builder.append(NULL);
            } else if (value instanceof String) {
                builder.append(escape((String) value));
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
                builder.newLine().incIdent();
                XmlObject.writeXml((Map) value, builder);
                builder.newLine().decIdent().fillSpaces();
            } else if (value instanceof Collection) {
                builder.newLine().incIdent();
                XmlArray.writeXml((Collection) value, builder);
                builder.newLine().decIdent().fillSpaces();
            } else if (value instanceof byte[]) {
                builder.newLine().incIdent();
                XmlArray.writeXml((byte[]) value, builder);
                builder.newLine().decIdent().fillSpaces();
            } else if (value instanceof short[]) {
                builder.newLine().incIdent();
                XmlArray.writeXml((short[]) value, builder);
                builder.newLine().decIdent().fillSpaces();
            } else if (value instanceof int[]) {
                builder.newLine().incIdent();
                XmlArray.writeXml((int[]) value, builder);
                builder.newLine().decIdent().fillSpaces();
            } else if (value instanceof long[]) {
                builder.newLine().incIdent();
                XmlArray.writeXml((long[]) value, builder);
                builder.newLine().decIdent().fillSpaces();
            } else if (value instanceof float[]) {
                builder.newLine().incIdent();
                XmlArray.writeXml((float[]) value, builder);
                builder.newLine().decIdent().fillSpaces();
            } else if (value instanceof double[]) {
                builder.newLine().incIdent();
                XmlArray.writeXml((double[]) value, builder);
                builder.newLine().decIdent().fillSpaces();
            } else if (value instanceof boolean[]) {
                builder.newLine().incIdent();
                XmlArray.writeXml((boolean[]) value, builder);
                builder.newLine().decIdent().fillSpaces();
            } else if (value instanceof char[]) {
                builder.newLine().incIdent();
                XmlArray.writeXml((char[]) value, builder);
                builder.newLine().decIdent().fillSpaces();
            } else if (value instanceof Object[]) {
                builder.newLine().incIdent();
                XmlArray.writeXml((Object[]) value, builder);
                builder.newLine().decIdent().fillSpaces();
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
                    sb.append("&quot;");
                    break;
                case '\'':
                    sb.append("&apos;");
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

    public static String toXml(Collection collection) {
        final XmlStringBuilder builder = new XmlStringBuilder();

        XmlArray.writeXml(collection, builder);
        return builder.toString();
    }

    public String toXml() {
        return toXml((Collection) getIterable());
    }

    public static String toXml(Map map) {
        final XmlStringBuilder builder;
        if (map != null && map.size() == 1) {
            builder = new XmlStringBuilderWithoutRoot();
        } else {
            builder = new XmlStringBuilder();
        }

        XmlObject.writeXml(map, builder);
        return builder.toString();
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
        if (value instanceof Map && ((Map<String, Object>) value).entrySet().iterator().hasNext()) {
            final Map.Entry<String, Object> entry = ((Map<String, Object>) value).entrySet().iterator().next();
            if (entry.getKey().equals("#text") || entry.getKey().equals("element")) {
                return entry.getValue();
            }
        }
        return value;
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> createMap(final org.w3c.dom.Node node) {
        final Map<String, Object> map = newLinkedHashMap();
        final org.w3c.dom.NodeList nodeList = node.getChildNodes();
        for (int index = 0; index < nodeList.getLength(); index++) {
            final org.w3c.dom.Node currentNode = nodeList.item(index);
            final String name = currentNode.getNodeName();
            final Object value;
            if (currentNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                value = createMap(currentNode);
            } else {
                value = currentNode.getTextContent();
            }
            if ("#text".equals(name) && value.toString().trim().isEmpty()) {
                continue;
            }
            if (map.containsKey(name)) {
                final Object object = map.get(name);
                if (object instanceof List) {
                    ((List<Object>) object).add(getValue(value));
                } else {
                    final List<Object> objects = newArrayList();
                    objects.add(object);
                    objects.add(getValue(value));
                    map.put(name, objects);
                }
            } else {
                map.put(name, getValue(value));
            }
        }
        return map;
    }

    public static Object fromXml(final String xml) {
        try {
            final java.io.InputStream stream = new java.io.ByteArrayInputStream(xml.getBytes("UTF-8"));
            final javax.xml.parsers.DocumentBuilderFactory factory =
                javax.xml.parsers.DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            final org.w3c.dom.Document document = factory.newDocumentBuilder().parse(stream);
            return createMap(document);
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

    public static void main(String ... args) {
        final String message = "Underscore-java-string is a string plugin for underscore-java.\n\n"
            + "For docs, license, tests, and downloads, see: http://javadev.github.io/underscore-java";
        System.out.println(message);
    }
}
