package ir.noori.littleneshan;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

import ir.noori.littleneshan.databinding.FragmentDestinationSearchBinding;

public class DestinationSearchBottomSheet extends BottomSheetDialogFragment {
    private AddressAdapter adapter;
    private List<SearchItem> addresses = new ArrayList<>(); // Initialize with empty list
    private FragmentDestinationSearchBinding binding;
    private DestinationSelectionListener listener;
    private SearchViewModel viewModel;
    SharedPreferencesUtility preferences;


    public interface DestinationSelectionListener {
        void onDestinationSelected(SearchItem destination);
    }

    public void setDestinationSelectionListener(DestinationSelectionListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialog);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDestinationSearchBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        preferences = SharedPreferencesUtility.getInstance(getContext());
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Make the bottom sheet full screen when opened
        if (getDialog() != null && getDialog().getWindow() != null) {
            BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
            FrameLayout bottomSheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                BottomSheetBehavior<FrameLayout> behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                behavior.setSkipCollapsed(true);
                ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();
                if (layoutParams != null) {
                    layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                }
                bottomSheet.setLayoutParams(layoutParams);
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViews();
        initObservers();
    }

    private void setupViews() {
        binding.edtDestination.requestFocus();
        binding.edtDestination.setOnEditorActionListener((v, actionId, event) -> {
            performSearch();
            return true;
        });
        SearchTextWatcher.attachTo(binding.edtDestination, query -> {
            performSearch();
        });
    }

    private void initObservers() {
        viewModel.getSearchResult().observe(getViewLifecycleOwner(), result -> {
            Log.i("TAG", "initObservers: " + result);
            if (result != null) {
                addresses.clear();
                addresses.addAll(result.getItems());
                setAdapter();
            }
        });
    }

    private void setAdapter() {
        adapter = new AddressAdapter(addresses, destination -> {
            if (listener != null) {
                listener.onDestinationSelected(destination);
            }
            dismiss();
        });
        binding.destinationsRecyclerView.setAdapter(adapter);

    }

    private void performSearch() {
        String query = binding.edtDestination.getText().toString().trim();
        if (!query.isEmpty()) {
            viewModel.searchAddress("service.e1818ace4a9a49a89328232697bbd9e8", query, preferences.getLatitude(), preferences.getLongitude());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Clean up binding reference
    }
}