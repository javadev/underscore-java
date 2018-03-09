package com.github.underscore.examples;

public interface Predicate<T> {
    boolean test(T arg);

    @Override
    boolean equals(Object object);
}
