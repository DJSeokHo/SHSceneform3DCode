package com.swein.shsceneform3dcode.sceneformpart;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.MotionEvent;
import android.view.PixelCopy;
import android.view.View;

import androidx.annotation.Nullable;

import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Camera;
import com.google.ar.sceneform.HitTestResult;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.SceneView;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.swein.shsceneform3dcode.R;
import com.swein.shsceneform3dcode.framework.util.debug.ILog;
import com.swein.shsceneform3dcode.framework.util.eventsplitshot.eventcenter.EventCenter;
import com.swein.shsceneform3dcode.framework.util.eventsplitshot.subject.ESSArrows;
import com.swein.shsceneform3dcode.framework.util.view.ViewUtil;
import com.swein.shsceneform3dcode.sceneformpart.data.room.bean.RoomBean;
import com.swein.shsceneform3dcode.sceneformpart.data.room.model.RoomModel;
import com.swein.shsceneform3dcode.sceneformpart.material.SFMaterial;
import com.swein.shsceneform3dcode.sceneformpart.renderable.SFRenderable;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class SceneFormViewHolder {

    public interface SceneFormViewHolderDelegate {
        void onLoadModelFinish();
    }

    public final static int TYPE_3D = 0;
    public final static int TYPE_2D = 1;
    public final static int TYPE_WALL = 2;

    private final static String TAG = "SceneFormViewHolder";

    public View view;

    private SceneView sceneView;

    private RoomBean roomBean;
    private RoomModel roomModel;

    private int type;
    private float maxFar = 0;

    private SceneFormViewHolderDelegate sceneFormViewHolderDelegate;

    public SceneFormViewHolder(Activity activity, RoomBean roomBean, int type, @Nullable SceneFormViewHolderDelegate sceneFormViewHolderDelegate) {
        view = ViewUtil.inflateView(activity, R.layout.view_holder_scene_form, null);
        this.sceneFormViewHolderDelegate = sceneFormViewHolderDelegate;
        this.type = type;
        this.roomBean = roomBean;

        findView();
        initSceneForm();

        setCameraPosition(0f, 0f, 4f);
        setCameraRange(0.0001f, 10);

        SFMaterial.instance.init(view.getContext(), () -> {
            SFRenderable.instance.init(view.getContext(), () -> {

                if(type == TYPE_3D) {
                    roomBean.calculate3DModelCenterPoint();
                }
                else if(type == TYPE_2D) {
                    roomBean.calculate2DModelCenterPoint();
                }
                else if(type == TYPE_WALL) {
                    roomBean.calculateWallCenterPoint();
                }

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

                                ILog.iLogDebug(TAG, roomBean.centerPoint.x + " " + roomBean.centerPoint.y + " " + roomBean.centerPoint.z);

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
//                                scale = anchorNode.getWorldScale().x;
                                scale = sceneView.getScene().getCamera().getWorldPosition().z;
                            }

                            float screenViewPercent = motionEventDistance(motionEvent) / sceneView.getWidth();

                            if (distance > tempDistance) {
                                // plus(zoom in)
                                scale -= screenViewPercent * 0.05f;
                            }
                            else if (distance < tempDistance) {
                                // minus(zoom out)
                                scale += screenViewPercent * 0.05f;
                            }
                            // =================== check is plus(zoom in) or minus(zoom out) =====================

                            // =================== set scale limit ===================
                            if (scale <= 0.01) {
                                scale = 0.01f;
                            }
                            else if (scale > maxFar * 10) {
                                scale = maxFar;
                            }
                            // =================== set scale limit ===================

                            sceneView.getScene().getCamera().setWorldPosition(
                                    new Vector3(sceneView.getScene().getCamera().getWorldPosition().x,
                                            sceneView.getScene().getCamera().getWorldPosition().y, scale));
//                            anchorNode.setWorldScale(new Vector3(scale, scale, scale));
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
            if(sceneFormViewHolderDelegate != null) {
                sceneFormViewHolderDelegate.onLoadModelFinish();
            }
        }
        else if(type == TYPE_WALL) {

            RoomBean wallRoomBean = tempRoomBean.createWallModel();
            createWallModel(wallRoomBean);
        }
    }

    private void create3DModel(RoomBean tempRoomBean) {

        // re set camera
        maxFar = roomBean.height;
        for(int i = 0; i < roomBean.floorPlaneBean.segmentBeanList.size(); i++) {
            if(roomBean.floorPlaneBean.segmentBeanList.get(i).length > maxFar) {
                maxFar = roomBean.floorPlaneBean.segmentBeanList.get(i).length;
            }
        }

        setCameraPosition(0f, 0f, maxFar * 1.5f);
        setCameraRange(0.0001f, maxFar * 10f);
        // re set camera


        roomModel = new RoomModel(tempRoomBean);
        roomModel.context = view.getContext();
        roomModel.create3DModel(anchorNode);
        roomModel.create3DSizeSymbol();
    }

    private void create2DModel(RoomBean tempRoomBean) {

        // re set camera
        maxFar = roomBean.height;
        for(int i = 0; i < roomBean.floorPlaneBean.segmentBeanList.size(); i++) {
            if(roomBean.floorPlaneBean.segmentBeanList.get(i).length > maxFar) {
                maxFar = roomBean.floorPlaneBean.segmentBeanList.get(i).length;
            }
        }

        setCameraPosition(0f, 0f, maxFar * 0.8f);
        setCameraRange(0.0001f, maxFar * 10f);
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
        maxFar = 0;
        for(int i = 0; i < roomBean.floorPlaneBean.pointList.size(); i++) {
            maxFar += roomBean.floorPlaneBean.pointList.get(i).x;
        }

        setCameraPosition(0f, 0f, maxFar * 4f);
        setCameraRange(0.0001f, maxFar * 10f);
        // re set camera

        roomModel = new RoomModel(tempRoomBean);
        roomModel.context = view.getContext();
        roomModel.createWallModel(anchorNode);
    }

    public void captureThumbnailImage(Runnable empty) {

        final HandlerThread handlerThread = new HandlerThread("PixelCopier");
        handlerThread.start();

        sceneView.post(() -> {
            final Bitmap bitmap = Bitmap.createBitmap(sceneView.getWidth(), sceneView.getHeight(), Bitmap.Config.ARGB_8888);

            PixelCopy.request(sceneView, bitmap, new PixelCopy.OnPixelCopyFinishedListener() {
                @Override
                public void onPixelCopyFinished(int i) {
                    if (i == PixelCopy.SUCCESS) {

                        try {
                            String filePath = generateFilePath();
                            saveBitmapToDisk(bitmap, filePath);
                            ILog.iLogDebug(TAG, filePath);

                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("filePath", filePath);
                            EventCenter.instance.sendEvent(ESSArrows.UPDATE_2D_IMAGE, this, hashMap);
                        }
                        catch (IOException e) {
                            empty.run();
                        }
                    }
                    else {
                        empty.run();
                    }
                }
            }, new Handler(handlerThread.getLooper()));
        });

    }

    private String generateFilePath() {
        String date =
                new SimpleDateFormat("yyyyMMddHHmmss", java.util.Locale.getDefault()).format(new Date());
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + "Sceneform/" + date + "_screenshot.jpg";
    }

    private void saveBitmapToDisk(Bitmap bitmap, String filename) throws IOException {

        File out = new File(filename);
        if (!out.getParentFile().exists()) {
            out.getParentFile().mkdirs();
        }
        try (FileOutputStream outputStream = new FileOutputStream(filename);
             ByteArrayOutputStream outputData = new ByteArrayOutputStream()) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputData);
            outputData.writeTo(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException ex) {
            throw new IOException("Failed to save bitmap to disk", ex);
        }
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
