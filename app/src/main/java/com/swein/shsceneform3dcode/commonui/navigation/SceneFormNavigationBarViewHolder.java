package com.swein.shsceneform3dcode.commonui.navigation;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.swein.shsceneform3dcode.R;
import com.swein.shsceneform3dcode.framework.util.view.ViewUtil;

public class SceneFormNavigationBarViewHolder {

    public interface SceneFormNavigationBarViewHolderDelegate {
        void onMenu();
        void onSearch();
        void onClose();
    }

    private View view;

    private ImageView imageViewMenu;
    private ImageView imageViewSearch;
    private ImageView imageViewClose;

    private TextView textViewTitle;
    private View viewBottomLine;

    public SceneFormNavigationBarViewHolderDelegate sceneFormNavigationBarViewHolderDelegate;

    public SceneFormNavigationBarViewHolder(Context context) {
        view = ViewUtil.inflateView(context, R.layout.view_holder_scene_form_navigation_bar, null);
        findView();
        setListener();
    }

    private void findView() {
        imageViewMenu = view.findViewById(R.id.imageViewMenu);
        imageViewSearch = view.findViewById(R.id.imageViewSearch);
        imageViewClose = view.findViewById(R.id.imageViewClose);
        textViewTitle = view.findViewById(R.id.textViewTitle);
        viewBottomLine = view.findViewById(R.id.viewBottomLine);
    }

    private void setListener() {
        imageViewMenu.setOnClickListener(view -> sceneFormNavigationBarViewHolderDelegate.onMenu());
        imageViewSearch.setOnClickListener(view -> sceneFormNavigationBarViewHolderDelegate.onSearch());
        imageViewClose.setOnClickListener(view -> sceneFormNavigationBarViewHolderDelegate.onClose());
    }

    public void setLeft(int resource) {
        imageViewMenu.setImageResource(resource);
    }

    public void setTitle(String title) {
        textViewTitle.setText(title);
    }

    public View getView() {
        return view;
    }

    public void hideBottomLine() {
        viewBottomLine.setVisibility(View.GONE);
    }

}
