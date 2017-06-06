package eu.michaeln.helsinkieventbrowser.entities;

import java.util.List;

public final class PaginatedResult<T> {
    private T[] data;
    private MetaInformation meta;

    public PaginatedResult() { }

    public PaginatedResult(T[] data, MetaInformation meta) {
        this.data = data;
        this.meta = meta;
    }

    public T[] getData() {
        return data;
    }

    public MetaInformation getMeta() {
        return meta;
    }
}
