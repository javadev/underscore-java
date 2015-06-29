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
package com.github.underscore;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Underscore-java is a java port of Underscore.js.
 *
 * @author Valentyn Kolesnikov
 */
public class $<T> {
    private static ClassForName classForName = new ClassForName();
    private static final Map<String, Function1<String, String>> FUNCTIONS = newLinkedHashMap();
    private static final Map<String, String> TEMPLATE_SETTINGS = new HashMap<String, String>() { {
        put("evaluate", "<%([\\s\\S]+?)%>");
        put("interpolate", "<%=([\\s\\S]+?)%>");
        put("escape", "<%-([\\s\\S]+?)%>");
    } };
    private static final java.util.concurrent.atomic.AtomicInteger UNIQUE_ID =
        new java.util.concurrent.atomic.AtomicInteger(0);
    private static final String ALL_SYMBOLS = "([\\s\\S]+?)";
    private final Iterable<T> iterable;
    private final Optional<String> string;

    public $(final Iterable<T> iterable) {
        this.iterable = iterable;
        this.string = Optional.absent();
    }

    public $(final String string) {
        this.iterable = null;
        this.string = Optional.of(string);
    }

    public static void setClassForName(final ClassForName classForName) {
        $.classForName = classForName;
    }

    private static void setTemplateKey(final Map<String, String> templateSettings, final String key) {
        if (templateSettings.containsKey(key) && templateSettings.get(key).contains(ALL_SYMBOLS)) {
            TEMPLATE_SETTINGS.put(key, templateSettings.get(key));
        }
    }

    public static void templateSettings(final Map<String, String> templateSettings) {
        setTemplateKey(templateSettings, "evaluate");
        setTemplateKey(templateSettings, "interpolate");
        setTemplateKey(templateSettings, "escape");
    }

    private static final class WherePredicate<E, T> implements Predicate<E> {
        private final List<Tuple<String, T>> properties;

        private WherePredicate(List<Tuple<String, T>> properties) {
            this.properties = properties;
        }

        @Override
        public Boolean apply(final E elem) {
            for (Tuple<String, T> prop : properties) {
                try {
                    if (!elem.getClass().getField(prop.fst()).get(elem)
                            .equals(prop.snd())) {
                        return false;
                    }
                } catch (Exception ex) {
                    ex.getMessage();
                }
            }
            return true;
        }
    }

    private static final class TemplateImpl<E> implements Template<Set<E>> {
        private final String template;

        private TemplateImpl(String template) {
            this.template = template;
        }

        @Override
        public String apply(Set<E> value) {
            final String evaluate = TEMPLATE_SETTINGS.get("evaluate");
            final String interpolate = TEMPLATE_SETTINGS.get("interpolate");
            final String escape = TEMPLATE_SETTINGS.get("escape");
            String result = template;
            for (final E element : value) {
                result = java.util.regex.Pattern.compile(interpolate.replace(ALL_SYMBOLS,
                    "\\s*\\Q" + ((Map.Entry) element).getKey()
                    + "\\E\\s*")).matcher(result).replaceAll(String.valueOf(((Map.Entry) element).getValue()));
                result = java.util.regex.Pattern.compile(escape.replace(ALL_SYMBOLS,
                    "\\s*\\Q" + ((Map.Entry) element).getKey()
                    + "\\E\\s*")).matcher(result).replaceAll(escape(String.valueOf(((Map.Entry) element)
                    .getValue())));
                java.util.regex.Matcher matcher = java.util.regex.Pattern.compile(
                    evaluate.replace(ALL_SYMBOLS,
                    "\\s*_\\.each\\((\\w+),\\s*function\\((\\w+)\\)\\s*\\{\\s*") + "(.*?)"
                    + evaluate.replace(ALL_SYMBOLS, " \\}\\);\\s*"))
                    .matcher(result);
                if (matcher.find() && ((Map.Entry) element).getKey().equals(matcher.group(1))) {
                    StringBuilder repeatResult = new StringBuilder();
                    for (String item : (List<String>) ((Map.Entry) element).getValue()) {
                        repeatResult.append(java.util.regex.Pattern.compile(
                            interpolate.replace(ALL_SYMBOLS, "\\s*\\Q" + matcher.group(2)
                            + "\\E\\s*")).matcher(matcher.group(3)).replaceAll(item));
                    }
                    result = matcher.replaceFirst(repeatResult.toString());
                }
                java.util.regex.Matcher matcherPrint = java.util.regex.Pattern.compile(
                    evaluate.replace(ALL_SYMBOLS,
                    "\\s*print\\('([^']*)'\\s*\\+\\s*(\\w+)\\);\\s*")).matcher(result);
                if (matcherPrint.find() && ((Map.Entry) element).getKey().equals(matcherPrint.group(2))) {
                    result = matcherPrint.replaceFirst(matcherPrint.group(1)
                        + ((Map.Entry) element).getValue());
                }
            }
            return result;
        }
    }

    public static class ClassForName {
        public Class<?> call(final String name) throws Exception {
            return Class.forName(name);
        }
    }

    private static Class<?> classForName(final String name) throws Exception {
        return classForName.call(name);
    }

    public static <K, V> Function1<Map<K, V>, V> iteratee(final K key) {
        return new Function1<Map<K, V>, V>() {
            public V apply(Map<K, V> item) {
                return item.get(key);
            }
        };
    }

    public static <T> void each(final Iterable<T> iterable, final Block<? super T> func) {
        for (T element : iterable) {
            func.apply(element);
        }
    }

    public static <T> void forEach(final Iterable<T> iterable, final Block<? super T> func) {
        each(iterable, func);
    }

    public void forEach(final Block<? super T> func) {
        each(iterable, func);
    }

    public static <T, E> List<T> map(final List<E> list, final Function1<? super E, T> func) {
        final List<T> transformed = newArrayListWithExpectedSize(list.size());
        for (E element : list) {
            transformed.add(func.apply(element));
        }
        return transformed;
    }

    public static <T, E> Set<T> map(final Set<E> set, final Function1<? super E, T> func) {
        final Set<T> transformed = newLinkedHashSetWithExpectedSize(set.size());
        for (E element : set) {
            transformed.add(func.apply(element));
        }
        return transformed;
    }

    public static <T, E> List<T> collect(final List<E> list, final Function1<? super E, T> func) {
        return map(list, func);
    }

    public static <T, E> Set<T> collect(final Set<E> set, final Function1<? super E, T> func) {
        return map(set, func);
    }

    public static <T, E> E reduce(final Iterable<T> iterable, final FunctionAccum<E, T> func, final E zeroElem) {
        E accum = zeroElem;
        for (T element : iterable) {
            accum = func.apply(accum, element);
        }
        return accum;
    }

