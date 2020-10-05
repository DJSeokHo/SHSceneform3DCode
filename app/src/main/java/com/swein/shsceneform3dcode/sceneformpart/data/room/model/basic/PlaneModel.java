package com.swein.shsceneform3dcode.sceneformpart.data.room.model.basic;

import com.google.ar.sceneform.AnchorNode;
import com.swein.shsceneform3dcode.sceneformpart.data.room.bean.basic.PlaneBean;
import com.swein.shsceneform3dcode.sceneformpart.data.room.bean.basic.PointBean;
import com.swein.shsceneform3dcode.sceneformpart.material.SFMaterial;
import com.swein.shsceneform3dcode.sceneformpart.tool.MathTool;
import com.swein.shsceneform3dcode.sceneformpart.tool.SFTool;

import java.util.ArrayList;
import java.util.List;

public class PlaneModel {

    public List<PointModel> pointModelList = new ArrayList<>();
    public List<SegmentModel> segmentModelList = new ArrayList<>();

    public PlaneModel(PlaneBean planeBean, AnchorNode anchorNode) {

        PointModel pointModel;
        for(int i = 0; i < planeBean.pointList.size(); i++) {
            pointModel = new PointModel(planeBean.pointList.get(i));
            pointModel.pointNode.setParent(anchorNode);
            pointModelList.add(pointModel);
        }

        if(pointModelList.size() < 2) {
            return;
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
                SFTool.drawSegment(pointModelList.get(planeBean.pointList.size() - 1).pointNode, pointModelList.get(0).pointNode, SFMaterial.instance.segmentMaterial, false),
                MathTool.getLengthOfTwoNode(pointModelList.get(planeBean.pointList.size() - 1).pointNode, pointModelList.get(0).pointNode)
        );
        segmentModelList.add(segmentModel);
    }

    public PlaneModel(PlaneBean planeBean, PointBean offsetPoint) {

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
