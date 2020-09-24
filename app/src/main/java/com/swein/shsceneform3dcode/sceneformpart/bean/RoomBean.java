package com.swein.shsceneform3dcode.sceneformpart.bean;

import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;
import com.swein.shsceneform3dcode.framework.parsing.ParsingUtil;
import com.swein.shsceneform3dcode.sceneformpart.bean.basic.PlaneBean;
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
    public PlaneBean floor;

    // ceiling
    public PlaneBean ceiling;

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

    public RoomBean() {

        normalVectorOfPlane = new Vector3();

        floor = new PlaneBean();
        ceiling = new PlaneBean();
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
    }

    public void calculate() {

        // 둘레 m
        circumference = 0;
        for(int i = 0; i < floor.segmentList.size(); i++) {
            circumference += floor.segmentList.get(i).length;
        }

        // 벽 면적 m2
        wallArea = 0;
        for(int i = 0; i < floor.segmentList.size(); i++) {
//            wallArea += MathTool.getLengthByUnit(ARConstants.arUnit, floor.segmentList.get(i).length) * MathTool.getLengthByUnit(ARConstants.arUnit, height);
            wallArea += floor.segmentList.get(i).length * height;
        }

        // 면적 m2
        List<Node> list = new ArrayList<>();
        for(int i = 0; i < floor.pointList.size(); i++) {
            list.add(floor.pointList.get(i).point);
        }
//        area = MathTool.getAreaByUnit(ARConstants.arUnit, MathTool.calculateArea(list, normalVectorOfPlane));
        area = MathTool.calculateArea(list, normalVectorOfPlane);

        // 체적 m3
//        volume = MathTool.getLengthByUnit(ARConstants.arUnit, height) * area;
        volume = height * area;
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


        if(ceiling != null) {
            ceiling.clear();
            ceiling = null;
        }

        if(floor != null) {
            floor.clear();
            floor = null;
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
    }

    public JSONObject toJSONObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("normalVectorOfPlaneX", String.valueOf(normalVectorOfPlane.x));
        jsonObject.put("normalVectorOfPlaneY", String.valueOf(normalVectorOfPlane.y));
        jsonObject.put("normalVectorOfPlaneZ", String.valueOf(normalVectorOfPlane.z));

        jsonObject.put("floor", floor.toJSONObject());
        jsonObject.put("ceiling", ceiling.toJSONObject());

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

        return jsonObject;
    }

    public void init(JSONObject jsonObject) throws JSONException {

        float normalVectorOfPlaneX = Float.parseFloat(ParsingUtil.parsingString(jsonObject, "normalVectorOfPlaneX"));
        float normalVectorOfPlaneY = Float.parseFloat(ParsingUtil.parsingString(jsonObject, "normalVectorOfPlaneY"));
        float normalVectorOfPlaneZ = Float.parseFloat(ParsingUtil.parsingString(jsonObject, "normalVectorOfPlaneZ"));

        normalVectorOfPlane.set(new Vector3(normalVectorOfPlaneX, normalVectorOfPlaneY, normalVectorOfPlaneZ));

        JSONObject floorObject = ParsingUtil.parsingJSONObject(jsonObject, "floor");
        floor.init(floorObject);

        JSONObject ceilingObject = ParsingUtil.parsingJSONObject(jsonObject, "ceiling");
        ceiling.init(ceilingObject);

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
    }

    public void init(JSONObject jsonObject, float cx, float cy, float cz) throws JSONException {

        float normalVectorOfPlaneX = Float.parseFloat(ParsingUtil.parsingString(jsonObject, "normalVectorOfPlaneX"));
        float normalVectorOfPlaneY = Float.parseFloat(ParsingUtil.parsingString(jsonObject, "normalVectorOfPlaneY"));
        float normalVectorOfPlaneZ = Float.parseFloat(ParsingUtil.parsingString(jsonObject, "normalVectorOfPlaneZ"));

        normalVectorOfPlane.set(new Vector3(normalVectorOfPlaneX, normalVectorOfPlaneY, normalVectorOfPlaneZ));

        JSONObject floorObject = ParsingUtil.parsingJSONObject(jsonObject, "floor");
        floor.init(floorObject, cx, cy, cz);

        JSONObject ceilingObject = ParsingUtil.parsingJSONObject(jsonObject, "ceiling");
        ceiling.init(ceilingObject, cx, cy, cz);

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

    }

}
