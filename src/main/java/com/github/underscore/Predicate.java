package com.github.underscore;

public interface Predicate<T> {
    boolean test(T arg);

    @Override
    boolean equals(Object object);
}
