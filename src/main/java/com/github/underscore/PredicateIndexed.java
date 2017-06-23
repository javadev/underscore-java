package com.github.underscore;

public interface PredicateIndexed<T> {
    boolean apply(int index, T arg);

    @Override
    boolean equals(Object object);
}
