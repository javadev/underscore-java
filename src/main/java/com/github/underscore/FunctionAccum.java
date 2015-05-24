package com.github.underscore;

public interface FunctionAccum<A, F> {
    A apply(A accum, F arg);

    @Override
    boolean equals(Object object);
}
