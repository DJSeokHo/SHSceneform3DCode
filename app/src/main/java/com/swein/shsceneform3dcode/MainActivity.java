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
    }

    private AnchorNode anchorNode;

    private void initSceneForm() {

        sceneView.setBackgroundColor(Color.WHITE);

        sceneView.getScene().addOnPeekTouchListener(new Scene.OnPeekTouchListener() {

            float downX = 0;
            float downY = 0;

            boolean isScale = false;

            @Override
            public void onPeekTouch(HitTestResult hitTestResult, MotionEvent motionEvent) {

                if(motionEvent.getPointerCount() == 1) {

                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:

                            downX = motionEvent.getX();
                            downY = motionEvent.getY();

                            break;

                        case MotionEvent.ACTION_MOVE:

                            if(isScale) {
                                return;
                            }

                            if(Math.abs(motionEvent.getX() - downX) > 50 || Math.abs(motionEvent.getY() - downY) > 50) {
                                resetToCenter(sceneView.getWidth(), sceneView.getHeight(), motionEvent.getX(), motionEvent.getY());
                            }

                            break;

                        case MotionEvent.ACTION_UP:

                            isScale = false;
                            downX = 0;
                            downY = 0;
                            break;
                    }

                }
                else if(motionEvent.getPointerCount() == 2) {

                    isScale = true;

                    float percentDistance = distance(motionEvent) / sceneView.getWidth();
                    ILog.iLogDebug(TAG, percentDistance);

                    anchorNode.setWorldScale(new Vector3(percentDistance, percentDistance, percentDistance));
                }

            }
        });

        sceneView.getScene().addOnUpdateListener(frameTime -> {

        });
    }

    private void resetToCenter(int width, int height, float x, float y) {
        x = x - width * 0.5f;
        y = y - height * 0.5f;

        float percentX = x / (width * 0.5f);
        float percentY = y / (height * 0.5f);

        ILog.iLogDebug(TAG, percentX  + " " + percentY);


        Quaternion xQuaternion = Quaternion.axisAngle(new Vector3(0.0f, 1.0f, 0.0f), percentX * 360 * 0.5f);
        Quaternion yQuaternion = Quaternion.axisAngle(new Vector3(1.0f, 0.0f, 0.0f), percentY * 360 * 0.5f);

        anchorNode.setWorldRotation(Quaternion.multiply(xQuaternion, yQuaternion));
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

        segmentNode = SFTool.drawSegment(nodeList.get(3), nodeList.get(7), SFMaterial.instance.segmentMaterial, false);
        SFTool.setSegmentSizeTextView(this, SFTool.getLengthOfTwoNode(nodeList.get(3), nodeList.get(7)), SFConstants.SFUnit.M,
                segmentNode, new SFTool.SetSegmentSizeTextViewDelegate() {
                    @Override
                    public void onFinish(ViewRenderable viewRenderable, FaceToCameraNode faceToCameraNode) {

                    }
                });
    }

    @Override
    protected void onDestroy() {
        SFMaterial.instance.destroy();
        SFRenderable.instance.destroy();
        sceneView.destroy();
        super.onDestroy();
    }
}