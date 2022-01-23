/*
 * The MIT License (MIT)
 *
 * Copyright 2015-2022 Valentyn Kolesnikov
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.github.underscore;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class Optional<T> {
    private static final Optional<?> EMPTY = new Optional<>();
    private final T arg;
    private final boolean absent;

    private Optional() {
        this.arg = null;
        this.absent = true;
    }

    private Optional(final T arg) {
        this.arg = arg;
        this.absent = false;
    }

    public static <T> Optional<T> of(final T arg) {
        return new Optional<>(arg);
    }

    public static <T> Optional<T> fromNullable(final T nullableReference) {
        return nullableReference == null ? Optional.empty() : new Optional<>(nullableReference);
    }

    @SuppressWarnings("unchecked")
    public static <T> Optional<T> empty() {
        return (Optional<T>) EMPTY;
    }

    public T get() {
        if (absent) {
            throw new IllegalStateException("Optional.get() cannot be called on an empty value");
        }
        return arg;
    }

    public T or(final T defaultValue) {
        if (absent) {
            return defaultValue;
        }
        return arg;
    }

    public T orNull() {
        if (absent) {
            return null;
        }
        return arg;
    }

    public boolean isEmpty() {
        return absent;
    }

    public boolean isPresent() {
        return !absent;
    }

    public Optional<T> filter(Predicate<? super T> predicate) {
        Underscore.checkNotNull(predicate);
        if (isPresent()) {
            return predicate.test(arg) ? this : Optional.empty();
        } else {
            return this;
        }
    }

    public <F> Optional<F> map(Function<? super T, F> mapper) {
        Underscore.checkNotNull(mapper);
        if (isPresent()) {
            return Optional.fromNullable(mapper.apply(arg));
        } else {
            return empty();
        }
    }

    public <X extends Throwable> T orThrow(Supplier<? extends X> exceptionFunction) throws X {
        if (absent) {
            throw exceptionFunction.get();
        } else {
            return arg;
        }
    }

    public java.util.Optional<T> toJavaOptional() {
        return java.util.Optional.ofNullable(arg);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Optional<?> optional = (Optional<?>) o;

        return absent == optional.absent && Objects.equals(arg, optional.arg);
    }

    @Override
    public int hashCode() {
        int result = arg == null ? 0 : arg.hashCode();
        result = 31 * result + (absent ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return absent ? "Optional.empty" : "Optional[" + arg + "]";
    }
}
