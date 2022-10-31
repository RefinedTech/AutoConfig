package dev.refinedtech.configure.storage;

import java.io.File;

public abstract class ConfigStorage {

    private final File file;

    protected ConfigStorage(File file) {
        this.file = file;
    }

    public abstract Object get(String path);

    public abstract void set(String path, Object obj);

    protected final File getFile() {
        return this.file;
    }
}
