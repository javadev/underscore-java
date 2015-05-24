package com.github.underscore;

public interface Function4<F1, F2, F3, F4, T> {
    T apply(F1 arg1, F2 arg2, F3 arg3, F4 arg4);

    @Override
    boolean equals(Object object);
}
