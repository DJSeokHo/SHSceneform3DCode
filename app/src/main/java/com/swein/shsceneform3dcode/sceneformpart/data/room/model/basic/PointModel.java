package com.swein.shsceneform3dcode.sceneformpart.data.room.model.basic;

import com.google.ar.sceneform.Node;
import com.swein.shsceneform3dcode.sceneformpart.data.room.bean.basic.PointBean;
import com.swein.shsceneform3dcode.sceneformpart.material.SFMaterial;
import com.swein.shsceneform3dcode.sceneformpart.tool.SFTool;

public class PointModel {

    public Node pointNode;

    public PointModel(PointBean pointBean) {
        pointNode = SFTool.createLocalNode(pointBean.x, pointBean.y, pointBean.z, 0.01f, SFMaterial.instance.pointMaterial, false);
    }

    public void clear() {
        if(pointNode != null) {
            pointNode.setParent(null);
            pointNode = null;
        }
    }

}
