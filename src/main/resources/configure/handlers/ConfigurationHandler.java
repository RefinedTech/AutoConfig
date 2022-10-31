package dev.refinedtech.configure.handlers;

import dev.refinedtech.configure.Configuration;
import dev.refinedtech.configure.data.AbstractDataHolder;
import dev.refinedtech.configure.utils.ImmutableArray;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Optional;

@SuppressWarnings("unused")
public abstract class ConfigurationHandler {


    private final Configuration parentConfig;

    protected ConfigurationHandler(Configuration parentConfig) {
        this.parentConfig = parentConfig;
    }

    public final Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return this.process(proxy, method, ImmutableArray.wrap(args).setFallback(Object::new));
    }

    protected abstract Object process(Object proxy, Method method, ImmutableArray<Object> args) throws Throwable;

    protected Configuration getParentConfig() {
        return this.parentConfig;
    }

    protected final String toCamelCase(String name) {
        return Character.toLowerCase(name.charAt(0)) + name.substring(1);
    }

    protected final Optional<Object> tryInvokeDefault(Object proxy, Method method, ImmutableArray<Object> args) throws Throwable {
        if (method.isDefault()) {
            return Optional.ofNullable(InvocationHandler.invokeDefault(proxy, method, args.getArray()));
        }

        return Optional.empty();
    }

    protected final String getPath(final Class<?> clazz) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(this.toCamelCase(clazz.getSimpleName()));

        Class<?> currentClass = clazz;
        while ((currentClass = currentClass.getEnclosingClass()) != null) {
            stringBuilder.insert(0, this.getParentConfig().getPathJoiner());
            stringBuilder.insert(0, this.toCamelCase(currentClass.getSimpleName()));
        }

        return stringBuilder.toString();
    }

    protected Object[] toArray(Object obj, int length) {
        if (obj instanceof Object[] array)
            return array;

        Object[] out = new Object[length];
        for (int i = 0; i < out.length; i++) {
            out[i] = Array.get(obj, i);
        }

        return out;
    }

    protected int getArrayLength(Object obj) {
        if (obj == null)
            return -1;

        Class<?> clazz = obj.getClass();
        if (!clazz.isArray())
            return -1;

        return Array.getLength(obj);
    }
}