package com.swein.shsceneform3dcode.framework.parsing;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ParsingUtil {

    public interface JSONObjectIterator {
        void onIterator(Object key, Object value) throws JSONException;
    }

    public interface JSONObjectIteratorWithIndex {
        void onIterator(int index, Object key, Object value);
    }

    public static void jsonObjectIterator(JSONObject jsonObject, JSONObjectIterator jsonObjectIterator) throws JSONException {

        Iterator iterator = jsonObject.keys();
        while (iterator.hasNext()) {

            String key = (String) iterator.next();
            Object value = jsonObject.get(key);
            jsonObjectIterator.onIterator(key, value);
        }
    }

    public static void jsonObjectIteratorWithIndex(JSONObject jsonObject, JSONObjectIteratorWithIndex jsonObjectIteratorWithIndex) throws JSONException {

        int index = 0;

        Iterator iterator = jsonObject.keys();
        while (iterator.hasNext()) {

            String key = (String) iterator.next();
            Object value = jsonObject.get(key);
            jsonObjectIteratorWithIndex.onIterator(index, key, value);
            index++;
        }
    }

    public static List<String> parsingStringList(JSONObject jsonObject, String key) throws JSONException {
        if(!jsonObject.has(key)) {
            return new ArrayList<>();
        }

        if(jsonObject.isNull(key)) {
            return new ArrayList<>();
        }
        else {
            List<String> list = new ArrayList<>();
            JSONArray jsonArray = jsonObject.getJSONArray(key);
            for(int i = 0; i < jsonArray.length(); i++) {
                list.add(jsonArray.getString(i));
            }

            return list;
        }

    }

    public static String parsingString(JSONObject jsonObject, String key) throws JSONException {

        if(!jsonObject.has(key)) {
            return "";
        }

        if(jsonObject.isNull(key)) {
            return "";
        }
        else {
            String result = jsonObject.getString(key);

            if(result == null) {
                return "";
            }
            else {
                return result;
            }
        }
    }

    public static boolean parsingBoolean(JSONObject jsonObject, String key) throws JSONException {

        if(!jsonObject.has(key)) {
            return false;
        }

        if(jsonObject.isNull(key)) {
            return false;
        }
        else {
            return jsonObject.getBoolean(key);
        }
    }

    public static int parsingInt(JSONObject jsonObject, String key) throws JSONException {

        if(!jsonObject.has(key)) {
            return 0;
        }

        if(jsonObject.isNull(key)) {
            return 0;
        }
        else {

            return jsonObject.getInt(key);
        }
    }

    public static double parsingDouble(JSONObject jsonObject, String key) throws JSONException {

        if(!jsonObject.has(key)) {
            return 0;
        }

        if(jsonObject.isNull(key)) {
            return 0;
        }
        else {

            return jsonObject.getDouble(key);
        }
    }

    public static JSONObject parsingJSONObject(JSONObject jsonObject, String key) throws JSONException {

        if(!jsonObject.has(key)) {
            return new JSONObject();
        }

        if(jsonObject.isNull(key)) {
            return new JSONObject();
        }
        else {
            return jsonObject.getJSONObject(key);
        }
    }

    public static JSONArray parsingJSONArray(JSONObject jsonObject, String key) throws JSONException {

        if(!jsonObject.has(key)) {
            return new JSONArray();
        }

        if(jsonObject.isNull(key)) {
            return new JSONArray();
        }
        else {
            return jsonObject.getJSONArray(key);
        }
    }

}
