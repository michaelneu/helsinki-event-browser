package eu.michaeln.helsinkieventbrowser.entities;

import java.util.Date;
import java.util.List;

public final class Event {
    private String id;

    private LocalizedString name;
    private LocalizedString description;
    private LocalizedString infoUrl;

    private Location location;
    private Offer[] offers;

    private Date start_time, end_time;

    private Keyword[] keywords;

    public String getId() {
        return id;
    }

    public LocalizedString getName() {
        return name;
    }

    public LocalizedString getDescription() {
        return description;
    }

    public LocalizedString getInfoUrl() {
        return infoUrl;
    }

    public Location getLocation() {
        return location;
    }

    public Offer[] getOffers() {
        return offers;
    }

    public Date getStartTime() {
        return start_time;
    }

    public Date getEndTime() {
        return end_time;
    }

    public Keyword[] getKeywords() {
        return keywords;
    }
}
