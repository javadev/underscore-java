package com.github.underscore.examples;

public interface Function3<F1, F2, F3, T> {
    T apply(F1 arg1, F2 arg2, F3 arg3);

    @Override
    boolean equals(Object object);
}
