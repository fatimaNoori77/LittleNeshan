package ir.noori.littleneshan.utils;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class SearchTextWatcher implements TextWatcher {

    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;
    private static final long DELAY_MILLIS = 1000; // 1 seconds delay
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

        handler.postDelayed(searchRunnable, DELAY_MILLIS);
    }

    public static void attachTo(EditText editText, SearchCallback callback) {
        editText.addTextChangedListener(new SearchTextWatcher(callback));
    }
}