    public static <T, E> E foldl(final Iterable<T> iterable, final FunctionAccum<E, T> func, final E zeroElem) {
        return reduce(iterable, func, zeroElem);
    }

    public static <T, E> E inject(final Iterable<T> iterable, final FunctionAccum<E, T> func, final E zeroElem) {
        return reduce(iterable, func, zeroElem);
    }

    public static <T, E> E reduceRight(final Iterable<T> iterable, final FunctionAccum<E, T> func, final E zeroElem) {
        final List<T> list = newArrayList();
        for (T elem : iterable) {
            list.add(0, elem);
        }
        E accum = zeroElem;
        for (T element : list) {
            accum = func.apply(accum, element);
        }
        return accum;
    }

    public static <T, E> E foldr(final Iterable<T> iterable, final FunctionAccum<E, T> func, final E zeroElem) {
        return reduceRight(iterable, func, zeroElem);
    }

    public static <E> Optional<E> find(final Iterable<E> iterable, final Predicate<E> pred) {
        for (E element : iterable) {
            if (pred.apply(element)) {
                return Optional.of(element);
            }
        }
        return Optional.absent();
    }

    public static <E> Optional<E> detect(final Iterable<E> iterable, final Predicate<E> pred) {
        return find(iterable, pred);
    }

    public static <E> Optional<E> findLast(final Iterable<E> iterable, final Predicate<E> pred) {
        final List<E> list = newArrayList(iterable);
        for (int index = list.size() - 1; index >= 0; index--) {
            if (pred.apply(list.get(index))) {
                return Optional.of(list.get(index));
            }
        }
        return Optional.absent();
    }

    public static <E> List<E> filter(final List<E> list,
                                     final Predicate<E> pred) {
        final List<E> filtered = newArrayList();
        for (E element : list) {
            if (pred.apply(element)) {
                filtered.add(element);
            }
        }
        return filtered;
    }

    public static <E> Set<E> filter(final Set<E> set,
                                    final Predicate<E> pred) {
        final Set<E> filtered = newLinkedHashSet();
        for (E element : set) {
            if (pred.apply(element)) {
                filtered.add(element);
            }
        }
        return filtered;
    }

    public static <E> List<E> select(final List<E> list,
                                     final Predicate<E> pred) {
        return filter(list, pred);
    }

    public static <E> Set<E> select(final Set<E> set,
                                    final Predicate<E> pred) {
        return filter(set, pred);
    }

    public static <E> List<E> reject(final List<E> list, final Predicate<E> pred) {
        return filter(list, new Predicate<E>() {
            @Override
            public Boolean apply(E input) {
                return !pred.apply(input);
            }
        });
    }

    public static <E> Set<E> reject(final Set<E> set, final Predicate<E> pred) {
        return filter(set, new Predicate<E>() {
            @Override
            public Boolean apply(E input) {
                return !pred.apply(input);
            }
        });
    }

    public static <E> boolean every(final Iterable<E> iterable, final Predicate<E> pred) {
        return !find(iterable, new Predicate<E>() {
            @Override
            public Boolean apply(E arg) {
                return !pred.apply(arg);
            }
        }).isPresent();
    }

    public static <E> boolean all(final Iterable<E> iterable, final Predicate<E> pred) {
        return every(iterable, pred);
    }

    public static <E> boolean some(final Iterable<E> iterable, final Predicate<E> pred) {
        return find(iterable, pred).isPresent();
    }

    public static <E> boolean any(final Iterable<E> iterable, final Predicate<E> pred) {
        return some(iterable, pred);
    }

    public static <E> boolean contains(final Iterable<E> iterable, final E elem) {
        return some(iterable, new Predicate<E>() {
            @Override
            public Boolean apply(E e) {
                return elem == null ? e == null : elem.equals(e);
            }
        });
    }

    public static <E> boolean contains(final Iterable<E> iterable, final E elem, final int fromIndex) {
        final List<E> list = newArrayList(iterable);
        return contains(list.subList(fromIndex, list.size()), elem);
    }

    public static <E> boolean include(final Iterable<E> iterable, final E elem) {
        return contains(iterable, elem);
    }

    public static <E> List<E> invoke(final Iterable<E> iterable, final String methodName,
                                  final List<Object> args) {
        final List<E> result = newArrayList();
        final List<Class<?>> argTypes = map(args, new Function1<Object, Class<?>>() {
            public Class<?> apply(Object input) {
                return input.getClass();
            }
        });
        try {
            final Method method = iterable.iterator().next().getClass().getMethod(methodName, argTypes.toArray(
                new Class[argTypes.size()]));
            each(iterable, new Block<E>() {
                public void apply(E arg) {
                    try {
                        result.add((E) method.invoke(arg, args.toArray(new Object[args.size()])));
                    } catch (Exception e) {
                        throw new IllegalArgumentException(e);
                    }
                }
            });
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(e);
        }
        return result;
    }

    public static <E> List<E> invoke(final Iterable<E> iterable, final String methodName) {
        return invoke(iterable, methodName, Collections.emptyList());
    }

    public static <E> List<Object> pluck(final List<E> list, final String propertyName) {
        if (list.isEmpty()) {
            return Collections.emptyList();
        }
        return map(list, new Function1<E, Object>() {
            @Override
            public Object apply(E elem) {
                try {
                    return elem.getClass().getField(propertyName).get(elem);
                } catch (Exception e) {
                    throw new IllegalArgumentException(e);
                }
            }
        });
    }

    public static <E> Set<Object> pluck(final Set<E> set, final String propertyName) {
        if (set.isEmpty()) {
            return Collections.emptySet();
        }
        return map(set, new Function1<E, Object>() {
            @Override
            public Object apply(E elem) {
                try {
                    return elem.getClass().getField(propertyName).get(elem);
                } catch (Exception e) {
                    throw new IllegalArgumentException(e);
                }
            }
        });
    }

    public static <T, E> List<E> where(final List<E> list,
                                    final List<Tuple<String, T>> properties) {
        return filter(list, new WherePredicate<E, T>(properties));

    }

    public static <T, E> Set<E> where(final Set<E> set,
                                   final List<Tuple<String, T>> properties) {
        return filter(set, new WherePredicate<E, T>(properties));
    }

    public static <T, E> Optional<E> findWhere(final Iterable<E> iterable,
                                  final List<Tuple<String, T>> properties) {
        return find(iterable, new Predicate<E>() {
            @Override
            public Boolean apply(final E elem) {
                for (Tuple<String, T> prop : properties) {
                    try {
                        if (!elem.getClass().getField(prop.fst()).get(elem)
                                .equals(prop.snd())) {
                            return false;
                        }
                    } catch (Exception ex) {
                        ex.getMessage();
                    }
                }
                return true;
            }
        });

    }

