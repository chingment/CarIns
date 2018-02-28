package com.uplink.carins.device;

import android.os.Handler;

import com.newland.mtype.ConnectionCloseEvent;
import com.newland.mtype.event.DeviceEventListener;



/**
 * Created by chingment on 2018/2/27.
 */

public class DeviceCloseListener implements DeviceEventListener<ConnectionCloseEvent> {
    @Override
    public Handler getUIHandler() {
        return null;
    }

    @Override
    public void onEvent(ConnectionCloseEvent event, Handler handler) {
        if (event.isFailed()) {
        }
    }
}

