package com.swein.shsceneform3dcode.sceneformpart;

import android.content.Context;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;

import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Camera;
import com.google.ar.sceneform.HitTestResult;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.SceneView;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.swein.shsceneform3dcode.R;
import com.swein.shsceneform3dcode.framework.debug.ILog;
import com.swein.shsceneform3dcode.framework.util.view.ViewUtil;
import com.swein.shsceneform3dcode.sceneformpart.data.room.bean.RoomBean;
import com.swein.shsceneform3dcode.sceneformpart.material.SFMaterial;
import com.swein.shsceneform3dcode.sceneformpart.data.room.model.RoomModel;
import com.swein.shsceneform3dcode.sceneformpart.renderable.SFRenderable;

import org.json.JSONException;
import org.json.JSONObject;

public class SceneFormViewHolder {

    public final static int TYPE_3D = 0;
    public final static int TYPE_2D = 1;
    public final static int TYPE_WALL = 2;

    private final static String TAG = "SceneFormViewHolder";

    public View view;

    private SceneView sceneView;

    private RoomBean roomBean;
    private RoomModel roomModel;

    private String jsonObjectString;

    private int type;

    public SceneFormViewHolder(Context context, String jsonObjectString, int type) {
        view = ViewUtil.inflateView(context, R.layout.view_holder_scene_form, null);
        this.type = type;
        this.jsonObjectString = jsonObjectString;

        findView();
        initSceneForm();

        setCameraPosition(0f, 0f, 4f);
        setCameraRange(0.0001f, 10);

        SFMaterial.instance.init(view.getContext(), () -> {
            SFRenderable.instance.init(view.getContext(), () -> {

                createRoomBean();
                createAnchorNode();

                RoomBean tempRoomBean = createTempRoomBean(roomBean.centerPoint.x, roomBean.centerPoint.y, roomBean.centerPoint.z);
                updateView(tempRoomBean);
            });
        });
    }

    private void findView() {
        sceneView = view.findViewById(R.id.sceneView);
    }

    private void setCameraPosition(float x, float y, float z) {
        Camera camera = sceneView.getScene().getCamera();
        camera.setWorldPosition(new Vector3(x, y, z));
    }

    private void setCameraRange(float near, float far) {

        Camera camera = sceneView.getScene().getCamera();
        camera.setNearClipPlane(near);
        camera.setFarClipPlane(far);
    }

    private AnchorNode anchorNode;

    // prev rotation state
    private float prevXAngle = 0;
    private float prevYAngle = 0;

    // prev movement state
    private float prevXMove = 0;
    private float prevYMove = 0;