    public static <E extends Comparable<? super E>> E max(final Collection<E> collection) {
        return Collections.max(collection);
    }

    public static <E, F extends Comparable> E max(final Collection<E> collection, final Function1<E, F> func) {
        return Collections.max(collection, new Comparator<E>() {
            @Override
            public int compare(E o1, E o2) {
                return func.apply(o1).compareTo(func.apply(o2));
            }
        });
    }

    public static <E extends Comparable<? super E>> E min(final Collection<E> collection) {
        return Collections.min(collection);
    }

    public static <E, F extends Comparable> E min(final Collection<E> collection, final Function1<E, F> func) {
        return Collections.min(collection, new Comparator<E>() {
            @Override
            public int compare(E o1, E o2) {
                return func.apply(o1).compareTo(func.apply(o2));
            }
        });
    }

    public static <E> List<E> shuffle(final List<E> list) {
        final List<E> shuffled = newArrayList(list);
        Collections.shuffle(shuffled);
        return shuffled;
    }

    public static <E> E sample(final List<E> list) {
        return list.get(new Random().nextInt(list.size()));
    }

    public static <E> Set<E> sample(final List<E> list, final int howMany) {
        final int size = Math.min(howMany, list.size());
        final Set<E> samples = newLinkedHashSetWithExpectedSize(size);
        while (samples.size() < size) {
            E sample = sample(list);
            samples.add(sample);
        }
        return samples;
    }

    public static <E, T extends Comparable<? super T>> List<E> sortBy(final List<E> list, final Function1<E, T> func) {
        final List<E> sortedList = newArrayList(list);
        Collections.sort(sortedList, new Comparator<E>() {
            @Override
            public int compare(E o1, E o2) {
                return func.apply(o1).compareTo(func.apply(o2));
            }
        });
        return sortedList;
    }

    public static <K, V extends Comparable<? super V>> List<Map<K, V>> sortBy(final List<Map<K, V>> list, final K key) {
        final List<Map<K, V>> sortedList = newArrayList(list);
        Collections.sort(sortedList, new Comparator<Map<K, V>>() {
            @Override
            public int compare(Map<K, V> o1, Map<K, V> o2) {
                return o1.get(key).compareTo(o2.get(key));
            }
        });
        return sortedList;
    }

    public static <K, E> Map<K, List<E>> groupBy(final Iterable<E> iterable, final Function1<E, K> func) {
        final Map<K, List<E>> retVal = newLinkedHashMap();
        for (E e : iterable) {
            final K key = func.apply(e);
            List<E> val;
            if (retVal.containsKey(key)) {
                val = retVal.get(key);
            } else {
                val = newArrayList();
            }
            val.add(e);
            retVal.put(key, val);
        }
        return retVal;
    }

    public static <K, E> Map<K, List<E>> indexBy(final Iterable<E> iterable, final String property) {
        return groupBy(iterable, new Function1<E, K>() {
            @Override
            public K apply(E elem) {
                try {
                    return (K) elem.getClass().getField(property).get(elem);
                } catch (Exception e) {
                    return null;
                }
            }
        });
    }

