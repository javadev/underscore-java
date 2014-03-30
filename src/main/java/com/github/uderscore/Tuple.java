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
    public int hashCode() {
        int hashFirst = first != null ? first.hashCode() : 0;
        int hashSecond = second != null ? second.hashCode() : 0;

        return (hashFirst + hashSecond) * hashSecond + hashFirst;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Tuple) {
            Tuple otherPair = (Tuple) other;
            return ((this.first == otherPair.first || (this.first != null
                    && otherPair.first != null && this.first
                        .equals(otherPair.first))) && (this.second == otherPair.second || (this.second != null
                    && otherPair.second != null && this.second
                        .equals(otherPair.second))));
        }

        return false;
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }
}
