package com.hwx.balancingcar.balancingcar.fragment;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hwx.balancingcar.balancingcar.R;
import com.hwx.balancingcar.balancingcar.activity.ContrlActivity;
import com.hwx.balancingcar.balancingcar.activity.SimpleFragment;
import com.hwx.balancingcar.balancingcar.util.LogUtils;
import com.hwx.balancingcar.balancingcar.view.DialChart03View;
import com.hwx.balancingcar.balancingcar.view.StateButton;
import com.joanzapata.iconify.widget.IconTextView;

import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

public class MainFragment extends SimpleFragment {


    @BindView(R.id.circle_view)
    DialChart03View circleView;
    @BindView(R.id.this_tag)
    IconTextView thisTag;
    @BindView(R.id.this_tag_number)
    TextView thisTagNumber;
    @BindView(R.id.all_tag)
    IconTextView allTag;
    @BindView(R.id.all_tag_number)
    TextView allTagNumber;
    @BindView(R.id.temperature_tag)
    IconTextView temperatureTag;
    @BindView(R.id.temperature_tag_number)
    TextView temperatureTagNumber;
    @BindView(R.id.current_tag)
    IconTextView currentTag;
    @BindView(R.id.current_tag_number)
    TextView currentTagNumber;
    @BindView(R.id.electricity_tag)
    IconTextView electricityTag;
    @BindView(R.id.electricity_tag_number)
    TextView electricityTagNumber;
    @BindView(R.id.star_stop_light)
    StateButton starStopLight;
    @BindView(R.id.star_stop_device)
    StateButton starStopDevice;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LogUtils.e("____________________");
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initEventAndData() {
    }

    @OnClick({R.id.star_stop_light, R.id.star_stop_device})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.star_stop_light:
                int max = 100;
                int min = 1;
                Random random = new Random();
                int p = random.nextInt(max) % (max - min + 1) + min;
                float pf = p / 100f;
                circleView.setMax(20);
                circleView.setCurrentStatus(pf);
                circleView.invalidate();
                break;
            case R.id.star_stop_device:
                startActivity(new Intent(getContext(), ContrlActivity.class));
                break;
        }
    }
}
