package com.swein.shsceneform3dcode.constants;

import com.swein.shsceneform3dcode.framework.util.parsing.ParsingUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WebConstants {

    public final static String DOMAIN = "http://13.124.112.44";

    public static boolean getIsSuccess(String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        return ParsingUtil.parsingBoolean(jsonObject, "success");
    }

    public static int getCode(String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        return ParsingUtil.parsingInt(jsonObject, "code");
    }

    public static String getMessage(String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        return ParsingUtil.parsingString(jsonObject, "msg");
    }

    public static JSONObject getData(String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        return ParsingUtil.parsingJSONObject(jsonObject, "data");
    }

    public static JSONArray getList(String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        return ParsingUtil.parsingJSONArray(jsonObject, "list");
    }


    public static String getUploadModelUrl() {
        return String.format("%s/v1/model/new", DOMAIN);
    }

    public static String getUploadModelImageUrl(String modelId) {
        return String.format("%s/v1/model/imgupload?modelId=%s", DOMAIN, modelId);
    }

    public static String getSearchModelUrl(String keyWord, String offset, String limitNo) {
        return String.format("%s/v1/search/model?keyWord=%s&offset=%s&limitNo=%s", DOMAIN, keyWord, offset, limitNo);
    }

    public static String getDeleteModelUrl(String modelId) {
        return String.format("%s/v1/model/delete?modelId=%s", DOMAIN, modelId);
    }
}
