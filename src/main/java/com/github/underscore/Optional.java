package com.github.underscore;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class Optional<T> {
    private static final Optional<?> EMPTY = new Optional<>();
    private final T arg;
    private final boolean empty;

    private Optional() {
        this.arg = null;
        this.empty = true;
    }

    private Optional(final T arg) {
        this.arg = arg;
        this.empty = false;
    }

    public static <T> Optional<T> of(final T arg) {
        return new Optional<>(arg);
    }

    public static <T> Optional<T> fromNullable(final T nullableReference) {
        return nullableReference == null ? Optional.<T>empty()
            : new Optional<>(nullableReference);
    }

    @SuppressWarnings("unchecked")
    public static <T> Optional<T> empty() {
        return (Optional<T>) EMPTY;
    }

    public T get() {
        if (empty) {
            throw new IllegalStateException("Optional.get() cannot be called on an empty value");
        }
        return arg;
    }

    public T or(final T defaultValue) {
        if (empty) {
            return defaultValue;
        }
        return arg;
    }

    public T orNull() {
        if (empty) {
            return null;
        }
        return arg;
    }

    public boolean isEmpty() {
        return empty;
    }

    public boolean isPresent() {
        return !empty;
    }

    @SuppressWarnings("unchecked")
    public Optional<T> filter(Predicate<? super T> predicate) {
        U.checkNotNull(predicate);
        if (isPresent()) {
            return predicate.test(arg) ? this : Optional.<T>empty();
        } else {
            return this;
        }
    }

    public <F> Optional<F> map(Function<? super T, F> mapper) {
        U.checkNotNull(mapper);
        if (isPresent()) {
            return Optional.fromNullable(mapper.apply(arg));
        } else {
            return empty();
        }
    }

    public <X extends Throwable> T orThrow(Supplier<? extends X> exceptionFunction) throws X {
        if (empty) {
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

        final Optional<?> optional = (Optional) o;

        return empty == optional.empty && Objects.equals(arg, optional.arg);
    }

    @Override
    public int hashCode() {
        int result = arg == null ? 0 : arg.hashCode();
        result = 31 * result + (empty ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return empty ? "Optional.empty" : "Optional[" + arg + "]";
    }
}
