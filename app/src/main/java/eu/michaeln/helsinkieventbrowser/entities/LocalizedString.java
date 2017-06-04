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

    public String resolve() {
        final Locale currentLocale = Locale.getDefault();
        final String language = currentLocale.getLanguage();

        return resolve(language);
    }

    public String resolve(String locale) {
        switch (locale) {
            case "fi":
                return getFi();

            case "sv":
                return getSv();

            default:
                return getEn();
        }
    }
}
