package com.github.underscore;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class MemoizeFunction<F, T> implements Function<F, T> {
    private final Map<F, T> cache = new LinkedHashMap<>();
    public abstract T calc(final F n);

    public T apply(final F key) {
        final T value = calc(key);
        cache.putIfAbsent(key, value);
        return value;
    }
}
