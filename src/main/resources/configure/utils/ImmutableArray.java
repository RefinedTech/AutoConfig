package dev.refinedtech.configure.utils;

import java.util.function.Supplier;

public class ImmutableArray<T> {

    private final T[] arr;
    private Supplier<T> fallback;

    private ImmutableArray(T[] arr) {
        this.arr = arr;
    }

    public static <T> ImmutableArray<T> wrap(final T... arr) {
        return new ImmutableArray<>(arr.clone());
    }

    public ImmutableArray<T> setFallback(final Supplier<T> fallback) {
        this.fallback = fallback;
        if (this.fallback == null) {
            this.fallback = () -> null;
        }
        return this;
    }

    public T get(int index) {
        if (index < 0 || index >= this.arr.length) {
            return this.fallback.get();
        }

        T val = this.arr[index];

        if (val == null) {
            return this.fallback.get();
        }

        return val;
    }

    public T getLast() {
        return this.arr.length == 0 ? this.fallback.get() : this.arr[this.arr.length - 1];
    }

    public int length() {
        return this.arr.length;
    }

    public T[] getArray() {
        return this.arr;
    }
}
