package com.github.underscore;

public class Optional<T> {
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
    public boolean equals(Object obj) {
        if (!(obj instanceof Optional)) {
            return false;
        }
        return (((Optional) obj).absent && absent)
            || (!((Optional) obj).absent && !absent
                && (arg == null ? ((Optional) obj).arg == null : arg.equals(((Optional) obj).arg)));
    }

    @Override
    public int hashCode() {
        return arg == null ? 0 : arg.hashCode();
    }

    @Override
    public String toString() {
        return absent ? "Optional.absent()" : "Optional.of(" + arg + ")";
    }
}
