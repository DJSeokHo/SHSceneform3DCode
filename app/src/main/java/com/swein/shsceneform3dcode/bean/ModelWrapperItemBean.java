package com.swein.shsceneform3dcode.bean;

import android.os.Bundle;

import com.swein.shsceneform3dcode.framework.util.parsing.ParsingUtil;
import com.swein.shsceneform3dcode.sceneformpart.data.room.bean.RoomBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class ModelWrapperItemBean {

    public long id = 0;
    public long userId = 0;
    public String name = "";
    public String imgUrl = "";
    public String jsonObj = "";
    public int isDelete = 0;
    public String createdAt = "";
    public long createdBy = 0;
    public String updatedAt = "";
    public long updatedBy = 0;

    public RoomBean roomBean = null;

    public void initWithJSONObject(JSONObject jsonObject) throws JSONException {
        id = ParsingUtil.parsingLong(jsonObject, "id");
        userId = ParsingUtil.parsingLong(jsonObject, "userId");
        name = ParsingUtil.parsingString(jsonObject, "name");
        imgUrl = ParsingUtil.parsingString(jsonObject, "imgUrl");
        jsonObj = ParsingUtil.parsingString(jsonObject, "jsonObj");
        isDelete = ParsingUtil.parsingInt(jsonObject, "isDelete");
        createdAt = ParsingUtil.parsingString(jsonObject, "createdAt");
        createdBy = ParsingUtil.parsingLong(jsonObject, "createdBy");
        updatedAt = ParsingUtil.parsingString(jsonObject, "updatedAt");
        updatedBy = ParsingUtil.parsingLong(jsonObject, "updatedBy");

        if(jsonObj.equals("")) {
            return;
        }

        try {
            jsonObj = URLDecoder.decode(jsonObj, "UTF-8");
            roomBean = new RoomBean();
            roomBean.init(new JSONObject(jsonObj));
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void initWithBundle(Bundle bundle) {
        id = bundle.getLong("id", 0);
        userId = bundle.getLong("userId", 0);
        name = bundle.getString("name", "");
        imgUrl = bundle.getString("imgUrl", "");
        jsonObj = bundle.getString("jsonObj", "");
        isDelete = bundle.getInt("isDelete", 0);
        createdAt = bundle.getString("createdAt", "");
        createdBy = bundle.getLong("createdBy", 0);
        updatedAt = bundle.getString("updatedAt", "");
        updatedBy = bundle.getLong("updatedBy", 0);

        if(jsonObj.equals("")) {
            return;
        }

        try {
            JSONObject room = new JSONObject(jsonObj);
            roomBean = new RoomBean();
            roomBean.init(room);
        }
        catch (Exception e) {
            e.printStackTrace();
            roomBean = null;
        }
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putLong("id", id);
        bundle.putLong("userId", userId);
        bundle.putString("name", name);
        bundle.putString("imgUrl", imgUrl);
        bundle.putString("jsonObj", jsonObj);
        bundle.putInt("isDelete", isDelete);
        bundle.putString("createdAt", createdAt);
        bundle.putLong("createdBy", createdBy);
        bundle.putString("updatedAt", updatedAt);
        bundle.putLong("updatedBy", updatedBy);

        return bundle;
    }
}
