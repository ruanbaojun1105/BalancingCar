package com.hwx.balancingcar.balancingcar.activity;


import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hwx.balancingcar.balancingcar.R;
import com.hwx.balancingcar.balancingcar.util.IClickListener;
import com.joanzapata.iconify.widget.IconTextView;

public class SettingsActivity extends BaseActivity {

    private IconTextView back_text;
    private ImageView image_set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
        setToolBar((Toolbar) findViewById(R.id.toolbar),"设置");
    }

    private void initView() {
        image_set = (ImageView) findViewById(R.id.image_set);
        Glide.with(this).load(getIntent().getExtras().getInt("image")).crossFade().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(image_set);
    }
}
