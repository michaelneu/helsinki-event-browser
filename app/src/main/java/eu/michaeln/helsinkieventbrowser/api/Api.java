package eu.michaeln.helsinkieventbrowser.api;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import eu.michaeln.helsinkieventbrowser.ErrorNotifier;

public abstract class Api {
    private final VolleySingleton server;
    private final ErrorNotifier<VolleyError> notifier;

    public Api(Context context, ErrorNotifier<VolleyError> errorNotifier) {
        server = VolleySingleton.getInstance(context);
        notifier = errorNotifier;
    }

    private void notifyError(JSONException error) {
        notifyError(new ParseError(error));
    }

    private void notifyError(VolleyError error) {
        if (notifier != null) {
            notifier.notify(error);
        }
    }

    protected void call(String endpoint, @NonNull final CheckedConsumer<String, JSONException> responseConsumer) {
        final StringRequest request = new StringRequest(Request.Method.GET, endpoint, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    responseConsumer.accept(response);
                } catch (JSONException error) {
                    notifyError(error);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                notifyError(error);
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        server.addToRequestQueue(request);
    }
}
