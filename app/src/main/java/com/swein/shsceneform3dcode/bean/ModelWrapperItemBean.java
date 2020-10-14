package com.swein.shsceneform3dcode.bean;

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
            roomBean.thumbnailImage = imgUrl;
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}
