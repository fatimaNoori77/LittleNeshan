package ir.noori.littleneshan;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import ir.noori.littleneshan.databinding.ItemAddressBinding;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.DestinationViewHolder> {

    private List<AddressModel> destinations;
    private OnDestinationClickListener listener;

    public interface OnDestinationClickListener {
        void onDestinationClick(AddressModel destination);
    }

    public AddressAdapter(List<AddressModel> destinations, OnDestinationClickListener listener) {
        this.destinations = destinations;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DestinationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAddressBinding binding = ItemAddressBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new DestinationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DestinationViewHolder holder, int position) {
        AddressModel destination = destinations.get(position);
        holder.bind(destination);
    }

    @Override
    public int getItemCount() {
        return destinations != null ? destinations.size() : 0;
    }

    class DestinationViewHolder extends RecyclerView.ViewHolder {
        private final ItemAddressBinding binding;

        DestinationViewHolder(ItemAddressBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(AddressModel destination) {
            binding.destinationName.setText(destination.getName());
            binding.destinationAddress.setText(destination.getAddress());

            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDestinationClick(destination);
                }
            });
        }
    }
}