package com.github.underscore;

import java.util.List;
import java.util.function.Function;

public interface Template<T> extends Function<T, String> {
    List<String> check(T arg);
}
