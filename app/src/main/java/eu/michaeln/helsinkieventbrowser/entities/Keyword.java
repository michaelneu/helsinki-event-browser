package eu.michaeln.helsinkieventbrowser.entities;

public class Keyword {
    private String id;
    private String[] alt_labels;
    private LocalizedString name;

    public String getId() {
        return id;
    }

    public String[] getAlternativeLabels() {
        return alt_labels;
    }

    public LocalizedString getName() {
        return name;
    }
}
