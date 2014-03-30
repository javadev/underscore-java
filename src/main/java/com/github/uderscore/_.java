/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2014 Valentyn Kolesnikov
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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.SynchronousQueue;

/**
 * Underscore library in java.
 *
 * @author Valentyn Kolesnikov
 */
public final class _ {
    public static <T> void each(final Iterable<T> iterable, final Block<? super T> func) {
        for (T element : iterable) {
            func.apply(element);
        }
    }

    public static <T> void forEach(final Iterable<T> iterable, final Block<? super T> func) {
        each(iterable, func);
    }

    public static <T, E> List<T> map(final List<E> list, final Function1<? super E, T> func) {
        final List<T> transformed = new ArrayList<T>(list.size());
        for (E element : list) {
            transformed.add(func.apply(element));
        }
        return transformed;
    }

    public static <T, E> Set<T> map(final Set<E> set, final Function1<? super E, T> func) {
        final Set<T> transformed = new HashSet<T>(set.size());
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

    public static <E> E reduce(final Iterable<E> iterable, final E zeroElem, final Function2<E, E, E> func) {
        E accum = zeroElem;
        for (E element : iterable) {
            accum = func.apply(accum, element);
        }
        return accum;
    }

    public static <E> E inject(final Iterable<E> iterable, final E zeroElem, final Function2<E, E, E> func) {
        return reduce(iterable, zeroElem, func);
    }

    public static <E> E foldl(final Iterable<E> iterable, final E zeroElem, final Function2<E, E, E> func) {
        return reduce(iterable, zeroElem, func);
    }

    public static <E> E reduceRight(final Iterable<E> iterable, final E zeroElem, final Function1<E, E> func) {
        final Stack<E> stack = new Stack<E>();
        for (E elem : iterable) {
            stack.push(elem);
        }
        E accum = zeroElem;
        int index = 0;
        for (E elem : stack) {
            accum = func.apply(accum);
            index += 1;
        }
        return accum;
    }

    public static <E> E foldr(final Iterable<E> iterable, final E zeroElem, final Function1<E, E> func) {
        return reduceRight(iterable, zeroElem, func);
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
        final List<E> filtered = new ArrayList<E>();
        for (E element : list) {
            if (pred.apply(element)) {
                filtered.add(element);
            }
        }
        return filtered;
    }

    public static <E> Set<E> filter(final Set<E> set,
                                    final Predicate<E> pred) {
        final Set<E> filtered = new HashSet<E>();
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

    public static <E> List<E> where(final List<E> list,
                                    final Iterable<Tuple<String, E>> properties) {
        return filter(list, new Predicate<E>() {
            @Override
            public Boolean apply(final E elem) {
                for (Tuple<String, E> prop : properties) {
                    try {
                        if (!elem.getClass().getField(prop.fst()).get(elem)
                                .equals(prop.snd())) {
                            return false;
                        }
                    } catch (Exception ex) {
                        //throw new IllegalArgumentException(ex);
                        return false;
                    }
                }
                return true;
            }
        });

    }

    public static <E> Set<E> where(final Set<E> list,
                                   final Iterable<Tuple<String, E>> properties) {
        return filter(list, new Predicate<E>() {
            @Override
            public Boolean apply(final E elem) {
                for (Tuple<String, E> prop : properties) {
                    try {
                        if (!elem.getClass().getField(prop.fst()).get(elem)
                                .equals(prop.snd())) {
                            return false;
                        }
                    } catch (Exception ex) {
                        //throw new IllegalArgumentException(ex);
                        return false;
                    }
                }
                return true;
            }
        });

    }

    public static <E> E findWhere(final Iterable<E> iterable,
                                  final Iterable<Tuple<String, E>> properties) {
        return find(iterable, new Predicate<E>() {
            @Override
            public Boolean apply(final E elem) {
                for (Tuple<String, E> prop : properties) {
                    try {
                        if (!elem.getClass().getField(prop.fst()).get(elem)
                                .equals(prop.snd())) {
                            return false;
                        }
                    } catch (Exception ex) {
                        //throw new IllegalArgumentException(ex);
                        return false;

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

    public static <E> void invoke(final Iterable<E> iterable, final String methodName,
                                  final List<Object> args) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        final List<Class<?>> argTypes = map(args, new Function1<Object, Class<?>>() {
            public Class<?> apply(Object input) {
                return input.getClass();
            }
        });
        final Method method = iterable.getClass().getMethod(methodName, argTypes.toArray(new Class[argTypes.size()]));
        _.each(iterable, new Block<E>() {
            public void apply(E arg) {
                try {
                    method.invoke(iterable, args.toArray(new Object[args.size()]));
                } catch (Exception e) {
                    throw new IllegalArgumentException(e);
                }
            }
        });
    }

    public static <E> void invoke(final Iterable<E> iterable, final String methodName) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        invoke(iterable, methodName, Collections.emptyList());
    }

    public static <E> List<Object> pluck(final List<E> list,
                                         final String propertyName) throws NoSuchFieldException, SecurityException {
        if (list.isEmpty()) {
            return Collections.emptyList();
        }
        final Field field = list.get(0).getClass().getField(propertyName);
        return _.map(list, new Function1<E, Object>() {
            @Override
            public Object apply(E input) {
                try {
                    return field.get(input);
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
        return _.map(set, new Function1<E, Object>() {
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
        final List<E> sortedList = new ArrayList<E>();
        _.each(list, new Block<E>() {
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
        final Map<K, List<E>> retVal = new HashMap<K, List<E>>();
        for (E e : iterable) {
            final K key = func.apply(e);
            List<E> val;
            if (retVal.containsKey(key)) {
                val = retVal.get(key);
            } else {
                val = new ArrayList<E>();
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
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                    return null;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        });
    }

    public static <K, E> Map<K, Integer> countBy(final Iterable<E> iterable, Function1<E, K> func) {
        final Map<K, Integer> retVal = new HashMap<K, Integer>();
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
        final List<E> shuffled = new ArrayList<E>(list.size());
        Collections.copy(list, shuffled);
        Collections.shuffle(shuffled);
        return shuffled;
    }

    public static <E> E sample(final List<E> list) {
        return list.get(new Random().nextInt(list.size()));
    }

    public static <E> Set<E> sample(final List<E> list, final int howMany) {
        final int size = Math.min(howMany, list.size());
        final Set<E> samples = new HashSet<E>(size);
        while (samples.size() < size) {
            E sample = sample(list);
            samples.add(sample);
        }
        return samples;
    }

    public static <E> E[] toArray(final Iterable<E> iterable) {
        final List<E> list = new ArrayList<E>();
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
        for (size = 0; iterator.hasNext(); size += 1) ;
        return size;
    }

    public static <E> E first(final Iterable<E> iterable) {
        return iterable.iterator().next();
    }

    public static <E> E first(final E[] array) {
        return array[0];
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

    public static <E> E last(final E[] array) {
        return array[array.length - 1];
    }

    public static <E> E last(final List<E> list) {
        return list.get(list.size() - 1);
    }

    public static <E> List<E> last(final List<E> list, final int n) {
        return list.subList(list.size() - n, list.size());
    }

    public static <E> List<E> rest(final List<E> list) {
        return rest(list, 1);
    }

    public static <E> List<E> rest(final List<E> list, int n) {
        return list.subList(n, list.size());
    }

    public static <E> List<E> tail(final List<E> list) {
        return rest(list);
    }

    public static <E> List<E> tail(final List<E> list, final int n) {
        return rest(list, n);
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

    public static <E> List<E> compact(final List<E> list, final E falsyValue) {
        return filter(list, new Predicate<E>() {
            @Override
            public Boolean apply(E arg) {
                return arg.equals(falsyValue);
            }
        });
    }

    public static <E> E[] compact(final E[] array, final E falsyValue) {
        return (E[]) compact(Arrays.asList(array), falsyValue).toArray();
    }

    public static <E> List<E> flatten(final List<?> list) {
        List<E> flattened = new ArrayList<E>();
        flatten(list, flattened);
        return flattened;
    }

    private static <E> void flatten(final List<?> fromTreeList, final List<E> toFlatList) {
        for (Object item : fromTreeList) {
            if (item instanceof List<?>) {
                flatten((List<?>) item, toFlatList);
            } else {
                toFlatList.add((E) item);
            }
        }
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
        return without(list, value);
    }

    public static <E> E[] without(final E[] array, final E... values) {
        return (E[]) without(Arrays.asList(array), values).toArray();
    }

    public static <E> E[] without(final E[] array, final E value) {
        return without(array, (E[]) Arrays.asList(value).toArray());
    }

    public static <E> List<E> union(final List<E> list1, final List<E> list2) {
        final Set<E> union = new HashSet<E>();
        union.addAll(list1);
        union.addAll(list2);
        return new ArrayList<E>(union);
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
        return new ArrayList<E>(new HashSet<E>(list));
    }

    public static <E> E[] uniq(final E[] array) {
        return (E[]) uniq(Arrays.asList(array)).toArray();
    }

    public static <T> List<List<T>> zip(final List<T>... lists) {
        final List<List<T>> zipped = new ArrayList<List<T>>();
        _.each(Arrays.asList(lists), new Block<List<T>>() {
            @Override
            public void apply(final List<T> list) {
                _.each(list, new Block<T>() {
                    int index = 0;

                    @Override
                    public void apply(T elem) {
                        final List<T> nTuple = index >= zipped.size() ? new ArrayList<T>() : zipped.get(index);
                        nTuple.add(elem);
                    }
                });
            }
        });
        return zipped;
    }

    public static <T> T[][] zip(final T[]... lists) {
        return (T[][]) zip(Arrays.asList(lists)).toArray();
    }

    public static <K, V> List<Tuple<K, V>> object(final List<K> keys, final List<V> values) {
        return _.map(keys, new Function1<K, Tuple<K, V>>() {
            int index = 0;

            @Override
            public Tuple<K, V> apply(K key) {
                return Tuple.create(key, values.get(index++));
            }
        });
    }

    public static <K, V> Tuple<K, V>[] object(final K[] keys, final V[] values) {
        return (Tuple<K, V>[]) object(Arrays.asList(keys), Arrays.asList(values)).toArray();
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
        }
        return -1;
    }

    public static <E extends Comparable<E>> int sortedIndex(final E[] array, final E value, final String propertyName) throws NoSuchFieldException, IllegalAccessException {
        return sortedIndex(Arrays.asList(array), value);
    }

    public static int[] range(int stop) {
        return range(0, stop, 1);
    }

    public static int[] range(int start, int stop) {
        return range(start, stop, 1);
    }

    public static int[] range(int start, int stop, int step) {
        int[] array = new int[stop - start];
        for (int index = 0; index < array.length; index += step) {
            array[index] = start + index;
        }
        return array;
    }

    public static <F1, T> Function<T> partial(final Function1<F1, T> func, final F1 value) {
        return new Function<T>() {
            @Override
            public T apply() {
                return func.apply(value);
            }
        };
    }

    public static <F2, F1, T> Function1<F1, T> partial(final Function2<F2, F1, T> func, final F2 value) {
        return new Function1<F1, T>() {
            @Override
            public T apply(F1 arg) {
                return func.apply(value, arg);
            }
        };
    }

    public static <F3, F2, F1, T> Function2<F2, F1, T> partial(final Function3<F3, F2, F1, T> func, final F3 value) {
        return new Function2<F2, F1, T>() {
            @Override
            public T apply(F2 arg1, F1 arg2) {
                return func.apply(value, arg1, arg2);
            }
        };
    }

    public static <F4, F3, F2, F1, T> Function3<F3, F2, F1, T> partial(final Function4<F4, F3, F2, F1, T> func, final F4 value) {
        return new Function3<F3, F2, F1, T>() {
            @Override
            public T apply(F3 arg1, F2 arg2, F1 arg3) {
                return func.apply(value, arg1, arg2, arg3);
            }
        };
    }

    public static <T> T partial(final Object function, final Class<T> unused, final Object... args) {
        try {
            if (args.length == 0) {
                throw new IllegalArgumentException("partial method must have at least one argument");
            }
            Object currentFunc = function;
            for (final Object arg : args) {
                final Method partialMethod = _.class.getMethod("partial",
                        new Class[]{currentFunc.getClass().getInterfaces()[0], Object.class});
                currentFunc = partialMethod.invoke(null, new Object[]{currentFunc, arg});
            }
            return (T) currentFunc;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
