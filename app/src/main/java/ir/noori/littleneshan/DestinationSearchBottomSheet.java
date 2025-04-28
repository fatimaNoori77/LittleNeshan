package ir.noori.littleneshan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

import ir.noori.littleneshan.databinding.FragmentDestinationSearchBinding;

public class DestinationSearchBottomSheet extends BottomSheetDialogFragment {
    private AddressAdapter adapter;
    private List<AddressModel> addresses = new ArrayList<>(); // Initialize with empty list
    private FragmentDestinationSearchBinding binding;
    private DestinationSelectionListener listener;

    public interface DestinationSelectionListener {
        void onDestinationSelected(AddressModel destination);
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
        return binding.getRoot();
    }
    @Override
    public void onStart() {
        super.onStart();
        // Make the bottom sheet full screen when opened
        binding.edtDestination.requestFocus();
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
    }

    private void setupViews() {
        // Setup search functionality
        binding.edtDestination.setOnEditorActionListener((v, actionId, event) -> {
            // Handle search action
            performSearch();
            return true;
        });

        addresses.add(new AddressModel("1", "خانه", "میدان شهدا", 36.29873381213554,59.60644999872008));
        addresses.add(new AddressModel("2", "محل کار", "سحاد ۲۱ نبش امین ۲ پلاک ۲۸", 36.31947365295402,59.544782018791157));
        addresses.add(new AddressModel("3", "کلینیک", " بلوار سید رضی، عدل ۸", 36.324848468582076,59.51894676720207));
        addresses.add(new AddressModel("4", "آموزشگاه", "پیروزی ۷۰", 36.32552135284385,59.48073720130864));
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
            // Perform your search logic here
            // Update RecyclerView with results
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Clean up binding reference
    }
}