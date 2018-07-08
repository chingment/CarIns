package com.uplink.carins.ui;

/**
 * Created by chingment on 2018/7/8.
 */

public class RadioGroupCheckBean {

    public RadioGroupCheckBean() {

    }

    public RadioGroupCheckBean(int currentPosition, int lastPosition) {
        this.currentPosition = currentPosition;
        this.lastPosition = lastPosition;
    }

    private int currentPosition;

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public int getLastPosition() {
        return lastPosition;
    }

    public void setLastPosition(int lastPosition) {
        this.lastPosition = lastPosition;
    }

    private int lastPosition;
}
