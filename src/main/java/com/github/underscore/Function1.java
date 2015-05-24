package com.github.underscore;

public interface Function1<F, T> {
    T apply(F arg);

    @Override
    boolean equals(Object object);
}
