package de.submit_ev.vendingapp.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;
import de.submit_ev.vendingapp.R;
import de.submit_ev.vendingapp.adapters.ProductAdapter;
import de.submit_ev.vendingapp.api.ServerApi;
import de.submit_ev.vendingapp.events.SelectedVendorChangedEvent;
import de.submit_ev.vendingapp.models.PriceStorageTable;
import de.submit_ev.vendingapp.models.Vendor;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentVendorProducts#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentVendorProducts extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @InjectView(R.id.my_recycler_view)
    RecyclerView recyclerView;

    ProductAdapter productAdapter;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment vendor_products.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentVendorProducts newInstance(String param1, String param2) {
        FragmentVendorProducts fragment = new FragmentVendorProducts();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentVendorProducts() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vendor_products, container, false);
        ButterKnife.inject(this, view);

        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);

        productAdapter = new ProductAdapter(getActivity().getApplicationContext());
        recyclerView.setAdapter(productAdapter);

        EventBus.getDefault().register(this);

        return view;
    }

    public void onEvent(SelectedVendorChangedEvent selectedVendorChangedEvent) {
        while(productAdapter.getItemCount() > 0)
            productAdapter.deleteEntity(0);

        ServerApi.getProducts(selectedVendorChangedEvent.getVendor().getId(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                Gson gson = new GsonBuilder().create();

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject item = response.getJSONObject(i);
                        PriceStorageTable priceStorageTable = gson.fromJson(item.toString(), PriceStorageTable.class);
                        productAdapter.addEntity(0, priceStorageTable);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                if (statusCode == 0) {
                    // No internet connection
                } else {

                }
            }
        });

    }


}
