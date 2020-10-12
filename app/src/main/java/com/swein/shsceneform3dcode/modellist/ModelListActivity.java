package com.swein.shsceneform3dcode.modellist;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.button.MaterialButton;
import com.swein.shsceneform3dcode.R;
import com.swein.shsceneform3dcode.bean.ModelWrapperItemBean;
import com.swein.shsceneform3dcode.commonui.navigation.SceneFormNavigationBarViewHolder;
import com.swein.shsceneform3dcode.commonui.popup.SimpleOneInputTwoBottomPopupViewHolder;
import com.swein.shsceneform3dcode.constants.WebConstants;
import com.swein.shsceneform3dcode.framework.module.basicpermission.BasicPermissionActivity;
import com.swein.shsceneform3dcode.framework.util.activity.ActivityUtil;
import com.swein.shsceneform3dcode.framework.util.animation.AnimationUtil;
import com.swein.shsceneform3dcode.framework.util.debug.ILog;
import com.swein.shsceneform3dcode.framework.util.eventsplitshot.eventcenter.EventCenter;
import com.swein.shsceneform3dcode.framework.util.eventsplitshot.subject.ESSArrows;
import com.swein.shsceneform3dcode.framework.util.theme.ThemeUtil;
import com.swein.shsceneform3dcode.framework.util.thread.ThreadUtil;
import com.swein.shsceneform3dcode.model.SceneFormModel;
import com.swein.shsceneform3dcode.modeldetailinfo.ModelDetailInfoActivity;
import com.swein.shsceneform3dcode.modellist.adapter.ModelListAdapter;
import com.swein.shsceneform3dcode.sceneformpart.data.room.bean.RoomBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ModelListActivity extends BasicPermissionActivity {

    private final static String TAG = "ModelListActivity";

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ModelListAdapter modelListAdapter;

    private FrameLayout frameLayoutProgress;

    private SceneFormNavigationBarViewHolder sceneFormNavigationBarViewHolder;
    private FrameLayout frameLayoutNavigationBar;

    private RoomBean roomBean;

    private MaterialButton materialButtonPlus;

    private FrameLayout frameLayoutPopup;
    private SimpleOneInputTwoBottomPopupViewHolder simpleOneInputTwoBottomPopupViewHolder;

    private String testToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI3OTIiLCJyb2xlcyI6WyJST0xFX1VTRVIiXSwiaWF0IjoxNjAwNDI0NTUyLCJleHAiOjE2MzE5NjA1NTJ9.HDiUJ3eepXQLs4OVak7wgF_dgGkNxxOQ4RzY4Vd_XHw";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.activity_from_bottom, R.anim.activity_no_slide);
        setContentView(R.layout.activity_model_list);

        ThemeUtil.setWindowStatusBarColor(this, Color.WHITE);
        ThemeUtil.setAndroidNativeLightStatusBar(this, true, false);

        checkBundle();
        initESS();
        findView();
        initNavigationBar();
        setListener();
        initList();
        reload();

        openDetail();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_no_slide, R.anim.activity_out_bottom);
    }

    private void checkBundle() {
        Bundle bundle = getIntent().getBundleExtra(ActivityUtil.BUNDLE_KEY);
        if(bundle != null) {
            String string = bundle.getString("roomBean", "");
            if(!string.equals("")) {
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
        }
    }

    private void initESS() {
        EventCenter.instance.addEventObserver(ESSArrows.UPDATE_MODEL_FINISHED, this, (arrow, poster, data) -> {
            ThreadUtil.startUIThread(0, this::reload);
        });

        EventCenter.instance.addEventObserver(ESSArrows.EDIT_NAME, this, (arrow, poster, data) -> {
            ModelWrapperItemBean modelWrapperItemBean = (ModelWrapperItemBean) data.get("modelWrapperItemBean");
            showEditNamePopup(modelWrapperItemBean);
        });
    }

    private void findView() {
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        recyclerView = findViewById(R.id.recyclerView);

        frameLayoutNavigationBar = findViewById(R.id.frameLayoutNavigationBar);
        frameLayoutProgress = findViewById(R.id.frameLayoutProgress);

        materialButtonPlus = findViewById(R.id.materialButtonPlus);

        frameLayoutPopup = findViewById(R.id.frameLayoutPopup);
    }

    private void initNavigationBar() {
        sceneFormNavigationBarViewHolder = new SceneFormNavigationBarViewHolder(this);
        sceneFormNavigationBarViewHolder.sceneFormNavigationBarViewHolderDelegate = new SceneFormNavigationBarViewHolder.SceneFormNavigationBarViewHolderDelegate() {
            @Override
            public void onMenu() {

            }

            @Override
            public void onSearch() {

            }

            @Override
            public void onClose() {
                onBackPressed();
            }
        };

        sceneFormNavigationBarViewHolder.setTitle(getString(R.string.scene_form_model_list_title));

        frameLayoutNavigationBar.addView(sceneFormNavigationBarViewHolder.getView());
    }

    private void setListener() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(false);
            reload();
        });

        materialButtonPlus.setOnClickListener(view -> {
            ILog.iLogDebug(TAG, "add");
        });
    }

    private void initList() {
        ILog.iLogDebug(TAG, "initList");

        modelListAdapter = new ModelListAdapter();
        modelListAdapter.modelListAdapterDelegate = this::loadMore;
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(modelListAdapter);
    }

    private void reload() {
        showProgress();

        SceneFormModel.instance.requestSearchModel(testToken, "", String.valueOf(0), String.valueOf(10), new SceneFormModel.SceneFormModelDelegate() {
            @Override
            public void onResponse(String response) {
                ILog.iLogDebug(TAG, response);

                try {
                    if(WebConstants.getIsSuccess(response)) {
                        JSONArray jsonArray = WebConstants.getList(response);

                        List<ModelWrapperItemBean> modelWrapperItemBeanList = new ArrayList<>();
                        ModelWrapperItemBean modelWrapperItemBean;

                        for(int i = 0; i < jsonArray.length(); i++) {

                            modelWrapperItemBean = new ModelWrapperItemBean();
                            modelWrapperItemBean.initWithJSONObject(jsonArray.getJSONObject(i));
                            modelWrapperItemBeanList.add(modelWrapperItemBean);
                        }

                        ThreadUtil.startUIThread(0, () -> {
                            modelListAdapter.reloadList(modelWrapperItemBeanList);
                        });
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }


                ThreadUtil.startUIThread(0, () -> hideProgress());
            }

            @Override
            public void onException(Exception e) {
                e.printStackTrace();
                ThreadUtil.startUIThread(0, () -> hideProgress());
            }
        });
    }

    private void openDetail() {

        if(roomBean == null) {
            return;
        }


        try {
            Bundle bundle = new Bundle();
            bundle.putString("roomBean", roomBean.toJSONObject().toString());
            bundle.putBoolean("isNew", true);
            ActivityUtil.startNewActivityWithoutFinish(this, ModelDetailInfoActivity.class, bundle);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadMore() {
        showProgress();

        SceneFormModel.instance.requestSearchModel(testToken, "", String.valueOf(modelListAdapter.getItemCount()), String.valueOf(10), new SceneFormModel.SceneFormModelDelegate() {
            @Override
            public void onResponse(String response) {
                ILog.iLogDebug(TAG, response);

                try {
                    if(WebConstants.getIsSuccess(response)) {
                        JSONArray jsonArray = WebConstants.getList(response);

                        List<ModelWrapperItemBean> modelWrapperItemBeanList = new ArrayList<>();
                        ModelWrapperItemBean modelWrapperItemBean;

                        for(int i = 0; i < jsonArray.length(); i++) {

                            modelWrapperItemBean = new ModelWrapperItemBean();
                            modelWrapperItemBean.initWithJSONObject(jsonArray.getJSONObject(i));
                            modelWrapperItemBeanList.add(modelWrapperItemBean);
                        }

                        ThreadUtil.startUIThread(0, () -> {
                            modelListAdapter.loadMoreList(modelWrapperItemBeanList);
                        });
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }


                ThreadUtil.startUIThread(0, () -> hideProgress());
            }

            @Override
            public void onException(Exception e) {
                e.printStackTrace();
                ThreadUtil.startUIThread(0, () -> hideProgress());
            }
        });
    }

    private void showEditNamePopup(ModelWrapperItemBean modelWrapperItemBean) {

        frameLayoutPopup.removeAllViews();
        simpleOneInputTwoBottomPopupViewHolder = new SimpleOneInputTwoBottomPopupViewHolder(this);
        simpleOneInputTwoBottomPopupViewHolder.setString(roomBean.name);
        simpleOneInputTwoBottomPopupViewHolder.simpleOneInputTwoBottomPopupViewHolderDelegate = new SimpleOneInputTwoBottomPopupViewHolder.SimpleOneInputTwoBottomPopupViewHolderDelegate() {
            @Override
            public void onConfirm(String name) {

                // TODO edit name

                hideEditNamePopup();
            }

            @Override
            public void onClose() {
                hideEditNamePopup();
            }
        };

        frameLayoutPopup.addView(simpleOneInputTwoBottomPopupViewHolder.getView());
        frameLayoutPopup.startAnimation(AnimationUtil.show(this));
        frameLayoutPopup.setVisibility(View.VISIBLE);
    }

    private boolean hideEditNamePopup() {
        if(simpleOneInputTwoBottomPopupViewHolder != null) {
             frameLayoutPopup.startAnimation(AnimationUtil.hide(this));
             frameLayoutPopup.setVisibility(View.GONE);
             ThreadUtil.startUIThread(300, () -> {
                 frameLayoutPopup.removeAllViews();
                 simpleOneInputTwoBottomPopupViewHolder = null;
             });

             return true;
        }

        return false;
    }

    private void showProgress() {
        frameLayoutProgress.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        frameLayoutProgress.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {

        if(hideEditNamePopup()) {
            return;
        }

        finish();
    }

    private void removeESS() {
        EventCenter.instance.removeAllObserver(this);
    }

    @Override
    protected void onDestroy() {
        removeESS();
        super.onDestroy();
    }
}