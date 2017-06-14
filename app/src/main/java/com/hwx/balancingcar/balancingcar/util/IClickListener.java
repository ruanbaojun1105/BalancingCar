package com.hwx.balancingcar.balancingcar.util;

import android.view.View;

public abstract class IClickListener implements View.OnClickListener {
    private long mLastClickTime = 0;
    public static final int TIME_INTERVAL = 500;

    @Override
    public final void onClick(View v) {
        if (System.currentTimeMillis() - mLastClickTime >= TIME_INTERVAL) {
            onIClick(v);
            mLastClickTime = System.currentTimeMillis();
        } else {
            onAgain(v);
        }
    }

    protected abstract void onIClick(View v);

    protected void onAgain(View v) {

    }
}