package dev.refinedtech.configure.utils;

import java.util.function.UnaryOperator;

public class DefaultedOperator<T> implements UnaryOperator<T> {

    private T defaultValue;

    private DefaultedOperator(T defaultValue) {
        this.defaultValue = defaultValue;
    }

    public static <T> DefaultedOperator<T> createDefaulted(T defaultValue) {
        return new DefaultedOperator<>(defaultValue);
    }

    public static <T> DefaultedOperator<T> blank() {
        return new DefaultedOperator<>(null);
    }

    @Override
    public T apply(T o) {
        if (this.defaultValue == null) return o;

        return o == null ? this.defaultValue : o;
    }

    public void setDefaultValue(T defaultValue) {
        this.defaultValue = defaultValue;
    }
}
