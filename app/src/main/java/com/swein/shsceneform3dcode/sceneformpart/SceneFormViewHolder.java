package com.swein.shsceneform3dcode.sceneformpart;

import android.content.Context;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;

import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Camera;
import com.google.ar.sceneform.HitTestResult;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.SceneView;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.swein.shsceneform3dcode.R;
import com.swein.shsceneform3dcode.framework.debug.ILog;
import com.swein.shsceneform3dcode.framework.util.view.ViewUtil;
import com.swein.shsceneform3dcode.sceneformpart.bean.RoomBean;
import com.swein.shsceneform3dcode.sceneformpart.bean.basic.PlaneBean;
import com.swein.shsceneform3dcode.sceneformpart.constants.SFConstants;
import com.swein.shsceneform3dcode.sceneformpart.material.SFMaterial;
import com.swein.shsceneform3dcode.sceneformpart.renderable.SFRenderable;
import com.swein.shsceneform3dcode.sceneformpart.tool.MathTool;
import com.swein.shsceneform3dcode.sceneformpart.tool.SFTool;

import org.json.JSONException;
import org.json.JSONObject;

public class SceneFormViewHolder {

    private final static String TAG = "SceneFormViewHolder";

    public View view;

    private SceneView sceneView;

    private RoomBean roomBean;

    private String jsonObjectString;

