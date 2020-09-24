package com.swein.shsceneform3dcode.sceneformpart.bean.basic;

import com.google.ar.sceneform.Node;
import com.swein.shsceneform3dcode.framework.parsing.ParsingUtil;
import com.swein.shsceneform3dcode.sceneformpart.material.SFMaterial;
import com.swein.shsceneform3dcode.sceneformpart.tool.SFTool;

import org.json.JSONException;
import org.json.JSONObject;

public class PointBean {

    public Node point;

    public PointBean() {
        point = new Node();
    }

    public void clear() {
        if(point != null) {
            point.setParent(null);
            point = null;
        }
    }

    public JSONObject toJSONObject() throws JSONException {

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("x", String.valueOf(point.getLocalPosition().x));
        jsonObject.put("y", String.valueOf(point.getLocalPosition().y));
        jsonObject.put("z", String.valueOf(point.getLocalPosition().z));

        return jsonObject;
    }

    public void init(JSONObject jsonObject) throws JSONException {

        String x = ParsingUtil.parsingString(jsonObject, "x");
        String y = ParsingUtil.parsingString(jsonObject, "y");
        String z = ParsingUtil.parsingString(jsonObject, "z");

        float xf = Float.parseFloat(x);
        float yf = Float.parseFloat(y);
        float zf = Float.parseFloat(z);

        point = SFTool.createLocalNode(xf, yf, zf, 0.01f, SFMaterial.instance.pointMaterial, false);
    }

    public void init(JSONObject jsonObject, float cx, float cy, float cz) throws JSONException {

        String x = ParsingUtil.parsingString(jsonObject, "x");
        String y = ParsingUtil.parsingString(jsonObject, "y");
        String z = ParsingUtil.parsingString(jsonObject, "z");

        float xf = Float.parseFloat(x) - cx;
        float yf = Float.parseFloat(y) - cy;
        float zf = Float.parseFloat(z) - cz;

        point = SFTool.createLocalNode(xf, yf, zf, 0.01f, SFMaterial.instance.pointMaterial, false);
    }

}
