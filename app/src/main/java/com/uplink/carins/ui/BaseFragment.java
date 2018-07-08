package com.uplink.carins.ui;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.uplink.carins.Own.AppContext;
import com.uplink.carins.utils.ToastUtil;

public class BaseFragment extends Fragment {

    public void showToast(String text) {
        ToastUtil.showMessage(getActivity(), text + "", Toast.LENGTH_SHORT);
    }


    private Activity activity;

    public Context getContext() {
        if (activity == null) {
            return AppContext.getInstance();
        }
        return activity;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        activity = getActivity();


    }
}
