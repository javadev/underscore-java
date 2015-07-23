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
import com.github.underscore.Function1;
import com.github.underscore.Function3;
import com.github.underscore.Predicate;

public class $<T> extends com.github.underscore.$<T> {
    private static final int DEFAULT_TRUNC_LENGTH = 30;
    private static final String DEFAULT_TRUNC_OMISSION = "...";
    private static final java.util.regex.Pattern RE_LATIN_1 = java.util.regex.Pattern.compile(
        "[\\xc0-\\xd6\\xd8-\\xde\\xdf-\\xf6\\xf8-\\xff]");
    private static final Map<String, String> DEBURRED_LETTERS = new LinkedHashMap<String, String>() { {
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
    private static String upper = "[A-Z\\xc0-\\xd6\\xd8-\\xde]";
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

        public Chain<List<Object>> fill(final Object value) {
            return new Chain<List<Object>>($.fill((List<Object>) value(), value));
        }

        public Chain<List<Object>> fill(final Object value, final Integer start, final Integer end) {
            return new Chain<List<Object>>($.fill((List<Object>) value(), value, start, end));
        }

        public Chain<List<?>> flattenDeep() {
            return new Chain<List<?>>($.flattenDeep((List<?>) value()));
        }

        public Chain<List<Object>> pull(final Object ... values) {
            return new Chain<List<Object>>($.pull((List<Object>) value(), values));
        }

        public Chain<List<Object>> pullAt(final Integer ... indexes) {
            return new Chain<List<Object>>($.pullAt((List<Object>) value(), indexes));
        }

        public Chain<List<T>> remove(final Predicate<T> pred) {
            return new Chain<List<T>>($.remove(value(), pred));
        }

        public Chain<List<T>> take() {
            return new Chain<List<T>>($.take(value()));
        }

        public Chain<List<T>> takeRight() {
            return new Chain<List<T>>($.takeRight(value()));
        }

        public Chain<List<T>> take(final Integer n) {
            return new Chain<List<T>>($.take(value(), n));
        }

        public Chain<List<T>> takeRight(final Integer n) {
            return new Chain<List<T>>($.takeRight(value(), n));
        }

        public Chain<List<T>> takeWhile(final Predicate<T> pred) {
            return new Chain<List<T>>($.takeWhile(value(), pred));
        }

        public Chain<List<T>> takeRightWhile(final Predicate<T> pred) {
            return new Chain<List<T>>($.takeRightWhile(value(), pred));
        }

        public Chain<List<T>> xor(final List<T> list) {
            return new Chain<List<T>>($.xor(value(), list));
        }

        public Chain<List<T>> at(final Integer ... indexes) {
            return new Chain<List<T>>($.at(value(), indexes));
        }

        public <T extends Number> Chain<T> sum() {
            return new Chain<T>($.sum((List<T>) value()));
        }

        public Chain<Double> mean() {
            return new Chain<Double>($.mean((List<Number>) value()));
        }

        public Chain<Double> median() {
            return new Chain<Double>($.median((List<Number>) value()));
        }

        public Chain<String> camelCase() {
            return new Chain<String>($.camelCase((String) item()));
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

        public Chain<String> padLeft(final int length) {
            return new Chain<String>($.padLeft((String) item(), length));
        }

        public Chain<String> padLeft(final int length, final String chars) {
            return new Chain<String>($.padLeft((String) item(), length, chars));
        }

        public Chain<String> padRight(final int length) {
            return new Chain<String>($.padRight((String) item(), length));
        }

        public Chain<String> padRight(final int length, final String chars) {
            return new Chain<String>($.padRight((String) item(), length, chars));
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

        public Chain<String> trimLeft() {
            return new Chain<String>($.trimLeft((String) item()));
        }

        public Chain<String> trimLeft(final String chars) {
            return new Chain<String>($.trimLeft((String) item(), chars));
        }

        public Chain<String> trimRight() {
            return new Chain<String>($.trimRight((String) item()));
        }

        public Chain<String> trunc() {
            return new Chain<String>($.trunc((String) item()));
        }

        public Chain<String> trunc(final int length) {
            return new Chain<String>($.trunc((String) item(), length));
        }

        public Chain<String> trimRight(final String chars) {
            return new Chain<String>($.trimRight((String) item(), chars));
        }

        public Chain<String> uncapitalize() {
            return new Chain<String>($.uncapitalize((String) item()));
        }

        public Chain<List<String>> words() {
            return new Chain<List<String>>($.words((String) item()));
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

    public Chain chain() {
        return new $.Chain<T>(newArrayList(value()));
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

    public List<Object> pullAt(final Integer ... indexes) {
        return pullAt((List<Object>) getIterable(), indexes);
    }

    public static <T> List<T> remove(final List<T> list, final Predicate<T> pred) {
        final List<T> result = newArrayList();
        for (final Iterator<T> iterator = list.iterator(); iterator.hasNext(); ) {
            final T object = iterator.next();
            if (pred.apply(object)) {
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

    public <T extends Number> T sum() {
        return (T) sum((List<T>) getIterable());
    }

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
        return result.doubleValue() / count;
    }

    public double mean() {
        return mean((Iterable<Number>) getIterable());
    }

    public static <T extends Number> double median(final Iterable<T> iterable) {
        final List<T> result = newArrayList((Collection) iterable);
        final int size = size(iterable);
        if (size % 2 != 0) {
            return result.get(size / 2).doubleValue();
        }
        return (result.get(size / 2 - 1).doubleValue() + result.get(size / 2).doubleValue()) / 2;
    }

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

    public static String capitalize(final String string) {
        final String localString = baseToString(string);
        return localString.isEmpty() ? "" : localString.substring(0, 1).toUpperCase(Locale.getDefault())
            + (localString.length() > 1 ? localString.substring(1) : "");
    }

    public static String uncapitalize(final String string) {
        final String localString = baseToString(string);
        return localString.isEmpty() ? "" : localString.substring(0, 1).toLowerCase(Locale.getDefault())
            + (localString.length() > 1 ? localString.substring(1) : "");
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

    private static Function1<String, String> createCompounder(
        final Function3<String, String, Integer, String> callback) {
        return new Function1<String, String>() {
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

    public static String padLeft(final String string, final Integer length) {
         return createPadDir(false).apply(string, length, null);
    }

    public static String padLeft(final String string, final Integer length, final String chars) {
         return createPadDir(false).apply(string, length, chars);
    }

    public static String padRight(final String string, final Integer length) {
         return createPadDir(true).apply(string, length, null);
    }

    public static String padRight(final String string, final Integer length, final String chars) {
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

    public static String trimLeft(final String string) {
        return trimLeft(string, null);
    }

    public static String trimLeft(final String string, final String chars) {
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

    public static String trimRight(final String string) {
        return trimRight(string, null);
    }

    public static String trimRight(final String string, final String chars) {
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

    public String camelCase() {
        return camelCase(getString().get());
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

    public String padLeft(final int length) {
        return padLeft(getString().get(), length);
    }

    public String padLeft(final int length, final String chars) {
        return padLeft(getString().get(), length, chars);
    }

    public String padRight(final int length) {
        return padRight(getString().get(), length);
    }

    public String padRight(final int length, final String chars) {
        return padRight(getString().get(), length, chars);
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

    public String trimLeft() {
        return trimLeft(getString().get());
    }

    public String trimLeftWith(final String chars) {
        return trimLeft(getString().get(), chars);
    }

    public String trimRight() {
        return trimRight(getString().get());
    }

    public String trimRightWith(final String chars) {
        return trimRight(getString().get(), chars);
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
        final String message = "Underscore-java-lodash is a lodash plugin for underscore-java.\n\n"
            + "For docs, license, tests, and downloads, see: http://javadev.github.io/underscore-java";
        System.out.println(message);
    }
}
