package com.swein.shsceneform3dcode;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;

import androidx.fragment.app.FragmentActivity;

import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Camera;
import com.google.ar.sceneform.HitTestResult;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.SceneView;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.swein.shsceneform3dcode.framework.debug.ILog;
import com.swein.shsceneform3dcode.framework.thread.ThreadUtil;
import com.swein.shsceneform3dcode.sceneformpart.FaceToCameraNode;
import com.swein.shsceneform3dcode.sceneformpart.constants.SFConstants;
import com.swein.shsceneform3dcode.sceneformpart.material.SFMaterial;
import com.swein.shsceneform3dcode.sceneformpart.renderable.SFRenderable;
import com.swein.shsceneform3dcode.sceneformpart.tool.SFTool;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {

    private final static String TAG = "MainActivity";

    private SceneView sceneView;

    private List<Node> nodeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findView();
        initCamera();
        initSceneForm();

        SFMaterial.instance.init(this);
        SFRenderable.instance.init(this);

        ThreadUtil.startUIThread(1000, () -> {
            test();
        });


        try {
            JSONObject jsonObject = new JSONObject("{\"normalVectorOfPlaneX\":\"0.0\",\"normalVectorOfPlaneY\":\"-2.6622875\",\"normalVectorOfPlaneZ\":\"0.0\",\"floor\":{\"pointArray\":[{\"x\":\"0.0\",\"y\":\"0.0\",\"z\":\"0.0\"},{\"x\":\"-0.42630434\",\"y\":\"1.6152859E-5\",\"z\":\"0.12004113\"},{\"x\":\"-0.83052826\",\"y\":\"-0.0020678043\",\"z\":\"-0.34649915\"},{\"x\":\"-0.5432865\",\"y\":\"-0.0011876822\",\"z\":\"-1.0865707\"},{\"x\":\"0.20097595\",\"y\":\"6.7943335E-4\",\"z\":\"-0.7151514\"}],\"segmentArray\":[{\"startPoint\":{\"x\":\"0.0\",\"y\":\"0.0\",\"z\":\"0.0\"},\"endPoint\":{\"x\":\"-0.42630434\",\"y\":\"1.6152859E-5\",\"z\":\"0.12004113\"},\"length\":\"0.44288293\"},{\"startPoint\":{\"x\":\"-0.42630434\",\"y\":\"1.6152859E-5\",\"z\":\"0.12004113\"},\"endPoint\":{\"x\":\"-0.83052826\",\"y\":\"-0.0020678043\",\"z\":\"-0.34649915\"},\"length\":\"0.61730146\"},{\"startPoint\":{\"x\":\"-0.83052826\",\"y\":\"-0.0020678043\",\"z\":\"-0.34649915\"},\"endPoint\":{\"x\":\"-0.5432865\",\"y\":\"-0.0011876822\",\"z\":\"-1.0865707\"},\"length\":\"0.7938606\"},{\"startPoint\":{\"x\":\"-0.5432865\",\"y\":\"-0.0011876822\",\"z\":\"-1.0865707\"},\"endPoint\":{\"x\":\"0.20097595\",\"y\":\"6.7943335E-4\",\"z\":\"-0.7151514\"},\"length\":\"0.8317946\"},{\"startPoint\":{\"x\":\"0.20097595\",\"y\":\"6.7943335E-4\",\"z\":\"-0.7151514\"},\"endPoint\":{\"x\":\"0.0\",\"y\":\"0.0\",\"z\":\"0.0\"},\"length\":\"0.7428548\"}],\"type\":\"FLOOR\"},\"ceiling\":{\"pointArray\":[{\"x\":\"0.0\",\"y\":\"1.15\",\"z\":\"0.0\"},{\"x\":\"-0.42630434\",\"y\":\"1.1500161\",\"z\":\"0.12004113\"},{\"x\":\"-0.83052826\",\"y\":\"1.1479322\",\"z\":\"-0.34649915\"},{\"x\":\"-0.5432865\",\"y\":\"1.1488123\",\"z\":\"-1.0865707\"},{\"x\":\"0.20097595\",\"y\":\"1.1506793\",\"z\":\"-0.7151514\"}],\"segmentArray\":[{\"startPoint\":{\"x\":\"0.0\",\"y\":\"1.15\",\"z\":\"0.0\"},\"endPoint\":{\"x\":\"-0.42630434\",\"y\":\"1.1500161\",\"z\":\"0.12004113\"},\"length\":\"0.4428829\"},{\"startPoint\":{\"x\":\"-0.42630434\",\"y\":\"1.1500161\",\"z\":\"0.12004113\"},\"endPoint\":{\"x\":\"-0.83052826\",\"y\":\"1.1479322\",\"z\":\"-0.34649915\"},\"length\":\"0.6173015\"},{\"startPoint\":{\"x\":\"-0.83052826\",\"y\":\"1.1479322\",\"z\":\"-0.34649915\"},\"endPoint\":{\"x\":\"-0.5432865\",\"y\":\"1.1488123\",\"z\":\"-1.0865707\"},\"length\":\"0.7938606\"},{\"startPoint\":{\"x\":\"-0.5432865\",\"y\":\"1.1488123\",\"z\":\"-1.0865707\"},\"endPoint\":{\"x\":\"0.20097595\",\"y\":\"1.1506793\",\"z\":\"-0.7151514\"},\"length\":\"0.8317946\"},{\"startPoint\":{\"x\":\"0.20097595\",\"y\":\"1.1506793\",\"z\":\"-0.7151514\"},\"endPoint\":{\"x\":\"0.0\",\"y\":\"1.15\",\"z\":\"0.0\"},\"length\":\"0.7428548\"}],\"type\":\"CEILING\"},\"wallArray\":[{\"pointArray\":[{\"x\":\"0.0\",\"y\":\"0.0\",\"z\":\"0.0\"},{\"x\":\"-0.42630434\",\"y\":\"1.6152859E-5\",\"z\":\"0.12004113\"},{\"x\":\"-0.42630434\",\"y\":\"1.1500161\",\"z\":\"0.12004113\"},{\"x\":\"0.0\",\"y\":\"1.15\",\"z\":\"0.0\"}],\"segmentArray\":[{\"startPoint\":{\"x\":\"0.0\",\"y\":\"0.0\",\"z\":\"0.0\"},\"endPoint\":{\"x\":\"-0.42630434\",\"y\":\"1.6152859E-5\",\"z\":\"0.12004113\"},\"length\":\"0.44288293\"},{\"startPoint\":{\"x\":\"-0.42630434\",\"y\":\"1.6152859E-5\",\"z\":\"0.12004113\"},\"endPoint\":{\"x\":\"-0.42630434\",\"y\":\"1.1500161\",\"z\":\"0.12004113\"},\"length\":\"1.15\"},{\"startPoint\":{\"x\":\"-0.42630434\",\"y\":\"1.1500161\",\"z\":\"0.12004113\"},\"endPoint\":{\"x\":\"0.0\",\"y\":\"1.15\",\"z\":\"0.0\"},\"length\":\"0.4428829\"},{\"startPoint\":{\"x\":\"0.0\",\"y\":\"1.15\",\"z\":\"0.0\"},\"endPoint\":{\"x\":\"0.0\",\"y\":\"0.0\",\"z\":\"0.0\"},\"length\":\"1.15\"}],\"type\":\"WALL\"},{\"pointArray\":[{\"x\":\"-0.42630434\",\"y\":\"1.6152859E-5\",\"z\":\"0.12004113\"},{\"x\":\"-0.83052826\",\"y\":\"-0.0020678043\",\"z\":\"-0.34649915\"},{\"x\":\"-0.83052826\",\"y\":\"1.1479322\",\"z\":\"-0.34649915\"},{\"x\":\"-0.42630434\",\"y\":\"1.1500161\",\"z\":\"0.12004113\"}],\"segmentArray\":[{\"startPoint\":{\"x\":\"-0.42630434\",\"y\":\"1.6152859E-5\",\"z\":\"0.12004113\"},\"endPoint\":{\"x\":\"-0.83052826\",\"y\":\"-0.0020678043\",\"z\":\"-0.34649915\"},\"length\":\"0.61730146\"},{\"startPoint\":{\"x\":\"-0.83052826\",\"y\":\"-0.0020678043\",\"z\":\"-0.34649915\"},\"endPoint\":{\"x\":\"-0.83052826\",\"y\":\"1.1479322\",\"z\":\"-0.34649915\"},\"length\":\"1.15\"},{\"startPoint\":{\"x\":\"-0.83052826\",\"y\":\"1.1479322\",\"z\":\"-0.34649915\"},\"endPoint\":{\"x\":\"-0.42630434\",\"y\":\"1.1500161\",\"z\":\"0.12004113\"},\"length\":\"0.6173015\"},{\"startPoint\":{\"x\":\"-0.42630434\",\"y\":\"1.1500161\",\"z\":\"0.12004113\"},\"endPoint\":{\"x\":\"-0.42630434\",\"y\":\"1.6152859E-5\",\"z\":\"0.12004113\"},\"length\":\"1.15\"}],\"type\":\"WALL\"},{\"pointArray\":[{\"x\":\"-0.83052826\",\"y\":\"-0.0020678043\",\"z\":\"-0.34649915\"},{\"x\":\"-0.5432865\",\"y\":\"-0.0011876822\",\"z\":\"-1.0865707\"},{\"x\":\"-0.5432865\",\"y\":\"1.1488123\",\"z\":\"-1.0865707\"},{\"x\":\"-0.83052826\",\"y\":\"1.1479322\",\"z\":\"-0.34649915\"}],\"segmentArray\":[{\"startPoint\":{\"x\":\"-0.83052826\",\"y\":\"-0.0020678043\",\"z\":\"-0.34649915\"},\"endPoint\":{\"x\":\"-0.5432865\",\"y\":\"-0.0011876822\",\"z\":\"-1.0865707\"},\"length\":\"0.7938606\"},{\"startPoint\":{\"x\":\"-0.5432865\",\"y\":\"-0.0011876822\",\"z\":\"-1.0865707\"},\"endPoint\":{\"x\":\"-0.5432865\",\"y\":\"1.1488123\",\"z\":\"-1.0865707\"},\"length\":\"1.15\"},{\"startPoint\":{\"x\":\"-0.5432865\",\"y\":\"1.1488123\",\"z\":\"-1.0865707\"},\"endPoint\":{\"x\":\"-0.83052826\",\"y\":\"1.1479322\",\"z\":\"-0.34649915\"},\"length\":\"0.7938606\"},{\"startPoint\":{\"x\":\"-0.83052826\",\"y\":\"1.1479322\",\"z\":\"-0.34649915\"},\"endPoint\":{\"x\":\"-0.83052826\",\"y\":\"-0.0020678043\",\"z\":\"-0.34649915\"},\"length\":\"1.15\"}],\"type\":\"WALL\"},{\"pointArray\":[{\"x\":\"-0.5432865\",\"y\":\"-0.0011876822\",\"z\":\"-1.0865707\"},{\"x\":\"0.20097595\",\"y\":\"6.7943335E-4\",\"z\":\"-0.7151514\"},{\"x\":\"0.20097595\",\"y\":\"1.1506793\",\"z\":\"-0.7151514\"},{\"x\":\"-0.5432865\",\"y\":\"1.1488123\",\"z\":\"-1.0865707\"}],\"segmentArray\":[{\"startPoint\":{\"x\":\"-0.5432865\",\"y\":\"-0.0011876822\",\"z\":\"-1.0865707\"},\"endPoint\":{\"x\":\"0.20097595\",\"y\":\"6.7943335E-4\",\"z\":\"-0.7151514\"},\"length\":\"0.8317946\"},{\"startPoint\":{\"x\":\"0.20097595\",\"y\":\"6.7943335E-4\",\"z\":\"-0.7151514\"},\"endPoint\":{\"x\":\"0.20097595\",\"y\":\"1.1506793\",\"z\":\"-0.7151514\"},\"length\":\"1.1499997\"},{\"startPoint\":{\"x\":\"0.20097595\",\"y\":\"1.1506793\",\"z\":\"-0.7151514\"},\"endPoint\":{\"x\":\"-0.5432865\",\"y\":\"1.1488123\",\"z\":\"-1.0865707\"},\"length\":\"0.8317946\"},{\"startPoint\":{\"x\":\"-0.5432865\",\"y\":\"1.1488123\",\"z\":\"-1.0865707\"},\"endPoint\":{\"x\":\"-0.5432865\",\"y\":\"-0.0011876822\",\"z\":\"-1.0865707\"},\"length\":\"1.15\"}],\"type\":\"WALL\"},{\"pointArray\":[{\"x\":\"0.20097595\",\"y\":\"6.7943335E-4\",\"z\":\"-0.7151514\"},{\"x\":\"0.0\",\"y\":\"0.0\",\"z\":\"0.0\"},{\"x\":\"0.0\",\"y\":\"1.15\",\"z\":\"0.0\"},{\"x\":\"0.20097595\",\"y\":\"1.1506793\",\"z\":\"-0.7151514\"}],\"segmentArray\":[{\"startPoint\":{\"x\":\"0.20097595\",\"y\":\"6.7943335E-4\",\"z\":\"-0.7151514\"},\"endPoint\":{\"x\":\"0.0\",\"y\":\"0.0\",\"z\":\"0.0\"},\"length\":\"0.7428548\"},{\"startPoint\":{\"x\":\"0.0\",\"y\":\"0.0\",\"z\":\"0.0\"},\"endPoint\":{\"x\":\"0.0\",\"y\":\"1.15\",\"z\":\"0.0\"},\"length\":\"1.15\"},{\"startPoint\":{\"x\":\"0.0\",\"y\":\"1.15\",\"z\":\"0.0\"},\"endPoint\":{\"x\":\"0.20097595\",\"y\":\"1.1506793\",\"z\":\"-0.7151514\"},\"length\":\"0.7428548\"},{\"startPoint\":{\"x\":\"0.20097595\",\"y\":\"1.1506793\",\"z\":\"-0.7151514\"},\"endPoint\":{\"x\":\"0.20097595\",\"y\":\"6.7943335E-4\",\"z\":\"-0.7151514\"},\"length\":\"1.1499997\"}],\"type\":\"WALL\"}],\"wallObjectArray\":[{\"pointArray\":[{\"x\":\"-0.2926181\",\"y\":\"0.39861858\",\"z\":\"0.08239287\"},{\"x\":\"-0.13242902\",\"y\":\"0.39883605\",\"z\":\"0.0382905\"},{\"x\":\"-0.13296159\",\"y\":\"0.67084306\",\"z\":\"0.037697554\"},{\"x\":\"-0.2931507\",\"y\":\"0.67062557\",\"z\":\"0.081799865\"}],\"segmentArray\":[{\"startPoint\":{\"x\":\"-0.2926181\",\"y\":\"0.39861858\",\"z\":\"0.08239287\"},\"endPoint\":{\"x\":\"-0.13242902\",\"y\":\"0.39883605\",\"z\":\"0.0382905\"},\"length\":\"0.16614936\"},{\"startPoint\":{\"x\":\"-0.13242902\",\"y\":\"0.39883605\",\"z\":\"0.0382905\"},\"endPoint\":{\"x\":\"-0.13296159\",\"y\":\"0.67084306\",\"z\":\"0.037697554\"},\"length\":\"0.2720082\"},{\"startPoint\":{\"x\":\"-0.13296159\",\"y\":\"0.67084306\",\"z\":\"0.037697554\"},\"endPoint\":{\"x\":\"-0.2931507\",\"y\":\"0.67062557\",\"z\":\"0.081799865\"},\"length\":\"0.16614941\"},{\"startPoint\":{\"x\":\"-0.2931507\",\"y\":\"0.67062557\",\"z\":\"0.081799865\"},\"endPoint\":{\"x\":\"-0.2926181\",\"y\":\"0.39861858\",\"z\":\"0.08239287\"},\"length\":\"0.27200818\"}],\"type\":\"WINDOW\"},{\"pointArray\":[{\"x\":\"0.022711337\",\"y\":\"0.70336175\",\"z\":\"-0.08081323\"},{\"x\":\"0.124578096\",\"y\":\"0.7026204\",\"z\":\"-0.44834197\"},{\"x\":\"0.12560484\",\"y\":\"0.15120435\",\"z\":\"-0.44694495\"},{\"x\":\"0.023738094\",\"y\":\"0.15194577\",\"z\":\"-0.079416096\"}],\"segmentArray\":[{\"startPoint\":{\"x\":\"0.022711337\",\"y\":\"0.70336175\",\"z\":\"-0.08081323\"},\"endPoint\":{\"x\":\"0.124578096\",\"y\":\"0.7026204\",\"z\":\"-0.44834197\"},\"length\":\"0.38138533\"},{\"startPoint\":{\"x\":\"0.124578096\",\"y\":\"0.7026204\",\"z\":\"-0.44834197\"},\"endPoint\":{\"x\":\"0.12560484\",\"y\":\"0.15120435\",\"z\":\"-0.44694495\"},\"length\":\"0.5514188\"},{\"startPoint\":{\"x\":\"0.12560484\",\"y\":\"0.15120435\",\"z\":\"-0.44694495\"},\"endPoint\":{\"x\":\"0.023738094\",\"y\":\"0.15194577\",\"z\":\"-0.079416096\"},\"length\":\"0.3813854\"},{\"startPoint\":{\"x\":\"0.023738094\",\"y\":\"0.15194577\",\"z\":\"-0.079416096\"},\"endPoint\":{\"x\":\"0.022711337\",\"y\":\"0.70336175\",\"z\":\"-0.08081323\"},\"length\":\"0.5514188\"}],\"type\":\"DOOR\"}],\"height\":\"1.15\",\"floorFixedY\":\"-0.8285\",\"area\":\"0.78425086\",\"circumference\":\"3.4286945\",\"wallArea\":\"3.9429984\",\"volume\":\"0.9018885\",\"name\":\"ㅂㅂㅂ\",\"unit\":\"m\"}");
            ILog.iLogDebug(TAG, jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onPause() {
        sceneView.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            sceneView.resume();
        }
        catch (CameraNotAvailableException e) {
            e.printStackTrace();
        }
    }

    private void findView() {
        sceneView = findViewById(R.id.sceneView);
    }

    private void initCamera() {
        Camera camera = sceneView.getScene().getCamera();
        camera.setWorldPosition(new Vector3(0.0f, 0.0f, 4f));
        camera.setNearClipPlane(0.1f);
        camera.setFarClipPlane(10);
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
                            float distance = distance(motionEvent);
                            if (tempDistance == 0) {
                                tempDistance = distance;
                                return;
                            }

                            if (0 == scale) {
                                scale = anchorNode.getWorldScale().x;
                            }

                            float screenViewPercent = distance(motionEvent) / sceneView.getWidth();

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


    private float distance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float)Math.sqrt(x * x + y * y);
    }

    private void test() {

        if(SFMaterial.instance.pointMaterial == null || SFMaterial.instance.segmentMaterial == null) {
            return;
        }

        if(anchorNode == null) {
            anchorNode = new AnchorNode();
        }

        anchorNode.setWorldPosition(new Vector3(0f, 0f, 0f));
        anchorNode.setParent(sceneView.getScene());

        Node node;

        node = SFTool.createLocalNodeSphere(-1f, -1f, 1f, 0.01f, SFMaterial.instance.pointMaterial, false);
        node.setParent(anchorNode);
        nodeList.add(node);

        node = SFTool.createLocalNodeSphere(-1f, 1f, 1f, 0.01f, SFMaterial.instance.pointMaterial, false);
        node.setParent(anchorNode);
        nodeList.add(node);

        node = SFTool.createLocalNodeSphere(1f, 1f, 1f, 0.01f, SFMaterial.instance.pointMaterial, false);
        node.setParent(anchorNode);
        nodeList.add(node);

        node = SFTool.createLocalNodeSphere(1f, -1f, 1f, 0.01f, SFMaterial.instance.pointMaterial, false);
        node.setParent(anchorNode);
        nodeList.add(node);

        node = SFTool.createLocalNodeSphere(-1f, -1f, -1f, 0.01f, SFMaterial.instance.pointMaterial, false);
        node.setParent(anchorNode);
        nodeList.add(node);

        node = SFTool.createLocalNodeSphere(-1f, 1f, -1f, 0.01f, SFMaterial.instance.pointMaterial, false);
        node.setParent(anchorNode);
        nodeList.add(node);

        node = SFTool.createLocalNodeSphere(1f, 1f, -1f, 0.01f, SFMaterial.instance.pointMaterial, false);
        node.setParent(anchorNode);
        nodeList.add(node);

        node = SFTool.createLocalNodeSphere(1f, -1f, -1f, 0.01f, SFMaterial.instance.pointMaterial, false);
        node.setParent(anchorNode);
        nodeList.add(node);

        Node segmentNode = SFTool.drawSegment(nodeList.get(0), nodeList.get(1), SFMaterial.instance.segmentMaterial, false);
        SFTool.setSegmentSizeTextView(this, SFTool.getLengthOfTwoNode(nodeList.get(0), nodeList.get(1)), SFConstants.SFUnit.M,
                segmentNode, new SFTool.SetSegmentSizeTextViewDelegate() {
                    @Override
                    public void onFinish(ViewRenderable viewRenderable, FaceToCameraNode faceToCameraNode) {

                    }
                });

        segmentNode = SFTool.drawSegment(nodeList.get(1), nodeList.get(2), SFMaterial.instance.segmentMaterial, false);
        SFTool.setSegmentSizeTextView(this, SFTool.getLengthOfTwoNode(nodeList.get(1), nodeList.get(2)), SFConstants.SFUnit.M,
                segmentNode, new SFTool.SetSegmentSizeTextViewDelegate() {
                    @Override
                    public void onFinish(ViewRenderable viewRenderable, FaceToCameraNode faceToCameraNode) {

                    }
                });

        segmentNode = SFTool.drawSegment(nodeList.get(2), nodeList.get(3), SFMaterial.instance.segmentMaterial, false);
        SFTool.setSegmentSizeTextView(this, SFTool.getLengthOfTwoNode(nodeList.get(2), nodeList.get(3)), SFConstants.SFUnit.M,
                segmentNode, new SFTool.SetSegmentSizeTextViewDelegate() {
                    @Override
                    public void onFinish(ViewRenderable viewRenderable, FaceToCameraNode faceToCameraNode) {

                    }
                });

        segmentNode = SFTool.drawSegment(nodeList.get(3), nodeList.get(0), SFMaterial.instance.segmentMaterial, false);
        SFTool.setSegmentSizeTextView(this, SFTool.getLengthOfTwoNode(nodeList.get(3), nodeList.get(0)), SFConstants.SFUnit.M,
                segmentNode, new SFTool.SetSegmentSizeTextViewDelegate() {
                    @Override
                    public void onFinish(ViewRenderable viewRenderable, FaceToCameraNode faceToCameraNode) {

                    }
                });

        segmentNode = SFTool.drawSegment(nodeList.get(4), nodeList.get(5), SFMaterial.instance.segmentMaterial, false);
        SFTool.setSegmentSizeTextView(this, SFTool.getLengthOfTwoNode(nodeList.get(4), nodeList.get(5)), SFConstants.SFUnit.M,
                segmentNode, new SFTool.SetSegmentSizeTextViewDelegate() {
                    @Override
                    public void onFinish(ViewRenderable viewRenderable, FaceToCameraNode faceToCameraNode) {

                    }
                });

        segmentNode = SFTool.drawSegment(nodeList.get(5), nodeList.get(6), SFMaterial.instance.segmentMaterial, false);
        SFTool.setSegmentSizeTextView(this, SFTool.getLengthOfTwoNode(nodeList.get(5), nodeList.get(6)), SFConstants.SFUnit.M,
                segmentNode, new SFTool.SetSegmentSizeTextViewDelegate() {
                    @Override
                    public void onFinish(ViewRenderable viewRenderable, FaceToCameraNode faceToCameraNode) {

                    }
                });

        segmentNode = SFTool.drawSegment(nodeList.get(6), nodeList.get(7), SFMaterial.instance.segmentMaterial, false);
        SFTool.setSegmentSizeTextView(this, SFTool.getLengthOfTwoNode(nodeList.get(6), nodeList.get(7)), SFConstants.SFUnit.M,
                segmentNode, new SFTool.SetSegmentSizeTextViewDelegate() {
                    @Override
                    public void onFinish(ViewRenderable viewRenderable, FaceToCameraNode faceToCameraNode) {

                    }
                });

        segmentNode = SFTool.drawSegment(nodeList.get(7), nodeList.get(4), SFMaterial.instance.segmentMaterial, false);
        SFTool.setSegmentSizeTextView(this, SFTool.getLengthOfTwoNode(nodeList.get(7), nodeList.get(4)), SFConstants.SFUnit.M,
                segmentNode, new SFTool.SetSegmentSizeTextViewDelegate() {
                    @Override
                    public void onFinish(ViewRenderable viewRenderable, FaceToCameraNode faceToCameraNode) {

                    }
                });

        segmentNode = SFTool.drawSegment(nodeList.get(0), nodeList.get(4), SFMaterial.instance.segmentMaterial, false);
        SFTool.setSegmentSizeTextView(this, SFTool.getLengthOfTwoNode(nodeList.get(0), nodeList.get(4)), SFConstants.SFUnit.M,
                segmentNode, new SFTool.SetSegmentSizeTextViewDelegate() {
                    @Override
                    public void onFinish(ViewRenderable viewRenderable, FaceToCameraNode faceToCameraNode) {

                    }
                });

        segmentNode = SFTool.drawSegment(nodeList.get(1), nodeList.get(5), SFMaterial.instance.segmentMaterial, false);
        SFTool.setSegmentSizeTextView(this, SFTool.getLengthOfTwoNode(nodeList.get(1), nodeList.get(5)), SFConstants.SFUnit.M,
                segmentNode, new SFTool.SetSegmentSizeTextViewDelegate() {
                    @Override
                    public void onFinish(ViewRenderable viewRenderable, FaceToCameraNode faceToCameraNode) {

                    }
                });

        segmentNode = SFTool.drawSegment(nodeList.get(2), nodeList.get(6), SFMaterial.instance.segmentMaterial, false);
        SFTool.setSegmentSizeTextView(this, SFTool.getLengthOfTwoNode(nodeList.get(2), nodeList.get(6)), SFConstants.SFUnit.M,
                segmentNode, new SFTool.SetSegmentSizeTextViewDelegate() {
                    @Override
                    public void onFinish(ViewRenderable viewRenderable, FaceToCameraNode faceToCameraNode) {

                    }
                });

//        segmentNode = SFTool.drawSegment(nodeList.get(3), nodeList.get(7), SFMaterial.instance.segmentMaterial, false);
//        SFTool.setSegmentSizeTextView(this, SFTool.getLengthOfTwoNode(nodeList.get(3), nodeList.get(7)), SFConstants.SFUnit.M,
//                segmentNode, new SFTool.SetSegmentSizeTextViewDelegate() {
//                    @Override
//                    public void onFinish(ViewRenderable viewRenderable, FaceToCameraNode faceToCameraNode) {
//
//                    }
//                });
    }

    @Override
    protected void onDestroy() {
        SFMaterial.instance.destroy();
        SFRenderable.instance.destroy();
        sceneView.destroy();
        super.onDestroy();
    }
}