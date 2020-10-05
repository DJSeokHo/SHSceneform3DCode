package com.swein.shsceneform3dcode.sceneformpart.data.room.model;

import android.content.Context;

import com.google.ar.sceneform.AnchorNode;
import com.swein.shsceneform3dcode.sceneformpart.data.room.bean.RoomBean;
import com.swein.shsceneform3dcode.sceneformpart.data.room.bean.basic.PlaneBean;
import com.swein.shsceneform3dcode.sceneformpart.constants.SFConstants;
import com.swein.shsceneform3dcode.sceneformpart.data.room.model.basic.PlaneModel;
import com.swein.shsceneform3dcode.sceneformpart.tool.SFTool;

import java.util.ArrayList;
import java.util.List;

public class RoomModel {

    public RoomBean roomBean;

    public Context context;

    // floor
    public PlaneModel floorPlaneModel;

    // ceiling
    public PlaneModel ceilingPlaneModel;

    // room wall
    public List<PlaneModel> wallPlaneModelList = new ArrayList<>();

    // object on the wall
    public List<PlaneModel> wallObjectPlaneModelList = new ArrayList<>();

    public RoomModel(RoomBean roomBean) {
        this.roomBean = roomBean;
    }

    public void createRoom(AnchorNode anchorNode) {

        // draw floor
        floorPlaneModel = new PlaneModel(roomBean.floorPlaneBean, anchorNode);

        // draw ceiling
        ceilingPlaneModel = new PlaneModel(roomBean.ceilingPlaneBean, anchorNode);

        // draw wall
        PlaneModel wallPlaneModel;
        for(PlaneBean wallPlaneBean : roomBean.wallList) {
            wallPlaneModel = new PlaneModel(wallPlaneBean, anchorNode);
            wallPlaneModelList.add(wallPlaneModel);
        }

        // draw wall object
        PlaneModel wallObjectPlaneModel;
        for(PlaneBean wallObjectPlaneBean : roomBean.wallObjectList) {
            wallObjectPlaneModel = new PlaneModel(wallObjectPlaneBean, anchorNode);
            wallObjectPlaneModelList.add(wallObjectPlaneModel);
        }

//        Node segmentNode;
//        for(int i = 0; i < roomBean.floor.segmentBeanList.size(); i++) {
//            segmentNode = SFTool.drawSegment(
//                    roomBean.floor.segmentBeanList.get(i).startPointBean.point,
//                    roomBean.floor.segmentBeanList.get(i).endPointBean.point,
//                    SFMaterial.instance.segmentMaterial, false);
//
//            SFTool.setSegmentSizeTextView(view.getContext(),
//                    MathTool.getLengthOfTwoNode(roomBean.floor.segmentBeanList.get(i).startPointBean.point,
//                            roomBean.floor.segmentBeanList.get(i).endPointBean.point), SFConstants.SFUnit.M,
//                    segmentNode, (viewRenderable, faceToCameraNode) -> {
//
//                    });
//        }
    }

    public void createSizeSymbol() {

        // ceiling
        for(int i = 0; i < ceilingPlaneModel.segmentModelList.size(); i++) {
            SFTool.setSegmentSizeTextView(context,
                    ceilingPlaneModel.segmentModelList.get(i).length, SFConstants.SFUnit.M,
                    ceilingPlaneModel.segmentModelList.get(i).segmentNode, (viewRenderable, faceToCameraNode) -> {

                    });
        }

        // height
        SFTool.setSegmentSizeTextView(context,
                wallPlaneModelList.get(0).segmentModelList.get(1).length, SFConstants.SFUnit.M,
                wallPlaneModelList.get(0).segmentModelList.get(1).segmentNode, (viewRenderable, faceToCameraNode) -> {

                });

        // wall object
        for(int i = 0; i < wallObjectPlaneModelList.size(); i++) {
            SFTool.setSegmentSizeTextView(context,
                    wallObjectPlaneModelList.get(i).segmentModelList.get(0).length, SFConstants.SFUnit.M,
                    wallObjectPlaneModelList.get(i).segmentModelList.get(0).segmentNode, (viewRenderable, faceToCameraNode) -> {

                    });

            SFTool.setSegmentSizeTextView(context,
                    wallObjectPlaneModelList.get(i).segmentModelList.get(1).length, SFConstants.SFUnit.M,
                    wallObjectPlaneModelList.get(i).segmentModelList.get(1).segmentNode, (viewRenderable, faceToCameraNode) -> {

                    });
        }
    }

    public void destroy() {
        if(floorPlaneModel != null) {
            floorPlaneModel.clear();
            floorPlaneModel = null;
        }

        if(ceilingPlaneModel != null) {
            ceilingPlaneModel.clear();
            ceilingPlaneModel = null;
        }

        if(wallPlaneModelList != null) {
            for(int i = 0; i < wallPlaneModelList.size(); i++) {
                wallPlaneModelList.get(i).clear();
            }
            wallPlaneModelList.clear();
            wallPlaneModelList = null;
        }

        if(wallObjectPlaneModelList != null) {
            for(int i = 0; i < wallObjectPlaneModelList.size(); i++) {
                wallObjectPlaneModelList.get(i).clear();
            }
            wallObjectPlaneModelList.clear();
            wallObjectPlaneModelList = null;
        }
    }
}
