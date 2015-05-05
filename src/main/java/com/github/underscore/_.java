/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Valentyn Kolesnikov
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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Underscore-java is a java port of Underscore.js.
 *
 * @author Valentyn Kolesnikov
 */
public final class _<T> {
    private final Iterable<T> iterable;

    public _(final Iterable<T> iterable) {
        this.iterable = iterable;
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
        final List<T> transformed = Lists.newArrayListWithExpectedSize(list.size());
        for (E element : list) {
            transformed.add(func.apply(element));
        }
        return transformed;
    }

    public static <T, E> Set<T> map(final Set<E> set, final Function1<? super E, T> func) {
        final Set<T> transformed = Sets.newLinkedHashSetWithExpectedSize(set.size());
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

    public static <T, E> E inject(final Iterable<T> iterable, final FunctionAccum<E, T> func, final E zeroElem) {
        return reduce(iterable, func, zeroElem);
    }

    public static <T, E> E foldl(final Iterable<T> iterable, final FunctionAccum<E, T> func, final E zeroElem) {
        return reduce(iterable, func, zeroElem);
    }

    public static <T, E> E reduceRight(final Iterable<T> iterable, final FunctionAccum<E, T> func, final E zeroElem) {
        final List<T> list = Lists.newArrayList();
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

    public static <E> E find(final Iterable<E> iterable, final Predicate<E> pred) {
        for (E element : iterable) {
            if (pred.apply(element)) {
                return element;
            }
        }
        return null;
    }

    public static <E> E detect(final Iterable<E> iterable, final Predicate<E> pred) {
        return find(iterable, pred);
    }

    public static <E> List<E> filter(final List<E> list,
                                     final Predicate<E> pred) {
        final List<E> filtered = Lists.newArrayList();
        for (E element : list) {
            if (pred.apply(element)) {
                filtered.add(element);
            }
        }
        return filtered;
    }

    public static <E> Set<E> filter(final Set<E> set,
                                    final Predicate<E> pred) {
        final Set<E> filtered = Sets.newLinkedHashSet();
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

    public static <T, E> List<E> where(final List<E> list,
                                       final List<Tuple<String, T>> properties) {
        return filter(list, new Predicate<E>() {
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

    public static <T, E> Set<E> where(final Set<E> list,
                                      final List<Tuple<String, T>> properties) {
        return filter(list, new Predicate<E>() {
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

    public static <T, E> E findWhere(final Iterable<E> iterable,
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
        return find(iterable, new Predicate<E>() {
            @Override
            public Boolean apply(E arg) {
                return !pred.apply(arg);
            }
        }) == null;
    }

    public static <E> boolean all(final Iterable<E> set, final Predicate<E> pred) {
        return every(set, pred);
    }

    public static <E> boolean some(final Iterable<E> iterable, final Predicate<E> pred) {
        return find(iterable, pred) != null;
    }

    public static <E> boolean any(final Iterable<E> iterable, final Predicate<E> pred) {
        return some(iterable, pred);
    }

    public static <E> boolean contains(final Iterable<E> iterable, final E elem) {
        return some(iterable, new Predicate<E>() {
            @Override
            public Boolean apply(E e) {
                return elem.equals(e);
            }
        });
    }

    public static <E> boolean include(final Iterable<E> iterable, final E elem) {
        return contains(iterable, elem);
    }

    public static <E> List<E> invoke(final Iterable<E> iterable, final String methodName,
                                     final List<Object> args) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        final List<E> result = Lists.newArrayList();
        final List<Class<?>> argTypes = map(args, new Function1<Object, Class<?>>() {
            public Class<?> apply(Object input) {
                return input.getClass();
            }
        });
        final Method method = iterable.iterator().next().getClass().getMethod(methodName, argTypes.toArray(new Class[argTypes.size()]));
        each(iterable, new Block<E>() {
            public void apply(E arg) {
                try {
                    result.add((E) method.invoke(arg, args.toArray(new Object[args.size()])));
                } catch (Exception e) {
                    throw new IllegalArgumentException(e);
                }
            }
        });
        return result;
    }

    public static <E> List<E> invoke(final Iterable<E> iterable, final String methodName) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        return invoke(iterable, methodName, Collections.emptyList());
    }

    public static <E> List<Object> pluck(final List<E> list,
                                         final String propertyName) throws NoSuchFieldException, SecurityException {
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

    public static <E> Set<Object> pluck(final Set<E> set,
                                        final String propertyName) throws NoSuchFieldException, SecurityException {
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

    public static <E, T extends Comparable<? super T>> List<E> sortBy(final List<E> list, final Function1<E, T> func) {
        final List<E> sortedList = Lists.newArrayList();
        each(list, new Block<E>() {
            @Override
            public void apply(E arg) {
                sortedList.add(arg);
            }
        });
        Collections.sort(sortedList, new Comparator<E>() {
            @Override
            public int compare(E o1, E o2) {
                return func.apply(o1).compareTo(func.apply(o2));
            }
        });
        return sortedList;
    }

    public static <K, E> Map<K, List<E>> groupBy(final Iterable<E> iterable, final Function1<E, K> func) {
        final Map<K, List<E>> retVal = Maps.newLinkedHashMap();
        for (E e : iterable) {
            final K key = func.apply(e);
            List<E> val;
            if (retVal.containsKey(key)) {
                val = retVal.get(key);
            } else {
                val = Lists.newArrayList();
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
        final Map<K, Integer> retVal = Maps.newLinkedHashMap();
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

    public static <E> List<E> shuffle(final List<E> list) {
        final List<E> shuffled = Lists.newArrayList(list);
        Collections.shuffle(shuffled);
        return shuffled;
    }

    public static <E> E sample(final List<E> list) {
        return list.get(new Random().nextInt(list.size()));
    }

    public static <E> Set<E> sample(final List<E> list, final int howMany) {
        final int size = Math.min(howMany, list.size());
        final Set<E> samples = Sets.newLinkedHashSetWithExpectedSize(size);
        while (samples.size() < size) {
            E sample = sample(list);
            samples.add(sample);
        }
        return samples;
    }

    public static <E> E[] toArray(final Iterable<E> iterable) {
        final List<E> list = Lists.newArrayList();
        each(iterable, new Block<E>() {
            @Override
            public void apply(E elem) {
                list.add(elem);
            }
        });
        return (E[]) list.toArray();
    }

    public static int size(final Iterable<?> iterable) {
        int size;
        final Iterator<?> iterator = iterable.iterator();
        for (size = 0; iterator.hasNext(); size += 1) {
            iterator.next();
        }
        return size;
    }

    public static <E> E first(final Iterable<E> iterable) {
        return iterable.iterator().next();
    }

    public static <E> E first(final E[] array) {
        return array[0];
    }

    public static <E> List<E> first(final List<E> list, final int n) {
        return list.subList(0, n);
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
        return list.subList(0, list.size() - n);
    }

    public static <E> E[] initial(final E[] array) {
        return initial(array, 1);
    }

    public static <E> E[] initial(final E[] array, final int n) {
        return Arrays.copyOf(array, array.length - n);
    }

    public List<T> initial() {
        return _.initial(((List) iterable), 1);
    }

    public List<T> initial(final int n) {
        return _.initial(((List) iterable), n);
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
        return _.last(((List<T>) iterable));
    }

    public List<T> last(final int n) {
        return _.last(((List) iterable), n);
    }

    public static <E> List<E> rest(final List<E> list) {
        return rest(list, 1);
    }

    public static <E> List<E> rest(final List<E> list, int n) {
        return list.subList(n, list.size());
    }

    public List<T> rest() {
        return _.rest((List) iterable);
    }

    public List<T> rest(int n) {
        return _.rest((List) iterable, n);
    }

    public static <E> List<E> tail(final List<E> list) {
        return rest(list);
    }

    public static <E> List<E> tail(final List<E> list, final int n) {
        return rest(list, n);
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

    public static <E> E[] rest(final E[] array, final int n) {
        return (E[]) rest(Arrays.asList(array), n).toArray();
    }

    public static <E> E[] rest(final E[] array) {
        return rest(array, 1);
    }

    public static <E> E[] tail(final E[] array) {
        return rest(array);
    }

    public static <E> E[] tail(final E[] array, final int n) {
        return rest(array, n);
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
                return !arg.equals(falsyValue);
            }
        });
    }

    public static <E> E[] compact(final E[] array, final E falsyValue) {
        return (E[]) compact(Arrays.asList(array), falsyValue).toArray();
    }

    public List<T> compact() {
        return _.compact(((List) iterable));
    }

    public List<T> compact(final T falsyValue) {
        return _.compact(((List) iterable), falsyValue);
    }

    public static <E> List<E> flatten(final List<?> list) {
        List<E> flattened = Lists.newArrayList();
        flatten(list, flattened, -1);
        return flattened;
    }

    public static <E> List<E> flatten(final List<?> list, final boolean shallow) {
        List<E> flattened = Lists.newArrayList();
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
        return _.flatten(((List) iterable));
    }

    public List<T> flatten(final boolean shallow) {
        return _.flatten(((List) iterable), shallow);
    }

    public static <E> List<E> without(final List<E> list, E... values) {
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

    public static <E> E[] without(final E[] array, final E... values) {
        return (E[]) without(Arrays.asList(array), values).toArray();
    }

    public static <E> E[] without(final E[] array, final E value) {
        return without(array, (E[]) Arrays.asList(value).toArray());
    }

    public static <E> List<E> union(final List<E>... lists) {
        final Set<E> union = Sets.newLinkedHashSet();
        for (List<E> list : lists) {
            union.addAll(list);
        }
        return Lists.newArrayList(union);
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

    public static <E> List<E> intersection(final List<E>... lists) {
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

    public static <E> List<E> uniq(final List<E> list) {
        return Lists.newArrayList(Sets.newHashSet(list));
    }

    public static <E> E[] uniq(final E[] array) {
        return (E[]) uniq(Arrays.asList(array)).toArray();
    }

    public static <K, E> Collection<E> uniq(final Iterable<E> iterable, final Function1<E, K> func) {
        final Map<K, E> retVal = Maps.newLinkedHashMap();
        for (final E e : iterable) {
            final K key = func.apply(e);
            retVal.put(key, e);
        }
        return retVal.values();
    }

    public static <K, E> E[] uniq(final E[] array, final Function1<E, K> func) {
        return (E[]) uniq(Arrays.asList(array), func).toArray();
    }

    public static <T> List<List<T>> zip(final List<T>... lists) {
        final List<List<T>> zipped = Lists.newArrayList();
        each(Arrays.asList(lists), new Block<List<T>>() {
            @Override
            public void apply(final List<T> list) {
                _.each(list, new Block<T>() {
                    int index = 0;

                    @Override
                    public void apply(T elem) {
                        final List<T> nTuple = index >= zipped.size() ? Lists.<T>newArrayList() : zipped.get(index);
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

    public static <K, V> List<Tuple<K, V>> object(final List<K> keys, final List<V> values) {
        return map(keys, new Function1<K, Tuple<K, V>>() {
            int index = 0;

            @Override
            public Tuple<K, V> apply(K key) {
                return Tuple.create(key, values.get(index++));
            }
        });
    }

    public static <E> int indexOf(final List<E> list, final E value) {
        return list.indexOf(value);
    }

    public static <E> int indexOf(final E[] array, final E value) {
        return indexOf(Arrays.asList(array), value);
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

    public static <E> int lastIndexOf(final List<E> list, final E value) {
        return list.lastIndexOf(value);
    }

    public static <E> int lastIndexOf(final E[] array, final E value) {
        return lastIndexOf(Arrays.asList(array), value);
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

    public static <E extends Comparable<E>> int sortedIndex(final List<E> list, final E value, final String propertyName) throws NoSuchFieldException, IllegalAccessException {
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
    }

    public static <E extends Comparable<E>> int sortedIndex(final E[] array, final E value, final String propertyName) throws NoSuchFieldException, IllegalAccessException {
        return sortedIndex(Arrays.asList(array), value, propertyName);
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

    public static <E> List<List<E>> partition(final Iterable<E> iterable, final Predicate<E> pred) {
        final List<E> retVal1 = Lists.newArrayList();
        final List<E> retVal2 = Lists.newArrayList();
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
        return (List<E>[]) partition(Arrays.asList(iterable), pred).toArray();
    }

    public static <T> Chain chain(final List<T> list) {
        return new _.Chain<T>(list);
    }

    public static <T> Chain chain(final Set<T> list) {
        return new _.Chain<T>(Lists.newArrayList(list));
    }

    public static <T> Chain chain(final T[] list) {
        return new _.Chain<T>(Arrays.asList(list));
    }

    public static class Chain<T> {
        private final T item;
        private final List<T> list;

        public Chain(final T item) {
            this.item = item;
            this.list = null;
        }

        public Chain(final List<T> list) {
            this.item = null;
            this.list = list;
        }

        public Chain<T> first() {
            return new Chain<T>(_.first(list));
        }

        public Chain<T> first(int n) {
            return new Chain<T>(_.first(list, n));
        }

        public Chain<T> initial() {
            return new Chain<T>(_.initial(list));
        }

        public Chain<T> initial(int n) {
            return new Chain<T>(_.initial(list, n));
        }

        public Chain<T> last() {
            return new Chain<T>(_.last(list));
        }

        public Chain<T> last(int n) {
            return new Chain<T>(_.last(list, n));
        }

        public Chain<T> rest() {
            return new Chain<T>(_.rest(list));
        }

        public Chain<T> rest(int n) {
            return new Chain<T>(_.rest(list, n));
        }

        public Chain<T> flatten() {
            final List<T> flattened = Lists.newArrayList();
            flatten(list, flattened);
            return new Chain<T>(flattened);
        }

        private void flatten(final List<?> fromTreeList, final List<T> toFlatList) {
            for (final Object item : fromTreeList) {
                if (item instanceof List<?>) {
                    flatten((List<?>) item, toFlatList);
                } else {
                    toFlatList.add((T) item);
                }
            }
        }

        public <F> Chain<F> map(final Function1<? super T, F> func) {
            return new Chain<F>(_.map(list, func));
        }

        public Chain<T> filter(final Predicate<T> pred) {
            return new Chain<T>(_.filter(list, pred));
        }

        public Chain<T> reject(final Predicate<T> pred) {
            return new Chain<T>(_.reject(list, pred));
        }

        public <F> Chain<F> reduce(final FunctionAccum<F, T> func, final F zeroElem) {
            F accum = zeroElem;
            for (T element : list) {
                accum = func.apply(accum, element);
            }
            return new Chain<F>(accum);
        }

        public <F> Chain<F> reduceRight(final FunctionAccum<F, T> func, final F zeroElem) {
            final List<T> localList = Lists.newArrayList();
            for (T elem : list) {
                localList.add(0, elem);
            }
            F accum = zeroElem;
            for (T element : localList) {
                accum = func.apply(accum, element);
            }
            return new Chain<F>(accum);
        }

        public <F extends Comparable<? super F>> Chain<T> sortBy(final Function1<T, F> func) {
            return new Chain<T>(_.sortBy(list, func));
        }

        public <F> Chain<F> uniq(final Function1<T, F> func) {
            return new Chain<F>((F) Lists.newArrayList(_.uniq(list, func)));
        }

        public <T> Chain<T> concat(final List<T> second) {
            return new Chain<T>((List<T>) Arrays.asList(_.concat(list.toArray(), second.toArray())));
        }

        public T item() {
            return item;
        }

        public List<T> value() {
            return list;
        }

    }

    public static String escape(String value) {
        return value.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\"", "&quot;");
    }

    public static String unescape(String value) {
        return value.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&quot;", "\"").replaceAll("&amp;", "&");
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

    public static <E> Template<Set<E>> template(final String template) {
        return new Template<Set<E>>() {
            @Override
            public String apply(Set<E> value) {
                String result = template;
                for (E element : value) {
                    result = java.util.regex.Pattern.compile("<%\\=\\s*\\Q" + ((Map.Entry) element).getKey() + "\\E\\s*%>").matcher(
                            result).replaceAll(String.valueOf(((Map.Entry) element).getValue()));
                    result = java.util.regex.Pattern.compile("<%\\-\\s*\\Q" + ((Map.Entry) element).getKey() + "\\E\\s*%>").matcher(
                            result).replaceAll(escape(String.valueOf(((Map.Entry) element).getValue())));
                    java.util.regex.Matcher matcher = java.util.regex.Pattern.compile(
                            "<%\\s*_\\.each\\((\\w+),\\s*function\\((\\w+)\\)\\s*\\{\\s*%>(.*?)<% \\}\\);\\s*%>").matcher(result);
                    if (matcher.find()) {
                        if (((Map.Entry) element).getKey().equals(matcher.group(1))) {
                            String repeatResult = "";
                            for (String item : ((List<String>) ((Map.Entry) element).getValue())) {
                                repeatResult += java.util.regex.Pattern.compile("<%=\\s*\\Q" + matcher.group(2) + "\\E\\s*%>").matcher(
                                        matcher.group(3)).replaceAll(item);
                            }
                            result = matcher.replaceFirst(repeatResult);
                        }
                    };
                    java.util.regex.Matcher matcherPrint = java.util.regex.Pattern.compile(
                            "<%\\s*print\\('([^']*)'\\s*\\+\\s*(\\w+)\\);\\s*%>").matcher(result);
                    if (matcherPrint.find()) {
                        if (((Map.Entry) element).getKey().equals(matcherPrint.group(2))) {
                            result = matcherPrint.replaceFirst(matcherPrint.group(1) + ((Map.Entry) element).getValue());
                        }
                    }
                }
                return result;
            }
        };
    }

    public static <T> void delay(final Function<T> function, final int delayMilliseconds) {
        final java.util.concurrent.ScheduledExecutorService scheduler = java.util.concurrent.Executors.newSingleThreadScheduledExecutor();
        scheduler.schedule(
                new Runnable() {
                    public void run() {
                        function.apply();
                    }
                }, delayMilliseconds, java.util.concurrent.TimeUnit.MILLISECONDS);
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

    public static <T extends Comparable<T>> List<T> sort(final List<T> list) {
        final List<T> localList = Lists.newArrayList(list);
        Collections.<T>sort(localList);
        return localList;
    }

    public static <T extends Comparable<T>> T[] sort(final T[] array) {
        final T[] localArray = array.clone();
        Arrays.<T>sort(localArray);
        return localArray;
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

    public static <T> List<T> concat(final List<T> first, final List<T>... other) {
        int length = 0;
        for (List<T> otherItem : other) {
            length += otherItem.size();
        }
        final T[] result = (T[]) Arrays.copyOf(first.toArray(), first.size() + length);
        int index = 0;
        for (List<T> otherItem : other) {
            System.arraycopy(otherItem.toArray(), 0, result, first.size() + index, otherItem.size());
            index += otherItem.size();
        }
        return (List<T>) Arrays.asList(result);
    }

    public static void main(String[] args) {
        final String message = "Underscore-java is a java port of Underscore.js.\n\n"
                + "In addition to porting Underscore's functionality, Underscore-java includes matching unit tests.\n\n"
                + "For docs, license, tests, and downloads, see: http://javadev.github.io/underscore-java";
        System.out.println(message);
    }
}
