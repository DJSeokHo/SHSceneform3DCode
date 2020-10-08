package com.swein.shsceneform3dcode.modeldetailinfo.item;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.TextView;

import com.swein.shsceneform3dcode.R;
import com.swein.shsceneform3dcode.framework.util.view.ViewUtil;

public class ModelDetailInfoItemViewHolder {

    private View view;


    private TextView textViewTitle;
    private TextView textViewInfo;

    public ModelDetailInfoItemViewHolder(Context context) {
        view = ViewUtil.inflateView(context, R.layout.view_holder_model_detail_info_item, null);
        findView();
    }

    private void findView() {
        textViewTitle = view.findViewById(R.id.textViewTitle);
        textViewInfo = view.findViewById(R.id.textViewInfo);
    }

    public void updateView(String title, String info) {
        textViewTitle.setText(title);
        textViewInfo.setText(info);
    }

    public void updateView(String title, SpannableStringBuilder info) {
        textViewTitle.setText(title);
        textViewInfo.setText(info);
    }

    public View getView() {
        return view;
    }
}
