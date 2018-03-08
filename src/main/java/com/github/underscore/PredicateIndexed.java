package com.github.underscore;

public interface PredicateIndexed<T> {
    boolean test(int index, T arg);

    @Override
    boolean equals(Object object);
}
