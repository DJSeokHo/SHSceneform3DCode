package com.swein.shsceneform3dcode.modeldetailinfo;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.fragment.app.FragmentActivity;

import com.swein.shsceneform3dcode.R;
import com.swein.shsceneform3dcode.commonui.customview.CustomHorizontalScrollViewDisableTouch;
import com.swein.shsceneform3dcode.commonui.navigation.SimpleNavigationBarViewHolder;
import com.swein.shsceneform3dcode.framework.util.activity.ActivityUtil;
import com.swein.shsceneform3dcode.framework.util.debug.ILog;
import com.swein.shsceneform3dcode.framework.util.display.DisplayUtil;
import com.swein.shsceneform3dcode.framework.util.theme.ThemeUtil;
import com.swein.shsceneform3dcode.framework.util.thread.ThreadUtil;
import com.swein.shsceneform3dcode.modeldetailinfo.item.ModelDetailInfoItemViewHolder;
import com.swein.shsceneform3dcode.sceneformpart.SceneFormViewHolder;
import com.swein.shsceneform3dcode.sceneformpart.constants.SFConstants;
import com.swein.shsceneform3dcode.sceneformpart.data.room.bean.RoomBean;
import com.swein.shsceneform3dcode.sceneformpart.tool.MathTool;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ModelDetailInfoActivity extends FragmentActivity {

    private final static String TAG = "ModelDetailInfoActivity";

    private FrameLayout frameLayout3D;
    private FrameLayout frameLayout2D;
    private FrameLayout frameLayoutWall;

    private SimpleNavigationBarViewHolder simpleNavigationBarViewHolder;
    private FrameLayout frameLayoutNavigationBar;

    private SceneFormViewHolder sceneFormViewHolder3D;
    private SceneFormViewHolder sceneFormViewHolder2D;
    private SceneFormViewHolder sceneFormViewHolderWall;

    private RoomBean roomBean;

    private CustomHorizontalScrollViewDisableTouch horizontalScrollView;

    private LinearLayout linearLayoutThreeD;
    private LinearLayout linearLayoutTwoD;
    private LinearLayout linearLayoutWall;

    private boolean canExit = false;

    private Spinner spinner;

    private LinearLayout linearLayoutInfo;

    private List<ModelDetailInfoItemViewHolder> modelDetailInfoItemViewHolderList = new ArrayList<>();

    private String unit;
    private List<String> unitList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.activity_slide_right_in, R.anim.activity_slide_left_out);
        setContentView(R.layout.activity_model_detail_info);

        ThemeUtil.setWindowStatusBarColor(this, Color.WHITE);
        ThemeUtil.setAndroidNativeLightStatusBar(this, true, false);

        checkBundle();
        findView();
        setListener();
        initNavigationBar();
        updateState();
        initView();

        initSceneForm();
        initSpinner();

        linearLayoutThreeD.performClick();
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

        linearLayoutThreeD = findViewById(R.id.linearLayoutThreeD);
        linearLayoutTwoD = findViewById(R.id.linearLayoutTwoD);
        linearLayoutWall = findViewById(R.id.linearLayoutWall);

        linearLayoutInfo = findViewById(R.id.linearLayoutInfo);

        spinner = findViewById(R.id.spinner);
    }

    private void initNavigationBar() {
        simpleNavigationBarViewHolder = new SimpleNavigationBarViewHolder(this);
        simpleNavigationBarViewHolder.simpleNavigationBarViewHolderDelegate = new SimpleNavigationBarViewHolder.SimpleNavigationBarViewHolderDelegate() {
            @Override
            public void onLeftClick() {
                onBackPressed();
            }

            @Override
            public void onRightClick() {

            }
        };

        simpleNavigationBarViewHolder.setImageViewLeft(R.drawable.i_back);
        simpleNavigationBarViewHolder.setImageViewRight(R.drawable.i_more_point);
        simpleNavigationBarViewHolder.setTitle(roomBean.name);

        frameLayoutNavigationBar.addView(simpleNavigationBarViewHolder.getView());
    }

    private void setListener() {
        linearLayoutThreeD.setOnClickListener(view -> {
            resetTab();
            horizontalScrollView.smoothScrollTo((int) frameLayout3D.getX(), 0);
            linearLayoutThreeD.setBackgroundColor(getColor(R.color.light_gray));
        });

        linearLayoutTwoD.setOnClickListener(view -> {
            resetTab();
            horizontalScrollView.smoothScrollTo((int) frameLayout2D.getX(), 0);
            linearLayoutTwoD.setBackgroundColor(getColor(R.color.light_gray));
        });

        linearLayoutWall.setOnClickListener(view -> {
            resetTab();
            horizontalScrollView.smoothScrollTo((int) frameLayoutWall.getX(), 0);
            linearLayoutWall.setBackgroundColor(getColor(R.color.light_gray));
        });
    }

    private void resetTab() {
        linearLayoutThreeD.setBackgroundColor(getColor(R.color.white));
        linearLayoutTwoD.setBackgroundColor(getColor(R.color.white));
        linearLayoutWall.setBackgroundColor(getColor(R.color.white));
    }

    private void initSpinner() {

        unitList.clear();
        unitList.add(getString(R.string.scene_form_unit_m));
        unitList.add(getString(R.string.scene_form_unit_cm));

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.spinner_item_select, unitList.toArray());

        arrayAdapter.setDropDownViewResource(R.layout.spinner_item_drop);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                unit = unitList.get(i);

                if(unit.equals("")) {
                    return;
                }

                ILog.iLogDebug(TAG, unit);

                if(unit.equals(getString(R.string.scene_form_unit_m))) {
                    SFConstants.sfUnit = SFConstants.SFUnit.M;
                }
                else if(unit.equals(getString(R.string.scene_form_unit_cm))) {
                    SFConstants.sfUnit = SFConstants.SFUnit.CM;
                }
                updateInfo();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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

    private void initView() {
        int rect = DisplayUtil.getScreenWidthPx(this) - DisplayUtil.dipToPx(this, 10);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(rect, rect);
        frameLayout3D.setLayoutParams(layoutParams);
        frameLayout2D.setLayoutParams(layoutParams);
        frameLayoutWall.setLayoutParams(layoutParams);
    }

    private void updateInfo() {

        modelDetailInfoItemViewHolderList.clear();
        linearLayoutInfo.removeAllViews();

        ModelDetailInfoItemViewHolder modelDetailInfoItemViewHolder;

        modelDetailInfoItemViewHolder = new ModelDetailInfoItemViewHolder(this);

        SpannableStringBuilder areaString = new SpannableStringBuilder(String.format("%.2f", MathTool.getAreaByUnit(SFConstants.sfUnit, roomBean.area)));
        areaString.append(MathTool.getAreaUnitString(SFConstants.sfUnit)) ;

        modelDetailInfoItemViewHolder.updateView(getString(R.string.scene_form_area_title), areaString);
        modelDetailInfoItemViewHolderList.add(modelDetailInfoItemViewHolder);
        linearLayoutInfo.addView(modelDetailInfoItemViewHolder.getView());


        modelDetailInfoItemViewHolder = new ModelDetailInfoItemViewHolder(this);
        modelDetailInfoItemViewHolder.updateView(
                getString(R.string.scene_form_area_circumference_title),
                String.format("%.2f", MathTool.getLengthByUnit(SFConstants.sfUnit, roomBean.circumference)) + MathTool.getLengthUnitString(SFConstants.sfUnit));
        modelDetailInfoItemViewHolderList.add(modelDetailInfoItemViewHolder);
        linearLayoutInfo.addView(modelDetailInfoItemViewHolder.getView());

        modelDetailInfoItemViewHolder = new ModelDetailInfoItemViewHolder(this);
        modelDetailInfoItemViewHolder.updateView(
                getString(R.string.scene_form_area_height_title),
                String.format("%.2f", MathTool.getLengthByUnit(SFConstants.sfUnit, roomBean.height)) + MathTool.getLengthUnitString(SFConstants.sfUnit)
        );
        modelDetailInfoItemViewHolderList.add(modelDetailInfoItemViewHolder);
        linearLayoutInfo.addView(modelDetailInfoItemViewHolder.getView());

        modelDetailInfoItemViewHolder = new ModelDetailInfoItemViewHolder(this);

        SpannableStringBuilder wallAreaString = new SpannableStringBuilder(String.format("%.2f", MathTool.getAreaByUnit(SFConstants.sfUnit, roomBean.wallArea)));
        wallAreaString.append(MathTool.getAreaUnitString(SFConstants.sfUnit));
        modelDetailInfoItemViewHolder.updateView(getString(R.string.scene_form_wall_area_title), wallAreaString);
        modelDetailInfoItemViewHolderList.add(modelDetailInfoItemViewHolder);
        linearLayoutInfo.addView(modelDetailInfoItemViewHolder.getView());

        modelDetailInfoItemViewHolder = new ModelDetailInfoItemViewHolder(this);
        SpannableStringBuilder volumeString = new SpannableStringBuilder(String.format("%.2f", MathTool.getVolumeByUnit(SFConstants.sfUnit, roomBean.volume)));
        volumeString.append(MathTool.getVolumeUnitString(SFConstants.sfUnit));
        modelDetailInfoItemViewHolder.updateView(getString(R.string.scene_form_volume_title), volumeString);
        modelDetailInfoItemViewHolderList.add(modelDetailInfoItemViewHolder);
        linearLayoutInfo.addView(modelDetailInfoItemViewHolder.getView());

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