package com.swein.shsceneform3dcode.sceneformpart.tool;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.Material;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.swein.shsceneform3dcode.R;
import com.swein.shsceneform3dcode.sceneformpart.FaceToCameraNode;
import com.swein.shsceneform3dcode.sceneformpart.constants.SFConstants;

public class SFTool {

    /**
     * create material
     */
    public interface MaterialCreatedDelegate {
        void onCreated(Material material);
    }
    
    public static void createColorMaterial(Context context, int color, MaterialCreatedDelegate materialCreatedDelegate) {
        MaterialFactory
                .makeOpaqueWithColor(context, new Color(color))
                .thenAccept(materialCreatedDelegate::onCreated);
    }

    /**
     * create view render able
     */
    public interface ViewRenderableCreatedDelegate {
        void onCreated(ViewRenderable viewRenderable);
    }
    public static void createViewRenderable(Context context, int layoutResource, ViewRenderableCreatedDelegate viewRenderableCreatedDelegate, boolean shadow) {
        ViewRenderable.builder()
                .setView(context, layoutResource)
                .build()
                .thenAccept(viewRenderable -> {
                    viewRenderable.setShadowCaster(shadow);
                    viewRenderable.setShadowReceiver(shadow);
                    viewRenderableCreatedDelegate.onCreated(viewRenderable);
                });
    }

    public static Node createLocalNode(float tx, float ty, float tz, float radius, Material material, boolean shadow) {
        ModelRenderable modelRenderable = ShapeFactory.makeSphere(radius, Vector3.zero(), material);
        modelRenderable.setShadowReceiver(shadow);
        modelRenderable.setShadowCaster(shadow);
        Node node = new Node();
        node.setRenderable(modelRenderable);
        node.setLocalPosition(new Vector3(tx, ty, tz));
        return node;
    }

    public static Node drawSegment(Node startNode, Node endNode, Material lineMaterial, boolean shadow) {

        Vector3 startVector3 = startNode.getWorldPosition();
        Vector3 endVector3 = endNode.getWorldPosition();

        Vector3 difference = Vector3.subtract(startVector3, endVector3);
        Vector3 directionFromTopToBottom = difference.normalized();
        Quaternion rotationFromAToB = Quaternion.lookRotation(directionFromTopToBottom, Vector3.up());

        ModelRenderable lineModelRenderable = ShapeFactory.makeCube(new Vector3(0.02f, 0.02f, difference.length()), Vector3.zero(), lineMaterial);
        lineModelRenderable.setShadowCaster(shadow);
        lineModelRenderable.setShadowReceiver(shadow);

        Node lineNode = new Node();

        lineNode.setParent(startNode);
        lineNode.setRenderable(lineModelRenderable);
        lineNode.setWorldPosition(Vector3.add(startVector3, endVector3).scaled(0.5f));
        lineNode.setWorldRotation(rotationFromAToB);

        return lineNode;
    }

    public interface SetSegmentSizeTextViewDelegate {
        void onFinish(ViewRenderable viewRenderable, FaceToCameraNode faceToCameraNode);
    }

    public static void setSegmentSizeTextView(Context context, float originalLength, SFConstants.SFUnit sfUnit, Node parentNode, @Nullable SetSegmentSizeTextViewDelegate setSegmentSizeTextViewDelegate) {
        float length = MathTool.getLengthByUnit(sfUnit, originalLength);

        ViewRenderable.builder()
                .setView(context, R.layout.view_renderable_text)
                .build()
                .thenAccept(viewRenderable -> {

                    TextView textView = ((TextView)viewRenderable.getView());
                    textView.setText(String.format("%.2f", length) + " " + MathTool.getLengthUnitString(sfUnit));
                    viewRenderable.setShadowCaster(false);
                    viewRenderable.setShadowReceiver(false);

                    FaceToCameraNode faceToCameraNode = new FaceToCameraNode();
                    faceToCameraNode.setParent(parentNode);

                    faceToCameraNode.setLocalRotation(Quaternion.axisAngle(new Vector3(0f, 1f, 0f), 0f));
                    faceToCameraNode.setLocalPosition(new Vector3(0f, 0.03f, 0f));
                    faceToCameraNode.setRenderable(viewRenderable);

                    if(setSegmentSizeTextViewDelegate != null) {
                        setSegmentSizeTextViewDelegate.onFinish(viewRenderable, faceToCameraNode);
                    }
                });
    }

    public static void setSegmentSizeTextView(Context context, float originalLength, SFConstants.SFUnit sfUnit, Node parentNode, float textHeight, @Nullable SetSegmentSizeTextViewDelegate setSegmentSizeTextViewDelegate) {
        float length = MathTool.getLengthByUnit(sfUnit, originalLength);

        ViewRenderable.builder()
                .setView(context, R.layout.view_renderable_text)
                .build()
                .thenAccept(viewRenderable -> {

                    TextView textView = ((TextView)viewRenderable.getView());
                    textView.setText(String.format("%.2f", length) + " " + MathTool.getLengthUnitString(sfUnit));
                    viewRenderable.setShadowCaster(false);
                    viewRenderable.setShadowReceiver(false);

                    FaceToCameraNode faceToCameraNode = new FaceToCameraNode();
                    faceToCameraNode.setParent(parentNode);

                    faceToCameraNode.setLocalRotation(Quaternion.axisAngle(new Vector3(0f, 1f, 0f), 0f));
                    faceToCameraNode.setLocalPosition(new Vector3(0f, textHeight, 0f));
                    faceToCameraNode.setRenderable(viewRenderable);

                    if(setSegmentSizeTextViewDelegate != null) {
                        setSegmentSizeTextViewDelegate.onFinish(viewRenderable, faceToCameraNode);
                    }
                });
    }

}
