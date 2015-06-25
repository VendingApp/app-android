package de.submit_ev.vendingapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.submit_ev.vendingapp.R;
import de.submit_ev.vendingapp.models.PriceStorageTable;

/**
 * Created by Igor on 25.06.2015.
 */
public class ProductAdapter extends AbstractListAdapter<PriceStorageTable, ProductAdapter.ViewHolder> {

    private final LayoutInflater mInflater;
    private final Context context;

    public ProductAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(mInflater.inflate(R.layout.list_item_product, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.bind(mData.get(i));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.textViewProductName)
        TextView textViewProductName;
        @InjectView(R.id.textViewProductBrand)
        TextView textViewProductBrand;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }

        public void bind(PriceStorageTable data) {
            textViewProductBrand.setText(data.getProduct().getBrand().getName());
            textViewProductName.setText(data.getProduct().getName());
        }
    }
}
