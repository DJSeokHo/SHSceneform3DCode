package com.swein.shsceneform3dcode.sceneformpart.data.room.model;

import android.content.Context;

import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Node;
import com.swein.shsceneform3dcode.sceneformpart.constants.SFConstants;
import com.swein.shsceneform3dcode.sceneformpart.data.room.bean.RoomBean;
import com.swein.shsceneform3dcode.sceneformpart.data.room.bean.basic.PlaneBean;
import com.swein.shsceneform3dcode.sceneformpart.data.room.bean.basic.PointBean;
import com.swein.shsceneform3dcode.sceneformpart.data.room.model.basic.PlaneModel;
import com.swein.shsceneform3dcode.sceneformpart.data.room.model.basic.PointModel;
import com.swein.shsceneform3dcode.sceneformpart.material.SFMaterial;
import com.swein.shsceneform3dcode.sceneformpart.tool.SFTool;

import java.util.ArrayList;
import java.util.List;

public class RoomModel {

    private final static String TAG = "RoomModel";

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

    public void createWallModel(AnchorNode anchorNode) {

        floorPlaneModel = new PlaneModel();

        // draw floor
        PointModel pointModel;
        for(int i = 0; i < roomBean.floorPlaneBean.pointList.size(); i++) {
            pointModel = new PointModel(roomBean.floorPlaneBean.pointList.get(i));
            floorPlaneModel.pointModelList.add(pointModel);
        }

        floorPlaneModel.drawPlaneWithoutClose(anchorNode);

        // draw ceiling
        ceilingPlaneModel = new PlaneModel();
        for(int i = 0; i < roomBean.ceilingPlaneBean.pointList.size(); i++) {
            pointModel = new PointModel(roomBean.ceilingPlaneBean.pointList.get(i));
            ceilingPlaneModel.pointModelList.add(pointModel);
        }

        ceilingPlaneModel.drawPlaneWithoutClose(anchorNode);

        // draw wall
        Node wallSegmentNode;
        for(int i = 0; i < floorPlaneModel.pointModelList.size(); i++) {
            wallSegmentNode = SFTool.drawSegment(floorPlaneModel.pointModelList.get(i).pointNode, ceilingPlaneModel.pointModelList.get(i).pointNode, SFMaterial.instance.segmentMaterial, false);
            SFTool.setSegmentSizeTextView(context,
                    roomBean.height, SFConstants.SFUnit.M,
                    wallSegmentNode, (viewRenderable, faceToCameraNode) -> {

                    });
        }

        for(int i = 0; i < ceilingPlaneModel.segmentModelList.size(); i++) {
            SFTool.setSegmentSizeTextView(context,
                    ceilingPlaneModel.segmentModelList.get(i).length, SFConstants.SFUnit.M,
                    ceilingPlaneModel.segmentModelList.get(i).segmentNode, (viewRenderable, faceToCameraNode) -> {

                    });
        }

        // draw wall object
        PlaneModel planeModel;
        PointModel wallObjectPointModel;
        PointBean offsetPointBean;
        PlaneBean wallObjectPlaneBean;

        // for wall object list
        for(int k = 0; k < roomBean.wallObjectList.size(); k++) {

            planeModel = new PlaneModel();

            wallObjectPlaneBean = roomBean.wallObjectList.get(k);

            // for floor point list
            for(int i = 0; i < roomBean.floorPlaneBean.pointList.size(); i++) {

                if(i == wallObjectPlaneBean.objectOnIndex) {

                    // for wall object point list
                    for(int j = 0; j < wallObjectPlaneBean.pointList.size(); j++) {

                        offsetPointBean = new PointBean();
                        offsetPointBean.x = wallObjectPlaneBean.pointList.get(j).x + roomBean.floorPlaneBean.pointList.get(i).x;
                        offsetPointBean.y = wallObjectPlaneBean.pointList.get(j).y + roomBean.floorPlaneBean.pointList.get(i).y;
                        offsetPointBean.z = 0;

                        wallObjectPointModel = new PointModel(offsetPointBean);
                        planeModel.pointModelList.add(wallObjectPointModel);
                    }
                }
            }

            planeModel.drawPlane(anchorNode);
            wallObjectPlaneModelList.add(planeModel);

            // draw wall object size
            for(int i = 0; i < planeModel.segmentModelList.size(); i++) {

                if(i == 0 || i == 1) {
                    SFTool.setSegmentSizeTextView(context,
                            planeModel.segmentModelList.get(i).length, SFConstants.SFUnit.M,
                            planeModel.segmentModelList.get(i).segmentNode, (viewRenderable, faceToCameraNode) -> {

                            });
                }

            }
        }

    }

    public void create2DModel(AnchorNode anchorNode) {
        // draw floor
        floorPlaneModel = new PlaneModel(roomBean.floorPlaneBean);
        floorPlaneModel.drawPlane(anchorNode);
    }

    public void create2DModelSizeSymbol() {
        // ceiling
        for(int i = 0; i < floorPlaneModel.segmentModelList.size(); i++) {
            SFTool.setSegmentSizeTextView(context,
                    floorPlaneModel.segmentModelList.get(i).length, SFConstants.SFUnit.M,
                    floorPlaneModel.segmentModelList.get(i).segmentNode, (viewRenderable, faceToCameraNode) -> {

                    });
        }
    }

    public void create3DModel(AnchorNode anchorNode) {

        // draw floor
        floorPlaneModel = new PlaneModel(roomBean.floorPlaneBean);
        floorPlaneModel.drawPlane(anchorNode);

        // draw ceiling
        ceilingPlaneModel = new PlaneModel(roomBean.ceilingPlaneBean);
        ceilingPlaneModel.drawPlane(anchorNode);

        // draw wall
        PlaneModel wallPlaneModel;
        for(PlaneBean wallPlaneBean : roomBean.wallList) {
            wallPlaneModel = new PlaneModel(wallPlaneBean);
            wallPlaneModel.drawPlane(anchorNode);
            wallPlaneModelList.add(wallPlaneModel);
        }

        // draw wall object
        PlaneModel wallObjectPlaneModel;
        for(PlaneBean wallObjectPlaneBean : roomBean.wallObjectList) {
            wallObjectPlaneModel = new PlaneModel(wallObjectPlaneBean);
            wallObjectPlaneModel.drawPlane(anchorNode);
            wallObjectPlaneModelList.add(wallObjectPlaneModel);
        }
    }

    public void create3DSizeSymbol() {

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
