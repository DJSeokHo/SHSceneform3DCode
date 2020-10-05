package com.swein.shsceneform3dcode.sceneformpart.data.room.model.basic;

import com.google.ar.sceneform.AnchorNode;
import com.swein.shsceneform3dcode.sceneformpart.data.room.bean.basic.PlaneBean;
import com.swein.shsceneform3dcode.sceneformpart.material.SFMaterial;
import com.swein.shsceneform3dcode.sceneformpart.tool.MathTool;
import com.swein.shsceneform3dcode.sceneformpart.tool.SFTool;

import java.util.ArrayList;
import java.util.List;

public class PlaneModel {

    public List<PointModel> pointModelList = new ArrayList<>();
    public List<SegmentModel> segmentModelList = new ArrayList<>();

    public PlaneModel() {

    }

    public PlaneModel(PlaneBean planeBean) {

        PointModel pointModel;
        for(int i = 0; i < planeBean.pointList.size(); i++) {
            pointModel = new PointModel(planeBean.pointList.get(i));
            pointModelList.add(pointModel);
        }

    }

    public void drawPlane(AnchorNode anchorNode) {

        if(pointModelList.size() < 2) {
            return;
        }

        for(int i = 0; i < pointModelList.size(); i++) {
            pointModelList.get(i).pointNode.setParent(anchorNode);
        }

        SegmentModel segmentModel;
        for(int i = 0; i < pointModelList.size() - 1; i++) {

            segmentModel = new SegmentModel(
                    SFTool.drawSegment(pointModelList.get(i).pointNode, pointModelList.get(i + 1).pointNode, SFMaterial.instance.segmentMaterial, false),
                    MathTool.getLengthOfTwoNode(pointModelList.get(i).pointNode, pointModelList.get(i + 1).pointNode)
            );
            segmentModelList.add(segmentModel);
        }

        segmentModel = new SegmentModel(
                SFTool.drawSegment(pointModelList.get(pointModelList.size() - 1).pointNode, pointModelList.get(0).pointNode, SFMaterial.instance.segmentMaterial, false),
                MathTool.getLengthOfTwoNode(pointModelList.get(pointModelList.size() - 1).pointNode, pointModelList.get(0).pointNode)
        );
        segmentModelList.add(segmentModel);

    }

    public void clear() {

        for(int i = 0; i < pointModelList.size(); i++) {
            pointModelList.get(i).clear();
        }
        pointModelList.clear();

        for(int i = 0; i < segmentModelList.size(); i++) {
            segmentModelList.get(i).clear();
        }
        segmentModelList.clear();
    }
}
