package com.github.underscore;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class MemoizeFunction<F, T> implements Function<F, T> {
    private final Map<F, T> cache = new LinkedHashMap<F, T>();
    public abstract T calc(final F n);

    public T apply(final F key) {
        if (!cache.containsKey(key)) {
            cache.put(key, calc(key));
        }
        return cache.get(key);
    }
}
