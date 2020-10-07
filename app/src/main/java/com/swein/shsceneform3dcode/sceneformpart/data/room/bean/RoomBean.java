package com.swein.shsceneform3dcode.sceneformpart.data.room.bean;

import com.google.ar.sceneform.math.Vector3;
import com.swein.shsceneform3dcode.framework.util.parsing.ParsingUtil;
import com.swein.shsceneform3dcode.sceneformpart.data.room.bean.basic.PlaneBean;
import com.swein.shsceneform3dcode.sceneformpart.data.room.bean.basic.PointBean;
import com.swein.shsceneform3dcode.sceneformpart.data.room.bean.basic.SegmentBean;
import com.swein.shsceneform3dcode.sceneformpart.tool.MathTool;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 모든 치수 다 m(미터) 단위로 저장합니다.
 */
public class RoomBean {

    private final static String TAG = "RoomBean";

    // normal vector of floor
    public Vector3 normalVectorOfPlane;

    // floor
    public PlaneBean floorPlaneBean;

    // ceiling
    public PlaneBean ceilingPlaneBean;

    // room wall
    public List<PlaneBean> wallList = new ArrayList<>();

    // object on the wall
    public List<PlaneBean> wallObjectList = new ArrayList<>();

    // room height
    public float height;

    // room floor fixed y
    public float floorFixedY;

    public float area; // 면적
    public float circumference; // 둘레
    public float wallArea; // 벽면적
    public float volume; // 체적

    public String name;
    public String unit;

    public PointBean centerPoint;

    public String thumbnailImage;

    public RoomBean() {

        normalVectorOfPlane = new Vector3();

        floorPlaneBean = new PlaneBean();
        ceilingPlaneBean = new PlaneBean();
        wallList.clear();
        wallObjectList.clear();

        height = 0;
        floorFixedY = 0;

        area = 0;
        circumference = 0;
        wallArea = 0;
        volume = 0;

        name = "";
        unit = "";
        thumbnailImage = "";
    }

    public JSONObject toJSONObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("normalVectorOfPlaneX", String.valueOf(normalVectorOfPlane.x));
        jsonObject.put("normalVectorOfPlaneY", String.valueOf(normalVectorOfPlane.y));
        jsonObject.put("normalVectorOfPlaneZ", String.valueOf(normalVectorOfPlane.z));

        jsonObject.put("floor", floorPlaneBean.toJSONObject());
        jsonObject.put("ceiling", ceilingPlaneBean.toJSONObject());

        JSONArray wallArray = new JSONArray();
        for(int i = 0; i < wallList.size(); i++) {
            wallArray.put(wallList.get(i).toJSONObject());
        }
        jsonObject.put("wallArray", wallArray);

        JSONArray wallObjectArray = new JSONArray();
        for(int i = 0; i < wallObjectList.size(); i++) {
            wallObjectArray.put(wallObjectList.get(i).toJSONObject());
        }
        jsonObject.put("wallObjectArray", wallObjectArray);

        jsonObject.put("height", String.valueOf(height));
        jsonObject.put("floorFixedY", String.valueOf(floorFixedY));
        jsonObject.put("area", String.valueOf(area));
        jsonObject.put("circumference", String.valueOf(circumference));
        jsonObject.put("wallArea", String.valueOf(wallArea));
        jsonObject.put("volume", String.valueOf(volume));

        jsonObject.put("name", name);
        jsonObject.put("unit", unit);
        jsonObject.put("thumbnailImage", thumbnailImage);

