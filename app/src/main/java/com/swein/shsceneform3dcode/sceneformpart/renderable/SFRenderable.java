package com.swein.shsceneform3dcode.sceneformpart.renderable;

import android.content.Context;

import com.google.ar.sceneform.rendering.ViewRenderable;
import com.swein.shsceneform3dcode.R;
import com.swein.shsceneform3dcode.sceneformpart.tool.SFTool;

public class SFRenderable {

    public static SFRenderable instance = new SFRenderable();
    private SFRenderable() {}

    public ViewRenderable guideSizeTextView;

    public void init(Context context) {
        SFTool.createViewRenderable(context, R.layout.view_renderable_text, viewRenderable -> {
            guideSizeTextView = viewRenderable;
        }, false);
    }

    public void destroy() {
        guideSizeTextView = null;
    }
}
