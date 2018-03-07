package com.github.underscore;

import java.util.List;

public interface Template<T> extends Function<T, String> {
    List<String> check(T arg);
}
