package dev.refinedtech.configure;

import dev.refinedtech.configure.data.AbstractDataHolder;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public final class Configuration {

    private String pathJoiner;

    private AbstractDataHolder dataHolder;

    public Configuration() {
        this.pathJoiner = ".";
    }

    public void setPathJoiner(String pathJoiner) {
        this.pathJoiner = pathJoiner == null || pathJoiner.isBlank() ? "." : pathJoiner;
    }

    public void useDataHolder(Class<? extends AbstractDataHolder> clazz) throws NoSuchMethodException,
                                                                                InvocationTargetException,
                                                                                InstantiationException,
                                                                                IllegalAccessException {
        Constructor<? extends AbstractDataHolder> constructor = clazz.getConstructor();
        this.dataHolder = constructor.newInstance();
    }

    public String getPathJoiner() {
        return this.pathJoiner;
    }

    public AbstractDataHolder getDataHolder() {
        return this.dataHolder;
    }
}