    public SceneFormViewHolder(Context context) {
        view = ViewUtil.inflateView(context, R.layout.view_holder_scene_form, null);

        jsonObjectString = "{\"normalVectorOfPlaneX\":\"0.0\",\"normalVectorOfPlaneY\":\"-2.6622875\",\"normalVectorOfPlaneZ\":\"0.0\",\"floor\":{\"pointArray\":[{\"x\":\"0.0\",\"y\":\"0.0\",\"z\":\"0.0\"},{\"x\":\"-0.42630434\",\"y\":\"1.6152859E-5\",\"z\":\"0.12004113\"},{\"x\":\"-0.83052826\",\"y\":\"-0.0020678043\",\"z\":\"-0.34649915\"},{\"x\":\"-0.5432865\",\"y\":\"-0.0011876822\",\"z\":\"-1.0865707\"},{\"x\":\"0.20097595\",\"y\":\"6.7943335E-4\",\"z\":\"-0.7151514\"}],\"segmentArray\":[{\"startPoint\":{\"x\":\"0.0\",\"y\":\"0.0\",\"z\":\"0.0\"},\"endPoint\":{\"x\":\"-0.42630434\",\"y\":\"1.6152859E-5\",\"z\":\"0.12004113\"},\"length\":\"0.44288293\"},{\"startPoint\":{\"x\":\"-0.42630434\",\"y\":\"1.6152859E-5\",\"z\":\"0.12004113\"},\"endPoint\":{\"x\":\"-0.83052826\",\"y\":\"-0.0020678043\",\"z\":\"-0.34649915\"},\"length\":\"0.61730146\"},{\"startPoint\":{\"x\":\"-0.83052826\",\"y\":\"-0.0020678043\",\"z\":\"-0.34649915\"},\"endPoint\":{\"x\":\"-0.5432865\",\"y\":\"-0.0011876822\",\"z\":\"-1.0865707\"},\"length\":\"0.7938606\"},{\"startPoint\":{\"x\":\"-0.5432865\",\"y\":\"-0.0011876822\",\"z\":\"-1.0865707\"},\"endPoint\":{\"x\":\"0.20097595\",\"y\":\"6.7943335E-4\",\"z\":\"-0.7151514\"},\"length\":\"0.8317946\"},{\"startPoint\":{\"x\":\"0.20097595\",\"y\":\"6.7943335E-4\",\"z\":\"-0.7151514\"},\"endPoint\":{\"x\":\"0.0\",\"y\":\"0.0\",\"z\":\"0.0\"},\"length\":\"0.7428548\"}],\"type\":\"FLOOR\"},\"ceiling\":{\"pointArray\":[{\"x\":\"0.0\",\"y\":\"1.15\",\"z\":\"0.0\"},{\"x\":\"-0.42630434\",\"y\":\"1.1500161\",\"z\":\"0.12004113\"},{\"x\":\"-0.83052826\",\"y\":\"1.1479322\",\"z\":\"-0.34649915\"},{\"x\":\"-0.5432865\",\"y\":\"1.1488123\",\"z\":\"-1.0865707\"},{\"x\":\"0.20097595\",\"y\":\"1.1506793\",\"z\":\"-0.7151514\"}],\"segmentArray\":[{\"startPoint\":{\"x\":\"0.0\",\"y\":\"1.15\",\"z\":\"0.0\"},\"endPoint\":{\"x\":\"-0.42630434\",\"y\":\"1.1500161\",\"z\":\"0.12004113\"},\"length\":\"0.4428829\"},{\"startPoint\":{\"x\":\"-0.42630434\",\"y\":\"1.1500161\",\"z\":\"0.12004113\"},\"endPoint\":{\"x\":\"-0.83052826\",\"y\":\"1.1479322\",\"z\":\"-0.34649915\"},\"length\":\"0.6173015\"},{\"startPoint\":{\"x\":\"-0.83052826\",\"y\":\"1.1479322\",\"z\":\"-0.34649915\"},\"endPoint\":{\"x\":\"-0.5432865\",\"y\":\"1.1488123\",\"z\":\"-1.0865707\"},\"length\":\"0.7938606\"},{\"startPoint\":{\"x\":\"-0.5432865\",\"y\":\"1.1488123\",\"z\":\"-1.0865707\"},\"endPoint\":{\"x\":\"0.20097595\",\"y\":\"1.1506793\",\"z\":\"-0.7151514\"},\"length\":\"0.8317946\"},{\"startPoint\":{\"x\":\"0.20097595\",\"y\":\"1.1506793\",\"z\":\"-0.7151514\"},\"endPoint\":{\"x\":\"0.0\",\"y\":\"1.15\",\"z\":\"0.0\"},\"length\":\"0.7428548\"}],\"type\":\"CEILING\"},\"wallArray\":[{\"pointArray\":[{\"x\":\"0.0\",\"y\":\"0.0\",\"z\":\"0.0\"},{\"x\":\"-0.42630434\",\"y\":\"1.6152859E-5\",\"z\":\"0.12004113\"},{\"x\":\"-0.42630434\",\"y\":\"1.1500161\",\"z\":\"0.12004113\"},{\"x\":\"0.0\",\"y\":\"1.15\",\"z\":\"0.0\"}],\"segmentArray\":[{\"startPoint\":{\"x\":\"0.0\",\"y\":\"0.0\",\"z\":\"0.0\"},\"endPoint\":{\"x\":\"-0.42630434\",\"y\":\"1.6152859E-5\",\"z\":\"0.12004113\"},\"length\":\"0.44288293\"},{\"startPoint\":{\"x\":\"-0.42630434\",\"y\":\"1.6152859E-5\",\"z\":\"0.12004113\"},\"endPoint\":{\"x\":\"-0.42630434\",\"y\":\"1.1500161\",\"z\":\"0.12004113\"},\"length\":\"1.15\"},{\"startPoint\":{\"x\":\"-0.42630434\",\"y\":\"1.1500161\",\"z\":\"0.12004113\"},\"endPoint\":{\"x\":\"0.0\",\"y\":\"1.15\",\"z\":\"0.0\"},\"length\":\"0.4428829\"},{\"startPoint\":{\"x\":\"0.0\",\"y\":\"1.15\",\"z\":\"0.0\"},\"endPoint\":{\"x\":\"0.0\",\"y\":\"0.0\",\"z\":\"0.0\"},\"length\":\"1.15\"}],\"type\":\"WALL\"},{\"pointArray\":[{\"x\":\"-0.42630434\",\"y\":\"1.6152859E-5\",\"z\":\"0.12004113\"},{\"x\":\"-0.83052826\",\"y\":\"-0.0020678043\",\"z\":\"-0.34649915\"},{\"x\":\"-0.83052826\",\"y\":\"1.1479322\",\"z\":\"-0.34649915\"},{\"x\":\"-0.42630434\",\"y\":\"1.1500161\",\"z\":\"0.12004113\"}],\"segmentArray\":[{\"startPoint\":{\"x\":\"-0.42630434\",\"y\":\"1.6152859E-5\",\"z\":\"0.12004113\"},\"endPoint\":{\"x\":\"-0.83052826\",\"y\":\"-0.0020678043\",\"z\":\"-0.34649915\"},\"length\":\"0.61730146\"},{\"startPoint\":{\"x\":\"-0.83052826\",\"y\":\"-0.0020678043\",\"z\":\"-0.34649915\"},\"endPoint\":{\"x\":\"-0.83052826\",\"y\":\"1.1479322\",\"z\":\"-0.34649915\"},\"length\":\"1.15\"},{\"startPoint\":{\"x\":\"-0.83052826\",\"y\":\"1.1479322\",\"z\":\"-0.34649915\"},\"endPoint\":{\"x\":\"-0.42630434\",\"y\":\"1.1500161\",\"z\":\"0.12004113\"},\"length\":\"0.6173015\"},{\"startPoint\":{\"x\":\"-0.42630434\",\"y\":\"1.1500161\",\"z\":\"0.12004113\"},\"endPoint\":{\"x\":\"-0.42630434\",\"y\":\"1.6152859E-5\",\"z\":\"0.12004113\"},\"length\":\"1.15\"}],\"type\":\"WALL\"},{\"pointArray\":[{\"x\":\"-0.83052826\",\"y\":\"-0.0020678043\",\"z\":\"-0.34649915\"},{\"x\":\"-0.5432865\",\"y\":\"-0.0011876822\",\"z\":\"-1.0865707\"},{\"x\":\"-0.5432865\",\"y\":\"1.1488123\",\"z\":\"-1.0865707\"},{\"x\":\"-0.83052826\",\"y\":\"1.1479322\",\"z\":\"-0.34649915\"}],\"segmentArray\":[{\"startPoint\":{\"x\":\"-0.83052826\",\"y\":\"-0.0020678043\",\"z\":\"-0.34649915\"},\"endPoint\":{\"x\":\"-0.5432865\",\"y\":\"-0.0011876822\",\"z\":\"-1.0865707\"},\"length\":\"0.7938606\"},{\"startPoint\":{\"x\":\"-0.5432865\",\"y\":\"-0.0011876822\",\"z\":\"-1.0865707\"},\"endPoint\":{\"x\":\"-0.5432865\",\"y\":\"1.1488123\",\"z\":\"-1.0865707\"},\"length\":\"1.15\"},{\"startPoint\":{\"x\":\"-0.5432865\",\"y\":\"1.1488123\",\"z\":\"-1.0865707\"},\"endPoint\":{\"x\":\"-0.83052826\",\"y\":\"1.1479322\",\"z\":\"-0.34649915\"},\"length\":\"0.7938606\"},{\"startPoint\":{\"x\":\"-0.83052826\",\"y\":\"1.1479322\",\"z\":\"-0.34649915\"},\"endPoint\":{\"x\":\"-0.83052826\",\"y\":\"-0.0020678043\",\"z\":\"-0.34649915\"},\"length\":\"1.15\"}],\"type\":\"WALL\"},{\"pointArray\":[{\"x\":\"-0.5432865\",\"y\":\"-0.0011876822\",\"z\":\"-1.0865707\"},{\"x\":\"0.20097595\",\"y\":\"6.7943335E-4\",\"z\":\"-0.7151514\"},{\"x\":\"0.20097595\",\"y\":\"1.1506793\",\"z\":\"-0.7151514\"},{\"x\":\"-0.5432865\",\"y\":\"1.1488123\",\"z\":\"-1.0865707\"}],\"segmentArray\":[{\"startPoint\":{\"x\":\"-0.5432865\",\"y\":\"-0.0011876822\",\"z\":\"-1.0865707\"},\"endPoint\":{\"x\":\"0.20097595\",\"y\":\"6.7943335E-4\",\"z\":\"-0.7151514\"},\"length\":\"0.8317946\"},{\"startPoint\":{\"x\":\"0.20097595\",\"y\":\"6.7943335E-4\",\"z\":\"-0.7151514\"},\"endPoint\":{\"x\":\"0.20097595\",\"y\":\"1.1506793\",\"z\":\"-0.7151514\"},\"length\":\"1.1499997\"},{\"startPoint\":{\"x\":\"0.20097595\",\"y\":\"1.1506793\",\"z\":\"-0.7151514\"},\"endPoint\":{\"x\":\"-0.5432865\",\"y\":\"1.1488123\",\"z\":\"-1.0865707\"},\"length\":\"0.8317946\"},{\"startPoint\":{\"x\":\"-0.5432865\",\"y\":\"1.1488123\",\"z\":\"-1.0865707\"},\"endPoint\":{\"x\":\"-0.5432865\",\"y\":\"-0.0011876822\",\"z\":\"-1.0865707\"},\"length\":\"1.15\"}],\"type\":\"WALL\"},{\"pointArray\":[{\"x\":\"0.20097595\",\"y\":\"6.7943335E-4\",\"z\":\"-0.7151514\"},{\"x\":\"0.0\",\"y\":\"0.0\",\"z\":\"0.0\"},{\"x\":\"0.0\",\"y\":\"1.15\",\"z\":\"0.0\"},{\"x\":\"0.20097595\",\"y\":\"1.1506793\",\"z\":\"-0.7151514\"}],\"segmentArray\":[{\"startPoint\":{\"x\":\"0.20097595\",\"y\":\"6.7943335E-4\",\"z\":\"-0.7151514\"},\"endPoint\":{\"x\":\"0.0\",\"y\":\"0.0\",\"z\":\"0.0\"},\"length\":\"0.7428548\"},{\"startPoint\":{\"x\":\"0.0\",\"y\":\"0.0\",\"z\":\"0.0\"},\"endPoint\":{\"x\":\"0.0\",\"y\":\"1.15\",\"z\":\"0.0\"},\"length\":\"1.15\"},{\"startPoint\":{\"x\":\"0.0\",\"y\":\"1.15\",\"z\":\"0.0\"},\"endPoint\":{\"x\":\"0.20097595\",\"y\":\"1.1506793\",\"z\":\"-0.7151514\"},\"length\":\"0.7428548\"},{\"startPoint\":{\"x\":\"0.20097595\",\"y\":\"1.1506793\",\"z\":\"-0.7151514\"},\"endPoint\":{\"x\":\"0.20097595\",\"y\":\"6.7943335E-4\",\"z\":\"-0.7151514\"},\"length\":\"1.1499997\"}],\"type\":\"WALL\"}],\"wallObjectArray\":[{\"pointArray\":[{\"x\":\"-0.2926181\",\"y\":\"0.39861858\",\"z\":\"0.08239287\"},{\"x\":\"-0.13242902\",\"y\":\"0.39883605\",\"z\":\"0.0382905\"},{\"x\":\"-0.13296159\",\"y\":\"0.67084306\",\"z\":\"0.037697554\"},{\"x\":\"-0.2931507\",\"y\":\"0.67062557\",\"z\":\"0.081799865\"}],\"segmentArray\":[{\"startPoint\":{\"x\":\"-0.2926181\",\"y\":\"0.39861858\",\"z\":\"0.08239287\"},\"endPoint\":{\"x\":\"-0.13242902\",\"y\":\"0.39883605\",\"z\":\"0.0382905\"},\"length\":\"0.16614936\"},{\"startPoint\":{\"x\":\"-0.13242902\",\"y\":\"0.39883605\",\"z\":\"0.0382905\"},\"endPoint\":{\"x\":\"-0.13296159\",\"y\":\"0.67084306\",\"z\":\"0.037697554\"},\"length\":\"0.2720082\"},{\"startPoint\":{\"x\":\"-0.13296159\",\"y\":\"0.67084306\",\"z\":\"0.037697554\"},\"endPoint\":{\"x\":\"-0.2931507\",\"y\":\"0.67062557\",\"z\":\"0.081799865\"},\"length\":\"0.16614941\"},{\"startPoint\":{\"x\":\"-0.2931507\",\"y\":\"0.67062557\",\"z\":\"0.081799865\"},\"endPoint\":{\"x\":\"-0.2926181\",\"y\":\"0.39861858\",\"z\":\"0.08239287\"},\"length\":\"0.27200818\"}],\"type\":\"WINDOW\"},{\"pointArray\":[{\"x\":\"0.022711337\",\"y\":\"0.70336175\",\"z\":\"-0.08081323\"},{\"x\":\"0.124578096\",\"y\":\"0.7026204\",\"z\":\"-0.44834197\"},{\"x\":\"0.12560484\",\"y\":\"0.15120435\",\"z\":\"-0.44694495\"},{\"x\":\"0.023738094\",\"y\":\"0.15194577\",\"z\":\"-0.079416096\"}],\"segmentArray\":[{\"startPoint\":{\"x\":\"0.022711337\",\"y\":\"0.70336175\",\"z\":\"-0.08081323\"},\"endPoint\":{\"x\":\"0.124578096\",\"y\":\"0.7026204\",\"z\":\"-0.44834197\"},\"length\":\"0.38138533\"},{\"startPoint\":{\"x\":\"0.124578096\",\"y\":\"0.7026204\",\"z\":\"-0.44834197\"},\"endPoint\":{\"x\":\"0.12560484\",\"y\":\"0.15120435\",\"z\":\"-0.44694495\"},\"length\":\"0.5514188\"},{\"startPoint\":{\"x\":\"0.12560484\",\"y\":\"0.15120435\",\"z\":\"-0.44694495\"},\"endPoint\":{\"x\":\"0.023738094\",\"y\":\"0.15194577\",\"z\":\"-0.079416096\"},\"length\":\"0.3813854\"},{\"startPoint\":{\"x\":\"0.023738094\",\"y\":\"0.15194577\",\"z\":\"-0.079416096\"},\"endPoint\":{\"x\":\"0.022711337\",\"y\":\"0.70336175\",\"z\":\"-0.08081323\"},\"length\":\"0.5514188\"}],\"type\":\"DOOR\"}],\"height\":\"1.15\",\"floorFixedY\":\"-0.8285\",\"area\":\"0.78425086\",\"circumference\":\"3.4286945\",\"wallArea\":\"3.9429984\",\"volume\":\"0.9018885\",\"name\":\"ㅂㅂㅂ\",\"unit\":\"m\"}";

        findView();
        initSceneForm();
        setCameraRange(4f, 0.0001f, 10);

        SFMaterial.instance.init(view.getContext(), () -> {
            SFRenderable.instance.init(view.getContext(), () -> {

                createRoomBean();
                createAnchorNode();

                calculateModellingCenter();
            });
        });
    }

