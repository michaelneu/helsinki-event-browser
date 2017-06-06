package eu.michaeln.helsinkieventbrowser.entities;

import java.util.Locale;

public final class LocalizedString {
    private String fi, sv, en;

    public LocalizedString(String fi, String sv, String en) {
        this.fi = fi;
        this.sv = sv;
        this.en = en;
    }

    public LocalizedString() { }

    public String getFi() {
        return fi;
    }

    public String getSv() {
        return sv;
    }

    public String getEn() {
        return en;
    }

    private String coalesce(String a, String b) {
        if (a == null) {
            return b;
        } else {
            return a;
        }
    }

    public String resolve() {
        final Locale currentLocale = Locale.getDefault();
        final String language = currentLocale.getLanguage();

        return resolve(language);
    }

    public String resolve(String locale) {
        switch (locale) {
            case "fi":
                return coalesce(getFi(), coalesce(getEn(), getSv()));

            case "sv":
                return coalesce(getSv(), coalesce(getEn(), getFi()));

            default:
                return coalesce(getEn(), coalesce(getFi(), getSv()));
        }
    }

    public boolean containsInAnyLanguageWithoutCase(String text) {
        text = text.toLowerCase();

        boolean inFi = fi != null && fi.toLowerCase().contains(text),
                inSv = sv != null && sv.toLowerCase().contains(text),
                inEn = en != null && en.toLowerCase().contains(text);

        return inFi || inSv || inEn;
    }
}
