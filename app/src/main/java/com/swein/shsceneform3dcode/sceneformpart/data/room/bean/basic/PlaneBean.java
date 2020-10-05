package com.swein.shsceneform3dcode.sceneformpart.data.room.bean.basic;

import com.swein.shsceneform3dcode.framework.parsing.ParsingUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PlaneBean {

    public List<PointBean> pointList = new ArrayList<>();
    public List<SegmentBean> segmentBeanList = new ArrayList<>();

    public String type;
    public int objectOnIndex;

    public PlaneBean() {
        pointList.clear();
        segmentBeanList.clear();
        type = "";
        objectOnIndex = -1;
    }

    public void clear() {

        for(int i = 0; i < pointList.size() - 1; i++) {
            pointList.get(i).clear();
        }
        pointList.clear();

        for(int i = 0; i < segmentBeanList.size() - 1; i++) {
            segmentBeanList.get(i).clear();
        }
        segmentBeanList.clear();

        type = "";
        objectOnIndex = -1;
    }

    public JSONObject toJSONObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();

        JSONArray pointArray = new JSONArray();
        for(int i = 0; i < pointList.size(); i++) {
            pointArray.put(pointList.get(i).toJSONObject());
        }
        jsonObject.put("pointArray", pointArray);

        JSONArray segmentArray = new JSONArray();
        for(int i = 0; i < segmentBeanList.size(); i++) {
            segmentArray.put(segmentBeanList.get(i).toJSONObject());
        }
        jsonObject.put("segmentArray", segmentArray);

        jsonObject.put("type", type);
        jsonObject.put("objectOnIndex", objectOnIndex);
        return jsonObject;
    }

    public void init(JSONObject jsonObject) throws JSONException {

        JSONArray pointArray = ParsingUtil.parsingJSONArray(jsonObject, "pointArray");
        JSONArray segmentArray = ParsingUtil.parsingJSONArray(jsonObject, "segmentArray");

        PointBean pointBean;
        for(int i = 0; i < pointArray.length(); i++) {
            pointBean = new PointBean();
            pointBean.init(pointArray.getJSONObject(i));
            pointList.add(pointBean);
        }

        SegmentBean segmentBean;
        for(int i = 0; i < segmentArray.length(); i++) {
            segmentBean = new SegmentBean();
            segmentBean.init(segmentArray.getJSONObject(i));
            segmentBeanList.add(segmentBean);
        }

        type = jsonObject.getString("type");
        objectOnIndex = jsonObject.getInt("objectOnIndex");
    }

    public void init(JSONObject jsonObject, float cx, float cy, float cz) throws JSONException {

        JSONArray pointArray = ParsingUtil.parsingJSONArray(jsonObject, "pointArray");
        JSONArray segmentArray = ParsingUtil.parsingJSONArray(jsonObject, "segmentArray");

        PointBean pointBean;
        for(int i = 0; i < pointArray.length(); i++) {
            pointBean = new PointBean();
            pointBean.init(pointArray.getJSONObject(i), cx, cy, cz);
            pointList.add(pointBean);
        }

        SegmentBean segmentBean;
        for(int i = 0; i < segmentArray.length(); i++) {
            segmentBean = new SegmentBean();
            segmentBean.init(segmentArray.getJSONObject(i), cx, cy, cz);
            segmentBeanList.add(segmentBean);
        }

        type = jsonObject.getString("type");
        objectOnIndex = jsonObject.getInt("objectOnIndex");
    }
}
