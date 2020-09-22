package com.swein.shsceneform3dcode.sceneformpart.material;

import android.content.Context;
import android.graphics.Color;

import com.google.ar.sceneform.rendering.Material;
import com.swein.shsceneform3dcode.sceneformpart.tool.SFTool;

public class SFMaterial {

    public static SFMaterial instance = new SFMaterial();
    private SFMaterial() {}

    // material
    public Material pointMaterial;
    public Material segmentMaterial;

    public Material wallPointMaterial;
    public Material wallSegmentMaterial;

    public void init(Context context) {

        // create node material
        SFTool.createColorMaterial(context, Color.GREEN, material -> {
            pointMaterial = material;
            segmentMaterial = material;
        });

        SFTool.createColorMaterial(context, Color.RED, material -> {
            wallPointMaterial = material;
            wallSegmentMaterial = material;
        });
    }

    public void destroy() {

        pointMaterial = null;
        segmentMaterial = null;
    }
}