    private void initSceneForm() {

        sceneView.setBackgroundColor(Color.WHITE);

        sceneView.getScene().addOnPeekTouchListener(new Scene.OnPeekTouchListener() {

            float downX = 0;
            float downY = 0;

            boolean isScale = false;
            float scale = 0;

            float tempDistance = 0;

            float xAngle = 0;
            float yAngle = 0;

            float xMove = 0;
            float yMove = 0;

            @Override
            public void onPeekTouch(HitTestResult hitTestResult, MotionEvent motionEvent) {

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {

                        downX = motionEvent.getX();
                        downY = motionEvent.getY();

                        break;
                    }
                    case MotionEvent.ACTION_MOVE:

                        if (motionEvent.getPointerCount() == 1) {

                            if (isScale) {
                                return;
                            }

                            if(type != TYPE_3D) {

                                if (Math.abs(motionEvent.getX() - downX) > 40 || Math.abs(motionEvent.getY() - downY) > 40) {

                                    // re-center point
                                    // let view center as touch center point
//                                float x = motionEvent.getX() - sceneView.getWidth() * 0.5f;
//                                float y = motionEvent.getY() - sceneView.getHeight() * 0.5f;

                                    // let touched point as touch center point
                                    float x = motionEvent.getX() - downX;
                                    float y = motionEvent.getY() - downY;
                                    // re-center point

                                    float percentX = x / (sceneView.getWidth() * 0.5f);
                                    float percentY = y / (sceneView.getHeight() * 0.5f);

                                    xMove = percentX + prevXMove;
                                    yMove = percentY + prevYMove;
//                                    anchorNode.setWorldPosition(new Vector3(percentX, -percentY, 0));

                                    Camera camera = sceneView.getScene().getCamera();
                                    camera.setWorldPosition(new Vector3(-xMove, yMove, camera.getWorldPosition().z));
                                }

                                return;
                            }

                            if (Math.abs(motionEvent.getX() - downX) > 40 || Math.abs(motionEvent.getY() - downY) > 40) {

                                // re-center point
                                // let view center as touch center point
//                                float x = motionEvent.getX() - sceneView.getWidth() * 0.5f;
//                                float y = motionEvent.getY() - sceneView.getHeight() * 0.5f;

                                // let touched point as touch center point
                                float x = motionEvent.getX() - downX;
                                float y = motionEvent.getY() - downY;
                                // re-center point

                                float percentX = x / (sceneView.getWidth() * 0.5f);
                                float percentY = y / (sceneView.getHeight() * 0.5f);

//                                anchorNode.getWorldRotation();

                                xAngle = percentX * 360 * 0.25f + prevXAngle;
                                yAngle = percentY * 360 * 0.25f + prevYAngle;

                                Quaternion xQuaternion = Quaternion.axisAngle(new Vector3(0.0f, 1.0f, 0.0f), xAngle);
                                Quaternion yQuaternion = Quaternion.axisAngle(new Vector3(1.0f, 0.0f, 0.0f), yAngle);

                                anchorNode.setWorldRotation(Quaternion.multiply(xQuaternion, yQuaternion));
                            }

                        }
                        else if (motionEvent.getPointerCount() == 2) {

                            isScale = true;

                            // =================== check is plus(zoom in) or minus(zoom out) =====================
                            float distance = motionEventDistance(motionEvent);
                            if (tempDistance == 0) {
                                tempDistance = distance;
                                return;
                            }

                            if (0 == scale) {
                                scale = anchorNode.getWorldScale().x;
                            }

                            float screenViewPercent = motionEventDistance(motionEvent) / sceneView.getWidth();

                            if (distance > tempDistance) {
                                // plus(zoom in)
                                scale += screenViewPercent * 0.05f;
                            }
                            else if (distance < tempDistance) {
                                // minus(zoom out)
                                scale -= screenViewPercent * 0.05f;
                            }
                            // =================== check is plus(zoom in) or minus(zoom out) =====================

                            // =================== set scale limit ===================
                            if (scale <= 0.5) {
                                scale = 0.5f;
                            }
                            else if (scale > 4) {
                                scale = 4;
                            }
                            // =================== set scale limit ===================

                            anchorNode.setWorldScale(new Vector3(scale, scale, scale));
                        }

                        break;

                    case MotionEvent.ACTION_UP:

                        if(type != TYPE_3D) {
                            prevXMove = xMove;
                            prevYMove = yMove;
                        }

                        if(isScale) {
                            // scale
                            scale = 0;
                            tempDistance = 0;
                        }
                        else {
                            // rotation
                            prevXAngle = xAngle;
                            prevYAngle = yAngle;
                        }

                        isScale = false;

                        break;
                }
            }
        });

