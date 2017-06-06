package eu.michaeln.helsinkieventbrowser.entities;

import com.google.gson.annotations.SerializedName;

public final class AutoCompleteItem {
    @SerializedName("name")
    private LocalizedString text;

    public AutoCompleteItem(LocalizedString text) {
        this.text = text;
    }

    public LocalizedString getText() {
        return text;
    }
}
