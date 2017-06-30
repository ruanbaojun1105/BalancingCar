package com.hwx.balancingcar.balancingcar.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hwx.balancingcar.balancingcar.R;

import java.util.List;

/**
 *
 */
public class BleTextItemAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public BleTextItemAdapter(List<String> data) {
        super(R.layout.ble_item_text, data);
    }


    @Override
    protected void convert(final BaseViewHolder helper, final String item) {
        helper.setText(R.id.text,item.toString());
    }
}