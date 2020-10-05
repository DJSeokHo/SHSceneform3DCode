package com.swein.shsceneform3dcode.sceneformpart.data.room.bean.basic;

import org.json.JSONException;
import org.json.JSONObject;

public class SegmentBean {

    public PointBean startPointBean;
    public PointBean endPointBean;

    public float length;
    
    public SegmentBean() {
        startPointBean = new PointBean();
        endPointBean = new PointBean();
        length = 0;
    }

    public JSONObject toJSONObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("startPoint", startPointBean.toJSONObject());
        jsonObject.put("endPoint", endPointBean.toJSONObject());
        jsonObject.put("length", String.valueOf(length));

        return jsonObject;
    }

    public void init(JSONObject jsonObject) throws JSONException {

        startPointBean.init(jsonObject.getJSONObject("startPoint"));
        endPointBean.init(jsonObject.getJSONObject("endPoint"));
        length = Float.parseFloat(jsonObject.getString("length"));

    }

    public void init(JSONObject jsonObject, float cx, float cy, float cz) throws JSONException {

        startPointBean.init(jsonObject.getJSONObject("startPoint"), cx, cy, cz);
        endPointBean.init(jsonObject.getJSONObject("endPoint"), cx, cy, cz);
        length = Float.parseFloat(jsonObject.getString("length"));

    }

    public void clear() {

        if(startPointBean != null) {
            startPointBean.clear();
            startPointBean = null;
        }

        if(endPointBean != null) {
            endPointBean.clear();
            endPointBean = null;
        }
    }

}
