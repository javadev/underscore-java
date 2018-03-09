package com.github.underscore.examples;

public interface Function<F, T> {
    T apply(F arg);

    @Override
    boolean equals(Object object);
}
