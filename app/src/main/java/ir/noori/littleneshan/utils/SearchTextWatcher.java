package ir.noori.littleneshan.utils;

import static ir.noori.littleneshan.utils.Constants.SEARCH_DELAY;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class SearchTextWatcher implements TextWatcher {

    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;
    private final SearchCallback callback;

    public interface SearchCallback {
        void onSearch(String query);
    }

    public SearchTextWatcher(SearchCallback callback) {
        this.callback = callback;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (searchRunnable != null) {
            handler.removeCallbacks(searchRunnable);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        final String query = s.toString().trim();

        searchRunnable = () -> {
            if (!query.isEmpty()) {
                callback.onSearch(query);
            }
        };

        handler.postDelayed(searchRunnable, SEARCH_DELAY);
    }

    public static void attachTo(EditText editText, SearchCallback callback) {
        editText.addTextChangedListener(new SearchTextWatcher(callback));
    }
}

