package ir.noori.littleneshan.ui.search;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import ir.noori.littleneshan.data.model.SearchItem;
import ir.noori.littleneshan.databinding.ItemAddressBinding;
public class AddressAdapter extends ListAdapter<SearchItem, AddressAdapter.DestinationViewHolder> {

    private final OnDestinationClickListener listener;

    public interface OnDestinationClickListener {
        void onDestinationClick(SearchItem destination);
    }

    public AddressAdapter(OnDestinationClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<SearchItem> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<SearchItem>() {
                @Override
                public boolean areItemsTheSame(@NonNull SearchItem oldItem, @NonNull SearchItem newItem) {
                    return oldItem.getTitle().equals(newItem.getTitle());
                }

                @Override
                public boolean areContentsTheSame(@NonNull SearchItem oldItem, @NonNull SearchItem newItem) {
                    return oldItem.equals(newItem);
                }
            };

    @NonNull
    @Override
    public DestinationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAddressBinding binding = ItemAddressBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new DestinationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DestinationViewHolder holder, int position) {
        SearchItem destination = getItem(position);
        holder.bind(destination);
    }

    class DestinationViewHolder extends RecyclerView.ViewHolder {
        private final ItemAddressBinding binding;

        DestinationViewHolder(ItemAddressBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(SearchItem destination) {
            binding.txtAddressTitle.setText(destination.getTitle());
            binding.txtAddress.setText(destination.getAddress());

            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDestinationClick(destination);
                }
            });
        }
    }
}
