package com.swein.shsceneform3dcode.commonui.navigation;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.swein.shsceneform3dcode.R;
import com.swein.shsceneform3dcode.framework.util.view.ViewUtil;


public class SimpleNavigationBarViewHolder {

    public interface SimpleNavigationBarViewHolderDelegate {
        void onLeftClick();
        void onRightClick();
    }

    private View view;

    private ImageView imageViewLeft;
    private ImageView imageViewRight;
    private TextView textViewTitle;
    private View viewBottomLine;

    public SimpleNavigationBarViewHolderDelegate simpleNavigationBarViewHolderDelegate;

    public SimpleNavigationBarViewHolder(Context context) {
        view = ViewUtil.inflateView(context, R.layout.view_holder_simple_navigation_bar, null);
        findView();
        setListener();
    }

    private void findView() {
        imageViewLeft = view.findViewById(R.id.imageViewLeft);
        imageViewRight = view.findViewById(R.id.imageViewRight);
        textViewTitle = view.findViewById(R.id.textViewTitle);
        viewBottomLine = view.findViewById(R.id.viewBottomLine);
    }

    private void setListener() {
        imageViewLeft.setOnClickListener(view -> simpleNavigationBarViewHolderDelegate.onLeftClick());
        imageViewRight.setOnClickListener(view -> simpleNavigationBarViewHolderDelegate.onRightClick());
    }

    public void setImageViewLeft(int resourceId) {
        imageViewLeft.setImageResource(resourceId);
    }

    public void setImageViewRight(int resourceId) {
        imageViewRight.setImageResource(resourceId);
    }

    public void setTitle(String title) {
        textViewTitle.setText(title);
    }

    public void setTitle(int title) {
        textViewTitle.setText(title);
    }

    public View getView() {
        return view;
    }

    public void hideBottomLine() {
        viewBottomLine.setVisibility(View.GONE);
    }

    public View getRightImageView() {
        return imageViewRight;
    }

}
