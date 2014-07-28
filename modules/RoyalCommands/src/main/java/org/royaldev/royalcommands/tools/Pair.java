package org.royaldev.royalcommands.tools;

public class Pair<T, U> {
    private final T first;
    private final U second;

    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return this.first;
    }

    public U getSecond() {
        return this.second;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Pair)) return false;
        final Pair<?, ?> other = (Pair<?, ?>) o;
        return this.getFirst().equals(other.getFirst()) && this.getSecond().equals(other.getSecond());
    }

    @Override
    public String toString() {
        return String.format("Pair<%s, %s>", this.getFirst().toString(), this.getSecond().toString());
    }
}
