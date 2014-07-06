package com.github.underscore;

public class Tuple<A, B> {
    private final A first;
    private final B second;

    public static <A, B> Tuple<A, B> create(final A a, final B b) {
        return new Tuple<A, B>(a, b);
    }

    public Tuple(final A first, final B second) {
        super();
        this.first = first;
        this.second = second;
    }

    public A fst() {
        return first;
    }

    public B snd() {
        return second;
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }
}
