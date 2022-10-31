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
            case GETTER -> {
                Object obj = this.storage.get(info.getFullPath());
                if (obj != null) {
                    return obj;
                }
            }
            case SETTER -> {
                if (args == null || args.length < 1) {
                    throw new IllegalStateException(
                        "Setter '%s' in '%s' requires at least one argument!".formatted(
                            method.getName(),
                            this.clazz.getCanonicalName()
                        )
                    );
                }

                this.storage.set(info.getFullPath(), args[0]);

                return null;
            }
        }

        if (method.getReturnType() == Void.class) {
            return null;
        }

        if (!method.isDefault()) {
            return null;
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
