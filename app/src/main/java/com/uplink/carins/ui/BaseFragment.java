package com.uplink.carins.ui;

import android.support.v4.app.Fragment;
import android.widget.Toast;

public class BaseFragment extends Fragment {

    public void showToast(String text) {
        Toast.makeText(getActivity(), text + "", Toast.LENGTH_SHORT).show();
    }

}
