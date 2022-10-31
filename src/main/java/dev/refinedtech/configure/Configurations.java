package dev.refinedtech.configure;

import dev.refinedtech.configure.invocation.ConfigInvocationHandler;
import dev.refinedtech.configure.storage.ConfigStorage;

import java.io.File;
import java.lang.reflect.Proxy;
import java.nio.file.Path;
import java.util.function.Function;

@SuppressWarnings("unused")
public final class Configurations {

    private Configurations() {

    }

    @SuppressWarnings("unchecked")
    public static <T> T create(Class<T> type, File path, Function<File, ConfigStorage> storage) {
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{type}, new ConfigInvocationHandler(storage.apply(path), type));
    }

    public static <T> T create(Class<T> type, Path path, Function<File, ConfigStorage> storage) {
        return Configurations.create(type, path.toFile(), storage);
    }

    public static <T> T create(Class<T> type, String path, Function<File, ConfigStorage> storage) {
        return Configurations.create(type, new File(path), storage);
    }

}
