package eu.michaeln.helsinkieventbrowser.entities;

import java.util.Locale;

public final class LocalizedString {
    private String fi, sv, en;

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
}
