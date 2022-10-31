package dev.refinedtech.configure.handlers;

import dev.refinedtech.configure.Configuration;
import dev.refinedtech.configure.data.AbstractDataHolder;
import dev.refinedtech.configure.data.ImmutableDataHolder;
import dev.refinedtech.configure.utils.DefaultedOperator;
import dev.refinedtech.configure.utils.ImmutableArray;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Optional;

public class ConfigurationGetHandler extends ConfigurationHandler {

    protected ConfigurationGetHandler(Configuration parentConfig) {
        super(parentConfig);
    }

    @Override
    protected Object process(final Object proxy, final Method method, final ImmutableArray<Object> args) throws Throwable {
        final Object last = args.getLast();
        final Class<?> lastClass = last.getClass();

        final DefaultedOperator<Object> operator = DefaultedOperator.blank();

        if (lastClass == method.getReturnType()) {
            operator.setDefaultValue(last);
        }

        if (lastClass.isArray() && lastClass.getComponentType() == method.getReturnType()) {
            final int length = this.getArrayLength(last);
            if (length > 0) {
                final Object[] arr = this.toArray(last, length);
                operator.setDefaultValue(arr[0]);
            }
        }

        final Optional<Object> defaultResult = this.tryInvokeDefault(proxy, method, args);

        final String variableName = this.toCamelCase(method.getName());
        final String optionPath = String.join(this.getParentConfig().getPathJoiner(), this.getPath(method.getDeclaringClass()), variableName);

        final Object configValue = this.getParentConfig().getDataHolder().get(optionPath);

        return Optional.ofNullable(operator.apply(configValue)).orElse(defaultResult.orElse(null));
    }

    interface Elimination {
        default boolean getEnvironmentStealsHearts(boolean... def) {
            return false;
        }
    }

    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Configuration configuration = new Configuration();
        configuration.setPathJoiner(".");
        configuration.useDataHolder(AbstractDataHolder.class);

        ConfigurationGetHandler getHandler = new ConfigurationGetHandler(configuration);
        Elimination elimination = (Elimination) Proxy.newProxyInstance(Thread.currentThread()
                                     .getContextClassLoader(), new Class[]{Elimination.class}, getHandler::invoke);

        System.out.println(elimination.getEnvironmentStealsHearts());
    }
}
