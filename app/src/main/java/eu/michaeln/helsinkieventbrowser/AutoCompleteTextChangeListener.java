package eu.michaeln.helsinkieventbrowser;

import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AutoCompleteTextView;

import java.util.ArrayList;
import java.util.function.Consumer;

import eu.michaeln.helsinkieventbrowser.adapters.AutoCompleteItemAdapter;
import eu.michaeln.helsinkieventbrowser.entities.AutoCompleteItem;

public abstract class AutoCompleteTextChangeListener implements TextWatcher {
    private final AutoCompleteTextView editText;

    public AutoCompleteTextChangeListener(@NonNull AutoCompleteTextView textView) {
        editText = textView;
        editText.addTextChangedListener(this);
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
                    final AutoCompleteItemAdapter adapter = new AutoCompleteItemAdapter(editText.getContext(), items);

                    editText.setAdapter(adapter);
                }
            });
        }
    }

    protected abstract void textChanged(String text, Consumer<AutoCompleteItem[]> consumer);
}
