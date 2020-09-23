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
import com.swein.shsceneform3dcode.framework.thread.ThreadUtil;
import com.swein.shsceneform3dcode.sceneformpart.FaceToCameraNode;
import com.swein.shsceneform3dcode.sceneformpart.constants.SFConstants;
import com.swein.shsceneform3dcode.sceneformpart.material.SFMaterial;
import com.swein.shsceneform3dcode.sceneformpart.renderable.SFRenderable;
import com.swein.shsceneform3dcode.sceneformpart.tool.SFTool;

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

        node = SFTool.createLocalNodeSphere(-1f, -1f, 1f, 0.1f, SFMaterial.instance.pointMaterial, false);
        node.setParent(anchorNode);
        nodeList.add(node);

        node = SFTool.createLocalNodeSphere(-1f, 1f, 1f, 0.1f, SFMaterial.instance.pointMaterial, false);
        node.setParent(anchorNode);
        nodeList.add(node);

        node = SFTool.createLocalNodeSphere(1f, 1f, 1f, 0.1f, SFMaterial.instance.pointMaterial, false);
        node.setParent(anchorNode);
        nodeList.add(node);

        node = SFTool.createLocalNodeSphere(1f, -1f, 1f, 0.1f, SFMaterial.instance.pointMaterial, false);
        node.setParent(anchorNode);
        nodeList.add(node);

        node = SFTool.createLocalNodeSphere(-1f, -1f, -1f, 0.1f, SFMaterial.instance.pointMaterial, false);
        node.setParent(anchorNode);
        nodeList.add(node);

        node = SFTool.createLocalNodeSphere(-1f, 1f, -1f, 0.1f, SFMaterial.instance.pointMaterial, false);
        node.setParent(anchorNode);
        nodeList.add(node);

        node = SFTool.createLocalNodeSphere(1f, 1f, -1f, 0.1f, SFMaterial.instance.pointMaterial, false);
        node.setParent(anchorNode);
        nodeList.add(node);

        node = SFTool.createLocalNodeSphere(1f, -1f, -1f, 0.1f, SFMaterial.instance.pointMaterial, false);
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