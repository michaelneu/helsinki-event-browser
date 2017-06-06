package eu.michaeln.helsinkieventbrowser.api;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import java.util.Stack;
import java.util.function.Consumer;

import eu.michaeln.helsinkieventbrowser.ErrorNotifier;
import eu.michaeln.helsinkieventbrowser.entities.AutoCompleteItem;
import eu.michaeln.helsinkieventbrowser.entities.Event;
import eu.michaeln.helsinkieventbrowser.entities.Keyword;
import eu.michaeln.helsinkieventbrowser.entities.LocalizedString;
import eu.michaeln.helsinkieventbrowser.entities.Location;
import eu.michaeln.helsinkieventbrowser.entities.MetaInformation;
import eu.michaeln.helsinkieventbrowser.entities.PaginatedResult;

public final class HelsinkiLinkedEventsApi extends Api {
    private static final String BASE_URL = "https://api.hel.fi/linkedevents/v1";
    private static final Gson JSON_DESERIALIZER = new Gson();

    private final Type paginatedEventsType, paginatedLocationsType, paginatedKeywordsType;

    public HelsinkiLinkedEventsApi(Context context, ErrorNotifier<VolleyError> errorNotifier) {
        super(context, errorNotifier, BASE_URL);

        paginatedEventsType = new TypeToken<PaginatedResult<Event>>(){ }.getType();
        paginatedLocationsType = new TypeToken<PaginatedResult<Location>>(){ }.getType();
        paginatedKeywordsType = new TypeToken<PaginatedResult<Keyword>>(){ }.getType();
    }

    public void getEvents(@NonNull final Consumer<PaginatedResult<Event>> eventConsumer) {
        call("/event?include=location,keywords", new CheckedConsumer<String, JSONException>() {
            @Override
            public void accept(String response) throws JSONException {
                final PaginatedResult<Event> result = JSON_DESERIALIZER.fromJson(response, paginatedEventsType);

                eventConsumer.accept(result);
            }
        });
    }

    private void searchPlaces(String query, @NonNull final Consumer<PaginatedResult<Location>> locationConsumer) {
        if (query.length() == 0) {
            locationConsumer.accept(new PaginatedResult<Location>(new Location[0], new MetaInformation()));
        } else {
            call("/search/?type=place&input=" + query, new CheckedConsumer<String, JSONException>() {
                @Override
                public void accept(String response) throws JSONException {
                    final PaginatedResult<Location> items = JSON_DESERIALIZER.fromJson(response, paginatedLocationsType);

                    locationConsumer.accept(items);
                }
            });
        }
    }

    public void autoCompletePlaces(String query, @NonNull final Consumer<AutoCompleteItem[]> itemsConsumer) {
        searchPlaces(query, new Consumer<PaginatedResult<Location>>() {
            @Override
            public void accept(PaginatedResult<Location> locationPaginatedResult) {
                final Location[] locations = locationPaginatedResult.getData();
                final AutoCompleteItem[] items = new AutoCompleteItem[locations.length];

                for (int i = 0; i < locations.length; i++) {
                    final LocalizedString name = locations[i].getName();

                    items[i] = new AutoCompleteItem(name);
                }

                itemsConsumer.accept(items);
            }
        });
    }

    private void searchKeywords(String query, @NonNull final Consumer<PaginatedResult<Keyword>> keywordConsumer) {
        if (query.length() == 0) {
            keywordConsumer.accept(new PaginatedResult<Keyword>(new Keyword[0], new MetaInformation()));
        } else {
            query = Uri.encode(query);

            call("/keyword?text=" + query, new CheckedConsumer<String, JSONException>() {
                @Override
                public void accept(String response) throws JSONException {
                    final PaginatedResult<Keyword> keywords = JSON_DESERIALIZER.fromJson(response, paginatedKeywordsType);

                    keywordConsumer.accept(keywords);
                }
            });
        }
    }

