package eu.michaeln.helsinkieventbrowser.entities;

import com.google.gson.annotations.SerializedName;

public final class AutoCompleteItem {
    private String id;

    @SerializedName("name")
    private LocalizedString text;

    public AutoCompleteItem(String id, LocalizedString text) {
        this.id = id;
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public LocalizedString getText() {
        return text;
    }
}
