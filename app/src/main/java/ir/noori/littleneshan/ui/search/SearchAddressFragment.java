package ir.noori.littleneshan.ui.search;

import android.os.Bundle;
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

import dagger.hilt.android.AndroidEntryPoint;
import ir.noori.littleneshan.R;
import ir.noori.littleneshan.data.local.entity.AddressEntity;
import ir.noori.littleneshan.data.local.entity.mapper.Mapper;
import ir.noori.littleneshan.data.model.LocationModel;
import ir.noori.littleneshan.data.model.SearchItem;
import ir.noori.littleneshan.databinding.FragmentSearchAddressBinding;
import ir.noori.littleneshan.utils.SearchTextWatcher;

@AndroidEntryPoint
public class SearchAddressFragment extends BottomSheetDialogFragment {
    public static final String TAG = SearchAddressFragment.class.getSimpleName();
    AddressAdapter adapter;
    private final List<SearchItem> addresses = new ArrayList<>(); // Initialize with empty list
    private FragmentSearchAddressBinding binding;
    private DestinationSelectionListener listener;
    private SearchAddressViewModel viewModel;


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
        viewModel = new ViewModelProvider(this).get(SearchAddressViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchAddressBinding.inflate(inflater, container, false);
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
        initAddressAdapter();
    }

    private void setupViews() {
        binding.edtDestination.requestFocus();
        binding.edtDestination.setOnEditorActionListener((v, actionId, event) -> {
            performSearch();
            return true;
        });
        SearchTextWatcher.attachTo(binding.edtDestination, query -> performSearch());
        binding.imgClose.setOnClickListener(v -> {
            if(binding.edtDestination.getText().toString().trim().isEmpty()){
                dismiss();
            }else{
                binding.edtDestination.setText("");
            }
        });
        binding.chipHome.setOnClickListener(v -> {
            LocationModel location = new LocationModel(59.606050921164695, 36.29799544485502);

            SearchItem item = new SearchItem(
                    "میدان شهدا",
                    "میدان شهدا",
                    "محله شهدا",
                    "مشهد، خراسان رضوی",
                    "religious",
                    "place",
                    location
            );

            listener.onDestinationSelected(item);
            dismiss();
        });
        binding.chipWork.setOnClickListener(v -> {
            LocationModel location = new LocationModel(59.544793812562546, 36.319510912113614);

            SearchItem item = new SearchItem(
                    "شتابندهنده هاوش",
                    "سجاد ۲۱ نبش امین ۲",
                    "سجاد",
                    "مشهد، خراسان رضوی",
                    "religious",
                    "place",
                    location
            );

            listener.onDestinationSelected(item);
            dismiss();
        });
    }

    private void initObservers() {
        viewModel.getAllAddressesFromDatabase().observe(getViewLifecycleOwner(), addresses -> {
            if (binding.edtDestination.getText().toString().isEmpty()) {
                List<SearchItem> searchItems = new ArrayList<>();
                for (AddressEntity entity : addresses) {
                    searchItems.add(Mapper.mapToSearchItem(entity));
                }
                adapter.submitList(searchItems);
            }
        });

        viewModel.getIsLoading().observe(getViewLifecycleOwner(), loader->{
            if (loader) {
                binding.viewFlipper.setDisplayedChild(0);
            } else {
                binding.viewFlipper.setDisplayedChild(1);
            }
        });
        viewModel.getSearchResult().observe(getViewLifecycleOwner(), result -> {
            binding.edtDestination.clearFocus();
            if (result != null) {
                addresses.clear();
                addresses.addAll(result.getItems());
                adapter.submitList(addresses);
            }
        });
    }

    private void initAddressAdapter() {
        adapter = new AddressAdapter(destination -> {
            if (listener != null) {
                viewModel.insertAddress(Mapper.mapToAddressEntity(destination));
                listener.onDestinationSelected(destination);
            }
            dismiss();
        });
        binding.destinationsRecyclerView.setAdapter(adapter);
        adapter.submitList(addresses);
    }

    private void performSearch() {
        String query = binding.edtDestination.getText().toString().trim();
        if (!query.isEmpty()) {
            viewModel.searchAddress(query,viewModel.getCurrentUserLocation());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}