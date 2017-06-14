/**
 * Copyright 2014  XCL-Charts
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @Project XCL-Charts
 * @Description Android图表基类库
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 * @license http://www.apache.org/licenses/  Apache v2 License
 * @version v0.1
 */

package com.hwx.balancingcar.balancingcar.view;

import java.util.ArrayList;
import java.util.List;

import org.xclcharts.chart.DialChart;
import org.xclcharts.common.MathHelper;
import org.xclcharts.renderer.XEnum;
import org.xclcharts.renderer.plot.PlotAttrInfo;
import org.xclcharts.renderer.plot.Pointer;
import org.xclcharts.view.GraphicalView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;

import com.hwx.balancingcar.balancingcar.util.LogUtils;

/**
 * @ClassName DialChart例子
 * @Description 仪表盘例子
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 *
 */
public class DialChart03View extends GraphicalView {

    private String TAG = "DialChart03View";

    private DialChart chart = new DialChart();
    private float mPercentage = 0.0f;//默认百分比
    private int max;//最大值

    public void setMax(int max) {
        this.max = max;
    }

    public int getMax() {
        return max;
    }

    public DialChart03View(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        LogUtils.e("initView");
        initView();
    }

    public DialChart03View(Context context, AttributeSet attrs) {
        super(context, attrs);
        LogUtils.e("initView");
        initView();
    }

    public DialChart03View(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LogUtils.e("initView");
        initView();
    }


    private void initView() {
        chartRender();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        chart.setChartRange(w, h);
        LogUtils.e("onSizeChanged");
    }

    public void chartRender() {
        LogUtils.e("chartRender");
        try {

            //设置标题背景
            chart.setApplyBackgroundColor(false);
            chart.setBackgroundColor( Color.rgb(192, 192, 192) );
            //绘制边框
            //chart.showRoundBorder();

            //设置当前百分比
            chart.getPointer().setPercentage(mPercentage);

            //设置指针长度
            chart.getPointer().setLength(0.70f);

            //增加轴
            addAxis();
            /////////////////////////////////////////////////////////////
            //增加指针
            addPointer();
            //设置附加信息
            addAttrInfo();
            /////////////////////////////////////////////////////////////

        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e(TAG, e.toString());
        }

    }

    public void addAxis() {
        LogUtils.e("addAxis");
        List<Float> ringPercentage = new ArrayList<Float>();
        float rper = MathHelper.getInstance().div(1, 4); //相当于40%	//270, 4
        ringPercentage.add(rper);
        ringPercentage.add(rper);
        ringPercentage.add(rper);
        ringPercentage.add(rper);

        List<Integer> rcolor = new ArrayList<Integer>();
        rcolor.add(Color.rgb(242, 110, 131));
        rcolor.add(Color.rgb(238, 204, 71));
        rcolor.add(Color.rgb(42, 231, 250));
        rcolor.add(Color.rgb(140, 196, 27));
        chart.addStrokeRingAxis(0.95f, 0.8f, ringPercentage, rcolor);


        List<String> rlabels = new ArrayList<String>();
        rlabels.add("0");
        rlabels.add("2");
        rlabels.add("4");
        rlabels.add("6");
        rlabels.add("8");
        rlabels.add("10");
        rlabels.add("12");
        rlabels.add("14");
        rlabels.add("16");
        rlabels.add("18");
        rlabels.add("20");
        chart.addInnerTicksAxis(0.8f, rlabels);


        chart.getPlotAxis().get(0).getFillAxisPaint().setColor(Color.rgb(59, 140, 163));
        chart.getPlotAxis().get(1).getFillAxisPaint().setColor(Color.rgb(28, 129, 243));
        chart.getPlotAxis().get(1).getTickLabelPaint().setColor(Color.WHITE);
        chart.getPlotAxis().get(1).getTickMarksPaint().setColor(Color.WHITE);
        chart.getPlotAxis().get(1).hideAxisLine();//隐藏内框黑线边
        chart.getPlotAxis().get(1).setDetailModeSteps(1);//间隔1个段粗刻度 2为两个段

        chart.getPointer().setPointerStyle(XEnum.PointerStyle.TRIANGLE);
        chart.getPointer().getPointerPaint().setColor(Color.BLACK);
        chart.getPointer().getPointerPaint().setStrokeWidth(3);
        chart.getPointer().getPointerPaint().setStyle(Style.STROKE);
        chart.getPointer().hideBaseCircle();

    }

    //增加指针
    public void addPointer() {
        LogUtils.e("addPointer");
        chart.addPointer();
        List<Pointer> mp = chart.getPlotPointer();
        mp.get(0).setPercentage(mPercentage);
        //设置指针长度
        mp.get(0).setLength(0.70f);
        mp.get(0).getPointerPaint().setColor(Color.WHITE);
        mp.get(0).setPointerStyle(XEnum.PointerStyle.TRIANGLE);
        mp.get(0).hideBaseCircle();

    }


    private void addAttrInfo() {
        LogUtils.e("addAttrInfo");
        /////////////////////////////////////////////////////////////
        PlotAttrInfo plotAttrInfo = chart.getPlotAttrInfo();
        //设置附加信息
        Paint paintTB = new Paint();
        paintTB.setColor(Color.WHITE);
        paintTB.setTextAlign(Align.CENTER);
        paintTB.setTextSize(30);
        paintTB.setAntiAlias(true);
        plotAttrInfo.addAttributeInfo(XEnum.Location.TOP, "当前速度", 0.3f, paintTB);

        Paint paintBT = new Paint();
        paintBT.setColor(Color.WHITE);
        paintBT.setTextAlign(Align.CENTER);
        paintBT.setTextSize(35);
        paintBT.setFakeBoldText(true);
        paintBT.setAntiAlias(true);
        plotAttrInfo.addAttributeInfo(XEnum.Location.BOTTOM,
                Float.toString(MathHelper.getInstance().round(mPercentage * max, 2)), 0.3f, paintBT);

        Paint paintBT2 = new Paint();
        paintBT2.setColor(Color.WHITE);
        paintBT2.setTextAlign(Align.CENTER);
        paintBT2.setTextSize(30);
        paintBT2.setFakeBoldText(true);
        paintBT2.setAntiAlias(true);
        plotAttrInfo.addAttributeInfo(XEnum.Location.BOTTOM, "km/h", 0.4f, paintBT2);
    }

    public void setCurrentStatus(float percentage) {
        mPercentage = percentage;
        chart.clearAll();

        //设置当前百分比
        chart.getPointer().setPercentage(mPercentage);
        addAxis();
        //增加指针
        addPointer();
        addAttrInfo();
    }

    public float getmPercentage() {
        return mPercentage;
    }

    @Override
    public void render(Canvas canvas) {
        // TODO Auto-generated method stub
        try {
            LogUtils.e("render");
            chart.render(canvas);
            chartRender();//防止fragment清空
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

}
