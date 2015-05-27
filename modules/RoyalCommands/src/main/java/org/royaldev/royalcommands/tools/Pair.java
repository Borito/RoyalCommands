/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.royaldev.royalcommands.tools;

/**
 * A pair of items.
 *
 * @param <T> The type of the first item
 * @param <U> The type of the second item
 */
public class Pair<T, U> {

    private final T first;
    private final U second;

    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Checks to see if this Pair is equal to another. Two Pairs are only equal when the first objects equal each other
     * and the second objects equal each other using <code>equals()</code>.
     *
     * @param o Object to test equality of
     * @return true if equal, false if otherwise
     */
    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Pair)) return false;
        final Pair<?, ?> other = (Pair<?, ?>) o;
        return this.getFirst().equals(other.getFirst()) && this.getSecond().equals(other.getSecond());
    }

    @Override
    public String toString() {
        return String.format("Pair<%s, %s>", this.getFirst().toString(), this.getSecond().toString());
    }

    /**
     * Gets the first object in the pair.
     *
     * @return First
     */
    public T getFirst() {
        return this.first;
    }

    /**
     * Gets the second object in the pair.
     *
     * @return Second
     */
    public U getSecond() {
        return this.second;
    }
}
