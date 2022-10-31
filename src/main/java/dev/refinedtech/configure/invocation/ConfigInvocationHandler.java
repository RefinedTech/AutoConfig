package dev.refinedtech.configure.invocation;

import dev.refinedtech.configure.invocation.info.MethodInfo;
import dev.refinedtech.configure.storage.ConfigStorage;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public final class ConfigInvocationHandler implements InvocationHandler {

    private final ConfigStorage storage;
    private final Class<?> clazz;

    public ConfigInvocationHandler(ConfigStorage storage, Class<?> clazz) {
        this.storage = storage;
        this.clazz = clazz;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MethodInfo info = MethodInfo.from(method);
        switch (info.getType()) {

        }

        Object obj = this.storage.get(info.getFullPath());
        if (obj != null) {
            return obj;
        }

        try {
            return InvocationHandler.invokeDefault(proxy, method, args);
        } catch (IllegalAccessException exception) {
            throw new IllegalStateException(
                "ConfigAPI cannot access the default method '%s' in '%s'. Try to make '%s' public."
                .formatted(method.getName(), this.clazz.getCanonicalName(), this.clazz.getCanonicalName()));
        }
    }
}
