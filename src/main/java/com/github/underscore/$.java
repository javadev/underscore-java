/*
 * The MIT License (MIT)
 *
 * Copyright 2016 Valentyn Kolesnikov
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
                    if (!elem.getClass().getField(prop.fst()).get(elem).equals(prop.snd())) {
                        return false;
                    }
                } catch (Exception ex) {
                    try {
                        if (!elem.getClass().getMethod(prop.fst()).invoke(elem).equals(prop.snd())) {
                            return false;
                        }
                    } catch (Exception e) {
                    }
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
        @SuppressWarnings("unchecked")
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
                    + evaluate.replace(ALL_SYMBOLS, "\\s*\\}\\);\\s*"))
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

    public void each(final Block<? super T> func) {
        each(iterable, func);
    }

    public static <T> void eachRight(final Iterable<T> iterable, final Block<? super T> func) {
        each(reverse(iterable), func);
    }

    public void eachRight(final Block<? super T> func) {
        eachRight(iterable, func);
    }

    public static <T> void forEach(final Iterable<T> iterable, final Block<? super T> func) {
        each(iterable, func);
    }

    public void forEach(final Block<? super T> func) {
        each(iterable, func);
    }

    public static <T> void forEachRight(final Iterable<T> iterable, final Block<? super T> func) {
        eachRight(iterable, func);
    }

    public void forEachRight(final Block<? super T> func) {
        eachRight(iterable, func);
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
        return reduce(reverse(iterable), func, zeroElem);
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
        return find(reverse(iterable), pred);
    }

    public static <E> List<E> filter(final List<E> list, final Predicate<E> pred) {
        final List<E> filtered = newArrayList();
        for (E element : list) {
            if (pred.apply(element)) {
                filtered.add(element);
            }
        }
        return filtered;
    }

    public static <E> Set<E> filter(final Set<E> set, final Predicate<E> pred) {
        final Set<E> filtered = newLinkedHashSet();
        for (E element : set) {
            if (pred.apply(element)) {
                filtered.add(element);
            }
        }
        return filtered;
    }

    public static <E> List<E> select(final List<E> list, final Predicate<E> pred) {
        return filter(list, pred);
    }

    public static <E> Set<E> select(final Set<E> set, final Predicate<E> pred) {
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

    public boolean every(final Predicate<T> pred) {
        return every(iterable, pred);
    }

    public static <E> boolean all(final Iterable<E> iterable, final Predicate<E> pred) {
        return every(iterable, pred);
    }

    public boolean all(final Predicate<T> pred) {
        return every(iterable, pred);
    }

    public static <E> boolean some(final Iterable<E> iterable, final Predicate<E> pred) {
        return find(iterable, pred).isPresent();
    }

    public boolean some(final Predicate<T> pred) {
        return some(iterable, pred);
    }

    public static <E> boolean any(final Iterable<E> iterable, final Predicate<E> pred) {
        return some(iterable, pred);
    }

    public boolean any(final Predicate<T> pred) {
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

    public boolean contains(final T elem) {
        return contains(iterable, elem);
    }

    public static <E> boolean contains(final Iterable<E> iterable, final E elem, final int fromIndex) {
        final List<E> list = newArrayList(iterable);
        return contains(list.subList(fromIndex, list.size()), elem);
    }

    public static <E> boolean include(final Iterable<E> iterable, final E elem) {
        return contains(iterable, elem);
    }

    @SuppressWarnings("unchecked")
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

    public List<T> invoke(final String methodName, final List<Object> args) {
        return invoke(iterable, methodName, args);
    }

    public static <E> List<E> invoke(final Iterable<E> iterable, final String methodName) {
        return invoke(iterable, methodName, Collections.emptyList());
    }

    public List<T> invoke(final String methodName) {
        return invoke(iterable, methodName);
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
                    try {
                        return elem.getClass().getMethod(propertyName).invoke(elem);
                    } catch (Exception ex) {
                        throw new IllegalArgumentException(ex);
                    }
                }
            }
        });
    }

    public List<Object> pluck(final String propertyName) {
        return pluck(newArrayList(iterable), propertyName);
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
                    try {
                        return elem.getClass().getMethod(propertyName).invoke(elem);
                    } catch (Exception ex) {
                        throw new IllegalArgumentException(ex);
                    }
                }
            }
        });
    }

    public static <T, E> List<E> where(final List<E> list,
                                    final List<Tuple<String, T>> properties) {
        return filter(list, new WherePredicate<E, T>(properties));

    }

    public <E> List<T> where(final List<Tuple<String, E>> properties) {
        return where(newArrayList(iterable), properties);
    }

    public static <T, E> Set<E> where(final Set<E> set,
                                   final List<Tuple<String, T>> properties) {
        return filter(set, new WherePredicate<E, T>(properties));
    }

    public static <T, E> Optional<E> findWhere(final Iterable<E> iterable,
                                  final List<Tuple<String, T>> properties) {
        return find(iterable, new WherePredicate<E, T>(properties));
    }

    public <E> Optional<T> findWhere(final List<Tuple<String, E>> properties) {
        return findWhere(iterable, properties);
    }

    public static <E extends Comparable<? super E>> E max(final Collection<E> collection) {
        return Collections.max(collection);
    }

    @SuppressWarnings("unchecked")
    public T max() {
        return (T) max((Collection) iterable);
    }

    @SuppressWarnings("unchecked")
    public static <E, F extends Comparable> E max(final Collection<E> collection, final Function1<E, F> func) {
        return Collections.max(collection, new Comparator<E>() {
            @Override
            public int compare(E o1, E o2) {
                return func.apply(o1).compareTo(func.apply(o2));
            }
        });
    }

    @SuppressWarnings("unchecked")
    public <F extends Comparable<? super F>> T max(final Function1<T, F> func) {
        return (T) max((Collection) iterable, func);
    }

    public static <E extends Comparable<? super E>> E min(final Collection<E> collection) {
        return Collections.min(collection);
    }

    @SuppressWarnings("unchecked")
    public T min() {
        return (T) min((Collection) iterable);
    }

    @SuppressWarnings("unchecked")
    public static <E, F extends Comparable> E min(final Collection<E> collection, final Function1<E, F> func) {
        return Collections.min(collection, new Comparator<E>() {
            @Override
            public int compare(E o1, E o2) {
                return func.apply(o1).compareTo(func.apply(o2));
            }
        });
    }

    @SuppressWarnings("unchecked")
    public <F extends Comparable<? super F>> T min(final Function1<T, F> func) {
        return (T) min((Collection) iterable, func);
    }

    public static <E> List<E> shuffle(final Iterable<E> iterable) {
        final List<E> shuffled = newArrayList(iterable);
        Collections.shuffle(shuffled);
        return shuffled;
    }

    public List<T> shuffle() {
        return shuffle(iterable);
    }

    public static <E> E sample(final Iterable<E> iterable) {
        return newArrayList(iterable).get(new Random().nextInt(size(iterable)));
    }

    public T sample() {
        return sample(iterable);
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

    public static <E, T extends Comparable<? super T>> List<E> sortBy(final Iterable<E> iterable,
        final Function1<E, T> func) {
        final List<E> sortedList = newArrayList(iterable);
        Collections.sort(sortedList, new Comparator<E>() {
            @Override
            public int compare(E o1, E o2) {
                return func.apply(o1).compareTo(func.apply(o2));
            }
        });
        return sortedList;
    }

    @SuppressWarnings("unchecked")
    public <E, V extends Comparable<? super V>> List<E> sortBy(final Function1<E, V> func) {
        return sortBy((Iterable<E>) iterable, func);
    }

    public static <K, V extends Comparable<? super V>> List<Map<K, V>> sortBy(final Iterable<Map<K, V>> iterable,
        final K key) {
        final List<Map<K, V>> sortedList = newArrayList(iterable);
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

    @SuppressWarnings("unchecked")
    public <K, E> Map<K, List<E>> groupBy(final Function1<E, K> func) {
        return groupBy((Iterable<E>) iterable, func);
    }

    @SuppressWarnings("unchecked")
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

    @SuppressWarnings("unchecked")
    public <K, E> Map<K, List<E>> indexBy(final String property) {
        return indexBy((Iterable<E>) iterable, property);
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

    @SuppressWarnings("unchecked")
    public <K, E> Map<K, Integer> countBy(Function1<E, K> func) {
        return countBy((Iterable<E>) iterable, func);
    }

    @SuppressWarnings("unchecked")
    public static <E> E[] toArray(final Iterable<E> iterable) {
        return (E[]) newArrayList(iterable).toArray();
    }

    @SuppressWarnings("unchecked")
    public <E> E[] toArray() {
        return toArray((Iterable<E>) iterable);
    }

    public static <K, V> Map<K, V> toMap(final Iterable<Map.Entry<K, V>> iterable) {
        final Map<K, V> result = newLinkedHashMap();
        for (Map.Entry<K, V> entry : iterable) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public <K, V> Map<K, V> toMap() {
        return toMap((Iterable<Map.Entry<K, V>>) iterable);
    }

    public static <K, V> Map<K, V> toMap(final List<Tuple<K, V>> tuples) {
        final Map<K, V> result = newLinkedHashMap();
        for (final Tuple<K, V> entry : tuples) {
            result.put(entry.fst(), entry.snd());
        }
        return result;
    }

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

    public int size() {
        return size(iterable);
    }

    @SuppressWarnings("unchecked")
    public static <E> int size(final E ... array) {
        return array.length;
    }

    @SuppressWarnings("unchecked")
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

    @SuppressWarnings("unchecked")
    public static <E> List<E>[] partition(final E[] iterable, final Predicate<E> pred) {
        return (List<E>[]) partition(Arrays.asList(iterable), pred).toArray(new ArrayList[2]);
    }

    public static <E> E first(final Iterable<E> iterable) {
        return iterable.iterator().next();
    }

    @SuppressWarnings("unchecked")
    public static <E> E first(final E ... array) {
        return array[0];
    }

    public static <E> List<E> first(final List<E> list, final int n) {
        return list.subList(0, Math.min(n, list.size()));
    }

    public T first() {
        return iterable.iterator().next();
    }

    @SuppressWarnings("unchecked")
    public List<T> first(final int n) {
        return ((List) iterable).subList(0, n);
    }

    public static <E> E head(final Iterable<E> iterable) {
        return first(iterable);
    }

    @SuppressWarnings("unchecked")
    public static <E> E head(final E ... array) {
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

    @SuppressWarnings("unchecked")
    public static <E> E[] initial(final E ... array) {
        return initial(array, 1);
    }

    public static <E> E[] initial(final E[] array, final int n) {
        return Arrays.copyOf(array, array.length - n);
    }

    @SuppressWarnings("unchecked")
    public List<T> initial() {
        return initial((List) iterable, 1);
    }

    @SuppressWarnings("unchecked")
    public List<T> initial(final int n) {
        return initial((List) iterable, n);
    }

    @SuppressWarnings("unchecked")
    public static <E> E last(final E ... array) {
        return array[array.length - 1];
    }

    public static <E> E last(final List<E> list) {
        return list.get(list.size() - 1);
    }

    public static <E> List<E> last(final List<E> list, final int n) {
        return list.subList(Math.max(0, list.size() - n), list.size());
    }

    public T last() {
        return last((List<T>) iterable);
    }

    @SuppressWarnings("unchecked")
    public List<T> last(final int n) {
        return last((List) iterable, n);
    }

    public static <E> List<E> rest(final List<E> list) {
        return rest(list, 1);
    }

    public static <E> List<E> rest(final List<E> list, int n) {
        return list.subList(Math.min(n, list.size()), list.size());
    }

    @SuppressWarnings("unchecked")
    public static <E> E[] rest(final E ... array) {
        return rest(array, 1);
    }

    @SuppressWarnings("unchecked")
    public static <E> E[] rest(final E[] array, final int n) {
        return (E[]) rest(Arrays.asList(array), n).toArray();
    }

    @SuppressWarnings("unchecked")
    public List<T> rest() {
        return rest((List) iterable);
    }

    @SuppressWarnings("unchecked")
    public List<T> rest(int n) {
        return rest((List) iterable, n);
    }

    public static <E> List<E> tail(final List<E> list) {
        return rest(list);
    }

    public static <E> List<E> tail(final List<E> list, final int n) {
        return rest(list, n);
    }

    @SuppressWarnings("unchecked")
    public static <E> E[] tail(final E ... array) {
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

    @SuppressWarnings("unchecked")
    public static <E> E[] drop(final E ... array) {
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

    @SuppressWarnings("unchecked")
    public static <E> E[] compact(final E ... array) {
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

    @SuppressWarnings("unchecked")
    public static <E> E[] compact(final E[] array, final E falsyValue) {
        return (E[]) compact(Arrays.asList(array), falsyValue).toArray();
    }

    @SuppressWarnings("unchecked")
    public List<T> compact() {
        return compact((List) iterable);
    }

    @SuppressWarnings("unchecked")
    public List<T> compact(final T falsyValue) {
        return compact((List) iterable, falsyValue);
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

    @SuppressWarnings("unchecked")
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
        return flatten((List) iterable);
    }

    public List<T> flatten(final boolean shallow) {
        return flatten((List) iterable, shallow);
    }

    @SuppressWarnings("unchecked")
    public static <E> List<E> without(final List<E> list, E ... values) {
        final List<E> valuesList = Arrays.asList(values);
        return filter(list, new Predicate<E>() {
            @Override
            public Boolean apply(E elem) {
                return !contains(valuesList, elem);
            }
        });
    }

    @SuppressWarnings("unchecked")
    public static <E> E[] without(final E[] array, final E ... values) {
        return (E[]) without(Arrays.asList(array), values).toArray();
    }

    public static <E> List<E> uniq(final List<E> list) {
        return newArrayList(newHashSet(list));
    }

    @SuppressWarnings("unchecked")
    public static <E> E[] uniq(final E ... array) {
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

    @SuppressWarnings("unchecked")
    public static <K, E> E[] uniq(final E[] array, final Function1<E, K> func) {
        return (E[]) uniq(Arrays.asList(array), func).toArray();
    }

    @SuppressWarnings("unchecked")
    public static <E> List<E> union(final List<E> list, final List<E> ... lists) {
        final Set<E> union = newLinkedHashSet();
        union.addAll(list);
        for (List<E> localList : lists) {
            union.addAll(localList);
        }
        return newArrayList(union);
    }

    @SuppressWarnings("unchecked")
    public List<T> unionWith(final List<T> ... lists) {
        return union(newArrayList(iterable), lists);
    }

    @SuppressWarnings("unchecked")
    public static <E> E[] union(final E[] ... arrays) {
        final Set<E> union = newLinkedHashSet();
        for (E[] array : arrays) {
            union.addAll(Arrays.asList(array));
        }
        return (E[]) newArrayList(union).toArray();
    }

    public static <E> List<E> intersection(final List<E> list1, final List<E> list2) {
        return filter(list1, new Predicate<E>() {
            @Override
            public Boolean apply(E elem) {
                return contains(list2, elem);
            }
        });
    }

    @SuppressWarnings("unchecked")
    public static <E> List<E> intersection(final List<E> list, final List<E> ... lists) {
        final Stack<List<E>> stack = new Stack<List<E>>();
        stack.push(list);
        for (int index = 0; index < lists.length; index += 1) {
          stack.push(intersection(stack.peek(), lists[index]));
        }
        return stack.peek();
    }

    @SuppressWarnings("unchecked")
    public List<T> intersectionWith(final List<T> ... lists) {
        return intersection(newArrayList(iterable), lists);
    }

    @SuppressWarnings("unchecked")
    public static <E> E[] intersection(final E[] ... arrays) {
        final Stack<List<E>> stack = new Stack<List<E>>();
        stack.push(Arrays.asList(arrays[0]));
        for (int index = 1; index < arrays.length; index += 1) {
          stack.push(intersection(stack.peek(), Arrays.asList(arrays[index])));
        }
        return (E[]) stack.peek().toArray();
    }

    public static <E> List<E> difference(final List<E> list1, final List<E> list2) {
        return filter(list1, new Predicate<E>() {
            @Override
            public Boolean apply(E elem) {
                return !contains(list2, elem);
            }
        });
    }

    @SuppressWarnings("unchecked")
    public static <E> List<E> difference(final List<E> list, final List<E> ... lists) {
        final Stack<List<E>> stack = new Stack<List<E>>();
        stack.push(list);
        for (int index = 0; index < lists.length; index += 1) {
          stack.push(difference(stack.peek(), lists[index]));
        }
        return stack.peek();
    }

    @SuppressWarnings("unchecked")
    public List<T> differenceWith(final List<T> ... lists) {
        return difference(newArrayList(iterable), lists);
    }

    @SuppressWarnings("unchecked")
    public static <E> E[] difference(final E[] ... arrays) {
        final Stack<List<E>> stack = new Stack<List<E>>();
        stack.push(Arrays.asList(arrays[0]));
        for (int index = 1; index < arrays.length; index += 1) {
          stack.push(difference(stack.peek(), Arrays.asList(arrays[index])));
        }
        return (E[]) stack.peek().toArray();
    }

    @SuppressWarnings("unchecked")
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

    @SuppressWarnings("unchecked")
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

    @SuppressWarnings("unchecked")
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

    public static <T> List<List<T>> chunk(final Iterable<T> iterable, final int size) {
        int index = 0;
        int length = size(iterable);
        final List<List<T>> result = new ArrayList<List<T>>(length / size);
        while (index < length) {
            result.add(newArrayList(iterable).subList(index, Math.min(length, index + size)));
            index += size;
        }
        return result;
    }

    public List<List<T>> chunk(final int size) {
        return chunk(getIterable(), size);
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
        delay(function, 0);
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

    @SuppressWarnings("unchecked")
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
        class AfterFunction implements Function<E> {
            private final int count;
            private int index;
            private E result;
            public AfterFunction(final int count) {
                this.count = count;
            }
            public E apply() {
                if (++index >= count) {
                    result = function.apply();
                }
                return result;
            }
        }
        return new AfterFunction(count);
    }

    public static <E> Function<E> before(final int count, final Function<E> function) {
        class BeforeFunction implements Function<E> {
            private final int count;
            private int index;
            private E result;
            public BeforeFunction(final int count) {
                this.count = count;
            }
            public E apply() {
                if (++index <= count) {
                    result = function.apply();
                }
                return result;
            }
        }
        return new BeforeFunction(count);
    }

    public static <T> Function<T> once(final Function<T> function) {
        return new Function<T>() {
            private volatile boolean executed;
            private T result;
            @Override
            public T apply() {
                if (!executed) {
                    executed = true;
                    result = function.apply();
                }
                return result;
            }
        };
    }

    public static <K, V> Set<K> keys(final Map<K, V> object) {
        return object.keySet();
    }

    public static <K, V> Collection<V> values(final Map<K, V> object) {
        return object.values();
    }

    public static <K, V> List<Tuple<K, V>> mapObject(final Map<K, V> object, final Function1<? super V, V> func) {
        return map(newArrayList(object.entrySet()), new Function1<Map.Entry<K, V>, Tuple<K, V>>() {
            @Override
            public Tuple<K, V> apply(Map.Entry<K, V> entry) {
                return Tuple.create(entry.getKey(), func.apply(entry.getValue()));
            }
        });
    }

    public static <K, V> List<Tuple<K, V>> pairs(final Map<K, V> object) {
        return map(newArrayList(object.entrySet()), new Function1<Map.Entry<K, V>, Tuple<K, V>>() {
            @Override
            public Tuple<K, V> apply(Map.Entry<K, V> entry) {
                return Tuple.create(entry.getKey(), entry.getValue());
            }
        });
    }

    public static <K, V> List<Tuple<V, K>> invert(final Map<K, V> object) {
        return map(newArrayList(object.entrySet()), new Function1<Map.Entry<K, V>, Tuple<V, K>>() {
            @Override
            public Tuple<V, K> apply(Map.Entry<K, V> entry) {
                return Tuple.create(entry.getValue(), entry.getKey());
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

    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> extend(final Map<K, V> destination, final Map<K, V> ... sources) {
        final Map<K, V> result = newLinkedHashMap();
        for (final Map.Entry<K, V> entry : destination.entrySet()) {
            result.put(entry.getKey(), entry.getValue());
        }
        for (final Map<K, V> source : sources) {
            for (final Map.Entry<K, V> entry : source.entrySet()) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
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

    @SuppressWarnings("unchecked")
    public static <K, V> List<Tuple<K, V>> pick(final Map<K, V> object, final V ... keys) {
        return without(map(newArrayList(object.entrySet()), new Function1<Map.Entry<K, V>, Tuple<K, V>>() {
            @Override
            public Tuple<K, V> apply(Map.Entry<K, V> entry) {
                if (Arrays.asList(keys).contains(entry.getKey())) {
                    return Tuple.create(entry.getKey(), entry.getValue());
                } else {
                    return null;
                }
            }
        }), (Tuple<K, V>) null);
    }

    @SuppressWarnings("unchecked")
    public static <K, V> List<Tuple<K, V>> pick(final Map<K, V> object, final Predicate<V> pred) {
        return without(map(newArrayList(object.entrySet()), new Function1<Map.Entry<K, V>, Tuple<K, V>>() {
            @Override
            public Tuple<K, V> apply(Map.Entry<K, V> entry) {
                if (pred.apply(object.get(entry.getKey()))) {
                    return Tuple.create(entry.getKey(), entry.getValue());
                } else {
                    return null;
                }
            }
        }), (Tuple<K, V>) null);
    }

    @SuppressWarnings("unchecked")
    public static <K, V> List<Tuple<K, V>> omit(final Map<K, V> object, final V ... keys) {
        return without(map(newArrayList(object.entrySet()), new Function1<Map.Entry<K, V>, Tuple<K, V>>() {
            @Override
            public Tuple<K, V> apply(Map.Entry<K, V> entry) {
                if (Arrays.asList(keys).contains(entry.getKey())) {
                    return null;
                } else {
                    return Tuple.create(entry.getKey(), entry.getValue());
                }
            }
        }), (Tuple<K, V>) null);
    }

    @SuppressWarnings("unchecked")
    public static <K, V> List<Tuple<K, V>> omit(final Map<K, V> object, final Predicate<V> pred) {
        return without(map(newArrayList(object.entrySet()), new Function1<Map.Entry<K, V>, Tuple<K, V>>() {
            @Override
            public Tuple<K, V> apply(Map.Entry<K, V> entry) {
                if (pred.apply(entry.getValue())) {
                    return null;
                } else {
                    return Tuple.create(entry.getKey(), entry.getValue());
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

    @SuppressWarnings("unchecked")
    public static <E> E[] clone(final E ... iterable) {
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

    public boolean isEmpty() {
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

    public static String escape(final String value) {
        return value.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;")
            .replaceAll("\"", "&quot;").replaceAll("'", "&#x27;").replaceAll("`", "&#x60;");
    }

    public static String unescape(final String value) {
        return value.replaceAll("&#x60;", "`").replaceAll("&#x27;", "'").replaceAll("&lt;", "<")
            .replaceAll("&gt;", ">").replaceAll("&quot;", "\"").replaceAll("&amp;", "&");
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

    public static String uniquePassword() {
        final String[] passwords = new String[] {
            "ALKJVBPIQYTUIWEBVPQALZVKQRWORTUYOYISHFLKAJMZNXBVMNFGAHKJSDFALAPOQIERIUYTGSFGKMZNXBVJAHGFAKX",
            "1234567890",
            "qpowiealksdjzmxnvbfghsdjtreiuowiruksfhksajmzxncbvlaksjdhgqwetytopskjhfgvbcnmzxalksjdfhgbvzm",
            ".@,-+/()#$%^&*!"
        };
        final StringBuilder result = new StringBuilder();
        final long passwordLength = Math.abs(UUID.randomUUID().getLeastSignificantBits() % 8) + 8;
        for (int index = 0; index < passwordLength; index += 1) {
            final int passIndex = (int) (passwords.length * index / passwordLength);
            final int charIndex = (int) Math.abs(
                UUID.randomUUID().getLeastSignificantBits() % passwords[passIndex].length());
            result.append(passwords[passIndex].charAt(charIndex));
        }
        return result.toString();
    }

    public static <E> Template<Set<E>> template(final String template) {
        return new TemplateImpl<E>(template);
    }

    public static <T> Chain<T> chain(final List<T> list) {
        return new $.Chain<T>(list);
    }

    public static <T> Chain<T> chain(final Iterable<T> iterable) {
        return new $.Chain<T>(newArrayList(iterable));
    }

    @SuppressWarnings("unchecked")
    public static <T> Chain<T> chain(final T ... array) {
        return new $.Chain<T>(Arrays.asList(array));
    }

    public Chain<T> chain() {
        return new $.Chain<T>(newArrayList(iterable));
    }

    public static class Chain<T> {
        private final T item;
        private final List<T> list;
        @SuppressWarnings("unchecked")
        public Chain(final T item) {
            this.item = item;
            this.list = null;
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

        public Chain<T> compact() {
            return new Chain<T>($.compact(list));
        }

        public Chain<T> compact(final T falsyValue) {
            return new Chain<T>($.compact(list, falsyValue));
        }

        @SuppressWarnings("unchecked")
        public Chain flatten() {
            return new Chain($.flatten(list));
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
            return new Chain<F>($.reduce(list, func, zeroElem));
        }

        public <F> Chain<F> reduceRight(final FunctionAccum<F, T> func, final F zeroElem) {
            return new Chain<F>($.reduceRight(list, func, zeroElem));
        }

        public Chain<Optional<T>> find(final Predicate<T> pred) {
            return new Chain<Optional<T>>($.find(list, pred));
        }

        public Chain<Optional<T>> findLast(final Predicate<T> pred) {
            return new Chain<Optional<T>>($.findLast(list, pred));
        }

        @SuppressWarnings("unchecked")
        public Chain<Comparable> max() {
            return new Chain<Comparable>($.max((Collection) list));
        }

        public <F extends Comparable<? super F>> Chain<T> max(final Function1<T, F> func) {
            return new Chain<T>($.max(list, func));
        }

        @SuppressWarnings("unchecked")
        public Chain<Comparable> min() {
            return new Chain<Comparable>($.min((Collection) list));
        }

        public <F extends Comparable<? super F>> Chain<T> min(final Function1<T, F> func) {
            return new Chain<T>($.min(list, func));
        }

        @SuppressWarnings("unchecked")
        public Chain<Comparable> sort() {
            return new Chain<Comparable>($.sort((List<Comparable>) list));
        }

        public <F extends Comparable<? super F>> Chain<T> sortBy(final Function1<T, F> func) {
            return new Chain<T>($.sortBy(list, func));
        }

        @SuppressWarnings("unchecked")
        public <K> Chain<Map<K, Comparable>> sortBy(final K key) {
            return new Chain<Map<K, Comparable>>($.sortBy((List<Map<K, Comparable>>) list, key));
        }

        public <F> Chain<Map<F, List<T>>> groupBy(final Function1<T, F> func) {
            return new Chain<Map<F, List<T>>>($.groupBy(list, func));
        }

        public Chain<Map<Object, List<T>>> indexBy(final String property) {
            return new Chain<Map<Object, List<T>>>($.indexBy(list, property));
        }

        public <F> Chain<Map<F, Integer>> countBy(final Function1<T, F> func) {
            return new Chain<Map<F, Integer>>($.countBy(list, func));
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

        public Chain<Boolean> every(final Predicate<T> pred) {
            return new Chain<Boolean>($.every(list, pred));
        }

        public Chain<Boolean> some(final Predicate<T> pred) {
            return new Chain<Boolean>($.some(list, pred));
        }

        public Chain<Boolean> contains(final T elem) {
            return new Chain<Boolean>($.contains(list, elem));
        }

        public Chain<T> invoke(final String methodName, final List<Object> args) {
            return new Chain<T>($.invoke(list, methodName, args));
        }

        public Chain<T> invoke(final String methodName) {
            return new Chain<T>($.invoke(list, methodName));
        }

        public Chain<Object> pluck(final String propertyName) {
            return new Chain<Object>($.pluck(list, propertyName));
        }

        public <E> Chain<T> where(final List<Tuple<String, E>> properties) {
            return new Chain<T>($.where(list, properties));
        }

        public <E> Chain<Optional<T>> findWhere(final List<Tuple<String, E>> properties) {
            return new Chain<Optional<T>>($.findWhere(list, properties));
        }

        public Chain<T> uniq() {
            return new Chain<T>($.uniq(list));
        }

        @SuppressWarnings("unchecked")
        public <F> Chain<F> uniq(final Function1<T, F> func) {
            return new Chain<F>($.newArrayList((Iterable<F>) $.uniq(list, func)));
        }

        @SuppressWarnings("unchecked")
        public Chain<T> union(final List<T> ... lists) {
            return new Chain<T>($.union(list, lists));
        }

        @SuppressWarnings("unchecked")
        public Chain<T> intersection(final List<T> ... lists) {
            return new Chain<T>($.intersection(list, lists));
        }

        @SuppressWarnings("unchecked")
        public Chain<T> difference(final List<T> ... lists) {
            return new Chain<T>($.difference(list, lists));
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
            return new Chain<T>($.concat(list, lists));
        }

        public Chain<T> slice(final int start) {
            return new Chain<T>($.slice(list, start));
        }

        public Chain<T> slice(final int start, final int end) {
            return new Chain<T>($.slice(list, start, end));
        }

        public Chain<T> reverse() {
            return new Chain<T>($.reverse(list));
        }

        public Chain<String> join() {
            return new Chain<String>($.join(list));
        }

        public Chain<String> join(final String separator) {
            return new Chain<String>($.join(list, separator));
        }

        public Chain<T> skip(final int numberToSkip) {
            return new Chain<T>(list.subList(numberToSkip, list.size()));
        }

        public Chain<T> limit(final int size) {
            return new Chain<T>(list.subList(0, size));
        }

        @SuppressWarnings("unchecked")
        public <K, V> Chain<Map<K, V>> toMap() {
            return new Chain<Map<K, V>>($.toMap((Iterable<Map.Entry<K, V>>) list));
        }

        public boolean isEmpty() {
            return $.isEmpty(list);
        }

        public int size() {
            return $.size(list);
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

    public static <T extends Comparable<T>> List<T> sort(final Iterable<T> iterable) {
        final List<T> localList = newArrayList(iterable);
        Collections.<T>sort(localList);
        return localList;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Comparable<T>> T[] sort(final T ... array) {
        final T[] localArray = array.clone();
        Arrays.<T>sort(localArray);
        return localArray;
    }

    @SuppressWarnings("unchecked")
    public List<Comparable> sort() {
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

    public static <T> String join(final Iterable<T> iterable) {
        return join(iterable, " ");
    }

    public static <T> String join(final T[] array, final String separator) {
        return join(Arrays.asList(array), separator);
    }

    public static <T> String join(final T[] array) {
        return join(array, " ");
    }

    public String join(final String separator) {
        return join(iterable, separator);
    }

    public String join() {
        return join(iterable);
    }

    @SuppressWarnings("unchecked")
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

    @SuppressWarnings("unchecked")
    public static <T> List<T> concat(final Iterable<T> first, final Iterable<T> ... other) {
        int length = 0;
        for (Iterable<T> otherItem : other) {
            length += size(otherItem);
        }
        final T[] result = Arrays.copyOf(toArray(first), size(first) + length);
        int index = 0;
        for (Iterable<T> otherItem : other) {
            System.arraycopy(toArray(otherItem), 0, result, size(first) + index, size(otherItem));
            index += size(otherItem);
        }
        return Arrays.asList(result);
    }


    @SuppressWarnings("unchecked")
    public List<T> concatWith(final Iterable<T> ... other) {
        return concat(iterable, other);
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> slice(final Iterable<T> iterable, final int start) {
        final List<T> result;
        if (start >= 0) {
            result = newArrayList(iterable).subList(start, size(iterable));
        } else {
            result = newArrayList(iterable).subList(size(iterable) + start, size(iterable));
        }
        return result;
    }

    @SuppressWarnings("unchecked")
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

    @SuppressWarnings("unchecked")
    public static <T> T[] slice(final T[] array, final int start, final int end) {
        return (T[]) slice(Arrays.asList(array), start, end).toArray();
    }

    public List<T> slice(final int start, final int end) {
        return slice(iterable, start, end);
    }

    public static <T> List<T> reverse(final Iterable<T> iterable) {
        final List<T> result = newArrayList(iterable);
        Collections.reverse(result);
        return result;
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] reverse(final T ... array) {
        return (T[]) reverse(Arrays.asList(array)).toArray();
    }

    public List<T> reverse() {
        return reverse(iterable);
    }

    public Iterable<T> getIterable() {
        return iterable;
    }

    public Iterable<T> value() {
        return iterable;
    }

    public Optional<String> getString() {
        return string;
    }

    @SuppressWarnings("unchecked")
    protected static <T> List<T> newArrayList() {
        try {
            final Class<?> listsClass = classForName("com.google.common.collect.Lists");
            return (List<T>) listsClass.getDeclaredMethod("newArrayList").invoke(null);
        } catch (Exception e) {
            return new ArrayList<T>();
        }
    }

    @SuppressWarnings("unchecked")
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

    protected static List<Integer> newIntegerList(int ... array) {
        final List<Integer> result = new ArrayList<Integer>(array.length);
        for (final int item : array) {
            result.add(item);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    protected static <T> List<T> newArrayListWithExpectedSize(int size) {
        try {
            final Class<?> listsClass = classForName("com.google.common.collect.Lists");
            return (List<T>) listsClass.getDeclaredMethod("newArrayListWithExpectedSize", Integer.TYPE)
                .invoke(null, size);
        } catch (Exception e) {
            return new ArrayList<T>(size);
        }
    }

    @SuppressWarnings("unchecked")
    protected static <T> Set<T> newLinkedHashSet() {
        try {
            final Class<?> setsClass = classForName("com.google.common.collect.Sets");
            return (Set<T>) setsClass.getDeclaredMethod("newLinkedHashSet").invoke(null);
        } catch (Exception e) {
            return new LinkedHashSet<T>();
        }
    }

    @SuppressWarnings("unchecked")
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

    @SuppressWarnings("unchecked")
    protected static <T> Set<T> newLinkedHashSetWithExpectedSize(int size) {
        try {
            final Class<?> setsClass = classForName("com.google.common.collect.Sets");
            return (Set<T>) setsClass.getDeclaredMethod("newLinkedHashSetWithExpectedSize", Integer.TYPE)
                .invoke(null, size);
        } catch (Exception e) {
            return new LinkedHashSet<T>(size);
        }
    }

    @SuppressWarnings("unchecked")
    protected static <K, E> Map<K, E> newLinkedHashMap() {
        try {
            final Class<?> mapsClass = classForName("com.google.common.collect.Maps");
            return (Map<K, E>) mapsClass.getDeclaredMethod("newLinkedHashMap").invoke(null);
        } catch (Exception e) {
            return new LinkedHashMap<K, E>();
        }
    }

    public static void main(String ... args) {
        final String message = "Underscore-java is a java port of Underscore.js.\n\n"
            + "In addition to porting Underscore's functionality, Underscore-java includes matching unit tests.\n\n"
            + "For docs, license, tests, and downloads, see: http://javadev.github.io/underscore-java";
        System.out.println(message);
    }
}
