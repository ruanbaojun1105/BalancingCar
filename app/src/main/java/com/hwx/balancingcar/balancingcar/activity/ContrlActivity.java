package com.hwx.balancingcar.balancingcar.activity;

import android.content.res.Configuration;
import android.support.v7.widget.Toolbar;

import com.hwx.balancingcar.balancingcar.R;
import com.hwx.balancingcar.balancingcar.fragment.PaFragment;
import com.hwx.balancingcar.balancingcar.simple.SimpleActivity;

public class ContrlActivity extends SimpleActivity {
    private static final String TAG = "ContrlActivity";

    /**
     * 屏幕旋转时调用此方法
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //newConfig.orientation获得当前屏幕状态是横向或者竖向
        //Configuration.ORIENTATION_PORTRAIT 表示竖向
        //Configuration.ORIENTATION_LANDSCAPE 表示横屏
        if(newConfig.orientation== Configuration.ORIENTATION_PORTRAIT){
            //Toast.makeText(ContrlActivity.this, "现在是竖屏", Toast.LENGTH_SHORT).show();
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.content, PaFragment.newInstance(1)).commitAllowingStateLoss();
        }
        if(newConfig.orientation== Configuration.ORIENTATION_LANDSCAPE){
            //Toast.makeText(ContrlActivity.this, "现在是横屏", Toast.LENGTH_SHORT).show();
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.content, PaFragment.newInstance(2)).commitAllowingStateLoss();
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_contrl;
    }

    @Override
    protected void initEventAndData() {
        setToolBar((Toolbar) findViewById(R.id.toolbar),"控制");
        getSupportFragmentManager().beginTransaction().
                replace(R.id.content, PaFragment.newInstance(1)).commitAllowingStateLoss();
    }

}
