package eu.michaeln.helsinkieventbrowser.entities;

public final class Offer {
    private boolean is_free;
    private LocalizedString description, price, info_url;

    public boolean isFree() {
        return is_free;
    }

    public LocalizedString getDescription() {
        return description;
    }

    public LocalizedString getPrice() {
        return price;
    }

    public LocalizedString getInfoUrl() {
        return info_url;
    }
}
