package com.magic.vulcan.netproject.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

public class UtilsJson {

    private final static String tag = UtilsJson.class.getSimpleName();

    public static String createJsonString(Object obj) {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(obj);
        Log.d("", "Jsonstr=" + jsonStr);
        return jsonStr;
    }

    public static <T> T getBean(String jsonStr, Class<T> cla) {
        Gson gson = new Gson();
        T obj = gson.fromJson(jsonStr, cla);
        return obj;
    }

    public static <T> List<T> getBeans(String jsonStr, Class<T> cls) {
        List<T> list = new ArrayList<T>();
        try {
            Gson gson = new Gson();

            JsonParser jsonParser = new JsonParser();
            JsonElement je = jsonParser.parse(jsonStr);
            JsonArray jsonArray;
            if (je.isJsonObject()) {
                JsonObject jo = (JsonObject) je;
                JsonElement element = jo.get("data");
                if (element.isJsonArray()) {
                    jsonArray = (JsonArray) element;
                    for (JsonElement e : jsonArray) {
                        if (!e.isJsonNull() && e.isJsonObject()) {
                            T obj = gson.fromJson(e, cls);
                            list.add(obj);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static Map<String, Object> getBeanMap(String jsonStr) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Gson gson = new Gson();
            map = gson.fromJson(jsonStr, new TypeToken<Map<String, Object>>() {

            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

}
