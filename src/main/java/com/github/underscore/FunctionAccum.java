package com.github.underscore;

public interface FunctionAccum<F1, F2> {
    F1 apply(F1 accum, F2 arg);

    @Override
    boolean equals(Object object);
}
