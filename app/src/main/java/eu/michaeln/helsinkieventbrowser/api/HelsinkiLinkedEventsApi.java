package eu.michaeln.helsinkieventbrowser.api;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.function.Consumer;

import eu.michaeln.helsinkieventbrowser.ErrorNotifier;
import eu.michaeln.helsinkieventbrowser.entities.AutoCompleteItem;
import eu.michaeln.helsinkieventbrowser.entities.Event;
import eu.michaeln.helsinkieventbrowser.entities.Keyword;
import eu.michaeln.helsinkieventbrowser.entities.LocalizedString;
import eu.michaeln.helsinkieventbrowser.entities.PaginatedResult;

public final class HelsinkiLinkedEventsApi extends Api {
    private static final String BASE_URL = "https://api.hel.fi/linkedevents/v1";
    private static final Gson JSON_DESERIALIZER = new Gson();

    private final Type paginatedEventsType, paginatedAutoCompleteItemsType, paginatedKeywordsType;

    public HelsinkiLinkedEventsApi(Context context, ErrorNotifier<VolleyError> errorNotifier) {
        super(context, errorNotifier, BASE_URL);

        paginatedEventsType = new TypeToken<PaginatedResult<Event>>(){ }.getType();
        paginatedAutoCompleteItemsType = new TypeToken<PaginatedResult<AutoCompleteItem>>(){ }.getType();
        paginatedKeywordsType = new TypeToken<PaginatedResult<Keyword>>(){ }.getType();
    }

    public void getEvents(@NonNull final Consumer<PaginatedResult<Event>> eventConsumer) {
        call("/event?include=location", new CheckedConsumer<String, JSONException>() {
            @Override
            public void accept(String response) throws JSONException {
                final PaginatedResult<Event> result = JSON_DESERIALIZER.fromJson(response, paginatedEventsType);

                eventConsumer.accept(result);
            }
        });
    }

    public void autoCompleteEvents(String query, @NonNull final Consumer<AutoCompleteItem[]> itemsConsumer) {
        call("/search/?type=event&input=" + query, new CheckedConsumer<String, JSONException>() {
            @Override
            public void accept(String response) throws JSONException {
                final PaginatedResult<AutoCompleteItem> items = JSON_DESERIALIZER.fromJson(response, paginatedAutoCompleteItemsType);

                itemsConsumer.accept(items.getData());
            }
        });
    }

    public void autoCompletePlaces(String query, @NonNull final Consumer<AutoCompleteItem[]> itemsConsumer) {
        call("/search/?type=place&input=" + query, new CheckedConsumer<String, JSONException>() {
            @Override
            public void accept(String response) throws JSONException {
                final PaginatedResult<AutoCompleteItem> items = JSON_DESERIALIZER.fromJson(response, paginatedAutoCompleteItemsType);

                itemsConsumer.accept(items.getData());
            }
        });
    }

    public void autoCompleteKeywords(String query, @NonNull final Consumer<AutoCompleteItem[]> itemsConsumer) {
        call("/keyword/?text=" + query, new CheckedConsumer<String, JSONException>() {
            @Override
            public void accept(String response) throws JSONException {
                final PaginatedResult<Keyword> items = JSON_DESERIALIZER.fromJson(response, paginatedKeywordsType);

                ArrayList<AutoCompleteItem> autoCompleteItems = new ArrayList<>();

                for (Keyword keyword : items.getData()) {
                    for (String alternative : keyword.getAlternativeLabels()) {
                        final String id = keyword.getId();
                        final LocalizedString text = new LocalizedString(alternative, alternative, alternative);

                        autoCompleteItems.add(new AutoCompleteItem(id, text));
                    }
                }

                AutoCompleteItem[] autoCompleteItemsAsArray = autoCompleteItems.toArray(new AutoCompleteItem[autoCompleteItems.size()]);
                itemsConsumer.accept(autoCompleteItemsAsArray);
            }
        });
    }
}
