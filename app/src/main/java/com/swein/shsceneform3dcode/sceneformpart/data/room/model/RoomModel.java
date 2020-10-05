package com.swein.shsceneform3dcode.sceneformpart.data.room.model;

import android.content.Context;

import com.google.ar.sceneform.AnchorNode;
import com.swein.shsceneform3dcode.sceneformpart.constants.SFConstants;
import com.swein.shsceneform3dcode.sceneformpart.data.room.bean.RoomBean;
import com.swein.shsceneform3dcode.sceneformpart.data.room.bean.basic.PlaneBean;
import com.swein.shsceneform3dcode.sceneformpart.data.room.bean.basic.PointBean;
import com.swein.shsceneform3dcode.sceneformpart.data.room.model.basic.PlaneModel;
import com.swein.shsceneform3dcode.sceneformpart.data.room.model.basic.PointModel;
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

        List<PlaneModel> list = new ArrayList<>();

        // create start wall
        PlaneModel planeModel = new PlaneModel();

        // bottom left
        PointBean pointBean = new PointBean();
        pointBean.x = 0;
        pointBean.y = 0;
        pointBean.z = 0;
        PointModel pointModel = new PointModel(pointBean);
        planeModel.pointModelList.add(pointModel);

        // bottom right
        pointBean = new PointBean();
        pointBean.x = roomBean.floorPlaneBean.segmentBeanList.get(0).length;
        pointBean.y = 0;
        pointBean.z = 0;
        pointModel = new PointModel(pointBean);
        planeModel.pointModelList.add(pointModel);

        // top right
        pointBean = new PointBean();
        pointBean.x = roomBean.floorPlaneBean.segmentBeanList.get(0).length;
        pointBean.y = roomBean.height;
        pointBean.z = 0;
        pointModel = new PointModel(pointBean);
        planeModel.pointModelList.add(pointModel);

        // top left
        pointBean = new PointBean();
        pointBean.x = 0;
        pointBean.y = roomBean.height;
        pointBean.z = 0;
        pointModel = new PointModel(pointBean);
        planeModel.pointModelList.add(pointModel);

        planeModel.drawPlane(anchorNode);
        list.add(planeModel);


        planeModel = new PlaneModel();

        // bottom left
        pointBean = new PointBean();
        pointBean.x = roomBean.floorPlaneBean.segmentBeanList.get(0).length;
        pointBean.y = 0;
        pointBean.z = 0;
        pointModel = new PointModel(pointBean);
        planeModel.pointModelList.add(pointModel);

        // bottom right
        pointBean = new PointBean();
        pointBean.x = roomBean.floorPlaneBean.segmentBeanList.get(0).length + roomBean.floorPlaneBean.segmentBeanList.get(1).length;
        pointBean.y = 0;
        pointBean.z = 0;
        pointModel = new PointModel(pointBean);
        planeModel.pointModelList.add(pointModel);

        // top right
        pointBean = new PointBean();
        pointBean.x = roomBean.floorPlaneBean.segmentBeanList.get(0).length + roomBean.floorPlaneBean.segmentBeanList.get(1).length;
        pointBean.y = roomBean.height;
        pointBean.z = 0;
        pointModel = new PointModel(pointBean);
        planeModel.pointModelList.add(pointModel);

        // top left
        pointBean = new PointBean();
        pointBean.x = roomBean.floorPlaneBean.segmentBeanList.get(0).length;
        pointBean.y = roomBean.height;
        pointBean.z = 0;
        pointModel = new PointModel(pointBean);
        planeModel.pointModelList.add(pointModel);

        planeModel.drawPlane(anchorNode);



        planeModel = new PlaneModel();

        // bottom left
        pointBean = new PointBean();
        pointBean.x = roomBean.floorPlaneBean.segmentBeanList.get(0).length;
        pointBean.y = 0;
        pointBean.z = 0;
        pointModel = new PointModel(pointBean);
        planeModel.pointModelList.add(pointModel);

        // bottom right
        pointBean = new PointBean();
        pointBean.x = roomBean.floorPlaneBean.segmentBeanList.get(0).length + roomBean.floorPlaneBean.segmentBeanList.get(1).length;
        pointBean.y = 0;
        pointBean.z = 0;
        pointModel = new PointModel(pointBean);
        planeModel.pointModelList.add(pointModel);

        // top right
        pointBean = new PointBean();
        pointBean.x = roomBean.floorPlaneBean.segmentBeanList.get(0).length + roomBean.floorPlaneBean.segmentBeanList.get(1).length;
        pointBean.y = roomBean.height;
        pointBean.z = 0;
        pointModel = new PointModel(pointBean);
        planeModel.pointModelList.add(pointModel);

        // top left
        pointBean = new PointBean();
        pointBean.x = roomBean.floorPlaneBean.segmentBeanList.get(0).length;
        pointBean.y = roomBean.height;
        pointBean.z = 0;
        pointModel = new PointModel(pointBean);
        planeModel.pointModelList.add(pointModel);

        planeModel.drawPlane(anchorNode);
    }

    public void createWallModelSizeSymbol() {

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
