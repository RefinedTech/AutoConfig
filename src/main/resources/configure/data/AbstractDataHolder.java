package dev.refinedtech.configure.data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;

public class AbstractDataHolder {

    private final HashMap<String, Object> values = new HashMap<>();

    public void set(String key, Object value) {
        this.setInternal(key, value);
    }

    public Object get(String key) {
        return this.values.get(key);
    }

    public Set<String> keys() {
        return new HashSet<>(this.values.keySet());
    }

    public void entries(BiConsumer<String, Object> consumer) {
        this.values.forEach(consumer);
    }

    public int clear() {
        int size = this.values.size();
        this.values.clear();
        return size;
    }


    protected void setInternal(String key, Object value) {
        this.values.put(key, value);
    }

}