    public static <K, E> Map<K, Integer> countBy(final Iterable<E> iterable, Function1<E, K> func) {
        final Map<K, Integer> retVal = newLinkedHashMap();
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

    public static <E> E[] toArray(final Iterable<E> iterable) {
        return (E[]) newArrayList(iterable).toArray();
    }

    public static int size(final Iterable<?> iterable) {
        int size;
        final Iterator<?> iterator = iterable.iterator();
        for (size = 0; iterator.hasNext(); size += 1) {
            iterator.next();
        }
        return size;
    }

    public static <E> List<List<E>> partition(final Iterable<E> iterable, final Predicate<E> pred) {
        final List<E> retVal1 = newArrayList();
        final List<E> retVal2 = newArrayList();
        for (final E e : iterable) {
            if (pred.apply(e)) {
                retVal1.add(e);
            } else {
                retVal2.add(e);
            }
        }
        return Arrays.asList(retVal1, retVal2);
    }

    public static <E> List<E>[] partition(final E[] iterable, final Predicate<E> pred) {
        return (List<E>[]) partition(Arrays.asList(iterable), pred).toArray(new ArrayList[2]);
    }

    public static <E> E first(final Iterable<E> iterable) {
        return iterable.iterator().next();
    }

    public static <E> E first(final E[] array) {
        return array[0];
    }

    public static <E> List<E> first(final List<E> list, final int n) {
        return list.subList(0, Math.min(n, list.size()));
    }

    public T first() {
        return iterable.iterator().next();
    }

    public List<T> first(final int n) {
        return ((List) iterable).subList(0, n);
    }

    public static <E> E head(final Iterable<E> iterable) {
        return first(iterable);
    }

    public static <E> E head(final E[] array) {
        return first(array);
    }

    public static <E> List<E> head(final List<E> list, final int n) {
        return first(list, n);
    }

    public T head() {
        return first();
    }

    public List<T> head(final int n) {
        return first(n);
    }

    public static <E> List<E> initial(final List<E> list) {
        return initial(list, 1);
    }

    public static <E> List<E> initial(final List<E> list, final int n) {
        return list.subList(0, Math.max(0, list.size() - n));
    }

    public static <E> E[] initial(final E[] array) {
        return initial(array, 1);
    }

    public static <E> E[] initial(final E[] array, final int n) {
        return Arrays.copyOf(array, array.length - n);
    }

    public List<T> initial() {
        return $.initial((List) iterable, 1);
    }

    public List<T> initial(final int n) {
        return $.initial((List) iterable, n);
    }

    public static <E> E last(final E[] array) {
        return array[array.length - 1];
    }

    public static <E> E last(final List<E> list) {
        return list.get(list.size() - 1);
    }

    public static <E> List<E> last(final List<E> list, final int n) {
        return list.subList(list.size() - n, list.size());
    }

    public T last() {
        return $.last((List<T>) iterable);
    }

    public List<T> last(final int n) {
        return $.last((List) iterable, n);
    }

    public static <E> List<E> rest(final List<E> list) {
        return rest(list, 1);
    }

    public static <E> List<E> rest(final List<E> list, int n) {
        return list.subList(Math.min(n, list.size()), list.size());
    }

    public static <E> E[] rest(final E[] array) {
        return rest(array, 1);
    }

    public static <E> E[] rest(final E[] array, final int n) {
        return (E[]) rest(Arrays.asList(array), n).toArray();
    }

    public List<T> rest() {
        return $.rest((List) iterable);
    }

    public List<T> rest(int n) {
        return $.rest((List) iterable, n);
    }

    public static <E> List<E> tail(final List<E> list) {
        return rest(list);
    }

    public static <E> List<E> tail(final List<E> list, final int n) {
        return rest(list, n);
    }

    public static <E> E[] tail(final E[] array) {
        return rest(array);
    }

    public static <E> E[] tail(final E[] array, final int n) {
        return rest(array, n);
    }

    public List<T> tail() {
        return rest();
    }

    public List<T> tail(final int n) {
        return rest(n);
    }

    public static <E> List<E> drop(final List<E> list) {
        return rest(list);
    }

    public static <E> List<E> drop(final List<E> list, final int n) {
        return rest(list, n);
    }

    public static <E> E[] drop(final E[] array) {
        return rest(array);
    }

    public static <E> E[] drop(final E[] array, final int n) {
        return rest(array, n);
    }

    public static <E> List<E> compact(final List<E> list) {
        return filter(list, new Predicate<E>() {
            @Override
            public Boolean apply(E arg) {
                return !String.valueOf(arg).equals("null") && !String.valueOf(arg).equals("0")
                    && !String.valueOf(arg).equals("false") && !String.valueOf(arg).equals("");
            }
        });
    }

    public static <E> E[] compact(final E[] array) {
        return (E[]) compact(Arrays.asList(array)).toArray();
    }

    public static <E> List<E> compact(final List<E> list, final E falsyValue) {
        return filter(list, new Predicate<E>() {
            @Override
            public Boolean apply(E arg) {
                return !(arg == null ? falsyValue == null : arg.equals(falsyValue));
            }
        });
    }

    public static <E> E[] compact(final E[] array, final E falsyValue) {
        return (E[]) compact(Arrays.asList(array), falsyValue).toArray();
    }

    public List<T> compact() {
        return $.compact((List) iterable);
    }

    public List<T> compact(final T falsyValue) {
        return $.compact((List) iterable, falsyValue);
    }

    public static <E> List<E> flatten(final List<?> list) {
        List<E> flattened = newArrayList();
        flatten(list, flattened, -1);
        return flattened;
    }

    public static <E> List<E> flatten(final List<?> list, final boolean shallow) {
        List<E> flattened = newArrayList();
        flatten(list, flattened, shallow ? 1 : -1);
        return flattened;
    }

    private static <E> void flatten(final List<?> fromTreeList, final List<E> toFlatList, final int shallowLevel) {
        for (Object item : fromTreeList) {
            if (item instanceof List<?> && shallowLevel != 0) {
                flatten((List<?>) item, toFlatList, shallowLevel - 1);
            } else {
                toFlatList.add((E) item);
            }
        }
    }

    public List<T> flatten() {
        return $.flatten((List) iterable);
    }

    public List<T> flatten(final boolean shallow) {
        return $.flatten((List) iterable, shallow);
    }

    public static <E> List<E> without(final List<E> list, E ... values) {
        final List<E> valuesList = Arrays.asList(values);
        return filter(list, new Predicate<E>() {
            @Override
            public Boolean apply(E elem) {
                return !contains(valuesList, elem);
            }
        });
    }

    public static <E> List<E> without(final List<E> list, final E value) {
        return without(list, (E[]) Arrays.asList(value).toArray());
    }

    public static <E> E[] without(final E[] array, final E ... values) {
        return (E[]) without(Arrays.asList(array), values).toArray();
    }

    public static <E> E[] without(final E[] array, final E value) {
        return without(array, (E[]) Arrays.asList(value).toArray());
    }

    public static <E> List<E> uniq(final List<E> list) {
        return newArrayList(newHashSet(list));
    }

    public static <E> E[] uniq(final E[] array) {
        return (E[]) uniq(Arrays.asList(array)).toArray();
    }

    public static <K, E> Collection<E> uniq(final Iterable<E> iterable, final Function1<E, K> func) {
        final Map<K, E> retVal = newLinkedHashMap();
        for (final E e : iterable) {
            final K key = func.apply(e);
            retVal.put(key, e);
        }
        return retVal.values();
    }

    public static <K, E> E[] uniq(final E[] array, final Function1<E, K> func) {
        return (E[]) uniq(Arrays.asList(array), func).toArray();
    }

    public static <E> E[] union(final E[] array1, final E[] array2) {
        return (E[]) union(Arrays.asList(array1), Arrays.asList(array2)).toArray();
    }

    public static <E> List<E> intersection(final List<E> list1, final List<E> list2) {
        return filter(list1, new Predicate<E>() {
            @Override
            public Boolean apply(E elem) {
                return contains(list2, elem);
            }
        });
    }

    public static <E> List<E> intersection(final List<E> ... lists) {
        final Stack<List<E>> stack = new Stack<List<E>>();
        stack.push(lists[0]);
        for (int index = 1; index < lists.length; index += 1) {
          stack.push(intersection(stack.peek(), lists[index]));
        }
        return stack.peek();
    }

    public static <E> E[] intersection(final E[] array1, final E[] array2) {
        return (E[]) intersection(Arrays.asList(array1), Arrays.asList(array2)).toArray();
    }

    public static <E> List<E> difference(final List<E> list1, final List<E> list2) {
        return filter(list1, new Predicate<E>() {
            @Override
            public Boolean apply(E elem) {
                return !contains(list2, elem);
            }
        });
    }

    public static <E> E[] difference(final E[] array1, final E[] array2) {
        return (E[]) difference(Arrays.asList(array1), Arrays.asList(array2)).toArray();
    }

    public static <T> List<List<T>> zip(final List<T> ... lists) {
        final List<List<T>> zipped = newArrayList();
        each(Arrays.asList(lists), new Block<List<T>>() {
            @Override
            public void apply(final List<T> list) {
                $.each(list, new Block<T>() {
                    private int index;

                    @Override
                    public void apply(T elem) {
                        final List<T> nTuple = index >= zipped.size() ? $.<T>newArrayList() : zipped.get(index);
                        if (index >= zipped.size()) {
                            zipped.add(nTuple);
                        }
                        index += 1;
                        nTuple.add(elem);
                    }
                });
            }
        });
        return zipped;
    }

    public static <T> List<List<T>> unzip(final List<T> ... lists) {
        final List<List<T>> unzipped = newArrayList();
        for (int index = 0; index < lists[0].size(); index += 1) {
            final List<T> nTuple = newArrayList();
            for (int index2 = 0; index2 < lists.length; index2 += 1) {
                nTuple.add(lists[index2].get(index));
            }
            unzipped.add(nTuple);
        }
        return unzipped;
    }

    public static <K, V> List<Tuple<K, V>> object(final List<K> keys, final List<V> values) {
        return map(keys, new Function1<K, Tuple<K, V>>() {
            private int index;

            @Override
            public Tuple<K, V> apply(K key) {
                return Tuple.create(key, values.get(index++));
            }
        });
    }

    public static <E> int findIndex(final List<E> list, final Predicate<E> pred) {
        for (int index = 0; index < list.size(); index++) {
            if (pred.apply(list.get(index))) {
                return index;
            }
        }
        return -1;
    }

    public static <E> int findIndex(final E[] array, final Predicate<E> pred) {
        return findIndex(Arrays.asList(array), pred);
    }

    public static <E> int findLastIndex(final List<E> list, final Predicate<E> pred) {
        for (int index = list.size() - 1; index >= 0; index--) {
            if (pred.apply(list.get(index))) {
                return index;
            }
        }
        return -1;
    }

    public static <E> int findLastIndex(final E[] array, final Predicate<E> pred) {
        return findLastIndex(Arrays.asList(array), pred);
    }

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

    public static <E extends Comparable<E>> int sortedIndex(final E[] array, final E value) {
        return sortedIndex(Arrays.asList(array), value);
    }

    public static <E extends Comparable<E>> int sortedIndex(final List<E> list, final E value,
        final String propertyName) {
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

    public static <E extends Comparable<E>> int sortedIndex(final E[] array, final E value,
        final String propertyName) {
        return sortedIndex(Arrays.asList(array), value, propertyName);
    }

    public static <E> int indexOf(final List<E> list, final E value) {
        return list.indexOf(value);
    }

    public static <E> int indexOf(final E[] array, final E value) {
        return indexOf(Arrays.asList(array), value);
    }

    public static <E> int lastIndexOf(final List<E> list, final E value) {
        return list.lastIndexOf(value);
    }

    public static <E> int lastIndexOf(final E[] array, final E value) {
        return lastIndexOf(Arrays.asList(array), value);
    }

    public static int[] range(int stop) {
        return range(0, stop, 1);
    }

    public static int[] range(int start, int stop) {
        return range(start, stop, 1);
    }

    public static int[] range(int start, int stop, int step) {
        int[] array = new int[Math.abs(stop - start) / Math.abs(step)];
        int index2 = 0;
        if (start < stop) {
            for (int index = start; index < stop; index += step, index2 += 1) {
                array[index2] = index;
            }
        } else {
            for (int index = start; index > stop; index += step, index2 += 1) {
                array[index2] = index;
            }
        }
        return array;
    }

    public static <T, F> Function1<T, F> bind(final Function1<T, F> function) {
        return new Function1<T, F>() {
            @Override
            public F apply(T arg) {
                return function.apply(arg);
            }
        };
    }

    public static <T> void delay(final Function<T> function, final int delayMilliseconds) {
        final java.util.concurrent.ScheduledExecutorService scheduler =
            java.util.concurrent.Executors.newSingleThreadScheduledExecutor();
        scheduler.schedule(
            new Runnable() {
                public void run() {
                    function.apply();
                }
            }, delayMilliseconds, java.util.concurrent.TimeUnit.MILLISECONDS);
        scheduler.shutdown();
    }

    public static <T> void defer(final Function<T> function) {
        final java.util.concurrent.ScheduledExecutorService scheduler =
            java.util.concurrent.Executors.newSingleThreadScheduledExecutor();
        scheduler.schedule(
            new Runnable() {
                public void run() {
                    function.apply();
                }
            }, 0, java.util.concurrent.TimeUnit.MILLISECONDS);
        scheduler.shutdown();
    }

    public static <T> Function<T> debounce(final Function<T> function, final int delayMilliseconds) {
        return new Function<T>() {
            @Override
            public T apply() {
                delay(function, delayMilliseconds);
                return null;
            }
        };
    }

    public static <T> Function1<Void, T> wrap(final Function1<T, T> function,
        final Function1<Function1<T, T>, T> wrapper) {
        return new Function1<Void, T>() {
            public T apply(final Void arg) {
                return wrapper.apply(function);
            }
        };
    }

    public static <E> Predicate<E> negate(final Predicate<E> pred) {
        return new Predicate<E>() {
            public Boolean apply(final E item) {
                return !pred.apply(item);
            }
        };
    }

    public static <T> Function1<T, T> compose(final Function1<T, T> ... func) {
        return new Function1<T, T>() {
            public T apply(final T arg) {
                T result = arg;
                for (int index = func.length - 1; index >= 0; index -= 1) {
                    result = func[index].apply(result);
                }
                return result;
            }
        };
    }

    public static <E> Function<E> after(final int count, final Function<E> function) {
        class AfterFunction implements Function {
            private final int count;
            private int index;
            public AfterFunction(final int count) {
                this.count = count;
            }
            public E apply() {
                if (++index >= count) {
                    return function.apply();
                } else {
                    return null;
                }
            }
        }
        return new AfterFunction(count);
    }

    public static <E> Function<E> before(final int count, final Function<E> function) {
        class BeforeFunction implements Function {
            private final int count;
            private int index;
            public BeforeFunction(final int count) {
                this.count = count;
            }
            public E apply() {
                if (++index <= count) {
                    return function.apply();
                } else {
                    return null;
                }
            }
        }
        return new BeforeFunction(count);
    }

    public static <T> Function<T> once(final Function<T> function) {
        return new Function<T>() {
            private volatile boolean executed;
            @Override
            public T apply() {
                if (!executed) {
                    executed = true;
                    delay(function, 0);
                }
                return null;
            }
        };
    }

    public static <K, V> Set<K> keys(final Map<K, V> object) {
        return object.keySet();
    }

    public static <K, V> List<V> values(final Map<K, V> object) {
        final List<V> result = newArrayList();
        for (final Map.Entry<K, V> entry : object.entrySet()) {
            result.add(entry.getValue());
        }
        return result;
    }

    public static <K, V> List<Tuple<K, V>> mapObject(final Map<K, V> object, final Function1<? super V, V> func) {
        return map(newArrayList(object.keySet()), new Function1<K, Tuple<K, V>>() {
            @Override
            public Tuple<K, V> apply(K key) {
                return Tuple.create(key, func.apply(object.get(key)));
            }
        });
    }

    public static <K, V> List<Tuple<K, V>> pairs(final Map<K, V> object) {
        return map(newArrayList(object.keySet()), new Function1<K, Tuple<K, V>>() {
            @Override
            public Tuple<K, V> apply(K key) {
                return Tuple.create(key, object.get(key));
            }
        });
    }

    public static <K, V> List<Tuple<V, K>> invert(final Map<K, V> object) {
        return map(newArrayList(object.keySet()), new Function1<K, Tuple<V, K>>() {
            @Override
            public Tuple<V, K> apply(K key) {
                return Tuple.create(object.get(key), key);
            }
        });
    }

    public static List<String> functions(final Object object) {
        final List<String> result = newArrayList();
        for (final Method method : object.getClass().getDeclaredMethods()) {
            if (!method.getName().contains("$")) {
                result.add(method.getName());
            }
        }
        return sort(uniq(result));
    }

    public static List<String> methods(final Object object) {
        return functions(object);
    }

    public static <E> E findKey(final List<E> list, final Predicate<E> pred) {
        for (int index = 0; index < list.size(); index++) {
            if (pred.apply(list.get(index))) {
                return list.get(index);
            }
        }
        return null;
    }

    public static <E> E findKey(final E[] array, final Predicate<E> pred) {
        return findKey(Arrays.asList(array), pred);
    }

    public static <E> E findLastKey(final List<E> list, final Predicate<E> pred) {
        for (int index = list.size() - 1; index >= 0; index--) {
            if (pred.apply(list.get(index))) {
                return list.get(index);
            }
        }
        return null;
    }

    public static <E> E findLastKey(final E[] array, final Predicate<E> pred) {
        return findLastKey(Arrays.asList(array), pred);
    }

    public static <K, V> List<Tuple<K, V>> pick(final Map<K, V> object, final V ... keys) {
        return without(map(newArrayList(object.keySet()), new Function1<K, Tuple<K, V>>() {
            @Override
            public Tuple<K, V> apply(K key) {
                if (Arrays.asList(keys).contains(key)) {
                    return Tuple.create(key, object.get(key));
                } else {
                    return null;
                }
            }
        }), (Tuple<K, V>) null);
    }

    public static <K, V> List<Tuple<K, V>> pick(final Map<K, V> object, final Predicate<V> pred) {
        return without(map(newArrayList(object.keySet()), new Function1<K, Tuple<K, V>>() {
            @Override
            public Tuple<K, V> apply(K key) {
                if (pred.apply(object.get(key))) {
                    return Tuple.create(key, object.get(key));
                } else {
                    return null;
                }
            }
        }), (Tuple<K, V>) null);
    }

    public static <K, V> List<Tuple<K, V>> omit(final Map<K, V> object, final V ... keys) {
        return without(map(newArrayList(object.keySet()), new Function1<K, Tuple<K, V>>() {
            @Override
            public Tuple<K, V> apply(K key) {
                if (Arrays.asList(keys).contains(key)) {
                    return null;
                } else {
                    return Tuple.create(key, object.get(key));
                }
            }
        }), (Tuple<K, V>) null);
    }

    public static <K, V> List<Tuple<K, V>> omit(final Map<K, V> object, final Predicate<V> pred) {
        return without(map(newArrayList(object.keySet()), new Function1<K, Tuple<K, V>>() {
            @Override
            public Tuple<K, V> apply(K key) {
                if (pred.apply(object.get(key))) {
                    return null;
                } else {
                    return Tuple.create(key, object.get(key));
                }
            }
        }), (Tuple<K, V>) null);
    }

    public static <K, V> Map<K, V> defaults(final Map<K, V> object, final Map<K, V> defaults) {
        final Map<K, V> result = newLinkedHashMap();
        for (final Map.Entry<K, V> entry : defaults.entrySet()) {
            result.put(entry.getKey(), entry.getValue());
        }
        for (final Map.Entry<K, V> entry : object.entrySet()) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    public static Object clone(final Object obj) {
        try {
            if (obj instanceof Cloneable) {
                for (final Method method : obj.getClass().getMethods()) {
                    if (method.getName().equals("clone") && method.getParameterTypes().length == 0) {
                        return method.invoke(obj);
                    }
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
        throw new IllegalArgumentException("Cannot clone object");
    }

    public static <E> E[] clone(final E[] iterable) {
        return Arrays.copyOf(iterable, iterable.length);
    }

    public static <T> void tap(final Iterable<T> iterable, final Block<? super T> func) {
        for (T element : iterable) {
            func.apply(element);
        }
    }

    public static <K, V> boolean isMatch(final Map<K, V> object, final Map<K, V> properties) {
        for (final K key : keys(properties)) {
            if (!object.containsKey(key) || !object.get(key).equals(properties.get(key))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isEqual(final Object object, final Object other) {
        return object == null ? other == null : object.equals(other);
    }

    public static <K, V> boolean isEmpty(final Map<K, V> object) {
        return object == null || object.isEmpty();
    }

    public static <T> boolean isEmpty(final Iterable<T> iterable) {
        return iterable == null || size(iterable) == 0;
    }

    public static boolean isArray(final Object object) {
        return object != null && object.getClass().isArray();
    }

    public static boolean isObject(final Object object) {
        return object instanceof Map;
    }

    public static boolean isFunction(final Object object) {
        return object instanceof Function1;
    }

    public static boolean isString(final Object object) {
        return object instanceof String;
    }

    public static boolean isNumber(final Object object) {
        return object instanceof Number;
    }

    public static boolean isDate(final Object object) {
        return object instanceof Date;
    }

    public static boolean isRegExp(final Object object) {
        return object instanceof java.util.regex.Pattern;
    }

    public static boolean isError(final Object object) {
        return object instanceof Throwable;
    }

    public static boolean isBoolean(final Object object) {
        return object instanceof Boolean;
    }

    public static boolean isNull(final Object object) {
        return object == null;
    }

    public static <K, V> boolean has(final Map<K, V> object, final K key) {
        return object.containsKey(key);
    }

    public static <E> E identity(final E value) {
        return value;
    }

    public static <E> Function<E> constant(final E value) {
        return new Function<E>() {
            public E apply() {
                return value;
            }
        };
    }

    public static <K, V> Function1<Map<K, V>, V> property(final K key) {
        return new Function1<Map<K, V>, V>() {
            public V apply(final Map<K, V> object) {
                return object.get(key);
            }
        };
    }

    public static <K, V> Function1<K, V> propertyOf(final Map<K, V> object) {
        return new Function1<K, V>() {
            public V apply(final K key) {
                return object.get(key);
            }
        };
    }

    public static <K, V> Predicate<Map<K, V>> matcher(final Map<K, V> object) {
        return new Predicate<Map<K, V>>() {
            public Boolean apply(final Map<K, V> item) {
                for (final K key : keys(object)) {
                    if (!item.containsKey(key) || !item.get(key).equals(object.get(key))) {
                        return false;
                    }
                }
                return true;
            }
        };
    }

    public static <E> void times(final int count, final Function<E> function) {
        for (int index = 0; index < count; index += 1) {
            function.apply();
        }
    }

    public static int random(final int from, final int to) {
        return new Random().nextInt(to - from) + from;
    }

    public static long now() {
        return new Date().getTime();
    }

    public static String escape(String value) {
        return value.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;")
            .replaceAll("\"", "&quot;");
    }

    public static String unescape(String value) {
        return value.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&quot;", "\"")
            .replaceAll("&amp;", "&");
    }

    public static <E> Object result(final Iterable<E> iterable, final Predicate<E> pred) {
        for (E element : iterable) {
            if (pred.apply(element)) {
                if (element instanceof Map.Entry) {
                    if (((Map.Entry) element).getValue() instanceof Function) {
                        return ((Function) ((Map.Entry) element).getValue()).apply();
                    }
                    return ((Map.Entry) element).getValue();
                }
                return element;
            }
        }
        return null;
    }

    public static String uniqueId(final String prefix) {
        return prefix + UNIQUE_ID.incrementAndGet();
    }

    public static <E> Template<Set<E>> template(final String template) {
        return new TemplateImpl<E>(template);
    }

    public static <T> Chain chain(final List<T> list) {
        return new $.Chain<T>(list);
    }

    public static <T> Chain chain(final Set<T> set) {
        return new $.Chain<T>(newArrayList(set));
    }

    public static <T> Chain chain(final T[] array) {
        return new $.Chain<T>(Arrays.asList(array));
    }

    public static class Chain<T> {
        private final T item;
        private final List<T> list;
        public Chain(final T item) {
            if (item instanceof List) {
                this.item = null;
                this.list = (List<T>) item;
            } else {
                this.item = item;
                this.list = null;
            }
        }
        public Chain(final List<T> list) {
            this.item = null;
            this.list = list;
        }

        public Chain<T> first() {
            return new Chain<T>($.first(list));
        }

        public Chain<T> first(int n) {
            return new Chain<T>($.first(list, n));
        }

        public Chain<T> initial() {
            return new Chain<T>($.initial(list));
        }

        public Chain<T> initial(int n) {
            return new Chain<T>($.initial(list, n));
        }

        public Chain<T> last() {
            return new Chain<T>($.last(list));
        }

        public Chain<T> last(int n) {
            return new Chain<T>($.last(list, n));
        }

        public Chain<T> rest() {
            return new Chain<T>($.rest(list));
        }

        public Chain<T> rest(int n) {
            return new Chain<T>($.rest(list, n));
        }

        public Chain<T> flatten() {
            final List<T> flattened = newArrayList();
            flatten(list, flattened);
            return new Chain<T>(flattened);
        }

        private void flatten(final List<?> fromTreeList, final List<T> toFlatList) {
            for (final Object localItem : fromTreeList) {
                if (localItem instanceof List<?>) {
                    flatten((List<?>) localItem, toFlatList);
                } else {
                    toFlatList.add((T) localItem);
                }
            }
        }

        public <F> Chain<F> map(final Function1<? super T, F> func) {
            return new Chain<F>($.map(list, func));
        }

        public Chain<T> filter(final Predicate<T> pred) {
            return new Chain<T>($.filter(list, pred));
        }

        public Chain<T> reject(final Predicate<T> pred) {
            return new Chain<T>($.reject(list, pred));
        }

        public <F> Chain<F> reduce(final FunctionAccum<F, T> func, final F zeroElem) {
            F accum = zeroElem;
            for (T element : list) {
                accum = func.apply(accum, element);
            }
            return new Chain<F>(accum);
        }

        public <F> Chain<F> reduceRight(final FunctionAccum<F, T> func, final F zeroElem) {
            final List<T> localList = newArrayList();
            for (T elem : list) {
                localList.add(0, elem);
            }
            F accum = zeroElem;
            for (T element : localList) {
                accum = func.apply(accum, element);
            }
            return new Chain<F>(accum);
        }

        public Chain<Optional<T>> find(final Predicate<T> pred) {
            for (final T element : list) {
                if (pred.apply(element)) {
                    return new Chain<Optional<T>>(Optional.of(element));
                }
            }
            return new Chain<Optional<T>>(Optional.<T>absent());
        }

        public Chain<Optional<T>> findLast(final Predicate<T> pred) {
            for (int index = list.size() - 1; index >= 0; index--) {
                if (pred.apply(list.get(index))) {
                    return new Chain<Optional<T>>(Optional.of(list.get(index)));
                }
            }
            return new Chain<Optional<T>>(Optional.<T>absent());
        }

        public Chain<Comparable> max() {
            return new Chain<Comparable>($.max((Collection) list));
        }

        public <F extends Comparable<? super F>> Chain<T> max(final Function1<T, F> func) {
            return new Chain<T>($.max(list, func));
        }

        public Chain<Comparable> min() {
            return new Chain<Comparable>($.min((Collection) list));
        }

        public <F extends Comparable<? super F>> Chain<T> min(final Function1<T, F> func) {
            return new Chain<T>($.min(list, func));
        }

        public Chain<T> sort() {
            return new Chain<T>($.sort((List<Comparable>) list));
        }

        public <F extends Comparable<? super F>> Chain<T> sortBy(final Function1<T, F> func) {
            return new Chain<T>($.sortBy(list, func));
        }

        public <K, V extends Comparable<? super V>> Chain<Map<K, V>> sortBy(final K key) {
            return new Chain<Map<K, V>>($.sortBy((List<Map<K, V>>) list, key));
        }

        public Chain<T> shuffle() {
            return new Chain<T>($.shuffle(list));
        }

        public Chain<T> sample() {
            return new Chain<T>($.sample(list));
        }

        public Chain<T> sample(final int howMany) {
            return new Chain<T>($.newArrayList($.sample(list, howMany)));
        }

        public Chain<T> tap(final Block<T> func) {
            $.tap(list, func);
            return new Chain<T>(list);
        }

        public Chain<Boolean> contains(final T elem) {
            return new Chain<Boolean>($.contains(list, elem));
        }

        public <T> Chain<T> uniq() {
            return new Chain<T>((List<T>) $.uniq(list));
        }

        public <F> Chain<F> uniq(final Function1<T, F> func) {
            return new Chain<F>($.newArrayList((Iterable<F>) $.uniq(list, func)));
        }

        public <T> Chain<T> concat(final List<T> second) {
            return new Chain<T>((List<T>) Arrays.asList($.concat(list.toArray(), second.toArray())));
        }

        public <T> Chain<T> slice(final int start) {
            return new Chain<T>((List<T>) $.slice(list, start));
        }

        public <T> Chain<T> slice(final int start, final int end) {
            return new Chain<T>((List<T>) $.slice(list, start, end));
        }

        public <T> Chain<T> reverse() {
            return new Chain<T>((List<T>) $.reverse(list));
        }

        public Chain<String> join(final String separator) {
            return new Chain<String>($.join(list, separator));
        }

        public <T> Chain<T> skip(final int numberToSkip) {
            return new Chain<T>((List<T>) list.subList(numberToSkip, list.size()));
        }

        public <T> Chain<T> limit(final int size) {
            return new Chain<T>((List<T>) list.subList(0, size));
        }

        public T item() {
            return item;
        }

        public List<T> value() {
            return list;
        }

        public String toString() {
            return String.valueOf(list);
        }
    }

    public static void mixin(final String funcName, final Function1<String, String> func) {
        FUNCTIONS.put(funcName, func);
    }

    public Optional<String> call(final String funcName) {
        if (string.isPresent() && FUNCTIONS.containsKey(funcName)) {
            return Optional.of(FUNCTIONS.get(funcName).apply(string.get()));
        }
        return Optional.absent();
    }

    public static <E> List<E> union(final List<E> ... lists) {
        final Set<E> union = newLinkedHashSet();
        for (List<E> list : lists) {
            union.addAll(list);
        }
        return newArrayList(union);
    }

    public static <T extends Comparable<T>> List<T> sort(final Iterable<T> iterable) {
        final List<T> localList = newArrayList(iterable);
        Collections.<T>sort(localList);
        return localList;
    }

    public static <T extends Comparable<T>> T[] sort(final T[] array) {
        final T[] localArray = array.clone();
        Arrays.<T>sort(localArray);
        return localArray;
    }

    public List<T> sort() {
        return sort((Iterable<Comparable>) iterable);
    }

    public static <T> String join(final Iterable<T> iterable, final String separator) {
        final StringBuilder sb = new StringBuilder();
        each(iterable, new Block<T>() {
            public void apply(T item) {
                if (!sb.toString().isEmpty()) {
                    sb.append(separator);
                }
                sb.append(item.toString());
            }
        });
        return sb.toString();
    }

    public static <T> String join(final T[] array, final String separator) {
        return join(Arrays.asList(array), separator);
    }

    public String join(final String separator) {
        return join(iterable, separator);
    }

    public static <T> T[] concat(final T[] first, final T[] ... other) {
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

    public static <T> List<T> concat(final Iterable<T> first, final Iterable<T> ... other) {
        int length = 0;
        for (Iterable<T> otherItem : other) {
            length += size(otherItem);
        }
        final T[] result = (T[]) Arrays.copyOf(toArray(first), size(first) + length);
        int index = 0;
        for (Iterable<T> otherItem : other) {
            System.arraycopy(toArray(otherItem), 0, result, size(first) + index, size(otherItem));
            index += size(otherItem);
        }
        return (List<T>) Arrays.asList(result);
    }


    public List<T> concatWith(final Iterable<T> ... other) {
        return concat(iterable, other);
    }

    public static <T> List<T> slice(final Iterable<T> iterable, final int start) {
        final List<T> result;
        if (start >= 0) {
            result = newArrayList(iterable).subList(start, size(iterable));
        } else {
            result = newArrayList(iterable).subList(size(iterable) + start, size(iterable));
        }
        return result;
    }

    public static <T> T[] slice(final T[] array, final int start) {
        return (T[]) slice(Arrays.asList(array), start).toArray();
    }

    public List<T> slice(final int start) {
        return slice(iterable, start);
    }

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
                result = newArrayList(iterable).subList(size(iterable) + start, size(iterable) + end);
            }
        }
        return result;
    }

    public static <T> T[] slice(final T[] array, final int start, final int end) {
        return (T[]) slice(Arrays.asList(array), start, end).toArray();
    }

    public List<T> slice(final int start, final int end) {
        return slice(iterable, start, end);
    }

    public static <T> List<T> reverse(final Iterable<T> iterable) {
        final List<T> result = (List<T>) clone(newArrayList(iterable));
        Collections.reverse(result);
        return result;
    }

    public static <T> T[] reverse(final T[] array) {
        return (T[]) reverse(Arrays.asList(array)).toArray();
    }

    public List<T> reverse() {
        return reverse(iterable);
    }

    public Iterable<T> getIterable() {
        return iterable;
    }

    public Optional<String> getString() {
        return string;
    }

    protected static <T> List<T> newArrayList() {
        try {
            final Class<?> listsClass = classForName("com.google.common.collect.Lists");
            return (List<T>) listsClass.getDeclaredMethod("newArrayList").invoke(null);
        } catch (Exception e) {
            return new ArrayList<T>();
        }
    }

    protected static <T> List<T> newArrayList(Iterable<T> iterable) {
        try {
            final Class<?> listsClass = classForName("com.google.common.collect.Lists");
            return (List<T>) listsClass.getDeclaredMethod("newArrayList", Iterable.class).invoke(null, iterable);
        } catch (Exception e) {
            final List<T> result;
            if (iterable instanceof Collection) {
                result = new ArrayList<T>((Collection) iterable);
            } else {
                result = new ArrayList<T>();
                for (final T item : iterable) {
                    result.add(item);
                }
            }
            return result;
        }
    }

    protected static <T> List<T> newArrayListWithExpectedSize(int size) {
        try {
            final Class<?> listsClass = classForName("com.google.common.collect.Lists");
            return (List<T>) listsClass.getDeclaredMethod("newArrayListWithExpectedSize", Integer.TYPE)
                .invoke(null, size);
        } catch (Exception e) {
            return new ArrayList<T>(size);
        }
    }

    protected static <T> Set<T> newLinkedHashSet() {
        try {
            final Class<?> setsClass = classForName("com.google.common.collect.Sets");
            return (Set<T>) setsClass.getDeclaredMethod("newLinkedHashSet").invoke(null);
        } catch (Exception e) {
            return new LinkedHashSet<T>();
        }
    }

    protected static <T> Set<T> newHashSet(Iterable<T> iterable) {
        try {
            final Class<?> setsClass = classForName("com.google.common.collect.Sets");
            return (Set<T>) setsClass.getDeclaredMethod("newHashSet", Iterable.class).invoke(null, iterable);
        } catch (Exception e) {
            final Set<T> result = new HashSet<T>();
            for (final T item : iterable) {
                result.add(item);
            }
            return result;
        }
    }

    protected static <T> Set<T> newLinkedHashSetWithExpectedSize(int size) {
        try {
            final Class<?> setsClass = classForName("com.google.common.collect.Sets");
            return (Set<T>) setsClass.getDeclaredMethod("newLinkedHashSetWithExpectedSize", Integer.TYPE)
                .invoke(null, size);
        } catch (Exception e) {
            return new LinkedHashSet<T>(size);
        }
    }

    protected static <K, E> Map<K, E> newLinkedHashMap() {
        try {
            final Class<?> mapsClass = classForName("com.google.common.collect.Maps");
            return (Map<K, E>) mapsClass.getDeclaredMethod("newLinkedHashMap").invoke(null);
        } catch (Exception e) {
            return new LinkedHashMap<K, E>();
        }
    }

    public static void main(String[] args) {
        final String message = "Underscore-java is a java port of Underscore.js.\n\n"
            + "In addition to porting Underscore's functionality, Underscore-java includes matching unit tests.\n\n"
            + "For docs, license, tests, and downloads, see: http://javadev.github.io/underscore-java";
        System.out.println(message);
    }
}
