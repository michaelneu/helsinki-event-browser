package eu.michaeln.helsinkieventbrowser.entities;

public final class Location {
    private String id;
    private Position position;
    private LocalizedString name, street_address, address_locality, telephone;

    public String getId() {
        return id;
    }

    public Position getPosition() {
        return position;
    }

    public LocalizedString getName() {
        return name;
    }

    public LocalizedString getStreetAddress() {
        return street_address;
    }

    public LocalizedString getAddressLocality() {
        return address_locality;
    }

    public LocalizedString getTelephone() {
        return telephone;
    }
}
