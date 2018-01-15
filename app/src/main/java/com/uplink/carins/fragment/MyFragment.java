package com.uplink.carins.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uplink.carins.R;
import com.uplink.carins.activity.MainActivity;
import com.uplink.carins.ui.BaseFragment;

/**
 * Created by chingment on 2017/8/23.
 */

public class MyFragment extends BaseFragment implements View.OnClickListener {

    private View root;
    private MainActivity context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return root = inflater.inflate(R.layout.fragment_my, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = (MainActivity) getActivity();
    }

    @Override
    public void onClick(View view) {

    }
}
