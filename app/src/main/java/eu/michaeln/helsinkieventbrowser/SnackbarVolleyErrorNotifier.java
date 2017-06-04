package eu.michaeln.helsinkieventbrowser;

import android.support.design.widget.Snackbar;
import android.view.View;

import com.android.volley.VolleyError;

public final class SnackbarVolleyErrorNotifier implements ErrorNotifier<VolleyError> {
    private final View view;

    public SnackbarVolleyErrorNotifier(View view) {
        this.view = view;
    }

    @Override
    public void notify(VolleyError warning) {
        Snackbar.make(view, R.string.error_network_connection, Snackbar.LENGTH_LONG)
                .show();
    }
}