        sceneView.getScene().addOnUpdateListener(frameTime -> {

        });
    }


    private float motionEventDistance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float)Math.sqrt(x * x + y * y);
    }

    private void createRoomBean() {
        try {
            JSONObject jsonObject = new JSONObject(jsonObjectString);

            roomBean = new RoomBean();
            roomBean.init(jsonObject);

            if(type == TYPE_3D) {
                roomBean.calculate3DModelCenterPoint();
            }
            else if(type == TYPE_2D) {
                roomBean.calculate2DModelCenterPoint();
            }
            else if(type == TYPE_WALL) {
                roomBean.calculateWallCenterPoint();
            }

            ILog.iLogDebug(TAG, roomBean.toString());

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void createAnchorNode() {

        if(anchorNode == null) {
            anchorNode = new AnchorNode();
        }

        anchorNode.setWorldPosition(new Vector3(0f, 0f, 0f));
        anchorNode.setParent(sceneView.getScene());

    }

    private RoomBean createTempRoomBean(float xAvg, float yAvg, float zAvg) {
        RoomBean roomBean = new RoomBean();
        try {
            roomBean.init(this.roomBean.toJSONObject(), xAvg, yAvg, zAvg);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return roomBean;
    }

    private void updateView(RoomBean tempRoomBean) {

        if(type == TYPE_3D) {
            create3DModel(tempRoomBean);
        }
        else if(type == TYPE_2D) {
            create2DModel(tempRoomBean);
        }
        else if(type == TYPE_WALL) {

            RoomBean wallRoomBean = tempRoomBean.createWallModel();
            createWallModel(wallRoomBean);
        }

    }

    private void create3DModel(RoomBean tempRoomBean) {

        // re set camera
        float z = roomBean.height;
        for(int i = 0; i < roomBean.floorPlaneBean.segmentBeanList.size(); i++) {
            if(roomBean.floorPlaneBean.segmentBeanList.get(i).length > z) {
                z = roomBean.floorPlaneBean.segmentBeanList.get(i).length;
            }
        }

        setCameraPosition(0f, 0f, z * 1.5f);
        setCameraRange(0.0001f, z * 10f);
        // re set camera


        roomModel = new RoomModel(tempRoomBean);
        roomModel.context = view.getContext();
        roomModel.create3DModel(anchorNode);
        roomModel.create3DSizeSymbol();
    }

    private void create2DModel(RoomBean tempRoomBean) {

        // re set camera
        float z = roomBean.height;
        for(int i = 0; i < roomBean.floorPlaneBean.segmentBeanList.size(); i++) {
            if(roomBean.floorPlaneBean.segmentBeanList.get(i).length > z) {
                z = roomBean.floorPlaneBean.segmentBeanList.get(i).length;
            }
        }

        setCameraPosition(0f, 0f, z * 0.8f);
        setCameraRange(0.0001f, z * 10f);
        // re set camera

        roomModel = new RoomModel(tempRoomBean);
        roomModel.context = view.getContext();
        roomModel.create2DModel(anchorNode);
        roomModel.create2DModelSizeSymbol();

        // rotate model
        Quaternion xQuaternion = Quaternion.axisAngle(new Vector3(1.0f, 0.0f, 0.0f), 90);
        anchorNode.setWorldRotation(xQuaternion);
    }

    private void createWallModel(RoomBean tempRoomBean) {

        // re set camera
        float z = 0;
        for(int i = 0; i < roomBean.floorPlaneBean.pointList.size(); i++) {
            z += roomBean.floorPlaneBean.pointList.get(i).x;
        }

        setCameraPosition(0f, 0f, z * 4f);
        setCameraRange(0.0001f, z * 10f);
        // re set camera


        roomModel = new RoomModel(tempRoomBean);
        roomModel.context = view.getContext();
        roomModel.createWallModel(anchorNode);
    }

    public void pause() {
        sceneView.pause();
    }

    public void resume() {
        try {
            sceneView.resume();
        }
        catch (CameraNotAvailableException e) {
            e.printStackTrace();
        }
    }

    public void destroy() {

        if(roomModel != null) {
            roomModel.destroy();
            roomModel = null;
        }

        if(roomBean != null) {
            roomBean.clear();
            roomBean = null;
        }

        SFMaterial.instance.destroy();
        SFRenderable.instance.destroy();
        sceneView.destroy();
    }
}
