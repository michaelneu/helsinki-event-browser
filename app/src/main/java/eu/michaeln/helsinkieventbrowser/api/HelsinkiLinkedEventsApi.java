package eu.michaeln.helsinkieventbrowser.api;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

import eu.michaeln.helsinkieventbrowser.ErrorNotifier;
import eu.michaeln.helsinkieventbrowser.entities.AutoCompleteItem;
import eu.michaeln.helsinkieventbrowser.entities.Event;
import eu.michaeln.helsinkieventbrowser.entities.PaginatedResult;

public final class HelsinkiLinkedEventsApi extends Api {
    private static final String BASE_URL = "https://api.hel.fi/linkedevents/v1";
    private static final Gson JSON_DESERIALIZER = new Gson();

    private final Type paginatedEventsType, paginatedAutoCompleteItemsType;

    public HelsinkiLinkedEventsApi(Context context, ErrorNotifier<VolleyError> errorNotifier) {
        super(context, errorNotifier, BASE_URL);

        paginatedEventsType = new TypeToken<PaginatedResult<Event>>(){ }.getType();
        paginatedAutoCompleteItemsType = new TypeToken<PaginatedResult<AutoCompleteItem>>(){ }.getType();
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
}
