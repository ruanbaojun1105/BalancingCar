package com.hwx.balancingcar.balancingcar.fragment;


import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.animation.AnticipateInterpolator;
import android.widget.TextView;

import com.hwx.balancingcar.balancingcar.R;
import com.hwx.balancingcar.balancingcar.simple.BluetoothService;
import com.hwx.balancingcar.balancingcar.simple.SimpleFragment;
import com.hwx.balancingcar.balancingcar.view.DialChart02View;
import com.hwx.balancingcar.balancingcar.view.SteeringWheelView;

import java.util.Locale;

import butterknife.BindView;

/**
 * 竖屏
 */
public class PaFragment extends SimpleFragment implements SteeringWheelView.SteeringWheelListener {


    @BindView(R.id.tv)
    TextView tv;
    @BindView(R.id.dial)
    DialChart02View dial;
    @BindView(R.id.steeringWheelView)
    SteeringWheelView steeringWheelView;
    private SparseArray<String> mSparseArray = new SparseArray<>();
    private int tag=1;

    public PaFragment() {
        // Required empty public constructor
    }
    public static PaFragment newInstance(int param1) {
        PaFragment fragment = new PaFragment();
        Bundle args = new Bundle();
        args.putInt("tag", param1);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tag = getArguments().getInt("tag",1);
        }
    }

    @Override
    protected int getLayoutId() {
        int res=0;
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.i("info", "landscape");
            res = R.layout.fragment_la;
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.i("info", "portrait");
            res = R.layout.fragment_pa;
        }
        return res;
    }

    @Override
    protected void initEventAndData() {
        initData();
        initView();
    }

    private void initData() {
        mSparseArray.put(SteeringWheelView.LEFT, getString(R.string.left));
        mSparseArray.put(SteeringWheelView.UP, getString(R.string.up));
        mSparseArray.put(SteeringWheelView.RIGHT, getString(R.string.right));
        mSparseArray.put(SteeringWheelView.DOWN, getString(R.string.down));
        mSparseArray.put(SteeringWheelView.INVALID, getString(R.string.idle));
    }

    private void initView() {
        steeringWheelView.notifyInterval(16).listener(this).interpolator(new AnticipateInterpolator(3));
    }

    @Override
    public void onStatusChanged(SteeringWheelView view, int angle, int power, int direction) {
        String text = constructText(angle, power, direction);
        tv.setText(text);
        BluetoothService.getInstance().sendData((byte) 0x03,new byte[]{(byte)direction},true);
    }

    private String constructText(int angle, int power, int direction) {
        return String.format(Locale.CHINESE, "angle = %3d\tpower = %3d\tdirection = %s",
                angle, power, direction2Text(angle,direction));
    }

    private String direction2Text(int angle, int direction) {
        /*int max = 100;
        int min = 1;
        Random random = new Random();
        int p = random.nextInt(max)%(max-min+1) + min;
        float pf = p / 100f;*/
        dial.setMax(360);
        dial.setCurrentStatus((float) ((float)angle/360.0));
        dial.invalidate();
        return mSparseArray.get(direction);
    }
}
