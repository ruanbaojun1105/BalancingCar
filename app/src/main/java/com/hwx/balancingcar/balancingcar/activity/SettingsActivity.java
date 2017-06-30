package com.hwx.balancingcar.balancingcar.activity;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hwx.balancingcar.balancingcar.App;
import com.hwx.balancingcar.balancingcar.R;
import com.hwx.balancingcar.balancingcar.simple.BaseActivity;
import com.hwx.balancingcar.balancingcar.util.ScreenParamsUtil;
import com.joanzapata.iconify.widget.IconTextView;

import java.util.concurrent.ExecutionException;

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
        //Glide.with(this).load(getIntent().getExtras().getInt("image")).crossFade().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(image_set);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap myBitmap = null;
                int res=getIntent().getExtras().getInt("image");
                if (res==0)
                    return;
                try {
                    myBitmap = Glide.with(SettingsActivity.this)
                            .load(getIntent().getExtras().getInt("image"))
                            .asBitmap() //必须
                            //.centerCrop()
                            .into(ScreenParamsUtil.getInstance(SettingsActivity.this).screenWidth, App.dip2px(200))
                            .get();
                    final Bitmap finalMyBitmap = myBitmap;
                    image_set.post(new Runnable() {
                        @Override
                        public void run() {
                            image_set.setImageBitmap(finalMyBitmap);
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                int width=myBitmap.getWidth();//获取位图的宽
                int height=myBitmap.getHeight();//获取位图的高
                int[] colors=new int[width];//取一行图片的像素点
                for(int i=0;i<height;i++){
                    //将位图bm的第i行的像素放入到数组 colors中去
                    myBitmap.getPixels(colors, 0, width, 0, i, width, 1);
                    for(int j=0;j<colors.length;j++) {
                        //将颜色数组中的RGB值取反，255减去当前颜色值就获得当前颜色的反色
                        if (colors[j] == -1||colors[j] ==-16777216) {
                            colors[j]=0;
                        }
                        if (colors[j] != 0) {
                            colors[j] = Color.rgb(255 - Color.red(colors[j]),
                                    255 - Color.green(colors[j]), 255 - Color.blue(colors[j]));
                        }else
                            continue;
                    }
                    myBitmap.setPixels(colors, 0, width, 0, i, width, 1);//颜色取反后，将像素加入到pm的第i行中去
                    try {
                        float a=(float)i/(float)height*10f;
                        Thread.sleep((long) a+5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    image_set.post(new Runnable() {
                        @Override
                        public void run() {
                            image_set.postInvalidate();//刷新图片
                        }
                    });
                }
            }
        }).start();
    }
}
