package com.uplink.carins.ui;

import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.uplink.carins.utils.ToastUtil;

public class BaseFragment extends Fragment {

    public void showToast(String text) {
        ToastUtil.showMessage(getActivity(), text + "", Toast.LENGTH_SHORT);
    }

}
