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
import com.swein.shsceneform3dcode.commonui.navigation.SceneFormNavigationBarViewHolder;
import com.swein.shsceneform3dcode.framework.module.basicpermission.BasicPermissionActivity;
import com.swein.shsceneform3dcode.framework.util.activity.ActivityUtil;
import com.swein.shsceneform3dcode.framework.util.debug.ILog;
import com.swein.shsceneform3dcode.framework.util.eventsplitshot.eventcenter.EventCenter;
import com.swein.shsceneform3dcode.framework.util.eventsplitshot.subject.ESSArrows;
import com.swein.shsceneform3dcode.framework.util.theme.ThemeUtil;
import com.swein.shsceneform3dcode.framework.util.thread.ThreadUtil;
import com.swein.shsceneform3dcode.modeldetailinfo.ModelDetailInfoActivity;
import com.swein.shsceneform3dcode.modellist.adapter.ModelListAdapter;
import com.swein.shsceneform3dcode.sceneformpart.data.room.bean.RoomBean;

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

    private void initESS() {
        EventCenter.instance.addEventObserver(ESSArrows.UPDATE_2D_IMAGE, this, (arrow, poster, data) -> {

            roomBean.thumbnailImage = (String) data.get("filePath");

            ThreadUtil.startUIThread(0, this::insert);

        });
    }

    private void findView() {
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        recyclerView = findViewById(R.id.recyclerView);

        frameLayoutNavigationBar = findViewById(R.id.frameLayoutNavigationBar);
        frameLayoutProgress = findViewById(R.id.frameLayoutProgress);

        materialButtonPlus = findViewById(R.id.materialButtonPlus);
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

        try {

            String jsonObjectString = "{\"normalVectorOfPlaneX\":\"0.0\",\"normalVectorOfPlaneY\":\"-3.9670887\",\"normalVectorOfPlaneZ\":\"0.0\",\"floor\":{\"pointArray\":[{\"x\":\"0.0\",\"y\":\"0.0\",\"z\":\"0.0\"},{\"x\":\"-0.5171293\",\"y\":\"-5.3942204E-5\",\"z\":\"-0.5819063\"},{\"x\":\"-0.11315587\",\"y\":\"-0.0042389035\",\"z\":\"-1.3167439\"},{\"x\":\"0.64567435\",\"y\":\"-0.03387165\",\"z\":\"-1.1832751\"},{\"x\":\"0.7780203\",\"y\":\"-0.033608437\",\"z\":\"-0.3282633\"},{\"x\":\"-0.02302806\",\"y\":\"-9.544492E-4\",\"z\":\"-0.28135982\"}],\"segmentArray\":[{\"startPoint\":{\"x\":\"0.0\",\"y\":\"0.0\",\"z\":\"0.0\"},\"endPoint\":{\"x\":\"-0.5171293\",\"y\":\"-5.3942204E-5\",\"z\":\"-0.5819063\"},\"length\":\"0.7784842\"},{\"startPoint\":{\"x\":\"-0.5171293\",\"y\":\"-5.3942204E-5\",\"z\":\"-0.5819063\"},\"endPoint\":{\"x\":\"-0.11315587\",\"y\":\"-0.0042389035\",\"z\":\"-1.3167439\"},\"length\":\"0.83856905\"},{\"startPoint\":{\"x\":\"-0.11315587\",\"y\":\"-0.0042389035\",\"z\":\"-1.3167439\"},\"endPoint\":{\"x\":\"0.64567435\",\"y\":\"-0.03387165\",\"z\":\"-1.1832751\"},\"length\":\"0.7710482\"},{\"startPoint\":{\"x\":\"0.64567435\",\"y\":\"-0.03387165\",\"z\":\"-1.1832751\"},\"endPoint\":{\"x\":\"0.7780203\",\"y\":\"-0.033608437\",\"z\":\"-0.3282633\"},\"length\":\"0.86519396\"},{\"startPoint\":{\"x\":\"0.7780203\",\"y\":\"-0.033608437\",\"z\":\"-0.3282633\"},\"endPoint\":{\"x\":\"-0.02302806\",\"y\":\"-9.544492E-4\",\"z\":\"-0.28135982\"},\"length\":\"0.8030845\"},{\"startPoint\":{\"x\":\"-0.02302806\",\"y\":\"-9.544492E-4\",\"z\":\"-0.28135982\"},\"endPoint\":{\"x\":\"0.0\",\"y\":\"0.0\",\"z\":\"0.0\"},\"length\":\"0.2823022\"}],\"type\":\"FLOOR\",\"objectOnIndex\":-1},\"ceiling\":{\"pointArray\":[{\"x\":\"0.0\",\"y\":\"1.25\",\"z\":\"0.0\"},{\"x\":\"-0.5171293\",\"y\":\"1.2499461\",\"z\":\"-0.5819063\"},{\"x\":\"-0.11315587\",\"y\":\"1.2457612\",\"z\":\"-1.3167439\"},{\"x\":\"0.64567435\",\"y\":\"1.2161283\",\"z\":\"-1.1832751\"},{\"x\":\"0.7780203\",\"y\":\"1.2163916\",\"z\":\"-0.3282633\"},{\"x\":\"-0.02302806\",\"y\":\"1.2490456\",\"z\":\"-0.28135982\"}],\"segmentArray\":[{\"startPoint\":{\"x\":\"0.0\",\"y\":\"1.25\",\"z\":\"0.0\"},\"endPoint\":{\"x\":\"-0.5171293\",\"y\":\"1.2499461\",\"z\":\"-0.5819063\"},\"length\":\"0.77848417\"},{\"startPoint\":{\"x\":\"-0.5171293\",\"y\":\"1.2499461\",\"z\":\"-0.5819063\"},\"endPoint\":{\"x\":\"-0.11315587\",\"y\":\"1.2457612\",\"z\":\"-1.3167439\"},\"length\":\"0.83856916\"},{\"startPoint\":{\"x\":\"-0.11315587\",\"y\":\"1.2457612\",\"z\":\"-1.3167439\"},\"endPoint\":{\"x\":\"0.64567435\",\"y\":\"1.2161283\",\"z\":\"-1.1832751\"},\"length\":\"0.7710482\"},{\"startPoint\":{\"x\":\"0.64567435\",\"y\":\"1.2161283\",\"z\":\"-1.1832751\"},\"endPoint\":{\"x\":\"0.7780203\",\"y\":\"1.2163916\",\"z\":\"-0.3282633\"},\"length\":\"0.86519384\"},{\"startPoint\":{\"x\":\"0.7780203\",\"y\":\"1.2163916\",\"z\":\"-0.3282633\"},\"endPoint\":{\"x\":\"-0.02302806\",\"y\":\"1.2490456\",\"z\":\"-0.28135982\"},\"length\":\"0.80308455\"},{\"startPoint\":{\"x\":\"-0.02302806\",\"y\":\"1.2490456\",\"z\":\"-0.28135982\"},\"endPoint\":{\"x\":\"0.0\",\"y\":\"1.25\",\"z\":\"0.0\"},\"length\":\"0.2823022\"}],\"type\":\"CEILING\",\"objectOnIndex\":-1},\"wallArray\":[{\"pointArray\":[{\"x\":\"0.0\",\"y\":\"0.0\",\"z\":\"0.0\"},{\"x\":\"-0.5171293\",\"y\":\"-5.3942204E-5\",\"z\":\"-0.5819063\"},{\"x\":\"-0.5171293\",\"y\":\"1.2499461\",\"z\":\"-0.5819063\"},{\"x\":\"0.0\",\"y\":\"1.25\",\"z\":\"0.0\"}],\"segmentArray\":[{\"startPoint\":{\"x\":\"0.0\",\"y\":\"0.0\",\"z\":\"0.0\"},\"endPoint\":{\"x\":\"-0.5171293\",\"y\":\"-5.3942204E-5\",\"z\":\"-0.5819063\"},\"length\":\"0.7784842\"},{\"startPoint\":{\"x\":\"-0.5171293\",\"y\":\"-5.3942204E-5\",\"z\":\"-0.5819063\"},\"endPoint\":{\"x\":\"-0.5171293\",\"y\":\"1.2499461\",\"z\":\"-0.5819063\"},\"length\":\"1.2500002\"},{\"startPoint\":{\"x\":\"-0.5171293\",\"y\":\"1.2499461\",\"z\":\"-0.5819063\"},\"endPoint\":{\"x\":\"0.0\",\"y\":\"1.25\",\"z\":\"0.0\"},\"length\":\"0.77848417\"},{\"startPoint\":{\"x\":\"0.0\",\"y\":\"1.25\",\"z\":\"0.0\"},\"endPoint\":{\"x\":\"0.0\",\"y\":\"0.0\",\"z\":\"0.0\"},\"length\":\"1.25\"}],\"type\":\"WALL\",\"objectOnIndex\":-1},{\"pointArray\":[{\"x\":\"-0.5171293\",\"y\":\"-5.3942204E-5\",\"z\":\"-0.5819063\"},{\"x\":\"-0.11315587\",\"y\":\"-0.0042389035\",\"z\":\"-1.3167439\"},{\"x\":\"-0.11315587\",\"y\":\"1.2457612\",\"z\":\"-1.3167439\"},{\"x\":\"-0.5171293\",\"y\":\"1.2499461\",\"z\":\"-0.5819063\"}],\"segmentArray\":[{\"startPoint\":{\"x\":\"-0.5171293\",\"y\":\"-5.3942204E-5\",\"z\":\"-0.5819063\"},\"endPoint\":{\"x\":\"-0.11315587\",\"y\":\"-0.0042389035\",\"z\":\"-1.3167439\"},\"length\":\"0.83856905\"},{\"startPoint\":{\"x\":\"-0.11315587\",\"y\":\"-0.0042389035\",\"z\":\"-1.3167439\"},\"endPoint\":{\"x\":\"-0.11315587\",\"y\":\"1.2457612\",\"z\":\"-1.3167439\"},\"length\":\"1.25\"},{\"startPoint\":{\"x\":\"-0.11315587\",\"y\":\"1.2457612\",\"z\":\"-1.3167439\"},\"endPoint\":{\"x\":\"-0.5171293\",\"y\":\"1.2499461\",\"z\":\"-0.5819063\"},\"length\":\"0.83856916\"},{\"startPoint\":{\"x\":\"-0.5171293\",\"y\":\"1.2499461\",\"z\":\"-0.5819063\"},\"endPoint\":{\"x\":\"-0.5171293\",\"y\":\"-5.3942204E-5\",\"z\":\"-0.5819063\"},\"length\":\"1.2500002\"}],\"type\":\"WALL\",\"objectOnIndex\":-1},{\"pointArray\":[{\"x\":\"-0.11315587\",\"y\":\"-0.0042389035\",\"z\":\"-1.3167439\"},{\"x\":\"0.64567435\",\"y\":\"-0.03387165\",\"z\":\"-1.1832751\"},{\"x\":\"0.64567435\",\"y\":\"1.2161283\",\"z\":\"-1.1832751\"},{\"x\":\"-0.11315587\",\"y\":\"1.2457612\",\"z\":\"-1.3167439\"}],\"segmentArray\":[{\"startPoint\":{\"x\":\"-0.11315587\",\"y\":\"-0.0042389035\",\"z\":\"-1.3167439\"},\"endPoint\":{\"x\":\"0.64567435\",\"y\":\"-0.03387165\",\"z\":\"-1.1832751\"},\"length\":\"0.7710482\"},{\"startPoint\":{\"x\":\"0.64567435\",\"y\":\"-0.03387165\",\"z\":\"-1.1832751\"},\"endPoint\":{\"x\":\"0.64567435\",\"y\":\"1.2161283\",\"z\":\"-1.1832751\"},\"length\":\"1.25\"},{\"startPoint\":{\"x\":\"0.64567435\",\"y\":\"1.2161283\",\"z\":\"-1.1832751\"},\"endPoint\":{\"x\":\"-0.11315587\",\"y\":\"1.2457612\",\"z\":\"-1.3167439\"},\"length\":\"0.7710482\"},{\"startPoint\":{\"x\":\"-0.11315587\",\"y\":\"1.2457612\",\"z\":\"-1.3167439\"},\"endPoint\":{\"x\":\"-0.11315587\",\"y\":\"-0.0042389035\",\"z\":\"-1.3167439\"},\"length\":\"1.25\"}],\"type\":\"WALL\",\"objectOnIndex\":-1},{\"pointArray\":[{\"x\":\"0.64567435\",\"y\":\"-0.03387165\",\"z\":\"-1.1832751\"},{\"x\":\"0.7780203\",\"y\":\"-0.033608437\",\"z\":\"-0.3282633\"},{\"x\":\"0.7780203\",\"y\":\"1.2163916\",\"z\":\"-0.3282633\"},{\"x\":\"0.64567435\",\"y\":\"1.2161283\",\"z\":\"-1.1832751\"}],\"segmentArray\":[{\"startPoint\":{\"x\":\"0.64567435\",\"y\":\"-0.03387165\",\"z\":\"-1.1832751\"},\"endPoint\":{\"x\":\"0.7780203\",\"y\":\"-0.033608437\",\"z\":\"-0.3282633\"},\"length\":\"0.86519396\"},{\"startPoint\":{\"x\":\"0.7780203\",\"y\":\"-0.033608437\",\"z\":\"-0.3282633\"},\"endPoint\":{\"x\":\"0.7780203\",\"y\":\"1.2163916\",\"z\":\"-0.3282633\"},\"length\":\"1.25\"},{\"startPoint\":{\"x\":\"0.7780203\",\"y\":\"1.2163916\",\"z\":\"-0.3282633\"},\"endPoint\":{\"x\":\"0.64567435\",\"y\":\"1.2161283\",\"z\":\"-1.1832751\"},\"length\":\"0.86519384\"},{\"startPoint\":{\"x\":\"0.64567435\",\"y\":\"1.2161283\",\"z\":\"-1.1832751\"},\"endPoint\":{\"x\":\"0.64567435\",\"y\":\"-0.03387165\",\"z\":\"-1.1832751\"},\"length\":\"1.25\"}],\"type\":\"WALL\",\"objectOnIndex\":-1},{\"pointArray\":[{\"x\":\"0.7780203\",\"y\":\"-0.033608437\",\"z\":\"-0.3282633\"},{\"x\":\"-0.02302806\",\"y\":\"-9.544492E-4\",\"z\":\"-0.28135982\"},{\"x\":\"-0.02302806\",\"y\":\"1.2490456\",\"z\":\"-0.28135982\"},{\"x\":\"0.7780203\",\"y\":\"1.2163916\",\"z\":\"-0.3282633\"}],\"segmentArray\":[{\"startPoint\":{\"x\":\"0.7780203\",\"y\":\"-0.033608437\",\"z\":\"-0.3282633\"},\"endPoint\":{\"x\":\"-0.02302806\",\"y\":\"-9.544492E-4\",\"z\":\"-0.28135982\"},\"length\":\"0.8030845\"},{\"startPoint\":{\"x\":\"-0.02302806\",\"y\":\"-9.544492E-4\",\"z\":\"-0.28135982\"},\"endPoint\":{\"x\":\"-0.02302806\",\"y\":\"1.2490456\",\"z\":\"-0.28135982\"},\"length\":\"1.2500002\"},{\"startPoint\":{\"x\":\"-0.02302806\",\"y\":\"1.2490456\",\"z\":\"-0.28135982\"},\"endPoint\":{\"x\":\"0.7780203\",\"y\":\"1.2163916\",\"z\":\"-0.3282633\"},\"length\":\"0.80308455\"},{\"startPoint\":{\"x\":\"0.7780203\",\"y\":\"1.2163916\",\"z\":\"-0.3282633\"},\"endPoint\":{\"x\":\"0.7780203\",\"y\":\"-0.033608437\",\"z\":\"-0.3282633\"},\"length\":\"1.25\"}],\"type\":\"WALL\",\"objectOnIndex\":-1},{\"pointArray\":[{\"x\":\"-0.02302806\",\"y\":\"-9.544492E-4\",\"z\":\"-0.28135982\"},{\"x\":\"0.0\",\"y\":\"0.0\",\"z\":\"0.0\"},{\"x\":\"0.0\",\"y\":\"1.25\",\"z\":\"0.0\"},{\"x\":\"-0.02302806\",\"y\":\"1.2490456\",\"z\":\"-0.28135982\"}],\"segmentArray\":[{\"startPoint\":{\"x\":\"-0.02302806\",\"y\":\"-9.544492E-4\",\"z\":\"-0.28135982\"},\"endPoint\":{\"x\":\"0.0\",\"y\":\"0.0\",\"z\":\"0.0\"},\"length\":\"0.2823022\"},{\"startPoint\":{\"x\":\"0.0\",\"y\":\"0.0\",\"z\":\"0.0\"},\"endPoint\":{\"x\":\"0.0\",\"y\":\"1.25\",\"z\":\"0.0\"},\"length\":\"1.25\"},{\"startPoint\":{\"x\":\"0.0\",\"y\":\"1.25\",\"z\":\"0.0\"},\"endPoint\":{\"x\":\"-0.02302806\",\"y\":\"1.2490456\",\"z\":\"-0.28135982\"},\"length\":\"0.2823022\"},{\"startPoint\":{\"x\":\"-0.02302806\",\"y\":\"1.2490456\",\"z\":\"-0.28135982\"},\"endPoint\":{\"x\":\"-0.02302806\",\"y\":\"-9.544492E-4\",\"z\":\"-0.28135982\"},\"length\":\"1.2500002\"}],\"type\":\"WALL\",\"objectOnIndex\":-1}],\"wallObjectArray\":[{\"pointArray\":[{\"x\":\"-0.27837884\",\"y\":\"0.8662507\",\"z\":\"-0.31325102\"},{\"x\":\"-0.038723323\",\"y\":\"0.8610536\",\"z\":\"-0.053757608\"},{\"x\":\"-0.045541458\",\"y\":\"0.6716971\",\"z\":\"-0.05125308\"},{\"x\":\"-0.285197\",\"y\":\"0.6768941\",\"z\":\"-0.3107465\"}],\"segmentArray\":[{\"startPoint\":{\"x\":\"-0.27837884\",\"y\":\"0.8662507\",\"z\":\"-0.31325102\"},\"endPoint\":{\"x\":\"-0.038723323\",\"y\":\"0.8610536\",\"z\":\"-0.053757608\"},\"length\":\"0.35326844\"},{\"startPoint\":{\"x\":\"-0.038723323\",\"y\":\"0.8610536\",\"z\":\"-0.053757608\"},\"endPoint\":{\"x\":\"-0.045541458\",\"y\":\"0.6716971\",\"z\":\"-0.05125308\"},\"length\":\"0.18949574\"},{\"startPoint\":{\"x\":\"-0.045541458\",\"y\":\"0.6716971\",\"z\":\"-0.05125308\"},\"endPoint\":{\"x\":\"-0.285197\",\"y\":\"0.6768941\",\"z\":\"-0.3107465\"},\"length\":\"0.35326844\"},{\"startPoint\":{\"x\":\"-0.285197\",\"y\":\"0.6768941\",\"z\":\"-0.3107465\"},\"endPoint\":{\"x\":\"-0.27837884\",\"y\":\"0.8662507\",\"z\":\"-0.31325102\"},\"length\":\"0.1894958\"}],\"type\":\"WINDOW\",\"objectOnIndex\":0},{\"pointArray\":[{\"x\":\"-0.3407782\",\"y\":\"0.45481724\",\"z\":\"-0.38346422\"},{\"x\":\"-0.08652692\",\"y\":\"0.44926023\",\"z\":\"-0.11145434\"},{\"x\":\"-0.095966645\",\"y\":\"0.18709564\",\"z\":\"-0.10798681\"},{\"x\":\"-0.35021797\",\"y\":\"0.1926527\",\"z\":\"-0.37999672\"}],\"segmentArray\":[{\"startPoint\":{\"x\":\"-0.3407782\",\"y\":\"0.45481724\",\"z\":\"-0.38346422\"},\"endPoint\":{\"x\":\"-0.08652692\",\"y\":\"0.44926023\",\"z\":\"-0.11145434\"},\"length\":\"0.3723761\"},{\"startPoint\":{\"x\":\"-0.08652692\",\"y\":\"0.44926023\",\"z\":\"-0.11145434\"},\"endPoint\":{\"x\":\"-0.095966645\",\"y\":\"0.18709564\",\"z\":\"-0.10798681\"},\"length\":\"0.26235738\"},{\"startPoint\":{\"x\":\"-0.095966645\",\"y\":\"0.18709564\",\"z\":\"-0.10798681\"},\"endPoint\":{\"x\":\"-0.35021797\",\"y\":\"0.1926527\",\"z\":\"-0.37999672\"},\"length\":\"0.37237617\"},{\"startPoint\":{\"x\":\"-0.35021797\",\"y\":\"0.1926527\",\"z\":\"-0.37999672\"},\"endPoint\":{\"x\":\"-0.3407782\",\"y\":\"0.45481724\",\"z\":\"-0.38346422\"},\"length\":\"0.26235732\"}],\"type\":\"WINDOW\",\"objectOnIndex\":0},{\"pointArray\":[{\"x\":\"0.10962978\",\"y\":\"0.8361199\",\"z\":\"-0.28914434\"},{\"x\":\"0.49427593\",\"y\":\"0.8218517\",\"z\":\"-0.32076973\"},{\"x\":\"0.46474317\",\"y\":\"0.0016527772\",\"z\":\"-0.30992132\"},{\"x\":\"0.08009699\",\"y\":\"0.015920997\",\"z\":\"-0.278296\"}],\"segmentArray\":[{\"startPoint\":{\"x\":\"0.10962978\",\"y\":\"0.8361199\",\"z\":\"-0.28914434\"},\"endPoint\":{\"x\":\"0.49427593\",\"y\":\"0.8218517\",\"z\":\"-0.32076973\"},\"length\":\"0.38620773\"},{\"startPoint\":{\"x\":\"0.49427593\",\"y\":\"0.8218517\",\"z\":\"-0.32076973\"},\"endPoint\":{\"x\":\"0.46474317\",\"y\":\"0.0016527772\",\"z\":\"-0.30992132\"},\"length\":\"0.8208021\"},{\"startPoint\":{\"x\":\"0.46474317\",\"y\":\"0.0016527772\",\"z\":\"-0.30992132\"},\"endPoint\":{\"x\":\"0.08009699\",\"y\":\"0.015920997\",\"z\":\"-0.278296\"},\"length\":\"0.38620776\"},{\"startPoint\":{\"x\":\"0.08009699\",\"y\":\"0.015920997\",\"z\":\"-0.278296\"},\"endPoint\":{\"x\":\"0.10962978\",\"y\":\"0.8361199\",\"z\":\"-0.28914434\"},\"length\":\"0.8208021\"}],\"type\":\"DOOR\",\"objectOnIndex\":4}],\"height\":\"1.25\",\"floorFixedY\":\"-0.8456003\",\"area\":\"1.0410913\",\"circumference\":\"4.338682\",\"wallArea\":\"5.4233522\",\"volume\":\"1.3013642\",\"name\":\"ㅈㅈㅈ\",\"unit\":\"m\"}";
            JSONObject jsonObject = new JSONObject(jsonObjectString);

            RoomBean roomBean = new RoomBean();
            roomBean.init(jsonObject);

            List<RoomBean> list = new ArrayList<>();
            list.add(roomBean);
            modelListAdapter.reloadList(list);

            if(this.roomBean != null) {
                openDetail();
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void insert() {
        modelListAdapter.insert(roomBean);
    }

    private void openDetail() throws JSONException {
        String string = roomBean.toJSONObject().toString();
        Bundle bundle = new Bundle();
        bundle.putString("roomBean", string);
        ActivityUtil.startNewActivityWithoutFinish(this, ModelDetailInfoActivity.class, bundle);
    }

    private void loadMore() {

    }

    private void showProgress() {
        frameLayoutProgress.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        frameLayoutProgress.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}