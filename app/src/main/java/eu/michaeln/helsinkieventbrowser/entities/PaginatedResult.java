package eu.michaeln.helsinkieventbrowser.entities;

import java.util.List;

public final class PaginatedResult<T> {
    private T[] data;
    private MetaInformation meta;

    public T[] getData() {
        return data;
    }

    public MetaInformation getMeta() {
        return meta;
    }
}
