package de.submit_ev.vendingapp.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;
import de.submit_ev.vendingapp.R;
import de.submit_ev.vendingapp.events.SelectedVendorChangedEvent;
import de.submit_ev.vendingapp.models.Vendor;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentVendorDetail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentVendorDetail extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    OnFragmentInteractionListener mListener;

    @InjectView(R.id.textViewDescriptionContent)
    TextView textViewDescriptionContent;

    @InjectView(R.id.textViewVendorTypeContent)
    TextView textViewVendorTypeContent;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_vendor_detail.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentVendorDetail newInstance(String param1, String param2) {
        FragmentVendorDetail fragment = new FragmentVendorDetail();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentVendorDetail() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        //EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vendor_detail, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    public interface OnFragmentInteractionListener {
        void onDataSubmit(Vendor vendor);
    }

    public void onEvent(SelectedVendorChangedEvent selectedVendorChangedEvent) {
        Vendor vendor = selectedVendorChangedEvent.getVendor();
        textViewDescriptionContent.setText(vendor.getDescription());
        if (vendor.getVendorType() != null) {
            textViewVendorTypeContent.setText(vendor.getVendorType().getName());
        }

    }

}
