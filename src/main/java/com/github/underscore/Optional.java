package com.github.underscore;

public final class Optional<T> {
    private final T arg;
    private final boolean absent;

    private Optional() {
        this.arg = null;
        this.absent = true;
    }

    private Optional(T arg) {
        this.arg = arg;
        this.absent = false;
    }

    public static <T> Optional<T> of(T arg) {
        return new Optional<T>(arg);
    }

    public static <T> Optional<T> absent() {
        return new Optional<T>();
    }

    public T get() {
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
        if (arg == null ? optional.arg != null : !arg.equals(optional.arg)) {
            return false;
        }
        return true;
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
