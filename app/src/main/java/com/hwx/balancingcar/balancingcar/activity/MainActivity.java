package com.hwx.balancingcar.balancingcar.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.hwx.balancingcar.balancingcar.R;
import com.hwx.balancingcar.balancingcar.fragment.MainFragment;
import com.hwx.balancingcar.balancingcar.fragment.TalkFragment;
import com.hwx.balancingcar.balancingcar.tool.scan.AnyScanActivity;
import com.hwx.balancingcar.balancingcar.tool.scan.NameScanActivity;
import com.hwx.balancingcar.balancingcar.util.IClickListener;
import com.hwx.balancingcar.balancingcar.util.LogUtils;
import com.hwx.balancingcar.balancingcar.view.MyFragmentPagerAdapter;
import com.joanzapata.iconify.widget.IconTextView;
import com.rd.PageIndicatorView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;


public class MainActivity extends SimpleActivity {

    @BindView(R.id.seting_btn)
    IconTextView setingBtn;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.pageIndicatorView)
    PageIndicatorView pageIndicatorView;
    @BindView(R.id.connect_btn)
    IconTextView connectBtn;
    private MyFragmentPagerAdapter mSectionsPagerAdapter;

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initEventAndData() {
        setSupportActionBar(toolbar);
        initViews();
    }

    @SuppressWarnings("ConstantConditions")
    private void initViews() {
        mSectionsPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), getfragmentList());
        mSectionsPagerAdapter.setmTitles(gettitleList());
        viewPager.setAdapter(mSectionsPagerAdapter);

        pageIndicatorView = (PageIndicatorView) findViewById(R.id.pageIndicatorView);
        //viewPager.setOffscreenPageLimit(2);
        pageIndicatorView.setViewPager(viewPager);

        tabs.setupWithViewPager(viewPager);

        setingBtn = (IconTextView) findViewById(R.id.seting_btn);
        connectBtn.setOnClickListener(new IClickListener() {
            @Override
            protected void onIClick(View v) {
                new AlertDialog.Builder(MainActivity.this)
                        .setItems(new CharSequence[]{"直连蓝牙（正式版本实现）", "自己选择蓝牙连接"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                checkP(i==0?false:true);
                            }
                        })
                        .show();
            }
        });
        setingBtn.setOnClickListener(new IClickListener() {
            @Override
            protected void onIClick(View v) {
                int res = 0;
                switch (viewPager.getCurrentItem()) {
                    case 0:
                        res = R.drawable.pingheng;
                        break;
                    case 1:
                        res = R.drawable.pingheng2;
                        break;
                    case 2:
                        res = R.drawable.pingheng3;
                        break;
                    case 3:
                        res = R.drawable.pingheng4;
                        break;
                    case 4:
                        res = 0;
                        break;
                }
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                intent.putExtra("image", res);
                startActivity(intent);
            }
        });
    }
    private void checkP(final boolean isScan){
        if (!HiPermission.checkPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)){
            List<PermissionItem> permissionItems = new ArrayList<PermissionItem>();
            permissionItems.add(new PermissionItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, "SD write permission", R.drawable.permission_ic_storage));
            permissionItems.add(new PermissionItem(Manifest.permission.READ_EXTERNAL_STORAGE, "SD read permission", R.drawable.permission_ic_storage));
            permissionItems.add(new PermissionItem(Manifest.permission.READ_PHONE_STATE, "Read Phone permission", R.drawable.permission_ic_phone));
            permissionItems.add(new PermissionItem(Manifest.permission.ACCESS_COARSE_LOCATION, "Get location permission", R.drawable.permission_ic_location));
            HiPermission.create(this).title("申请手机权限").permissions(permissionItems)
                    .filterColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, getTheme()))//permission icon color
                    .msg("To protect the peace of the world, open these permissions! You and I together save the world!")
                    .style(R.style.PermissionBlueStyle)
                    .checkMutiPermission(new PermissionCallback() {
                        @Override
                        public void onClose() {
                            Log.i("", "onClose");
                            LogUtils.e("用户关闭权限申请");
                            Snackbar.make(connectBtn,"已拒绝权限申请",Snackbar.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFinish() {
                            LogUtils.e("所有权限申请完成");
                            startActivity(new Intent(MainActivity.this, isScan?AnyScanActivity.class:NameScanActivity.class));
                        }

                        @Override
                        public void onDeny(String permission, int position) {
                            Log.i("", "onDeny");
                        }

                        @Override
                        public void onGuarantee(String permission, int position) {
                            Log.i("", "onGuarantee");
                        }
                    });
        }else {
            startActivity(new Intent(MainActivity.this, isScan?AnyScanActivity.class:NameScanActivity.class));
        }
    }
    @NonNull
    private List<View> createPageList() {
        List<View> pageList = new ArrayList<>();
        pageList.add(createPageView(R.color.red));
        pageList.add(createPageView(R.color.blue));
        pageList.add(createPageView(R.color.yellow));
        pageList.add(createPageView(R.color.green));
        pageList.add(createPageView(R.color.yellow));
        pageList.add(createPageView(R.color.green));
        pageList.add(createPageView(R.color.yellow));

        return pageList;
    }

    @NonNull
    private List<String> gettitleList() {
        List<String> pageList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            pageList.add(i + "tag");
        }
        pageList.add("论坛");
        return pageList;
    }

    @NonNull
    private List<Fragment> getfragmentList() {
        List<Fragment> pageList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            pageList.add(new MainFragment());
        }
        pageList.add(new TalkFragment());
        return pageList;
    }

    @NonNull
    private View createPageView(int color) {
        View view = new View(this);
        view.setBackgroundColor(getResources().getColor(color));

        return view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
