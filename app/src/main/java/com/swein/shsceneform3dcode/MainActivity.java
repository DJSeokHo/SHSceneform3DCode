package com.swein.shsceneform3dcode;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.fragment.app.FragmentActivity;

import com.swein.shsceneform3dcode.sceneformpart.SceneFormViewHolder;

public class MainActivity extends FragmentActivity {

    private final static String TAG = "MainActivity";

    private FrameLayout frameLayout;
    private SceneFormViewHolder sceneFormViewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findView();
        initSceneForm();
    }

    @Override
    protected void onPause() {
        if(sceneFormViewHolder != null) {
            sceneFormViewHolder.pause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(sceneFormViewHolder != null) {
            sceneFormViewHolder.resume();
        }
    }

    private void findView() {
        frameLayout = findViewById(R.id.frameLayout);
    }

    private void initSceneForm() {
        sceneFormViewHolder = new SceneFormViewHolder(this);
        frameLayout.addView(sceneFormViewHolder.view);
    }

    @Override
    protected void onDestroy() {

        if(sceneFormViewHolder != null) {
            sceneFormViewHolder.destroy();
            sceneFormViewHolder = null;
        }
        super.onDestroy();
    }
}