    private void searchAllKeywords(final Stack<String> keywords, final ArrayList<Keyword> foundKeywords, @NonNull final Consumer<Keyword[]> keywordConsumer) {
        if (keywords.size() == 0) {
            final Keyword[] foundKeywordsAsArray = foundKeywords.toArray(new Keyword[foundKeywords.size()]);

            keywordConsumer.accept(foundKeywordsAsArray);
        } else {
            String keyword = keywords.pop();

            searchKeywords(keyword, new Consumer<PaginatedResult<Keyword>>() {
                @Override
                public void accept(PaginatedResult<Keyword> keywordPaginatedResult) {
                    final Keyword[] results = keywordPaginatedResult.getData();

                    for (Keyword keyword : results) {
                        if (!foundKeywords.contains(keyword)) {
                            foundKeywords.add(keyword);
                        }
                    }

                    searchAllKeywords(keywords, foundKeywords, keywordConsumer);
                }
            });
        }
    }

    private void searchAllKeywords(String query, @NonNull final Consumer<Keyword[]> keywordConsumer) {
        final String[] keywords = query.split(" ");
        final Stack<String> keywordsAsStack = new Stack<>();

        for (String keyword : keywords) {
            keywordsAsStack.push(keyword);
        }

        searchAllKeywords(keywordsAsStack, new ArrayList<Keyword>(), keywordConsumer);
    }

    public void autoCompleteKeywords(String query, @NonNull final Consumer<AutoCompleteItem[]> itemsConsumer) {
        searchAllKeywords(query, new Consumer<Keyword[]>() {
            @Override
            public void accept(Keyword[] keywords) {
                ArrayList<AutoCompleteItem> autoCompleteItems = new ArrayList<>();

                for (Keyword keyword : keywords) {
                    autoCompleteItems.add(new AutoCompleteItem(keyword.getName()));

                    for (String alternative : keyword.getAlternativeLabels()) {
                        final LocalizedString text = new LocalizedString(alternative, alternative, alternative);

                        autoCompleteItems.add(new AutoCompleteItem(text));
                    }
                }

                AutoCompleteItem[] autoCompleteItemsAsArray = autoCompleteItems.toArray(new AutoCompleteItem[autoCompleteItems.size()]);
                itemsConsumer.accept(autoCompleteItemsAsArray);
            }
        });
    }

    public void searchEvents(final Calendar dateQuery, final String placeQuery, final String keywordQuery, @NonNull final Consumer<PaginatedResult<Event>> eventConsumer) {
        searchPlaces(placeQuery, new Consumer<PaginatedResult<Location>>() {
            @Override
            public void accept(PaginatedResult<Location> paginatedLocations) {
                final Location[] locations = paginatedLocations.getData();
                final String[] locationIds = new String[locations.length];

                for (int i = 0; i < locations.length; i++) {
                    locationIds[i] = locations[i].getId();
                }

                final String locationIdsAsString = TextUtils.join(",", locationIds);

                searchAllKeywords(keywordQuery, new Consumer<Keyword[]>() {
                    @Override
                    public void accept(Keyword[] keywords) {
                        final String[] keywordIds = new String[keywords.length];

                        for (int i = 0; i < keywords.length; i++) {
                            keywordIds[i] = keywords[i].getId();
                        }

                        final String keywordIdsAsString = TextUtils.join(",", keywordIds);

                        final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                        final String date = dateFormatter.format(dateQuery.getTime());

                        call("/event?include=location,keywords&location=" + locationIdsAsString + "&keyword=" + keywordIdsAsString + "&start=" + date, new CheckedConsumer<String, JSONException>() {
                            @Override
                            public void accept(String response) throws JSONException {
                                final PaginatedResult<Event> result = JSON_DESERIALIZER.fromJson(response, paginatedEventsType);

                                eventConsumer.accept(result);
                            }
                        });
                    }
                });
            }
        });
    }
}
