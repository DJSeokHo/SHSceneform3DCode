package com.swein.shsceneform3dcode.modeldetailinfo;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.fragment.app.FragmentActivity;

import com.swein.shsceneform3dcode.R;
import com.swein.shsceneform3dcode.commonui.customview.CustomHorizontalScrollViewDisableTouch;
import com.swein.shsceneform3dcode.framework.util.activity.ActivityUtil;
import com.swein.shsceneform3dcode.framework.util.theme.ThemeUtil;
import com.swein.shsceneform3dcode.framework.util.thread.ThreadUtil;
import com.swein.shsceneform3dcode.sceneformpart.SceneFormViewHolder;
import com.swein.shsceneform3dcode.sceneformpart.data.room.bean.RoomBean;

import org.json.JSONException;
import org.json.JSONObject;

public class ModelDetailInfoActivity extends FragmentActivity {

    private final static String TAG = "ModelDetailInfoActivity";

    private FrameLayout frameLayout3D;
    private FrameLayout frameLayout2D;
    private FrameLayout frameLayoutWall;

    private FrameLayout frameLayoutNavigationBar;

    private SceneFormViewHolder sceneFormViewHolder3D;
    private SceneFormViewHolder sceneFormViewHolder2D;
    private SceneFormViewHolder sceneFormViewHolderWall;

    private RoomBean roomBean;

    private CustomHorizontalScrollViewDisableTouch horizontalScrollView;

    private boolean canExit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.activity_slide_right_in, R.anim.activity_slide_left_out);
        setContentView(R.layout.activity_model_detail_info);

        ThemeUtil.setWindowStatusBarColor(this, Color.WHITE);
        ThemeUtil.setAndroidNativeLightStatusBar(this, true, false);

        checkBundle();
        findView();
        initNavigationBar();
        initSceneForm();
        updateState();


        ThreadUtil.startUIThread(1000, () -> {
            horizontalScrollView.smoothScrollTo(500, 0);
        });
    }

    @Override
    public void finish() {
        super.finish();
        // when activity finish, current activity will slide_right_out(left in screen to right)
        // and old activity will slide_left_in(show in screen from left)
        overridePendingTransition(R.anim.activity_slide_left_in, R.anim.activity_slide_right_out);
    }

    private void checkBundle() {
        Bundle bundle = getIntent().getBundleExtra(ActivityUtil.BUNDLE_KEY);
        if(bundle != null) {
            String string = bundle.getString("roomBean", "");
            if(string.equals("")) {
                finish();
            }

            try {
                JSONObject jsonObject = new JSONObject(string);
                roomBean = new RoomBean();
                roomBean.init(jsonObject);
            }
            catch (JSONException e) {
                e.printStackTrace();
                finish();
            }
        }
        else {
            finish();
        }
    }

    private void findView() {

        frameLayoutNavigationBar = findViewById(R.id.frameLayoutNavigationBar);
        horizontalScrollView = findViewById(R.id.horizontalScrollView);

        frameLayout3D = findViewById(R.id.frameLayout3D);
        frameLayout2D = findViewById(R.id.frameLayout2D);
        frameLayoutWall = findViewById(R.id.frameLayoutWall);
    }

    private void initNavigationBar() {

    }

    private void initSceneForm() {

        sceneFormViewHolder3D = new SceneFormViewHolder(this, roomBean, SceneFormViewHolder.TYPE_3D, null);
        frameLayout3D.addView(sceneFormViewHolder3D.view);

        sceneFormViewHolder2D = new SceneFormViewHolder(this, roomBean, SceneFormViewHolder.TYPE_2D, () -> {
            if(roomBean.thumbnailImage.equals("")) {

                ThreadUtil.startThread(() -> {
                    try {
                        Thread.sleep(1000);

                        ThreadUtil.startUIThread(0, this::captureThumbnailImage);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            }
        });
        frameLayout2D.addView(sceneFormViewHolder2D.view);

        sceneFormViewHolderWall = new SceneFormViewHolder(this, roomBean, SceneFormViewHolder.TYPE_WALL, null);
        frameLayoutWall.addView(sceneFormViewHolderWall.view);
    }

    private void captureThumbnailImage() {
        if(sceneFormViewHolder2D != null) {
            sceneFormViewHolder2D.captureThumbnailImage(this::finish);

            canExit = true;
        }
    }

    private void updateState() {
        if(!roomBean.thumbnailImage.equals("")) {
            canExit = true;
        }
        else {
            canExit = false;
        }
    }

    @Override
    public void onBackPressed() {

        if(canExit) {
            finish();
        }

    }

    @Override
    protected void onPause() {
        if(sceneFormViewHolder3D != null) {
            sceneFormViewHolder3D.pause();
        }

        if(sceneFormViewHolder2D != null) {
            sceneFormViewHolder2D.pause();
        }

        if(sceneFormViewHolderWall != null) {
            sceneFormViewHolderWall.pause();
        }

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(sceneFormViewHolder3D != null) {
            sceneFormViewHolder3D.resume();
        }

        if(sceneFormViewHolder2D != null) {
            sceneFormViewHolder2D.resume();
        }

        if(sceneFormViewHolderWall != null) {
            sceneFormViewHolderWall.resume();
        }
    }

    @Override
    protected void onDestroy() {

        if(sceneFormViewHolder3D != null) {
            sceneFormViewHolder3D.destroy();
            sceneFormViewHolder3D = null;
        }

        if(sceneFormViewHolder2D != null) {
            sceneFormViewHolder2D.destroy();
            sceneFormViewHolder2D = null;
        }

        if(sceneFormViewHolderWall != null) {
            sceneFormViewHolderWall.destroy();
            sceneFormViewHolderWall = null;
        }
        super.onDestroy();
    }
}