        return jsonObject;
    }

    public void init(JSONObject jsonObject) throws JSONException {

        float normalVectorOfPlaneX = Float.parseFloat(ParsingUtil.parsingString(jsonObject, "normalVectorOfPlaneX"));
        float normalVectorOfPlaneY = Float.parseFloat(ParsingUtil.parsingString(jsonObject, "normalVectorOfPlaneY"));
        float normalVectorOfPlaneZ = Float.parseFloat(ParsingUtil.parsingString(jsonObject, "normalVectorOfPlaneZ"));

        normalVectorOfPlane.set(new Vector3(normalVectorOfPlaneX, normalVectorOfPlaneY, normalVectorOfPlaneZ));

        JSONObject floorObject = ParsingUtil.parsingJSONObject(jsonObject, "floor");
        floorPlaneBean.init(floorObject);

        JSONObject ceilingObject = ParsingUtil.parsingJSONObject(jsonObject, "ceiling");
        ceilingPlaneBean.init(ceilingObject);

        JSONArray wallArray = ParsingUtil.parsingJSONArray(jsonObject, "wallArray");

        PlaneBean planeBean;
        for(int i = 0; i < wallArray.length(); i++) {
            planeBean = new PlaneBean();
            planeBean.init(wallArray.getJSONObject(i));
            wallList.add(planeBean);
        }

        JSONArray wallObjectArray = ParsingUtil.parsingJSONArray(jsonObject, "wallObjectArray");
        for(int i = 0; i < wallObjectArray.length(); i++) {
            planeBean = new PlaneBean();
            planeBean.init(wallObjectArray.getJSONObject(i));
            wallObjectList.add(planeBean);
        }

        height = Float.parseFloat(ParsingUtil.parsingString(jsonObject, "height"));
        floorFixedY = Float.parseFloat(ParsingUtil.parsingString(jsonObject, "floorFixedY"));

        area = Float.parseFloat(ParsingUtil.parsingString(jsonObject, "area"));
        circumference = Float.parseFloat(ParsingUtil.parsingString(jsonObject, "circumference"));
        wallArea = Float.parseFloat(ParsingUtil.parsingString(jsonObject, "wallArea"));
        volume = Float.parseFloat(ParsingUtil.parsingString(jsonObject, "volume"));

        name = ParsingUtil.parsingString(jsonObject, "name");
        unit = ParsingUtil.parsingString(jsonObject, "unit");
        thumbnailImage = ParsingUtil.parsingString(jsonObject, "thumbnailImage");
    }

    public void init(JSONObject jsonObject, float cx, float cy, float cz) throws JSONException {

        float normalVectorOfPlaneX = Float.parseFloat(ParsingUtil.parsingString(jsonObject, "normalVectorOfPlaneX"));
        float normalVectorOfPlaneY = Float.parseFloat(ParsingUtil.parsingString(jsonObject, "normalVectorOfPlaneY"));
        float normalVectorOfPlaneZ = Float.parseFloat(ParsingUtil.parsingString(jsonObject, "normalVectorOfPlaneZ"));

        normalVectorOfPlane.set(new Vector3(normalVectorOfPlaneX, normalVectorOfPlaneY, normalVectorOfPlaneZ));

        JSONObject floorObject = ParsingUtil.parsingJSONObject(jsonObject, "floor");
        floorPlaneBean.init(floorObject, cx, cy, cz);

        JSONObject ceilingObject = ParsingUtil.parsingJSONObject(jsonObject, "ceiling");
        ceilingPlaneBean.init(ceilingObject, cx, cy, cz);

        JSONArray wallArray = ParsingUtil.parsingJSONArray(jsonObject, "wallArray");

        PlaneBean planeBean;
        for(int i = 0; i < wallArray.length(); i++) {
            planeBean = new PlaneBean();
            planeBean.init(wallArray.getJSONObject(i), cx, cy, cz);
            wallList.add(planeBean);
        }

        JSONArray wallObjectArray = ParsingUtil.parsingJSONArray(jsonObject, "wallObjectArray");
        for(int i = 0; i < wallObjectArray.length(); i++) {
            planeBean = new PlaneBean();
            planeBean.init(wallObjectArray.getJSONObject(i), cx, cy, cz);
            wallObjectList.add(planeBean);
        }

        height = Float.parseFloat(ParsingUtil.parsingString(jsonObject, "height"));
        floorFixedY = Float.parseFloat(ParsingUtil.parsingString(jsonObject, "floorFixedY"));

        area = Float.parseFloat(ParsingUtil.parsingString(jsonObject, "area"));
        circumference = Float.parseFloat(ParsingUtil.parsingString(jsonObject, "circumference"));
        wallArea = Float.parseFloat(ParsingUtil.parsingString(jsonObject, "wallArea"));
        volume = Float.parseFloat(ParsingUtil.parsingString(jsonObject, "volume"));

        name = ParsingUtil.parsingString(jsonObject, "name");
        unit = ParsingUtil.parsingString(jsonObject, "unit");
        thumbnailImage = ParsingUtil.parsingString(jsonObject, "thumbnailImage");

    }

    public void calculate3DModelCenterPoint() {

        float tx = 0;
        float tz = 0;
        for(int i = 0; i < floorPlaneBean.pointList.size(); i++) {
            tx += floorPlaneBean.pointList.get(i).x;
            tz += floorPlaneBean.pointList.get(i).z;
        }

        centerPoint = new PointBean();
        centerPoint.x = tx / floorPlaneBean.pointList.size();
        centerPoint.y = height * 0.5f;
        centerPoint.z = tz / floorPlaneBean.pointList.size();
    }

    public void calculate2DModelCenterPoint() {

        float tx = 0;
        float tz = 0;
        for(int i = 0; i < floorPlaneBean.pointList.size(); i++) {
            tx += floorPlaneBean.pointList.get(i).x;
            tz += floorPlaneBean.pointList.get(i).z;
        }

        centerPoint = new PointBean();
        centerPoint.x = tx / floorPlaneBean.pointList.size();
        centerPoint.y = 0;
        centerPoint.z = tz / floorPlaneBean.pointList.size();
    }

    /**
     * 입면도
     *
     * this part need re-calculate all point
     * because all plane are in same surface
     */
    public RoomBean createWallModel() {

        RoomBean wallRoomBean = new RoomBean();

        wallRoomBean.height = height;

        // calculate floor
        List<PointBean> floorPointList = new ArrayList<>();

        PointBean pointBean = new PointBean();
        pointBean.x = floorPlaneBean.pointList.get(0).x;
        pointBean.y = floorPlaneBean.pointList.get(0).y;
        pointBean.z = 0;
        floorPointList.add(pointBean);

        float floorSegmentLength = pointBean.x;
        for(int i = 0; i < floorPlaneBean.segmentBeanList.size(); i++) {

            floorSegmentLength += floorPlaneBean.segmentBeanList.get(i).length;

            pointBean = new PointBean();
            pointBean.x = floorSegmentLength;
            pointBean.y = floorPlaneBean.pointList.get(i).y;
            pointBean.z = 0;
            floorPointList.add(pointBean);
        }

        wallRoomBean.floorPlaneBean.pointList.addAll(floorPointList);


        // calculate ceiling
        List<PointBean> ceilingPointList = new ArrayList<>();

        pointBean = new PointBean();
        pointBean.x = ceilingPlaneBean.pointList.get(0).x;
        pointBean.y = ceilingPlaneBean.pointList.get(0).y;
        pointBean.z = 0;
        ceilingPointList.add(pointBean);

        float ceilingSegmentLength = pointBean.x;
        for(int i = 0; i < ceilingPlaneBean.segmentBeanList.size(); i++) {

            ceilingSegmentLength += ceilingPlaneBean.segmentBeanList.get(i).length;

            pointBean = new PointBean();
            pointBean.x = ceilingSegmentLength;
            pointBean.y = ceilingPlaneBean.pointList.get(i).y;
            pointBean.z = 0;
            ceilingPointList.add(pointBean);
        }

        wallRoomBean.ceilingPlaneBean.pointList.addAll(ceilingPointList);


        // calculate segment length
        SegmentBean segmentBean;
        for(int i = 0; i < wallRoomBean.floorPlaneBean.pointList.size() - 1; i++) {
            segmentBean = new SegmentBean();
            segmentBean.startPointBean = wallRoomBean.floorPlaneBean.pointList.get(i);
            segmentBean.endPointBean = wallRoomBean.floorPlaneBean.pointList.get(i + 1);
            segmentBean.length = floorPlaneBean.segmentBeanList.get(i).length;
            wallRoomBean.floorPlaneBean.segmentBeanList.add(segmentBean);
        }

        for(int i = 0; i < wallRoomBean.ceilingPlaneBean.pointList.size() - 1; i++) {
            segmentBean = new SegmentBean();
            segmentBean.startPointBean = wallRoomBean.ceilingPlaneBean.pointList.get(i);
            segmentBean.endPointBean = wallRoomBean.ceilingPlaneBean.pointList.get(i + 1);
            segmentBean.length = ceilingPlaneBean.segmentBeanList.get(i).length;
            wallRoomBean.ceilingPlaneBean.segmentBeanList.add(segmentBean);
        }

        // calculate wall object

        List<PlaneBean> wallObjectPlaneBeanList = new ArrayList<>();

        PlaneBean wallObjectPlaneBean;
        for(int i = 0; i < wallObjectList.size(); i++) {

            PlaneBean wallObject = wallObjectList.get(i);
            PlaneBean wall = wallList.get(wallObject.objectOnIndex);

            wallObjectPlaneBean = new PlaneBean();
            wallObjectPlaneBean.objectOnIndex = wallObject.objectOnIndex;

            List<PointBean> wallObjectPointList = new ArrayList<>();
            PointBean objectPointBean;
            for(int j = 0; j < wallObject.pointList.size(); j++) {

                // get the index of wall object's object of reference point
                // we need get index that left bottom point of plane
                // because this part is draw start from left bottom point
//                int leftBottomPointIndex = getIndexOfLeftBottomPointOnThePlane(wall.pointList);
                int leftBottomPointIndex = 0;

                float distanceOfTwoPointByXZ = MathTool.getLengthOfTwoNode2D(
                        wallObject.pointList.get(j).x, wallObject.pointList.get(j).z,
                        wall.pointList.get(leftBottomPointIndex).x, wall.pointList.get(leftBottomPointIndex).z
                );

//                ILog.iLogDebug(TAG, distanceOfTwoPointByXZ);

                float distanceOfTwoPointByY = wallObject.pointList.get(j).y - wall.pointList.get(leftBottomPointIndex).y;

                objectPointBean = new PointBean();
                objectPointBean.x = distanceOfTwoPointByXZ;
                objectPointBean.y = distanceOfTwoPointByY;
                objectPointBean.z = 0;
                wallObjectPointList.add(objectPointBean);
            }

            wallObjectPlaneBean.pointList.addAll(wallObjectPointList);
            wallObjectPlaneBeanList.add(wallObjectPlaneBean);
        }

        wallRoomBean.wallObjectList.addAll(wallObjectPlaneBeanList);

        return wallRoomBean;
    }

    public void calculateWallCenterPoint() {

        float length = 0;
        for(int i = 0; i < floorPlaneBean.segmentBeanList.size(); i++) {
            length += floorPlaneBean.segmentBeanList.get(i).length;
        }

        centerPoint = new PointBean();
        centerPoint.x = length * 0.5f;
        centerPoint.y = height * 0.5f;
        centerPoint.z = 0;
    }

    private int getIndexOfLeftBottomPointOnThePlane(List<PointBean> list) {
        PointBean tempPointBean = new PointBean();

        float x = 0;
        float y = 0;
        float z = 0;
        for(int i = 0; i < list.size(); i++) {
            if(x >= list.get(i).x) {
                x = list.get(i).x;
            }
            if(y >= list.get(i).y) {
                y = list.get(i).y;
            }
            if(z >= list.get(i).z) {
                z = list.get(i).z;
            }
        }

        tempPointBean.x = x;
        tempPointBean.y = y;
        tempPointBean.z = z;


        int index = 0;
        for(int i = 0; i < list.size(); i++) {
            if(tempPointBean.x == list.get(i).x && tempPointBean.y == list.get(i).y && tempPointBean.z == list.get(i).z) {
                index = i;
                break;
            }
        }

        return index;
    }

    public void clear() {

        for(int i = 0; i < wallObjectList.size(); i++) {
            wallObjectList.get(i).clear();
        }
        wallObjectList.clear();


        for(int i = 0; i < wallList.size(); i++) {
            wallList.get(i).clear();
        }
        wallList.clear();


        if(ceilingPlaneBean != null) {
            ceilingPlaneBean.clear();
            ceilingPlaneBean = null;
        }

        if(floorPlaneBean != null) {
            floorPlaneBean.clear();
            floorPlaneBean = null;
        }

        normalVectorOfPlane = null;

        height = 0;
        floorFixedY = 0;

        area = 0;
        circumference = 0;
        wallArea = 0;
        volume = 0;

        name = null;
        unit = null;
        thumbnailImage = null;
    }
}
