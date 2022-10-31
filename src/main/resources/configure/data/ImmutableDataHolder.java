package dev.refinedtech.configure.data;

import java.util.HashMap;

public final class ImmutableDataHolder extends AbstractDataHolder {

    public ImmutableDataHolder(HashMap<String, Object> map) {
        map.forEach(this::setInternal);
    }

    public ImmutableDataHolder(AbstractDataHolder other) {
        other.entries(this::setInternal);
    }

    @Override
    public void set(String key, Object value) {
        throw new UnsupportedOperationException("ImmutableDataHolder cannot have it's contents changed! (Setting '%s' to '%s')".formatted(key, value));
    }

    @Override
    public int clear() {
        throw new UnsupportedOperationException("Cannot clear ImmutableDataHolder!");
    }

}
