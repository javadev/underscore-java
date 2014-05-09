package com.github.underscore;

public interface Function<T> {
    T apply();

    @Override
    boolean equals(Object object);
}
