package com.github.underscore;

public interface Supplier<T> {
    T get();

    @Override
    boolean equals(Object object);
}
