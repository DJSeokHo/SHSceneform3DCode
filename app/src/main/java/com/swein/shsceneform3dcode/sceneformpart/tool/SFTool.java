package com.swein.shsceneform3dcode.sceneformpart.tool;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.SuperscriptSpan;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.ar.core.Anchor;
import com.google.ar.sceneform.AnchorNode;
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

import java.util.List;

import static com.swein.shsceneform3dcode.sceneformpart.constants.SFConstants.SFUnit.CM;
import static com.swein.shsceneform3dcode.sceneformpart.constants.SFConstants.SFUnit.M;

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

    public static Node createWorldNodeSphere(float tx, float ty, float tz, float radius, Material material, boolean shadow) {
        ModelRenderable modelRenderable = ShapeFactory.makeSphere(radius, Vector3.zero(), material);
        modelRenderable.setShadowReceiver(shadow);
        modelRenderable.setShadowCaster(shadow);
        Node node = new Node();
        node.setRenderable(modelRenderable);
        node.setWorldPosition(new Vector3(tx, ty, tz));
        return node;
    }

    public static Node createLocalNodeSphere(float tx, float ty, float tz, float radius, Material material, boolean shadow) {
        ModelRenderable modelRenderable = ShapeFactory.makeSphere(radius, Vector3.zero(), material);
        modelRenderable.setShadowReceiver(shadow);
        modelRenderable.setShadowCaster(shadow);
        Node node = new Node();
        node.setRenderable(modelRenderable);
        node.setLocalPosition(new Vector3(tx, ty, tz));
        return node;
    }

    public static Node createWorldNodeCube(float tx, float ty, float tz, float radius, Material material, boolean shadow) {
        ModelRenderable modelRenderable = ShapeFactory.makeCube(new Vector3(radius, radius, radius), Vector3.zero(), material);
        modelRenderable.setShadowReceiver(shadow);
        modelRenderable.setShadowCaster(shadow);
        Node node = new Node();
        node.setRenderable(modelRenderable);
        node.setWorldPosition(new Vector3(tx, ty, tz));
        return node;
    }

    public static Node drawSegment(Node startNode, Node endNode, Material lineMaterial, boolean shadow) {

        Vector3 startVector3 = startNode.getWorldPosition();
        Vector3 endVector3 = endNode.getWorldPosition();

        Vector3 difference = Vector3.subtract(startVector3, endVector3);
        Vector3 directionFromTopToBottom = difference.normalized();
        Quaternion rotationFromAToB = Quaternion.lookRotation(directionFromTopToBottom, Vector3.up());

        ModelRenderable lineModelRenderable = ShapeFactory.makeCube(new Vector3(0.05f, 0.05f, difference.length()), Vector3.zero(), lineMaterial);
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
        float length = getLengthByUnit(sfUnit, originalLength);

        ViewRenderable.builder()
                .setView(context, R.layout.view_renderable_text)
                .build()
                .thenAccept(viewRenderable -> {

                    TextView textView = ((TextView)viewRenderable.getView());
                    textView.setText(String.format("%.2f", length) + " " + getLengthUnitString(sfUnit));
                    viewRenderable.setShadowCaster(false);
                    viewRenderable.setShadowReceiver(false);

                    FaceToCameraNode faceToCameraNode = new FaceToCameraNode();
                    faceToCameraNode.setParent(parentNode);

                    faceToCameraNode.setLocalRotation(Quaternion.axisAngle(new Vector3(0f, 1f, 0f), 0f));
                    faceToCameraNode.setLocalPosition(new Vector3(0f, 0.5f, 0f));
                    faceToCameraNode.setRenderable(viewRenderable);

                    if(setSegmentSizeTextViewDelegate != null) {
                        setSegmentSizeTextViewDelegate.onFinish(viewRenderable, faceToCameraNode);
                    }
                });
    }

    public static void setSegmentSizeTextView(Context context, float originalLength, SFConstants.SFUnit sfUnit, Node parentNode, float textHeight, @Nullable SetSegmentSizeTextViewDelegate setSegmentSizeTextViewDelegate) {
        float length = getLengthByUnit(sfUnit, originalLength);

        ViewRenderable.builder()
                .setView(context, R.layout.view_renderable_text)
                .build()
                .thenAccept(viewRenderable -> {

                    TextView textView = ((TextView)viewRenderable.getView());
                    textView.setText(String.format("%.2f", length) + " " + getLengthUnitString(sfUnit));
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

    public static float getLengthOfTwoNode(Node startNode, Node endNode) {
        Vector3 startVector3 = startNode.getWorldPosition();
        Vector3 endVector3 = endNode.getWorldPosition();

        Vector3 difference = Vector3.subtract(startVector3, endVector3);
        return difference.length();
    }

    public static SFConstants.SFUnit getUnit(String unit) {
        switch (unit) {
            case "m":
                return M;

            case "cm":
                return CM;

            default:
                return M;
        }
    }

    public static String getLengthUnitString(SFConstants.SFUnit sfUnit) {
        switch (sfUnit) {
            case M:
                return "m";

            case CM:
                return "cm";

            default:
                return "";
        }
    }

    public static SpannableString getAreaUnitString(SFConstants.SFUnit sfUnit) {
        switch (sfUnit) {
            case M:
                return getM2();

            case CM:
                return getCM2();

            default:
                return null;
        }
    }

    public static SpannableString getVolumeUnitString(SFConstants.SFUnit sfUnit) {
        switch (sfUnit) {
            case M:
                return getM3();

            case CM:
                return getCM3();

            default:
                return null;
        }
    }

    public static float getLengthByUnit(SFConstants.SFUnit sfUnit, float length) {
        switch (sfUnit) {
            case CM:
                return length * 100;

            case M:
                return length;

            default:
                return 0;
        }
    }

    public static float getHeightByUnit(SFConstants.SFUnit sfUnit, float height) {
        switch (sfUnit) {
            case CM:
                return height * 0.01f;

            case M:
                return height;

            default:
                return 0;
        }
    }

    public static float getAreaByUnit(SFConstants.SFUnit sfUnit, float area) {
        switch (sfUnit) {
            case CM:
                return area * 10000;

            case M:
                return area;

            default:
                return 0;
        }
    }

    public static float getVolumeByUnit(SFConstants.SFUnit sfUnit, float volume) {
        switch (sfUnit) {
            case CM:
                return volume * 1000000;

            case M:
                return volume;

            default:
                return 0;
        }
    }

    public static SpannableString getM2() {
        SpannableString m2 = new SpannableString("m2");
        m2.setSpan(new RelativeSizeSpan(0.5f), 1, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        m2.setSpan(new SuperscriptSpan(), 1, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return m2;
    }

    public static SpannableString getCM2() {
        SpannableString cm2 = new SpannableString("cm2");
        cm2.setSpan(new RelativeSizeSpan(0.5f), 2, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        cm2.setSpan(new SuperscriptSpan(), 2, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return cm2;
    }

    public static SpannableString getM3() {
        SpannableString m2 = new SpannableString("m3");
        m2.setSpan(new RelativeSizeSpan(0.5f), 1, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        m2.setSpan(new SuperscriptSpan(), 1, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return m2;
    }

    public static SpannableString getCM3() {
        SpannableString cm2 = new SpannableString("cm3");
        cm2.setSpan(new RelativeSizeSpan(0.5f), 2, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        cm2.setSpan(new SuperscriptSpan(), 2, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return cm2;
    }
}
