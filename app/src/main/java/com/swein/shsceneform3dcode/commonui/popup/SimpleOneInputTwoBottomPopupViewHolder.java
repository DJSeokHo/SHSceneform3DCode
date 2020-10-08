package com.swein.shsceneform3dcode.commonui.popup;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.swein.shsceneform3dcode.R;
import com.swein.shsceneform3dcode.framework.util.view.ViewUtil;

public class SimpleOneInputTwoBottomPopupViewHolder {

    public interface SimpleOneInputTwoBottomPopupViewHolderDelegate {
        void onConfirm();
        void onClose();
    }

    private View view;

    private EditText editTextName;
    private TextView textViewCancel;
    private TextView textViewConfirm;

    public SimpleOneInputTwoBottomPopupViewHolderDelegate simpleOneInputTwoBottomPopupViewHolderDelegate;

    public SimpleOneInputTwoBottomPopupViewHolder(Context context) {
        view = ViewUtil.inflateView(context, R.layout.view_holder_simple_one_input_two_button_popup, null);
        findView();
        setListener();
    }

    private void findView() {
        editTextName = view.findViewById(R.id.editTextName);
        textViewCancel = view.findViewById(R.id.textViewCancel);
        textViewConfirm = view.findViewById(R.id.textViewConfirm);
    }

    private void setListener() {
        textViewCancel.setOnClickListener(view -> simpleOneInputTwoBottomPopupViewHolderDelegate.onClose());
        textViewConfirm.setOnClickListener(view -> simpleOneInputTwoBottomPopupViewHolderDelegate.onConfirm());
    }


    public void setString(String title) {
        editTextName.setText(title);
    }


    public View getView() {
        return view;
    }
}
