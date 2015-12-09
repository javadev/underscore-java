package com.github.underscore.examples;

public final class Optional<T> {
    private static final Optional<?> EMPTY = new Optional();
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
        return new Optional<T>(arg);
    }

    public static <T> Optional<T> fromNullable(final T nullableReference) {
        return nullableReference == null ? Optional.<T>absent()
            : new Optional<T>(nullableReference);
    }

    @SuppressWarnings("unchecked")
    public static<T> Optional<T> absent() {
        return (Optional<T>) EMPTY;
    }

    public T get() {
        if (absent) {
            throw new IllegalStateException("Optional.get() cannot be called on an absent value");
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

    public boolean isPresent() {
        return !absent;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Optional optional = (Optional) o;

        if (absent != optional.absent) {
            return false;
        }
        return !(arg == null ? optional.arg != null : !arg.equals(optional.arg));
    }

    @Override
    public int hashCode() {
        int result = arg == null ? 0 : arg.hashCode();
        result = 31 * result + (absent ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return absent ? "Optional.absent()" : "Optional.of(" + arg + ")";
    }
}