    private void findView() {
        sceneView = view.findViewById(R.id.sceneView);
    }

    private void setCameraRange(float z, float near, float far) {

        Camera camera = sceneView.getScene().getCamera();
        camera.setWorldPosition(new Vector3(0.0f, 0.0f, z));
        camera.setNearClipPlane(near);
        camera.setFarClipPlane(far);

    }

    private AnchorNode anchorNode;

    private float prevXAngle = 0;
    private float prevYAngle = 0;

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

                                anchorNode.getWorldRotation();

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

    private void calculateModellingCenter() {

        // get center point of modelling

        float tx = 0;
        float tz = 0;
        for(int i = 0; i < roomBean.floor.pointList.size(); i++) {
            tx += roomBean.floor.pointList.get(i).point.getLocalPosition().x;
            tz += roomBean.floor.pointList.get(i).point.getLocalPosition().z;
        }

        float xAvg = tx / roomBean.floor.pointList.size();
        float zAvg = tz / roomBean.floor.pointList.size();
        float yAvg = roomBean.height * 0.5f;


        // show center point
//        Node node = SFTool.createLocalNode(xAvg, yAvg, zAvg, 0.05f, SFMaterial.instance.pointMaterial, false);
//        node.setParent(anchorNode);

        ILog.iLogDebug(TAG, "center " + xAvg + " " + zAvg);

        // get center point of modelling


        // set camera
        float z = roomBean.height;

        for(int i = 0; i < roomBean.floor.segmentList.size(); i++) {
            if(roomBean.floor.segmentList.get(i).length > z) {
                z = roomBean.floor.segmentList.get(i).length;
            }
        }
        setCameraRange(z * 1.5f, 0.0001f, z * 10f);
        // set camera

        RoomBean roomBean = createTempRoomBean(xAvg, yAvg, zAvg);
        updateView(roomBean);
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

    private void updateView(RoomBean roomBean) {

        roomBean.floor.createSegment();
        for(int i = 0; i < roomBean.floor.pointList.size(); i++) {
            roomBean.floor.pointList.get(i).point.setParent(anchorNode);
        }

        roomBean.ceiling.createSegment();
        for(int i = 0; i < roomBean.ceiling.pointList.size(); i++) {
            roomBean.ceiling.pointList.get(i).point.setParent(anchorNode);
        }

        Node segmentNode;
        for(int i = 0; i < roomBean.floor.segmentList.size(); i++) {
            segmentNode = SFTool.drawSegment(
                    roomBean.floor.segmentList.get(i).startPoint.point,
                    roomBean.floor.segmentList.get(i).endPoint.point,
                    SFMaterial.instance.segmentMaterial, false);

            SFTool.setSegmentSizeTextView(view.getContext(),
                    MathTool.getLengthOfTwoNode(roomBean.floor.segmentList.get(i).startPoint.point,
                            roomBean.floor.segmentList.get(i).endPoint.point), SFConstants.SFUnit.M,
                    segmentNode, (viewRenderable, faceToCameraNode) -> {

                    });
        }

        for(int i = 0; i < roomBean.ceiling.segmentList.size(); i++) {
            segmentNode = SFTool.drawSegment(
                    roomBean.ceiling.segmentList.get(i).startPoint.point,
                    roomBean.ceiling.segmentList.get(i).endPoint.point,
                    SFMaterial.instance.segmentMaterial, false);

            SFTool.setSegmentSizeTextView(view.getContext(),
                    MathTool.getLengthOfTwoNode(roomBean.ceiling.segmentList.get(i).startPoint.point,
                            roomBean.ceiling.segmentList.get(i).endPoint.point), SFConstants.SFUnit.M,
                    segmentNode, (viewRenderable, faceToCameraNode) -> {

                    });
        }

        for(PlaneBean planeBean : roomBean.wallList) {

            for(int i = 0; i < planeBean.pointList.size(); i++) {
                planeBean.pointList.get(i).point.setParent(anchorNode);
            }

            planeBean.createSegment();

            for(int i = 0; i < planeBean.segmentList.size(); i++) {
                segmentNode = SFTool.drawSegment(
                        planeBean.segmentList.get(i).startPoint.point,
                        planeBean.segmentList.get(i).endPoint.point,
                        SFMaterial.instance.segmentMaterial, false);

                SFTool.setSegmentSizeTextView(view.getContext(),
                        MathTool.getLengthOfTwoNode(planeBean.segmentList.get(i).startPoint.point,
                                planeBean.segmentList.get(i).endPoint.point), SFConstants.SFUnit.M,
                        segmentNode, (viewRenderable, faceToCameraNode) -> {

                        });
            }
        }


        for(PlaneBean planeBean : roomBean.wallObjectList) {

            for(int i = 0; i < planeBean.pointList.size(); i++) {
                planeBean.pointList.get(i).point.setParent(anchorNode);
            }

            planeBean.createSegment();

            for(int i = 0; i < planeBean.segmentList.size(); i++) {
                segmentNode = SFTool.drawSegment(
                        planeBean.segmentList.get(i).startPoint.point,
                        planeBean.segmentList.get(i).endPoint.point,
                        SFMaterial.instance.segmentMaterial, false);

                SFTool.setSegmentSizeTextView(view.getContext(),
                        MathTool.getLengthOfTwoNode(planeBean.segmentList.get(i).startPoint.point,
                                planeBean.segmentList.get(i).endPoint.point), SFConstants.SFUnit.M,
                        segmentNode, (viewRenderable, faceToCameraNode) -> {

                        });
            }
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

        if(roomBean != null) {
            roomBean.clear();
            roomBean = null;
        }

        SFMaterial.instance.destroy();
        SFRenderable.instance.destroy();
        sceneView.destroy();

    }
}
