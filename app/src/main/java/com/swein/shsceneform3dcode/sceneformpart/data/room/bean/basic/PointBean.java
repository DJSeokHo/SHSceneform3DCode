package com.swein.shsceneform3dcode.sceneformpart.data.room.bean.basic;

import com.swein.shsceneform3dcode.framework.parsing.ParsingUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class PointBean {

    public float x;
    public float y;
    public float z;

    public JSONObject toJSONObject() throws JSONException {

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("x", String.valueOf(x));
        jsonObject.put("y", String.valueOf(y));
        jsonObject.put("z", String.valueOf(z));

        return jsonObject;
    }

    public void init(JSONObject jsonObject) throws JSONException {

        String x = ParsingUtil.parsingString(jsonObject, "x");
        String y = ParsingUtil.parsingString(jsonObject, "y");
        String z = ParsingUtil.parsingString(jsonObject, "z");

        this.x = Float.parseFloat(x);
        this.y = Float.parseFloat(y);
        this.z = Float.parseFloat(z);
    }

    public void init(JSONObject jsonObject, float cx, float cy, float cz) throws JSONException {

        String x = ParsingUtil.parsingString(jsonObject, "x");
        String y = ParsingUtil.parsingString(jsonObject, "y");
        String z = ParsingUtil.parsingString(jsonObject, "z");

        this.x = Float.parseFloat(x) - cx;
        this.y = Float.parseFloat(y) - cy;
        this.z = Float.parseFloat(z) - cz;
    }

    public void clear() {
        x = 0;
        y = 0;
        z = 0;
    }
}
