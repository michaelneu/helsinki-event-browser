package eu.michaeln.helsinkieventbrowser;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import java.util.ArrayList;
import java.util.function.Consumer;

import eu.michaeln.helsinkieventbrowser.adapters.AutoCompleteItemAdapter;
import eu.michaeln.helsinkieventbrowser.entities.AutoCompleteItem;

public abstract class AutoCompleteTextChangeListener implements TextWatcher {
    private final AutoCompleteTextView editText;
    private final Context context;

    public AutoCompleteTextChangeListener(@NonNull AutoCompleteTextView textView, Context context) {
        editText = textView;
        editText.addTextChangedListener(this);

        this.context = context;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) { }

    @Override
    public void afterTextChanged(Editable editable) { }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
        final String text = charSequence.toString()
                                        .trim();

        if (text.length() > 0) {
            textChanged(text, new Consumer<AutoCompleteItem[]>() {
                @Override
                public void accept(AutoCompleteItem[] items) {
                    final ArrayList<String> results = new ArrayList<>();

                    // why does this work, but resolving the text in the adapter doesn't?!
                    for (int i = 0; i < items.length; i++) {
                        final String item = items[i].getText().resolve();

                        if (!results.contains(item)) {
                            results.add(item);
                        }
                    }

                    final AutoCompleteItemAdapter adapter = new AutoCompleteItemAdapter(context, results);

                    editText.setAdapter(adapter);
                }
            });
        }
    }

    protected abstract void textChanged(String text, Consumer<AutoCompleteItem[]> consumer);
}
